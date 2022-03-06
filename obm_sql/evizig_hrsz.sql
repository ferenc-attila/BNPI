CREATE TABLE evizig_polygons
AS
SELECT *
FROM kuvet_bevet_bnpi
JOIN evizig_hrsz_lista_alr_nelkul ON kuvet_bevet_bnpi.telepules = evizig_hrsz_lista_alr_nelkul.telepules AND kuvet_bevet_bnpi.hrsz = evizig_hrsz_lista_alr_nelkul.hrsz
