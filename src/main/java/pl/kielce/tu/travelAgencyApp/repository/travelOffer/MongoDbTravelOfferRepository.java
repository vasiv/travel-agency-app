package pl.kielce.tu.travelAgencyApp.repository.travelOffer;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.repository.QuerySpec;
import pl.kielce.tu.travelAgencyApp.util.PropertiesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * @author ciepluchs
 */
public class MongoDbTravelOfferRepository implements TravelOfferRepository {

    private static final String COLLECTION_PROPERTY = "store.mongodb.collection";
    private static final String ID = "_id";
    private static final String $_SET = "$set";
    MongoCollection travelOfferCollection;

    public MongoDbTravelOfferRepository(MongoDatabase db) {
        String collectionName = PropertiesUtil.getProperty(COLLECTION_PROPERTY);
        this.travelOfferCollection = db.getCollection(collectionName);
    }

    @Override
    public List<TravelOffer> findAll() {
        MongoCursor cursor = travelOfferCollection.find().cursor();
        return transformCursorToList(cursor);
    }

    @Override
    public Optional<TravelOffer> findById(String id) {
        Document travelOfferDocument = (Document) travelOfferCollection.find(eq(ID, new ObjectId(id))).first();
        return travelOfferDocument != null ? Optional.of(new TravelOffer(travelOfferDocument)) : Optional.empty();
    }

    @Override
    public TravelOffer save(TravelOffer travelOffer) {
        Document travelOfferDocument = TravelOfferMapper.parseToMongoDbDocument(travelOffer);
        travelOfferCollection.insertOne(travelOfferDocument);
        travelOffer.setId(travelOfferDocument.getObjectId(ID).toString());
        return travelOffer;
    }

    @Override
    public List<TravelOffer> findBy(QuerySpec querySpec) {
        MongoCursor cursor = travelOfferCollection.find(and(querySpec.toMongoDbQuery())).cursor();
        return transformCursorToList(cursor);
    }

    @Override
    public void delete(TravelOffer travelOffer) {
        Document travelOfferDocument = TravelOfferMapper.parseToMongoDbDocument(travelOffer);
        travelOfferCollection.deleteOne(travelOfferDocument);
    }

    @Override
    public TravelOffer update(TravelOffer travelOffer) {
        Document query = new Document().append(ID, new ObjectId(travelOffer.getId()));
        Document travelOfferDocument = TravelOfferMapper.parseToMongoDbDocument(travelOffer);
        Document update = new Document();
        update.append($_SET, travelOfferDocument);
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
}
