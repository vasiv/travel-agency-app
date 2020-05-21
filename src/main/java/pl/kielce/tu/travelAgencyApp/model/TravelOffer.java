package pl.kielce.tu.travelAgencyApp.model;

import org.bson.Document;

import java.util.Objects;

/**
 * @author ciepluchs
 */
public class TravelOffer {

    public TravelOffer(Document document) {
        id = String.valueOf(document.getObjectId("_id"));
        destinationCountry = (String) document.get("destinationCountry");
        destinationCity = (String) document.get("destinationCity");
        costPerPerson  = (double) document.get("costPerPerson");
    }

    public TravelOffer(String destinationCountry, String destinationCity, float costPerPerson) {
        this.destinationCountry = destinationCountry;
        this.destinationCity = destinationCity;
        this.costPerPerson = costPerPerson;
    }

    private String id;
    private String destinationCountry;
    private String destinationCity;
    private double costPerPerson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public double getCostPerPerson() {
        return costPerPerson;
    }

    public void setCostPerPerson(double costPerPerson) {
        this.costPerPerson = costPerPerson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelOffer that = (TravelOffer) o;
        return Double.compare(that.getCostPerPerson(), getCostPerPerson()) == 0 &&
                getId().equals(that.getId()) &&
                getDestinationCountry().equals(that.getDestinationCountry()) &&
                getDestinationCity().equals(that.getDestinationCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDestinationCountry(), getDestinationCity(), getCostPerPerson());
    }
}
