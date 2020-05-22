package pl.kielce.tu.travelAgencyApp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.slf4j.LoggerFactory;
import pl.kielce.tu.travelAgencyApp.model.User;
import pl.kielce.tu.travelAgencyApp.model.enumeration.Role;
import pl.kielce.tu.travelAgencyApp.repository.travelOffer.CouchDbTravelOfferRepository;
import pl.kielce.tu.travelAgencyApp.repository.travelOffer.MongoDbTravelOfferRepository;
import pl.kielce.tu.travelAgencyApp.repository.travelOffer.TravelOfferRepository;
import pl.kielce.tu.travelAgencyApp.session.LoginService;
import pl.kielce.tu.travelAgencyApp.session.Session;
import pl.kielce.tu.travelAgencyApp.util.PropertiesUtil;
import pl.kielce.tu.travelAgencyApp.util.ViewUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ciepluchs
 */
public class App {

    private static final String STORE = "store";
    private static final String MONGODB = "mongoDb";
    private static final String COUCHDB = "couchDb";
    private static final String ORG_MONGODB_DRIVER = "org.mongodb.driver";
    private static final String MONGODB_HOST_PROPERTY = "store.mongodb.host";
    private static final String MONGODB_PORT_PROPERTY = "store.mongodb.port";
    private static final String MONGODB_DATABASE_PROPERTY = "store.mongodb.database";

    public static void main(String[] args) {
        TravelOfferRepository travelOfferRepository = initializeTravelOfferRepository();
        if (travelOfferRepository == null) {
            System.exit(0);
        }
        LoginService loginService = new LoginService(getDemoUsers());
        ViewUtil.cls();
        while (true) {
            System.out.println("################################################ LOGIN  #################################################");
            ViewUtil.displayLoginPrompt(loginService);
            if (Session.getUser() == null) {
                ViewUtil.cls();
                System.out.println("Wrong personal ID / password!");
                continue;
            }
            ViewUtil.displayMainMenu(travelOfferRepository);
            ViewUtil.cls();
        }
    }

    private static Map<String, User> getDemoUsers() {
        Map<String, User> users = new HashMap<>();
        User customer = new User("Adam", "Nowak", "12345678912", "pass123", Role.CUSTOMER);
        User travelAgent = new User("Jan", "Kowalski", "21987654321", "password123", Role.TRAVEL_AGENT);
        users.put(customer.getPersonalId(), customer);
        users.put(travelAgent.getPersonalId(), travelAgent);
        return users;
    }

    private static TravelOfferRepository initializeTravelOfferRepository() {
        String store = PropertiesUtil.getProperty(STORE);
        if (MONGODB.equals(store)) {
            String host = PropertiesUtil.getProperty(MONGODB_HOST_PROPERTY);
            String port = PropertiesUtil.getProperty(MONGODB_PORT_PROPERTY);
            MongoClient mongoClient = new MongoClient(host, Integer.valueOf(port));
            String dbName = PropertiesUtil.getProperty(MONGODB_DATABASE_PROPERTY);
            MongoDatabase db = mongoClient.getDatabase(dbName);
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            Logger rootLogger = loggerContext.getLogger(ORG_MONGODB_DRIVER);
            rootLogger.setLevel(Level.OFF);
            return new MongoDbTravelOfferRepository(db);
        } else if (COUCHDB.equals(store)) {
            return new CouchDbTravelOfferRepository();
        } else {
            System.err.println("Incorrect configuration!");
            return null;
        }
    }
}
