package inputparcels;

import org.junit.jupiter.api.Test;
import parcel.Parcel;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReadParcelsFromCsvTest {

    ReadParcelsFromCsv readParcelsFromCsv = new ReadParcelsFromCsv(Path.of("src/test/resources/evizig_hrszlista_alr_nelkuli.csv"));

    @Test
    void getListOfParcelsTest() {
        List<Parcel> parcels = readParcelsFromCsv.getListOfParcels();

        assertEquals(1962, parcels.size());
    }
}