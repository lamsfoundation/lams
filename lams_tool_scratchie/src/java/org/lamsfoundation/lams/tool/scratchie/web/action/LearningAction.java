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

package org.lamsfoundation.lams.tool.scratchie.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.lamsfoundation.lams.learningdesign.dto.ActivityPositionDTO;
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
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.quartz.SchedulerException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Andrey Balan
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static IScratchieService service;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response)
	    throws IOException, ServletException, ScratchieApplicationException, SchedulerException {

	String param = mapping.getParameter();
	// -----------------------Scratchie Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("recordItemScratched")) {
	    return recordItemScratched(mapping, form, request, response);
	}
	if (param.equals("launchTimeLimit")) {
	    return launchTimeLimit(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}
	if (param.equals("autosaveBurningQuestions")) {
	    return autosaveBurningQuestions(mapping, form, request, response);
	}
	if (param.equals("showResults")) {
	    return showResults(mapping, form, request, response);
	}
	if (param.equals("editBurningQuestion")) {
	    return editBurningQuestion(mapping, form, request, response);
	}
	if (param.equals("like")) {
	    return like(mapping, form, request, response);
	}
	if (param.equals("removeLike")) {
	    return removeLike(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(ScratchieConstants.ERROR);
    }

    /**
     * Read scratchie data from database and put them into HttpSession.
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	initializeScratchieService();

	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	final Long toolSessionId = new Long(request.getParameter(ScratchieConstants.PARAM_TOOL_SESSION_ID));
	ScratchieSession toolSession = service.getScratchieSessionBySessionId(toolSessionId);
	// get back the scratchie and item list and display them on page
	final Scratchie scratchie = service.getScratchieBySessionId(toolSessionId);
	boolean isReflectOnActivity = scratchie.isReflectOnActivity();

	final ScratchieUser user;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // scratchieUser may be null if the user was force completed.
	    user = getSpecifiedUser(toolSessionId, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(toolSessionId);
	}

	ScratchieUser groupLeader = service.checkLeaderSelectToolForSessionLeader(user, toolSessionId);

	// forwards to the leaderSelection page
	if (groupLeader == null) {

	    // get group users and store it to request as DTO objects
	    List<ScratchieUser> groupUsers = service.getUsersBySession(toolSessionId);
	    List<User> groupUserDtos = new ArrayList<User>();
	    for (ScratchieUser groupUser : groupUsers) {
		User groupUserDto = new User();
		groupUserDto.setFirstName(groupUser.getFirstName());
		groupUserDto.setLastName(groupUser.getLastName());
		groupUserDtos.add(groupUserDto);
	    }
	    request.setAttribute(ScratchieConstants.ATTR_GROUP_USERS, groupUserDtos);
	    request.setAttribute(ScratchieConstants.PARAM_TOOL_SESSION_ID, toolSessionId);
	    request.setAttribute(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	    request.setAttribute(AttributeNames.ATTR_MODE, mode);
	    return mapping.findForward("waitforleader");
	}

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// get notebook entry
	NotebookEntry notebookEntry = null;
	if (isReflectOnActivity && (groupLeader != null)) {
	    notebookEntry = service.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
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
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(ScratchieConstants.ATTR_IS_BURNING_QUESTIONS_ENABLED, scratchie.isBurningQuestionsEnabled());
	// reflection information
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ON, isReflectOnActivity);
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_INSTRUCTION, scratchie.getReflectInstructions());
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ENTRY, entryText);
	// add all answer uids to one set
	if (isUserLeader) {
	    Set<Long> answerUids = new HashSet<Long>();
	    for (ScratchieItem item : scratchie.getScratchieItems()) {
		for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {
		    answerUids.add(answer.getUid());
		}
	    }
	    sessionMap.put(ScratchieConstants.ATTR_ANSWER_UIDS, answerUids);
	}

	// add define later support
	if (scratchie.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	ActivityPositionDTO activityPosition = WebUtil.putActivityPositionInRequestByToolSessionId(toolSessionId,
		request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

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
		return mapping.findForward("submissionDeadline");
	    }
	}

	storeItemsToSessionMap(toolSessionId, scratchie, sessionMap, mode.isTeacher());

	sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	// calculate max score
	int maxScore = service.getMaxPossibleScore(scratchie);
	sessionMap.put(ScratchieConstants.ATTR_MAX_SCORE, maxScore);

	boolean isScratchingFinished = toolSession.isScratchingFinished();
	boolean isWaitingForLeaderToSubmitNotebook = isReflectOnActivity && (notebookEntry == null);
	boolean isShowResults = (isScratchingFinished && !isWaitingForLeaderToSubmitNotebook) && !mode.isTeacher();

	// show notebook page to the leader
	if (isUserLeader && isScratchingFinished && isWaitingForLeaderToSubmitNotebook) {
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("newReflection"));
	    redirect.addParameter(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    redirect.addParameter(AttributeNames.ATTR_MODE, mode);
	    return redirect;

	    // show results page
	} else if (isShowResults) {

	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("showResults"));
	    redirect.addParameter(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    redirect.addParameter(AttributeNames.ATTR_MODE, mode);
	    return redirect;

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
		    return mapping.findForward(ScratchieConstants.WAIT_FOR_LEADER_TIME_LIMIT);
		}

		// check if the time limit is exceeded
		boolean isTimeLimitExceeded = toolSession.getTimeLimitLaunchedDate().getTime()
			+ scratchie.getTimeLimit() * 60000 < System.currentTimeMillis();

		// if the time limit is over and the leader hasn't submitted notebook or burning questions (thus
		// non-leaders should wait) - show waitForLeaderFinish page
		if (isTimeLimitExceeded && isWaitingForLeaderToSubmitNotebook) {
		    request.setAttribute(ScratchieConstants.ATTR_WAITING_MESSAGE_KEY,
			    "label.waiting.for.leader.submit.notebook");
		    return mapping.findForward(ScratchieConstants.WAIT_FOR_LEADER_TIME_LIMIT);
		}
	    }

	    sessionMap.put(ScratchieConstants.ATTR_IS_SCRATCHING_FINISHED, isScratchingFinished);
	    // make non-leaders wait for notebook to be submitted, if required
	    sessionMap.put(ScratchieConstants.ATTR_IS_WAITING_FOR_LEADER_TO_SUBMIT_NOTEBOOK,
		    isWaitingForLeaderToSubmitNotebook);
	    return mapping.findForward(ScratchieConstants.SUCCESS);
	}

    }

    /**
     * Stores into session map all data needed to display scratchies and answers
     */
    private void storeItemsToSessionMap(Long toolSessionId, Scratchie scratchie, SessionMap<String, Object> sessionMap,
	    boolean showOrder) {
	// set scratched flag for display purpose
	Collection<ScratchieItem> items = service.getItemsWithIndicatedScratches(toolSessionId);

	// shuffling items
	if (scratchie.isShuffleItems()) {
	    //items is a Set at this moment
	    ArrayList<ScratchieItem> shuffledItems = new ArrayList<ScratchieItem>(items);
	    //use random with a seed so people from the same group get the "same shuffle"
	    Random randomGenerator = new Random(toolSessionId);
	    Collections.shuffle(shuffledItems, randomGenerator);
	    items = shuffledItems;
	}

	// for teacher in monitoring display the number of attempt.
	if (showOrder) {
	    service.getScratchesOrder(items, toolSessionId);
	}

	//display confidence levels
	if (scratchie.isConfidenceLevelsEnabled()) {
	    service.populateItemsWithConfidenceLevels((Long) sessionMap.get(ScratchieConstants.ATTR_USER_ID),
		    toolSessionId, scratchie.getConfidenceLevelsActivityUiid(), items);
	}

	// populate items with the existing burning questions for displaying purposes
	List<ScratchieBurningQuestion> burningQuestions = null;
	if (scratchie.isBurningQuestionsEnabled()) {

	    burningQuestions = service.getBurningQuestionsBySession(toolSessionId);
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
    }

    /**
     * Record in DB that leader has scratched specified answer. And return whether scratchie answer is correct or not.
     */
    private ActionForward recordItemScratched(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ScratchieApplicationException {
	initializeScratchieService();
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	final Long answerUid = NumberUtils.createLong(request.getParameter(ScratchieConstants.PARAM_ANSWER_UID));

	ScratchieSession toolSession = service.getScratchieSessionBySessionId(toolSessionId);

	ScratchieUser leader = getCurrentUser(toolSessionId);
	// only leader is allowed to scratch answers
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return null;
	}

	// check answer is belong to current session
	Set<Long> answerUids = (Set<Long>) sessionMap.get(ScratchieConstants.ATTR_ANSWER_UIDS);
	if (!answerUids.contains(answerUid)) {
	    return null;
	}

	// Return whether scratchie answer is correct or not
	ScratchieAnswer answer = service.getScratchieAnswerByUid(answerUid);
	if (answer == null) {
	    return null;
	}

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put(ScratchieConstants.ATTR_ANSWER_CORRECT, answer.isCorrect());
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(ObjectNode);

	// create a new thread to record item scratched (in order to do this task in parallel not to slow down sending
	// response back)
	Thread recordItemScratchedThread = new Thread(new Runnable() {
	    @Override
	    public void run() {
		service.recordItemScratched(toolSessionId, answerUid);
	    }
	}, "LAMS_recordItemScratched_thread");
	recordItemScratchedThread.start();

	return null;
    }

    /**
     * Stores date when user has started activity with time limit.
     */
    private ActionForward launchTimeLimit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException, SchedulerException {
	initializeScratchieService();
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = service.getScratchieSessionBySessionId(toolSessionId);

	ScratchieUser leader = getCurrentUser(toolSessionId);
	// only leader is allowed to launch time limit
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return null;
	}

	service.launchTimeLimit(toolSessionId);

	return null;
    }

    /**
     * Displays results page. When leader gets to this page, scratchingFinished column is set to true for all users.
     */
    private ActionForward showResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException, IOException {
	initializeScratchieService();
	// get back SessionMap
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	boolean isReflectOnActivity = (Boolean) sessionMap.get(ScratchieConstants.ATTR_REFLECTION_ON);
	boolean isBurningQuestionsEnabled = (Boolean) sessionMap
		.get(ScratchieConstants.ATTR_IS_BURNING_QUESTIONS_ENABLED);

	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = service.getScratchieSessionBySessionId(toolSessionId);
	Scratchie scratchie = toolSession.getScratchie();
	Long userUid = (Long) sessionMap.get(ScratchieConstants.ATTR_USER_UID);

	//handle burning questions saving if needed
	if (toolSession.isUserGroupLeader(userUid) && scratchie.isBurningQuestionsEnabled()
		&& !toolSession.isScratchingFinished()) {
	    saveBurningQuestions(request);
	}

	if (toolSession.isUserGroupLeader(userUid) && !toolSession.isScratchingFinished()) {
	    service.setScratchingFinished(toolSessionId);
	}

	// get updated score from ScratchieSession
	int score = toolSession.getMark();
	int maxScore = (Integer) sessionMap.get(ScratchieConstants.ATTR_MAX_SCORE);
	double percentage = (maxScore == 0) ? 0 : ((score * 100) / maxScore);
	request.setAttribute(ScratchieConstants.ATTR_SCORE, (int) percentage);

	// display other groups' BurningQuestions
	if (isBurningQuestionsEnabled) {
	    List<BurningQuestionItemDTO> burningQuestionItemDtos = service.getBurningQuestionDtos(scratchie,
		    toolSessionId, false);
	    request.setAttribute(ScratchieConstants.ATTR_BURNING_QUESTION_ITEM_DTOS, burningQuestionItemDtos);
	}

	// display other groups' notebooks
	if (isReflectOnActivity) {
	    List<ReflectDTO> reflections = service.getReflectionList(toolSession.getScratchie().getContentId());

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
	    storeItemsToSessionMap(toolSessionId, scratchie, sessionMap, true);
	    request.setAttribute(ScratchieConstants.ATTR_SHOW_RESULTS, true);
	}

	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Saves newly entered burning question. Used by jqGrid cellediting feature.
     */
    private ActionForward editBurningQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (!StringUtils.isEmpty(request.getParameter(ScratchieConstants.ATTR_ITEM_UID))
		&& !StringUtils.isEmpty(request.getParameter(ScratchieConstants.PARAM_SESSION_ID))) {
	    initializeScratchieService();

	    Long itemUid = WebUtil.readLongParam(request, ScratchieConstants.ATTR_ITEM_UID) == 0 ? null
		    : WebUtil.readLongParam(request, ScratchieConstants.ATTR_ITEM_UID);
	    Long sessionId = WebUtil.readLongParam(request, ScratchieConstants.PARAM_SESSION_ID);
	    String question = request.getParameter(ScratchieConstants.ATTR_BURNING_QUESTION_PREFIX);
	    service.saveBurningQuestion(sessionId, itemUid, question);
	}

	return null;
    }

    private ActionForward like(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ScratchieApplicationException {
	initializeScratchieService();

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = service.getScratchieSessionBySessionId(sessionId);

	Long burningQuestionUid = WebUtil.readLongParam(request, ScratchieConstants.PARAM_BURNING_QUESTION_UID);

	ScratchieUser leader = this.getCurrentUser(sessionId);
	// only leader is allowed to scratch answers
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return null;
	}

	boolean added = service.addLike(burningQuestionUid, sessionId);

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put("added", added);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(ObjectNode);
	return null;
    }

    private ActionForward removeLike(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ScratchieApplicationException {
	initializeScratchieService();

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ScratchieSession toolSession = service.getScratchieSessionBySessionId(sessionId);

	Long burningQuestionUid = WebUtil.readLongParam(request, ScratchieConstants.PARAM_BURNING_QUESTION_UID);

	ScratchieUser leader = this.getCurrentUser(sessionId);
	// only leader is allowed to scratch answers
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return null;
	}

	service.removeLike(burningQuestionUid, sessionId);

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put("added", true);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(ObjectNode);
	return null;
    }

    /**
     * Finish learning session.
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initializeScratchieService();
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
	    nextActivityUrl = service.finishToolSession(toolSessionId, userId);

	    request.setAttribute(ScratchieConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (ScratchieApplicationException e) {
	    log.error("Failed get next activity url:" + e.getMessage());
	}
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Autosaves burning questions. Only leaders can perform it.
     */
    private ActionForward autosaveBurningQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	initializeScratchieService();
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// only leader is allowed to submit burning questions
	ScratchieUser leader = getCurrentUser(sessionId);
	ScratchieSession toolSession = service.getScratchieSessionBySessionId(sessionId);
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    return null;
	}

	saveBurningQuestions(request);

	return null;
    }

    /**
     * Saves burning questions entered by user. It also updates its values in SessionMap.
     */
    private void saveBurningQuestions(HttpServletRequest request) {
	initializeScratchieService();

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	Collection<ScratchieItem> items = (Collection<ScratchieItem>) sessionMap.get(ScratchieConstants.ATTR_ITEM_LIST);

	for (ScratchieItem item : items) {
	    final Long itemUid = item.getUid();

	    final String burningQuestion = request
		    .getParameter(ScratchieConstants.ATTR_BURNING_QUESTION_PREFIX + itemUid);
	    // update question in sessionMap
	    item.setBurningQuestion(burningQuestion);

	    // update new entry
	    service.saveBurningQuestion(sessionId, itemUid, burningQuestion);
	}

	// handle general burning question
	final String generalQuestion = request.getParameter(ScratchieConstants.ATTR_GENERAL_BURNING_QUESTION);
	service.saveBurningQuestion(sessionId, null, generalQuestion);
	// update general question in sessionMap
	sessionMap.put(ScratchieConstants.ATTR_GENERAL_BURNING_QUESTION, generalQuestion);
    }

    /**
     * Display empty reflection form.
     */
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException, IOException {

	initializeScratchieService();
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	Long userUid = (Long) sessionMap.get(ScratchieConstants.ATTR_USER_UID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	ReflectionForm refForm = (ReflectionForm) form;
	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	NotebookEntry entry = service.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ScratchieConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	ScratchieSession toolSession = service.getScratchieSessionBySessionId(toolSessionId);
	Scratchie scratchie = toolSession.getScratchie();

	//handle burning questions saving if needed
	if (toolSession.isUserGroupLeader(userUid) && scratchie.isBurningQuestionsEnabled()
		&& !toolSession.isScratchingFinished()) {
	    saveBurningQuestions(request);
	}

	// in case of the leader we should let all other learners see Next Activity button
	if (toolSession.isUserGroupLeader(userUid) && !toolSession.isScratchingFinished()) {
	    service.setScratchingFinished(toolSessionId);
	}

	return mapping.findForward(ScratchieConstants.NOTEBOOK);
    }

    /**
     * Submit reflection form input database. Only leaders can submit reflections.
     */
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	initializeScratchieService();
	ReflectionForm refForm = (ReflectionForm) form;
	final Integer userId = refForm.getUserID();
	final String entryText = refForm.getEntryText();

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	final NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ScratchieConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ScratchieConstants.TOOL_SIGNATURE, userId, entryText);
	} else {
	    // update existing entry
	    entry.setEntry(entryText);
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ENTRY, entryText);

	ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("showResults"));
	redirect.addParameter(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	return redirect;
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************

    private void initializeScratchieService() {
	if (service == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    service = (IScratchieService) wac.getBean(ScratchieConstants.SCRATCHIE_SERVICE);
	}
    }

    private ScratchieUser getCurrentUser(Long sessionId) throws ScratchieApplicationException {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	ScratchieUser scratchieUser = service.getUserByIDAndSession(user.getUserID().longValue(), sessionId);

	if (scratchieUser == null) {
	    ScratchieSession session = service.getScratchieSessionBySessionId(sessionId);
	    final ScratchieUser newScratchieUser = new ScratchieUser(user, session);
	    service.createUser(newScratchieUser);

	    scratchieUser = newScratchieUser;
	}
	return scratchieUser;
    }

    private ScratchieUser getSpecifiedUser(Long sessionId, Integer userId) {
	ScratchieUser scratchieUser = service.getUserByIDAndSession(userId.longValue(), sessionId);
	if (scratchieUser == null) {
	    log.error("Unable to find specified user for scratchie activity. Screens are likely to fail. SessionId="
		    + sessionId + " UserId=" + userId);
	}
	return scratchieUser;
    }
}