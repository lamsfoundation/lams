package org.lamsfoundation.lams.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author asukkar
 *
 *
 *
 *
 */
public class FindUserLessonsAction extends DispatchAction {
    private static IUserManagementService userManagementService;
    private static ILessonService lessonService;
    private static ISecurityService securityService;

    public ActionForward getResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer courseID = WebUtil.readIntParam(request, "courseID");

	User viewer = (User) getUserManagementService().findById(User.class, getUserId());
	if (!getSecurityService().isGroupMonitor(courseID, viewer.getUserId(), "find user lessons", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	String query = null;
	List<User> users = new LinkedList<>();
	Organisation group = (Organisation) getUserManagementService().findById(Organisation.class, courseID);
	// if an exact user was chosen from autocomplete list, display lessons just for him
	Integer userID = WebUtil.readIntParam(request, "userID", true);
	if (userID != null) {
	    User user = (User) getUserManagementService().findById(User.class, userID);
	    users.add(user);
	    query = user.getFirstName() + " " + user.getLastName();
	} else {
	    // if a generic query was entered, look for all users who match the query
	    query = WebUtil.readStrParam(request, "query", true);
	    if (query != null) {
		users = getUserManagementService().findUsers(query, group.getOrganisationId(), true);
	    }
	}

	Map<User, List<LessonDTO>> userLessonsMap = new HashMap<>();
	for (User user : users) {
	    // get all lessons for 'user' in 'group' and add to lessons map
	    List<Lesson> lessons = getLessonService().getLessonsByGroupAndUser(user.getUserId(),
		    group.getOrganisationId());

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

	return mapping.findForward("success-getResults");
    }

    public ActionForward autocomplete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer courseID = WebUtil.readIntParam(request, "courseID", true);
	if (!getSecurityService().isGroupMonitor(courseID, getUserId(), "autocomplete for find user lessons", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	String query = WebUtil.readStrParam(request, "term", true);
	Organisation rootOrg = (Organisation) getUserManagementService().findById(Organisation.class, courseID);

	List<User> userSet = getUserManagementService().findUsers(query, rootOrg.getOrganisationId(), true);
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

    private IUserManagementService getUserManagementService() {
	if (FindUserLessonsAction.userManagementService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    FindUserLessonsAction.userManagementService = (IUserManagementService) wac
		    .getBean(CentralConstants.USER_MANAGEMENT_SERVICE_BEAN_NAME);
	}
	return FindUserLessonsAction.userManagementService;
    }

    private ILessonService getLessonService() {
	if (FindUserLessonsAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    FindUserLessonsAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return FindUserLessonsAction.lessonService;
    }

    private ISecurityService getSecurityService() {
	if (FindUserLessonsAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    FindUserLessonsAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return FindUserLessonsAction.securityService;
    }
}