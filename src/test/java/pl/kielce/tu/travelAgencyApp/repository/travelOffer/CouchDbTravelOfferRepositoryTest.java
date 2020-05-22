package pl.kielce.tu.travelAgencyApp.repository.travelOffer;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.repository.QuerySpec;
import pl.kielce.tu.travelAgencyApp.util.PropertiesUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CouchDbTravelOfferRepositoryTest {

    private static final String DATABASE_PROPERTY = "store.couchdb.database";
    private static final String HOST_PROPERTY = "store.couchdb.host";
    private static final String PORT_PROPERTY = "store.couchdb.port";

    private String db_base_url;
    private TravelOfferRepository repository;

    @BeforeEach
    void setUp() {
        String dbName = PropertiesUtil.getProperty(DATABASE_PROPERTY);
        String host = PropertiesUtil.getProperty(HOST_PROPERTY);
        String port = PropertiesUtil.getProperty(PORT_PROPERTY);
        db_base_url = host + ":" + port + "/" + dbName + "/";
        HttpClient httpClient = HttpClients.createDefault();
        repository = new CouchDbTravelOfferRepository();
        HttpPut httpPut = new HttpPut(db_base_url);
        httpPut.addHeader("Content-Type", "application/json");
        try {
            httpClient.execute(httpPut, (ResponseHandler<Object>) httpResponse -> {
                int httpResponseCode = httpResponse.getStatusLine().getStatusCode();
                if (httpResponseCode >= 200 && httpResponseCode < 300) {
                    HttpEntity entity = httpResponse.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    System.err.println("Unexpected response status: " + httpResponseCode);
                    throw new ClientProtocolException("Unexpected response status: " + httpResponseCode);

                }
            });
        } catch (IOException e) {
            System.err.println("Exception during database creation." + e);
        }
    }

    @AfterEach
    void tearDown() {
        HttpClient httpClient = HttpClients.createDefault();
        repository = new CouchDbTravelOfferRepository();
        HttpDelete httpDelete = new HttpDelete(db_base_url);
        httpDelete.addHeader("Content-Type", "application/json");
        try {
            httpClient.execute(httpDelete, (ResponseHandler<Object>) httpResponse -> {
                int httpResponseCode = httpResponse.getStatusLine().getStatusCode();
                if (httpResponseCode >= 200 && httpResponseCode < 300) {
                    HttpEntity entity = httpResponse.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    System.err.println("Unexpected response status: " + httpResponseCode);
                    throw new ClientProtocolException("Unexpected response status: " + httpResponseCode);

                }
            });
        } catch (IOException e) {
            System.err.println("Exception during database creation." + e);
        }
    }

    @Test
    void shouldFindAll() {
        //given
        TravelOffer travelOffer = new TravelOffer("Poland", "Kielce", 1000);
        TravelOffer travelOffer2 = new TravelOffer("Germany", "Berlin", 2000);
        travelOffer = repository.save(travelOffer);
        travelOffer2 = repository.save(travelOffer2);
        //when
        List<TravelOffer> foundTravelOffers = repository.findAll();
        //then
        assertEquals(travelOffer, foundTravelOffers.get(0));
        assertEquals(travelOffer2, foundTravelOffers.get(1));
    }

    @Test
    void shouldSave() {
        //given
        TravelOffer travelOffer = new TravelOffer("Poland", "Kielce", 1000);
        //when
        repository.save(travelOffer);
        //then
        assertTrue(StringUtils.isNotBlank(travelOffer.getId()));
    }

    @Test
    void shouldReturnObjectFoundById() {
        //given
        TravelOffer travelOffer = new TravelOffer("Poland", "Kielce", 1000);
        travelOffer = repository.save(travelOffer);
        //when
        TravelOffer foundTravelOffer = repository.findById(travelOffer.getId()).get();
        //then
        assertEquals(travelOffer.getId(), foundTravelOffer.getId());
    }

    @Test
    void shouldReturnEmptyOptionalWhenObjectNotPresent() {
        //given
        //when
        Optional<TravelOffer> maybeTravelOffer = repository.findById("sampleId");
        //then
        assertTrue(maybeTravelOffer.isEmpty());
    }

    @Test
    void shouldDelete() {
        //given
        TravelOffer travelOffer = new TravelOffer("Poland", "Kielce", 1000);
        travelOffer = repository.save(travelOffer);
        //when
        repository.delete(travelOffer);
        //then
        Optional<TravelOffer> maybeTravelOffer = repository.findById(travelOffer.getId());
        assertTrue(maybeTravelOffer.isEmpty());
    }

    @Test
    void shouldFindObjectByCountryAndCost() {
        //given
        TravelOffer travelOffer = new TravelOffer("Poland", "Kielce", 1500);
        TravelOffer travelOffer2 = new TravelOffer("Germany", "Berlin", 2000);
        TravelOffer travelOffer3 = new TravelOffer("Poland", "Warsaw", 2000);
        repository.save(travelOffer);
        repository.save(travelOffer2);
        repository.save(travelOffer3);
        QuerySpec querySpec = new QuerySpec();
        querySpec.setDestinationCountry("Poland");
        querySpec.setCostPerPersonLesserOrEqual(1600);
        querySpec.setCostPerPersonGreaterOrEqual(1400);
        //when
        List<TravelOffer> foundTravelOffer = repository.findBy(querySpec);
        //then
        assertEquals(1, foundTravelOffer.size());
        assertEquals(travelOffer, foundTravelOffer.get(0));
    }

    @Test
    void shouldUpdateObject() {
        //given
        TravelOffer travelOffer = new TravelOffer("Poland", "Kielce", 1000);
        travelOffer = repository.save(travelOffer);
        travelOffer.setDestinationCity("Warsaw");
        //when
        repository.update(travelOffer);
        //then
        TravelOffer updatedObject = repository.findById(travelOffer.getId()).get();
        assertEquals(travelOffer.getDestinationCity(), updatedObject.getDestinationCity());
    }

}