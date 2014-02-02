/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.sbmt.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
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
import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitUserDAO;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SubmitUserDTO;
import org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.util.LastNameAlphabeticComparator;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.dao.DataAccessException;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesService implements ToolContentManager, ToolSessionManager, ISubmitFilesService,
	ToolContentImport102Manager {

    private static Logger log = Logger.getLogger(SubmitFilesService.class);

    private ISubmitFilesContentDAO submitFilesContentDAO;

    private ISubmitFilesReportDAO submitFilesReportDAO;

    private ISubmitFilesSessionDAO submitFilesSessionDAO;

    private ISubmissionDetailsDAO submissionDetailsDAO;

    private ISubmitUserDAO submitUserDAO;

    private IToolContentHandler sbmtToolContentHandler;

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private IRepositoryService repositoryService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IUserManagementService userManagementService;

    private IEventNotificationService eventNotificationService;

    private MessageService messageService;

    private ILessonService lessonService;

    private IGradebookService gradebookService;

    private SubmitFilesOutputFactory submitFilesOutputFactory;

    private class FileDtoComparator implements Comparator<FileDetailsDTO> {

	public int compare(FileDetailsDTO o1, FileDetailsDTO o2) {
	    if (o1 != null && o2 != null && o1.getDateOfSubmission() != null && o2.getDateOfSubmission() != null) {
		// don't use Date.comparaTo() directly, because the date could be Timestamp or Date (depeneds the object
		// is persist or not)
		return o1.getDateOfSubmission().getTime() - o2.getDateOfSubmission().getTime() > 0 ? 1 : -1;
	    } else if (o1 != null) {
		return 1;
	    } else {
		return -1;
	    }
	}

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

    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.ToolContentManager#copyToolContent(java.lang.Long, java.lang.Long)
     */
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SubmitFiles tool seession");
	}

	SubmitFilesContent fromContent = null;
	if (fromContentId != null) {
	    fromContent = submitFilesContentDAO.getContentByID(fromContentId);
	}
	if (fromContent == null) {
	    fromContent = getDefaultSubmit();
	}
	SubmitFilesContent toContent = SubmitFilesContent.newInstance(fromContent, toContentId);

	submitFilesContentDAO.saveOrUpdate(toContent);
    }

    /**
     * @throws SessionDataExistsException
     * 
     * @see org.lamsfoundation.lams.tool.ToolContentManager#removeToolContent(java.lang.Long)
     */
    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException {
	SubmitFilesContent submitFilesContent = submitFilesContentDAO.getContentByID(toolContentId);
	if (submitFilesContent != null) {
	    // if session data exist and removeSessionData=false, throw an exception
	    List submissionData = submitFilesSessionDAO.getSubmitFilesSessionByContentID(toolContentId);
	    if (!(submissionData == null || submissionData.isEmpty()) && !removeSessionData) {
		throw new SessionDataExistsException(
			"Delete failed: There is session data that belongs to this tool content id");
	    } else if (submissionData != null) {
		Iterator iter = submissionData.iterator();
		while (iter.hasNext()) {
		    SubmitFilesSession element = (SubmitFilesSession) iter.next();
		    removeToolSession(element);
		}
	    }
	    submitFilesContentDAO.delete(submitFilesContent);
	}
    }
    
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (log.isDebugEnabled()) {
	    log.debug("Removing Submit Files content for user ID " + userId + " and toolContentId " + toolContentId);
	}

	List<SubmitFilesSession> sessions = submitFilesSessionDAO.getSubmitFilesSessionByContentID(toolContentId);
	for (SubmitFilesSession session : sessions) {
	    List<SubmissionDetails> submissions = submissionDetailsDAO.getBySessionAndLearner(session.getSessionID(),
		    userId);
	    submissionDetailsDAO.deleteAll(submissions);

	    SubmitUser user = submitUserDAO.getLearner(session.getSessionID(), userId);
	    if (user != null) {
		user.setFinished(false);
		submitUserDAO.update(user);
	    }
	}
    }

    public List<SubmitFilesSession> getSessionsByContentID(Long toolContentID) {
	return submitFilesSessionDAO.getSubmitFilesSessionByContentID(toolContentID);
    }

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws ExportToolContentException
     */
    public void exportToolContent(Long toolContentId, String toPath) throws ToolException, DataMissingException {
	SubmitFilesContent toolContentObj = submitFilesContentDAO.getContentByID(toolContentId);
	if (toolContentObj == null) {
	    toolContentObj = getDefaultSubmit();
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the submit files tool");
	}

	// set toolContentHandler as null to avoid duplicate file node in repository.
	toolContentObj = SubmitFilesContent.newInstance(toolContentObj, toolContentId);
	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, sbmtToolContentHandler, toPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(SubmitFilesImportContentVersionFilter.class);
	
	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, sbmtToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof SubmitFilesContent)) {
		throw new ImportToolContentException("Import Submit tool content failed. Deserialized object is "
			+ toolPOJO);
	    }
	    SubmitFilesContent toolContentObj = (SubmitFilesContent) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentID(toolContentId);

	    SubmitUser user = submitUserDAO.getContentUser(toolContentId, newUserUid);
	    if (user == null) {
		user = new SubmitUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLogin(sysUser.getLogin());
		user.setUserID(newUserUid);
		user.setContentID(toolContentId);
		submitUserDAO.saveOrUpdateUser(user);
	    }

	    toolContentObj.setCreatedBy(user);
	    toolContentObj.setCreated(new Date());
	    toolContentObj.setUpdated(new Date());

	    submitFilesContentDAO.saveOrUpdate(toolContentObj);
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
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	SubmitFilesContent content = getSubmitFilesContent(toolContentId);
	if (content == null) {
	    content = getDefaultSubmit();
	}
	return getSubmitFilesOutputFactory().getToolOutputDefinitions(content, definitionType);
    }
    
    public String getToolContentTitle(Long toolContentId) {
	return getSubmitFilesContent(toolContentId).getTitle();
    }
   
    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#updateSubmitFilesContent(org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent)
     */
    public void saveOrUpdateContent(SubmitFilesContent submitFilesContent) {
	submitFilesContent.setUpdated(new Date());
	submitFilesContentDAO.saveOrUpdate(submitFilesContent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getSubmitFilesContent(java.lang.Long)
     */
    public SubmitFilesContent getSubmitFilesContent(Long contentID) {
	SubmitFilesContent content = null;
	try {
	    content = submitFilesContentDAO.getContentByID(contentID);
	} catch (Exception e) {
	    SubmitFilesService.log.error("Could not find the content by given ID:" + contentID + ". Excpetion is " + e);
	}
	if (content == null) {
	    SubmitFilesService.log.error("Could not find the content by given ID:" + contentID);
	}

	return content;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getSubmitFilesReport(java.lang.Long)
     */
    public SubmitFilesReport getSubmitFilesReport(Long reportID) {
	return submitFilesReportDAO.getReportByID(reportID);
    }

    /**
     * This method verifies the credentials of the SubmitFiles Tool and gives it the <code>Ticket</code> to login and
     * access the Content Repository.
     * 
     * A valid ticket is needed in order to access the content from the repository. This method would be called evertime
     * the tool needs to upload/download files from the content repository.
     * 
     * @return ITicket The ticket for repostory access
     * @throws SubmitFilesException
     */
    private ITicket getRepositoryLoginTicket() throws SubmitFilesException {
	ICredentials credentials = new SimpleCredentials(SbmtToolContentHandler.repositoryUser,
		SbmtToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, SbmtToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new SubmitFilesException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new SubmitFilesException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new SubmitFilesException("Login failed." + e.getMessage());
	}
    }

    /**
     * This method deletes the content with the given <code>uuid</code> and <code>versionID</code> from the content
     * repository
     * 
     * @param uuid
     *                The <code>uuid</code> of the node to be deleted
     * @param versionID
     *                The <code>version_id</code> of the node to be deleted.
     * @throws SubmitFilesException
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws SubmitFilesException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new SubmitFilesException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#createToolSession(java.lang.Long,java.lang.String,
     *      java.lang.Long)
     */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) {
	// pre-condition validation
	if (toolSessionId == null || toolContentId == null) {
	    throw new SubmitFilesException("Fail to create a submission session"
		    + " based on null toolSessionId or toolContentId");
	}

	SubmitFilesService.log.debug("Start to create submission session based on toolSessionId["
		+ toolSessionId.longValue() + "] and toolContentId[" + toolContentId.longValue() + "]");
	try {
	    SubmitFilesContent submitContent = getSubmitFilesContent(toolContentId);
	    if (submitContent == null || !toolContentId.equals(submitContent.getContentID())) {
		submitContent = new SubmitFilesContent();
		submitContent.setContentID(toolContentId);
	    }
	    SubmitFilesSession submitSession = new SubmitFilesSession();

	    submitSession.setSessionID(toolSessionId);
	    submitSession.setSessionName(toolSessionName);
	    submitSession.setStatus(new Integer(SubmitFilesSession.INCOMPLETE));
	    submitSession.setContent(submitContent);
	    submitFilesSessionDAO.createSession(submitSession);
	    SubmitFilesService.log.debug("Submit File session created");
	} catch (DataAccessException e) {
	    throw new SubmitFilesException("Exception occured when lams is creating" + " a submission Session: "
		    + e.getMessage(), e);
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#leaveToolSession(java.lang.Long,
     *      org.lamsfoundation.lams.usermanagement.User)
     */
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    SubmitFilesService.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    SubmitFilesService.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	SubmitFilesSession session = submitFilesSessionDAO.getSessionByID(toolSessionId);
	if (session != null) {
	    session.setStatus(new Integer(SubmitFilesSession.COMPLETED));
	    submitFilesSessionDAO.update(session);
	} else {
	    SubmitFilesService.log.error("Fail to leave tool Session.Could not find submit file "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find submit file session by given session id: " + toolSessionId);
	}
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#exportToolSession(java.lang.Long)
     */
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#exportToolSession(java.util.List)
     */
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#removeToolSession(java.lang.Long)
     */
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    SubmitFilesService.log.error("Fail to remove tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}

	SubmitFilesSession session = submitFilesSessionDAO.getSessionByID(toolSessionId);
	if (session != null) {
	    removeToolSession(session);
	} else {
	    SubmitFilesService.log.error("Could not find submit file session by given session id: " + toolSessionId);
	    throw new DataMissingException("Could not find submit file session by given session id: " + toolSessionId);
	}
    }

    /** Remove a tool session. The session parameter must not be null. */
    private void removeToolSession(SubmitFilesSession session) {
	Set filesUploaded = session.getSubmissionDetails();
	if (filesUploaded != null) {
	    Iterator fileIterator = filesUploaded.iterator();
	    while (fileIterator.hasNext()) {
		SubmissionDetails details = (SubmissionDetails) fileIterator.next();
		deleteFromRepository(details.getUuid(), details.getVersionID());
		submissionDetailsDAO.delete(details);
	    }
	}
	submitFilesSessionDAO.delete(session);
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util .List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getSubmitFilesOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getSubmitFilesOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager# uploadFileToSession(Long,FormFile,String,Long )
     */
    public void uploadFileToSession(Long sessionID, FormFile uploadFile, String fileDescription, Integer userID)
	    throws SubmitFilesException {

	if (uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName())) {
	    throw new SubmitFilesException("Could not find upload file: " + uploadFile);
	}

	SubmitFilesSession session = submitFilesSessionDAO.getSessionByID(sessionID);
	if (session == null) {
	    throw new SubmitFilesException("No such session with a sessionID of: " + sessionID + " found.");
	}

	NodeKey nodeKey = processFile(uploadFile);

	SubmissionDetails details = new SubmissionDetails();
	details.setFileDescription(fileDescription);
	details.setFilePath(uploadFile.getFileName());
	details.setDateOfSubmission(new Date());

	SubmitUser learner = submitUserDAO.getLearner(sessionID, userID);
	details.setLearner(learner);
	details.setUuid(nodeKey.getUuid());
	details.setVersionID(nodeKey.getVersion());
	SubmitFilesReport report = new SubmitFilesReport();
	details.setReport(report);
	details.setSubmitFileSession(session);

	// update session, then insert the detail too.
	Set detailSet = session.getSubmissionDetails();
	detailSet.add(details);
	session.setSubmissionDetails(detailSet);
	submissionDetailsDAO.saveOrUpdate(session);

    }

    /**
     * Process an uploaded file.
     * 
     * @param forumForm
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file) {
	NodeKey node = null;
	if (file != null && !StringUtils.isEmpty(file.getFileName())) {
	    String fileName = file.getFileName();
	    try {
		node = getSbmtToolContentHandler().uploadFile(file.getInputStream(), fileName, file.getContentType());
	    } catch (InvalidParameterException e) {
		throw new SubmitFilesException("FileNotFoundException occured while trying to upload File"
			+ e.getMessage());
	    } catch (FileNotFoundException e) {
		throw new SubmitFilesException("FileNotFoundException occured while trying to upload File"
			+ e.getMessage());
	    } catch (RepositoryCheckedException e) {
		throw new SubmitFilesException("FileNotFoundException occured while trying to upload File"
			+ e.getMessage());
	    } catch (IOException e) {
		throw new SubmitFilesException("FileNotFoundException occured while trying to upload File"
			+ e.getMessage());
	    }
	}
	return node;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getFilesUploadedByUserForContent(java.lang.Long,
     *      java.lang.Long)
     */
    public List getFilesUploadedByUser(Integer userID, Long sessionID, Locale currentLocale) {
	List<SubmissionDetails> list = submissionDetailsDAO.getBySessionAndLearner(sessionID, userID);
	SortedSet details = new TreeSet(this.new FileDtoComparator());
	if (list == null) {
	    return new ArrayList(details);
	}

	NumberFormat numberFormat = currentLocale != null ? NumberFormat.getInstance(currentLocale) : null;
	for (SubmissionDetails submissionDetails : list) {
	    FileDetailsDTO detailDto = new FileDetailsDTO(submissionDetails, numberFormat);
	    details.add(detailDto);
	}
	return new ArrayList(details);
    }

    /**
     * This method save SubmissionDetails list into a map container: key is user id, value is a list container, which
     * contains all <code>FileDetailsDTO</code> object belong to this user.
     */
    public SortedMap getFilesUploadedBySession(Long sessionID, Locale currentLocale) {
	List list = submissionDetailsDAO.getSubmissionDetailsBySession(sessionID);
	if (list != null) {
	    SortedMap map = new TreeMap(new LastNameAlphabeticComparator());
	    Iterator iterator = list.iterator();
	    List userFileList;
	    NumberFormat numberFormat = currentLocale != null ? NumberFormat.getInstance(currentLocale) : null;
	    while (iterator.hasNext()) {
		SubmissionDetails submissionDetails = (SubmissionDetails) iterator.next();
		SubmitUser learner = submissionDetails.getLearner();
		if (learner == null) {
		    SubmitFilesService.log.error("Could not find learer for special submission item:"
			    + submissionDetails);
		    return null;
		}
		SubmitUserDTO submitUserDTO = new SubmitUserDTO(learner);

		FileDetailsDTO detailDto = new FileDetailsDTO(submissionDetails, numberFormat);
		userFileList = (List) map.get(submitUserDTO);
		// if it is first time to this user, creating a new ArrayList for this user.
		if (userFileList == null) {
		    userFileList = new ArrayList();
		}
		userFileList.add(detailDto);
		map.put(submitUserDTO, userFileList);
	    }
	    return map;
	} else {
	    return null;
	}
    }

    public FileDetailsDTO getFileDetails(Long detailID, Locale currentLocale) {
	SubmissionDetails details = submissionDetailsDAO.getSubmissionDetailsByID(detailID);
	return new FileDetailsDTO(details, currentLocale != null ? NumberFormat.getInstance(currentLocale) : null);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getUsersBySession(java.lang.Long)
     */
    public List<SubmitUser> getUsersBySession(Long sessionID) {
	return submitUserDAO.getUsersBySession(sessionID);
    }

    public void updateMarks(Long reportID, Float marks, String comments, FormFile markFile)
	    throws InvalidParameterException, RepositoryCheckedException {

	IToolContentHandler toolContentHandler = getSbmtToolContentHandler();
	SubmitFilesReport report = submitFilesReportDAO.getReportByID(reportID);
	if (report != null) {
	    report.setComments(comments);
	    report.setMarks(marks);

	    // If there is a new file, delete the existing and add the mark file
	    if (markFile != null && !StringUtils.isEmpty(markFile.getFileName())) {

		// Delete the existing
		if (report.getMarkFileUUID() != null) {
		    toolContentHandler.deleteFile(report.getMarkFileUUID());
		    report.setMarkFileName(null);
		    report.setMarkFileUUID(null);
		    report.setMarkFileVersionID(null);
		}

		// Add the new file
		NodeKey nodeKey = this.processFile(markFile);
		// NodeKey nodeKey = toolContentHandler.uploadFile(marksFileInputStream, marksFileName, null,
		// IToolContentHandler.TYPE_ONLINE);

		report.setMarkFileName(markFile.getFileName());
		report.setMarkFileUUID(nodeKey.getUuid());
		report.setMarkFileVersionID(nodeKey.getVersion());
	    }

	    submitFilesReportDAO.update(report);
	}

    }

    public void removeMarkFile(Long reportID, Long markFileUUID, Long markFileVersionID) {
	SubmitFilesReport report = submitFilesReportDAO.getReportByID(reportID);
	if (report != null) {
	    deleteFromRepository(markFileUUID, markFileVersionID);
	    report.setMarkFileName(null);
	    report.setMarkFileUUID(null);
	    report.setMarkFileVersionID(null);
	    submitFilesReportDAO.update(report);
	}
    }

    public IVersionedNode downloadFile(Long uuid, Long versionID) throws SubmitFilesException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    IVersionedNode node = repositoryService.getFileItem(ticket, uuid, null);
	    return node;
	} catch (AccessDeniedException ae) {
	    throw new SubmitFilesException("AccessDeniedException occured while trying to download file "
		    + ae.getMessage());
	} catch (FileException fe) {
	    throw new SubmitFilesException("FileException occured while trying to download file " + fe.getMessage());
	} catch (ItemNotFoundException ie) {
	    throw new SubmitFilesException("ItemNotFoundException occured while trying to download file "
		    + ie.getMessage());
	}
    }

    public SubmitFilesSession getSessionById(Long sessionID) {
	return submitFilesSessionDAO.getSessionByID(sessionID);
    }

    public boolean releaseMarksForSession(Long sessionID) {
	List list = submissionDetailsDAO.getSubmissionDetailsBySession(sessionID);
	Iterator iter = list.iterator();
	SubmissionDetails details;
	SubmitFilesReport report;
	SubmitFilesSession session = getSessionById(sessionID);

	SubmitFilesContent content = session.getContent();
	boolean notifyLearnersOnMarkRelease = getEventNotificationService().eventExists(SbmtConstants.TOOL_SIGNATURE,
		SbmtConstants.EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE, content.getContentID());
	Map<Integer, StringBuilder> notificationMessages = null;
	Object[] notificationMessageParameters = null;
	if (notifyLearnersOnMarkRelease) {
	    notificationMessages = new TreeMap<Integer, StringBuilder>();
	    notificationMessageParameters = new Object[3];
	}
	while (iter.hasNext()) {
	    details = (SubmissionDetails) iter.next();
	    report = details.getReport();
	    report.setDateMarksReleased(new Date());
	    if (notifyLearnersOnMarkRelease) {
		SubmitUser user = details.getLearner();
		StringBuilder notificationMessage = notificationMessages.get(user.getUserID().longValue());
		if (notificationMessage == null) {
		    notificationMessage = new StringBuilder();
		}
		notificationMessageParameters[0] = details.getFilePath();
		notificationMessageParameters[1] = details.getDateOfSubmission();
		notificationMessageParameters[2] = report.getMarks();
		notificationMessage
			.append(getLocalisedMessage("event.mark.release.mark", notificationMessageParameters));
		notificationMessages.put(user.getUserID(), notificationMessage);
	    }
	    submitFilesReportDAO.updateReport(report);
	}
	// current there is no false return
	if (notifyLearnersOnMarkRelease) {
	    notificationMessageParameters = new Object[1];
	    for (Integer userID : notificationMessages.keySet()) {
		notificationMessageParameters[0] = notificationMessages.get(userID).toString();
		getEventNotificationService().triggerForSingleUser(SbmtConstants.TOOL_SIGNATURE,
			SbmtConstants.EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE, content.getContentID(), userID,
			notificationMessageParameters);
	    }
	}

	// pushing outputs to gradebook
	List<SubmitUser> users = getUsersBySession(sessionID);

	if (users != null) {
	    for (SubmitUser user : users) {
		// send marks to gradebook where applicable
		sendMarksToGradebook(user, sessionID);
	    }
	}

	return true;
    }

    public void finishSubmission(Long sessionID, Integer userID) {
	SubmitUser learner = submitUserDAO.getLearner(sessionID, userID);
	learner.setFinished(true);
	submitUserDAO.saveOrUpdateUser(learner);
    }

    /**
     * Sends marks to gradebook
     * 
     * @param user
     * @param toolSessionID
     */
    @SuppressWarnings("unchecked")
    public void sendMarksToGradebook(SubmitUser user, Long toolSessionID) {
	submissionDetailsDAO.getSubmissionDetailsBySession(toolSessionID);
	List<SubmissionDetails> detailsList = submissionDetailsDAO.getBySessionAndLearner(toolSessionID, user
		.getUserID());
	if (detailsList != null) {
	    Float totalMark = null;
	    for (SubmissionDetails details : detailsList) {
		SubmitFilesReport report = details.getReport();
		if (report != null) {
		    if (totalMark == null) {
			totalMark = details.getReport().getMarks();
		    } else if (report.getMarks() != null) {
			totalMark += report.getMarks();
		    }
		}

	    }
	    if (totalMark != null) {
		Double mark = new Double(totalMark);
		gradebookService.updateActivityMark(mark, null, user.getUserID(), toolSessionID, false);
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getToolDefaultContentIdBySignature(java.lang.Long)
     */
    public Long getToolDefaultContentIdBySignature(String toolSignature) {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    SubmitFilesService.log.error(error);
	    throw new SubmitFilesException(error);
	}
	return contentId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#createDefaultContent(java.lang.Long)
     */
    public SubmitFilesContent createDefaultContent(Long contentID) {
	if (contentID == null) {
	    String error = "Could not retrieve default content id for this tool";
	    SubmitFilesService.log.error(error);
	    throw new SubmitFilesException(error);
	}
	SubmitFilesContent defaultContent = getDefaultSubmit();

	// save default content by given ID.
	SubmitFilesContent content = new SubmitFilesContent();
	content = SubmitFilesContent.newInstance(defaultContent, contentID);
	content.setContentID(contentID);

	return content;
    }

    private SubmitFilesContent getDefaultSubmit() {
	Long defaultToolContentId = getToolDefaultContentIdBySignature(SbmtConstants.TOOL_SIGNATURE);
	SubmitFilesContent defaultContent = getSubmitFilesContent(defaultToolContentId);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    SubmitFilesService.log.error(error);
	    throw new SubmitFilesException(error);
	}
	return defaultContent;
    }

    public List getSubmitFilesSessionByContentID(Long contentID) {
	List learners = submitFilesSessionDAO.getSubmitFilesSessionByContentID(contentID);
	if (learners == null) {
	    learners = new ArrayList(); // return sized 0 list rather than null value
	}
	return learners;
    }
    
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	SubmitFilesContent toolContentObj = new SubmitFilesContent();

	toolContentObj.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	toolContentObj.setContentID(toolContentId);
	toolContentObj.setContentInUse(Boolean.FALSE);
	toolContentObj.setCreated(now);
	toolContentObj.setDefineLater(Boolean.FALSE);
	toolContentObj.setInstruction(WebUtil.convertNewlines((String) importValues
		.get(ToolContentImport102Manager.CONTENT_BODY)));
	toolContentObj.setUpdated(now);
	// 1.0.2 doesn't allow users to go back after completion, which is the equivalent of lock on finish.
	toolContentObj.setLockOnFinished(Boolean.TRUE);
	toolContentObj.setReflectOnActivity(Boolean.FALSE);
	toolContentObj.setReflectInstructions(null);

	SubmitUser suser = createContentUser(user, toolContentId);
	toolContentObj.setCreatedBy(suser);

	// leave as empty, no need to set them to anything.
	// toolContentObj.setInstructionFiles(attachments);

	submitFilesContentDAO.saveOrUpdate(toolContentObj);
    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	SubmitFilesContent toolContentObj = getSubmitFilesContent(toolContentId);
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	toolContentObj.setReflectOnActivity(Boolean.TRUE);
	toolContentObj.setReflectInstructions(description);
    }

    public SubmitUser getUserByUid(Long learnerID) {
	return (SubmitUser) submitUserDAO.find(SubmitUser.class, learnerID);

    }

    public SubmitUser createSessionUser(UserDTO userDto, Long sessionID) {
	SubmitUser learner = submitUserDAO.getLearner(sessionID, userDto.getUserID());
	if (learner != null) {
	    return learner;
	}
	learner = new SubmitUser();
	learner.setUserID(userDto.getUserID());
	learner.setFirstName(userDto.getFirstName());
	learner.setLastName(userDto.getLastName());
	learner.setLogin(userDto.getLogin());
	learner.setSessionID(sessionID);
	learner.setFinished(false);

	submitUserDAO.saveOrUpdateUser(learner);

	return learner;
    }

    public SubmitUser getSessionUser(Long sessionID, Integer userID) {
	return submitUserDAO.getLearner(sessionID, userID);
    }

    public SubmitUser createContentUser(UserDTO userDto, Long contentId) {
	SubmitUser learner = submitUserDAO.getContentUser(contentId, userDto.getUserID());
	if (learner != null) {
	    return learner;
	}
	learner = new SubmitUser();
	learner.setUserID(userDto.getUserID());
	learner.setFirstName(userDto.getFirstName());
	learner.setLastName(userDto.getLastName());
	learner.setLogin(userDto.getLogin());
	learner.setContentID(contentId);
	learner.setFinished(false);

	submitUserDAO.saveOrUpdateUser(learner);
	return learner;

    }

    public SubmitUser getContentUser(Long contentId, Integer userID) {
	return submitUserDAO.getContentUser(contentId, userID);
    }

    /*******************************************************************************************************************
     * Property Injection Methods
     ******************************************************************************************************************/

    /**
     * @param submitFilesContentDAO
     *                The submitFilesContentDAO to set.
     */
    public void setSubmitFilesContentDAO(ISubmitFilesContentDAO submitFilesContentDAO) {
	this.submitFilesContentDAO = submitFilesContentDAO;
    }

    /**
     * @param submitFilesReportDAO
     *                The submitFilesReportDAO to set.
     */
    public void setSubmitFilesReportDAO(ISubmitFilesReportDAO submitFilesReportDAO) {
	this.submitFilesReportDAO = submitFilesReportDAO;
    }

    /**
     * @param submitFilesSessionDAO
     *                The submitFilesSessionDAO to set.
     */
    public void setSubmitFilesSessionDAO(ISubmitFilesSessionDAO submitFilesSessionDAO) {
	this.submitFilesSessionDAO = submitFilesSessionDAO;
    }

    /**
     * @param submissionDetailsDAO
     *                The submissionDetailsDAO to set.
     */
    public void setSubmissionDetailsDAO(ISubmissionDetailsDAO submissionDetailsDAO) {
	this.submissionDetailsDAO = submissionDetailsDAO;
    }

    /**
     * @return Returns the sbmtToolContentHandler.
     */
    public IToolContentHandler getSbmtToolContentHandler() {
	return sbmtToolContentHandler;
    }

    /**
     * @param sbmtToolContentHandler
     *                The sbmtToolContentHandler to set.
     */
    public void setSbmtToolContentHandler(IToolContentHandler sbmtToolContentHandler) {
	this.sbmtToolContentHandler = sbmtToolContentHandler;
    }

    /**
     * @return Returns the learnerDAO.
     */
    public ISubmitUserDAO getSubmitUserDAO() {
	return submitUserDAO;
    }

    /**
     * @param learnerDAO
     *                The learnerDAO to set.
     */
    public void setSubmitUserDAO(ISubmitUserDAO learnerDAO) {
	submitUserDAO = learnerDAO;
    }

    public ILearnerService getLearnerService() {
	return learnerService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public IRepositoryService getRepositoryService() {
	return repositoryService;
    }

    public void setRepositoryService(IRepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
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

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return getLessonService().getMonitorsByToolSessionId(sessionId);
    }

    public ILessonService getLessonService() {
	return lessonService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public void setGradebookService(IGradebookService gradebookService) {
	this.gradebookService = gradebookService;
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getSubmitFilesOutputFactory().getSupportedDefinitionClasses(definitionType);

    }

    public SubmitFilesOutputFactory getSubmitFilesOutputFactory() {
	return submitFilesOutputFactory;
    }

    public void setSubmitFilesOutputFactory(SubmitFilesOutputFactory submitFilesOutputFactory) {
	this.submitFilesOutputFactory = submitFilesOutputFactory;
    }
}