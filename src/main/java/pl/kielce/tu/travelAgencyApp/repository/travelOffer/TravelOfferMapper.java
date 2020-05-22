package pl.kielce.tu.travelAgencyApp.repository.travelOffer;

import com.google.gson.Gson;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ciepluchs
 */
public class TravelOfferMapper {

    public static Document parseToMongoDbDocument(TravelOffer travelOffer) {
        return new Document()
                .append("destinationCountry", travelOffer.getDestinationCountry())
                .append("destinationCity", travelOffer.getDestinationCity())
                .append("costPerPerson", travelOffer.getCostPerPerson());
    }

    public static TravelOffer parseResponseToTravelOffer(HttpClient httpClient, HttpUriRequest httpRequest,
                                                   ResponseHandler<String> responseHandler) {
        String strResponse = "";
        try {
            strResponse = httpClient.execute(httpRequest, responseHandler);
        } catch (IOException e) {
            System.err.println("Exception during HTTP request: " + e);
        }
        Gson g = new Gson();
        return g.fromJson(strResponse, TravelOffer.class);
    }

    public static String getIdFromResponse(HttpClient httpClient, HttpUriRequest httpRequest,
                                     ResponseHandler<String> responseHandler) {
        String strResponse = "";
        try {
            strResponse = httpClient.execute(httpRequest, responseHandler);
        } catch (IOException e) {
            System.err.println("Exception during HTTP request: " + e);
        }
        Gson g = new Gson();
        return strResponse.split(",")[1].split(":")[1].replace("\"", "");
    }

    public static Map<String, String> parseResponseToMap(HttpClient httpClient, HttpUriRequest httpRequest,
                                                   ResponseHandler<String> responseHandler) {
        String strResponse = "";
        try {
            strResponse = httpClient.execute(httpRequest, responseHandler);
        } catch (IOException e) {
            System.err.println("Exception during HTTP request: " + e);
        }
        String[] reponsePairs = strResponse.replace("{", "").replace("}", "").split(",");
        Map<String, String> reponse = new HashMap<>();
        for (String pair : reponsePairs) {
            String[] splittedPair = pair.split(":");
            splittedPair[0] = splittedPair[0].replace("\"", "");
            splittedPair[1] = splittedPair[1].replace("\"", "");
            reponse.put(splittedPair[0], splittedPair[1]);
        }
        return reponse;
    }

    public static List<TravelOffer> parseResponseToTravelOffers(HttpClient httpClient, HttpUriRequest httpRequest,
                                                          ResponseHandler<String> responseHandler) {
        List<TravelOffer> travelOffers = new ArrayList<>();
        String strResponse = "";
        try {
            strResponse = httpClient.execute(httpRequest, responseHandler);
        } catch (IOException e) {
            System.err.println("Exception during HTTP request: " + e);
        }
        Gson gson = new Gson();
        JSONObject objects = new JSONObject(strResponse);
        Object rows = objects.opt("rows");
        if (rows != null) {
            List<Object> rowsList = ((JSONArray) rows).toList();
            for (Object row : rowsList) {
                Map documentJson = (Map) ((Map) row).get("doc");
                String s = gson.toJson(documentJson);
                travelOffers.add(gson.fromJson(s, TravelOffer.class));
            }
        } else {
            Object docs = objects.opt("docs");
            List<Object> docsList = ((JSONArray) docs).toList();
            for (Object row : docsList) {
                String s = gson.toJson(row);
                travelOffers.add(gson.fromJson(s, TravelOffer.class));
            }
        }
        return travelOffers;
    }
}
