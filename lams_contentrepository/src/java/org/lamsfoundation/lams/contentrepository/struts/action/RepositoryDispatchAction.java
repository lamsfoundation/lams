//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.contentrepository.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;


/** 
 * Base class for the dispatch actions used in the repository tests.
 * Shares functionality such as checking ticket.
 */
public class RepositoryDispatchAction extends DispatchAction {

	public final static String TICKET_NAME = "ticket";
	
	public static final String NODE_LIST_NAME = "nodeList";
	public static final String UUID_NAME = "uuid";
	public static final String VERSION_NAME = "version";
	
	public static final String SUCCESS_PATH = "success";
	public static final String ERROR_PATH = "error";
	public static final String LOGOUT_PATH = "logout";

	public static final String PACKAGE_LIST = "packageList";

	protected static Logger log = Logger.getLogger(RepositoryDispatchAction.class);
	
	/** Adds this error to the errors, then goes to the error forward */
	protected ActionForward returnError(ActionMapping mapping, 
			HttpServletRequest request, ActionError error) {
		ActionErrors errors = new ActionErrors();
		errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		saveErrors(request,errors);		
		return mapping.findForward(ERROR_PATH);
	}

	protected static ITicket getTicket(HttpServletRequest request) {
		return (ITicket) request.getSession().getAttribute(TICKET_NAME);
	}

	protected static void setTicket(HttpServletRequest request, ITicket ticket) {
		request.getSession().setAttribute(TICKET_NAME, ticket);
	}
	
	protected static Long getLong(String longAsString) {
		try {
			return new Long(longAsString);
		} catch ( NumberFormatException e ) {
			return null;
		}
	}

	/** 
	 * Logout of the workspace. Doesn't need a form.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward logout(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response) throws RepositoryCheckedException {

		log.debug("In logout");

    	ITicket ticket = getTicket(request);
		log.debug("In getNode, ticket is "+ticket);
		if ( ticket == null ) {
			log.error("Ticket missing from session");
        	return returnError(mapping, request, 
        			new ActionError("error.noTicket"));
		} 

		log.debug("About to logout");

		Download.getRepository().logout(ticket);
		
		log.debug("Logged out to "+mapping.findForward(LOGOUT_PATH));
		return mapping.findForward(LOGOUT_PATH);
	
	}
}