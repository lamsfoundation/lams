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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.OrganisationForm;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @version
 *
 * <p>
 * <a href="OrgSaveAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 16:42:53 on 2006-6-7
 */

/**
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

@Controller
public class OrgSaveController {

    private static Logger log = Logger.getLogger(OrgSaveController.class);
    private static IUserManagementService service;
    private MessageService messageService;

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/orgsave")
    public String execute(@ModelAttribute OrganisationForm organisationForm, BindingResult bindingResult,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	if (service == null) {
	    service = AdminServiceProxy.getService(applicationContext.getServletContext());
	}
	if (messageService == null) {
	    messageService = AdminServiceProxy.getMessageService(applicationContext.getServletContext());
	}

	Integer orgId = organisationForm.getOrgId();
	Organisation org;

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	//organisation name validation
	String orgName = (organisationForm.getName() == null) ? null : organisationForm.getName();
	if (StringUtils.isBlank(orgName)) {
	    errorMap.add("name", messageService.getMessage("error.name.required"));
	} else if (!ValidationUtil.isOrgNameValid(orgName)) {
	    errorMap.add("name", messageService.getMessage("error.name.invalid.characters"));
	}

	if (errorMap.isEmpty()) {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    SupportedLocale locale = (SupportedLocale) service.findById(SupportedLocale.class,
		    organisationForm.getLocaleId());
	    OrganisationState state = (OrganisationState) service.findById(OrganisationState.class,
		    organisationForm.getStateId());

	    if (orgId != 0) {
		if (service.canEditGroup(user.getUserID(), orgId)) {
		    org = (Organisation) service.findById(Organisation.class, orgId);
		    // set archived date only when it first changes to become archived
		    if (state.getOrganisationStateId().equals(OrganisationState.ARCHIVED) && !org.getOrganisationState()
			    .getOrganisationStateId().equals(OrganisationState.ARCHIVED)) {
			org.setArchivedDate(new Date());
		    }
		    writeAuditLog(user, org, organisationForm, state, locale);
		    BeanUtils.copyProperties(org, organisationForm);
		} else {
		    request.setAttribute("errorName", "UserAction");
		    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
		    return "error";
		}
	    } else {
		org = new Organisation();
		BeanUtils.copyProperties(org, organisationForm);
		org.setParentOrganisation(
			(Organisation) service.findById(Organisation.class, organisationForm.getParentId()));
		org.setOrganisationType(
			(OrganisationType) service.findById(OrganisationType.class, organisationForm.getOrgId()));
		writeAuditLog(user, org, organisationForm, org.getOrganisationState(), org.getLocale());
	    }
	    org.setLocale(locale);
	    org.setOrganisationState(state);
	    if (log.isDebugEnabled()) {
		log.debug("orgId: " + org.getOrganisationId() + " create date: " + org.getCreateDate());
	    }
	    org = service.saveOrganisation(org, user.getUserID());

	    request.setAttribute("org", organisationForm.getParentId());
	    return "forward:/orgmanage.do";
	} else {
	    return "forward:/organisation/edit.do";
	}
    }

    private void writeAuditLog(UserDTO user, Organisation org, OrganisationForm orgForm, OrganisationState newState,
	    SupportedLocale newLocale) {

	WebApplicationContext ctx = WebApplicationContextUtils
		.getRequiredWebApplicationContext(applicationContext.getServletContext());
	ILogEventService logEventService = (ILogEventService) ctx.getBean("logEventService");
	MessageService messageService = (MessageService) ctx.getBean("adminMessageService");

	String message;

	// audit log entries for organisation attribute changes
	if (orgForm.getOrgId() != 0) {
	    final String key = "audit.organisation.change";
	    String[] args = new String[4];
	    args[1] = org.getName() + "(" + org.getOrganisationId() + ")";
	    if (!org.getOrganisationState().getOrganisationStateId().equals(orgForm.getStateId())) {
		args[0] = "state";
		args[2] = org.getOrganisationState().getDescription();
		args[3] = newState.getDescription();
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null, null,
			null, null, message);
	    }
	    if (!StringUtils.equals(org.getName(), orgForm.getName())) {
		args[0] = "name";
		args[2] = org.getName();
		args[3] = orgForm.getName();
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null, null,
			null, null, message);
	    }
	    if (!StringUtils.equals(org.getCode(), orgForm.getCode())) {
		args[0] = "code";
		args[2] = org.getCode();
		args[3] = orgForm.getCode();
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null, null,
			null, null, message);
	    }
	    if (!StringUtils.equals(org.getDescription(), orgForm.getDescription())) {
		args[0] = "description";
		args[2] = org.getDescription();
		args[3] = orgForm.getDescription();
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null, null,
			null, null, message);
	    }
	    if (!org.getCourseAdminCanAddNewUsers().equals(orgForm.isCourseAdminCanAddNewUsers())) {
		args[0] = "courseAdminCanAddNewUsers";
		args[2] = org.getCourseAdminCanAddNewUsers() ? "true" : "false";
		args[3] = orgForm.isCourseAdminCanAddNewUsers() ? "true" : "false";
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null, null,
			null, null, message);
	    }
	    if (!org.getCourseAdminCanBrowseAllUsers().equals(orgForm.isCourseAdminCanAddNewUsers())) {
		args[0] = "courseAdminCanBrowseAllUsers";
		args[2] = org.getCourseAdminCanBrowseAllUsers() ? "true" : "false";
		args[3] = orgForm.isCourseAdminCanBrowseAllUsers() ? "true" : "false";
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null, null,
			null, null, message);
	    }
	    if (!org.getCourseAdminCanChangeStatusOfCourse().equals(orgForm.isCourseAdminCanChangeStatusOfCourse())) {
		args[0] = "courseAdminCanChangeStatusOfCourse";
		args[2] = org.getCourseAdminCanChangeStatusOfCourse() ? "true" : "false";
		args[3] = orgForm.isCourseAdminCanChangeStatusOfCourse() ? "true" : "false";
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null, null,
			null, null, message);
	    }
	    /*
	     * this field not set yet
	     * if(!org.getCourseAdminCanCreateGuestAccounts().equals(isCourseAdminCanCreateGuestAccounts())) {
	     * args[0] = "courseAdminCanCreateGuestAccounts";
	     * args[2] = org.getCourseAdminCanCreateGuestAccounts() ? "true" : "false";
	     * args[3] = orgForm.isCourseAdminCanCreateGuestAccounts() ? "true" : "false";
	     * message = messageService.getMessage(key, args);
	     * auditService.log(AdminConstants.MODULE_NAME, message);
	     * }
	     */
	    if (!org.getLocale().getLocaleId().equals(orgForm.getLocaleId())) {
		args[0] = "locale";
		args[2] = org.getLocale().getDescription();
		args[3] = newLocale.getDescription();
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null, null,
			null, null, message);
	    }
	} else {
	    String[] args = new String[2];
	    args[0] = org.getName() + "(" + org.getOrganisationId() + ")";
	    args[1] = org.getOrganisationType().getName();
	    message = messageService.getMessage("audit.organisation.create", args);
	    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null, null, null,
		    null, message);
	}
    }

}
