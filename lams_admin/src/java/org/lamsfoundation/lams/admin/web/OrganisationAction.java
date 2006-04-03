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

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
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
import org.lamsfoundation.lams.usermanagement.OrganisationType;
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
 * @struts:action path="/admin/organisation"
 *  			name="OrganisationActionForm"
 * 				input=".admin.organisation"
 * 				scope="session"  
 * 				validate="true"
 * 
 * @struts:action-forward name="admin" path=".admin"
 * @struts:action-forward name="error" path=".admin.error"
 */
public class OrganisationAction extends Action {

	private static Logger log = Logger.getLogger(OrganisationAction.class);
	private static WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
	private static UserManagementService service = (UserManagementService) ctx.getBean("userManagementServiceTarget");
	
    /**
     * Perform is called when the OrganisationActionForm is processed.
     * 
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
  	{

        ActionErrors errors = new ActionErrors();
		log.debug("Form type is "+form.getClass().toString());
		log.debug("Form is "+form.toString());

		OrganisationActionForm orgForm = (OrganisationActionForm) form;
    	Organisation org = null;
		
       // -- isCancelled?
        if (isCancelled(request)) {
        	if(orgForm.getOrgId().intValue()!=-1){
        		org=service.getOrganisationById(orgForm.getOrgId());
        	}else{
        		org=service.getOrganisationById(orgForm.getParentOrgId());
        	}
        	AdminPreparer.prepare(org,request,service);	
        	request.getSession(true).removeAttribute(OrganisationActionForm.formName);
			return mapping.findForward("admin");
        }

        if (errors.isEmpty()) 
        {
        	log.debug("Call update organisation, based on form data "+orgForm);
        	
        	try {
	        	if(orgForm.getOrgId().intValue()!=-1){//edit organisation
	        		org = service.getOrganisationById(orgForm.getOrgId());
	        		org.setName(orgForm.getName());
	        		org.setDescription(orgForm.getDescription());
	        		service.saveOrUpdateOrganisation(org);
	        	}else{//create child organisation
	        		org = new Organisation();
	        		org.setName(orgForm.getName());
	        		org.setDescription(orgForm.getDescription());
	        		Organisation parentOrg = service.getOrganisationById(orgForm.getParentOrgId());
	        		org.setParentOrganisation(parentOrg);
	        		org.setCreateDate(new Date());
	        		OrganisationType orgType;
	        		if(parentOrg.getOrganisationType().getName().equals(OrganisationType.ROOT)){
	        			orgType = service.getOrganisationTypeByName(OrganisationType.BASE);
	        		}else{
	        			orgType = service.getOrganisationTypeByName(OrganisationType.SUB);
	        		}
	        		org.setOrganisationType(orgType);
	        		service.saveOrUpdateOrganisation(org);
	        		UserOrganisation userOrg = new UserOrganisation();
	        		User user = service.getUserByLogin(request.getRemoteUser());
	        		userOrg.setUser(user);
	        		userOrg.setOrganisation(org);
	        		service.saveOrUpdateUserOrganisation(userOrg);
	        		UserOrganisationRole userOrgRole = new UserOrganisationRole();
	        		userOrgRole.setUserOrganisation(userOrg);
	        		userOrgRole.setRole(service.getRoleByName(Role.ADMIN));
	        		service.saveOrUpdateUserOrganisationRole(userOrgRole);
	        	}
				AdminPreparer.prepare(org,request,service);
			} catch (Exception e) {
				log.error("Exception happened when getOrganisationEdit",e);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getMessage()));				
			}
			
        } 

        // -- Report any errors
        if (!errors.isEmpty()) {
            addErrors(request, errors);
            if (mapping.getInput()!=null)
                return (new ActionForward(mapping.getInput()));
            // If no input page, use error forwarding
            return (mapping.findForward("error"));
        }else{
        	return mapping.findForward("admin");
        }


    } // end ActionForward


} // end Action


