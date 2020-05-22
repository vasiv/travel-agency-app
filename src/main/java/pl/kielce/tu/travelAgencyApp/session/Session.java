package pl.kielce.tu.travelAgencyApp.session;

import pl.kielce.tu.travelAgencyApp.model.User;

/**
 * @author ciepluchs
 */
public class Session {

    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user1) {
        user = user1;
    }
}
