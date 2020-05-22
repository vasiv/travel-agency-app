package pl.kielce.tu.travelAgencyApp.repository.travelOffer;

import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.repository.QuerySpec;

import java.util.List;
import java.util.Optional;

public interface TravelOfferRepository {

    List<TravelOffer> findAll();

    Optional<TravelOffer> findById(String id);

    List<TravelOffer> findBy(QuerySpec querySpec);

    TravelOffer save(TravelOffer travelOffer);

    void delete(TravelOffer travelOffer);

    TravelOffer update(TravelOffer travelOffer);

}
