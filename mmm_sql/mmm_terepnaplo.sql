/*Miután QGIS-ben a metszések segítségével előállt a megfigyeléses pontréteg, az alábbi lekérdezéseket kell futtani. Jó volna az ez előttieket is SQL-ben megcsinálni, hogy egyben le lehessen az egészet futtatni.*/

/*Ha már létezik, el el kell dobni a létrehozandó tábláinkat*/
DROP TABLE terepnaplo_adattabla;
DROP TABLE summarized_data;

/*Megfigyelések összesítése fajbúbos pacsirtaonként a pontokon az alapján, hogy melyik körgyűrűbe esnek.*/
CREATE TABLE terepnaplo_adattabla AS
 SELECT megfigyelesek_2018_part.fid AS fid,
   megfigyelesek_2018_part.utm_name AS utm_name,
   megfigyelesek_2018_part.observer AS observer,
   megfigyelesek_2018_part.observer_id AS observer_id,
   megfigyelesek_2018_part.observation_date AS observation_date,
   megfigyelesek_2018_part.pointnum AS pointnum,
   megfigyelesek_2018_part.count_start AS count_start,
   megfigyelesek_2018_part.wind AS wind,
   megfigyelesek_2018_part.species AS species,
   megfigyelesek_2018_part.species_huring AS species_huring,
   megfigyelesek_2018_part.location AS location,
   sum(megfigyelesek_2018_part.individuals) AS individuals
   FROM megfigyelesek_2018_part
   GROUP BY
     megfigyelesek_2018_part.utm_name,
     megfigyelesek_2018_part.observation_date,
     megfigyelesek_2018_part.pointnum,
     megfigyelesek_2018_part.species,
     megfigyelesek_2018_part.location
   ORDER BY
     observation_date,
     utm_name,
     pointnum,
     species;

/*Oszlopok létrehozása az egyes körgyűrűknek*/
ALTER TABLE terepnaplo_adattabla
  ADD COLUMN "ring200" INT;
ALTER TABLE terepnaplo_adattabla
  ADD COLUMN "atrepult" INT;
ALTER TABLE terepnaplo_adattabla
  ADD COLUMN "circle50" INT;
ALTER TABLE terepnaplo_adattabla
  ADD COLUMN "ring100" INT;

/*Körgyűrűnként az egyedszámok feltöltése*/
UPDATE terepnaplo_adattabla
  SET "ring200" = "individuals"
    WHERE "location" = '200_ring';
UPDATE terepnaplo_adattabla
  SET "atrepult" = "individuals"
    WHERE "location" = 'atrepult';
UPDATE terepnaplo_adattabla
  SET "circle50" = "individuals"
    WHERE "location" = '50_circle';
UPDATE terepnaplo_adattabla
  SET "ring100" = "individuals"
    WHERE "location" = '100_ring';

/*Fajonként és pontonként egy sor előállítása*/

CREATE TABLE summarized_data AS
  SELECT terepnaplo_adattabla.fid AS fid,
    terepnaplo_adattabla.utm_name AS utm_name,
    terepnaplo_adattabla.observer_id AS observer_id,
    terepnaplo_adattabla.observation_date AS observation_date,
    terepnaplo_adattabla.pointnum AS pointnum,
    terepnaplo_adattabla.count_start AS count_start,
    terepnaplo_adattabla.wind AS wind,
    terepnaplo_adattabla.species AS species,
    terepnaplo_adattabla.species_huring AS species_huring,
    sum(terepnaplo_adattabla.ring200) AS ring200,
    sum(terepnaplo_adattabla.atrepult) AS atrepult,
    sum(terepnaplo_adattabla.circle50) AS circle50,
    sum(terepnaplo_adattabla.ring100) AS ring100
    FROM terepnaplo_adattabla
    GROUP BY
      terepnaplo_adattabla.utm_name,
      terepnaplo_adattabla.observation_date,
      terepnaplo_adattabla.pointnum,
      terepnaplo_adattabla.species
    ORDER BY
     observation_date,
     utm_name,
     pointnum,
     species;

/*HURING kódok beírása*/

UPDATE summarized_data
  SET species_huring = (SELECT fajlista.HURING FROM fajlista WHERE summarized_data.species = fajlista.SCI)
  WHERE species IN (SELECT SCI FROM fajlista WHERE fajlista.SCI = summarized_data.species);

/*Ha magyar név szerint kell. Kisbetűs magyar neveknek kell lenni a fajlista táblában!!!*/

UPDATE summarized_data
  SET species_huring = (SELECT fajlista.HURING FROM fajlista WHERE summarized_data.species = fajlista.HUN)
  WHERE species IN (SELECT HUN FROM fajlista WHERE fajlista.HUN = summarized_data.species);
