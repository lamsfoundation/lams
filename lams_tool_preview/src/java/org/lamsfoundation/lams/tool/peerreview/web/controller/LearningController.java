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

package org.lamsfoundation.lams.tool.peerreview.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.rating.dto.StyledCriteriaRatingDTO;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.tool.peerreview.service.PeerreviewApplicationException;
import org.lamsfoundation.lams.tool.peerreview.service.PeerreviewServiceImpl;
import org.lamsfoundation.lams.tool.peerreview.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Steve.Ni
 */
@Controller
@RequestMapping("/learning")
public class LearningController {

    private static Logger log = Logger.getLogger(LearningController.class);

    private static final String LEARNING_SUCCESS_PATH = "/pages/learning/learning";
    private static final String HIDDEN_USER_PATH = "/pages/learning/learningHiddenUser";
    private static final String DEFINE_LATER_PATH = "/pages/learning/definelater";
    private static final String SHOW_RESULTS_REDIRECT = "redirect:/learning/showResults.do";
    private static final String NEW_REFLECTION_REDIRECT = "redirect:/learning/newReflection.do";
    private static final String FINISH_PATH = "/pages/learning/finish";
    private static final String SHOW_RESULTS_PAGE_PATH = "/pages/learning/results";
    private static final String NOTEBOOK_PATH = "/pages/learning/notebook";

    @Autowired
    @Qualifier("peerreviewService")
    private IPeerreviewService service;

    /**
     * Read peerreview data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     * @throws IOException
     * @throws ServletException
     *
     */
    @RequestMapping("/start")
    public String start(HttpServletRequest request, HttpSession session) throws IOException, ServletException {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	session.setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID));

	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(PeerreviewConstants.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(PeerreviewConstants.PARAM_TOOL_SESSION_ID, sessionId);

	// If user already exists go straight to the normal screen, otherwise go to a refresh screen
	// until the user is created. The user will be created by the UserCreateThread(), which should
	// always be run as even if this user exists, others may have been added to the lesson/group
	// and need to be included for this user. If it is an update, the user won't see them this time
	// but they will if they choose to refresh the activity.
	PeerreviewUser user = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // peerreviewUser may be null if the user was force completed.
	    long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    user = service.getUserByIDAndSession(userId, sessionId);
	    if (user == null) {
		log.error(new StringBuilder(
			"Unable to find specified user for peerreview activity. Screens are likely to fail. SessionId=")
				.append(sessionId).append(" UserId=").append(userId).toString());
	    }
	} else {
	    UserDTO userDTO = (UserDTO) session.getAttribute(AttributeNames.USER);
	    user = service.getUserByIDAndSession(new Long(userDTO.getUserID().intValue()), sessionId);
	}

	try {
	    Thread t = new Thread(new UserCreateThread(sessionId, service));
	    t.start();
	} catch (Throwable e) {
	    throw new IOException(e);
	}

	// goto refresh screen
	if (user == null) {
	    request.setAttribute(PeerreviewConstants.ATTR_CREATING_USERS, "true");
	    return DEFINE_LATER_PATH;

	    //in case user is hidden by the monitor - show him learningHiddenUser.jsp page
	} else if (user.isHidden()) {
	    return HIDDEN_USER_PATH;

	    // goto standard screen
	} else {
	    sessionMap.put(AttributeNames.ATTR_MODE, mode);
	    sessionMap.put(PeerreviewConstants.ATTR_USER, user);
	    return startRating(request, session, sessionMap, sessionId, user, mode, null, null);
	}

    }

    /**
     * Same as start but doesn't create a new session map.
     *
     * @throws IOException
     * @throws ServletException
     *
     */
    @RequestMapping("/refresh")
    @SuppressWarnings("unchecked")
    public String refresh(HttpServletRequest request, HttpSession session) throws IOException, ServletException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Long sessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	PeerreviewUser user = (PeerreviewUser) sessionMap.get(PeerreviewConstants.ATTR_USER);

	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(PeerreviewConstants.PARAM_TOOL_SESSION_ID, sessionId);

	Long criteriaId = WebUtil.readLongParam(request, "criteriaId", true);
	RatingCriteria criteria = criteriaId != null ? service.getCriteriaByCriteriaId(criteriaId) : null;

	return startRating(request, session, sessionMap, sessionId, user, mode, criteria, null);

    }

    private class UserCreateThread implements Runnable {
	private Long toolSessionId;
	private IPeerreviewService service;

	private Logger log = Logger.getLogger(UserCreateThread.class);

	public UserCreateThread(Long toolSessionId, IPeerreviewService service) {
	    this.toolSessionId = toolSessionId;
	    this.service = service;
	}

	@Override
	public void run() {
	    try {
		service.createUsersFromLesson(toolSessionId);
	    } catch (Throwable e) {
		String message = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
		this.log.error(
			"Exception thrown creating Peer Review users for session " + toolSessionId + ": " + message, e);
		e.printStackTrace();
	    }
	}
    } // end Thread class

    /**
     * Read peerreview data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     * @throws IOException
     * @throws ServletException
     *
     */
    private String startRating(HttpServletRequest request, HttpSession session, SessionMap<String, Object> sessionMap,
	    Long sessionId, PeerreviewUser user, ToolAccessMode mode, RatingCriteria currentCriteria, Boolean next)
	    throws IOException, ServletException {

	Peerreview peerreview = service.getPeerreviewBySessionId(sessionId);

	// get notebook entry
	String entryText = new String();
	NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		PeerreviewConstants.TOOL_SIGNATURE, user.getUserId().intValue());
	if (notebookEntry != null) {
	    entryText = notebookEntry.getEntry();
	}

	// basic information
	sessionMap.put(PeerreviewConstants.ATTR_PEERREVIEW, peerreview);
	sessionMap.put(PeerreviewConstants.ATTR_TITLE, peerreview.getTitle());
	sessionMap.put(PeerreviewConstants.ATTR_RESOURCE_INSTRUCTION, peerreview.getInstructions());
	sessionMap.put(PeerreviewConstants.ATTR_LOCK_ON_FINISH, peerreview.getLockWhenFinished());

	// reflection information
	sessionMap.put(PeerreviewConstants.ATTR_REFLECTION_ON, peerreview.isReflectOnActivity());
	sessionMap.put(PeerreviewConstants.ATTR_REFLECTION_INSTRUCTION, peerreview.getReflectInstructions());
	sessionMap.put(PeerreviewConstants.ATTR_REFLECTION_ENTRY, entryText);

	// add define later support
	if (peerreview.isDefineLater()) {
	    return DEFINE_LATER_PATH;
	}

	// set contentInUse flag to true!
	peerreview.setContentInUse(true);
	peerreview.setDefineLater(false);
	service.saveOrUpdatePeerreview(peerreview);

	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, service.isLastActivity(sessionId));

	//markUser as not Finished if it's redo
	boolean isRedo = WebUtil.readBooleanParam(request, "isRedo", false);
	if (!mode.isTeacher() && !peerreview.getLockWhenFinished() && isRedo && user.isSessionFinished()) {
	    user.setSessionFinished(false);
	    service.updateUser(user);
	}

	RatingCriteria newCriteria = null;
	List<RatingCriteria> criterias = null;
	if (!user.isSessionFinished()) {
	    // mark user as finished if there are not any criterias or we have processed the last one.
	    criterias = service.getCriteriasByToolContentId(peerreview.getContentId());

	    // for criteria groups, like rubrics, count only first criterion
	    PeerreviewServiceImpl.removeGroupedCriteria(criterias);

	    if (criterias.size() > 0) {
		if (currentCriteria == null) {
		    // get the first one
		    newCriteria = criterias.get(0);
		} else if (next == null) {
		    // reload the current one
		    newCriteria = currentCriteria;
		} else if (next) {
		    // get the next one
		    for (RatingCriteria toCheck : criterias) {
			if (toCheck.getOrderId() > currentCriteria.getOrderId()) {
			    newCriteria = toCheck;
			    break;
			}
		    }
		} else {
		    // get the previous one
		    RatingCriteria prev = null;
		    for (RatingCriteria toCheck : criterias) {
			if (toCheck.getOrderId() == currentCriteria.getOrderId()) {
			    newCriteria = prev != null ? prev : currentCriteria;
			    break;
			} else {
			    prev = toCheck;
			}
		    }

		}
	    }

	    if (newCriteria == null) {
		user.setSessionFinished(true);
		service.updateUser(user);
	    } else {
		// work out the step details.
		int numCriteria = criterias.size();
		request.setAttribute("numCriteria", numCriteria);

		int stepNum = 1;
		for (RatingCriteria toCheck : criterias) {
		    if (newCriteria.getRatingCriteriaId() == toCheck.getRatingCriteriaId()) {
			break;
		    }
		    stepNum++;
		}
		request.setAttribute("stepNumber", stepNum);
	    }

	}

	sessionMap.put("isDisabled",
		peerreview.getLockWhenFinished() && user.isSessionFinished() || (mode != null) && mode.isTeacher());
	sessionMap.put(PeerreviewConstants.ATTR_USER_FINISHED, user.isSessionFinished());
	sessionMap.put("isSessionCompleted", user.getSession().getStatus() == PeerreviewConstants.COMPLETED);

	// finally, work out which page to go to!
	if (user.isSessionFinished()) {
	    if (peerreview.isShowRatingsLeftForUser() || peerreview.isShowRatingsLeftByUser()
		    || entryText.length() > 0) {
		String redirectURL = SHOW_RESULTS_REDIRECT;
		redirectURL = WebUtil.appendParameterToURL(redirectURL, PeerreviewConstants.ATTR_SESSION_MAP_ID,
			sessionMap.getSessionID());
		return redirectURL;
	    } else if (peerreview.isReflectOnActivity()) {
		String redirectURL = NEW_REFLECTION_REDIRECT;
		redirectURL = WebUtil.appendParameterToURL(redirectURL, PeerreviewConstants.ATTR_SESSION_MAP_ID,
			sessionMap.getSessionID());
		return redirectURL;
	    } else {
		// finish
		return finish(request, session);
	    }
	} else {
	    return doEdit(request, sessionMap, sessionId, peerreview, newCriteria);
	}

    }

    /**
     * Displays page with user's ratings left for others and ratings others left for him.
     */
    @RequestMapping("/showResults")
    @SuppressWarnings("unchecked")
    public String showResults(HttpServletRequest request, HttpSession session) {

	// get back SessionMap
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);
	Peerreview peerreview = (Peerreview) sessionMap.get(PeerreviewConstants.ATTR_PEERREVIEW);
	PeerreviewUser user = (PeerreviewUser) sessionMap.get(PeerreviewConstants.ATTR_USER);

	//markUserFinished if it hasn't been done previously
	if (!mode.isTeacher() && !user.isSessionFinished()) {
	    service.markUserFinished(sessionId, user.getUserId());
	    sessionMap.put(PeerreviewConstants.ATTR_USER_FINISHED, true);
	}

	// ratings left by and by the user
	List<RatingCriteria> ratingCriterias = service.getRatingCriterias(peerreview.getContentId());
	List<StyledCriteriaRatingDTO> allUsersDtos = peerreview.isShowRatingsLeftByUser()
		? new ArrayList<>(ratingCriterias.size())
		: null;
	List<StyledCriteriaRatingDTO> currentUserDtos = peerreview.isShowRatingsLeftForUser()
		? new ArrayList<>(ratingCriterias.size())
		: null;

	Set<Integer> processedCriteriaGroups = new HashSet<>();
	for (RatingCriteria criteria : ratingCriterias) {
	    if (criteria.getRatingCriteriaGroupId() != null) {
		if (processedCriteriaGroups.contains(criteria.getRatingCriteriaGroupId())) {
		    continue;
		}
		processedCriteriaGroups.add(criteria.getRatingCriteriaGroupId());
	    }

	    boolean showAllUsers = peerreview.isSelfReview() || criteria.isRankingStyleRating()
		    || criteria.isHedgeStyleRating() || (mode != null && mode.isTeacher());

	    int sorting = (criteria.isStarStyleRating() || criteria.isHedgeStyleRating())
		    ? PeerreviewConstants.SORT_BY_AVERAGE_RESULT_DESC
		    : PeerreviewConstants.SORT_BY_AVERAGE_RESULT_ASC;

	    if (allUsersDtos != null) {
		Function<RatingCriteria, StyledCriteriaRatingDTO> dtoBuilder = c -> service
			.getUsersRatingsCommentsByCriteriaIdDTO(peerreview.getContentId(), sessionId, c,
				user.getUserId(), false, sorting, null, showAllUsers, true);

		// for rubrics there is a single dto (first row) with list of all rows (including first) filled
		StyledCriteriaRatingDTO dto = criteria.isRubricsStyleRating()
			? PeerreviewServiceImpl.fillCriteriaGroup(criteria, ratingCriterias, dtoBuilder)
			: dtoBuilder.apply(criteria);
		allUsersDtos.add(dto);
	    }

	    if (currentUserDtos != null) {
		Function<RatingCriteria, StyledCriteriaRatingDTO> dtoBuilder = c -> service
			.getUsersRatingsCommentsByCriteriaIdDTO(peerreview.getContentId(), sessionId, c,
				user.getUserId(), false, sorting, null, showAllUsers, false);

		// for rubrics there is a single dto (first row) with list of all rows (including first) filled
		StyledCriteriaRatingDTO dto = criteria.isRubricsStyleRating()
			? PeerreviewServiceImpl.fillCriteriaGroup(criteria, ratingCriterias, dtoBuilder)
			: dtoBuilder.apply(criteria);
		currentUserDtos.add(dto);
	    }
	}

	if (allUsersDtos != null) {
	    request.setAttribute("allCriteriaRatings", allUsersDtos);
	}

	if (currentUserDtos != null) {
	    request.setAttribute("userRatings", currentUserDtos);
	}

	int[] numPossibleRatings = service.getNumberPossibleRatings(peerreview.getContentId(), sessionId,
		user.getUserId());
	request.setAttribute("numberRatings", numPossibleRatings[0]);
	request.setAttribute("numberPotentialRatings", numPossibleRatings[1]);

	// check whether finish lock is enabled
	sessionMap.put(PeerreviewConstants.ATTR_FINISH_LOCK, peerreview.getLockWhenFinished());

	// store how many items are rated
	int countRatedUsers = service.getCountItemsRatedByUser(peerreview.getContentId(), user.getUserId().intValue());
	sessionMap.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedUsers);

	return SHOW_RESULTS_PAGE_PATH;
    }

    /**
     * Gets a paged set of data for stars or comments. These are directly saved to the database, not through
     * LearnerAction like Ranking and Hedging.
     */
    @RequestMapping("/getUsers")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public String getUsers(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

	// get back SessionMap
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);
	Peerreview peerreview = (Peerreview) sessionMap.get(PeerreviewConstants.ATTR_PEERREVIEW);

	Long toolContentId = WebUtil.readLongParam(request, "toolContentId");
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	Long userId = WebUtil.readLongParam(request, "userId");

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSort1 = WebUtil.readIntParam(request, "column[0]", true);

	int sorting = PeerreviewConstants.SORT_BY_NO;
	if (isSort1 != null && isSort1.equals(0)) {
	    sorting = PeerreviewConstants.SORT_BY_USERNAME_ASC;
	} else if (isSort1 != null && isSort1.equals(1)) {
	    sorting = PeerreviewConstants.SORT_BY_USERNAME_DESC;
	}

	Long criteriaId = WebUtil.readLongParam(request, "criteriaId");
	RatingCriteria criteria = service.getCriteriaByCriteriaId(criteriaId);

	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows",
		service.getCountUsersBySession(toolSessionId, peerreview.isSelfReview() ? -1 : userId));
	responsedata.set("rows",
		service.getUsersRatingsCommentsByCriteriaIdJSON(toolContentId, toolSessionId, criteria, userId, page,
			size, sorting, null, peerreview.isSelfReview(), true, peerreview.getMaximumRatesPerUser() > 0));
	responsedata.put("countRatedItems", service.getCountItemsRatedByUserByCriteria(criteriaId, userId.intValue()));

	response.setContentType("application/json;charset=UTF-8");
	return responsedata.toString();
    }

    /**
     * Edit / View an indiviual criteria.
     *
     * @throws IOException
     * @throws ServletException
     *
     */
    @RequestMapping("/edit")
    @SuppressWarnings("unchecked")
    public String edit(HttpServletRequest request, HttpSession session) throws IOException, ServletException {

	// get back SessionMap
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");
	Peerreview peerreview = service.getPeerreviewBySessionId(toolSessionId);

	Long criteriaId = WebUtil.readLongParam(request, "criteriaId");
	RatingCriteria criteria = service.getCriteriaByCriteriaId(criteriaId);

	return doEdit(request, sessionMap, toolSessionId, peerreview, criteria);
    }

    private String doEdit(HttpServletRequest request, SessionMap<String, Object> sessionMap, Long toolSessionId,
	    Peerreview peerreview, RatingCriteria criteria) throws ServletException {

	Long toolContentId = peerreview.getContentId();

	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	PeerreviewUser user = (PeerreviewUser) sessionMap.get(PeerreviewConstants.ATTR_USER);

	if ((peerreview.getLockWhenFinished() && user.isSessionFinished()) || (mode != null && mode.isTeacher())) {
	    log.error("Unable to edit ranking/mark  for criteria " + criteria.getRatingCriteriaId()
		    + " as either the mode is teacher or the user is finished. ToolSessionId " + toolSessionId
		    + " User " + user + " mode " + mode);
	    throw new ServletException(
		    "Unable to edit ranking/mark as either the mode is teacher or the user is finished.");

	}

	Long userId = (mode != null && mode.isTeacher()) ? -1 : user.getUserId();
	StyledCriteriaRatingDTO criteriaDto = null;

	if (criteria.isRubricsStyleRating()) {
	    List<RatingCriteria> criterias = service.getRatingCriterias(peerreview.getContentId());
	    criteriaDto = PeerreviewServiceImpl.fillCriteriaGroup(criteria, criterias,
		    entryCriteria -> service.getUsersRatingsCommentsByCriteriaIdDTO(peerreview.getContentId(),
			    toolSessionId, entryCriteria, userId, false, PeerreviewConstants.SORT_BY_USERNAME_ASC, null,
			    peerreview.isSelfReview(), true));
	} else {
	    criteriaDto = service.getUsersRatingsCommentsByCriteriaIdDTO(toolContentId, toolSessionId, criteria, userId,
		    (criteria.isCommentRating() || criteria.isStarStyleRating()),
		    PeerreviewConstants.SORT_BY_USERNAME_ASC, null, peerreview.isSelfReview(), true);

	    // Send the number of users to rate in rateAll, or send 0. Do not want to modify the criteria min/max as it is originally
	    // a Hibernate object and don't want to risk updating it in the db. Need to send a flag so why not make flag double as the
	    // runtime min/max value while leaving min/max as the original criteria definition.
	    int rateAllUsers = 0;
	    if ((criteria.isRankingStyleRating() && criteria.getMaxRating() == RatingCriteria.RATING_RANK_ALL)
		    || (criteria.isStarStyleRating() && criteria.getMinimumRates() == RatingCriteria.RATING_RANK_ALL)
		    || (criteria.isCommentRating() && criteria.getMinimumRates() == RatingCriteria.RATING_RANK_ALL)) {
		rateAllUsers = service.getCountUsersBySession(toolSessionId, peerreview.isSelfReview() ? -1 : userId);
	    } else if ((criteria.isStarStyleRating() || criteria.isCommentRating())
		    && (peerreview.getMinimumRates() > 0 || peerreview.getMaximumRates() > 0)
		    && (criteriaDto.getRatingCriteria().getMinimumRates() == 0
			    && criteriaDto.getRatingCriteria().getMaximumRates() == 0)) {
		// override the min/max for stars based on old settings if needed (original Peer Review kept one setting for all criteria )
		// does not matter if this change gets persisted to database.
		criteria.setMinimumRates(peerreview.getMinimumRates());
		criteria.setMaximumRates(peerreview.getMaximumRates());
	    }

	    int countRatedUsers = service.getCountItemsRatedByUserByCriteria(criteria.getRatingCriteriaId(),
		    user.getUserId().intValue());
	    request.setAttribute(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedUsers);
	    request.setAttribute("rateAllUsers", rateAllUsers);
	}
	request.setAttribute("criteriaRatings", criteriaDto);

	return LEARNING_SUCCESS_PATH;
    }

    /**
     * Same as submitRankingHedging but doesn't update the records.
     *
     * @throws IOException
     * @throws ServletException
     *
     */
    @RequestMapping("/nextPrev")
    @SuppressWarnings("unchecked")
    public String nextPrev(HttpServletRequest request, HttpSession session) throws IOException, ServletException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);

	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	PeerreviewUser user = (PeerreviewUser) sessionMap.get(PeerreviewConstants.ATTR_USER);
	Long toolSessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);

	Long criteriaId = WebUtil.readLongParam(request, "criteriaId");
	RatingCriteria criteria = service.getCriteriaByCriteriaId(criteriaId);

	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(PeerreviewConstants.PARAM_TOOL_SESSION_ID, toolSessionId);

	Boolean next = WebUtil.readBooleanParam(request, "next");

	// goto standard screen
	return startRating(request, session, sessionMap, toolSessionId, user, mode, criteria, next);
    }

    /**
     * Submit any comments not already submitted and go back to the main learning screen.
     */
    /**
     * Submit the ranking / hedging data and go back to the main learning screen.
     *
     * @throws IOException
     * @throws ServletException
     * @throws JSONException
     *
     */
    @RequestMapping("/submitComments")
    @SuppressWarnings("unchecked")
    public String submitComments(HttpServletRequest request, HttpSession session) throws IOException, ServletException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	PeerreviewUser user = (PeerreviewUser) sessionMap.get(PeerreviewConstants.ATTR_USER);
	Long toolSessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);
	Peerreview peerreview = service.getPeerreviewBySessionId(toolSessionId);
	Long criteriaId = WebUtil.readLongParam(request, "criteriaId");
	RatingCriteria criteria = service.getCriteriaByCriteriaId(criteriaId);

	saveComments(request, toolSessionId, peerreview, user, criteria);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(PeerreviewConstants.PARAM_TOOL_SESSION_ID, toolSessionId);

	int countRatedItems = service.getCountItemsRatedByUserByCriteria(criteriaId, user.getUserId().intValue());
	sessionMap.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedItems);

	boolean valid = true;
	if (criteria.getMaxRating() == RatingCriteria.RATING_RANK_ALL) {
	    valid = (countRatedItems == service.getCountUsersBySession(toolSessionId,
		    peerreview.isSelfReview() ? -1 : user.getUserId()));
	} else {
	    valid = (criteria.getMinimumRates() <= countRatedItems);
	}

	if (!valid) {
	    request.setAttribute("notcomplete", true);
	    return doEdit(request, sessionMap, toolSessionId, peerreview, criteria);
	}

	Boolean next = WebUtil.readBooleanParam(request, "next");

	// goto standard screen
	return startRating(request, session, sessionMap, toolSessionId, user, mode, criteria, next);
    }

    private int saveComments(HttpServletRequest request, Long toolSessionId, Peerreview peerreview, PeerreviewUser user,
	    RatingCriteria criteria) throws IOException, ServletException {
	int countCommentsSaved = 0;
	if (!(peerreview.getLockWhenFinished() && user.isSessionFinished())) {

	    Integer userId = user.getUserId().intValue();
	    for (String key : request.getParameterMap().keySet()) {
		if (key.startsWith("comment-textarea-")) {
		    String itemIdString = key.substring(17);
		    Long itemId = new Long(itemIdString);
		    String comment = request.getParameter(key);
		    if (comment != null) {
			countCommentsSaved++;
			// save the comment to the database.
			if (comment.length() > 0) {
			    service.commentItem(criteria, toolSessionId, userId, itemId, comment);
			}
		    }
		}
	    }
	}
	return countCommentsSaved;
    }

    @RequestMapping("/submitCommentsAjax")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public String submitCommentsAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	    throws IOException, ServletException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	PeerreviewUser user = (PeerreviewUser) sessionMap.get(PeerreviewConstants.ATTR_USER);
	Long toolSessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);
	Peerreview peerreview = service.getPeerreviewBySessionId(toolSessionId);
	Long criteriaId = WebUtil.readLongParam(request, "criteriaId");
	RatingCriteria criteria = service.getCriteriaByCriteriaId(criteriaId);

	int countCommentsSaved = saveComments(request, toolSessionId, peerreview, user, criteria);

	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	int countRatedQuestions = service.getCountItemsRatedByUserByCriteria(criteriaId, user.getUserId().intValue());
	responsedata.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedQuestions);
	responsedata.put("countCommentsSaved", countCommentsSaved);
	response.setContentType("application/json;charset=utf-8");
	return responsedata.toString();
    }

    /**
     * Submit the ranking / hedging data and go back to the main learning screen.
     *
     * @throws IOException
     * @throws ServletException
     *
     */
    @RequestMapping("/submitRankingHedging")
    @SuppressWarnings("unchecked")
    public String submitRankingHedging(HttpServletRequest request, HttpServletResponse response, HttpSession session)
	    throws IOException, ServletException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	PeerreviewUser user = (PeerreviewUser) sessionMap.get(PeerreviewConstants.ATTR_USER);
	Long toolSessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);

	Peerreview peerreview = service.getPeerreviewBySessionId(toolSessionId);

	Long criteriaId = WebUtil.readLongParam(request, "criteriaId");
	RatingCriteria criteria = service.getCriteriaByCriteriaId(criteriaId);

	if (!(peerreview.getLockWhenFinished() && user.isSessionFinished())) {

	    Integer userId = user.getUserId().intValue();
	    Map<Long, Float> ratings = new HashMap<>();
	    boolean valid = false;

	    if (criteria.isHedgeStyleRating()) {
		int totalMark = 0;
		// mark1 contains the mark for itemid 1
		for (String key : request.getParameterMap().keySet()) {
		    if (key.startsWith("mark")) {
			String itemIdString = key.substring(4);
			Long itemId = new Long(itemIdString);
			Long value = WebUtil.readLongParam(request, key);
			if (value > 0) {
			    ratings.put(itemId, value.floatValue());
			}
			totalMark += value;
		    }
		}
		valid = (totalMark == criteria.getMaxRating());
	    } else if (criteria.isRankingStyleRating()) {
		// rank1 contains the itemid for the learner ranked first.
		for (String key : request.getParameterMap().keySet()) {
		    if (key.startsWith("rank")) {
			String valueString = key.substring(4);
			Long value = new Long(valueString);
			Long itemId = WebUtil.readLongParam(request, key, true);
			if (itemId != null) {
			    ratings.put(itemId, value.floatValue());
			}
		    }
		}
		valid = (ratings.size() == criteria.getMaxRating() || (ratings.size() >= service
			.getCountUsersBySession(toolSessionId, peerreview.isSelfReview() ? -1 : user.getUserId())));
	    }

	    service.rateItems(criteria, toolSessionId, userId, ratings);
	    if (!valid) {
		request.setAttribute("notcomplete", true);
		return doEdit(request, sessionMap, toolSessionId, peerreview, criteria);
	    }

	    if (criteria.isHedgeStyleRating() && criteria.isCommentsEnabled()) {
		String justify = request.getParameter("justify");
		if (justify != null && justify.length() > 0) {
		    service.commentItem(criteria, toolSessionId, userId, criteria.getRatingCriteriaId(), justify);
		}
	    }

	}

	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(PeerreviewConstants.PARAM_TOOL_SESSION_ID, toolSessionId);

	Boolean next = WebUtil.readBooleanParam(request, "next");

	// goto standard screen
	return startRating(request, session, sessionMap, toolSessionId, user, mode, criteria, next);
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
    @RequestMapping("/finish")
    @SuppressWarnings("unchecked")
    public String finish(HttpServletRequest request, HttpSession session) {

	// get back SessionMap
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	Long sessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);

	return doFinish(request, sessionId, session);
    }

    private String doFinish(HttpServletRequest request, Long sessionId, HttpSession ss) {
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(PeerreviewConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (PeerreviewApplicationException e) {
	    LearningController.log.error("Failed get next activity url:" + e.getMessage());
	}

	return FINISH_PATH;
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
    @RequestMapping("/newReflection")
    @SuppressWarnings("unchecked")
    public String newReflection(@ModelAttribute ReflectionForm refForm, HttpServletRequest request,
	    HttpServletResponse response, HttpSession session) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, PeerreviewConstants.ATTR_SESSION_MAP_ID);

	UserDTO user = (UserDTO) session.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	SessionMap<String, Object> map = (SessionMap<String, Object>) session.getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = service.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		PeerreviewConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return NOTEBOOK_PATH;
    }

    /**
     * Submit reflection form input database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/submitReflection")
    @SuppressWarnings("unchecked")
    public String submitReflection(@ModelAttribute ReflectionForm form, HttpServletRequest request,
	    HttpServletResponse response, HttpSession session) {
	ReflectionForm refForm = form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		PeerreviewConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    PeerreviewConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finish(request, session);
    }
}
