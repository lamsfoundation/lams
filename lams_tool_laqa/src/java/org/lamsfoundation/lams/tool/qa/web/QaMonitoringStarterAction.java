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


package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
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
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.dto.GroupDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaStatsDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.util.QaSessionComparator;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.tool.qa.web.form.QaMonitoringForm;
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
public class QaMonitoringStarterAction extends Action implements QaAppConstants {
    private static Logger logger = Logger.getLogger(QaMonitoringStarterAction.class.getName());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, QaApplicationException {
	QaUtils.cleanUpSessionAbsolute(request);

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	ActionForward validateParameters = validateParameters(request, mapping, qaMonitoringForm);
	if (validateParameters != null) {
	    return validateParameters;
	}

	String toolContentID = qaMonitoringForm.getToolContentID();
	QaContent qaContent = qaService.getQaContent(new Long(toolContentID).longValue());
	if (qaContent == null) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    throw new ServletException("Data not initialised in Monitoring");
	}

	qaMonitoringForm.setCurrentTab("1");

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	qaMonitoringForm.setToolContentID(strToolContentID);

	/* this section is related to summary tab. Starts here. */
//	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
//	sessionMap.put(ACTIVITY_TITLE_KEY, qaContent.getTitle());
//	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, qaContent.getInstructions());
//
//	qaMonitoringForm.setHttpSessionID(sessionMap.getSessionID());
//	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	List questionDTOs = new LinkedList();
	for (QaQueContent question : qaContent.getQaQueContents()) {
	    QaQuestionDTO questionDTO = new QaQuestionDTO(question);
	    questionDTOs.add(questionDTO);
	}
	request.setAttribute(LIST_QUESTION_DTOS, questionDTOs);
//	sessionMap.put(LIST_QUESTION_DTOS, questionDTOs);
//	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	//session dto list
	List<GroupDTO> groupDTOs = new LinkedList<GroupDTO>();
	Set<QaSession> sessions = new TreeSet<QaSession>(new QaSessionComparator());
	sessions.addAll(qaContent.getQaSessions());
	for (QaSession session : sessions) {
	    String sessionId = session.getQaSessionId().toString();
	    String sessionName = session.getSession_name();

	    GroupDTO groupDTO = new GroupDTO();
	    groupDTO.setSessionName(sessionName);
	    groupDTO.setSessionId(sessionId);
	    groupDTOs.add(groupDTO);
	}
	request.setAttribute(LIST_ALL_GROUPS_DTO, groupDTOs);

	// setting up the advanced summary for LDEV-1662
	request.setAttribute(QaAppConstants.ATTR_CONTENT, qaContent);

	boolean isGroupedActivity = qaService.isGroupedActivity(qaContent.getQaContentId());
	request.setAttribute("isGroupedActivity", isGroupedActivity);
	
	//ratings stuff
	boolean isRatingsEnabled = qaService.isRatingsEnabled(qaContent);
	request.setAttribute("isRatingsEnabled", isRatingsEnabled);

	//comments stuff
	boolean isCommentsEnabled = qaService.isCommentsEnabled(qaContent.getQaContentId());
	request.setAttribute("isCommentsEnabled", isCommentsEnabled);

	//buildQaStatsDTO
	QaStatsDTO qaStatsDTO = new QaStatsDTO();
	int countSessionComplete = 0;
	int countAllUsers = 0;
	Iterator iteratorSession = qaContent.getQaSessions().iterator();
	while (iteratorSession.hasNext()) {
	    QaSession qaSession = (QaSession) iteratorSession.next();

	    if (qaSession != null) {

		if (qaSession.getSession_status().equals(COMPLETED)) {
		    ++countSessionComplete;
		}

		Iterator iteratorUser = qaSession.getQaQueUsers().iterator();
		while (iteratorUser.hasNext()) {
		    QaQueUsr qaQueUsr = (QaQueUsr) iteratorUser.next();

		    if (qaQueUsr != null) {
			++countAllUsers;
		    }
		}
	    }
	}
	qaStatsDTO.setCountAllUsers(new Integer(countAllUsers).toString());
	qaStatsDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
	request.setAttribute(QA_STATS_DTO, qaStatsDTO);

	// set SubmissionDeadline, if any
	if (qaContent.getSubmissionDeadline() != null) {
	    Date submissionDeadline = qaContent.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(QaAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	}

	return (mapping.findForward(LOAD_MONITORING));
    }

    /**
     * validates request paramaters based on tool contract
     *
     * @param request
     * @param mapping
     * @return ActionForward
     * @throws ServletException
     */
    protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping,
	    QaMonitoringForm qaMonitoringForm) throws ServletException {

	String strToolContentId = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	if ((strToolContentId == null) || (strToolContentId.length() == 0)) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    throw new ServletException("No Tool Content ID found");
	} else {
	    try {
		long toolContentId = new Long(strToolContentId).longValue();

		qaMonitoringForm.setToolContentID(new Long(toolContentId).toString());
	    } catch (NumberFormatException e) {
		QaUtils.cleanUpSessionAbsolute(request);
		throw e;
	    }
	}
	return null;
    }
}
