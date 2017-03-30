/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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


package org.lamsfoundation.lams.tool.leaderselection.web.actions;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.lamsfoundation.lams.tool.leaderselection.service.ILeaderselectionService;
import org.lamsfoundation.lams.tool.leaderselection.service.LeaderselectionServiceProxy;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants;
import org.lamsfoundation.lams.tool.leaderselection.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 *
 *
 *
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public ILeaderselectionService leaderselectionService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";

    /**
     * Default method when no dispatch parameter is specified. It is expected that the parameter
     * <code>toolContentID</code> will be passed in. This will be used to retrieve content for this tool.
     *
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// set up leaderselectionService
	if (leaderselectionService == null) {
	    leaderselectionService = LeaderselectionServiceProxy
		    .getLeaderselectionService(this.getServlet().getServletContext());
	}

	// retrieving Leaderselection with given toolContentID
	Leaderselection leaderselection = leaderselectionService.getContentByContentId(toolContentID);
	if (leaderselection == null) {
	    leaderselection = leaderselectionService.copyDefaultContent(toolContentID);
	    leaderselection.setCreateDate(new Date());
	    leaderselectionService.saveOrUpdateLeaderselection(leaderselection);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	if (mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    leaderselection.setDefineLater(true);
	    leaderselectionService.saveOrUpdateLeaderselection(leaderselection);
	}

	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;
	authForm.setTitle(leaderselection.getTitle());
	authForm.setInstructions(leaderselection.getInstructions());

	// Set up sessionMap
	SessionMap<String, Object> map = new SessionMap<String, Object>();
	map.put(AuthoringAction.KEY_MODE, mode);
	map.put(AuthoringAction.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringAction.KEY_TOOL_CONTENT_ID, toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(LeaderselectionConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// TODO need error checking.

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession()
		.getAttribute(authForm.getSessionMapID());

	// get leaderselection content.
	Leaderselection leaderselection = leaderselectionService
		.getContentByContentId((Long) map.get(AuthoringAction.KEY_TOOL_CONTENT_ID));

	// update leaderselection content using form inputs.
	ToolAccessMode mode = (ToolAccessMode) map.get(AuthoringAction.KEY_MODE);
	leaderselection.setTitle(authForm.getTitle());
	leaderselection.setInstructions(authForm.getInstructions());

	// set the update date
	leaderselection.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	leaderselection.setDefineLater(false);

	leaderselectionService.saveOrUpdateLeaderselection(leaderselection);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(LeaderselectionConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }
}
