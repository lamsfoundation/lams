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


package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Emailer;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Andrey Balan, Marcin Cieslak
 *
 *
 *
 *
 */
public class EmailUserAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(EmailUserAction.class);
    private static final EmailValidator emailValidator = EmailValidator.getInstance();
    private static IUserManagementService userManagementService;
    private static IEventNotificationService eventNotificationService;
    private static MessageService messageService;

    public ActionForward composeMail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	UserDTO currentUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	if (canSend(request, currentUser)) {
	    Integer userId = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID);
	    User user = (User) getUserManagementService().findById(User.class, userId);
	    request.setAttribute(AttributeNames.USER, user);
	    if (!EmailUserAction.emailValidator.isValid(user.getEmail())) {
		EmailUserAction.log.error("Recipient " + user.getLogin() + " does not have a valid email");
		saveError(request, "error.valid.email.required", true);
	    }
	} else {
	    EmailUserAction.log.error("User " + currentUser.getLogin() + " is not allowed to send email");
	    saveError(request, "error.authorisation", true);
	}

	return mapping.findForward("emailuser");
    }

    public ActionForward send(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	UserDTO currentUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	if (!canSend(request, currentUser)) {
	    EmailUserAction.log.error("User " + currentUser.getLogin() + " is not allowed to send email");
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().write(getMessageService().getMessage("error.authorisation"));
	    return null;
	}

	EmailForm emailForm = (EmailForm) form;
	Long userId = emailForm.getUserId();

	String subject = emailForm.getSubject();
	String body = WebUtil.removeHTMLtags(emailForm.getBody());

	if (EmailUserAction.log.isDebugEnabled()) {
	    EmailUserAction.log.debug("User " + currentUser.getLogin() + " (" + currentUser.getEmail() + ") "
		    + " sent email to user ID " + userId + ": \n[subject] " + subject + "\n[message] " + body);
	}

	boolean IS_HTML_FORMAT = false;
	getEventNotificationService().sendMessage(currentUser.getUserID(), userId.intValue(),
		IEventNotificationService.DELIVERY_METHOD_MAIL, subject, body, IS_HTML_FORMAT);

	String ccEmail = emailForm.getCcEmail();
	if (StringUtils.isNotBlank(ccEmail) && ValidationUtil.isEmailValid(ccEmail, false)) {
	    Emailer.sendFromSupportEmail(subject, ccEmail, body, IS_HTML_FORMAT);

	    if (EmailUserAction.log.isDebugEnabled()) {
		EmailUserAction.log.debug("User " + currentUser.getLogin() + " (" + currentUser.getEmail() + ") "
			+ " sent email to user ID " + userId + ": \n[subject] " + subject + "\n[message] " + body);
	    }
	}

	return null;
    }

    private void saveError(HttpServletRequest request, String error, boolean sendDisabled) {
	ActionMessages errors = new ActionMessages();
	errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(error));
	saveErrors(request, errors);
	request.setAttribute("errorsPresent", true);
	request.setAttribute("sendDisabled", sendDisabled);
    }

    private IEventNotificationService getEventNotificationService() {
	if (EmailUserAction.eventNotificationService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    EmailUserAction.eventNotificationService = (IEventNotificationService) ctx
		    .getBean("eventNotificationService");
	}
	return EmailUserAction.eventNotificationService;
    }

    private MessageService getMessageService() {
	if (EmailUserAction.messageService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    EmailUserAction.messageService = (MessageService) ctx
		    .getBean(CentralConstants.CENTRAL_MESSAGE_SERVICE_BEAN_NAME);

	}
	return EmailUserAction.messageService;
    }

    private IUserManagementService getUserManagementService() {
	if (EmailUserAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    EmailUserAction.userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return EmailUserAction.userManagementService;
    }

    private boolean canSend(HttpServletRequest request, UserDTO currentUser) {
	if (currentUser == null) {
	    currentUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	}

	boolean result = request.isUserInRole(Role.SYSADMIN) || getUserManagementService().isUserGlobalGroupAdmin();
	if (!result) {
	    String orgId = request.getParameter(AttributeNames.PARAM_ORGANISATION_ID);
	    if (StringUtils.isBlank(orgId)) {
		String lessonId = request.getParameter(AttributeNames.PARAM_LESSON_ID);
		if (!StringUtils.isBlank(lessonId)) {
		    Lesson lesson = (Lesson) getUserManagementService().findById(Lesson.class, new Long(lessonId));
		    if (lesson != null) {
			orgId = lesson.getOrganisation().getOrganisationId().toString();
		    }
		}
	    }
	    if (!StringUtils.isBlank(orgId)) {
		result = getUserManagementService().isUserInRole(currentUser.getUserID(), new Integer(orgId),
			Role.MONITOR)
			|| getUserManagementService().isUserInRole(currentUser.getUserID(), new Integer(orgId),
				Role.GROUP_MANAGER);
	    }
	}

	return result;
    }
}