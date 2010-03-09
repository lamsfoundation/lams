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
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.AdminConstants;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.dto.UserOrgRoleDTO;
import org.lamsfoundation.lams.admin.web.form.EmailForm;
import org.lamsfoundation.lams.events.DeliveryMethodMail;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.service.IThemeService;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Emailer;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Andrey Balan
 * 
 * @struts:action path="/emailUser" name="emailForm" scope="request"
 *                parameter="method" validate="false"
 * 
 * @struts:action-forward name="emailuser" path=".emailuser"
 * @struts:action-forward name="usersearch" path="/usersearch.do"
 */
public class EmailUserAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(EmailUserAction.class);
    private IUserManagementService service;
    private MessageService messageService;
    private IEventNotificationService eventNotificationService;

    private void initServices() {
	if (service == null) {
	    service = AdminServiceProxy.getService(getServlet().getServletContext());
	}
	if (messageService == null) {
	    messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
	}
	if (eventNotificationService == null) {
	    eventNotificationService = AdminServiceProxy.getEventNotificationService(getServlet().getServletContext());
	}
    }
    
    public ActionForward composeMail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	initServices();
	
	if (!(request.isUserInRole(Role.SYSADMIN) || service.isUserGlobalGroupAdmin())) {
	    request.setAttribute("errorName", "UserAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}
	
	Integer userId = WebUtil.readIntParam(request, "userId");
	User user = (User) service.findById(User.class, userId);
	request.setAttribute("user", user);

	return mapping.findForward("emailuser");
    }
    
    public ActionForward send(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	initServices();
	
	if (!(request.isUserInRole(Role.SYSADMIN) || service.isUserGlobalGroupAdmin())) {
	    request.setAttribute("errorName", "UserAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}
	
	EmailForm emailForm = (EmailForm) form;
	Integer userId = emailForm.getUserId();
	User user = (User) service.findById(User.class, userId);
	
	//String to = WebUtil.readStrParam(request, "to");
	String subject = emailForm.getSubject();
	String body = emailForm.getBody();

	HttpSession ss1 = SessionManager.getSession();
	UserDTO administrator = (UserDTO) ss1.getAttribute(AttributeNames.USER);
	
	EmailUserAction.log.debug("Administrator " + administrator.getFirstName() + " " + administrator.getLastName()
		+ " sent email to user " + user.getFirstName() + " " + user.getLastName() + ": \n[subject] " + subject + "\n[message] " + body);
	Properties properties = new Properties();
	Emailer.send(subject, user.getEmail(), administrator.getEmail(), body, properties);
	
	return mapping.findForward("usersearch");
    }

}
