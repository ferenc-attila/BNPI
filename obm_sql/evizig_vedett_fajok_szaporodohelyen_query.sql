select evizig_polygons_dhte.fid, group_concat(distinct dhte_species_data.fajnev) AS fajlista from dhte_species_data, evizig_polygons_dhte
where st_intersects (dhte_species_data.geom, evizig_polygons_dhte.geom)
and (dhte_species_data.vedettseg = 'V' OR dhte_species_data.vedettseg = 'FV')
and (dhte_species_data.aktivitas LIKE '%pár%'
OR dhte_species_data.aktivitas LIKE '%zaporod%'
OR dhte_species_data.aktivitas LIKE '%ad.%'
OR dhte_species_data.aktivitas LIKE '%neklő%'
OR dhte_species_data.aktivitas LIKE '%fészek%'
OR dhte_species_data.aktivitas LIKE '%fészkel%'
OR dhte_species_data.aktivitas LIKE '%pelyhes%'
OR dhte_species_data.aktivitas LIKE '%tojás%'
OR dhte_species_data.aktivitas LIKE '%erritor%'
OR dhte_species_data.aktivitas LIKE '%költőh%'
OR dhte_species_data.aktivitas LIKE '%tojás%')
group by evizig_polygons_dhte.fid;
