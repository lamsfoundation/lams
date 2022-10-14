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

package org.lamsfoundation.lams.admin.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jliew
 */
@Controller
@RequestMapping("/clone")
public class CloneLessonsController {
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private IMonitoringService monitoringService;

    @RequestMapping(path = "/start")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws UserAccessDeniedException {

	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    throw new UserAccessDeniedException();
	}

	List<String> errors = new ArrayList<>();
	try {
	    String method = WebUtil.readStrParam(request, "method", true);
	    if (StringUtils.equals(method, "getGroups")) {
		return getGroups(response);
	    } else if (StringUtils.equals(method, "getSubgroups")) {
		return getSubgroups(request, response);
	    } else if (StringUtils.equals(method, "availableLessons")) {
		return availableLessons(request, response);
	    } else if (StringUtils.equals(method, "selectStaff")) {
		return selectStaff(request, response);
	    } else if (StringUtils.equals(method, "selectLearners")) {
		return selectLearners(request, response);
	    } else if (StringUtils.equals(method, "clone")) {
		return clone(request);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    errors.add(e.getMessage());
	}
	request.setAttribute("errors", errors);

	// default action
	Integer groupId = WebUtil.readIntParam(request, "groupId", false);
	request.setAttribute("org", userManagementService.findById(Organisation.class, groupId));

	return "organisation/cloneStart";
    }

    // ajax
    @RequestMapping("/getGroups")
    @ResponseBody
    public String getGroups(HttpServletResponse response) throws Exception {

	response.addHeader("Cache-Control", "no-cache");
	response.addHeader("content-type", "text/html; charset=UTF-8");

	List groups = userManagementService.getOrganisationsByTypeAndStatus(OrganisationType.COURSE_TYPE,
		OrganisationState.ACTIVE);
	for (Object o : groups) {
	    Organisation org = (Organisation) o;
	    response.getWriter()
		    .println("<option value='" + org.getOrganisationId() + "'>" + org.getName() + "</option>");
	}

	return null;
    }

    // ajax
    @RequestMapping("/getSubgroups")
    @ResponseBody
    public String getSubgroups(HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer groupId = WebUtil.readIntParam(request, "groupId", true);

	response.addHeader("Cache-Control", "no-cache");
	response.addHeader("content-type", "text/html; charset=UTF-8");

	if (groupId != null) {
	    HashMap<String, Object> properties = new HashMap<>();
	    properties.put("parentOrganisation.organisationId", groupId);
	    properties.put("organisationType.organisationTypeId", OrganisationType.CLASS_TYPE);
	    properties.put("organisationState.organisationStateId", OrganisationState.ACTIVE);

	    response.getWriter().println("<option value=''></option>");
	    List groups = userManagementService.findByProperties(Organisation.class, properties);
	    for (Object o : groups) {
		Organisation org = (Organisation) o;
		response.getWriter()
			.println("<option value='" + org.getOrganisationId() + "'>" + org.getName() + "</option>");
	    }
	}

	return null;
    }

    // ajax
    @RequestMapping(path = "/availableLessons")
    public String availableLessons(HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer sourceGroupId = WebUtil.readIntParam(request, "sourceGroupId", true);

	if (sourceGroupId != null) {
	    List<Lesson> lessons = lessonService.getLessonsByGroup(sourceGroupId);
	    request.setAttribute("lessons", lessons);
	}

	response.addHeader("Cache-Control", "no-cache");
	return "organisation/parts/availableLessons";
    }

    // ajax
    @RequestMapping("/selectStaff")
    public String selectStaff(HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer groupId = WebUtil.readIntParam(request, "groupId", false);

	Vector monitors = userManagementService.getUsersFromOrganisationByRole(groupId, Role.MONITOR, true);
	request.setAttribute("monitors", monitors);

	response.addHeader("Cache-Control", "no-cache");
	return "organisation/parts/selectStaff";
    }

    // ajax
    @RequestMapping(path = "/selectLearners", method = RequestMethod.POST)
    public String selectLearners(HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer groupId = WebUtil.readIntParam(request, "groupId", false);

	Vector learners = userManagementService.getUsersFromOrganisationByRole(groupId, Role.LEARNER, true);
	request.setAttribute("learners", learners);

	response.addHeader("Cache-Control", "no-cache");
	return "organisation/parts/selectLearners";
    }

    @RequestMapping(path = "/clone", method = RequestMethod.POST)
    public String clone(HttpServletRequest request) throws Exception {

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

	int result = 0;
	Organisation group = (Organisation) userManagementService.findById(Organisation.class, groupId);
	if (group != null) {
	    result = monitoringService.cloneLessons(lessonIds, addAllStaff, addAllLearners, staffIds, learnerIds,
		    group);
	} else {
	    throw new UserException("Couldn't find Organisation based on id=" + groupId);
	}

	request.setAttribute("org", group);
	request.setAttribute("result", result);

	return "organisation/cloneResult";
    }

}
