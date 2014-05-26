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

package org.lamsfoundation.lams.tool.vote.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.learningdesign.dao.IDataFlowDAO;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dao.IVoteContentDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteQueContentDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteSessionDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUserDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUsrAttemptDAO;
import org.lamsfoundation.lams.tool.vote.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.vote.dto.SessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteQuestionDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.tool.vote.web.MonitoringUtil;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.springframework.dao.DataAccessException;

/**
 * The POJO implementation of Voting service. All business logic of Voting tool is implemented in this class. It
 * translates the request from presentation layer and performs appropriate database operation.
 * 
 * @author Ozgur Demirtas
 */
public class VoteServicePOJO implements IVoteService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager, VoteAppConstants {
    private static Logger logger = Logger.getLogger(VoteServicePOJO.class.getName());

    private IVoteContentDAO voteContentDAO;
    private IVoteQueContentDAO voteQueContentDAO;
    private IVoteSessionDAO voteSessionDAO;
    private IVoteUserDAO voteUserDAO;
    private IVoteUsrAttemptDAO voteUsrAttemptDAO;

    private IUserManagementService userManagementService;
    private ILearnerService learnerService;
    private IAuditService auditService;
    private ILamsToolService toolService;
    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;
    private IToolContentHandler voteToolContentHandler = null;
    private VoteOutputFactory voteOutputFactory;
    private IDataFlowDAO dataFlowDAO;

    private MessageService messageService;
    private ILamsCoreToolService lamsCoreToolService;

    public VoteServicePOJO() {
    }

    @Override
    public boolean isUserGroupLeader(VoteQueUsr user, Long toolSessionId) {

	VoteSession session = getSessionBySessionId(toolSessionId);
	VoteQueUsr groupLeader = session.getGroupLeader();

	boolean isUserLeader = (groupLeader != null) && user.getUid().equals(groupLeader.getUid());
	return isUserLeader;
    }

    @Override
    public VoteQueUsr checkLeaderSelectToolForSessionLeader(VoteQueUsr user, Long toolSessionId) {
	if (user == null || toolSessionId == null) {
	    return null;
	}

	VoteSession session = getSessionBySessionId(toolSessionId);
	VoteQueUsr leader = session.getGroupLeader();
	// check leader select tool for a leader only in case QA tool doesn't know it. As otherwise it will screw
	// up previous scratches done
	if (leader == null) {

	    Long leaderUserId = toolService.getLeaderUserId(toolSessionId, user.getQueUsrId().intValue());
	    if (leaderUserId != null) {

		leader = getVoteUserBySession(leaderUserId, session.getUid());

		// create new user in a DB
		if (leader == null) {
		    logger.debug("creating new user with userId: " + leaderUserId);
		    User leaderDto = (User) getUserManagementService().findById(User.class, leaderUserId.intValue());
		    String userName = leaderDto.getLogin();
		    String fullName = leaderDto.getFirstName() + " " + leaderDto.getLastName();
		    leader = new VoteQueUsr(leaderUserId, userName, fullName, session, new TreeSet());
		    voteUserDAO.saveVoteUser(user);
		}

		// set group leader
		session.setGroupLeader(leader);
		this.updateVoteSession(session);
	    }
	}

	return leader;
    }

    @Override
    public void copyAnswersFromLeader(VoteQueUsr user, VoteQueUsr leader) {

	if ((user == null) || (leader == null) || user.getUid().equals(leader.getUid())) {
	    return;
	}

	List<VoteUsrAttempt> leaderAttempts = this.getAttemptsForUser(leader.getUid());
	List<VoteUsrAttempt> userAttempts = this.getAttemptsForUser(user.getUid());

	for (VoteUsrAttempt leaderAttempt : leaderAttempts) {

	    VoteQueContent question = leaderAttempt.getVoteQueContent();
	    Date attempTime = leaderAttempt.getAttemptTime();
	    String timeZone = leaderAttempt.getTimeZone();
	    String userEntry = leaderAttempt.getUserEntry();

	    VoteUsrAttempt userAttempt = null;
	    for (VoteUsrAttempt userAttemptDb : userAttempts) {
		if (userAttemptDb.getUid().equals(leaderAttempt.getUid())) {
		    userAttempt = userAttemptDb;
		}
	    }

	    // if response doesn't exist - create VoteUsrAttempt in the db
	    if (userAttempt == null) {
		VoteUsrAttempt voteUsrAttempt = new VoteUsrAttempt(attempTime, timeZone, question, user, userEntry,
			true);
		this.createVoteUsrAttempt(voteUsrAttempt);

		// if it's been changed by the leader
	    } else if (leaderAttempt.getAttemptTime().compareTo(userAttempt.getAttemptTime()) != 0) {
		userAttempt.setUserEntry(userEntry);
		userAttempt.setAttemptTime(attempTime);
		userAttempt.setTimeZone(timeZone);
		this.updateVoteUsrAttempt(userAttempt);

		// remove userAttempt from the list so we can know which one is redundant(presumably, leader has removed
		// this one)
		userAttempts.remove(userAttempt);
	    }
	}

	// remove redundant ones
	for (VoteUsrAttempt redundantUserAttempt : userAttempts) {
	    this.removeAttempt(redundantUserAttempt);
	}
    }

    @Override
    public VoteGeneralLearnerFlowDTO prepareChartData(HttpServletRequest request, Long toolContentID,
	    Long toolSessionUid, VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO) {

	VoteContent voteContent = this.getVoteContent(toolContentID);

	int entriesCount = 0;
	Set<VoteUsrAttempt> userEntries = null;
	if (toolSessionUid != null) {
	    entriesCount = this.getSessionEntriesCount(toolSessionUid);
	    userEntries = this.getSessionUserEntriesSet(toolSessionUid);
	}

	Long mapIndex = 1L;
	int totalStandardVotesCount = 0;

	Map<Long, Long> mapStandardUserCount = new TreeMap<Long, Long>(new VoteComparator());
	Map<Long, String> mapStandardNominationsHTMLedContent = new TreeMap<Long, String>(new VoteComparator());
	Map<Long, Long> mapStandardQuestionUid = new TreeMap<Long, Long>(new VoteComparator());
	Map<Long, Long> mapStandardToolSessionUid = new TreeMap<Long, Long>(new VoteComparator());
	Map<Long, String> mapStandardNominationsContent = new TreeMap<Long, String>(new VoteComparator());
	Map<Long, Double> mapVoteRates = new TreeMap<Long, Double>(new VoteComparator());

	for (VoteQueContent question : (Set<VoteQueContent>) voteContent.getVoteQueContents()) {

	    mapStandardNominationsHTMLedContent.put(mapIndex, question.getQuestion());
	    String noHTMLNomination = VoteUtils.stripHTML(question.getQuestion());
	    mapStandardNominationsContent.put(mapIndex, noHTMLNomination);

	    int votesCount = this.getStandardAttemptsForQuestionContentAndSessionUid(question.getUid(), toolSessionUid);
	    totalStandardVotesCount += votesCount;
	    mapStandardUserCount.put(mapIndex, new Long(votesCount));

	    mapStandardQuestionUid.put(mapIndex, question.getUid());
	    mapStandardToolSessionUid.put(mapIndex, toolSessionUid);

	    Double voteRate = (entriesCount != 0) ? ((votesCount * 100) / entriesCount) : 0d;
	    mapVoteRates.put(mapIndex, voteRate);

	    // mapIndex++
	    mapIndex = new Long(mapIndex + 1);
	}

	// open votes
	if (voteContent.isAllowText()) {
	    int userEnteredVotesCount = entriesCount - totalStandardVotesCount;
	    Double voteRate = (userEnteredVotesCount != 0) ? ((userEnteredVotesCount * 100) / entriesCount) : 0d;
	    mapVoteRates.put(mapIndex, voteRate);

	    mapStandardNominationsContent.put(mapIndex, messageService.getMessage("label.open.vote"));
	    mapStandardNominationsHTMLedContent.put(mapIndex, messageService.getMessage("label.open.vote"));
	    mapStandardUserCount.put(mapIndex, new Long(userEnteredVotesCount));
	    /** following are needed just for proper iteration in the summary jsp */
	    mapStandardQuestionUid.put(mapIndex, 1L);
	    mapStandardToolSessionUid.put(mapIndex, 1L);
	}

	voteGeneralLearnerFlowDTO.setMapStandardNominationsContent(mapStandardNominationsContent);
	voteGeneralLearnerFlowDTO.setMapStandardNominationsHTMLedContent(mapStandardNominationsHTMLedContent);
	voteGeneralLearnerFlowDTO.setMapStandardRatesContent(mapVoteRates);
	voteGeneralLearnerFlowDTO.setMapStandardUserCount(mapStandardUserCount);
	voteGeneralLearnerFlowDTO.setMapStandardToolSessionUid(mapStandardToolSessionUid);
	voteGeneralLearnerFlowDTO.setMapStandardQuestionUid(mapStandardQuestionUid);
	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	request.setAttribute(LIST_USER_ENTRIES_CONTENT, userEntries);

	// return value is only used in VoteChartGeneratorAction
	return voteGeneralLearnerFlowDTO;
    }

    @Override
    public LinkedList<SessionDTO> getSessionDTOs(Long toolContentID) {

	LinkedList<SessionDTO> sessionDTOs = new LinkedList<SessionDTO>();

	VoteContent voteContent = this.getVoteContent(toolContentID);
	for (VoteSession session : (Set<VoteSession>) voteContent.getVoteSessions()) {

	    SessionDTO sessionDTO = new SessionDTO();
	    sessionDTO.setSessionId(session.getVoteSessionId().toString());
	    sessionDTO.setSessionName(session.getSession_name());

	    int entriesCount = this.getSessionEntriesCount(session.getUid());

	    // potentialUserCount
	    int potentialUserCount = this.getVoteSessionPotentialLearnersCount(session.getUid());
	    sessionDTO.setSessionUserCount(potentialUserCount);

	    // completedSessionUserCount
	    int completedSessionUserCount = this.getCompletedVoteUserBySessionUid(session.getUid());
	    sessionDTO.setCompletedSessionUserCount(completedSessionUserCount);

	    Long mapIndex = 1L;
	    int totalStandardVotesCount = 0;

	    Map<Long, Double> mapVoteRates = new TreeMap<Long, Double>(new VoteComparator());
	    Map<Long, Long> mapStandardUserCount = new TreeMap<Long, Long>(new VoteComparator());
	    Map<Long, String> mapStandardNominationsHTMLedContent = new TreeMap<Long, String>(new VoteComparator());
	    Map<Long, Long> mapStandardQuestionUid = new TreeMap<Long, Long>(new VoteComparator());
	    Map<Long, Long> mapStandardToolSessionUid = new TreeMap<Long, Long>(new VoteComparator());

	    for (VoteQueContent question : (Set<VoteQueContent>) voteContent.getVoteQueContents()) {
		mapStandardNominationsHTMLedContent.put(mapIndex, question.getQuestion());

		int votesCount = this.getStandardAttemptsForQuestionContentAndSessionUid(question.getUid(),
			session.getUid());
		totalStandardVotesCount += votesCount;
		mapStandardUserCount.put(mapIndex, new Long(votesCount));

		mapStandardQuestionUid.put(mapIndex, question.getUid());
		mapStandardToolSessionUid.put(mapIndex, session.getUid());

		Double voteRate = (entriesCount != 0) ? ((votesCount * 100) / entriesCount) : 0d;
		mapVoteRates.put(mapIndex, voteRate);

		// mapIndex++
		mapIndex = new Long(mapIndex + 1);
	    }

	    // open votes
	    if (voteContent.isAllowText()) {
		int userEnteredVotesCount = entriesCount - totalStandardVotesCount;
		Double voteRate = (userEnteredVotesCount != 0) ? ((userEnteredVotesCount * 100) / entriesCount) : 0d;
		mapVoteRates.put(mapIndex, voteRate);

		mapStandardNominationsHTMLedContent.put(mapIndex, messageService.getMessage("label.open.vote"));
		mapStandardUserCount.put(mapIndex, new Long(userEnteredVotesCount));
		/** following are needed just for proper iteration in the summary jsp */
		mapStandardQuestionUid.put(mapIndex, 1L);
		mapStandardToolSessionUid.put(mapIndex, 1L);
	    }

	    sessionDTO.setMapStandardNominationsHTMLedContent(mapStandardNominationsHTMLedContent);
	    sessionDTO.setMapStandardUserCount(mapStandardUserCount);
	    sessionDTO.setMapStandardRatesContent(mapVoteRates);
	    sessionDTO.setMapStandardQuestionUid(mapStandardQuestionUid);
	    sessionDTO.setMapStandardToolSessionUid(mapStandardToolSessionUid);

	    List<VoteMonitoredAnswersDTO> openVotes = this.getOpenVotes(voteContent.getUid(),
		    session.getVoteSessionId(), null);
	    sessionDTO.setOpenVotes(openVotes);
	    boolean isExistsOpenVote = openVotes.size() > 0;
	    sessionDTO.setExistsOpenVote(isExistsOpenVote);

	    sessionDTOs.add(sessionDTO);
	}

	// All groups total
	if (sessionDTOs.size() > 1) {
	    SessionDTO totalSessionDTO = new SessionDTO();
	    totalSessionDTO.setSessionId("0");
	    totalSessionDTO.setSessionName(messageService.getMessage("label.all.groups.total"));

	    List<VoteMonitoredAnswersDTO> totalOpenVotes = new ArrayList<VoteMonitoredAnswersDTO>();
	    int totalPotentialUserCount = 0;
	    int totalCompletedSessionUserCount = 0;
	    int allSessionsVotesCount = 0;
	    Map<Long, Long> totalMapStandardUserCount = new TreeMap<Long, Long>(new VoteComparator());
	    for (SessionDTO sessionDTO : sessionDTOs) {

		totalPotentialUserCount += sessionDTO.getSessionUserCount();
		totalCompletedSessionUserCount += sessionDTO.getCompletedSessionUserCount();

		Long mapIndex = 1L;
		for (VoteQueContent question : (Set<VoteQueContent>) voteContent.getVoteQueContents()) {
		    Long votesCount = sessionDTO.getMapStandardUserCount().get(mapIndex);
		    Long oldTotalVotesCount = (totalMapStandardUserCount.get(mapIndex) != null) ? totalMapStandardUserCount
			    .get(mapIndex) : 0;
		    totalMapStandardUserCount.put(mapIndex, oldTotalVotesCount + votesCount);

		    allSessionsVotesCount += votesCount;

		    // mapIndex++
		    mapIndex = new Long(mapIndex + 1);
		}

		// open votes
		if (voteContent.isAllowText()) {
		    Long votesCount = sessionDTO.getMapStandardUserCount().get(mapIndex);
		    Long oldTotalVotesCount = (totalMapStandardUserCount.get(mapIndex) != null) ? totalMapStandardUserCount
			    .get(mapIndex) : 0;
		    totalMapStandardUserCount.put(mapIndex, oldTotalVotesCount + votesCount);

		    allSessionsVotesCount += votesCount;
		}

		totalOpenVotes.addAll(sessionDTO.getOpenVotes());
	    }
	    totalSessionDTO.setSessionUserCount(totalPotentialUserCount);
	    totalSessionDTO.setCompletedSessionUserCount(totalCompletedSessionUserCount);
	    totalSessionDTO.setOpenVotes(totalOpenVotes);
	    boolean isExistsOpenVote = totalOpenVotes.size() > 0;
	    totalSessionDTO.setExistsOpenVote(isExistsOpenVote);
	    totalSessionDTO.setMapStandardNominationsHTMLedContent(sessionDTOs.get(0)
		    .getMapStandardNominationsHTMLedContent());
	    totalSessionDTO.setMapStandardQuestionUid(sessionDTOs.get(0).getMapStandardQuestionUid());
	    totalSessionDTO.setMapStandardToolSessionUid(sessionDTOs.get(0).getMapStandardToolSessionUid());
	    totalSessionDTO.setMapStandardUserCount(totalMapStandardUserCount);

	    // All groups total -- totalMapVoteRates part
	    Long mapIndex = 1L;
	    Map<Long, Double> totalMapVoteRates = new TreeMap<Long, Double>(new VoteComparator());
	    int totalStandardVotesCount = 0;
	    for (VoteQueContent question : (Set<VoteQueContent>) voteContent.getVoteQueContents()) {

		Long votesCount = totalMapStandardUserCount.get(mapIndex);

		double voteRate = (allSessionsVotesCount != 0) ? ((votesCount * 100) / allSessionsVotesCount) : 0d;
		totalMapVoteRates.put(mapIndex, voteRate);

		totalStandardVotesCount += votesCount;

		// mapIndex++
		mapIndex = new Long(mapIndex + 1);
	    }
	    // open votes
	    if (voteContent.isAllowText()) {
		int userEnteredVotesCount = allSessionsVotesCount - totalStandardVotesCount;
		double voteRate = (userEnteredVotesCount != 0) ? ((userEnteredVotesCount * 100) / allSessionsVotesCount)
			: 0;
		totalMapVoteRates.put(mapIndex, voteRate);
	    }
	    totalSessionDTO.setMapStandardRatesContent(totalMapVoteRates);

	    sessionDTOs.addFirst(totalSessionDTO);
	}

	return sessionDTOs;
    }

    @Override
    public List<VoteMonitoredAnswersDTO> getOpenVotes(Long voteContentUid, Long currentSessionId, Long userId) {
	Set<String> userEntries = voteUsrAttemptDAO.getUserEntries(voteContentUid);

	List<VoteMonitoredAnswersDTO> monitoredAnswersDTOs = new LinkedList<VoteMonitoredAnswersDTO>();
	for (String userEntry : userEntries) {

	    if (userEntry == null || userEntry.length() == 0) {
		continue;
	    }

	    VoteMonitoredAnswersDTO voteMonitoredAnswersDTO = new VoteMonitoredAnswersDTO();
	    voteMonitoredAnswersDTO.setQuestion(userEntry);

	    List<VoteUsrAttempt> userAttempts = voteUsrAttemptDAO.getUserAttempts(voteContentUid, userEntry);
	    List<VoteMonitoredUserDTO> monitoredUserContainerDTOs = new LinkedList<VoteMonitoredUserDTO>();

	    for (VoteUsrAttempt voteUsrAttempt : userAttempts) {
		VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();

		if (currentSessionId == null) {
		    voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
		    voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
		    voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
		    voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
		    voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
		    voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
		    voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());
		    monitoredUserContainerDTOs.add(voteMonitoredUserDTO);

		} else {
		    // showUserEntriesBySession is true: the case with learner export portfolio
		    // show user entries by same same session and same user
		    Long userSessionId = voteUsrAttempt.getVoteQueUsr().getVoteSession().getVoteSessionId();

		    if (userId != null) {
			if (userSessionId.equals(currentSessionId)) {
			    Long localUserId = voteUsrAttempt.getVoteQueUsr().getQueUsrId();
			    if (userId.equals(localUserId)) {
				voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
				voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
				voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
				voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
				voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
				monitoredUserContainerDTOs.add(voteMonitoredUserDTO);
				voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
				voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());
				if (voteUsrAttempt.isVisible() == false) {
				    voteMonitoredAnswersDTO.setQuestion("Nomination Hidden");
				}

			    }
			}
		    } else {
			// showUserEntriesByUserId is false
			// show user entries by same session
			if (userSessionId.equals(currentSessionId)) {
			    voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
			    voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
			    voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
			    voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
			    voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
			    monitoredUserContainerDTOs.add(voteMonitoredUserDTO);
			    voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
			    voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());
			}
		    }
		}

	    }

	    if (monitoredUserContainerDTOs.size() > 0) {
		Map<String, VoteMonitoredUserDTO> mapMonitoredUserContainerDTO = MonitoringUtil
			.convertToVoteMonitoredUserDTOMap(monitoredUserContainerDTOs);

		voteMonitoredAnswersDTO.setQuestionAttempts(mapMonitoredUserContainerDTO);
		monitoredAnswersDTOs.add(voteMonitoredAnswersDTO);
	    }
	}

	return monitoredAnswersDTOs;
    }

    @Override
    public List<ReflectionDTO> getReflectionData(VoteContent voteContent, Long userID) {
	List<ReflectionDTO> reflectionsContainerDTO = new LinkedList<ReflectionDTO>();

	if (userID == null) {
	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) {
		VoteSession voteSession = (VoteSession) sessionIter.next();

		for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) {
		    VoteQueUsr user = (VoteQueUsr) userIter.next();

		    NotebookEntry notebookEntry = this.getEntry(voteSession.getVoteSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(user
				    .getQueUsrId().toString()));

		    if (notebookEntry != null) {
			ReflectionDTO reflectionDTO = new ReflectionDTO();
			reflectionDTO.setUserId(user.getQueUsrId().toString());
			reflectionDTO.setSessionId(voteSession.getVoteSessionId().toString());
			reflectionDTO.setUserName(user.getFullname());
			reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			// String notebookEntryPresentable = VoteUtils.replaceNewLines(notebookEntry.getEntry());
			reflectionDTO.setEntry(notebookEntry.getEntry());
			reflectionsContainerDTO.add(reflectionDTO);
		    }
		}
	    }
	} else {
	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) {
		VoteSession voteSession = (VoteSession) sessionIter.next();
		for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) {
		    VoteQueUsr user = (VoteQueUsr) userIter.next();
		    if (user.getQueUsrId().equals(userID)) {
			NotebookEntry notebookEntry = this.getEntry(voteSession.getVoteSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(user
					.getQueUsrId().toString()));

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(voteSession.getVoteSessionId().toString());
			    reflectionDTO.setUserName(user.getFullname());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    // String notebookEntryPresentable = VoteUtils.replaceNewLines(notebookEntry.getEntry());
			    reflectionDTO.setEntry(notebookEntry.getEntry());
			    reflectionsContainerDTO.add(reflectionDTO);
			}
		    }
		}
	    }
	}

	return reflectionsContainerDTO;
    }

    public VoteContent getVoteContent(Long toolContentID) {
	return voteContentDAO.getVoteContentByContentId(toolContentID);
    }

    public void updateVoteContent(VoteContent voteContent) {
	voteContentDAO.updateVoteContent(voteContent);
    }

    public VoteQueContent getQuestionByDisplayOrder(final Long displayOrder, final Long voteContentUid) {
	return voteQueContentDAO.getQuestionByDisplayOrder(displayOrder, voteContentUid);
    }

    public VoteQueUsr getUserById(long voteQueUsrId) {
	VoteQueUsr voteQueUsr = voteUserDAO.getVoteQueUsrById(voteQueUsrId);
	return voteQueUsr;
    }

    public void createVoteQue(VoteQueContent voteQueContent) {
	voteQueContentDAO.saveQuestion(voteQueContent);
    }

    public List<VoteUsrAttempt> getAttemptsForQuestionContentAndSessionUid(final Long questionUid,
	    final Long voteSessionUid) {
	return voteUsrAttemptDAO.getAttemptsForQuestionContentAndSessionUid(questionUid, voteSessionUid);
    }

    public Set<String> getAttemptsForUserAndSessionUseOpenAnswer(final Long userUid, final Long sessionUid) {
	return voteUsrAttemptDAO.getAttemptsForUserAndSessionUseOpenAnswer(userUid, sessionUid);
    }

    public Set<String> getAttemptsForUserAndSession(final Long queUsrUid, final Long sessionUid) {
	return voteUsrAttemptDAO.getAttemptsForUserAndSession(queUsrUid, sessionUid);
    }

    public Set<VoteUsrAttempt> getSessionUserEntriesSet(final Long voteSessionUid) {
	return voteUsrAttemptDAO.getSessionUserEntriesSet(voteSessionUid);
    }

    public VoteQueContent getVoteQueContentByUID(Long uid) {
	if (uid == null) {
	    return null;
	}

	return voteQueContentDAO.getQuestionByUid(uid);
    }

    public void saveOrUpdateVoteQueContent(VoteQueContent voteQueContent) {
	voteQueContentDAO.saveOrUpdateQuestion(voteQueContent);
    }

    public List<VoteQueUsr> getUserBySessionOnly(final VoteSession voteSession) {
	return voteUserDAO.getUserBySessionOnly(voteSession);
    }

    public void createVoteSession(VoteSession voteSession) {
	voteSessionDAO.saveVoteSession(voteSession);
    }

    public VoteSession getVoteSessionByUID(Long uid) {
	return voteSessionDAO.getVoteSessionByUID(uid);
    }

    public int getVoteSessionPotentialLearnersCount(Long sessionUid) {
	VoteSession session = voteSessionDAO.getVoteSessionByUID(sessionUid);
	if (session != null) {
	    Set<User> potentialLearners = toolService.getAllPotentialLearners(session.getVoteSessionId().longValue());
	    return potentialLearners != null ? potentialLearners.size() : 0;
	} else {
	    VoteServicePOJO.logger
		    .error("Unable to find vote session record id=" + sessionUid + ". Returning 0 users.");
	    return 0;
	}
    }

    public void createVoteQueUsr(VoteQueUsr voteQueUsr) {
	voteUserDAO.saveVoteUser(voteQueUsr);
    }

    public VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long sessionUid) {
	return voteUserDAO.getVoteUserBySession(queUsrId, sessionUid);
    }

    public VoteQueUsr getVoteUserByUID(Long uid) {
	return voteUserDAO.getVoteUserByUID(uid);
    }

    public void updateVoteUser(VoteQueUsr voteUser) {
	voteUserDAO.updateVoteUser(voteUser);
    }

    public VoteQueUsr getUserByUserId(Long userID) {
	VoteQueUsr voteQueUsr = voteUserDAO.getUserByUserId(userID);
	return voteQueUsr;
    }

    public void createVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	voteUsrAttemptDAO.saveVoteUsrAttempt(voteUsrAttempt);
    }

    public List<VoteUsrAttempt> getStandardAttemptsByQuestionUid(final Long voteQueContentId) {
	return voteUsrAttemptDAO.getStandardAttemptsByQuestionUid(voteQueContentId);
    }

    public int getSessionEntriesCount(final Long voteSessionUid) {
	return voteUsrAttemptDAO.getSessionEntriesCount(voteSessionUid);
    }

    public VoteUsrAttempt getAttemptByUID(Long uid) {
	return voteUsrAttemptDAO.getAttemptByUID(uid);
    }

    public int getAttemptsForQuestionContent(final Long voteQueContentId) {
	return voteUsrAttemptDAO.getAttemptsForQuestionContent(voteQueContentId);
    }

    public int getStandardAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId, final Long voteSessionUid) {
	return voteUsrAttemptDAO.getStandardAttemptsForQuestionContentAndSessionUid(voteQueContentId, voteSessionUid);
    }

    public VoteUsrAttempt getAttemptForUserAndQuestionContentAndSession(final Long queUsrId,
	    final Long voteQueContentId, final Long toolSessionUid) {
	return voteUsrAttemptDAO.getAttemptForUserAndQuestionContentAndSession(queUsrId, voteQueContentId,
		toolSessionUid);
    }

    public void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	voteUsrAttemptDAO.updateVoteUsrAttempt(voteUsrAttempt);
    }

    public void removeAttemptsForUserandSession(final Long queUsrId, final Long sessionUid) {
	voteUsrAttemptDAO.removeAttemptsForUserandSession(queUsrId, sessionUid);
    }

    public List<VoteUsrAttempt> getAttemptsForUser(final Long userUid) {
	return voteUsrAttemptDAO.getAttemptsForUser(userUid);
    }

    public int getUserEnteredVotesCountForContent(final Long voteContentUid) {
	return voteUsrAttemptDAO.getUserEnteredVotesCountForContent(voteContentUid);
    }

    public VoteQueContent getQuestionByUid(Long uid) {
	return voteQueContentDAO.getQuestionByUid(uid);
    }

    public void removeVoteQueContent(VoteQueContent voteQueContent) {
	voteQueContentDAO.removeQuestion(voteQueContent);
    }

    public VoteSession getSessionBySessionId(Long voteSessionId) {
	return voteSessionDAO.getSessionBySessionId(voteSessionId);
    }

    public int getCompletedVoteUserBySessionUid(final Long voteSessionUid) {
	return voteUserDAO.getCompletedVoteUserBySessionUid(voteSessionUid);
    }

    public List getVoteUserBySessionUid(final Long voteSessionUid) {
	return voteUserDAO.getVoteUserBySessionUid(voteSessionUid);
    }

    public List getSessionNamesFromContent(VoteContent voteContent) {
	return voteSessionDAO.getSessionNamesFromContent(voteContent);
    }

    public void updateVote(VoteContent vote) {
	voteContentDAO.updateVoteContent(vote);
    }

    public void updateVoteSession(VoteSession voteSession) {
	voteSessionDAO.updateVoteSession(voteSession);
    }

    public void deleteVote(VoteContent vote) {
	voteContentDAO.removeVote(vote);
    }

    public void deleteVoteById(Long voteId) {
	voteContentDAO.removeVoteById(voteId);
    }

    public int countSessionComplete() {
	return voteSessionDAO.countSessionComplete();
    }

    public void deleteVoteSession(VoteSession voteSession) {
	voteSessionDAO.removeVoteSession(voteSession);
    }

    public void removeAttempt(VoteUsrAttempt attempt) {
	voteUsrAttemptDAO.removeVoteUsrAttempt(attempt);
    }

    /**
     * logs hiding of a user entered vote
     */
    public void hideOpenVote(VoteUsrAttempt voteUsrAttempt) {
	auditService.logHideEntry(VoteAppConstants.MY_SIGNATURE, voteUsrAttempt.getQueUsrId(), voteUsrAttempt
		.getVoteQueUsr().getUsername(), voteUsrAttempt.getUserEntry());
    }

    /**
     * logs showing of a user entered vote
     */
    public void showOpenVote(VoteUsrAttempt voteUsrAttempt) {
	auditService.logShowEntry(VoteAppConstants.MY_SIGNATURE, voteUsrAttempt.getQueUsrId(), voteUsrAttempt
		.getVoteQueUsr().getUsername(), voteUsrAttempt.getUserEntry());
    }

    public void deleteVoteQueUsr(VoteQueUsr voteQueUsr) {
	try {
	    voteUserDAO.removeVoteUser(voteQueUsr);
	} catch (DataAccessException e) {
	    throw new VoteApplicationException("Exception occured when lams is removing" + " the user: "
		    + e.getMessage(), e);
	}
    }

    public void saveVoteContent(VoteContent vote) {
	voteContentDAO.saveVoteContent(vote);
    }

    public List<Long> getSessionsFromContent(VoteContent voteContent) {
	return voteSessionDAO.getSessionsFromContent(voteContent);
    }

    public int getTotalNumberOfUsers() {
	return voteUserDAO.getTotalNumberOfUsers();
    }

    public User getCurrentUserData(String username) {
	/**
	 * this will return null if the username not found
	 */
	User user = userManagementService.getUserByLogin(username);
	if (user == null) {
	    VoteServicePOJO.logger.error("No user with the username: " + username + " exists.");
	    throw new VoteApplicationException("No user with that username exists.");
	}
	return user;
    }

    @Override
    public List<VoteUsrAttempt> getAttemptsForUserAndQuestionContent(final Long userUid, final Long questionUid) {
	try {
	    return voteUsrAttemptDAO.getAttemptsForUserAndQuestionContent(userUid, questionUid);
	} catch (DataAccessException e) {
	    throw new VoteApplicationException(
		    "Exception occured when lams is getting vote voteUsrRespDAO by user id and que content id: "
			    + e.getMessage(), e);
	}
    }

    @Override
    public boolean studentActivityOccurredGlobal(VoteContent voteContent) {
	return !voteContent.getVoteSessions().isEmpty();
    }

    @Override
    public boolean studentActivityOccurredStandardAndOpen(VoteContent voteContent) {
	boolean studentActivityOccurredGlobal = studentActivityOccurredGlobal(voteContent);

	int userEnteredVotesCount = getUserEnteredVotesCountForContent(voteContent.getUid());
	if (studentActivityOccurredGlobal == true || userEnteredVotesCount > 0) {
	    return true;
	}

	// there is no votes/nominations for this content
	return false;
    }

    @Override
    public void recalculateUserAnswers(VoteContent content, Set<VoteQueContent> oldQuestions,
	    List<VoteQuestionDTO> questionDTOs, List<VoteQuestionDTO> deletedQuestions) {

	// create list of modified questions
	List<VoteQuestionDTO> modifiedQuestions = new ArrayList<VoteQuestionDTO>();
	for (VoteQueContent oldQuestion : oldQuestions) {
	    for (VoteQuestionDTO questionDTO : questionDTOs) {
		if (oldQuestion.getUid().equals(questionDTO.getUid())) {

		    // question is different
		    if (!oldQuestion.getQuestion().equals(questionDTO.getQuestion())) {
			modifiedQuestions.add(questionDTO);
		    }
		}
	    }
	}

	Set<VoteSession> sessionList = content.getVoteSessions();
	for (VoteSession session : sessionList) {
	    Long toolSessionId = session.getVoteSessionId();
	    Set<VoteQueUsr> sessionUsers = session.getVoteQueUsers();

	    for (VoteQueUsr user : sessionUsers) {

		// get all finished user results
		List<VoteUsrAttempt> userAttempts = this.getAttemptsForUser(user.getUid());
		Iterator<VoteUsrAttempt> iter = userAttempts.iterator();
		while (iter.hasNext()) {
		    VoteUsrAttempt userAttempt = iter.next();

		    VoteQueContent question = userAttempt.getVoteQueContent();

		    boolean isRemoveQuestionResult = false;

		    // [+] if the question is modified
		    for (VoteQuestionDTO modifiedQuestion : modifiedQuestions) {
			if (question.getUid().equals(modifiedQuestion.getUid())) {
			    isRemoveQuestionResult = true;
			    break;
			}
		    }

		    // [+] if the question was removed
		    for (VoteQuestionDTO deletedQuestion : deletedQuestions) {
			if (question.getUid().equals(deletedQuestion.getUid())) {
			    isRemoveQuestionResult = true;
			    break;
			}
		    }

		    if (isRemoveQuestionResult) {
			iter.remove();
			voteUsrAttemptDAO.removeVoteUsrAttempt(userAttempt);
		    }

		    // [+] doing nothing if the new question was added

		}

	    }
	}

    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (fromContentId == null) {
	    // attempt retrieving tool's default content id with signatute VoteAppConstants.MY_SIGNATURE
	    long defaultContentId = getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    fromContentId = new Long(defaultContentId);
	}

	if (toContentId == null) {
	    VoteServicePOJO.logger.error("throwing ToolException: toContentId is null");
	    throw new ToolException("toContentId is missing");
	}

	VoteContent fromContent = voteContentDAO.getVoteContentByContentId(fromContentId);

	if (fromContent == null) {
	    // attempt retrieving tool's default content id with signatute VoteAppConstants.MY_SIGNATURE
	    long defaultContentId = getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    fromContentId = new Long(defaultContentId);

	    fromContent = voteContentDAO.getVoteContentByContentId(fromContentId);
	}

	VoteContent toContent = VoteContent.newInstance(fromContent, toContentId);
	if (toContent == null) {
	    VoteServicePOJO.logger.error("throwing ToolException: WARNING!, retrieved toContent is null.");
	    throw new ToolException("WARNING! Fail to create toContent. Can't continue!");
	} else {
	    voteContentDAO.saveVoteContent(toContent);
	}
    }

    @Override
    public void removeToolContent(Long toolContentID, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {

	if (toolContentID == null) {
	    VoteServicePOJO.logger.error("toolContentID is null");
	    throw new ToolException("toolContentID is missing");
	}

	VoteContent voteContent = voteContentDAO.getVoteContentByContentId(toolContentID);

	if (voteContent != null) {
	    Iterator sessionIterator = voteContent.getVoteSessions().iterator();
	    while (sessionIterator.hasNext()) {
		if (removeSessionData == false) {
		    VoteServicePOJO.logger.error("removeSessionData is false, throwing SessionDataExistsException.");
		    throw new SessionDataExistsException();
		}

		VoteSession voteSession = (VoteSession) sessionIterator.next();

		Iterator sessionUsersIterator = voteSession.getVoteQueUsers().iterator();
		while (sessionUsersIterator.hasNext()) {
		    VoteQueUsr voteQueUsr = (VoteQueUsr) sessionUsersIterator.next();

		    Iterator sessionUsersAttemptsIterator = voteQueUsr.getVoteUsrAttempts().iterator();
		    while (sessionUsersAttemptsIterator.hasNext()) {
			VoteUsrAttempt voteUsrAttempt = (VoteUsrAttempt) sessionUsersAttemptsIterator.next();
			removeAttempt(voteUsrAttempt);
		    }
		}
	    }
	    // removed all existing responses of toolContent with toolContentID
	    voteContentDAO.removeVoteById(toolContentID);
	} else {
	    VoteServicePOJO.logger.error("Warning!!!, We should have not come here. voteContent is null.");
	    throw new ToolException("toolContentID is missing");
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("Removing Vote attempts for user ID " + userId + " and toolContentId " + toolContentId);
	}

	VoteContent voteContent = voteContentDAO.getVoteContentByContentId(toolContentId);
	if (voteContent == null) {
	    logger.warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	for (VoteSession session : (Set<VoteSession>) voteContent.getVoteSessions()) {
	    VoteQueUsr user = voteUserDAO.getVoteUserBySession(userId.longValue(), session.getUid());
	    if (user != null) {
		voteUsrAttemptDAO.removeAttemptsForUserandSession(user.getUid(), session.getUid());

		NotebookEntry entry = getEntry(session.getVoteSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			VoteAppConstants.MY_SIGNATURE, userId);
		if (entry != null) {
		    voteContentDAO.delete(entry);
		}

		voteUserDAO.removeVoteUser(user);
	    }
	}
    }

    @Override
    public void exportToolContent(Long toolContentID, String rootPath) throws DataMissingException, ToolException {
	VoteContent toolContentObj = voteContentDAO.getVoteContentByContentId(toolContentID);
	if (toolContentObj == null) {
	    long defaultContentId = getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    toolContentObj = voteContentDAO.getVoteContentByContentId(defaultContentId);
	}

	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the voting tool");
	}

	try {
	    // set ToolContentHandler as null to avoid copy file node in repository again.
	    toolContentObj = VoteContent.newInstance(toolContentObj, toolContentID);

	    // clear unnecessary information attach
	    toolContentObj.setVoteSessions(null);
	    exportContentService.exportToolContent(toolContentID, toolContentObj, voteToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentID, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(VoteImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, voteToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof VoteContent)) {
		throw new ImportToolContentException("Import Vote tool content failed. Deserialized object is "
			+ toolPOJO);
	    }
	    VoteContent toolContentObj = (VoteContent) toolPOJO;

	    // reset it to new toolContentID
	    toolContentObj.setVoteContentId(toolContentID);
	    toolContentObj.setCreatedBy(newUserUid);

	    voteContentDAO.saveVoteContent(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Implemented as part of the tool contract. Gets called only in the Learner mode. All the learners in the same
     * group have the same toolSessionID.
     * 
     * @param toolSessionID
     *            the generated tool session id.
     * @param toolSessionName
     *            the tool session name.
     * @param toolContentID
     *            the tool content id specified.
     * @throws ToolException
     *             if an error occurs e.g. defaultContent is missing.
     * 
     */
    public void createToolSession(Long toolSessionID, String toolSessionName, Long toolContentID) throws ToolException {

	if (toolSessionID == null) {
	    VoteServicePOJO.logger.error("toolSessionID is null");
	    throw new ToolException("toolSessionID is missing");
	}

	VoteContent voteContent = voteContentDAO.getVoteContentByContentId(toolContentID);

	/*
	 * create a new a new tool session if it does not already exist in the tool session table
	 */
	VoteSession voteSession = getSessionBySessionId(toolSessionID);
	if (voteSession == null) {
	    try {
		voteSession = new VoteSession(toolSessionID, new Date(System.currentTimeMillis()),
			VoteSession.INCOMPLETE, toolSessionName, voteContent, new TreeSet());

		voteSessionDAO.saveVoteSession(voteSession);

	    } catch (Exception e) {
		VoteServicePOJO.logger.error("Error creating new toolsession in the db");
		throw new ToolException("Error creating new toolsession in the db: " + e);
	    }
	}
    }

    /**
     * Implemented as part of the tool contract.
     * 
     * @param toolSessionID
     * @param toolContentID
     *            return
     * @throws ToolException
     */
    public void removeToolSession(Long toolSessionID) throws DataMissingException, ToolException {
	if (toolSessionID == null) {
	    VoteServicePOJO.logger.error("toolSessionID is null");
	    throw new DataMissingException("toolSessionID is missing");
	}

	VoteSession voteSession = null;
	try {
	    voteSession = getSessionBySessionId(toolSessionID);
	} catch (VoteApplicationException e) {
	    throw new DataMissingException("error retrieving voteSession: " + e);
	} catch (Exception e) {
	    throw new ToolException("error retrieving voteSession: " + e);
	}

	if (voteSession == null) {
	    VoteServicePOJO.logger.error("voteSession is null");
	    throw new DataMissingException("voteSession is missing");
	}

	try {
	    voteSessionDAO.removeVoteSession(voteSession);
	} catch (VoteApplicationException e) {
	    throw new ToolException("error deleting voteSession:" + e);
	}
    }

    /**
     * Implemtented as part of the tool contract.
     * 
     * @param toolSessionID
     * @param learnerId
     *            return String
     * @throws ToolException
     * 
     */
    public String leaveToolSession(Long toolSessionID, Long learnerId) throws DataMissingException, ToolException {

	if (learnerService == null) {
	    return "dummyNextUrl";
	}

	if (learnerId == null) {
	    VoteServicePOJO.logger.error("learnerId is null");
	    throw new DataMissingException("learnerId is missing");
	}

	if (toolSessionID == null) {
	    VoteServicePOJO.logger.error("toolSessionID is null");
	    throw new DataMissingException("toolSessionID is missing");
	}

	VoteSession voteSession = null;
	try {
	    voteSession = getSessionBySessionId(toolSessionID);
	} catch (VoteApplicationException e) {
	    throw new DataMissingException("error retrieving voteSession: " + e);
	} catch (Exception e) {
	    throw new ToolException("error retrieving voteSession: " + e);
	}
	voteSession.setSessionStatus(VoteAppConstants.COMPLETED);
	voteSessionDAO.updateVoteSession(voteSession);

	String nextUrl = learnerService.completeToolSession(toolSessionID, learnerId);
	if (nextUrl == null) {
	    VoteServicePOJO.logger.error("nextUrl is null");
	    throw new ToolException("nextUrl is null");
	}
	return nextUrl;
    }

    /**
     * exportToolSession(Long toolSessionID) throws DataMissingException, ToolException
     * 
     * @param toolSessionID
     *            return ToolSessionExportOutputData
     * @throws ToolException
     */
    public ToolSessionExportOutputData exportToolSession(Long toolSessionID) throws DataMissingException, ToolException {
	throw new ToolException("not yet implemented");
    }

    /**
     * exportToolSession(Long toolSessionID) throws DataMissingException, ToolException
     * 
     * @param toolSessionIDs
     *            return ToolSessionExportOutputData
     * @throws ToolException
     */
    public ToolSessionExportOutputData exportToolSession(List toolSessionIDs) throws DataMissingException,
	    ToolException {
	throw new ToolException("not yet implemented");

    }

    public IToolVO getToolBySignature(String toolSignature) {
	IToolVO tool = toolService.getToolBySignature(toolSignature);
	return tool;
    }

    public long getToolDefaultContentIdBySignature(String toolSignature) {
	long contentId = 0;
	contentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	return contentId;
    }

    @Override
    public VoteQueContent getDefaultVoteContentFirstQuestion() {
	VoteQueContent voteQueContent = voteQueContentDAO.getDefaultVoteContentFirstQuestion();
	return voteQueContent;
    }
    
    public List getToolSessionsForContent(VoteContent vote) {
	List listToolSessionIds = voteSessionDAO.getSessionsFromContent(vote);
	return listToolSessionIds;
    }

    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. Currently we have one
     * definition, which is whether or not the user has selected a particular answer
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	VoteContent content = getVoteContent(toolContentId);
	if (content == null) {
	    long defaultToolContentId = getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    content = getVoteContent(defaultToolContentId);
	}
	return getVoteOutputFactory().getToolOutputDefinitions(content, definitionType);
    }

    public String getToolContentTitle(Long toolContentId) {
	return getVoteContent(toolContentId).getTitle();
    }

    public boolean isContentEdited(Long toolContentId) {
	return getVoteContent(toolContentId).isDefineLater();
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return voteOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return voteOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Vote
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	VoteContent toolContentObj = new VoteContent();
	toolContentObj.setCreatedBy(user.getUserID().longValue());
	toolContentObj.setCreationDate(now);
	toolContentObj.setDefineLater(false);
	toolContentObj.setInstructions(WebUtil.convertNewlines((String) importValues
		.get(ToolContentImport102Manager.CONTENT_BODY)));
	toolContentObj.setReflectionSubject(null);
	toolContentObj.setReflect(false);
	toolContentObj.setUseSelectLeaderToolOuput(false);
	toolContentObj.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));

	toolContentObj.setContent(null);
	toolContentObj.setUpdateDate(now);
	toolContentObj.setVoteContentId(toolContentId);
	toolContentObj.setLockOnFinish(false);
	toolContentObj.setShowResults(true);

	try {
	    Boolean bool = WDDXProcessor.convertToBoolean(importValues,
		    ToolContentImport102Manager.CONTENT_VOTE_ALLOW_POLL_NOMINATIONS);
	    toolContentObj.setAllowText(bool != null ? bool : false);

	    bool = WDDXProcessor.convertToBoolean(importValues, ToolContentImport102Manager.CONTENT_REUSABLE);
	    toolContentObj.setLockOnFinish(bool != null ? bool : true);

	    Integer maxCount = WDDXProcessor.convertToInteger(importValues,
		    ToolContentImport102Manager.CONTENT_VOTE_MAXCHOOSE);
	    toolContentObj.setMaxNominationCount(maxCount != null ? maxCount.toString() : "1");

	    Integer minCount = WDDXProcessor.convertToInteger(importValues,
		    ToolContentImport102Manager.CONTENT_VOTE_MINCHOOSE);
	    toolContentObj.setMinNominationCount(minCount != null ? minCount.toString() : "1");

	} catch (WDDXProcessorConversionException e) {
	    VoteServicePOJO.logger.error("Unable to content for activity " + toolContentObj.getTitle()
		    + "properly due to a WDDXProcessorConversionException.", e);
	    throw new ToolException(
		    "Invalid import data format for activity "
			    + toolContentObj.getTitle()
			    + "- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
	}

	// leave as empty, no need to set them to anything.
	// setVoteSessions(Set voteSessions);

	// set up question from body
	Vector nominations = (Vector) importValues.get(ToolContentImport102Manager.CONTENT_VOTE_NOMINATIONS);
	if (nominations != null) {
	    Iterator iter = nominations.iterator();
	    int order = 1;
	    while (iter.hasNext()) {
		String element = (String) iter.next();
		VoteQueContent nomination = new VoteQueContent(element, toolContentObj);
		nomination.setDisplayOrder(order++);
		toolContentObj.getVoteQueContents().add(nomination);
	    }
	}

	voteContentDAO.saveVoteContent(toolContentObj);
    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	VoteContent toolContentObj = null;
	if (toolContentId != null) {
	    toolContentObj = getVoteContent(toolContentId);
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	toolContentObj.setReflect(Boolean.TRUE.booleanValue());
	toolContentObj.setReflectionSubject(description);
    }

    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID) {

	List<NotebookEntry> list = coreNotebookService.getEntry(id, idType, signature, userID);
	if (list == null || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    public List<VoteQueContent> getAllQuestionsSorted(final long voteContentId) {
	return voteQueContentDAO.getAllQuestionsSorted(voteContentId);
    }

    /**
     * @return Returns the toolService.
     */
    public ILamsToolService getToolService() {
	return toolService;
    }

    /**
     * @return Returns the userManagementService.
     */
    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    /**
     * @return Returns the voteSessionDAO.
     */
    public IVoteSessionDAO getvoteSessionDAO() {
	return voteSessionDAO;
    }

    /**
     * @param voteSessionDAO
     *            The voteSessionDAO to set.
     */
    public void setvoteSessionDAO(IVoteSessionDAO voteSessionDAO) {
	this.voteSessionDAO = voteSessionDAO;
    }

    /**
     * @return Returns the voteUserDAO.
     */
    public IVoteUserDAO getvoteUserDAO() {
	return voteUserDAO;
    }

    /**
     * @param voteUserDAO
     *            The voteUserDAO to set.
     */
    public void setvoteUserDAO(IVoteUserDAO voteUserDAO) {
	this.voteUserDAO = voteUserDAO;
    }

    /**
     * @return Returns the voteUsrAttemptDAO.
     */
    public IVoteUsrAttemptDAO getvoteUsrAttemptDAO() {
	return voteUsrAttemptDAO;
    }

    /**
     * @param voteUsrAttemptDAO
     *            The voteUsrAttemptDAO to set.
     */
    public void setvoteUsrAttemptDAO(IVoteUsrAttemptDAO voteUsrAttemptDAO) {
	this.voteUsrAttemptDAO = voteUsrAttemptDAO;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    /**
     * @return Returns the voteToolContentHandler.
     */
    public IToolContentHandler getVoteToolContentHandler() {
	return voteToolContentHandler;
    }

    /**
     * @param voteToolContentHandler
     *            The voteToolContentHandler to set.
     */
    public void setVoteToolContentHandler(IToolContentHandler voteToolContentHandler) {
	this.voteToolContentHandler = voteToolContentHandler;
    }

    /**
     * @return Returns the learnerService.
     */
    public ILearnerService getLearnerService() {
	return learnerService;
    }

    /**
     * @param learnerService
     *            The learnerService to set.
     */
    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    /**
     * @return Returns the voteContentDAO.
     */
    public IVoteContentDAO getvoteContentDAO() {
	return voteContentDAO;
    }

    /**
     * @param voteContentDAO
     *            The voteContentDAO to set.
     */
    public void setvoteContentDAO(IVoteContentDAO voteContentDAO) {
	this.voteContentDAO = voteContentDAO;
    }

    /**
     * @return Returns the voteQueContentDAO.
     */
    public IVoteQueContentDAO getvoteQueContentDAO() {
	return voteQueContentDAO;
    }

    /**
     * @param voteQueContentDAO
     *            The voteQueContentDAO to set.
     */
    public void setvoteQueContentDAO(IVoteQueContentDAO voteQueContentDAO) {
	this.voteQueContentDAO = voteQueContentDAO;
    }

    /**
     * @return Returns the voteContentDAO.
     */
    public IVoteContentDAO getVoteContentDAO() {
	return voteContentDAO;
    }

    /**
     * @param voteContentDAO
     *            The voteContentDAO to set.
     */
    public void setVoteContentDAO(IVoteContentDAO voteContentDAO) {
	this.voteContentDAO = voteContentDAO;
    }

    /**
     * @return Returns the voteQueContentDAO.
     */
    public IVoteQueContentDAO getVoteQueContentDAO() {
	return voteQueContentDAO;
    }

    /**
     * @param voteQueContentDAO
     *            The voteQueContentDAO to set.
     */
    public void setVoteQueContentDAO(IVoteQueContentDAO voteQueContentDAO) {
	this.voteQueContentDAO = voteQueContentDAO;
    }

    /**
     * @return Returns the voteSessionDAO.
     */
    public IVoteSessionDAO getVoteSessionDAO() {
	return voteSessionDAO;
    }

    /**
     * @param voteSessionDAO
     *            The voteSessionDAO to set.
     */
    public void setVoteSessionDAO(IVoteSessionDAO voteSessionDAO) {
	this.voteSessionDAO = voteSessionDAO;
    }

    /**
     * @return Returns the voteUserDAO.
     */
    public IVoteUserDAO getVoteUserDAO() {
	return voteUserDAO;
    }

    /**
     * @param voteUserDAO
     *            The voteUserDAO to set.
     */
    public void setVoteUserDAO(IVoteUserDAO voteUserDAO) {
	this.voteUserDAO = voteUserDAO;
    }

    /**
     * @return Returns the voteUsrAttemptDAO.
     */
    public IVoteUsrAttemptDAO getVoteUsrAttemptDAO() {
	return voteUsrAttemptDAO;
    }

    /**
     * @param voteUsrAttemptDAO
     *            The voteUsrAttemptDAO to set.
     */
    public void setVoteUsrAttemptDAO(IVoteUsrAttemptDAO voteUsrAttemptDAO) {
	this.voteUsrAttemptDAO = voteUsrAttemptDAO;
    }

    /**
     * @return Returns the auditService.
     */
    public IAuditService getAuditService() {
	return auditService;
    }

    /**
     * @param auditService
     *            The auditService to set.
     */
    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    /**
     * @return Returns the coreNotebookService.
     */
    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    /**
     * @param coreNotebookService
     *            The coreNotebookService to set.
     */
    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public VoteOutputFactory getVoteOutputFactory() {
	return voteOutputFactory;
    }

    public void setVoteOutputFactory(VoteOutputFactory voteOutputFactory) {
	this.voteOutputFactory = voteOutputFactory;
    }

    /**
     * @return Returns the MessageService.
     */
    public MessageService getMessageService() {
	return messageService;
    }

    /**
     * @param messageService
     *            The MessageService to set.
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setLamsCoreToolService(ILamsCoreToolService lamsCoreToolService) {
	this.lamsCoreToolService = lamsCoreToolService;
    }

    public void removeQuestionsFromCache(VoteContent voteContent) {
	voteContentDAO.removeQuestionsFromCache(voteContent);
    }

    public void removeVoteContentFromCache(VoteContent voteContent) {
	voteContentDAO.removeVoteContentFromCache(voteContent);
    }

    public void setDataFlowDAO(IDataFlowDAO dataFlowDAO) {
	this.dataFlowDAO = dataFlowDAO;
    }

    public ToolOutput getToolInput(Long requestingToolContentId, Integer learnerId) {
	// just forwarding to learner service
	return learnerService.getToolInput(requestingToolContentId, VoteAppConstants.DATA_FLOW_OBJECT_ASSIGMENT_ID,
		learnerId);
    }

    public void saveDataFlowObjectAssigment(DataFlowObject assignedDataFlowObject) {
	// this also should be done in learner service, but for simplicity...
	if (assignedDataFlowObject != null) {
	    assignedDataFlowObject.setToolAssigmentId(VoteAppConstants.DATA_FLOW_OBJECT_ASSIGMENT_ID);
	    dataFlowDAO.update(assignedDataFlowObject);
	}
    }

    public DataFlowObject getAssignedDataFlowObject(Long toolContentId) {
	return dataFlowDAO.getAssignedDataFlowObject(toolContentId, VoteAppConstants.DATA_FLOW_OBJECT_ASSIGMENT_ID);
    }

    public List<DataFlowObject> getDataFlowObjects(Long toolContentId) {
	return dataFlowDAO.getDataFlowObjectsByToolContentId(toolContentId);
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getVoteOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
}