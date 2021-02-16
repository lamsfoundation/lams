/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.admin.web.controller;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.AdminConstants;
import org.lamsfoundation.lams.admin.web.form.OrgPasswordChangeForm;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/orgPasswordChange")
public class OrgPasswordChangeController {
    private static Logger log = Logger.getLogger(OrgPasswordChangeController.class);

    @Autowired
    private IEventNotificationService eventNotificationService;
    @Autowired
    private IIntegrationService integrationService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping("/start")
    public String unspecified(@ModelAttribute OrgPasswordChangeForm orgPasswordChangeForm, HttpServletRequest request) {
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	orgPasswordChangeForm.setOrganisationID(organisationID);
	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationID);
	orgPasswordChangeForm.setOrgName(organisation.getName());
	orgPasswordChangeForm.setStaffChange(true);
	orgPasswordChangeForm.setLearnerChange(true);
	orgPasswordChangeForm.setStaffPass(RandomPasswordGenerator.nextPasswordValidated());
	orgPasswordChangeForm.setLearnerPass(RandomPasswordGenerator.nextPasswordValidated());

	return "orgPasswordChange";
    }

    @RequestMapping("/generatePassword")
    @ResponseBody
    public String generatePassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(RandomPasswordGenerator.nextPasswordValidated());
	return null;
    }

    @RequestMapping("/getGridUsers")
    @ResponseBody
    public String getGridUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	String role = WebUtil.readStrParam(request, AttributeNames.PARAM_ROLE);

	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	if (!securityService.isSysadmin(currentUserId, "get grid users for org password change", false)) {
	    String warning = "User " + currentUserId + " is not a sysadmin";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	int page = WebUtil.readIntParam(request, AdminConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, AdminConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, AdminConstants.PARAM_SORD);
	String sortColumn = WebUtil.readStrParam(request, AdminConstants.PARAM_SIDX, true);

	// fetch staff or learners
	List<UserDTO> users = getUsersByRole(organisationID, role.equalsIgnoreCase("staff"), sortColumn, sortOrder);

	// paging
	int totalPages = 1;
	int totalUsers = users.size();
	if (rowLimit < users.size()) {
	    totalPages = new Double(
		    Math.ceil(new Integer(users.size()).doubleValue() / new Integer(rowLimit).doubleValue()))
			    .intValue();
	    int firstRow = (page - 1) * rowLimit;
	    int lastRow = firstRow + rowLimit;

	    if (lastRow > users.size()) {
		users = users.subList(firstRow, users.size());
	    } else {
		users = users.subList(firstRow, lastRow);
	    }
	}

	ObjectNode resultJSON = JsonNodeFactory.instance.objectNode();
	resultJSON.put(AdminConstants.ELEMENT_PAGE, page);
	resultJSON.put(AdminConstants.ELEMENT_TOTAL, totalPages);
	resultJSON.put(AdminConstants.ELEMENT_RECORDS, totalUsers);

	ArrayNode rowsJSON = JsonNodeFactory.instance.arrayNode();
	// build rows for grid
	for (UserDTO user : users) {
	    ObjectNode rowJSON = JsonNodeFactory.instance.objectNode();
	    rowJSON.put(AdminConstants.ELEMENT_ID, user.getUserID());

	    ArrayNode cellJSON = JsonNodeFactory.instance.arrayNode();
	    cellJSON.add(user.getFirstName() + " " + user.getLastName());
	    cellJSON.add(user.getLogin());
	    cellJSON.add(user.getEmail());

	    rowJSON.set(AdminConstants.ELEMENT_CELL, cellJSON);
	    rowsJSON.add(rowJSON);
	}

	resultJSON.set(AdminConstants.ELEMENT_ROWS, rowsJSON);

	response.setContentType("application/json;charset=utf-8");
	return resultJSON.toString();
    }

    @RequestMapping(path = "/changePassword", method = RequestMethod.POST)
    public String changePassword(@ModelAttribute OrgPasswordChangeForm orgPasswordChangeForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	UserDTO userDTO = getUserDTO();
	Integer currentUserId = userDTO.getUserID();
	// security check
	if (!securityService.isSysadmin(currentUserId, "org password change", false)) {
	    String warning = "User " + currentUserId + " is not a sysadmin";
	    log.warn(warning);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, warning);
	    return null;
	}

	Integer organisationID = orgPasswordChangeForm.getOrganisationID();
	Boolean email = orgPasswordChangeForm.isEmail();
	Boolean force = orgPasswordChangeForm.isForce();

	Boolean isStaffChange = orgPasswordChangeForm.getStaffChange();
	Boolean isLearnerChange = orgPasswordChangeForm.getLearnerChange();
	// get data needed for each group
	if (isStaffChange) {
	    String staffString = orgPasswordChangeForm.getExcludedStaff();
	    ArrayNode excludedStaff = StringUtils.isBlank(staffString) ? null : JsonUtil.readArray(staffString);
	    staffString = orgPasswordChangeForm.getIncludedStaff();
	    ArrayNode includedStaff = StringUtils.isBlank(staffString) ? null : JsonUtil.readArray(staffString);

	    String staffPass = orgPasswordChangeForm.getStaffPass();
	    Collection<User> users = getUsersByRole(organisationID, true);
	    Collection<Integer> changedUserIDs = changePassword(staffPass, users, includedStaff, excludedStaff, force);
	    if (email && !changedUserIDs.isEmpty()) {
		notifyOnPasswordChange(changedUserIDs, staffPass);
	    }
	}
	if (isLearnerChange) {
	    String learnersString = orgPasswordChangeForm.getExcludedLearners();
	    ArrayNode excludedLearners = StringUtils.isBlank(learnersString) ? null
		    : JsonUtil.readArray(learnersString);
	    learnersString = orgPasswordChangeForm.getIncludedLearners();
	    ArrayNode includedLearners = StringUtils.isBlank(learnersString) ? null
		    : JsonUtil.readArray(learnersString);

	    String learnerPass = orgPasswordChangeForm.getLearnerPass();
	    Collection<User> users = getUsersByRole(organisationID, false);
	    Collection<Integer> changedUserIDs = changePassword(learnerPass, users, includedLearners, excludedLearners,
		    force);
	    if (email && !changedUserIDs.isEmpty()) {
		notifyOnPasswordChange(changedUserIDs, learnerPass);
	    }
	}

	request.setAttribute("success", true);
	return "orgPasswordChange";
    }

    private void notifyOnPasswordChange(Collection<Integer> userIDs, String password) {
	eventNotificationService.sendMessage(null, userIDs.toArray(new Integer[] {}),
		IEventNotificationService.DELIVERY_METHOD_MAIL,
		messageService.getMessage("admin.org.password.change.email.subject"),
		messageService.getMessage("admin.org.password.change.email.body", new String[] { password }), false);
    }

    private Set<Integer> changePassword(String password, Collection<User> users, ArrayNode includedUsers,
	    ArrayNode excludedUsers, boolean force) {
	if (!ValidationUtil.isPasswordValueValid(password, password)) {
	    // this should have been picked up by JS validator on the page!
	    throw new InvalidParameterException("Password does not pass validation");
	}
	if (includedUsers != null && excludedUsers != null) {
	    throw new IllegalArgumentException("Both included and excluded users arrays must not be passed together");
	}
	Set<Integer> changedUserIDs = new TreeSet<>();
	UserDTO currentUserDTO = getUserDTO();
	User currentUser = (User) userManagementService.findById(User.class, currentUserDTO.getUserID());
	for (User user : users) {
	    if (!ValidationUtil.isPasswordNotUserDetails(password, user)) {
		throw new InvalidParameterException("Password is the same as user details");
	    }
	    // either we work with white list or black list
	    if (includedUsers == null) {
		boolean excluded = false;
		// skip excluded (unchecked on the page) users
		if (excludedUsers != null) {
		    for (int index = 0; index < excludedUsers.size(); index++) {
			Integer excludedUserID = excludedUsers.get(index).asInt();
			if (user.getUserId().equals(excludedUserID)) {
			    excluded = true;
			    break;
			}
		    }
		}
		if (excluded) {
		    continue;
		}
	    } else {
		boolean included = false;
		if (includedUsers != null) {
		    for (int index = 0; index < includedUsers.size(); index++) {
			Integer includedUserID = includedUsers.get(index).asInt();
			if (user.getUserId().equals(includedUserID)) {
			    included = true;
			    break;
			}
		    }
		}
		if (!included) {
		    continue;
		}
	    }

	    // change password
	    if (force) {
		user.setChangePassword(true);
	    }
	    userManagementService.updatePassword(user, password);

	    log.info("Changed password for user ID " + user.getUserId());
	    userManagementService.logPasswordChanged(user, currentUser);
	    changedUserIDs.add(user.getUserId());
	}
	return changedUserIDs;
    }

    /**
     * Get unsorted users for password change
     */
    @SuppressWarnings("unchecked")
    private List<User> getUsersByRole(Integer organisationID, boolean isStaff) {
	Set<User> staff = new HashSet<>();
	staff.addAll(userManagementService.getUsersFromOrganisationByRole(organisationID, Role.AUTHOR, true));
	staff.addAll(userManagementService.getUsersFromOrganisationByRole(organisationID, Role.MONITOR, true));

	Set<User> users = null;
	if (isStaff) {
	    users = staff;
	} else {
	    users = new HashSet<>();
	    users.addAll(userManagementService.getUsersFromOrganisationByRole(organisationID, Role.LEARNER, true));
	    users.removeAll(staff);
	}
	return new LinkedList<>(users);
    }

    /**
     * Gets sorted users for grids
     */
    private List<UserDTO> getUsersByRole(Integer organisationID, boolean isStaff, String sortBy, String sortOrder) {
	List<UserDTO> staff = userManagementService.getAllUsers(organisationID,
		new String[] { Role.AUTHOR, Role.MONITOR }, null, null, sortBy, sortOrder, null);

	List<UserDTO> users = null;
	if (isStaff) {
	    users = staff;
	} else {
	    users = new LinkedList<>();
	    users.addAll(userManagementService.getAllUsers(organisationID, new String[] { Role.LEARNER }, null, null,
		    sortBy, sortOrder, null));
	    users.removeAll(staff);
	}
	return users;
    }

    private UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}