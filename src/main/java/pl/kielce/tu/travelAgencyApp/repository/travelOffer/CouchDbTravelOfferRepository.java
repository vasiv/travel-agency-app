package pl.kielce.tu.travelAgencyApp.repository.travelOffer;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.repository.QuerySpec;
import pl.kielce.tu.travelAgencyApp.util.PropertiesUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author ciepluchs
 */
public class CouchDbTravelOfferRepository implements TravelOfferRepository {

    private static final String DATABASE_PROPERTY = "store.couchdb.database";
    private static final String HOST_PROPERTY = "store.couchdb.host";
    private static final String PORT_PROPERTY = "store.couchdb.port";

    private String db_base_url;
    private HttpClient httpClient;

    public CouchDbTravelOfferRepository() {
        httpClient = HttpClients.createDefault();
        String dbName = PropertiesUtil.getProperty(DATABASE_PROPERTY);
        String host = PropertiesUtil.getProperty(HOST_PROPERTY);
        String port = PropertiesUtil.getProperty(PORT_PROPERTY);
        db_base_url = host + ":" + port + "/" + dbName + "/";
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

    @Override
    public List<TravelOffer> findAll() {
        HttpGet httpGet = new HttpGet(db_base_url + "_all_docs?include_docs=true");
        httpGet.addHeader("Content-Type", "application/json");
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        return TravelOfferMapper.parseResponseToTravelOffers(httpClient, httpGet, responseHandler);
    }

    @Override
    public List<TravelOffer> findBy(QuerySpec querySpec) {
        HttpPost httpPost = new HttpPost(db_base_url + "_find");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(querySpec.toCouchDbSelector());
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        return TravelOfferMapper.parseResponseToTravelOffers(httpClient, httpPost, responseHandler);
    }

    @Override
    public Optional<TravelOffer> findById(String id) {
        HttpGet httpGet = new HttpGet(db_base_url + id);
        httpGet.addHeader("Content-Type", "application/json");
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        return Optional.ofNullable(TravelOfferMapper.parseResponseToTravelOffer(httpClient, httpGet, responseHandler));
    }

    @Override
    public TravelOffer save(TravelOffer travelOffer) {
        HttpPost httpPost = new HttpPost(db_base_url);
        httpPost.addHeader("Content-Type", "application/json");
        Gson gson = new Gson();
        String travelOfferJson = gson.toJson(travelOffer);
        HttpEntity httpEntity = new StringEntity(travelOfferJson, ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        String createdObjectId = TravelOfferMapper.getIdFromResponse(httpClient, httpPost, responseHandler);
        travelOffer.setId(createdObjectId);
        return travelOffer;
    }

    @Override
    public void delete(TravelOffer travelOffer) {
        HttpGet httpGet = new HttpGet(db_base_url + travelOffer.getId());
        httpGet.addHeader("Content-Type", "application/json");
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        Map<String, String> stringObjectMap = TravelOfferMapper.parseResponseToMap(httpClient, httpGet, responseHandler);
        String rev = stringObjectMap.get("_rev");
        HttpDelete httpDelete = new HttpDelete(db_base_url + travelOffer.getId() + "?rev=" + rev);
        httpDelete.addHeader("Content-Type", "application/json");
        ResponseHandler<String> responseHandler2 = CouchDbTravelOfferRepository::returnResponseAsString;
        TravelOfferMapper.parseResponseToMap(httpClient, httpDelete, responseHandler2);
    }

    @Override
    public TravelOffer update(TravelOffer travelOffer) {
        HttpGet httpGet = new HttpGet(db_base_url + travelOffer.getId());
        httpGet.addHeader("Content-Type", "application/json");
        ResponseHandler<String> responseHandler = CouchDbTravelOfferRepository::returnResponseAsString;
        Map<String, String> stringObjectMap = TravelOfferMapper.parseResponseToMap(httpClient, httpGet, responseHandler);
        String rev = stringObjectMap.get("_rev");

        HttpPut httpPut = new HttpPut(db_base_url + travelOffer.getId());
        Gson gson = new Gson();
        String travelOfferJson = gson.toJson(travelOffer);
        HttpEntity httpEntity = new StringEntity(travelOfferJson, ContentType.APPLICATION_JSON);
        httpPut.setEntity(httpEntity);
        httpPut.addHeader("Content-Type", "application/json");
        httpPut.addHeader("If-Match", rev);

        ResponseHandler<String> responseHandler2 = CouchDbTravelOfferRepository::returnResponseAsString;
        return TravelOfferMapper.parseResponseToTravelOffer(httpClient, httpPut, responseHandler2);
    }
}
