SELECT datum, faj, egyed, ivar, kor, idojaras, elohely, viselkedes, megjegyzes, adatkozlo, gyujtesi_mod, lon, lat, koord_pont, feltolto, szamossag, gyujto, idopont, aktivitas, hatarozo, felmeres_mod, veszely, koltohely_azonosito, rtm_id, szalakota_id, gyuruzes_id, mikrohabitat, statusz, mmm_szamlalas
FROM public.dhte
WHERE faj IN
('Alternanthera philoxeroides','Cabomba caroliniana','Eichhornia crassipes','Elodea nuttallii','Hydrocotyle ranunculoides','Lagarosiphon major','Ludwigia grandiflora','Ludwigia peploides','Myriophyllum aquaticum','Myriophyllum heterophyllum','Gymnocoronis spilanthoides','Salvinia molesta','Asclepias syriaca','Gunnera tinctoria','Heracleum mantegazzianum','Heracleum persicum','Heracleum sosnowskyi','Impatiens glandulifera','Lysichiton americanus','Microstegium vimineum','Parthenium hysterophorus','Pennisetum setaceum','Persicaria perfoliata','Polygonum perfoliatum','Pueraria montana var.lobata','Pueraria lobata','Andropogon virginicus','Cardiospermum grandiflorum','Cortaderia jubata','Ehrharta calycina','Lespedeza cuneata','Lygodium japonicum','Humulus scandes','Baccharis halimifolia','Acacia saligna','Prosopis juliflora','Triadica sebifera','Ailanthus altissima','Arthurdendyus triangulatus','Eriocheir sinensis','Orconectes limosus','Orconectes virilis','Pacifastacus leniusculus','Procambarus clarkii','Procambarus falax formavirginalis','Vespa velutina nigrithorax','Perccottus glenii','Pseudorasbora parva','Plotosus lineatus','Lepomis gibbosus','Lithobates catesbeianus','Rana catesbeianus','Trachemys scripta','Alopochen aegyptiacus','Corvus splendens','Oxyura jamaicensis','Threskiornis aethiopicus','Acridotheres tristis','Callosciurus erythraeus','Herpestes javanicus','Muntiacus reevesii','Myocastor coypus','Nasua nasua','Nyctereutes procyonoides','Ondatra zibethicus','Procyon lotor','Sciurus carolinensis','Sciurus niger','Tamias sibiricus')
AND datum BETWEEN '2021-01-01' AND '2021-12-31'