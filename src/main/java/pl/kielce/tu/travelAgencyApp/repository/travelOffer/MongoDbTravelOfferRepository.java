package pl.kielce.tu.travelAgencyApp.repository.travelOffer;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * @author ciepluchs
 */
public class MongoDbTravelOfferRepository implements TravelOfferRepository {

    MongoCollection travelOfferCollection;

    public MongoDbTravelOfferRepository(MongoDatabase db) {
        this.travelOfferCollection = db.getCollection("travelOffer");
    }

    @Override
    public List<TravelOffer> findAll() {
        MongoCursor cursor = travelOfferCollection.find().cursor();
        return transformCursorToList(cursor);
    }

    @Override
    public Optional<TravelOffer> findById(String id) {
        Document travelOfferDocument = (Document) travelOfferCollection.find(eq("_id", new ObjectId(id))).first();
        return travelOfferDocument != null ? Optional.of(new TravelOffer(travelOfferDocument)) : Optional.empty();
    }

    @Override
    public TravelOffer save(TravelOffer travelOffer) {
        Document travelOfferDocument = TravelOfferMapper.parseToMongoDbDocument(travelOffer);
        travelOfferCollection.insertOne(travelOfferDocument);
        travelOffer.setId(travelOfferDocument.getObjectId("_id").toString());
        return travelOffer;
    }

    @Override
    public List<TravelOffer> findBy(Map<String, Object> querySpec) {
        List<Bson> query = transformQuerySpecToBsons(querySpec);
        MongoCursor cursor = travelOfferCollection.find(and(query)).cursor();
        return transformCursorToList(cursor);
    }

    @Override
    public void delete(TravelOffer travelOffer) {
        Document travelOfferDocument = TravelOfferMapper.parseToMongoDbDocument(travelOffer);
        travelOfferCollection.deleteOne(travelOfferDocument);
    }

    @Override
    public TravelOffer update(TravelOffer travelOffer) {
        Document query = new Document().append("_id", new ObjectId(travelOffer.getId()));
        Document travelOfferDocument = TravelOfferMapper.parseToMongoDbDocument(travelOffer);
        Document update = new Document();
        update.append("$set", travelOfferDocument);
        travelOfferCollection.updateOne(query, update);
        return travelOffer;
    }

    private List<TravelOffer> transformCursorToList(MongoCursor cursor) {
        List<TravelOffer> travelOffers = new ArrayList<>();
        while (cursor.hasNext()) {
            Document travelOfferDocument = (Document) cursor.next();
            TravelOffer travelOffer = new TravelOffer(travelOfferDocument);
            travelOffers.add(travelOffer);
        }
        return travelOffers;
    }

    private List<Bson> transformQuerySpecToBsons(Map<String, Object> querySpec) {
        List<Bson> query = new ArrayList<>();
        querySpec.forEach((key, value) -> {
            query.add(eq(key, value));
        });
        return query;
    }
}
