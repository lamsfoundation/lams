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
package org.lamsfoundation.lams.tool.daco.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

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
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dao.DacoAnswerDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoAnswerOptionDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoAttachmentDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoQuestionDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoQuestionVisitDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoSessionDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoUserDAO;
import org.lamsfoundation.lams.tool.daco.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.daco.dto.Summary;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoAttachment;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestionVisitLog;
import org.lamsfoundation.lams.tool.daco.model.DacoSession;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.daco.util.DacoToolContentHandler;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * 
 * @author Dapeng.Ni
 * 
 */
public class DacoServiceImpl implements IDacoService, ToolContentManager, ToolSessionManager

{
	static Logger log = Logger.getLogger(DacoServiceImpl.class.getName());
	private DacoDAO dacoDao;
	private DacoQuestionDAO dacoQuestionDao;
	private DacoAttachmentDAO dacoAttachmentDao;
	private DacoUserDAO dacoUserDao;
	private DacoSessionDAO dacoSessionDao;
	private DacoAnswerDAO dacoAnswerDao;
	private DacoAnswerOptionDAO dacoAnswerOptionDao;
	private DacoQuestionVisitDAO dacoQuestionVisitDao;
	// tool service
	private DacoToolContentHandler dacoToolContentHandler;
	private MessageService messageService;
	// system services
	private IRepositoryService repositoryService;
	private ILamsToolService toolService;
	private ILearnerService learnerService;
	private IAuditService auditService;
	private IUserManagementService userManagementService;
	private IExportToolContentService exportContentService;
	private ICoreNotebookService coreNotebookService;

	private class ReflectDTOComparator implements Comparator<ReflectDTO> {
		public int compare(ReflectDTO o1, ReflectDTO o2) {
			if (o1 != null && o2 != null) {
				return o1.getFullName().compareTo(o2.getFullName());
			}
			else if (o1 != null) {
				return 1;
			}
			else {
				return -1;
			}
		}
	}

	public IVersionedNode getFileNode(Long answerUid, String relPathString) throws DacoApplicationException {
		DacoAnswer answer = (DacoAnswer) dacoAnswerDao.getObject(DacoQuestion.class, answerUid);
		if (answer == null) {
			throw new DacoApplicationException("Reource question " + answerUid + " not found.");
		}

		return getFile(answer.getFileUuid(), answer.getFileVersionId(), relPathString);
	}

	// *******************************************************************************
	// Service method
	// *******************************************************************************
	/**
	 * Try to get the file. If forceLogin = false and an access denied exception occurs, call this method again to get a new
	 * ticket and retry file lookup. If forceLogin = true and it then fails then throw exception.
	 * 
	 * @param uuid
	 * @param versionId
	 * @param relativePath
	 * @param attemptCount
	 * @return file node
	 * @throws ImscpApplicationException
	 */
	private IVersionedNode getFile(Long uuid, Long versionId, String relativePath) throws DacoApplicationException {

		ITicket tic = getRepositoryLoginTicket();

		try {

			return repositoryService.getFileItem(tic, uuid, versionId, relativePath);

		}
		catch (AccessDeniedException e) {

			String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId + " path "
					+ relativePath + ".";

			error = error + "AccessDeniedException: " + e.getMessage() + " Unable to retry further.";
			DacoServiceImpl.log.error(error);
			throw new DacoApplicationException(error, e);

		}
		catch (Exception e) {

			String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId + " path "
					+ relativePath + "." + " Exception: " + e.getMessage();
			DacoServiceImpl.log.error(error);
			throw new DacoApplicationException(error, e);

		}
	}

	/**
	 * This method verifies the credentials of the Daco Tool and gives it the <code>Ticket</code> to login and access the
	 * Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the repository. This method would be called evertime the tool
	 * needs to upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws DacoApplicationException
	 */
	private ITicket getRepositoryLoginTicket() throws DacoApplicationException {
		ICredentials credentials = new SimpleCredentials(dacoToolContentHandler.getRepositoryUser(), dacoToolContentHandler
				.getRepositoryId());
		try {
			ITicket ticket = repositoryService.login(credentials, dacoToolContentHandler.getRepositoryWorkspaceName());
			return ticket;
		}
		catch (AccessDeniedException ae) {
			throw new DacoApplicationException("Access Denied to repository." + ae.getMessage());
		}
		catch (WorkspaceNotFoundException we) {
			throw new DacoApplicationException("Workspace not found." + we.getMessage());
		}
		catch (LoginException e) {
			throw new DacoApplicationException("Login failed." + e.getMessage());
		}
	}

	public Daco getDacoByContentId(Long contentId) {
		Daco rs = dacoDao.getByContentId(contentId);
		if (rs == null) {
			DacoServiceImpl.log.error("Could not find the content by given ID:" + contentId);
		}
		return rs;
	}

	public Daco getDefaultContent(Long contentId) throws DacoApplicationException {
		if (contentId == null) {
			String error = messageService.getMessage("error.msg.default.content.not.find");
			DacoServiceImpl.log.error(error);
			throw new DacoApplicationException(error);
		}

		Daco defaultContent = getDefaultDaco();
		// save default content by given ID.
		Daco content = new Daco();
		content = Daco.newInstance(defaultContent, contentId, dacoToolContentHandler);
		return content;
	}

	public List getAuthoredQuestions(Long dacoUid) {
		return dacoQuestionDao.getAuthoringQuestions(dacoUid);
	}

	public DacoAttachment uploadInstructionFile(FormFile uploadFile, String fileType) throws UploadDacoFileException {
		if (uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName())) {
			throw new UploadDacoFileException(messageService.getMessage("error.msg.upload.file.not.found",
					new Object[] { uploadFile }));
		}

		// upload file to repository
		NodeKey nodeKey = processFile(uploadFile, fileType);

		// create new attachement
		DacoAttachment file = new DacoAttachment();
		file.setFileType(fileType);
		file.setFileUuid(nodeKey.getUuid());
		file.setFileVersionId(nodeKey.getVersion());
		file.setFileName(uploadFile.getFileName());
		file.setCreated(new Date());

		return file;
	}

	public void createUser(DacoUser dacoUser) {
		dacoUserDao.saveObject(dacoUser);
	}

	public DacoUser getUserByIDAndContent(Long userId, Long contentId) {

		return dacoUserDao.getUserByUserIDAndContentID(userId, contentId);

	}

	public DacoUser getUserByIDAndSession(Long userId, Long sessionId) {

		return dacoUserDao.getUserByUserIDAndSessionID(userId, sessionId);

	}

	public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws DacoApplicationException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, fileUuid, fileVersionId);
		}
		catch (Exception e) {
			throw new DacoApplicationException("Exception occured while deleting files from" + " the repository "
					+ e.getMessage());
		}
	}

	public void saveOrUpdateDaco(Daco daco) {
		dacoDao.saveObject(daco);
	}

	public void deleteDacoAttachment(Long attachmentUid) {
		dacoAttachmentDao.removeObject(DacoAttachment.class, attachmentUid);

	}

	public void saveOrUpdateDacoQuestion(DacoQuestion question) {
		dacoQuestionDao.saveObject(question);
	}

	public void deleteDacoQuestion(Long uid) {
		dacoQuestionDao.removeObject(DacoQuestion.class, uid);
	}

	public void deleteDacoAnswer(Long uid) {
		dacoAnswerDao.removeObject(DacoAnswer.class, uid);
	}

	public List<List<DacoAnswer>> getDacoAnswersByUserAndDaco(Long userUid, Daco daco) {
		return dacoAnswerDao.getRecordsByUserUidAndDaco(userUid, daco);
	}

	public List<Summary> exportBySessionId(Long sessionId, boolean skipHide) {
		DacoSession session = dacoSessionDao.getSessionBySessionId(sessionId);
		if (session == null) {
			DacoServiceImpl.log.error("Failed get DacoSession by ID [" + sessionId + "]");
			return null;
		}
		// initial daco questions list
		List<Summary> questionList = new ArrayList();
		Set<DacoQuestion> resList = session.getDaco().getDacoQuestions();
		for (DacoQuestion question : resList) {
			if (skipHide && question.isHide()) {
				continue;
			}
			// if question is create by author
			if (question.isCreateByAuthor()) {
				Summary sum = new Summary(session.getSessionName(), question, false);
				questionList.add(sum);
			}
		}

		// get this session's all daco questions
		Set<DacoQuestion> sessList = session.getDacoQuestions();
		for (DacoQuestion question : sessList) {
			if (skipHide && question.isHide()) {
				continue;
			}

			// to skip all question create by author
			if (!question.isCreateByAuthor()) {
				Summary sum = new Summary(session.getSessionName(), question, false);
				questionList.add(sum);
			}
		}

		return questionList;
	}

	public List<List<Summary>> exportByContentId(Long contentId) {
		Daco daco = dacoDao.getByContentId(contentId);
		List<List<Summary>> groupList = new ArrayList();

		// create init daco questions list
		List<Summary> initList = new ArrayList();
		groupList.add(initList);
		Set<DacoQuestion> resList = daco.getDacoQuestions();
		for (DacoQuestion question : resList) {
			if (question.isCreateByAuthor()) {
				Summary sum = new Summary(null, question, true);
				initList.add(sum);
			}
		}

		// session by session
		List<DacoSession> sessionList = dacoSessionDao.getByContentId(contentId);
		for (DacoSession session : sessionList) {
			List<Summary> group = new ArrayList<Summary>();
			// get this session's all daco questions
			Set<DacoQuestion> sessList = session.getDacoQuestions();
			for (DacoQuestion question : sessList) {
				// to skip all question create by author
				if (!question.isCreateByAuthor()) {
					Summary sum = new Summary(session.getSessionName(), question, false);
					group.add(sum);
				}
			}
			if (group.size() == 0) {
				group.add(new Summary(session.getSessionName(), null, false));
			}
			groupList.add(group);
		}

		return groupList;
	}

	public Daco getDacoBySessionId(Long sessionId) {
		DacoSession session = dacoSessionDao.getSessionBySessionId(sessionId);
		// to skip CGLib problem
		Long contentId = session.getDaco().getContentId();
		Daco res = dacoDao.getByContentId(contentId);
		// construct dto fields;
		return res;
	}

	public DacoSession getDacoSessionBySessionId(Long sessionId) {
		return dacoSessionDao.getSessionBySessionId(sessionId);
	}

	public void saveOrUpdateDacoSession(DacoSession resSession) {
		dacoSessionDao.saveObject(resSession);
	}

	public void setQuestionComplete(Long dacoQuestionUid, Long userId, Long sessionId) {
		DacoQuestionVisitLog log = dacoQuestionVisitDao.getDacoQuestionLog(dacoQuestionUid, userId);
		if (log == null) {
			log = new DacoQuestionVisitLog();
			DacoQuestion question = dacoQuestionDao.getByUid(dacoQuestionUid);
			log.setDacoQuestion(question);
			DacoUser user = dacoUserDao.getUserByUserIDAndSessionID(userId, sessionId);
			log.setUser(user);
			log.setSessionId(sessionId);
			log.setAccessDate(new Timestamp(new Date().getTime()));
		}
		log.setComplete(true);
		dacoQuestionVisitDao.saveObject(log);
	}

	public void setQuestionAccess(Long dacoQuestionUid, Long userId, Long sessionId) {
		DacoQuestionVisitLog log = dacoQuestionVisitDao.getDacoQuestionLog(dacoQuestionUid, userId);
		if (log == null) {
			log = new DacoQuestionVisitLog();
			DacoQuestion question = dacoQuestionDao.getByUid(dacoQuestionUid);
			log.setDacoQuestion(question);
			DacoUser user = dacoUserDao.getUserByUserIDAndSessionID(userId, sessionId);
			log.setUser(user);
			log.setComplete(false);
			log.setSessionId(sessionId);
			log.setAccessDate(new Timestamp(new Date().getTime()));
			dacoQuestionVisitDao.saveObject(log);
		}
	}

	public String finishToolSession(Long toolSessionId, Long userId) throws DacoApplicationException {
		DacoUser user = dacoUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
		user.setSessionFinished(true);
		dacoUserDao.saveObject(user);

		String nextUrl = null;
		try {
			nextUrl = this.leaveToolSession(toolSessionId, userId);
		}
		catch (DataMissingException e) {
			throw new DacoApplicationException(e);
		}
		catch (ToolException e) {
			throw new DacoApplicationException(e);
		}
		return nextUrl;
	}

	public int checkMiniView(Long toolSessionId, Long userUid) {
		int miniView = dacoQuestionVisitDao.getUserViewLogCount(toolSessionId, userUid);
		DacoSession session = dacoSessionDao.getSessionBySessionId(toolSessionId);
		if (session == null) {
			DacoServiceImpl.log.error("Failed get session by ID [" + toolSessionId + "]");
			return 0;
		}
		// MARCIN: changed from "session.getDaco().getMiniViewDacoNumber()" to 0
		int reqView = 0;

		return reqView - miniView;
	}

	public DacoQuestion getDacoQuestionByUid(Long questionUid) {
		return dacoQuestionDao.getByUid(questionUid);
	}

	public List<List<Summary>> getSummary(Long contentId) {
		List<List<Summary>> groupList = new ArrayList<List<Summary>>();
		List<Summary> group = new ArrayList<Summary>();

		// get all question which is accessed by user
		Map<Long, Integer> visitCountMap = dacoQuestionVisitDao.getSummary(contentId);

		Daco daco = dacoDao.getByContentId(contentId);
		Set<DacoQuestion> resQuestionList = daco.getDacoQuestions();

		// get all sessions in a daco and retrieve all daco questions under this
		// session
		// plus initial daco questions by author creating (resQuestionList)
		List<DacoSession> sessionList = dacoSessionDao.getByContentId(contentId);
		for (DacoSession session : sessionList) {
			// one new group for one session.
			group = new ArrayList<Summary>();
			// firstly, put all initial daco question into this group.
			for (DacoQuestion question : resQuestionList) {
				Summary sum = new Summary(session.getSessionId(), session.getSessionName(), question);
				// set viewNumber according visit log
				if (visitCountMap.containsKey(question.getUid())) {
					sum.setViewNumber(visitCountMap.get(question.getUid()).intValue());
				}
				group.add(sum);
			}
			// get this session's all daco questions
			Set<DacoQuestion> sessQuestionList = session.getDacoQuestions();
			for (DacoQuestion question : sessQuestionList) {
				// to skip all question create by author
				if (!question.isCreateByAuthor()) {
					Summary sum = new Summary(session.getSessionId(), session.getSessionName(), question);
					// set viewNumber according visit log
					if (visitCountMap.containsKey(question.getUid())) {
						sum.setViewNumber(visitCountMap.get(question.getUid()).intValue());
					}
					group.add(sum);
				}
			}
			// so far no any question available, so just put session name info
			// to Summary
			if (group.size() == 0) {
				group.add(new Summary(session.getSessionId(), session.getSessionName(), null));
			}
			groupList.add(group);
		}

		return groupList;

	}

	public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId) {
		Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();

		List<DacoSession> sessionList = dacoSessionDao.getByContentId(contentId);
		for (DacoSession session : sessionList) {
			Long sessionId = session.getSessionId();
			boolean hasRefection = session.getDaco().isReflectOnActivity();
			Set<ReflectDTO> list = new TreeSet<ReflectDTO>(this.new ReflectDTOComparator());
			// get all users in this session
			List<DacoUser> users = dacoUserDao.getBySessionID(sessionId);
			for (DacoUser user : users) {
				ReflectDTO ref = new ReflectDTO(user);
				ref.setHasRefection(hasRefection);
				list.add(ref);
			}
			map.put(sessionId, list);
		}

		return map;
	}

	public List<DacoUser> getUserListBySessionQuestion(Long sessionId, Long questionUid) {
		List<DacoQuestionVisitLog> logList = dacoQuestionVisitDao.getDacoQuestionLogBySession(sessionId, questionUid);
		List<DacoUser> userList = new ArrayList(logList.size());
		for (DacoQuestionVisitLog visit : logList) {
			DacoUser user = visit.getUser();
			user.setAccessDate(visit.getAccessDate());
			userList.add(user);
		}
		return userList;
	}

	public void setQuestionVisible(Long questionUid, boolean visible) {
		DacoQuestion question = dacoQuestionDao.getByUid(questionUid);
		if (question != null) {
			// createBy should be null for system default value.
			Long userId = 0L;
			String loginName = "No user";
			if (question.getCreateBy() != null) {
				userId = question.getCreateBy().getUserId();
				loginName = question.getCreateBy().getLoginName();
			}
			if (visible) {
				auditService.logShowEntry(DacoConstants.TOOL_SIGNATURE, userId, loginName, question.toString());
			}
			else {
				auditService.logHideEntry(DacoConstants.TOOL_SIGNATURE, userId, loginName, question.toString());
			}
			question.setHide(!visible);
			dacoQuestionDao.saveObject(question);
		}
	}

	public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
			String entryText) {
		return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "", entryText);
	}

	public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
		List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
		if (list == null || list.isEmpty()) {
			return null;
		}
		else {
			return list.get(0);
		}
	}

	/**
	 * @param notebookEntry
	 */
	public void updateEntry(NotebookEntry notebookEntry) {
		coreNotebookService.updateEntry(notebookEntry);
	}

	public DacoUser getUser(Long uid) {
		return (DacoUser) dacoUserDao.getObject(DacoUser.class, uid);
	}

	// *****************************************************************************
	// private methods
	// *****************************************************************************
	private Daco getDefaultDaco() throws DacoApplicationException {
		Long defaultDacoId = getToolDefaultContentIdBySignature(DacoConstants.TOOL_SIGNATURE);
		Daco defaultDaco = getDacoByContentId(defaultDacoId);
		if (defaultDaco == null) {
			String error = messageService.getMessage("error.msg.default.content.not.find");
			DacoServiceImpl.log.error(error);
			throw new DacoApplicationException(error);
		}

		return defaultDaco;
	}

	private Long getToolDefaultContentIdBySignature(String toolSignature) throws DacoApplicationException {
		Long contentId = null;
		contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
		if (contentId == null) {
			String error = messageService.getMessage("error.msg.default.content.not.find");
			DacoServiceImpl.log.error(error);
			throw new DacoApplicationException(error);
		}
		return contentId;
	}

	/**
	 * Process an uploaded file.
	 * 
	 * @throws DacoApplicationException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RepositoryCheckedException
	 * @throws InvalidParameterException
	 */
	private NodeKey processFile(FormFile file, String fileType) throws UploadDacoFileException {
		NodeKey node = null;
		if (file != null && !StringUtils.isEmpty(file.getFileName())) {
			String fileName = file.getFileName();
			try {
				node = dacoToolContentHandler.uploadFile(file.getInputStream(), fileName, file.getContentType(), fileType);
			}
			catch (InvalidParameterException e) {
				throw new UploadDacoFileException(messageService.getMessage("error.msg.invaid.param.upload"));
			}
			catch (FileNotFoundException e) {
				throw new UploadDacoFileException(messageService.getMessage("error.msg.file.not.found"));
			}
			catch (RepositoryCheckedException e) {
				throw new UploadDacoFileException(messageService.getMessage("error.msg.repository"));
			}
			catch (IOException e) {
				throw new UploadDacoFileException(messageService.getMessage("error.msg.io.exception"));
			}
		}
		return node;
	}

	private NodeKey processPackage(String packageDirectory, String initFile) throws UploadDacoFileException {
		NodeKey node = null;
		try {
			node = dacoToolContentHandler.uploadPackage(packageDirectory, initFile);
		}
		catch (InvalidParameterException e) {
			throw new UploadDacoFileException(messageService.getMessage("error.msg.invaid.param.upload"));
		}
		catch (RepositoryCheckedException e) {
			throw new UploadDacoFileException(messageService.getMessage("error.msg.repository"));
		}
		return node;
	}

	public void uploadDacoAnswerFile(DacoAnswer answer, FormFile file) throws UploadDacoFileException {
		try {
			InputStream is = file.getInputStream();
			String fileName = file.getFileName();
			String fileType = file.getContentType();
			// For file only upload one sigle file
			if (answer.getQuestion().getType() == DacoConstants.QUESTION_TYPE_FILE
					|| answer.getQuestion().getType() == DacoConstants.QUESTION_TYPE_IMAGE) {
				NodeKey nodeKey = processFile(file, IToolContentHandler.TYPE_ONLINE);
				answer.setFileUuid(nodeKey.getUuid());
				answer.setFileVersionId(nodeKey.getVersion());
			}

			// create the package from the directory contents
			answer.setFileType(fileType);
			answer.setFileName(fileName);
		}
		catch (FileNotFoundException e) {
			DacoServiceImpl.log.error(messageService.getMessage("error.msg.file.not.found") + ":" + e.toString());
			throw new UploadDacoFileException(messageService.getMessage("error.msg.file.not.found"));
		}
		catch (IOException e) {
			DacoServiceImpl.log.error(messageService.getMessage("error.msg.io.exception") + ":" + e.toString());
			throw new UploadDacoFileException(messageService.getMessage("error.msg.io.exception"));
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

	public void setDacoAttachmentDao(DacoAttachmentDAO dacoAttachmentDao) {
		this.dacoAttachmentDao = dacoAttachmentDao;
	}

	public void setDacoDao(DacoDAO dacoDao) {
		this.dacoDao = dacoDao;
	}

	public void setDacoQuestionDao(DacoQuestionDAO dacoQuestionDao) {
		this.dacoQuestionDao = dacoQuestionDao;
	}

	public void setDacoSessionDao(DacoSessionDAO dacoSessionDao) {
		this.dacoSessionDao = dacoSessionDao;
	}

	public void setDacoAnswerDao(DacoAnswerDAO dacoAnswerDao) {
		this.dacoAnswerDao = dacoAnswerDao;
	}

	public void setDacoAnswerOptionDao(DacoAnswerOptionDAO dacoOptionDao) {
		dacoAnswerOptionDao = dacoOptionDao;
	}

	public void setDacoToolContentHandler(DacoToolContentHandler dacoToolContentHandler) {
		this.dacoToolContentHandler = dacoToolContentHandler;
	}

	public void setDacoUserDao(DacoUserDAO dacoUserDao) {
		this.dacoUserDao = dacoUserDao;
	}

	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}

	public DacoQuestionVisitDAO getDacoQuestionVisitDao() {
		return dacoQuestionVisitDao;
	}

	public void setDacoQuestionVisitDao(DacoQuestionVisitDAO dacoQuestionVisitDao) {
		this.dacoQuestionVisitDao = dacoQuestionVisitDao;
	}

	// *******************************************************************************
	// ToolContentManager, ToolSessionManager methods
	// *******************************************************************************

	public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
		Daco toolContentObj = dacoDao.getByContentId(toolContentId);
		if (toolContentObj == null) {
			try {
				toolContentObj = getDefaultDaco();
			}
			catch (DacoApplicationException e) {
				throw new DataMissingException(e.getMessage());
			}
		}
		if (toolContentObj == null) {
			throw new DataMissingException("Unable to find default content for the daco tool");
		}

		// set DacoToolContentHandler as null to avoid copy file node in
		// repository again.
		toolContentObj = Daco.newInstance(toolContentObj, toolContentId, null);
		toolContentObj.setToolContentHandler(null);
		toolContentObj.setOfflineFileList(null);
		toolContentObj.setOnlineFileList(null);
		try {
			exportContentService.registerFileClassForExport(DacoAttachment.class.getName(), "fileUuid", "fileVersionId");
			exportContentService.registerFileClassForExport(DacoQuestion.class.getName(), "fileUuid", "fileVersionId");
			exportContentService.exportToolContent(toolContentId, toolContentObj, dacoToolContentHandler, rootPath);
		}
		catch (ExportToolContentException e) {
			throw new ToolException(e);
		}
	}

	public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
			String toVersion) throws ToolException {

		try {
			exportContentService.registerFileClassForImport(DacoAttachment.class.getName(), "fileUuid", "fileVersionId",
					"fileName", "fileType", null, null);
			exportContentService.registerFileClassForImport(DacoQuestion.class.getName(), "fileUuid", "fileVersionId",
					"fileName", "fileType", null, "initialQuestion");

			Object toolPOJO = exportContentService.importToolContent(toolContentPath, dacoToolContentHandler, fromVersion,
					toVersion);
			if (!(toolPOJO instanceof Daco)) {
				throw new ImportToolContentException("Import Share daco tool content failed. Deserialized object is " + toolPOJO);
			}
			Daco toolContentObj = (Daco) toolPOJO;

			// reset it to new toolContentId
			toolContentObj.setContentId(toolContentId);
			DacoUser user = dacoUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()), toolContentId);
			if (user == null) {
				user = new DacoUser();
				UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
				user.setFirstName(sysUser.getFirstName());
				user.setLastName(sysUser.getLastName());
				user.setLoginName(sysUser.getLogin());
				user.setUserId(new Long(newUserUid.longValue()));
				user.setDaco(toolContentObj);
			}
			toolContentObj.setCreatedBy(user);

			// reset all dacoQuestion createBy user
			Set<DacoQuestion> questions = toolContentObj.getDacoQuestions();
			for (DacoQuestion question : questions) {
				question.setCreateBy(user);
			}
			dacoDao.saveObject(toolContentObj);
		}
		catch (ImportToolContentException e) {
			throw new ToolException(e);
		}
	}

	/**
	 * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions that are
	 * always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created for a particular
	 * activity such as the answer to the third question contains the word Koala and hence the need for the toolContentId
	 * 
	 * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
	 */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
		return new TreeMap<String, ToolOutputDefinition>();
	}

	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
		if (toContentId == null) {
			throw new ToolException("Failed to create the SharedDacoFiles tool seession");
		}

		Daco daco = null;
		if (fromContentId != null) {
			daco = dacoDao.getByContentId(fromContentId);
		}
		if (daco == null) {
			try {
				daco = getDefaultDaco();
			}
			catch (DacoApplicationException e) {
				throw new ToolException(e);
			}
		}

		Daco toContent = Daco.newInstance(daco, toContentId, dacoToolContentHandler);
		dacoDao.saveObject(toContent);

		// save daco questions as well
		Set questions = toContent.getDacoQuestions();
		if (questions != null) {
			Iterator iter = questions.iterator();
			while (iter.hasNext()) {
				DacoQuestion question = (DacoQuestion) iter.next();
				// createRootTopic(toContent.getUid(),null,msg);
			}
		}
	}

	public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
		Daco daco = dacoDao.getByContentId(toolContentId);
		if (daco == null) {
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		daco.setDefineLater(value);
	}

	public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
		Daco daco = dacoDao.getByContentId(toolContentId);
		if (daco == null) {
			throw new ToolException("No found tool content by given content ID:" + toolContentId);
		}
		daco.setRunOffline(value);
	}

	public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException {
		Daco daco = dacoDao.getByContentId(toolContentId);
		if (removeSessionData) {
			List list = dacoSessionDao.getByContentId(toolContentId);
			Iterator iter = list.iterator();
			while (iter.hasNext()) {
				DacoSession session = (DacoSession) iter.next();
				dacoSessionDao.delete(session);
			}
		}
		dacoDao.delete(daco);
	}

	public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
		DacoSession session = new DacoSession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		Daco daco = dacoDao.getByContentId(toolContentId);
		session.setDaco(daco);
		dacoSessionDao.saveObject(session);
	}

	public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
		if (toolSessionId == null) {
			DacoServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
			throw new ToolException("Fail to remove tool Session based on null tool session id.");
		}
		if (learnerId == null) {
			DacoServiceImpl.log.error("Fail to leave tool Session based on null learner.");
			throw new ToolException("Fail to remove tool Session based on null learner.");
		}

		DacoSession session = dacoSessionDao.getSessionBySessionId(toolSessionId);
		if (session != null) {
			session.setStatus(DacoConstants.COMPLETED);
			dacoSessionDao.saveObject(session);
		}
		else {
			DacoServiceImpl.log.error("Fail to leave tool Session.Could not find shared daco " + "session by given session id: "
					+ toolSessionId);
			throw new DataMissingException("Fail to leave tool Session."
					+ "Could not find shared daco session by given session id: " + toolSessionId);
		}
		return learnerService.completeToolSession(toolSessionId, learnerId);
	}

	public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		return null;
	}

	public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException {
		return null;
	}

	public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
		dacoSessionDao.deleteBySessionId(toolSessionId);
	}

	/**
	 * Get the tool output for the given tool output names.
	 * 
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long, java.lang.Long)
	 */
	public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
		return new TreeMap<String, ToolOutput>();
	}

	/**
	 * Get the tool output for the given tool output name.
	 * 
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
		return null;
	}

	/*
	 * ===============Methods implemented from ToolContentImport102Manager ===============
	 */

	/**
	 * Set the description, throws away the title value as this is not supported in 2.0
	 */
	public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
			DataMissingException {

		Daco toolContentObj = getDacoByContentId(toolContentId);
		if (toolContentObj == null) {
			throw new DataMissingException("Unable to set reflective data titled " + title + " on activity toolContentId "
					+ toolContentId + " as the tool content does not exist.");
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

	public void saveOrUpdateAnswer(DacoAnswer answer) {
		dacoAnswerDao.saveObject(answer);
	}

	public String getLocalisedMessage(String key, Object[] args) {
		return messageService.getMessage(key, args);
	}
}