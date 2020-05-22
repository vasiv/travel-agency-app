package pl.kielce.tu.travelAgencyApp.util;

import org.apache.commons.lang3.StringUtils;
import pl.kielce.tu.travelAgencyApp.actions.*;
import pl.kielce.tu.travelAgencyApp.model.TravelOffer;
import pl.kielce.tu.travelAgencyApp.model.User;
import pl.kielce.tu.travelAgencyApp.repository.travelOffer.TravelOfferRepository;
import pl.kielce.tu.travelAgencyApp.session.LoginService;
import pl.kielce.tu.travelAgencyApp.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author ciepluchs
 */
public abstract class ViewUtil {

    private static final String NEXT_LINE = "\n";
    private static final String SPACE_DELIMITER = " ";
    private static final int COLUMN_WIDTH = 32;
    private static final int NUMBER_OF_COLUMNS = 3;
    private static final String BACK_SYMBOL = "<-";
    private static final String BACK = "back";
    private static final String QUIT = "quit";

    private ViewUtil() {
    }

    public static void cls() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception E) {
            System.out.println(E);
        }
    }

    public static void displayLoginPrompt(LoginService loginService) {
        java.io.Console console = System.console();
        String username = console.readLine("Personal ID: ");
        String password = new String(console.readPassword("Password: "));
        User user = loginService.login(username, password);
        if (user != null) {
            Session.setUser(user);
        }
    }

    public static void displayMainMenu(TravelOfferRepository travelOfferRepository) {
        List<Action> mainMenuActions = new ArrayList<>();
        mainMenuActions.add(new CreateTravelOffer(travelOfferRepository));
        mainMenuActions.add(new ShowAllTravelOffers(travelOfferRepository));
        mainMenuActions.add(new FindTravelOfferById(travelOfferRepository));
        mainMenuActions.add(new FindTravelOffer(travelOfferRepository));
        ViewUtil.cls();
        List<Action> availableActions = ActionUtil.getAvailableActions(mainMenuActions);
        while (true) {
            System.out.println("############################################# TRAVEL AGENCY  ############################################");
            int i = 0;
            for (Action action : availableActions) {
                System.out.println(++i + ") " + action.getDisplayName());
            }
            System.out.println("Where you want to go: ");
            Scanner scanner = new Scanner(System.in);
            String operation = scanner.nextLine();
            if (QUIT.equals(operation)) {
                return;
            }
            int nextOperation = Integer.parseInt(operation) - 1;
            availableActions.get(nextOperation).execute();
        }
    }

    public static void displaySubMenu(List<Action> availableActions) {
        System.out.println("What you want to do: ");
        int i = 0;
        for (Action action : availableActions) {
            System.out.println(++i + ") " + action.getDisplayName());
        }
        System.out.println("<- back");
    }

    public static boolean isBackOptionSelected(String selectedOption) {
        return BACK_SYMBOL.equals(selectedOption) || BACK.equals(selectedOption);
    }

    public static String getSelectedOption() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    public static String getTable(List<TravelOffer> travelOffers) {
        String tableHeader = getTableHeader();
        String tableContent = getTableContent(travelOffers);
        return tableHeader + "\n" + tableContent;
    }

    private static String getTableContent(List<TravelOffer> travelOffers) {
        String tableContent = StringUtils.EMPTY;
        for (TravelOffer offer : travelOffers) {
            StringBuilder sb = new StringBuilder();
            String destinationCountry = offer.getDestinationCountry();
            String destinationCity = offer.getDestinationCity();
            String cost = String.valueOf(offer.getCostPerPerson());
            sb.append(travelOffers.indexOf(offer) + 1);
            sb.append(SPACE_DELIMITER.repeat(9 - sb.length()));
            sb.append(destinationCountry).append(SPACE_DELIMITER.repeat((COLUMN_WIDTH + 9) - sb.length()));
            sb.append(destinationCity);
            sb.append(SPACE_DELIMITER.repeat((2 * COLUMN_WIDTH + 9) - sb.length()));
            sb.append(cost);
            sb.append(NEXT_LINE);
            tableContent += sb.toString();
        }
        return tableContent;
    }

    private static String getTableHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------|");
        sb.append("-------------------------------|".repeat(NUMBER_OF_COLUMNS) + "\n");
        sb.append("   No.  |");
        sb.append("            COUNTRY            |");
        sb.append("             CITY              |");
        sb.append("      COST (per person)        |\n");
        sb.append("--------|");
        sb.append("-------------------------------|".repeat(NUMBER_OF_COLUMNS) + "\n");
        return sb.toString();
    }

    public static String getDetailedView(TravelOffer travelOffer) {
        StringBuilder sb = new StringBuilder();
        sb.append("Destination country: ").append(travelOffer.getDestinationCountry()).append(NEXT_LINE);
        sb.append("Destination city   : ").append(travelOffer.getDestinationCity()).append(NEXT_LINE);
        sb.append("Cost (per person)  : ").append(travelOffer.getCostPerPerson()).append(NEXT_LINE);
        sb.append("Description        : ").append("Example desc").append(NEXT_LINE);
        sb.append("ID                 : ").append(travelOffer.getId()).append(NEXT_LINE);
        return sb.toString();
    }
}
