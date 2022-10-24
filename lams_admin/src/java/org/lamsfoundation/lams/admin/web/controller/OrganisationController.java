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

package org.lamsfoundation.lams.admin.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.web.form.OrganisationForm;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.filter.AuditLogFilter;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Fei Yang
 */
@Controller
@RequestMapping("/organisation")
public class OrganisationController {
    private static Logger log = Logger.getLogger(OrganisationController.class);

    @Autowired
    private IMonitoringService monitoringService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;
    @Autowired
    @Qualifier("lessonService")
    private ILessonService lessonService;

    private static List status;

    @RequestMapping(path = "/edit")
    public String edit(@ModelAttribute OrganisationForm organisationForm, HttpServletRequest request) throws Exception {
	initLocalesAndStatus();
	Integer orgId = WebUtil.readIntParam(request, "orgId", true);

	HttpSession session = SessionManager.getSession();
	if (session != null) {
	    UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	    if (userDto != null) {
		Integer userId = userDto.getUserID();
		// appadmin, global group admin, group manager, group admin can edit group
		if (userManagementService.canEditGroup(userId, orgId)) {
		    // edit existing organisation
		    if (orgId != null) {
			Organisation org = (Organisation) userManagementService.findById(Organisation.class, orgId);
			BeanUtils.copyProperties(organisationForm, org);
			organisationForm.setParentId(org.getParentOrganisation().getOrganisationId());
			organisationForm.setParentName(org.getParentOrganisation().getName());
			organisationForm.setTypeId(org.getOrganisationType().getOrganisationTypeId());
			organisationForm.setStateId(org.getOrganisationState().getOrganisationStateId());

			// find a course or subcourse with any lessons, so we warn user when he tries to delete the course
			Integer courseToDeleteLessons = org.getLessons().size() > 0 ? orgId : null;
			if (courseToDeleteLessons == null) {
			    for (Organisation subcourse : org.getChildOrganisations()) {
				if (subcourse.getLessons().size() > 0) {
				    courseToDeleteLessons = subcourse.getOrganisationId();
				    break;
				}
			    }
			}
			request.setAttribute("courseToDeleteLessons", courseToDeleteLessons);
		    }
		    request.getSession().setAttribute("status", status);
		    if (userManagementService.isUserAppAdmin()
			    || userManagementService.isUserGlobalGroupManager()) {
			return "organisation/createOrEdit";
		    } else {
			return "organisation/courseAdminEdit";
		    }
		}
	    }
	}

	return error(request);
    }

    @RequestMapping(path = "/create")
    public String create(@ModelAttribute OrganisationForm organisationForm, HttpServletRequest request)
	    throws Exception {
	initLocalesAndStatus();

	if (!(request.isUserInRole(Role.APPADMIN) || userManagementService.isUserGlobalGroupManager())) {
	    // only appadmins and global group admins can create groups
	    if (((organisationForm.getTypeId() != null)
		    && organisationForm.getTypeId().equals(OrganisationType.COURSE_TYPE))
		    || (organisationForm.getTypeId() == null)) {
		return error(request);
	    }
	}

	// creating new organisation
	organisationForm.setOrgId(null);
	Integer parentId = WebUtil.readIntParam(request, "parentId", true);
	if (parentId != null) {
	    Organisation parentOrg = (Organisation) userManagementService.findById(Organisation.class, parentId);
	    organisationForm.setParentName(parentOrg.getName());
	}
	request.getSession().setAttribute("status", status);
	return "organisation/createOrEdit";
    }

    /**
     * Looks up course ID by its name. Used mainly by TestHarness.
     */
    @RequestMapping("/getOrganisationIdByName")
    @ResponseBody
    public String getOrganisationIdByName(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String organisationName = WebUtil.readStrParam(request, "name");
	List<Organisation> organisations = userManagementService.findByProperty(Organisation.class, "name",
		organisationName);
	if (!organisations.isEmpty()) {
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().print(organisations.get(0).getOrganisationId());
	}
	return null;
    }

    @RequestMapping(path = "/deleteAllLessonsInit")
    public String deleteAllLessonsInit(HttpServletRequest request, HttpServletResponse response) throws IOException {
	if (!securityService.isAppadmin(getUserID(), "display cleanup preview lessons", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not an appadmin");
	    return null;
	}

	if (!(request.isUserInRole(Role.APPADMIN))) {
	    request.setAttribute("errorName", "OrganisationAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.need.appadmin"));
	    return "error";
	}

	Integer organisationId = WebUtil.readIntParam(request, "orgId");
	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationId);
	int lessonCount = organisation.getLessons().size();
	request.setAttribute("lessonCount", lessonCount);
	request.setAttribute("courseName", organisation.getName());

	return "organisation/deleteAllLessons";
    }

    @RequestMapping(path = "/deleteAllLessons", method = RequestMethod.POST)
    public String deleteAllLessons(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Integer userID = getUserID();
	Integer limit = WebUtil.readIntParam(request, "limit", true);
	Integer organisationId = WebUtil.readIntParam(request, "orgId");
	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationId);
	List<Long> lessonIDs = lessonService.getOrganisationLessons(organisationId);
	StringBuilder logMessageBuilder = new StringBuilder("removed permanently lessons in organisation \"")
		.append(organisation.getName()).append("\": ");
	for (Long lessonId : lessonIDs) {
	    Lesson lesson = lessonService.getLesson(lessonId);
	    logMessageBuilder.append("\"").append(lesson.getLessonName()).append("\" (").append(lessonId)
		    .append("), )");
	    log.info("Deleting lesson: " + lessonId);
	    // role is checked in this method. This method requires that the lesson object has not be loaded into the Hibernate cache
	    monitoringService.removeLessonPermanently(lessonId, userID);

	    if (limit != null) {
		limit--;
		if (limit == 0) {
		    break;
		}
	    }
	}

	logMessageBuilder.delete(logMessageBuilder.length() - 2, logMessageBuilder.length());
	AuditLogFilter.log(AuditLogFilter.LESSON_REMOVE_PERMAMENTLY_ACTION, logMessageBuilder);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(organisation.getLessons().size());
	return null;
    }

    @RequestMapping("/error")
    public String error(HttpServletRequest request) {
	request.setAttribute("errorName", "OrganisationAction");
	request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	return "error";
    }

    private Integer getUserID() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user == null ? null : user.getUserID();
    }

    @SuppressWarnings("unchecked")
    private void initLocalesAndStatus() {
	if ((status == null) && (userManagementService != null)) {
	    status = userManagementService.findAll(OrganisationState.class);
	}
    }
}
