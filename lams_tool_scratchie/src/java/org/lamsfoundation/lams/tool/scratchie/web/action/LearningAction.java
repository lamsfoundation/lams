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

/* $Id$ */
package org.lamsfoundation.lams.tool.scratchie.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Callable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
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
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Andrey Balan
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static IScratchieService service;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException,
	    ScratchieApplicationException {

	String param = mapping.getParameter();
	// -----------------------Scratchie Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("refreshQuestionList")) {
	    return refreshQuestionList(mapping, form, request, response);
	}
	if (param.equals("isAnswerCorrect")) {
	    return isAnswerCorrect(mapping, form, request, response);
	}
	if (param.equals("recordItemScratched")) {
	    return recordItemScratched(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}
	if (param.equals("showResults")) {
	    return showResults(mapping, form, request, response);
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
     * Read scratchie data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     * 
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     * 
     * @throws ScratchieApplicationException
     * 
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: entered start()");
	initializeScratchieService();

	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	final Long toolSessionId = new Long(request.getParameter(ScratchieConstants.PARAM_TOOL_SESSION_ID));
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting ScratchieSession by ID: " + toolSessionId);
	ScratchieSession toolSession = LearningAction.service.getScratchieSessionBySessionId(toolSessionId);
	// get back the scratchie and item list and display them on page
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting Scratchi by session ID: " + toolSessionId);
	final Scratchie scratchie = LearningAction.service.getScratchieBySessionId(toolSessionId);

	final ScratchieUser user;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // scratchieUser may be null if the user was force completed.
	    user = getSpecifiedUser(toolSessionId, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(toolSessionId);
	}

	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: trying to execute checkLeaderSelectToolForSessionLeader for user ID: " + user.getUserId()
		+ " and session ID: " + toolSessionId);
	final ScratchieUser groupLeader = (ScratchieUser) tryExecute(new Callable<Object>() {
	    @Override
	    public Object call() throws ScratchieApplicationException {
		return LearningAction.service.checkLeaderSelectToolForSessionLeader(user, toolSessionId);
	    }
	});

	// forwards to the leaderSelection page
	if ((groupLeader == null) && !mode.isTeacher()) {

	    // get group users and store it to request as DTO objects
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: getting ScratchieUsers by session ID: " + toolSessionId);
	    List<ScratchieUser> groupUsers = LearningAction.service.getUsersBySession(toolSessionId);
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
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: leaving start(), waiting for leader");
	    return mapping.findForward("waitforleader");
	}

	if ((groupLeader != null) && !mode.isTeacher()) {
	    // in case user joins the lesson after leader has scratched some answers already - we need to make sure
	    // he has the same scratches as leader
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: trying to execute copyScratchesFromLeade for user ID: " + user.getUserId()
		    + " and leader ID: " + groupLeader.getUserId());
	    tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    LearningAction.service.copyScratchesFromLeader(user, groupLeader);
		    return null;
		}
	    });

	    // if user joins a lesson after leader has already finished an activity set his scratchingFinished
	    // parameter to true
	    if (groupLeader.isScratchingFinished()) {
		user.setScratchingFinished(true);
		LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
			+ "]: trying to execute saveUser for user ID: " + user.getUserId());
		tryExecute(new Callable<Object>() {
		    @Override
		    public Object call() throws ScratchieApplicationException {
			LearningAction.service.saveUser(user);
			return null;
		    }
		});
	    }
	}

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: setting sessionMapID to " + sessionMap.getSessionID());
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// get notebook entry
	String entryText = new String();
	if (groupLeader != null) {
	    NotebookEntry notebookEntry = LearningAction.service.getEntry(toolSessionId,
		    CoreNotebookConstants.NOTEBOOK_TOOL, ScratchieConstants.TOOL_SIGNATURE, groupLeader.getUserId()
			    .intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}

	// basic information
	sessionMap.put(ScratchieConstants.ATTR_TITLE, scratchie.getTitle());
	sessionMap.put(ScratchieConstants.ATTR_RESOURCE_INSTRUCTION, scratchie.getInstructions());
	sessionMap.put(ScratchieConstants.ATTR_USER_ID, user.getUserId());
	sessionMap.put(ScratchieConstants.ATTR_USER_UID, user.getUid());
	User groupLeaderDto = new User();
	if (groupLeader != null) {
	    user.setFirstName(groupLeader.getFirstName());
	    user.setLastName(groupLeader.getLastName());
	}
	sessionMap.put(ScratchieConstants.ATTR_GROUP_LEADER, groupLeaderDto);
	boolean isUserLeader = toolSession.isUserGroupLeader(user.getUid());
	sessionMap.put(ScratchieConstants.ATTR_IS_USER_LEADER, isUserLeader);
	boolean isUserFinished = (user != null) && user.isSessionFinished();
	sessionMap.put(ScratchieConstants.ATTR_USER_FINISHED, isUserFinished);
	sessionMap.put(ScratchieConstants.PARAM_RUN_OFFLINE, scratchie.getRunOffline());
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ON, scratchie.isReflectOnActivity());
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_INSTRUCTION, scratchie.getReflectInstructions());
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ENTRY, entryText);

	// add define later support
	if (scratchie.isDefineLater()) {
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: leaving start()");
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	scratchie.setContentInUse(true);
	scratchie.setDefineLater(false);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: trying to execute saveOrUpdateScratchie() with content ID: " + scratchie.getContentId());
	tryExecute(new Callable<Object>() {
	    @Override
	    public Object call() throws ScratchieApplicationException {
		LearningAction.service.saveOrUpdateScratchie(scratchie);
		return null;
	    }
	});

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(
		toolSessionId, request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	// run offline support
	if (scratchie.getRunOffline()) {
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: leaving start()");
	    return mapping.findForward("runOffline");
	}

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

	    // calculate whether submission deadline has passed, and if so forward to "runOffline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
			+ "]: leaving start()");
		return mapping.findForward("runOffline");
	    }
	}

	// set scratched flag for display purpose
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting ScratchieItems for user ID: " + user.getUserId() + " and session ID: " + toolSessionId);
	Collection<ScratchieItem> items = LearningAction.service.getItemsWithIndicatedScratches(toolSessionId, user);

	// for teacher in monitoring display the number of attempt.
	if (mode.isTeacher()) {
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: getting Scratches order for user ID: " + user.getUserId());
	    LearningAction.service.getScratchesOrder(items, user);
	}

	// calculate max score
	int maxScore = 0;
	for (ScratchieItem item : items) {
	    maxScore += item.getAnswers().size() - 1;
	    if (scratchie.isExtraPoint()) {
		maxScore++;
	    }
	}

	sessionMap.put(ScratchieConstants.ATTR_ITEM_LIST, items);
	sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	sessionMap.put(ScratchieConstants.ATTR_MAX_SCORE, maxScore);
	boolean isScratchingFinished = (user != null) && user.isScratchingFinished();
	sessionMap.put(ScratchieConstants.ATTR_IS_SCRATCHING_FINISHED, isScratchingFinished);

	// decide whether to show results page or learning one
	if (isScratchingFinished && !mode.isTeacher()) {
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("showResults"));
	    redirect.addParameter(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    redirect.addParameter(AttributeNames.ATTR_MODE, mode);
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: leaving start()");
	    return redirect;
	} else {
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: leaving start()");
	    return mapping.findForward(ScratchieConstants.SUCCESS);
	}

    }

    /**
     * Refresh
     * 
     * @throws ScratchieApplicationException
     */
    private ActionForward refreshQuestionList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: entered refreshQuestionList()");
	initializeScratchieService();
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: sessionMapID is " + sessionMapID);
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting ScratchieSession by ID: " + toolSessionId);
	ScratchieSession toolSession = LearningAction.service.getScratchieSessionBySessionId(toolSessionId);
	ScratchieUser user = this.getCurrentUser(toolSessionId);

	// set scratched flag for display purpose
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting ScratchieItems by user ID: " + user.getUserId() + " and session ID: " + toolSessionId
		+ " and putting them in sessionMap(!)");
	Set<ScratchieItem> items = LearningAction.service.getItemsWithIndicatedScratches(toolSessionId, user);
	sessionMap.put(ScratchieConstants.ATTR_ITEM_LIST, items);

	// refresh leadership status
	boolean isUserLeader = toolSession.isUserGroupLeader(user.getUid());
	sessionMap.put(ScratchieConstants.ATTR_IS_USER_LEADER, isUserLeader);
	// refresh ScratchingFinished status
	boolean isScratchingFinished = (user != null) && user.isScratchingFinished();
	sessionMap.put(ScratchieConstants.ATTR_IS_SCRATCHING_FINISHED, isScratchingFinished);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: leaving refreshQuestionList()");
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Return whether scratchie answer is correct or not
     */
    private ActionForward isAnswerCorrect(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: entered isAnswerCorrect()");
	initializeScratchieService();

	Long answerUid = NumberUtils.createLong(request.getParameter(ScratchieConstants.PARAM_ANSWER_UID));
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting ScratchieAnswer by UID: " + answerUid);
	ScratchieAnswer answer = LearningAction.service.getScratchieAnswerByUid(answerUid);
	if (answer == null) {
	    return mapping.findForward(ScratchieConstants.ERROR);
	}

	JSONObject JSONObject = new JSONObject();
	JSONObject.put(ScratchieConstants.ATTR_ANSWER_CORRECT, answer.isCorrect());
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(JSONObject);

	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: leaving isAnswerCorrect()");
	return null;
    }

    /**
     * Record in DB that leader has scratched specified answer.
     * 
     * @throws ScratchieApplicationException
     */
    private ActionForward recordItemScratched(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException, ScratchieApplicationException {
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: entered recordItemScratched()");
	initializeScratchieService();
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: sessionMapID is " + sessionMapID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(
		sessionMapID);
	Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting ScratchieSession by ID: " + toolSessionId);
	ScratchieSession toolSession = LearningAction.service.getScratchieSessionBySessionId(toolSessionId);

	final ScratchieUser leader = this.getCurrentUser(toolSessionId);
	// only leaders are allowed to scratch answers
	if (!toolSession.isUserGroupLeader(leader.getUid())) {
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: leaving recordItemScratched(), it is not a leader");
	    return null;
	}

	final Long answerUid = NumberUtils.createLong(request.getParameter(ScratchieConstants.PARAM_ANSWER_UID));
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: trying to execute recordItemScratched() for leader ID: " + leader.getUserId()
		+ " and answer UID: " + answerUid);
	tryExecute(new Callable<Object>() {
	    @Override
	    public Object call() throws ScratchieApplicationException {
		LearningAction.service.recordItemScratched(leader, answerUid);
		return null;
	    }
	});
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: leaving recordItemScratched()");
	return null;
    }

    /**
     * Displays results page. When leader gets to this page, scratchingFinished column is set to true for all users.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ScratchieApplicationException
     */
    private ActionForward showResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: entered showResults()");
	initializeScratchieService();
	// get back SessionMap
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: sessionMapID is " + sessionMapID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(
		sessionMapID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting ScratchiSession by ID " + toolSessionId);
	ScratchieSession toolSession = LearningAction.service.getScratchieSessionBySessionId(toolSessionId);
	Long userUid = (Long) sessionMap.get(ScratchieConstants.ATTR_USER_UID);

	// in case of the leader we should let all other learners see Next Activity button
	if (toolSession.isUserGroupLeader(userUid)) {
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: trying to execute setScratchingFinished() for session ID: " + toolSessionId);
	    tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    LearningAction.service.setScratchingFinished(toolSessionId);
		    return null;
		}
	    });
	}

	// get user from DB to get his updated score
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting ScratchieUser by UID: " + userUid);
	ScratchieUser userUpdated = LearningAction.service.getUser(userUid);
	int score = userUpdated.getMark();
	int maxScore = (Integer) sessionMap.get(ScratchieConstants.ATTR_MAX_SCORE);
	double percentage = (maxScore == 0) ? 0 : ((score * 100) / maxScore);
	request.setAttribute(ScratchieConstants.ATTR_SCORE, (int) percentage);

	// Create reflectList if reflection is enabled.
	boolean isReflectOnActivity = (Boolean) sessionMap.get(ScratchieConstants.ATTR_REFLECTION_ON);
	if (isReflectOnActivity) {
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: getting reflections by content ID: " + toolSession.getScratchie().getContentId());
	    List<ReflectDTO> reflections = LearningAction.service.getReflectionList(toolSession.getScratchie()
		    .getContentId(), false);

	    // remove current session leader reflection
	    Iterator<ReflectDTO> refIterator = reflections.iterator();
	    while (refIterator.hasNext()) {
		ReflectDTO reflection = refIterator.next();
		if (toolSession.getSessionName().equals(reflection.getGroupName())) {
		    refIterator.remove();
		    break;
		}
	    }

	    request.setAttribute(ScratchieConstants.ATTR_REFLECTIONS, reflections);
	}
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: leaving showResults()");
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Finish learning session.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: entered finish()");
	initializeScratchieService();
	// get back SessionMap
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: sessionMapID is " + sessionMapID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(
		sessionMapID);
	final Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	final Long userId = user.getUserID().longValue();

	String nextActivityUrl = null;

	try {
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: trying to execute finishToolSession() for user ID: " + userId + " and session ID: "
		    + toolSessionId);
	    nextActivityUrl = (String) tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    return LearningAction.service.finishToolSession(toolSessionId, userId);
		}
	    });

	    request.setAttribute(ScratchieConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (ScratchieApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: leaving finish()");
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    private Object tryExecute(Callable<Object> command) throws ScratchieApplicationException {
	final int MAX_TRANSACTION_RETRIES = 5;
	Object returnValue = null;

	for (int i = 0; i < MAX_TRANSACTION_RETRIES; i++) {
	    try {
		returnValue = command.call();
		LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
			+ "]: successfully called command");
		break;
	    } catch (CannotAcquireLockException e) {
		if (i == (MAX_TRANSACTION_RETRIES - 1)) {
		    throw new ScratchieApplicationException(e);
		}
	    } catch (Exception e) {
		throw new ScratchieApplicationException(e);
	    }
	    LearningAction.log.warn("Transaction retry: " + (i + 1));
	}

	return returnValue;
    }

    /**
     * Display empty reflection form.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	initializeScratchieService();
	// get session value
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = LearningAction.service.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ScratchieConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Submit reflection form input database. Only leaders can submit reflections.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ScratchieApplicationException
     */
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ScratchieApplicationException {
	initializeScratchieService();
	ReflectionForm refForm = (ReflectionForm) form;
	final Integer userId = refForm.getUserID();
	final String entryText = refForm.getEntryText();

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(
		sessionMapID);
	final Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	final NotebookEntry entry = LearningAction.service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ScratchieConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    LearningAction.service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			    ScratchieConstants.TOOL_SIGNATURE, userId, entryText);
		    return null;
		}
	    });
	} else {
	    // update existing entry
	    entry.setEntry(entryText);
	    entry.setLastModified(new Date());
	    tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    LearningAction.service.updateEntry(entry);
		    return null;
		}
	    });
	}
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ENTRY, entryText);

	return showResults(mapping, refForm, request, response);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************

    private void initializeScratchieService() {
	if (LearningAction.service == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    LearningAction.service = (IScratchieService) wac.getBean(ScratchieConstants.SCRATCHIE_SERVICE);
	}
    }

    private ScratchieUser getCurrentUser(Long sessionId) throws ScratchieApplicationException {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting current ScratchieUser with ID: " + user.getUserID() + " and session ID: " + sessionId);
	ScratchieUser scratchieUser = LearningAction.service.getUserByIDAndSession(user.getUserID().longValue(),
		sessionId);

	if (scratchieUser == null) {
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: getting Scratchie by session ID: " + sessionId);
	    ScratchieSession session = LearningAction.service.getScratchieSessionBySessionId(sessionId);
	    final ScratchieUser newScratchieUser = new ScratchieUser(user, session);
	    LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		    + "]: creating ScratchieUser with ID: " + user.getUserID() + " and session ID: " + sessionId);
	    tryExecute(new Callable<Object>() {
		@Override
		public Object call() throws ScratchieApplicationException {
		    LearningAction.service.createUser(newScratchieUser);
		    return null;
		}
	    });

	    scratchieUser = newScratchieUser;
	}
	return scratchieUser;
    }

    private ScratchieUser getSpecifiedUser(Long sessionId, Integer userId) {
	LearningAction.log.info("LKC:[" + Thread.currentThread().getId() + "|" + Thread.activeCount()
		+ "]: getting specified ScratchieUser with ID: " + userId + " and session ID: " + sessionId);
	ScratchieUser scratchieUser = LearningAction.service.getUserByIDAndSession(userId.longValue(), sessionId);
	if (scratchieUser == null) {
	    LearningAction.log
		    .error("Unable to find specified user for scratchie activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return scratchieUser;
    }

}
