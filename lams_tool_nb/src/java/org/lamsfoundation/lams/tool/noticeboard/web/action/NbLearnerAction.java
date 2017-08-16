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


package org.lamsfoundation.lams.tool.noticeboard.web.action;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;
import org.lamsfoundation.lams.tool.noticeboard.util.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.util.NbWebUtil;
import org.lamsfoundation.lams.tool.noticeboard.web.form.NbLearnerForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Creation Date: 29-06-05
 * 
 * This class has been created so that when a learner finishes an activity,
 * leaveToolSession() will be called to inform the progress engine
 * that the user has completed this activity.
 *
 * A note: at the time of writing (11-08-05) a null pointer exception
 * occurs when making a call to leaveToolSession(). Will have to wait until
 * the learner side of things is tested first.
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class NbLearnerAction extends LamsDispatchAction {

    static Logger logger = Logger.getLogger(NbLearnerAction.class.getName());

    /** Get the user id from the shared session */
    public Long getUserID(HttpServletRequest request) {
	// set up the user details
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if (user == null) {
	    MessageResources resources = getResources(request);
	    String error = resources.getMessage(NoticeboardConstants.ERR_MISSING_PARAM, "User");
	    logger.error(error);
	    throw new NbApplicationException(error);
	}
	return new Long(user.getUserID().longValue());
    }

    /**
     * Indicates that the user has finished viewing the noticeboard.
     * The session is set to complete and leaveToolSession is called.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response)
	    throws NbApplicationException, ToolException, DataMissingException, ServletException, IOException {

	NbLearnerForm learnerForm = (NbLearnerForm) form;
	Long userID = getUserID(request);

	Long toolSessionID = NbWebUtil.convertToLong(learnerForm.getToolSessionID());
	if (toolSessionID == null) {
	    String error = "Unable to continue. The parameters tool session id is missing";
	    logger.error(error);
	    throw new NbApplicationException(error);
	}

	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
	ToolSessionManager sessionMgrService = NoticeboardServiceProxy
		.getNbSessionManager(getServlet().getServletContext());

	ToolAccessMode mode = WebUtil.getToolAccessMode(learnerForm.getMode());
	if (mode == ToolAccessMode.LEARNER || mode == ToolAccessMode.AUTHOR) {
	    NoticeboardSession nbSession = nbService.retrieveNoticeboardSession(toolSessionID);
	    NoticeboardUser nbUser = nbService.retrieveNbUserBySession(userID, toolSessionID);

	    nbUser.setUserStatus(NoticeboardUser.COMPLETED);
	    nbService.updateNoticeboardSession(nbSession);
	    nbService.updateNoticeboardUser(nbUser);

	    // Create the notebook entry if reflection is set.
	    NoticeboardContent nbContent = nbService.retrieveNoticeboardBySessionID(toolSessionID);
	    if (nbContent.getReflectOnActivity()) {
		// check for existing notebook entry
		NotebookEntry entry = nbService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
			NoticeboardConstants.TOOL_SIGNATURE, userID.intValue());

		if (entry == null) {
		    // create new entry
		    nbService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
			    NoticeboardConstants.TOOL_SIGNATURE, userID.intValue(), learnerForm.getReflectionText());
		} else {
		    // update existing entry
		    entry.setEntry(learnerForm.getReflectionText());
		    entry.setLastModified(new Date());
		    nbService.updateEntry(entry);
		}
	    }

	    String nextActivityUrl;
	    try {
		nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, getUserID(request));
	    } catch (DataMissingException e) {
		log.error(e);
		throw new ServletException(e);
	    } catch (ToolException e) {
		log.error(e);
		throw new ServletException(e);
	    }

	    response.sendRedirect(nextActivityUrl);

	    return null;

	}
	request.setAttribute(NoticeboardConstants.READ_ONLY_MODE, "true");

	return mapping.findForward(NoticeboardConstants.DISPLAY_LEARNER_CONTENT);

    }

    /**
     * Indicates that the user has finished viewing the noticeboard, and will be
     * passed onto the Notebook reflection screen.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward reflect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NbApplicationException {

	INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());

	NbLearnerForm learnerForm = (NbLearnerForm) form;
	Long toolSessionID = NbWebUtil.convertToLong(learnerForm.getToolSessionID());
	NoticeboardContent nbContent = nbService.retrieveNoticeboardBySessionID(toolSessionID);
	request.setAttribute("reflectInstructions", nbContent.getReflectInstructions());
	request.setAttribute("title", nbContent.getTitle());
	request.setAttribute("allowComments", nbContent.isAllowComments());
	request.setAttribute("likeAndDislike", nbContent.isCommentsLikeAndDislike());

	// get the existing reflection entry
	NotebookEntry entry = nbService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		NoticeboardConstants.TOOL_SIGNATURE, getUserID(request).intValue());
	if (entry != null) {
	    request.setAttribute("reflectEntry", entry.getEntry());
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request,
		getServlet().getServletContext());

	return mapping.findForward(NoticeboardConstants.REFLECT_ON_ACTIVITY);
    }
}