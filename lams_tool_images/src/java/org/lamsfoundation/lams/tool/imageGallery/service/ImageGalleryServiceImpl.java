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
package org.lamsfoundation.lams.tool.imageGallery.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
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
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryAttachmentDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryItemDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryItemVisitDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGallerySessionDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryUserDAO;
import org.lamsfoundation.lams.tool.imageGallery.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.imageGallery.dto.Summary;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallery;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryAttachment;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItemVisitLog;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGallerySession;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
import org.lamsfoundation.lams.tool.imageGallery.util.ImageGalleryToolContentHandler;
import org.lamsfoundation.lams.tool.imageGallery.util.ReflectDTOComparator;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * @author Dapeng.Ni
 * 
 */
public class ImageGalleryServiceImpl implements IImageGalleryService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager

{
    static Logger log = Logger.getLogger(ImageGalleryServiceImpl.class.getName());

    private ImageGalleryDAO imageGalleryDao;

    private ImageGalleryItemDAO imageGalleryItemDao;

    private ImageGalleryAttachmentDAO imageGalleryAttachmentDao;

    private ImageGalleryUserDAO imageGalleryUserDao;

    private ImageGallerySessionDAO imageGallerySessionDao;

    private ImageGalleryItemVisitDAO imageGalleryItemVisitDao;

    // tool service
    private ImageGalleryToolContentHandler imageGalleryToolContentHandler;

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

    public IVersionedNode getFileNode(Long itemUid, String relPathString) throws ImageGalleryApplicationException {
	ImageGalleryItem item = (ImageGalleryItem) imageGalleryItemDao.getObject(ImageGalleryItem.class, itemUid);
	if (item == null) {
	    throw new ImageGalleryApplicationException("Reource item " + itemUid + " not found.");
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
    private IVersionedNode getFile(Long uuid, Long versionId, String relativePath)
	    throws ImageGalleryApplicationException {

	ITicket tic = getRepositoryLoginTicket();

	try {

	    return repositoryService.getFileItem(tic, uuid, versionId, relativePath);

	} catch (AccessDeniedException e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + ".";

	    error = error + "AccessDeniedException: " + e.getMessage() + " Unable to retry further.";
	    ImageGalleryServiceImpl.log.error(error);
	    throw new ImageGalleryApplicationException(error, e);

	} catch (Exception e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + "." + " Exception: " + e.getMessage();
	    ImageGalleryServiceImpl.log.error(error);
	    throw new ImageGalleryApplicationException(error, e);

	}
    }

    /**
     * This method verifies the credentials of the ImageGallery Tool and gives it the <code>Ticket</code> to login and
     * access the Content Repository.
     * 
     * A valid ticket is needed in order to access the content from the repository. This method would be called evertime
     * the tool needs to upload/download files from the content repository.
     * 
     * @return ITicket The ticket for repostory access
     * @throws ImageGalleryApplicationException
     */
    private ITicket getRepositoryLoginTicket() throws ImageGalleryApplicationException {
	ICredentials credentials = new SimpleCredentials(imageGalleryToolContentHandler.getRepositoryUser(),
		imageGalleryToolContentHandler.getRepositoryId());
	try {
	    ITicket ticket = repositoryService.login(credentials, imageGalleryToolContentHandler
		    .getRepositoryWorkspaceName());
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new ImageGalleryApplicationException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new ImageGalleryApplicationException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new ImageGalleryApplicationException("Login failed." + e.getMessage());
	}
    }

    public ImageGallery getImageGalleryByContentId(Long contentId) {
	ImageGallery rs = imageGalleryDao.getByContentId(contentId);
	if (rs == null) {
	    ImageGalleryServiceImpl.log.error("Could not find the content by given ID:" + contentId);
	}
	return rs;
    }

    public ImageGallery getDefaultContent(Long contentId) throws ImageGalleryApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ImageGalleryServiceImpl.log.error(error);
	    throw new ImageGalleryApplicationException(error);
	}

	ImageGallery defaultContent = getDefaultImageGallery();
	// save default content by given ID.
	ImageGallery content = new ImageGallery();
	content = ImageGallery.newInstance(defaultContent, contentId, imageGalleryToolContentHandler);
//	content.setNextImageTitle(new Long(1));
	return content;
    }

    public List getAuthoredItems(Long imageGalleryUid) {
	return imageGalleryItemDao.getAuthoringItems(imageGalleryUid);
    }

    public ImageGalleryAttachment uploadInstructionFile(FormFile uploadFile, String fileType)
	    throws UploadImageGalleryFileException {
	if ((uploadFile == null) || StringUtils.isEmpty(uploadFile.getFileName())) {
	    throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.upload.file.not.found",
		    new Object[] { uploadFile }));
	}

	// upload file to repository
	NodeKey nodeKey = processFile(uploadFile, fileType);

	// create new attachement
	ImageGalleryAttachment file = new ImageGalleryAttachment();
	file.setFileType(fileType);
	file.setFileUuid(nodeKey.getUuid());
	file.setFileVersionId(nodeKey.getVersion());
	file.setFileName(uploadFile.getFileName());
	file.setCreated(new Date());

	return file;
    }

    public void createUser(ImageGalleryUser imageGalleryUser) {
	imageGalleryUserDao.saveObject(imageGalleryUser);
    }

    public ImageGalleryUser getUserByIDAndContent(Long userId, Long contentId) {

	return imageGalleryUserDao.getUserByUserIDAndContentID(userId, contentId);

    }

    public ImageGalleryUser getUserByIDAndSession(Long userId, Long sessionId) {

	return imageGalleryUserDao.getUserByUserIDAndSessionID(userId, sessionId);

    }

    public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws ImageGalleryApplicationException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, fileUuid, fileVersionId);
	} catch (Exception e) {
	    throw new ImageGalleryApplicationException("Exception occured while deleting files from"
		    + " the repository " + e.getMessage());
	}
    }

    public void saveOrUpdateImageGallery(ImageGallery imageGallery) {
	imageGalleryDao.saveObject(imageGallery);
    }

    public void deleteImageGalleryAttachment(Long attachmentUid) {
	imageGalleryAttachmentDao.removeObject(ImageGalleryAttachment.class, attachmentUid);

    }

    public void saveOrUpdateImageGalleryItem(ImageGalleryItem item) {
	imageGalleryItemDao.saveObject(item);
    }

    public void deleteImageGalleryItem(Long uid) {
	imageGalleryItemDao.removeObject(ImageGalleryItem.class, uid);
    }

    public List<ImageGalleryItem> getImageGalleryItemsBySessionId(Long sessionId) {
	ImageGallerySession session = imageGallerySessionDao.getSessionBySessionId(sessionId);
	if (session == null) {
	    ImageGalleryServiceImpl.log.error("Failed get ImageGallerySession by ID [" + sessionId + "]");
	    return null;
	}
	// add imageGallery items from Authoring
	ImageGallery imageGallery = session.getImageGallery();
	List<ImageGalleryItem> items = new ArrayList<ImageGalleryItem>();
	items.addAll(imageGallery.getImageGalleryItems());

	// add imageGallery items from ImageGallerySession
	items.addAll(session.getImageGalleryItems());

	return items;
    }

    public List<Summary> exportBySessionId(Long sessionId, boolean skipHide) {
	ImageGallerySession session = imageGallerySessionDao.getSessionBySessionId(sessionId);
	if (session == null) {
	    ImageGalleryServiceImpl.log.error("Failed get ImageGallerySession by ID [" + sessionId + "]");
	    return null;
	}
	// initial imageGallery items list
	List<Summary> itemList = new ArrayList();
	Set<ImageGalleryItem> resList = session.getImageGallery().getImageGalleryItems();
	for (ImageGalleryItem item : resList) {
	    if (skipHide && item.isHide()) {
		continue;
	    }
	    // if item is create by author
	    if (item.isCreateByAuthor()) {
		Summary sum = new Summary(session.getSessionId(), session.getSessionName(), item, false);
		itemList.add(sum);
	    }
	}

	// get this session's all imageGallery items
	Set<ImageGalleryItem> sessList = session.getImageGalleryItems();
	for (ImageGalleryItem item : sessList) {
	    if (skipHide && item.isHide()) {
		continue;
	    }

	    // to skip all item create by author
	    if (!item.isCreateByAuthor()) {
		Summary sum = new Summary(session.getSessionId(), session.getSessionName(), item, false);
		itemList.add(sum);
	    }
	}

	return itemList;
    }

    public List<List<Summary>> exportByContentId(Long contentId) {
	ImageGallery imageGallery = imageGalleryDao.getByContentId(contentId);
	List<List<Summary>> groupList = new ArrayList();

	// create init imageGallery items list
	List<Summary> initList = new ArrayList();
	groupList.add(initList);
	Set<ImageGalleryItem> resList = imageGallery.getImageGalleryItems();
	for (ImageGalleryItem item : resList) {
	    if (item.isCreateByAuthor()) {
		Summary sum = new Summary(null, null, item, true);
		initList.add(sum);
	    }
	}

	// session by session
	List<ImageGallerySession> sessionList = imageGallerySessionDao.getByContentId(contentId);
	for (ImageGallerySession session : sessionList) {
	    List<Summary> group = new ArrayList<Summary>();
	    // get this session's all imageGallery items
	    Set<ImageGalleryItem> sessList = session.getImageGalleryItems();
	    for (ImageGalleryItem item : sessList) {
		// to skip all item create by author
		if (!item.isCreateByAuthor()) {
		    Summary sum = new Summary(session.getSessionId(), session.getSessionName(), item, false);
		    group.add(sum);
		}
	    }
	    if (group.size() == 0) {
		group.add(new Summary(session.getSessionId(), session.getSessionName(), null, false));
	    }
	    groupList.add(group);
	}

	return groupList;
    }

    public ImageGallery getImageGalleryBySessionId(Long sessionId) {
	ImageGallerySession session = imageGallerySessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getImageGallery().getContentId();
	ImageGallery res = imageGalleryDao.getByContentId(contentId);
	int miniView = res.getNumberColumns();
	// construct dto fields;
	res.setMiniViewNumberStr(messageService.getMessage("label.learning.minimum.review", new Object[] { new Integer(
		miniView) }));
	return res;
    }

    public ImageGallerySession getImageGallerySessionBySessionId(Long sessionId) {
	return imageGallerySessionDao.getSessionBySessionId(sessionId);
    }

    public void saveOrUpdateImageGallerySession(ImageGallerySession resSession) {
	imageGallerySessionDao.saveObject(resSession);
    }

    public void retrieveComplete(SortedSet<ImageGalleryItem> imageGalleryItemList, ImageGalleryUser user) {
	for (ImageGalleryItem item : imageGalleryItemList) {
	    ImageGalleryItemVisitLog log = imageGalleryItemVisitDao.getImageGalleryItemLog(item.getUid(), user
		    .getUserId());
	    if (log == null) {
		item.setComplete(false);
	    } else {
		item.setComplete(log.isComplete());
	    }
	}
    }

    public void setItemComplete(Long imageGalleryItemUid, Long userId, Long sessionId) {
	ImageGalleryItemVisitLog log = imageGalleryItemVisitDao.getImageGalleryItemLog(imageGalleryItemUid, userId);
	if (log == null) {
	    log = new ImageGalleryItemVisitLog();
	    ImageGalleryItem item = imageGalleryItemDao.getByUid(imageGalleryItemUid);
	    log.setImageGalleryItem(item);
	    ImageGalleryUser user = imageGalleryUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	    log.setUser(user);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	}
	log.setComplete(true);
	imageGalleryItemVisitDao.saveObject(log);
    }

    public void setItemAccess(Long imageGalleryItemUid, Long userId, Long sessionId) {
	ImageGalleryItemVisitLog log = imageGalleryItemVisitDao.getImageGalleryItemLog(imageGalleryItemUid, userId);
	if (log == null) {
	    log = new ImageGalleryItemVisitLog();
	    ImageGalleryItem item = imageGalleryItemDao.getByUid(imageGalleryItemUid);
	    log.setImageGalleryItem(item);
	    ImageGalleryUser user = imageGalleryUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	    log.setUser(user);
	    log.setComplete(false);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	    imageGalleryItemVisitDao.saveObject(log);
	}
    }

    public String finishToolSession(Long toolSessionId, Long userId) throws ImageGalleryApplicationException {
	ImageGalleryUser user = imageGalleryUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	imageGalleryUserDao.saveObject(user);

	// ImageGallerySession session = imageGallerySessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(ImageGalleryConstants.COMPLETED);
	// imageGallerySessionDao.saveObject(session);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new ImageGalleryApplicationException(e);
	} catch (ToolException e) {
	    throw new ImageGalleryApplicationException(e);
	}
	return nextUrl;
    }

    public int checkMiniView(Long toolSessionId, Long userUid) {
	int miniView = imageGalleryItemVisitDao.getUserViewLogCount(toolSessionId, userUid);
	ImageGallerySession session = imageGallerySessionDao.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    ImageGalleryServiceImpl.log.error("Failed get session by ID [" + toolSessionId + "]");
	    return 0;
	}
	int reqView = session.getImageGallery().getNumberColumns();

	return reqView - miniView;
    }

    public ImageGalleryItem getImageGalleryItemByUid(Long itemUid) {
	return imageGalleryItemDao.getByUid(itemUid);
    }

    public List<List<Summary>> getSummary(Long contentId) {
	List<List<Summary>> groupList = new ArrayList<List<Summary>>();
	List<Summary> group = new ArrayList<Summary>();

	// get all item which is accessed by user
	Map<Long, Integer> visitCountMap = imageGalleryItemVisitDao.getSummary(contentId);

	ImageGallery imageGallery = imageGalleryDao.getByContentId(contentId);
	Set<ImageGalleryItem> resItemList = imageGallery.getImageGalleryItems();

	// get all sessions in a imageGallery and retrieve all imageGallery items under this session
	// plus initial imageGallery items by author creating (resItemList)
	List<ImageGallerySession> sessionList = imageGallerySessionDao.getByContentId(contentId);
	for (ImageGallerySession session : sessionList) {
	    // one new group for one session.
	    group = new ArrayList<Summary>();
	    // firstly, put all initial imageGallery item into this group.
	    for (ImageGalleryItem item : resItemList) {
		Summary sum = new Summary(session.getSessionId(), session.getSessionName(), item);
		// set viewNumber according visit log
		if (visitCountMap.containsKey(item.getUid())) {
		    sum.setViewNumber(visitCountMap.get(item.getUid()).intValue());
		}
		group.add(sum);
	    }
	    // get this session's all imageGallery items
	    Set<ImageGalleryItem> sessItemList = session.getImageGalleryItems();
	    for (ImageGalleryItem item : sessItemList) {
		// to skip all item create by author
		if (!item.isCreateByAuthor()) {
		    Summary sum = new Summary(session.getSessionId(), session.getSessionName(), item);
		    // set viewNumber according visit log
		    if (visitCountMap.containsKey(item.getUid())) {
			sum.setViewNumber(visitCountMap.get(item.getUid()).intValue());
		    }
		    group.add(sum);
		}
	    }
	    // so far no any item available, so just put session name info to Summary
	    if (group.size() == 0) {
		group.add(new Summary(session.getSessionId(), session.getSessionName(), null));
	    }
	    groupList.add(group);
	}

	return groupList;

    }

    public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry) {
	Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();

	List<ImageGallerySession> sessionList = imageGallerySessionDao.getByContentId(contentId);
	for (ImageGallerySession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    boolean hasRefection = session.getImageGallery().isReflectOnActivity();
	    Set<ReflectDTO> list = new TreeSet<ReflectDTO>(new ReflectDTOComparator());
	    // get all users in this session
	    List<ImageGalleryUser> users = imageGalleryUserDao.getBySessionID(sessionId);
	    for (ImageGalleryUser user : users) {
		ReflectDTO ref = new ReflectDTO(user);

		if (setEntry) {
		    NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			    ImageGalleryConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		    if (entry != null) {
			ref.setReflect(entry.getEntry());
		    }
		}

		ref.setHasRefection(hasRefection);
		list.add(ref);
	    }
	    map.put(sessionId, list);
	}

	return map;
    }

    public List<ImageGalleryUser> getUserListBySessionItem(Long sessionId, Long itemUid) {
	List<ImageGalleryItemVisitLog> logList = imageGalleryItemVisitDao.getImageGalleryItemLogBySession(sessionId,
		itemUid);
	List<ImageGalleryUser> userList = new ArrayList(logList.size());
	for (ImageGalleryItemVisitLog visit : logList) {
	    ImageGalleryUser user = visit.getUser();
	    user.setAccessDate(visit.getAccessDate());
	    userList.add(user);
	}
	return userList;
    }

    public void setItemVisible(Long itemUid, boolean visible) {
	ImageGalleryItem item = imageGalleryItemDao.getByUid(itemUid);
	if (item != null) {
	    // createBy should be null for system default value.
	    Long userId = 0L;
	    String loginName = "No user";
	    if (item.getCreateBy() != null) {
		userId = item.getCreateBy().getUserId();
		loginName = item.getCreateBy().getLoginName();
	    }
	    if (visible) {
		auditService.logShowEntry(ImageGalleryConstants.TOOL_SIGNATURE, userId, loginName, item.toString());
	    } else {
		auditService.logHideEntry(ImageGalleryConstants.TOOL_SIGNATURE, userId, loginName, item.toString());
	    }
	    item.setHide(!visible);
	    imageGalleryItemDao.saveObject(item);
	}
    }

    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    /**
     * @param notebookEntry
     */
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    public ImageGalleryUser getUser(Long uid) {
	return (ImageGalleryUser) imageGalleryUserDao.getObject(ImageGalleryUser.class, uid);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private ImageGallery getDefaultImageGallery() throws ImageGalleryApplicationException {
	Long defaultImageGalleryId = getToolDefaultContentIdBySignature(ImageGalleryConstants.TOOL_SIGNATURE);
	ImageGallery defaultImageGallery = getImageGalleryByContentId(defaultImageGalleryId);
	if (defaultImageGallery == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ImageGalleryServiceImpl.log.error(error);
	    throw new ImageGalleryApplicationException(error);
	}

	return defaultImageGallery;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws ImageGalleryApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ImageGalleryServiceImpl.log.error(error);
	    throw new ImageGalleryApplicationException(error);
	}
	return contentId;
    }

    /**
     * Process an uploaded file.
     * 
     * @throws ImageGalleryApplicationException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file, String fileType) throws UploadImageGalleryFileException {
	NodeKey node = null;
	if ((file != null) && !StringUtils.isEmpty(file.getFileName())) {
	    String fileName = file.getFileName();
	    try {
		node = imageGalleryToolContentHandler.uploadFile(file.getInputStream(), fileName,
			file.getContentType(), fileType);
		String rr = imageGalleryToolContentHandler.getFileNode(node.getUuid()).getPath();
		String ff = "";
	    } catch (InvalidParameterException e) {
		throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	    } catch (FileNotFoundException e) {
		throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.file.not.found"));
	    } catch (RepositoryCheckedException e) {
		throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.repository"));
	    } catch (IOException e) {
		throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.io.exception"));
	    }
	}
	return node;
    }

    public void uploadImageGalleryItemFile(ImageGalleryItem item, FormFile file) throws UploadImageGalleryFileException {
	try {
	    InputStream is = file.getInputStream();
	    String fileName = file.getFileName();
	    String fileType = file.getContentType();

	    //TODO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		String packageDirectory = ZipFileUtil.expandZip(is, fileName);
//		String initFile = findWebsiteInitialItem(packageDirectory);
//		if (initFile == null) {
//		    throw new UploadImageGalleryFileException(messageService
//			    .getMessage("error.msg.website.no.initial.file"));
//		}

	    
	    
	    // upload file
	    NodeKey nodeKey = processFile(file, IToolContentHandler.TYPE_ONLINE);
	    item.setFileUuid(nodeKey.getUuid());
	    item.setFileVersionId(nodeKey.getVersion());
		
	    // create the package from the directory contents
	    item.setFileType(fileType);
	    item.setFileName(fileName);
	} catch (FileNotFoundException e) {
	    ImageGalleryServiceImpl.log.error(messageService.getMessage("error.msg.file.not.found") + ":"
		    + e.toString());
	    throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.file.not.found"));
	} catch (IOException e) {
	    ImageGalleryServiceImpl.log.error(messageService.getMessage("error.msg.io.exception") + ":" + e.toString());
	    throw new UploadImageGalleryFileException(messageService.getMessage("error.msg.io.exception"));
	}
    }
    
    /**
     * Reads an image in a file and creates a thumbnail in another file.
     * 
     * @param orig
     *                The name of image file.
     * @param thumb
     *                The name of thumbnail file. Will be created if necessary.
     * @param maxDim
     *                The width and height of the thumbnail must be maxDim pixels or less.
     */
    private static void createThumbnail(String orig, String thumb, int maxDim) {
	try {
	    // Get the image from a file.
	    Image inImage = new ImageIcon(orig).getImage();

	    // Determine the scale.
	    double scale = (double) maxDim / (double) inImage.getHeight(null);
	    if (inImage.getWidth(null) > inImage.getHeight(null)) {
		scale = (double) maxDim / (double) inImage.getWidth(null);
	    }

	    // Determine size of new image.
	    // One of them
	    // should equal maxDim.
	    int scaledW = (int) (scale * inImage.getWidth(null));
	    int scaledH = (int) (scale * inImage.getHeight(null));

	    // Create an image buffer in
	    // which to paint on.
	    BufferedImage outImage = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_RGB);

	    // Set the scale.
	    AffineTransform tx = new AffineTransform();

	    // If the image is smaller than
	    // the desired image size,
	    // don't bother scaling.
	    if (scale < 1.0d) {
		tx.scale(scale, scale);
	    }

	    // Paint image.
	    Graphics2D g2d = outImage.createGraphics();
	    g2d.drawImage(inImage, tx, null);
	    g2d.dispose();

	    // JPEG-encode the image
	    // and write to file.
	    OutputStream os = new FileOutputStream(thumb);
	    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
	    encoder.encode(outImage);
	    os.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	System.exit(0);
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

    public void setImageGalleryAttachmentDao(ImageGalleryAttachmentDAO imageGalleryAttachmentDao) {
	this.imageGalleryAttachmentDao = imageGalleryAttachmentDao;
    }

    public void setImageGalleryDao(ImageGalleryDAO imageGalleryDao) {
	this.imageGalleryDao = imageGalleryDao;
    }

    public void setImageGalleryItemDao(ImageGalleryItemDAO imageGalleryItemDao) {
	this.imageGalleryItemDao = imageGalleryItemDao;
    }

    public void setImageGallerySessionDao(ImageGallerySessionDAO imageGallerySessionDao) {
	this.imageGallerySessionDao = imageGallerySessionDao;
    }

    public void setImageGalleryToolContentHandler(ImageGalleryToolContentHandler imageGalleryToolContentHandler) {
	this.imageGalleryToolContentHandler = imageGalleryToolContentHandler;
    }

    public void setImageGalleryUserDao(ImageGalleryUserDAO imageGalleryUserDao) {
	this.imageGalleryUserDao = imageGalleryUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public ImageGalleryItemVisitDAO getImageGalleryItemVisitDao() {
	return imageGalleryItemVisitDao;
    }

    public void setImageGalleryItemVisitDao(ImageGalleryItemVisitDAO imageGalleryItemVisitDao) {
	this.imageGalleryItemVisitDao = imageGalleryItemVisitDao;
    }

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	ImageGallery toolContentObj = imageGalleryDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultImageGallery();
	    } catch (ImageGalleryApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the imageGallery tool");
	}

	// set ImageGalleryToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = ImageGallery.newInstance(toolContentObj, toolContentId, null);
	toolContentObj.setToolContentHandler(null);
	toolContentObj.setOfflineFileList(null);
	toolContentObj.setOnlineFileList(null);
	toolContentObj.setMiniViewNumberStr(null);
	try {
	    exportContentService.registerFileClassForExport(ImageGalleryAttachment.class.getName(), "fileUuid",
		    "fileVersionId");
	    exportContentService.registerFileClassForExport(ImageGalleryItem.class.getName(), "fileUuid",
		    "fileVersionId");
	    exportContentService.exportToolContent(toolContentId, toolContentObj, imageGalleryToolContentHandler,
		    rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    exportContentService.registerFileClassForImport(ImageGalleryAttachment.class.getName(), "fileUuid",
		    "fileVersionId", "fileName", "fileType", null, null);
	    exportContentService.registerFileClassForImport(ImageGalleryItem.class.getName(), "fileUuid",
		    "fileVersionId", "fileName", "fileType", null, "initialItem");

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, imageGalleryToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof ImageGallery)) {
		throw new ImportToolContentException(
			"Import Share imageGallery tool content failed. Deserialized object is " + toolPOJO);
	    }
	    ImageGallery toolContentObj = (ImageGallery) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    ImageGalleryUser user = imageGalleryUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new ImageGalleryUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setImageGallery(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    // reset all imageGalleryItem createBy user
	    Set<ImageGalleryItem> items = toolContentObj.getImageGalleryItems();
	    for (ImageGalleryItem item : items) {
		item.setCreateBy(user);
	    }
	    imageGalleryDao.saveObject(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     * 
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
	return new TreeMap<String, ToolOutputDefinition>();
    }

    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedImageGalleryFiles tool seession");
	}

	ImageGallery imageGallery = null;
	if (fromContentId != null) {
	    imageGallery = imageGalleryDao.getByContentId(fromContentId);
	}
	if (imageGallery == null) {
	    try {
		imageGallery = getDefaultImageGallery();
	    } catch (ImageGalleryApplicationException e) {
		throw new ToolException(e);
	    }
	}

	ImageGallery toContent = ImageGallery.newInstance(imageGallery, toContentId, imageGalleryToolContentHandler);
	imageGalleryDao.saveObject(toContent);

	// save imageGallery items as well
	Set items = toContent.getImageGalleryItems();
	if (items != null) {
	    Iterator iter = items.iterator();
	    while (iter.hasNext()) {
		ImageGalleryItem item = (ImageGalleryItem) iter.next();
		// createRootTopic(toContent.getUid(),null,msg);
	    }
	}
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	ImageGallery imageGallery = imageGalleryDao.getByContentId(toolContentId);
	if (imageGallery == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	imageGallery.setDefineLater(value);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	ImageGallery imageGallery = imageGalleryDao.getByContentId(toolContentId);
	if (imageGallery == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	imageGallery.setRunOffline(value);
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	ImageGallery imageGallery = imageGalleryDao.getByContentId(toolContentId);
	if (removeSessionData) {
	    List list = imageGallerySessionDao.getByContentId(toolContentId);
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		ImageGallerySession session = (ImageGallerySession) iter.next();
		imageGallerySessionDao.delete(session);
	    }
	}
	imageGalleryDao.delete(imageGallery);
    }

    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	ImageGallerySession session = new ImageGallerySession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	ImageGallery imageGallery = imageGalleryDao.getByContentId(toolContentId);
	session.setImageGallery(imageGallery);
	imageGallerySessionDao.saveObject(session);
    }

    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    ImageGalleryServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    ImageGalleryServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	ImageGallerySession session = imageGallerySessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(ImageGalleryConstants.COMPLETED);
	    imageGallerySessionDao.saveObject(session);
	} else {
	    ImageGalleryServiceImpl.log.error("Fail to leave tool Session.Could not find shared imageGallery "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared imageGallery session by given session id: " + toolSessionId);
	}
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	return null;
    }

    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	return null;
    }

    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	imageGallerySessionDao.deleteBySessionId(toolSessionId);
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return new TreeMap<String, ToolOutput>();
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return null;
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {}

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	ImageGallery toolContentObj = getImageGalleryByContentId(toolContentId);
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	toolContentObj.setReflectOnActivity(Boolean.TRUE);
	toolContentObj.setReflectInstructions(description);
    }

    /* =================================================================================== */

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

    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

}
