package pl.kielce.tu.travelAgencyApp.actions;

import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.model.enumeration.Role;
import pl.kielce.tu.travelAgencyApp.repository.travelOffer.TravelOfferRepository;
import pl.kielce.tu.travelAgencyApp.util.ViewUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author ciepluchs
 */
public class DeleteTravelOffer implements Action {

    private static final List<Role> ALLOWED_ROLES = Arrays.asList(Role.TRAVEL_AGENT);
    private static final String DISPLAY_NAME = "Delete";

    private TravelOfferRepository repository;
    private TravelOffer travelOffer;

    public DeleteTravelOffer(TravelOfferRepository repository, TravelOffer travelOffer) {
        this.repository = repository;
        this.travelOffer = travelOffer;
    }

    @Override
    public void execute() {
        repository.delete(travelOffer);
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

}
