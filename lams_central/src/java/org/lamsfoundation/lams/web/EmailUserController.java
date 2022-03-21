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
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Emailer;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Andrey Balan, Marcin Cieslak
 *
 *
 *
 *
 */
@Controller
@RequestMapping("/emailUser")
public class EmailUserController {
    private static Logger log = Logger.getLogger(EmailUserController.class);
    private static final EmailValidator emailValidator = EmailValidator.getInstance();

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private IEventNotificationService eventNotificationService;
    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;

    @RequestMapping("/composeMail")
    public String composeMail(HttpServletRequest request) throws Exception {
	UserDTO currentUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	if (canSend(request, currentUser)) {
	    Integer userId = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID);
	    User user = (User) userManagementService.findById(User.class, userId);
	    request.setAttribute(AttributeNames.USER, user);
	    if (!EmailUserController.emailValidator.isValid(user.getEmail())) {
		EmailUserController.log.error("Recipient " + user.getLogin() + " does not have a valid email");
		saveError(request, "error.valid.email.required", true);
	    }
	} else {
	    EmailUserController.log.error("User " + currentUser.getLogin() + " is not allowed to send email");
	    saveError(request, "error.authorisation", true);
	}

	return "emailuser";
    }

    @ResponseBody
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public void send(@ModelAttribute EmailForm emailForm, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	UserDTO currentUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	if (!canSend(request, currentUser)) {
	    EmailUserController.log.error("User " + currentUser.getLogin() + " is not allowed to send email");
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().write(messageService.getMessage("error.authorisation"));
	}

	Long userId = emailForm.getUserId();

	String subject = emailForm.getSubject();
	String body = WebUtil.removeHTMLtags(emailForm.getBody());

	if (EmailUserController.log.isDebugEnabled()) {
	    EmailUserController.log.debug("User " + currentUser.getLogin() + " (" + currentUser.getEmail() + ") "
		    + " sent email to user ID " + userId + ": \n[subject] " + subject + "\n[message] " + body);
	}

	boolean IS_HTML_FORMAT = false;
	eventNotificationService.sendMessage(currentUser.getUserID(), userId.intValue(),
		IEventNotificationService.DELIVERY_METHOD_MAIL, subject, body, IS_HTML_FORMAT);

	String ccEmail = emailForm.getCcEmail();
	if (StringUtils.isNotBlank(ccEmail) && ValidationUtil.isEmailValid(ccEmail, false)) {
	    Emailer.sendFromSupportEmail(subject, ccEmail, body, IS_HTML_FORMAT);

	    if (EmailUserController.log.isDebugEnabled()) {
		EmailUserController.log.debug("User " + currentUser.getLogin() + " (" + currentUser.getEmail() + ") "
			+ " sent email to user ID " + userId + ": \n[subject] " + subject + "\n[message] " + body);
	    }
	}

    }

    private void saveError(HttpServletRequest request, String error, boolean sendDisabled) {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	errorMap.add("GLOBAL", messageService.getMessage(error));
	request.setAttribute("errorMap", errorMap);
	;
	request.setAttribute("errorsPresent", true);
	request.setAttribute("sendDisabled", sendDisabled);
    }

    private boolean canSend(HttpServletRequest request, UserDTO currentUser) {
	if (currentUser == null) {
	    currentUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	}

	boolean result = request.isUserInRole(Role.APPADMIN) || userManagementService.isUserGlobalGroupManager();
	if (!result) {
	    String orgId = request.getParameter(AttributeNames.PARAM_ORGANISATION_ID);
	    if (StringUtils.isBlank(orgId)) {
		String lessonId = request.getParameter(AttributeNames.PARAM_LESSON_ID);
		if (!StringUtils.isBlank(lessonId)) {
		    Lesson lesson = (Lesson) userManagementService.findById(Lesson.class, new Long(lessonId));
		    if (lesson != null) {
			orgId = lesson.getOrganisation().getOrganisationId().toString();
		    }
		}
	    }
	    if (!StringUtils.isBlank(orgId)) {
		result = userManagementService.isUserInRole(currentUser.getUserID(), new Integer(orgId), Role.MONITOR)
			|| userManagementService.isUserInRole(currentUser.getUserID(), new Integer(orgId),
				Role.GROUP_MANAGER);
	    }
	}

	return result;
    }
}
