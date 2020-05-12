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
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionItemDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieBurningQuestion;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.service.ScratchieApplicationException;
import org.lamsfoundation.lams.tool.scratchie.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

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
     */
    @RequestMapping("/start")
    private String start(HttpServletRequest request, HttpServletResponse response, @RequestParam Long toolSessionID)
	    throws ScratchieApplicationException {
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(toolSessionID);
	// get back the scratchie and item list and display them on page
	final Scratchie scratchie = scratchieService.getScratchieBySessionId(toolSessionID);
	boolean isReflectOnActivity = scratchie.isReflectOnActivity();

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

	// get notebook entry
	NotebookEntry notebookEntry = null;
	if (isReflectOnActivity && (groupLeader != null)) {
	    notebookEntry = scratchieService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ScratchieConstants.TOOL_SIGNATURE, groupLeader.getUserId().intValue());
	}
	String entryText = (notebookEntry == null) ? null : notebookEntry.getEntry();

	// basic information
	sessionMap.put(ScratchieConstants.ATTR_TITLE, scratchie.getTitle());
	sessionMap.put(ScratchieConstants.ATTR_RESOURCE_INSTRUCTION, scratchie.getInstructions());
	sessionMap.put(ScratchieConstants.ATTR_USER_ID, user.getUserId());
	sessionMap.put(ScratchieConstants.ATTR_USER_UID, user.getUid());
	String groupLeaderName = groupLeader.getFirstName() + " " + groupLeader.getLastName();
	sessionMap.put(ScratchieConstants.ATTR_GROUP_LEADER_NAME, groupLeaderName);
	sessionMap.put(ScratchieConstants.ATTR_GROUP_LEADER_USER_ID, groupLeader.getUserId());
	boolean isUserLeader = toolSession.isUserGroupLeader(user.getUid());
	sessionMap.put(ScratchieConstants.ATTR_IS_USER_LEADER, isUserLeader);
	boolean isUserFinished = (user != null) && user.isSessionFinished();
	sessionMap.put(ScratchieConstants.ATTR_USER_FINISHED, isUserFinished);
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionID);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(ScratchieConstants.ATTR_IS_BURNING_QUESTIONS_ENABLED, scratchie.isBurningQuestionsEnabled());
	// reflection information
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ON, isReflectOnActivity);
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_INSTRUCTION, scratchie.getReflectInstructions());
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ENTRY, entryText);
	// add all answer uids to one set
	if (isUserLeader) {
	    Set<Long> answerUids = new HashSet<>();
	    for (ScratchieItem item : scratchie.getScratchieItems()) {
		for (ScratchieAnswer answer : item.getAnswers()) {
		    answerUids.add(answer.getUid());
		}
	    }
	    sessionMap.put(ScratchieConstants.ATTR_ANSWER_UIDS, answerUids);
	}

	// add define later support
	if (scratchie.isDefineLater()) {
	    return "pages/learning/definelater";
	}

	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, scratchieService.isLastActivity(toolSessionID));

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

	Collection<ScratchieItem> items = storeItemsToSessionMap(toolSessionID, scratchie, sessionMap,
		mode.isTeacher());

	sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	// calculate max score
	int maxScore = scratchieService.getMaxPossibleScore(scratchie);
	sessionMap.put(ScratchieConstants.ATTR_MAX_SCORE, maxScore);

	boolean isScratchingFinished = toolSession.isScratchingFinished();
	boolean isWaitingForLeaderToSubmitNotebook = isReflectOnActivity && (notebookEntry == null);
	boolean isShowResults = (isScratchingFinished && !isWaitingForLeaderToSubmitNotebook) && !mode.isTeacher();

	// show notebook page to the leader
	if (isUserLeader && isScratchingFinished && isWaitingForLeaderToSubmitNotebook) {

	    String redirectURL = "redirect:/learning/newReflection.do";
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, ScratchieConstants.ATTR_SESSION_MAP_ID,
		    sessionMap.getSessionID());
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, AttributeNames.ATTR_MODE, mode.toString());
	    return redirectURL;

	    // show results page
	} else if (isShowResults) {

	    String redirectURL = "redirect:showResults.do";
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, ScratchieConstants.ATTR_SESSION_MAP_ID,
		    sessionMap.getSessionID());
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, AttributeNames.ATTR_MODE, mode.toString());
	    return redirectURL;

	    // show learning.jsp page
	} else {

	    // time limit feature
	    boolean isTimeLimitEnabled = isUserLeader && !isScratchingFinished && scratchie.getTimeLimit() != 0;
	    boolean isTimeLimitNotLaunched = toolSession.getTimeLimitLaunchedDate() == null;
	    long secondsLeft = 1;
	    if (isTimeLimitEnabled) {
		// if user has pressed OK button already - calculate remaining time, and full time otherwise
		secondsLeft = isTimeLimitNotLaunched ? scratchie.getTimeLimit() * 60
			: scratchie.getTimeLimit() * 60
				- (System.currentTimeMillis() - toolSession.getTimeLimitLaunchedDate().getTime())
					/ 1000;
		// change negative number or zero to 1 so it can autosubmit results
		secondsLeft = Math.max(1, secondsLeft);
	    }
	    request.setAttribute(ScratchieConstants.ATTR_IS_TIME_LIMIT_ENABLED, isTimeLimitEnabled);
	    request.setAttribute(ScratchieConstants.ATTR_IS_TIME_LIMIT_NOT_LAUNCHED, isTimeLimitNotLaunched);
	    request.setAttribute(ScratchieConstants.ATTR_SECONDS_LEFT, secondsLeft);

	    // in case we can't show learning.jsp to non-leaders forward them to the waitForLeaderTimeLimit page
	    if (!isUserLeader && scratchie.getTimeLimit() != 0 && !mode.isTeacher()) {

		// show waitForLeaderLaunchTimeLimit page if the leader hasn't started activity or hasn't pressed OK
		// button to launch time limit
		if (toolSession.getTimeLimitLaunchedDate() == null) {
		    request.setAttribute(ScratchieConstants.ATTR_WAITING_MESSAGE_KEY,
			    "label.waiting.for.leader.launch.time.limit");
		    return "pages/learning/waitForLeaderTimeLimit";
		}

		// check if the time limit is exceeded
		boolean isTimeLimitExceeded = toolSession.getTimeLimitLaunchedDate().getTime()
			+ scratchie.getTimeLimit() * 60000 < System.currentTimeMillis();

		// if the time limit is over and the leader hasn't submitted notebook or burning questions (thus
		// non-leaders should wait) - show waitForLeaderFinish page
		if (isTimeLimitExceeded && isWaitingForLeaderToSubmitNotebook) {
		    request.setAttribute(ScratchieConstants.ATTR_WAITING_MESSAGE_KEY,
			    "label.waiting.for.leader.submit.notebook");
		    return "pages/learning/waitForLeaderTimeLimit";
		}
	    }

	    if (mode.isTeacher()) {
		scratchieService.populateScratchieItemsWithMarks(scratchie, items, toolSessionID);
		// get updated score from ScratchieSession
		int score = toolSession.getMark();
		request.setAttribute(ScratchieConstants.ATTR_SCORE, score);
		int percentage = (maxScore == 0) ? 0 : ((score * 100) / maxScore);
		request.setAttribute(ScratchieConstants.ATTR_SCORE_PERCENTAGE, percentage);
	    }
	    
	    sessionMap.put(ScratchieConstants.ATTR_IS_SCRATCHING_FINISHED, isScratchingFinished);
	    // make non-leaders wait for notebook to be submitted, if required
	    sessionMap.put(ScratchieConstants.ATTR_IS_WAITING_FOR_LEADER_TO_SUBMIT_NOTEBOOK,
		    isWaitingForLeaderToSubmitNotebook);

	    boolean questionEtherpadEnabled = scratchie.isQuestionEtherpadEnabled()
		    && StringUtils.isNotBlank(Configuration.get(ConfigurationKeys.ETHERPAD_API_KEY));
	    request.setAttribute(ScratchieConstants.ATTR_IS_QUESTION_ETHERPAD_ENABLED, questionEtherpadEnabled);
	    if (questionEtherpadEnabled && scratchieService.isGroupedActivity(scratchie.getContentId())) {
		// get all users from the group, even if they did not reach the Scratchie yet
		// order them by first and last name
		Collection<User> allGroupUsers = scratchieService.getAllGroupUsers(toolSessionID).stream()
			.sorted(Comparator.comparing(u -> u.getFirstName() + u.getLastName()))
			.collect(Collectors.toList());
		request.setAttribute(ScratchieConstants.ATTR_ALL_GROUP_USERS, allGroupUsers);
	    }

	    return "pages/learning/learning";
	}

    }

    /**
     * Stores into session map all data needed to display scratchies and answers
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

	// for teacher in monitoring display the number of attempt.
	if (showOrder) {
	    scratchieService.getScratchesOrder(items, toolSessionId);
	}

	//display confidence levels
	if (scratchie.isConfidenceLevelsEnabled()) {
	    scratchieService.populateItemsWithConfidenceLevels((Long) sessionMap.get(ScratchieConstants.ATTR_USER_ID),
		    toolSessionId, scratchie.getConfidenceLevelsActivityUiid(), items);
	}

	// populate items with the existing burning questions for displaying purposes
	List<ScratchieBurningQuestion> burningQuestions = null;
	if (scratchie.isBurningQuestionsEnabled()) {

	    burningQuestions = scratchieService.getBurningQuestionsBySession(toolSessionId);
	    for (ScratchieItem item : items) {

		// find corresponding burningQuestion
		String question = "";
		for (ScratchieBurningQuestion burningQuestion : burningQuestions) {
		    if (!burningQuestion.isGeneralQuestion()
			    && burningQuestion.getScratchieItem().getUid().equals(item.getUid())) {
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
     * Record in DB that leader has scratched specified answer. And return whether scratchie answer is correct or not.
     */
    @RequestMapping("/recordItemScratched")
    @ResponseStatus(HttpStatus.OK)
    private void recordItemScratched(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ScratchieApplicationException {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	final Long answerUid = NumberUtils.createLong(request.getParameter(ScratchieConstants.PARAM_ANSWER_UID));

	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(toolSessionId);

	ScratchieUser leader = getCurrentUser(toolSessionId);
	// only leader is allowed to scratch answers
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return;
	}

	// check answer is belong to current session
	Set<Long> answerUids = (Set<Long>) sessionMap.get(ScratchieConstants.ATTR_ANSWER_UIDS);
	if (!answerUids.contains(answerUid)) {
	    return;
	}

	// Return whether scratchie answer is correct or not
	ScratchieAnswer answer = scratchieService.getScratchieAnswerByUid(answerUid);
	if (answer == null) {
	    return;
	}

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put(ScratchieConstants.ATTR_ANSWER_CORRECT, answer.isCorrect());
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(ObjectNode);

	// create a new thread to record item scratched (in order to do this task in parallel not to slow down sending
	// response back)
	Thread recordItemScratchedThread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		scratchieService.recordItemScratched(toolSessionId, answerUid);
	    }
	}, "LAMS_recordItemScratched_thread");
	recordItemScratchedThread.start();
    }

    /**
     * Stores date when user has started activity with time limit.
     */
    @RequestMapping("/launchTimeLimit")
    @ResponseStatus(HttpStatus.OK)
    private void launchTimeLimit(HttpServletRequest request) throws ScratchieApplicationException, SchedulerException {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(toolSessionId);

	ScratchieUser leader = getCurrentUser(toolSessionId);
	// only leader is allowed to launch time limit
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return;
	}

	scratchieService.launchTimeLimit(toolSessionId);
    }

    /**
     * Displays results page. When leader gets to this page, scratchingFinished column is set to true for all users.
     */
    @RequestMapping("/showResults")
    private String showResults(HttpServletRequest request) throws ScratchieApplicationException, IOException {
	// get back SessionMap
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	boolean isReflectOnActivity = (Boolean) sessionMap.get(ScratchieConstants.ATTR_REFLECTION_ON);
	boolean isBurningQuestionsEnabled = (Boolean) sessionMap
		.get(ScratchieConstants.ATTR_IS_BURNING_QUESTIONS_ENABLED);

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
	}

	// get updated score from ScratchieSession
	int score = toolSession.getMark();
	request.setAttribute(ScratchieConstants.ATTR_SCORE, score);
	int maxScore = (Integer) sessionMap.get(ScratchieConstants.ATTR_MAX_SCORE);
	int percentage = (maxScore == 0) ? 0 : ((score * 100) / maxScore);
	request.setAttribute(ScratchieConstants.ATTR_SCORE_PERCENTAGE, percentage);

	// display other groups' BurningQuestions
	if (isBurningQuestionsEnabled) {
	    List<BurningQuestionItemDTO> burningQuestionItemDtos = scratchieService.getBurningQuestionDtos(scratchie,
		    toolSessionId, false, true);
	    request.setAttribute(ScratchieConstants.ATTR_BURNING_QUESTION_ITEM_DTOS, burningQuestionItemDtos);
	}

	// display other groups' notebooks
	if (isReflectOnActivity) {
	    List<ReflectDTO> reflections = scratchieService
		    .getReflectionList(toolSession.getScratchie().getContentId());

	    // remove current session leader reflection
	    Iterator<ReflectDTO> refIterator = reflections.iterator();
	    while (refIterator.hasNext()) {
		ReflectDTO reflection = refIterator.next();
		if (toolSession.getSessionName().equals(reflection.getGroupName())) {

		    // store for displaying purposes
		    String reflectEntry = StringEscapeUtils.unescapeJavaScript(reflection.getReflection());
		    sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ENTRY, reflectEntry);

		    // remove from list to display other groups' notebooks
		    refIterator.remove();
		    break;
		}
	    }

	    request.setAttribute(ScratchieConstants.ATTR_REFLECTIONS, reflections);
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
	if (StringUtils.isEmpty(request.getParameter(ScratchieConstants.ATTR_ITEM_UID))
		|| StringUtils.isEmpty(request.getParameter(ScratchieConstants.PARAM_SESSION_ID))) {
	    return;
	}

	Long itemUid = WebUtil.readLongParam(request, ScratchieConstants.ATTR_ITEM_UID) == 0 ? null
		: WebUtil.readLongParam(request, ScratchieConstants.ATTR_ITEM_UID);
	Long sessionId = WebUtil.readLongParam(request, ScratchieConstants.PARAM_SESSION_ID);
	String question = request.getParameter(ScratchieConstants.ATTR_BURNING_QUESTION_PREFIX);
	scratchieService.saveBurningQuestion(sessionId, itemUid, question);
    }

    @RequestMapping("/like")
    @ResponseStatus(HttpStatus.OK)
    private void like(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException, ScratchieApplicationException {

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(sessionId);

	Long burningQuestionUid = WebUtil.readLongParam(request, ScratchieConstants.PARAM_BURNING_QUESTION_UID);

	ScratchieUser leader = this.getCurrentUser(sessionId);
	// only leader is allowed to scratch answers
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

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(sessionId);

	Long burningQuestionUid = WebUtil.readLongParam(request, ScratchieConstants.PARAM_BURNING_QUESTION_UID);

	ScratchieUser leader = this.getCurrentUser(sessionId);
	// only leader is allowed to scratch answers
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
    private String finish(HttpServletRequest request) {
	// get back SessionMap
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	final Long userId = user.getUserID().longValue();

	String nextActivityUrl = null;

	try {
	    nextActivityUrl = scratchieService.finishToolSession(toolSessionId, userId);

	    request.setAttribute(ScratchieConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (ScratchieApplicationException e) {
	    log.error("Failed get next activity url:" + e.getMessage());
	}
	return "pages/learning/finish";
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

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	Collection<ScratchieItem> items = (Collection<ScratchieItem>) sessionMap.get(ScratchieConstants.ATTR_ITEM_LIST);

	for (ScratchieItem item : items) {
	    final Long itemUid = item.getUid();

	    final String burningQuestion = request
		    .getParameter(ScratchieConstants.ATTR_BURNING_QUESTION_PREFIX + itemUid);
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

    /**
     * Display empty reflection form.
     */
    @RequestMapping("/newReflection")
    private String newReflection(@ModelAttribute("reflectionForm") ReflectionForm reflectionForm,
	    HttpServletRequest request) throws ScratchieApplicationException, IOException {

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	Long userUid = (Long) sessionMap.get(ScratchieConstants.ATTR_USER_UID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	reflectionForm.setUserID(user.getUserID());
	reflectionForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	NotebookEntry entry = scratchieService.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ScratchieConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    reflectionForm.setEntryText(entry.getEntry());
	}

	ScratchieSession toolSession = scratchieService.getScratchieSessionBySessionId(toolSessionId);
	Scratchie scratchie = toolSession.getScratchie();

	//handle burning questions saving if needed
	if (toolSession.isUserGroupLeader(userUid) && scratchie.isBurningQuestionsEnabled()
		&& !toolSession.isScratchingFinished()) {
	    saveBurningQuestions(request);
	}

	// in case of the leader we should let all other learners see Next Activity button
	if (toolSession.isUserGroupLeader(userUid) && !toolSession.isScratchingFinished()) {
	    scratchieService.setScratchingFinished(toolSessionId);
	}

	return "pages/learning/notebook";
    }

    /**
     * Submit reflection form input database. Only leaders can submit reflections.
     */
    @RequestMapping("/submitReflection")
    private String submitReflection(@ModelAttribute("reflectionForm") ReflectionForm reflectionForm,
	    HttpServletRequest request) throws ScratchieApplicationException {
	final Integer userId = reflectionForm.getUserID();
	final String entryText = reflectionForm.getEntryText();

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	final NotebookEntry entry = scratchieService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ScratchieConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    scratchieService.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ScratchieConstants.TOOL_SIGNATURE, userId, entryText);
	} else {
	    // update existing entry
	    entry.setEntry(entryText);
	    entry.setLastModified(new Date());
	    scratchieService.updateEntry(entry);
	}
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ENTRY, entryText);

	String redirectURL = "redirect:showResults.do";
	redirectURL = WebUtil.appendParameterToURL(redirectURL, ScratchieConstants.ATTR_SESSION_MAP_ID,
		sessionMap.getSessionID());
	return redirectURL;
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
}