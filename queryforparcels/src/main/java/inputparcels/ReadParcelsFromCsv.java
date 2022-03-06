package inputparcels;

import parcel.Parcel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ReadParcelsFromCsv implements ReadParcels {

    private Path path;

    public ReadParcelsFromCsv(Path path) {
        this.path = path;
    }

    @Override
    public List<Parcel> getListOfParcels() {
        List<Parcel> parcels = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            fillListOfParcels(parcels, br);
            return parcels;
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Cannot read file: " + path + "!");
        }
    }

    private void fillListOfParcels(List<Parcel> parcels, BufferedReader br) throws IOException {
        String line;
        boolean first = true;
        Map<String, Integer> indexes = new HashMap<>();
        while ((line = br.readLine()) != null) {
            List<String> row = new ArrayList<>(Arrays.asList(line.split(";")));
            if (first) {
                List<String> header = getHeader(row);
                getColumnIndexes(header, indexes);
                first = false;
            } else {
                addNewParcelsByRow(parcels, row, indexes);
            }
        }
    }

    private List<String> getHeader(List<String> row) {
        List<String> header = new ArrayList<>();
        for (String columnName : row) {
            header.add(columnName.toLowerCase());
        }
        return header;
    }

    private void addNewParcelsByRow(List<Parcel> parcels, List<String> row, Map<String, Integer> indexes) {
        double area = changeDecimalCommaToPoint(row, indexes);
        parcels.add(new Parcel(row.get(indexes.get("township")),
                row.get(indexes.get("parcelNumber")),
//                row.get(indexes.get("subpart")),
                row.get(indexes.get("cultivation")),
//                row.get(indexes.get("uncultivated")),
                area));
    }

    private double changeDecimalCommaToPoint(List<String> row, Map<String, Integer> indexes) {
        if (row.get(indexes.get("area")).contains(",")) {
            String area = row.get(indexes.get("area")).replace(',', '.');
            return Double.parseDouble(area);
        } else {
            try {
                return Double.parseDouble(row.get(indexes.get("area")));
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Invalid decimal delimiter!");
            }
        }
    }

    private void getColumnIndexes(List<String> row, Map<String, Integer> indexes) {
        indexes.put("township", findColumnIndex(row, ReadParcels.LIST_OF_TOWNSHIP_COLUMN_NAMES));
        indexes.put("parcelNumber", findColumnIndex(row, ReadParcels.LIST_OF_PARCEL_NUMBER_COLUMN_NAMES));
//        indexes.put("subpart", findColumnIndex(row, ReadParcels.LIST_OF_PARCEL_SUBPART_COLUMN_NAMES));
        indexes.put("cultivation", findColumnIndex(row, ReadParcels.LIST_OF_CULTIVATION_COLUMN_NAMES));
//        indexes.put("uncultivated", findColumnIndex(row, ReadParcels.LIST_OF_UNCULTIVATED_COLUMN_NAMES));
        indexes.put("area", findColumnIndex(row, ReadParcels.LIST_OF_AREA_COLUMN_NAMES));
    }

    private int findColumnIndex(List<String> header, List<String> possibleColumnNames) {
        return header.indexOf(
                header.stream()
                        .filter(possibleColumnNames::contains)
                        .findFirst()
                        .orElse("")
        );
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
