package pl.kielce.tu.travelAgencyApp.repository.travelOffer;

import pl.kielce.tu.travelAgencyApp.model.TravelOffer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TravelOfferRepository {

    List<TravelOffer> findAll();

    Optional<TravelOffer> findById(String id);

    List<TravelOffer> findBy(Map<String, Object> querySpec);

    TravelOffer save(TravelOffer travelOffer);

    void delete(TravelOffer travelOffer);

    TravelOffer update(TravelOffer travelOffer);

}
