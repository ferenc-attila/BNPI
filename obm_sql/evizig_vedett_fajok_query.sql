select evizig_polygons_dhte.fid, group_concat(distinct dhte_species_data.fajnev) AS fajlista from dhte_species_data, evizig_polygons_dhte
where st_intersects (dhte_species_data.geom, evizig_polygons_dhte.geom)
and (dhte_species_data.vedettseg = 'V' OR dhte_species_data.vedettseg = 'FV')
group by evizig_polygons_dhte.fid;
