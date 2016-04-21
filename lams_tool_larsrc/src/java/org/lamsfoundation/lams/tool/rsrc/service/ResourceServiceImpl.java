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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.rsrc.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolAccessMode;
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
import org.lamsfoundation.lams.tool.rsrc.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.rsrc.dto.ResourceItemDTO;
import org.lamsfoundation.lams.tool.rsrc.dto.SessionDTO;
import org.lamsfoundation.lams.tool.rsrc.dto.VisitLogDTO;
import org.lamsfoundation.lams.tool.rsrc.ims.ImscpApplicationException;
import org.lamsfoundation.lams.tool.rsrc.ims.SimpleContentPackageConverter;
import org.lamsfoundation.lams.tool.rsrc.model.Resource;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemInstruction;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemVisitLog;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceSession;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceUser;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceItemComparator;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.lamsfoundation.lams.web.util.AttributeNames;

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
    private ResourceToolContentHandler resourceToolContentHandler;

    private MessageService messageService;

    // system services
    private IRepositoryService repositoryService;

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private IAuditService auditService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IEventNotificationService eventNotificationService;

    private ResourceOutputFactory resourceOutputFactory;

    @Override
    public IVersionedNode getFileNode(Long itemUid, String relPathString) throws ResourceApplicationException {
	ResourceItem item = (ResourceItem) resourceItemDao.getObject(ResourceItem.class, itemUid);
	if (item == null) {
	    throw new ResourceApplicationException("Reource item " + itemUid + " not found.");
	}

	return getFile(item.getFileUuid(), item.getFileVersionId(), relPathString);
    }

    // *******************************************************************************
    // Service method
    // *******************************************************************************
    /**
     * Try to get the file. If forceLogin = false and an access denied exception occurs, call this method again to get a
     * new ticket and retry file lookup. If forceLogin = true and it then fails then throw exception.
     * 
     * @param uuid
     * @param versionId
     * @param relativePath
     * @param attemptCount
     * @return file node
     * @throws ImscpApplicationException
     */
    private IVersionedNode getFile(Long uuid, Long versionId, String relativePath) throws ResourceApplicationException {

	ITicket tic = getRepositoryLoginTicket();

	try {

	    return repositoryService.getFileItem(tic, uuid, versionId, relativePath);

	} catch (AccessDeniedException e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + ".";

	    error = error + "AccessDeniedException: " + e.getMessage() + " Unable to retry further.";
	    ResourceServiceImpl.log.error(error);
	    throw new ResourceApplicationException(error, e);

	} catch (Exception e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + "." + " Exception: " + e.getMessage();
	    ResourceServiceImpl.log.error(error);
	    throw new ResourceApplicationException(error, e);

	}
    }

    /**
     * This method verifies the credentials of the Share Resource Tool and gives it the <code>Ticket</code> to login and
     * access the Content Repository.
     * 
     * A valid ticket is needed in order to access the content from the repository. This method would be called evertime
     * the tool needs to upload/download files from the content repository.
     * 
     * @return ITicket The ticket for repostory access
     * @throws ResourceApplicationException
     */
    private ITicket getRepositoryLoginTicket() throws ResourceApplicationException {
	ICredentials credentials = new SimpleCredentials(resourceToolContentHandler.getRepositoryUser(),
		resourceToolContentHandler.getRepositoryId());
	try {
	    ITicket ticket = repositoryService.login(credentials,
		    resourceToolContentHandler.getRepositoryWorkspaceName());
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new ResourceApplicationException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new ResourceApplicationException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new ResourceApplicationException("Login failed." + e.getMessage());
	}
    }

    @Override
    public Resource getResourceByContentId(Long contentId) {
	Resource rs = resourceDao.getByContentId(contentId);
	return rs;
    }

    @Override
    public Resource getDefaultContent(Long contentId) throws ResourceApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ResourceServiceImpl.log.error(error);
	    throw new ResourceApplicationException(error);
	}

	Resource defaultContent = getDefaultResource();
	// save default content by given ID.
	Resource content = new Resource();
	content = Resource.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public List getAuthoredItems(Long resourceUid) {
	return resourceItemDao.getAuthoringItems(resourceUid);
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
    public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws ResourceApplicationException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, fileUuid, fileVersionId);
	} catch (Exception e) {
	    throw new ResourceApplicationException(
		    "Exception occured while deleting files from" + " the repository " + e.getMessage());
	}
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
	    ResourceServiceImpl.log.error("Failed get ResourceSession by ID [" + sessionId + "]");
	    return null;
	}
	// add resource items from Authoring
	Resource resource = session.getResource();
	List<ResourceItem> items = new ArrayList<ResourceItem>();
	items.addAll(resource.getResourceItems());

	// add resource items from ResourceSession
	items.addAll(session.getResourceItems());

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
	    ResourceServiceImpl.log.error("Failed get session by ID [" + toolSessionId + "]");
	    return 0;
	}
	int reqView = session.getResource().getMiniViewResourceNumber();

	return reqView - miniView;
    }

    @Override
    public ResourceItem getResourceItemByUid(Long itemUid) {
	return resourceItemDao.getByUid(itemUid);
    }

    @Override
    public List<SessionDTO> getSummary(Long contentId) {
	List<SessionDTO> groupList = new ArrayList<SessionDTO>();

	// get all item which is accessed by user
	Map<Long, Integer> visitCountMap = resourceItemVisitDao.getSummary(contentId);
	Resource resource = resourceDao.getByContentId(contentId);

	// get all sessions in a resource and retrieve all resource items under this session
	// plus initial resource items by author creating (resItemList)
	List<ResourceSession> sessionList = resourceSessionDao.getByContentId(contentId);

	for (ResourceSession session : sessionList) {
	    // one new group for one session.
	    SessionDTO group = new SessionDTO();
	    group.setSessionId(session.getSessionId());
	    group.setSessionName(session.getSessionName());

	    Set<ResourceItem> items = new TreeSet<ResourceItem>(new ResourceItemComparator());
	    // firstly, put all initial resource item into this group.
	    items.addAll(resource.getResourceItems());
	    // add this session's resource items
	    items.addAll(session.getResourceItems());

	    for (ResourceItem item : items) {
		ResourceItemDTO resourceItemDTO = new ResourceItemDTO(item);
		// set viewNumber according visit log
		if (visitCountMap.containsKey(item.getUid())) {
		    resourceItemDTO.setViewNumber(visitCountMap.get(item.getUid()).intValue());
		}
		group.getItems().add(resourceItemDTO);
	    }

	    groupList.add(group);
	}

	return groupList;
    }

    @Override
    public List<ReflectDTO> getReflectList(Long contentId) {
	List<ReflectDTO> reflections = new LinkedList<ReflectDTO>();

	List<ResourceSession> sessionList = resourceSessionDao.getByContentId(contentId);
	for (ResourceSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // get all users in this session
	    List<ResourceUser> users = resourceUserDao.getBySessionID(sessionId);
	    for (ResourceUser user : users) {

		NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			ResourceConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		if (entry != null) {
		    ReflectDTO ref = new ReflectDTO(user);
		    ref.setReflect(entry.getEntry());
		    Date postedDate = (entry.getLastModified() != null) ? entry.getLastModified()
			    : entry.getCreateDate();
		    ref.setDate(postedDate);
		    reflections.add(ref);
		}

	    }

	}

	return reflections;
    }

    @Override
    public List<ResourceUser> getUserListBySessionItem(Long sessionId, Long itemUid) {
	List<ResourceItemVisitLog> logList = resourceItemVisitDao.getResourceItemLogBySession(sessionId, itemUid);
	List<ResourceUser> userList = new ArrayList(logList.size());
	for (ResourceItemVisitLog visit : logList) {
	    ResourceUser user = visit.getUser();
	    user.setAccessDate(visit.getAccessDate());
	    user.setCompleteDate(visit.getCompleteDate());
	    Date timeTaken = ((visit.getCompleteDate() != null) && (visit.getAccessDate() != null))
		    ? new Date(visit.getCompleteDate().getTime() - visit.getAccessDate().getTime()) : null;
	    user.setTimeTaken(timeTaken);
	    userList.add(user);
	}
	return userList;
    }

    @Override
    public List<VisitLogDTO> getPagedVisitLogsBySessionAndItem(Long sessionId, Long itemUid, int page, int size,
	    String sortBy, String sortOrder, String searchString) {
	return resourceItemVisitDao.getPagedVisitLogsBySessionAndItem(sessionId, itemUid, page, size, sortBy, sortOrder,
		searchString);
    }

    @Override
    public int getCountVisitLogsBySessionAndItem(Long sessionId, Long itemUid, String searchString) {
	return resourceItemVisitDao.getCountVisitLogsBySessionAndItem(sessionId, itemUid, searchString);
    }

    @Override
    public void setItemVisible(Long itemUid, boolean visible) {
	ResourceItem item = resourceItemDao.getByUid(itemUid);
	if (item != null) {
	    // createBy should be null for system default value.
	    Long userId = 0L;
	    String loginName = "No user";
	    if (item.getCreateBy() != null) {
		userId = item.getCreateBy().getUserId();
		loginName = item.getCreateBy().getLoginName();
	    }
	    if (visible) {
		auditService.logShowEntry(ResourceConstants.TOOL_SIGNATURE, userId, loginName, item.toString());
	    } else {
		auditService.logHideEntry(ResourceConstants.TOOL_SIGNATURE, userId, loginName, item.toString());
	    }
	    item.setHide(!visible);
	    resourceItemDao.saveObject(item);
	}
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
    public ResourceUser getUser(Long uid) {
	return (ResourceUser) resourceUserDao.getObject(ResourceUser.class, uid);
    }

    @Override
    public void notifyTeachersOnAssigmentSumbit(Long sessionId, ResourceUser resourceUser) {
	String userName = resourceUser.getLastName() + " " + resourceUser.getFirstName();
	String message = getLocalisedMessage("event.assigment.submit.body", new Object[] { userName });
	eventNotificationService.notifyLessonMonitors(sessionId, message, false);
    }

    @Override
    public void notifyTeachersOnFileUpload(Long toolContentId, Long toolSessionId, String sessionMapId, String userName,
	    Long itemUid, String fileName) {
	String eventName = new StringBuilder("resources_file_upload_").append(toolContentId).append("_")
		.append(System.currentTimeMillis()).toString();
	String url = new StringBuilder("<a href='").append(WebUtil.getBaseServerURL())
		.append("/lams/tool/larsrc11/reviewItem.do?").append(ResourceConstants.ATTR_SESSION_MAP_ID).append("=")
		.append(sessionMapId).append("&").append(AttributeNames.ATTR_MODE).append("=")
		.append(ToolAccessMode.TEACHER.toString()).append("&").append(ResourceConstants.ATTR_TOOL_SESSION_ID)
		.append("=").append(toolSessionId).append("&").append(ResourceConstants.ATTR_RESOURCE_ITEM_UID)
		.append("=").append(itemUid).append("'>")
		.append(getLocalisedMessage("event.file.upload", new Object[] { userName, fileName })).append("</a>")
		.toString();
	eventNotificationService.createLessonEvent(IEventNotificationService.LESSON_MONITORS_SCOPE, eventName,
		toolContentId, null, url, true, IEventNotificationService.DELIVERY_METHOD_NOTIFICATION);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Resource getDefaultResource() throws ResourceApplicationException {
	Long defaultResourceId = getToolDefaultContentIdBySignature(ResourceConstants.TOOL_SIGNATURE);
	Resource defaultResource = getResourceByContentId(defaultResourceId);
	if (defaultResource == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ResourceServiceImpl.log.error(error);
	    throw new ResourceApplicationException(error);
	}

	return defaultResource;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws ResourceApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ResourceServiceImpl.log.error(error);
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
    private NodeKey processFile(FormFile file) throws UploadResourceFileException {
	NodeKey node = null;
	if ((file != null) && !StringUtils.isEmpty(file.getFileName())) {
	    String fileName = file.getFileName();
	    try {
		node = resourceToolContentHandler.uploadFile(file.getInputStream(), fileName, file.getContentType());
	    } catch (InvalidParameterException e) {
		throw new UploadResourceFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	    } catch (FileNotFoundException e) {
		throw new UploadResourceFileException(messageService.getMessage("error.msg.file.not.found"));
	    } catch (RepositoryCheckedException e) {
		throw new UploadResourceFileException(messageService.getMessage("error.msg.repository"));
	    } catch (IOException e) {
		throw new UploadResourceFileException(messageService.getMessage("error.msg.io.exception"));
	    }
	}
	return node;
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

    @Override
    public void uploadResourceItemFile(ResourceItem item, FormFile file) throws UploadResourceFileException {
	try {
	    InputStream is = file.getInputStream();
	    String fileName = file.getFileName();
	    String fileType = file.getContentType();
	    // For file only upload one sigle file
	    if (item.getType() == ResourceConstants.RESOURCE_TYPE_FILE) {
		NodeKey nodeKey = processFile(file);
		item.setFileUuid(nodeKey.getUuid());
		item.setFileVersionId(nodeKey.getVersion());
	    }
	    // need unzip upload, and check the initial item :default.htm/html or index.htm/html
	    if (item.getType() == ResourceConstants.RESOURCE_TYPE_WEBSITE) {
		String packageDirectory = ZipFileUtil.expandZip(is, fileName);
		String initFile = findWebsiteInitialItem(packageDirectory);
		if (initFile == null) {
		    throw new UploadResourceFileException(
			    messageService.getMessage("error.msg.website.no.initial.file"));
		}
		item.setInitialItem(initFile);
		// upload package
		NodeKey nodeKey = processPackage(packageDirectory, initFile);
		item.setFileUuid(nodeKey.getUuid());
		item.setFileVersionId(nodeKey.getVersion());
	    }
	    // need unzip upload, and parse learning object information from XML file.
	    if (item.getType() == ResourceConstants.RESOURCE_TYPE_LEARNING_OBJECT) {
		String packageDirectory = ZipFileUtil.expandZip(is, fileName);
		SimpleContentPackageConverter cpConverter = new SimpleContentPackageConverter(packageDirectory);
		String initFile = cpConverter.getDefaultItem();
		item.setInitialItem(initFile);
		item.setImsSchema(cpConverter.getSchema());
		item.setOrganizationXml(cpConverter.getOrganzationXML());
		// upload package
		NodeKey nodeKey = processPackage(packageDirectory, initFile);
		item.setFileUuid(nodeKey.getUuid());
		item.setFileVersionId(nodeKey.getVersion());
	    }
	    // create the package from the directory contents
	    item.setFileType(fileType);
	    item.setFileName(fileName);
	} catch (ZipFileUtilException e) {
	    ResourceServiceImpl.log
		    .error(messageService.getMessage("error.msg.zip.file.exception") + " : " + e.toString());
	    throw new UploadResourceFileException(messageService.getMessage("error.msg.zip.file.exception"));
	} catch (FileNotFoundException e) {
	    ResourceServiceImpl.log.error(messageService.getMessage("error.msg.file.not.found") + ":" + e.toString());
	    throw new UploadResourceFileException(messageService.getMessage("error.msg.file.not.found"));
	} catch (IOException e) {
	    ResourceServiceImpl.log.error(messageService.getMessage("error.msg.io.exception") + ":" + e.toString());
	    throw new UploadResourceFileException(messageService.getMessage("error.msg.io.exception"));
	}
    }

    /**
     * Find out default.htm/html or index.htm/html in the given directory folder
     * 
     * @param packageDirectory
     * @return
     */
    private String findWebsiteInitialItem(String packageDirectory) {
	File file = new File(packageDirectory);
	if (!file.isDirectory()) {
	    return null;
	}

	File[] initFiles = file.listFiles(new FileFilter() {
	    @Override
	    public boolean accept(File pathname) {
		if ((pathname == null) || (pathname.getName() == null)) {
		    return false;
		}
		String name = pathname.getName();
		if (name.endsWith("default.html") || name.endsWith("default.htm") || name.endsWith("index.html")
			|| name.endsWith("index.htm")) {
		    return true;
		}
		return false;
	    }
	});
	if ((initFiles != null) && (initFiles.length > 0)) {
	    return initFiles[0].getName();
	} else {
	    return null;
	}
    }

    /**
     * Gets a message from resource bundle. Same as <code><fmt:message></code> in JSP pages.
     * 
     * @param key
     *            key of the message
     * @param args
     *            arguments for the message
     * @return message content
     */
    private String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
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
	    ResourceUser user = resourceUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new ResourceUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setResource(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    // reset all resourceItem createBy user
	    Set<ResourceItem> items = toolContentObj.getResourceItems();
	    for (ResourceItem item : items) {
		item.setCreateBy(user);
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
	Set items = toContent.getResourceItems();
	if (items != null) {
	    Iterator iter = items.iterator();
	    while (iter.hasNext()) {
		ResourceItem item = (ResourceItem) iter.next();
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
	    ResourceServiceImpl.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (ResourceSession session : resourceSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, ResourceConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}
	resourceDao.delete(resource);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (ResourceServiceImpl.log.isDebugEnabled()) {
	    ResourceServiceImpl.log.debug(
		    "Removing Share Resources content for user ID " + userId + " and toolContentId " + toolContentId);
	}

	Resource resource = resourceDao.getByContentId(toolContentId);
	if (resource == null) {
	    ResourceServiceImpl.log
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	Iterator<ResourceItem> itemIterator = resource.getResourceItems().iterator();
	while (itemIterator.hasNext()) {
	    ResourceItem item = itemIterator.next();
	    ResourceItemVisitLog visitLog = resourceItemVisitDao.getResourceItemLog(item.getUid(), userId.longValue());
	    if (visitLog != null) {
		resourceItemVisitDao.removeObject(ResourceItemVisitLog.class, visitLog.getUid());
	    }

	    if (!item.isCreateByAuthor() && item.getCreateBy().getUserId().equals(userId.longValue())) {
		if (item.getFileUuid() != null) {
		    try {
			resourceToolContentHandler.deleteFile(item.getFileUuid());
		    } catch (Exception e) {
			throw new ToolException("Error while removing Share Resources file UUID " + item.getFileUuid(),
				e);
		    }
		}
		resourceItemDao.removeObject(ResourceItem.class, item.getUid());
		itemIterator.remove();
	    }
	}

	List<ResourceSession> sessions = resourceSessionDao.getByContentId(toolContentId);
	for (ResourceSession session : sessions) {
	    ResourceUser user = resourceUserDao.getUserByUserIDAndSessionID(userId.longValue(), session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			ResourceConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    resourceDao.removeObject(NotebookEntry.class, entry.getUid());
		}

		resourceUserDao.removeObject(ResourceUser.class, user.getUid());
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
	    ResourceServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    ResourceServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	ResourceSession session = resourceSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(ResourceConstants.COMPLETED);
	    resourceSessionDao.saveObject(session);
	} else {
	    ResourceServiceImpl.log.error("Fail to leave tool Session.Could not find shared resources "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared resource session by given session id: " + toolSessionId);
	}
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
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getResourceOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************
    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setRepositoryService(IRepositoryService repositoryService) {
	this.repositoryService = repositoryService;
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

    public void setResourceToolContentHandler(ResourceToolContentHandler resourceToolContentHandler) {
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

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    public ResourceOutputFactory getResourceOutputFactory() {
	return resourceOutputFactory;
    }

    public void setResourceOutputFactory(ResourceOutputFactory resourceOutputFactory) {
	this.resourceOutputFactory = resourceOutputFactory;
    }

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content. Mandatory fields in toolContentJSON: title, instructions, resources,
     * user fields firstName, lastName and loginName Resources must contain a JSONArray of JSONObject objects, which
     * have the following mandatory fields: title, description, type. If there are instructions for a resource, the
     * instructions are a JSONArray of Strings. There should be at least one resource object in the resources array.
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON)
	    throws JSONException {

	Date updateDate = new Date();

	Resource resource = new Resource();
	resource.setContentId(toolContentID);
	resource.setTitle(toolContentJSON.getString(RestTags.TITLE));
	resource.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));
	resource.setCreated(updateDate);

	resource.setAllowAddFiles(JsonUtil.opt(toolContentJSON, "allowAddFiles", Boolean.FALSE));
	resource.setAllowAddUrls(JsonUtil.opt(toolContentJSON, "allowAddUrls", Boolean.FALSE));
	resource.setLockWhenFinished(JsonUtil.opt(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	resource.setMiniViewResourceNumber(JsonUtil.opt(toolContentJSON, "minViewResourceNumber", 0));
	resource.setNotifyTeachersOnAssigmentSumbit(
		JsonUtil.opt(toolContentJSON, "notifyTeachersOnAssigmentSubmit", Boolean.FALSE));
	resource.setNotifyTeachersOnAssigmentSumbit(
		JsonUtil.opt(toolContentJSON, "notifyTeachersOnFileUpload", Boolean.FALSE));
	resource.setReflectOnActivity(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	resource.setReflectInstructions(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, (String) null));
	resource.setRunAuto(JsonUtil.opt(toolContentJSON, "runAuto", Boolean.FALSE));

	resource.setContentInUse(false);
	resource.setDefineLater(false);

	ResourceUser resourceUser = getUserByIDAndContent(userID.longValue(), toolContentID);
	if (resourceUser == null) {
	    resourceUser = new ResourceUser();
	    resourceUser.setFirstName(toolContentJSON.getString("firstName"));
	    resourceUser.setLastName(toolContentJSON.getString("lastName"));
	    resourceUser.setLoginName(toolContentJSON.getString("loginName"));
	    //   resourceUser.setResource(content);
	}

	resource.setCreatedBy(resourceUser);

	// **************************** Handle topic *********************
	JSONArray resources = toolContentJSON.getJSONArray("resources");
	Set itemList = new LinkedHashSet();
	for (int i = 0; i < resources.length(); i++) {
	    JSONObject itemData = (JSONObject) resources.get(i);
	    ResourceItem item = new ResourceItem();
	    item.setTitle(itemData.getString("title"));
	    item.setType((short) itemData.getInt("type"));
	    item.setCreateBy(resourceUser);
	    item.setCreateDate(updateDate);
	    item.setComplete(false);
	    item.setCreateByAuthor(true);
	    item.setHide(false);
	    item.setOrderId(itemData.getInt(RestTags.DISPLAY_ORDER));

	    item.setDescription(JsonUtil.opt(itemData, "description", (String) null));
	    item.setFileName(JsonUtil.opt(itemData, "name", (String) null));
	    item.setFileType(JsonUtil.opt(itemData, "fileType", (String) null));
	    item.setFileUuid(JsonUtil.optLong(itemData, "crUuid"));
	    item.setFileVersionId(JsonUtil.optLong(itemData, "crVersionId"));
	    item.setImsSchema(JsonUtil.opt(itemData, "imsSchema", (String) null));
	    item.setOrganizationXml(JsonUtil.opt(itemData, "organizationXml", (String) null));
	    item.setOpenUrlNewWindow(JsonUtil.opt(itemData, "openUrlNewWindow", Boolean.FALSE));
	    item.setUrl(JsonUtil.opt(itemData, "url", (String) null));

	    JSONArray instructionStrings = itemData.getJSONArray("instructions");
	    if ((instructionStrings != null) && (instructionStrings.length() > 0)) {
		Set instructions = new LinkedHashSet();
		for (int j = 0; j < instructionStrings.length(); j++) {
		    ResourceItemInstruction rii = new ResourceItemInstruction();
		    rii.setDescription(instructionStrings.getString(j));
		    rii.setSequenceId(j);
		    instructions.add(rii);
		}
		item.setItemInstructions(instructions);
	    }

	    // TODO files - need to save it somehow, validate the file size, etc. Needed for websites, files & LO
	    if ((item.getFileName() != null) || (item.getFileUuid() != null)) {
		throw new JSONException(
			"Only URLS supported via REST interface currently - files and learning objects are not supported.");
	    }

	    itemList.add(item);
	}

	resource.setResourceItems(itemList);

	saveOrUpdateResource(resource);

    }
}