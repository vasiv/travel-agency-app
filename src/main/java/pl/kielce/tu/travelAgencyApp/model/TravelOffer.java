package pl.kielce.tu.travelAgencyApp.model;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.google.gson.annotations.SerializedName;
import org.bson.Document;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;

/**
 * @author ciepluchs
 */
public class TravelOffer implements Jsonable {

    public TravelOffer(Document document) {
        id = String.valueOf(document.getObjectId("_id"));
        destinationCountry = (String) document.get("destinationCountry");
        destinationCity = (String) document.get("destinationCity");
        costPerPerson  = (double) document.get("costPerPerson");
    }

    public TravelOffer(String destinationCountry, String destinationCity, double costPerPerson) {
        this.destinationCountry = destinationCountry;
        this.destinationCity = destinationCity;
        this.costPerPerson = costPerPerson;
    }

    @SerializedName(value = "_id")
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
                getDestinationCountry().equals(that.getDestinationCountry()) &&
                getDestinationCity().equals(that.getDestinationCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDestinationCountry(), getDestinationCity(), getCostPerPerson());
    }

    @Override
    public String toJson() {
        final StringWriter writable = new StringWriter();
        try {
            this.toJson(writable);
        } catch (final IOException e) {
            System.err.println("Exception during parsing object to JSON " +  e);
        }
        return writable.toString();
    }

    @Override
    public void toJson(Writer writer) throws IOException {
        final JsonObject json = new JsonObject();
        json.put("destinationCountry", getDestinationCountry());
        json.put("destinationCity", getDestinationCity());
        json.put("costPerPerson", getCostPerPerson());
        json.toJson(writer);
    }
}
