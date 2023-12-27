/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.peerreview.service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.dto.StyledCriteriaRatingDTO;
import org.lamsfoundation.lams.rating.dto.StyledRatingDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.RatingRubricsColumn;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewDAO;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewSessionDAO;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewUserDAO;
import org.lamsfoundation.lams.tool.peerreview.dto.GroupSummary;
import org.lamsfoundation.lams.tool.peerreview.dto.PeerreviewStatisticsDTO;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.util.EmailAnalysisBuilder;
import org.lamsfoundation.lams.tool.peerreview.util.EmailAnalysisBuilder.LearnerData;
import org.lamsfoundation.lams.tool.peerreview.util.SpreadsheetBuilder;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Andrey Balan
 */
public class PeerreviewServiceImpl
	implements IPeerreviewService, ToolContentManager, ToolSessionManager, ToolRestManager {
    private static Logger log = Logger.getLogger(PeerreviewServiceImpl.class.getName());

    private PeerreviewDAO peerreviewDao;

    private PeerreviewUserDAO peerreviewUserDao;

    private PeerreviewSessionDAO peerreviewSessionDao;

    // tool service
    private IToolContentHandler peerreviewToolContentHandler;

    private MessageService messageService;

    private PeerreviewOutputFactory peerreviewOutputFactory;

    // system services

    private ILamsToolService toolService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private IRatingService ratingService;

    private IEventNotificationService eventNotificationService;

    private SortedSet<Long> creatingUsersForSessionIds;

    PeerreviewServiceImpl() {
	creatingUsersForSessionIds = Collections.synchronizedSortedSet(new TreeSet<Long>());
    }

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public Peerreview getPeerreviewByContentId(Long contentId) {
	Peerreview rs = peerreviewDao.getByContentId(contentId);
	if (rs == null) {
	    log.debug("Could not find the content by given ID:" + contentId);
	}
	return rs;
    }

    @Override
    public Peerreview getDefaultContent(Long contentId) throws PeerreviewApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new PeerreviewApplicationException(error);
	}

	Peerreview defaultContent = getDefaultPeerreview();
	// save default content by given ID.
	Peerreview content = new Peerreview();
	content = Peerreview.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public void updateUser(PeerreviewUser peerreviewUser) {
	peerreviewUserDao.insertOrUpdate(peerreviewUser);
    }

    @Override
    public PeerreviewUser getUserByIDAndContent(Long userId, Long contentId) {
	return peerreviewUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public PeerreviewUser getUserByIDAndSession(Long userId, Long sessionId) {
	return peerreviewUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public List<PeerreviewUser> getUsersBySession(Long sessionId) {
	return peerreviewUserDao.getBySessionID(sessionId);
    }

    @Override
    public void saveOrUpdatePeerreview(Peerreview peerreview) {
	peerreviewDao.insertOrUpdate(peerreview);
    }

    @Override
    public Peerreview getPeerreviewBySessionId(Long sessionId) {
	PeerreviewSession session = peerreviewSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getPeerreview().getContentId();
	Peerreview res = peerreviewDao.getByContentId(contentId);
	return res;
    }

    @Override
    public PeerreviewSession getPeerreviewSessionBySessionId(Long sessionId) {
	return peerreviewSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public List<PeerreviewSession> getPeerreviewSessionsByConentId(Long toolContentId) {
	return peerreviewSessionDao.getByContentId(toolContentId);
    }

    @Override
    public void saveOrUpdatePeerreviewSession(PeerreviewSession resSession) {
	peerreviewSessionDao.insertOrUpdate(resSession);
    }

    @Override
    public void markUserFinished(Long toolSessionId, Long userId) {
	PeerreviewUser user = peerreviewUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	peerreviewUserDao.update(user);
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws PeerreviewApplicationException {
	markUserFinished(toolSessionId, userId);

	// PeerreviewSession session = peerreviewSessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(PeerreviewConstants.COMPLETED);
	// peerreviewSessionDao.saveObject(session);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new PeerreviewApplicationException(e);
	} catch (ToolException e) {
	    throw new PeerreviewApplicationException(e);
	}
	return nextUrl;
    }

    @Override
    public List<GroupSummary> getGroupSummaries(Long contentId) {
	Set<GroupSummary> groupSet = new TreeSet<>();

	// get all sessions in a peerreview and retrieve all peerreview items under this session
	// plus initial peerreview items by author creating (resItemList)
	List<PeerreviewSession> sessionList = peerreviewSessionDao.getByContentId(contentId);

	for (PeerreviewSession session : sessionList) {
	    // one new group for one session.
	    GroupSummary group = new GroupSummary();
	    group.setSessionId(session.getSessionId());
	    group.setSessionName(session.getSessionName());
	    group.setEmailsSent(session.isEmailsSent());

	    groupSet.add(group);
	}

	return new ArrayList<>(groupSet);
    }

    @Override
    public int getCountUsersBySession(final Long toolSessionId, final Long excludeUserId) {
	return peerreviewUserDao.getCountUsersBySession(toolSessionId, excludeUserId);
    }

    @Override
    public int getCountUsersBySession(final Long toolSessionId) {
	return peerreviewUserDao.getCountUsersBySession(toolSessionId);
    }

    @Override
    public PeerreviewUser getUser(Long uid) {
	return peerreviewUserDao.find(PeerreviewUser.class, uid);
    }

    @Override
    public boolean createUsersFromLesson(Long toolSessionId) throws Throwable {

	User currentUser = null;
	try {
	    boolean wasNotInSetAlready = creatingUsersForSessionIds.add(toolSessionId);
	    if (!wasNotInSetAlready) {
		return false;
	    }

	    PeerreviewSession session = getPeerreviewSessionBySessionId(toolSessionId);
	    int numberPotentialLearners = toolService.getCountUsersForActivity(toolSessionId);
	    int numberActualLearners = peerreviewUserDao.getCountUsersBySession(toolSessionId);
	    int numUsersCreated = 0;
	    if (numberActualLearners != numberPotentialLearners) {
		numUsersCreated = peerreviewUserDao.createUsersForSession(session);
	    }

	    if (log.isDebugEnabled()) {
		log.debug("Peer Review UserCreateThread " + toolSessionId + ": numUsersCreated " + numUsersCreated);
	    }

	    creatingUsersForSessionIds.remove(toolSessionId);
	    return true;
	} catch (Throwable e) {
	    creatingUsersForSessionIds.remove(toolSessionId);
	    String message = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
	    log.error("Exception thrown creating Peer Review users for session " + toolSessionId + " user id: " + (
		    currentUser != null
			    ? currentUser.getUserId().toString()
			    : "null") + "; " + message, e);
	    e.printStackTrace();
	    throw (e);
	}
    }

    @Override
    public void setUserHidden(Long toolContentId, Long userUid, boolean isHidden) {
	PeerreviewUser user = peerreviewUserDao.getUserByUid(userUid);
	if (user == null) {
	    return;
	}

	//If user is marked as hidden - it will automatically remove all rating left by him to prevent statistics mess up.
	if (isHidden) {
	    ratingService.removeUserCommitsByContent(toolContentId, user.getUserId().intValue());
	}

	user.setHidden(isHidden);
	peerreviewUserDao.update(user);
    }

    @Override
    public int rateItems(RatingCriteria ratingCriteria, Long toolSessionId, Integer userId,
	    Map<Long, Float> newRatings) {
	return ratingService.rateItems(ratingCriteria, toolSessionId, userId, newRatings);
    }

    @Override
    public String commentItem(RatingCriteria ratingCriteria, Long toolSessionId, Integer userId, Long itemId,
	    String comment) {
	return ratingService.commentItem(ratingCriteria, toolSessionId, userId, itemId, comment);
    }

    @Override
    public RatingCriteria getCriteriaByCriteriaId(Long ratingCriteriaId) {
	return ratingService.getCriteriaByCriteriaId(ratingCriteriaId);
    }

    @Override
    public StyledCriteriaRatingDTO getUsersRatingsCommentsByCriteriaIdDTO(Long toolContentId, Long toolSessionId,
	    RatingCriteria criteria, Long currentUserId, boolean skipRatings, int sorting, String searchString,
	    boolean getAllUsers, boolean getByUser) {

	if (skipRatings) {
	    return ratingService.convertToStyledDTO(criteria, currentUserId, getAllUsers, null);
	}

	if (criteria.isRubricsStyleRating() && !getByUser) {
	    Collection<Rating> ratings = ratingService.getRatingsByCriteriasAndItems(
		    Set.of(criteria.getRatingCriteriaId()), Set.of(currentUserId));

	    return PeerreviewServiceImpl.getRubricsCriteriaDTO(criteria, currentUserId.intValue(), getAllUsers,
		    ratings);
	}

	List<Object[]> rawData = peerreviewUserDao.getRatingsComments(toolContentId, toolSessionId, criteria,
		currentUserId, null, null, sorting, searchString, getByUser, !getByUser || getAllUsers, ratingService,
		userManagementService);

	for (Object[] raw : rawData) {
	    raw[raw.length - 2] = HtmlUtils.htmlEscape((String) raw[raw.length - 2]);
	}
	// if !getByUser -> is get current user's ratings from other users ->
	// convertToStyledJSON.getAllUsers needs to be true otherwise current user (the only one in the set!) is dropped
	return ratingService.convertToStyledDTO(criteria, currentUserId, !getByUser || getAllUsers, rawData);
    }

    @Override
    public ArrayNode getUsersRatingsCommentsByCriteriaIdJSON(Long toolContentId, Long toolSessionId,
	    RatingCriteria criteria, Long currentUserId, Integer page, Integer size, int sorting, String searchString,
	    boolean getAllUsers, boolean getByUser, boolean needRatesPerUser) {

	List<Object[]> rawData = peerreviewUserDao.getRatingsComments(toolContentId, toolSessionId, criteria,
		currentUserId, page, size, sorting, searchString, getByUser, !getByUser || getAllUsers, ratingService,
		userManagementService);

	for (Object[] raw : rawData) {
	    raw[raw.length - 2] = HtmlUtils.htmlEscape((String) raw[raw.length - 2]);
	}
	// if !getByUser -> is get current user's ratings from other users ->
	// convertToStyledJSON.getAllUsers needs to be true otherwise current user (the only one in the set!) is dropped
	return ratingService.convertToStyledJSON(criteria, toolSessionId, currentUserId, !getByUser || getAllUsers,
		rawData, needRatesPerUser);
    }

    @Override
    public List<Object[]> getDetailedRatingsComments(Long toolContentId, Long toolSessionId, Long criteriaId,
	    Long itemId) {
	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);

	// raw data: user_id, comment, rating, first_name, last_name
	List<Object[]> rawData = peerreviewUserDao.getDetailedRatingsComments(toolContentId, toolSessionId, criteriaId,
		itemId);
	for (Object[] raw : rawData) {
	    raw[2] = (raw[2] == null ? null : numberFormat.format(raw[2])); // format rating
	    // format name
	    StringBuilder description = new StringBuilder((String) raw[3]).append(" ").append((String) raw[4]);
	    raw[4] = HtmlUtils.htmlEscape(description.toString());

	}
	return rawData;
    }

    @Override
    public List<Object[]> getCommentsCounts(Long toolContentId, Long toolSessionId, RatingCriteria criteria,
	    Integer page, Integer size, int sorting, String searchString) {

	List<Object[]> rawData = peerreviewUserDao.getCommentsCounts(toolContentId, toolSessionId, criteria, page, size,
		sorting, searchString, userManagementService);

	// raw data: user_id, comment_count, first_name  last_name, portrait id
	for (Object[] raw : rawData) {
	    raw[2] = HtmlUtils.htmlEscape((String) raw[2]);
	}

	return rawData;
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
    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    @Override
    public List<PeerreviewStatisticsDTO> getStatistics(Long toolContentId) {
	return peerreviewDao.getStatistics(toolContentId);
    }

    @Override
    public List<Object[]> getPagedUsers(Long toolSessionId, Integer page, Integer size, int sorting,
	    String searchString) {
	return peerreviewUserDao.getPagedUsers(toolSessionId, page, size, sorting, searchString);
    }

    private String getResultsEmailSubject(Peerreview peerreview) {
	return getLocalisedMessage("event.sent.results.subject", new Object[] { peerreview.getTitle() });
    }

    @Override
    public Map<Long, LearnerData> getLearnerData(Long toolContentId, Long sessionId) {
	PeerreviewSession session = peerreviewSessionDao.getSessionBySessionId(sessionId);
	Peerreview peerreview = getPeerreviewByContentId(toolContentId);
	return new EmailAnalysisBuilder(peerreview, session, ratingService, peerreviewSessionDao, peerreviewUserDao,
		this, messageService).generateTeamData();
    }

    @Override
    public String generateEmailReportToUser(Long toolContentId, Long sessionId, Long userId) {
	PeerreviewSession session = peerreviewSessionDao.getSessionBySessionId(sessionId);
	Peerreview peerreview = getPeerreviewByContentId(toolContentId);
	return new EmailAnalysisBuilder(peerreview, session, ratingService, peerreviewSessionDao, peerreviewUserDao,
		this, messageService).generateHTMLEMailForLearner(userId);
    }

    @Override
    public int emailReportToUser(Long toolContentId, Long sessionId, Long userId, String email) {
	if (log.isDebugEnabled()) {
	    log.debug("Sending email with results to learner " + userId + " for session ID " + sessionId);
	}
	PeerreviewUser user = peerreviewUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	if (user != null) {
	    eventNotificationService.sendMessage(null, userId.intValue(),
		    IEventNotificationService.DELIVERY_METHOD_MAIL,
		    getResultsEmailSubject(user.getSession().getPeerreview()), email, true);
	    return 1;
	} else {
	    log.error("Unable to send Peer Review email as user is not in session. SessionId=" + sessionId + " userId="
		    + userId);
	    return 0;
	}
    }

    @Override
    public int emailReportToUsers(Long toolContentId, Long sessionId) {
	List<PeerreviewSession> sessions = null;
	if (sessionId == null) {
	    sessions = peerreviewSessionDao.getByContentId(toolContentId);
	} else {
	    sessions = List.of(peerreviewSessionDao.getSessionBySessionId(sessionId));
	}

	int emailsSent = 0;
	for (PeerreviewSession session : sessions) {
	    if (log.isDebugEnabled()) {
		log.debug("Sending email with results to all learners for session ID " + session.getSessionId());
	    }
	    Peerreview peerreview = getPeerreviewByContentId(toolContentId);
	    Map<Long, String> emails = new EmailAnalysisBuilder(peerreview, session, ratingService,
		    peerreviewSessionDao, peerreviewUserDao, this, messageService).generateHTMLEmailsForSession();
	    for (Map.Entry<Long, String> entry : emails.entrySet()) {
		eventNotificationService.sendMessage(null, entry.getKey().intValue(),
			IEventNotificationService.DELIVERY_METHOD_MAIL, getResultsEmailSubject(peerreview),
			entry.getValue(), true);
	    }

	    session.setEmailsSent(true);
	    peerreviewSessionDao.update(session);

	    emailsSent += emails.size();
	}

	return emailsSent;
    }

    @Override
    public List<ExcelSheet> exportTeamReportSpreadsheet(Long toolContentId) {

	Peerreview peerreview = peerreviewDao.getByContentId(toolContentId);
	if (peerreview == null) {
	    log.warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return null;
	}

	return new SpreadsheetBuilder(peerreview, ratingService, peerreviewSessionDao, peerreviewUserDao,
		this).generateTeamReport();
    }

    @Override
    public int[] getNumberPossibleRatings(Long toolContentId, Long toolSessionId, Long userId) {
	int[] retValue = new int[2];

	ArrayList<Long> itemIds = new ArrayList<>(1);
	itemIds.add(userId);
	Map<Long, Long> numRatingsForUserMap = ratingService.countUsersRatedEachItem(toolContentId, toolSessionId,
		itemIds, -1);
	Long numRatingsForUser = numRatingsForUserMap.get(userId);
	retValue[0] = numRatingsForUser != null ? numRatingsForUser.intValue() : 0;

	int numUsersInSession = peerreviewUserDao.getCountUsersBySession(toolSessionId, -1L);
	Peerreview peerreview = peerreviewDao.getByContentId(toolContentId);
	retValue[1] = peerreview.isSelfReview() ? numUsersInSession : numUsersInSession - 1;
	return retValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, Map<PeerreviewUser, StyledCriteriaRatingDTO>> getRubricsData(SessionMap<String, Object> sessionMap,
	    RatingCriteria criteria, Collection<RatingCriteria> criterias) {
	List<GroupSummary> sessionList = (List<GroupSummary>) sessionMap.get(PeerreviewConstants.ATTR_SUMMARY_LIST);

	Map<Long, Map<PeerreviewUser, StyledCriteriaRatingDTO>> rubricsData = new HashMap<>();
	for (GroupSummary session : sessionList) {
	    Long toolSessionId = session.getSessionId();
	    Map<PeerreviewUser, StyledCriteriaRatingDTO> learnerData = getRubricsLearnerData(toolSessionId, criteria,
		    criterias);
	    rubricsData.put(toolSessionId, learnerData);
	}

	return rubricsData;
    }

    @Override
    public Map<PeerreviewUser, StyledCriteriaRatingDTO> getRubricsLearnerData(Long toolSessionId,
	    RatingCriteria criteria, Collection<RatingCriteria> criterias) {
	Map<PeerreviewUser, StyledCriteriaRatingDTO> learnerData = new TreeMap<>(
		Comparator.comparing(PeerreviewUser::getFirstName).thenComparing(PeerreviewUser::getLastName));

	criterias = criterias.stream()
		.filter(c -> criteria.getRatingCriteriaGroupId().equals(c.getRatingCriteriaGroupId()))
		.collect(Collectors.toList());
	Collection<Long> criteriaIds = criterias.stream()
		.collect(Collectors.mapping(RatingCriteria::getRatingCriteriaId, Collectors.toSet()));

	Collection<PeerreviewUser> learners = getUsersBySession(toolSessionId);
	List<Rating> ratings = ratingService.getRatingsByCriteriasAndItems(criteriaIds,
		learners.stream().collect(Collectors.mapping(PeerreviewUser::getUserId, Collectors.toSet())));

	for (PeerreviewUser learner : learners) {
	    Function<RatingCriteria, StyledCriteriaRatingDTO> dtoBuilder = c -> PeerreviewServiceImpl.getRubricsCriteriaDTO(
		    c, learner.getUserId().intValue(), true, ratings);
	    StyledCriteriaRatingDTO dto = PeerreviewServiceImpl.fillCriteriaGroup(criteria, criterias, dtoBuilder);
	    learnerData.put(learner, dto);
	}

	return learnerData;
    }

    public static StyledCriteriaRatingDTO fillCriteriaGroup(RatingCriteria targetCriteria,
	    Collection<RatingCriteria> allCriteria, Function<RatingCriteria, StyledCriteriaRatingDTO> dtoProducer) {
	Integer groupId = targetCriteria.getRatingCriteriaGroupId();
	StyledCriteriaRatingDTO result = null;
	List<StyledCriteriaRatingDTO> criteriaGroup = new LinkedList<>();
	for (RatingCriteria criteriaInGroup : allCriteria) {
	    if (!groupId.equals(criteriaInGroup.getRatingCriteriaGroupId())) {
		continue;
	    }

	    StyledCriteriaRatingDTO dto = dtoProducer.apply(criteriaInGroup);
	    if (criteriaInGroup.getRatingCriteriaId().equals(targetCriteria.getRatingCriteriaId())) {
		criteriaGroup.add(0, dto);
		result = dto;
		result.setCriteriaGroup(criteriaGroup);
	    } else {
		criteriaGroup.add(dto);
	    }
	}
	return result;
    }

    public static void removeGroupedCriteria(Collection<RatingCriteria> criteria) {
	Set<Integer> processedCriteriaGroups = new HashSet<>();
	Iterator<RatingCriteria> criteriaIter = criteria.iterator();
	while (criteriaIter.hasNext()) {
	    RatingCriteria criterion = criteriaIter.next();
	    if (criterion.getRatingCriteriaGroupId() != null) {
		if (processedCriteriaGroups.contains(criterion.getRatingCriteriaGroupId())) {
		    criteriaIter.remove();
		} else {
		    processedCriteriaGroups.add(criterion.getRatingCriteriaGroupId());
		}
	    }
	}
    }

    public static StyledCriteriaRatingDTO getRubricsCriteriaDTO(RatingCriteria criteria, Integer currentUserId,
	    boolean includeCurrentUser, Collection<Rating> ratings) {
	StyledCriteriaRatingDTO dto = new StyledCriteriaRatingDTO();
	dto.setRatingCriteria(criteria);
	List<StyledRatingDTO> ratingDtos = ratings.stream().filter(rating ->
			rating.getRatingCriteria().getRatingCriteriaId().equals(criteria.getRatingCriteriaId())
				&& rating.getItemId().equals(currentUserId.longValue()) && (includeCurrentUser
				|| !rating.getLearner().getUserId().equals(currentUserId)))
		.collect(Collectors.mapping(rating -> {
		    StyledRatingDTO ratingDto = new StyledRatingDTO(currentUserId.longValue());
		    if (rating.getRating() != null) {
			if (rating.getRating() % 1 > 0) {
			    // for 0.5 rating columns get exact value
			    ratingDto.setUserRating(rating.getRating().toString());
			} else {
			    // for whole points round them up so we get "1" instead of "1.0"
			    ratingDto.setUserRating(String.valueOf(rating.getRating().intValue()));
			}

		    }
		    ratingDto.setItemDescription(rating.getLearner().getFullName());
		    ratingDto.setItemDescription2(rating.getLearner().getUserId().toString());
		    return ratingDto;
		}, Collectors.toList()));

	dto.setRatingDtos(ratingDtos);
	return dto;
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************

    private Peerreview getDefaultPeerreview() throws PeerreviewApplicationException {
	Long defaultPeerreviewId = new Long(
		toolService.getToolDefaultContentIdBySignature(PeerreviewConstants.TOOL_SIGNATURE));
	if (defaultPeerreviewId.equals(0L)) {
	    String error = new StringBuilder("Could not retrieve default content id for this tool ").append(
		    PeerreviewConstants.TOOL_SIGNATURE).toString();
	    log.error(error);
	    throw new PeerreviewApplicationException(error);
	}
	Peerreview defaultPeerreview = getPeerreviewByContentId(defaultPeerreviewId);
	if (defaultPeerreview == null) {
	    String error = new StringBuilder("Could not retrieve default content id for this tool ").append(
			    PeerreviewConstants.TOOL_SIGNATURE).append(" Looking for id ").append(defaultPeerreviewId)
		    .toString();
	    log.error(error);
	    throw new PeerreviewApplicationException(error);
	}

	return defaultPeerreview;
    }

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Peerreview toolContentObj = peerreviewDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultPeerreview();
	    } catch (PeerreviewApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the peerreview tool");
	}

	// need to clone the Peer Review details, otherwise clearing the fields may update the database!
	toolContentObj = Peerreview.newInstance(toolContentObj, toolContentId);

	// don't export following fields
	for (LearnerItemRatingCriteria criteria : toolContentObj.getRatingCriterias()) {
	    criteria.setToolContentId(null);
	}
	toolContentObj.setCreatedBy(null);

	fillRubricsColumnHeaders(toolContentObj.getRatingCriterias());

	// set PeerreviewToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Peerreview.newInstance(toolContentObj, toolContentId);
	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, peerreviewToolContentHandler,
		    rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(PeerreviewImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, peerreviewToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Peerreview)) {
		throw new ImportToolContentException(
			"Import Share peerreview tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Peerreview toolContentObj = (Peerreview) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    Map<Integer, Integer> groupIdMap = new HashMap<>();
	    if (toolContentObj.getRatingCriterias() != null) {
		Integer nextRatingCriteriaGroupId = null;
		for (LearnerItemRatingCriteria criteria : toolContentObj.getRatingCriterias()) {
		    criteria.setToolContentId(toolContentId);

		    if (criteria.getRatingStyle().equals(RatingCriteria.RATING_STYLE_RUBRICS)) {
			int existingGroupId = criteria.getRatingCriteriaGroupId();
			Integer newGroupId = groupIdMap.get(existingGroupId);
			if (newGroupId == null) {
			    if (nextRatingCriteriaGroupId == null) {
				nextRatingCriteriaGroupId = ratingService.getNextRatingCriteriaGroupId();
			    } else {
				nextRatingCriteriaGroupId++;
			    }
			    newGroupId = nextRatingCriteriaGroupId;
			    groupIdMap.put(existingGroupId, newGroupId);

			    for (int columnIndex = 0;
				    columnIndex < criteria.getRubricsColumnHeaders().size(); columnIndex++) {
				RatingRubricsColumn columnHeader = new RatingRubricsColumn(columnIndex + 1,
					criteria.getRubricsColumnHeaders().get(columnIndex));
				columnHeader.setRatingCriteriaGroupId(newGroupId);
				peerreviewDao.insert(columnHeader);
			    }
			}

			criteria.setRatingCriteriaGroupId(newGroupId);
		    }
		}
	    }

	    // reset the user
	    PeerreviewUser user = peerreviewUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new PeerreviewUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setPeerreview(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    peerreviewDao.insertOrUpdate(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	return peerreviewOutputFactory.getToolOutputDefinitions(toolContentId, definitionType);
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the PeerreviewFiles tool seession");
	}

	Peerreview peerreview = null;
	if (fromContentId != null) {
	    peerreview = peerreviewDao.getByContentId(fromContentId);
	}
	if (peerreview == null) {
	    try {
		peerreview = getDefaultPeerreview();
	    } catch (PeerreviewApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Peerreview toContent = Peerreview.newInstance(peerreview, toContentId);
	peerreviewDao.insert(toContent);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getPeerreviewByContentId(toolContentId).getTitle();
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Peerreview peerreview = peerreviewDao.getByContentId(toolContentId);
	if (peerreview == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	peerreview.setDefineLater(false);
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getPeerreviewByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	List<PeerreviewSession> list = peerreviewSessionDao.getByContentId(toolContentId);
	Iterator<PeerreviewSession> iter = list.iterator();
	while (iter.hasNext()) {
	    PeerreviewSession session = iter.next();
	    int sessionUsersNumber = peerreviewUserDao.getCountUsersBySession(session.getSessionId());
	    if (sessionUsersNumber == 0) {
		log.debug("Peer Review isReadOnly called. Returning true. Count of users for session id "
			+ session.getSessionId() + " is " + sessionUsersNumber);
		return true;
	    } else {
		log.debug("Peer Review isReadOnly called. Count of users for session id " + session.getSessionId()
			+ " is 0");
	    }
	}
	log.debug("Peer Review isReadOnly called. Returning false.");
	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Peerreview peerreview = peerreviewDao.getByContentId(toolContentId);
	if (peerreview == null) {
	    log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	peerreviewDao.delete(peerreview);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (log.isDebugEnabled()) {
	    log.debug("Removing Peerreview content for user ID " + userId + " and toolContentId " + toolContentId);
	}

	Peerreview peerreview = peerreviewDao.getByContentId(toolContentId);
	if (peerreview == null) {
	    log.warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	ratingService.removeUserCommitsByContent(toolContentId, userId);

	List<PeerreviewSession> sessions = peerreviewSessionDao.getByContentId(toolContentId);
	for (PeerreviewSession session : sessions) {
	    PeerreviewUser user = peerreviewUserDao.getUserByUserIDAndSessionID(userId.longValue(),
		    session.getSessionId());
	    if (user != null) {
		peerreviewUserDao.delete(user);
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	PeerreviewSession session = new PeerreviewSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Peerreview peerreview = peerreviewDao.getByContentId(toolContentId);
	session.setPeerreview(peerreview);
	peerreviewSessionDao.insert(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	PeerreviewSession session = peerreviewSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(PeerreviewConstants.COMPLETED);
	    peerreviewSessionDao.update(session);
	} else {
	    log.error("Fail to leave tool Session.Could not find peerreview " + "session by given session id: "
		    + toolSessionId);
	    throw new DataMissingException(
		    "Fail to leave tool Session." + "Could not find peerreview session by given session id: "
			    + toolSessionId);
	}
	return toolService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public List<RatingCriteria> getRatingCriterias(Long toolContentId) {
	List<RatingCriteria> result = ratingService.getCriteriasByToolContentId(toolContentId);
	fillRubricsColumnHeaders(result);
	for (RatingCriteria ratingCriteria : result) {
	    Hibernate.initialize(ratingCriteria.getRubricsColumns());
	}
	return result;
    }

    private void fillRubricsColumnHeaders(Collection<? extends RatingCriteria> ratingCriterias) {
	for (RatingCriteria ratingCriteria : ratingCriterias) {
	    fillRubricsColumnHeaders(ratingCriteria);
	}
    }

    @Override
    public void fillRubricsColumnHeaders(RatingCriteria ratingCriteria) {
	if (ratingCriteria.getRatingStyle().equals(RatingCriteria.RATING_STYLE_RUBRICS)) {
	    ratingCriteria.setRubricsColumnHeaders(
		    ratingService.getRubricsColumnHeaders(ratingCriteria.getRatingCriteriaGroupId()));
	}
    }

    @Override
    public void saveRatingCriterias(HttpServletRequest request, Collection<RatingCriteria> oldCriterias,
	    Long toolContentId) {
	ratingService.saveRatingCriterias(request, oldCriterias, toolContentId);
    }

    @Override
    public boolean isCommentsEnabled(Long toolContentId) {
	return ratingService.isCommentsEnabled(toolContentId);
    }

    @Override
    public int getCommentsMinWordsLimit(Long toolContentId) {
	return ratingService.getCommentsMinWordsLimit(toolContentId);
    }

    @Override
    public List<RatingCriteria> getCriteriasByToolContentId(Long toolContentId) {
	return ratingService.getCriteriasByToolContentId(toolContentId);
    }

    @Override
    public List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Long toolSessionId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId) {
	return ratingService.getRatingCriteriaDtos(contentId, toolSessionId, itemIds, isCommentsByOtherUsersRequired,
		userId);
    }

    @Override
    public List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Long toolSessionId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId, boolean isCountUsersRatedEachItem) {
	List<ItemRatingDTO> itemRatingDTOs = getRatingCriteriaDtos(contentId, toolSessionId, itemIds,
		isCommentsByOtherUsersRequired, userId);

	if (isCountUsersRatedEachItem) {
	    Map<Long, Long> itemIdToRatedUsersCountMap = ratingService.countUsersRatedEachItem(contentId, toolSessionId,
		    itemIds, userId.intValue());

	    for (ItemRatingDTO itemRatingDTO : itemRatingDTOs) {
		Long itemId = itemRatingDTO.getItemId();

		int countUsersRatedEachItem = itemIdToRatedUsersCountMap.get(itemId).intValue();
		itemRatingDTO.setCountUsersRatedEachItem(countUsersRatedEachItem);
	    }
	}

	return itemRatingDTOs;
    }

    @Override
    public int getCountItemsRatedByUser(Long toolContentId, Integer userId) {
	return ratingService.getCountItemsRatedByUser(toolContentId, userId);
    }

    @Override
    public int getCountItemsRatedByUserByCriteria(final Long criteriaId, final Integer userId) {
	return ratingService.getCountItemsRatedByUserByCriteria(criteriaId, userId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	peerreviewSessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return peerreviewOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return peerreviewOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return peerreviewOutputFactory.getToolOutputs(name, this, toolContentId);
    }

    @Override
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	return null;
    }

    @Override
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	return false;
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setPeerreviewOutputFactory(PeerreviewOutputFactory peerreviewOutputFactory) {
	this.peerreviewOutputFactory = peerreviewOutputFactory;
    }

    public void setPeerreviewDao(PeerreviewDAO peerreviewDao) {
	this.peerreviewDao = peerreviewDao;
    }

    public void setPeerreviewSessionDao(PeerreviewSessionDAO peerreviewSessionDao) {
	this.peerreviewSessionDao = peerreviewSessionDao;
    }

    public void setPeerreviewToolContentHandler(IToolContentHandler peerreviewToolContentHandler) {
	this.peerreviewToolContentHandler = peerreviewToolContentHandler;
    }

    public void setPeerreviewUserDao(PeerreviewUserDAO peerreviewUserDao) {
	this.peerreviewUserDao = peerreviewUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setRatingService(IRatingService ratingService) {
	this.ratingService = ratingService;
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	// db doesn't have a start/finish date for learner, and session start/finish is null
	PeerreviewUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isSessionFinished()
		? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }

    // ****************** REST methods *************************

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    /**
     * Used by the Rest calls to create content. Mandatory fields in toolContentJSON: title, instructions, peerreview,
     * user fields firstName, lastName and loginName Peerreview must contain a JSONArray of JSONObject objects, which
     * have the following mandatory fields: title, description, type. It will create
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {

	Date updateDate = new Date();

	Peerreview peerreview = new Peerreview();
	peerreview.setContentId(toolContentID);
	peerreview.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	peerreview.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));
	peerreview.setCreated(updateDate);
	peerreview.setUpdated(updateDate);

	peerreview.setLockWhenFinished(
		JsonUtil.optBoolean(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));

	peerreview.setContentInUse(false);
	peerreview.setDefineLater(false);

	PeerreviewUser peerreviewUser = getUserByIDAndContent(userID.longValue(), toolContentID);
	if (peerreviewUser == null) {
	    peerreviewUser = new PeerreviewUser();
	    peerreviewUser.setFirstName(JsonUtil.optString(toolContentJSON, "firstName"));
	    peerreviewUser.setLastName(JsonUtil.optString(toolContentJSON, "lastName"));
	    peerreviewUser.setLoginName(JsonUtil.optString(toolContentJSON, "loginName"));
	    // peerreviewUser.setPeerreview(content);
	}

	peerreview.setCreatedBy(peerreviewUser);

	// not expecting to get these but add them in case an Authoring Template does want them
	peerreview.setMinimumRates(JsonUtil.optInt(toolContentJSON, "minimumRates", 0));
	peerreview.setMaximumRates(JsonUtil.optInt(toolContentJSON, "maximumRate", 0));
	peerreview.setMaximumRatesPerUser(JsonUtil.optInt(toolContentJSON, "maximumRatesPerUser", 0));
	peerreview.setShowRatingsLeftForUser(
		JsonUtil.optBoolean(toolContentJSON, "showRatingsLeftForUser", Boolean.TRUE));
	peerreview.setShowRatingsLeftByUser(
		JsonUtil.optBoolean(toolContentJSON, "showRatingsLeftByUser", Boolean.FALSE));
	peerreview.setSelfReview(JsonUtil.optBoolean(toolContentJSON, "notifyUsersOfResults", Boolean.FALSE));

	saveOrUpdatePeerreview(peerreview);

	// Criterias
	ArrayNode criterias = JsonUtil.optArray(toolContentJSON, "criterias");
	for (int i = 0; i < criterias.size(); i++) {
	    ObjectNode criteriaData = (ObjectNode) criterias.get(i);
	    // allowComment true, minWords defaults to 1. allowComment is false, minWords defaults to 0
	    boolean allowComment = JsonUtil.optBoolean(criteriaData, "commentsEnabled", false);
	    int minWords = JsonUtil.optInt(criteriaData, "minWordsInComment", allowComment ? 1 : 0);
	    ratingService.saveLearnerItemRatingCriteria(toolContentID, JsonUtil.optString(criteriaData, "title"),
		    JsonUtil.optInt(criteriaData, "orderId"), JsonUtil.optInt(criteriaData, "ratingStyle"),
		    allowComment, minWords);
	}

	saveOrUpdatePeerreview(peerreview);
    }

}