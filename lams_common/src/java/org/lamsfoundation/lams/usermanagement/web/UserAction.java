package org.lamsfoundation.lams.usermanagement.web;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.usermanagement.util.AdminPreparer;
import org.lamsfoundation.lams.web.HttpSessionManager;


/**
 * @author Fei Yang
 *
 * @struts:action path="/user"
 *  			name="UserActionForm"
 * 				input=".admin.user"
 * 				scope="session"  
 * 				validate="true"
 * 
 * @struts:action-forward name="admin" path=".admin"
 * @struts:action-forward name="error" path=".admin.error"
 * 
 */
public class UserAction extends Action {

	private static Logger log = Logger.getLogger(UserAction.class);
	private static WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
	private static UserManagementService service = (UserManagementService) ctx.getBean("userManagementServiceTarget");
	

    /**
     * Perform is called when the UserActionForm is processed.
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

		UserActionForm userForm = (UserActionForm) form;
		Integer editUserId = null; // id of the user being modified
		Integer orgId = userForm.getOrgId();
		Organisation org = service.getOrganisationById(orgId);

       // -- isCancelled?
        if (isCancelled(request)) {
        	AdminPreparer.prepare(org,request,service);
        	request.getSession(true).removeAttribute(UserActionForm.formName);
			return mapping.findForward("admin");
        }

        // Okay, update the database
        if (errors.isEmpty()) 
        {
        	log.debug("Call update user, based on form data "+userForm.toMap().toString());
        	
        	User user = new User();
        	
        	try {

				BeanUtils.copyProperties(user,userForm.toMap());
				log.debug("Copied values "+user.toString());
				String authMethodName = userForm.getAuthMethodName();
				user.setAuthenticationMethod(service.getAuthenticationMethodByName(authMethodName));
				service.saveOrUpdateUser(user);
	        	editUserId = user.getUserId();
	        	UserOrganisation userOrg;
	        	if(userForm.isCreateNew()){
	        		userOrg = new UserOrganisation();
	        		userOrg.setUser(user);
	        		userOrg.setOrganisation(org);
	        		service.saveOrUpdateUserOrganisation(userOrg);
	        	}else{
	        		userOrg = service.getUserOrganisation(user.getUserId(),orgId);
	        	}
				
				// interpret the role strings returned by the checkboxes.
				String roleNames[] = userForm.getRoleNames();
				log.debug("Rolenames from form: "+roleNames);
				
				List roles = new ArrayList();
				for(int i=0; i<roleNames.length; i++){
					Role role = service.getRoleByName(roleNames[i]);
					roles.add(role);
					UserOrganisationRole userOrgRole = new UserOrganisationRole();
					userOrgRole.setUserOrganisation(userOrg);
					userOrgRole.setRole(role);
					service.saveOrUpdateUserOrganisationRole(userOrgRole);
				}
				AdminPreparer.prepare(org,request,service);	        	
			} catch (IllegalAccessException e) {
				log.error("Exception occured ",e);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.system"));				
			}			
        } // end try

        // -- Report any errors
        if (!errors.isEmpty()) {
            addErrors(request, errors);
            if (mapping.getInput()!=null)
                return (new ActionForward(mapping.getInput()));
            else{
                request.getSession(true).removeAttribute(UserActionForm.formName);
            	return (mapping.findForward("error"));
            }
        }

        request.getSession(true).removeAttribute(UserActionForm.formName);
		if(editUserId != null )
	        return (mapping.findForward("admin"));
		else
			return mapping.findForward("error");
    } // end ActionForward


} // end Action


