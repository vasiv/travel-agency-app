package pl.kielce.tu.travelAgencyApp.actions;


import pl.kielce.tu.travelAgencyApp.model.enumeration.Role;

import java.util.List;

public interface Action {

    void execute();
    String getDisplayName();
    List<Role> getAllowedRoles();
}
