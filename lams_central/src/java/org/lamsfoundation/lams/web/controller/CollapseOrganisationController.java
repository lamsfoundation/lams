package org.lamsfoundation.lams.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationCollapsed;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Stores user collapsed organisation.
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("collapseOrganisation")
public class CollapseOrganisationController {

    @Autowired
    private IUserManagementService userManagementService;

    @RequestMapping("")
    @ResponseStatus(HttpStatus.OK)
    public void execute(HttpServletRequest request, @RequestParam Integer orgId, @RequestParam boolean collapsed) {
	User user = userManagementService.getUserByLogin(request.getRemoteUser());
	UserOrganisationCollapsed userOrganisationCollapsed = userManagementService
		.getUserOrganisationCollapsed(user.getUserId(), orgId);

	// insert or update userorg's collapsed status
	if (userOrganisationCollapsed == null) {
	    // new row in lams_user_organisation_collapsed
	    userOrganisationCollapsed = new UserOrganisationCollapsed();
	    Organisation organisation = userManagementService.getOrganisationById(orgId);
	    userOrganisationCollapsed.setOrganisation(organisation);
	    userOrganisationCollapsed.setUser(user);
	    userOrganisationCollapsed.setCollapsed(collapsed);

	} else {
	    userOrganisationCollapsed.setCollapsed(collapsed);
	}
	userManagementService.save(userOrganisationCollapsed);
    }
}