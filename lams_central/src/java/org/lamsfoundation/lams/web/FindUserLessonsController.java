package org.lamsfoundation.lams.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author asukkar
 */
@Controller
@RequestMapping("/findUserLessons")
public class FindUserLessonsController {
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ISecurityService securityService;

    @RequestMapping("/getResults")
    public String getResults(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Integer courseID = WebUtil.readIntParam(request, "courseID");

	User viewer = (User) userManagementService.findById(User.class, getUserId());
	if (!securityService.isGroupMonitor(courseID, viewer.getUserId(), "find user lessons")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	String query = null;
	List<User> users = new LinkedList<>();
	Organisation group = (Organisation) userManagementService.findById(Organisation.class, courseID);
	// if an exact user was chosen from autocomplete list, display lessons just for him
	Integer userID = WebUtil.readIntParam(request, "userID", true);
	if (userID != null) {
	    User user = (User) userManagementService.findById(User.class, userID);
	    users.add(user);
	    query = user.getFirstName() + " " + user.getLastName();
	} else {
	    // if a generic query was entered, look for all users who match the query
	    query = WebUtil.readStrParam(request, "query", true);
	    if (query != null) {
		users = userManagementService.findUsers(query, group.getOrganisationId(), true);
	    }
	}

	Map<User, List<LessonDTO>> userLessonsMap = new HashMap<>();
	for (User user : users) {
	    // get all lessons for 'user' in 'group' and add to lessons map
	    List<Lesson> lessons = lessonService.getLessonsByGroupAndUser(user.getUserId(), group.getOrganisationId());

	    List<LessonDTO> lessonDTOs = new ArrayList<>();
	    for (Lesson lesson : lessons) {
		LessonDTO dto = new LessonDTO(lesson);
		// flag to display monitor link only if user is staff member of lesson
		dto.setDisplayMonitor(lesson.getLessonClass().isStaffMember(viewer));
		lessonDTOs.add(dto);
	    }

	    userLessonsMap.put(user, lessonDTOs);
	}

	request.setAttribute("userLessonsMap", userLessonsMap);
	// set attributes
	request.setAttribute("courseID", courseID);
	request.setAttribute("originalQuery", query);

	return "findUserLessons";
    }

    @ResponseBody
    @RequestMapping("/autocomplete")
    public String autocomplete(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Integer courseID = WebUtil.readIntParam(request, "courseID", true);
	if (!securityService.isGroupMonitor(courseID, getUserId(), "autocomplete for find user lessons")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	String query = WebUtil.readStrParam(request, "term", true);
	Organisation rootOrg = (Organisation) userManagementService.findById(Organisation.class, courseID);

	List<User> userSet = userManagementService.findUsers(query, rootOrg.getOrganisationId(), true);
	ArrayNode jsonArray = JsonNodeFactory.instance.arrayNode();
	for (User user : userSet) {
	    ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
	    jsonObject.put("label", user.getFirstName() + " " + user.getLastName());
	    jsonObject.put("value", user.getUserId());
	    jsonArray.add(jsonObject);
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(jsonArray);
	return null;
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}