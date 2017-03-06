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


package org.lamsfoundation.lams.tool.dokumaran.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.dokumaran.dto.SessionDTO;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranConfigItem;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranSession;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;
import org.lamsfoundation.lams.tool.dokumaran.service.DokumaranConfigurationException;
import org.lamsfoundation.lams.tool.dokumaran.service.IDokumaranService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {
    public static Logger log = Logger.getLogger(MonitoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException, DokumaranConfigurationException, URISyntaxException {
	String param = mapping.getParameter();

	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	if (param.equals("summary")) {
	    return summary(mapping, form, request, response);
	} else if (param.equals("fixFaultySession")) {
	    return fixFaultySession(mapping, form, request, response);
	} else if (param.equals("launchTimeLimit")) {
	    return launchTimeLimit(mapping, form, request, response);
	} else if (param.equals("addOneMinute")) {
	    return addOneMinute(mapping, form, request, response);
	}

	return mapping.findForward(DokumaranConstants.ERROR);
    }

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws DokumaranConfigurationException, URISyntaxException {
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(DokumaranConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	IDokumaranService service = getDokumaranService();
	List<SessionDTO> groupList = service.getSummary(contentId);
	boolean hasFaultySession = false;
	for (SessionDTO group : groupList) {
	    hasFaultySession |= group.isSessionFaulty();
	}

	Dokumaran dokumaran = service.getDokumaranByContentId(contentId);

	// Create reflectList if reflection is enabled.
	if (dokumaran.isReflectOnActivity()) {
	    List<ReflectDTO> relectList = service.getReflectList(contentId);
	    sessionMap.put(DokumaranConstants.ATTR_REFLECT_LIST, relectList);
	}
	
	//time limit
	boolean isTimeLimitEnabled = dokumaran.getTimeLimit() != 0;
	long secondsLeft = isTimeLimitEnabled ? service.getSecondsLeft(dokumaran) : 0;
	sessionMap.put(DokumaranConstants.ATTR_SECONDS_LEFT, secondsLeft);

	// cache into sessionMap
	sessionMap.put(DokumaranConstants.ATTR_SUMMARY_LIST, groupList);
	sessionMap.put(DokumaranConstants.ATTR_HAS_FAULTY_SESSION, hasFaultySession);
	sessionMap.put(DokumaranConstants.PAGE_EDITABLE, dokumaran.isContentInUse());
	sessionMap.put(DokumaranConstants.ATTR_DOKUMARAN, dokumaran);
	sessionMap.put(DokumaranConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(DokumaranConstants.ATTR_IS_GROUPED_ACTIVITY, service.isGroupedActivity(contentId));
	
	// get the API key from the config table and add it to the session
	DokumaranConfigItem etherpadServerUrlConfig = service.getConfigItem(DokumaranConfigItem.KEY_ETHERPAD_URL);
	if (etherpadServerUrlConfig == null || etherpadServerUrlConfig.getConfigValue() == null) {
	    return mapping.findForward("notconfigured");
	}
	String etherpadServerUrl = etherpadServerUrlConfig.getConfigValue();
	request.setAttribute(DokumaranConstants.KEY_ETHERPAD_SERVER_URL, etherpadServerUrl);
	
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	
	//no need to store cookie if there are no sessions created yet
	if (!groupList.isEmpty()) {
	    // add new sessionID cookie in order to access pad
	    Cookie etherpadSessionCookie = service.createEtherpadCookieForMonitor(user, contentId);
	    response.addCookie(etherpadSessionCookie);
	}
	
	return mapping.findForward(DokumaranConstants.SUCCESS);
    }
    
    private ActionForward fixFaultySession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws DokumaranConfigurationException, ServletException, IOException {
	IDokumaranService service = getDokumaranService();
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	DokumaranSession session = service.getDokumaranSessionBySessionId(toolSessionId);
	
	try {
	    log.debug("Fixing faulty session (sessionId=" + toolSessionId + ").");
	    service.createPad(session.getDokumaran(), session);

	} catch (Exception e) {
	    // printing out error cause
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.write("Failed! " + e.getMessage());
	    out.flush();
	    out.close();
	    log.error("Failed! " + e.getMessage());
	    return null;
	}
	
	return null;
    }
    
    /**
     * Stores date when user has started activity with time limit
     * @throws IOException 
     * @throws JSONException 
     */
    private ActionForward launchTimeLimit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	IDokumaranService service = getDokumaranService();
	Long toolContentId = WebUtil.readLongParam(request, DokumaranConstants.ATTR_TOOL_CONTENT_ID, false);
	
	service.launchTimeLimit(toolContentId);

	return null;
    }
    
    /**
     * Stores date when user has started activity with time limit
     * @throws IOException 
     * @throws JSONException 
     */
    private ActionForward addOneMinute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	IDokumaranService service = getDokumaranService();
	Long toolContentId = WebUtil.readLongParam(request, DokumaranConstants.ATTR_TOOL_CONTENT_ID, false);
	
	service.addOneMinute(toolContentId);

	return null;
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    
    private IDokumaranService getDokumaranService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IDokumaranService) wac.getBean(DokumaranConstants.RESOURCE_SERVICE);
    }
}
