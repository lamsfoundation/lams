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

package org.lamsfoundation.lams.tool.whiteboard.web.controller;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.tool.whiteboard.WhiteboardConstants;
import org.lamsfoundation.lams.tool.whiteboard.model.Whiteboard;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardUser;
import org.lamsfoundation.lams.tool.whiteboard.service.IWhiteboardService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    public static Logger log = Logger.getLogger(MonitoringController.class);

    public static final int LEARNER_MARKS_SORTING_FIRST_NAME_ASC = 0;
    public static final int LEARNER_MARKS_SORTING_FIRST_NAME_DESC = 1;
    public static final int LEARNER_MARKS_SORTING_LAST_NAME_ASC = 2;
    public static final int LEARNER_MARKS_SORTING_LAST_NAME_DESC = 3;

    private static final Comparator<User> USER_NAME_COMPARATOR = Comparator.comparing(User::getFirstName)
	    .thenComparing(User::getLastName).thenComparing(User::getLogin);

    @Autowired
    private IWhiteboardService whiteboardService;

    @Autowired
    private IUserManagementService userManagementService;

    @Autowired
    private IGradebookService gradebookService;

    @Autowired
    @Qualifier("lamsCoreToolService")
    private ILamsCoreToolService toolService;

    @Autowired
    private ISecurityService securityService;

    @Autowired
    @Qualifier("whiteboardMessageService")
    private MessageService messageService;

    /*
     * @RequestMapping("/summary")
     * private String summary(HttpServletRequest request, HttpServletResponse response) throws EtherpadException {
     * // initial Session Map
     * SessionMap<String, Object> sessionMap = new SessionMap<>();
     * request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
     * request.setAttribute(WhiteboardConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
     * // save contentFolderID into session
     * sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
     * WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID, true));
     * 
     * Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
     * List<SessionDTO> groupList = whiteboardService.getSummary(contentId, null);
     * boolean hasFaultySession = false;
     * int attemptedLearnersNumber = 0;
     * for (SessionDTO group : groupList) {
     * hasFaultySession |= group.isSessionFaulty();
     * attemptedLearnersNumber += group.getNumberOfLearners();
     * }
     * 
     * Whiteboard whiteboard = whiteboardService.getWhiteboardByContentId(contentId);
     * 
     * // Create reflectList if reflection is enabled.
     * if (whiteboard.isReflectOnActivity()) {
     * List<ReflectDTO> relectList = whiteboardService.getReflectList(contentId);
     * sessionMap.put(WhiteboardConstants.ATTR_REFLECT_LIST, relectList);
     * }
     * 
     * // cache into sessionMap
     * sessionMap.put(WhiteboardConstants.ATTR_SUMMARY_LIST, groupList);
     * sessionMap.put(WhiteboardConstants.ATTR_HAS_FAULTY_SESSION, hasFaultySession);
     * sessionMap.put(WhiteboardConstants.PAGE_EDITABLE, whiteboard.isContentInUse());
     * sessionMap.put(WhiteboardConstants.ATTR_WHITEBOARD, whiteboard);
     * sessionMap.put(WhiteboardConstants.ATTR_TOOL_CONTENT_ID, contentId);
     * sessionMap.put(WhiteboardConstants.ATTR_IS_GROUPED_ACTIVITY, whiteboardService.isGroupedActivity(contentId));
     * request.setAttribute("attemptedLearnersNumber", attemptedLearnersNumber);
     * 
     * // get the API key from the config table and add it to the session
     * String etherpadServerUrl = Configuration.get(ConfigurationKeys.ETHERPAD_SERVER_URL);
     * String etherpadApiKey = Configuration.get(ConfigurationKeys.ETHERPAD_API_KEY);
     * if (StringUtils.isBlank(etherpadServerUrl) || StringUtils.isBlank(etherpadApiKey)) {
     * return "pages/learning/notconfigured";
     * }
     * request.setAttribute(WhiteboardConstants.KEY_ETHERPAD_SERVER_URL, etherpadServerUrl);
     * 
     * HttpSession ss = SessionManager.getSession();
     * // get back login user DTO
     * UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
     * 
     * //no need to store cookie if there are no sessions created yet
     * if (!groupList.isEmpty()) {
     * // add new sessionID cookie in order to access pad
     * Cookie etherpadSessionCookie = whiteboardService.createEtherpadCookieForMonitor(user, contentId);
     * response.addCookie(etherpadSessionCookie);
     * }
     * 
     * return "pages/monitoring/monitoring";
     * }
     */

    @RequestMapping("/getLearnerMarks")
    @ResponseBody
    private String getLearnerMarks(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSortFirstName = WebUtil.readIntParam(request, "column[0]", true);
	Integer isSortLastName = WebUtil.readIntParam(request, "column[1]", true);

	// identify sorting type
	int sorting = LEARNER_MARKS_SORTING_FIRST_NAME_ASC;
	if (isSortFirstName != null) {
	    sorting = isSortFirstName.equals(1) ? LEARNER_MARKS_SORTING_FIRST_NAME_DESC
		    : LEARNER_MARKS_SORTING_FIRST_NAME_ASC;
	} else if (isSortLastName != null) {
	    sorting = isSortLastName.equals(1) ? LEARNER_MARKS_SORTING_LAST_NAME_DESC
		    : LEARNER_MARKS_SORTING_LAST_NAME_ASC;
	}

	// get all session users and sort them according to the parameter from tablesorter
	List<WhiteboardUser> users = whiteboardService.getUsersBySession(toolSessionId).stream()
		.sorted(Comparator.comparing(sorting <= 1 ? WhiteboardUser::getFirstName : WhiteboardUser::getLastName))
		.collect(Collectors.toList());

	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	if (!users.isEmpty()) {
	    // reverse if sorting is descending
	    if (sorting == LEARNER_MARKS_SORTING_FIRST_NAME_DESC || sorting == LEARNER_MARKS_SORTING_LAST_NAME_DESC) {
		Collections.reverse(users);
	    }

	    // paging
	    int endIndex = (page + 1) * size;
	    users = users.subList(page * size, users.size() > endIndex ? endIndex : users.size());

	    ArrayNode rows = JsonNodeFactory.instance.arrayNode();

	    responsedata.put("total_rows", users.size());

	    ToolSession toolSession = toolService.getToolSessionById(toolSessionId);
	    Map<Integer, Double> gradebookUserActivities = gradebookService
		    .getGradebookUserActivities(toolSession.getToolActivity().getActivityId()).stream()
		    .filter(g -> g.getMark() != null)
		    .collect(Collectors.toMap(g -> g.getLearner().getUserId(), GradebookUserActivity::getMark));

	    WhiteboardUser leader = users.get(0).getSession().getGroupLeader();
	    for (WhiteboardUser user : users) {
		ObjectNode responseRow = JsonNodeFactory.instance.objectNode();

		responseRow.put("userId", user.getUserId());
		responseRow.put("firstName", user.getFirstName());
		responseRow.put("lastName", user.getLastName());
		Double mark = gradebookUserActivities.get(user.getUserId().intValue());
		responseRow.put("mark", mark == null ? "" : String.valueOf(mark));
		responseRow.put("isLeader", leader != null && leader.getUid().equals(user.getUid()));

		rows.add(responseRow);
	    }
	    responsedata.set("rows", rows);
	}

	response.setContentType("application/json;charset=utf-8");
	return responsedata.toString();
    }

    @RequestMapping(path = "/updateLearnerMark", method = RequestMethod.POST)
    private void updateLearnerMark(@RequestParam long toolSessionId, @RequestParam int userId,
	    @RequestParam Double mark) {
	ToolSession toolSession = toolService.getToolSessionById(toolSessionId);
	long lessonId = toolSession.getLesson().getLessonId();
	securityService.isLessonMonitor(lessonId, getUserId(), "update Whiteboard learner mark", true);

	gradebookService.updateGradebookUserActivityMark(mark, null, userId, toolSessionId, true);

    }

    @RequestMapping(path = "/displayChangeLeaderForGroupDialogFromActivity")
    public String displayChangeLeaderForGroupDialogFromActivity(
	    @RequestParam(name = AttributeNames.PARAM_TOOL_SESSION_ID) long toolSessionId) {
	// tell Change Leader dialog in Leader Selection tool which learner has already reached this activity
	String availableLearners = whiteboardService.getUsersBySession(toolSessionId).stream()
		.collect(Collectors.mapping(user -> Long.toString(user.getUserId()), Collectors.joining(",")));

	return new StringBuilder("redirect:").append(Configuration.get(ConfigurationKeys.SERVER_URL))
		.append("tool/lalead11/monitoring/displayChangeLeaderForGroupDialogFromActivity.do?toolSessionId=")
		.append(toolSessionId).append("&availableLearners=").append(availableLearners).toString();
    }

    @RequestMapping(path = "/changeLeaderForGroup", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void changeLeaderForGroup(@RequestParam(name = AttributeNames.PARAM_TOOL_SESSION_ID) long toolSessionId,
	    @RequestParam long leaderUserId) {
	whiteboardService.changeLeaderForGroup(toolSessionId, leaderUserId);
    }

    @RequestMapping("/startGalleryWalk")
    private void startGalleryWalk(HttpServletRequest request) throws IOException {
	Long toolContentId = WebUtil.readLongParam(request, WhiteboardConstants.ATTR_TOOL_CONTENT_ID, false);

	whiteboardService.startGalleryWalk(toolContentId);
    }

    @RequestMapping("/finishGalleryWalk")
    private void finishGalleryWalk(HttpServletRequest request) throws IOException {
	Long toolContentId = WebUtil.readLongParam(request, WhiteboardConstants.ATTR_TOOL_CONTENT_ID, false);

	whiteboardService.finishGalleryWalk(toolContentId);
    }

    @RequestMapping(path = "/updateTimeLimit", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateTimeLimit(@RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    @RequestParam int relativeTimeLimit, @RequestParam(required = false) Long absoluteTimeLimit) {
	if (relativeTimeLimit < 0) {
	    throw new InvalidParameterException(
		    "Relative time limit must not be negative and it is " + relativeTimeLimit);
	}
	if (absoluteTimeLimit != null && relativeTimeLimit != 0) {
	    throw new InvalidParameterException(
		    "Relative time limit must not be provided when absolute time limit is set");
	}

	Whiteboard whiteboard = whiteboardService.getWhiteboardByContentId(toolContentId);
	whiteboard.setRelativeTimeLimit(relativeTimeLimit);
	// set time limit as seconds from start of epoch, using current server time zone
	whiteboard.setAbsoluteTimeLimit(absoluteTimeLimit == null ? null
		: LocalDateTime.ofEpochSecond(absoluteTimeLimit, 0, OffsetDateTime.now().getOffset()));
	whiteboardService.saveOrUpdate(whiteboard);
    }

    @RequestMapping(path = "/getPossibleIndividualTimeLimitUsers", method = RequestMethod.GET)
    @ResponseBody
    public String getPossibleIndividualTimeLimitUsers(
	    @RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    @RequestParam(name = "term") String searchString) {
	Whiteboard whiteboard = whiteboardService.getWhiteboardByContentId(toolContentId);
	Map<Integer, Integer> timeLimitAdjustments = whiteboard.getTimeLimitAdjustments();

	List<User> users = whiteboardService.getPossibleIndividualTimeLimitUsers(toolContentId, searchString);
	Grouping grouping = whiteboardService.getGrouping(toolContentId);

	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	String groupLabel = messageService.getMessage("monitoring.label.group") + " \"";
	if (grouping != null) {
	    Set<Group> groups = grouping.getGroups();
	    for (Group group : groups) {
		if (!group.getUsers().isEmpty() && group.getGroupName().contains(searchString.toLowerCase())) {
		    ObjectNode groupJSON = JsonNodeFactory.instance.objectNode();
		    groupJSON.put("label", groupLabel + group.getGroupName() + "\"");
		    groupJSON.put("value", "group-" + group.getGroupId());
		    responseJSON.add(groupJSON);
		}
	    }
	}

	for (User user : users) {
	    if (!timeLimitAdjustments.containsKey(user.getUserId())) {
		// this format is required by jQuery UI autocomplete
		ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
		userJSON.put("value", "user-" + user.getUserId());

		String name = user.getFirstName() + " " + user.getLastName() + " (" + user.getLogin() + ")";
		if (grouping != null) {
		    Group group = grouping.getGroupBy(user);
		    if (group != null) {
			name += " - " + group.getGroupName();
		    }
		}

		userJSON.put("label", name);
		responseJSON.add(userJSON);
	    }
	}
	return responseJSON.toString();
    }

    @RequestMapping(path = "/getExistingIndividualTimeLimitUsers", method = RequestMethod.GET)
    @ResponseBody
    public String getExistingIndividualTimeLimitUsers(
	    @RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId) {
	Whiteboard whiteboard = whiteboardService.getWhiteboardByContentId(toolContentId);
	Map<Integer, Integer> timeLimitAdjustments = whiteboard.getTimeLimitAdjustments();
	Grouping grouping = whiteboardService.getGrouping(toolContentId);
	// find User objects based on their userIDs and sort by name
	List<User> users = timeLimitAdjustments.keySet().stream()
		.map(userId -> userManagementService.getUserById(userId)).sorted(USER_NAME_COMPARATOR)
		.collect(Collectors.toList());

	if (grouping != null) {
	    // Make a map group -> its users who have a time limit set
	    // key are sorted by group name, users in each group are sorted by name
	    List<User> groupedUsers = grouping.getGroups().stream()
		    .collect(Collectors.toMap(Group::getGroupName, group -> {
			return group.getUsers().stream()
				.filter(user -> timeLimitAdjustments.containsKey(user.getUserId()))
				.collect(Collectors.toCollection(() -> new TreeSet<>(USER_NAME_COMPARATOR)));
		    }, (s1, s2) -> {
			s1.addAll(s2);
			return s1;
		    }, TreeMap::new)).values().stream().flatMap(Set::stream).collect(Collectors.toList());

	    // from general user list remove grouped users
	    users.removeAll(groupedUsers);
	    // at the end of list, add remaining, not yet grouped users
	    groupedUsers.addAll(users);
	    users = groupedUsers;
	}

	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	for (User user : users) {
	    ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
	    userJSON.put("userId", user.getUserId());
	    userJSON.put("adjustment", timeLimitAdjustments.get(user.getUserId().intValue()));

	    String name = user.getFirstName() + " " + user.getLastName() + " (" + user.getLogin() + ")";
	    if (grouping != null) {
		Group group = grouping.getGroupBy(user);
		if (group != null && !group.isNull()) {
		    name += " - " + group.getGroupName();
		}
	    }
	    userJSON.put("name", name);

	    responseJSON.add(userJSON);
	}
	return responseJSON.toString();
    }

    @RequestMapping(path = "/updateIndividualTimeLimit", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateIndividualTimeLimit(@RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    @RequestParam String itemId, @RequestParam(required = false) Integer adjustment) {
	Whiteboard whiteboard = whiteboardService.getWhiteboardByContentId(toolContentId);
	Map<Integer, Integer> timeLimitAdjustments = whiteboard.getTimeLimitAdjustments();
	Set<Integer> userIds = null;

	// itemId can user-<userId> or group-<groupId>
	String[] itemIdParts = itemId.split("-");
	if (itemIdParts[0].equalsIgnoreCase("group")) {
	    // add all users from a group, except for ones who are already added
	    Group group = (Group) userManagementService.findById(Group.class, Long.valueOf(itemIdParts[1]));
	    userIds = group.getUsers().stream().map(User::getUserId)
		    .filter(userId -> !timeLimitAdjustments.containsKey(userId)).collect(Collectors.toSet());
	} else {
	    // adjust for a single user
	    userIds = new HashSet<>();
	    userIds.add(Integer.valueOf(itemIdParts[1]));
	}

	for (Integer userId : userIds) {
	    if (adjustment == null) {
		timeLimitAdjustments.remove(userId);
	    } else {
		timeLimitAdjustments.put(userId, adjustment);
	    }
	}
	whiteboardService.saveOrUpdate(whiteboard);
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}