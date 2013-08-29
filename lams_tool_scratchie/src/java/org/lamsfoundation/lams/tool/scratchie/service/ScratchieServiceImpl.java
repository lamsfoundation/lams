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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
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
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieAnswerVisitDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieAttachmentDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieItemDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieSessionDAO;
import org.lamsfoundation.lams.tool.scratchie.dao.ScratchieUserDAO;
import org.lamsfoundation.lams.tool.scratchie.dto.GroupSummary;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.Summary;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAttachment;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieAnswerComparator;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.MessageService;

/**
 * 
 * @author Andrey Balan
 */
public class ScratchieServiceImpl implements IScratchieService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager {
    private static Logger log = Logger.getLogger(ScratchieServiceImpl.class.getName());

    private static final ExcelCell[] EMPTY_ROW = new ExcelCell[4];

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

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IEventNotificationService eventNotificationService;

    private ILamsCoreToolService lamsCoreToolService;
    
    private IActivityDAO activityDAO;

    private ScratchieOutputFactory scratchieOutputFactory;

    // *******************************************************************************
    // Service method
    // *******************************************************************************

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

    @Override
    public Scratchie getScratchieByContentId(Long contentId) {
	Scratchie rs = scratchieDao.getByContentId(contentId);
	if (rs == null) {
	    ScratchieServiceImpl.log.debug("Could not find the content by given ID:" + contentId);
	}
	return rs;
    }

    @Override
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

    @Override
    public List getAuthoredItems(Long scratchieUid) {
	return scratchieItemDao.getAuthoringItems(scratchieUid);
    }

    @Override
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

    @Override
    public void createUser(ScratchieUser scratchieUser) {
	scratchieUserDao.saveObject(scratchieUser);
    }

    @Override
    public ScratchieUser getUserByIDAndContent(Long userId, Long contentId) {

	return scratchieUserDao.getUserByUserIDAndContentID(userId, contentId);

    }

    @Override
    public ScratchieUser getUserByIDAndSession(Long userId, Long sessionId) {

	return scratchieUserDao.getUserByUserIDAndSessionID(userId, sessionId);

    }

    @Override
    public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws ScratchieApplicationException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, fileUuid, fileVersionId);
	} catch (Exception e) {
	    throw new ScratchieApplicationException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    @Override
    public void saveOrUpdateScratchie(Scratchie scratchie) {
	scratchieDao.saveObject(scratchie);
    }

    @Override
    public void deleteScratchieAttachment(Long attachmentUid) {
	scratchieAttachmentDao.removeObject(ScratchieAttachment.class, attachmentUid);

    }

    @Override
    public void deleteScratchieItem(Long uid) {
	scratchieItemDao.removeObject(ScratchieItem.class, uid);
    }

    @Override
    public boolean isUserGroupLeader(ScratchieUser user, ScratchieSession session) {

	ScratchieUser groupLeader = session.getGroupLeader();
	boolean isUserLeader = (groupLeader != null) && user.getUid().equals(groupLeader.getUid());
	return isUserLeader;
    }

    @Override
    public ScratchieUser checkLeaderSelectToolForSessionLeader(ScratchieUser user, Long toolSessionId) {
	if (user == null || toolSessionId == null) {
	    return null;
	}

	ScratchieSession scratchieSession = getScratchieSessionBySessionId(toolSessionId);
	ScratchieUser leader = scratchieSession.getGroupLeader();
	// check leader select tool for a leader only in case scratchie tool doesn't know it. As otherwise it will screw
	// up previous scratches done
	if (leader == null) {

	    ToolSession toolSession = toolService.getToolSession(toolSessionId);
	    LearningDesign learningDesign = toolSession.getToolActivity().getLearningDesign();

	    // find leaderSelection Tool in a lesson
	    /*
	     * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some
	     * mysterious reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	     * 
	     * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it
	     * is one
	     */
	    Set<Activity> activities = new TreeSet<Activity>();
	    Activity firstActivity = activityDAO.getActivityByActivityId(learningDesign.getFirstActivity()
		    .getActivityId());
	    activities.add(firstActivity);
	    activities.addAll(learningDesign.getActivities());

	    ToolActivity leaderSelectionTool = null;
	    for (Activity activity : activities) {
		if (activity instanceof ToolActivity) {
		    ToolActivity toolActivity = (ToolActivity) activity;
		    if (ScratchieConstants.LEADER_SELECTION_TOOL_SIGNATURE.equals(toolActivity.getTool().getToolSignature())) {
			leaderSelectionTool = toolActivity;
		    }
		}
	    }

	    // check if there is leaderSelectionTool in a sequence
	    if (leaderSelectionTool != null) {
		User learner = (User) getUserManagementService().findById(User.class, user.getUserId().intValue());
		String outputName = ScratchieConstants.LEADER_SELECTION_TOOL_OUTPUT_NAME_LEADER_USERID;
		ToolSession leaderSelectionSession = lamsCoreToolService.getToolSessionByLearner(learner,
			leaderSelectionTool);
		ToolOutput output = lamsCoreToolService.getOutputFromTool(outputName, leaderSelectionSession, null);

		// check if tool produced output
		if (output != null && output.getValue() != null) {
		    Long userId = output.getValue().getLong();
		    leader = getUserByIDAndSession(userId, toolSessionId);

		    // create new user in a DB
		    if (leader == null) {
			log.debug("creating new user with userId: " + userId);
			User leaderDto = (User) getUserManagementService().findById(User.class, userId.intValue());
			leader = new ScratchieUser(leaderDto.getUserDTO(), scratchieSession);
			this.createUser(leader);
		    }

		    // set group leader
		    scratchieSession.setGroupLeader(leader);
		    saveOrUpdateScratchieSession(scratchieSession);
		}
	    }
	}

	return leader;
    }

    @Override
    public void copyScratchesFromLeader(ScratchieUser user, ScratchieUser leader) {

	if ((user == null) || (leader == null) || user.getUid().equals(leader.getUid())) {
	    return;
	}

	List<ScratchieAnswerVisitLog> leaderLogs = scratchieAnswerVisitDao.getLogsByScratchieUser(leader.getUid());

	for (ScratchieAnswerVisitLog leaderLog : leaderLogs) {
	    ScratchieAnswer answer = leaderLog.getScratchieAnswer();
	    ScratchieAnswerVisitLog userLog = scratchieAnswerVisitDao.getLog(answer.getUid(),
		    user.getUserId());

	    // create and save new ScratchieAnswerVisitLog
	    if (userLog == null) {
		userLog = new ScratchieAnswerVisitLog();
		userLog.setScratchieAnswer(answer);
		userLog.setUser(user);
		userLog.setSessionId(user.getSession().getSessionId());
		userLog.setAccessDate(leaderLog.getAccessDate());
		scratchieAnswerVisitDao.saveObject(userLog);
	    }
	}
    }

    @Override
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

    @Override
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

    @Override
    public Scratchie getScratchieBySessionId(Long sessionId) {
	ScratchieSession session = scratchieSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getScratchie().getContentId();
	Scratchie res = scratchieDao.getByContentId(contentId);
	return res;
    }

    @Override
    public ScratchieSession getScratchieSessionBySessionId(Long sessionId) {
	return scratchieSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public void saveOrUpdateScratchieSession(ScratchieSession resSession) {
	scratchieSessionDao.saveObject(resSession);
    }

    @Override
    public void setAnswerAccess(Long answerUid, Long sessionId) {

	List<ScratchieUser> users = getUsersBySession(sessionId);
	for (ScratchieUser user : users) {
	    ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getLog(answerUid, user.getUserId());
	    if (log == null) {
		log = new ScratchieAnswerVisitLog();
		ScratchieAnswer answer = getScratchieAnswerById(answerUid);
		log.setScratchieAnswer(answer);
		log.setUser(user);
		log.setSessionId(sessionId);
		log.setAccessDate(new Timestamp(new Date().getTime()));
		scratchieAnswerVisitDao.saveObject(log);
	    }
	}

    }

    @Override
    public ScratchieAnswer getScratchieAnswerById(Long answerUid) {
	return (ScratchieAnswer) userManagementService.findById(ScratchieAnswer.class, answerUid);
    }

    @Override
    public void setScratchingFinished(Long toolSessionId) {
	List<ScratchieUser> users = getUsersBySession(toolSessionId);
	for (ScratchieUser user : users) {
	    user.setScratchingFinished(true);
	    scratchieUserDao.saveObject(user);
	}
    }

    @Override
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

    @Override
    public ScratchieItem getScratchieItemByUid(Long itemUid) {
	return scratchieItemDao.getByUid(itemUid);
    }

    @Override
    public Set<ScratchieUser> getAllLearners(Long contentId) {

	Set<ScratchieUser> users = new TreeSet<ScratchieUser>();

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    List<ScratchieUser> sessionUsers = scratchieUserDao.getBySessionID(sessionId);
	    users.addAll(sessionUsers);
	}

	return users;
    }

    @Override
    public List<ScratchieUser> getUsersBySession(Long toolSessionId) {
	return scratchieUserDao.getBySessionID(toolSessionId);
    }

    @Override
    public void saveUser(ScratchieUser user) {
	scratchieUserDao.saveObject(user);
    }

    @Override
    public List<GroupSummary> getMonitoringSummary(Long contentId) {
	List<GroupSummary> groupSummaryList = new ArrayList<GroupSummary>();

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(sessionId, session.getSessionName());

	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);
	    for (ScratchieUser user : users) {

		int totalAttempts = scratchieAnswerVisitDao.getLogCountTotal(sessionId, user.getUserId());
		user.setTotalAttempts(totalAttempts);

		// for displaying purposes if there is no attemps we assign -1 which will be shown as "-"
		int mark = (totalAttempts == 0) ? -1 : getUserMark(session, user.getUid());
		user.setMark(mark);
	    }

	    groupSummary.setUsers(users);
	    groupSummaryList.add(groupSummary);
	}

	return groupSummaryList;
    }

    @Override
    public void retrieveScratchesOrder(Collection<ScratchieItem> items, ScratchieUser user) {

	for (ScratchieItem item : items) {
	    List<ScratchieAnswerVisitLog> itemLogs = scratchieAnswerVisitDao.getLogsByScratchieUserAndItem(
		    user.getUid(), item.getUid());

	    for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {

		int attemptNumber;
		ScratchieAnswerVisitLog log = scratchieAnswerVisitDao.getLog(answer.getUid(),
			user.getUserId());
		if (log == null) {
		    // -1 if there is no log
		    attemptNumber = -1;
		} else {
		    // adding 1 to start from 1.
		    attemptNumber = itemLogs.indexOf(log) + 1;
		}

		answer.setAttemptOrder(attemptNumber);
	    }
	}

    }

    @Override
    public Set<ScratchieItem> getItemsWithIndicatedScratches(Long toolSessionId, ScratchieUser user) {
	
	List<ScratchieAnswerVisitLog> userLogs = scratchieAnswerVisitDao.getLogsByScratchieUser(user.getUid());

	Scratchie scratchie = this.getScratchieBySessionId(toolSessionId);
	Set<ScratchieItem> items = new TreeSet<ScratchieItem>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	
	for (ScratchieItem item : items) {

	    for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {
		
		//find according log if it exists
		ScratchieAnswerVisitLog log = null;
		for (ScratchieAnswerVisitLog userLog : userLogs) {
		    if (userLog.getScratchieAnswer().getUid().equals(answer.getUid())) {
			log = userLog;
			break;
		    }
		}
		if (log == null) {
		    answer.setScratched(false);
		} else {
		    answer.setScratched(true);
		}
	    }

	    boolean isItemUnraveled = isItemUnraveled(item, userLogs);
	    item.setUnraveled(isItemUnraveled);
	}
	
	return items;
    }
    
    /**
     * Check if the specified item was unraveled by user
     * 
     * @param item
     *            specified item
     * @param userLogs
     *            uses logs from it (The main reason to have this parameter is to reduce number of queries to DB)
     * @return
     */
    private boolean isItemUnraveled(ScratchieItem item, List<ScratchieAnswerVisitLog> userLogs) {
	boolean isItemUnraveled = false;

	for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {
	    
	    ScratchieAnswerVisitLog log = null;
	    for (ScratchieAnswerVisitLog userLog : userLogs) {
		if (userLog.getScratchieAnswer().getUid().equals(answer.getUid())) {
		    log = userLog;
		    break;
		}
	    }
	    
	    if (log != null) {
		isItemUnraveled |= answer.isCorrect();
	    }
	}

	return isItemUnraveled;
    }

    @Override
    public int getUserMark(ScratchieSession session, Long userUid) {
	
	//created to reduce number of queries to DB
	List<ScratchieAnswerVisitLog> userLogs = scratchieAnswerVisitDao.getLogsByScratchieUser(userUid);
	
	Scratchie scratchie = session.getScratchie();
	Set<ScratchieItem> items = scratchie.getScratchieItems();

	int mark = 0;
	for (ScratchieItem item : items) {
	    mark += getUserMarkPerItem(scratchie, item, userLogs);
	}

	return mark;
    }

    /**
     * 
     * 
     * @param scratchie
     * @param item
     * @param userLogs
     *            if this parameter is provided - uses logs from it, otherwise queries DB. (The main reason to have this
     *            parameter is to reduce number of queries to DB)
     * @return
     */
    private int getUserMarkPerItem(Scratchie scratchie, ScratchieItem item, List<ScratchieAnswerVisitLog> userLogs) {

	int mark = 0;
	// add mark only if an item was unraveled
	if (isItemUnraveled(item, userLogs)) {
	    
	    int itemAttempts = calculateItemAttempts(userLogs, item);
	    mark += item.getAnswers().size() - itemAttempts;

	    // add extra point if needed
	    if (scratchie.isExtraPoint() && (itemAttempts == 1)) {
		mark++;
	    }
	}

	return mark;
    }
    
    private int calculateItemAttempts(List<ScratchieAnswerVisitLog> userLogs, ScratchieItem item) {
	
	int itemAttempts = 0;
	for (ScratchieAnswerVisitLog userLog : userLogs) {
	    if (userLog.getScratchieAnswer().getScratchieItem().getUid().equals(item.getUid())) {
		itemAttempts++;
	    }
	}
	
	return itemAttempts;
    }

    @Override
    public Set<ScratchieItem> populateItemsResults(Long sessionId, Long userUid) {
	ScratchieSession session = scratchieSessionDao.getSessionBySessionId(sessionId);
	Scratchie scratchie = session.getScratchie();
	Set<ScratchieItem> items = scratchie.getScratchieItems();
	List<ScratchieAnswerVisitLog> userLogs = scratchieAnswerVisitDao.getLogsByScratchieUser(userUid);

	for (ScratchieItem item : items) {
	    int mark = getUserMarkPerItem(scratchie, item, userLogs);
	    item.setUserMark(mark);

	    int itemAttempts = calculateItemAttempts(userLogs, item);
	    item.setUserAttempts(itemAttempts);

	    String correctAnswer = "";
	    for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {
		if (answer.isCorrect()) {
		    correctAnswer = answer.getDescription();
		}
	    }
	    item.setCorrectAnswer(correctAnswer);
	}

	return items;
    }

    @Override
    public List<GroupSummary> getQuestionSummary(Long contentId, Long itemUid) {

	List<GroupSummary> groupSummaryList = new ArrayList<GroupSummary>();

	ScratchieItem item = scratchieItemDao.getByUid(itemUid);
	Collection<ScratchieAnswer> answers = item.getAnswers();

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(contentId);
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();

	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(sessionId, session.getSessionName());

	    Map<Long, ScratchieAnswer> answerMap = new HashMap<Long, ScratchieAnswer>();
	    for (ScratchieAnswer dbAnswer : (Set<ScratchieAnswer>) answers) {

		// clone it so it doesn't interfere with values from other sessions
		ScratchieAnswer answer = (ScratchieAnswer) dbAnswer.clone();
		answer.setUid(dbAnswer.getUid());
		int[] attempts = new int[answers.size()];
		answer.setAttempts(attempts);
		answerMap.put(dbAnswer.getUid(), answer);
	    }

	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);
	    // calculate attempts table
	    for (ScratchieUser user : users) {

		int attemptNumber = 0;
		List<ScratchieAnswerVisitLog> userAttempts = scratchieAnswerVisitDao.getLogsByScratchieUserAndItem(
			user.getUid(), itemUid);
		for (ScratchieAnswerVisitLog userAttempt : userAttempts) {
		    ScratchieAnswer answer = answerMap.get(userAttempt.getScratchieAnswer().getUid());
		    int[] attempts = answer.getAttempts();
		    // +1 for corresponding choice
		    attempts[attemptNumber++]++;
		}
	    }

	    Collection<ScratchieAnswer> sortedAnswers = new TreeSet<ScratchieAnswer>(new ScratchieAnswerComparator());
	    sortedAnswers.addAll(answerMap.values());
	    groupSummary.setAnswers(sortedAnswers);
	    groupSummaryList.add(groupSummary);
	}

	// show total groupSummary if there is more than 1 group available
	if (sessionList.size() > 1) {
	    Long sessionId = new Long(0);
	    GroupSummary groupSummaryTotal = new GroupSummary(sessionId, "Summary");

	    Map<Long, ScratchieAnswer> answerMapTotal = new HashMap<Long, ScratchieAnswer>();
	    for (ScratchieAnswer dbAnswer : (Set<ScratchieAnswer>) answers) {
		// clone it so it doesn't interfere with values from other sessions
		ScratchieAnswer answer = (ScratchieAnswer) dbAnswer.clone();
		int[] attempts = new int[answers.size()];
		answer.setAttempts(attempts);
		answerMapTotal.put(dbAnswer.getUid(), answer);
	    }

	    for (GroupSummary groupSummary : groupSummaryList) {
		Collection<ScratchieAnswer> sortedAnswers = groupSummary.getAnswers();
		for (ScratchieAnswer sortedAnswer : sortedAnswers) {
		    int[] attempts = sortedAnswer.getAttempts();

		    ScratchieAnswer answerTotal = answerMapTotal.get(sortedAnswer.getUid());
		    int[] attemptsTotal = answerTotal.getAttempts();
		    for (int i = 0; i < attempts.length; i++) {
			attemptsTotal[i] += attempts[i];
		    }
		}
	    }

	    Collection<ScratchieAnswer> sortedAnswers = new TreeSet<ScratchieAnswer>(new ScratchieAnswerComparator());
	    sortedAnswers.addAll(answerMapTotal.values());
	    groupSummaryTotal.setAnswers(sortedAnswers);
	    groupSummaryList.add(0, groupSummaryTotal);
	}

	return groupSummaryList;
    }

    @Override
    public List<ReflectDTO> getReflectionList(Set<ScratchieUser> users) {

	ArrayList<ReflectDTO> reflections = new ArrayList<ReflectDTO>();

	for (ScratchieUser user : users) {
	    ScratchieSession session = user.getSession();
	    NotebookEntry notebookEntry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
		    ScratchieConstants.TOOL_SIGNATURE, user.getUserId().intValue());
	    if ((notebookEntry != null) && StringUtils.isNotBlank(notebookEntry.getEntry())) {
		ReflectDTO reflectDTO = new ReflectDTO(notebookEntry.getUser());
		reflectDTO.setReflection(notebookEntry.getEntry());
		reflectDTO.setIsGroupLeader(this.isUserGroupLeader(user, session));

		reflections.add(reflectDTO);
	    }
	}

	return reflections;
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
	if (list == null || list.isEmpty()) {
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
    public ScratchieUser getUser(Long uid) {
	return (ScratchieUser) scratchieUserDao.getObject(ScratchieUser.class, uid);
    }

    @Override
    public LinkedHashMap<String, ExcelCell[][]> exportExcel(Long contentId) {
	Scratchie scratchie = scratchieDao.getByContentId(contentId);
	Collection<ScratchieItem> items = new TreeSet<ScratchieItem>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	int numberOfItems = items.size();

	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();
	
	// ======================================================= For Immediate Analysis page
	// =======================================

	List<ExcelCell[]> rowList = new LinkedList<ExcelCell[]>();

	ExcelCell[] row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.quick.analysis"), true);
	rowList.add(row);
	row = new ExcelCell[2];
	row[1] = new ExcelCell(getMessage("label.in.table.below.we.show"), false);
	rowList.add(row);
	rowList.add(EMPTY_ROW);

	row = new ExcelCell[3];
	row[2] = new ExcelCell(getMessage("label.questions"), false);
	rowList.add(row);

	row = new ExcelCell[numberOfItems + 4];
	int columnCount = 1;
	row[columnCount++] = new ExcelCell(getMessage("label.teams"), true);
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row[columnCount++] = new ExcelCell("Q" + (itemCount + 1), true);
	}
	row[columnCount++] = new ExcelCell(getMessage("label.total"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.total") + " %", true);
	rowList.add(row);

	int groupCount = 1;
	List<GroupSummary> summaryByTeam = getSummaryByTeam(scratchie, items);
	for (GroupSummary summary : summaryByTeam) {

	    row = new ExcelCell[numberOfItems + 4];
	    columnCount = 1;
	    row[columnCount++] = new ExcelCell("T" + groupCount++, true);

	    int numberOfFirstChoiceEvents = 0;
	    for (ScratchieItem item : summary.getItems()) {
		int attempts = item.getUserAttempts();

		String isFirstChoice;
		IndexedColors color;
		if (item.getCorrectAnswer().equals(Boolean.TRUE.toString())) {
		    isFirstChoice = getMessage("label.correct");
		    color = IndexedColors.GREEN;
		    numberOfFirstChoiceEvents++;
		} else if (attempts == 0) {
		    isFirstChoice = null;
		    color = null;
		} else {
		    isFirstChoice = getMessage("label.incorrect");
		    color = IndexedColors.RED;
		}
		row[columnCount++] = new ExcelCell(isFirstChoice, color);
	    }
	    row[columnCount++] = new ExcelCell(new Integer(numberOfFirstChoiceEvents), false);
	    int percentage = (numberOfItems == 0) ? 0 : 100*numberOfFirstChoiceEvents/numberOfItems;
	    row[columnCount++] = new ExcelCell(percentage + "%", false);
	    rowList.add(row);
	}

	ExcelCell[][] firstPageData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("label.for.immediate.analysis"), firstPageData);
	
	
	// ======================================================= For Report by Team TRA page
	// =======================================

	rowList = new LinkedList<ExcelCell[]>();

	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.quick.analysis"), true);
	rowList.add(row);
	row = new ExcelCell[2];
	row[1] = new ExcelCell(getMessage("label.table.below.shows.which.answer.teams.selected.first.try"), false);
	rowList.add(row);
	rowList.add(EMPTY_ROW);

	row = new ExcelCell[numberOfItems + 3];
	columnCount = 1;
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row[columnCount++] = new ExcelCell(getMessage("label.authoring.basic.instruction") + " " + (itemCount + 1), false);
	}
	row[columnCount++] = new ExcelCell(getMessage("label.total"), false);
	row[columnCount++] = new ExcelCell(getMessage("label.total") + " %", false);
	rowList.add(row);
	
	row = new ExcelCell[numberOfItems + 1];
	columnCount = 0;
	row[columnCount++] = new ExcelCell(getMessage("label.correct.answer"), false);
	for (ScratchieItem item : items) {
	    
	    // find out the correct answer's sequential letter - A,B,C...
	    String correctAnswerLetter = "";
	    int answerCount = 1;
	    for (ScratchieAnswer answer : (Set<ScratchieAnswer>)item.getAnswers()) {
		if (answer.isCorrect()) {
		    correctAnswerLetter = String.valueOf((char)(answerCount + 'A' - 1));
		    break;
		}
		answerCount++;
	    }
	    row[columnCount++] = new ExcelCell(correctAnswerLetter, false);
	}
	rowList.add(row);
	
	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("monitoring.label.group"), false);
	rowList.add(row);

	groupCount = 1;
	int[] percentages = new int[summaryByTeam.size()];
	for (GroupSummary summary : summaryByTeam) {

	    row = new ExcelCell[numberOfItems + 3];
	    columnCount = 0;
	    row[columnCount++] = new ExcelCell(groupCount, false);

	    int numberOfFirstChoiceEvents = 0;
	    for (ScratchieItem item : summary.getItems()) {

		IndexedColors color = null;
		if (item.getCorrectAnswer().equals(Boolean.TRUE.toString())) {
		    color = IndexedColors.GREEN;
		    numberOfFirstChoiceEvents++;
		}
		row[columnCount++] = new ExcelCell(item.getFirstChoiceAnswerLetter(), color);
	    }
	    row[columnCount++] = new ExcelCell(new Integer(numberOfFirstChoiceEvents), false);
	    int percentage = (numberOfItems == 0) ? 0 : 100*numberOfFirstChoiceEvents/numberOfItems;
	    row[columnCount++] = new ExcelCell(percentage + "%", false);
	    rowList.add(row);
	    percentages[groupCount-1] = percentage;
	    groupCount++;
	}
	
	Arrays.sort(percentages);
	
	//avg mean
	int sum = 0;
	for (int i = 0; i < percentages.length; i++) {
	    sum += percentages[i];
	}
	int avgMean = sum / percentages.length;
	row = new ExcelCell[numberOfItems + 3];
	row[0] = new ExcelCell(getMessage("label.avg.mean"), false);
	row[numberOfItems + 2] = new ExcelCell(avgMean + "%", false);
	rowList.add(row);

	// median
	int median;
	int middle = percentages.length / 2;
	if (percentages.length % 2 == 1) {
	    median = percentages[middle];
	} else {
	    median = (int) ((percentages[middle - 1] + percentages[middle]) / 2.0);
	}
	row = new ExcelCell[numberOfItems + 3];
	row[0] = new ExcelCell(getMessage("label.median"), false);
	row[numberOfItems + 2] = new ExcelCell(median, false);
	rowList.add(row);
	
	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.legend"), false);
	rowList.add(row);
	
	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.denotes.correct.answer"), IndexedColors.GREEN);
	rowList.add(row);

	ExcelCell[][] secondPageData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("label.report.by.team.tra"), secondPageData);
	

	// ======================================================= Research and Analysis page
	// =======================================

	// all rows
	rowList = new LinkedList<ExcelCell[]>();

	// Caption
	row = new ExcelCell[2];
	row[0] = new ExcelCell(getMessage("label.scratchie.report"), true);
	rowList.add(row);
	rowList.add(EMPTY_ROW);
	rowList.add(EMPTY_ROW);

	// Overall Summary by Team --------------------------------------------------
	row = new ExcelCell[2];
	row[0] = new ExcelCell(getMessage("label.overall.summary.by.team"), true);
	rowList.add(row);

	row = new ExcelCell[numberOfItems * 3 + 1];
	columnCount = 1;
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row[columnCount] = new ExcelCell(getMessage("label.for.question", new Object[] { itemCount + 1 }), false);
	    columnCount += 3;
	}
	rowList.add(row);

	row = new ExcelCell[numberOfItems * 3 + 1];
	columnCount = 1;
	for (int itemCount = 0; itemCount < numberOfItems; itemCount++) {
	    row[columnCount++] = new ExcelCell(getMessage("label.first.choice"), IndexedColors.BLUE);
	    row[columnCount++] = new ExcelCell(getMessage("label.attempts"), IndexedColors.BLUE);
	    row[columnCount++] = new ExcelCell(getMessage("label.mark"), IndexedColors.BLUE);
	}
	rowList.add(row);

	for (GroupSummary summary : summaryByTeam) {
	    row = new ExcelCell[numberOfItems * 3 + 1];
	    columnCount = 0;

	    row[columnCount++] = new ExcelCell(summary.getSessionName(), false);

	    for (ScratchieItem item : summary.getItems()) {
		int attempts = item.getUserAttempts();

		String isFirstChoice;
		IndexedColors color;
		if (item.getCorrectAnswer().equals(Boolean.TRUE.toString())) {
		    isFirstChoice = getMessage("label.correct");
		    color = IndexedColors.GREEN;
		} else if (attempts == 0) {
		    isFirstChoice = null;
		    color = null;
		} else {
		    isFirstChoice = getMessage("label.incorrect");
		    color = IndexedColors.RED;
		}
		row[columnCount++] = new ExcelCell(isFirstChoice, color);
		row[columnCount++] = new ExcelCell(new Long(attempts), color);
		Long mark = (item.getUserMark() == -1) ? null : new Long(item.getUserMark());
		row[columnCount++] = new ExcelCell(mark, false);
	    }
	    rowList.add(row);
	}
	rowList.add(EMPTY_ROW);
	rowList.add(EMPTY_ROW);
	rowList.add(EMPTY_ROW);

	// Overall Summary By Individual Student in each Team----------------------------------------
	row = new ExcelCell[2];
	row[0] = new ExcelCell(getMessage("label.overall.summary.by.individual.student"), true);
	rowList.add(row);
	rowList.add(EMPTY_ROW);

	row = new ExcelCell[4];
	row[1] = new ExcelCell(getMessage("label.attempts"), false);
	row[2] = new ExcelCell(getMessage("label.mark"), false);
	row[3] = new ExcelCell(getMessage("label.group"), false);
	rowList.add(row);

	List<GroupSummary> summaryList = getMonitoringSummary(contentId);
	for (GroupSummary summary : summaryList) {
	    for (ScratchieUser user : summary.getUsers()) {
		row = new ExcelCell[4];
		row[0] = new ExcelCell(user.getFirstName() + " " + user.getLastName(), false);
		row[1] = new ExcelCell(new Long(user.getTotalAttempts()), false);
		Long mark = (user.getMark() == -1) ? null : new Long(user.getMark());
		row[2] = new ExcelCell(mark, false);
		row[3] = new ExcelCell(summary.getSessionName(), false);
		rowList.add(row);
	    }
	}
	rowList.add(EMPTY_ROW);
	rowList.add(EMPTY_ROW);

	// Question Reports-----------------------------------------------------------------
	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.question.reports"), true);
	rowList.add(row);
	rowList.add(EMPTY_ROW);

	SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

	for (ScratchieItem item : items) {
	    List<GroupSummary> itemSummary = getQuestionSummary(contentId, item.getUid());

	    row = new ExcelCell[1];
	    row[0] = new ExcelCell(getMessage("label.question.semicolon", new Object[] { item.getTitle() }), true);
	    rowList.add(row);

	    row = new ExcelCell[1];
	    row[0] = new ExcelCell(removeHtmlMarkup(item.getDescription()), true);
	    rowList.add(row);
	    rowList.add(EMPTY_ROW);
	    rowList.add(EMPTY_ROW);

	    // show all team summary in case there is more than 1 group
	    if (summaryList.size() > 1) {
		row = new ExcelCell[1];
		row[0] = new ExcelCell(getMessage("label.all.teams.summary"), true);
		rowList.add(row);

		GroupSummary allTeamSummary = itemSummary.get(0);
		Collection<ScratchieAnswer> answers = allTeamSummary.getAnswers();

		row = new ExcelCell[1 + answers.size()];
		for (int i = 0; i < answers.size(); i++) {
		    row[i + 1] = new ExcelCell((long) i + 1, IndexedColors.YELLOW);
		}
		rowList.add(row);

		for (ScratchieAnswer answer : answers) {
		    row = new ExcelCell[1 + answers.size()];
		    String answerTitle = removeHtmlMarkup(answer.getDescription());
		    IndexedColors color = null;
		    if (answer.isCorrect()) {
			answerTitle += "(" + getMessage("label.monitoring.item.summary.correct") + ")";
			color = IndexedColors.GREEN;
		    }
		    columnCount = 0;
		    row[columnCount++] = new ExcelCell(answerTitle, color);

		    for (int numberAttempts : answer.getAttempts()) {
			row[columnCount++] = new ExcelCell(new Long(numberAttempts), false);
		    }
		    rowList.add(row);
		}
		rowList.add(EMPTY_ROW);
		rowList.add(EMPTY_ROW);
	    }

	    row = new ExcelCell[1];
	    row[0] = new ExcelCell(getMessage("label.breakdown.by.team"), true);
	    rowList.add(row);
	    for (GroupSummary groupSummary : itemSummary) {
		if (groupSummary.getSessionId().equals(0L)) {
		    continue;
		}

		Collection<ScratchieAnswer> answers = groupSummary.getAnswers();

		row = new ExcelCell[1];
		row[0] = new ExcelCell(groupSummary.getSessionName(), true);
		rowList.add(row);

		row = new ExcelCell[1 + answers.size()];
		for (int i = 0; i < answers.size(); i++) {
		    row[i + 1] = new ExcelCell(new Long(i + 1), false);
		}
		rowList.add(row);

		for (ScratchieAnswer answer : answers) {
		    row = new ExcelCell[1 + answers.size()];
		    String answerTitle = removeHtmlMarkup(answer.getDescription());
		    if (answer.isCorrect()) {
			answerTitle += "(" + getMessage("label.monitoring.item.summary.correct") + ")";
		    }
		    columnCount = 0;
		    row[columnCount++] = new ExcelCell(answerTitle, false);

		    for (int numberAttempts : answer.getAttempts()) {
			row[columnCount++] = new ExcelCell(new Long(numberAttempts), false);
		    }
		    rowList.add(row);
		}

	    }
	    rowList.add(EMPTY_ROW);
	    rowList.add(EMPTY_ROW);
	}

	// Breakdown By Student with Timing----------------------------------------------------

	row = new ExcelCell[1];
	row[0] = new ExcelCell(getMessage("label.breakdown.by.student.with.timing"), true);
	rowList.add(row);
	rowList.add(EMPTY_ROW);

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(scratchie.getContentId());
	for (ScratchieSession session : sessionList) {

	    ScratchieUser groupLeader = session.getGroupLeader();
	    Long sessionId = session.getSessionId();

	    if (groupLeader != null) {

		Long userId = groupLeader.getUserId();
		row = new ExcelCell[5];
		row[0] = new ExcelCell(groupLeader.getFirstName() + " " + groupLeader.getLastName(), true);
		row[1] = new ExcelCell(getMessage("label.attempts") + ":", false);
		Long attempts = (long) scratchieAnswerVisitDao.getLogCountTotal(sessionId, userId);
		row[2] = new ExcelCell(attempts, false);
		row[3] = new ExcelCell(getMessage("label.mark") + ":", false);
		row[4] = new ExcelCell(new Long(getUserMark(session, groupLeader.getUid())), false);
		rowList.add(row);

		row = new ExcelCell[1];
		row[0] = new ExcelCell(getMessage("label.team.leader") + session.getSessionName(), false);
		rowList.add(row);

		for (ScratchieItem item : items) {
		    row = new ExcelCell[1];
		    row[0] = new ExcelCell(getMessage("label.question.semicolon", new Object[] { item.getTitle() }),
			    false);
		    rowList.add(row);
		    rowList.add(EMPTY_ROW);

		    int i = 1;
		    List<ScratchieAnswerVisitLog> logs = scratchieAnswerVisitDao.getLogsByScratchieUserAndItem(
			    groupLeader.getUid(), item.getUid());
		    for (ScratchieAnswerVisitLog log : logs) {
			row = new ExcelCell[4];
			row[0] = new ExcelCell(new Long(i++), false);
			String answerDescr = removeHtmlMarkup(log.getScratchieAnswer().getDescription());
			row[1] = new ExcelCell(answerDescr, false);
			row[3] = new ExcelCell(fullDateFormat.format(log.getAccessDate()), false);
			rowList.add(row);
		    }
		    rowList.add(EMPTY_ROW);
		}

	    }
	}

	ExcelCell[][] thirdPageData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("label.research.analysis"), thirdPageData);


	// ======================================================= For_XLS_export(SPSS analysis) page
	// =======================================

	rowList = new LinkedList<ExcelCell[]>();

	// Table header------------------------------------

	int maxAnswers = 0;
	for (ScratchieItem item : items) {
	    if (item.getAnswers().size() > maxAnswers) {
		maxAnswers = item.getAnswers().size();
	    }
	}

	row = new ExcelCell[9 + maxAnswers * 2];
	columnCount = 0;
	row[columnCount++] = new ExcelCell(getMessage("label.student.name"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.team"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.question.number"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.question"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.correct.answer"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.first.choice.accuracy"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.number.of.attempts"), true);
	row[columnCount++] = new ExcelCell(getMessage("label.mark.awarded"), true);
	for (int i = 0; i < maxAnswers; i++) {
	    row[columnCount++] = new ExcelCell(getMessage("label." + (i + 1) + ".answer.selected"), true);
	}
	row[columnCount++] = new ExcelCell(getMessage("label.date"), true);
	for (int i = 0; i < maxAnswers; i++) {
	    row[columnCount++] = new ExcelCell(getMessage("label.time.of.selection." + (i + 1)), true);
	}
	rowList.add(row);

	// Table content------------------------------------

	for (GroupSummary summary : summaryByTeam) {
	    Long sessionId = summary.getSessionId();

	    ScratchieSession session = getScratchieSessionBySessionId(sessionId);
	    ScratchieUser groupLeader = session.getGroupLeader();
	    List<ScratchieUser> users = scratchieUserDao.getBySessionID(sessionId);

	    for (ScratchieUser user : users) {

		int questionCount = 1;
		for (ScratchieItem item : summary.getItems()) {

		    row = new ExcelCell[9 + maxAnswers * 2];
		    columnCount = 0;
		    // user name
		    row[columnCount++] = new ExcelCell(user.getFirstName() + " " + user.getLastName(), false);
		    // group name
		    row[columnCount++] = new ExcelCell(summary.getSessionName(), false);
		    // question number
		    row[columnCount++] = new ExcelCell(new Long(questionCount++), false);
		    // question title
		    row[columnCount++] = new ExcelCell(item.getTitle(), false);

		    // correct answer
		    String correctAnswer = "";
		    Set<ScratchieAnswer> answers = item.getAnswers();
		    for (ScratchieAnswer answer : answers) {
			if (answer.isCorrect()) {
			    correctAnswer = removeHtmlMarkup(answer.getDescription());
			}
		    }
		    row[columnCount++] = new ExcelCell(correctAnswer, false);

		    // isFirstChoice
		    int attempts = item.getUserAttempts();
		    String isFirstChoice;
		    if (item.getCorrectAnswer().equals(Boolean.TRUE.toString())) {
			isFirstChoice = getMessage("label.correct");
		    } else if (attempts == 0) {
			isFirstChoice = null;
		    } else {
			isFirstChoice = getMessage("label.incorrect");
		    }
		    row[columnCount++] = new ExcelCell(isFirstChoice, false);
		    // attempts
		    row[columnCount++] = new ExcelCell(new Long(attempts), false);
		    // mark
		    Object mark = (item.getUserMark() == -1) ? "" : new Long(item.getUserMark());
		    row[columnCount++] = new ExcelCell(mark, false);

		    // Answers selected
		    List<ScratchieAnswerVisitLog> logs = (groupLeader != null) ? scratchieAnswerVisitDao
			    .getLogsByScratchieUserAndItem(groupLeader.getUid(), item.getUid())
			    : new ArrayList<ScratchieAnswerVisitLog>();
		    for (ScratchieAnswerVisitLog log : logs) {
			String answer = removeHtmlMarkup(log.getScratchieAnswer().getDescription());
			row[columnCount++] = new ExcelCell(answer, false);
		    }
		    for (int i = logs.size(); i < item.getAnswers().size(); i++) {
			row[columnCount++] = new ExcelCell(getMessage("label.none"), false);
		    }
		    for (int i = answers.size(); i < maxAnswers; i++) {
			row[columnCount++] = new ExcelCell("", false);
		    }

		    // Date
		    String dateStr = "";
		    if (logs.size() > 0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
			Date accessDate = logs.iterator().next().getAccessDate();
			dateStr = dateFormat.format(accessDate);
		    }
		    row[columnCount++] = new ExcelCell(dateStr, false);

		    // time of selection
		    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		    for (ScratchieAnswerVisitLog log : logs) {
			Date accessDate = log.getAccessDate();
			String timeStr = timeFormat.format(accessDate);
			row[columnCount++] = new ExcelCell(timeStr, false);
		    }
		    for (int i = logs.size(); i < maxAnswers; i++) {
			row[columnCount++] = new ExcelCell("", false);
		    }

		    rowList.add(row);
		}

	    }

	}

	ExcelCell[][] fourthPageData = rowList.toArray(new ExcelCell[][] {});
	dataToExport.put(getMessage("label.spss.analysis"), fourthPageData);

	return dataToExport;
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************

    /**
     * Currently removes only <div> tags.
     */
    private String removeHtmlMarkup(String string) {
	return string.replaceAll("[<](/)?div[^>]*[>]", "");
    }

    /**
     * Serves merely for excel export purposes. Produces data for "Summary By Team" section.
     */
    private List<GroupSummary> getSummaryByTeam(Scratchie scratchie, Collection<ScratchieItem> sortedItems) {
	List<GroupSummary> groupSummaries = new ArrayList<GroupSummary>();

	List<ScratchieSession> sessionList = scratchieSessionDao.getByContentId(scratchie.getContentId());
	for (ScratchieSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // one new summary for one session.
	    GroupSummary groupSummary = new GroupSummary(sessionId, session.getSessionName());
	    ArrayList<ScratchieItem> items = new ArrayList<ScratchieItem>();

	    ScratchieUser groupLeader = session.getGroupLeader();

	    List<ScratchieAnswerVisitLog> groupLeaderLogs = (groupLeader != null) ? scratchieAnswerVisitDao
		    .getLogsByScratchieUser(groupLeader.getUid()) : null;

	    for (ScratchieItem item : sortedItems) {
		ScratchieItem newItem = new ScratchieItem();
		int numberOfAttempts = 0;
		int mark = -1;
		boolean isFirstChoice = false;
		String firstChoiceAnswerLetter = "";

		// if there is no group leader don't calculate numbers - there aren't any
		if (groupLeader != null) {
		    
		    numberOfAttempts = calculateItemAttempts(groupLeaderLogs, item);

		    // for displaying purposes if there is no attemps we assign -1 which will be shown as "-"
		    mark = (numberOfAttempts == 0) ? -1 : getUserMarkPerItem(scratchie, item, groupLeaderLogs);

		    isFirstChoice = (numberOfAttempts == 1) && isItemUnraveled(item, groupLeaderLogs);
		    
		    if (numberOfAttempts > 0) {
			ScratchieAnswer firstChoiceAnswer = scratchieAnswerVisitDao
				.getFirstScratchedAnswerByUserAndItem(groupLeader.getUid(), item.getUid());

			// find out the correct answer's sequential letter - A,B,C...
			int answerCount = 1;
			for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {
			    if (answer.getUid().equals(firstChoiceAnswer.getUid())) {
				firstChoiceAnswerLetter = String.valueOf((char) (answerCount + 'A' - 1));
				break;
			    }
			    answerCount++;
			}
		    }

		}

		newItem.setUid(item.getUid());
		newItem.setTitle(item.getTitle());
		newItem.setAnswers(item.getAnswers());
		newItem.setUserAttempts(numberOfAttempts);
		newItem.setUserMark(mark);
		newItem.setCorrectAnswer("" + isFirstChoice);
		newItem.setFirstChoiceAnswerLetter(firstChoiceAnswerLetter);

		items.add(newItem);
	    }

	    groupSummary.setItems(items);
	    groupSummaries.add(groupSummary);
	}

	return groupSummaries;
    }

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

    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
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
    
    public String getToolContentTitle(Long toolContentId) {
	return getScratchieByContentId(toolContentId).getTitle();
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

    @Override
    public String getMessage(String key) {
	return messageService.getMessage(key);
    }

    /**
     * Returns localized message
     */
    public String getMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }
    
    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    public void setLamsCoreToolService(ILamsCoreToolService lamsCoreToolService) {
	this.lamsCoreToolService = lamsCoreToolService;
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
