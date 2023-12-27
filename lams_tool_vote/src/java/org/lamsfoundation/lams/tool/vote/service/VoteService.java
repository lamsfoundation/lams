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

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.learningdesign.dao.IDataFlowDAO;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.SimpleURL;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dao.IVoteContentDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteQueContentDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteSessionDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUserDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUsrAttemptDAO;
import org.lamsfoundation.lams.tool.vote.dto.OpenTextAnswerDTO;
import org.lamsfoundation.lams.tool.vote.dto.SessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.SessionNominationDTO;
import org.lamsfoundation.lams.tool.vote.dto.SummarySessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteQuestionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteStatsDTO;
import org.lamsfoundation.lams.tool.vote.model.VoteContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.model.VoteSession;
import org.lamsfoundation.lams.tool.vote.model.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.util.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.tool.vote.web.controller.MonitoringController;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.dao.DataAccessException;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The POJO implementation of Voting service. All business logic of Voting tool is implemented in this class. It
 * translates the request from presentation layer and performs appropriate database operation.
 *
 * @author Ozgur Demirtas
 */
public class VoteService
	implements IVoteService, ToolContentManager, ToolSessionManager, VoteAppConstants, ToolRestManager {
    private static Logger logger = Logger.getLogger(VoteService.class.getName());

    private IVoteContentDAO voteContentDAO;
    private IVoteQueContentDAO voteQueContentDAO;
    private IVoteSessionDAO voteSessionDAO;
    private IVoteUserDAO voteUserDAO;
    private IVoteUsrAttemptDAO voteUsrAttemptDAO;
    private IUserManagementService userManagementService;
    private ILearnerService learnerService;
    private ILogEventService logEventService;
    private ILamsToolService toolService;
    private IExportToolContentService exportContentService;
    private IToolContentHandler voteToolContentHandler = null;
    private VoteOutputFactory voteOutputFactory;
    private IDataFlowDAO dataFlowDAO;
    private MessageService messageService;

    public VoteService() {
    }

    @Override
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	VoteSession session = getSessionBySessionId(toolSessionId);
	VoteQueUsr groupLeader = session.getGroupLeader();
	boolean isUserLeader = (groupLeader != null) && userId.equals(groupLeader.getQueUsrId());
	return isUserLeader;
    }

    @Override
    public VoteQueUsr checkLeaderSelectToolForSessionLeader(VoteQueUsr user, Long toolSessionId) {
	if ((user == null) || (toolSessionId == null)) {
	    logger.info("user" + user + "or" + "toolSessionId" + toolSessionId + "is null");
	    return null;
	}

	VoteSession session = getSessionBySessionId(toolSessionId);
	VoteQueUsr leader = session.getGroupLeader();
	// check leader select tool for a leader only in case QA tool doesn't know it. As otherwise it will screw
	// up previous scratches done
	if (leader == null) {
	    Long leaderUserId = toolService.getLeaderUserId(toolSessionId, user.getQueUsrId().intValue());
	    // set leader only if the leader entered the activity
	    if (user.getQueUsrId().equals(leaderUserId)) {
		// is it me?
		leader = user;
	    } else {
		leader = getVoteUserBySession(leaderUserId, session.getUid());
	    }
	    if (leader != null) {
		// set group leader
		session.setGroupLeader(leader);
		voteSessionDAO.updateVoteSession(session);
	    }
	}

	return leader;
    }

    @Override
    public void copyAnswersFromLeader(VoteQueUsr user, VoteQueUsr leader) {

	if ((user == null) || (leader == null) || user.getUid().equals(leader.getUid())) {
	    if (logger.isDebugEnabled()) {
		logger.debug("User" + user + "or" + "leader" + leader + "or Userid and Leaderid is equal");
		return;
	    }
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
		logger.info("Response does not exist hence creating VoteUsrAttempt in db");
		VoteUsrAttempt voteUsrAttempt = new VoteUsrAttempt(attempTime, timeZone, question, user, userEntry,
			true);
		voteUsrAttemptDAO.saveVoteUsrAttempt(voteUsrAttempt);

		// if it's been changed by the leader
	    } else if (leaderAttempt.getAttemptTime().compareTo(userAttempt.getAttemptTime()) != 0) {
		logger.info("Incase of the change done by the leader");
		userAttempt.setUserEntry(userEntry);
		userAttempt.setAttemptTime(attempTime);
		userAttempt.setTimeZone(timeZone);
		this.updateVoteUsrAttempt(userAttempt);

		// remove userAttempt from the list so we can know which one is redundant(presumably, leader has removed
		// this one)
		if (logger.isDebugEnabled()) {
		    logger.debug("Leader has removed the userAttempt" + userAttempt);
		}
		userAttempts.remove(userAttempt);
	    }
	}

	// remove redundant ones
	for (VoteUsrAttempt redundantUserAttempt : userAttempts) {
	    voteUsrAttemptDAO.removeVoteUsrAttempt(redundantUserAttempt);
	}
    }

    @Override
    public void changeLeaderForGroup(long toolSessionId, long leaderUserId) {
	VoteSession session = getSessionBySessionId(toolSessionId);
	if (VoteAppConstants.COMPLETED.equals(session.getSessionStatus())) {
	    throw new InvalidParameterException("Attempting to assing a new leader with user ID " + leaderUserId
		    + " to a finished session wtih ID " + toolSessionId);
	}

	VoteQueUsr existingLeader = session.getGroupLeader();
	if (existingLeader == null || existingLeader.getQueUsrId().equals(leaderUserId)) {
	    return;
	}

	VoteQueUsr newLeader = getVoteUserBySession(leaderUserId, session.getUid());
	if (newLeader == null) {
	    User user = userManagementService.getUserById(Long.valueOf(leaderUserId).intValue());

	    String userName = user.getLogin();
	    String fullName = user.getFirstName() + " " + user.getLastName();

	    newLeader = new VoteQueUsr(leaderUserId, userName, fullName, session, new TreeSet<VoteUsrAttempt>());
	    createVoteQueUsr(newLeader);

	    if (logger.isDebugEnabled()) {
		logger.debug("Created user with ID " + leaderUserId + " to become a new leader for session with ID "
			+ toolSessionId);
	    }
	}

	session.setGroupLeader(newLeader);
	voteSessionDAO.updateVoteSession(session);

	voteUsrAttemptDAO.removeAttemptsForUserandSession(existingLeader.getQueUsrId(), toolSessionId);

	for (VoteUsrAttempt vote : existingLeader.getVoteUsrAttempts()) {
	    vote.setVoteQueUsr(newLeader);
	    voteUsrAttemptDAO.updateVoteUsrAttempt(vote);
	}

	if (logger.isDebugEnabled()) {
	    logger.debug("User with ID " + leaderUserId + " became a new leader for session with ID " + toolSessionId);
	}

	Set<Integer> userIds = session.getVoteQueUsers().stream()
		.collect(Collectors.mapping(voteUser -> voteUser.getQueUsrId().intValue(), Collectors.toSet()));

	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("hookTrigger", "vote-leader-change-refresh-" + toolSessionId);
	learnerService.createCommandForLearners(session.getVoteContent().getVoteContentId(), userIds,
		jsonCommand.toString());
    }

    @Override
    public VoteGeneralLearnerFlowDTO prepareChartData(HttpServletRequest request, Long toolContentID,
	    Long toolSessionUid, VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO) {

	VoteContent voteContent = this.getVoteContent(toolContentID);

	int entriesCount = 0;
	List<VoteUsrAttempt> userEntries = null;
	if (toolSessionUid != null) {
	    entriesCount = voteUsrAttemptDAO.getSessionEntriesCount(toolSessionUid);
	    if (voteContent.isAllowText()) {
		userEntries = voteUsrAttemptDAO.getSessionOpenTextUserEntries(toolSessionUid);
	    } else {
		userEntries = new ArrayList<>(0);
	    }
	}

	Long mapIndex = 1L;
	int totalStandardVotesCount = 0;

	Map<Long, Long> mapStandardUserCount = new TreeMap<>(new VoteComparator());
	Map<Long, String> mapStandardNominationsHTMLedContent = new TreeMap<>(new VoteComparator());
	Map<Long, Long> mapStandardQuestionUid = new TreeMap<>(new VoteComparator());
	Map<Long, Long> mapStandardToolSessionUid = new TreeMap<>(new VoteComparator());
	Map<Long, String> mapStandardNominationsContent = new TreeMap<>(new VoteComparator());
	Map<Long, Double> mapVoteRates = new TreeMap<>(new VoteComparator());

	for (VoteQueContent question : voteContent.getVoteQueContents()) {

	    mapStandardNominationsHTMLedContent.put(mapIndex, question.getQuestion());
	    String noHTMLNomination = VoteUtils.stripHTML(question.getQuestion());
	    mapStandardNominationsContent.put(mapIndex, noHTMLNomination);

	    int votesCount = voteUsrAttemptDAO.getStandardAttemptsForQuestionContentAndSessionUid(question.getUid(),
		    toolSessionUid);
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
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	request.setAttribute(VoteAppConstants.LIST_USER_ENTRIES_CONTENT, userEntries);

	// return value is only used in VoteChartGeneratorAction
	return voteGeneralLearnerFlowDTO;
    }

    @SuppressWarnings("unused")
    @Override
    public LinkedList<SessionDTO> getSessionDTOs(Long toolContentID) {

	LinkedList<SessionDTO> sessionDTOs = new LinkedList<>();

	VoteContent voteContent = this.getVoteContent(toolContentID);
	for (VoteSession session : voteContent.getVoteSessions()) {

	    SessionDTO sessionDTO = new SessionDTO();
	    sessionDTO.setSessionId(session.getVoteSessionId().toString());
	    sessionDTO.setSessionName(session.getSession_name());

	    int entriesCount = voteUsrAttemptDAO.getSessionEntriesCount(session.getUid());

	    Long mapIndex = 1L;
	    int totalStandardVotesCount = 0;

	    Map<Long, Double> mapVoteRates = new TreeMap<>(new VoteComparator());
	    Map<Long, Long> mapStandardUserCount = new TreeMap<>(new VoteComparator());
	    Map<Long, String> mapStandardNominationsHTMLedContent = new TreeMap<>(new VoteComparator());
	    Map<Long, Long> mapStandardQuestionUid = new TreeMap<>(new VoteComparator());
	    Map<Long, Long> mapStandardToolSessionUid = new TreeMap<>(new VoteComparator());

	    for (VoteQueContent question : voteContent.getVoteQueContents()) {
		mapStandardNominationsHTMLedContent.put(mapIndex, question.getQuestion());

		int votesCount = voteUsrAttemptDAO.getStandardAttemptsForQuestionContentAndSessionUid(question.getUid(),
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

	    List<VoteMonitoredAnswersDTO> totalOpenVotes = new ArrayList<>();
	    int totalPotentialUserCount = 0;
	    int totalCompletedSessionUserCount = 0;
	    int allSessionsVotesCount = 0;
	    Map<Long, Long> totalMapStandardUserCount = new TreeMap<>(new VoteComparator());
	    for (SessionDTO sessionDTO : sessionDTOs) {

		totalPotentialUserCount += sessionDTO.getSessionUserCount();
		totalCompletedSessionUserCount += sessionDTO.getCompletedSessionUserCount();

		Long mapIndex = 1L;
		for (VoteQueContent question : voteContent.getVoteQueContents()) {
		    Long votesCount = sessionDTO.getMapStandardUserCount().get(mapIndex);
		    Long oldTotalVotesCount = (totalMapStandardUserCount.get(mapIndex) != null)
			    ? totalMapStandardUserCount.get(mapIndex)
			    : 0L;
		    totalMapStandardUserCount.put(mapIndex, oldTotalVotesCount + votesCount);

		    allSessionsVotesCount += votesCount;

		    // mapIndex++
		    mapIndex = new Long(mapIndex + 1);
		}

		// open votes
		if (voteContent.isAllowText()) {
		    Long votesCount = sessionDTO.getMapStandardUserCount().get(mapIndex);
		    Long oldTotalVotesCount = (totalMapStandardUserCount.get(mapIndex) != null)
			    ? totalMapStandardUserCount.get(mapIndex)
			    : 0L;
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
	    totalSessionDTO.setMapStandardNominationsHTMLedContent(
		    sessionDTOs.get(0).getMapStandardNominationsHTMLedContent());
	    totalSessionDTO.setMapStandardQuestionUid(sessionDTOs.get(0).getMapStandardQuestionUid());
	    totalSessionDTO.setMapStandardToolSessionUid(sessionDTOs.get(0).getMapStandardToolSessionUid());
	    totalSessionDTO.setMapStandardUserCount(totalMapStandardUserCount);

	    // All groups total -- totalMapVoteRates part
	    Long mapIndex = 1L;
	    Map<Long, Double> totalMapVoteRates = new TreeMap<>(new VoteComparator());
	    int totalStandardVotesCount = 0;
	    for (VoteQueContent question : voteContent.getVoteQueContents()) {

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
    public SortedSet<SummarySessionDTO> getMonitoringSessionDTOs(Long toolContentID) {

	SortedSet<SummarySessionDTO> sessionDTOs = new TreeSet<>();

	VoteContent voteContent = this.getVoteContent(toolContentID);
	for (VoteSession session : voteContent.getVoteSessions()) {

	    SummarySessionDTO sessionDTO = new SummarySessionDTO();
	    sessionDTO.setSessionName(session.getSession_name());
	    sessionDTO.setSessionUid(session.getUid());
	    sessionDTO.setToolSessionId(session.getVoteSessionId());
	    sessionDTO.setNominations(new TreeSet<SessionNominationDTO>());
	    sessionDTO.setSessionUserCount(session.getVoteQueUsers().size());
	    sessionDTO.setSessionFinished(VoteAppConstants.COMPLETED.equals(session.getSessionStatus()));

	    int entriesCount = voteUsrAttemptDAO.getSessionEntriesCount(session.getUid());

	    int totalStandardVotesCount = 0;

	    for (VoteQueContent question : voteContent.getVoteQueContents()) {

		SessionNominationDTO nominationDTO = new SessionNominationDTO();
		nominationDTO.setQuestionUid(question.getUid());
		nominationDTO.setNomination(question.getQuestion());

		int votesCount = voteUsrAttemptDAO.getStandardAttemptsForQuestionContentAndSessionUid(question.getUid(),
			session.getUid());
		totalStandardVotesCount += votesCount;

		nominationDTO.setNumberOfVotes(votesCount);
		nominationDTO.setPercentageOfVotes((entriesCount != 0) ? ((votesCount * 100) / entriesCount) : 0d);
		sessionDTO.getNominations().add(nominationDTO);

	    }

	    // open votes
	    if (voteContent.isAllowText()) {
		int userEnteredVotesCount = entriesCount - totalStandardVotesCount;
		Double voteRate = (userEnteredVotesCount != 0) ? ((userEnteredVotesCount * 100) / entriesCount) : 0d;
		sessionDTO.setOpenTextNumberOfVotes(userEnteredVotesCount);
		sessionDTO.setOpenTextPercentageOfVotes(voteRate);
	    } else {
		sessionDTO.setOpenTextNumberOfVotes(0);
		sessionDTO.setOpenTextPercentageOfVotes(0D);
	    }

	    sessionDTOs.add(sessionDTO);
	}

	// All groups total
	if (sessionDTOs.size() > 1) {
	    SummarySessionDTO totalSessionDTO = new SummarySessionDTO();
	    totalSessionDTO.setSessionUid(0L);
	    totalSessionDTO.setToolSessionId(0L);
	    totalSessionDTO.setSessionName(messageService.getMessage("label.all.groups.total"));
	    totalSessionDTO.setNominations(new TreeSet<SessionNominationDTO>());

	    HashMap<Long, SessionNominationDTO> nominationsTotals = new HashMap<>();
	    int totalOpenVotes = 0;
	    int totalVotes = 0;
	    for (SummarySessionDTO sessionDTO : sessionDTOs) {

		for (SessionNominationDTO nomination : sessionDTO.getNominations()) {
		    Long questionUid = nomination.getQuestionUid();
		    SessionNominationDTO dto = nominationsTotals.get(questionUid);
		    if (dto == null) {
			dto = new SessionNominationDTO();
			dto.setQuestionUid(questionUid);
			dto.setNomination(nomination.getNomination());
			dto.setNumberOfVotes(0);
			nominationsTotals.put(questionUid, dto);
			totalSessionDTO.getNominations().add(dto);
		    }
		    totalVotes += nomination.getNumberOfVotes();
		    dto.setNumberOfVotes(dto.getNumberOfVotes() + nomination.getNumberOfVotes());
		}

		totalVotes += sessionDTO.getOpenTextNumberOfVotes();
		totalOpenVotes += sessionDTO.getOpenTextNumberOfVotes();
	    }
	    for (SessionNominationDTO nomination : totalSessionDTO.getNominations()) {
		nomination.setPercentageOfVotes(
			(totalVotes != 0) ? ((nomination.getNumberOfVotes() * 100) / totalVotes) : 0d);
	    }
	    totalSessionDTO.setOpenTextNumberOfVotes(totalOpenVotes);
	    totalSessionDTO
		    .setOpenTextPercentageOfVotes((totalVotes != 0) ? ((totalOpenVotes * 100) / totalVotes) : 0d);
	    sessionDTOs.add(totalSessionDTO);
	}

	return sessionDTOs;
    }

    /**
     * Get the count of all the potential learners for the vote session. This will include the people that have never
     * logged into the lesson. Not great, but it is a better estimate of how many users there will be eventually than
     * the number of people already known to the tool.
     *
     * @param voteSessionId
     *            The tool session id
     */
    private int getVoteSessionPotentialLearnersCount(Long sessionUid) {
	VoteSession session = voteSessionDAO.getVoteSessionByUID(sessionUid);
	if (session != null) {
	    return toolService.getCountUsersForActivity(session.getVoteSessionId());
	} else {
	    logger.error("Unable to find vote session record id=" + sessionUid + ". Returning 0 users.");
	    return 0;
	}
    }

    @Override
    public List<VoteMonitoredAnswersDTO> getOpenVotes(Long voteContentUid, Long currentSessionId, Long userId) {
	Set<String> userEntries = voteUsrAttemptDAO.getUserEntries(voteContentUid);

	List<VoteMonitoredAnswersDTO> monitoredAnswersDTOs = new LinkedList<>();
	for (String userEntry : userEntries) {

	    if ((userEntry == null) || (userEntry.length() == 0)) {
		continue;
	    }

	    VoteMonitoredAnswersDTO voteMonitoredAnswersDTO = new VoteMonitoredAnswersDTO();
	    voteMonitoredAnswersDTO.setQuestion(userEntry);

	    List<VoteUsrAttempt> userAttempts = voteUsrAttemptDAO.getUserAttempts(voteContentUid, userEntry);
	    List<VoteMonitoredUserDTO> monitoredUserContainerDTOs = new LinkedList<>();

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
		Map<String, VoteMonitoredUserDTO> mapMonitoredUserContainerDTO = MonitoringController
			.convertToVoteMonitoredUserDTOMap(monitoredUserContainerDTOs);

		voteMonitoredAnswersDTO.setQuestionAttempts(mapMonitoredUserContainerDTO);
		monitoredAnswersDTOs.add(voteMonitoredAnswersDTO);
	    }
	}

	return monitoredAnswersDTOs;
    }

    @Override
    public VoteContent getVoteContent(Long toolContentID) {
	return voteContentDAO.getVoteContentByContentId(toolContentID);
    }

    @Override
    public VoteQueContent getQuestionByDisplayOrder(final Long displayOrder, final Long voteContentUid) {
	return voteQueContentDAO.getQuestionByDisplayOrder(displayOrder, voteContentUid);
    }

    @Override
    public VoteQueUsr getUserById(long voteQueUsrId) {
	VoteQueUsr voteQueUsr = voteUserDAO.getVoteQueUsrById(voteQueUsrId);
	return voteQueUsr;
    }

    @Override
    public List<VoteUsrAttempt> getAttemptsForQuestionContentAndSessionUid(final Long questionUid,
	    final Long voteSessionUid) {
	return voteUsrAttemptDAO.getAttemptsForQuestionContentAndSessionUid(questionUid, voteSessionUid);
    }

    @Override
    public Set<String> getAttemptsForUserAndSessionUseOpenAnswer(final Long userUid, final Long sessionUid) {
	List<VoteUsrAttempt> list = voteUsrAttemptDAO.getAttemptsForUserAndSessionUseOpenAnswer(userUid, sessionUid);

	//String openAnswer = "";
	Set<String> userEntries = new HashSet<>();
	if ((list != null) && (list.size() > 0)) {
	    Iterator<VoteUsrAttempt> listIterator = list.iterator();
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = listIterator.next();

		Long questionUid = attempt.getVoteQueContent().getUid();
		if (!questionUid.toString().equals("1")) {
		    userEntries.add(attempt.getVoteQueContent().getQuestion());
		} else {
		    // this is a user entered vote
		    if (attempt.getUserEntry().length() > 0) {
			if (attempt.isVisible()) {
			    userEntries.add(attempt.getUserEntry());
			} else {
			    userEntries.add(getMessageService().getMessage("label.hidden"));
			}
		    }

		}
	    }
	}
	return userEntries;
    }

    @Override
    public Set<String> getAttemptsForUserAndSession(final Long queUsrUid, final Long sessionUid) {
	return voteUsrAttemptDAO.getAttemptsForUserAndSession(queUsrUid, sessionUid);
    }

    @Override
    public VoteQueContent getVoteQueContentByUID(Long uid) {
	if (uid == null) {
	    return null;
	}

	return voteQueContentDAO.getQuestionByUid(uid);
    }

    @Override
    public void saveOrUpdateVoteQueContent(VoteQueContent voteQueContent) {
	voteQueContentDAO.saveOrUpdateQuestion(voteQueContent);
    }

    @Override
    public VoteContent createQuestions(List<VoteQuestionDTO> questionDTOs, VoteContent voteContent) {

	int displayOrder = 0;
	for (VoteQuestionDTO questionDTO : questionDTOs) {
	    String currentQuestionText = questionDTO.getQuestion();

	    // skip empty questions
	    if (currentQuestionText.isEmpty()) {
		continue;
	    }

	    ++displayOrder;

	    VoteQueContent question = getVoteQueContentByUID(questionDTO.getUid());

	    // in case question doesn't exist
	    if (question == null) {
		question = new VoteQueContent(currentQuestionText, displayOrder, voteContent);
		// adding a new question to content
		if (logger.isDebugEnabled()) {
		    logger.debug("Adding a new question to content" + question);
		}
		voteContent.getVoteQueContents().add(question);
		question.setVoteContent(voteContent);

		// in case question exists already
	    } else {

		question.setQuestion(currentQuestionText);
		question.setDisplayOrder(displayOrder);
	    }

	    saveOrUpdateVoteQueContent(question);
	}

	return voteContent;
    }

    @Override
    public Map<String, String> buildQuestionMap(VoteContent voteContent, Collection<String> checkedOptions) {
	Map<String, String> mapQuestionsContent = new TreeMap<>(new VoteComparator());
	Set<VoteQueContent> questions = voteContent.getVoteQueContents();

	// should we add questions from data flow from other activities?
	if (Boolean.TRUE.equals(voteContent.getAssignedDataFlowObject())
		&& ((voteContent.getMaxExternalInputs() == null) || (voteContent.getExternalInputsAdded() == null)
			|| (voteContent.getExternalInputsAdded() < voteContent.getMaxExternalInputs()))) {
	    // If we are using tool input, we need to get it now and
	    // create questions. Once they are created, they will be not altered, no matter if another learner gets to
	    // this point and the tool input changed
	    HttpSession ss = SessionManager.getSession();
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    long userId = toolUser.getUserID().longValue();

	    // We get whatever the source tool provides us with and try to create questions out of it
	    ToolOutput toolInput = getToolInput(voteContent.getVoteContentId(), new Long(userId).intValue());

	    Object value = toolInput.getValue().getComplex();
	    short inputsAdded = voteContent.getExternalInputsAdded() == null ? 0 : voteContent.getExternalInputsAdded();
	    Short maxInputs = voteContent.getMaxExternalInputs();
	    Set<VoteQueContent> existingNominations = voteContent.getVoteQueContents();
	    // The input is an array (users) of arrays of strings (their answers)
	    if (value instanceof String[][]) {
		if (value != null) {
		    String[][] usersAndAnswers = (String[][]) value;
		    int nominationIndex = voteContent.getVoteQueContents().size() + 1;
		    for (String[] userAnswers : usersAndAnswers) {
			if (userAnswers != null) {
			    if ((maxInputs != null) && (inputsAdded >= maxInputs)) {
				// if we reached the maximum number of inputs, i.e. number of students that will be
				// taken
				// into account
				logger.info(
					"We have reached max no of inputs,i.e number of students will be taken into account");
				break;
			    }
			    boolean anyAnswersAdded = false;
			    for (String questionText : userAnswers) {
				if (!StringUtils.isBlank(questionText)) {
				    VoteQueContent nomination = new VoteQueContent();
				    nomination.setDisplayOrder(nominationIndex);
				    nomination.setMcContent(voteContent);
				    nomination.setQuestion(questionText);
				    if (!VoteService.isNominationExists(nomination, existingNominations)) {
					saveOrUpdateVoteQueContent(nomination);
					voteContent.getVoteQueContents().add(nomination);
					nominationIndex++;
					anyAnswersAdded = true;
				    }
				}
			    }
			    if (anyAnswersAdded) {
				inputsAdded++;
			    }
			}
		    }
		}
	    } else if (value instanceof String[]) {
		// the input is a list of strings (questions, for example)
		int nominationIndex = voteContent.getVoteQueContents().size() + 1;
		String[] userAnswers = (String[]) value;
		for (String questionText : userAnswers) {
		    if ((maxInputs != null) && (inputsAdded >= maxInputs)) {
			logger.info(
				"We have reached max no of inputs,i.e number of students will be taken into account");
			// if we reached the maximum number of inputs, i.e. number of students that will be taken
			// into account
			break;
		    }

		    if (!StringUtils.isBlank(questionText)) {
			VoteQueContent nomination = new VoteQueContent();
			nomination.setDisplayOrder(nominationIndex);
			nomination.setMcContent(voteContent);
			nomination.setQuestion(questionText);
			if (!VoteService.isNominationExists(nomination, existingNominations)) {
			    saveOrUpdateVoteQueContent(nomination);
			    voteContent.getVoteQueContents().add(nomination);
			    nominationIndex++;
			    inputsAdded++;
			}
		    }
		}
	    } else if ((value instanceof String) && !StringUtils.isBlank((String) value)) {
		int nominationIndex = voteContent.getVoteQueContents().size() + 1;
		VoteQueContent nomination = new VoteQueContent();
		nomination.setDisplayOrder(nominationIndex);
		nomination.setMcContent(voteContent);
		nomination.setQuestion((String) value);
		if (!VoteService.isNominationExists(nomination, existingNominations)) {
		    saveOrUpdateVoteQueContent(nomination);
		    voteContent.getVoteQueContents().add(nomination);
		}
	    }
	    if (value instanceof SimpleURL[][]) {
		if (value != null) {
		    SimpleURL[][] usersAndUrls = (SimpleURL[][]) value;
		    int nominationIndex = voteContent.getVoteQueContents().size() + 1;
		    for (SimpleURL[] userUrls : usersAndUrls) {
			if (userUrls != null) {
			    if ((maxInputs != null) && (inputsAdded >= maxInputs)) {
				logger.info(
					"We have reached max no of inputs,i.e number of students will be taken into account");
				// if we reached the maximum number of inputs, i.e. number of students that will be
				// taken
				// into account
				break;
			    }
			    boolean anyAnswersAdded = false;
			    for (SimpleURL url : userUrls) {
				if (url != null) {
				    VoteQueContent nomination = new VoteQueContent();
				    nomination.setDisplayOrder(nominationIndex);
				    nomination.setMcContent(voteContent);

				    String link = "<a href=\"" + url.getUrl() + "\">" + url.getNameToDisplay() + "</a>";
				    nomination.setQuestion(link);
				    if (!VoteService.isNominationExists(nomination, existingNominations)) {
					saveOrUpdateVoteQueContent(nomination);
					voteContent.getVoteQueContents().add(nomination);
					nominationIndex++;
					anyAnswersAdded = true;
				    }
				}
			    }
			    if (anyAnswersAdded) {
				inputsAdded++;
			    }
			}
		    }
		}
	    }

	    else if (value instanceof SimpleURL[]) {
		// the input is a list of strings (questions, for example)
		int nominationIndex = voteContent.getVoteQueContents().size() + 1;
		SimpleURL[] userUrls = (SimpleURL[]) value;
		for (SimpleURL url : userUrls) {
		    if ((maxInputs != null) && (inputsAdded >= maxInputs)) {
			// if we reached the maximum number of inputs, i.e. number of students that will be taken
			// into account
			break;
		    }
		    if (url != null) {
			VoteQueContent nomination = new VoteQueContent();
			nomination.setDisplayOrder(nominationIndex);
			nomination.setMcContent(voteContent);

			String link = "<a href=\"" + url.getUrl() + "\">" + url.getNameToDisplay() + "</a>";
			nomination.setQuestion(link);
			if (!VoteService.isNominationExists(nomination, existingNominations)) {
			    saveOrUpdateVoteQueContent(nomination);
			    voteContent.getVoteQueContents().add(nomination);
			    nominationIndex++;
			    inputsAdded++;
			}
		    }
		}
	    } else if (value instanceof SimpleURL) {
		int nominationIndex = voteContent.getVoteQueContents().size() + 1;
		VoteQueContent nomination = new VoteQueContent();
		nomination.setDisplayOrder(nominationIndex);

		SimpleURL url = (SimpleURL) value;
		String link = "<a href=\"" + url.getUrl() + "\">" + url.getNameToDisplay() + "</a>";
		nomination.setQuestion(link);
		if (!VoteService.isNominationExists(nomination, existingNominations)) {
		    nomination.setMcContent(voteContent);
		    saveOrUpdateVoteQueContent(nomination);
		    voteContent.getVoteQueContents().add(nomination);
		}
	    }

	    voteContent.setExternalInputsAdded(inputsAdded);
	    saveVoteContent(voteContent);
	    questions = voteContent.getVoteQueContents();
	}

	for (VoteQueContent question : questions) {
	    String displayOrder = "" + question.getDisplayOrder();
	    if (((checkedOptions == null) || checkedOptions.contains(displayOrder)) && !displayOrder.equals("0")) {
		/* add the question to the questions Map in the displayOrder */
		mapQuestionsContent.put(displayOrder.toString(), question.getQuestion());
	    }
	}

	return mapQuestionsContent;
    }

    private static boolean isNominationExists(VoteQueContent nomination, Set<VoteQueContent> existingNominations) {
	if ((existingNominations != null) && (nomination != null)) {
	    for (VoteQueContent existingNomination : existingNominations) {
		if ((existingNomination.getQuestion() != null)
			&& existingNomination.getQuestion().equals(nomination.getQuestion())) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public List<VoteQueUsr> getUserBySessionOnly(final VoteSession voteSession) {
	return voteUserDAO.getUserBySessionOnly(voteSession);
    }

    @Override
    public VoteSession getVoteSessionByUID(Long uid) {
	return voteSessionDAO.getVoteSessionByUID(uid);
    }

    @Override
    public void createVoteQueUsr(VoteQueUsr voteQueUsr) {
	voteUserDAO.saveVoteUser(voteQueUsr);
    }

    @Override
    public VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long sessionUid) {
	return voteUserDAO.getVoteUserBySession(queUsrId, sessionUid);
    }

    @Override
    public void updateVoteUser(VoteQueUsr voteUser) {
	voteUserDAO.updateVoteUser(voteUser);
    }

    @Override
    public VoteQueUsr getUserByUserId(Long userID) {
	VoteQueUsr voteQueUsr = voteUserDAO.getUserByUserId(userID);
	return voteQueUsr;
    }

    @Override
    public VoteUsrAttempt getAttemptByUID(Long uid) {
	return voteUsrAttemptDAO.getAttemptByUID(uid);
    }

    @Override
    public void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) {
	voteUsrAttemptDAO.updateVoteUsrAttempt(voteUsrAttempt);
    }

    @Override
    public void removeAttemptsForUserandSession(final Long queUsrId, final Long sessionUid) {
	voteUsrAttemptDAO.removeAttemptsForUserandSession(queUsrId, sessionUid);
    }

    @Override
    public List<VoteUsrAttempt> getAttemptsForUser(final Long userUid) {
	return voteUsrAttemptDAO.getAttemptsForUser(userUid);
    }

    @Override
    public void createAttempt(VoteQueUsr voteQueUsr, Map<String, String> mapGeneralCheckedOptionsContent,
	    String userEntry, VoteSession voteSession, Long voteContentUid) {

	Date attempTime = new Date(System.currentTimeMillis());
	String timeZone = TimeZone.getDefault().getDisplayName();

	//in case of free entry
	if (mapGeneralCheckedOptionsContent.size() == 0) {
	    logger.info("In case of free entry");
	    VoteQueContent defaultContentFirstQuestion = voteQueContentDAO.getDefaultVoteContentFirstQuestion();
	    createAttempt(defaultContentFirstQuestion, voteQueUsr, attempTime, timeZone, userEntry, voteSession);

	    //if the question is selected
	} else if (voteContentUid != null) {
	    logger.info("In case of question is selected");

	    for (String key : mapGeneralCheckedOptionsContent.keySet()) {
		Long questionDisplayOrder = new Long(key);

		VoteQueContent question = getQuestionByDisplayOrder(questionDisplayOrder, voteContentUid);
		createAttempt(question, voteQueUsr, attempTime, timeZone, userEntry, voteSession);
	    }
	}

    }

    private void createAttempt(VoteQueContent question, VoteQueUsr user, Date attempTime, String timeZone,
	    String userEntry, VoteSession session) {

	if (question != null) {
	    VoteUsrAttempt existingAttempt = voteUsrAttemptDAO.getAttemptForUserAndQuestionContentAndSession(
		    user.getQueUsrId(), question.getVoteContent().getUid(), session.getUid());

	    if (existingAttempt != null) {
		existingAttempt.setUserEntry(userEntry);
		existingAttempt.setAttemptTime(attempTime);
		existingAttempt.setTimeZone(timeZone);
		updateVoteUsrAttempt(existingAttempt);
	    } else {
		VoteUsrAttempt voteUsrAttempt = new VoteUsrAttempt(attempTime, timeZone, question, user, userEntry,
			true);
		voteUsrAttemptDAO.saveVoteUsrAttempt(voteUsrAttempt);
	    }
	}
    }

    @Override
    public VoteQueContent getQuestionByUid(Long uid) {
	return voteQueContentDAO.getQuestionByUid(uid);
    }

    @Override
    public void removeVoteQueContent(VoteQueContent voteQueContent) {
	voteQueContentDAO.removeQuestion(voteQueContent);
    }

    @Override
    public VoteSession getSessionBySessionId(Long voteSessionId) {
	return voteSessionDAO.getSessionBySessionId(voteSessionId);
    }

    @Override
    public void updateVote(VoteContent vote) {
	voteContentDAO.updateVoteContent(vote);
    }

    @Override
    public int countSessionComplete() {
	return voteSessionDAO.countSessionComplete();
    }

    /**
     * logs hiding of a user entered vote
     */
    @Override
    public void hideOpenVote(VoteUsrAttempt voteUsrAttempt) {
	Long toolContentId = null;
	if (voteUsrAttempt.getVoteQueContent() != null && voteUsrAttempt.getVoteQueContent().getMcContent() != null) {
	    toolContentId = voteUsrAttempt.getVoteQueContent().getMcContent().getVoteContentId();
	}

	logEventService.logHideLearnerContent(voteUsrAttempt.getVoteQueUsr().getQueUsrId(),
		voteUsrAttempt.getVoteQueUsr().getUsername(), toolContentId, voteUsrAttempt.getUserEntry());

    }

    /**
     * logs showing of a user entered vote
     */
    @Override
    public void showOpenVote(VoteUsrAttempt voteUsrAttempt) {
	Long toolContentId = null;
	if (voteUsrAttempt.getVoteQueContent() != null && voteUsrAttempt.getVoteQueContent().getMcContent() != null) {
	    toolContentId = voteUsrAttempt.getVoteQueContent().getMcContent().getVoteContentId();
	}

	logEventService.logShowLearnerContent(voteUsrAttempt.getVoteQueUsr().getQueUsrId(),
		voteUsrAttempt.getVoteQueUsr().getUsername(), toolContentId, voteUsrAttempt.getUserEntry());
    }

    @Override
    public void saveVoteContent(VoteContent vote) {
	voteContentDAO.saveVoteContent(vote);
    }

    @Override
    public List<Long> getSessionsFromContent(VoteContent voteContent) {
	return voteSessionDAO.getSessionsFromContent(voteContent);
    }

    @Override
    public int getTotalNumberOfUsers() {
	return voteUserDAO.getTotalNumberOfUsers();
    }

    @Override
    public List<VoteUsrAttempt> getAttemptsForUserAndQuestionContent(final Long userUid, final Long questionUid) {
	try {
	    return voteUsrAttemptDAO.getAttemptsForUserAndQuestionContent(userUid, questionUid);
	} catch (DataAccessException e) {
	    throw new VoteApplicationException(
		    "Exception occured when lams is getting vote voteUsrRespDAO by user id and que content id: "
			    + e.getMessage(),
		    e);
	}
    }

    @Override
    public boolean studentActivityOccurredGlobal(VoteContent voteContent) {
	return !voteContent.getVoteSessions().isEmpty();
    }

    @Override
    public void recalculateUserAnswers(VoteContent content, Set<VoteQueContent> oldQuestions,
	    List<VoteQuestionDTO> questionDTOs, List<VoteQuestionDTO> deletedQuestions) {

	// create list of modified questions
	List<VoteQuestionDTO> modifiedQuestions = new ArrayList<>();
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
	if (logger.isDebugEnabled()) {
	    logger.debug("Copy tool content fromContentId" + fromContentId + " and toContentId " + toContentId);
	}
	if (fromContentId == null) {
	    // attempt retrieving tool's default content id with signatute VoteAppConstants.MY_SIGNATURE
	    long defaultContentId = getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    fromContentId = new Long(defaultContentId);
	}

	if (toContentId == null) {
	    logger.error("throwing ToolException: toContentId is null");
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
	    logger.error("throwing ToolException: WARNING!, retrieved toContent is null.");
	    throw new ToolException("WARNING! Fail to create toContent. Can't continue!");
	} else {
	    voteContentDAO.saveVoteContent(toContent);
	}
    }

    //   @SuppressWarnings("unchecked")
    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	VoteContent voteContent = voteContentDAO.getVoteContentByContentId(toolContentId);
	if (voteContent == null) {
	    logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	voteContentDAO.delete(voteContent);
    }

    // @SuppressWarnings("unchecked")
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

	for (VoteSession session : voteContent.getVoteSessions()) {
	    VoteQueUsr user = voteUserDAO.getVoteUserBySession(userId.longValue(), session.getUid());
	    if (user != null) {
		voteUsrAttemptDAO.removeAttemptsForUserandSession(user.getUid(), session.getUid());
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
	    logger.error("Unable to find default content for the voting tool");
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
	    if (logger.isDebugEnabled()) {
		logger.debug("Import tool Content : newUserUid" + newUserUid + " and toolContentID " + toolContentID
			+ "and toolContentPath" + toolContentPath + "and fromVersion" + fromVersion + "and toVersion"
			+ toVersion);
	    }

	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(VoteImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, voteToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof VoteContent)) {
		logger.error("Import Vote tool content failed. Deserialized object is " + toolPOJO);
		throw new ImportToolContentException(
			"Import Vote tool content failed. Deserialized object is " + toolPOJO);
	    }
	    VoteContent toolContentObj = (VoteContent) toolPOJO;

	    // reset it to new toolContentID
	    toolContentObj.setVoteContentId(toolContentID);
	    toolContentObj.setCreatedBy(newUserUid);

	    voteContentDAO.saveVoteContent(toolContentObj);
	} catch (ImportToolContentException e) {
	    logger.error("Error importing the tool content", e);
	    throw new ToolException(e);
	}
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	VoteContent voteContent = getVoteContent(toolContentId);
	if (voteContent == null) {
	    logger.error("throwing DataMissingException: WARNING!: retrieved voteContent is null.");
	    throw new DataMissingException("voteContent is missing");
	}
	voteContent.setDefineLater(false);
	saveVoteContent(voteContent);
    }

    @Override
    public void createToolSession(Long toolSessionID, String toolSessionName, Long toolContentID) throws ToolException {

	if (toolSessionID == null) {
	    logger.error("toolSessionID is null");
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
			VoteSession.INCOMPLETE, toolSessionName, voteContent, new TreeSet<VoteQueUsr>());

		voteSessionDAO.saveVoteSession(voteSession);

	    } catch (Exception e) {
		logger.error("Error creating new toolsession in the db");
		throw new ToolException("Error creating new toolsession in the db: " + e);
	    }
	}
    }

    @Override
    public void removeToolSession(Long toolSessionID) throws DataMissingException, ToolException {
	if (toolSessionID == null) {
	    logger.error("toolSessionID is null");
	    throw new DataMissingException("toolSessionID is missing");
	}

	VoteSession voteSession = null;
	try {
	    voteSession = getSessionBySessionId(toolSessionID);
	} catch (VoteApplicationException e) {
	    logger.error("error retrieving voteSession:");
	    throw new DataMissingException("error retrieving voteSession: " + e);
	} catch (Exception e) {
	    logger.error("error retrieving voteSession:");
	    throw new ToolException("error retrieving voteSession: " + e);
	}

	if (voteSession == null) {
	    logger.error("voteSession is null");
	    throw new DataMissingException("voteSession is missing");
	}

	try {
	    voteSessionDAO.removeVoteSession(voteSession);
	} catch (VoteApplicationException e) {
	    throw new ToolException("error deleting voteSession:" + e);
	}
    }

    @Override
    public String leaveToolSession(Long toolSessionID, Long learnerId) throws DataMissingException, ToolException {

	if (learnerId == null) {
	    logger.error("learnerId is null");
	    throw new DataMissingException("learnerId is missing");
	}

	if (toolSessionID == null) {
	    logger.error("toolSessionID is null");
	    throw new DataMissingException("toolSessionID is missing");
	}

	VoteSession voteSession = null;
	try {
	    voteSession = getSessionBySessionId(toolSessionID);
	} catch (VoteApplicationException e) {
	    logger.error("error retrieving voteSession");
	    throw new DataMissingException("error retrieving voteSession: " + e);
	} catch (Exception e) {
	    throw new ToolException("error retrieving voteSession: " + e);
	}
	voteSession.setSessionStatus(VoteAppConstants.COMPLETED);
	voteSessionDAO.updateVoteSession(voteSession);

	String nextUrl = toolService.completeToolSession(toolSessionID, learnerId);
	if (nextUrl == null) {
	    logger.error("nextUrl is null");
	    throw new ToolException("nextUrl is null");
	}
	return nextUrl;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionID)
	    throws DataMissingException, ToolException {
	throw new ToolException("not yet implemented");
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIDs)
	    throws DataMissingException, ToolException {
	throw new ToolException("not yet implemented");

    }

    @Override
    public Tool getToolBySignature(String toolSignature) {
	Tool tool = toolService.getToolBySignature(toolSignature);
	return tool;
    }

    @Override
    public long getToolDefaultContentIdBySignature(String toolSignature) {
	long contentId = 0;
	contentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	return contentId;
    }

    @Override
    public List<Long> getToolSessionsForContent(VoteContent vote) {
	List<Long> listToolSessionIds = voteSessionDAO.getSessionsFromContent(vote);
	return listToolSessionIds;
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    @Override
    public void auditLogStartEditingActivityInMonitor(long toolContentID) {
	toolService.auditLogStartEditingActivityInMonitor(toolContentID);
    }

    @Override
    public boolean isLastActivity(Long toolSessionId) {
	return toolService.isLastActivity(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	VoteContent content = getVoteContent(toolContentId);
	if (content == null) {
	    long defaultToolContentId = getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    content = getVoteContent(defaultToolContentId);
	}
	return getVoteOutputFactory().getToolOutputDefinitions(content, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getVoteContent(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getVoteContent(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	VoteContent voteContent = voteContentDAO.getVoteContentByContentId(toolContentId);
	for (VoteSession session : voteContent.getVoteSessions()) {
	    if (!session.getVoteQueUsers().isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return voteOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return voteOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<>();
    }

    @Override
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	return null;
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	Long userId = user.getUserId().longValue();

	VoteSession session = getSessionBySessionId(toolSessionId);
	if ((session == null) || (session.getVoteContent() == null)) {
	    return;
	}
	VoteContent content = session.getVoteContent();

	// copy answers only in case leader aware feature is ON
	if (content.isUseSelectLeaderToolOuput()) {

	    VoteQueUsr voteUser = voteUserDAO.getVoteUserBySession(userId, toolSessionId);
	    // create user if he hasn't accessed this activity yet
	    if (voteUser == null) {
		String userName = user.getLogin();
		String fullName = user.getFirstName() + " " + user.getLastName();

		voteUser = new VoteQueUsr(userId, userName, fullName, session, new TreeSet<VoteUsrAttempt>());
		createVoteQueUsr(voteUser);
	    }

	    VoteQueUsr groupLeader = session.getGroupLeader();

	    // check if leader has submitted answers
	    if ((groupLeader != null) && groupLeader.isResponseFinalised()) {

		// we need to make sure specified user has the same scratches as a leader
		copyAnswersFromLeader(voteUser, groupLeader);
	    }
	}
    }

    @Override
    public List<VoteQueContent> getAllQuestionsSorted(final long voteContentId) {
	return voteQueContentDAO.getAllQuestionsSorted(voteContentId);
    }

    /******** Tablesorter methods ************/
    /**
     * Gets the basic details about an attempt for a nomination. questionUid must not be null, sessionUid may be NULL.
     * This is
     * unusual for these methods - usually sessionId may not be null. In this case if sessionUid is null then you get
     * the values for the whole class, not just the group.
     *
     * Will return List<[login (String), fullname(String), attemptTime(Timestamp]>
     */
    @Override
    public List<Object[]> getUserAttemptsForTablesorter(Long sessionUid, Long questionUid, int page, int size,
	    int sorting, String searchString) {
	return voteUsrAttemptDAO.getUserAttemptsForTablesorter(sessionUid, questionUid, page, size, sorting,
		searchString, userManagementService);
    }

    @Override
    public int getCountUsersBySession(Long sessionUid, Long questionUid, String searchString) {
	return voteUsrAttemptDAO.getCountUsersBySession(sessionUid, questionUid, searchString);
    }

    @Override
    public List<VoteStatsDTO> getStatisticsBySession(Long toolContentId) {

	List<VoteStatsDTO> stats = voteUsrAttemptDAO.getStatisticsBySession(toolContentId);
	for (VoteStatsDTO stat : stats) {
	    stat.setCountAllUsers(getVoteSessionPotentialLearnersCount(stat.getSessionUid()));
	}
	return stats;
    }

    /** Gets the details for the open text nominations */
    @Override
    public List<OpenTextAnswerDTO> getUserOpenTextAttemptsForTablesorter(Long sessionUid, Long contentUid, int page,
	    int size, int sorting, String searchStringVote, String searchStringUsername) {
	return voteUsrAttemptDAO.getUserOpenTextAttemptsForTablesorter(sessionUid, contentUid, page, size, sorting,
		searchStringVote, searchStringUsername, userManagementService);
    }

    @Override
    public int getCountUsersForOpenTextEntries(Long sessionUid, Long contentUid, String searchStringVote,
	    String searchStringUsername) {
	return voteUsrAttemptDAO.getCountUsersForOpenTextEntries(sessionUid, contentUid, searchStringVote,
		searchStringUsername);
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

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
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
     * @return Returns the logEventService.
     */
    public ILogEventService getLogEventService() {
	return logEventService;
    }

    /**
     * @param logEventService
     *            The logEventService to set.
     */
    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
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
    @Override
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

    @Override
    public void removeQuestionsFromCache(VoteContent voteContent) {
	voteContentDAO.removeQuestionsFromCache(voteContent);
    }

    @Override
    public void removeVoteContentFromCache(VoteContent voteContent) {
	voteContentDAO.removeVoteContentFromCache(voteContent);
    }

    public void setDataFlowDAO(IDataFlowDAO dataFlowDAO) {
	this.dataFlowDAO = dataFlowDAO;
    }

    @Override
    public ToolOutput getToolInput(Long requestingToolContentId, Integer learnerId) {
	// just forwarding to learner service
	return toolService.getToolInput(requestingToolContentId, VoteAppConstants.DATA_FLOW_OBJECT_ASSIGMENT_ID,
		learnerId);
    }

    @Override
    public void saveDataFlowObjectAssigment(DataFlowObject assignedDataFlowObject) {
	// this also should be done in learner service, but for simplicity...
	if (assignedDataFlowObject != null) {
	    assignedDataFlowObject.setToolAssigmentId(VoteAppConstants.DATA_FLOW_OBJECT_ASSIGMENT_ID);
	    dataFlowDAO.update(assignedDataFlowObject);
	}
    }

    @Override
    public DataFlowObject getAssignedDataFlowObject(Long toolContentId) {
	return dataFlowDAO.getAssignedDataFlowObject(toolContentId, VoteAppConstants.DATA_FLOW_OBJECT_ASSIGMENT_ID);
    }

    @Override
    public List<DataFlowObject> getDataFlowObjects(Long toolContentId) {
	return dataFlowDAO.getDataFlowObjectsByToolContentId(toolContentId);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getVoteOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	VoteSession session = getSessionBySessionId(toolSessionId);
	VoteQueUsr learner = getVoteUserBySession(learnerId, session.getUid());
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Date startDate = null;
	Date endDate = null;
	Set<VoteUsrAttempt> attempts = learner.getVoteUsrAttempts(); // expect only one
	for (VoteUsrAttempt item : attempts) {
	    Date newDate = item.getAttemptTime();
	    if (newDate != null) {
		if (startDate == null || newDate.before(startDate)) {
		    startDate = newDate;
		}
		if (endDate == null || newDate.after(endDate)) {
		    endDate = newDate;
		}
	    }
	}

	if (learner.isResponseFinalised()) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, startDate, endDate);
	} else {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, startDate, null);
	}
    }

    // ****************** REST methods *************************

    /**
     * Rest call to create a new Vote content. Required fields in toolContentJSON: "title", "instructions", "answers".
     * The "answers" entry should be a ArrayNode of Strings.
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {
	if (logger.isDebugEnabled()) {
	    logger.debug("Rest call to create a new Vote content for userID" + userID + " and toolContentID "
		    + toolContentID);
	}
	Date updateDate = new Date();

	VoteContent vote = new VoteContent();
	vote.setVoteContentId(toolContentID);
	vote.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	vote.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));
	vote.setCreatedBy(userID);
	vote.setCreationDate(updateDate);
	vote.setUpdateDate(updateDate);

	vote.setAllowText(JsonUtil.optBoolean(toolContentJSON, "allowText", Boolean.FALSE));
	vote.setDefineLater(false);
	vote.setLockOnFinish(JsonUtil.optBoolean(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	vote.setMaxNominationCount(JsonUtil.optString(toolContentJSON, "maxNominations", "1"));
	vote.setMinNominationCount(JsonUtil.optString(toolContentJSON, "minNominations", "1"));
	vote.setShowResults(JsonUtil.optBoolean(toolContentJSON, "showResults", Boolean.TRUE));
	vote.setUseSelectLeaderToolOuput(
		JsonUtil.optBoolean(toolContentJSON, RestTags.USE_SELECT_LEADER_TOOL_OUTPUT, Boolean.FALSE));

	// Is the data flow functionality actually used anywhere?
	vote.setAssignedDataFlowObject(JsonUtil.optBoolean(toolContentJSON, "assignedDataFlowObject"));
	Integer externalInputsAdded = JsonUtil.optInt(toolContentJSON, "externalInputsAdded");
	if (externalInputsAdded != null) {
	    vote.setExternalInputsAdded(externalInputsAdded.shortValue());
	}
	vote.setMaxExternalInputs(JsonUtil.optInt(toolContentJSON, "maxInputs", 0).shortValue());

	// submissionDeadline is set in monitoring

	// **************************** Nomination entries *********************
	ArrayNode answers = JsonUtil.optArray(toolContentJSON, RestTags.ANSWERS);
	//Set newAnswersSet = vote.getVoteQueContents();
	for (int i = 0; i < answers.size(); i++) {
	    //  String answerJSONData = (String) answers.get(i);
	    VoteQueContent answer = new VoteQueContent();
	    answer.setDisplayOrder(i + 1);
	    answer.setMcContent(vote);
	    answer.setQuestion(answers.get(i).asText());
	    answer.setVoteContent(vote);
	    vote.getVoteQueContents().add(answer);
	}

	saveVoteContent(vote);

    }

}