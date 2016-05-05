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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.contentrepository.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
    protected ActionForward returnError(ActionMapping mapping, HttpServletRequest request, String errorKey) {
	ActionMessages am = new ActionMessages();
	am.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errorKey));
	saveErrors(request, am);
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
	} catch (NumberFormatException e) {
	    return null;
	}
    }

    /**
     * Logout of the workspace. Doesn't need a form.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws RepositoryCheckedException {

	log.debug("In logout");

	ITicket ticket = RepositoryDispatchAction.getTicket(request);
	log.debug("In getNode, ticket is " + ticket);
	if (ticket == null) {
	    log.error("Ticket missing from session");
	    return returnError(mapping, request, "error.noTicket");
	}

	log.debug("About to logout");

	getRepository().logout(ticket);

	log.debug("Logged out to " + mapping.findForward(LOGOUT_PATH));
	return mapping.findForward(LOGOUT_PATH);

    }

    public IRepositoryService getRepository() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(this.getServlet().getServletContext());
	return (IRepositoryService) wac.getBean(IRepositoryService.REPOSITORY_SERVICE_ID);
    }

}