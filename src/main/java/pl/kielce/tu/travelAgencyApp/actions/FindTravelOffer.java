package pl.kielce.tu.travelAgencyApp.actions;

import org.apache.commons.lang3.StringUtils;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.model.enumeration.Role;
import pl.kielce.tu.travelAgencyApp.repository.QuerySpec;
import pl.kielce.tu.travelAgencyApp.repository.travelOffer.TravelOfferRepository;
import pl.kielce.tu.travelAgencyApp.util.ViewUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author ciepluchs
 */
public class FindTravelOffer implements Action {

    private static final String HEADER = "############################################## FIND OFFER  ##############################################";
    private static final List<Role> ALLOWED_ROLES = Arrays.asList(Role.TRAVEL_AGENT, Role.CUSTOMER);
    private static final String DISPLAY_NAME = "Find offer";

    private TravelOfferRepository repository;

    public FindTravelOffer(TravelOfferRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() {
        ViewUtil.cls();
        System.out.println(HEADER);
        QuerySpec querySpec = getQuerySpec();
        List<TravelOffer> travelOffers = repository.findBy(querySpec);
        if (travelOffers.isEmpty()){
            System.out.println("Nothing found...");
            try {
                System.in.read();
            } catch (IOException e) {
                System.err.println("Exception: " + e);
            }
            ViewUtil.cls();
            return;
        }
        if (travelOffers.size() == 1) {
            new ShowTravelOffer(repository, travelOffers.get(0)).execute();
        } else {
            System.out.println("-".repeat(105) + "\n");
            String tableWithBooks = ViewUtil.getTable(travelOffers);
            System.out.println(tableWithBooks);
            System.out.println("Type number of offer to see details:");
            String selectedOption = ViewUtil.getSelectedOption();
            ViewUtil.cls();
            if (ViewUtil.isBackOptionSelected(selectedOption)) {
                return;
            } else {
                new ShowTravelOffer(repository, travelOffers.get(Integer.parseInt(selectedOption) - 1)).execute();
            }
        }
        return;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public List<Role> getAllowedRoles() {
        return ALLOWED_ROLES;
    }

    private QuerySpec getQuerySpec() {
        Scanner input = new Scanner(System.in);
        QuerySpec querySpec = new QuerySpec();

        System.out.println("Which destination country: (press ENTER to make no change)");
        String destinationCountry = input.nextLine();
        if (StringUtils.isNotEmpty(destinationCountry)) {
            querySpec.setDestinationCountry(destinationCountry);
            System.out.println("\n");
        }

        System.out.println("Which destination city (press ENTER to make no change)");
        String destinationCity = input.nextLine();
        if (StringUtils.isNotEmpty(destinationCity)) {
            querySpec.setDestinationCity(destinationCity);
            System.out.println("\n");
        }


        System.out.println("Min cost: (press ENTER to make no change)");
        String minCost = input.nextLine();
        if (StringUtils.isNotEmpty(minCost)) {
            querySpec.setCostPerPersonGreaterOrEqual(Double.valueOf(minCost));
            System.out.println("\n");
        }

        System.out.println("Max cost: (press ENTER to make no change)");
        String maxCost = input.nextLine();
        if (StringUtils.isNotEmpty(maxCost)) {
            querySpec.setCostPerPersonLesserOrEqual(Double.valueOf(maxCost));
            System.out.println("\n");
        }
        return querySpec;
    }
}
