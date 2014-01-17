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
package org.eucm.lams.tool.eadventure.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.eucm.lams.tool.eadventure.EadventureConstants;
import org.eucm.lams.tool.eadventure.dao.EadventureConditionDAO;
import org.eucm.lams.tool.eadventure.dao.EadventureDAO;
import org.eucm.lams.tool.eadventure.dao.EadventureExpressionDAO;
import org.eucm.lams.tool.eadventure.dao.EadventureItemVisitDAO;
import org.eucm.lams.tool.eadventure.dao.EadventureParamDAO;
import org.eucm.lams.tool.eadventure.dao.EadventureSessionDAO;
import org.eucm.lams.tool.eadventure.dao.EadventureUserDAO;
import org.eucm.lams.tool.eadventure.dao.EadventureVarsDAO;
import org.eucm.lams.tool.eadventure.dto.ReflectDTO;
import org.eucm.lams.tool.eadventure.dto.Summary;
import org.eucm.lams.tool.eadventure.ims.IContentPackageConverter;
import org.eucm.lams.tool.eadventure.ims.IMSManifestException;
import org.eucm.lams.tool.eadventure.ims.ImscpApplicationException;
import org.eucm.lams.tool.eadventure.ims.SimpleContentPackageConverter;
import org.eucm.lams.tool.eadventure.model.Eadventure;
import org.eucm.lams.tool.eadventure.model.EadventureCondition;
import org.eucm.lams.tool.eadventure.model.EadventureExpression;
import org.eucm.lams.tool.eadventure.model.EadventureItemVisitLog;
import org.eucm.lams.tool.eadventure.model.EadventureParam;
import org.eucm.lams.tool.eadventure.model.EadventureSession;
import org.eucm.lams.tool.eadventure.model.EadventureUser;
import org.eucm.lams.tool.eadventure.model.EadventureVars;
import org.eucm.lams.tool.eadventure.util.EadventureToolContentHandler;
import org.eucm.lams.tool.eadventure.util.InputOutputReader;
import org.eucm.lams.tool.eadventure.util.ReflectDTOComparator;
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
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
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

/**
 * 
 * @author Dapeng.Ni
 * 
 */
public class EadventureServiceImpl implements IEadventureService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager

{
    static Logger log = Logger.getLogger(EadventureServiceImpl.class.getName());

    private EadventureDAO eadventureDao;

    private EadventureUserDAO eadventureUserDao;

    private EadventureSessionDAO eadventureSessionDao;
    
    private EadventureItemVisitDAO eadventureItemVisitDao;
    
    private EadventureVarsDAO eadventureVarsDao;
    
    private EadventureParamDAO eadventureParamDao;
    
    private EadventureConditionDAO eadventureConditionDao;
    
    private EadventureExpressionDAO eadventureExpressionDao;

    // tool service
    private EadventureToolContentHandler eadventureToolContentHandler;

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
    
    private IGradebookService gradebookService;

    private EadventureOutputFactory eadventureOutputFactory;
    

   

    public IVersionedNode getFileNode(Long itemUid, String relPathString) throws EadventureApplicationException {
	Eadventure ead = (Eadventure) eadventureDao.getObject(Eadventure.class, itemUid);
	if (ead == null) {
	    throw new EadventureApplicationException("Eadventure " + itemUid + " not found.");
	}

	return getFile(ead.getFileUuid(), ead.getFileVersionId(), relPathString);
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
    private IVersionedNode getFile(Long uuid, Long versionId, String relativePath) throws EadventureApplicationException {

	ITicket tic = getRepositoryLoginTicket();

	try {

	    return repositoryService.getFileItem(tic, uuid, versionId, relativePath);

	} catch (AccessDeniedException e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + ".";

	    error = error + "AccessDeniedException: " + e.getMessage() + " Unable to retry further.";
	    EadventureServiceImpl.log.error(error);
	    throw new EadventureApplicationException(error, e);

	} catch (Exception e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + "." + " Exception: " + e.getMessage();
	    EadventureServiceImpl.log.error(error);
	    throw new EadventureApplicationException(error, e);

	}
    }

    /**
     * This method verifies the credentials of the Eadventure Tool and gives it the <code>Ticket</code> to login
     * and access the Content Repository.
     * 
     * A valid ticket is needed in order to access the content from the repository. This method would be called evertime
     * the tool needs to upload/download files from the content repository.
     * 
     * @return ITicket The ticket for repostory access
     * @throws EadventureApplicationException
     */
    private ITicket getRepositoryLoginTicket() throws EadventureApplicationException {
	ICredentials credentials = new SimpleCredentials(eadventureToolContentHandler.getRepositoryUser(),
		eadventureToolContentHandler.getRepositoryId());
	try {
	    ITicket ticket = repositoryService.login(credentials, eadventureToolContentHandler
		    .getRepositoryWorkspaceName());
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new EadventureApplicationException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new EadventureApplicationException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new EadventureApplicationException("Login failed." + e.getMessage());
	}
    }

    public Eadventure getEadventureByContentId(Long contentId) {
	Eadventure rs = eadventureDao.getByContentId(contentId);
	if (rs == null) {
	    EadventureServiceImpl.log.debug("Could not find the content by given ID:" + contentId);
	}
	return rs;
    }

    public Eadventure getDefaultContent(Long contentId) throws EadventureApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    EadventureServiceImpl.log.error(error);
	    throw new EadventureApplicationException(error);
	}

	Eadventure defaultContent = getDefaultEadventure();
	// save default content by given ID.
	Eadventure content = new Eadventure();
	content = Eadventure.newInstance(defaultContent, contentId);
	return content;
    }

    //TODO revisar
  /*  public List getAuthoredItems(Long eadventureUid) {
	return eadventureItemDao.getAuthoringItems(eadventureUid);
    }*/

    public void createUser(EadventureUser eadventureUser) {
	eadventureUserDao.saveObject(eadventureUser);
    }

    public EadventureUser getUserByIDAndContent(Long userId, Long contentId) {

	return eadventureUserDao.getUserByUserIDAndContentID(userId, contentId);

    }

    public EadventureUser getUserByIDAndSession(Long userId, Long sessionId) {

	return eadventureUserDao.getUserByUserIDAndSessionID(userId, sessionId);

    }

    public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws EadventureApplicationException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, fileUuid, fileVersionId);
	} catch (Exception e) {
	    throw new EadventureApplicationException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    public void saveOrUpdateEadventure(Eadventure eadventure) {
	eadventureDao.saveObject(eadventure);
    }
   
    
    public void saveOrUpdateEadventureExpressions(Set<EadventureExpression> eadExpressions, Long condUID) {
		Iterator<EadventureExpression> it = eadExpressions.iterator();
		while (it.hasNext()){
		    EadventureExpression expression = it.next();
		//    expression.setCondition_uid(condUID);
		    eadventureExpressionDao.saveObject(expression);
		}
    }
    
    public void saveOrUpdateEadventureExpression(EadventureExpression eadExpression) {
	eadventureExpressionDao.saveObject(eadExpression);
    }
    
    public void saveOrUpdateEadventureCondition(EadventureCondition eadCondition) {
	eadventureConditionDao.saveObject(eadCondition);
    }
    
    public void saveOrUpdateEadventureConditions(Set<EadventureCondition> eadConditions) {
	Iterator<EadventureCondition> it = eadConditions.iterator();
	while (it.hasNext()){
	    EadventureCondition cond = it.next();
	 //   Set<EadventureExpression> expList = cond.getEadListExpression();
	  //  cond.setEadListExpression(null);
	    eadventureConditionDao.saveObject(cond);
	   // saveOrUpdateEadventureExpressions(expList, cond.getUid());
	   // cond.setEadListExpression(expList);
	}
}
    
    public void deleteEadventureCondition(Long conditionUid) {
	eadventureConditionDao.removeObject(EadventureCondition.class, conditionUid);

    }
    
    public void deleteEadventureExpression(Long expressionUid) {
	eadventureExpressionDao.removeObject(EadventureExpression.class, expressionUid);

    }
    
    //TODO revisar!!!!!! Monitoring
    public List<Summary> exportBySessionId(Long sessionId, Long userId) {
	EadventureSession session = eadventureSessionDao.getSessionBySessionId(sessionId);
	if (session == null) {
	    EadventureServiceImpl.log.error("Failed get EadventureSession by ID [" + sessionId + "]");
	    return null;
	}
	// initial eadventure items list
	List<Summary> itemList = new ArrayList();
	Eadventure ead = session.getEadventure();
	Summary sum = new Summary(session.getSessionId(), session.getSessionName(), ead, false);

	
	
	//List<EadventureUser> userList = getUserListBySessionItem(session.getSessionId(), ead.getUid());
	boolean[] existList = new boolean[1];
	String[] reportList = new String[1];
	
	EadventureUser eadUser = eadventureUserDao.getUserByUserIDAndSessionID(userId,sessionId);
	//TODO doble acceso a vistit log... (aqui y en getUserListBySessionItem)
	EadventureItemVisitLog log = getEadventureItemLog(ead.getUid(), userId);
	eadUser.setAccessDate(log.getAccessDate());
	EadventureVars var = getEadventureVars(log.getUid(), EadventureConstants.VAR_NAME_REPORT);


	if (var!=null){
	    existList[0]=true;
	    reportList[0]=var.getValue();
	}else{
	    existList[0]=false;
	    reportList[0]=null;
	}

	
	ArrayList<EadventureUser> userList = new ArrayList<EadventureUser>();
	userList.add(eadUser);
	sum.setUsers(userList);
	sum.setExistList(existList);
	sum.setReportList(reportList);
	
	
	//TODO ver si tiene sentido que sea una lista
	ArrayList<Summary> list = new ArrayList<Summary>();
	list.add(sum);
	
	return list;
    }
  //TODO revisar!!!!!! Monitoring
    public List<Summary> exportByContentId(Long contentId) {
	Eadventure eadventure = eadventureDao.getByContentId(contentId);
	List<Summary> groupList = new ArrayList();

	// session by session
	List<EadventureSession> sessionList = eadventureSessionDao.getByContentId(contentId);
	for (EadventureSession session : sessionList) {
	    
	    Summary sum = new Summary(session.getSessionId(), session.getSessionName(), session.getEadventure(), false);
	   
	    List<EadventureUser> userList = getUserListBySessionItem(session.getSessionId(), eadventure.getUid());
		boolean[] existList = new boolean[userList.size()];
		String[] reportList = new String[userList.size()];
		int numberOfFinishedLearners = 0;
		int i=0;
		for (EadventureUser eadUser : userList){
		//TODO doble acceso a vistit log... (aqui y en getUserListBySessionItem)
		EadventureItemVisitLog log = getEadventureItemLog(eadventure.getUid(), eadUser.getUserId());
		
		EadventureVars var = getEadventureVars(log.getUid(), EadventureConstants.VAR_NAME_REPORT);
		
		if (log.isComplete())
		    numberOfFinishedLearners++;

		if (var!=null){
		    existList[i]=true;
		    reportList[i]=var.getValue();
		}else{
		    existList[i]=false;
		    reportList[i]=null;
		}
		i++;
		
		}
		sum.setUsers(userList);
		sum.setExistList(existList);
		sum.setReportList(reportList);
		
	    
	    groupList.add(sum);
	}

	return groupList;
    }

    public Eadventure getEadventureBySessionId(Long sessionId) {
	EadventureSession session = eadventureSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getEadventure().getContentId();
	Eadventure res = eadventureDao.getByContentId(contentId);
	return res;
    }

    public EadventureSession getEadventureSessionBySessionId(Long sessionId) {
	return eadventureSessionDao.getSessionBySessionId(sessionId);
    }

    public void saveOrUpdateEadventureSession(EadventureSession resSession) {
	eadventureSessionDao.saveObject(resSession);
    }

   /* public void retrieveComplete(SortedSet<EadventureItem> eadventureItemList, EadventureUser user) {
	for (EadventureItem item : eadventureItemList) {
	    EadventureItemVisitLog log = eadventureItemVisitDao.getEadventureItemLog(item.getUid(), user.getUserId());
	    if (log == null) {
		item.setComplete(false);
	    } else {
		item.setComplete(log.isComplete());
	    }
	}
    }*/

    
    public void setItemComplete(Long eadventureItemUid, Long userId, Long sessionId) {
	EadventureItemVisitLog log = eadventureItemVisitDao.getEadventureItemLog(eadventureItemUid, userId);
	if (log == null) {
	    log = new EadventureItemVisitLog();
	    Eadventure ead = eadventureDao.getByUid(eadventureItemUid);
	    log.setEadventure(ead);
	    EadventureUser user = eadventureUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	    log.setUser(user);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	}
	log.setComplete(true);
	eadventureItemVisitDao.saveObject(log);
    }

    public void setItemAccess(Long eadventureItemUid, Long userId, Long sessionId) {
	EadventureItemVisitLog log = eadventureItemVisitDao.getEadventureItemLog(eadventureItemUid, userId);
	EadventureServiceImpl.log.error("Set item acces!!!!!");
	
	if (log == null) {
	    log = new EadventureItemVisitLog();
	    Eadventure item = eadventureDao.getByUid(eadventureItemUid);
	    log.setEadventure(item);
	    EadventureServiceImpl.log.error("El id de usuario es " + userId);
	    EadventureServiceImpl.log.error("USER ID "+userId);
	    EadventureServiceImpl.log.error("SESSION ID "+sessionId);
	    EadventureUser user = eadventureUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	    if (user==null)
		EadventureServiceImpl.log.error("NOS DA NULL!!!!!!!");
	    log.setUser(user);
	    log.setComplete(false);
	    log.setSessionId(sessionId);
	    log.setAccessDate(new Timestamp(new Date().getTime()));
	    eadventureItemVisitDao.saveObject(log);
	} else 
	    EadventureServiceImpl.log.error("NO ES NULL!!!");
    }

    public String finishToolSession(Long toolSessionId, Long userId) throws EadventureApplicationException {
	EadventureUser user = eadventureUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	eadventureUserDao.saveObject(user);

	// EadventureSession session = eadventureSessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(EadventureConstants.COMPLETED);
	// eadventureSessionDao.saveObject(session);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new EadventureApplicationException(e);
	} catch (ToolException e) {
	    throw new EadventureApplicationException(e);
	}
	return nextUrl;
    }
    
    //TODO revisar!!!! monitoring
    public List<Summary> getSummary(Long contentId) {
	List<Summary> groupList = new ArrayList<Summary>();

	// get all item which is accessed by user
	Map<Long, Integer> visitCountMap = eadventureItemVisitDao.getSummary(contentId);

	Eadventure eadventure = eadventureDao.getByContentId(contentId);

	List<EadventureSession> sessionList = eadventureSessionDao.getByContentId(contentId);
	for (EadventureSession session : sessionList) {
	    // one new group for one session.
	 // so far no any ead available, so just put session name info to Summary
	    Summary sum = new Summary(session.getSessionId(), session.getSessionName(), session.getEadventure());
	    // set viewNumber according visit log
		if (visitCountMap.containsKey(eadventure.getUid())) {
		    sum.setViewNumber(visitCountMap.get(eadventure.getUid()).intValue());
		}
		List<EadventureUser> userList = getUserListBySessionItem(session.getSessionId(), eadventure.getUid());
		boolean[] existList = new boolean[userList.size()];
		int numberOfFinishedLearners = 0;
		int i=0;
		for (EadventureUser eadUser : userList){
		//TODO doble acceso a vistit log... (aqui y en getUserListBySessionItem)
		EadventureItemVisitLog log = getEadventureItemLog(eadventure.getUid(), eadUser.getUserId());
		
		EadventureVars var = getEadventureVars(log.getUid(), EadventureConstants.VAR_NAME_REPORT);
		
		if (log.isComplete())
		    numberOfFinishedLearners++;

		if (var!=null)
		    existList[i]=true;
		else
		    existList[i]=false;
		
		i++;
		
		}
		sum.setUsers(userList);
		sum.setExistList(existList);
		sum.setNumberOfLearners(userList.size());
		sum.setNumberOfFinishedLearners(numberOfFinishedLearners);
		
		groupList.add(sum);
	}
	
	return groupList;

    }

    public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry) {
	Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();

	List<EadventureSession> sessionList = eadventureSessionDao.getByContentId(contentId);
	for (EadventureSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    boolean hasRefection = session.getEadventure().isReflectOnActivity();
	    Set<ReflectDTO> list = new TreeSet<ReflectDTO>(new ReflectDTOComparator());
	    // get all users in this session
	    List<EadventureUser> users = eadventureUserDao.getBySessionID(sessionId);
	    for (EadventureUser user : users) {
		ReflectDTO ref = new ReflectDTO(user);

		if (setEntry) {
		    NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			    EadventureConstants.TOOL_SIGNATURE, user.getUserId().intValue());
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

    public List<EadventureUser> getUserListBySessionItem(Long sessionId, Long itemUid) {
	List<EadventureItemVisitLog> logList = eadventureItemVisitDao.getEadventureItemLogBySession(sessionId, itemUid);
	List<EadventureUser> userList = new ArrayList(logList.size());
	for (EadventureItemVisitLog visit : logList) {
	    EadventureUser user = visit.getUser();
	    user.setAccessDate(visit.getAccessDate());
	    userList.add(user);
	}
	//List<EadventureUser> userList = null;
	return userList;
    }

    //TODO revisar Monitoring!!!
    public void setItemVisible(Long itemUid, boolean visible) {
	/*EadventureItem item = eadventureItemDao.getByUid(itemUid);
	if (item != null) {
	    // createBy should be null for system default value.
	    Long userId = 0L;
	    String loginName = "No user";
	    if (item.getCreateBy() != null) {
		userId = item.getCreateBy().getUserId();
		loginName = item.getCreateBy().getLoginName();
	    }
	    if (visible) {
		auditService.logShowEntry(EadventureConstants.TOOL_SIGNATURE, userId, loginName, item.toString());
	    } else {
		auditService.logHideEntry(EadventureConstants.TOOL_SIGNATURE, userId, loginName, item.toString());
	    }
	    item.setHide(!visible);
	    eadventureItemDao.saveObject(item);
	}*/
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

    public EadventureUser getUser(Long uid) {
	return (EadventureUser) eadventureUserDao.getObject(EadventureUser.class, uid);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Eadventure getDefaultEadventure() throws EadventureApplicationException {
	Long defaultEadventureId = getToolDefaultContentIdBySignature(EadventureConstants.TOOL_SIGNATURE);
	Eadventure defaultEadventure = getEadventureByContentId(defaultEadventureId);
	if (defaultEadventure == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    EadventureServiceImpl.log.error(error);
	    throw new EadventureApplicationException(error);
	}

	return defaultEadventure;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws EadventureApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    EadventureServiceImpl.log.error(error);
	    throw new EadventureApplicationException(error);
	}
	return contentId;
    }

    private NodeKey processPackage(String packageDirectory, String initFile) throws UploadEadventureFileException {
	NodeKey node = null;
	try {
	    node = eadventureToolContentHandler.uploadPackage(packageDirectory, initFile);
	} catch (InvalidParameterException e) {
	    throw new UploadEadventureFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	} catch (RepositoryCheckedException e) {
	    throw new UploadEadventureFileException(messageService.getMessage("error.msg.repository"));
	}
	return node;
    }
    
    public void saveOrUpdateEadventureParams(Set<EadventureParam> eadParams){
	Iterator<EadventureParam> it = eadParams.iterator();
	while (it.hasNext()){
	    EadventureParam param = it.next();
	    eadventureParamDao.saveObject(param);
	}
    }
    
    public void removeParam(EadventureParam eadParams){
    	eadventureParamDao.delete(eadParams);	
    }

    
    public void uploadEadventureFile(Eadventure ead, FormFile file) throws UploadEadventureFileException {
	try {
	    InputStream is = file.getInputStream();
	    String fileName = file.getFileName();
	    String fileType = file.getContentType();
	
	    // need unzip upload, and parse learning object information from XML file.
	   
		String packageDirectory = ZipFileUtil.expandZip(is, fileName);
		log.error("Direcci�n del zip: "+packageDirectory);
		IContentPackageConverter cpConverter = new SimpleContentPackageConverter(packageDirectory);
		String initFile = cpConverter.getDefaultItem();
		ead.setImsSchema(cpConverter.getSchema());
		ead.setOrganizationXml(cpConverter.getOrganzationXML());
		ead.setInitialItem(initFile);
		// upload package
		NodeKey nodeKey = processPackage(packageDirectory, initFile);
		ead.setFileUuid(nodeKey.getUuid());
		ead.setFileVersionId(nodeKey.getVersion());
	    
	    // create the package from the directory contents
		ead.setFileType(fileType);
		ead.setFileName(fileName);
		
	    // parse parameters.xml file, and create the eadParams
		//TODO reportar bien el error cuando no se produce xk no es un e-Ad file
		HashMap<String,String> params = InputOutputReader.getOutputParameterList(packageDirectory+"//"+EadventureConstants.PARAMETERS_FILE_NAME);
		// chek if its a real e-adventure package
		
		if (! new File(packageDirectory+"//"+EadventureConstants.PARAMETERS_FILE_NAME).exists()){
			EadventureServiceImpl.log.error(messageService.getMessage("error.msg.ims.package") + " : "
				    + "No eAdventure game!! ");
			    throw new UploadEadventureFileException(messageService.getMessage("error.msg.ims.package"));
		}
		Iterator<String> it = params.keySet().iterator();
		Set<EadventureParam> eadParam = new HashSet<EadventureParam>();
		while (it.hasNext()){
		    EadventureParam param = new EadventureParam();
		    String key = it.next();
		    param.setInput(false);
		    param.setName(key);
		    param.setType(params.get(key));
		   // eadventureParamDao.saveObject(param);
		    eadParam.add(param);
		    log.error(key+" ha sido subido con exito!!!!!!");
		    
		}
		//add default params (this are not included in the params file at eAd LAMS export due to they always have to appear)
		eadParam.addAll(getDefaultParams());
		ead.setParams(eadParam);
		
	} catch (ZipFileUtilException e) {
	    EadventureServiceImpl.log.error(messageService.getMessage("error.msg.zip.file.exception") + " : "
		    + e.toString());
	    throw new UploadEadventureFileException(messageService.getMessage("error.msg.zip.file.exception"));
	} catch (FileNotFoundException e) {
	    EadventureServiceImpl.log.error(messageService.getMessage("error.msg.file.not.found") + ":" + e.toString());
	    throw new UploadEadventureFileException(messageService.getMessage("error.msg.file.not.found"));
	} catch (IOException e) {
	    EadventureServiceImpl.log.error(messageService.getMessage("error.msg.io.exception") + ":" + e.toString());
	    throw new UploadEadventureFileException(messageService.getMessage("error.msg.io.exception"));
	} catch (IMSManifestException e) {
	    EadventureServiceImpl.log.error(messageService.getMessage("error.msg.ims.package") + ":" + e.toString());
	    throw new UploadEadventureFileException(messageService.getMessage("error.msg.ims.package"));
	} catch (ImscpApplicationException e) {
	    EadventureServiceImpl.log.error(messageService.getMessage("error.msg.ims.application") + ":" + e.toString());
	    throw new UploadEadventureFileException(messageService.getMessage("error.msg.ims.application"));
	}
    }
    
    private List<EadventureParam> getDefaultParams(){
	List<EadventureParam> defaultParams = new ArrayList<EadventureParam>();
	defaultParams.add(new EadventureParam("score","integer",false));
	defaultParams.add(new EadventureParam("game-completed","boolean",false));
	defaultParams.add(new EadventureParam("total-time","integer",false));
	return defaultParams;
    }

    /**
     * Find out default.htm/html or index.htm/html in the given directory folder
     * 
     * @param packageDirectory
     * @return
     */
    
    //TODO creo que vga a sobrar
    private String findWebsiteInitialItem(String packageDirectory) {
	File file = new File(packageDirectory);
	if (!file.isDirectory()) {
	    return null;
	}

	File[] initFiles = file.listFiles(new FileFilter() {
	    public boolean accept(File pathname) {
		if (pathname == null || pathname.getName() == null) {
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
	if (initFiles != null && initFiles.length > 0) {
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

    public void setEadventureDao(EadventureDAO eadventureDao) {
	this.eadventureDao = eadventureDao;
    }
    
    public List<EadventureParam> getEadventureParamByContentId(Long contentId){
	return eadventureParamDao.getEadventureParamByEadContentId(contentId);
    }

    public void setEadventureSessionDao(EadventureSessionDAO eadventureSessionDao) {
	this.eadventureSessionDao = eadventureSessionDao;
    }

    public void setEadventureToolContentHandler(EadventureToolContentHandler eadventureToolContentHandler) {
	this.eadventureToolContentHandler = eadventureToolContentHandler;
    }

    public void setEadventureUserDao(EadventureUserDAO eadventureUserDao) {
	this.eadventureUserDao = eadventureUserDao;
    }

    public void setEadventureItemVisitDao(EadventureItemVisitDAO eadventureItemVisitDao) {
        this.eadventureItemVisitDao = eadventureItemVisitDao;
    }

    public EadventureItemVisitDAO getEadventureItemVisitDao() {
        return eadventureItemVisitDao;
    }

    public void setEadventureVarsDao(EadventureVarsDAO eadventureVarsDao) {
        this.eadventureVarsDao = eadventureVarsDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }


    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Eadventure toolContentObj = eadventureDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultEadventure();
	    } catch (EadventureApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the eadventure tool");
	}

	// set EadventureToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Eadventure.newInstance(toolContentObj, toolContentId);
	try {
	    //TODO revisar!!
	    exportContentService.registerFileClassForExport(Eadventure.class.getName(), "fileUuid", "fileVersionId");
	    exportContentService.exportToolContent(toolContentId, toolContentObj, eadventureToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(EadventureImportContentVersionFilter.class);
	
	    //TODO revisar
	    exportContentService.registerFileClassForImport(Eadventure.class.getName(), "fileUuid", "fileVersionId",
		    "fileName", "fileType", null, "initialItem");

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, eadventureToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Eadventure)) {
		throw new ImportToolContentException(
			"Import eadventure tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Eadventure toolContentObj = (Eadventure) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    EadventureUser user = eadventureUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new EadventureUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setEadventure(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);
	    //TODO revisar!! que no falte nada al objeto de EAD
	    // reset all eadventureItem createBy user
	    /*Set<EadventureItem> items = toolContentObj.getEadventureItems();
	    for (EadventureItem item : items) {
		item.setCreateBy(user);
	    }*/
	    //TODO ver si esto es correcto:
	    Set par = toolContentObj.getParams();
	    List listPar = new ArrayList(par);
	    for (Object o:listPar){
	    	((EadventureParam)o).setEadventure_uid(null);
	    	this.eadventureParamDao.saveObject(o);
	    }
	    Set con = toolContentObj.getConditions();
	    List listCon = new ArrayList(con);
	    for (Object o:listCon){
	    	((EadventureCondition)o).setEadventure_uid(null);
	    	this.eadventureConditionDao.saveObject(o);
	    }
	    
	    
	    eadventureDao.saveObject(toolContentObj);
	    
	    
	    
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
     * @throws EadventureApplicationException
     */
    
    // @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Eadventure content = getEadventureByContentId(toolContentId);
	if (content == null) {
	    try {
		content = getDefaultContent(toolContentId);

	    } catch (EadventureApplicationException e) {
		throw new ToolException(e);
	    }
	}

	SortedMap<String, ToolOutputDefinition> prueba = getEadventureOutputFactory().getToolOutputDefinitions(content,
		definitionType);

	return prueba;
    }

    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the EadventureFiles tool seession");
	}

	Eadventure eadventure = null;
	if (fromContentId != null) {
	    eadventure = eadventureDao.getByContentId(fromContentId);
	}
	if (eadventure == null) {
	    try {
		eadventure = getDefaultEadventure();
	    } catch (EadventureApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Eadventure toContent = Eadventure.newInstance(eadventure, toContentId);
	eadventureDao.saveObject(toContent);

	//TODO no hace nada... pero comprobar que no de problema
	// save eadventure items as well
	/*Set items = toContent.getEadventureItems();
	if (items != null) {
	    Iterator iter = items.iterator();
	    while (iter.hasNext()) {
		EadventureItem item = (EadventureItem) iter.next();
		// createRootTopic(toContent.getUid(),null,msg);
	    }
	}*/
    }

    public String getToolContentTitle(Long toolContentId) {
	return getEadventureByContentId(toolContentId).getTitle();
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	Eadventure eadventure = eadventureDao.getByContentId(toolContentId);
	if (removeSessionData) {
	    List list = eadventureSessionDao.getByContentId(toolContentId);
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		EadventureSession session = (EadventureSession) iter.next();
		eadventureSessionDao.delete(session);
	    }
	}
	eadventureDao.delete(eadventure);
    }
    
    public void removeParams(Long toolContentId){
	List<EadventureParam> params = getEadventureParamByContentId(toolContentId);
	if (params!=null){
	for (EadventureParam param:params){
	    eadventureParamDao.delete(param);
	}
	}
    }

    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	EadventureSession session = new EadventureSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Eadventure eadventure = eadventureDao.getByContentId(toolContentId);
	session.setEadventure(eadventure);
	eadventureSessionDao.saveObject(session);
    }

    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    EadventureServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    EadventureServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	EadventureSession session = eadventureSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(EadventureConstants.COMPLETED);
	    eadventureSessionDao.saveObject(session);
	} else {
	    EadventureServiceImpl.log.error("Fail to leave tool Session.Could not find shared eadventure "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared eadventure session by given session id: " + toolSessionId);
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
	eadventureSessionDao.deleteBySessionId(toolSessionId);
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getEadventureOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getEadventureOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Eadventure toolContentObj = new Eadventure();

	try {
	    toolContentObj.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	    toolContentObj.setContentId(toolContentId);
	    toolContentObj.setContentInUse(Boolean.FALSE);
	    toolContentObj.setCreated(now);
	    toolContentObj.setDefineLater(Boolean.FALSE);
	    toolContentObj.setInstructions(WebUtil.convertNewlines((String) importValues
		    .get(ToolContentImport102Manager.CONTENT_BODY)));
	    toolContentObj.setUpdated(now);
	    toolContentObj.setReflectOnActivity(Boolean.FALSE);
	    toolContentObj.setReflectInstructions(null);

	    Boolean bool = WDDXProcessor.convertToBoolean(importValues,
		    ToolContentImport102Manager.CONTENT_URL_RUNTIME_LEARNER_SUBMIT_FILE);
	    bool = WDDXProcessor.convertToBoolean(importValues,
		    ToolContentImport102Manager.CONTENT_URL_RUNTIME_LEARNER_SUBMIT_URL);
	    bool = WDDXProcessor.convertToBoolean(importValues,
		    ToolContentImport102Manager.CONTENT_URL_RUNTIME_LEARNER_SUBMIT_URL);
	    toolContentObj.setLockWhenFinished(Boolean.FALSE);

	    /*
	     * unused entries from 1.0.2 [directoryName=] no equivalent in 2.0 [runtimeSubmissionStaffFile=true] no
	     * equivalent in 2.0 [contentShowUser=false] no equivalent in 2.0 [isHTML=false] no equivalent in 2.0
	     * [showbuttons=false] no equivalent in 2.0 [isReusable=false] not used in 1.0.2 (would be lock when
	     * finished)
	     */
	    EadventureUser ruser = new EadventureUser();
	    ruser.setUserId(new Long(user.getUserID().longValue()));
	    ruser.setFirstName(user.getFirstName());
	    ruser.setLastName(user.getLastName());
	    ruser.setLoginName(user.getLogin());
	    createUser(ruser);
	    toolContentObj.setCreatedBy(ruser);

	    // Eadventure Items. They are ordered on the screen by create date so they need to be saved in the right
	    // order.
	    // So read them all in first, then go through and assign the dates in the correct order and then save.
	  //TODO nos cargamos la parte de los items.... comprobar que se pase todo bien y no de ning�n problema
	    /* Vector urls = (Vector) importValues.get(ToolContentImport102Manager.CONTENT_URL_URLS);
	    SortedMap<Integer, EadventureItem> items = new TreeMap<Integer, EadventureItem>();
	    if (urls != null) {
		Iterator iter = urls.iterator();
		while (iter.hasNext()) {
		    Hashtable urlMap = (Hashtable) iter.next();
		    Integer itemOrder = WDDXProcessor.convertToInteger(urlMap,
			    ToolContentImport102Manager.CONTENT_URL_URL_VIEW_ORDER);
		    EadventureItem item = new EadventureItem();
		    item.setTitle((String) urlMap.get(ToolContentImport102Manager.CONTENT_TITLE));
		    item.setCreateBy(ruser);
		    item.setCreateByAuthor(true);
		    item.setHide(false);

		    Vector instructions = (Vector) urlMap
			    .get(ToolContentImport102Manager.CONTENT_URL_URL_INSTRUCTION_ARRAY);
		    if (instructions != null && instructions.size() > 0) {
			item.setItemInstructions(new HashSet());
			Iterator insIter = instructions.iterator();
			while (insIter.hasNext()) {
			    item.getItemInstructions().add(createInstruction((Hashtable) insIter.next()));
			}
		    }

		    String eadventureType = (String) urlMap.get(ToolContentImport102Manager.CONTENT_URL_URL_TYPE);
		    if (ToolContentImport102Manager.URL_RESOURCE_TYPE_URL.equals(eadventureType)) {
			item.setType(EadventureConstants.RESOURCE_TYPE_URL);
			item.setUrl((String) urlMap.get(ToolContentImport102Manager.CONTENT_URL_URL_URL));
			item.setOpenUrlNewWindow(false);
		    } else if (ToolContentImport102Manager.URL_RESOURCE_TYPE_WEBSITE.equals(eadventureType)) {
			item.setType(EadventureConstants.RESOURCE_TYPE_WEBSITE);
		    } else if (ToolContentImport102Manager.URL_RESOURCE_TYPE_FILE.equals(eadventureType)) {
			item.setType(EadventureConstants.RESOURCE_TYPE_FILE);
		    } else {
			throw new ToolException("Invalid eadventure type. Type was " + eadventureType);
		    }

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

		EadventureItem item = (EadventureItem) iter.next();
		item.setCreateDate(itemDate);
		toolContentObj.getEadventureItems().add(item);
	    }*/

	} catch (WDDXProcessorConversionException e) {
	    EadventureServiceImpl.log.error("Unable to content for activity " + toolContentObj.getTitle()
		    + "properly due to a WDDXProcessorConversionException.", e);
	    throw new ToolException(
		    "Invalid import data format for activity "
			    + toolContentObj.getTitle()
			    + "- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
	}

	eadventureDao.saveObject(toolContentObj);

    }

    // TODO comprobar que no hay problema por quitar esto
    /*private EadventureItemInstruction createInstruction(Hashtable instructionEntry)
	    throws WDDXProcessorConversionException {

	Integer instructionOrder = WDDXProcessor.convertToInteger(instructionEntry,
		ToolContentImport102Manager.CONTENT_URL_URL_VIEW_ORDER);

	// the description column in 1.0.2 was longer than 255 chars, so truncate.
	String instructionText = (String) instructionEntry.get(ToolContentImport102Manager.CONTENT_URL_INSTRUCTION);
	if (instructionText != null && instructionText.length() > 255) {
	    if (EadventureServiceImpl.log.isDebugEnabled()) {
		EadventureServiceImpl.log
			.debug("1.0.2 Import truncating Item Instruction to 255 characters. Original text was\'"
				+ instructionText + "\'");
	    }
	    instructionText = instructionText.substring(0, 255);
	}

	EadventureItemInstruction instruction = new EadventureItemInstruction();
	instruction.setDescription(instructionText);
	instruction.setSequenceId(instructionOrder);

	return instruction;
    }*/

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	Eadventure toolContentObj = getEadventureByContentId(toolContentId);
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
     *                tool session ID
     * @return list of teachers that monitor the lesson which contains the tool with given session ID
     */
    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return getLessonService().getMonitorsByToolSessionId(sessionId);
    }

    //TODO Resultado de la actualizaci�n a la brach de release 2.3.5, ver si afecta!!!
    
    /*public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getEadventureOutputFactory().getSupportedDefinitionClasses(definitionType);
    }*/

    public EadventureOutputFactory getEadventureOutputFactory() {
	return eadventureOutputFactory;
    }

    public void setEadventureOutputFactory(EadventureOutputFactory eadventureOutputFactory) {
	this.eadventureOutputFactory = eadventureOutputFactory;
    }
    
    public void setGradebookService(IGradebookService gradebookService) {
    	this.gradebookService = gradebookService;
      }
    
    public IGradebookService getGradebookService() {
    	return gradebookService;
      }
    
    
    /** 
	 * {@inheritDoc}
	 */
	public boolean checkCondition(String conditionName, Long toolSessionId, Long userUid) {
		EadventureUser user = eadventureUserDao.getUserByUserIDAndSessionID(userUid, toolSessionId);
		Eadventure eadventure = eadventureSessionDao.getSessionBySessionId(toolSessionId).getEadventure();
		Set<EadventureCondition> conditions = eadventure.getConditions();
		EadventureCondition condition = null;
		for (EadventureCondition cond:conditions) {
			if (cond.getName().equals(conditionName)) {
				condition = cond;
				break;
			}	
		}

		boolean result = true;
		if (condition != null) {
		    	EadventureItemVisitLog visitLog = eadventureItemVisitDao.getEadventureItemLog(eadventure.getUid(), userUid);
		    	Set eadV = visitLog.getEadventureVars();
		    if (!eadV.isEmpty()){
		    	List eadventureVars = new ArrayList<EadventureVars>( eadV);
		    	//TODO comprobar si no lo tengo que meter con comparator para que salga en orden
			Iterator<EadventureExpression> it = condition.getEadListExpression().iterator();
			String previousOp = null;
			while(it.hasNext()) {
				Boolean andExpression= null;
				boolean iniNewAnd=false;
				EadventureExpression expr = it.next();
				String nextOp = expr.getNextOp();
				if (visitLog != null) {
				    boolean partialResult = checkExpression(expr, eadventureVars);
				    // first expr
				    if (expr.getSequenceId()==0||iniNewAnd)
					    result = partialResult;
				    else if (previousOp==null||previousOp.equals("and"))
				    	result &= partialResult;
				    else if (previousOp!=null&&previousOp.equals("or")||!it.hasNext()){
				    	if (andExpression==null){
				    		andExpression = new Boolean(result);
				    		iniNewAnd = true;
				    	}else {
				    		result = partialResult||andExpression;
				    		iniNewAnd=false;
				    		andExpression=null;
				    	}
				    }
					
				    previousOp = nextOp;
				    
				} else {
					
					result = false;
					break;
				}
			}
		    }else {
		    	//There aren't vars set for this user and session
		    	result=false;
		    }
		} else {
			//there is no such a condition
			result = false;
		}
		
		return result;
	}
	
	private boolean checkExpression(EadventureExpression expr, List eadventureVars){
	    EadventureVars firstVar = getVarByName(expr.getFirstOp().getName(),eadventureVars);
//	    firstVar.setType(expr.getFirstOp().getType());
	    EadventureVars secVar = null;
	    String operator = expr.getExpresionOp();
	    String value = expr.getValueIntroduced();
	    if (expr.getVarIntroduced()!=null){
		 secVar = getVarByName(expr.getVarIntroduced().getName(),eadventureVars);
		 secVar.setType(expr.getVarIntroduced().getType());
	    }
	    // when tries to check a var that has not been send by the game
	    if (firstVar!=null){
	    if (secVar==null)
	    	return evalExpr(new String(firstVar.getType()), new String(firstVar.getValue()), value, operator); 
	    else
	    	return evalExpr(firstVar.getType(), firstVar.getValue(), secVar.getValue(), operator);
	    } else
	    	return false;
	}
	
	private boolean evalExpr(String type, String firstValue, String secondValue, String op){
	    if (type.equals("string")){
		if (op.equals("=="))
		    return firstValue.equals(secondValue);
		else
		    return !firstValue.equals(secondValue);
	    } else if (type.equals("boolean")){
		return firstValue.equals(secondValue);
	    }else if (type.equals("integer")){
		if (op.equals("=="))
		    return Integer.parseInt(firstValue) == Integer.parseInt(secondValue);
		else if (op.equals("!="))
		    return Integer.parseInt(firstValue) != Integer.parseInt(secondValue);
		else if (op.equals(">"))
			return  Integer.parseInt(firstValue) > Integer.parseInt(secondValue);
		else if (op.equals("<"))
		    return Integer.parseInt(firstValue) < Integer.parseInt(secondValue);
		else if (op.equals(">="))
		    return Integer.parseInt(firstValue) >= Integer.parseInt(secondValue);
		else if (op.equals("<="))
		    return Integer.parseInt(firstValue) <= Integer.parseInt(secondValue);
		 
		    
	    }
	
		return false;
	}
	
	private EadventureVars getVarByName(String name, List<EadventureVars> eadventureVars){
	    for (EadventureVars var: eadventureVars) {
		if (var.getName().equals(name)) {
			return var;
			
		}
	}
	    return null;
	    
	}
    

    
    //TODO IMPORRRRRRRRRRRRRRRTANTE!!!!!!!!!!!!!!! el par�metro toolContentID que estamos pasando es realmente toolSessionID!!!!
    //@Override
    public boolean setAppletInput(String name, String value, String userId, String toolContentID) {
	//TODO restart when  
	EadventureServiceImpl.log.error("EEEEEEEEEEEEEEEEEE " + name);
	EadventureServiceImpl.log.error("EEEEEEEEEEEEEEEEEE " + value);
	//EadventureServiceImpl.log.error("USER ID "+ userId);
	EadventureServiceImpl.log.error("TOOL SESION ID "+ toolContentID);
	EadventureServiceImpl.log.error("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOEEEEEEEEEEEEEEEE");
	
	
	//TODO Ahora recuperamos la session para sacar el EAD!!! pero a�adir el toolContentId para que sea mas sencillo!!!
	//TODO user va a sobrar!! con el userID que ya se nos pasa por par�metro vamos sobraos!!					
	//EadventureUser user = eadventureUserDao.getUserByUserIDAndContentID(Long.parseLong(userId), Long.parseLong(toolContentID));
	//EadventureUser user = eadventureUserDao.getUserByUserIDAndSessionID(Long.parseLong(userId), Long.parseLong(toolContentID));
	    //eadventureUserDao.getUserByUserIDAndSessionID(Long.parseLong(userId), Long.parseLong(toolSessionID));
	EadventureServiceImpl.log.error("USER ID "+ userId);
	EadventureSession eadSession = eadventureSessionDao.getSessionBySessionId( Long.parseLong(toolContentID));
	//Eadventure ead = eadventureDao.getByContentId(Long.parseLong(toolContentID));
	EadventureItemVisitLog log = eadventureItemVisitDao.getEadventureItemLog(eadSession.getEadventure().getUid(), Long.parseLong(userId));
	EadventureVars var = eadventureVarsDao.getEadventureVars(log.getUid(), name);
	if (var==null){
	    var = new EadventureVars();
	    var.setName(name);
	    var.setVisitLog(log);
	    //Get the type from the params list
	    var.setType(eadventureParamDao.getEadventureParamTypeByNameAndEadContentID(name, eadSession.getEadventure().getUid()));
	}
	var.setValue(value);
	this.eadventureVarsDao.saveObject(var);
	boolean changeButton = eadSession.getEadventure().isDefineComplete()&!log.isComplete();
	if (name.equals(EadventureConstants.VAR_NAME_COMPLETED)&&value.equals("true")&&changeButton)
	    setItemComplete(eadSession.getEadventure().getUid(), Long.parseLong(userId), eadSession.getSessionId());
	
       return changeButton;
    }

    //@Override
    public void setReportInput(String name, String value, String userId, String toolSessionID) {
	// TODO Auto-generated method stub
	
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }
    
    //@Override
    public EadventureItemVisitLog getEadventureItemLog(Long itemUid, Long userId) {
	
	return eadventureItemVisitDao.getEadventureItemLog(itemUid, userId);
    }

   // @Override
    public EadventureVars getEadventureVars(Long itemVisitLogID, String name) {
	return eadventureVarsDao.getEadventureVars(itemVisitLogID, name);
    }

    public void setEadventureParamDao(EadventureParamDAO eadventureParamDao) {
        this.eadventureParamDao = eadventureParamDao;
    }

    public void setEadventureConditionDao(EadventureConditionDAO eadventureConditionDao) {
        this.eadventureConditionDao = eadventureConditionDao;
    }

    public void setEadventureExpressionDao(EadventureExpressionDAO eadventureExpressionDao) {
        this.eadventureExpressionDao = eadventureExpressionDao;
    }
}