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
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

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
import org.lamsfoundation.lams.tool.sbmt.dto.LearnerDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.StatusReportDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO;
import org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.wddx.FlashMessage;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesService implements ToolContentManager,
		ToolSessionManager, ISubmitFilesService {

	private ISubmitFilesContentDAO submitFilesContentDAO;

	private ISubmitFilesReportDAO submitFilesReportDAO;

	private ISubmitFilesSessionDAO submitFilesSessionDAO;
	
	private ISubmissionDetailsDAO submissionDetailsDAO;
	
	private IUserDAO userDAO;

	private IRepositoryService repositoryService;

	private FlashMessage flashMessage;

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
		SubmitFilesContent toContent = SubmitFilesContent.newInstance(
				fromContent, toContentId);
		submitFilesContentDAO.insert(toContent);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.ToolContentManager#setAsDefineLater(java.lang.Long)
	 */
	public void setAsDefineLater(Long toolContentId) {
		// TODO Auto-generated method stub

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.lamsfoundation.lams.tool.ToolContentManager#setAsRunOffline(java.lang.Long)
	 */
	public void setAsRunOffline(Long toolContentId) {
		// TODO Auto-generated method stub

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
		submitFilesContentDAO.insert(submitFilesContent);
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
	 * @param contentID
	 *            The <code>contentID</code> of the file being uploaded
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
	private void uploadFile(InputStream stream, Long contentID, String filePath,
							String fileDescription, String fileName, String mimeType,
							Date dateOfSubmission, Long userID) throws SubmitFilesException {

		SubmitFilesContent content = submitFilesContentDAO.getContentByID(contentID);
		if (content == null)
			throw new SubmitFilesException(
					"No such content with a contentID of: " + contentID
							+ " found.");
		else {
			NodeKey nodeKey = uploadFileToRepository(stream, fileName, mimeType);
			SubmissionDetails details = new SubmissionDetails(filePath,fileDescription,dateOfSubmission,
															  nodeKey.getUuid(),nodeKey.getVersion(),
															  userID,content);
			submissionDetailsDAO.insert(details);
			submitFilesReportDAO.insert(new SubmitFilesReport(details));
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
		// TODO Auto-generated method stub

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
	public void uploadFile(Long contentID, String filePath,
						   String fileDescription, Long userID) throws SubmitFilesException{
		SubmitFilesContent submitFilesContent = submitFilesContentDAO.getContentByID(contentID);
		try{
			File file = new File(filePath);
			String fileName = file.getName();
			String mimeType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
			FileInputStream stream = new FileInputStream(file);
			uploadFile(stream,contentID,filePath,fileDescription,fileName,mimeType,new Date(),userID);			
		}catch(FileNotFoundException fe){
			throw new SubmitFilesException("FileNotFoundException occured while trying to upload File" + fe.getMessage());
		}

	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getFilesUploadedByUserForContent(java.lang.Long, java.lang.Long)
	 */
	public List getFilesUploadedByUser(Long userID, Long contentID){
		List list =  submissionDetailsDAO.getSubmissionDetailsForUserByContent(userID,contentID);
		if(list!=null)
			return getDetails(list.iterator());			
		else
			return null;
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
			FileDetailsDTO reportDTO = new FileDetailsDTO(submissionDetails,report);
			details.add(reportDTO);
		}
		return details;
	}
	public FileDetailsDTO getFileDetails(Long reportID){
		SubmitFilesReport report = submitFilesReportDAO.getReportByID(reportID);
		if(report!=null){
			SubmissionDetails details = report.getSubmissionDetails();
			return new FileDetailsDTO(details,report);			
		}else
			return null;
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#generateReport(java.lang.Long)
	 */
	public Hashtable generateReport(Long contentID){
		List users = submissionDetailsDAO.getUsersForContent(contentID);
		Iterator iterator = users.iterator();
		Hashtable table = new Hashtable();
		while(iterator.hasNext()){
			Long userID = (Long)iterator.next();			
			User user = userDAO.getUserById(new Integer(userID.intValue()));
			List userDetails = submissionDetailsDAO.getSubmissionDetailsForUserByContent(userID,contentID);
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
		Integer userID = new Integer(details.getUserID().intValue());		
		File file = new File(details.getFilePath());		
		return new LearnerDetailsDTO(file.getName(),
									 details.getFileDescription(),
									 details.getDateOfSubmission(),
									 report.getComments(),
									 report.getMarks(),
									 report.getDateMarksReleased());
	}	
	
	public ArrayList getStatus(Long contentID){		
		ArrayList details = new ArrayList();
		List users = submissionDetailsDAO.getUsersForContent(contentID);
		Iterator iterator = users.iterator();
		while(iterator.hasNext()){
			Long userID = (Long)iterator.next();
			List allFiles = submissionDetailsDAO.getSubmissionDetailsForUserByContent(userID,contentID);			
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
	public InputStream downloadFile(Long uuid, Long versionID)throws SubmitFilesException{
		ITicket ticket = getRepositoryLoginTicket();		
		try{
			IVersionedNode node = repositoryService.getFileItem(ticket,uuid,null);
			return node.getFile();
		}catch(AccessDeniedException ae){
			throw new SubmitFilesException("AccessDeniedException occured while trying to download file " + ae.getMessage());
		}catch(FileException fe){
			throw new SubmitFilesException("FileException occured while trying to download file " + fe.getMessage());
		}catch(ItemNotFoundException ie){
			throw new SubmitFilesException("ItemNotFoundException occured while trying to download file " + ie.getMessage());			
		}
	}
}