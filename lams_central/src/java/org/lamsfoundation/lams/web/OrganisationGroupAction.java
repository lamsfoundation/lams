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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.integration.dto.ExtGroupDTO;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupComparator;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.web.GroupingAJAXAction;
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
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OrganisationGroupAction extends DispatchAction {

    private static Logger log = Logger.getLogger(OrganisationGroupAction.class);

    private static IUserManagementService userManagementService;
    private static ICoreLearnerService learnerService;
    private static ILessonService lessonService;
    private static ISecurityService securityService;
    private static IIntegrationService integrationService;

    private static final String MAPPING_VIEW_GROUPINGS = "viewGroupings";
    private static final String MAPPING_VIEW_GROUPS = "viewGroups";
    private static final String MAPPING_VIEW_EXT_GROUPS = "viewExtGroups";

    /**
     * Shows course grouping list or redirects to groups if a grouping was already chosen.
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward viewGroupings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);

	Integer userId = getUserDTO().getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Organisation organisation = null;
	if (organisationId == null) {
	    organisation = ((Lesson) getUserManagementService().findById(Lesson.class, lessonId)).getOrganisation();
	    // read organisation ID from lesson
	    organisationId = organisation.getOrganisationId();
	}
	if (organisation == null) {
	    organisation = (Organisation) getUserManagementService().findById(Organisation.class, organisationId);
	}
	// get course groupings from top-leve course
	if (OrganisationType.CLASS_TYPE.equals(organisation.getOrganisationType().getOrganisationTypeId())) {
	    organisation = organisation.getParentOrganisation();
	    organisationId = organisation.getOrganisationId();
	}

	// check if user is allowed to view and edit groupings
	if (!getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_ADMIN, Role.GROUP_MANAGER, Role.MONITOR, Role.AUTHOR },
		"view organisation groupings", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
	    return null;
	}

	List<OrganisationGrouping> orgGroupings = getUserManagementService().findByProperty(OrganisationGrouping.class,
		"organisationId", organisationId);
	Grouping grouping = getLessonGrouping(activityID);

	// show groups page if this is a lesson mode and user have already chosen a grouping or there is no organisation
	// groupings available
	boolean lessonGroupsExist = (grouping != null) && (grouping.getGroups() != null)
		&& !grouping.getGroups().isEmpty() && !isDefaultChosenGrouping(grouping);
	if (lessonGroupsExist || (activityID != null && orgGroupings.isEmpty())) {
	    return viewGroups(mapping, form, request, response);
	}

	// if this grouping is used for branching then it should use groups set in authoring. It will be possible to
	// remove users from the groups, but not delete groups due to the branching relationships.
	boolean isUsedForBranching = (grouping != null) && grouping.isUsedForBranching();
	request.setAttribute(GroupingAJAXAction.PARAM_USED_FOR_BRANCHING, isUsedForBranching);

	if (OrganisationGroupAction.log.isDebugEnabled()) {
	    OrganisationGroupAction.log
		    .debug("Displaying course groupings for user " + userId + " and organisation " + organisationId);
	}
	request.setAttribute(AttributeNames.PARAM_ORGANISATION_ID, organisationId);

	// if it's not a group-based branching and lesson is created using integrations - show groups received from LMS instead of actual LAMS ones
	if (!isUsedForBranching && getIntegrationService().isIntegratedServerGroupFetchingAvailable(lessonId)) {

	    if (lessonId == null) {
		//it's when a learner clicks back button on groups page
		Activity activity = getLearnerService().getActivity(activityID);
		lessonId = getLearnerService().getLessonByActivity(activity).getLessonId();
		request.setAttribute("lessonID", lessonId);
	    }

	    List<ExtGroupDTO> extGroups = getIntegrationService().getExtGroups(lessonId, null);
	    request.setAttribute("extGroups", extGroups);
	    // TODO ? show only with user number >0
	    return mapping.findForward(OrganisationGroupAction.MAPPING_VIEW_EXT_GROUPS);
	}

	boolean isGroupSuperuser = getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_ADMIN)
		|| getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_MANAGER);
	request.setAttribute("canEdit", isGroupSuperuser || (activityID != null));

	Set<OrganisationGroupingDTO> orgGroupingDTOs = new TreeSet<>();
	for (OrganisationGrouping orgGrouping : orgGroupings) {
	    orgGroupingDTOs.add(new OrganisationGroupingDTO(orgGrouping));
	}
	request.setAttribute("groupings", orgGroupingDTOs);

	return mapping.findForward(OrganisationGroupAction.MAPPING_VIEW_GROUPINGS);
    }

    /**
     * View groups of the given grouping.
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward viewGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer userId = getUserDTO().getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Lesson lesson = null;
	if (organisationId == null) {
	    // read organisation ID from lesson
	    lesson = (Lesson) getUserManagementService().findById(Lesson.class, lessonId);
	    organisationId = lesson.getOrganisation().getOrganisationId();
	}

	// check if user is allowed to view and edit groups
	if (!getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_ADMIN, Role.GROUP_MANAGER, Role.MONITOR, Role.AUTHOR },
		"view organisation groups", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
	    return null;
	}

	boolean isGroupSuperuser = getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_ADMIN)
		|| getUserManagementService().isUserInRole(userId, organisationId, Role.GROUP_MANAGER);

	if (OrganisationGroupAction.log.isDebugEnabled()) {
	    OrganisationGroupAction.log
		    .debug("Displaying course groups for user " + userId + " and organisation " + organisationId);
	}
	Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);
	request.setAttribute("canEdit", isGroupSuperuser || (activityId != null));

	JSONObject orgGroupingJSON = new JSONObject();
	orgGroupingJSON.put("organisationId", organisationId);

	Long orgGroupingId = WebUtil.readLongParam(request, "groupingId", true);
	OrganisationGrouping orgGrouping = null;
	// check if course grouping already exists or it is a new one
	if (orgGroupingId != null) {
	    orgGrouping = (OrganisationGrouping) getUserManagementService().findById(OrganisationGrouping.class,
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
	    if (OrganisationGroupAction.log.isDebugEnabled()) {
		OrganisationGroupAction.log.debug("Removing default groups for grouping " + orgGroupingId);
	    }

	    Set<Long> groupIDs = new HashSet<>(lessonGroups.size());
	    for (Group group : lessonGroups) {
		groupIDs.add(group.getGroupId());
	    }
	    for (Long groupId : groupIDs) {
		getLessonService().removeGroup(lessonGrouping, groupId);
	    }

	    lessonGroups = null;
	}

	// if this grouping is used for branching then it should use groups set in authoring. It will be possible to
	// remove users from the groups, but not delete groups due to the branching relationships.
	boolean isUsedForBranching = (lessonGrouping != null) && lessonGrouping.isUsedForBranching();
	request.setAttribute(GroupingAJAXAction.PARAM_USED_FOR_BRANCHING, isUsedForBranching);

	JSONArray orgGroupsJSON = new JSONArray();
	Collection<User> learners = null;

	// if teacher selected groups from integrated server - show them
	if (isExternalGroupsSelected) {

	    if (lesson == null) {
		lesson = (Lesson) getUserManagementService().findById(Lesson.class, lessonId);
	    }
	    learners = lesson.getLessonClass().getLearners();

	    //request all users from selected groups from integrated server
	    List<ExtGroupDTO> extGroups = getIntegrationService().getExtGroups(lessonId, extGroupIds);

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
		    JSONObject groupJSON = new JSONObject();
		    groupJSON.put("name", extGroup.getGroupName());
		    groupJSON.put("groupId", extGroup.getGroupId());
		    if (extGroup.getUsers() != null) {
			for (User groupUser : (List<User>) extGroup.getUsers()) {
			    JSONObject groupUserJSON = WebUtil.userToJSON(groupUser);
			    groupJSON.append("users", groupUserJSON);

			    // remove the user who is already assigned to a group
			    learners.remove(groupUser);
			}
		    }
		    orgGroupsJSON.put(groupJSON);
		}
	    }

	    // if groups haven't been selected yet - show all available groups in organisation
	} else if ((lessonGroups == null) || lessonGroups.isEmpty()) {

	    learners = getUserManagementService().getUsersFromOrganisationByRole(organisationId, Role.LEARNER, true);
	    Set<OrganisationGroup> orgGroups = orgGrouping == null ? null : orgGrouping.getGroups();
	    orgGroupsJSON = getOrgGroupsDetails(orgGroups, learners);

	    // show already selected groups
	} else {

	    if (lesson == null) {
		lesson = (Lesson) getUserManagementService().findById(Lesson.class, lessonId);
	    }
	    learners = lesson.getLessonClass().getLearners();
	    orgGroupsJSON = getLessonGroupsDetails(lessonGroups, learners);
	    request.setAttribute("skipInitialAssigning", true);
	}
	orgGroupingJSON.put("groups", orgGroupsJSON);
	request.setAttribute("grouping", orgGroupingJSON);

	// all the remaining users are unassigned to any group
	JSONArray unassignedUsersJSON = new JSONArray();
	for (User unassignedUser : learners) {
	    JSONObject unassignedUserJSON = WebUtil.userToJSON(unassignedUser);
	    unassignedUsersJSON.put(unassignedUserJSON);
	}
	request.setAttribute("unassignedUsers", unassignedUsersJSON);

	return mapping.findForward(OrganisationGroupAction.MAPPING_VIEW_GROUPS);
    }

    /**
     * Saves a course grouping.
     */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, InvalidParameterException, IOException {
	// check if user is allowed to edit groups
	Integer userId = getUserDTO().getUserID();
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	// check if user is allowed to save grouping
	if (!getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_ADMIN, Role.GROUP_MANAGER }, "save organisation grouping", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a manager or admin in the organisation");
	    return null;
	}

	if (OrganisationGroupAction.log.isDebugEnabled()) {
	    OrganisationGroupAction.log
		    .debug("Saving course groups for user " + userId + " and organisation " + organisationId);
	}

	// deserialize grouping
	JSONObject orgGroupingJSON = new JSONObject(request.getParameter("grouping"));
	// check if already exists
	Long orgGroupingId = orgGroupingJSON.optLong("groupingId");
	if (orgGroupingId == 0L) {
	    orgGroupingId = null;
	}

	// iterate over groups
	List<OrganisationGroup> orgGroups = new LinkedList<>();
	JSONArray orgGroupsJSON = orgGroupingJSON.optJSONArray("groups");
	if (orgGroupsJSON != null) {
	    for (int i = 0; i < orgGroupsJSON.length(); i++) {
		// just overwrite existing groups; they will be updated if already exist
		Set<User> users = new HashSet<>();
		JSONObject orgGroupJSON = orgGroupsJSON.getJSONObject(i);
		JSONArray usersJSON = orgGroupJSON.optJSONArray("users");
		if (usersJSON != null) {
		    // find user objects based on delivered IDs
		    for (int j = 0; j < usersJSON.length(); j++) {
			Integer learnerId = usersJSON.getInt(j);
			User user = (User) getUserManagementService().findById(User.class, learnerId);
			users.add(user);
		    }
		}

		OrganisationGroup orgGroup = new OrganisationGroup();
		Long orgGroupId = orgGroupJSON.optLong("groupId");
		if (orgGroupId > 0) {
		    orgGroup.setGroupId(orgGroupId);
		    orgGroup.setGroupingId(orgGroupingId);
		}
		orgGroup.setName(orgGroupJSON.optString("name", null));
		orgGroup.setUsers(users);

		orgGroups.add(orgGroup);
	    }
	}

	OrganisationGrouping orgGrouping = null;
	if (orgGroupingId != null) {
	    orgGrouping = (OrganisationGrouping) getUserManagementService().findById(OrganisationGrouping.class,
		    orgGroupingId);
	}
	if (orgGrouping == null) {
	    orgGrouping = new OrganisationGrouping();
	    orgGrouping.setOrganisationId(organisationId);
	}
	// update grouping name
	String orgGroupingName = orgGroupingJSON.getString("name");
	orgGrouping.setName(orgGroupingName);

	getUserManagementService().saveOrganisationGrouping(orgGrouping, orgGroups);
	return null;
    }

    /**
     * Deletes course grouping with the given ID.
     *
     * @throws Exception
     */
    public ActionForward removeGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// check if user is allowed to edit groups
	Integer userId = getUserDTO().getUserID();
	int organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	if (!getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_ADMIN, Role.GROUP_MANAGER }, "remove organisation grouping", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a manager or admin in the organisation");
	    return null;
	}

	Long groupingId = WebUtil.readLongParam(request, "groupingId");
	if (OrganisationGroupAction.log.isDebugEnabled()) {
	    OrganisationGroupAction.log.debug(
		    "Removing grouping " + groupingId + " for user " + userId + " and organisation " + organisationId);
	}
	getUserManagementService().deleteById(OrganisationGrouping.class, groupingId);

	return viewGroupings(mapping, form, request, response);
    }

    /**
     * Fetches course and branching so they can get matched by user.
     */
    public ActionForward getGroupsForMapping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	Long orgGroupingId = WebUtil.readLongParam(request, "groupingId");
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

	OrganisationGrouping orgGrouping = (OrganisationGrouping) getUserManagementService()
		.findById(OrganisationGrouping.class, orgGroupingId);
	JSONArray groupsJSON = new JSONArray();
	SortedSet<OrganisationGroup> orgGroups = new TreeSet<>(orgGrouping.getGroups());
	for (OrganisationGroup group : orgGroups) {
	    JSONObject groupJSON = new JSONObject();
	    groupJSON.put("id", group.getGroupId());
	    groupJSON.put("name", group.getName());
	    groupsJSON.put(groupJSON);
	}
	Activity activity = (Activity) getUserManagementService().findById(Activity.class, activityID);
	Grouping grouping = activity.isGroupingActivity() ? ((GroupingActivity) activity).getCreateGrouping()
		: ((BranchingActivity) activity).getGrouping();

	JSONArray branchesJSON = new JSONArray();
	SortedSet<Group> groups = new TreeSet<>(grouping.getGroups());
	for (Group group : groups) {
	    JSONObject groupJSON = new JSONObject();
	    groupJSON.put("id", group.getGroupId());
	    groupJSON.put("name", group.getGroupName());
	    branchesJSON.put(groupJSON);
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("branches", branchesJSON);
	responseJSON.put("groups", groupsJSON);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    /**
     * Stores course groups to branching groups mapping.
     */
    public ActionForward saveGroupMappings(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	JSONArray groupMapping = new JSONArray(request.getParameter("mapping"));
	for (int index = 0; index < groupMapping.length(); index++) {
	    JSONObject entry = groupMapping.getJSONObject(index);
	    Long orgGroupID = entry.getLong("groupID");
	    Long branchingGroupID = entry.getLong("branchID");
	    OrganisationGroup orgGroup = (OrganisationGroup) getUserManagementService()
		    .findById(OrganisationGroup.class, orgGroupID);
	    Group branchingGroup = (Group) getUserManagementService().findById(Group.class, branchingGroupID);
	    // put all users from course group to mapped branching group
	    branchingGroup.getUsers().addAll(orgGroup.getUsers());
	    getUserManagementService().save(branchingGroup);
	}
	response.setContentType("text/plain;charset=utf-8");
	// Javascript waits for this response
	response.getWriter().write("OK");
	return null;
    }

    /**
     * Build JSON objects based on existing lesson-level groups.
     */
    private JSONArray getLessonGroupsDetails(Set<Group> groups, Collection<User> learners) throws JSONException {
	// serialize database group objects into JSON
	JSONArray groupsJSON = new JSONArray();
	if (groups != null) {
	    // sort groups by their name
	    List<Group> groupList = new LinkedList<>(groups);
	    Collections.sort(groupList, new GroupComparator());
	    for (Group group : groupList) {
		JSONObject groupJSON = new JSONObject();
		groupJSON.put("name", group.getGroupName());
		groupJSON.put("groupId", group.getGroupId());
		groupJSON.put("locked", !group.mayBeDeleted());
		if (group.getUsers() != null) {
		    for (User groupUser : group.getUsers()) {
			JSONObject groupUserJSON = WebUtil.userToJSON(groupUser);
			groupJSON.append("users", groupUserJSON);

			// remove the user who is already assigned to a group
			learners.remove(groupUser);
		    }
		}
		groupsJSON.put(groupJSON);
	    }
	}

	return groupsJSON;
    }

    /**
     * Build JSON objects based on existing course-level groups.
     */
    private JSONArray getOrgGroupsDetails(Set<OrganisationGroup> groups, Collection<User> learners)
	    throws JSONException {

	final Comparator<OrganisationGroup> ORG_GROUP_COMPARATOR = new Comparator<OrganisationGroup>() {
	    @Override
	    public int compare(OrganisationGroup o1, OrganisationGroup o2) {
		String grp1Name = o1 != null ? o1.getName() : "";
		String grp2Name = o2 != null ? o2.getName() : "";

		AlphanumComparator comparator = new AlphanumComparator();
		return comparator.compare(grp1Name, grp2Name);
	    }
	};

	// serialize database group objects into JSON
	JSONArray groupsJSON = new JSONArray();
	if (groups != null) {
	    // sort groups by their name
	    List<OrganisationGroup> groupList = new LinkedList<>(groups);
	    Collections.sort(groupList, ORG_GROUP_COMPARATOR);

	    for (OrganisationGroup group : groupList) {
		JSONObject groupJSON = new JSONObject();
		groupJSON.put("name", group.getName());
		groupJSON.put("groupId", group.getGroupId());
		for (User groupUser : group.getUsers()) {
		    JSONObject groupUserJSON = WebUtil.userToJSON(groupUser);
		    groupJSON.append("users", groupUserJSON);

		    // remove the user who is already assigned to a group
		    learners.remove(groupUser);
		}

		groupsJSON.put(groupJSON);
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
	    Activity activity = (Activity) getUserManagementService().findById(Activity.class, activityID);
	    Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		    : ((GroupingActivity) getUserManagementService().findById(GroupingActivity.class, activityID))
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

    private IUserManagementService getUserManagementService() {
	if (OrganisationGroupAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OrganisationGroupAction.userManagementService = (IUserManagementService) ctx
		    .getBean("userManagementService");
	}
	return OrganisationGroupAction.userManagementService;
    }

    private ICoreLearnerService getLearnerService() {
	if (OrganisationGroupAction.learnerService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OrganisationGroupAction.learnerService = (ICoreLearnerService) ctx.getBean("learnerService");
	}
	return OrganisationGroupAction.learnerService;
    }

    private ILessonService getLessonService() {
	if (OrganisationGroupAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OrganisationGroupAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return OrganisationGroupAction.lessonService;
    }

    private ISecurityService getSecurityService() {
	if (OrganisationGroupAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OrganisationGroupAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return OrganisationGroupAction.securityService;
    }

    private IIntegrationService getIntegrationService() {
	if (OrganisationGroupAction.integrationService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OrganisationGroupAction.integrationService = (IIntegrationService) ctx.getBean("integrationService");
	}
	return OrganisationGroupAction.integrationService;
    }
}