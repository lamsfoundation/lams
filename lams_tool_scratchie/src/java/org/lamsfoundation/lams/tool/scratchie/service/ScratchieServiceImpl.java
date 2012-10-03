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
package org.lamsfoundation.lams.tool.scratchie.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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
import java.util.Vector;

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
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.service.ILessonService;
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
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieItemDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieAnswerVisitDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieAttachmentDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieSessionDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieUserDAO;
import org.lamsfoundation.lams.tool.scratchie.dto.GroupSummary;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.Summary;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAttachment;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.util.ReflectDTOComparator;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieAnswerComparator;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;

/**
 * 
 * @author Andrey Balan
 */
public class ScratchieServiceImpl implements IScratchieService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager {
    static Logger log = Logger.getLogger(ScratchieServiceImpl.class.getName());

    private ScratchieDAO scratchieDao;

    private ScratchieItemDAO scratchieItemDao;

    private ScratchieAttachmentDAO scratchieAttachmentDao;

    private ScratchieUserDAO scratchieUserDao;

    private ScratchieSessionDAO scratchieSessionDao;

    private ScratchieAnswerVisitDAO scratchieAnswerVisitDao;

    // tool service
    private ScratchieToolContentHandler scratchieToolContentHandler;

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

    private ILessonService lessonService;

    private ScratchieOutputFactory scratchieOutputFactory;

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
    private IVersionedNode getFile(Long uuid, Long versionId, String relativePath) throws ScratchieApplicationException {

	ITicket tic = getRepositoryLoginTicket();

	try {

	    return repositoryService.getFileItem(tic, uuid, versionId, relativePath);

	} catch (AccessDeniedException e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + ".";

	    error = error + "AccessDeniedException: " + e.getMessage() + " Unable to retry further.";
	    ScratchieServiceImpl.log.error(error);
	    throw new ScratchieApplicationException(error, e);

	} catch (Exception e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + "." + " Exception: " + e.getMessage();
	    ScratchieServiceImpl.log.error(error);
	    throw new ScratchieApplicationException(error, e);

	}
    }

    /**
     * This method verifies the credentials of the Scratchie Tool and gives it the <code>Ticket</code> to login and
     * access the Content Repository.
     * 
     * A valid ticket is needed in order to access the content from the repository. This method would be called evertime
     * the tool needs to upload/download files from the content repository.
     * 
     * @return ITicket The ticket for repostory access
     * @throws ScratchieApplicationException
     */
    private ITicket getRepositoryLoginTicket() throws ScratchieApplicationException {
	ICredentials credentials = new SimpleCredentials(scratchieToolContentHandler.getRepositoryUser(),
		scratchieToolContentHandler.getRepositoryId());
	try {
	    ITicket ticket = repositoryService.login(credentials,
		    scratchieToolContentHandler.getRepositoryWorkspaceName());
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new ScratchieApplicationException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new ScratchieApplicationException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new ScratchieApplicationException("Login failed." + e.getMessage());
	}
    }

    public Scratchie getScratchieByContentId(Long contentId) {
	Scratchie rs = scratchieDao.getByContentId(contentId);
	if (rs == null) {
	    ScratchieServiceImpl.log.debug("Could not find the content by given ID:" + contentId);
	}
	return rs;
    }

    public Scratchie getDefaultContent(Long contentId) throws ScratchieApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ScratchieServiceImpl.log.error(error);
	    throw new ScratchieApplicationException(error);
	}

	Scratchie defaultContent = getDefaultScratchie();
	// save default content by given ID.
	Scratchie content = new Scratchie();
	content = Scratchie.newInstance(defaultContent, contentId, scratchieToolContentHandler);
	return content;
    }

    public List getAuthoredItems(Long scratchieUid) {
	return scratchieItemDao.getAuthoringItems(scratchieUid);
    }

    public ScratchieAttachment uploadInstructionFile(FormFile uploadFile, String fileType)
	    throws UploadScratchieFileException {
	if (uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName())) {
	    throw new UploadScratchieFileException(messageService.getMessage("error.msg.upload.file.not.found",
		    new Object[] { uploadFile }));
	}

	// upload file to repository
	NodeKey nodeKey = processFile(uploadFile, fileType);

	// create new attachement
	ScratchieAttachment file = new ScratchieAttachment();
	file.setFileType(fileType);
	file.setFileUuid(nodeKey.getUuid());
	file.setFileVersionId(nodeKey.getVersion());
	file.setFileName(uploadFile.getFileName());
	file.setCreated(new Date());

	return file;
    }

    public void createUser(ScratchieUser scratchieUser) {
	scratchieUserDao.saveObject(scratchieUser);
    }

    public ScratchieUser getUserByIDAndContent(Long userId, Long contentId) {

	return scratchieUserDao.getUserByUserIDAndContentID(userId, contentId);

    }

    public ScratchieUser getUserByIDAndSession(Long userId, Long sessionId) {

	return scratchieUserDao.getUserByUserIDAndSessionID(userId, sessionId);

    }

    public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws ScratchieApplicationException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, fileUuid, fileVersionId);
	} catch (Exception e) {
	    throw new ScratchieApplicationException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    public void saveOrUpdateScratchie(Scratchie scratchie) {
	scratchieDao.saveObject(scratchie);
    }

    public void deleteScratchieAttachment(Long attachmentUid) {
	scratchieAttachmentDao.removeObject(ScratchieAttachment.class, attachmentUid);

    }

    public void saveOrUpdateScratchieItem(ScratchieItem item) {
	scratchieItemDao.saveObject(item);
    }

    public void deleteScratchieItem(Long uid) {
	scratchieItemDao.removeObject(ScratchieItem.class, uid);
    }

    public List<Summary> exportBySessionId(Long sessionId) {
	ScratchieSession session = scratchieSessionDao.getSessionBySessionId(sessionId);
	if (session == null) {
	    ScratchieServiceImpl.log.error("Failed get ScratchieSession by ID [" + sessionId + "]");
	    return null;
	}
	// initial scratchie items list
	List<Summary> itemList = new ArrayList();
	Set<ScratchieItem> resList = session.getScratchie().getScratchieItems();
	for (ScratchieItem item : resList) {
	    // if item is create by author
	    if (item.isCreateByAuthor()) {
		Summary sum = new Summary(session.getSessionId(), session.getSessionName(), item, false);
		itemList.add(sum);
	    }
	}

	// get this session's all scratchie items
	Set<ScratchieItem> sessList = session.getScratchieItems();
	for (ScratchieItem item : sessList) {

	    // to skip all item create by author
	    if (!item.isCreateByAuthor()) {
		Summary sum = new Summary(session.getSessionId(), session.getSessionName(), item, false);
		itemList.add(sum);
	    }
	}

	return itemList;
    }

    public List<List<Summary>> exportByContentId(Long contentId) {
	Scratchie scratchie = scratchieDao.getByContentId(contentId);
	List<List<Summary>> groupList = new ArrayList();

	// create init scratchie items list
	List<Summary> initList = new ArrayList();
	groupList.add(initList);
	Set<ScratchieItem> resList = scratchie.getScratchieItems();
	for (ScratchieItem item : resList) {
	    if (item.isCreateByAuthor()) {
		Summary sum = new Summary(null, null, item, true);
		initList.add(sum);
	    }
	}

	// session by session
	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    List<Summary> group = new ArrayList<Summary>();
	    // get this session's all scratchie items
	    Set<ScratchieItem> sessList = session.getScratchieItems();
	    for (ScratchieItem item : sessList) {
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

    public Scratchie getScratchieBySessionId(Long sessionId) {
	ScratchieSession session = scratchieSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getScratchie().getContentId();
	Scratchie res = scratchieDao.getByContentId(contentId);
	return res;
    }

    public ScratchieSession getScratchieSessionBySessionId(Long sessionId) {
	return scratchieSessionDao.getSessionBySessionId(sessionId);
    }

    public void saveOrUpdateScratchieSession(ScratchieSession resSession) {
	scratchieSessionDao.saveObject(resSession);
    }

    public void setAnswerAccess(Long answerUid, Long userId, Long sessionId) {
	ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getScratchieAnswerLog(answerUid, userId);
	if (log == null) {
	    log = new ScratchieAnswerVisitLog();
	    ScratchieAnswer answer = getScratchieAnswerById(answerUid);
	    log.setScratchieAnswer(answer);
	    ScratchieUser user = scratchieUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	    log.setUser(user);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	    scratchieAnswerVisitDao.saveObject(log);
	}
    }
    
    public ScratchieAnswer getScratchieAnswerById (Long answerUid) {
	return  (ScratchieAnswer) userManagementService.findById(ScratchieAnswer.class, answerUid);
    }
    
    public void setScratchingFinished(Long toolSessionId, Long userId) {
	ScratchieUser user = scratchieUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setScratchingFinished(true);
	scratchieUserDao.saveObject(user);
    }

    public String finishToolSession(Long toolSessionId, Long userId) throws ScratchieApplicationException {
	String nextUrl = null;
	try {
	    ScratchieUser user = scratchieUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	    user.setSessionFinished(true);
	    scratchieUserDao.saveObject(user);
	    
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new ScratchieApplicationException(e);
	} catch (ToolException e) {
	    throw new ScratchieApplicationException(e);
	}
	return nextUrl;
    }

    public ScratchieItem getScratchieItemByUid(Long itemUid) {
	return scratchieItemDao.getByUid(itemUid);
    }
    
    @Override
    public Set<ScratchieUser> getAllLearners(Long contentId) {

	Scratchie scratchie = scratchieDao.getByContentId(contentId);
	Set<ScratchieUser> users = new TreeSet<ScratchieUser>();
	
	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    List<ScratchieUser> sessionUsers = scratchieUserDao.getBySessionID(sessionId);
	    users.addAll(sessionUsers);
	}
	
	return users;
    }
    
    public List<GroupSummary> getMonitoringSummary(Long contentId) {
	List<GroupSummary> groupSummaryList = new ArrayList<GroupSummary>();

	Scratchie scratchie = scratchieDao.getByContentId(contentId);
	
	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(sessionId, session.getSessionName());

	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);
	    for (ScratchieUser user : users) {
		
		int totalAttempts = scratchieAnswerVisitDao.getLogCountTotal(sessionId, user.getUserId()); 
		user.setTotalAttempts(totalAttempts);
		
		//for displaying purposes if there is no attemps we assign -1 which will be shown as "-"
		int mark = (totalAttempts == 0) ? -1 : getUserMark(sessionId, user.getUserId());		
		user.setMark(mark);
	    }
	    
	    groupSummary.setUsers(users);
	    groupSummaryList.add(groupSummary);
	}

	return groupSummaryList;
    }
    
    public void retrieveScratchesOrder(Collection<ScratchieItem> items, ScratchieUser user) {
	
	for (ScratchieItem item : items) {
	    List<ScratchieAnswerVisitLog> itemLogs = scratchieAnswerVisitDao.getLogsByScratchieUserAndItem(user.getUid(), item.getUid());
	    
	    for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {

		int attemptNumber;
		ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getScratchieAnswerLog(answer.getUid(), user.getUserId());
		if (log == null) {
		    // -1 if there is no log
		    attemptNumber = -1;
		} else {
		    //adding 1 to start from 1.
		    attemptNumber = itemLogs.indexOf(log) + 1;
		}

		answer.setAttemptOrder(attemptNumber);
	    }
	}
	
    }
    
    public void retrieveScratched(Collection<ScratchieItem> items, ScratchieUser user) {
	
	for (ScratchieItem item : items) {
	    
	    for (ScratchieAnswer answer : (Set<ScratchieAnswer>)item.getAnswers()) {
		ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getScratchieAnswerLog(answer.getUid(),
			user.getUserId());
		if (log == null) {
		    answer.setScratched(false);
		} else {
		    answer.setScratched(true);
		}
	    }
	    
	    item.setUnraveled(isItemUnraveled(item, user.getUserId()));
	}
    }
    
    private boolean isItemUnraveled(ScratchieItem item, Long userId) {
	boolean isItemUnraveled = false;

	for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {
	    ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getScratchieAnswerLog(answer.getUid(), userId);
	    if (log != null) {
		isItemUnraveled |= answer.isCorrect();
	    }
	}

	return isItemUnraveled;
    }
    
    public int getUserMark(Long sessionId, Long userId) {
	ScratchieUser user = getUserByIDAndSession(userId, sessionId);
	ScratchieSession session = scratchieSessionDao.getSessionBySessionId(sessionId);
	Scratchie scratchie = session.getScratchie();
	Set<ScratchieItem> items = scratchie.getScratchieItems();
	
	int mark = 0;
	for (ScratchieItem item : items) {
	    mark += getUserMarkPerItem(scratchie, item, sessionId, userId);
	}
	
	return mark;
    }
    
    /**
     * 
     * 
     * @param sessionId
     * @param userId
     * @param item
     * @return
     */
    private int getUserMarkPerItem(Scratchie scratchie, ScratchieItem item, Long sessionId, Long userId) {
	
	int mark = 0;
	// add mark only if an item was unraveled
	if (isItemUnraveled(item, userId)) {
	    int attempts = scratchieAnswerVisitDao.getLogCountPerItem(sessionId, userId, item.getUid());
	    mark += item.getAnswers().size() - attempts;

	    // add extra point if needed
	    if (scratchie.isExtraPoint() && (attempts == 1)) {
		mark++;
	    }
	}
	
	return mark;
    }
    
    public Set<ScratchieItem> populateItemsResults(Long sessionId, Long userId) {
	ScratchieUser user = getUserByIDAndSession(userId, sessionId);
	ScratchieSession session = scratchieSessionDao.getSessionBySessionId(sessionId);
	Scratchie scratchie = session.getScratchie();
	Set<ScratchieItem> items = scratchie.getScratchieItems();
	
	for (ScratchieItem item : items) {
	    int mark = getUserMarkPerItem(scratchie, item, sessionId, userId);
	    item.setUserMark(mark);
	    
	    int attempts = scratchieAnswerVisitDao.getLogCountPerItem(sessionId, userId, item.getUid());
	    item.setUserAttempts(attempts);
	    
	    String correctAnswer = "";
	    for (ScratchieAnswer answer : (Set<ScratchieAnswer>)item.getAnswers()) {
		if (answer.isCorrect()) {
		    correctAnswer = answer.getDescription();
		}
	    }
	    item.setCorrectAnswer(correctAnswer);
	}
	
	return items;
    }
    
    public List<GroupSummary> getQuestionSummary(Long contentId, Long itemUid) {
	
	List<GroupSummary> groupSummaryList = new ArrayList<GroupSummary>();

	Scratchie scratchie = scratchieDao.getByContentId(contentId);
	ScratchieItem item = scratchieItemDao.getByUid(itemUid);
	Collection<ScratchieAnswer> answers = item.getAnswers();
	
	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    
	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(sessionId, session.getSessionName());
	    
	    Map<Long, ScratchieAnswer> answerMap = new HashMap<Long, ScratchieAnswer>();
	    for (ScratchieAnswer dbAnswer : (Set<ScratchieAnswer>)answers) {
		
		//clone it so it doesn't interfere with values from other sessions
		ScratchieAnswer answer = (ScratchieAnswer) dbAnswer.clone();
		int[] attempts = new int[answers.size()];
		answer.setAttempts(attempts);
		answerMap.put(dbAnswer.getUid(), answer);
	    }

	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);
	    //calculate attempts table
	    for (ScratchieUser user : users) {
		
		int attemptNumber = 0;
		List<ScratchieAnswerVisitLog> userAttempts = scratchieAnswerVisitDao.getLogsByScratchieUserAndItem(user.getUid(), itemUid);
		for (ScratchieAnswerVisitLog userAttempt : userAttempts) {
		    ScratchieAnswer answer = answerMap.get(userAttempt.getScratchieAnswer().getUid());
		    int[] attempts = answer.getAttempts();
		    //+1 for corresponding choice
		    attempts[attemptNumber++]++;
		}
	    }
	    
	    Collection<ScratchieAnswer> sortedAnswers = new TreeSet<ScratchieAnswer>(new ScratchieAnswerComparator());
	    sortedAnswers.addAll(answerMap.values());
	    groupSummary.setAnswers(sortedAnswers);
	    groupSummaryList.add(groupSummary);
	}

	return groupSummaryList;
    }

    public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry) {
	Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    boolean hasRefection = session.getScratchie().isReflectOnActivity();
	    Set<ReflectDTO> list = new TreeSet<ReflectDTO>(new ReflectDTOComparator());
	    // get all users in this session
	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);
	    for (ScratchieUser user : users) {
		ReflectDTO ref = new ReflectDTO(user);

		if (setEntry) {
		    NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			    ScratchieConstants.TOOL_SIGNATURE, user.getUserId().intValue());
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

    public ScratchieUser getUser(Long uid) {
	return (ScratchieUser) scratchieUserDao.getObject(ScratchieUser.class, uid);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Scratchie getDefaultScratchie() throws ScratchieApplicationException {
	Long defaultScratchieId = getToolDefaultContentIdBySignature(ScratchieConstants.TOOL_SIGNATURE);
	Scratchie defaultScratchie = getScratchieByContentId(defaultScratchieId);
	if (defaultScratchie == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ScratchieServiceImpl.log.error(error);
	    throw new ScratchieApplicationException(error);
	}

	return defaultScratchie;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws ScratchieApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    ScratchieServiceImpl.log.error(error);
	    throw new ScratchieApplicationException(error);
	}
	return contentId;
    }

    /**
     * Process an uploaded file.
     * 
     * @throws ScratchieApplicationException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file, String fileType) throws UploadScratchieFileException {
	NodeKey node = null;
	if (file != null && !StringUtils.isEmpty(file.getFileName())) {
	    String fileName = file.getFileName();
	    try {
		node = scratchieToolContentHandler.uploadFile(file.getInputStream(), fileName, file.getContentType(),
			fileType);
	    } catch (InvalidParameterException e) {
		throw new UploadScratchieFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	    } catch (FileNotFoundException e) {
		throw new UploadScratchieFileException(messageService.getMessage("error.msg.file.not.found"));
	    } catch (RepositoryCheckedException e) {
		throw new UploadScratchieFileException(messageService.getMessage("error.msg.repository"));
	    } catch (IOException e) {
		throw new UploadScratchieFileException(messageService.getMessage("error.msg.io.exception"));
	    }
	}
	return node;
    }

    private NodeKey processPackage(String packageDirectory, String initFile) throws UploadScratchieFileException {
	NodeKey node = null;
	try {
	    node = scratchieToolContentHandler.uploadPackage(packageDirectory, initFile);
	} catch (InvalidParameterException e) {
	    throw new UploadScratchieFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	} catch (RepositoryCheckedException e) {
	    throw new UploadScratchieFileException(messageService.getMessage("error.msg.repository"));
	}
	return node;
    }

    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
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

    public void setScratchieAttachmentDao(ScratchieAttachmentDAO scratchieAttachmentDao) {
	this.scratchieAttachmentDao = scratchieAttachmentDao;
    }

    public void setScratchieDao(ScratchieDAO scratchieDao) {
	this.scratchieDao = scratchieDao;
    }

    public void setScratchieItemDao(ScratchieItemDAO scratchieAnswerDao) {
	this.scratchieItemDao = scratchieAnswerDao;
    }

    public void setScratchieSessionDao(ScratchieSessionDAO scratchieSessionDao) {
	this.scratchieSessionDao = scratchieSessionDao;
    }

    public void setScratchieToolContentHandler(ScratchieToolContentHandler scratchieToolContentHandler) {
	this.scratchieToolContentHandler = scratchieToolContentHandler;
    }

    public void setScratchieUserDao(ScratchieUserDAO scratchieUserDao) {
	this.scratchieUserDao = scratchieUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public ScratchieAnswerVisitDAO getScratchieAnswerVisitDao() {
	return scratchieAnswerVisitDao;
    }

    public void setScratchieAnswerVisitDao(ScratchieAnswerVisitDAO scratchieItemVisitDao) {
	this.scratchieAnswerVisitDao = scratchieItemVisitDao;
    }

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Scratchie toolContentObj = scratchieDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultScratchie();
	    } catch (ScratchieApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the scratchie tool");
	}

	// set ScratchieToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Scratchie.newInstance(toolContentObj, toolContentId, null);
	toolContentObj.setToolContentHandler(null);
	toolContentObj.setOfflineFileList(null);
	toolContentObj.setOnlineFileList(null);
	try {
	    exportContentService.registerFileClassForExport(ScratchieAttachment.class.getName(), "fileUuid",
		    "fileVersionId");
	    exportContentService.registerFileClassForExport(ScratchieItem.class.getName(), "fileUuid", "fileVersionId");
	    exportContentService
		    .exportToolContent(toolContentId, toolContentObj, scratchieToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    exportContentService.registerFileClassForImport(ScratchieAttachment.class.getName(), "fileUuid",
		    "fileVersionId", "fileName", "fileType", null, null);
	    exportContentService.registerFileClassForImport(ScratchieItem.class.getName(), "fileUuid", "fileVersionId",
		    "fileName", "fileType", null, "initialItem");

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, scratchieToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Scratchie)) {
		throw new ImportToolContentException(
			"Import Share scratchie tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Scratchie toolContentObj = (Scratchie) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    ScratchieUser user = scratchieUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new ScratchieUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setScratchie(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    // reset all scratchieItem createBy user
	    Set<ScratchieItem> items = toolContentObj.getScratchieItems();
	    for (ScratchieItem item : items) {
		item.setCreateBy(user);
	    }
	    scratchieDao.saveObject(toolContentObj);
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
     * @throws ScratchieApplicationException
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Scratchie content = getScratchieByContentId(toolContentId);
	if (content == null) {
	    try {
		content = getDefaultContent(toolContentId);
	    } catch (ScratchieApplicationException e) {
		throw new ToolException(e);
	    }
	}
	return getScratchieOutputFactory().getToolOutputDefinitions(content, definitionType);
    }

    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedScratchieFiles tool seession");
	}

	Scratchie scratchie = null;
	if (fromContentId != null) {
	    scratchie = scratchieDao.getByContentId(fromContentId);
	}
	if (scratchie == null) {
	    try {
		scratchie = getDefaultScratchie();
	    } catch (ScratchieApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Scratchie toContent = Scratchie.newInstance(scratchie, toContentId, scratchieToolContentHandler);
	scratchieDao.saveObject(toContent);

	// save scratchie items as well
	Set items = toContent.getScratchieItems();
	if (items != null) {
	    Iterator iter = items.iterator();
	    while (iter.hasNext()) {
		ScratchieItem item = (ScratchieItem) iter.next();
		// createRootTopic(toContent.getUid(),null,msg);
	    }
	}
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	Scratchie scratchie = scratchieDao.getByContentId(toolContentId);
	if (scratchie == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	scratchie.setDefineLater(value);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	Scratchie scratchie = scratchieDao.getByContentId(toolContentId);
	if (scratchie == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	scratchie.setRunOffline(value);
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	Scratchie scratchie = scratchieDao.getByContentId(toolContentId);
	if (removeSessionData) {
	    List list = scratchieSessionDao.getByContentId(toolContentId);
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		ScratchieSession session = (ScratchieSession) iter.next();
		scratchieSessionDao.delete(session);
	    }
	}
	scratchieDao.delete(scratchie);
    }

    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	ScratchieSession session = new ScratchieSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Scratchie scratchie = scratchieDao.getByContentId(toolContentId);
	session.setScratchie(scratchie);
	scratchieSessionDao.saveObject(session);
    }

    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    ScratchieServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    ScratchieServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	ScratchieSession session = scratchieSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(ScratchieConstants.COMPLETED);
	    scratchieSessionDao.saveObject(session);
	} else {
	    ScratchieServiceImpl.log.error("Fail to leave tool Session.Could not find shared scratchie "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared scratchie session by given session id: " + toolSessionId);
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
	scratchieSessionDao.deleteBySessionId(toolSessionId);
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getScratchieOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getScratchieOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Scratchie toolContentObj = new Scratchie();

	try {
	    toolContentObj.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	    toolContentObj.setContentId(toolContentId);
	    toolContentObj.setContentInUse(Boolean.FALSE);
	    toolContentObj.setCreated(now);
	    toolContentObj.setDefineLater(Boolean.FALSE);
	    toolContentObj.setInstructions(WebUtil.convertNewlines((String) importValues
		    .get(ToolContentImport102Manager.CONTENT_BODY)));
	    toolContentObj.setOfflineInstructions(null);
	    toolContentObj.setOnlineInstructions(null);
	    toolContentObj.setRunOffline(Boolean.FALSE);
	    toolContentObj.setUpdated(now);
	    toolContentObj.setReflectOnActivity(Boolean.FALSE);
	    toolContentObj.setExtraPoint(Boolean.FALSE);
	    toolContentObj.setShowResultsPage(Boolean.TRUE);
	    toolContentObj.setReflectInstructions(null);

	    // leave as empty, no need to set them to anything.
	    // toolContentObj.setAttachments(attachments);

	    /*
	     * unused entries from 1.0.2 [directoryName=] no equivalent in 2.0 [runtimeSubmissionStaffFile=true] no
	     * equivalent in 2.0 [contentShowUser=false] no equivalent in 2.0 [isHTML=false] no equivalent in 2.0
	     * [showbuttons=false] no equivalent in 2.0 [isReusable=false] not used in 1.0.2 (would be lock when
	     * finished)
	     */
	    ScratchieUser ruser = new ScratchieUser();
	    ruser.setUserId(new Long(user.getUserID().longValue()));
	    ruser.setFirstName(user.getFirstName());
	    ruser.setLastName(user.getLastName());
	    ruser.setLoginName(user.getLogin());
	    createUser(ruser);
	    toolContentObj.setCreatedBy(ruser);

	    // Scratchie Items. They are ordered on the screen by create date so they need to be saved in the right
	    // order.
	    // So read them all in first, then go through and assign the dates in the correct order and then save.
	    Vector urls = (Vector) importValues.get(ToolContentImport102Manager.CONTENT_URL_URLS);
	    SortedMap<Integer, ScratchieItem> items = new TreeMap<Integer, ScratchieItem>();
	    if (urls != null) {
		Iterator iter = urls.iterator();
		while (iter.hasNext()) {
		    Hashtable urlMap = (Hashtable) iter.next();
		    Integer itemOrder = WDDXProcessor.convertToInteger(urlMap,
			    ToolContentImport102Manager.CONTENT_URL_URL_VIEW_ORDER);
		    ScratchieItem item = new ScratchieItem();
		    //TODO check if this right?
		    item.setDescription((String) urlMap.get(ToolContentImport102Manager.CONTENT_TITLE));
		    item.setCreateBy(ruser);
		    item.setCreateByAuthor(true);

		    items.put(itemOrder, item);
		}
	    }

	    Iterator iter = items.values().iterator();
	    Date itemDate = null;
	    while (iter.hasNext()) {
		if (itemDate != null) {
		    try {
			Thread.sleep(1000);
		    } catch (Exception e) {
		    }
		}
		itemDate = new Date();

		ScratchieItem item = (ScratchieItem) iter.next();
		item.setCreateDate(itemDate);
		toolContentObj.getScratchieItems().add(item);
	    }

	} catch (WDDXProcessorConversionException e) {
	    ScratchieServiceImpl.log.error("Unable to content for activity " + toolContentObj.getTitle()
		    + "properly due to a WDDXProcessorConversionException.", e);
	    throw new ToolException(
		    "Invalid import data format for activity "
			    + toolContentObj.getTitle()
			    + "- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
	}

	scratchieDao.saveObject(toolContentObj);

    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	Scratchie toolContentObj = getScratchieByContentId(toolContentId);
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

    public ILessonService getLessonService() {
	return lessonService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    /**
     * Finds out which lesson the given tool content belongs to and returns its monitoring users.
     * 
     * @param sessionId
     *            tool session ID
     * @return list of teachers that monitor the lesson which contains the tool with given session ID
     */
    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return getLessonService().getMonitorsByToolSessionId(sessionId);
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getScratchieOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    public ScratchieOutputFactory getScratchieOutputFactory() {
	return scratchieOutputFactory;
    }

    public void setScratchieOutputFactory(ScratchieOutputFactory scratchieOutputFactory) {
	this.scratchieOutputFactory = scratchieOutputFactory;
    }
}
