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
package org.lamsfoundation.lams.tool.spreadsheet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetMarkDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetSessionDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetUserDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.UserModifiedSpreadsheetDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.Summary;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetMark;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetSession;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.model.UserModifiedSpreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.util.ReflectDTOComparator;
import org.lamsfoundation.lams.tool.spreadsheet.util.SpreadsheetToolContentHandler;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

/**
 * 
 * @author Andrey Balan
 * 
 */
public class SpreadsheetServiceImpl implements ISpreadsheetService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager {
    private static Logger log = Logger.getLogger(SpreadsheetServiceImpl.class.getName());
    private SpreadsheetDAO spreadsheetDao;
    private SpreadsheetUserDAO spreadsheetUserDao;
    private SpreadsheetSessionDAO spreadsheetSessionDao;
    private UserModifiedSpreadsheetDAO userModifiedSpreadsheetDao;
    private SpreadsheetMarkDAO spreadsheetMarkDao;
    // tool service
    private SpreadsheetToolContentHandler spreadsheetToolContentHandler;
    private MessageService messageService;
    // system services
    private ILamsToolService toolService;
    private ILearnerService learnerService;
    private IUserManagementService userManagementService;
    private IExportToolContentService exportContentService;
    private ICoreNotebookService coreNotebookService;

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    public Spreadsheet getSpreadsheetByContentId(Long contentId) {
	Spreadsheet rs = spreadsheetDao.getByContentId(contentId);
	return rs;
    }

    public Spreadsheet getDefaultContent(Long contentId) throws SpreadsheetApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    SpreadsheetServiceImpl.log.error(error);
	    throw new SpreadsheetApplicationException(error);
	}

	Spreadsheet defaultContent = getDefaultSpreadsheet();
	// save default content by given ID.
	Spreadsheet content = new Spreadsheet();
	content = Spreadsheet.newInstance(defaultContent, contentId);
	return content;
    }

    public void saveOrUpdateUser(SpreadsheetUser spreadsheetUser) {
	spreadsheetUserDao.saveObject(spreadsheetUser);
    }

    public void saveOrUpdateUserModifiedSpreadsheet(UserModifiedSpreadsheet userModifiedSpreadsheet) {
	userModifiedSpreadsheetDao.saveObject(userModifiedSpreadsheet);
    }

    public SpreadsheetUser getUserByIDAndContent(Long userId, Long contentId) {
	return spreadsheetUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    public SpreadsheetUser getUserByIDAndSession(Long userId, Long sessionId) {
	return spreadsheetUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    public List<SpreadsheetUser> getUserListBySessionId(Long sessionId) {
	List<SpreadsheetUser> userList = spreadsheetUserDao.getBySessionID(sessionId);
	return userList;
    }

    public void saveOrUpdateSpreadsheet(Spreadsheet spreadsheet) {
	spreadsheetDao.saveObject(spreadsheet);
    }

    public List<Summary> exportForLearner(Long sessionId, SpreadsheetUser learner) {
	SpreadsheetSession session = spreadsheetSessionDao.getSessionBySessionId(sessionId);
	if (session == null) {
	    SpreadsheetServiceImpl.log.error("Failed get SpreadsheetSession by ID [" + sessionId + "]");
	    return null;
	}

	Spreadsheet spreadsheet = session.getSpreadsheet();
	List<Summary> summaryList = new ArrayList<Summary>();

	List<SpreadsheetUser> userList = new ArrayList<SpreadsheetUser>();
	userList.add(learner);
	Summary summary = new Summary(session, spreadsheet, userList);

	// Fill up reflect dto
	NotebookEntry notebookEntry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
		SpreadsheetConstants.TOOL_SIGNATURE, learner.getUserId().intValue());
	ReflectDTO reflectDTO = new ReflectDTO(learner);
	if (notebookEntry == null) {
	    reflectDTO.setFinishReflection(false);
	    reflectDTO.setReflect(null);
	} else {
	    reflectDTO.setFinishReflection(true);
	    reflectDTO.setReflect(notebookEntry.getEntry());
	}
	reflectDTO.setReflectInstructions(session.getSpreadsheet().getReflectInstructions());
	summary.getReflectDTOList().add(reflectDTO);
	summaryList.add(summary);

	return summaryList;

	// initial spreadsheet items list
	// List<Summary> itemList = new ArrayList();
	// Set<SpreadsheetItem> resList = session.getSpreadsheet().getSpreadsheetItems();
	// for(SpreadsheetItem item:resList){
	// if(skipHide && item.isHide())
	// continue;
	// //if item is create by author
	// if(item.isCreateByAuthor()){
	// Summary sum = new Summary(session.getSessionId(), session.getSessionName(),item,false);
	// itemList.add(sum);
	// }
	// }
	//
	// //get this session's all spreadsheet items
	// Set<SpreadsheetItem> sessList =session.getSpreadsheetItems();
	// for(SpreadsheetItem item:sessList){
	// if(skipHide && item.isHide())
	// continue;
	//
	// //to skip all item create by author
	// if(!item.isCreateByAuthor()){
	// Summary sum = new Summary(session.getSessionId(), session.getSessionName(),item,false);
	// itemList.add(sum);
	// }
	// }

	// return itemList;
    }

    public List<Summary> exportForTeacher(Long contentId) {
	Spreadsheet spreadsheet = spreadsheetDao.getByContentId(contentId);
	List<Summary> summaryList = new ArrayList<Summary>();

	List<SpreadsheetSession> sessionList = spreadsheetSessionDao.getByContentId(contentId);
	// create the user list of all whom were started this task
	for (SpreadsheetSession session : sessionList) {
	    List<SpreadsheetUser> userList = spreadsheetUserDao.getBySessionID(session.getSessionId());
	    Summary summary = new Summary(session, spreadsheet, userList);

	    // Fill up reflect dto
	    for (SpreadsheetUser user : userList) {
		NotebookEntry notebookEntry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			SpreadsheetConstants.TOOL_SIGNATURE, user.getUserId().intValue());

		ReflectDTO reflectDTO = new ReflectDTO(user);
		if (notebookEntry == null) {
		    reflectDTO.setFinishReflection(false);
		    reflectDTO.setReflect(null);
		} else {
		    reflectDTO.setFinishReflection(true);
		    reflectDTO.setReflect(notebookEntry.getEntry());
		}
		reflectDTO.setReflectInstructions(session.getSpreadsheet().getReflectInstructions());

		summary.getReflectDTOList().add(reflectDTO);
	    }
	    summaryList.add(summary);
	}

	return summaryList;
    }

    public Spreadsheet getSpreadsheetBySessionId(Long sessionId) {
	SpreadsheetSession session = spreadsheetSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getSpreadsheet().getContentId();
	Spreadsheet res = spreadsheetDao.getByContentId(contentId);
	return res;
    }

    public SpreadsheetSession getSessionBySessionId(Long sessionId) {
	return spreadsheetSessionDao.getSessionBySessionId(sessionId);
    }

    public void saveOrUpdateSpreadsheetSession(SpreadsheetSession resSession) {
	spreadsheetSessionDao.saveObject(resSession);
    }

    public String finishToolSession(Long toolSessionId, Long userId) throws SpreadsheetApplicationException {
	SpreadsheetUser user = spreadsheetUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	spreadsheetUserDao.saveObject(user);

	// SpreadsheetSession session = spreadsheetSessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(SpreadsheetConstants.COMPLETED);
	// spreadsheetSessionDao.saveObject(session);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new SpreadsheetApplicationException(e);
	} catch (ToolException e) {
	    throw new SpreadsheetApplicationException(e);
	}
	return nextUrl;
    }

    public List<Summary> getSummary(Long contentId) {

	Spreadsheet spreadsheet = spreadsheetDao.getByContentId(contentId);
	List<SpreadsheetSession> sessionList = spreadsheetSessionDao.getByContentId(contentId);

	List<Summary> summaryList = new ArrayList<Summary>();

	// create the user list of all whom were started this task
	for (SpreadsheetSession session : sessionList) {
	    // LDEV-3590 Monitoring is now paged so don't get the list of users
	    // List<SpreadsheetUser> userList = spreadsheetUserDao.getBySessionID(session.getSessionId());
	    Summary summary = new Summary(session, spreadsheet, null);
	    summaryList.add(summary);
	}

	return summaryList;
    }

    @Override
    public List<SpreadsheetUser> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting, String searchString) {
	return spreadsheetUserDao.getUsersForTablesorter(sessionId, page, size, sorting, searchString);
    }
    
    @Override
    public int getCountUsersBySession(Long sessionId, String searchString) {
	return spreadsheetUserDao.getCountUsersBySession(sessionId, searchString);
    }


    public List<StatisticDTO> getStatistics(Long contentId) {
	List<SpreadsheetSession> sessionList = spreadsheetSessionDao.getByContentId(contentId);

	List<StatisticDTO> statisticList = new ArrayList<StatisticDTO>();

	for (SpreadsheetSession session : sessionList) {
	    List<SpreadsheetUser> userList = spreadsheetUserDao.getBySessionID(session.getSessionId());
	    int totalUserModifiedSpreadsheets = 0;
	    int totalMarkedSpreadsheets = 0;
	    for (SpreadsheetUser user : userList) {
		if (user.getUserModifiedSpreadsheet() != null) {
		    totalUserModifiedSpreadsheets++;
		    if (user.getUserModifiedSpreadsheet().getMark() != null) {
			totalMarkedSpreadsheets++;
		    }
		}
	    }

	    StatisticDTO statistic = new StatisticDTO(session.getSessionName(), totalMarkedSpreadsheets,
		    totalUserModifiedSpreadsheets - totalMarkedSpreadsheets, totalUserModifiedSpreadsheets);
	    statisticList.add(statistic);
	}

	return statisticList;
    }

    public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry) {
	Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();

	List<SpreadsheetSession> sessionList = spreadsheetSessionDao.getByContentId(contentId);
	for (SpreadsheetSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    boolean hasRefection = session.getSpreadsheet().isReflectOnActivity();
	    Set<ReflectDTO> list = new TreeSet<ReflectDTO>(new ReflectDTOComparator());
	    // get all users in this session
	    List<SpreadsheetUser> users = spreadsheetUserDao.getBySessionID(sessionId);
	    for (SpreadsheetUser user : users) {
		ReflectDTO ref = new ReflectDTO(user);

		if (setEntry) {
		    NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			    SpreadsheetConstants.TOOL_SIGNATURE, user.getUserId().intValue());
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

    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
	if (list == null || list.isEmpty()) {
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

    public SpreadsheetUser getUser(Long uid) {
	return (SpreadsheetUser) spreadsheetUserDao.getObject(SpreadsheetUser.class, uid);
    }

    public void releaseMarksForSession(Long sessionId) {
	List<SpreadsheetUser> users = spreadsheetUserDao.getBySessionID(sessionId);
	for (SpreadsheetUser user : users) {
	    if (user.getUserModifiedSpreadsheet() != null && user.getUserModifiedSpreadsheet().getMark() != null) {
		SpreadsheetMark mark = user.getUserModifiedSpreadsheet().getMark();
		mark.setDateMarksReleased(new Date());
		spreadsheetMarkDao.saveObject(mark);
	    }
	}
    }

    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Spreadsheet getDefaultSpreadsheet() throws SpreadsheetApplicationException {
	Long defaultSpreadsheetId = getToolDefaultContentIdBySignature(SpreadsheetConstants.TOOL_SIGNATURE);
	Spreadsheet defaultSpreadsheet = getSpreadsheetByContentId(defaultSpreadsheetId);
	if (defaultSpreadsheet == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    SpreadsheetServiceImpl.log.error(error);
	    throw new SpreadsheetApplicationException(error);
	}

	return defaultSpreadsheet;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws SpreadsheetApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    SpreadsheetServiceImpl.log.error(error);
	    throw new SpreadsheetApplicationException(error);
	}
	return contentId;
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

    public MessageService getMessageService() {
	return messageService;
    }

    public void setSpreadsheetDao(SpreadsheetDAO spreadsheetDao) {
	this.spreadsheetDao = spreadsheetDao;
    }

    public void setSpreadsheetSessionDao(SpreadsheetSessionDAO spreadsheetSessionDao) {
	this.spreadsheetSessionDao = spreadsheetSessionDao;
    }

    public void setSpreadsheetToolContentHandler(SpreadsheetToolContentHandler spreadsheetToolContentHandler) {
	this.spreadsheetToolContentHandler = spreadsheetToolContentHandler;
    }

    public void setSpreadsheetUserDao(SpreadsheetUserDAO spreadsheetUserDao) {
	this.spreadsheetUserDao = spreadsheetUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public void setUserModifiedSpreadsheetDao(UserModifiedSpreadsheetDAO userModifiedSpreadsheetDao) {
	this.userModifiedSpreadsheetDao = userModifiedSpreadsheetDao;
    }

    public void setSpreadsheetMarkDao(SpreadsheetMarkDAO spreadsheetMarkDao) {
	this.spreadsheetMarkDao = spreadsheetMarkDao;
    }

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Spreadsheet toolContentObj = spreadsheetDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultSpreadsheet();
	    } catch (SpreadsheetApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the spreadsheet tool");
	}

	// set SpreadsheetToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Spreadsheet.newInstance(toolContentObj, toolContentId);
	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, spreadsheetToolContentHandler,
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
	    exportContentService.registerImportVersionFilterClass(SpreadsheetImportContentVersionFilter.class);
	
	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, spreadsheetToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Spreadsheet)) {
		throw new ImportToolContentException(
			"Import Share spreadsheet tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Spreadsheet toolContentObj = (Spreadsheet) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    SpreadsheetUser user = spreadsheetUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new SpreadsheetUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setSpreadsheet(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    spreadsheetDao.saveObject(toolContentObj);
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
	    throw new ToolException("Failed to create the SharedSpreadsheetFiles tool seession");
	}

	Spreadsheet spreadsheet = null;
	if (fromContentId != null) {
	    spreadsheet = spreadsheetDao.getByContentId(fromContentId);
	}
	if (spreadsheet == null) {
	    try {
		spreadsheet = getDefaultSpreadsheet();
	    } catch (SpreadsheetApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Spreadsheet toContent = Spreadsheet.newInstance(spreadsheet, toContentId);
	spreadsheetDao.saveObject(toContent);
    }

    public String getToolContentTitle(Long toolContentId) {
	return getSpreadsheetByContentId(toolContentId).getTitle();
    }
   
    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Spreadsheet spreadsheet = spreadsheetDao.getByContentId(toolContentId);
	if (spreadsheet == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	spreadsheet.setDefineLater(false);
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getSpreadsheetByContentId(toolContentId).isDefineLater();
    }
    
    @Override
    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	Spreadsheet spreadsheet = spreadsheetDao.getByContentId(toolContentId);
	if (removeSessionData) {
	    List list = spreadsheetSessionDao.getByContentId(toolContentId);
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		SpreadsheetSession session = (SpreadsheetSession) iter.next();
		spreadsheetSessionDao.delete(session);
	    }
	}
	spreadsheetDao.delete(spreadsheet);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (log.isDebugEnabled()) {
	    log.debug("Removing Spreadsheet contents for user ID " + userId + " and toolContentId " + toolContentId);
	}

	List<SpreadsheetSession> sessions = spreadsheetSessionDao.getByContentId(toolContentId);
	for (SpreadsheetSession session : sessions) {
	    SpreadsheetUser user = spreadsheetUserDao.getUserByUserIDAndSessionID(userId.longValue(),
		    session.getSessionId());
	    if (user != null) {
		user.setSessionFinished(false);
		if (user.getUserModifiedSpreadsheet() != null) {
		    userModifiedSpreadsheetDao.removeObject(UserModifiedSpreadsheet.class, user
			    .getUserModifiedSpreadsheet().getUid());
		}

		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			SpreadsheetConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    spreadsheetDao.removeObject(NotebookEntry.class, entry.getUid());
		}
		
		spreadsheetUserDao.removeObject(SpreadsheetUser.class, user.getUid());
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	SpreadsheetSession session = new SpreadsheetSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Spreadsheet spreadsheet = spreadsheetDao.getByContentId(toolContentId);
	session.setSpreadsheet(spreadsheet);
	spreadsheetSessionDao.saveObject(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    SpreadsheetServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    SpreadsheetServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	SpreadsheetSession session = spreadsheetSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(SpreadsheetConstants.COMPLETED);
	    spreadsheetSessionDao.saveObject(session);
	} else {
	    SpreadsheetServiceImpl.log.error("Fail to leave tool Session.Could not find shared spreadsheet "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared spreadsheet session by given session id: " + toolSessionId);
	}
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	spreadsheetSessionDao.deleteBySessionId(toolSessionId);
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
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    @Override
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	Spreadsheet toolContentObj = getSpreadsheetByContentId(toolContentId);
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

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }
}
