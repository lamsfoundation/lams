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

package org.lamsfoundation.lams.tool.dokumaran.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    public static Logger log = Logger.getLogger(MonitoringController.class);

    public static final int LEARNER_MARKS_SORTING_FIRST_NAME_ASC = 0;
    public static final int LEARNER_MARKS_SORTING_FIRST_NAME_DESC = 1;
    public static final int LEARNER_MARKS_SORTING_LAST_NAME_ASC = 2;
    public static final int LEARNER_MARKS_SORTING_LAST_NAME_DESC = 3;

    @Autowired
    private IDokumaranService dokumaranService;

    @RequestMapping("/summary")
    private String summary(HttpServletRequest request, HttpServletResponse response)
	    throws DokumaranConfigurationException, URISyntaxException {
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(DokumaranConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<SessionDTO> groupList = dokumaranService.getSummary(contentId);
	boolean hasFaultySession = false;
	for (SessionDTO group : groupList) {
	    hasFaultySession |= group.isSessionFaulty();
	}

	Dokumaran dokumaran = dokumaranService.getDokumaranByContentId(contentId);

	// Create reflectList if reflection is enabled.
	if (dokumaran.isReflectOnActivity()) {
	    List<ReflectDTO> relectList = dokumaranService.getReflectList(contentId);
	    sessionMap.put(DokumaranConstants.ATTR_REFLECT_LIST, relectList);
	}

	//time limit
	boolean isTimeLimitEnabled = dokumaran.getTimeLimit() != 0;
	long secondsLeft = isTimeLimitEnabled ? dokumaranService.getSecondsLeft(dokumaran) : 0;
	sessionMap.put(DokumaranConstants.ATTR_SECONDS_LEFT, secondsLeft);

	// cache into sessionMap
	sessionMap.put(DokumaranConstants.ATTR_SUMMARY_LIST, groupList);
	sessionMap.put(DokumaranConstants.ATTR_HAS_FAULTY_SESSION, hasFaultySession);
	sessionMap.put(DokumaranConstants.PAGE_EDITABLE, dokumaran.isContentInUse());
	sessionMap.put(DokumaranConstants.ATTR_DOKUMARAN, dokumaran);
	sessionMap.put(DokumaranConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(DokumaranConstants.ATTR_IS_GROUPED_ACTIVITY, dokumaranService.isGroupedActivity(contentId));

	// get the API key from the config table and add it to the session
	DokumaranConfigItem etherpadServerUrlConfig = dokumaranService
		.getConfigItem(DokumaranConfigItem.KEY_ETHERPAD_URL);
	if (etherpadServerUrlConfig == null || etherpadServerUrlConfig.getConfigValue() == null) {
	    return "pages/learning/notconfigured";
	}
	String etherpadServerUrl = etherpadServerUrlConfig.getConfigValue();
	request.setAttribute(DokumaranConstants.KEY_ETHERPAD_SERVER_URL, etherpadServerUrl);

	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	//no need to store cookie if there are no sessions created yet
	if (!groupList.isEmpty()) {
	    // add new sessionID cookie in order to access pad
	    Cookie etherpadSessionCookie = dokumaranService.createEtherpadCookieForMonitor(user, contentId);
	    response.addCookie(etherpadSessionCookie);
	}

	return "pages/monitoring/monitoring";
    }

    @RequestMapping("/getLearnerMarks")
    @ResponseBody
    public String getLearnerMarks(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSortFirstName = WebUtil.readIntParam(request, "column[0]", true);
	Integer isSortLastName = WebUtil.readIntParam(request, "column[1]", true);

	// identify sorting type
	int sorting = LEARNER_MARKS_SORTING_FIRST_NAME_ASC;
	if (isSortFirstName != null) {
	    sorting = isSortFirstName.equals(1) ? LEARNER_MARKS_SORTING_FIRST_NAME_DESC
		    : LEARNER_MARKS_SORTING_FIRST_NAME_ASC;
	} else if (isSortLastName != null) {
	    sorting = isSortLastName.equals(1) ? LEARNER_MARKS_SORTING_LAST_NAME_DESC
		    : LEARNER_MARKS_SORTING_LAST_NAME_ASC;
	}

	// get all session users and sort them according to the parameter from tablesorter
	List<DokumaranUser> users = dokumaranService.getUsersBySession(toolSessionId).stream()
		.sorted(Comparator.comparing(sorting <= 1 ? DokumaranUser::getFirstName : DokumaranUser::getLastName))
		.collect(Collectors.toList());
	// reverse if sorting is descending
	if (sorting == LEARNER_MARKS_SORTING_FIRST_NAME_DESC || sorting == LEARNER_MARKS_SORTING_LAST_NAME_DESC) {
	    Collections.reverse(users);
	}

	// paging
	int endIndex = (page + 1) * size;
	users = users.subList(page * size, users.size() > endIndex ? endIndex : users.size());

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows", users.size());

	for (DokumaranUser user : users) {
	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();

	    responseRow.put("firstName", user.getFirstName());
	    responseRow.put("lastName", user.getLastName());

	    rows.add(responseRow);
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	return responsedata.toString();
    }

    @RequestMapping("/fixFaultySession")
    private void fixFaultySession(HttpServletRequest request, HttpServletResponse response)
	    throws DokumaranConfigurationException, ServletException, IOException {
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	DokumaranSession session = dokumaranService.getDokumaranSessionBySessionId(toolSessionId);

	try {
	    log.debug("Fixing faulty session (sessionId=" + toolSessionId + ").");
	    dokumaranService.createPad(session.getDokumaran(), session);

	} catch (Exception e) {
	    // printing out error cause
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    out.write("Failed! " + e.getMessage());
	    out.flush();
	    out.close();
	    log.error("Failed! " + e.getMessage());
	}

    }

    /**
     * Stores date when user has started activity with time limit
     *
     * @throws IOException
     * @throws JSONException
     */
    @RequestMapping("/launchTimeLimit")
    private void launchTimeLimit(HttpServletRequest request) throws IOException {
	Long toolContentId = WebUtil.readLongParam(request, DokumaranConstants.ATTR_TOOL_CONTENT_ID, false);

	dokumaranService.launchTimeLimit(toolContentId);
    }

    /**
     * Stores date when user has started activity with time limit
     *
     * @throws IOException
     * @throws JSONException
     */
    @RequestMapping("/addOneMinute")
    private void addOneMinute(HttpServletRequest request) throws IOException {
	Long toolContentId = WebUtil.readLongParam(request, DokumaranConstants.ATTR_TOOL_CONTENT_ID, false);

	dokumaranService.addOneMinute(toolContentId);

    }

}
