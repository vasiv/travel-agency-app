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
public class ShowAllTravelOffers implements Action {

    private static final String HEADER = "################################################ OFFERS #################################################";
    private static final List<Role> ALLOWED_ROLES = Arrays.asList(Role.TRAVEL_AGENT, Role.CUSTOMER);
    private static final String DISPLAY_NAME = "Show all offer";

    private TravelOfferRepository repository;

    public ShowAllTravelOffers(TravelOfferRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        while (true) {
            List<TravelOffer> allTravelOffers = repository.findAll();
            String tableWithBooks = ViewUtil.getTable(allTravelOffers);
            ViewUtil.cls();
            System.out.println(HEADER);
            System.out.println(tableWithBooks);
            System.out.println("Type number of offer to see details:");
            String selectedOption = ViewUtil.getSelectedOption();
            ViewUtil.cls();
            if (ViewUtil.isBackOptionSelected(selectedOption)) {
                return;
            } else {
                ShowTravelOffer showTravelOffer =
                        new ShowTravelOffer(repository, allTravelOffers.get(Integer.parseInt(selectedOption) - 1));
                showTravelOffer.execute();
            }
        }
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
