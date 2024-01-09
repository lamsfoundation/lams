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

package org.lamsfoundation.lams.tool.scratchie.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.QbConstants;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionItemDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.OptionDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieBurningQuestion;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieConfigItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.service.ScratchieApplicationException;
import org.lamsfoundation.lams.tool.scratchie.service.ScratchieServiceImpl;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/learning")
public class LearningController {
    private static Logger log = Logger.getLogger(LearningController.class);

    @Autowired
    private IScratchieService scratchieService;

    /**
     * Read scratchie data from database and put them into HttpSession.
     *
     * @throws IOException
     */
    @RequestMapping("/start")
    private String start(HttpServletRequest request, HttpServletResponse response, @RequestParam Long toolSessionID)
	    throws ScratchieApplicationException, IOException {

	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(toolSessionID);
	// get back the scratchie and item list and display them on page
	final Scratchie scratchie = scratchieService.getScratchieBySessionId(toolSessionID);

	final ScratchieUser user;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // scratchieUser may be null if the user was force completed.
	    user = getSpecifiedUser(toolSessionID, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(toolSessionID);
	}

	ScratchieUser groupLeader = scratchieService.checkLeaderSelectToolForSessionLeader(user, toolSessionID);

	// forwards to the leaderSelection page
	if (groupLeader == null) {

	    // get group users and store it to request as DTO objects
	    List<ScratchieUser> groupUsers = scratchieService.getUsersBySession(toolSessionID);
	    List<User> groupUserDtos = new ArrayList<>();
	    for (ScratchieUser groupUser : groupUsers) {
		User groupUserDto = new User();
		groupUserDto.setUserId(groupUser.getUserId().intValue());
		groupUserDto.setFirstName(groupUser.getFirstName());
		groupUserDto.setLastName(groupUser.getLastName());
		groupUserDtos.add(groupUserDto);
	    }
	    request.setAttribute(ScratchieConstants.ATTR_GROUP_USERS, groupUserDtos);
	    request.setAttribute(ScratchieConstants.PARAM_TOOL_SESSION_ID, toolSessionID);
	    request.setAttribute(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	    request.setAttribute(AttributeNames.ATTR_MODE, mode);
	    return "pages/learning/waitforleader";
	}

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// basic information
	sessionMap.put(ScratchieConstants.ATTR_TITLE, scratchie.getTitle());
	sessionMap.put(ScratchieConstants.ATTR_RESOURCE_INSTRUCTION, scratchie.getInstructions());
	sessionMap.put(ScratchieConstants.ATTR_USER_ID, user.getUserId());
	sessionMap.put(ScratchieConstants.ATTR_USER_UID, user.getUid());
	String groupLeaderName = groupLeader.getFullName();
	sessionMap.put(ScratchieConstants.ATTR_GROUP_LEADER_NAME, groupLeaderName);
	sessionMap.put(ScratchieConstants.ATTR_GROUP_LEADER_USER_ID, groupLeader.getUserId());
	boolean isUserLeader = toolSession.isUserGroupLeader(user.getUid());
	sessionMap.put(ScratchieConstants.ATTR_IS_USER_LEADER, isUserLeader);
	boolean isUserFinished = (user != null) && user.isSessionFinished();
	sessionMap.put(ScratchieConstants.ATTR_USER_FINISHED, isUserFinished);
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionID);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(ScratchieConstants.ATTR_IS_BURNING_QUESTIONS_ENABLED, scratchie.isBurningQuestionsEnabled());
	// add all option uids to one set
	if (isUserLeader) {
	    Set<Long> optionUids = new HashSet<>();
	    for (ScratchieItem item : scratchie.getScratchieItems()) {
		for (QbOption option : item.getQbQuestion().getQbOptions()) {
		    optionUids.add(option.getUid());
		}
	    }
	    sessionMap.put(ScratchieConstants.ATTR_OPTION_UIDS, optionUids);
	}

	ScratchieConfigItem hideTitles = scratchieService.getConfigItem(ScratchieConfigItem.KEY_HIDE_TITLES);
	sessionMap.put(ScratchieConfigItem.KEY_HIDE_TITLES, Boolean.valueOf(hideTitles.getConfigValue()));

	// add define later support
	if (scratchie.isDefineLater()) {
	    return "pages/learning/definelater";
	}

	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, scratchieService.isLastActivity(toolSessionID));

	Collection<ScratchieItem> items = storeItemsToSessionMap(toolSessionID, scratchie, sessionMap,
		mode.isTeacher());

	sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	// calculate max score
	double maxScore = scratchieService.getMaxPossibleScore(scratchie);
	sessionMap.put(ScratchieConstants.ATTR_MAX_SCORE, maxScore);

	boolean isScratchingFinished = toolSession.isScratchingFinished();
	boolean isShowResults = isScratchingFinished && !mode.isTeacher();

	// check if there is submission deadline
	Date submissionDeadline = scratchie.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    // store submission deadline to sessionMap
	    sessionMap.put(ScratchieConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);

	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return "pages/learning/submissionDeadline";
	    }
	}

	// show results page
	if (isShowResults) {
	    String redirectURL = "redirect:showResults.do";
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, ScratchieConstants.ATTR_SESSION_MAP_ID,
		    sessionMap.getSessionID());
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, AttributeNames.ATTR_MODE, mode.toString());
	    return redirectURL;

	}

	// check time limits
	if ((scratchie.getRelativeTimeLimit() != 0 || scratchie.getAbsoluteTimeLimit() != 0
		|| scratchie.getAbsoluteTimeLimitFinish() != null) && !mode.isTeacher()) {

	    // show waitForLeaderLaunchTimeLimit page if the leader hasn't started activity
	    if (!isUserLeader && toolSession.getTimeLimitLaunchedDate() == null) {
		request.setAttribute(ScratchieConstants.ATTR_WAITING_MESSAGE_KEY,
			"label.waiting.for.leader.launch.time.limit");
		return "pages/learning/waitForLeaderTimeLimit";
	    }

	    // check if the time limit is exceeded
	    boolean isTimeLimitExceeded = scratchieService.checkTimeLimitExceeded(scratchie.getContentId(),
		    groupLeader.getUserId().intValue());

	    if (isTimeLimitExceeded) {
		// first learner, even non-leader, who detects that scratching is finished (probably after time expired refresh)
		// marks scratching as finished (but does not send refresh to everyone as they are already refreshing on time expired)
		if (!isScratchingFinished) {
		    scratchieService.setScratchingFinished(toolSessionID);
		}

		// go through whole method again, with new settings
		return "forward:start.do";

	    }
	}

	// show learning.jsp page
	if (mode.isTeacher()) {
	    scratchieService.populateScratchieItemsWithMarks(scratchie, items, toolSessionID);
	    // get updated score from ScratchieSession
	    double score = toolSession.getMark();
	    request.setAttribute(ScratchieConstants.ATTR_SCORE, score);
	    int percentage = (maxScore == 0) ? 0 : Double.valueOf((score * 100) / maxScore).intValue();
	    request.setAttribute(ScratchieConstants.ATTR_SCORE_PERCENTAGE, percentage);
	}

	sessionMap.put(ScratchieConstants.ATTR_IS_SCRATCHING_FINISHED, isScratchingFinished);

	boolean questionEtherpadEnabled = scratchie.isQuestionEtherpadEnabled() && StringUtils.isNotBlank(
		Configuration.get(ConfigurationKeys.ETHERPAD_API_KEY));
	request.setAttribute(ScratchieConstants.ATTR_IS_QUESTION_ETHERPAD_ENABLED, questionEtherpadEnabled);
	if (questionEtherpadEnabled && scratchieService.isGroupedActivity(scratchie.getContentId())) {
	    // get all users from the group, even if they did not reach the Scratchie yet
	    // order them by first and last name
	    Collection<User> allGroupUsers = scratchieService.getAllGroupUsers(toolSessionID).stream().sorted()
		    .collect(Collectors.toList());
	    request.setAttribute(ScratchieConstants.ATTR_ALL_GROUP_USERS, allGroupUsers);
	}

	return "pages/learning/learning";

    }

    /**
     * Stores into session map all data needed to display scratchies and options
     */
    private Collection<ScratchieItem> storeItemsToSessionMap(Long toolSessionId, Scratchie scratchie,
	    SessionMap<String, Object> sessionMap, boolean showOrder) {
	// set scratched flag for display purpose
	Collection<ScratchieItem> items = scratchieService.getItemsWithIndicatedScratches(toolSessionId);

	// shuffling items
	if (scratchie.isShuffleItems()) {
	    //items is a Set at this moment
	    ArrayList<ScratchieItem> shuffledItems = new ArrayList<>(items);
	    //use random with a seed so people from the same group get the "same shuffle"
	    Random randomGenerator = new Random(toolSessionId);
	    Collections.shuffle(shuffledItems, randomGenerator);
	    items = shuffledItems;
	}

	//display confidence levels
	if (scratchie.isConfidenceLevelsEnabled()) {
	    scratchieService.populateItemsWithConfidenceLevels((Long) sessionMap.get(ScratchieConstants.ATTR_USER_ID),
		    toolSessionId, scratchie.getConfidenceLevelsActivityUiid(), items);
	}

	// for teacher in monitoring display the number of attempt.
	if (showOrder) {
	    scratchieService.getScratchesOrder(items, toolSessionId);
	}

	// populate items with the existing burning questions for displaying purposes
	List<ScratchieBurningQuestion> burningQuestions = null;
	if (scratchie.isBurningQuestionsEnabled()) {

	    burningQuestions = scratchieService.getBurningQuestionsBySession(toolSessionId);
	    for (ScratchieItem item : items) {

		// find corresponding burningQuestion
		String question = "";
		for (ScratchieBurningQuestion burningQuestion : burningQuestions) {
		    if (!burningQuestion.isGeneralQuestion() && burningQuestion.getScratchieItem().getUid()
			    .equals(item.getUid())) {
			question = burningQuestion.getQuestion();
			break;
		    }
		}
		item.setBurningQuestion(question);
	    }

	    // find general burning question
	    String generalBurningQuestion = "";
	    for (ScratchieBurningQuestion burningQuestion : burningQuestions) {
		if (burningQuestion.isGeneralQuestion()) {
		    generalBurningQuestion = burningQuestion.getQuestion();
		    break;
		}
	    }
	    sessionMap.put(ScratchieConstants.ATTR_GENERAL_BURNING_QUESTION, generalBurningQuestion);
	}

	sessionMap.put(ScratchieConstants.ATTR_ITEM_LIST, items);
	return items;
    }

    /**
     * Record in DB that leader has scratched specified option. And return whether scratchie option is correct or not.
     */
    @RequestMapping("/recordItemScratched")
    @ResponseStatus(HttpStatus.OK)
    private void recordItemScratched(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ScratchieApplicationException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	final Long optionUid = NumberUtils.createLong(request.getParameter(ScratchieConstants.PARAM_OPTION_UID));
	final Long itemUid = NumberUtils.createLong(request.getParameter(ScratchieConstants.PARAM_ITEM_UID));

	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(toolSessionId);

	ScratchieUser leader = getCurrentUser(toolSessionId);
	// only leader is allowed to scratch options
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return;
	}

	// check option is belong to current session
	Set<Long> optionUids = (Set<Long>) sessionMap.get(ScratchieConstants.ATTR_OPTION_UIDS);
	if (!optionUids.contains(optionUid)) {
	    return;
	}

	// Return whether option is correct or not
	QbOption option = scratchieService.getQbOptionByUid(optionUid);
	if (option == null) {
	    return;
	}

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put(QbConstants.ATTR_OPTION_CORRECT, option.isCorrect());
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(ObjectNode);

	// create a new thread to record item scratched (in order to do this task in parallel not to slow down sending
	// response back)
	Thread recordItemScratchedThread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		scratchieService.recordItemScratched(toolSessionId, itemUid, optionUid);
	    }
	}, "LAMS_recordItemScratched_thread");
	recordItemScratchedThread.start();
    }

    /**
     * Record in DB that leader has provided this answer. And return whether it's correct or not.
     */
    @RequestMapping("/recordVsaAnswer")
    @ResponseBody
    public String recordVsaAnswer(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ScratchieApplicationException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	final Long itemUid = NumberUtils.createLong(request.getParameter(ScratchieConstants.PARAM_ITEM_UID));
	final String answer = request.getParameter("answer");

	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(toolSessionId);
	ScratchieUser leader = getCurrentUser(toolSessionId);
	ScratchieItem item = scratchieService.getScratchieItemByUid(itemUid);
	final boolean isCaseSensitive = item.getQbQuestion().isCaseSensitive();

	// only leader is allowed to answer
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return null;
	}

	// return whether option is correct or not
	boolean isAnswerCorrect = ScratchieServiceImpl.isItemUnraveled(item, answer) != null;

	// return whether such answer was already logged (and also the answer hash which was logged previously)
	int loggedAnswerHash = -1;
	//1) search for the answer in logs stored in SessionMap
	Collection<ScratchieItem> items = (Collection<ScratchieItem>) sessionMap.get(ScratchieConstants.ATTR_ITEM_LIST);
	for (ScratchieItem itemIter : items) {
	    if (itemIter.getUid().equals(itemUid)) {
		for (OptionDTO optionDto : itemIter.getOptionDtos()) {
		    boolean isAnswerMatched = isCaseSensitive
			    ? optionDto.getAnswer().equals(answer)
			    : optionDto.getAnswer().equalsIgnoreCase(answer);
		    if (isAnswerMatched) {
			loggedAnswerHash = optionDto.getAnswer().hashCode();
			break;
		    }
		}
		break;
	    }
	}
	//2) search in DB logs, as SessionMap might not be updated after user answered and didn't refresh the page
	if (loggedAnswerHash == -1) {
	    ScratchieAnswerVisitLog log = scratchieService.getLog(toolSessionId, itemUid, isCaseSensitive, answer);
	    if (log != null) {
		loggedAnswerHash = log.getAnswer().hashCode();
	    }
	}

	ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
	objectNode.put("isAnswerCorrect", isAnswerCorrect);
	objectNode.put("loggedAnswerHash", loggedAnswerHash);

	// create a new thread to record item scratched (in order to do this task in parallel not to slow down sending
	// response back)
	Thread recordVsaAnswerThread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		scratchieService.recordVsaAnswer(toolSessionId, itemUid, isCaseSensitive, answer);
	    }
	}, "LAMS_recordVsaAnswer_thread");
	recordVsaAnswerThread.start();

	response.setContentType("application/json;charset=utf-8");
	return objectNode.toString();
    }

    @RequestMapping("/vsaAutocomplete")
    @ResponseBody
    public String vsaAutocomplete(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam Long itemUid) {
	String userAnswer = WebUtil.readStrParam(request, "term", true);
	ScratchieItem item = scratchieService.getScratchieItemByUid(itemUid);
	QbQuestion qbQuestion = item.getQbQuestion();

	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	if (StringUtils.isNotBlank(userAnswer)) {
	    userAnswer = userAnswer.trim();
	    userAnswer = qbQuestion.isCaseSensitive() ? userAnswer : userAnswer.toLowerCase();

	    for (QbOption option : qbQuestion.getQbOptions()) {

		//filter out options not starting with 'term' and containing '*'
		String optionTitle = qbQuestion.isCaseSensitive() ? option.getName() : option.getName().toLowerCase();
		int i = 0;
		for (String optionAnswer : optionTitle.split("\\r\\n")) {
		    if (optionAnswer.startsWith(userAnswer) && !optionAnswer.contains("*")) {
			ObjectNode optionJSON = JsonNodeFactory.instance.objectNode();
			String originalOptionAnswer = option.getName().split("\\r\\n")[i];
			optionJSON.put("label", originalOptionAnswer);
			responseJSON.add(optionJSON);
		    }
		    i++;
		}
	    }
	}
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Displays results page. When leader gets to this page, scratchingFinished column is set to true for all users.
     */
    @RequestMapping("/showResults")
    public String showResults(HttpServletRequest request) throws ScratchieApplicationException, IOException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	boolean isBurningQuestionsEnabled = (Boolean) sessionMap.get(
		ScratchieConstants.ATTR_IS_BURNING_QUESTIONS_ENABLED);

	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(toolSessionId);
	Scratchie scratchie = toolSession.getScratchie();
	Long userUid = (Long) sessionMap.get(ScratchieConstants.ATTR_USER_UID);

	//handle burning questions saving if needed
	if (toolSession.isUserGroupLeader(userUid) && scratchie.isBurningQuestionsEnabled()
		&& !toolSession.isScratchingFinished()) {
	    saveBurningQuestions(request);
	}

	if (toolSession.isUserGroupLeader(userUid) && !toolSession.isScratchingFinished()) {
	    scratchieService.setScratchingFinished(toolSessionId);
	    // non-leaders need to go to results page
	    LearningWebsocketServer.getInstance().sendPageRefreshRequest(scratchie.getContentId(), toolSessionId);
	}

	// get updated score from ScratchieSession
	double score = toolSession.getMark();
	request.setAttribute(ScratchieConstants.ATTR_SCORE, score);
	double maxScore = (Double) sessionMap.get(ScratchieConstants.ATTR_MAX_SCORE);
	int percentage = (maxScore == 0) ? 0 : Double.valueOf((score * 100) / maxScore).intValue();
	request.setAttribute(ScratchieConstants.ATTR_SCORE_PERCENTAGE, percentage);

	// display other groups' BurningQuestions
	if (isBurningQuestionsEnabled) {
	    List<BurningQuestionItemDTO> burningQuestionItemDtos = scratchieService.getBurningQuestionDtos(scratchie,
		    toolSessionId, true, true);
	    boolean burningQuestionsAvailable = false;
	    for (BurningQuestionItemDTO burningQuestionItemDto : burningQuestionItemDtos) {
		if (!burningQuestionItemDto.getBurningQuestionDtos().isEmpty()) {
		    burningQuestionsAvailable = true;
		    break;
		}
	    }
	    if (burningQuestionsAvailable) {
		request.setAttribute(ScratchieConstants.ATTR_BURNING_QUESTION_ITEM_DTOS, burningQuestionItemDtos);
	    }
	}

	if (scratchie.isShowScrachiesInResults()) {
	    Collection<ScratchieItem> items = storeItemsToSessionMap(toolSessionId, scratchie, sessionMap, true);
	    scratchieService.populateScratchieItemsWithMarks(scratchie, items, toolSessionId);
	    request.setAttribute(ScratchieConstants.ATTR_SHOW_RESULTS, true);
	}

	return "pages/learning/results";
    }

    /**
     * Saves newly entered burning question. Used by jqGrid cellediting feature.
     */
    @RequestMapping("/editBurningQuestion")
    @ResponseStatus(HttpStatus.OK)
    private void editBurningQuestion(HttpServletRequest request) {
	if (StringUtils.isEmpty(request.getParameter(ScratchieConstants.ATTR_ITEM_UID)) || StringUtils.isEmpty(
		request.getParameter(ScratchieConstants.PARAM_SESSION_ID))) {
	    return;
	}

	Long itemUid = WebUtil.readLongParam(request, ScratchieConstants.ATTR_ITEM_UID) == 0
		? null
		: WebUtil.readLongParam(request, ScratchieConstants.ATTR_ITEM_UID);
	Long sessionId = WebUtil.readLongParam(request, ScratchieConstants.PARAM_SESSION_ID);
	String question = request.getParameter(ScratchieConstants.ATTR_BURNING_QUESTION_PREFIX);
	// this make is work with both plain HTML and HTML textfield
	question = question.replaceAll("\n", "\r\n");
	scratchieService.saveBurningQuestion(sessionId, itemUid, question);
    }

    @RequestMapping("/like")
    @ResponseStatus(HttpStatus.OK)
    private void like(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException, ScratchieApplicationException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(sessionId);

	Long burningQuestionUid = WebUtil.readLongParam(request, ScratchieConstants.PARAM_BURNING_QUESTION_UID);

	ScratchieUser leader = this.getCurrentUser(sessionId);
	// only leader is allowed to scratch options
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return;
	}

	boolean added = scratchieService.addLike(burningQuestionUid, sessionId);

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put("added", added);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(ObjectNode);
    }

    @RequestMapping("/removeLike")
    @ResponseStatus(HttpStatus.OK)
    private void removeLike(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException, ScratchieApplicationException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(sessionId);

	Long burningQuestionUid = WebUtil.readLongParam(request, ScratchieConstants.PARAM_BURNING_QUESTION_UID);

	ScratchieUser leader = this.getCurrentUser(sessionId);
	// only leader is allowed to scratch options
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return;
	}

	scratchieService.removeLike(burningQuestionUid, sessionId);

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put("added", true);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(ObjectNode);
    }

    /**
     * Finish learning session.
     */
    @RequestMapping("/finish")
    public void finish(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ScratchieApplicationException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	final Long userId = user.getUserID().longValue();

	String nextActivityUrl = scratchieService.finishToolSession(toolSessionId, userId);
	response.sendRedirect(nextActivityUrl);
    }

    /**
     * Autosaves burning questions. Only leaders can perform it.
     */
    @RequestMapping("/autosaveBurningQuestions")
    @ResponseStatus(HttpStatus.OK)
    private void autosaveBurningQuestions(HttpServletRequest request) throws ScratchieApplicationException {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// only leader is allowed to submit burning questions
	ScratchieUser leader = getCurrentUser(sessionId);
	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(sessionId);
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	}

	saveBurningQuestions(request);
    }

    /**
     * Saves burning questions entered by user. It also updates its values in SessionMap.
     */
    private void saveBurningQuestions(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	Collection<ScratchieItem> items = (Collection<ScratchieItem>) sessionMap.get(ScratchieConstants.ATTR_ITEM_LIST);

	for (ScratchieItem item : items) {
	    final Long itemUid = item.getUid();

	    final String burningQuestion = request.getParameter(
		    ScratchieConstants.ATTR_BURNING_QUESTION_PREFIX + itemUid);
	    if (StringUtils.isNotBlank(burningQuestion)) {
		// update question in sessionMap
		item.setBurningQuestion(burningQuestion);

		// update new entry
		scratchieService.saveBurningQuestion(sessionId, itemUid, burningQuestion);
	    }
	}

	// handle general burning question
	final String generalQuestion = request.getParameter(ScratchieConstants.ATTR_GENERAL_BURNING_QUESTION);
	if (StringUtils.isNotBlank(generalQuestion)) {
	    scratchieService.saveBurningQuestion(sessionId, null, generalQuestion);
	}
	// update general question in sessionMap
	sessionMap.put(ScratchieConstants.ATTR_GENERAL_BURNING_QUESTION, generalQuestion);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************

    private ScratchieUser getCurrentUser(Long sessionId) throws ScratchieApplicationException {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	ScratchieUser scratchieUser = scratchieService.getUserByIDAndSession(user.getUserID().longValue(), sessionId);

	if (scratchieUser == null) {
	    ScratchieSession session = scratchieService.getScratchieSessionBySessionId(sessionId);
	    final ScratchieUser newScratchieUser = new ScratchieUser(user, session);
	    scratchieService.createUser(newScratchieUser);

	    scratchieUser = newScratchieUser;
	}
	return scratchieUser;
    }

    private ScratchieUser getSpecifiedUser(Long sessionId, Integer userId) {
	ScratchieUser scratchieUser = scratchieService.getUserByIDAndSession(userId.longValue(), sessionId);
	if (scratchieUser == null) {
	    log.error("Unable to find specified user for scratchie activity. Screens are likely to fail. SessionId="
		    + sessionId + " UserId=" + userId);
	}
	return scratchieUser;
    }

    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    }
}