package pl.kielce.tu.travelAgencyApp.repository;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * @author ciepluchs
 */
public class QuerySpec {

    private static final String DESTINATION_COUNTRY = "destinationCountry";
    private static final String DESTINATION_CITY = "destinationCity";
    private static final String COST_PER_PERSON = "costPerPerson";
    public static final String SELECTOR = "selector";

    private String destinationCountry;
    private String destinationCity;
    private double costPerPersonLesserOrEqual;
    private double costPerPersonGreaterOrEqual;

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

    public double getCostPerPersonLesserOrEqual() {
        return costPerPersonLesserOrEqual;
    }

    public void setCostPerPersonLesserOrEqual(double costPerPersonLesserOrEqual) {
        this.costPerPersonLesserOrEqual = costPerPersonLesserOrEqual;
    }

    public double getCostPerPersonGreaterOrEqual() {
        return costPerPersonGreaterOrEqual;
    }

    public void setCostPerPersonGreaterOrEqual(double costPerPersonGreaterOrEqual) {
        this.costPerPersonGreaterOrEqual = costPerPersonGreaterOrEqual;
    }

    public List<Bson> toMongoDbQuery() {
        List<Bson> query = new ArrayList<>();
        if (StringUtils.isNotEmpty(this.destinationCountry)) {
            query.add(eq(DESTINATION_COUNTRY, this.destinationCountry));
        }
        if (StringUtils.isNotEmpty(this.destinationCity)) {
            query.add(eq(DESTINATION_CITY, this.destinationCity));
        }
        if (this.costPerPersonLesserOrEqual != 0) {
            query.add(lte(COST_PER_PERSON, this.costPerPersonLesserOrEqual));
        }
        if (this.costPerPersonGreaterOrEqual != 0) {
            query.add(gte(COST_PER_PERSON, this.costPerPersonGreaterOrEqual));
        }
        return query;
    }

    public StringEntity toCouchDbSelector() {
        JSONObject jsonSelector = new JSONObject();
        if (StringUtils.isNotEmpty(this.destinationCountry)) {
            jsonSelector.put(DESTINATION_COUNTRY, this.destinationCountry);
        }
        if (StringUtils.isNotEmpty(this.destinationCity)) {
            jsonSelector.put(DESTINATION_CITY, this.destinationCity);
        }
        JSONObject costSelector = new JSONObject();
        if (this.costPerPersonLesserOrEqual != 0) {
            costSelector.put("$lte", this.costPerPersonLesserOrEqual);
        }
        if (this.costPerPersonGreaterOrEqual != 0) {
            costSelector.put("$gte", this.costPerPersonGreaterOrEqual);
        }
        if(costSelector.length() != 0){
            jsonSelector.put(COST_PER_PERSON, costSelector);
        }
        JSONObject jsonEntity = new JSONObject();
        jsonEntity.put(SELECTOR, jsonSelector);
        return new StringEntity(jsonEntity.toString(), ContentType.APPLICATION_JSON);
    }
}
