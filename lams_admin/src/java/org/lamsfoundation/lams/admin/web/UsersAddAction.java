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
/* $$Id$$ */
package org.lamsfoundation.lams.admin.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.admin.util.AdminPreparer;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Fei Yang
 *
 * @struts:action path="/usersadd"
 *  			name="UsersAddActionForm"
 * 				input=".admin.usersadd"
 * 				scope="session"  
 * 				validate="false"
 * 
 * @struts:action-forward name="admin" path=".admin"
 * @struts:action-forward name="usersadd" path=".admin.usersadd"
 * @struts:action-forward name="error" path=".admin.error"
 */
public class UsersAddAction extends Action {

	private static Logger log = Logger.getLogger(UsersAddAction.class);
	private static WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
	private static UserManagementService service = (UserManagementService) ctx.getBean("userManagementServiceTarget");
	
	public static final String SUBMIT_SHOW_USERS = "Show Users";
	public static final String SUBMIT_ADD_USERS = "Add Selected Users";
	
    /**
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception Exception if any error occurs
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
  	{

		UsersAddActionForm dataForm = (UsersAddActionForm) form;
       // -- isCancelled?
        if (isCancelled(request)) {
        	Organisation org = service.getOrganisationById(dataForm.getOrgId());
        	AdminPreparer.prepare(org,request,service);
        	request.getSession(true).removeAttribute(UsersAddActionForm.formName);
			return mapping.findForward("admin");
        }

        ActionErrors errors = new ActionErrors();

		Integer orgId = null;
		boolean finished = false;
		
        // Okay, update the database
        if (errors.isEmpty()) 
        {
        	try {
        		log.debug("dataForm is "+dataForm);
        		orgId = dataForm.getOrgId();
        		if ( orgId == null || orgId.intValue()==-1)
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.system"));				
        		else{
    				if ( isShowUsers(request) )
    				{
    					addUsersToForm(request, dataForm);
    				}
    				else
    				{
    					addUsersToOrg(request, dataForm,errors);
    					finished = true;
    				}					
        		}
			} catch (Exception e) {
				log.error("Exception occured ",e);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.system"));				
        	}
			
        } // end if errors empty

        // -- Report any errors
        if (!errors.isEmpty()) {
            addErrors(request, errors);
            request.getSession(true).removeAttribute(UsersAddActionForm.formName);
            if (mapping.getInput()!=null)
                return (new ActionForward(mapping.getInput()));
            // If no input page, use error forwarding
            return (mapping.findForward("error"));
        }

		if ( finished )
		{
	        request.getSession(true).removeAttribute(UsersAddActionForm.formName);
			Organisation org = service.getOrganisationById(orgId);
			AdminPreparer.prepare(org,request,service);
			return ( mapping.findForward("admin") );
		}
		else
		{
			return ( mapping.findForward("usersadd"));
		}
    }

	private void addUsersToOrg(HttpServletRequest request, 
			UsersAddActionForm dataForm,ActionErrors errors)
	{
		Integer orgId = dataForm.getOrgId();
		Organisation org = service.getOrganisationById(orgId);
		String[] potentialRoleNames = dataForm.getPotentialRoleNames();
		String lastLogin="";
		UserOrganisation lastUserOrg = null;
		for(int i=0; i<potentialRoleNames.length; i++){
			int index = potentialRoleNames[i].indexOf("_");
			String login = potentialRoleNames[i].substring(0,index).trim();
			String roleName = potentialRoleNames[i].substring(index+1).trim();
			if(!login.equals(lastLogin)){
				UserOrganisation userOrg = new UserOrganisation();
				userOrg.setUser(service.getUserByLogin(login));
				userOrg.setOrganisation(org);
				service.saveOrUpdateUserOrganisation(userOrg);
				lastUserOrg = userOrg;
				lastLogin=login;
			}
			Role role = service.getRoleByName(roleName);
			UserOrganisationRole userOrgRole = new UserOrganisationRole();
			userOrgRole.setRole(role);
			userOrgRole.setUserOrganisation(lastUserOrg);
			service.saveOrUpdateUserOrganisationRole(userOrgRole);
		}
	}



	private void addUsersToForm(HttpServletRequest request, 
			UsersAddActionForm dataForm)
	{
		Integer orgId = dataForm.getOrgId();
		Integer selectedOrgId = dataForm.getSelectedOrgId();
		Map potentialRoleNames = new HashMap();
		if ( selectedOrgId != null )
		{
			List existingUsers = service.getUsersFromOrganisation(orgId);
			List users = service.getUsersFromOrganisation(selectedOrgId);
			if ( users != null )
			{
				//remove users who already exists in current organisation
				for(int i=0; i<users.size(); i++ ){
					User user = (User)users.get(i);
					Iterator iter2 = existingUsers.iterator();
					while(iter2.hasNext()){
						User existingUser = (User)iter2.next();
						if(user.getUserId().equals(existingUser.getUserId())){
							users.remove(i);
						}
					}
				}
				log.debug("Adding users to form "+users.toString());
				dataForm.setPotentialRoleNames(new String[0]);
				dataForm.setPotentialUsers(users);
			}
		}
	}
	
	private boolean isShowUsers(HttpServletRequest request)
	{
		String submitValue = request.getParameter("submit");
		log.debug("Submit value is "+submitValue);
		return SUBMIT_SHOW_USERS.equals(submitValue);
	}

} // end Action


