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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * @author Manpreet Minhas
 */
public interface ISubmitFilesService {

	public static final String SBMT_LOGIN = "SubmitFilesLogin";

	public static final String SBMT_PASSWORD = "SubmitFilesPassword";

	public static final String SBMT_WORKSPACE = "SubmitFilesWorkspace";

	/**
	 * This method adds a new content record to the database.
	 * 
	 * @param contentID
	 *            The <code>content_id</code> of the new record
	 * @param title
	 *            The title of the tool
	 * @param instructions
	 *            The instructions for working with that tool
	 */
	public void addSubmitFilesContent(Long contentID, String title,
			String instructions);

	/**
	 * Updates the record in the database
	 * 
	 * @param submitFilesContent
	 *            The <code>SubmitFilesContent</code> to be updated
	 */
	public void updateSubmitFilesContent(SubmitFilesContent submitFilesContent);

	/**
	 * Returns the <code>SubmitFilesContent</code> object corresponding to the
	 * given <code>contentID</code>
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
	 * 
	 * @param contentID
	 *            The content_id of the record to be updated in the database
	 * @param filePath
	 *            The physical location of the file from where it has to be
	 *            uploaded
	 * @param fileDescription
	 *            The description of the file being uploaded.
	 * @param userID
	 * 			  The <code>User</code> who has uploaded the file.
	 * @throws SubmitFilesException
	 */
	public void uploadFile(Long contentID, String filePath,
			String fileDescription, Long userID) throws SubmitFilesException;
	
	/**
	 * This method returns a list of files that were uploaded by the
	 * given <code>User<code> for given <code>contentID</code>.
	 * 
	 * This method is used in the learning enviornment for displaying 
	 * the files being uploaded by the given user, as the user 
	 * uploads them one by one.
	 * 
	 * @param userID The <code>user_id</code> of the <code>User</code>
	 * @param contentID The <code>content_id</code> to be looked up
	 * @return List The list of required objects.
	 */
	public List getFilesUploadedByUser(Long userID, Long contentID);
	/**
	 * Get information of all users who have submitted file.
	 * @return The user information list
	 */
	public List getUsers(Long contentID);
	/**
	 * This method is required in the monitoring enviornment
	 * when the teacher wants to view all the learners who have
	 * uploaded one file or the other for marking purposes.	 * 
	 *   
	 * @param contentID The content_id of the tool
	 * @return Hashtable The required information
	 */
	public Hashtable generateReport(Long contentID);
	
	public ArrayList getStatus(Long contentID);	
	
	public void updateMarks(Long reportID, Long marks, String comments);
	
	public UserDTO getUserDetails(Long userID);
	
	public FileDetailsDTO getFileDetails(Long reportID);
	
	public InputStream downloadFile(Long uuid, Long versionID);
}
