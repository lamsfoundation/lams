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


package org.lamsfoundation.lams.admin.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;

/**
 * @author jliew
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class CloneLessonsAction extends Action {

    private static final Logger log = Logger.getLogger(CloneLessonsAction.class);
    private static IUserManagementService userManagementService;
    private static ILessonService lessonService;
    private static IMonitoringService monitoringService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UserAccessDeniedException {

	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    throw new UserAccessDeniedException();
	}

	List<String> errors = new ArrayList<String>();
	try {
	    CloneLessonsAction.userManagementService = AdminServiceProxy.getService(getServlet().getServletContext());

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
	} catch (Exception e) {
	    e.printStackTrace();
	    errors.add(e.getMessage());
	}
	request.setAttribute("errors", errors);

	// default action
	Integer groupId = WebUtil.readIntParam(request, "groupId", false);
	request.setAttribute("org", CloneLessonsAction.userManagementService.findById(Organisation.class, groupId));

	return mapping.findForward("start");
    }

    // ajax
    public ActionForward getGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	response.addHeader("Cache-Control", "no-cache");
	response.addHeader("content-type", "text/html; charset=UTF-8");

	List groups = CloneLessonsAction.userManagementService
		.getOrganisationsByTypeAndStatus(OrganisationType.COURSE_TYPE, OrganisationState.ACTIVE);
	for (Object o : groups) {
	    Organisation org = (Organisation) o;
	    response.getWriter()
		    .println("<option value='" + org.getOrganisationId() + "'>" + org.getName() + "</option>");
	}

	return null;
    }

    // ajax
    public ActionForward getSubgroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer groupId = WebUtil.readIntParam(request, "groupId", true);

	response.addHeader("Cache-Control", "no-cache");
	response.addHeader("content-type", "text/html; charset=UTF-8");

	if (groupId != null) {
	    HashMap<String, Object> properties = new HashMap<String, Object>();
	    properties.put("parentOrganisation.organisationId", groupId);
	    properties.put("organisationType.organisationTypeId", OrganisationType.CLASS_TYPE);
	    properties.put("organisationState.organisationStateId", OrganisationState.ACTIVE);

	    response.getWriter().println("<option value=''></option>");
	    List groups = CloneLessonsAction.userManagementService.findByProperties(Organisation.class, properties);
	    for (Object o : groups) {
		Organisation org = (Organisation) o;
		response.getWriter()
			.println("<option value='" + org.getOrganisationId() + "'>" + org.getName() + "</option>");
	    }
	}

	return null;
    }

    // ajax
    public ActionForward availableLessons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer sourceGroupId = WebUtil.readIntParam(request, "sourceGroupId", true);

	if (sourceGroupId != null) {
	    CloneLessonsAction.lessonService = AdminServiceProxy.getLessonService(getServlet().getServletContext());

	    List<Lesson> lessons = CloneLessonsAction.lessonService.getLessonsByGroup(sourceGroupId);
	    request.setAttribute("lessons", lessons);
	}

	response.addHeader("Cache-Control", "no-cache");
	return mapping.findForward("availableLessonsPart");
    }

    // ajax
    public ActionForward selectStaff(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer groupId = WebUtil.readIntParam(request, "groupId", false);

	Vector monitors = CloneLessonsAction.userManagementService.getUsersFromOrganisationByRole(groupId, Role.MONITOR,
		true);
	request.setAttribute("monitors", monitors);

	response.addHeader("Cache-Control", "no-cache");
	return mapping.findForward("selectStaffPart");
    }

    // ajax
    public ActionForward selectLearners(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer groupId = WebUtil.readIntParam(request, "groupId", false);

	Vector learners = CloneLessonsAction.userManagementService.getUsersFromOrganisationByRole(groupId, Role.LEARNER,
		true);
	request.setAttribute("learners", learners);

	response.addHeader("Cache-Control", "no-cache");
	return mapping.findForward("selectLearnersPart");
    }

    public ActionForward clone(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer groupId = WebUtil.readIntParam(request, "groupId", false);
	String lessons = request.getParameter("lessons");
	String staff = request.getParameter("staff");
	String learners = request.getParameter("learners");
	Boolean addAllStaff = WebUtil.readBooleanParam(request, "addAllStaff", false);
	Boolean addAllLearners = WebUtil.readBooleanParam(request, "addAllLearners", false);

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

	CloneLessonsAction.monitoringService = AdminServiceProxy.getMonitoringService(getServlet().getServletContext());
	int result = 0;

	Organisation group = (Organisation) CloneLessonsAction.userManagementService.findById(Organisation.class,
		groupId);
	if (group != null) {
	    result = CloneLessonsAction.monitoringService.cloneLessons(lessonIds, addAllStaff, addAllLearners, staffIds,
		    learnerIds, group);
	} else {
	    throw new UserException("Couldn't find Organisation based on id=" + groupId);
	}

	request.setAttribute("org", group);
	request.setAttribute("result", result);

	return mapping.findForward("result");
    }

}
