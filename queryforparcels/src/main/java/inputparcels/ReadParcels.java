package inputparcels;

import parcel.Parcel;

import java.util.List;

public interface ReadParcels {

    List<String> LIST_OF_TOWNSHIP_COLUMN_NAMES = List.of("település", "telepules", "telepulesnev", "településnév");
    List<String> LIST_OF_PARCEL_NUMBER_COLUMN_NAMES = List.of("hrsz", "helyrajzi_szam", "helyrajzi szám");
    List<String> LIST_OF_PARCEL_SUBPART_COLUMN_NAMES = List.of("alr", "alreszlet");
    List<String> LIST_OF_CULTIVATION_COLUMN_NAMES = List.of("muv_ag", "műv_ag", "muvelesi_ag", "művelési_ag", "muvelesi ag", "művelési ág", "művelési ág megnevezés");
    List<String> LIST_OF_AREA_COLUMN_NAMES = List.of("terulet", "terület", "area", "hrsz_ter", "terület nagysága");
    List<String> LIST_OF_UNCULTIVATED_COLUMN_NAMES = List.of("kivett", "kivett_meg", "kivett_megnevezes", "kivett_megnevezés");

    List<Parcel> getListOfParcels();
}
