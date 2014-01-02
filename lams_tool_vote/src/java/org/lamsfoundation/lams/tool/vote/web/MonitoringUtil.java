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
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.vote.VoteAllGroupsDTO;
import org.lamsfoundation.lams.tool.vote.VoteAllSessionsDTO;
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

    public static Map populateToolSessions(HttpServletRequest request, VoteContent voteContent, IVoteService voteService) {
	List sessionsList = voteService.getSessionNamesFromContent(voteContent);

	Map sessionsMap = VoteUtils.convertToStringMap(sessionsList, "String");

	if (sessionsMap.isEmpty()) {
	    sessionsMap.put(new Long(1).toString(), "None");
	} else {
	    sessionsMap.put(new Long(sessionsMap.size() + 1).toString(), "All");
	}

	return sessionsMap;
    }

    /**
     * 
     * used in presenting user votes data
     * 
     * @param request
     * @param voteContent
     * @param isUserNamesVisible
     * @param isLearnerRequest
     * @param currentSessionId
     * @param userId
     * @param voteService
     * @return
     */
    public static List buildGroupsQuestionData(HttpServletRequest request, VoteContent voteContent,
	    boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId,
	    IVoteService voteService) {
	List listQuestions = voteService.getAllQuestionEntries(voteContent.getUid());

	List listMonitoredAnswersContainerDTO = new LinkedList();

	Iterator itListQuestions = listQuestions.iterator();
	while (itListQuestions.hasNext()) {
	    VoteQueContent voteQueContent = (VoteQueContent) itListQuestions.next();

	    if (voteQueContent != null) {
		VoteMonitoredAnswersDTO voteMonitoredAnswersDTO = new VoteMonitoredAnswersDTO();
		voteMonitoredAnswersDTO.setQuestionUid(voteQueContent.getUid().toString());
		voteMonitoredAnswersDTO.setQuestion(voteQueContent.getQuestion());

		Map questionAttemptData = buildGroupsAttemptData(request, voteContent, voteQueContent, voteQueContent
			.getUid().toString(), isUserNamesVisible, isLearnerRequest, currentSessionId, userId,
			voteService);
		voteMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
		listMonitoredAnswersContainerDTO.add(voteMonitoredAnswersDTO);

	    }
	}
	return listMonitoredAnswersContainerDTO;
    }

    /**
     * groups user's votes
     * 
     * @param request
     * @param voteContent
     * @param voteQueContent
     * @param questionUid
     * @param isUserNamesVisible
     * @param isLearnerRequest
     * @param currentSessionId
     * @param userId
     * @param voteService
     * @return
     */
    public static Map buildGroupsAttemptData(HttpServletRequest request, VoteContent voteContent,
	    VoteQueContent voteQueContent, String questionUid, boolean isUserNamesVisible, boolean isLearnerRequest,
	    String currentSessionId, String userId, IVoteService voteService) {

	Map mapMonitoredAttemptsContainerDTO = new TreeMap(new VoteComparator());
	List listMonitoredAttemptsContainerDTO = new LinkedList();

	Map summaryToolSessions = populateToolSessionsId(request, voteContent, voteService);

	Iterator itMap = summaryToolSessions.entrySet().iterator();

	/*request is for monitoring summary */
	if (!isLearnerRequest) {
	    while (itMap.hasNext()) {
		Map.Entry pairs = (Map.Entry) itMap.next();

		if (!(pairs.getValue().toString().equals("None")) && !(pairs.getValue().toString().equals("All"))) {
		    VoteSession voteSession = voteService.retrieveVoteSession(new Long(pairs.getValue().toString()));
		    if (voteSession != null) {
			List listUsers = voteService.getUserBySessionOnly(voteSession);
			Map sessionUsersAttempts = populateSessionUsersAttempts(request, voteContent, voteSession
				.getVoteSessionId(), listUsers, questionUid, isUserNamesVisible, isLearnerRequest,
				userId, voteService);
			listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
		    }
		}
	    }
	} else {
	    /*request is for learner report, use only the passed tool session in the report*/
	    while (itMap.hasNext()) {
		Map.Entry pairs = (Map.Entry) itMap.next();

		if (!(pairs.getValue().toString().equals("None")) && !(pairs.getValue().toString().equals("All"))) {

		    if (currentSessionId.equals(pairs.getValue())) {
			VoteSession voteSession = voteService
				.retrieveVoteSession(new Long(pairs.getValue().toString()));
			if (voteSession != null) {
			    List listUsers = voteService.getUserBySessionOnly(voteSession);
			    Map sessionUsersAttempts = populateSessionUsersAttempts(request, voteContent, voteSession
				    .getVoteSessionId(), listUsers, questionUid, isUserNamesVisible, isLearnerRequest,
				    userId, voteService);
			    listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
			}
		    }
		}
	    }
	}

	mapMonitoredAttemptsContainerDTO = convertToMap(listMonitoredAttemptsContainerDTO);
	return mapMonitoredAttemptsContainerDTO;
    }

    public static Map populateSessionUsersAttempts(HttpServletRequest request, VoteContent voteContent, Long sessionId,
	    List listUsers, String questionUid, boolean isUserNamesVisible, boolean isLearnerRequest, String userId,
	    IVoteService voteService) {

	Map mapMonitoredUserContainerDTO = new TreeMap(new VoteStringComparator());
	List listMonitoredUserContainerDTO = new LinkedList();
	Iterator itUsers = listUsers.iterator();

	if (userId == null) {
	    if ((isUserNamesVisible) && (!isLearnerRequest)) {
		while (itUsers.hasNext()) {
		    VoteQueUsr voteQueUsr = (VoteQueUsr) itUsers.next();

		    if (voteQueUsr != null) {
			List listUserAttempts = voteService.getAttemptsForUserAndQuestionContent(voteQueUsr
				.getUid(), new Long(questionUid));

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
				if (voteContent.getVoteContentId().toString().equals(
					localUserSession.getVoteContentId().toString())) {
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
		//summary reporting case 2
		//just populating data normally just like monitoring summary, except that the data is ony for a specific session
		String userID = (String) request.getSession().getAttribute(USER_ID);
		VoteQueUsr voteQueUsr = voteService.getVoteQueUsrById(new Long(userID).longValue());

		while (itUsers.hasNext()) {
		    voteQueUsr = (VoteQueUsr) itUsers.next();

		    if (voteQueUsr != null) {
			List listUserAttempts = voteService.getAttemptsForUserAndQuestionContent(voteQueUsr
				.getUid(), new Long(questionUid));

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
				if (voteContent.getVoteContentId().toString().equals(
					localUserSession.getVoteContentId().toString())) {
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
		//summary reporting case 3
		//populating data normally exception are for a specific session and other user names are not visible
		String userID = (String) request.getSession().getAttribute(USER_ID);

		while (itUsers.hasNext()) {
		    VoteQueUsr voteQueUsr = (VoteQueUsr) itUsers.next();

		    if (voteQueUsr != null) {
			List listUserAttempts = voteService.getAttemptsForUserAndQuestionContent(voteQueUsr
				.getUid(), new Long(questionUid));

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
				if (voteContent.getVoteContentId().toString().equals(
					localUserSession.getVoteContentId().toString())) {
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
	    //summary reporting case 4
	    //request is for learner progress report userId
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
				//this is the user requested , include his name for learner progress
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
				if (voteContent.getVoteContentId().toString().equals(
					localUserSession.getVoteContentId().toString())) {
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

	mapMonitoredUserContainerDTO = convertToVoteMonitoredUserDTOMap(listMonitoredUserContainerDTO);
	return mapMonitoredUserContainerDTO;
    }

    public static Map populateToolSessionsId(HttpServletRequest request, VoteContent voteContent,
	    IVoteService voteService) {
	List sessionsList = voteService.getSessionsFromContent(voteContent);

	Map sessionsMap = VoteUtils.convertToStringMap(sessionsList, "Long");

	if (sessionsMap.isEmpty()) {
	    sessionsMap.put(new Long(1).toString(), "None");
	} else {
	    sessionsMap.put(new Long(sessionsMap.size() + 1).toString(), "All");
	}

	return sessionsMap;
    }

    public static Map convertToVoteMonitoredUserDTOMap(List list) {
	Map map = new TreeMap(new VoteComparator());

	Iterator listIterator = list.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    VoteMonitoredUserDTO data = (VoteMonitoredUserDTO) listIterator.next();

	    map.put(mapIndex.toString(), data);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    public static double calculateTotal(Map mapVoteRatesContent) {
	double total = 0d;
	Iterator itMap = mapVoteRatesContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();

	    if (pairs.getValue() != null) {
		total = total + new Double(pairs.getValue().toString()).doubleValue();
	    }
	}
	return total;
    }

    public static Map convertToMap(List list) {
	Map map = new TreeMap(new VoteComparator());

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
     * Generates chart data for all sessions in the Monitoring Summary.
     * 
     * @param request
     * @param voteService
     * @param voteMonitoringForm
     * @param toolContentID
     * @return
     */
    public static List prepareChartDTO(HttpServletRequest request, IVoteService voteService,
	    VoteMonitoringForm voteMonitoringForm, Long toolContentID, MessageService messageService) {

	List listVoteAllSessionsDTO = new LinkedList();

	VoteContent voteContent = voteService.retrieveVote(toolContentID);

	Iterator itListSessions = voteContent.getVoteSessions().iterator();
	while (itListSessions.hasNext()) {
	    VoteSession voteSession = (VoteSession) itListSessions.next();

	    Map mapOptionsContent = new TreeMap(new VoteComparator());
	    Map mapVoteRatesContent = new TreeMap(new VoteComparator());

	    VoteAllSessionsDTO voteAllSessionsDTO = new VoteAllSessionsDTO();
	    voteAllSessionsDTO.setSessionId(voteSession.getVoteSessionId().toString());
	    voteAllSessionsDTO.setSessionName(voteSession.getSession_name());

	    int entriesCount = voteService.getSessionEntriesCount(voteSession.getUid());
	    Set userEntries = voteService.getSessionUserEntriesSet(voteSession.getUid());

	    int potentialUserCount = voteService.getVoteSessionPotentialLearnersCount(voteSession.getUid());
	    voteAllSessionsDTO.setSessionUserCount(Integer.toString(potentialUserCount));

	    int completedSessionUserCount = voteService.getCompletedVoteUserBySessionUid(voteSession.getUid());
	    voteAllSessionsDTO.setCompletedSessionUserCount(new Integer(completedSessionUserCount).toString());

	    if (potentialUserCount != 0) {
		double completedPercent = (completedSessionUserCount * 100) / potentialUserCount;

		if (completedPercent > 100)
		    completedPercent = 100;

		voteAllSessionsDTO.setCompletedSessionUserPercent(new Double(completedPercent).toString());
	    } else {
		voteAllSessionsDTO.setCompletedSessionUserPercent("Not Available");
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
		    votesCount = voteService.getStandardAttemptsForQuestionContentAndSessionUid(
			    voteQueContent.getUid(), voteSession.getUid());

		    mapStandardQuestionUid.put(mapIndex.toString(), voteQueContent.getUid().toString());
		    mapStandardToolSessionUid.put(mapIndex.toString(), voteSession.getUid());

		    mapStandardUserCount.put(mapIndex.toString(), new Integer(votesCount).toString());
		    totalStandardVotesCount = totalStandardVotesCount + votesCount;

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

	    Iterator itListQuestions = userEntries.iterator();
	    int mapVoteRatesSize = mapVoteRatesContent.size();
	    mapIndex = new Long(mapVoteRatesSize + 1);

	    double total = MonitoringUtil.calculateTotal(mapVoteRatesContent);
	    double share = 100d - total;
	    int userEnteredVotesCount = entriesCount - totalStandardVotesCount;

	    if (userEnteredVotesCount != 0) {
		share = ((userEnteredVotesCount * 100) / entriesCount);
	    } else {
		share = 0;
	    }

	    if (voteContent.isAllowText()) {
		mapStandardNominationsContent.put(mapIndex.toString(), messageService.getMessage("label.open.vote"));
		mapStandardNominationsHTMLedContent.put(mapIndex.toString(), messageService
			.getMessage("label.open.vote"));
	    }
	    mapStandardRatesContent.put(mapIndex.toString(), new Double(share).toString());
	    mapStandardUserCount.put(mapIndex.toString(), new Integer(userEnteredVotesCount).toString());
	    /** following are needed just for proper iteration in the summary jsp */
	    mapStandardQuestionUid.put(mapIndex.toString(), "1");
	    mapStandardToolSessionUid.put(mapIndex.toString(), "1");

	    voteAllSessionsDTO.setMapStandardNominationsContent(mapStandardNominationsContent);
	    voteAllSessionsDTO.setMapStandardNominationsHTMLedContent(mapStandardNominationsHTMLedContent);
	    voteAllSessionsDTO.setMapStandardUserCount(mapStandardUserCount);
	    voteAllSessionsDTO.setMapStandardRatesContent(mapStandardRatesContent);
	    voteAllSessionsDTO.setMapStandardQuestionUid(mapStandardQuestionUid);
	    voteAllSessionsDTO.setMapStandardToolSessionUid(mapStandardToolSessionUid);

	    VoteMonitoringAction voteMonitoringAction = new VoteMonitoringAction();
	    List listUserEntries = voteMonitoringAction.processUserEnteredNominations(voteService, voteContent,
		    voteSession.getVoteSessionId().toString(), true, null, false);
	    voteAllSessionsDTO.setListUserEntries(listUserEntries);

	    if (listUserEntries.size() > 0)
		voteAllSessionsDTO.setExistsOpenVote(new Boolean(true).toString());
	    else
		voteAllSessionsDTO.setExistsOpenVote(new Boolean(false).toString());

	    listVoteAllSessionsDTO.add(voteAllSessionsDTO);
	}

	return listVoteAllSessionsDTO;
    }

    /**
     * Generates chart data for the learner module and monitoring module
     * Summary tab (Individual Sessions mode)
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

		if (potentialUserCount != 0) {
		    double completedPercent = (completedSessionUserCount * 100) / potentialUserCount;
		    if (completedPercent > 100)
			completedPercent = 100;

		    voteMonitoringForm.setCompletedSessionUserPercent(new Double(completedPercent).toString());
		    if (voteGeneralMonitoringDTO != null) {
			voteGeneralMonitoringDTO
				.setCompletedSessionUserPercent(new Double(completedPercent).toString());
		    }
		} else {
		    voteMonitoringForm.setCompletedSessionUserPercent("Not Available");
		    if (voteGeneralMonitoringDTO != null) {
			voteGeneralMonitoringDTO.setCompletedSessionUserPercent("Not Available");
		    }
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

	double total = MonitoringUtil.calculateTotal(mapVoteRatesContent);
	double share = 100 - total;

	int userEnteredVotesCount = entriesCount - totalStandardVotesCount;

	if (userEnteredVotesCount != 0) {
	    share = ((userEnteredVotesCount * 100) / entriesCount);
	} else {
	    share = 0;
	}

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

    public static void generateGroupsSessionData(HttpServletRequest request, IVoteService voteService,
	    VoteContent voteContent) {
	List listAllGroupsDTO = buildGroupBasedSessionData(request, voteContent, voteService);
	request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);
    }

    public static List buildGroupBasedSessionData(HttpServletRequest request, VoteContent voteContent,
	    IVoteService voteService) {
	List listQuestions = voteService.getAllQuestionEntries(voteContent.getUid());
	List listAllGroupsContainerDTO = new LinkedList();

	Iterator iteratorSession = voteContent.getVoteSessions().iterator();
	while (iteratorSession.hasNext()) {
	    VoteSession voteSession = (VoteSession) iteratorSession.next();
	    String currentSessionId = voteSession.getVoteSessionId().toString();

	    String currentSessionName = voteSession.getSession_name();

	    VoteAllGroupsDTO voteAllGroupsDTO = new VoteAllGroupsDTO();
	    List listMonitoredAnswersContainerDTO = new LinkedList();

	    if (voteSession != null) {
		Iterator itListQuestions = listQuestions.iterator();
		while (itListQuestions.hasNext()) {
		    VoteQueContent voteQueContent = (VoteQueContent) itListQuestions.next();

		    if (voteQueContent != null) {
			VoteMonitoredAnswersDTO voteMonitoredAnswersDTO = new VoteMonitoredAnswersDTO();
			voteMonitoredAnswersDTO.setQuestionUid(voteQueContent.getUid().toString());
			voteMonitoredAnswersDTO.setQuestion(voteQueContent.getQuestion());
			voteMonitoredAnswersDTO.setSessionId(currentSessionId);
			voteMonitoredAnswersDTO.setSessionName(currentSessionName);

			Map questionAttemptData = buildGroupsAttemptData(request, voteContent, voteService,
				voteQueContent, voteQueContent.getUid().toString(), true, false, currentSessionId, null);
			voteMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);

			listMonitoredAnswersContainerDTO.add(voteMonitoredAnswersDTO);
		    }
		}
	    }
	    voteAllGroupsDTO.setGroupData(listMonitoredAnswersContainerDTO);
	    voteAllGroupsDTO.setSessionName(currentSessionName);
	    voteAllGroupsDTO.setSessionId(currentSessionId);

	    listAllGroupsContainerDTO.add(voteAllGroupsDTO);

	}

	return listAllGroupsContainerDTO;
    }

    public static Map buildGroupsAttemptData(HttpServletRequest request, VoteContent voteContent,
	    IVoteService voteService, VoteQueContent voteQueContent, String questionUid, boolean isUserNamesVisible,
	    boolean isLearnerRequest, String currentSessionId, String userId) {

	Map mapMonitoredAttemptsContainerDTO = new TreeMap(new VoteStringComparator());
	List listMonitoredAttemptsContainerDTO = new LinkedList();

	Map summaryToolSessions = populateToolSessionsId(request, voteContent, voteService);

	Iterator itMap = summaryToolSessions.entrySet().iterator();

	/*request is for monitoring summary */
	if (!isLearnerRequest) {

	    if (currentSessionId != null) {
		if (currentSessionId.equals("All")) {
		    //**summary request is for All**
		    while (itMap.hasNext()) {
			Map.Entry pairs = (Map.Entry) itMap.next();
			if (!(pairs.getValue().toString().equals("None"))
				&& !(pairs.getValue().toString().equals("All"))) {
			    VoteSession voteSession = voteService.retrieveVoteSession(new Long(pairs.getValue()
				    .toString()));
			    if (voteSession != null) {
				List listUsers = voteService.getUserBySessionOnly(voteSession);
				Map sessionUsersAttempts = populateSessionUsersAttempts(request, voteService,
					voteSession.getVoteSessionId(), listUsers, questionUid, isUserNamesVisible,
					isLearnerRequest, userId);
				listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
			    }
			}
		    }
		} else if (!currentSessionId.equals("All")) {
		    //**summary request is for currentSessionId** currentSessionId
		    VoteSession voteSession = voteService.retrieveVoteSession(new Long(currentSessionId.toString()));

		    List listUsers = voteService.getUserBySessionOnly(voteSession);

		    Map sessionUsersAttempts = populateSessionUsersAttempts(request, voteService, new Long(
			    currentSessionId), listUsers, questionUid, isUserNamesVisible, isLearnerRequest, userId);
		    listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
		}
	    }
	} else {
	    /*request is for learner report, use only the passed tool session in the report*/
	    while (itMap.hasNext()) {
		Map.Entry pairs = (Map.Entry) itMap.next();

		if (!(pairs.getValue().toString().equals("None")) && !(pairs.getValue().toString().equals("All"))) {

		    if (currentSessionId.equals(pairs.getValue())) {
			VoteSession voteSession = voteService
				.retrieveVoteSession(new Long(pairs.getValue().toString()));
			if (voteSession != null) {
			    List listUsers = voteService.getUserBySessionOnly(voteSession);
			    Map sessionUsersAttempts = populateSessionUsersAttempts(request, voteService, voteSession
				    .getVoteSessionId(), listUsers, questionUid, isUserNamesVisible, isLearnerRequest,
				    userId);
			    listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
			}
		    }
		}
	    }
	}

	mapMonitoredAttemptsContainerDTO = convertToMap(listMonitoredAttemptsContainerDTO);
	return mapMonitoredAttemptsContainerDTO;
    }

    public static Map populateSessionUsersAttempts(HttpServletRequest request, IVoteService voteService,
	    Long sessionId, List listUsers, String questionUid, boolean isUserNamesVisible, boolean isLearnerRequest,
	    String userId) {

	Map mapMonitoredUserContainerDTO = new TreeMap(new VoteStringComparator());
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
				//voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimezone());
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
		//just populating data normally just like monitoring summary, except that the data is ony for a specific session

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
				//voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimezone());
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
		//populating data normally exception are for a specific session and other user names are not visible
		//getting only current user's data

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
				//voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimezone());
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
	    //request is for learner progress report
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
				//voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimezone());
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

}
