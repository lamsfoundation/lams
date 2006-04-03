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
package org.lamsfoundation.lams.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.usermanagement.service.UserManagementService;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;


import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;

/**
 * @author Fei Yang
 * 
 * @struts:action path="/passwordChanged"
 *  			name="PasswordChangeActionForm"
 * 				input=".passwordChange"
 * 				validate="true"
 * 
 * @struts:action-forward name="okay" path=".passwordChangeOk"
 * @struts:action-forward name="cancelled" path="/index.jsp"
 */
public class PasswordChangeAction extends Action {

	private static Logger log = Logger.getLogger(PasswordChangeAction.class);
	
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
                 HttpServletResponse response)
  	  throws Exception 
  	{
       // -- isCancelled?
        if (isCancelled(request)) {
        	request.getSession().removeAttribute(PasswordChangeActionForm.formName);
        	return mapping.findForward("cancelled");
        }


        ActionErrors errors = new ActionErrors();

		PasswordChangeActionForm passwordChangeForm = (PasswordChangeActionForm) form;
		
        if (errors.isEmpty()) 
        {
        	try {
		        	
	        	String loggedInUser = request.getRemoteUser();
	        	String login = passwordChangeForm.getLogin();
	        	String oldPassword = passwordChangeForm.getOldPassword();
	        	String password = passwordChangeForm.getPassword();
	        	
	    	    if ( loggedInUser == null || ! loggedInUser.equals(login) )
	    	    {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.authorisation"));
	    	    }
	    	    else 
	    	    {

					WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession(true).getServletContext());
					UserManagementService service = (UserManagementService)ctx.getBean("userManagementServiceTarget");
					if(!service.getUserByLogin(login).getPassword().equals(oldPassword))
					{
						errors.add("oldPassword", new ActionMessage("error.oldpassword.mismatch"));
					}
					else
					{
			        	service.updatePassword(login, password );
					}
	    	    }
		        	
			} catch (Exception e) {
				log.error("Exception occured ",e);
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(e.getMessage()));				
			}
			
        } // end if no errors

        // -- Report any errors
        if (!errors.isEmpty()) {
            addErrors(request, errors);
            if (mapping.getInput()!=null)
            {
            	passwordChangeForm.reset(mapping,request);
                return (new ActionForward(mapping.getInput()));
            }
            // If no input page, use error forwarding
            return (mapping.findForward("error"));
        }
		return mapping.findForward("okay");

    } 
} 


