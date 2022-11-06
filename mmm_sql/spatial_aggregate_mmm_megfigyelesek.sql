/* QGIS Field calculator */

/*Add point id to layers*/
aggregate(layer:='megfigyelesi_pont',
aggregate:= 'concatenate',
expression:= point_id,
concatenator:='',
filter:=intersects(geometry(@parent), $geometry))

/*Átrepült viselkedésből önálló réteg*/

/*Attribútumok kapcsolása hely alapján (a körhöz, vagy körgyűrűhöz kapcsolni egy a többhöz kapcsolással a megfigyeléseket) QGIS-ban*/

/*SQLite lekérdezés*/

SELECT dissolve01.fid AS fid, dissolve01.point_id, dissolve01.FAJ AS faj, sum(dissolve01.EGYEDSZAM) AS egyedszam, dissolve01.SZAMLALAS AS szamlalas, dissolve01.geom
	FROM dissolve01
	GROUP by
		dissolve01.point_id,
		dissolve01.FAJ,
		dissolve01.SZAMLALAS
	ORDER BY
		point_id;

/*Egyedi, összekapcsolható azonosító létrehozása*/

point_id || FAJ || SZAMLALAS

/* LEFT OUTER JOIN nincs a spatilaite dbms-ben, ezért egy UNION után egy LEFT JOIN a megoldás*/

SELECT d.type,
         d.color,
         c.type,
         c.color
FROM dogs d
LEFT JOIN cats c USING(color)
UNION ALL
SELECT d.type,
         d.color,
         c.type,
         c.color
FROM cats c
LEFT JOIN dogs d USING(color)
WHERE d.color IS NULL;

/* Végül egy tábla, aminek a geometriája a pont kellene, hogy legyen.*/


/*2. variáció

Körgyűrűk előállítása.
Új réteg létrehozása a megfigyelésekből.
Új mezők létrehozása a megfigyelések rétegben: point_id, 50m, 100m, 200m, átrepült.

Mező kalkulátor: 	Ha metszi valamelyik kategóriát, akkor egyedszám = adott kategória és point_id a másik rétegből.
									Ha viselkedés egyenlő átrepült, akkor point_id = 100as körből, és egyedszám = átrepült.
									Geometria hozzárendelése a megfigyelési ponthoz.
*/
