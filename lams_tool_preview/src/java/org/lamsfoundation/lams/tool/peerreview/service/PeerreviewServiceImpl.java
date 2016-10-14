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
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.dto.StyledCriteriaRatingDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
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
import org.lamsfoundation.lams.tool.peerreview.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.util.PeerreviewToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;

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
    private PeerreviewToolContentHandler peerreviewToolContentHandler;

    private MessageService messageService;

    // system services

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IRatingService ratingService;

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
	    PeerreviewServiceImpl.log.debug("Could not find the content by given ID:" + contentId);
	}
	return rs;
    }

    @Override
    public Peerreview getDefaultContent(Long contentId) throws PeerreviewApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    PeerreviewServiceImpl.log.error(error);
	    throw new PeerreviewApplicationException(error);
	}

	Peerreview defaultContent = getDefaultPeerreview();
	// save default content by given ID.
	Peerreview content = new Peerreview();
	content = Peerreview.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public void createUser(PeerreviewUser peerreviewUser) {
	peerreviewUserDao.saveObject(peerreviewUser);
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
    public List<Long> getUserIdsBySessionID(Long sessionId) {
	return peerreviewUserDao.getUserIdsBySessionID(sessionId);
    }

    @Override
    public void saveOrUpdatePeerreview(Peerreview peerreview) {
	peerreviewDao.saveObject(peerreview);
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
    public void saveOrUpdatePeerreviewSession(PeerreviewSession resSession) {
	peerreviewSessionDao.saveObject(resSession);
    }

    @Override
    public void markUserFinished(Long toolSessionId, Long userId) {
	PeerreviewUser user = peerreviewUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	peerreviewUserDao.saveObject(user);
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
	List<GroupSummary> groupList = new ArrayList<GroupSummary>();

	// get all sessions in a peerreview and retrieve all peerreview items under this session
	// plus initial peerreview items by author creating (resItemList)
	List<PeerreviewSession> sessionList = peerreviewSessionDao.getByContentId(contentId);

	for (PeerreviewSession session : sessionList) {
	    // one new group for one session.
	    GroupSummary group = new GroupSummary();
	    group.setSessionId(session.getSessionId());
	    group.setSessionName(session.getSessionName());

	    groupList.add(group);
	}

	return groupList;
    }

    @Override
    public List<ReflectDTO> getReflectList(Long contentId, Long sessionId) {

	List<ReflectDTO> reflections = new LinkedList<ReflectDTO>();

	List<PeerreviewUser> users = peerreviewUserDao.getBySessionID(sessionId);
	for (PeerreviewUser user : users) {

	    NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    PeerreviewConstants.TOOL_SIGNATURE, user.getUserId().intValue());
	    if (entry != null) {
		ReflectDTO ref = new ReflectDTO(user);
		ref.setReflect(entry.getEntry());
		Date postedDate = (entry.getLastModified() != null) ? entry.getLastModified()
			: entry.getCreateDate();
		ref.setDate(postedDate);
		reflections.add(ref);
	    }

	}

	return reflections;
    }

    @Override
    public List<PeerreviewUser> getUsersForTablesorter(final Long toolSessionId, final Long excludeUserId, int page,
	    int size, int sorting) {
	return peerreviewUserDao.getUsersForTablesorter(toolSessionId, excludeUserId, page, size, sorting);
    }

    @Override
    public int getCountUsersBySession(final Long toolSessionId, final Long excludeUserId) {
	return peerreviewUserDao.getCountUsersBySession(toolSessionId, excludeUserId);
    }

    @Override
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    @Override
    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    @Override
    public PeerreviewUser getUser(Long uid) {
	return (PeerreviewUser) peerreviewUserDao.getObject(PeerreviewUser.class, uid);
    }

    @Override
    public List<PeerreviewUser> getUsersBySession(Long toolSessionId) {
	return peerreviewUserDao.getBySessionID(toolSessionId);
    }

    @Override
    public List<PeerreviewUser> getUsersByContent(Long toolContentId) {
	return peerreviewUserDao.getByContentId(toolContentId);
    }

    @Override
    public boolean createUsersFromLesson(Long toolSessionId) throws Throwable {

	User currentUser = null;
	try {
	    boolean wasNotInSetAlready = creatingUsersForSessionIds.add(toolSessionId);
	    if (!wasNotInSetAlready) {
		return false;
	    }

//	    long start = System.currentTimeMillis();

	    PeerreviewSession session = getPeerreviewSessionBySessionId(toolSessionId);
	    int numberPotentialLearners = toolService.getCountUsersForActivity(toolSessionId);
	    int numberActualLearners = peerreviewUserDao.getCountUsersBySession(toolSessionId);
	    int numUsersCreated = 0;
	    if (numberActualLearners != numberPotentialLearners) {
		numUsersCreated = peerreviewUserDao.createUsersForSession(session);
	    }

//	    log.debug("Peer Review UserCreateThread " + toolSessionId + ": numUsersCreated "+numUsersCreated+" took: "
//	    	    + (System.currentTimeMillis() - start) + "ms.");

	    creatingUsersForSessionIds.remove(toolSessionId);
	    return true;
	} catch (Throwable e) {
	    creatingUsersForSessionIds.remove(toolSessionId);
	    String message = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
	    PeerreviewServiceImpl.log
		    .error("Exception thrown creating Peer Review users for session " + toolSessionId + " user id: "
			    + (currentUser != null ? currentUser.getUserId().toString() : "null") + "; " + message, e);
	    e.printStackTrace();
	    throw (e);
	}
    }

    @Override
    public int rateItems(RatingCriteria ratingCriteria, Integer userId, Map<Long, Float> newRatings) {
	return ratingService.rateItems(ratingCriteria, userId, newRatings);
    }

    @Override
    public void commentItem(RatingCriteria ratingCriteria, Integer userId, Long itemId, String comment)
    {
	ratingService.commentItem(ratingCriteria, userId, itemId, comment);
    }

    @Override
    public RatingCriteria getCriteriaByCriteriaId(Long ratingCriteriaId) {
	return ratingService.getCriteriaByCriteriaId(ratingCriteriaId);
    }

    @Override
    public StyledCriteriaRatingDTO getUsersRatingsCommentsByCriteriaIdDTO(Long toolContentId, Long toolSessionId, RatingCriteria criteria, 
	    Long currentUserId, boolean skipRatings, int sorting, boolean getAllUsers, boolean getByUser) {
	
	if ( skipRatings ) {
	    return ratingService.convertToStyledDTO(criteria, currentUserId, getAllUsers, null);
	}
	
	List<Object[]> rawData = peerreviewUserDao.getRatingsComments(toolContentId, toolSessionId, criteria, 
		currentUserId, null, null, sorting, getByUser, ratingService);

	for ( Object[] raw : rawData ) {
	    int len = raw.length;
	    StringBuilder description = new StringBuilder((String)raw[len-2] ).append(" ").append((String)raw[len-1]);	    
	    raw[len-1] = (Object) StringEscapeUtils.escapeCsv(description.toString());
	}
	// if !getByUser -> is get current user's ratings from other users -> 
	// convertToStyledJSON.getAllUsers needs to be true otherwise current user (the only one in the set!) is dropped
	return ratingService.convertToStyledDTO(criteria, currentUserId, !getByUser || getAllUsers, rawData);
    }

    @Override
    public JSONArray getUsersRatingsCommentsByCriteriaIdJSON(Long toolContentId, Long toolSessionId, RatingCriteria criteria, Long currentUserId, 
	    Integer page, Integer size, int sorting, boolean getAllUsers, boolean getByUser, boolean needRatesPerUser) throws JSONException {

	List<Object[]> rawData = peerreviewUserDao.getRatingsComments(toolContentId, toolSessionId, criteria, 
		currentUserId, page, size, sorting, getByUser, ratingService);
	
	for ( Object[] raw : rawData ) {
	    int len = raw.length;
	    StringBuilder description = new StringBuilder((String)raw[len-2] ).append(" ").append((String)raw[len-1]);	    
	    raw[len-1] = (Object) StringEscapeUtils.escapeCsv(description.toString());
	}
	// if !getByUser -> is get current user's ratings from other users -> 
	// convertToStyledJSON.getAllUsers needs to be true otherwise current user (the only one in the set!) is dropped
	return ratingService.convertToStyledJSON(criteria, currentUserId, !getByUser || getAllUsers, rawData, needRatesPerUser);
    }

    @Override
    public List<Object[]> getDetailedRatingsComments(Long toolContentId, Long toolSessionId, Long criteriaId, Long itemId ) {
	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);
	    
	// raw data: user_id, comment, rating, first_name, last_name 
	List<Object[]> rawData = peerreviewUserDao.getDetailedRatingsComments( toolContentId,  toolSessionId,  criteriaId,  itemId );
	for ( Object[] raw : rawData ) {
	    raw[2] = ( raw[2] == null ? null : numberFormat.format((Float) raw[2])); // format rating
	    // format name
	    StringBuilder description = new StringBuilder((String)raw[3] ).append(" ").append((String)raw[4]);	    
	    raw[4] = (Object) StringEscapeUtils.escapeCsv(description.toString());
	    
	}
	return rawData;
    }
    
    @Override
    public List<Object[]> getCommentsCounts(Long toolContentId, Long toolSessionId, RatingCriteria criteria,
	    Integer page, Integer size, int sorting) {
	
	List<Object[]> rawData = peerreviewUserDao.getCommentsCounts(toolContentId, toolSessionId, criteria, 
		page, size, sorting);
	
	// raw data: user_id, comment_count, first_name, last_name 
	for ( Object[] raw : rawData ) {
	    StringBuilder description = new StringBuilder((String)raw[2] ).append(" ").append((String)raw[3]);	    
	    raw[3] = (Object) StringEscapeUtils.escapeCsv(description.toString());
	}
	
	return rawData;
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    @Override
    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    @Override
    public List<PeerreviewStatisticsDTO> getStatistics(Long toolContentId) {
	return peerreviewDao.getStatistics(toolContentId);
    }
    
    // *****************************************************************************
    // private methods
    // *****************************************************************************

    private Peerreview getDefaultPeerreview() throws PeerreviewApplicationException {
	Long defaultPeerreviewId = getToolDefaultContentIdBySignature(PeerreviewConstants.TOOL_SIGNATURE);
	Peerreview defaultPeerreview = getPeerreviewByContentId(defaultPeerreviewId);
	if (defaultPeerreview == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    PeerreviewServiceImpl.log.error(error);
	    throw new PeerreviewApplicationException(error);
	}

	return defaultPeerreview;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws PeerreviewApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    PeerreviewServiceImpl.log.error(error);
	    throw new PeerreviewApplicationException(error);
	}
	return contentId;
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
	    if (toolContentObj.getRatingCriterias() != null) {
		for (LearnerItemRatingCriteria criteria : toolContentObj.getRatingCriterias()) {
		    criteria.setToolContentId(toolContentId);
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

	    peerreviewDao.saveObject(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	return new TreeMap<String, ToolOutputDefinition>();
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
	peerreviewDao.saveObject(toContent);
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
	    if (peerreviewUserDao.getCountUsersBySession(session.getSessionId(), -1L) == 0) {
		PeerreviewServiceImpl.log
			.debug("Peer Review isReadOnly called. Returning true. Count of users for session id "
				+ session.getSessionId() + " is "
				+ peerreviewUserDao.getCountUsersBySession(session.getSessionId(), -1L));
		return true;
	    } else {
		PeerreviewServiceImpl.log.debug("Peer Review isReadOnly called. Count of users for session id "
			+ session.getSessionId() + " is 0");
	    }
	}
	PeerreviewServiceImpl.log.debug("Peer Review isReadOnly called. Returning false.");
	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Peerreview peerreview = peerreviewDao.getByContentId(toolContentId);
	if (peerreview == null) {
	    PeerreviewServiceImpl.log
		    .warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (PeerreviewSession session : peerreviewSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, PeerreviewConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	peerreviewDao.delete(peerreview);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (PeerreviewServiceImpl.log.isDebugEnabled()) {
	    PeerreviewServiceImpl.log
		    .debug("Removing Peerreview content for user ID " + userId + " and toolContentId " + toolContentId);
	}

	Peerreview peerreview = peerreviewDao.getByContentId(toolContentId);
	if (peerreview == null) {
	    PeerreviewServiceImpl.log
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	List<PeerreviewSession> sessions = peerreviewSessionDao.getByContentId(toolContentId);
	for (PeerreviewSession session : sessions) {
	    PeerreviewUser user = peerreviewUserDao.getUserByUserIDAndSessionID(userId.longValue(),
		    session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			PeerreviewConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    peerreviewDao.removeObject(NotebookEntry.class, entry.getUid());
		}

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
	peerreviewSessionDao.saveObject(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    PeerreviewServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    PeerreviewServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	PeerreviewSession session = peerreviewSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(PeerreviewConstants.COMPLETED);
	    peerreviewSessionDao.saveObject(session);
	} else {
	    PeerreviewServiceImpl.log.error("Fail to leave tool Session.Could not find peerreview "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find peerreview session by given session id: " + toolSessionId);
	}
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public List<RatingCriteria> getRatingCriterias(Long toolContentId) {
	return ratingService.getCriteriasByToolContentId(toolContentId);
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
    public List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId) {
	return ratingService.getRatingCriteriaDtos(contentId, itemIds, isCommentsByOtherUsersRequired, userId);
    }

    @Override
    public List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId, boolean isCountUsersRatedEachItem) {
	List<ItemRatingDTO> itemRatingDTOs = getRatingCriteriaDtos(contentId, itemIds, isCommentsByOtherUsersRequired,
		userId);

	if (isCountUsersRatedEachItem) {
	    Map<Long, Long> itemIdToRatedUsersCountMap = ratingService.countUsersRatedEachItem(contentId, itemIds,
		    userId.intValue());

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
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

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
	return new TreeMap<String, ToolOutput>();
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return null;
    }
    
    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<ToolOutput>();
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setPeerreviewDao(PeerreviewDAO peerreviewDao) {
	this.peerreviewDao = peerreviewDao;
    }

    public void setPeerreviewSessionDao(PeerreviewSessionDAO peerreviewSessionDao) {
	this.peerreviewSessionDao = peerreviewSessionDao;
    }

    public void setPeerreviewToolContentHandler(PeerreviewToolContentHandler peerreviewToolContentHandler) {
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

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public void setRatingService(IRatingService ratingService) {
	this.ratingService = ratingService;
    }

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content. Mandatory fields in toolContentJSON: title, instructions, peerreview,
     * user fields firstName, lastName and loginName Peerreview must contain a JSONArray of JSONObject objects, which
     * have the following mandatory fields: title, description, type. If there are instructions for a peerreview, the
     * instructions are a JSONArray of Strings. There should be at least one peerreview object in the peerreview array.
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON)
	    throws JSONException {

	Date updateDate = new Date();

	Peerreview peerreview = new Peerreview();
	peerreview.setContentId(toolContentID);
	peerreview.setTitle(toolContentJSON.getString(RestTags.TITLE));
	peerreview.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));
	peerreview.setCreated(updateDate);

	peerreview.setLockWhenFinished(JsonUtil.opt(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	peerreview.setReflectOnActivity(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	peerreview.setReflectInstructions(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, (String) null));

	peerreview.setContentInUse(false);
	peerreview.setDefineLater(false);

	PeerreviewUser peerreviewUser = getUserByIDAndContent(userID.longValue(), toolContentID);
	if (peerreviewUser == null) {
	    peerreviewUser = new PeerreviewUser();
	    peerreviewUser.setFirstName(toolContentJSON.getString("firstName"));
	    peerreviewUser.setLastName(toolContentJSON.getString("lastName"));
	    peerreviewUser.setLoginName(toolContentJSON.getString("loginName"));
	    // peerreviewUser.setPeerreview(content);
	}

	peerreview.setCreatedBy(peerreviewUser);

	saveOrUpdatePeerreview(peerreview);

    }
}
