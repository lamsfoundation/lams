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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.admin.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.AdminConstants;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Jun-Dir Liew
 *
 * Created at 17:00:18 on 13/06/2006
 */

/**
 * @struts:action path="/user"
 *              name="UserForm"
 *              scope="request"
 *              parameter="method"
 * 				validate="false"
 * 
 * @struts:action-forward name="user" path=".user"
 * @struts:action-forward name="userlist" path="/usermanage.do"
 * @struts:action-forward name="remove" path=".remove"
 * @struts:action-forward name="disabledlist" path="/disabledmanage.do"
 */
public class UserAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(UserAction.class);
	private static IUserManagementService service;
	private static MessageService messageService;
	private static IAuditService auditService;
	private static List<Role> rolelist;
	private static List<SupportedLocale> locales;
	
	public ActionForward edit(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		// retain orgId to return to userlist
		Integer orgId = WebUtil.readIntParam(request,"orgId");
		Organisation org = (Organisation)getService().findById(Organisation.class,orgId);
		OrganisationType orgType = org.getOrganisationType();
		Boolean isSysadmin = request.isUserInRole(Role.SYSADMIN);
		User requestor = (User)getService().getUserByLogin(request.getRemoteUser());
		Boolean hasRole = getService().isUserInRole(requestor.getUserId(), orgId, Role.COURSE_ADMIN)
							|| getService().isUserInRole(requestor.getUserId(), orgId, Role.COURSE_MANAGER);
		Boolean canEdit = org.getCourseAdminCanAddNewUsers() && hasRole;
		
		ActionMessages errors = new ActionMessages();
		
		request.setAttribute("rolelist",filterRoles(rolelist,isSysadmin, orgType));
		// set canEdit for whether user should be able to edit anything other than roles
		request.setAttribute("canEdit",isSysadmin || canEdit);
		request.setAttribute("locales",locales);

		// editing a user
		Integer userId = WebUtil.readIntParam(request,"userId",true);
		DynaActionForm userForm = (DynaActionForm)form;
		if(userId != null) {
			log.debug("got userid to edit: "+userId);
			User user = (User)getService().findById(User.class,userId);
			BeanUtils.copyProperties(userForm, user);
			userForm.set("password",null);
			
			String[] roles = null;
		    UserOrganisation uo = getService().getUserOrganisation(userId, orgId);
		    if (uo != null) {
		    	Iterator iter2 = uo.getUserOrganisationRoles().iterator();
		    	roles = new String[uo.getUserOrganisationRoles().size()];
		    	int i=0;
		    	while(iter2.hasNext()){
		    		UserOrganisationRole uor = (UserOrganisationRole)iter2.next();
		    		roles[i]=uor.getRole().getRoleId().toString();
		    		log.debug("got roleid: "+roles[i]);
		    		i++;
		    	}
		    } else {
		    	errors.add("roles", new ActionMessage("error.not.member"));
		    	saveErrors(request,errors);
		    }
	        userForm.set("roles",roles);
	        SupportedLocale locale = user.getLocale();
			userForm.set("localeId",locale.getLocaleId());
			
		}else{
			String[] roles = new String[0];
			userForm.set("roles",roles);
			try{
				String defaultLocale = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
				log.debug("defaultLocale: "+defaultLocale);
				SupportedLocale locale = getService().getSupportedLocale(defaultLocale.substring(0,2),defaultLocale.substring(3));
				userForm.set("localeId",locale.getLocaleId());
			}catch(Exception e){
                log.debug(e);				
			}
		}
		userForm.set("orgId", org.getOrganisationId());

		Organisation parentOrg = org.getParentOrganisation();
		if(parentOrg!=null){
			request.setAttribute("pOrgId",parentOrg.getOrganisationId());
			request.setAttribute("pOrgName", parentOrg.getName());
		}
		request.setAttribute("orgName",org.getName());
		request.setAttribute("orgType",orgType.getOrganisationTypeId());
		return mapping.findForward("user");
	}
	
	// determine whether to disable or delete user based on their lams data
	public ActionForward remove(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		if (!request.isUserInRole(Role.SYSADMIN)) {
			request.setAttribute("errorName","UserAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.need.sysadmin"));
			return mapping.findForward("error");
		}
		
		Integer orgId = WebUtil.readIntParam(request,"orgId");
		Integer userId = WebUtil.readIntParam(request,"userId",true);
		User user = (User)getService().findById(User.class,userId);
		
		Boolean hasData = getService().userHasData(user);

		request.setAttribute("method", (hasData?"disable":"delete"));
		request.setAttribute("orgId",orgId);
		request.setAttribute("userId",userId);
		return mapping.findForward("remove");
	}
	
	public ActionForward disable(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		if (!request.isUserInRole(Role.SYSADMIN)) {
			request.setAttribute("errorName","UserAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.need.sysadmin"));
			return mapping.findForward("error");
		}
		
		Integer orgId = WebUtil.readIntParam(request,"orgId");
		Integer userId = WebUtil.readIntParam(request,"userId",true);
		getService().disableUser(userId);
		String[] args = new String[1];
		args[0] = userId.toString();
		String message = getMessageService().getMessage("audit.user.disable", args);
		getAuditService().log(AdminConstants.MODULE_NAME, message);
		
		request.setAttribute("org",orgId);
		return mapping.findForward("userlist");
	}
	
	public ActionForward delete(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		if (!request.isUserInRole(Role.SYSADMIN)) {
			request.setAttribute("errorName","UserAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.need.sysadmin"));
			return mapping.findForward("error");
		}
		
		Integer orgId = WebUtil.readIntParam(request,"orgId");
		Integer userId = WebUtil.readIntParam(request,"userId",true);
		try {
			getService().removeUser(userId);
		} catch (Exception e) {
			request.setAttribute("errorName","UserAction");
			request.setAttribute("errorMessage",e.getMessage());
			return mapping.findForward("error");
		}
		String[] args = new String[1];
		args[0] = userId.toString();
		String message = getMessageService().getMessage("audit.user.delete", args);
		getAuditService().log(AdminConstants.MODULE_NAME, message);
		
		request.setAttribute("org",orgId);
		return mapping.findForward("userlist");
	}
	
	// called from disabled users screen
	public ActionForward enable(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		
		if (!request.isUserInRole(Role.SYSADMIN)) {
			request.setAttribute("errorName","UserAction");
			request.setAttribute("errorMessage",getMessageService().getMessage("error.need.sysadmin"));
			return mapping.findForward("error");
		}
		
		Integer userId = WebUtil.readIntParam(request,"userId",true);
		User user = (User)getService().findById(User.class,userId);
		
		log.debug("enabling user: "+userId);
		user.setDisabledFlag(false);
		getService().save(user);
				
		return mapping.findForward("disabledlist");
	}
	
	private List<Role> filterRoles(List<Role> rolelist, Boolean isSysadmin, OrganisationType orgType){
		List<Role> allRoles = new ArrayList<Role>();
		allRoles.addAll(rolelist);
		Role role = new Role();
		if(!isSysadmin) {
			role.setRoleId(Role.ROLE_SYSADMIN);
			allRoles.remove(role);
		}
		if(orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
			role.setRoleId(Role.ROLE_COURSE_ADMIN);
			allRoles.remove(role);
			role.setRoleId(Role.ROLE_COURSE_MANAGER);
			allRoles.remove(role);
		}
		return allRoles;
	}
	
	@SuppressWarnings("unchecked")
	private IUserManagementService getService(){
		if(service==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
			locales = service.findAll(SupportedLocale.class);
			Collections.sort(locales);
			rolelist = service.findAll(Role.class);
			Collections.sort(rolelist);
		}
		return service;
	}
	
	private MessageService getMessageService(){
		if(messageService==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			messageService = (MessageService)ctx.getBean("adminMessageService");
		}
		return messageService;
	}
	
	private IAuditService getAuditService(){
		if(auditService==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			auditService = (IAuditService)ctx.getBean("auditService");
		}
		return auditService;
	}
}