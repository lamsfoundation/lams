/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.tool.sbmt.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.LearnerDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.StatusReportDTO;
import org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.springframework.dao.DataAccessException;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesService implements ToolContentManager,
		ToolSessionManager, ISubmitFilesService {

	private static Logger log = Logger.getLogger(SubmitFilesService.class);
	
	private ISubmitFilesContentDAO submitFilesContentDAO;

	private ISubmitFilesReportDAO submitFilesReportDAO;

	private ISubmitFilesSessionDAO submitFilesSessionDAO;
	
	private ISubmissionDetailsDAO submissionDetailsDAO;
	
	private IUserDAO userDAO;

	private IRepositoryService repositoryService;

	/***************************************************************************
	 * Property Injection Methods
	 **************************************************************************/

	/**
	 * @param submitFilesContentDAO
	 *            The submitFilesContentDAO to set.
	 */
	public void setSubmitFilesContentDAO(
			ISubmitFilesContentDAO submitFilesContentDAO) {
		this.submitFilesContentDAO = submitFilesContentDAO;
	}

	/**
	 * @param submitFilesReportDAO
	 *            The submitFilesReportDAO to set.
	 */
	public void setSubmitFilesReportDAO(
			ISubmitFilesReportDAO submitFilesReportDAO) {
		this.submitFilesReportDAO = submitFilesReportDAO;
	}

	/**
	 * @param submitFilesSessionDAO
	 *            The submitFilesSessionDAO to set.
	 */
	public void setSubmitFilesSessionDAO(
			ISubmitFilesSessionDAO submitFilesSessionDAO) {
		this.submitFilesSessionDAO = submitFilesSessionDAO;
	}
	
	/**
	 * @param submissionDetailsDAO The submissionDetailsDAO to set.
	 */
	public void setSubmissionDetailsDAO(
			ISubmissionDetailsDAO submissionDetailsDAO) {
		this.submissionDetailsDAO = submissionDetailsDAO;
	}
	
	/**
	 * @param userDAO The userDAO to set.
	 */
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}
	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.ToolContentManager#copyToolContent(java.lang.Long,
	 *      java.lang.Long)
	 */
	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	    // TODO fix this to use the default content id - shouldn't throw the exception
		if (fromContentId == null || toContentId == null)
			throw new ToolException(
					"Failed to create the SubmitFiles tool seession");

		SubmitFilesContent fromContent = submitFilesContentDAO
				.getContentByID(fromContentId);
		SubmitFilesContent toContent = (SubmitFilesContent) fromContent.clone();
		//reset some new attributes for toContent
		toContent.setContentID(toContentId);
		toContent.setToolSession(new TreeSet());
		
		submitFilesContentDAO.insert(toContent);
	}
    /**
     * @see org.lamsfoundation.lams.tool.ToolContentManager#setAsRunOffline(java.lang.Long)
     */
    public void setAsRunOffline(Long toolContentId)
    {
        //pre-condition validation
        if (toolContentId == null)
            throw new SubmitFilesException("Fail to set tool content to run offline - "
                    + " based on null toolContentId");
        try
        {
            SubmitFilesContent content = submitFilesContentDAO.getContentByID(toolContentId);
            if ( content == null ) {
                content = duplicateDefaultToolContent(toolContentId);
            }
            content.setRunOffline(true);
            submitFilesContentDAO.save(content);
        }
        catch (DataAccessException e)
        {
            throw new SubmitFilesException("Exception occured when LAMS is setting content to run offline"
                                                  + e.getMessage(),e);
        }
    }
    
	/**
	 * @param toolContentId
	 * @return
	 */
	private SubmitFilesContent duplicateDefaultToolContent(Long toolContentId) {
		//TODO
		return null;
	}

	/**
     * @see org.lamsfoundation.lams.tool.ToolContentManager#setAsDefineLater(java.lang.Long)
     */
    public void setAsDefineLater(Long toolContentId) {
        //pre-condition validation
        if (toolContentId == null)
            throw new SubmitFilesException("Fail to set tool content to define later - "
                    + " based on null toolContentId");
        try
        {
        	SubmitFilesContent content = submitFilesContentDAO.getContentByID(toolContentId);
            if ( content == null ) {
                content = duplicateDefaultToolContent(toolContentId);
            }
            content.setDefineLater(true);
            submitFilesContentDAO.save(content);
        }
        catch (DataAccessException e)
        {
            throw new SubmitFilesException("Exception occured when LAMS is setting content to run define later"
                                                  + e.getMessage(),e);
        }
       
    }

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.ToolContentManager#removeToolContent(java.lang.Long)
	 */
	public void removeToolContent(Long toolContentId, boolean removeSessionData)throws DataMissingException {
		SubmitFilesContent submitFilesContent = submitFilesContentDAO
				.getContentByID(toolContentId);
		if (submitFilesContent == null)
			throw new DataMissingException(
					"No such content with a contentID of : " + toolContentId
							+ " exists");
		else {
			List filesUploaded = submissionDetailsDAO.getSubmissionDetailsByContentID(toolContentId);
			if(filesUploaded!=null){
				Iterator fileIterator = filesUploaded.iterator();				
				while(fileIterator.hasNext()){
					SubmissionDetails details = (SubmissionDetails)fileIterator.next();
					deleteFromRepository(details.getUuid(),details.getVersionID());
					submissionDetailsDAO.delete(details);
				}
				submitFilesContentDAO.delete(submitFilesContent);
			}			
		}
		//TODO check for related session data and delete as appropriate
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#addSubmitFilesContent(java.lang.Long,
	 *      java.lang.String, java.lang.String)
	 */
	public void addSubmitFilesContent(Long contentID, String title,
			String instructions) {
		SubmitFilesContent submitFilesContent = new SubmitFilesContent(
				contentID, title, instructions);
		
		submitFilesContentDAO.save(submitFilesContent);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#updateSubmitFilesContent(org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent)
	 */
	public void updateSubmitFilesContent(SubmitFilesContent submitFilesContent) {
		submitFilesContentDAO.update(submitFilesContent);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getSubmitFilesContent(java.lang.Long)
	 */
	public SubmitFilesContent getSubmitFilesContent(Long contentID) {
		return submitFilesContentDAO.getContentByID(contentID);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getSubmitFilesReport(java.lang.Long)
	 */
	public SubmitFilesReport getSubmitFilesReport(Long reportID) {
		return submitFilesReportDAO.getReportByID(reportID);
	}	

	/**
	 * This method verifies the credentials of the SubmitFiles Tool and gives it
	 * the <code>Ticket</code> to login and access the Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the
	 * repository. This method would be called evertime the tool needs to
	 * upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws SubmitFilesException
	 */
	private ITicket getRepositoryLoginTicket() throws SubmitFilesException {
		repositoryService = RepositoryProxy.getLocalRepositoryService();
		ICredentials credentials = new SimpleCredentials(
				ISubmitFilesService.SBMT_LOGIN,
				ISubmitFilesService.SBMT_PASSWORD.toCharArray());
		try {
			ITicket ticket = repositoryService.login(credentials,
					ISubmitFilesService.SBMT_WORKSPACE);
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new SubmitFilesException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new SubmitFilesException("Workspace not found."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new SubmitFilesException("Login failed." + e.getMessage());
		}
	}

	/**
	 * This method deletes the content with the given <code>uuid</code> and
	 * <code>versionID</code> from the content repository
	 * 
	 * @param uuid
	 *            The <code>uuid</code> of the node to be deleted
	 * @param versionID
	 *            The <code>version_id</code> of the node to be deleted.
	 * @throws SubmitFilesException
	 */
	public void deleteFromRepository(Long uuid, Long versionID)
			throws SubmitFilesException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			String files[] = repositoryService.deleteVersion(ticket, uuid,versionID);
		} catch (Exception e) {
			throw new SubmitFilesException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}

	/**
	 * This method is called when the user requests to upload a file. It's a
	 * three step process.
	 * <ol>
	 * <li>Firstly, the tool authenticates itself and obtains a valid ticket
	 * from the respository</li>
	 * <li>Secondly, using the above ticket it uploads the file to the
	 * repository. Upon successful uploading of the file, repository returns a
	 * <code>NodeKey</code> object which is a unique indentifier of the file
	 * in the repsoitory</li>
	 * <li>Finally, this information is updated into the database for the given
	 * contentID in the following tables
	 * 	<ul>
	 * 		<li><code>tl_lasbmt11_submission_details</code></li>
	 * 		<li><code>tl_lasbmt11_report</code></li>
	 * 	</ul>
	 * </li>
	 * </ol>
	 * 
	 * @param stream
	 *            The <code>InputStream</code> representing the data to be
	 *            uploaded
	 * @param sessionID
	 *            The <code>ToolSessionID</code> of the file being uploaded
	 * @param filePath
	 *            The physical path of the file
	 * @param fileDescription
	 *            The description of the file
	 * @param fileName
	 *            The name of the file being added
	 * @param mimeType
	 *            The MIME type of the file (eg. TXT, DOC, GIF etc)
	 * @param dateOfSubmission
	 *            The date this file was uploaded by the user
	 * @param userID
	 * 			  The <code>User</code> who has uploaded the file.
	 * @throws SubmitFilesException
	 */
	private void uploadFile(InputStream stream, Long sessionID, String filePath,
							String fileDescription, String fileName, String mimeType,
							Date dateOfSubmission, Long userID) throws SubmitFilesException {

		SubmitFilesSession session = submitFilesSessionDAO.getSessionByID(sessionID);
		if (session == null)
			throw new SubmitFilesException(
					"No such session with a ToolSessionID of: " + sessionID
							+ " found.");
		else {
			NodeKey nodeKey = uploadFileToRepository(stream, fileName, mimeType);
			SubmissionDetails details = new SubmissionDetails(filePath,fileDescription,dateOfSubmission,
															  userID,nodeKey.getUuid(),nodeKey.getVersion());
			SubmitFilesReport report = new SubmitFilesReport();
			details.setReport(report);
			details.setSubmitFileSession(session);
			//update session, then insert the detail too.
			Set detailSet = session.getSubmissionDetails();
			detailSet.add(details);
			session.setSubmissionDetails(detailSet);
			submissionDetailsDAO.saveOrUpdate(session);
		}
	}	

	/**
	 * This method is called everytime a new content has to be added to the
	 * repository. In order to do so first of all a valid ticket is obtained
	 * from the Repository hence authenticating the tool(SubmitFiles) and then
	 * the corresponding file is added to the repository.
	 * 
	 * @param stream
	 *            The <code>InputStream</code> representing the data to be
	 *            added
	 * @param fileName
	 *            The name of the file being added
	 * @param mimeType
	 *            The MIME type of the file (eg. TXT, DOC, GIF etc)
	 * @return NodeKey Represents the two part key - UUID and Version.
	 * @throws SubmitFilesException
	 */
	public NodeKey uploadFileToRepository(InputStream stream, String fileName,
			String mimeType) throws SubmitFilesException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			NodeKey nodeKey = repositoryService.addFileItem(ticket, stream,
					fileName, mimeType, null);
			return nodeKey;
		} catch (Exception e) {
			throw new SubmitFilesException("Exception occured while trying to"
					+ " upload file into the repository" + e.getMessage());
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#createToolSession(java.lang.Long,
	 *      java.lang.Long)
	 */
	public void createToolSession(Long toolSessionId, Long toolContentId) {
        //pre-condition validation
        if (toolSessionId == null || toolContentId == null)
            throw new SubmitFilesException("Fail to create a submission session"
                    + " based on null toolSessionId or toolContentId");

        log.debug("Start to create submission session based on toolSessionId["
                + toolSessionId.longValue() + "] and toolContentId["
                + toolContentId.longValue() + "]");
        try
        {
            SubmitFilesContent submitContent = submitFilesContentDAO.getContentByID(toolContentId);

            SubmitFilesSession submitSession = new SubmitFilesSession (toolSessionId,
            												SubmitFilesSession.INCOMPLETE);
                                                            
            submitFilesSessionDAO.createSession(submitSession);
            log.debug("Survey session created");
        }
        catch (DataAccessException e)
        {
            throw new SubmitFilesException("Exception occured when lams is creating"
                                                         + " a submission Session: "
                                                         + e.getMessage(),e);
        }

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#leaveToolSession(java.lang.Long,
	 *      org.lamsfoundation.lams.usermanagement.User)
	 */
	public String leaveToolSession(Long toolSessionId, User learner) {
		// TODO Auto-generated method stub
		return null;
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
	public void removeToolSession(Long toolSessionId)
	   throws DataMissingException, ToolException{
			// TODO Auto-generated method stub
			return;
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#uploadFile(java.lang.Long,
	 *      java.lang.String, java.lang.String)
	 */
	public void uploadFile(Long sessionID, String filePath,
						   String fileDescription, Long userID) throws SubmitFilesException{
		try{
			File file = new File(filePath);
			String fileName = file.getName();
			String mimeType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
			FileInputStream stream = new FileInputStream(file);
			uploadFile(stream,sessionID,filePath,fileDescription,fileName,mimeType,new Date(),userID);			
		}catch(FileNotFoundException fe){
			throw new SubmitFilesException("FileNotFoundException occured while trying to upload File" + fe.getMessage());
		}

	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getFilesUploadedByUserForContent(java.lang.Long, java.lang.Long)
	 */
	public List getFilesUploadedByUser(Long userID, Long sessionID){
		List list =  submissionDetailsDAO.getSubmissionDetailsForUserBySession(userID,sessionID);
		if(list!=null)
			return getDetails(list.iterator());			
		else
			return null;
	}
	public Map getFilesUploadedBySession(Long sessionID) {
		List list =  submissionDetailsDAO.getSubmissionDetailsBySession(sessionID);
		if(list!=null){
			return getUserFileDetailsMap(list);
		}
		else
			return null;
	}
	/**
	 * This method save SubmissionDetails list into a map container: key is user id,
	 * value is a list container, which contains all <code>FileDetailsDTO</code> object belong to
	 * this user.
	 * 
	 * @param list
	 * @return
	 */
	private Map getUserFileDetailsMap(List list) {
		Map map = new HashMap();
		Iterator iterator = list.iterator();
		List element;
		while(iterator.hasNext()){
			SubmissionDetails submissionDetails = (SubmissionDetails)iterator.next();
			SubmitFilesReport report = submissionDetails.getReport();
			UserDTO user = getUserDetails(submissionDetails.getUserID());
			
			FileDetailsDTO detailDto = new FileDetailsDTO(submissionDetails,report,user);
			element = (List) map.get(submissionDetails.getUserID());
			//if it is first time to this user, creating a new ArrayList for this user.
			if(element == null)
				element = new ArrayList();
			element.add(detailDto);
			map.put(submissionDetails.getUserID(),element);
		}
		return map;
	}

	/** 
	 * This is a utility method used by <code>getFilesUploadedByUser</code>
	 * method to generate a list of data transfer objects containing the
	 * details of files uploaded by a given user. 
	 * 
	 * It combines the submission details and the reporting deatils 
	 * to generate a generic DTO tailored according to the 
	 * application requirements.
	 * 
	 * @param iterator
	 * @return ArrayList 
	 */
	private ArrayList getDetails(Iterator iterator){
		ArrayList details = new ArrayList();
		while(iterator.hasNext()){
			SubmissionDetails submissionDetails = (SubmissionDetails)iterator.next();
			SubmitFilesReport report = submissionDetails.getReport();
			FileDetailsDTO detailDto = new FileDetailsDTO(submissionDetails,report);
			details.add(detailDto);
		}
		return details;
	}
	public FileDetailsDTO getFileDetails(Long detailID){
			SubmissionDetails details = submissionDetailsDAO.getSubmissionDetailsByID(detailID);
			return new FileDetailsDTO(details);			
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getUsers(java.lang.Long)
	 */
	public List getUsers(Long sessionID){
		List users = submissionDetailsDAO.getUsersForSession(sessionID);
		Iterator iterator = users.iterator();
		List table = new ArrayList();
		while(iterator.hasNext()){
			Long userID = (Long)iterator.next();			
			User user = userDAO.getUserById(new Integer(userID.intValue()));
			table.add(user.getUserDTO());
		}
		return table;
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#generateReport(java.lang.Long)
	 */
	public Hashtable generateReport(Long sessionID){
		List users = submissionDetailsDAO.getUsersForSession(sessionID);
		Iterator iterator = users.iterator();
		Hashtable table = new Hashtable();
		while(iterator.hasNext()){
			Long userID = (Long)iterator.next();			
			User user = userDAO.getUserById(new Integer(userID.intValue()));
			List userDetails = submissionDetailsDAO.getSubmissionDetailsForUserBySession(userID,sessionID);
			table.put(user.getUserDTO(),getUserDetails(userDetails.iterator()));
		}
		return table;
	}
	/**
	 * Utility function used by <code>generateReport</code> method
	 * above to assist in generating the report.
	 * 
	 * @param iterator
	 * @return ArrayList
	 */
	private ArrayList getUserDetails(Iterator iterator){
		ArrayList details = new ArrayList();
		while(iterator.hasNext()){
			SubmissionDetails submissionDetails = (SubmissionDetails)iterator.next();		
			details.add(getLearnerDetailsDTO(submissionDetails,
											 submissionDetails.getReport()));
		}
		return details;		
	}
	/**
	 * Utility function used by <code>getUserDetails</code> method
	 * above to assist in generating the report.
	 * 
	 * @param details
	 * @param report
	 * @return LearnerDetailsDTO
	 */
	private LearnerDetailsDTO getLearnerDetailsDTO(SubmissionDetails details, 
												  SubmitFilesReport report){
		File file = new File(details.getFilePath());		
		return new LearnerDetailsDTO(file.getName(),
									 details.getFileDescription(),
									 details.getDateOfSubmission(),
									 report.getComments(),
									 report.getMarks(),
									 report.getDateMarksReleased());
	}	
	
	public ArrayList getStatus(Long sessionID){		
		ArrayList details = new ArrayList();
		List users = submissionDetailsDAO.getUsersForSession(sessionID);
		Iterator iterator = users.iterator();
		while(iterator.hasNext()){
			Long userID = (Long)iterator.next();
			List allFiles = submissionDetailsDAO.getSubmissionDetailsForUserBySession(userID,sessionID);			
			boolean unmarked = hasUnmarkedContent(allFiles.iterator());
			details.add(getStatusDetails(userID,unmarked));
		}
		return details;		
	}
	private boolean hasUnmarkedContent(Iterator details){
		boolean unmarked = false; 
		while(details.hasNext()){
			SubmissionDetails submissionDetails = (SubmissionDetails)details.next();
			if(submissionDetails.getReport().getMarks()==null)
				return true;
		}
		return unmarked;
	}	
	private StatusReportDTO getStatusDetails(Long userID,boolean unmarked){
		User user = userDAO.getUserById(new Integer(userID.intValue()));
		return new StatusReportDTO(user.getUserId(),
								   user.getLogin(),
								   user.getFullName(),
								   new Boolean(unmarked));
	}
	public void updateMarks(Long reportID, Long marks, String comments){
		SubmitFilesReport report = submitFilesReportDAO.getReportByID(reportID);
		if(report!=null){
			report.setComments(comments);
			report.setMarks(marks);
			submitFilesReportDAO.update(report);
		}
	}
	public UserDTO getUserDetails(Long userID){
		User user = userDAO.getUserById(new Integer(userID.intValue()));
		return user.getUserDTO();
	}
	public IVersionedNode downloadFile(Long uuid, Long versionID)throws SubmitFilesException{
		ITicket ticket = getRepositoryLoginTicket();		
		try{
			IVersionedNode node = repositoryService.getFileItem(ticket,uuid,null);
			return node;
		}catch(AccessDeniedException ae){
			throw new SubmitFilesException("AccessDeniedException occured while trying to download file " + ae.getMessage());
		}catch(FileException fe){
			throw new SubmitFilesException("FileException occured while trying to download file " + fe.getMessage());
		}catch(ItemNotFoundException ie){
			throw new SubmitFilesException("ItemNotFoundException occured while trying to download file " + ie.getMessage());			
		}
	}
	public SubmitFilesSession getSessionById(Long sessionID){
		return submitFilesSessionDAO.getSessionByID(sessionID);
	}

	public boolean releaseMarksForSession(Long sessionID){
		List list = submissionDetailsDAO.getSubmissionDetailsBySession(sessionID);
		Iterator iter = list.iterator();
		SubmissionDetails details;
		SubmitFilesReport report;
		Date now = Calendar.getInstance().getTime();
		while(iter.hasNext()){
			details = (SubmissionDetails) iter.next();
			report = details.getReport();
			report.setDateMarksReleased(now);
			submitFilesReportDAO.updateReport(report);
		}
		//current there is no false return
		return true;
	}





}