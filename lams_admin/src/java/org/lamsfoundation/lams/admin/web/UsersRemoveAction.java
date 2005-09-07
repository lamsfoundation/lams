package org.lamsfoundation.lams.admin.web;

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
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Fei Yang
 *
 * @struts:action path="usersremove"
 *  			name="UsersRemoveActionForm"
 * 				input=".admin.usersremove"
 * 				scope="session"  
 * 				validate="false"
 * 
 * @struts:action-forward name="admin" path=".admin"
 * @struts:action-forward name="error" path=".admin.error"
 */
public class UsersRemoveAction extends Action {

	private static Logger log = Logger.getLogger(UsersRemoveAction.class);
	private static WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
	private static UserManagementService service = (UserManagementService) ctx.getBean("userManagementServiceTarget");
	
    /**
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     */
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception
  	{

        ActionErrors errors = new ActionErrors();

		UsersRemoveActionForm dataForm = (UsersRemoveActionForm) form;
		Integer orgId = null;
		Organisation org = null;
		orgId = dataForm.getOrgId();
		if ( orgId == null || orgId.intValue()==-1){
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.system"));
		}
		else{
			org = service.getOrganisationById(orgId);
		}
       // -- isCancelled?
        if (isCancelled(request)) {
        	AdminPreparer.prepare(org,request,service);
        	request.getSession(true).removeAttribute(UsersRemoveActionForm.formName);
			return mapping.findForward("admin");
        }

		
        // Okay, update the database
        if (errors.isEmpty()) 
        {
        	try {
        		log.debug("dataForm is "+dataForm);
				Integer[] toRemove = dataForm.getToRemove();
				for (int i = 0; i < toRemove.length; i++) {
					service.removeUserOrganisation(service.getUserOrganisation(toRemove[i],orgId));
				}
			} catch (Exception e) {
				log.error("Exception occured ",e);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.system"));				
        	}
			
        } // end if errors empty

        // -- Report any errors
        if (!errors.isEmpty()) {
            addErrors(request, errors);
            if (mapping.getInput()!=null)
                return (new ActionForward(mapping.getInput()));
            // If no input page, use error forwarding
            request.getSession(true).removeAttribute(UsersRemoveActionForm.formName);
            return (mapping.findForward("error"));
        }

        request.getSession(true).removeAttribute(UsersRemoveActionForm.formName);
        AdminPreparer.prepare(org,request,service);
        return ( mapping.findForward("admin") );
    }


} // end Action


