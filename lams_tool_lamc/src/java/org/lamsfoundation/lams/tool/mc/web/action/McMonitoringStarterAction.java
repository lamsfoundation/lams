/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.dto.McGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.mc.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.dto.SessionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McApplicationException;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.tool.mc.util.McSessionComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Starts up the monitoring module
 *
 * @author Ozgur Demirtas
 */
public class McMonitoringStarterAction extends Action {
    private static Logger logger = Logger.getLogger(McMonitoringStarterAction.class.getName());
    private static IMcService service;

    @Override
    @SuppressWarnings("unchecked")
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, McApplicationException {
	initializeMcService();

	McGeneralMonitoringDTO mcGeneralMonitoringDTO = new McGeneralMonitoringDTO();

	String toolContentID = null;
	try {
	    Long toolContentIDLong = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, false);
	    toolContentID = toolContentIDLong.toString();
	} catch (IllegalArgumentException e) {
	    logger.error("Unable to start monitoring as tool content id is missing");
	    throw (e);
	}

	McContent mcContent = service.getMcContent(new Long(toolContentID));
	mcGeneralMonitoringDTO.setToolContentID(toolContentID);
	mcGeneralMonitoringDTO.setActivityTitle(mcContent.getTitle());
	mcGeneralMonitoringDTO.setActivityInstructions(mcContent.getInstructions());

	//set up sessionDTOs list
	Set<McSession> sessions = new TreeSet<McSession>(new McSessionComparator());
	sessions.addAll(mcContent.getMcSessions());
	List<SessionDTO> sessionDtos = new LinkedList<SessionDTO>();
	for (McSession session : sessions) {
	    SessionDTO sessionDto = new SessionDTO();
	    sessionDto.setSessionId(session.getMcSessionId());
	    sessionDto.setSessionName(session.getSession_name());

	    sessionDtos.add(sessionDto);
	}
	request.setAttribute(McAppConstants.SESSION_DTOS, sessionDtos);

	// setting up the advanced summary

	request.setAttribute(McAppConstants.ATTR_CONTENT, mcContent);
	request.setAttribute("questionsSequenced", mcContent.isQuestionsSequenced());
	request.setAttribute("enableConfidenceLevels", mcContent.isEnableConfidenceLevels());
	request.setAttribute("showMarks", mcContent.isShowMarks());
	request.setAttribute("useSelectLeaderToolOuput", mcContent.isUseSelectLeaderToolOuput());
	request.setAttribute("prefixAnswersWithLetters", mcContent.isPrefixAnswersWithLetters());
	request.setAttribute("randomize", mcContent.isRandomize());
	request.setAttribute("displayAnswers", mcContent.isDisplayAnswers());
	request.setAttribute("retries", mcContent.isRetries());
	request.setAttribute("reflect", mcContent.isReflect());
	request.setAttribute("reflectionSubject", mcContent.getReflectionSubject());
	request.setAttribute("passMark", mcContent.getPassMark());
	request.setAttribute("toolContentID", mcContent.getMcContentId());
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	// setting up Date and time restriction in activities
	HttpSession ss = SessionManager.getSession();
	Date submissionDeadline = mcContent.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    request.setAttribute(McAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute(McAppConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	boolean isGroupedActivity = service.isGroupedActivity(new Long(mcContent.getMcContentId()));
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	/* this section is needed for Edit Activity screen, from here... */

	mcGeneralMonitoringDTO.setDisplayAnswers(new Boolean(mcContent.isDisplayAnswers()).toString());

	List<ReflectionDTO> reflectionsContainerDTO = service.getReflectionList(mcContent, null);
	request.setAttribute(McAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

	request.setAttribute(McAppConstants.MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
	
	//count users
	int countSessionComplete = 0;
	int countAllUsers = 0;
	Iterator iteratorSession = mcContent.getMcSessions().iterator();
	while (iteratorSession.hasNext()) {
	    McSession mcSession = (McSession) iteratorSession.next();

	    if (mcSession != null) {

		if (mcSession.getSessionStatus().equals(McAppConstants.COMPLETED)) {
		    countSessionComplete++;
		}
		countAllUsers += mcSession.getMcQueUsers().size();
	    }
	}
	mcGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers));
	mcGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete));

	return (mapping.findForward(McAppConstants.LOAD_MONITORING_CONTENT));
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private void initializeMcService() {
	if (service == null) {
	    service = McServiceProxy.getMcService(getServlet().getServletContext());
	}
    }
}
