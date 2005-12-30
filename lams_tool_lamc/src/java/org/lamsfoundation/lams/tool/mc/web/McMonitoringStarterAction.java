
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * 
 * @author Ozgur Demirtas
 *
 */

public class McMonitoringStarterAction extends Action implements McAppConstants {
	static Logger logger = Logger.getLogger(McMonitoringStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, McApplicationException {

		IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	    logger.debug("retrieving mcService from proxy: " + mcService);
	    request.getSession().setAttribute(TOOL_SERVICE, mcService);		

		McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;

	    /*
	     * mark the http session as a monitoring activity 
	     */
	    request.getSession().setAttribute(TARGET_MODE,TARGET_MODE_MONITORING);
	    
	    /*
	     * persist time zone information to session scope. 
	     */
	    McUtils.persistTimeZone(request);
	    ActionForward validateParameters=validateParameters(request, mapping);
	    logger.debug("validateParamaters: " + validateParameters);
	    if (validateParameters != null)
	    {
	    	return validateParameters;
	    }
  
	    Long toolSessionID=(Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
	    logger.debug("retrieved toolSessionID: " + toolSessionID);
    	
	    logger.debug("forwarding to: " + LOAD_MONITORING);
	    return (mapping.findForward(LOAD_MONITORING));	
	}
	
	
	protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping)
	{
		/*
	     * obtain and setup the current user's data 
	     */
	    String userID = "";
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if ((user == null) || (user.getUserID() == null))
	    {
	    	logger.debug("error: The tool expects userId");
	    	persistError(request,"error.learningUser.notAvailable");
	    	request.setAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE, new Boolean(true));
			return (mapping.findForward(ERROR_LIST));
	    }else
	    	userID = user.getUserID().toString();
	    
	    logger.debug("retrieved userId: " + userID);
    	request.getSession().setAttribute(USER_ID, userID);
		
	    
	    /*
	     * process incoming tool session id and later derive toolContentId from it. 
	     */
	    //String strToolSessionId=request.getParameter(TOOL_SESSION_ID);
    	String strToolSessionId=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	    long toolSessionId=0;
	    if ((strToolSessionId == null) || (strToolSessionId.length() == 0)) 
	    {
	    	persistError(request, "error.toolSessionId.required");
	    	request.setAttribute(USER_EXCEPTION_TOOLSESSIONID_REQUIRED, new Boolean(true));
			return (mapping.findForward(ERROR_LIST));
	    }
	    else
	    {
	    	try
			{
	    		toolSessionId=new Long(strToolSessionId).longValue();
		    	logger.debug("passed TOOL_SESSION_ID : " + new Long(toolSessionId));
		    	request.getSession().setAttribute(TOOL_SESSION_ID,new Long(toolSessionId));	
			}
	    	catch(NumberFormatException e)
			{
	    		persistError(request, "error.sessionId.numberFormatException");
	    		logger.debug("add error.sessionId.numberFormatException to ActionMessages.");
				request.setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true));
				return (mapping.findForward(ERROR_LIST));
			}
	    }
	    return null;
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
