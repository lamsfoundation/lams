package org.lamsfoundation.lams.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONObject;
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

/**
 * 
 * @author asukkar
 * 
 * @struts.action path="/findUserLessons" parameter="dispatch" validate="false"
 * 
 * @struts.action-forward name="success-getResults" path="/findUserLessons.jsp"
 */
public class FindUserLessonsAction extends DispatchAction {
    private static final Logger log = Logger.getLogger(FindUserLessonsAction.class);

    private static IUserManagementService userManagementService;
    private static ILessonService lessonService;
    private static ISecurityService securityService;

    public ActionForward getResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String query = WebUtil.readStrParam(request, "query", true);
	Integer courseID = WebUtil.readIntParam(request, "courseID", true);
	Integer classID = WebUtil.readIntParam(request, "classID", true);

	if (courseID == null) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing course ID");
	    return null;
	}

	User viewer = getUserManagementService().getUserByLogin(request.getRemoteUser());
	if (!getSecurityService().isGroupMonitor(courseID, viewer.getUserId(), "find user lessons", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	if (query != null) {
	    Organisation group = (Organisation) getUserManagementService().findById(Organisation.class, courseID);
	    Set<User> users = getUserSet(query, group);

	    Map<User, List<LessonDTO>> userLessonsMap = new HashMap<User, List<LessonDTO>>();
	    for (User user : users) {
		// get all lessons for 'user' in 'group' and add to lessons map
		List<Lesson> lessons = (getLessonService().getLessonsByGroupAndUser(user.getUserId(),
			group.getOrganisationId()));

		List<LessonDTO> lessonDTOs = new ArrayList<LessonDTO>();
		for (Lesson lesson : lessons) {
		    LessonDTO dto = new LessonDTO(lesson);
		    // flag to display monitor link only if user is staff member of lesson
		    dto.setDisplayMonitor(lesson.getLessonClass().isStaffMember(viewer));
		    lessonDTOs.add(dto);
		}

		userLessonsMap.put(user, lessonDTOs);
	    }

	    request.setAttribute("userLessonsMap", userLessonsMap);
	}

	// set attributes
	request.setAttribute("courseID", courseID);
	request.setAttribute("classID", classID);
	request.setAttribute("originalQuery", query);

	return mapping.findForward("success-getResults");
    }

    @SuppressWarnings("unchecked")
    private Set<User> getUserSet(String query, Organisation rootOrg) {
	Set<User> userSet = new HashSet<User>();
	String[] tokens = query.trim().split("\\s+"); // Separated by "whitespace"
	for (String token : tokens) {
	    userSet.addAll(getUserManagementService().searchUserSingleTerm(token, rootOrg.getOrganisationId(), true));
	}

	return userSet;
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

	Set<User> userSet = getUserSet(query, rootOrg);
	JSONArray jsonArray = new JSONArray();
	for (User user : userSet) {
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("value", user.getFirstName() + " " + user.getLastName());
	    jsonArray.put(jsonObject);
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
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    FindUserLessonsAction.userManagementService = (IUserManagementService) wac
		    .getBean(CentralConstants.USER_MANAGEMENT_SERVICE_BEAN_NAME);
	}
	return FindUserLessonsAction.userManagementService;
    }

    private ILessonService getLessonService() {
	if (FindUserLessonsAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    FindUserLessonsAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return FindUserLessonsAction.lessonService;
    }

    private ISecurityService getSecurityService() {
	if (FindUserLessonsAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    FindUserLessonsAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return FindUserLessonsAction.securityService;
    }
}