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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
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
 * struts doclets
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

public class OrgSaveAction extends Action {

    private static Logger log = Logger.getLogger(OrgSaveAction.class);
    private static IUserManagementService service;
    private MessageService messageService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (service == null) {
	    service = AdminServiceProxy.getService(getServlet().getServletContext());
	}
	if (messageService == null) {
	    messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
	}
	DynaActionForm orgForm = (DynaActionForm) form;
	Integer orgId = (Integer) orgForm.get("orgId");
	Organisation org;

	if (isCancelled(request)) {
	    if (orgId != 0) {
		request.setAttribute("org", orgId);
		org = (Organisation) service.findById(Organisation.class, orgId);
		if (org.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
		    return mapping.findForward("userlist");
		}
	    } else {
		request.setAttribute("org", orgForm.get("parentId"));
	    }
	    return mapping.findForward("orglist");
	}

	ActionMessages errors = new ActionMessages();

	//organisation name validation
	String orgName = (orgForm.get("name") == null) ? null : (String) orgForm.get("name");
	if (StringUtils.isBlank(orgName)) {
	    errors.add("name", new ActionMessage("error.name.required"));
	} else if (!ValidationUtil.isOrgNameValid(orgName)) {
	    errors.add("name", new ActionMessage("error.name.invalid.characters"));
	}

	if (errors.isEmpty()) {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    SupportedLocale locale = (SupportedLocale) service.findById(SupportedLocale.class,
		    (Integer) orgForm.get("localeId"));
	    OrganisationState state = (OrganisationState) service.findById(OrganisationState.class,
		    (Integer) orgForm.get("stateId"));

	    if (orgId != 0) {
		if (service.canEditGroup(user.getUserID(), orgId)) {
		    org = (Organisation) service.findById(Organisation.class, orgId);
		    // set archived date only when it first changes to become archived
		    if (state.getOrganisationStateId().equals(OrganisationState.ARCHIVED) && !org.getOrganisationState()
			    .getOrganisationStateId().equals(OrganisationState.ARCHIVED)) {
			org.setArchivedDate(new Date());
		    }
		    writeAuditLog(user, org, orgForm, state, locale);
		    BeanUtils.copyProperties(org, orgForm);
		} else {
		    request.setAttribute("errorName", "UserAction");
		    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
		    return mapping.findForward("error");
		}
	    } else {
		org = new Organisation();
		BeanUtils.copyProperties(org, orgForm);
		org.setParentOrganisation(
			(Organisation) service.findById(Organisation.class, (Integer) orgForm.get("parentId")));
		org.setOrganisationType(
			(OrganisationType) service.findById(OrganisationType.class, (Integer) orgForm.get("typeId")));
		writeAuditLog(user, org, orgForm, org.getOrganisationState(), org.getLocale());
	    }
	    org.setLocale(locale);
	    org.setOrganisationState(state);
	    if (log.isDebugEnabled()) {
		log.debug("orgId: " + org.getOrganisationId() + " create date: " + org.getCreateDate());
	    }
	    org = service.saveOrganisation(org, user.getUserID());

	    request.setAttribute("org", orgForm.get("parentId"));
	    return mapping.findForward("orglist");
	} else {
	    saveErrors(request, errors);
	    return mapping.findForward("organisation");
	}
    }

    private void writeAuditLog(UserDTO user, Organisation org, DynaActionForm orgForm, OrganisationState newState,
	    SupportedLocale newLocale) {

	WebApplicationContext ctx = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	ILogEventService logEventService = (ILogEventService) ctx.getBean("logEventService");
	MessageService messageService = (MessageService) ctx.getBean("adminMessageService");

	String message;

	// audit log entries for organisation attribute changes	
	if ((Integer) orgForm.get("orgId") != 0) {
	    final String key = "audit.organisation.change";
	    String[] args = new String[4];
	    args[1] = org.getName() + "(" + org.getOrganisationId() + ")";
	    if (!org.getOrganisationState().getOrganisationStateId().equals(orgForm.get("stateId"))) {
		args[0] = "state";
		args[2] = org.getOrganisationState().getDescription();
		args[3] = newState.getDescription();
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null,
			null, null, null, message);
	    }
	    if (!StringUtils.equals(org.getName(), (String) orgForm.get("name"))) {
		args[0] = "name";
		args[2] = org.getName();
		args[3] = (String) orgForm.get("name");
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null,
			null, null, null, message);
	    }
	    if (!StringUtils.equals(org.getCode(), (String) orgForm.get("code"))) {
		args[0] = "code";
		args[2] = org.getCode();
		args[3] = (String) orgForm.get("code");
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null,
			null, null, null, message);
	    }
	    if (!StringUtils.equals(org.getDescription(), orgForm.getString("description"))) {
		args[0] = "description";
		args[2] = org.getDescription();
		args[3] = (String) orgForm.get("description");
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null,
			null, null, null, message);
	    }
	    if (!org.getCourseAdminCanAddNewUsers().equals(orgForm.get("courseAdminCanAddNewUsers"))) {
		args[0] = "courseAdminCanAddNewUsers";
		args[2] = org.getCourseAdminCanAddNewUsers() ? "true" : "false";
		args[3] = (Boolean) orgForm.get("courseAdminCanAddNewUsers") ? "true" : "false";
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null,
			null, null, null, message);
	    }
	    if (!org.getCourseAdminCanBrowseAllUsers().equals(orgForm.get("courseAdminCanBrowseAllUsers"))) {
		args[0] = "courseAdminCanBrowseAllUsers";
		args[2] = org.getCourseAdminCanBrowseAllUsers() ? "true" : "false";
		args[3] = (Boolean) orgForm.get("courseAdminCanBrowseAllUsers") ? "true" : "false";
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null,
			null, null, null, message);
	    }
	    if (!org.getCourseAdminCanChangeStatusOfCourse()
		    .equals(orgForm.get("courseAdminCanChangeStatusOfCourse"))) {
		args[0] = "courseAdminCanChangeStatusOfCourse";
		args[2] = org.getCourseAdminCanChangeStatusOfCourse() ? "true" : "false";
		args[3] = (Boolean) orgForm.get("courseAdminCanChangeStatusOfCourse") ? "true" : "false";
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null,
			null, null, null, message);
	    }
	    /*
	     * this field not set yet
	     * if(!org.getCourseAdminCanCreateGuestAccounts().equals((Boolean)orgForm.get(
	     * "courseAdminCanCreateGuestAccounts"))) {
	     * args[0] = "courseAdminCanCreateGuestAccounts";
	     * args[2] = org.getCourseAdminCanCreateGuestAccounts() ? "true" : "false";
	     * args[3] = (Boolean)orgForm.get("courseAdminCanCreateGuestAccounts") ? "true" : "false";
	     * message = messageService.getMessage(key, args);
	     * auditService.log(AdminConstants.MODULE_NAME, message);
	     * }
	     */
	    if (!org.getLocale().getLocaleId().equals(orgForm.get("localeId"))) {
		args[0] = "locale";
		args[2] = org.getLocale().getDescription();
		args[3] = newLocale.getDescription();
		message = messageService.getMessage(key, args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null,
			null, null, null, message);
	    }
	} else {
	    String[] args = new String[2];
	    args[0] = org.getName() + "(" + org.getOrganisationId() + ")";
	    args[1] = org.getOrganisationType().getName();
	    message = messageService.getMessage("audit.organisation.create", args);
		logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, user != null ? user.getUserID() : null,
			null, null, null, message);
	}
    }

}
