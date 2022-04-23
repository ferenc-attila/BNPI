package parcel;

import java.util.Objects;

public class Parcel {

    private String township;
    private String parcelNumber;
    private String subpart;
    private String cultivation;
    private String uncultivatedName;
    private double area;

    public Parcel(String township, String parcelNumber) {
        this.township = township;
        this.parcelNumber = parcelNumber;
    }

    public Parcel(String township, String parcelNumber, String subpart) {
        this(township, parcelNumber);
        this.subpart = subpart;
    }

    public Parcel(String township, String parcelNumber, String cultivation, double area) {
        this(township, parcelNumber);
        this.cultivation = cultivation;
        this.area = area;
    }

    public Parcel(String township, String parcelNumber, String subpart, String cultivation, String uncultivated, double area) {
        this(township, parcelNumber, subpart);
        this.cultivation = cultivation;
        this.uncultivatedName = uncultivated;
        this.area = area;
    }

    public String toCsvString(String delimiter) {
        return township + delimiter +
                parcelNumber + delimiter +
                subpart + delimiter +
                cultivation + delimiter +
                uncultivatedName + delimiter +
                area + delimiter +
                toIdString();
    }

    public String toIdString() {
        return township + "-" + parcelNumber + subpart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return parcelNumber.equals(parcel.parcelNumber) && township.equals(parcel.township) && subpart.equals(parcel.subpart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(township, parcelNumber, subpart);
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public String getSubpart() {
        return subpart;
    }

    public void setSubpart(String subpart) {
        this.subpart = subpart;
    }

    public String getCultivation() {
        return cultivation;
    }

    public void setCultivation(String cultivation) {
        this.cultivation = cultivation;
    }

    public String getUncultivatedName() {
        return uncultivatedName;
    }

    public void setUncultivatedName(String uncultivatedName) {
        this.uncultivatedName = uncultivatedName;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}
