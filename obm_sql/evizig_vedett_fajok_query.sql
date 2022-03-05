/*BNPI teljes területe, 2010-01-01 után észlelt védett és fokozottan védett, szaporodóhelyen lévő fajok listázása helyrajzi számonként,
adatot nem tartalmazó hrsz-ek is szerepeltetve*/
SELECT evizig_polygons.fid AS id, group_concat(DISTINCT bnpi_species_data.fajnev) AS fajlista
FROM bnpi_species_data, evizig_polygons
WHERE st_intersects (bnpi_species_data.geom, evizig_polygons.geom)
AND (bnpi_species_data.vedettseg = 'V' OR bnpi_species_data.vedettseg = 'FV')
AND (bnpi_species_data >= '2010-01-01')
AND (bnpi_species_data.aktivitas LIKE '%pár%'
OR bnpi_species_data.aktivitas LIKE '%zaporod%'
OR bnpi_species_data.aktivitas LIKE '%ad.%'
OR bnpi_species_data.aktivitas LIKE '%neklő%'
OR bnpi_species_data.aktivitas LIKE '%fészek%'
OR bnpi_species_data.aktivitas LIKE '%fészkel%'
OR bnpi_species_data.aktivitas LIKE '%pelyhes%'
OR bnpi_species_data.aktivitas LIKE '%erritor%'
OR bnpi_species_data.aktivitas LIKE '%költőh%'
OR bnpi_species_data.aktivitas LIKE '%tojás%')
LEFT JOIN evizig_polygons.fid = id
GROUP BY evizig_polygons.fid;

/*DHTE specfikus lekérdezés, minden védett és fokozottan védett faj listázása helyrajzi számonként, csak adatot tartalmazó hrsz-ek*/ 
SELECT evizig_polygons_dhte.fid AS id, group_concat(DISTINCT dhte_species_data.fajnev) AS fajlista
FROM dhte_species_data, evizig_polygons_dhte
WHERE st_intersects (dhte_species_data.geom, evizig_polygons_dhte.geom)
AND (dhte_species_data.vedettseg = 'V' OR dhte_species_data.vedettseg = 'FV')
GROUP BY evizig_polygons_dhte.fid;
