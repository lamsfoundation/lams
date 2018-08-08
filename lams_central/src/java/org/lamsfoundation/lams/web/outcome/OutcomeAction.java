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

package org.lamsfoundation.lams.web.outcome;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeScale;
import org.lamsfoundation.lams.outcome.service.IOutcomeService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OutcomeAction extends DispatchAction {

    private static Logger log = Logger.getLogger(OutcomeAction.class);

    private static IUserManagementService userManagementService;
    private static ICoreLearnerService learnerService;
    private static ILessonService lessonService;
    private static ISecurityService securityService;
    private static IIntegrationService integrationService;
    private static IOutcomeService outcomeService;

    public ActionForward outcomeManage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer userId = getUserDTO().getUserID();
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);

	if (organisationId == null) {
	    // check if user is allowed to view and edit global outcomes
	    if (!getSecurityService().isSysadmin(userId, "manage global outcomes", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a sysadmin");
		return null;
	    }
	} else {
	    // check if user is allowed to view and edit course outcomes
	    if (!getSecurityService().hasOrgRole(organisationId, userId, new String[] { Role.AUTHOR },
		    "manage course outcomes", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not an author in the organisation");
		return null;
	    }
	}
	List<Outcome> outcomes = getOutcomeService().getOutcomesForManagement(organisationId);
	request.setAttribute("outcomes", outcomes);

	request.setAttribute("canManageGlobal", getUserManagementService().isUserSysAdmin());
	return mapping.findForward("outcomeManage");
    }

    @SuppressWarnings("unchecked")
    public ActionForward outcomeEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer userId = getUserDTO().getUserID();
	Long outcomeId = WebUtil.readLongParam(request, "outcomeId", true);
	Outcome outcome = null;
	Integer organisationId = null;
	if (outcomeId == null) {
	    organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	} else {
	    outcome = (Outcome) getUserManagementService().findById(Outcome.class, outcomeId);
	    if (outcome.getOrganisation() != null) {
		// get organisation ID from the outcome - the safest way
		organisationId = outcome.getOrganisation().getOrganisationId();
	    }
	}

	if (organisationId != null && !getSecurityService().hasOrgRole(organisationId, userId,
		new String[] { Role.AUTHOR }, "add/edit course outcome", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not an author in the organisation");
	    return null;
	}

	OutcomeForm outcomeForm = (OutcomeForm) form;
	outcomeForm.setOrganisationId(organisationId);
	outcomeForm.setContentFolderId(getOutcomeService().getContentFolderId(organisationId));
	if (outcome != null) {
	    outcomeForm.setOutcomeId(outcome.getOutcomeId());
	    outcomeForm.setName(outcome.getName());
	    outcomeForm.setCode(outcome.getCode());
	    outcomeForm.setDescription(outcome.getDescription());
	    outcomeForm.setScaleId(outcome.getScale().getScaleId());
	}

	List<OutcomeScale> scales = getUserManagementService().findAll(OutcomeScale.class);
	request.setAttribute("scales", scales);

	request.setAttribute("canManageGlobal", getUserManagementService().isUserSysAdmin());
	return mapping.findForward("outcomeEdit");
    }

    @SuppressWarnings("unchecked")
    public ActionForward outcomeSave(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	OutcomeForm outcomeForm = (OutcomeForm) form;
	Integer userId = getUserDTO().getUserID();
	Long outcomeId = outcomeForm.getOutcomeId();
	Outcome outcome = null;
	Integer organisationId = null;
	if (outcomeId == null) {
	    organisationId = outcomeForm.getOrganisationId();
	} else {
	    outcome = (Outcome) getUserManagementService().findById(Outcome.class, outcomeId);
	    if (outcome.getOrganisation() != null) {
		// get organisation ID from the outcome - the safest way
		organisationId = outcome.getOrganisation().getOrganisationId();
	    }
	}

	if (organisationId == null) {
	    if (!getSecurityService().isSysadmin(userId, "persist global outcome", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a sysadmin");
		return null;
	    }
	} else {
	    if (!getSecurityService().hasOrgRole(organisationId, userId, new String[] { Role.AUTHOR },
		    "persist course outcome", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not an author in the organisation");
		return null;
	    }
	}

	ActionErrors errors = validateOutcomeForm(outcomeForm);
	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	} else {
	    try {
		Organisation organisation = (Organisation) (organisationId == null ? null
			: getUserManagementService().findById(Organisation.class, organisationId));
		if (outcome == null) {
		    outcome = new Outcome();
		    outcome.setOrganisation(organisation);
		    User user = (User) getUserManagementService().findById(User.class, userId);
		    outcome.setCreateBy(user);
		    outcome.setCreateDateTime(new Date());
		}

		outcome.setName(outcomeForm.getName());
		outcome.setCode(outcomeForm.getCode());
		outcome.setDescription(outcomeForm.getDescription());
		outcome.setContentFolderId(outcomeForm.getContentFolderId());
		if (outcomeForm.getScaleId() != null) {
		    OutcomeScale scale = (OutcomeScale) getUserManagementService().findById(OutcomeScale.class,
			    outcomeForm.getScaleId());
		    outcome.setScale(scale);
		}
		getUserManagementService().save(outcome);
		request.setAttribute("saved", true);
	    } catch (Exception e) {
		log.error("Exception while saving an outcome", e);
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("outcome.manage.add.error"));
		this.addErrors(request, errors);
	    }
	}

	List<OutcomeScale> scales = getUserManagementService().findAll(OutcomeScale.class);
	request.setAttribute("scales", scales);
	return mapping.findForward("outcomeEdit");
    }

    public ActionForward outcomeRemove(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long outcomeId = WebUtil.readLongParam(request, "outcomeId", false);
	Outcome outcome = (Outcome) getUserManagementService().findById(Outcome.class, outcomeId);
	if (outcome == null) {
	    throw new IllegalArgumentException("Can not find an outcome with ID " + outcomeId);
	}
	Integer organisationId = outcome.getOrganisation() == null ? null
		: outcome.getOrganisation().getOrganisationId();
	Integer userId = getUserDTO().getUserID();

	if (organisationId == null) {
	    if (!getSecurityService().isSysadmin(userId, "remove global outcome", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a sysadmin");
		return null;
	    }
	} else {
	    if (!getSecurityService().hasOrgRole(organisationId, userId, new String[] { Role.AUTHOR },
		    "remove course outcome", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not an author in the organisation");
		return null;
	    }
	}
	getUserManagementService().delete(outcome);
	return outcomeManage(mapping, form, request, response);
    }

    private UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private ActionErrors validateOutcomeForm(OutcomeForm outcomeForm) {
	ActionErrors errors = new ActionErrors();
	if (StringUtils.isBlank(outcomeForm.getName())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("outcome.manage.add.error.name.blank"));
	}
	if (StringUtils.isBlank(outcomeForm.getCode())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("outcome.manage.add.error.code.blank"));
	}
	if (outcomeForm.getScaleId() == null || outcomeForm.getScaleId() == 0) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("outcome.manage.add.error.scale.choose"));
	}
	return errors;
    }

    private IUserManagementService getUserManagementService() {
	if (OutcomeAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OutcomeAction.userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return OutcomeAction.userManagementService;
    }

    private ICoreLearnerService getLearnerService() {
	if (OutcomeAction.learnerService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OutcomeAction.learnerService = (ICoreLearnerService) ctx.getBean("learnerService");
	}
	return OutcomeAction.learnerService;
    }

    private ILessonService getLessonService() {
	if (OutcomeAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OutcomeAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return OutcomeAction.lessonService;
    }

    private ISecurityService getSecurityService() {
	if (OutcomeAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OutcomeAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return OutcomeAction.securityService;
    }

    private IOutcomeService getOutcomeService() {
	if (OutcomeAction.outcomeService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OutcomeAction.outcomeService = (IOutcomeService) ctx.getBean("outcomeService");
	}
	return OutcomeAction.outcomeService;
    }

    private IIntegrationService getIntegrationService() {
	if (OutcomeAction.integrationService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    OutcomeAction.integrationService = (IIntegrationService) ctx.getBean("integrationService");
	}
	return OutcomeAction.integrationService;
    }
}