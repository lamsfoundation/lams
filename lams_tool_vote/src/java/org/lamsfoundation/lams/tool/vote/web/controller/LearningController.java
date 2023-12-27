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

package org.lamsfoundation.lams.tool.vote.web.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.model.VoteContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.model.VoteSession;
import org.lamsfoundation.lams.tool.vote.model.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.util.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.tool.vote.web.form.VoteLearningForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Ozgur Demirtas
 */
@Controller
@RequestMapping("/learning")
public class LearningController implements VoteAppConstants {
    private static Logger logger = Logger.getLogger(LearningController.class.getName());

    @Autowired
    @Qualifier("lavoteMessageService")
    private MessageService messageService;

    @Autowired
    private IVoteService voteService;

    @RequestMapping("/viewAllResults")
    public String viewAllResults(VoteLearningForm voteLearningForm, HttpServletRequest request) {
	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();

	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMinNominationCountReached(new Boolean(false).toString());

	LearningController.repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	logger.info("Tool session ID" + toolSessionID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	voteLearningForm.setUserID(userID);

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));
	VoteContent voteContent = voteSession.getVoteContent();

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	Long toolContentID = voteContent.getVoteContentId();

	Long toolSessionUid = voteSession.getUid();

	VoteQueUsr existingVoteQueUsr = voteService.getVoteUserBySession(new Long(userID), voteSession.getUid());

	existingVoteQueUsr.setFinalScreenRequested(true);
	voteService.updateVoteUser(existingVoteQueUsr);

	Set<String> userAttempts = voteService.getAttemptsForUserAndSession(existingVoteQueUsr.getUid(),
		toolSessionUid);
	request.setAttribute(LIST_GENERAL_CHECKED_OPTIONS_CONTENT, userAttempts);

	voteService.prepareChartData(request, toolContentID, toolSessionUid, voteGeneralLearnerFlowDTO);

	voteLearningForm.resetCommands();

	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	boolean isLastActivity = voteService.isLastActivity(new Long(toolSessionID));
	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, isLastActivity);
	return "/learning/AllNominations";
    }

    @RequestMapping("/viewAnswers")
    public String viewAnswers(VoteLearningForm voteLearningForm, HttpServletRequest request) {
	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();

	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMinNominationCountReached(new Boolean(false).toString());

	LearningController.repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	logger.info("Tool session id :" + toolSessionID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	logger.info("User id :" + userID);
	voteLearningForm.setUserID(userID);

	String revisitingUser = request.getParameter(REVISITING_USER);
	voteLearningForm.setRevisitingUser(revisitingUser);

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));
	VoteContent voteContent = voteSession.getVoteContent();

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	if (revisitingUser.equals("true")) {
	    /* get back login user DTO */
	    HttpSession ss = SessionManager.getSession();
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    long userId = toolUser.getUserID().longValue();
	    VoteQueUsr voteQueUsr = voteService.getUserByUserId(userId);

	    List<VoteUsrAttempt> attempts = voteService.getAttemptsForUser(voteQueUsr.getUid());

	    Map<String, String> mapQuestionsContent = new TreeMap<>(new VoteComparator());
	    Iterator<VoteUsrAttempt> listIterator = attempts.iterator();
	    int order = 0;
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = listIterator.next();
		VoteQueContent voteQueContent = attempt.getVoteQueContent();
		order++;
		if (voteQueContent != null) {
		    mapQuestionsContent.put(new Integer(order).toString(), voteQueContent.getQuestion());
		}
	    }
	    request.setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapQuestionsContent);
	} else {
	    //this is not a revisiting user
	    logger.info("If not a revisiting user");
	}

	voteLearningForm.resetCommands();

	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);
	boolean isLastActivity = voteService.isLastActivity(new Long(toolSessionID));
	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, isLastActivity);
	return "/learning/ViewAnswers";
    }

    @RequestMapping("/redoQuestionsOk")
    public String redoQuestionsOk(VoteLearningForm voteLearningForm, HttpServletRequest request) {

	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();

	LearningController.repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	logger.info("Tool session id:" + toolSessionID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	logger.info("User id:" + userID);
	voteLearningForm.setUserID(userID);

	String revisitingUser = request.getParameter(REVISITING_USER);
	voteLearningForm.setRevisitingUser(revisitingUser);

	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMinNominationCountReached(new Boolean(false).toString());

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));
	VoteContent voteContent = voteSession.getVoteContent();
	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	voteLearningForm.resetCommands();

	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);
	return redoQuestions(voteLearningForm, request);
    }

    @RequestMapping("/learnerFinished")
    public String learnerFinished(VoteLearningForm voteLearningForm, HttpServletRequest request) {
	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();

	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMinNominationCountReached(new Boolean(false).toString());

	LearningController.repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	logger.info("Tool Session id :" + toolSessionID);
	voteLearningForm.setToolSessionID(toolSessionID);

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));
	String userID = request.getParameter(USER_ID);
	logger.info("User id:" + userID);
	voteLearningForm.setUserID(userID);

	VoteQueUsr voteQueUsr = voteService.getVoteUserBySession(new Long(userID), voteSession.getUid());

	voteQueUsr.setResponseFinalised(true);
	if (!voteSession.getVoteContent().isShowResults()) {
	    // if not isShowResults then we will have skipped the final screen.
	    voteQueUsr.setFinalScreenRequested(true);
	}
	voteService.updateVoteUser(voteQueUsr);

	String revisitingUser = request.getParameter(REVISITING_USER);
	voteLearningForm.setRevisitingUser(revisitingUser);

	VoteContent voteContent = voteSession.getVoteContent();

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	String nextUrl = null;
	try {
	    nextUrl = voteService.leaveToolSession(new Long(toolSessionID), new Long(userID));
	} catch (DataMissingException e) {
	    logger.error("failure getting nextUrl: " + e);
	    voteLearningForm.resetCommands();
	    //throw new ServletException(e);
	    return "/learningIndex";
	} catch (ToolException e) {
	    logger.error("failure getting nextUrl: " + e);
	    voteLearningForm.resetCommands();
	    //throw new ServletException(e);
	    return "/learningIndex";
	} catch (Exception e) {
	    logger.error("unknown exception getting nextUrl: " + e);
	    voteLearningForm.resetCommands();
	    //throw new ServletException(e);
	    return "/learningIndex";
	}

	voteLearningForm.resetCommands();

	/* pay attention here */
	return "redirect:" + nextUrl;
    }

    @RequestMapping("/continueOptionsCombined")
    public String continueOptionsCombined(VoteLearningForm voteLearningForm, HttpServletRequest request) {

	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();

	LearningController.repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	logger.info("Tool session id:" + toolSessionID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	logger.info("User id:" + userID);
	voteLearningForm.setUserID(userID);

	String maxNominationCount = request.getParameter(MAX_NOMINATION_COUNT);
	voteLearningForm.setMaxNominationCount(maxNominationCount);

	String minNominationCount = request.getParameter(MIN_NOMINATION_COUNT);
	voteLearningForm.setMinNominationCount(minNominationCount);

	String userEntry = request.getParameter(USER_ENTRY);
	voteLearningForm.setUserEntry(userEntry);

	VoteSession session = voteService.getSessionBySessionId(new Long(toolSessionID));
	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMinNominationCountReached(new Boolean(false).toString());

	Collection<String> voteDisplayOrderIds = voteLearningForm.votesAsCollection();

	// check number of votes
	int castVoteCount = voteDisplayOrderIds != null ? voteDisplayOrderIds.size() : 0;
	if ((userEntry != null) && (userEntry.length() > 0)) {
	    ++castVoteCount;
	}

	int intMaxNominationCount = 0;
	if (maxNominationCount != null) {
	    intMaxNominationCount = new Integer(maxNominationCount).intValue();
	}

	if (castVoteCount > intMaxNominationCount) {
	    voteLearningForm.setMaxNominationCountReached(new Boolean(true).toString());
	    voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(true).toString());
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	    errorMap.add("GLOBAL", messageService.getMessage("error.maxNominationCount.reached"));
	    request.setAttribute("errorMap", errorMap);
	    logger.error("You have selected too many nominations.");
	    return "/learning/AnswersContent";
	}

	VoteContent voteContent = session.getVoteContent();

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	Long toolContentID = voteContent.getVoteContentId();
	Long voteContentUid = voteContent.getUid();

	boolean userEntryAvailable = false;
	if ((userEntry != null) && (userEntry.length() > 0)) {
	    userEntryAvailable = true;
	}

	Long toolSessionUid = session.getUid();

	VoteQueUsr user = voteService.getVoteUserBySession(new Long(userID), session.getUid());
	if (user == null) {
	    throw new VoteApplicationException(
		    "User with userId= " + userID + " and sessionUid= " + session.getUid() + " doesn't exist.");
	}

	voteService.removeAttemptsForUserandSession(user.getUid(), session.getUid());

	/*
	 * to minimize changes to working code, convert the String[] array to the mapGeneralCheckedOptionsContent
	 * structure
	 */
	Map<String, String> mapGeneralCheckedOptionsContent = voteService.buildQuestionMap(voteContent,
		voteDisplayOrderIds);

	if (mapGeneralCheckedOptionsContent.size() > 0) {
	    voteService.createAttempt(user, mapGeneralCheckedOptionsContent, "", session, voteContentUid);
	}

	if ((mapGeneralCheckedOptionsContent.size() == 0 && (userEntryAvailable == true))) {
	    Map<String, String> mapLeanerCheckedOptionsContent = new TreeMap<>(new VoteComparator());

	    if (userEntry.length() > 0) {
		voteService.createAttempt(user, mapLeanerCheckedOptionsContent, userEntry, session, voteContentUid);
	    }
	}

	if ((mapGeneralCheckedOptionsContent.size() > 0) && (userEntryAvailable == true)) {
	    Map<String, String> mapLeanerCheckedOptionsContent = new TreeMap<>(new VoteComparator());

	    if (userEntry.length() > 0) {
		voteService.createAttempt(user, mapLeanerCheckedOptionsContent, userEntry, session, voteContentUid);
	    }
	}

	/* put the map in the request ready for the next screen */
	request.setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);
	voteLearningForm.setMapGeneralCheckedOptionsContent(mapGeneralCheckedOptionsContent);
	voteGeneralLearnerFlowDTO.setMapGeneralCheckedOptionsContent(mapGeneralCheckedOptionsContent);

	voteLearningForm.setNominationsSubmited(new Boolean(true).toString());
	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(true).toString());

	voteService.prepareChartData(request, toolContentID, toolSessionUid, voteGeneralLearnerFlowDTO);

	voteLearningForm.resetCommands();
	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	return "/learning/IndividualLearnerResults";
    }

    @RequestMapping("/redoQuestions")
    public String redoQuestions(VoteLearningForm voteLearningForm, HttpServletRequest request) {

	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();

	LearningController.repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	voteLearningForm.setUserID(userID);

	String revisitingUser = request.getParameter(REVISITING_USER);
	voteLearningForm.setRevisitingUser(revisitingUser);

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));

	VoteContent voteContent = voteSession.getVoteContent();

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());

	Map<String, String> mapQuestionsContent = voteService.buildQuestionMap(voteContent, null);
	request.setAttribute(MAP_QUESTION_CONTENT_LEARNER, mapQuestionsContent);

	Map<String, String> mapGeneralCheckedOptionsContent = new TreeMap<>(new VoteComparator());
	request.setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);

	voteLearningForm.setUserEntry("");

	voteLearningForm.resetCommands();
	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	return "/learning/AnswersContent";
    }

    @RequestMapping(path = "/checkLeaderProgress")
    @ResponseBody
    public String checkLeaderProgress(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	VoteSession session = voteService.getSessionBySessionId(toolSessionId);
	VoteQueUsr leader = session.getGroupLeader();
	logger.info("Leader :" + leader);

	boolean isLeaderResponseFinalized = leader.isResponseFinalised();

	ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
	objectNode.put("isLeaderResponseFinalized", isLeaderResponseFinalized);
	response.setContentType("application/json;charset=UTF-8");
	return objectNode.toString();
    }

    private static void repopulateRequestParameters(HttpServletRequest request, VoteLearningForm voteLearningForm) {
	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	voteLearningForm.setUserID(userID);

	String revisitingUser = request.getParameter(REVISITING_USER);
	voteLearningForm.setRevisitingUser(revisitingUser);

	String previewOnly = request.getParameter(PREVIEW_ONLY);
	voteLearningForm.setPreviewOnly(previewOnly);

	String maxNominationCount = request.getParameter(MAX_NOMINATION_COUNT);
	voteLearningForm.setMaxNominationCount(maxNominationCount);

	String minNominationCount = request.getParameter(MIN_NOMINATION_COUNT);
	voteLearningForm.setMinNominationCount(minNominationCount);

	String useSelectLeaderToolOuput = request.getParameter(USE_SELECT_LEADER_TOOL_OUTPUT);
	voteLearningForm.setUseSelectLeaderToolOuput(useSelectLeaderToolOuput);

	String allowTextEntry = request.getParameter(ALLOW_TEXT_ENTRY);
	voteLearningForm.setAllowTextEntry(allowTextEntry);

	String showResults = request.getParameter(SHOW_RESULTS);
	voteLearningForm.setShowResults(showResults);

	String lockOnFinish = request.getParameter(LOCK_ON_FINISH);
	voteLearningForm.setLockOnFinish(lockOnFinish);

	String reportViewOnly = request.getParameter(REPORT_VIEW_ONLY);
	voteLearningForm.setReportViewOnly(reportViewOnly);

	String userEntry = request.getParameter(USER_ENTRY);
	voteLearningForm.setUserEntry(userEntry);

	String groupLeaderName = request.getParameter(ATTR_GROUP_LEADER_NAME);
	voteLearningForm.setGroupLeaderName(groupLeaderName);

	String groupLeaderUserId = request.getParameter(ATTR_GROUP_LEADER_USER_ID);
	voteLearningForm.setGroupLeaderUserId(groupLeaderUserId);

	boolean isUserLeader = WebUtil.readBooleanParam(request, "userLeader");
	voteLearningForm.setIsUserLeader(isUserLeader);
    }

    /*
     * By now, the passed tool session id MUST exist in the db through the calling of: public void
     * createToolSession(Long toolSessionID, Long toolContentId) by the container.
     *
     *
     * make sure this session exists in tool's session table by now.
     */
    @RequestMapping("/start")
    public String start(VoteLearningForm voteLearningForm, HttpServletRequest request) {

	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();

	voteLearningForm.setRevisitingUser(new Boolean(false).toString());
	voteLearningForm.setUserEntry("");
	voteLearningForm.setCastVoteCount(0);
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setRevisitingUser(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setUserEntry("");
	voteGeneralLearnerFlowDTO.setCastVoteCount("0");
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());

	boolean validateParameters = LearningController.validateParameters(request, voteLearningForm);
	if (!validateParameters) {
	    return "/error";
	}

	String toolSessionID = voteLearningForm.getToolSessionID();

	/*
	 * by now, we made sure that the passed tool session id exists in the db as a new record Make sure we can
	 * retrieve it and the relavent content
	 */

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));

	if (voteSession == null) {

	    logger.error("error: The tool expects voteSession.");
	    return "/error";
	}

	/*
	 * find out what content this tool session is referring to get the content for this tool session Each passed
	 * tool session id points to a particular content. Many to one mapping.
	 */
	VoteContent voteContent = voteSession.getVoteContent();
	if (voteContent == null) {

	    logger.error("error: The tool expects voteContent.");
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	    errorMap.add("GLOBAL", messageService.getMessage("error.content.doesNotExist"));
	    request.setAttribute("errorMap", errorMap);
	    return "/error";
	}

	/*
	 * The content we retrieved above must have been created before in Authoring time. And the passed tool session
	 * id already refers to it.
	 */
	LearningController.setupAttributes(request, voteContent, voteLearningForm, voteGeneralLearnerFlowDTO);

	voteLearningForm.setToolContentID(voteContent.getVoteContentId().toString());
	voteGeneralLearnerFlowDTO.setToolContentID(voteContent.getVoteContentId().toString());

	voteLearningForm.setToolContentUID(voteContent.getUid().toString());
	voteGeneralLearnerFlowDTO.setToolContentUID(voteContent.getUid().toString());

	String mode = voteLearningForm.getLearningMode();
	voteGeneralLearnerFlowDTO.setLearningMode(mode);

	String userId = voteLearningForm.getUserID();

	Map<String, String> mapQuestions = voteService.buildQuestionMap(voteContent, null);
	request.setAttribute(VoteAppConstants.MAP_QUESTION_CONTENT_LEARNER, mapQuestions);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	VoteQueUsr user = null;
	if ((mode != null) && mode.equals(ToolAccessMode.TEACHER.toString())) {
	    // monitoring mode - user is specified in URL
	    // user may be null if the user was force completed.
	    user = getSpecifiedUser(toolSessionID, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(toolSessionID);
	}

	boolean isLastActivity = voteService.isLastActivity(new Long(toolSessionID));
	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, isLastActivity);

	// check if there is submission deadline
	Date submissionDeadline = voteContent.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    request.setAttribute(VoteAppConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());
	    voteGeneralLearnerFlowDTO.setSubmissionDeadline(tzSubmissionDeadline);

	    // calculate whether deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return "/learning/submissionDeadline";
	    }
	}

	/* find out if the content is being modified at the moment. */
	if (voteContent.isDefineLater()) {
	    return "/learning/defineLater";
	}

	//process group leader
	VoteQueUsr groupLeader = null;
	if (voteContent.isUseSelectLeaderToolOuput()) {
	    groupLeader = voteService.checkLeaderSelectToolForSessionLeader(user, new Long(toolSessionID));

	    // forwards to the leaderSelection page
	    if (groupLeader == null && !mode.equals(ToolAccessMode.TEACHER.toString())) {
		Set<VoteQueUsr> groupVoteUsers = voteSession.getVoteQueUsers();
		List<User> groupUsers = groupVoteUsers.stream().map(qaUser -> {
		    User userI = new User();
		    userI.setUserId(qaUser.getQueUsrId().intValue());
		    userI.setFirstName(qaUser.getFullname());
	            return userI;
	        }).collect(Collectors.toList());
		
		request.setAttribute(ATTR_GROUP_USERS, groupUsers);
		request.setAttribute(TOOL_SESSION_ID, toolSessionID);
		request.setAttribute(ATTR_CONTENT, voteContent);

		return "/learning/WaitForLeader";
	    }

	    // check if leader has submitted all answers
	    if (groupLeader.isResponseFinalised() && !mode.equals(ToolAccessMode.TEACHER.toString())) {

		// in case user joins the lesson after leader has answers some answers already - we need to make sure
		// he has the same scratches as leader
		voteService.copyAnswersFromLeader(user, groupLeader);

		user.setFinalScreenRequested(true);
		user.setResponseFinalised(true);
		voteService.updateVoteUser(user);
	    }

	    // store group leader information
	    voteLearningForm.setGroupLeaderName(groupLeader.getFullname());
	    voteLearningForm.setGroupLeaderUserId(
		    groupLeader.getQueUsrId() != null ? groupLeader.getQueUsrId().toString() : "");
	    boolean isUserLeader = voteService.isUserGroupLeader(user.getQueUsrId(), new Long(toolSessionID));
	    voteLearningForm.setIsUserLeader(isUserLeader);
	}

	if (mode.equals("teacher")) {

	    Long sessionUid = user.getVoteSession().getUid();
	    putMapQuestionsContentIntoRequest(request, voteService, user);
	    Set<String> userAttempts = voteService.getAttemptsForUserAndSessionUseOpenAnswer(user.getUid(), sessionUid);
	    request.setAttribute(VoteAppConstants.LIST_GENERAL_CHECKED_OPTIONS_CONTENT, userAttempts);

	    // since this is progress view, present a screen which can not be edited
	    voteLearningForm.setReportViewOnly(new Boolean(true).toString());
	    voteGeneralLearnerFlowDTO.setReportViewOnly(new Boolean(true).toString());

	    voteService.prepareChartData(request, voteContent.getVoteContentId(), voteSession.getUid(),
		    voteGeneralLearnerFlowDTO);

	    boolean isGroupedActivity = voteService.isGroupedActivity(new Long(voteLearningForm.getToolContentID()));
	    request.setAttribute("isGroupedActivity", isGroupedActivity);

	    return "/learning/ExitLearning";
	}

	/*
	 * the user's session id AND user id exists in the tool tables goto this screen if the OverAll Results scren has
	 * been already called up by this user
	 */
	if (user.isFinalScreenRequested()) {
	    Long sessionUid = user.getVoteSession().getUid();
	    VoteSession voteUserSession = voteService.getVoteSessionByUID(sessionUid);
	    String userSessionId = voteUserSession.getVoteSessionId().toString();

	    if (toolSessionID.toString().equals(userSessionId)) {
		// the learner has already responsed to this content, just generate a read-only report. Use redo
		// questions for this
		putMapQuestionsContentIntoRequest(request, voteService, user);

		boolean isResponseFinalised = user.isResponseFinalised();
		if (isResponseFinalised) {
		    // since the response is finalised present a screen which can not be edited
		    voteLearningForm.setReportViewOnly(new Boolean(true).toString());
		    voteGeneralLearnerFlowDTO.setReportViewOnly(new Boolean(true).toString());
		}

		Set<String> userAttempts = voteService.getAttemptsForUserAndSessionUseOpenAnswer(user.getUid(),
			sessionUid);
		request.setAttribute(VoteAppConstants.LIST_GENERAL_CHECKED_OPTIONS_CONTENT, userAttempts);

		voteService.prepareChartData(request, voteContent.getVoteContentId(), voteSession.getUid(),
			voteGeneralLearnerFlowDTO);

		String isContentLockOnFinish = voteLearningForm.getLockOnFinish();
		if (isContentLockOnFinish.equals(new Boolean(true).toString()) && isResponseFinalised == true) {
		    // user with session id: userSessionId should not redo votes. session is locked
		    return "/learning/ExitLearning";
		}

		voteLearningForm.setRevisitingUser(new Boolean(true).toString());
		voteGeneralLearnerFlowDTO.setRevisitingUser(new Boolean(true).toString());
		request.setAttribute(VoteAppConstants.VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

		if (isContentLockOnFinish.equals(new Boolean(false).toString()) && isResponseFinalised == true) {
		    // isContentLockOnFinish is false, enable redo of votes
		    return "/learning/RevisitedAllNominations";
		}

		return "/learning/AllNominations";
	    }
	}
	// presenting standard learner screen..
	return "/learning/AnswersContent";
    }

    /**
     * Build the attempts map and put it in the request, based on the supplied user. If the user is null then the map
     * will be set but it will be empty TODO This shouldn't go in the request, it should go in our special session map.
     */
    private void putMapQuestionsContentIntoRequest(HttpServletRequest request, IVoteService voteService,
	    VoteQueUsr user) {
	List<VoteUsrAttempt> attempts = null;
	if (user != null) {
	    attempts = voteService.getAttemptsForUser(user.getUid());
	}
	Map<String, String> localMapQuestionsContent = new TreeMap<>(new VoteComparator());

	if (attempts != null) {

	    Iterator<VoteUsrAttempt> listIterator = attempts.iterator();
	    int order = 0;
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = listIterator.next();
		VoteQueContent voteQueContent = attempt.getVoteQueContent();
		order++;
		if (voteQueContent != null) {
		    String entry = voteQueContent.getQuestion();

		    String questionUid = attempt.getVoteQueContent().getUid().toString();
		    if (entry != null) {
			if (entry.equals("sample nomination") && questionUid.equals("1")) {
			    localMapQuestionsContent.put(new Integer(order).toString(), attempt.getUserEntry());
			} else {
			    localMapQuestionsContent.put(new Integer(order).toString(), voteQueContent.getQuestion());
			}
		    }
		}
	    }
	}

	request.setAttribute(VoteAppConstants.MAP_GENERAL_CHECKED_OPTIONS_CONTENT, localMapQuestionsContent);
    }

    /**
     * sets up session scope attributes based on content linked to the passed tool session id
     * setupAttributes(HttpServletRequest request, VoteContent voteContent)
     *
     * @param request
     * @param voteContent
     */
    private static void setupAttributes(HttpServletRequest request, VoteContent voteContent,
	    VoteLearningForm voteLearningForm, VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO) {

	Map<String, String> mapGeneralCheckedOptionsContent = new TreeMap<>(new VoteComparator());
	request.setAttribute(VoteAppConstants.MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);

	voteLearningForm.setActivityTitle(voteContent.getTitle());
	voteLearningForm.setActivityInstructions(voteContent.getInstructions());
	voteLearningForm.setMaxNominationCount(voteContent.getMaxNominationCount());
	voteLearningForm.setMinNominationCount(voteContent.getMinNominationCount());
	voteLearningForm.setUseSelectLeaderToolOuput(new Boolean(voteContent.isUseSelectLeaderToolOuput()).toString());
	voteLearningForm.setAllowTextEntry(new Boolean(voteContent.isAllowText()).toString());
	voteLearningForm.setShowResults(new Boolean(voteContent.isShowResults()).toString());
	voteLearningForm.setLockOnFinish(new Boolean(voteContent.isLockOnFinish()).toString());

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());
	voteGeneralLearnerFlowDTO.setMaxNominationCount(voteContent.getMaxNominationCount());
	voteGeneralLearnerFlowDTO.setMinNominationCount(voteContent.getMinNominationCount());
	voteGeneralLearnerFlowDTO
		.setUseSelectLeaderToolOuput(new Boolean(voteContent.isUseSelectLeaderToolOuput()).toString());
	voteGeneralLearnerFlowDTO.setAllowTextEntry(new Boolean(voteContent.isAllowText()).toString());
	voteGeneralLearnerFlowDTO.setLockOnFinish(new Boolean(voteContent.isLockOnFinish()).toString());
	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());
    }

    private static boolean validateParameters(HttpServletRequest request, VoteLearningForm voteLearningForm) {
	/*
	 * obtain and setup the current user's data
	 */

	String userID = "";
	HttpSession ss = SessionManager.getSession();

	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null && user.getUserID() != null) {
		userID = user.getUserID().toString();
		logger.info("User Id : " + userID);
		voteLearningForm.setUserID(userID);
	    }
	}

	/*
	 * process incoming tool session id and later derive toolContentId from it.
	 */
	String strToolSessionId = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	long toolSessionID = 0;
	if (strToolSessionId == null || strToolSessionId.length() == 0) {

	    // persistInRequestError(request, "error.toolSessionId.required");
	    logger.error("error.toolSessionId.required");
	    return false;
	} else {
	    try {
		toolSessionID = new Long(strToolSessionId).longValue();
		voteLearningForm.setToolSessionID(new Long(toolSessionID).toString());
	    } catch (NumberFormatException e) {

		// persistInRequestError(request, "error.sessionId.numberFormatException");
		logger.error("add error.sessionId.numberFormatException to ActionMessages.");
		return false;
	    }
	}

	/* mode can be learner, teacher or author */
	String mode = request.getParameter(VoteAppConstants.MODE);

	if (mode == null || mode.length() == 0) {

	    logger.error("mode missing: ");
	    return false;
	}

	if (!mode.equals("learner") && !mode.equals("teacher") && !mode.equals("author")) {

	    logger.error("mode invalid: ");
	    return false;
	}
	voteLearningForm.setLearningMode(mode);

	return true;
    }

    private VoteQueUsr getCurrentUser(String toolSessionId) {

	// get back login user DTO
	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userId = new Long(toolUser.getUserID().longValue());

	VoteSession session = voteService.getSessionBySessionId(new Long(toolSessionId));
	VoteQueUsr user = voteService.getVoteUserBySession(userId, session.getUid());
	if (user == null) {
	    String userName = toolUser.getLogin();
	    String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();

	    user = new VoteQueUsr(userId, userName, fullName, session, new TreeSet<VoteUsrAttempt>());
	    voteService.createVoteQueUsr(user);
	}

	return user;
    }

    private VoteQueUsr getSpecifiedUser(String toolSessionId, Integer userId) {
	VoteSession session = voteService.getSessionBySessionId(new Long(toolSessionId));
	VoteQueUsr user = voteService.getVoteUserBySession(new Long(userId.intValue()), session.getUid());
	if (user == null) {
	    logger.error("Unable to find specified user for Vote activity. Screens are likely to fail. SessionId="
		    + new Long(toolSessionId) + " UserId=" + userId);
	}
	return user;
    }
}