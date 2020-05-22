package pl.kielce.tu.travelAgencyApp.repository.travelOffer;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.repository.QuerySpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MongoDbTravelOfferRepositoryTest {

    private TravelOfferRepository repository;
    private MongoCollection travelOfferCollection;
    private MongoDatabase db;

    @BeforeEach
    void setUp() {
        MongoClient mongoClient = new MongoClient("hoefliger-dev.hoefliger.de", 27017);
        db = mongoClient.getDatabase("travel-agency-app-test");
        travelOfferCollection = db.getCollection("travelOffer");
        repository = new MongoDbTravelOfferRepository(db);
    }

    @AfterEach
    void tearDown() {
        db.drop();
    }

    @Test
    void shouldSave() {
        //given
        TravelOffer travelOffer = new TravelOffer("Poland", "Kielce", 1000);
        //when
        repository.save(travelOffer);
        //then
        assertTrue(StringUtils.isNotBlank(travelOffer.getId()));
        assertEquals(1, travelOfferCollection.countDocuments());
    }

    @Test
    void shouldFindAll() {
        //given
        TravelOffer travelOffer = new TravelOffer("Poland", "Kielce", 1000);
        TravelOffer travelOffer2 = new TravelOffer("Germany", "Berlin", 2000);
        repository.save(travelOffer);
        repository.save(travelOffer2);
        //when
        List<TravelOffer> allTravelOffers = repository.findAll();
        //then
        assertEquals(2, allTravelOffers.size());
        assertEquals(travelOffer, allTravelOffers.get(0));
        assertEquals(travelOffer2, allTravelOffers.get(1));
    }

    @Test
    void shouldReturnObjectFoundById() {
        //given
        TravelOffer travelOffer = new TravelOffer("Poland", "Kielce", 1000);
        travelOffer = repository.save(travelOffer);
        //when
        TravelOffer foundTravelOffer = repository.findById(travelOffer.getId()).get();
        //then
        assertEquals(travelOffer, foundTravelOffer);
    }

    @Test
    void shouldReturnEmptyOptionalWhenObjectNotPresent() {
        //given
        //when
        Optional<TravelOffer> maybeTravelOffer = repository.findById(String.valueOf(new ObjectId()));
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
        assertEquals(0, travelOfferCollection.countDocuments());
    }

    @Test
    void shouldFindObjectByCountryAndCost() {
        //given
        TravelOffer travelOffer = new TravelOffer("Poland", "Kielce", 1000);
        TravelOffer travelOffer2 = new TravelOffer("Germany", "Berlin", 2000);
        TravelOffer travelOffer3 = new TravelOffer("Poland", "Warsaw", 2000);
        repository.save(travelOffer);
        repository.save(travelOffer2);
        repository.save(travelOffer3);
        QuerySpec querySpec = new QuerySpec();
        querySpec.setDestinationCountry("Poland");
        querySpec.setCostPerPersonLesserOrEqual(1000);
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
        assertEquals(1, travelOfferCollection.countDocuments());
        assertEquals(travelOffer.getDestinationCity(), updatedObject.getDestinationCity());
    }
}