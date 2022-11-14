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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.integration.UserInfoValidationException;
import org.lamsfoundation.lams.integration.dto.ExtGroupDTO;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.integration.util.GroupInfoFetchException;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupComparator;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationGroupingDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.AlphanumComparator;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/grouping")
public class GroupingController {
    private static Logger log = Logger.getLogger(GroupingController.class);

    private static final String PARAM_USED_FOR_BRANCHING = "usedForBranching";

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILearnerService learnerService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IIntegrationService integrationService;

    /**
     * Shows course grouping list or redirects to groups if a grouping was already chosen.
     *
     * @throws Exception
     */
    @RequestMapping("/viewGroupings")
    @SuppressWarnings("unchecked")
    public String viewGroupings(HttpServletRequest request, HttpServletResponse response)
	    throws GroupInfoFetchException, UserInfoValidationException, IOException {
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);

	Integer userId = getUserDTO().getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Organisation organisation = null;
	if (organisationId == null) {
	    organisation = ((Lesson) userManagementService.findById(Lesson.class, lessonId)).getOrganisation();
	    // read organisation ID from lesson
	    organisationId = organisation.getOrganisationId();
	}
	if (organisation == null) {
	    organisation = (Organisation) userManagementService.findById(Organisation.class, organisationId);
	}
	// get course groupings from top-leve course
	if (OrganisationType.CLASS_TYPE.equals(organisation.getOrganisationType().getOrganisationTypeId())) {
	    organisation = organisation.getParentOrganisation();
	    organisationId = organisation.getOrganisationId();
	}

	// check if user is allowed to view and edit groupings
	if (!securityService.hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.AUTHOR }, "view organisation groupings")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
	    return null;
	}

	List<OrganisationGrouping> orgGroupings = userManagementService.findByProperty(OrganisationGrouping.class,
		"organisationId", organisationId);
	Grouping grouping = getLessonGrouping(activityID);

	// show groups page if this is a lesson mode and user have already chosen a grouping or there is no organisation
	// groupings available
	boolean lessonGroupsExist = (grouping != null) && (grouping.getGroups() != null)
		&& !grouping.getGroups().isEmpty() && !isDefaultChosenGrouping(grouping);
	if (lessonGroupsExist || (activityID != null && orgGroupings.isEmpty())) {
	    return viewGroups(request, response, organisationId);
	}

	// if this grouping is used for branching then it should use groups set in authoring. It will be possible to
	// remove users from the groups, but not delete groups due to the branching relationships.
	boolean isUsedForBranching = (grouping != null) && grouping.isUsedForBranching();
	request.setAttribute(PARAM_USED_FOR_BRANCHING, isUsedForBranching);

	if (log.isDebugEnabled()) {
	    log.debug("Displaying course groupings for user " + userId + " and organisation " + organisationId);
	}
	request.setAttribute(AttributeNames.PARAM_ORGANISATION_ID, organisationId);

	// if it's not a group-based branching and lesson is created using integrations - show groups received from LMS instead of actual LAMS ones
	if (!isUsedForBranching && integrationService.isIntegratedServerGroupFetchingAvailable(lessonId)) {

	    if (lessonId == null) {
		//it's when a learner clicks back button on groups page
		Activity activity = learnerService.getActivity(activityID);
		lessonId = learnerService.getLessonByActivity(activity).getLessonId();
		request.setAttribute("lessonID", lessonId);
	    }

	    List<ExtGroupDTO> extGroups = integrationService.getExtGroups(lessonId, null);
	    request.setAttribute("extGroups", extGroups);
	    // TODO ? show only with user number >0
	    return "extGroups";
	}

	boolean isGroupSuperuser = userManagementService.isUserInRole(userId, organisationId, Role.GROUP_MANAGER);
	request.setAttribute("canEdit", isGroupSuperuser || (activityID != null));

	Set<OrganisationGroupingDTO> orgGroupingDTOs = new TreeSet<>();
	for (OrganisationGrouping orgGrouping : orgGroupings) {
	    orgGroupingDTOs.add(new OrganisationGroupingDTO(orgGrouping));
	}
	request.setAttribute("groupings", orgGroupingDTOs);

	return "orgGrouping";
    }

    /**
     * View groups of the given grouping.
     */
    @RequestMapping("/viewGroups")
    @SuppressWarnings("unchecked")
    public String viewGroups(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam(value = "organisationID", required = false) Integer organisationId)
	    throws IOException, GroupInfoFetchException, UserInfoValidationException {
	Integer userId = getUserDTO().getUserID();
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Lesson lesson = null;
	if (organisationId == null) {
	    // read organisation ID from lesson
	    lesson = (Lesson) userManagementService.findById(Lesson.class, lessonId);
	    organisationId = lesson.getOrganisation().getOrganisationId();
	}
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId); // Needed for the download spreadsheet call.

	// check if user is allowed to view and edit groups
	if (!securityService.hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.AUTHOR }, "view organisation groups")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
	    return null;
	}

	boolean isGroupSuperuser = userManagementService.isUserInRole(userId, organisationId, Role.GROUP_MANAGER);

	if (log.isDebugEnabled()) {
	    log.debug("Displaying course groups for user " + userId + " and organisation " + organisationId);
	}
	Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);

	if (activityId == null) {
	    request.setAttribute("canEdit", isGroupSuperuser);
	} else {
	    Activity activity = (Activity) userManagementService.findById(Activity.class, activityId);
	    request.setAttribute(AttributeNames.PARAM_TITLE, activity.getTitle());
	    request.setAttribute("description", activity.getDescription());
	    request.setAttribute("canEdit", true);
	}

	ObjectNode orgGroupingJSON = JsonNodeFactory.instance.objectNode();
	orgGroupingJSON.put("organisationId", organisationId);

	Long orgGroupingId = WebUtil.readLongParam(request, "groupingId", true);
	OrganisationGrouping orgGrouping = null;
	// check if course grouping already exists or it is a new one
	if (orgGroupingId != null) {
	    orgGrouping = (OrganisationGrouping) userManagementService.findById(OrganisationGrouping.class,
		    orgGroupingId);
	    if (orgGrouping != null) {
		orgGroupingJSON.put("groupingId", orgGroupingId);
		orgGroupingJSON.put("name", orgGrouping.getName());
	    }
	}

	//selected groups from integrated server
	String[] extGroupIds = request.getParameterValues("extGroupIds");
	boolean isExternalGroupsSelected = extGroupIds != null && extGroupIds.length > 0;

	// check if any groups already exist in this grouping
	Grouping lessonGrouping = getLessonGrouping(activityId);
	Set<Group> lessonGroups = lessonGrouping == null ? null : lessonGrouping.getGroups();
	if ((activityId != null) && (lessonGrouping != null) && (isExternalGroupsSelected || (orgGroupingId != null))
		&& isDefaultChosenGrouping(lessonGrouping)) {
	    if (log.isDebugEnabled()) {
		log.debug("Removing default groups for grouping " + orgGroupingId);
	    }

	    Set<Long> groupIDs = new HashSet<>(lessonGroups.size());
	    for (Group group : lessonGroups) {
		groupIDs.add(group.getGroupId());
	    }
	    for (Long groupId : groupIDs) {
		lessonService.removeGroup(lessonGrouping, groupId);
	    }

	    lessonGroups = null;
	}

	// if this grouping is used for branching then it should use groups set in authoring. It will be possible to
	// remove users from the groups, but not delete groups due to the branching relationships.
	boolean isUsedForBranching = (lessonGrouping != null) && lessonGrouping.isUsedForBranching();
	request.setAttribute(PARAM_USED_FOR_BRANCHING, isUsedForBranching);

	ArrayNode orgGroupsJSON = JsonNodeFactory.instance.arrayNode();
	Collection<User> learners = null;

	// if teacher selected groups from integrated server - show them
	if (isExternalGroupsSelected) {

	    if (lesson == null) {
		lesson = (Lesson) userManagementService.findById(Lesson.class, lessonId);
	    }
	    learners = lesson.getLessonClass().getLearners();

	    //request all users from selected groups from integrated server
	    List<ExtGroupDTO> extGroups = integrationService.getExtGroups(lessonId, extGroupIds);

	    // serialize database group objects into JSON
	    if (extGroups != null) {

		//if there are duplicate users - put them into unassigned column
		List<User> allDuplicates = new ArrayList<>();
		for (ExtGroupDTO groupA : extGroups) {
		    for (ExtGroupDTO groupB : extGroups) {
			List<User> usersA = groupA.getUsers();
			List<User> usersB = groupB.getUsers();

			//proceed for non empty and different groups
			if ((usersA != null) && (usersB != null) && !groupA.getGroupId().equals(groupB.getGroupId())) {

			    Collection<User> duplicates = CollectionUtils.intersection(usersA, usersB);
			    allDuplicates.addAll(duplicates);

			    usersA.removeAll(duplicates);
			    usersB.removeAll(duplicates);
			}

		    }
		}

		// sort groups by their name
		Collections.sort(extGroups);
		for (ExtGroupDTO extGroup : extGroups) {
		    ObjectNode groupJSON = JsonNodeFactory.instance.objectNode();
		    groupJSON.put("name", extGroup.getGroupName());
		    groupJSON.put("groupId", extGroup.getGroupId());
		    if (extGroup.getUsers() != null) {
			for (User groupUser : (List<User>) extGroup.getUsers()) {
			    ObjectNode groupUserJSON = WebUtil.userToJSON(groupUser);
			    groupJSON.withArray("users").add(groupUserJSON);

			    // remove the user who is already assigned to a group
			    learners.remove(groupUser);
			}
		    }
		    orgGroupsJSON.add(groupJSON);
		}
	    }

	    // if groups haven't been selected yet - show all available groups in organisation
	} else if ((lessonGroups == null) || lessonGroups.isEmpty()) {

	    learners = userManagementService.getUsersFromOrganisationByRole(organisationId, Role.LEARNER, true);
	    Set<OrganisationGroup> orgGroups = orgGrouping == null ? null : orgGrouping.getGroups();
	    orgGroupsJSON = getOrgGroupsDetails(orgGroups, learners);

	    // show already selected groups
	} else {

	    if (lesson == null) {
		lesson = (Lesson) userManagementService.findById(Lesson.class, lessonId);
	    }
	    learners = lesson.getLessonClass().getLearners();
	    orgGroupsJSON = getLessonGroupsDetails(lessonGroups, learners);
	    request.setAttribute("skipInitialAssigning", true);
	}
	orgGroupingJSON.set("groups", orgGroupsJSON);
	request.setAttribute("grouping", orgGroupingJSON);

	// all the remaining users are unassigned to any group
	ArrayNode unassignedUsersJSON = JsonNodeFactory.instance.arrayNode();
	for (User unassignedUser : learners) {
	    ObjectNode unassignedUserJSON = WebUtil.userToJSON(unassignedUser);
	    unassignedUsersJSON.add(unassignedUserJSON);
	}
	request.setAttribute("unassignedUsers", unassignedUsersJSON);

	return "orgGroup";
    }

    /**
     * Saves a course grouping.
     */
    @ResponseBody
    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public void save(HttpServletRequest request, HttpServletResponse response)
	    throws InvalidParameterException, IOException {
	// check if user is allowed to edit groups
	Integer userId = getUserDTO().getUserID();
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	// check if user is allowed to save grouping
	if (!securityService.hasOrgRole(organisationId, userId, new String[] { Role.GROUP_MANAGER },
		"save organisation grouping")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a manager or admin in the organisation");
	}

	if (log.isDebugEnabled()) {
	    log.debug("Saving course groups for user " + userId + " and organisation " + organisationId);
	}

	// deserialize grouping
	ObjectNode orgGroupingJSON = new ObjectMapper().readValue(request.getParameter("grouping"), ObjectNode.class);
	// check if already exists
	Long orgGroupingId = JsonUtil.optLong(orgGroupingJSON, "groupingId");

	// iterate over groups
	List<OrganisationGroup> orgGroups = new LinkedList<>();
	ArrayNode orgGroupsJSON = JsonUtil.optArray(orgGroupingJSON, "groups");
	if (orgGroupsJSON != null) {
	    for (JsonNode orgGroupNode : orgGroupsJSON) {
		// just overwrite existing groups; they will be updated if already exist
		Set<User> users = new HashSet<>();
		ObjectNode orgGroupJSON = (ObjectNode) orgGroupNode;
		ArrayNode usersJSON = JsonUtil.optArray(orgGroupJSON, "users");
		if (usersJSON != null) {
		    // find user objects based on delivered IDs
		    for (JsonNode learnerId : usersJSON) {
			User user = (User) userManagementService.findById(User.class, learnerId.asInt());
			users.add(user);
		    }
		}

		OrganisationGroup orgGroup = new OrganisationGroup();
		Long orgGroupId = JsonUtil.optLong(orgGroupJSON, "groupId");
		if (orgGroupId != null) {
		    orgGroup.setGroupId(orgGroupId);
		    orgGroup.setGroupingId(orgGroupingId);
		}
		orgGroup.setName(JsonUtil.optString(orgGroupJSON, "name"));
		orgGroup.setUsers(users);

		orgGroups.add(orgGroup);
	    }
	}

	OrganisationGrouping orgGrouping = null;
	if (orgGroupingId != null) {
	    orgGrouping = (OrganisationGrouping) userManagementService.findById(OrganisationGrouping.class,
		    orgGroupingId);
	}
	if (orgGrouping == null) {
	    orgGrouping = new OrganisationGrouping();
	    orgGrouping.setOrganisationId(organisationId);
	}
	// update grouping name
	String orgGroupingName = JsonUtil.optString(orgGroupingJSON, "name");
	orgGrouping.setName(orgGroupingName);

	userManagementService.saveOrganisationGrouping(orgGrouping, orgGroups);
    }

    /**
     * Deletes course grouping with the given ID.
     */
    @RequestMapping(path = "/removeGrouping", method = RequestMethod.POST)
    public String removeGrouping(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, GroupInfoFetchException, UserInfoValidationException {
	// check if user is allowed to edit groups
	Integer userId = getUserDTO().getUserID();
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	if (!securityService.hasOrgRole(organisationId, userId, new String[] { Role.GROUP_MANAGER },
		"remove organisation grouping")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a manager or admin in the organisation");
	    return null;
	}

	Long groupingId = WebUtil.readLongParam(request, "groupingId");
	if (log.isDebugEnabled()) {
	    log.debug(
		    "Removing grouping " + groupingId + " for user " + userId + " and organisation " + organisationId);
	}
	userManagementService.deleteById(OrganisationGrouping.class, groupingId);

	return viewGroupings(request, response);
    }

    /**
     * Fetches course and branching so they can get matched by user.
     */
    @ResponseBody
    @RequestMapping("/getGroupsForMapping")
    public void getGroupsForMapping(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long orgGroupingId = WebUtil.readLongParam(request, "groupingId");
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

	OrganisationGrouping orgGrouping = (OrganisationGrouping) userManagementService
		.findById(OrganisationGrouping.class, orgGroupingId);
	ArrayNode groupsJSON = JsonNodeFactory.instance.arrayNode();
	SortedSet<OrganisationGroup> orgGroups = new TreeSet<>(orgGrouping.getGroups());
	for (OrganisationGroup group : orgGroups) {
	    ObjectNode groupJSON = JsonNodeFactory.instance.objectNode();
	    groupJSON.put("id", group.getGroupId());
	    groupJSON.put("name", group.getName());
	    groupsJSON.add(groupJSON);
	}
	Activity activity = (Activity) userManagementService.findById(Activity.class, activityID);
	Grouping grouping = activity.isGroupingActivity() ? ((GroupingActivity) activity).getCreateGrouping()
		: ((BranchingActivity) activity).getGrouping();

	ArrayNode branchesJSON = JsonNodeFactory.instance.arrayNode();
	SortedSet<Group> groups = new TreeSet<>(grouping.getGroups());
	for (Group group : groups) {
	    ObjectNode groupJSON = JsonNodeFactory.instance.objectNode();
	    groupJSON.put("id", group.getGroupId());
	    groupJSON.put("name", group.getGroupName());
	    branchesJSON.add(groupJSON);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.set("branches", branchesJSON);
	responseJSON.set("groups", groupsJSON);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
    }

    /**
     * Stores course groups to branching groups mapping.
     */
    @ResponseBody
    @RequestMapping(path = "/saveGroupMappings", method = RequestMethod.POST)
    public void saveGroupMappings(HttpServletRequest request, HttpServletResponse response) throws IOException {
	ArrayNode groupMapping = JsonUtil.readArray(request.getParameter("mapping"));
	for (JsonNode entryNode : groupMapping) {
	    ObjectNode entry = (ObjectNode) entryNode;
	    Long orgGroupID = JsonUtil.optLong(entry, "groupID");
	    Long branchingGroupID = JsonUtil.optLong(entry, "branchID");
	    OrganisationGroup orgGroup = (OrganisationGroup) userManagementService.findById(OrganisationGroup.class,
		    orgGroupID);
	    Group branchingGroup = (Group) userManagementService.findById(Group.class, branchingGroupID);
	    // put all users from course group to mapped branching group
	    branchingGroup.getUsers().addAll(orgGroup.getUsers());
	    userManagementService.save(branchingGroup);
	}
	response.setContentType("text/plain;charset=utf-8");
	// Javascript waits for this response
	response.getWriter().write("OK");
    }

    /**
     * Build JSON objects based on existing lesson-level groups.
     */
    private ArrayNode getLessonGroupsDetails(Set<Group> groups, Collection<User> learners) {
	// serialize database group objects into JSON
	ArrayNode groupsJSON = JsonNodeFactory.instance.arrayNode();
	if (groups != null) {
	    // sort groups by their name
	    List<Group> groupList = new LinkedList<>(groups);
	    Collections.sort(groupList, new GroupComparator());
	    for (Group group : groupList) {
		ObjectNode groupJSON = JsonNodeFactory.instance.objectNode();
		groupJSON.put("name", group.getGroupName());
		groupJSON.put("groupId", group.getGroupId());
		groupJSON.put("locked", !group.mayBeDeleted());
		if (group.getUsers() != null) {
		    for (User groupUser : group.getUsers()) {
			ObjectNode groupUserJSON = WebUtil.userToJSON(groupUser);
			groupJSON.withArray("users").add(groupUserJSON);

			// remove the user who is already assigned to a group
			learners.remove(groupUser);
		    }
		}
		groupsJSON.add(groupJSON);
	    }
	}

	return groupsJSON;
    }

    /**
     * Build JSON objects based on existing course-level groups.
     */
    private ArrayNode getOrgGroupsDetails(Set<OrganisationGroup> groups, Collection<User> learners) {

	final Comparator<OrganisationGroup> ORG_GROUP_COMPARATOR = new Comparator<>() {
	    @Override
	    public int compare(OrganisationGroup o1, OrganisationGroup o2) {
		String grp1Name = o1 != null ? o1.getName() : "";
		String grp2Name = o2 != null ? o2.getName() : "";

		AlphanumComparator comparator = new AlphanumComparator();
		return comparator.compare(grp1Name, grp2Name);
	    }
	};

	// serialize database group objects into JSON
	ArrayNode groupsJSON = JsonNodeFactory.instance.arrayNode();
	if (groups != null) {
	    // sort groups by their name
	    List<OrganisationGroup> groupList = new LinkedList<>(groups);
	    Collections.sort(groupList, ORG_GROUP_COMPARATOR);

	    for (OrganisationGroup group : groupList) {
		ObjectNode groupJSON = JsonNodeFactory.instance.objectNode();
		groupJSON.put("name", group.getName());
		groupJSON.put("groupId", group.getGroupId());
		for (User groupUser : group.getUsers()) {
		    ObjectNode groupUserJSON = WebUtil.userToJSON(groupUser);
		    groupJSON.withArray("users").add(groupUserJSON);

		    // remove the user who is already assigned to a group
		    learners.remove(groupUser);
		}

		groupsJSON.add(groupJSON);
	    }
	}

	return groupsJSON;
    }

    /**
     * Checks if lesson-level groups exist for the given activity.
     */
    private Grouping getLessonGrouping(Long activityID) {
	if (activityID != null) {
	    // we need to fetch real objects instead of stubs/proxies
	    Activity activity = (Activity) userManagementService.findById(Activity.class, activityID);
	    Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		    : ((GroupingActivity) userManagementService.findById(GroupingActivity.class, activityID))
			    .getCreateGrouping();

	    return grouping;
	}

	return null;
    }

    /**
     * Check if the given groups are default for chosen grouping. There is actually no good way to detect this, but even
     * if a custom grouping is mistaken for the default one, it should bring little harm.
     */
    private boolean isDefaultChosenGrouping(Grouping grouping) {
	Set<Group> groups = grouping.getGroups();
	for (Group group : groups) {
	    if (!group.getUsers().isEmpty()) {
		return false;
	    }
	}
	if (groups == null || (grouping.getMaxNumberOfGroups() != null
		&& !grouping.getMaxNumberOfGroups().equals(groups.size()))) {
	    return false;
	}
	return true;
    }

    private UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}