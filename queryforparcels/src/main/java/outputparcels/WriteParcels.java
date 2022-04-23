package outputparcels;

import parcel.Parcel;

import java.nio.file.Path;
import java.util.List;

public interface WriteParcels {

    List<String> HEADER_HU = List.of("telepules", "hrsz", "alreszlet", "muv_ag", "kivett_megnevezes", "terulet", "tel_hrsz");

    void writeParcels (List<Parcel> parcels, Path path);
}
