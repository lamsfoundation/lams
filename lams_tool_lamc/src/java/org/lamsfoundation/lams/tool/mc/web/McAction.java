/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.service.IMcService;


/**
 * 
 * change the logic about completion status
 *
 */

/**
 * 
 * once lams_learning is ready and appContext file is src/ then FINISH toool session will work.
 * 
 */

/**
 * 
 * done: removed styling, except error messages and table centering
 * 
 */

/**
 * The tool's Spring configuration file: qaCompactApplicationContext.xml
 * Main service bean of the tool is: org.lamsfoundation.lams.tool.qa.service.McServicePOJO
 * 
 * done: config file is read from classpath
 */


/**
 * 
 * the tool's web.xml will be modified to have classpath to learning service.
 * This is how the tool gets the definition of "learnerService"
 */

/**
 * 
 * when to reset define later and synchin monitor etc..
 *  
 */

/** make sure the tool gets called on:
 *	setAsForceComplete(Long userId) throws McApplicationException 
 */


/**
 * 
 * User Issue:
 * Right now:
 * 1- the tool gets the request object from the container.
 * 2- Principal principal = req.getUserPrincipal();
 * 3- String username = principal.getName();
 * 4- User userCompleteData = qaService.getCurrentUserData(userName);
 * 5- write back userCompleteData.getUserId()
 */


/**
 * 
 * JBoss Issue: 
 * Currently getUserPrincipal() returns null and ServletRequest.isUserInRole() always returns false on unsecured pages, 
 * even after the user has been authenticated.
 * http://jira.jboss.com/jira/browse/JBWEB-19 
 */


/**
 * eliminate calls:
 * authoringUtil.simulatePropertyInspector_RunOffline(request);
 * authoringUtil.simulatePropertyInspector_setAsDefineLater(request);
 */


/**
 * 
 * @author ozgurd
 *
 * TOOL PARAMETERS: ?? (toolAccessMode) ??
 * Authoring environment: toolContentId
 * Learning environment: toolSessionId + toolContentId  
 * Monitoring environment: toolContentId / Contribute tab:toolSessionId(s)
 * 	 
 * 
 */

/**
 * Note: the tool must support deletion of an existing content from within the authoring environment.
 * The current support for this is by implementing the tool contract : removeToolContent(Long toolContentId)
 */


/**
 * 
 * We have had to simulate container bahaviour in development stage by calling 
 * createToolSession and leaveToolSession from the web layer. These will go once the tool is 
 * in deployment environment.
 * 
 * 
 * CHECK: leaveToolSession and relavent LearnerService may need to be defined in the spring config file.
 * 
 */


/**
 * 
 * GROUPING SUPPORT: Find out what to do.
 */


/**
 * <p>Action class that controls the logic of tool behavior. </p>
 * 
 * <p>Note that Struts action class only has the responsibility to navigate 
 * page flow. All database operation should go to service layer and data 
 * transformation from domain model to struts form bean should go to form 
 * bean class. This ensure clean and maintainable code.
 * </p>
 * 
 * <code>SystemException</code> is thrown whenever an known error condition is
 * identified. No system exception error handling code should appear in the 
 * Struts action class as all of them are handled in 
 * <code>CustomStrutsExceptionHandler<code>.
 * 
 * @author Ozgur Demirtas
 */
public class McAction extends DispatchAction implements McAppConstants
{
	static Logger logger = Logger.getLogger(McAction.class.getName());
	
    /** 
     * <p>Struts dispatch method.</p> 
     * 
     * <p>It is assuming that progress engine should pass in the tool access
     * mode and the tool session id as http parameters.</p>
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     *
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     * @throws McApplicationException the known runtime exception 
     * 
     */
    
	/**
	 * loadQ(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
                                                            
	 * return ActionForward
	 * main content/question content management and workflow logic
	 * 
	 * if the passed toolContentId exists in the db, we need to get the relevant data into the Map 
	 * if not, create the default Map 
	*/
	
    public ActionForward loadQ(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
    {
    	
		logger.debug("loadQ initialised...");
	 	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	 	
	 	logger.debug("mcAuthoringForm.getEditDefaultQuestion():" + mcAuthoringForm.getEditDefaultQuestion());
	 	String userAction=null;
	 	if (mcAuthoringForm.getEditDefaultQuestion() != null)
	 	{
	 		userAction="editDefaultQuestion";
	 		
	 	}
	 	logger.debug("userAction:" + userAction);

	 	IMcService mcService =McUtils.getToolService(request);
	 	logger.debug("mcService:" + mcService);
   	    return (mapping.findForward(LOAD_QUESTIONS));
    }
    

    /**
     * persists error messages to request scope
     * @param request
     * @param message
     */
    public void persistError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	} 
}
