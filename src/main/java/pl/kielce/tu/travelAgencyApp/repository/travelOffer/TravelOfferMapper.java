package pl.kielce.tu.travelAgencyApp.repository.travelOffer;

import org.bson.Document;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;

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
}
