package org.lamsfoundation.lams.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
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

    private static final Logger logger = Logger.getLogger(FindUserLessonsAction.class);

    private static IUserManagementService userManagementService;

    private static ILessonService lessonService;

    private static ILearnerService learnerService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// setup service
	if (userManagementService == null || lessonService == null) {
	    setupService();
	}

	return super.execute(mapping, form, request, response);
    }

    public ActionForward getResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// get query
	String query = WebUtil.readStrParam(request, "query", true);
	Integer courseID = WebUtil.readIntParam(request, "courseID", true);
	Integer classID = WebUtil.readIntParam(request, "classID", true);

	if (courseID == null) {
	    throw new ServletException();
	}

	if (query != null) {
	    Organisation group = (Organisation) userManagementService.findById(Organisation.class, courseID);

	    Set<User> users = getUserSet(query, group);
	    
	    User viewer = (User) userManagementService.getUserByLogin(request.getRemoteUser());

	    Map<User, List<LessonDTO>> userLessonsMap = new HashMap<User, List<LessonDTO>>();
	    for (User user : users) {

		// get all lessons for 'user' in 'group' and add to lessons map
		List<Lesson> lessons = (lessonService.getLessonsByGroupAndUser(user.getUserId(), group
			.getOrganisationId()));
		
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

    private Set<User> getUserSet(String query, Organisation rootOrg) {

	Set<User> userSet = new HashSet<User>();

	String[] tokens = query.trim().split("\\s+"); // Separated by "whitespace"

	for (String token : tokens) {
	    userSet.addAll(userManagementService.searchUserSingleTerm(token, rootOrg.getOrganisationId(), true));
	}

	return userSet;
    }

    public ActionForward autocomplete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String query = WebUtil.readStrParam(request, "term", true);
	Integer courseID = WebUtil.readIntParam(request, "courseID", true);

	Organisation rootOrg = (Organisation) userManagementService.findById(Organisation.class, courseID);

	Set<User> userSet = getUserSet(query, rootOrg);

	JSONArray jsonArray=new JSONArray();
	for (User user : userSet) {
	    JSONObject jsonObject = new JSONObject();
	    jsonObject.put("value", user.getFirstName() + " " + user.getLastName());
	    jsonArray.put(jsonObject);
	}
	
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(jsonArray);
	
	return null;
    }

    private void setupService() {
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}

	if (lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    lessonService = (ILessonService) ctx.getBean("lessonService");
	}

	if (learnerService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    learnerService = (ILearnerService) ctx.getBean("learnerService");
	}

    }

}
