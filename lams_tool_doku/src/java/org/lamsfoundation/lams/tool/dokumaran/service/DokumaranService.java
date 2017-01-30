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

package org.lamsfoundation.lams.tool.dokumaran.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.dao.DokumaranConfigItemDAO;
import org.lamsfoundation.lams.tool.dokumaran.dao.DokumaranDAO;
import org.lamsfoundation.lams.tool.dokumaran.dao.DokumaranSessionDAO;
import org.lamsfoundation.lams.tool.dokumaran.dao.DokumaranUserDAO;
import org.lamsfoundation.lams.tool.dokumaran.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.dokumaran.dto.SessionDTO;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranConfigItem;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranSession;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;
import org.lamsfoundation.lams.tool.dokumaran.util.DokumaranToolContentHandler;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;

import net.gjerull.etherpad.client.EPLiteClient;
import net.gjerull.etherpad.client.EPLiteException;

/**
 * @author Dapeng.Ni
 */
public class DokumaranService implements IDokumaranService, ToolContentManager, ToolSessionManager, ToolRestManager {
    private static Logger log = Logger.getLogger(DokumaranService.class.getName());

    private DokumaranDAO dokumaranDao;

    private DokumaranUserDAO dokumaranUserDao;

    private DokumaranSessionDAO dokumaranSessionDao;

    private DokumaranConfigItemDAO dokumaranConfigItemDAO;

    // tool service
    private DokumaranToolContentHandler dokumaranToolContentHandler;

    private MessageService messageService;

    // system services

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private IAuditService auditService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private DokumaranOutputFactory dokumaranOutputFactory;

    private EPLiteClient client = null;

    // *******************************************************************************
    // Service method
    // *******************************************************************************

    @Override
    public Dokumaran getDokumaranByContentId(Long contentId) {
	Dokumaran rs = dokumaranDao.getByContentId(contentId);
	return rs;
    }

    @Override
    public Dokumaran getDefaultContent(Long contentId) throws DokumaranApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    DokumaranService.log.error(error);
	    throw new DokumaranApplicationException(error);
	}

	Dokumaran defaultContent = getDefaultDokumaran();
	// save default content by given ID.
	Dokumaran content = new Dokumaran();
	content = Dokumaran.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public List<DokumaranUser> checkLeaderSelectToolForSessionLeader(DokumaranUser user, Long toolSessionId, boolean isFirstTimeAccess) {
	if ((user == null) || (toolSessionId == null)) {
	    return null;
	}

	DokumaranSession session = getDokumaranSessionBySessionId(toolSessionId);
	Dokumaran dokumaran = session.getDokumaran();
	List<DokumaranUser> leaders = new ArrayList<DokumaranUser>();
	if (dokumaran.isAllowMultipleLeaders() && !isGroupedActivity(dokumaran.getContentId())) {
	    
	    List<DokumaranUser> createdLeaders = dokumaranUserDao.getLeadersBySessionId(toolSessionId);
	    leaders.addAll(createdLeaders);
	    
	    // check leader select tool for a leader only in case Dokumaran activity is accessed by this user for the first
	    // time. We need to add this check in order to reduce amount of queries to Leader Selection tool.
	    if (isFirstTimeAccess) {

		//get all leaders from Leader Selection tool
		Set<Long> allLeaderUserIds = toolService.getAllLeaderUserIds(toolSessionId, user.getUserId().intValue());
		for (Long leaderUserId : allLeaderUserIds) {
		    //in case current user is leader - store his leader status
		    if (leaderUserId.equals(user.getUserId())) {
			user.setLeader(true);
			saveUser(user);
			leaders.add(user);
			continue;
		    }
		    
		    //check if such leader is already created inside doKumaran
		    boolean isLeaderCreated = false;
		    for (DokumaranUser leader : createdLeaders) {
			if (leader.getUserId().equals(leaderUserId)) {
			    isLeaderCreated = true;
			    break;
			}
		    }
		    
		    //if the leader is not yet created - create him
		    if (!isLeaderCreated && (getUserByIDAndSession(leaderUserId, toolSessionId) == null)) {
			log.debug("creating new user with userId: " + leaderUserId);
			User leaderDto = (User) userManagementService.findById(User.class, leaderUserId.intValue());
			DokumaranUser leader = new DokumaranUser(leaderDto.getUserDTO(), session);
			leader.setLeader(true);
			saveUser(leader);
			leaders.add(leader);
		    }
		}
		
	    }
	} else {
	    DokumaranUser leader = session.getGroupLeader();
	    // check leader select tool for a leader only in case Dokumaran tool doesn't know it. As otherwise it will
	    // screw
	    // up previous scratches done
	    if (leader == null) {

		Long leaderUserId = toolService.getLeaderUserId(toolSessionId, user.getUserId().intValue());
		if (leaderUserId != null) {
		    leader = getUserByIDAndSession(leaderUserId, toolSessionId);

		    // create new user in a DB
		    if (leader == null) {
			log.debug("creating new user with userId: " + leaderUserId);
			User leaderDto = (User) userManagementService.findById(User.class, leaderUserId.intValue());
			leader = new DokumaranUser(leaderDto.getUserDTO(), session);
			saveUser(leader);
		    }

		    // set group leader
		    session.setGroupLeader(leader);
		    dokumaranSessionDao.saveObject(session);
		}
	    }
	    
	    leaders.add(leader);
	}

	return leaders;
    }

    @Override
    public boolean isUserLeader(List<DokumaranUser> leaders, Long userId) {
	for (DokumaranUser leader : leaders) {
	    if (userId.equals(leader.getUserId())) {
		return true;
	    }
	}
	return false;
    }
    
    @Override
    public boolean isLeaderResponseFinalized(List<DokumaranUser> leaders) {
	for (DokumaranUser leader : leaders) {
	    if (leader.isSessionFinished()) {
		return true;
	    }
	}
	return false;
    }
    
    @Override
    public boolean isLeaderResponseFinalized(Long toolSessionId) {
	DokumaranSession session = getDokumaranSessionBySessionId(toolSessionId);
	Dokumaran dokumaran = session.getDokumaran();
	
	boolean isLeaderResponseFinalized = false;
	if (dokumaran.isAllowMultipleLeaders() && !isGroupedActivity(dokumaran.getContentId())) {
	    
	    List<DokumaranUser> leaders = dokumaranUserDao.getLeadersBySessionId(toolSessionId);
	    for (DokumaranUser leader : leaders) {
		if (leader.isSessionFinished()) {
		    isLeaderResponseFinalized = true;
		    break;
		}
	    }
	    
	} else {
	    DokumaranUser leader = session.getGroupLeader();
	    isLeaderResponseFinalized = (leader != null) && leader.isSessionFinished();
	}

	return isLeaderResponseFinalized;
    }

    @Override
    public void saveUser(DokumaranUser dokumaranUser) {
	dokumaranUserDao.saveObject(dokumaranUser);
    }

    @Override
    public DokumaranUser getUserByIDAndContent(Long userId, Long contentId) {
	return dokumaranUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public DokumaranUser getUserByIDAndSession(Long userId, Long sessionId) {
	return dokumaranUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public List<DokumaranUser> getUsersBySession(Long toolSessionId) {
	return dokumaranUserDao.getBySessionID(toolSessionId);
    }

    @Override
    public DokumaranConfigItem getConfigItem(String key) {
	return dokumaranConfigItemDAO.getConfigItemByKey(key);
    }

    @Override
    public void saveOrUpdateDokumaranConfigItem(DokumaranConfigItem item) {
	dokumaranConfigItemDAO.saveOrUpdate(item);
    }

    @Override
    public void saveOrUpdateDokumaran(Dokumaran dokumaran) {
	dokumaranDao.saveObject(dokumaran);
    }

    @Override
    public Dokumaran getDokumaranBySessionId(Long sessionId) {
	DokumaranSession session = dokumaranSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getDokumaran().getContentId();
	Dokumaran res = dokumaranDao.getByContentId(contentId);
	return res;
    }

    @Override
    public DokumaranSession getDokumaranSessionBySessionId(Long sessionId) {
	return dokumaranSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public void saveOrUpdateDokumaranSession(DokumaranSession resSession) {
	dokumaranSessionDao.saveObject(resSession);
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws DokumaranApplicationException {
	DokumaranUser user = dokumaranUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	dokumaranUserDao.saveObject(user);

	// DokumaranSession session = dokumaranSessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(DokumaranConstants.COMPLETED);
	// dokumaranSessionDao.saveObject(session);
	
	//finish Etherpad session. Encapsulate it in try-catch block as we don't want it to affect regular LAMS workflow.
	try {
	    EPLiteClient client = initializeEPLiteClient();

	    DokumaranSession session = dokumaranSessionDao.getSessionBySessionId(toolSessionId);
	    String groupId = session.getEtherpadGroupId();

	    String userName = user.getFirstName() + " " + user.getLastName();
	    Map<String, String> map = client.createAuthorIfNotExistsFor(user.getUserId().toString(), userName);
	    String authorId = map.get("authorID");

	    // search for already existing user's session at Etherpad server
	    Map sessionsMap = client.listSessionsOfAuthor(authorId);
	    for (String sessionId : (Set<String>) sessionsMap.keySet()) {
		Map<String, String> sessessionAttributes = (Map<String, String>) sessionsMap.get(sessionId);
		String groupIdIter = sessessionAttributes.get("groupID");
		if (groupIdIter.equals(groupId)) {
		    client.deleteSession(sessionId);
		    break;
		}
	    }
	} catch (Exception e1) {
	    log.debug(e1.getMessage());
	}

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new DokumaranApplicationException(e);
	} catch (ToolException e) {
	    throw new DokumaranApplicationException(e);
	}
	return nextUrl;
    }

    @Override
    public List<SessionDTO> getSummary(Long contentId) {
	List<SessionDTO> groupList = new ArrayList<SessionDTO>();

	// get all sessions in a dokumaran and retrieve all dokumaran items under this session
	// plus initial dokumaran items by author creating (resItemList)
	List<DokumaranSession> sessionList = dokumaranSessionDao.getByContentId(contentId);

	for (DokumaranSession session : sessionList) {
	    // one new group for one session.
	    SessionDTO group = new SessionDTO();
	    group.setSessionId(session.getSessionId());
	    group.setSessionName(session.getSessionName());

	    String padId = session.getPadId();
	    group.setPadId(padId);
	    
	    //mark all session that has had problems with pad initializations so that they could be fixed in monitoring by a teacher
	    if (StringUtils.isEmpty(session.getEtherpadReadOnlyId()) || StringUtils.isEmpty(session.getEtherpadGroupId())) {
		group.setSessionFaulty(true);
	    }

	    groupList.add(group);
	}

	return groupList;
    }

    @Override
    public List<ReflectDTO> getReflectList(Long contentId) {
	List<ReflectDTO> reflections = new LinkedList<ReflectDTO>();

	List<DokumaranSession> sessionList = dokumaranSessionDao.getByContentId(contentId);
	for (DokumaranSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // get all users in this session
	    List<DokumaranUser> users = dokumaranUserDao.getBySessionID(sessionId);
	    for (DokumaranUser user : users) {

		NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			DokumaranConstants.TOOL_SIGNATURE, user.getUserId().intValue());
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
    public DokumaranUser getUser(Long uid) {
	return (DokumaranUser) dokumaranUserDao.getObject(DokumaranUser.class, uid);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Dokumaran getDefaultDokumaran() throws DokumaranApplicationException {
	Long defaultDokumaranId = getToolDefaultContentIdBySignature(DokumaranConstants.TOOL_SIGNATURE);
	Dokumaran defaultDokumaran = getDokumaranByContentId(defaultDokumaranId);
	if (defaultDokumaran == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    DokumaranService.log.error(error);
	    throw new DokumaranApplicationException(error);
	}

	return defaultDokumaran;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws DokumaranApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    DokumaranService.log.error(error);
	    throw new DokumaranApplicationException(error);
	}
	return contentId;
    }

    /**
     * Gets a message from dokumaran bundle. Same as <code><fmt:message></code> in JSP pages.
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
	Dokumaran toolContentObj = dokumaranDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultDokumaran();
	    } catch (DokumaranApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the dokumaran tool");
	}

	// set DokumaranToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Dokumaran.newInstance(toolContentObj, toolContentId);
	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, dokumaranToolContentHandler,
		    rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, dokumaranToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Dokumaran)) {
		throw new ImportToolContentException(
			"Import Share dokumaran tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Dokumaran toolContentObj = (Dokumaran) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    DokumaranUser user = dokumaranUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new DokumaranUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setDokumaran(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    dokumaranDao.saveObject(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Dokumaran content = getDokumaranByContentId(toolContentId);
	if (content == null) {
	    try {
		content = getDefaultContent(toolContentId);
	    } catch (DokumaranApplicationException e) {
		throw new ToolException(e);
	    }
	}
	return getDokumaranOutputFactory().getToolOutputDefinitions(content, definitionType);
    }

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedDokumaranFiles tool seession");
	}

	Dokumaran dokumaran = null;
	if (fromContentId != null) {
	    dokumaran = dokumaranDao.getByContentId(fromContentId);
	}
	if (dokumaran == null) {
	    try {
		dokumaran = getDefaultDokumaran();
	    } catch (DokumaranApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Dokumaran toContent = Dokumaran.newInstance(dokumaran, toContentId);
	dokumaranDao.saveObject(toContent);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getDokumaranByContentId(toolContentId).getTitle();
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Dokumaran dokumaran = dokumaranDao.getByContentId(toolContentId);
	if (dokumaran == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	dokumaran.setDefineLater(false);
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getDokumaranByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	List<DokumaranSession> sessions = dokumaranSessionDao.getByContentId(toolContentId);
	for (DokumaranSession session : sessions) {
	    if (!dokumaranUserDao.getBySessionID(session.getSessionId()).isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Dokumaran dokumaran = dokumaranDao.getByContentId(toolContentId);
	if (dokumaran == null) {
	    DokumaranService.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (DokumaranSession session : dokumaranSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, DokumaranConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}
	dokumaranDao.delete(dokumaran);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (DokumaranService.log.isDebugEnabled()) {
	    DokumaranService.log
		    .debug("Removing Dokumaran content for user ID " + userId + " and toolContentId " + toolContentId);
	}

	Dokumaran dokumaran = dokumaranDao.getByContentId(toolContentId);
	if (dokumaran == null) {
	    DokumaranService.log
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	List<DokumaranSession> sessions = dokumaranSessionDao.getByContentId(toolContentId);
	for (DokumaranSession session : sessions) {
	    DokumaranUser user = dokumaranUserDao.getUserByUserIDAndSessionID(userId.longValue(),
		    session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			DokumaranConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    dokumaranDao.removeObject(NotebookEntry.class, entry.getUid());
		}

		dokumaranUserDao.removeObject(DokumaranUser.class, user.getUid());
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	DokumaranSession session = new DokumaranSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Dokumaran dokumaran = dokumaranDao.getByContentId(toolContentId);
	session.setDokumaran(dokumaran);
	dokumaranSessionDao.saveObject(session);

	//create pad in a try-catch block so it doesn't affect session creation operation
	try {
	    createPad(dokumaran, session);
	} catch (Exception e) {
	    log.warn(e.getMessage(), e);
	}

    }
    
    @Override
    public void createPad(Dokumaran dokumaran, DokumaranSession session) throws DokumaranConfigurationException {
	Long toolSessionId = session.getSessionId();
	Long toolContentId = dokumaran.getContentId();
	String groupIdentifier = DokumaranConstants.PREFIX_REGULAR_GROUP + toolSessionId;

	// in case sharedPadId is present - all sessions will share the same padId
	if (dokumaran.isSharedPadEnabled()) {

	    // find existing pad in any of the sessions associated with this toolContentId
	    List<DokumaranSession> sessions = dokumaranSessionDao.getByContentId(toolContentId);
	    DokumaranSession sessionWithAlreadyCreatedPad = null;
	    for (DokumaranSession sessionIter : sessions) {
		if (StringUtils.isNotBlank(session.getEtherpadGroupId())) {
		    sessionWithAlreadyCreatedPad = sessionIter;
		    break;
		}
	    }

	    if (sessionWithAlreadyCreatedPad == null) {
		ToolSession toolSession = toolService.getToolSession(toolSessionId);
		Long lessonId = toolSession.getLesson().getLessonId();
		groupIdentifier = DokumaranConstants.PREFIX_SHARED_GROUP + dokumaran.getSharedPadId() + lessonId;

	    } else {
		session.setEtherpadGroupId(sessionWithAlreadyCreatedPad.getEtherpadGroupId());
		session.setEtherpadReadOnlyId(sessionWithAlreadyCreatedPad.getEtherpadReadOnlyId());
		dokumaranSessionDao.saveObject(session);
		return;
	    }
	}

	EPLiteClient client = initializeEPLiteClient();

	// create Etherpad Group assossiated with this session
	Map map = client.createGroupIfNotExistsFor(groupIdentifier);
	String groupId = (String) map.get("groupID");
	session.setEtherpadGroupId(groupId);
	String padId = session.getPadId();

	boolean isPadAlreadyCreated = false;
	try {
	    client.createGroupPad(groupId, DokumaranConstants.DEFAULT_PAD_NAME);
	} catch (EPLiteException e) {
	    // allow recreating existing pads
	    if ("padName does already exist".equals(e.getMessage())) {
		isPadAlreadyCreated = true;
		// throw exception in all other cases
	    } else {
		throw e;
	    }
	}

	// set initial content
	if (!dokumaran.isSharedPadEnabled() || !isPadAlreadyCreated) {
	    String etherpadHtml = "<html><body>"
		    + dokumaran.getInstructions().replaceAll("[\n\r\f]", "").replaceAll("&nbsp;", "")
		    + "</body></html>";
	    client.setHTML(padId, etherpadHtml);
	}

	// gets read-only id
	String etherpadReadOnlyId = (String) client.getReadOnlyID(padId).get("readOnlyID");
	session.setEtherpadReadOnlyId(etherpadReadOnlyId);

	dokumaranSessionDao.saveObject(session);
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	DokumaranUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isSessionFinished() ? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }

    @Override
    public Cookie createEtherpadCookieForLearner(DokumaranUser user, DokumaranSession session)
	    throws DokumaranConfigurationException, URISyntaxException, DokumaranApplicationException {
	String groupId = session.getEtherpadGroupId();
	
	//don't allow sessions that has had problems with pad initializations. they could be fixed in monitoring by a teacher
	if (StringUtils.isEmpty(session.getEtherpadReadOnlyId()) || StringUtils.isEmpty(groupId)) {
	    throw new DokumaranApplicationException(
		    "This session has had problems with initialization. Please seek help from your teacher.");
	}

	EPLiteClient client = initializeEPLiteClient();

	String userName = user.getFirstName() + " " + user.getLastName();
	Map<String, String> map = client.createAuthorIfNotExistsFor(user.getUserId().toString(), userName);
	String authorId = map.get("authorID");

	// search for already existing user's session at Etherpad server
	Map etherpadSessions = client.listSessionsOfAuthor(authorId);
	String etherpadSessionId = getEtherpadSession(authorId, groupId, etherpadSessions);

	return createEtherpadCookie(etherpadSessionId);
    }

    @Override
    public Cookie createEtherpadCookieForMonitor(UserDTO user, Long contentId)
	    throws DokumaranConfigurationException, URISyntaxException {

	EPLiteClient client = initializeEPLiteClient();

	String userName = user.getFirstName() + " " + user.getLastName();
	Map<String, String> map = client.createAuthorIfNotExistsFor(user.getUserID().toString(), userName);
	String authorId = map.get("authorID");

	List<DokumaranSession> sessionList = dokumaranSessionDao.getByContentId(contentId);
	if (sessionList.isEmpty()) {
	    return null;
	}

	// in case sharedPadId is present - all sessions will share the same padId - and thus show only one pad
	Dokumaran dokumaran = getDokumaranByContentId(contentId);
	if (dokumaran.isSharedPadEnabled()) {
	    sessionList = sessionList.subList(0, 1);
	}

	// find according session
	String etherpadSessionIds = "";
	Map etherpadSessions = client.listSessionsOfAuthor(authorId);
	for (DokumaranSession session : sessionList) {
	    String groupId = session.getEtherpadGroupId();
	    
	    //skip sessions that has had problems with pad initializations so that they could be fixed in monitoring by a teacher
	    if (StringUtils.isEmpty(session.getEtherpadReadOnlyId()) || StringUtils.isEmpty(groupId)) {
		continue;
	    }

	    String etherpadSessionId = getEtherpadSession(authorId, groupId, etherpadSessions);
	    etherpadSessionIds += StringUtils.isEmpty(etherpadSessionIds) ? etherpadSessionId : "," + etherpadSessionId;
	}

	return createEtherpadCookie(etherpadSessionIds);
    }
    
    /**
     * Returns valid Etherpad session. Returns existing one if finds such one and creates the new one otherwise
     */
    private String getEtherpadSession(String authorId, String etherpadGroupId, Map etherpadSessions) {
	String etherpadSessionId = null;
	
	// search for already existing user's session
	boolean isValidForMoreThan1Hour = false;
	for (String etherpadSessionIdIter : (Set<String>) etherpadSessions.keySet()) {
	    Map<String, Object> sessessionAttributes = (Map<String, Object>) etherpadSessions
		    .get(etherpadSessionIdIter);
	    String groupIdIter = (String) sessessionAttributes.get("groupID");
	    if (groupIdIter.equals(etherpadGroupId)) {

		// check session expiration date
		long validUntil = (Long) sessessionAttributes.get("validUntil") * 1000;
		long now = System.currentTimeMillis();
		isValidForMoreThan1Hour = ((validUntil - now) > 0) && ((validUntil - now) >= 60 * 60 * 1000);

		//use existing session if it's valid for more than 1 hour
		if (isValidForMoreThan1Hour) {
		    etherpadSessionId = etherpadSessionIdIter;
		    break;
		
		} else {
		    // can't delete expired sessions as Etherpad throws an exception. Nonetheless it returns expired
		    // ones when client.listSessionsOfAuthor(authorId) is requested
		}
	    }

	}

	// if session with validity of more than 1 hour doesn't exist yet  - create it
	if (etherpadSessionId == null) {
	    Map map2 = client.createSession(etherpadGroupId, authorId, 24);
	    etherpadSessionId = (String) map2.get("sessionID");
	}

	return etherpadSessionId;
    }
    
    /**
     * Constructs cookie to be stored at a clientside browser.
     * 
     * @param etherpadSessionIds
     * @return
     * @throws URISyntaxException
     */
    private Cookie createEtherpadCookie(String etherpadSessionIds) throws URISyntaxException {
	DokumaranConfigItem etherpadServerUrlConfig = getConfigItem(DokumaranConfigItem.KEY_ETHERPAD_URL);
	String etherpadServerUrl = etherpadServerUrlConfig.getConfigValue();
	URI uri = new URI(etherpadServerUrl);
	//regex to get the top level part of a domain
        Pattern p = Pattern.compile("^(?:\\w+://)?[^:?#/\\s]*?([^.\\s]+\\.(?:[a-z]{2,}|co\\.uk|org\\.uk|ac\\.uk|edu\\.au|org\\.au|com\\.au|edu\\.sg|com\\.sg|net\\.sg|org\\.sg|gov\\.sg|per\\.sg))(?:[:?#/]|$)");
	// eg: uri.getHost() will return "www.foo.com"
	Matcher m = p.matcher(uri.getHost());
	String topLevelDomain = m.matches() ? m.group(1) : uri.getHost();

	Cookie etherpadSessionCookie = new Cookie("sessionID", etherpadSessionIds);
	etherpadSessionCookie.setDomain(topLevelDomain);
	// A negative value means that the cookie is not stored persistently and will be deleted when the Web browser
	// exits. A zero value causes the cookie to be deleted.
	etherpadSessionCookie.setMaxAge(-1);
	etherpadSessionCookie.setPath("/");

	return etherpadSessionCookie;	
    }

    @Override
    public EPLiteClient initializeEPLiteClient() throws DokumaranConfigurationException {
	if (client == null) {
	    // get the API key from the config table and create EPLiteClient using it
	    DokumaranConfigItem etherpadServerUrlConfig = getConfigItem(DokumaranConfigItem.KEY_ETHERPAD_URL);
	    DokumaranConfigItem etherpadApiKeyConfig = getConfigItem(DokumaranConfigItem.KEY_API_KEY);
	    if (etherpadApiKeyConfig == null || etherpadApiKeyConfig.getConfigValue() == null
		    || etherpadServerUrlConfig == null || etherpadServerUrlConfig.getConfigValue() == null) {
		throw new DokumaranConfigurationException("Dokumaran settings are not configured. apiKeyConfig="
			+ etherpadApiKeyConfig + " etherpadServerUrlConfig=" + etherpadServerUrlConfig
			+ " Please seek help from your administrator");
	    }

	    // create EPLiteClient
	    String etherpadServerUrl = etherpadServerUrlConfig.getConfigValue();
	    String etherpadApiKey = etherpadApiKeyConfig.getConfigValue();
	    client = new EPLiteClient(etherpadServerUrl, etherpadApiKey);
	}
	return client;
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    DokumaranService.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    DokumaranService.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	DokumaranSession session = dokumaranSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(DokumaranConstants.COMPLETED);
	    dokumaranSessionDao.saveObject(session);
	} else {
	    DokumaranService.log.error("Fail to leave tool Session.Could not find shared dokumaran "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared dokumaran session by given session id: " + toolSessionId);
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
	dokumaranSessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getDokumaranOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getDokumaranOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<ToolOutput>();
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getDokumaranOutputFactory().getSupportedDefinitionClasses(definitionType);
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

    public void setDokumaranDao(DokumaranDAO dokumaranDao) {
	this.dokumaranDao = dokumaranDao;
    }

    public void setDokumaranSessionDao(DokumaranSessionDAO dokumaranSessionDao) {
	this.dokumaranSessionDao = dokumaranSessionDao;
    }

    public void setDokumaranConfigItemDAO(DokumaranConfigItemDAO dokumaranConfigItemDAO) {
	this.dokumaranConfigItemDAO = dokumaranConfigItemDAO;
    }

    public void setDokumaranToolContentHandler(DokumaranToolContentHandler dokumaranToolContentHandler) {
	this.dokumaranToolContentHandler = dokumaranToolContentHandler;
    }

    public void setDokumaranUserDao(DokumaranUserDAO dokumaranUserDao) {
	this.dokumaranUserDao = dokumaranUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
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

    public DokumaranOutputFactory getDokumaranOutputFactory() {
	return dokumaranOutputFactory;
    }

    public void setDokumaranOutputFactory(DokumaranOutputFactory dokumaranOutputFactory) {
	this.dokumaranOutputFactory = dokumaranOutputFactory;
    }

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content. Mandatory fields in toolContentJSON: title, instructions, dokumaran,
     * user fields firstName, lastName and loginName Dokumaran must contain a JSONArray of JSONObject objects, which
     * have the following mandatory fields: title, description, type. If there are instructions for a dokumaran, the
     * instructions are a JSONArray of Strings. There should be at least one dokumaran object in the dokumaran array.
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON)
	    throws JSONException {

	Date updateDate = new Date();

	Dokumaran dokumaran = new Dokumaran();
	dokumaran.setContentId(toolContentID);
	dokumaran.setTitle(toolContentJSON.getString(RestTags.TITLE));
	dokumaran.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));
	dokumaran.setCreated(updateDate);

	dokumaran.setShowChat(JsonUtil.opt(toolContentJSON, "showChat", Boolean.FALSE));
	dokumaran.setShowLineNumbers(JsonUtil.opt(toolContentJSON, "showLineNumbers", Boolean.FALSE));
	dokumaran.setSharedPadId(JsonUtil.opt(toolContentJSON, "sharedPadId", (String) null));
	dokumaran.setLockWhenFinished(JsonUtil.opt(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	dokumaran.setReflectOnActivity(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	dokumaran.setReflectInstructions(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, (String) null));
	dokumaran.setUseSelectLeaderToolOuput(JsonUtil.opt(toolContentJSON, "useSelectLeaderToolOuput", Boolean.FALSE));
	dokumaran.setAllowMultipleLeaders(JsonUtil.opt(toolContentJSON, "allowMultipleLeaders", Boolean.FALSE));

	dokumaran.setContentInUse(false);
	dokumaran.setDefineLater(false);

	DokumaranUser dokumaranUser = getUserByIDAndContent(userID.longValue(), toolContentID);
	if (dokumaranUser == null) {
	    dokumaranUser = new DokumaranUser();
	    dokumaranUser.setFirstName(toolContentJSON.getString("firstName"));
	    dokumaranUser.setLastName(toolContentJSON.getString("lastName"));
	    dokumaranUser.setLoginName(toolContentJSON.getString("loginName"));
	    // dokumaranUser.setDokumaran(content);
	}

	dokumaran.setCreatedBy(dokumaranUser);

	saveOrUpdateDokumaran(dokumaran);

    }
}
