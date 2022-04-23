package outputparcels;

import parcel.Parcel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WriteParcelsToCsv implements WriteParcels {

    private final String delimiter;

    public WriteParcelsToCsv(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public void writeParcels(List<Parcel> parcels, Path path) {
        List<String> stringOfParcels = writeParcelsToCsvString(parcels);
        try {
            Files.write(path, stringOfParcels);
        } catch (IOException ioe) {
            throw new IllegalStateException("Can not write file: '" + path + "'!");
        }
    }

    private List<String> writeParcelsToCsvString(List<Parcel> parcels) {
        List<String> fileContent = new ArrayList<>();
        fileContent.add(createHeader());
        fileContent.addAll(parcels.stream().map(parcel -> parcel.toCsvString(delimiter)).toList());
        return fileContent;
    }

    private String createHeader() {
        StringBuilder header = new StringBuilder();
        boolean first = true;
        for (String actual : HEADER_HU) {
            if (first) {
                header.append(actual);
                first = false;
            } else {
                header.append(delimiter);
                header.append(actual);
            }
        }
        return header.toString();
    }
}
