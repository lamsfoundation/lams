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


package org.lamsfoundation.lams.tool.kaltura.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaCommentDAO;
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaDAO;
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaItemDAO;
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaItemVisitDAO;
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaRatingDAO;
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaSessionDAO;
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaUserDAO;
import org.lamsfoundation.lams.tool.kaltura.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.kaltura.dto.NotebookEntryDTO;
import org.lamsfoundation.lams.tool.kaltura.model.Kaltura;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaComment;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaItem;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaItemVisitLog;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaRating;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaSession;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaUser;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaConstants;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaException;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaImportContentVersionFilter;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaItemComparator;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the IKalturaService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class KalturaService implements ToolSessionManager, ToolContentManager, IKalturaService {

    private static Logger logger = Logger.getLogger(KalturaService.class.getName());

    private IKalturaDAO kalturaDao = null;

    private IKalturaItemDAO kalturaItemDao = null;

    private IKalturaItemVisitDAO kalturaItemVisitDao = null;

    private IKalturaCommentDAO kalturaCommentDao = null;

    private IKalturaRatingDAO kalturaRatingDao = null;

    private IKalturaSessionDAO kalturaSessionDao = null;

    private IKalturaUserDAO kalturaUserDao = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IUserManagementService userManagementService;

    private MessageService messageService;

    private IToolContentHandler kalturaToolContentHandler = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private KalturaOutputFactory kalturaOutputFactory;

    public KalturaService() {
	super();
    }

    /* ************ Methods from ToolSessionManager ************* */
    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (KalturaService.logger.isDebugEnabled()) {
	    KalturaService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	KalturaSession session = new KalturaSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	// learner starts
	Kaltura kaltura = kalturaDao.getByContentId(toolContentId);
	session.setKaltura(kaltura);
	kalturaSessionDao.saveOrUpdate(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
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
	kalturaSessionDao.deleteBySessionID(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getKalturaOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getKalturaOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }
    
    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<ToolOutput>();
    }
    
    @Override
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	return null;
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    /* ************ Methods from ToolContentManager ************************* */

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (KalturaService.logger.isDebugEnabled()) {
	    KalturaService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Kaltura fromContent = null;
	if (fromContentId != null) {
	    fromContent = kalturaDao.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Kaltura toContent = Kaltura.newInstance(fromContent, toContentId);

	Set items = toContent.getKalturaItems();
	if (items != null) {
	    Iterator iter = items.iterator();
	    while (iter.hasNext()) {
		KalturaItem item = (KalturaItem) iter.next();
		if (!item.isCreateByAuthor()) {
		    iter.remove();
		}
	    }
	}

	kalturaDao.saveOrUpdate(toContent);
	for (KalturaItem item : (Set<KalturaItem>) items) {
	    kalturaUserDao.saveOrUpdate(item.getCreatedBy());
	    kalturaItemDao.insert(item);
	}
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Kaltura kaltura = kalturaDao.getByContentId(toolContentId);
	if (kaltura == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	kaltura.setDefineLater(false);
	kalturaDao.saveOrUpdate(kaltura);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Kaltura kaltura = kalturaDao.getByContentId(toolContentId);
	if (kaltura == null) {
	    KalturaService.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (KalturaSession session : (Set<KalturaSession>) kaltura.getKalturaSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, KalturaConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	kalturaDao.delete(kaltura);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (KalturaService.logger.isDebugEnabled()) {
	    KalturaService.logger.debug("This tool does not support learner content removing yet.");
	}
    }

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Kaltura kaltura = kalturaDao.getByContentId(toolContentId);
	if (kaltura == null) {
	    kaltura = getDefaultContent();
	}
	if (kaltura == null) {
	    throw new DataMissingException("Unable to find default content for the kaltura tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	kaltura = Kaltura.newInstance(kaltura, toolContentId);
	kaltura.setKalturaSessions(null);
	Set<KalturaItem> items = kaltura.getKalturaItems();
	for (KalturaItem item : items) {
	    item.setKalturaUid(null);
	}
	try {
	    exportContentService.exportToolContent(toolContentId, kaltura, kalturaToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(KalturaImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, kalturaToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Kaltura)) {
		throw new ImportToolContentException(
			"Import Kaltura tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Kaltura kaltura = (Kaltura) toolPOJO;

	    // reset it to new toolContentId
	    kaltura.setToolContentId(toolContentId);
	    KalturaUser user = kalturaUserDao.getByUserIdAndContentId(new Long(newUserUid.longValue()), toolContentId);
	    if (user == null) {
		user = new KalturaUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setKaltura(kaltura);
	    }
	    kaltura.setCreatedBy(user);

	    // reset all assessmentquestion createBy user
	    Set<KalturaItem> items = kaltura.getKalturaItems();
	    for (KalturaItem item : items) {
		item.setCreatedBy(user);
	    }

	    kalturaDao.saveOrUpdate(kaltura);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Kaltura kaltura = kalturaDao.getByContentId(toolContentId);
	if (kaltura == null) {
	    kaltura = getDefaultContent();
	}
	return getKalturaOutputFactory().getToolOutputDefinitions(kaltura, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getKalturaByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getKalturaByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	Kaltura kaltura = kalturaDao.getByContentId(toolContentId);
	for (KalturaSession session : (Set<KalturaSession>) kaltura.getKalturaSessions()) {
	    if (session.getKalturaUsers().isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    /* ********** IKalturaService Methods ********************************* */
    @Override
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    @Override
    public NotebookEntry getEntry(Long sessionId, Integer userId) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		KalturaConstants.TOOL_SIGNATURE, userId);
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
    public List<NotebookEntryDTO> getReflectList(Kaltura kaltura) {
	List<NotebookEntryDTO> reflectList = new LinkedList<NotebookEntryDTO>();

	Set<KalturaSession> sessions = kaltura.getKalturaSessions();
	for (KalturaSession session : sessions) {
	    Long sessionId = session.getSessionId();
	    // get all users in this session
	    Set<KalturaUser> users = session.getKalturaUsers();
	    for (KalturaUser user : users) {

		NotebookEntry entry = getEntry(sessionId, user.getUserId().intValue());
		if (entry != null) {
		    NotebookEntryDTO notebookEntryDTO = new NotebookEntryDTO(entry);
		    notebookEntryDTO.setFullName(user.getFirstName() + " " + user.getLastName());
		    notebookEntryDTO.setUserId(user.getUserId());
		    Date postedDate = (entry.getLastModified() != null) ? entry.getLastModified()
			    : entry.getCreateDate();
		    notebookEntryDTO.setLastModified(postedDate);
		    reflectList.add(notebookEntryDTO);
		}

	    }
	}

	return reflectList;
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws KalturaException {
	KalturaUser user = kalturaUserDao.getByUserIdAndSessionId(userId, toolSessionId);
	user.setFinishedActivity(true);
	kalturaUserDao.saveOrUpdate(user);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new KalturaException(e);
	} catch (ToolException e) {
	    throw new KalturaException(e);
	}
	return nextUrl;
    }

    @Override
    public AverageRatingDTO rateMessage(Long itemUid, Long userId, Long toolSessionId, float rating) {
	KalturaUser user = getUserByUserIdAndSessionId(userId, toolSessionId);
	KalturaRating itemRating = kalturaRatingDao.getKalturaRatingByItemAndUser(itemUid, userId);
	KalturaItem item = getKalturaItem(itemUid);

	//persist KalturaRating changes in DB
	if (itemRating == null) { // add
	    itemRating = new KalturaRating();
	    itemRating.setCreateBy(user);
	    itemRating.setKalturaItem(item);
	}
	itemRating.setRating(rating);
	kalturaRatingDao.insertOrUpdate(itemRating);

	//to make available new changes be visible in jsp page
	return kalturaRatingDao.getAverageRatingDtoByItem(itemUid, toolSessionId);
    }

    @Override
    public AverageRatingDTO getAverageRatingDto(Long itemUid, Long sessionId) {
	return kalturaRatingDao.getAverageRatingDtoByItem(itemUid, sessionId);
    }

    @Override
    public void deleteKalturaItem(Long uid) {
	kalturaItemDao.deleteById(KalturaItem.class, uid);
    }

    @Override
    public KalturaItem getKalturaItem(Long itemUid) {
	return kalturaItemDao.getByUid(itemUid);
    }

    @Override
    public void saveKalturaItem(KalturaItem item) {
	kalturaItemDao.insertOrUpdate(item);
    }

    @Override
    public Set<KalturaItem> getGroupItems(Long toolContentId, Long toolSessionId, Long useId, boolean isMonitoring) {
	TreeSet<KalturaItem> groupItems = new TreeSet<KalturaItem>(new KalturaItemComparator());

	Kaltura kaltura = getKalturaByContentId(toolContentId);
	Set<KalturaItem> allItems = kaltura.getKalturaItems();

	for (KalturaItem item : allItems) {
	    //hide hidden items from learner and ignore this parameter for teacher
	    boolean isHidden = isMonitoring || (!isMonitoring && !item.isHidden());

	    //remove hidden
	    if (isHidden &&
	    //show authored items
		    (item.isCreateByAuthor()
			    //user should see his own items
			    || item.getCreatedBy().getUserId().equals(useId)
			    //filter items from other groups
			    || (item.getCreatedBy().getSession().getSessionId().equals(toolSessionId)
				    && (kaltura.isAllowSeeingOtherUsersRecordings() || isMonitoring)))) {

		groupItems.add(item);
	    }
	}

	return groupItems;
    }

    @Override
    public void logItemWatched(Long itemUid, Long userId, Long toolSessionId) {
	KalturaItemVisitLog log = kalturaItemVisitDao.getKalturaItemLog(itemUid, userId);
	if (log == null) {
	    log = new KalturaItemVisitLog();
	    KalturaItem item = kalturaItemDao.getByUid(itemUid);
	    log.setKalturaItem(item);
	    KalturaUser user = kalturaUserDao.getByUserIdAndSessionId(userId, toolSessionId);
	    log.setUser(user);
	    log.setComplete(false);
	    log.setSessionId(toolSessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	    kalturaItemVisitDao.insert(log);
	}
    }

    @Override
    public void markItem(Long itemUid, Long mark) {
	KalturaItem item = kalturaItemDao.getByUid(itemUid);
	item.setMark(mark);
	kalturaItemDao.update(item);
    }

    @Override
    public void hideItem(Long itemUid, boolean isHiding) {
	KalturaItem item = kalturaItemDao.getByUid(itemUid);
	if (item != null) {
	    item.setHidden(isHiding);
	    kalturaItemDao.update(item);
	}
    }

    @Override
    public void hideComment(Long commentUid, boolean isHiding) {
	KalturaComment comment = kalturaCommentDao.getCommentByUid(commentUid);
	if (comment != null) {
	    comment.setHidden(isHiding);
	    kalturaCommentDao.update(comment);
	}
    }

    @Override
    public int getNumberViewedVideos(Long toolSessionId, Long userId) {
	return kalturaItemVisitDao.getUserViewLogCount(toolSessionId, userId);
    }

    @Override
    public int getNumberUploadedVideos(Long toolSessionId, Long userId) {
	return kalturaItemDao.getItemsCountByUser(toolSessionId, userId);
    }

    @Override
    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    KalturaService.logger.error(error);
	    throw new KalturaException(error);
	}
	return toolContentId;
    }

    @Override
    public Kaltura getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(KalturaConstants.TOOL_SIGNATURE);
	Kaltura defaultContent = getKalturaByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    KalturaService.logger.error(error);
	    throw new KalturaException(error);
	}
	return defaultContent;
    }

    @Override
    public Kaltura copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Kaltura tools default content: + " + "newContentID is null";
	    KalturaService.logger.error(error);
	    throw new KalturaException(error);
	}

	Kaltura defaultContent = getDefaultContent();
	// create new kaltura using the newContentID
	Kaltura newContent = new Kaltura();
	newContent = Kaltura.newInstance(defaultContent, newContentID);
	kalturaDao.saveOrUpdate(newContent);
	return newContent;
    }

    @Override
    public Kaltura getKalturaByContentId(Long toolContentID) {
	Kaltura kaltura = kalturaDao.getByContentId(toolContentID);
	if (kaltura == null) {
	    KalturaService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return kaltura;
    }

    @Override
    public KalturaSession getSessionBySessionId(Long toolSessionId) {
	KalturaSession kalturaSession = kalturaSessionDao.getBySessionId(toolSessionId);
	if (kalturaSession == null) {
	    KalturaService.logger.debug("Could not find the kaltura session with toolSessionID:" + toolSessionId);
	}
	return kalturaSession;
    }

    @Override
    public KalturaUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return kalturaUserDao.getByUserIdAndSessionId(userId, toolSessionId);
    }

    @Override
    public KalturaUser getUserByUid(Long uid) {
	return kalturaUserDao.getByUid(uid);
    }

    @Override
    public KalturaUser getUserByUserIdAndContentId(Long userId, Long contentId) {
	return kalturaUserDao.getByUserIdAndContentId(userId, contentId);
    }

    @Override
    public void saveOrUpdateKaltura(Kaltura kaltura) {
	kalturaDao.saveOrUpdate(kaltura);
    }

    @Override
    public void saveOrUpdateKalturaSession(KalturaSession kalturaSession) {
	kalturaSessionDao.saveOrUpdate(kalturaSession);
    }

    @Override
    public void saveOrUpdateKalturaUser(KalturaUser kalturaUser) {
	kalturaUserDao.saveOrUpdate(kalturaUser);
    }

    @Override
    public KalturaUser createKalturaUser(UserDTO user, KalturaSession kalturaSession) {
	KalturaUser kalturaUser = new KalturaUser(user, kalturaSession);
	saveOrUpdateKalturaUser(kalturaUser);
	return kalturaUser;
    }

    public IAuditService getAuditService() {
	return auditService;
    }

    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

    @Override
    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    // =========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public void setKalturaDao(IKalturaDAO kalturaDAO) {
	this.kalturaDao = kalturaDAO;
    }

    public void setKalturaItemDao(IKalturaItemDAO kalturaItemDAO) {
	this.kalturaItemDao = kalturaItemDAO;
    }

    public void setKalturaItemVisitDao(IKalturaItemVisitDAO kalturaItemVisitDAO) {
	this.kalturaItemVisitDao = kalturaItemVisitDAO;
    }

    public void setKalturaCommentDao(IKalturaCommentDAO kalturaCommentDAO) {
	this.kalturaCommentDao = kalturaCommentDAO;
    }

    public void setKalturaRatingDao(IKalturaRatingDAO kalturaRatingDAO) {
	this.kalturaRatingDao = kalturaRatingDAO;
    }

    public void setKalturaToolContentHandler(IToolContentHandler kalturaToolContentHandler) {
	this.kalturaToolContentHandler = kalturaToolContentHandler;
    }

    public void setKalturaSessionDao(IKalturaSessionDAO sessionDAO) {
	kalturaSessionDao = sessionDAO;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public void setKalturaUserDao(IKalturaUserDAO userDAO) {
	kalturaUserDao = userDAO;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
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

    public KalturaOutputFactory getKalturaOutputFactory() {
	return kalturaOutputFactory;
    }

    public void setKalturaOutputFactory(KalturaOutputFactory kalturaOutputFactory) {
	this.kalturaOutputFactory = kalturaOutputFactory;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
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
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getKalturaOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
    
    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	KalturaUser learner = getUserByUserIdAndSessionId(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Kaltura kaltura = getSessionBySessionId(toolSessionId).getKaltura();
	Date startDate = null;
	Date endDate = null;
	Set<KalturaItem> allItems = kaltura.getKalturaItems();
	for (KalturaItem item : allItems) {
	    if (!item.isCreateByAuthor() && item.getCreatedBy().getUserId() == learnerId) {
		Date newDate = item.getCreateDate();
		if (newDate != null) {
		    if (startDate == null || newDate.before(startDate))
			startDate = newDate;
		    if (endDate == null || newDate.after(endDate))
			endDate = newDate;
		}
	    }
	}

	if (learner.isFinishedActivity())
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, startDate, endDate);
	else
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, startDate, null);
    }
    
}
