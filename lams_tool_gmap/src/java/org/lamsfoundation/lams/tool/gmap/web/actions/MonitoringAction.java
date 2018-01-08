/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.tool.gmap.web.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.gmap.dto.GmapDTO;
import org.lamsfoundation.lams.tool.gmap.dto.GmapSessionDTO;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapConfigItem;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;
import org.lamsfoundation.lams.tool.gmap.service.GmapServiceProxy;
import org.lamsfoundation.lams.tool.gmap.service.IGmapService;
import org.lamsfoundation.lams.tool.gmap.util.GmapConstants;
import org.lamsfoundation.lams.tool.gmap.util.GmapException;
import org.lamsfoundation.lams.tool.gmap.web.forms.MonitoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    public IGmapService gmapService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Gmap gmap = gmapService.getGmapByContentId(toolContentID);

	if (gmap == null) {
	    // TODO error page.
	}

	GmapDTO gmapDT0 = new GmapDTO(gmap);

	// Adding the markers lists to a map with tool sessions as the key
	

	if (gmapDT0.getSessionDTOs() != null) {
	    for (GmapSessionDTO sessionDTO : gmapDT0.getSessionDTOs()) {
		Long toolSessionID = sessionDTO.getSessionID();
		sessionDTO.setMarkerDTOs(gmapService.getGmapMarkersBySessionId(toolSessionID));
	    }
	}

	// get the gmap API key from the config table and add it to the session
	GmapConfigItem gmapKey = gmapService.getConfigItem(GmapConfigItem.KEY_GMAP_KEY);
	if (gmapKey != null && gmapKey.getConfigValue() != null) {
	    request.setAttribute(GmapConstants.ATTR_GMAP_KEY, gmapKey.getConfigValue());
	}

	boolean isGroupedActivity = gmapService.isGroupedActivity(toolContentID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);
	request.setAttribute("gmapDTO", gmapDT0);
	request.setAttribute("contentFolderID", contentFolderID);
	return mapping.findForward("success");
    }

    /**
     * set up gmapService
     */
    private void setupService() {
	if (gmapService == null) {
	    gmapService = GmapServiceProxy.getGmapService(this.getServlet().getServletContext());
	}
    }

    /**
     * Allows teachers to edit/remove existing markers
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward saveMarkers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	MonitoringForm monitoringForm = (MonitoringForm) form;
	Long toolSessionID = monitoringForm.getToolSessionID();
	//Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	GmapUser gmapUser = getCurrentUser(toolSessionID);

	if (gmapUser != null) {

	    //MonitoringForm monitoringForm = (MonitoringForm) form;

	    // Retrieve the session and content.
	    GmapSession gmapSession = gmapService.getSessionBySessionId(toolSessionID);
	    if (gmapSession == null) {
		throw new GmapException("Cannot retrieve session with toolSessionID" + toolSessionID);
	    }

	    // update the marker list
	    Gmap gmap = gmapSession.getGmap();
	    gmapService.updateMarkerListFromXML(monitoringForm.getMarkersXML(), gmap, gmapUser, true, gmapSession);

	} else {
	    log.error("saveMarkers(): couldn't find GmapUser with id: " + gmapUser.getUserId() + "and toolSessionID: "
		    + toolSessionID);
	}

	return unspecified(mapping, form, request, response);
    }

    /**
     * Get the current user
     *
     * @param toolSessionId
     * @return
     */
    private GmapUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	GmapUser gmapUser = gmapService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (gmapUser == null) {
	    GmapSession gmapSession = gmapService.getSessionBySessionId(toolSessionId);
	    gmapUser = gmapService.createGmapUser(user, gmapSession);
	}

	return gmapUser;
    }

    /** Ajax call to populate the tablesorter */
    public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {

	Long sessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = GmapConstants.SORT_BY_NO;
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? GmapConstants.SORT_BY_USERNAME_ASC : GmapConstants.SORT_BY_USERNAME_DESC;
	}

	// return user list according to the given sessionID
	GmapSession session = gmapService.getSessionBySessionId(sessionID);
	List<Object[]> users = gmapService.getUsersForTablesorter(sessionID, page, size, sorting, searchString,
		session.getGmap().isReflectOnActivity());

	JSONArray rows = new JSONArray();
	JSONObject responsedata = new JSONObject();
	responsedata.put("total_rows", gmapService.getCountUsersBySession(session.getUid(), searchString));
	for (Object[] userAndReflection : users) {

	    JSONObject responseRow = new JSONObject();
	    responseRow.put(GmapConstants.ATTR_USER_ID, userAndReflection[0]);
	    String fullName = new StringBuilder((String)userAndReflection[1]).append(" ").append((String)userAndReflection[2]).toString();
	    responseRow.put(GmapConstants.ATTR_USER_FULLNAME, StringEscapeUtils.escapeHtml(fullName));

	    if (userAndReflection.length > 3) {
		responseRow.put(GmapConstants.ATTR_PORTRAIT_ID, (Integer)userAndReflection[3]);
	    }

	    if (userAndReflection.length > 4) {
		responseRow.put(GmapConstants.ATTR_USER_REFLECTION, userAndReflection[4]);
	    }
	    rows.put(responseRow);
	}

	responsedata.put("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;

    }

}
