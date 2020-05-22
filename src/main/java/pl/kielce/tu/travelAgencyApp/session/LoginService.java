package pl.kielce.tu.travelAgencyApp.session;

import pl.kielce.tu.travelAgencyApp.model.User;

import java.util.Map;

/**
 * @author ciepluchs
 */
public class LoginService {

    Map<String, User> users;

    public LoginService(Map<String, User> users) {
        this.users = users;
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }
}
