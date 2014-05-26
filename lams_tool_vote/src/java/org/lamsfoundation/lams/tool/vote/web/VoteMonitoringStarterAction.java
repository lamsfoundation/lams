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

package org.lamsfoundation.lams.tool.vote.web;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.vote.dto.SessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteQuestionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteStatsDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.tool.vote.web.form.VoteMonitoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Starts up the monitoring module
 * 
 * @author Ozgur Demirtas
 */
public class VoteMonitoringStarterAction extends Action implements VoteAppConstants {
    static Logger logger = Logger.getLogger(VoteMonitoringStarterAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, VoteApplicationException {
	VoteUtils.cleanUpUserExceptions(request);

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	ActionForward validateParameters = validateParameters(request, mapping, voteMonitoringForm);
	if (validateParameters != null) {
	    return validateParameters;
	}

	//initialiseMonitoringData
	voteGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());

	/* we have made sure TOOL_CONTENT_ID is passed */
	String toolContentID = voteMonitoringForm.getToolContentID();
	VoteContent voteContent = voteService.getVoteContent(new Long(toolContentID));

	if (voteContent == null) {
	    VoteUtils.cleanUpUserExceptions(request);
	    voteGeneralMonitoringDTO.setUserExceptionContentDoesNotExist(new Boolean(true).toString());
	    return (mapping.findForward(ERROR_LIST));
	}

	voteGeneralMonitoringDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralMonitoringDTO.setActivityInstructions(voteContent.getInstructions());

	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    VoteUtils.cleanUpUserExceptions(request);
	    voteGeneralMonitoringDTO.setUserExceptionContentInUse(new Boolean(true).toString());
	}

	/*
	 * get the nominations section is needed for the Edit tab's View Only mode, starts here
	 */
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	sessionMap.put(ACTIVITY_TITLE_KEY, voteContent.getTitle());
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, voteContent.getInstructions());

	voteMonitoringForm.setHttpSessionID(sessionMap.getSessionID());
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	List listQuestionDTO = new LinkedList();

	Iterator queIterator = voteContent.getVoteQueContents().iterator();
	while (queIterator.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = new VoteQuestionDTO();

	    VoteQueContent voteQueContent = (VoteQueContent) queIterator.next();
	    if (voteQueContent != null) {

		voteQuestionDTO.setQuestion(voteQueContent.getQuestion());
		voteQuestionDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
		listQuestionDTO.add(voteQuestionDTO);
	    }
	}

	request.setAttribute(LIST_QUESTION_DTO, listQuestionDTO);
	sessionMap.put(LIST_QUESTION_DTO, listQuestionDTO);

	/* this section is related to summary tab. Starts here. */

	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	
	Set questions = voteContent.getVoteQueContents();

	List listMonitoredAnswersContainerDTO = new LinkedList();

	Iterator itListQuestions = questions.iterator();
	while (itListQuestions.hasNext()) {
	    VoteQueContent voteQueContent = (VoteQueContent) itListQuestions.next();

	    if (voteQueContent != null) {
		VoteMonitoredAnswersDTO voteMonitoredAnswersDTO = new VoteMonitoredAnswersDTO();
		voteMonitoredAnswersDTO.setQuestionUid(voteQueContent.getUid().toString());
		voteMonitoredAnswersDTO.setQuestion(voteQueContent.getQuestion());

		String questionUid = voteQueContent.getUid().toString();

		List<Map<String, VoteMonitoredUserDTO>> listMonitoredAttemptsContainerDTO = new LinkedList<Map<String, VoteMonitoredUserDTO>>();

		//populateToolSessionsId
		List<Long> sessionIds = voteService.getSessionsFromContent(voteContent);
		Map<String, String> summaryToolSessions = new TreeMap(new VoteComparator());
		int mapIndex = 1;
		for (Long sessionId : sessionIds) {
		    summaryToolSessions.put("" + mapIndex, sessionId.toString());
		    mapIndex++;
		}
		if (summaryToolSessions.isEmpty()) {
		    summaryToolSessions.put(new Long(1).toString(), "None");
		} else {
		    summaryToolSessions.put(new Long(summaryToolSessions.size() + 1).toString(), "All");
		}

		for (Entry<String, String> pairs : summaryToolSessions.entrySet()) {

		    if (!(pairs.getValue().equals("None")) && !(pairs.getValue().equals("All"))) {
			VoteSession voteSession = voteService.getSessionBySessionId(new Long(pairs.getValue()));
			if (voteSession != null) {
			    List<VoteQueUsr> users = voteService.getUserBySessionOnly(voteSession);
			    Map<String, VoteMonitoredUserDTO> sessionUsersAttempts = VoteMonitoringStarterAction
				    .populateSessionUsersAttempts(request, voteContent, voteSession.getVoteSessionId(),
					    users, questionUid, false, null, voteService);
			    listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
			}
		    }
		}
		    
//		//create data for All sessions in total
//		if (summaryToolSessions.size() > 1) {
//			Map<String, VoteMonitoredUserDTO> sessionUsersAttempts = populateSessionUsersAttempts(request,
//				voteContent, voteSession.getVoteSessionId(), users, questionUid, isUserNamesVisible,
//				isLearnerRequest, userId, voteService);
//			listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
//		}

		Map<String, Map> questionAttemptData = new TreeMap<String, Map>(new VoteComparator());
		Iterator listIterator = listMonitoredAttemptsContainerDTO.iterator();
		Long mapIndex2 = new Long(1);
		while (listIterator.hasNext()) {
		    Map data = (Map) listIterator.next();
		    questionAttemptData.put(mapIndex2.toString(), data);
		    mapIndex2 = new Long(mapIndex2.longValue() + 1);
		}
		voteMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
		listMonitoredAnswersContainerDTO.add(voteMonitoredAnswersDTO);

	    }
	}
	/* ends here. */

	List<VoteMonitoredAnswersDTO> userEnteredNominations = voteService.getOpenVotes(
		voteContent.getUid(), null, null);

	voteGeneralMonitoringDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	voteGeneralMonitoringDTO.setListUserEntries(userEnteredNominations);

	voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(false).toString());
	if (userEnteredNominations.size() > 0) {
	    voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(true).toString());
	}

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	// find out if there are any reflection entries
	if (!voteContent.getVoteSessions().isEmpty()) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    if (userExceptionNoToolSessions.equals("true")) {
		//there are no online student activity but there are reflections
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}

	// buildVoteStatsDTO
	int countSessionComplete = 0;
	int countAllUsers = 0;
	Iterator iteratorSession = voteContent.getVoteSessions().iterator();
	while (iteratorSession.hasNext()) {
	    VoteSession voteSession = (VoteSession) iteratorSession.next();

	    if (voteSession != null) {
		if (voteSession.getSessionStatus().equals(COMPLETED)) {
		    ++countSessionComplete;
		}

		Iterator iteratorUser = voteSession.getVoteQueUsers().iterator();
		while (iteratorUser.hasNext()) {
		    VoteQueUsr voteQueUsr = (VoteQueUsr) iteratorUser.next();
		    if (voteQueUsr != null) {
			++countAllUsers;
		    }
		}
	    }
	}

	VoteStatsDTO voteStatsDTO = new VoteStatsDTO();
	voteStatsDTO.setCountAllUsers(new Integer(countAllUsers).toString());
	voteStatsDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
	request.setAttribute(VOTE_STATS_DTO, voteStatsDTO);

	// setting up the advanced summary for LDEV-1662
	request.setAttribute("useSelectLeaderToolOuput", voteContent.isUseSelectLeaderToolOuput());
	request.setAttribute("lockOnFinish", voteContent.isLockOnFinish());
	request.setAttribute("allowText", voteContent.isAllowText());
	request.setAttribute("maxNominationCount", voteContent.getMaxNominationCount());
	request.setAttribute("minNominationCount", voteContent.getMinNominationCount());
	request.setAttribute("showResults", voteContent.isShowResults());
	request.setAttribute("reflect", voteContent.isReflect());
	request.setAttribute("reflectionSubject", voteContent.getReflectionSubject());
	request.setAttribute("toolContentID", voteContent.getVoteContentId());

	// setting up the SubmissionDeadline
	if (voteContent.getSubmissionDeadline() != null) {
	    Date submissionDeadline = voteContent.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(VoteAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	}

	voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(false).toString());

	voteMonitoringForm.setCurrentTab("1");
	voteGeneralMonitoringDTO.setCurrentTab("1");

	/* true means there is at least 1 response */
	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	voteGeneralMonitoringDTO.setIsPortfolioExport(new Boolean(false).toString());

	// this section is needed for Edit Activity screen
	voteGeneralAuthoringDTO.setActivityTitle(voteGeneralMonitoringDTO.getActivityTitle());
	voteGeneralAuthoringDTO.setActivityInstructions(voteGeneralMonitoringDTO.getActivityInstructions());

	MonitoringUtil.repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	List<SessionDTO> sessionDTOs = voteService.getSessionDTOs(new Long(toolContentID));
	voteGeneralMonitoringDTO.setSessionDTOs(sessionDTOs);
	
	boolean isGroupedActivity = voteService.isGroupedActivity(new Long(toolContentID));
	request.setAttribute("isGroupedActivity", isGroupedActivity);


	//refreshStatsData
	/* it is possible that no users has ever logged in for the activity yet */
	int countAllUsers2 = voteService.getTotalNumberOfUsers();
	if (countAllUsers2 == 0) {
	    voteGeneralMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
	}
	voteGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers2).toString());
	int countSessionComplete2 = voteService.countSessionComplete();
	voteGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete2).toString());

	List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	
	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }
    
    private static Map<String, VoteMonitoredUserDTO> populateSessionUsersAttempts(HttpServletRequest request,
	    VoteContent voteContent, Long sessionId, List listUsers, String questionUid, boolean isLearnerRequest,
	    Long userId, IVoteService voteService) {

	List listMonitoredUserContainerDTO = new LinkedList();
	Iterator itUsers = listUsers.iterator();

	if (userId == null) {
	    if (!isLearnerRequest) {
		while (itUsers.hasNext()) {
		    VoteQueUsr voteQueUsr = (VoteQueUsr) itUsers.next();

		    if (voteQueUsr != null) {
			List listUserAttempts = voteService.getAttemptsForUserAndQuestionContent(voteQueUsr.getUid(),
				new Long(questionUid));

			Iterator itAttempts = listUserAttempts.iterator();
			while (itAttempts.hasNext()) {
			    VoteUsrAttempt voteUsrResp = (VoteUsrAttempt) itAttempts.next();

			    if (voteUsrResp != null) {
				VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
				voteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime());
				voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
				voteMonitoredUserDTO.setUserName(voteQueUsr.getFullname());
				voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
				voteMonitoredUserDTO.setSessionId(sessionId.toString());
				voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());

				voteMonitoredUserDTO.setQuestionUid(questionUid);

				VoteQueContent voteQueContent = voteUsrResp.getVoteQueContent();
				String entry = voteQueContent.getQuestion();

				String voteQuestionUid = voteUsrResp.getVoteQueContent().getUid().toString();

				VoteSession localUserSession = voteUsrResp.getVoteQueUsr().getVoteSession();
				if (voteContent.getVoteContentId().toString()
					.equals(localUserSession.getVoteContentId().toString())) {
				    if (entry != null) {
					if (entry.equals("sample nomination") && (voteQuestionUid.equals("1"))) {
					    voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
					} else {
					    voteMonitoredUserDTO.setResponse(voteQueContent.getQuestion());
					}
				    }
				}

				listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
			    }
			}
		    }
		}
	    } else {
		// summary reporting case 2
		// just populating data normally just like monitoring summary, except that the data is ony for a
		// specific session
		String userID = (String) request.getSession().getAttribute(USER_ID);
		VoteQueUsr voteQueUsr = voteService.getUserById(new Long(userID).longValue());

		while (itUsers.hasNext()) {
		    voteQueUsr = (VoteQueUsr) itUsers.next();

		    if (voteQueUsr != null) {
			List listUserAttempts = voteService.getAttemptsForUserAndQuestionContent(voteQueUsr.getUid(),
				new Long(questionUid));

			Iterator itAttempts = listUserAttempts.iterator();
			while (itAttempts.hasNext()) {
			    VoteUsrAttempt voteUsrResp = (VoteUsrAttempt) itAttempts.next();

			    if (voteUsrResp != null) {
				VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
				voteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime());
				voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
				voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());
				voteMonitoredUserDTO.setUserName(voteQueUsr.getFullname());
				voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
				voteMonitoredUserDTO.setSessionId(sessionId.toString());
				voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());
				voteMonitoredUserDTO.setQuestionUid(questionUid);

				VoteQueContent voteQueContent = voteUsrResp.getVoteQueContent();
				String entry = voteQueContent.getQuestion();
				String questionUid2 = voteUsrResp.getVoteQueContentId().toString();

				VoteSession localUserSession = voteUsrResp.getVoteQueUsr().getVoteSession();
				if (voteContent.getVoteContentId().toString()
					.equals(localUserSession.getVoteContentId().toString())) {
				    if (entry != null) {
					if (entry.equals("sample nomination") && (questionUid2.equals("1"))) {
					    voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
					} else {
					    voteMonitoredUserDTO.setResponse(voteQueContent.getQuestion());
					}
				    }
				}

				listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
			    }
			}
		    }
		}
	    }
	} else {
	    // summary reporting case 4
	    // request is for learner progress report userId
	    while (itUsers.hasNext()) {
		VoteQueUsr voteQueUsr = (VoteQueUsr) itUsers.next();

		if (voteQueUsr != null) {
		    List listUserAttempts = voteService.getAttemptsForUserAndQuestionContent(voteQueUsr.getUid(),
			    new Long(questionUid));

		    Iterator itAttempts = listUserAttempts.iterator();
		    while (itAttempts.hasNext()) {
			VoteUsrAttempt voteUsrResp = (VoteUsrAttempt) itAttempts.next();

			if (voteUsrResp != null) {
			    if (userId.equals(voteQueUsr.getQueUsrId())) {
				// this is the user requested , include his name for learner progress
				VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
				voteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime());
				voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimeZone());
				voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());
				voteMonitoredUserDTO.setUserName(voteQueUsr.getFullname());
				voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
				voteMonitoredUserDTO.setSessionId(sessionId.toString());
				voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());
				voteMonitoredUserDTO.setQuestionUid(questionUid);

				VoteQueContent voteQueContent = voteUsrResp.getVoteQueContent();
				String entry = voteQueContent.getQuestion();

				String voteQueContentId = voteUsrResp.getVoteQueContentId().toString();

				VoteSession localUserSession = voteUsrResp.getVoteQueUsr().getVoteSession();
				if (voteContent.getVoteContentId().toString()
					.equals(localUserSession.getVoteContentId().toString())) {
				    if (entry != null) {
					if (entry.equals("sample nomination") && (voteQueContentId.equals("1"))) {
					    voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());
					} else {
					    voteMonitoredUserDTO.setResponse(voteQueContent.getQuestion());
					}
				    }
				}

				listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
			    }
			}
		    }
		}
	    }

	}

	Map<String, VoteMonitoredUserDTO> mapMonitoredUserContainerDTO = MonitoringUtil.convertToVoteMonitoredUserDTOMap(listMonitoredUserContainerDTO);
	return mapMonitoredUserContainerDTO;
    }

    private ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping,
	    VoteMonitoringForm voteMonitoringForm) {

	String strToolContentId = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	if ((strToolContentId == null) || (strToolContentId.length() == 0)) {
	    VoteUtils.cleanUpUserExceptions(request);
	    return (mapping.findForward(ERROR_LIST));
	} else {
	    try {
		voteMonitoringForm.setToolContentID(strToolContentId);
	    } catch (NumberFormatException e) {
		logger.error("add error.numberFormatException to ActionMessages.");
		VoteUtils.cleanUpUserExceptions(request);
		return (mapping.findForward(ERROR_LIST));
	    }
	}
	return null;
    }
}
