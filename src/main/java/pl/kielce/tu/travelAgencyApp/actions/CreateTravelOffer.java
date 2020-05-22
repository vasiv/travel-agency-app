package pl.kielce.tu.travelAgencyApp.actions;

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
public class CreateTravelOffer implements Action {

    private static final String HEADER = "############################################ NEW TRAVEL OFFER ###########################################";
    private static final List<Role> ALLOWED_ROLES = Arrays.asList(Role.TRAVEL_AGENT);
    private static final String DISPLAY_NAME = "Create new offer";

    private TravelOfferRepository repository;

    public CreateTravelOffer(TravelOfferRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        ViewUtil.cls();
        System.out.println(HEADER);
        TravelOffer bookToBeAdded = getTravelToBeCreated();
        repository.save(bookToBeAdded);
        ViewUtil.cls();
        System.out.println("Travel created!");
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    private TravelOffer getTravelToBeCreated() {
        Scanner input = new Scanner(System.in);
        System.out.println("What is destination country: ");
        String destinationCountry = input.nextLine();
        System.out.println("What is destination city ");
        String destinationCity = input.nextLine();
        System.out.println("How much does it cost (per person): ");
        double cost = input.nextDouble();
        return new TravelOffer(destinationCountry, destinationCity, cost);
    }
}
