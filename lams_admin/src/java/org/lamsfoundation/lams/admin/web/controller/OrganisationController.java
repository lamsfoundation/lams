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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.OrganisationForm;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Fei Yang
 */
@Controller
@RequestMapping("/organisation")
public class OrganisationController {

    private static IUserManagementService service;
    private static MessageService messageService;
    private static List<SupportedLocale> locales;
    private static List status;

    private static Logger log = Logger.getLogger(OrganisationController.class);

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute OrganisationForm organisationForm, HttpServletRequest request) throws Exception {

	OrganisationController.service = AdminServiceProxy.getService(applicationContext.getServletContext());
	initLocalesAndStatus();
	Integer orgId = WebUtil.readIntParam(request, "orgId", true);

	HttpSession session = SessionManager.getSession();
	if (session != null) {
	    UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	    if (userDto != null) {
		Integer userId = userDto.getUserID();
		// sysadmin, global group admin, group manager, group admin can edit group
		if (OrganisationController.service.canEditGroup(userId, orgId)) {
		    // edit existing organisation
		    if (orgId != null) {
			Organisation org = (Organisation) OrganisationController.service.findById(Organisation.class,
				orgId);
			BeanUtils.copyProperties(organisationForm, org);
			organisationForm.setParentId(org.getParentOrganisation().getOrganisationId());
			organisationForm.setParentName(org.getParentOrganisation().getName());
			organisationForm.setOrgId(org.getOrganisationType().getOrganisationTypeId());
			organisationForm.setStateId(org.getOrganisationState().getOrganisationStateId());
			SupportedLocale locale = org.getLocale();
			organisationForm.setLocaleId(locale != null ? locale.getLocaleId() : null);
			;

			// find a course or subcourse with any lessons, so we warn user when he tries to delete the course
			Integer courseToDeleteLessons = org.getLessons().size() > 0 ? orgId : null;
			if (courseToDeleteLessons == null) {
			    for (Organisation subcourse : (Set<Organisation>) org.getChildOrganisations()) {
				if (subcourse.getLessons().size() > 0) {
				    courseToDeleteLessons = subcourse.getOrganisationId();
				    break;
				}
			    }
			}
			request.setAttribute("courseToDeleteLessons", courseToDeleteLessons);
		    }
		    request.getSession().setAttribute("locales", OrganisationController.locales);
		    request.getSession().setAttribute("status", OrganisationController.status);
		    if (OrganisationController.service.isUserSysAdmin()
			    || OrganisationController.service.isUserGlobalGroupAdmin()) {
			return "organisation/createOrEdit";
		    } else {
			return "organisation/courseAdminEdit";
		    }
		}
	    }
	}

	return error(request);
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public String create(@ModelAttribute OrganisationForm organisationForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	OrganisationController.service = AdminServiceProxy.getService(applicationContext.getServletContext());
	initLocalesAndStatus();

	if (!(request.isUserInRole(Role.SYSADMIN) || OrganisationController.service.isUserGlobalGroupAdmin())) {
	    // only sysadmins and global group admins can create groups
	    if (((organisationForm.getOrgId() != null)
		    && organisationForm.getOrgId().equals(OrganisationType.COURSE_TYPE))
		    || (organisationForm.getOrgId() == null)) {
		return error(request);
	    }
	}

	// creating new organisation
	organisationForm.setOrgId(null);
	;
	Integer parentId = WebUtil.readIntParam(request, "parentId", true);
	if (parentId != null) {
	    Organisation parentOrg = (Organisation) OrganisationController.service.findById(Organisation.class,
		    parentId);
	    organisationForm.setParentName(parentOrg.getName());
	}
	request.getSession().setAttribute("locales", OrganisationController.locales);
	request.getSession().setAttribute("status", OrganisationController.status);
	return "organisation/createOrEdit";
    }

    /**
     * Looks up course ID by its name. Used mainly by TestHarness.
     */
    @RequestMapping("/getOrganisationIdByName")
    @ResponseBody
    public String getOrganisationIdByName(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String organisationName = WebUtil.readStrParam(request, "name");
	OrganisationController.service = AdminServiceProxy.getService(applicationContext.getServletContext());
	List<Organisation> organisations = service.findByProperty(Organisation.class, "name", organisationName);
	if (!organisations.isEmpty()) {
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().print(organisations.get(0).getOrganisationId());
	}
	return null;
    }

    @RequestMapping("/deleteAllLessonsInit")
    public String deleteAllLessonsInit(HttpServletRequest request, HttpServletResponse response) throws IOException {
	if (!AdminServiceProxy.getSecurityService(applicationContext.getServletContext()).isSysadmin(getUserID(),
		"display cleanup preview lessons", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a sysadmin");
	    return null;
	}

	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    request.setAttribute("errorName", "OrganisationAction");
	    request.setAttribute("errorMessage", AdminServiceProxy
		    .getMessageService(applicationContext.getServletContext()).getMessage("error.need.sysadmin"));
	    return "error";
	}

	Integer organisationId = WebUtil.readIntParam(request, "orgId");
	Organisation organisation = (Organisation) AdminServiceProxy.getService(applicationContext.getServletContext())
		.findById(Organisation.class, organisationId);
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
	Organisation organisation = (Organisation) AdminServiceProxy.getService(applicationContext.getServletContext())
		.findById(Organisation.class, organisationId);
	for (Lesson lesson : (Set<Lesson>) organisation.getLessons()) {
	    log.info("Deleting lesson: " + lesson.getLessonId());
	    // role is checked in this method
	    AdminServiceProxy.getMonitoringService(applicationContext.getServletContext())
		    .removeLessonPermanently(lesson.getLessonId(), userID);
	    if (limit != null) {
		limit--;
		if (limit == 0) {
		    break;
		}
	    }
	}

	organisation = (Organisation) AdminServiceProxy.getService(applicationContext.getServletContext())
		.findById(Organisation.class, organisationId);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(organisation.getLessons().size());
	return null;
    }

    @RequestMapping("/error")
    public String error(HttpServletRequest request) {
	OrganisationController.messageService = AdminServiceProxy
		.getMessageService(applicationContext.getServletContext());
	request.setAttribute("errorName", "OrganisationAction");
	request.setAttribute("errorMessage", OrganisationController.messageService.getMessage("error.authorisation"));
	return "error";
    }

    private Integer getUserID() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user == null ? null : user.getUserID();
    }

    @SuppressWarnings("unchecked")
    private void initLocalesAndStatus() {
	if ((OrganisationController.locales == null)
		|| ((OrganisationController.status == null) && (OrganisationController.service != null))) {
	    OrganisationController.locales = OrganisationController.service.findAll(SupportedLocale.class);
	    OrganisationController.status = OrganisationController.service.findAll(OrganisationState.class);
	    Collections.sort(OrganisationController.locales);
	}
    }
}