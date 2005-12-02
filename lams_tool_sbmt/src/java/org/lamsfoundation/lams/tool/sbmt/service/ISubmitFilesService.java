/**
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.sbmt.service;

import java.util.List;
import java.util.Map;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.tool.sbmt.InstructionFiles;
import org.lamsfoundation.lams.tool.sbmt.Learner;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * @author Manpreet Minhas
 */
public interface ISubmitFilesService {

	/**
	 * Returns the <code>SubmitFilesContent</code> object corresponding to the
	 * given <code>contentID</code>. If could not find out corresponding 
	 * <code>SubmitFilesContent</code> by given <code>contentID</code>, return a not-null
	 * but emtpy <code>SubmitFilesContent</code> instance.
	 * 
	 * @param contentID
	 *            The <code>content_id</code> of the object to be looked up
	 * @return SubmitFilesContent The required populated object
	 */
	public SubmitFilesContent getSubmitFilesContent(Long contentID);

	/**
	 * 
	 * Returns the <code>SubmitFilesReport</code> object corresponding to the
	 * given <code>reportID</code>
	 * 
	 * @param reportID
	 * @return SubmitFilesReport The required populated object
	 */
	public SubmitFilesReport getSubmitFilesReport(Long reportID);

	/**
	 * This method uploads a file with the given name and description. It's a
	 * two step process
	 * <ol>
	 * <li>It first uploads the file to the content repository</li>
	 * <li>And then it updates the database</li>
	 * </ol>
	 * @param fileDescription
	 *            The description of the file being uploaded.
	 * @param userID
	 * 			  The <code>User</code> who has uploaded the file.
	 * @param contentID
	 *            The content_id of the record to be updated in the database
	 * @param uploadedFile
	 *            The STRUTS org.apache.struts.upload.FormFile type
	 * 
	 * @throws SubmitFilesException
	 */
	public void uploadFileToSession(Long sessionID, FormFile uploadFile,
			   String fileDescription, Long userID) throws SubmitFilesException;
	/**
	 * Upload file to repository and persist relative attributes into database.
	 *   
	 * @param contentID
	 * @param uploadFile
	 * @param fileType
	 * @return If successs, return an instance of <code>InstructionFile</code>. Otherwise, return null.
	 * @throws SubmitFilesException
	 */
	public InstructionFiles uploadFileToContent(Long contentID, FormFile uploadFile, String fileType) throws SubmitFilesException;
	/**
	 * This method returns a list of files that were uploaded by the
	 * given <code>User<code> for given <code>contentID</code>.
	 * 
	 * This method is used in the learning enviornment for displaying 
	 * the files being uploaded by the given user, as the user 
	 * uploads them one by one.
	 * 
	 * @param userID The <code>user_id</code> of the <code>User</code>
	 * @param sessionID The <code>session_id</code> to be looked up
	 * @return List The list of required objects.
	 */
	public List getFilesUploadedByUser(Long userID, Long sessionID);
	/**
	 * This method returns a list of files that were uploaded by the
	 * given <code>contentID</code>.
	 * 
	 * This method is used in the authoring enviornment for displaying 
	 * the files being uploaded by the given session, as the user 
	 * uploads them one by one.
	 * 
	 * @param sessionID The <code>session_id</code> to be looked up
	 * @return List The list of required objects.
	 * @param sessionID
	 * @return
	 */
	public Map getFilesUploadedBySession(Long sessionID);

	/**
	 * Get information of all users who have submitted file.
	 * @return The user information list
	 */
	public List getUsers(Long contentID);
	
	public void updateMarks(Long reportID, Long marks, String comments);
	
	public UserDTO getUserDetails(Long userID);
	
	public FileDetailsDTO getFileDetails(Long detailID);
	/**
	 * Get SubmitFilesSession instance according to the given session id.
	 * @param sessionID
	 * @return
	 */
	public SubmitFilesSession getSessionById(Long sessionID);
	public IVersionedNode downloadFile(Long uuid, Long versionID);

	/**
	 * Release marks and comments information to learners, for a special session.
	 * @param sessionID
	 * @return success return true, otherwise return false.
	 */
	public boolean releaseMarksForSession(Long sessionID);

	public void deleteFromRepository(Long uuid, Long versionID);
	public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type);
	/**
	 * When learner finish submission, it invokes this function and will remark the <code>finished</code> field.
	 * 
	 * @param sessionID
	 * @param userID
	 */
	public void finishSubmission(Long sessionID, Long userID);
	/**
	 * Get learner by given <code>toolSessionID</code> and <code>userID</code>.
	 *  
	 * @param sessionID
	 * @param userID
	 * @return
	 */
	public Learner getLearner(Long sessionID, Long userID);
    /**
     * Create the default content for the given contentID. These default data will copy from default record in 
     * Tool Content database table.
     * 
     * @return
     * 		The SubmitFilesContent with default content and given contentID
     */
	public SubmitFilesContent createDefaultContent(Long contentID);
	
	
    /**
     * This method retrieves the default content id.
     * @param toolSignature The tool signature which is defined in lams_tool table.
     * @return the default content id
     */
    public Long getToolDefaultContentIdBySignature(String toolSignature);
    
    /**
     * This method retrieves a list of SubmitFileSession from the contentID.
     * @param contentID
     * @return a list of SubmitFileSession
     */
    //public List getSubmitFilesSessionsByContentID(Long contentID);
    public List getSubmitFilesSessionByContentID(Long contentID);
    /**
     * Save or update tool content into database.
     * @param persistContent
     * 			The <code>SubmitFilesContent</code> to be updated
     */
	public void saveOrUpdateContent(SubmitFilesContent persistContent);
}
