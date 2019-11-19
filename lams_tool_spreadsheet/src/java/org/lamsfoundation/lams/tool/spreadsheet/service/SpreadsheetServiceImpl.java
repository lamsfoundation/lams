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

package org.lamsfoundation.lams.tool.spreadsheet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
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
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetSessionDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetUserDAO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.Summary;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetMark;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetSession;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.model.UserModifiedSpreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.util.ReflectDTOComparator;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Andrey Balan
 */
public class SpreadsheetServiceImpl implements ISpreadsheetService, ToolContentManager, ToolSessionManager {
    private static Logger log = Logger.getLogger(SpreadsheetServiceImpl.class.getName());
    private SpreadsheetDAO spreadsheetDao;
    private SpreadsheetUserDAO spreadsheetUserDao;
    private SpreadsheetSessionDAO spreadsheetSessionDao;
    // tool service
    private IToolContentHandler spreadsheetToolContentHandler;
    private MessageService messageService;
    // system services
    private ILamsToolService toolService;
    private IUserManagementService userManagementService;
    private IExportToolContentService exportContentService;
    private ICoreNotebookService coreNotebookService;

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public Spreadsheet getSpreadsheetByContentId(Long contentId) {
	return spreadsheetDao.getByContentId(contentId);
    }

    @Override
    public Spreadsheet getDefaultContent(Long contentId) throws SpreadsheetApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new SpreadsheetApplicationException(error);
	}

	Spreadsheet defaultContent = getDefaultSpreadsheet();
	// save default content by given ID.
	Spreadsheet content = new Spreadsheet();
	content = Spreadsheet.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public void saveOrUpdateUser(SpreadsheetUser spreadsheetUser) {
	spreadsheetUserDao.saveObject(spreadsheetUser);
    }

    @Override
    public void saveOrUpdateUserModifiedSpreadsheet(UserModifiedSpreadsheet userModifiedSpreadsheet) {
	spreadsheetDao.saveObject(userModifiedSpreadsheet);
    }

    @Override
    public SpreadsheetUser getUserByIDAndContent(Long userId, Long contentId) {
	return spreadsheetUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public SpreadsheetUser getUserByIDAndSession(Long userId, Long sessionId) {
	return spreadsheetUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public List<SpreadsheetUser> getUserListBySessionId(Long sessionId) {
	return spreadsheetUserDao.getBySessionID(sessionId);
    }

    @Override
    public void saveOrUpdateSpreadsheet(Spreadsheet spreadsheet) {
	spreadsheetDao.saveObject(spreadsheet);
    }

    @Override
    public Spreadsheet getSpreadsheetBySessionId(Long sessionId) {
	SpreadsheetSession session = spreadsheetSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getSpreadsheet().getContentId();
	Spreadsheet res = spreadsheetDao.getByContentId(contentId);
	return res;
    }

    @Override
    public SpreadsheetSession getSessionBySessionId(Long sessionId) {
	return spreadsheetSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public void saveOrUpdateSpreadsheetSession(SpreadsheetSession resSession) {
	spreadsheetSessionDao.saveObject(resSession);
    }

    @Override
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

    @Override
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
    public List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries) {
	return spreadsheetUserDao.getUsersForTablesorter(sessionId, page, size, sorting, searchString,
		getNotebookEntries, coreNotebookService, userManagementService);
    }

    @Override
    public int getCountUsersBySession(Long sessionId, String searchString) {
	return spreadsheetUserDao.getCountUsersBySession(sessionId, searchString);
    }

    @Override
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

    @Override
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
    public SpreadsheetUser getUser(Long uid) {
	return (SpreadsheetUser) spreadsheetUserDao.getObject(SpreadsheetUser.class, uid);
    }

    @Override
    public void releaseMarksForSession(Long sessionId) {
	List<SpreadsheetUser> users = spreadsheetUserDao.getBySessionID(sessionId);
	for (SpreadsheetUser user : users) {
	    if ((user.getUserModifiedSpreadsheet() != null) && (user.getUserModifiedSpreadsheet().getMark() != null)) {
		SpreadsheetMark mark = user.getUserModifiedSpreadsheet().getMark();
		mark.setDateMarksReleased(new Date());
		spreadsheetDao.saveObject(mark);

		// send marks to gradebook where applicable
		if (mark.getMarks() != null) {
		    Double doubleMark = mark.getMarks().doubleValue();
		    toolService.updateActivityMark(doubleMark, null, user.getUserId().intValue(), sessionId,
			    false);
		}
	    }
	}
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

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Spreadsheet getDefaultSpreadsheet() throws SpreadsheetApplicationException {
	Long defaultSpreadsheetId = getToolDefaultContentIdBySignature(SpreadsheetConstants.TOOL_SIGNATURE);
	Spreadsheet defaultSpreadsheet = getSpreadsheetByContentId(defaultSpreadsheetId);
	if (defaultSpreadsheet == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new SpreadsheetApplicationException(error);
	}

	return defaultSpreadsheet;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws SpreadsheetApplicationException {
	Long contentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new SpreadsheetApplicationException(error);
	}
	return contentId;
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    @Override
    public MessageService getMessageService() {
	return messageService;
    }

    public void setSpreadsheetDao(SpreadsheetDAO spreadsheetDao) {
	this.spreadsheetDao = spreadsheetDao;
    }

    public void setSpreadsheetSessionDao(SpreadsheetSessionDAO spreadsheetSessionDao) {
	this.spreadsheetSessionDao = spreadsheetSessionDao;
    }

    public void setSpreadsheetToolContentHandler(IToolContentHandler spreadsheetToolContentHandler) {
	this.spreadsheetToolContentHandler = spreadsheetToolContentHandler;
    }

    public void setSpreadsheetUserDao(SpreadsheetUserDAO spreadsheetUserDao) {
	this.spreadsheetUserDao = spreadsheetUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
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
	    SpreadsheetUser user = spreadsheetUserDao.getUserByUserIDAndContentID(newUserUid.longValue(),
		    toolContentId);
	    if (user == null) {
		user = new SpreadsheetUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(newUserUid.longValue());
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

    @Override
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
    public boolean isReadOnly(Long toolContentId) {
	List<SpreadsheetSession> sessions = spreadsheetSessionDao.getByContentId(toolContentId);
	for (SpreadsheetSession session : sessions) {
	    if (!spreadsheetUserDao.getBySessionID(session.getSessionId()).isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Spreadsheet spreadsheet = spreadsheetDao.getByContentId(toolContentId);
	if (spreadsheet == null) {
	    log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (SpreadsheetSession session : spreadsheetSessionDao.getByContentId(toolContentId)) {
	    // this can not be done via DB cascade
	    List<SpreadsheetUser> users = spreadsheetUserDao.getBySessionID(session.getSessionId());
	    for (SpreadsheetUser user : users) {
		UserModifiedSpreadsheet modified = user.getUserModifiedSpreadsheet();
		if (modified != null) {
		    user.setUserModifiedSpreadsheet(null);
		    spreadsheetDao.removeObject(UserModifiedSpreadsheet.class, modified.getUid());
		}
	    }

	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, SpreadsheetConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
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
		    spreadsheetDao.removeObject(UserModifiedSpreadsheet.class,
			    user.getUserModifiedSpreadsheet().getUid());
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
	    log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	SpreadsheetSession session = spreadsheetSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(SpreadsheetConstants.COMPLETED);
	    spreadsheetSessionDao.saveObject(session);
	} else {
	    log.error("Fail to leave tool Session.Could not find shared spreadsheet " + "session by given session id: "
		    + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared spreadsheet session by given session id: " + toolSessionId);
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
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<ToolOutput>();
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

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }
    
    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	SpreadsheetUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isSessionFinished() ? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }
}
