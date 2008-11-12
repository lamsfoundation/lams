package org.lamsfoundation.lams.web;

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
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.learning.service.ILearnerService;
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
 * @struts.action-forward name="success-autocomplete" path="/findUserLessonsAutocomplete.jsp"
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

	    Map<User, Set<IndexLessonBean>> userLessonsMap = new HashMap<User, Set<IndexLessonBean>>();
	    for (User user : users) {

		Set<IndexLessonBean> lessons = userLessonsMap.get(user);
		
		boolean addLessonsToMap = false;
		if (lessons == null) {
		    addLessonsToMap = true;
		    lessons = new HashSet<IndexLessonBean>();
		}

		// get all lessons for 'user' in 'group' and add to lessons map
		lessons.addAll(lessonService.getLessonsByGroupAndUser(user.getUserId(), group
			.getOrganisationId()).values());

		if (addLessonsToMap) {
		    userLessonsMap.put(user, lessons);
		}
	    }

	    request.setAttribute("userLessonsMap", userLessonsMap);
	}

	// set attributes
	request.setAttribute("courseID", courseID);
	request.setAttribute("classID", classID);
	request.setAttribute("originalQuery", query);

	return mapping.findForward("success-getResults");
    }

    private Set<Organisation> getOrgSet(Organisation rootOrg) {
	Set<Organisation> orgSet = new HashSet<Organisation>();
	orgSet.add(rootOrg);
	orgSet.addAll(rootOrg.getChildOrganisations());
	return orgSet;
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

	String query = WebUtil.readStrParam(request, "q", true);
	Integer courseID = WebUtil.readIntParam(request, "courseID", true);

	Organisation rootOrg = (Organisation) userManagementService.findById(Organisation.class, courseID);

	Set<User> userSet = getUserSet(query, rootOrg);

	List<String> list = new LinkedList<String>();

	for (User user : userSet) {
	    list.add(user.getFirstName() + " " + user.getLastName());
	}

	request.setAttribute("results", list);

	return mapping.findForward("success-autocomplete");
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