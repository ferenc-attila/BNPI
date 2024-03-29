/*
QGIS műveletek:

1. Megfigyelések tábla szükséges mezőinek újtraformázása (Feldolgozás eszköztár/Vektor:attribútumtábla):
  datum -> observation_date
  mmm_szamlalas -> counting
  adatkozlo -> observer
  faj -> species
  egyed -> individuals
  aktivitas -> activity

2. Térbeli adatkapcsolás (Vektor/Adatkezelő eszközök/Attribútumok összekapcsolása hely alapján):
  ujrakonfiguralt (metszik, benne vannak) mmm_200 (csak a pont és kvad mezők) egy-egy kapcsolat
  kvad és pont mezők átnevezése (rendre utm_name, pointnum)

3. Térbeli adatkapcsolás számlálásonként külön:
    Összekapcsolt réteg (1. számlálás) - mmm_200 (csak az ido1, szel1 mezők) egy-egy kapcsolat
    Összekapcsolt réteg (2. számlálás) - mmm_200 (csak az ido2, szel2 mezők) egy-egy kapcsolat

4. Az összekapcsolt rétegeken a idő és szél mezők átnevezése egységesen (rendre count_start, wind),
  majd a két számlálás rétegeinek összefűzése.
  location (TEXT, 10) mező létrehozása.

5. 50-es kör, 100-as és 200-as körgyűrű rétegek létrehozása

6. Szelekció hely alapján az egyes rétegekre, szelektált elemek location mezőjének feltöltése a megfelelő értékekkel (200_ring, 100_ring, 50_circle).
  Átrepülő egyedek location mezőjének feltöltése atrepult értékkel.

7. Eredmény réteg mentése formatted_observations táblaként.

8. Új mezők beszúrása: observer_id (INTEGER), species_huring (STRING, 6).
  Felesleges mezők törlése: counting, activity

Miután QGIS-ben a metszések segítségével előállt a megfigyeléses pontréteg, az alábbi lekérdezéseket kell futtani. Jó volna az ez előttieket is SQL-ben megcsinálni, hogy egyben le lehessen az egészet futtatni.*/

/*Megfigyelések réteg létrehozása

DROP TABLE formatted_observations;

CREATE TABLE formatted_observations (
  row_id INTEGER PRIMARY KEY AUTOINCREMENT,
  utm_name TEXT,
  observer TEXT,
  observer_id INTEGER,
  observation_date TEXT,
  pointnum INTEGER,
  count_start TEXT,
  wind INTEGER,
  species TEXT,
  species_huring TEXT,
  location TEXT,
  counting INTEGER,
  individuals INTEGER
);

Geometria mező hozzáadása és térbeli index létrehozása
SELECT AddGeometryColumn('formatted_observations', 'geometry', 23700, 'POINT', 'XY', 1);
SELECT CreateSpatialIndex('formatted_observations', 'geometry');

Rekordok átvétele az eredeti megfigyelések táblából
Valami PITTY-PUTTY van a geometria oszlop átvitelével!!!

INSERT INTO formatted_observations (observer, observation_date, species, counting, individuals, geometry)
SELECT adatkozlo, datum, faj, mmm_szamlalas, egyed, ST_GeomFromWKB(GEOMETRY) FROM megfigyelesek;
*/

/*Megfigyelő kódokat tartalmazó tábla*/
CREATE TABLE observers (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	observer_name TEXT(100),
	observer_id INTEGER
);

INSERT INTO observers (observer_name,observer_id) VALUES
	 ('Juhász Benedek',2590),
	 ('Széles Tamás',1764),
	 ('Tóth László',940),
	 ('Ferenc Attila',614),
   ('Sasvári János',460);

 /* Megfigyelő kódok beírása*/
UPDATE formatted_observations 
  SET observer_id = (SELECT observers.observer_id FROM observers WHERE formatted_observations.observer = observers.observer_name)
  WHERE observer IN (SELECT observer_name FROM observers WHERE observers.observer_name = formatted_observations.observer);

/*Ha már létezik, el el kell dobni a létrehozandó tábláinkat*/
DROP TABLE terepnaplo_adattabla;
DROP TABLE summarized_data;

/*Megfigyelések összesítése fajonként a pontokon az alapján, hogy melyik körgyűrűbe esnek.*/
CREATE TABLE terepnaplo_adattabla AS
 SELECT formatted_observations.ogc_fid AS fid,
   formatted_observations.utm_name AS utm_name,
   formatted_observations.observer AS observer,
   formatted_observations.observer_id AS observer_id,
   formatted_observations.observation_date AS observation_date,
   formatted_observations.pointnum AS pointnum,
   formatted_observations.count_start AS count_start,
   formatted_observations.wind AS wind,
   formatted_observations.species AS species,
   formatted_observations.species_huring AS species_huring,
   formatted_observations.location AS location,
   sum(formatted_observations.individuals) AS individuals
   FROM formatted_observations
   GROUP BY
     formatted_observations.utm_name,
     formatted_observations.observation_date,
     formatted_observations.pointnum,
     formatted_observations.species,
     formatted_observations.location
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
  SET species_huring = (SELECT fajlista.HURING FROM fajlista WHERE summarized_data.species = fajlista.sci)
  WHERE species IN (SELECT sci FROM fajlista WHERE fajlista.sci = summarized_data.species);

/*Ha magyar név szerint kell. Kisbetűs magyar neveknek kell lenni a fajlista táblában!!!*/

UPDATE summarized_data
  SET species_huring = (SELECT fajlista.HURING FROM fajlista WHERE summarized_data.species = fajlista.hun)
  WHERE species IN (SELECT hun FROM fajlista WHERE fajlista.hun = summarized_data.species);

/* Végső tábla összállítása*/

/* A szüksléges kvadrátok:
DT56C2
DT56C4
DT66C2
DT67B2
DT67C2
DT67D2
DT68B1
DT68D2
DT77A2
DT78B2
*/

DROP TABLE final_data;

CREATE TABLE final_data (
  cover_id INTEGER,
  utm_name TEXT(6),
	observer_id INTEGER,
  observation_date TEXT(10),
  covers_description TEXT(100),
  uploader TEXT(100),
  submit_date TEXT(100),
  pointnum INTEGER,
  count_start TEXT(8),
  wind INTEGER,
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  point_id INTEGER,
  species_huring TEXT(6),
  '100m+' INTEGER,
  'atrepult' INTEGER,
  '0-50m' INTEGER,
  '50-100m' INTEGER
);

INSERT INTO final_data (
  utm_name,
  observer_id,
  observation_date,
  pointnum,
  count_start,
  wind,
  species_huring,
  '100m+',
  atrepult,
  '0-50m',
  '50-100m'
  )
SELECT
  utm_name,
  observer_id,
  observation_date,
  pointnum,
  count_start,
  wind,
  species_huring,
  ring200,
  atrepult,
  circle50,
  ring100
FROM summarized_data
WHERE summarized_data.utm_name IN
(
  'DT56C2',
  'DT56C4',
  'DT66C2',
  'DT67B2',
  'DT67C2',
  'DT67D2',
  'DT68B1',
  'DT68D2',
  'DT77A2',
  'DT78B2'
);

/*Ezt kell exposrtálni csv-be.*/