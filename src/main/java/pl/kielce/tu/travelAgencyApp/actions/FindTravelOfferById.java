package pl.kielce.tu.travelAgencyApp.actions;

import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.model.enumeration.Role;
import pl.kielce.tu.travelAgencyApp.repository.travelOffer.TravelOfferRepository;
import pl.kielce.tu.travelAgencyApp.util.ViewUtil;

import java.util.*;

/**
 * @author ciepluchs
 */
public class FindTravelOfferById implements Action {

    private static final String HEADER = "############################################## FIND OFFER  ##############################################";
    private static final List<Role> ALLOWED_ROLES = Arrays.asList(Role.TRAVEL_AGENT);
    private static final String DISPLAY_NAME = "Find offer by ID";

    private TravelOfferRepository repository;

    public FindTravelOfferById(TravelOfferRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        ViewUtil.cls();
        System.out.println(HEADER);
        String id = getId();
        Optional<TravelOffer> maybeTravelOffer = repository.findById(id);
        TravelOffer travelOffer = maybeTravelOffer.orElseThrow(NoSuchElementException::new);
        ViewUtil.cls();
        new ShowTravelOffer(repository, travelOffer).execute();
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    private String getId() {
        Scanner input = new Scanner(System.in);
        System.out.println("ID: ");
        return input.nextLine();
    }
}
