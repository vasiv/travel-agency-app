package pl.kielce.tu.travelAgencyApp.repository.travelOffer;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.repository.QuerySpec;

import java.io.IOException;
import java.util.*;

/**
 * @author ciepluchs
 */
public class CouchDbTravelOfferRepository implements TravelOfferRepository {

    private static final String DB_NAME = "travel-offer";
    private static final String DB_BASE_URL = "http://hoefliger-dev.hoefliger.de:5984/" + DB_NAME + "/";
    private HttpClient httpClient;

    public CouchDbTravelOfferRepository() {
        httpClient = HttpClients.createDefault();
    }

    @Override
    public List<TravelOffer> findAll() {
        HttpGet httpGet = new HttpGet(DB_BASE_URL + "_all_docs?include_docs=true");
        httpGet.addHeader("Content-Type", "application/json");
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        return parseResponseToTravelOffers(httpClient, httpGet, responseHandler);
    }

    @Override
    public List<TravelOffer> findBy(QuerySpec querySpec) {
        HttpPost httpPost = new HttpPost(DB_BASE_URL + "_find");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(querySpec.toCouchDbSelector());
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        return parseResponseToTravelOffers(httpClient, httpPost, responseHandler);
    }

    @Override
    public Optional<TravelOffer> findById(String id) {
        HttpGet httpGet = new HttpGet(DB_BASE_URL + id);
        httpGet.addHeader("Content-Type", "application/json");
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        return Optional.ofNullable(parseResponseToTravelOffer(httpClient, httpGet, responseHandler));
    }

    @Override
    public TravelOffer save(TravelOffer travelOffer) {
        HttpPost httpPost = new HttpPost(DB_BASE_URL);
        httpPost.addHeader("Content-Type", "application/json");
        Gson gson = new Gson();
        String travelOfferJson = gson.toJson(travelOffer);
        HttpEntity httpEntity = new StringEntity(travelOfferJson, ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        String createdObjectId = getIdFromResponse(httpClient, httpPost, responseHandler);
        travelOffer.setId(createdObjectId);
        return travelOffer;
    }

    @Override
    public void delete(TravelOffer travelOffer) {
        HttpGet httpGet = new HttpGet(DB_BASE_URL + travelOffer.getId());
        httpGet.addHeader("Content-Type", "application/json");
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        Map<String, String> stringObjectMap = parseResponseToMap(httpClient, httpGet, responseHandler);
        String rev = stringObjectMap.get("_rev");
        HttpDelete httpDelete = new HttpDelete(DB_BASE_URL + travelOffer.getId() + "?rev=" + rev);
        httpDelete.addHeader("Content-Type", "application/json");
        ResponseHandler<String> responseHandler2 = CouchDbTravelOfferRepository::returnResponseAsString;
        parseResponseToMap(httpClient, httpDelete, responseHandler2);
    }

    @Override
    public TravelOffer update(TravelOffer travelOffer) {
        HttpGet httpGet = new HttpGet(DB_BASE_URL + travelOffer.getId());
        httpGet.addHeader("Content-Type", "application/json");
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        Map<String, String> stringObjectMap = parseResponseToMap(httpClient, httpGet, responseHandler);
        String rev = stringObjectMap.get("_rev");

        HttpPut httpPut = new HttpPut(DB_BASE_URL + travelOffer.getId());
        Gson gson = new Gson();
        String travelOfferJson = gson.toJson(travelOffer);
        HttpEntity httpEntity = new StringEntity(travelOfferJson, ContentType.APPLICATION_JSON);
        httpPut.setEntity(httpEntity);
        httpPut.addHeader("Content-Type", "application/json");
        httpPut.addHeader("If-Match", rev);

        ResponseHandler<String> responseHandler2 = CouchDbTravelOfferRepository::returnResponseAsString;
        return parseResponseToTravelOffer(httpClient, httpPut, responseHandler2);
    }

    private static String returnResponseAsString(HttpResponse httpResponse) throws IOException {
        int httpResponseCode = httpResponse.getStatusLine().getStatusCode();
        if (httpResponseCode >= 200 && httpResponseCode < 300) {
            HttpEntity entity = httpResponse.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        } else {
            System.err.println("Unexpected response status: " + httpResponseCode);
            throw new ClientProtocolException("Unexpected response status: " + httpResponseCode);

        }
    }

    private TravelOffer parseResponseToTravelOffer(HttpClient httpClient, HttpUriRequest httpRequest,
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

    private String getIdFromResponse(HttpClient httpClient, HttpUriRequest httpRequest,
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

    private Map<String, String> parseResponseToMap(HttpClient httpClient, HttpUriRequest httpRequest,
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

    private List<TravelOffer> parseResponseToTravelOffers(HttpClient httpClient, HttpUriRequest httpRequest,
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
