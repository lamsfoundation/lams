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

package org.lamsfoundation.lams.tool.commonCartridge.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.commonCartridge.CommonCartridgeConstants;
import org.lamsfoundation.lams.tool.commonCartridge.dao.CommonCartridgeConfigItemDAO;
import org.lamsfoundation.lams.tool.commonCartridge.dao.CommonCartridgeDAO;
import org.lamsfoundation.lams.tool.commonCartridge.dao.CommonCartridgeItemDAO;
import org.lamsfoundation.lams.tool.commonCartridge.dao.CommonCartridgeItemVisitDAO;
import org.lamsfoundation.lams.tool.commonCartridge.dao.CommonCartridgeSessionDAO;
import org.lamsfoundation.lams.tool.commonCartridge.dao.CommonCartridgeUserDAO;
import org.lamsfoundation.lams.tool.commonCartridge.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.commonCartridge.dto.Summary;
import org.lamsfoundation.lams.tool.commonCartridge.ims.SimpleCommonCartridgeConverter;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridge;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeConfigItem;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItemVisitLog;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeSession;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeUser;
import org.lamsfoundation.lams.tool.commonCartridge.util.ReflectDTOComparator;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Andrey Balan
 *
 */
public class CommonCartridgeServiceImpl implements ICommonCartridgeService, ToolContentManager, ToolSessionManager {
    static Logger log = Logger.getLogger(CommonCartridgeServiceImpl.class.getName());

    private CommonCartridgeDAO commonCartridgeDao;

    private CommonCartridgeItemDAO commonCartridgeItemDao;

    private CommonCartridgeUserDAO commonCartridgeUserDao;

    private CommonCartridgeSessionDAO commonCartridgeSessionDao;

    private CommonCartridgeItemVisitDAO commonCartridgeItemVisitDao;

    private CommonCartridgeConfigItemDAO commonCartridgeConfigItemDao;

    // tool service
    private IToolContentHandler commonCartridgeToolContentHandler;

    private MessageService messageService;

    // system services

    private ILamsToolService toolService;

    private ILogEventService logEventService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IEventNotificationService eventNotificationService;

    private ILessonService lessonService;

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public CommonCartridge getCommonCartridgeByContentId(Long contentId) {
	CommonCartridge rs = commonCartridgeDao.getByContentId(contentId);
	return rs;
    }

    @Override
    public CommonCartridge getDefaultContent(Long contentId) throws CommonCartridgeApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new CommonCartridgeApplicationException(error);
	}

	CommonCartridge defaultContent = getDefaultCommonCartridge();
	// save default content by given ID.
	CommonCartridge content = new CommonCartridge();
	content = CommonCartridge.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public List getAuthoredItems(Long commonCartridgeUid) {
	return commonCartridgeItemDao.getAuthoringItems(commonCartridgeUid);
    }

    @Override
    public void createUser(CommonCartridgeUser commonCartridgeUser) {
	commonCartridgeUserDao.saveObject(commonCartridgeUser);
    }

    @Override
    public CommonCartridgeUser getUserByIDAndContent(Long userId, Long contentId) {

	return commonCartridgeUserDao.getUserByUserIDAndContentID(userId, contentId);

    }

    @Override
    public CommonCartridgeUser getUserByIDAndSession(Long userId, Long sessionId) {

	return commonCartridgeUserDao.getUserByUserIDAndSessionID(userId, sessionId);

    }

    @Override
    public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws InvalidParameterException, RepositoryCheckedException {
	commonCartridgeToolContentHandler.deleteFile(fileUuid);
    }

    @Override
    public void saveOrUpdateCommonCartridge(CommonCartridge commonCartridge) {
	commonCartridgeDao.saveObject(commonCartridge);
    }

    public void saveOrUpdateCommonCartridgeItem(CommonCartridgeItem item) {
	commonCartridgeItemDao.saveObject(item);
    }

    @Override
    public void deleteCommonCartridgeItem(Long uid) {
	commonCartridgeItemDao.removeObject(CommonCartridgeItem.class, uid);
    }

    @Override
    public CommonCartridge getCommonCartridgeBySessionId(Long sessionId) {
	CommonCartridgeSession session = commonCartridgeSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getCommonCartridge().getContentId();
	CommonCartridge res = commonCartridgeDao.getByContentId(contentId);
	int miniView = res.getMiniViewCommonCartridgeNumber();
	// construct dto fields;
	res.setMiniViewNumberStr(
		messageService.getMessage("label.learning.minimum.review", new Object[] { new Integer(miniView) }));
	return res;
    }

    @Override
    public CommonCartridgeSession getCommonCartridgeSessionBySessionId(Long sessionId) {
	return commonCartridgeSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public void saveOrUpdateCommonCartridgeSession(CommonCartridgeSession resSession) {
	commonCartridgeSessionDao.saveObject(resSession);
    }

    @Override
    public void retrieveComplete(SortedSet<CommonCartridgeItem> commonCartridgeItemList, CommonCartridgeUser user) {
	for (CommonCartridgeItem item : commonCartridgeItemList) {
	    CommonCartridgeItemVisitLog log = commonCartridgeItemVisitDao.getCommonCartridgeItemLog(item.getUid(),
		    user.getUserId());
	    if (log == null) {
		item.setComplete(false);
	    } else {
		item.setComplete(log.isComplete());
	    }
	}
    }

    @Override
    public void setItemComplete(Long commonCartridgeItemUid, Long userId, Long sessionId) {
	CommonCartridgeItemVisitLog log = commonCartridgeItemVisitDao.getCommonCartridgeItemLog(commonCartridgeItemUid,
		userId);
	if (log == null) {
	    log = new CommonCartridgeItemVisitLog();
	    CommonCartridgeItem item = commonCartridgeItemDao.getByUid(commonCartridgeItemUid);
	    log.setCommonCartridgeItem(item);
	    CommonCartridgeUser user = commonCartridgeUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	    log.setUser(user);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	}
	log.setComplete(true);
	commonCartridgeItemVisitDao.saveObject(log);
    }

    @Override
    public void setItemAccess(Long commonCartridgeItemUid, Long userId, Long sessionId) {
	CommonCartridgeItemVisitLog log = commonCartridgeItemVisitDao.getCommonCartridgeItemLog(commonCartridgeItemUid,
		userId);
	if (log == null) {
	    log = new CommonCartridgeItemVisitLog();
	    CommonCartridgeItem item = commonCartridgeItemDao.getByUid(commonCartridgeItemUid);
	    log.setCommonCartridgeItem(item);
	    CommonCartridgeUser user = commonCartridgeUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	    log.setUser(user);
	    log.setComplete(false);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	    commonCartridgeItemVisitDao.saveObject(log);
	}
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws CommonCartridgeApplicationException {
	CommonCartridgeUser user = commonCartridgeUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	commonCartridgeUserDao.saveObject(user);

	// CommonCartridgeSession session = commonCartridgeSessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(CommonCartridgeConstants.COMPLETED);
	// commonCartridgeSessionDao.saveObject(session);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new CommonCartridgeApplicationException(e);
	} catch (ToolException e) {
	    throw new CommonCartridgeApplicationException(e);
	}
	return nextUrl;
    }

    @Override
    public int checkMiniView(Long toolSessionId, Long userUid) {
	int miniView = commonCartridgeItemVisitDao.getUserViewLogCount(toolSessionId, userUid);
	CommonCartridgeSession session = commonCartridgeSessionDao.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    log.error("Failed get session by ID [" + toolSessionId + "]");
	    return 0;
	}
	int reqView = session.getCommonCartridge().getMiniViewCommonCartridgeNumber();

	return reqView - miniView;
    }

    @Override
    public CommonCartridgeItem getCommonCartridgeItemByUid(Long itemUid) {
	return commonCartridgeItemDao.getByUid(itemUid);
    }

    @Override
    public List<List<Summary>> getSummary(Long contentId) {
	List<List<Summary>> groupList = new ArrayList<>();
	List<Summary> group = new ArrayList<>();

	// get all item which is accessed by user
	Map<Long, Integer> visitCountMap = commonCartridgeItemVisitDao.getSummary(contentId);

	CommonCartridge commonCartridge = commonCartridgeDao.getByContentId(contentId);
	Set<CommonCartridgeItem> items = commonCartridge.getCommonCartridgeItems();

	List<CommonCartridgeSession> sessions = commonCartridgeSessionDao.getByContentId(contentId);
	for (CommonCartridgeSession session : sessions) {
	    // one new group for one session.
	    group = new ArrayList<>();
	    // firstly, put all initial commonCartridge item into this group.
	    for (CommonCartridgeItem item : items) {
		Summary sum = new Summary(session.getSessionId(), session.getSessionName(), item);
		// set viewNumber according visit log
		if (visitCountMap.containsKey(item.getUid())) {
		    sum.setViewNumber(visitCountMap.get(item.getUid()).intValue());
		}
		group.add(sum);
	    }
	    // so far no any item available, so just put session name info to Summary
	    if (group.size() == 0) {
		group.add(new Summary(session.getSessionId(), session.getSessionName(), null));
	    }
	    groupList.add(group);
	}

	return groupList;

    }

    @Override
    public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry) {
	Map<Long, Set<ReflectDTO>> map = new HashMap<>();

	List<CommonCartridgeSession> sessionList = commonCartridgeSessionDao.getByContentId(contentId);
	for (CommonCartridgeSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    boolean hasRefection = session.getCommonCartridge().getReflectOnActivity();
	    Set<ReflectDTO> list = new TreeSet<>(new ReflectDTOComparator());
	    // get all users in this session
	    List<CommonCartridgeUser> users = commonCartridgeUserDao.getBySessionID(sessionId);
	    for (CommonCartridgeUser user : users) {
		ReflectDTO ref = new ReflectDTO(user);

		if (setEntry) {
		    NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			    CommonCartridgeConstants.TOOL_SIGNATURE, user.getUserId().intValue());
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

    @Override
    public List<CommonCartridgeUser> getUserListBySessionItem(Long sessionId, Long itemUid) {
	List<CommonCartridgeItemVisitLog> logList = commonCartridgeItemVisitDao
		.getCommonCartridgeItemLogBySession(sessionId, itemUid);
	List<CommonCartridgeUser> userList = new ArrayList(logList.size());
	for (CommonCartridgeItemVisitLog visit : logList) {
	    CommonCartridgeUser user = visit.getUser();
	    user.setAccessDate(visit.getAccessDate());
	    userList.add(user);
	}
	return userList;
    }

    @Override
    public void setItemVisible(Long itemUid, Long toolContentId, boolean visible) {
	CommonCartridgeItem item = commonCartridgeItemDao.getByUid(itemUid);
	if (item != null) {
	    // createBy should be null for system default value.
	    Long userId = 0L;
	    String loginName = "No user";
	    if (item.getCreateBy() != null) {
		userId = item.getCreateBy().getUserId();
		loginName = item.getCreateBy().getLoginName();
	    }
	    if (visible) {
		logEventService.logShowLearnerContent(userId, loginName, toolContentId, item.toString());
	    } else {
		logEventService.logHideLearnerContent(userId, loginName, toolContentId, item.toString());
	    }
	    item.setHide(!visible);
	    commonCartridgeItemDao.saveObject(item);
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

    /**
     * @param notebookEntry
     */
    @Override
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    @Override
    public CommonCartridgeUser getUser(Long uid) {
	return (CommonCartridgeUser) commonCartridgeUserDao.getObject(CommonCartridgeUser.class, uid);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private CommonCartridge getDefaultCommonCartridge() throws CommonCartridgeApplicationException {
	Long defaultCommonCartridgeId = getToolDefaultContentIdBySignature(CommonCartridgeConstants.TOOL_SIGNATURE);
	CommonCartridge defaultCommonCartridge = getCommonCartridgeByContentId(defaultCommonCartridgeId);
	if (defaultCommonCartridge == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new CommonCartridgeApplicationException(error);
	}

	return defaultCommonCartridge;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws CommonCartridgeApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new CommonCartridgeApplicationException(error);
	}
	return contentId;
    }

    @Override
    public List<CommonCartridgeItem> uploadCommonCartridgeFile(CommonCartridgeItem item, MultipartFile file)
	    throws UploadCommonCartridgeFileException {
	try {
	    InputStream is = file.getInputStream();
	    String fileName = file.getName();
	    String fileType = file.getContentType();
	    // need unzip upload, and parse learning object information from XML file.
	    String packageDirectory = ZipFileUtil.expandZip(is, fileName);
	    SimpleCommonCartridgeConverter cpConverter = new SimpleCommonCartridgeConverter(packageDirectory);
	    String initFile = cpConverter.getDefaultItem();
	    item.setInitialItem(initFile);
	    item.setImsSchema(cpConverter.getSchema());
	    item.setOrganizationXml(cpConverter.getOrganzationXML());
	    //	    // upload package
	    //	    NodeKey nodeKey = processPackage(packageDirectory, initFile);
	    //	    item.setFileUuid(nodeKey.getUuid());
	    //	    item.setFileVersionId(nodeKey.getVersion());
	    //	    item.setFileType(fileType);
	    //	    item.setFileName(fileName);

	    List<CommonCartridgeItem> items = cpConverter.getBasicLTIItems();
	    return items;

	} catch (ZipFileUtilException e) {
	    log.error(messageService.getMessage("error.msg.zip.file.exception") + " : " + e.toString());
	    throw new UploadCommonCartridgeFileException(messageService.getMessage("error.msg.zip.file.exception"));
	} catch (FileNotFoundException e) {
	    log.error(messageService.getMessage("error.msg.file.not.found") + ":" + e.toString());
	    throw new UploadCommonCartridgeFileException(messageService.getMessage("error.msg.file.not.found"));
	} catch (IOException e) {
	    log.error(messageService.getMessage("error.msg.io.exception") + ":" + e.toString());
	    throw new UploadCommonCartridgeFileException(messageService.getMessage("error.msg.io.exception"));
	}
    }

    @Override
    public CommonCartridgeConfigItem getConfigItem(String key) {
	return commonCartridgeConfigItemDao.getConfigItemByKey(key);
    }

    @Override
    public void saveOrUpdateConfigItem(CommonCartridgeConfigItem item) {
	commonCartridgeConfigItemDao.saveOrUpdate(item);
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

    public void setCommonCartridgeDao(CommonCartridgeDAO commonCartridgeDao) {
	this.commonCartridgeDao = commonCartridgeDao;
    }

    public void setCommonCartridgeItemDao(CommonCartridgeItemDAO commonCartridgeItemDao) {
	this.commonCartridgeItemDao = commonCartridgeItemDao;
    }

    public void setCommonCartridgeSessionDao(CommonCartridgeSessionDAO commonCartridgeSessionDao) {
	this.commonCartridgeSessionDao = commonCartridgeSessionDao;
    }

    public void setCommonCartridgeToolContentHandler(IToolContentHandler commonCartridgeToolContentHandler) {
	this.commonCartridgeToolContentHandler = commonCartridgeToolContentHandler;
    }

    public void setCommonCartridgeUserDao(CommonCartridgeUserDAO commonCartridgeUserDao) {
	this.commonCartridgeUserDao = commonCartridgeUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public CommonCartridgeItemVisitDAO getCommonCartridgeItemVisitDao() {
	return commonCartridgeItemVisitDao;
    }

    public void setCommonCartridgeItemVisitDao(CommonCartridgeItemVisitDAO commonCartridgeItemVisitDao) {
	this.commonCartridgeItemVisitDao = commonCartridgeItemVisitDao;
    }

    public CommonCartridgeConfigItemDAO getCommonCartridgeConfigItemDao() {
	return commonCartridgeConfigItemDao;
    }

    public void setCommonCartridgeConfigItemDao(CommonCartridgeConfigItemDAO commonCartridgeConfigItemDao) {
	this.commonCartridgeConfigItemDao = commonCartridgeConfigItemDao;
    }

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	CommonCartridge toolContentObj = commonCartridgeDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultCommonCartridge();
	    } catch (CommonCartridgeApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the commonCartridge tool");
	}

	// set CommonCartridgeToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = CommonCartridge.newInstance(toolContentObj, toolContentId);
	toolContentObj.setMiniViewNumberStr(null);
	try {
	    exportContentService.registerFileClassForExport(CommonCartridgeItem.class.getName(), "fileUuid",
		    "fileVersionId");
	    exportContentService.exportToolContent(toolContentId, toolContentObj, commonCartridgeToolContentHandler,
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
	    exportContentService.registerImportVersionFilterClass(CommonCartridgeImportContentVersionFilter.class);

	    exportContentService.registerFileClassForImport(CommonCartridgeItem.class.getName(), "fileUuid",
		    "fileVersionId", "fileName", "fileType", "initialItem");

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, commonCartridgeToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof CommonCartridge)) {
		throw new ImportToolContentException(
			"Import Share commonCartridge tool content failed. Deserialized object is " + toolPOJO);
	    }
	    CommonCartridge toolContentObj = (CommonCartridge) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    CommonCartridgeUser user = commonCartridgeUserDao
		    .getUserByUserIDAndContentID(new Long(newUserUid.longValue()), toolContentId);
	    if (user == null) {
		user = new CommonCartridgeUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setCommonCartridge(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    // reset all commonCartridgeItem createBy user
	    Set<CommonCartridgeItem> items = toolContentObj.getCommonCartridgeItems();
	    for (CommonCartridgeItem item : items) {
		item.setCreateBy(user);
	    }
	    commonCartridgeDao.saveObject(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     *
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	return new TreeMap<>();
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedCommonCartridgeFiles tool seession");
	}

	CommonCartridge commonCartridge = null;
	if (fromContentId != null) {
	    commonCartridge = commonCartridgeDao.getByContentId(fromContentId);
	}
	if (commonCartridge == null) {
	    try {
		commonCartridge = getDefaultCommonCartridge();
	    } catch (CommonCartridgeApplicationException e) {
		throw new ToolException(e);
	    }
	}

	CommonCartridge toContent = CommonCartridge.newInstance(commonCartridge, toContentId);
	commonCartridgeDao.saveObject(toContent);

	// save commonCartridge items as well
	Set items = toContent.getCommonCartridgeItems();
	if (items != null) {
	    Iterator iter = items.iterator();
	    while (iter.hasNext()) {
		CommonCartridgeItem item = (CommonCartridgeItem) iter.next();
		// createRootTopic(toContent.getUid(),null,msg);
	    }
	}
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getCommonCartridgeByContentId(toolContentId).getTitle();
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	CommonCartridge commonCartridge = commonCartridgeDao.getByContentId(toolContentId);
	if (commonCartridge == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	commonCartridge.setDefineLater(false);
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getCommonCartridgeByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	CommonCartridge cartridge = commonCartridgeDao.getByContentId(toolContentId);
	for (CommonCartridgeItem item : (Set<CommonCartridgeItem>) cartridge.getCommonCartridgeItems()) {
	    if (!item.isCreateByAuthor()) {
		// we don't remove users in removeLearnerContent()
		// we just remove their items
		return true;
	    }
	}

	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	CommonCartridge commonCartridge = commonCartridgeDao.getByContentId(toolContentId);
	if (commonCartridge == null) {
	    log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (CommonCartridgeSession session : commonCartridgeSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, CommonCartridgeConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}
	commonCartridgeDao.delete(commonCartridge);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (log.isDebugEnabled()) {
	    log.debug(
		    "Removing Common Cartridge content for user ID " + userId + " and toolContentId " + toolContentId);
	}

	CommonCartridge cartridge = commonCartridgeDao.getByContentId(toolContentId);
	if (cartridge == null) {
	    log.warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	Iterator<CommonCartridgeItem> itemIterator = cartridge.getCommonCartridgeItems().iterator();
	while (itemIterator.hasNext()) {
	    CommonCartridgeItem item = itemIterator.next();
	    CommonCartridgeItemVisitLog visitLog = commonCartridgeItemVisitDao.getCommonCartridgeItemLog(item.getUid(),
		    userId.longValue());
	    if (visitLog != null) {
		commonCartridgeItemVisitDao.removeObject(CommonCartridgeItemVisitLog.class, visitLog.getUid());
	    }

	    if (!item.isCreateByAuthor() && item.getCreateBy().getUserId().equals(userId.longValue())) {
		if (item.getFileUuid() != null) {
		    try {
			commonCartridgeToolContentHandler.deleteFile(item.getFileUuid());
		    } catch (Exception e) {
			throw new ToolException("Error while removing Common Cartridge file UUID " + item.getFileUuid(),
				e);
		    }
		}
		commonCartridgeItemDao.removeObject(CommonCartridgeItem.class, item.getUid());
		itemIterator.remove();
	    }
	}

	List<CommonCartridgeSession> sessions = commonCartridgeSessionDao.getByContentId(toolContentId);

	for (CommonCartridgeSession session : sessions) {
	    CommonCartridgeUser user = commonCartridgeUserDao.getUserByUserIDAndSessionID(userId.longValue(),
		    session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			CommonCartridgeConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    commonCartridgeDao.removeObject(NotebookEntry.class, entry.getUid());
		}

		commonCartridgeUserDao.removeObject(CommonCartridgeUser.class, user.getUid());
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	CommonCartridgeSession session = new CommonCartridgeSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	CommonCartridge commonCartridge = commonCartridgeDao.getByContentId(toolContentId);
	session.setCommonCartridge(commonCartridge);
	commonCartridgeSessionDao.saveObject(session);
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

	CommonCartridgeSession session = commonCartridgeSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(CommonCartridgeConstants.COMPLETED);
	    commonCartridgeSessionDao.saveObject(session);
	} else {
	    log.error("Fail to leave tool Session.Could not find shared commonCartridge "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared commonCartridge session by given session id: " + toolSessionId);
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
	commonCartridgeSessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return new TreeMap<>();
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return null;
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
    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return lessonService.getMonitorsByToolSessionId(sessionId);
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
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	CommonCartridgeUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Object[] dates = commonCartridgeItemVisitDao.getDateRangeOfAccesses(learner.getUid());
	if (learner.isSessionFinished()) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, (Date) dates[0], (Date) dates[1]);
	} else {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, (Date) dates[0], null);
	}
    }

    @Override
    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    /* =================================================================================== */

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    @Override
    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

}
