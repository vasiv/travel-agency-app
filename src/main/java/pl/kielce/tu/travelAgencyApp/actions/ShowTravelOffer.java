package pl.kielce.tu.travelAgencyApp.actions;

import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.model.enumeration.Role;
import pl.kielce.tu.travelAgencyApp.repository.travelOffer.TravelOfferRepository;
import pl.kielce.tu.travelAgencyApp.util.ActionUtil;
import pl.kielce.tu.travelAgencyApp.util.ViewUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ciepluchs
 */
public class ShowTravelOffer implements Action {

    private static final String HEADER = "################################################ OFFER ##################################################";
    private static final List<Role> ALLOWED_ROLES = Arrays.asList(Role.TRAVEL_AGENT, Role.CUSTOMER);
    private static final String DISPLAY_NAME = "Show all offer";

    private List<Action> subActions = new ArrayList<>();
    private TravelOffer travelOffer;

    public ShowTravelOffer(TravelOfferRepository repository, TravelOffer travelOffer) {
        this.travelOffer = travelOffer;
        subActions.add(new DeleteTravelOffer(repository, travelOffer));
        subActions.add(new ModifyTravelOffer(repository, travelOffer));
    }

    @Override
    public void execute() {
        System.out.println(HEADER);
        System.out.println(ViewUtil.getDetailedView(travelOffer));
        List<Action> availableActions = ActionUtil.getAvailableActions(subActions);
        while (true) {
            ViewUtil.displaySubMenu(availableActions);
            String selectedOption = ViewUtil.getSelectedOption();
            ViewUtil.cls();
            if (ActionUtil.isBackOptionSelected(selectedOption)) {
                return;
            } else {
                Action selectedAction = ActionUtil.getSelectedAction(availableActions, selectedOption);
                selectedAction.execute();
                return;
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
