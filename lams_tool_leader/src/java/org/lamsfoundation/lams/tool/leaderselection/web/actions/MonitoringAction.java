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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.tool.leaderselection.dto.LeaderselectionDTO;
import org.lamsfoundation.lams.tool.leaderselection.dto.LeaderselectionSessionDTO;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.lamsfoundation.lams.tool.leaderselection.service.ILeaderselectionService;
import org.lamsfoundation.lams.tool.leaderselection.service.LeaderselectionServiceProxy;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 *
 *
 *
 *
 */
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    private ILeaderselectionService service;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	initService();
	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(LeaderselectionConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	Leaderselection content = service.getContentByContentId(toolContentID);
	if (content == null) {
	    // TODO error page.
	}

	// // cache into sessionMap
	boolean isGroupedActivity = service.isGroupedActivity(toolContentID);
	sessionMap.put(LeaderselectionConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	// sessionMap.put(ScratchieConstants.PAGE_EDITABLE, scratchie.isContentInUse());
	// sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	// sessionMap.put(ScratchieConstants.ATTR_TOOL_CONTENT_ID, toolContentID);
	// sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
	// WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	// sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ON, scratchie.isReflectOnActivity());

	LeaderselectionDTO leaderselectionDT0 = new LeaderselectionDTO(content);
	sessionMap.put("leaderselectionDT0", leaderselectionDT0);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	leaderselectionDT0.setCurrentTab(currentTab);

	request.setAttribute("leaderselectionDTO", leaderselectionDT0);
	request.setAttribute("contentFolderID", contentFolderID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	return mapping.findForward(LeaderselectionConstants.SUCCESS);
    }

    /**
     * Show leaders manage page
     */
    public ActionForward manageLeaders(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = request.getParameter(LeaderselectionConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(LeaderselectionConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	return mapping.findForward("manageLeaders");
    }

    /**
     * Save selected users as a leaders
     * @throws IOException 
     * @throws JSONException 
     */
    public ActionForward saveLeaders(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	String sessionMapID = request.getParameter(LeaderselectionConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(LeaderselectionConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	initService();

	LeaderselectionDTO leaderselectionDT0 = (LeaderselectionDTO) sessionMap.get("leaderselectionDT0");
	for (LeaderselectionSessionDTO sessionDto : leaderselectionDT0.getSessionDTOs()) {
	    Long toolSessionId = sessionDto.getSessionID();
	    Long leaderUserUid = WebUtil.readLongParam(request, "sessionId" + toolSessionId, true);

	    // save selected users as a leaders
	    if (leaderUserUid != null) {
		service.setGroupLeader(leaderUserUid, toolSessionId);
	    }
	}

	return null;
    }

    /**
     * set up service
     */
    private void initService() {
	if (service == null) {
	    service = LeaderselectionServiceProxy.getLeaderselectionService(this.getServlet().getServletContext());
	}
    }
}
