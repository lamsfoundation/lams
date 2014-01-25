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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.vote.VoteAllGroupsDTO;
import org.lamsfoundation.lams.tool.vote.SessionDTO;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.VoteStatsDTO;
import org.lamsfoundation.lams.tool.vote.VoteStringComparator;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * <p>
 * More generic monitoring mode functions live here
 * </p>
 * 
 * @author Ozgur Demirtas
 * 
 */
public class MonitoringUtil implements VoteAppConstants {

    /**
     * groups user's votes
     */
    public static Map<String, Map> buildGroupsAttemptData(HttpServletRequest request, VoteContent voteContent,
	    VoteQueContent voteQueContent, String questionUid, boolean isUserNamesVisible, boolean isLearnerRequest,
	    String currentSessionId, String userId, IVoteService voteService) {

	List<Map<String, VoteMonitoredUserDTO>> listMonitoredAttemptsContainerDTO = new LinkedList<Map<String, VoteMonitoredUserDTO>>();

	Map<String, String> summaryToolSessions = populateToolSessionsId(voteContent, voteService);

	/* request is for monitoring summary */
	if (!isLearnerRequest) {
	    for (Entry<String, String> pairs : summaryToolSessions.entrySet()) {

		if (!(pairs.getValue().equals("None")) && !(pairs.getValue().equals("All"))) {
		    VoteSession voteSession = voteService.retrieveVoteSession(new Long(pairs.getValue()));
		    if (voteSession != null) {
			List<VoteQueUsr> users = voteService.getUserBySessionOnly(voteSession);
			Map<String, VoteMonitoredUserDTO> sessionUsersAttempts = populateSessionUsersAttempts(request,
				voteContent, voteSession.getVoteSessionId(), users, questionUid, isUserNamesVisible,
				isLearnerRequest, userId, voteService);
			listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
		    }
		}
	    }
	    
//	    //create data for All sessions in total
//	    if (summaryToolSessions.size() > 1) {
//		Map<String, VoteMonitoredUserDTO> sessionUsersAttempts = populateSessionUsersAttempts(request,
//			voteContent, voteSession.getVoteSessionId(), users, questionUid, isUserNamesVisible,
//			isLearnerRequest, userId, voteService);
//		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
//	    }

	} else {
	    /* request is for learner report, use only the passed tool session in the report */
	    for (Entry<String, String> pairs : summaryToolSessions.entrySet()) {

		if (!(pairs.getValue().toString().equals("None")) && !(pairs.getValue().toString().equals("All"))) {

		    if (currentSessionId.equals(pairs.getValue())) {
			VoteSession voteSession = voteService
				.retrieveVoteSession(new Long(pairs.getValue().toString()));
			if (voteSession != null) {
			    List<VoteQueUsr> listUsers = voteService.getUserBySessionOnly(voteSession);
			    Map<String, VoteMonitoredUserDTO> sessionUsersAttempts = populateSessionUsersAttempts(
				    request, voteContent, voteSession.getVoteSessionId(), listUsers, questionUid,
				    isUserNamesVisible, isLearnerRequest, userId, voteService);
			    listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
			}
		    }
		}
	    }
	}

	Map<String, Map> mapMonitoredAttemptsContainerDTO = convertToMap(listMonitoredAttemptsContainerDTO);
	return mapMonitoredAttemptsContainerDTO;
    }

    public static Map<String, VoteMonitoredUserDTO> populateSessionUsersAttempts(HttpServletRequest request,
	    VoteContent voteContent, Long sessionId, List listUsers, String questionUid, boolean isUserNamesVisible,
	    boolean isLearnerRequest, String userId, IVoteService voteService) {

	List listMonitoredUserContainerDTO = new LinkedList();
	Iterator itUsers = listUsers.iterator();

	if (userId == null) {
	    if ((isUserNamesVisible) && (!isLearnerRequest)) {
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

				Long voteQuestionUid = voteUsrResp.getVoteQueContent().getUid();
				String voteQueContentId = voteQuestionUid.toString();

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
	    } else if ((isUserNamesVisible) && (isLearnerRequest)) {
		// summary reporting case 2
		// just populating data normally just like monitoring summary, except that the data is ony for a
		// specific session
		String userID = (String) request.getSession().getAttribute(USER_ID);
		VoteQueUsr voteQueUsr = voteService.getVoteQueUsrById(new Long(userID).longValue());

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
	    } else if ((!isUserNamesVisible) && (isLearnerRequest)) {
		// summary reporting case 3
		// populating data normally exception are for a specific session and other user names are not visible
		String userID = (String) request.getSession().getAttribute(USER_ID);

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
				voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());

				if (userID.equals(voteQueUsr.getQueUsrId().toString())) {
				    voteMonitoredUserDTO.setUserName(voteQueUsr.getFullname());
				} else {
				    voteMonitoredUserDTO.setUserName("[        ]");
				}

				voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
				voteMonitoredUserDTO.setSessionId(sessionId.toString());

				voteMonitoredUserDTO.setQuestionUid(questionUid);
				voteMonitoredUserDTO.setUserEntry(voteUsrResp.getUserEntry());

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
			    if (userId.equals(voteQueUsr.getQueUsrId().toString())) {
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

	Map<String, VoteMonitoredUserDTO> mapMonitoredUserContainerDTO = convertToVoteMonitoredUserDTOMap(listMonitoredUserContainerDTO);
	return mapMonitoredUserContainerDTO;
    }

    public static Map<String, String> populateToolSessionsId(VoteContent voteContent, IVoteService voteService) {
	List<Long> sessionIds = voteService.getSessionsFromContent(voteContent);

	Map<String, String> sessionsMap = new TreeMap(new VoteComparator());
	int mapIndex = 1;
	for (Long sessionId : sessionIds) {
	    sessionsMap.put("" + mapIndex, sessionId.toString());
	    mapIndex++;
	}

	if (sessionsMap.isEmpty()) {
	    sessionsMap.put(new Long(1).toString(), "None");
	} else {
	    sessionsMap.put(new Long(sessionsMap.size() + 1).toString(), "All");
	}

	return sessionsMap;
    }

    public static Map<String, VoteMonitoredUserDTO> convertToVoteMonitoredUserDTOMap(List<VoteMonitoredUserDTO> list) {
	Map<String, VoteMonitoredUserDTO> map = new TreeMap<String, VoteMonitoredUserDTO>(new VoteComparator());

	Iterator<VoteMonitoredUserDTO> listIterator = list.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    VoteMonitoredUserDTO data = listIterator.next();

	    map.put(mapIndex.toString(), data);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    public static Map<String, Map> convertToMap(List list) {
	Map<String, Map> map = new TreeMap<String, Map>(new VoteComparator());

	Iterator listIterator = list.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    Map data = (Map) listIterator.next();
	    map.put(mapIndex.toString(), data);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    /**
     * Generates chart data for the learner module and monitoring module Summary tab (Individual Sessions mode)
     * 
     * @param request
     * @param voteService
     * @param voteMonitoringForm
     * @param toolContentID
     * @param toolSessionUid
     */
    public static void prepareChartData(HttpServletRequest request, IVoteService voteService,
	    VoteMonitoringForm voteMonitoringForm, String toolContentID, String toolSessionUid,
	    VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO, VoteGeneralMonitoringDTO voteGeneralMonitoringDTO,
	    MessageService messageService) {
	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	Map mapOptionsContent = new TreeMap(new VoteComparator());
	Map mapVoteRatesContent = new TreeMap(new VoteComparator());

	List distinctSessionUsers = new ArrayList();
	boolean sessionLevelCharting = true;
	int entriesCount = 0;
	Set userEntries = null;
	if (toolSessionUid != null) {
	    entriesCount = voteService.getSessionEntriesCount(new Long(toolSessionUid));
	    userEntries = voteService.getSessionUserEntriesSet(new Long(toolSessionUid));

	    int completedSessionUserCount = voteService.getCompletedVoteUserBySessionUid(new Long(toolSessionUid));

	    int completedEntriesCount = voteService.getCompletedSessionEntriesCount(new Long(toolSessionUid));

	    if (voteMonitoringForm != null) {
		int potentialUserCount = voteService.getVoteSessionPotentialLearnersCount(new Long(toolSessionUid));
		voteMonitoringForm.setSessionUserCount(Integer.toString(potentialUserCount));
		voteMonitoringForm.setCompletedSessionUserCount(new Integer(completedSessionUserCount).toString());

		if (voteGeneralMonitoringDTO != null) {
		    voteGeneralMonitoringDTO.setSessionUserCount(Integer.toString(potentialUserCount));
		    voteGeneralMonitoringDTO.setCompletedSessionUserCount(new Integer(completedSessionUserCount)
			    .toString());
		}
	    }
	}

	Map mapStandardUserCount = new TreeMap(new VoteComparator());

	mapOptionsContent.clear();
	Iterator queIterator = voteContent.getVoteQueContents().iterator();
	Long mapIndex = new Long(1);
	int totalStandardVotesCount = 0;

	Map mapStandardNominationsHTMLedContent = new TreeMap(new VoteComparator());
	Map mapStandardQuestionUid = new TreeMap(new VoteComparator());
	Map mapStandardToolSessionUid = new TreeMap(new VoteComparator());
	while (queIterator.hasNext()) {
	    VoteQueContent voteQueContent = (VoteQueContent) queIterator.next();
	    if (voteQueContent != null) {
		mapStandardNominationsHTMLedContent.put(mapIndex.toString(), voteQueContent.getQuestion());
		String noHTMLNomination = VoteUtils.stripHTML(voteQueContent.getQuestion());
		mapOptionsContent.put(mapIndex.toString(), noHTMLNomination);

		int votesCount = 0;
		if (sessionLevelCharting == true) {
		    votesCount = voteService.getStandardAttemptsForQuestionContentAndSessionUid(
			    voteQueContent.getUid(), new Long(toolSessionUid));

		    mapStandardQuestionUid.put(mapIndex.toString(), voteQueContent.getUid().toString());
		    mapStandardToolSessionUid.put(mapIndex.toString(), toolSessionUid.toString());
		    mapStandardUserCount.put(mapIndex.toString(), new Integer(votesCount).toString());
		    totalStandardVotesCount = totalStandardVotesCount + votesCount;
		} else {
		    votesCount = voteService.getAttemptsForQuestionContent(voteQueContent.getUid());
		}

		double voteRate = 0d;
		double doubleVotesCount = votesCount * 1d;
		double doubleEntriesCount = entriesCount * 1d;
		if (entriesCount != 0) {
		    voteRate = ((doubleVotesCount * 100) / doubleEntriesCount);
		}

		String stringVoteRate = new Double(voteRate).toString();
		int lengthVoteRate = stringVoteRate.length();
		if (lengthVoteRate > 5)
		    stringVoteRate = stringVoteRate.substring(0, 6);

		mapVoteRatesContent.put(mapIndex.toString(), stringVoteRate);
		mapIndex = new Long(mapIndex.longValue() + 1);
	    }
	}

	Map mapStandardNominationsContent = new TreeMap(new VoteComparator());
	mapStandardNominationsContent = mapOptionsContent;

	Map mapStandardRatesContent = new TreeMap(new VoteComparator());
	mapStandardRatesContent = mapVoteRatesContent;

	int mapVoteRatesSize = mapVoteRatesContent.size();
	mapIndex = new Long(mapVoteRatesSize + 1);

	int userEnteredVotesCount = entriesCount - totalStandardVotesCount;
	double share = (userEnteredVotesCount != 0) ? ((userEnteredVotesCount * 100) / entriesCount) : 0;

	if (voteContent.isAllowText()) {
	    mapStandardNominationsContent.put(mapIndex.toString(), messageService.getMessage("label.open.vote"));
	    mapStandardNominationsHTMLedContent.put(mapIndex.toString(), messageService.getMessage("label.open.vote"));
	}

	mapStandardRatesContent.put(mapIndex.toString(), new Double(share).toString());
	mapStandardUserCount.put(mapIndex.toString(), new Integer(userEnteredVotesCount).toString());

	/** following are needed just for proper iteration in the summary jsp */
	mapStandardQuestionUid.put(mapIndex.toString(), "1");
	mapStandardToolSessionUid.put(mapIndex.toString(), "1");

	request.setAttribute(LIST_USER_ENTRIES_CONTENT, userEntries);

	request.getSession().setAttribute(MAP_STANDARD_NOMINATIONS_CONTENT, mapStandardNominationsContent);

	request.getSession().setAttribute(MAP_STANDARD_RATES_CONTENT, mapStandardRatesContent);

	if (voteGeneralLearnerFlowDTO != null) {
	    voteGeneralLearnerFlowDTO.setMapStandardNominationsContent(mapStandardNominationsContent);
	    voteGeneralLearnerFlowDTO.setMapStandardNominationsHTMLedContent(mapStandardNominationsHTMLedContent);
	    voteGeneralLearnerFlowDTO.setMapStandardRatesContent(mapStandardRatesContent);
	    voteGeneralLearnerFlowDTO.setMapStandardUserCount(mapStandardUserCount);
	    voteGeneralLearnerFlowDTO.setMapStandardToolSessionUid(mapStandardToolSessionUid);
	    voteGeneralLearnerFlowDTO.setMapStandardQuestionUid(mapStandardQuestionUid);
	}

	if (voteGeneralMonitoringDTO != null) {
	    voteGeneralMonitoringDTO.setMapStandardNominationsContent(mapStandardNominationsContent);
	    voteGeneralMonitoringDTO.setMapStandardNominationsHTMLedContent(mapStandardNominationsHTMLedContent);
	    voteGeneralMonitoringDTO.setMapStandardRatesContent(mapStandardRatesContent);
	    voteGeneralMonitoringDTO.setMapStandardUserCount(mapStandardUserCount);
	    voteGeneralMonitoringDTO.setMapStandardToolSessionUid(mapStandardToolSessionUid);
	    voteGeneralMonitoringDTO.setMapStandardQuestionUid(mapStandardQuestionUid);
	}

	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);
	request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
    }

    public static boolean notebookEntriesExist(IVoteService voteService, VoteContent voteContent) {
	Iterator iteratorSession = voteContent.getVoteSessions().iterator();
	while (iteratorSession.hasNext()) {
	    VoteSession voteSession = (VoteSession) iteratorSession.next();

	    if (voteSession != null) {

		Iterator iteratorUser = voteSession.getVoteQueUsers().iterator();
		while (iteratorUser.hasNext()) {
		    VoteQueUsr voteQueUsr = (VoteQueUsr) iteratorUser.next();

		    if (voteQueUsr != null) {
			NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(voteQueUsr.getQueUsrId()
					.intValue()));

			if (notebookEntry != null) {
			    return true;
			}

		    }
		}
	    }
	}
	return false;
    }

    public static void buildVoteStatsDTO(HttpServletRequest request, IVoteService voteService, VoteContent voteContent) {

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
    }

    public static Map<String, Map> buildGroupsAttemptData(HttpServletRequest request, VoteContent voteContent,
	    IVoteService voteService, VoteQueContent voteQueContent, String questionUid, String currentSessionId) {

	List listMonitoredAttemptsContainerDTO = new LinkedList();

	Map<String, String> summaryToolSessions = populateToolSessionsId(voteContent, voteService);

	Iterator itMap = summaryToolSessions.entrySet().iterator();

	/* request is for monitoring summary */

	if (currentSessionId != null) {
	    if (currentSessionId.equals("All")) {
		// **summary request is for All**
		while (itMap.hasNext()) {
		    Map.Entry pairs = (Map.Entry) itMap.next();
		    if (!(pairs.getValue().toString().equals("None")) && !(pairs.getValue().toString().equals("All"))) {
			VoteSession voteSession = voteService
				.retrieveVoteSession(new Long(pairs.getValue().toString()));
			if (voteSession != null) {
			    List<VoteQueUsr> listUsers = voteService.getUserBySessionOnly(voteSession);
			    Map sessionUsersAttempts = populateSessionUsersAttempts(request, voteService,
				    voteSession.getVoteSessionId(), listUsers, questionUid, true, false, null);
			    listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
			}
		    }
		}
	    } else if (!currentSessionId.equals("All")) {
		// **summary request is for currentSessionId** currentSessionId
		VoteSession voteSession = voteService.retrieveVoteSession(new Long(currentSessionId.toString()));

		List listUsers = voteService.getUserBySessionOnly(voteSession);

		Map sessionUsersAttempts = populateSessionUsersAttempts(request, voteService,
			new Long(currentSessionId), listUsers, questionUid, true, false, null);
		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
	    }
	}

	Map<String, Map> mapMonitoredAttemptsContainerDTO = convertToMap(listMonitoredAttemptsContainerDTO);
	return mapMonitoredAttemptsContainerDTO;
    }

    public static Map populateSessionUsersAttempts(HttpServletRequest request, IVoteService voteService,
	    Long sessionId, List<VoteQueUsr> users, String questionUid, boolean isUserNamesVisible,
	    boolean isLearnerRequest, String userId) {

	Map mapMonitoredUserContainerDTO = new TreeMap(new VoteStringComparator());
	List listMonitoredUserContainerDTO = new LinkedList();
	Iterator itUsers = users.iterator();

	if (userId == null) {
	    if ((isUserNamesVisible) && (!isLearnerRequest)) {
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
				// voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimezone());
				voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());
				voteMonitoredUserDTO.setUserName(voteQueUsr.getFullname());
				voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
				voteMonitoredUserDTO.setSessionId(sessionId.toString());
				voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());

				String responsePresentable = VoteUtils.replaceNewLines(voteUsrResp.getUserEntry());
				voteMonitoredUserDTO.setResponsePresentable(responsePresentable);

				voteMonitoredUserDTO.setQuestionUid(questionUid);
				voteMonitoredUserDTO.setVisible(new Boolean(voteUsrResp.isVisible()).toString());
				listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
			    }
			}
		    }
		}
	    } else if ((isUserNamesVisible) && (isLearnerRequest)) {
		// just populating data normally just like monitoring summary, except that the data is ony for a
		// specific session

		String userID = VoteUtils.getCurrentLearnerID();
		VoteQueUsr voteQueUsr = voteService.getVoteQueUsrById(new Long(userID).longValue());

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
				// voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimezone());
				voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());
				voteMonitoredUserDTO.setUserName(voteQueUsr.getFullname());
				voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
				voteMonitoredUserDTO.setSessionId(sessionId.toString());
				voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());

				String responsePresentable = VoteUtils.replaceNewLines(voteUsrResp.getUserEntry());
				voteMonitoredUserDTO.setResponsePresentable(responsePresentable);

				voteMonitoredUserDTO.setQuestionUid(questionUid);
				voteMonitoredUserDTO.setVisible(new Boolean(voteUsrResp.isVisible()).toString());
				listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
			    }
			}
		    }
		}
	    } else if ((!isUserNamesVisible) && (isLearnerRequest)) {
		// populating data normally exception are for a specific session and other user names are not visible
		// getting only current user's data

		String userID = VoteUtils.getCurrentLearnerID();
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
				// voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimezone());
				voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());

				if (userID.equals(voteQueUsr.getQueUsrId().toString())) {
				    voteMonitoredUserDTO.setUserName(voteQueUsr.getFullname());
				} else {
				    voteMonitoredUserDTO.setUserName("        ");
				}

				voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
				voteMonitoredUserDTO.setSessionId(sessionId.toString());
				voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());

				String responsePresentable = VoteUtils.replaceNewLines(voteUsrResp.getUserEntry());
				voteMonitoredUserDTO.setResponsePresentable(responsePresentable);

				voteMonitoredUserDTO.setQuestionUid(questionUid);
				voteMonitoredUserDTO.setVisible(new Boolean(voteUsrResp.isVisible()).toString());
				listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
			    }
			}
		    }
		}
	    }
	} else {
	    // request is for learner progress report
	    while (itUsers.hasNext()) {
		VoteQueUsr voteQueUsr = (VoteQueUsr) itUsers.next();

		if (voteQueUsr != null) {
		    List listUserAttempts = voteService.getAttemptsForUserAndQuestionContent(voteQueUsr.getUid(),
			    new Long(questionUid));

		    Iterator itAttempts = listUserAttempts.iterator();
		    while (itAttempts.hasNext()) {
			VoteUsrAttempt voteUsrResp = (VoteUsrAttempt) itAttempts.next();

			if (voteUsrResp != null) {
			    if (userId.equals(voteQueUsr.getQueUsrId().toString())) {
				VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
				voteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime());
				// voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimezone());
				voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());
				voteMonitoredUserDTO.setUserName(voteQueUsr.getFullname());
				voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
				voteMonitoredUserDTO.setSessionId(sessionId.toString());
				voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());

				String responsePresentable = VoteUtils.replaceNewLines(voteUsrResp.getUserEntry());
				voteMonitoredUserDTO.setResponsePresentable(responsePresentable);

				voteMonitoredUserDTO.setQuestionUid(questionUid);
				voteMonitoredUserDTO.setVisible(new Boolean(voteUsrResp.isVisible()).toString());
				listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
			    }
			}
		    }
		}
	    }

	}

	mapMonitoredUserContainerDTO = convertToMcMonitoredUserDTOMap(listMonitoredUserContainerDTO);
	return mapMonitoredUserContainerDTO;
    }

    public static Map convertToMcMonitoredUserDTOMap(List list) {
	Map map = new TreeMap(new VoteStringComparator());

	Iterator listIterator = list.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    VoteMonitoredUserDTO data = (VoteMonitoredUserDTO) listIterator.next();
	    map.put(mapIndex.toString(), data);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    public static void repopulateRequestParameters(HttpServletRequest request, VoteMonitoringForm voteMonitoringForm,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) {

	String toolContentID = request.getParameter(VoteAppConstants.TOOL_CONTENT_ID);
	voteMonitoringForm.setToolContentID(toolContentID);
	voteGeneralMonitoringDTO.setToolContentID(toolContentID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	voteMonitoringForm.setActiveModule(activeModule);
	voteGeneralMonitoringDTO.setActiveModule(activeModule);

	String defineLaterInEditMode = request.getParameter(VoteAppConstants.DEFINE_LATER_IN_EDIT_MODE);
	voteMonitoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);

	String responseId = request.getParameter(VoteAppConstants.RESPONSE_ID);
	voteMonitoringForm.setResponseId(responseId);
	voteGeneralMonitoringDTO.setResponseId(responseId);

	String currentUid = request.getParameter(VoteAppConstants.CURRENT_UID);
	voteMonitoringForm.setCurrentUid(currentUid);
	voteGeneralMonitoringDTO.setCurrentUid(currentUid);
    }

}
