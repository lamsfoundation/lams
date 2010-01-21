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

/* $Id$ */
package org.lamsfoundation.lams.admin.web.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author jliew
 * 
 * @struts:action path="/clone" scope="request" validate="false"
 * 
 * @struts:action-forward name="start" path=".clone-start"
 * @struts:action-forward name="availableLessonsPart" path="/organisation/parts/availableLessons.jsp"
 * @struts:action-forward name="selectStaffPart" path="/organisation/parts/selectStaff.jsp"
 * @struts:action-forward name="selectLearnersPart" path="/organisation/parts/selectLearners.jsp"
 * @struts:action-forward name="result" path=".clone-result"
 */
public class CloneGroupAction extends Action {

    private static final Logger log = Logger.getLogger(CloneGroupAction.class);
    private static IUserManagementService userManagementService;
    private static ILessonService lessonService;
    private static IMonitoringService monitoringService;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UserAccessDeniedException {

	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    throw new UserAccessDeniedException();
	}

	List<String> errors = new ArrayList<String>();
	try {
	    userManagementService = AdminServiceProxy.getService(getServlet().getServletContext());

	    String method = WebUtil.readStrParam(request, "method", true);
	    if (StringUtils.equals(method, "getGroups")) {
		return getGroups(mapping, form, request, response);
	    } else if (StringUtils.equals(method, "getSubgroups")) {
		return getSubgroups(mapping, form, request, response);
	    } else if (StringUtils.equals(method, "availableLessons")) {
		return availableLessons(mapping, form, request, response);
	    } else if (StringUtils.equals(method, "selectStaff")) {
		return selectStaff(mapping, form, request, response);
	    } else if (StringUtils.equals(method, "selectLearners")) {
		return selectLearners(mapping, form, request, response);
	    } else if (StringUtils.equals(method, "clone")) {
		return clone(mapping, form, request, response);
	    }

	    // default action
	    Integer groupId = WebUtil.readIntParam(request, "groupId", false);
	    request.setAttribute("org", userManagementService.findById(Organisation.class, groupId));
	} catch (Exception e) {
	    e.printStackTrace();
	    errors.add(e.getMessage());
	}
	request.setAttribute("errors", errors);

	return mapping.findForward("start");
    }

    // ajax
    public ActionForward getGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	response.addHeader("Cache-Control", "no-cache");

	List groups = userManagementService.getOrganisationsByTypeAndStatus(OrganisationType.COURSE_TYPE,
		OrganisationState.ACTIVE);
	for (Object o : groups) {
	    Organisation org = (Organisation) o;
	    response.getWriter().println(
		    "<option value='" + org.getOrganisationId() + "'>" + org.getName() + "</option>");
	}

	return null;
    }

    // ajax
    public ActionForward getSubgroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer groupId = WebUtil.readIntParam(request, "groupId", true);

	response.addHeader("Cache-Control", "no-cache");

	if (groupId != null) {
	    HashMap<String, Object> properties = new HashMap<String, Object>();
	    properties.put("parentOrganisation.organisationId", groupId);
	    properties.put("organisationType.organisationTypeId", OrganisationType.CLASS_TYPE);
	    properties.put("organisationState.organisationStateId", OrganisationState.ACTIVE);

	    response.getWriter().println("<option value=''></option>");
	    List groups = userManagementService.findByProperties(Organisation.class, properties);
	    for (Object o : groups) {
		Organisation org = (Organisation) o;
		response.getWriter().println(
			"<option value='" + org.getOrganisationId() + "'>" + org.getName() + "</option>");
	    }
	}

	return null;
    }

    // ajax
    public ActionForward availableLessons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer sourceGroupId = WebUtil.readIntParam(request, "sourceGroupId", true);

	if (sourceGroupId != null) {
	    lessonService = AdminServiceProxy.getLessonService(getServlet().getServletContext());

	    List<Lesson> lessons = lessonService.getLessonsByGroup(sourceGroupId);
	    request.setAttribute("lessons", lessons);
	}

	response.addHeader("Cache-Control", "no-cache");
	return mapping.findForward("availableLessonsPart");
    }

    // ajax
    public ActionForward selectStaff(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer groupId = WebUtil.readIntParam(request, "groupId", false);

	Vector monitors = userManagementService.getUsersFromOrganisationByRole(groupId, Role.MONITOR, false, true);
	request.setAttribute("monitors", monitors);

	response.addHeader("Cache-Control", "no-cache");
	return mapping.findForward("selectStaffPart");
    }

    // ajax
    public ActionForward selectLearners(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer groupId = WebUtil.readIntParam(request, "groupId", false);

	Vector learners = userManagementService.getUsersFromOrganisationByRole(groupId, Role.LEARNER, false, true);
	request.setAttribute("learners", learners);

	response.addHeader("Cache-Control", "no-cache");
	return mapping.findForward("selectLearnersPart");
    }

    public ActionForward clone(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UserAccessDeniedException {

	Enumeration e = request.getParameterNames();
	while (e.hasMoreElements()) {
	    String name = (String) e.nextElement();
	    log.debug(name + "=" + request.getParameter(name));
	}

	// Integer sourceGroupId = WebUtil.readIntParam(request, "sourceGroupId", false);
	Integer groupId = WebUtil.readIntParam(request, "groupId", false);
	String lessons = request.getParameter("lessons");
	String staff = request.getParameter("staff");
	String learners = request.getParameter("learners");
	Boolean addAllStaff = WebUtil.readBooleanParam(request, "addAllStaff");
	Boolean addAllLearners = WebUtil.readBooleanParam(request, "addAllLearners");

	String[] lessonIds = new String[0], staffIds = new String[0], learnerIds = new String[0];
	if (StringUtils.isNotEmpty(lessons)) {
	    lessonIds = lessons.split(",");
	}
	if (StringUtils.isNotEmpty(staff)) {
	    staffIds = staff.split(",");
	}
	if (StringUtils.isNotEmpty(learners)) {
	    learnerIds = learners.split(",");
	}

	monitoringService = AdminServiceProxy.getMonitoringService(getServlet().getServletContext());
	String error = null;
	List<String> errors = new ArrayList<String>();

	Organisation group = (Organisation) userManagementService.findById(Organisation.class, groupId);
	if (group != null) {
	    for (String l : lessonIds) {
		Lesson lesson = lessonService.getLesson(Long.valueOf(l));
		if (lesson != null) {
		    HttpSession ss = SessionManager.getSession();
		    if (ss != null) {
			UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
			if (userDto != null) {
			    if ((!addAllStaff && staffIds.length > 0) || addAllStaff) {
				// create staff LessonClass
				String staffGroupName = group.getName() + " Staff";
				List<User> staffUsers = createStaffGroup(groupId, addAllStaff, staffIds);

				if ((!addAllLearners && learnerIds.length > 0) || addAllLearners) {
				    // create learner LessonClass for lesson
				    String learnerGroupName = group.getName() + " Learners";
				    List<User> learnerUsers = createLearnerGroup(groupId, addAllLearners, learnerIds);

				    // init Lesson with user as creator
				    Lesson newLesson = monitoringService.initializeLesson(lesson.getLessonName(),
					    lesson.getLessonDescription(), lesson.getLearnerExportAvailable(), lesson
						    .getLearningDesign().getLearningDesignId(), groupId, userDto
						    .getUserID(), null, lesson.getLearnerPresenceAvailable(), lesson
						    .getLearnerImAvailable(), lesson.getLiveEditEnabled());

				    // save LessonClasses
				    newLesson = monitoringService.createLessonClassForLesson(newLesson.getLessonId(),
					    group, learnerGroupName, learnerUsers, staffGroupName, staffUsers, userDto
						    .getUserID());

				    // start Lessons
				    // TODO user-specified creator
				    monitoringService
					    .startLesson(newLesson.getLessonId(), Integer.valueOf(staffIds[0]));
				} else {
				    error = "No learners specified, can't create any Lessons.";
				}
			    } else {
				error = "No staff specified, can't create any Lessons.";
			    }
			} else {
			    error = "No UserDTO in session, can't create any Lessons.";
			}
		    }
		} else {
		    error = "Couldn't find Lesson based on id=" + l;
		}
	    }
	} else {
	    error = "Couldn't find Organisation based on id=" + groupId;
	}

	if (error != null) {
	    log.error(error);
	    errors.add(error);
	}
	request.setAttribute("errors", errors);
	request.setAttribute("org", group);

	return mapping.findForward("result");
    }

    private List<User> createLearnerGroup(Integer groupId, Boolean addAllLearners, String[] learnerIds) {
	List<User> learnerUsers = new ArrayList<User>();
	if (addAllLearners) {
	    Vector learnerVector = userManagementService.getUsersFromOrganisationByRole(groupId, Role.LEARNER, false,
		    true);
	    learnerUsers.addAll(learnerVector);
	}
	return learnerUsers;
    }

    private List<User> createStaffGroup(Integer groupId, Boolean addAllStaff, String[] staffIds) {
	List<User> staffUsers = new ArrayList<User>();
	if (addAllStaff) {
	    Vector staffVector = userManagementService.getUsersFromOrganisationByRole(groupId, Role.MONITOR, false,
		    true);
	    staffUsers.addAll(staffVector);
	} else {
	    User user = null;
	    for (String s : staffIds) {
		user = (User) userManagementService.findById(User.class, s);
		if (user != null) {
		    staffUsers.add(user);
		} else {
		    log.error("Couldn't find User based on id=" + s);
		}
	    }
	}
	return staffUsers;
    }
}
