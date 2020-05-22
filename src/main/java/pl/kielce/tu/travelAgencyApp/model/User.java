package pl.kielce.tu.travelAgencyApp.model;


import pl.kielce.tu.travelAgencyApp.model.enumeration.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ciepluchs
 */
public class User implements Serializable, Comparable<User> {

    private static final long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
    private String personalId;
    private String password;
    private List<Role> roles;

    public User(String firstName, String lastName, String personalId, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.password = password;
        roles = new ArrayList<>();
        roles.add(role);
    }

    @Override
    public int compareTo(User o) {
        if (this.personalId.compareTo(o.personalId) == 0) {
            return 0;
        } else {
            return this.lastName.compareTo(lastName);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return personalId.equals(user.personalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalId);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
