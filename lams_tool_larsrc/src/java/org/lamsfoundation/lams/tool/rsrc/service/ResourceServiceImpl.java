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

package org.lamsfoundation.lams.tool.rsrc.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.rating.RatingException;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.rsrc.ResourceConstants;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceItemVisitDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceSessionDAO;
import org.lamsfoundation.lams.tool.rsrc.dao.ResourceUserDAO;
import org.lamsfoundation.lams.tool.rsrc.dto.ResourceItemDTO;
import org.lamsfoundation.lams.tool.rsrc.dto.SessionDTO;
import org.lamsfoundation.lams.tool.rsrc.dto.VisitLogDTO;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemVisitLog;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceItemComparator;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.lamsfoundation.lams.web.util.AttributeNames;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Dapeng.Ni
 */
public class ResourceServiceImpl implements IResourceService, ToolContentManager, ToolSessionManager, ToolRestManager {
    private static Logger log = Logger.getLogger(ResourceServiceImpl.class.getName());

    private ResourceDAO resourceDao;

    private ResourceItemDAO resourceItemDao;

    private ResourceUserDAO resourceUserDao;

    private ResourceSessionDAO resourceSessionDao;

    private ResourceItemVisitDAO resourceItemVisitDao;

    // tool service
    private IToolContentHandler resourceToolContentHandler;

    private MessageService messageService;

    // system services

    private ILamsToolService toolService;

    private ILogEventService logEventService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private IEventNotificationService eventNotificationService;

    private IRatingService ratingService;

    private ResourceOutputFactory resourceOutputFactory;

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public Resource getResourceByContentId(Long contentId) {
	Resource rs = resourceDao.getByContentId(contentId);
	return rs;
    }

    @Override
    public Resource getDefaultContent(Long contentId) throws ResourceApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new ResourceApplicationException(error);
	}

	Resource defaultContent = getDefaultResource();
	// save default content by given ID.
	Resource content = new Resource();
	content = Resource.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public List<ResourceItem> getAuthoredItems(Long resourceUid) {
	List<ResourceItem> items = resourceItemDao.getAuthoringItems(resourceUid);
	for (ResourceItem item : items) {
	    item.setFileDisplayUuid(resourceToolContentHandler.getFileUuid(item.getFileUuid()));
	}
	return items;
    }

    @Override
    public void createUser(ResourceUser resourceUser) {
	resourceUserDao.saveObject(resourceUser);
    }

    @Override
    public ResourceUser getUserByIDAndContent(Long userId, Long contentId) {
	return resourceUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public ResourceUser getUserByIDAndSession(Long userId, Long sessionId) {
	return resourceUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public void deleteFromRepository(Long fileUuid, Long fileVersionId)
	    throws InvalidParameterException, RepositoryCheckedException {
	resourceToolContentHandler.deleteFile(fileUuid);
    }

    @Override
    public void saveOrUpdateResource(Resource resource) {
	resourceDao.saveObject(resource);
    }

    @Override
    public void deleteResourceItem(Long uid) {
	resourceItemDao.removeObject(ResourceItem.class, uid);
    }

    @Override
    public List<ResourceItem> getResourceItemsBySessionId(Long sessionId) {
	ResourceSession session = resourceSessionDao.getSessionBySessionId(sessionId);
	if (session == null) {
	    log.error("Failed get ResourceSession by ID [" + sessionId + "]");
	    return null;
	}
	// add resource items from Authoring
	Resource resource = session.getResource();
	List<ResourceItem> items = new ArrayList<>();
	items.addAll(resource.getResourceItems());

	// add resource items from ResourceSession
	items.addAll(session.getResourceItems());
	for (ResourceItem item : items) {
	    item.setFileDisplayUuid(resourceToolContentHandler.getFileUuid(item.getFileUuid()));
	}

	return items;
    }

    @Override
    public Resource getResourceBySessionId(Long sessionId) {
	ResourceSession session = resourceSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getResource().getContentId();
	Resource res = resourceDao.getByContentId(contentId);
	int miniView = res.getMiniViewResourceNumber();
	// construct dto fields;
	res.setMiniViewNumberStr(
		messageService.getMessage("label.learning.minimum.review", new Object[] { new Integer(miniView) }));
	return res;
    }

    @Override
    public ResourceSession getResourceSessionBySessionId(Long sessionId) {
	return resourceSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public void saveOrUpdateResourceSession(ResourceSession resSession) {
	resourceSessionDao.saveObject(resSession);
    }

    @Override
    public void retrieveComplete(SortedSet<ResourceItem> resourceItemList, ResourceUser user) {
	for (ResourceItem item : resourceItemList) {
	    ResourceItemVisitLog log = resourceItemVisitDao.getResourceItemLog(item.getUid(), user.getUserId());
	    if (log == null) {
		item.setComplete(false);
	    } else {
		item.setComplete(log.isComplete());
	    }
	}
    }

    @Override
    public void setItemComplete(Long resourceItemUid, Long userId, Long sessionId) {
	ResourceItemVisitLog log = resourceItemVisitDao.getResourceItemLog(resourceItemUid, userId);
	if (log == null) {
	    log = new ResourceItemVisitLog();
	    ResourceItem item = resourceItemDao.getByUid(resourceItemUid);
	    log.setResourceItem(item);
	    ResourceUser user = resourceUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	    log.setUser(user);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	}
	log.setComplete(true);
	log.setCompleteDate(new Timestamp(new Date().getTime()));
	resourceItemVisitDao.saveObject(log);
    }

    @Override
    public void setItemAccess(Long resourceItemUid, Long userId, Long sessionId) {
	ResourceItemVisitLog log = resourceItemVisitDao.getResourceItemLog(resourceItemUid, userId);
	if (log == null) {
	    log = new ResourceItemVisitLog();
	    ResourceItem item = resourceItemDao.getByUid(resourceItemUid);
	    log.setResourceItem(item);
	    ResourceUser user = resourceUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	    log.setUser(user);
	    log.setComplete(false);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	    resourceItemVisitDao.saveObject(log);
	}
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws ResourceApplicationException {
	ResourceUser user = resourceUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	resourceUserDao.saveObject(user);

	// ResourceSession session = resourceSessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(ResourceConstants.COMPLETED);
	// resourceSessionDao.saveObject(session);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new ResourceApplicationException(e);
	} catch (ToolException e) {
	    throw new ResourceApplicationException(e);
	}
	return nextUrl;
    }

    @Override
    public int checkMiniView(Long toolSessionId, Long userUid) {
	int miniView = resourceItemVisitDao.getUserViewLogCount(toolSessionId, userUid);
	ResourceSession session = resourceSessionDao.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    log.error("Failed get session by ID [" + toolSessionId + "]");
	    return 0;
	}
	int reqView = session.getResource().getMiniViewResourceNumber();

	return reqView - miniView;
    }

    @Override
    public ResourceItem getResourceItemByUid(Long itemUid) {
	ResourceItem item = resourceItemDao.getByUid(itemUid);
	item.setFileDisplayUuid(resourceToolContentHandler.getFileUuid(item.getFileUuid()));
	return item;
    }

    @Override
    public List<SessionDTO> getSummary(Long contentId) {
	List<SessionDTO> groupList = new ArrayList<>();

	Resource resource = resourceDao.getByContentId(contentId);

	// get all sessions in a resource and retrieve all resource items under this session
	// plus initial resource items by author creating (resItemList)
	List<ResourceSession> sessionList = resourceSessionDao.getByContentId(contentId);

	for (ResourceSession session : sessionList) {
	    // one new group for one session.
	    SessionDTO group = new SessionDTO();
	    group.setSessionId(session.getSessionId());
	    group.setSessionName(session.getSessionName());

	    Set<ResourceItem> items = new TreeSet<>(new ResourceItemComparator());
	    // firstly, put all initial resource item into this group.
	    items.addAll(resource.getResourceItems());
	    // add this session's resource items
	    items.addAll(session.getResourceItems());

	    // item ids of items that could be rated.
	    List<Long> itemsToRate = new ArrayList<>();

	    // get all item which is accessed by users in this session
	    Map<Long, Integer> visitCountMap = resourceItemVisitDao.getSummary(contentId, session.getSessionId());
	    boolean allowComments = false;
	    for (ResourceItem item : items) {
		ResourceItemDTO resourceItemDTO = new ResourceItemDTO(item);
		// set viewNumber according visit log
		if (visitCountMap.containsKey(item.getUid())) {
		    resourceItemDTO.setViewNumber(visitCountMap.get(item.getUid()).intValue());
		}
		group.getItems().add(resourceItemDTO);
		if (item.isAllowRating()) {
		    itemsToRate.add(item.getUid());
		}
		allowComments = allowComments || item.isAllowComments();
	    }

	    List<ItemRatingDTO> itemRatingDtos = null;
	    if (itemsToRate.size() > 0) {
		itemRatingDtos = ratingService.getRatingCriteriaDtos(contentId, session.getSessionId(), itemsToRate,
			false, -1L);
		group.setAllowRating(true);
	    } else {
		group.setAllowRating(false);
	    }

	    for (ResourceItemDTO item : group.getItems()) {
		if (item.isAllowRating()) {
		    // find corresponding itemRatingDto
		    for (ItemRatingDTO ratingDTO : itemRatingDtos) {
			if (item.getItemUid().equals(ratingDTO.getItemId())) {
			    item.setRatingDTO(ratingDTO);
			    break;
			}
		    }
		}
	    }

	    group.setAllowComments(allowComments);

	    groupList.add(group);
	}

	if (groupList.size() == 0) {
	    // no sessions but we still need to be able to view the resources in monitoring
	    groupList.add(createAuthoredItemsGroupList(contentId, resource));
	}
	return groupList;
    }

    private SessionDTO createAuthoredItemsGroupList(Long contentId, Resource resource) {
	SessionDTO group = new SessionDTO();
	group.setSessionId(0L);
	group.setSessionName("");

	Set<ResourceItem> items = new TreeSet<>(new ResourceItemComparator());
	// get the authored items
	items.addAll(resource.getResourceItems());

	// get all item which is accessed by users in this session
	for (ResourceItem item : items) {
	    ResourceItemDTO resourceItemDTO = new ResourceItemDTO(item);
	    group.getItems().add(resourceItemDTO);
	    if (item.isAllowRating()) {
		group.setAllowRating(true);
	    }
	    if (item.isAllowComments()) {
		group.setAllowComments(true);
	    }
	}

	return group;
    }

    @Override
    public List<ResourceUser> getUserListBySessionItem(Long sessionId, Long itemUid) {
	List<ResourceItemVisitLog> logList = resourceItemVisitDao.getResourceItemLogBySession(sessionId, itemUid);
	List<ResourceUser> userList = new ArrayList(logList.size());
	for (ResourceItemVisitLog visit : logList) {
	    ResourceUser user = visit.getUser();
	    user.setAccessDate(visit.getAccessDate());
	    user.setCompleteDate(visit.getCompleteDate());
	    Date timeTaken = ((visit.getCompleteDate() != null) && (visit.getAccessDate() != null)) ? new Date(
		    visit.getCompleteDate().getTime() - visit.getAccessDate().getTime()) : null;
	    user.setTimeTaken(timeTaken);
	    userList.add(user);
	}
	return userList;
    }

    @Override
    public List<VisitLogDTO> getPagedVisitLogsBySessionAndItem(Long sessionId, Long itemUid, int page, int size,
	    String sortBy, String sortOrder, String searchString) {
	return resourceItemVisitDao.getPagedVisitLogsBySessionAndItem(sessionId, itemUid, page, size, sortBy, sortOrder,
		searchString, userManagementService);
    }

    @Override
    public int getCountVisitLogsBySessionAndItem(Long sessionId, Long itemUid, String searchString) {
	return resourceItemVisitDao.getCountVisitLogsBySessionAndItem(sessionId, itemUid, searchString);
    }

    @Override
    public void setItemVisible(Long itemUid, Long sessionId, Long contentId, boolean visible) {
	ResourceItem item = resourceItemDao.getByUid(itemUid);
	if (item != null) {
	    // createBy should be null for system default value.
	    Long userId = 0L;
	    String loginName = "No user";
	    if (item.getCreateBy() != null) {
		userId = item.getCreateBy().getUserId();
		loginName = item.getCreateBy().getLoginName();
	    }
	    Long toolContentId = contentId;
	    if (toolContentId == null) {
		ResourceSession session = resourceSessionDao.getSessionBySessionId(sessionId);
		if (session != null) {
		    toolContentId = session.getResource().getContentId();
		} else {
		    log.error("setItemVisible: Failed get ResourceSession by ID [" + sessionId
			    + "]. Audit log entry will be created but will be missing tool content id");
		}
	    }
	    if (visible) {
		logEventService.logShowLearnerContent(userId, loginName, toolContentId, item.toString());
	    } else {
		logEventService.logHideLearnerContent(userId, loginName, toolContentId, item.toString());
	    }
	    item.setHide(!visible);
	    resourceItemDao.saveObject(item);
	}
    }

    @Override
    public ResourceUser getUser(Long uid) {
	return (ResourceUser) resourceUserDao.getObject(ResourceUser.class, uid);
    }

    @Override
    public void notifyTeachersOnAssigmentSumbit(long itemUid) {
	ResourceItem item = getResourceItemByUid(itemUid);
	ResourceUser resourceUser = item.getCreateBy();
	ResourceSession session = resourceUser.getSession();

	String userName = new StringBuilder().append(
			StringUtils.isBlank(resourceUser.getFirstName()) ? "" : resourceUser.getFirstName() + " ")
		.append(StringUtils.isBlank(resourceUser.getLastName()) ? "" : resourceUser.getLastName() + " ")
		.append("(").append(resourceUser.getLoginName()).append(")").toString();
	String resourceType = getLocalisedMessage(item.getType() == ResourceConstants.RESOURCE_TYPE_URL
		? "label.authoring.basic.resource.url"
		: "label.authoring.basic.resource.file", new Object[] {});

	String url = null;
	StringBuilder link = new StringBuilder("<a href='");

	if (item.getType() == ResourceConstants.RESOURCE_TYPE_URL) {
	    url = item.getUrl();
	} else {
	    url = new StringBuilder(WebUtil.getBaseServerURL()).append("/lams/tool/larsrc11/reviewItem.do?")
		    .append(AttributeNames.ATTR_MODE).append("=").append(ToolAccessMode.TEACHER.toString()).append("&")
		    .append(ResourceConstants.ATTR_TOOL_SESSION_ID).append("=").append(session.getSessionId())
		    .append("&").append(ResourceConstants.ATTR_RESOURCE_ITEM_UID).append("=").append(itemUid)
		    .toString();
	}

	link.append(url).append("'>").append(url).append("</a>");

	String subject = getLocalisedMessage("event.assigment.submit.subject", new Object[] {});
	String message = getLocalisedMessage("event.assigment.submit.body",
		new Object[] { userName, resourceType, link.toString() });

	eventNotificationService.notifyLessonMonitors(session.getSessionId(), subject, message, true);

    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Resource getDefaultResource() throws ResourceApplicationException {
	Long defaultResourceId = getToolDefaultContentIdBySignature(ResourceConstants.TOOL_SIGNATURE);
	Resource defaultResource = getResourceByContentId(defaultResourceId);
	if (defaultResource == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new ResourceApplicationException(error);
	}

	return defaultResource;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws ResourceApplicationException {
	Long contentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new ResourceApplicationException(error);
	}
	return contentId;
    }

    /**
     * Process an uploaded file.
     *
     * @throws ResourceApplicationException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(File file) throws UploadResourceFileException {
	NodeKey node = null;
	if ((file != null) && !StringUtils.isEmpty(file.getName())) {
	    String fileName = file.getName();
	    try {
		node = resourceToolContentHandler.uploadFile(new FileInputStream(file), fileName, null);
	    } catch (InvalidParameterException e) {
		throw new UploadResourceFileException(
			messageService.getMessage("error.msg.invaid.param.upload") + " " + e.getMessage());
	    } catch (FileNotFoundException e) {
		throw new UploadResourceFileException(
			messageService.getMessage("error.msg.file.not.found") + " " + e.getMessage());
	    } catch (RepositoryCheckedException e) {
		throw new UploadResourceFileException(
			messageService.getMessage("error.msg.repository") + " " + e.getMessage());
	    }
	}
	return node;
    }

    @Override
    public void uploadResourceItemFile(ResourceItem item, File file) throws UploadResourceFileException {
	String fileName = file.getName();
	// For file only upload one sigle file
	if (item.getType() == ResourceConstants.RESOURCE_TYPE_FILE) {
	    NodeKey nodeKey = processFile(file);
	    item.setFileUuid(nodeKey.getNodeId());
	    item.setFileVersionId(nodeKey.getVersion());
	    item.setFileDisplayUuid(nodeKey.getUuid());
	}
	// need unzip upload, and check the initial item :default.htm/html or index.htm/html
	if (item.getType() == ResourceConstants.RESOURCE_TYPE_WEBSITE) {
	    try {
		InputStream is = new FileInputStream(file);
		String packageDirectory = ZipFileUtil.expandZip(is, fileName);
		String initFile = findWebsiteInitialItem(packageDirectory);
		if (initFile == null) {
		    throw new UploadResourceFileException(
			    messageService.getMessage("error.msg.website.no.initial.file"));
		}
		item.setInitialItem(initFile);
		// upload package
		NodeKey nodeKey = processPackage(packageDirectory, initFile);
		item.setFileUuid(nodeKey.getNodeId());
		item.setFileVersionId(nodeKey.getVersion());
		item.setFileDisplayUuid(nodeKey.getUuid());
	    } catch (ZipFileUtilException e) {
		log.error(messageService.getMessage("error.msg.zip.file.exception") + " : " + e.toString());
		throw new UploadResourceFileException(messageService.getMessage("error.msg.zip.file.exception"));
	    } catch (FileNotFoundException e) {
		log.error(messageService.getMessage("error.msg.file.not.found") + ":" + e.toString());
		throw new UploadResourceFileException(messageService.getMessage("error.msg.file.not.found"));
	    }
	}

	// create the package from the directory contents
	item.setFileName(fileName);
    }

    private NodeKey processPackage(String packageDirectory, String initFile) throws UploadResourceFileException {
	NodeKey node = null;
	try {
	    node = resourceToolContentHandler.uploadPackage(packageDirectory, initFile);
	} catch (InvalidParameterException e) {
	    throw new UploadResourceFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	} catch (RepositoryCheckedException e) {
	    throw new UploadResourceFileException(messageService.getMessage("error.msg.repository"));
	}
	return node;
    }

    /**
     * Find out default.htm/html or index.htm/html in the given directory folder
     *
     * @param packageDirectory
     */
    private String findWebsiteInitialItem(String packageDirectory) {
	File file = new File(packageDirectory);
	if (!file.isDirectory()) {
	    return null;
	}

	File[] initFiles = file.listFiles(new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		if (pathname == null || pathname.getName() == null) {
		    return false;
		}
		String name = pathname.getName();
		return name.endsWith("default.html") || name.endsWith("default.htm") || name.endsWith("index.html")
			|| name.endsWith("index.htm");
	    }
	});
	return initFiles.length > 0 ? initFiles[0].getName() : null;
    }

    /**
     * Gets a message from resource bundle. Same as <code><fmt:message></code> in JSP pages.
     *
     * @param key
     * 	key of the message
     * @param args
     * 	arguments for the message
     * @return message content
     */
    private String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
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

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Resource toolContentObj = resourceDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultResource();
	    } catch (ResourceApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the share resources tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Resource.newInstance(toolContentObj, toolContentId);
	toolContentObj.setMiniViewNumberStr(null);
	try {
	    exportContentService.registerFileClassForExport(ResourceItem.class.getName(), "fileUuid", "fileVersionId");
	    exportContentService.exportToolContent(toolContentId, toolContentObj, resourceToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(ResourceImportContentVersionFilter.class);

	    exportContentService.registerFileClassForImport(ResourceItem.class.getName(), "fileUuid", "fileVersionId",
		    "fileName", "fileType", "initialItem");

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, resourceToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Resource)) {
		throw new ImportToolContentException(
			"Import Share resources tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Resource toolContentObj = (Resource) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);

	    ResourceUser user = resourceUserDao.getUserByUserIDAndContentID(newUserUid.longValue(), toolContentId);
	    if (user == null) {
		user = new ResourceUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(newUserUid.longValue());
		user.setResource(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    // reset all resourceItem createBy user
	    boolean useRatings = false;
	    Set<ResourceItem> items = toolContentObj.getResourceItems();
	    for (ResourceItem item : items) {
		item.setCreateBy(user);
		useRatings = useRatings || item.isAllowRating();
	    }

	    Set<LearnerItemRatingCriteria> criterias = toolContentObj.getRatingCriterias();
	    if (criterias != null) {
		for (LearnerItemRatingCriteria criteria : criterias) {
		    criteria.setToolContentId(toolContentId);
		}
	    }

	    resourceDao.saveObject(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Resource content = getResourceByContentId(toolContentId);
	if (content == null) {
	    try {
		content = getDefaultContent(toolContentId);
	    } catch (ResourceApplicationException e) {
		throw new ToolException(e);
	    }
	}
	return getResourceOutputFactory().getToolOutputDefinitions(content, definitionType);
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedResourceFiles tool seession");
	}

	Resource resource = null;
	if (fromContentId != null) {
	    resource = resourceDao.getByContentId(fromContentId);
	}
	if (resource == null) {
	    try {
		resource = getDefaultResource();
	    } catch (ResourceApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Resource toContent = Resource.newInstance(resource, toContentId);
	resourceDao.saveObject(toContent);

	// save resource items as well
	Set<ResourceItem> items = toContent.getResourceItems();
	if (items != null) {
	    Iterator<ResourceItem> iter = items.iterator();
	    while (iter.hasNext()) {
		ResourceItem item = iter.next();
		// createRootTopic(toContent.getUid(),null,msg);
	    }
	}
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getResourceByContentId(toolContentId).getTitle();
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Resource resource = resourceDao.getByContentId(toolContentId);
	if (resource == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	resource.setDefineLater(false);
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getResourceByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	List<ResourceSession> sessions = resourceSessionDao.getByContentId(toolContentId);
	for (ResourceSession session : sessions) {
	    if (!resourceUserDao.getBySessionID(session.getSessionId()).isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Resource resource = resourceDao.getByContentId(toolContentId);
	if (resource == null) {
	    log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	resourceDao.delete(resource);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId, boolean resetActivityCompletionOnly)
	    throws ToolException {
	if (log.isDebugEnabled()) {
	    if (resetActivityCompletionOnly) {
		log.debug("Resetting Share Resources completion for user ID " + userId + " and toolContentId "
			+ toolContentId);
	    } else {
		log.debug("Removing Share Resources content for user ID " + userId + " and toolContentId "
			+ toolContentId);
	    }
	}

	Resource resource = resourceDao.getByContentId(toolContentId);
	if (resource == null) {
	    log.warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	if (!resetActivityCompletionOnly) {
	    Iterator<ResourceItem> itemIterator = resource.getResourceItems().iterator();
	    while (itemIterator.hasNext()) {
		ResourceItem item = itemIterator.next();
		ResourceItemVisitLog visitLog = resourceItemVisitDao.getResourceItemLog(item.getUid(),
			userId.longValue());
		if (visitLog != null) {
		    resourceItemVisitDao.removeObject(ResourceItemVisitLog.class, visitLog.getUid());
		}

		if (!item.isCreateByAuthor() && item.getCreateBy().getUserId().equals(userId.longValue())) {
		    if (item.getFileUuid() != null) {
			try {
			    resourceToolContentHandler.deleteFile(item.getFileUuid());
			} catch (Exception e) {
			    throw new ToolException(
				    "Error while removing Share Resources file UUID " + item.getFileUuid(), e);
			}
		    }
		    resourceItemDao.removeObject(ResourceItem.class, item.getUid());
		    itemIterator.remove();
		}
	    }
	}

	List<ResourceSession> sessions = resourceSessionDao.getByContentId(toolContentId);
	for (ResourceSession session : sessions) {
	    ResourceUser user = resourceUserDao.getUserByUserIDAndSessionID(userId.longValue(), session.getSessionId());
	    if (user != null) {
		if (resetActivityCompletionOnly) {
		    user.setSessionFinished(false);
		    resourceUserDao.saveObject(user);
		} else {
		    resourceUserDao.removeObject(ResourceUser.class, user.getUid());
		}
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	ResourceSession session = new ResourceSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Resource resource = resourceDao.getByContentId(toolContentId);
	session.setResource(resource);
	resourceSessionDao.saveObject(session);
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

	ResourceSession session = resourceSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(ResourceConstants.COMPLETED);
	    resourceSessionDao.saveObject(session);
	} else {
	    log.error("Fail to leave tool Session.Could not find shared resources " + "session by given session id: "
		    + toolSessionId);
	    throw new DataMissingException(
		    "Fail to leave tool Session." + "Could not find shared resource session by given session id: "
			    + toolSessionId);
	}
	return toolService.completeToolSession(toolSessionId, learnerId);
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
	resourceSessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getResourceOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getResourceOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
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
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	return false;
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getResourceOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public LearnerItemRatingCriteria createRatingCriteria(Long toolContentId) throws RatingException {
	List<RatingCriteria> ratingCriterias = ratingService.getCriteriasByToolContentId(toolContentId);
	if (ratingCriterias == null || ratingCriterias.size() == 0) {
	    return ratingService.saveLearnerItemRatingCriteria(toolContentId, null, 1, RatingCriteria.RATING_STYLE_STAR,
		    false, 0);
	} else {
	    return (LearnerItemRatingCriteria) ratingCriterias.get(0);
	}
    }

    @Override
    public int deleteRatingCriteria(Long toolContentId) {
	return ratingService.deleteAllRatingCriterias(toolContentId);
    }

    @Override
    public List<ItemRatingDTO> getRatingCriteriaDtos(Long toolContentId, Long toolSessionId, Collection<Long> itemIds,
	    Long userId) {
	return ratingService.getRatingCriteriaDtos(toolContentId, toolSessionId, itemIds, false, userId);
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************
    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setResourceDao(ResourceDAO resourceDao) {
	this.resourceDao = resourceDao;
    }

    public void setResourceItemDao(ResourceItemDAO resourceItemDao) {
	this.resourceItemDao = resourceItemDao;
    }

    public void setResourceSessionDao(ResourceSessionDAO resourceSessionDao) {
	this.resourceSessionDao = resourceSessionDao;
    }

    public void setResourceToolContentHandler(IToolContentHandler resourceToolContentHandler) {
	this.resourceToolContentHandler = resourceToolContentHandler;
    }

    public void setResourceUserDao(ResourceUserDAO resourceUserDao) {
	this.resourceUserDao = resourceUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public ResourceItemVisitDAO getResourceItemVisitDao() {
	return resourceItemVisitDao;
    }

    public void setResourceItemVisitDao(ResourceItemVisitDAO resourceItemVisitDao) {
	this.resourceItemVisitDao = resourceItemVisitDao;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    public IRatingService getRatingService() {
	return ratingService;
    }

    public void setRatingService(IRatingService ratingService) {
	this.ratingService = ratingService;
    }

    public ResourceOutputFactory getResourceOutputFactory() {
	return resourceOutputFactory;
    }

    public void setResourceOutputFactory(ResourceOutputFactory resourceOutputFactory) {
	this.resourceOutputFactory = resourceOutputFactory;
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	ResourceUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Object[] dates = resourceItemVisitDao.getDateRangeOfAccesses(learner.getUid(), toolSessionId);
	if (learner.isSessionFinished()) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, (Date) dates[0], (Date) dates[1]);
	} else {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, (Date) dates[0], null);
	}
    }

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content. Mandatory fields in toolContentJSON: title, instructions, resources,
     * user fields firstName, lastName and loginName Resources must contain a ArrayNode of ObjectNode objects, which
     * have the following mandatory fields: title, description, type. If there are instructions for a resource, the
     * instructions are a ArrayNode of Strings. There should be at least one resource object in the resources array.
     *
     * @throws IOException
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON)
	    throws IOException {

	Date updateDate = new Date();

	Resource resource = new Resource();
	resource.setContentId(toolContentID);
	resource.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	resource.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));
	resource.setCreated(updateDate);

	resource.setAllowAddFiles(JsonUtil.optBoolean(toolContentJSON, "allowAddFiles", Boolean.FALSE));
	resource.setAllowAddUrls(JsonUtil.optBoolean(toolContentJSON, "allowAddUrls", Boolean.FALSE));
	resource.setLockWhenFinished(JsonUtil.optBoolean(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	resource.setMiniViewResourceNumber(JsonUtil.optInt(toolContentJSON, "minViewResourceNumber", 0));
	resource.setNotifyTeachersOnAssigmentSumbit(
		JsonUtil.optBoolean(toolContentJSON, "notifyTeachersOnAssigmentSubmit", Boolean.FALSE));

	resource.setContentInUse(false);
	resource.setDefineLater(false);

	ResourceUser resourceUser = getUserByIDAndContent(userID.longValue(), toolContentID);
	if (resourceUser == null) {
	    resourceUser = new ResourceUser();
	    resourceUser.setFirstName(JsonUtil.optString(toolContentJSON, "firstName"));
	    resourceUser.setLastName(JsonUtil.optString(toolContentJSON, "lastName"));
	    resourceUser.setLoginName(JsonUtil.optString(toolContentJSON, "loginName"));
	    //   resourceUser.setResource(content);
	}

	resource.setCreatedBy(resourceUser);

	// **************************** Handle topic *********************
	ArrayNode resources = JsonUtil.optArray(toolContentJSON, "resources");
	Set<ResourceItem> itemList = new LinkedHashSet<>();
	if (resources != null) {
	    for (JsonNode itemData : resources) {
		ResourceItem item = new ResourceItem();
		item.setTitle(JsonUtil.optString(itemData, "title"));
		item.setType(JsonUtil.optInt(itemData, "type").shortValue());
		item.setCreateBy(resourceUser);
		item.setCreateDate(updateDate);
		item.setComplete(false);
		item.setCreateByAuthor(true);
		item.setHide(false);
		item.setOrderId(JsonUtil.optInt(itemData, RestTags.DISPLAY_ORDER));

		item.setInstructions(JsonUtil.optString(itemData, "instructions"));
		item.setFileName(JsonUtil.optString(itemData, "name"));
		item.setFileType(JsonUtil.optString(itemData, "fileType"));
		item.setFileUuid(JsonUtil.optLong(itemData, "crUuid"));
		item.setFileVersionId(JsonUtil.optLong(itemData, "crVersionId"));
		item.setUrl(JsonUtil.optString(itemData, "url"));

		// TODO files - need to save it somehow, validate the file size, etc. Needed for websites, files & LO
		if ((item.getFileName() != null) || (item.getFileUuid() != null)) {
		    throw new IOException(
			    "Only URLS supported via REST interface currently - files and learning objects are not supported.");
		}

		itemList.add(item);
	    }
	}
	resource.setResourceItems(itemList);

	saveOrUpdateResource(resource);
    }

    @Override
    public void evict(Object object) {
	resourceDao.releaseFromCache(object);
    }
}