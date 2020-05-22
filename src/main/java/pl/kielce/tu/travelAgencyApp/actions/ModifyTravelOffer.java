package pl.kielce.tu.travelAgencyApp.actions;

import org.apache.commons.lang3.StringUtils;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.model.enumeration.Role;
import pl.kielce.tu.travelAgencyApp.repository.travelOffer.TravelOfferRepository;
import pl.kielce.tu.travelAgencyApp.util.ViewUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author ciepluchs
 */
public class ModifyTravelOffer implements Action {

    private static final String HEADER = "################################################ MODIFY  ################################################";
    private static final List<Role> ALLOWED_ROLES = Arrays.asList(Role.TRAVEL_AGENT);
    private static final String DISPLAY_NAME = "Modify";

    private TravelOfferRepository repository;
    private TravelOffer travelOffer;

    public ModifyTravelOffer(TravelOfferRepository repository, TravelOffer travelOffer) {
        this.repository = repository;
        this.travelOffer = travelOffer;
    }

    @Override
    public void execute() {
        System.out.println(HEADER);
        System.out.println(ViewUtil.getDetailedView(travelOffer));
        System.out.println("-".repeat(105) + "\n");
        TravelOffer modifiedTravel = getModifiedTravel(travelOffer);
        repository.update(modifiedTravel);
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    private TravelOffer getModifiedTravel(TravelOffer travelOffer) {
        Scanner input = new Scanner(System.in);

        System.out.println("What is new destination country: (press ENTER to make no change)");
        String destinationCountry = input.nextLine();
        if(StringUtils.isNotEmpty(destinationCountry)){
            travelOffer.setDestinationCountry(destinationCountry);
            System.out.println("\n");
        }

        System.out.println("What is destination city (press ENTER to make no change)");
        String destinationCity = input.nextLine();
        if(StringUtils.isNotEmpty(destinationCity)){
            travelOffer.setDestinationCity(destinationCity);
            System.out.println("\n");
        }


        System.out.println("How much does it cost (per person): (press ENTER to make no change)");
        String cost = input.nextLine();
        if(StringUtils.isNotEmpty(cost)){
            travelOffer.setCostPerPerson(Double.valueOf(cost));
            System.out.println("\n");
        }
        return travelOffer;
    }
}
