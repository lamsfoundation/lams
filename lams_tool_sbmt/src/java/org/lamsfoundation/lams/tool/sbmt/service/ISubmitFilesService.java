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

package org.lamsfoundation.lams.tool.sbmt.service;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;

import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SubmitUserDTO;
import org.lamsfoundation.lams.tool.sbmt.model.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.util.SubmitFilesException;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Manpreet Minhas
 */
public interface ISubmitFilesService extends ICommonToolService {

    /**
     * Returns the <code>SubmitFilesContent</code> object corresponding to the given <code>contentID</code>. If
     * could not find out corresponding <code>SubmitFilesContent</code> by given <code>contentID</code>, return a
     * not-null but emtpy <code>SubmitFilesContent</code> instance.
     *
     * @param contentID
     *            The <code>content_id</code> of the object to be looked up
     * @return SubmitFilesContent The required populated object
     */
    public SubmitFilesContent getSubmitFilesContent(Long contentID);

    /**
     *
     * Returns the <code>SubmitFilesReport</code> object corresponding to the given <code>reportID</code>
     *
     * @param reportID
     * @return SubmitFilesReport The required populated object
     */
    public SubmitFilesReport getSubmitFilesReport(Long reportID);

    /**
     * This method uploads a file with the given name and description. It's a two step process
     * <ol>
     * <li>It first uploads the file to the content repository</li>
     * <li>And then it updates the database</li>
     * </ol>
     *
     * @param fileDescription
     *            The description of the file being uploaded.
     * @param userID
     *            The <code>User</code> who has uploaded the file.
     * @param contentID
     *            The content_id of the record to be updated in the database
     * @param uploadedFile
     *            The STRUTS org.apache.struts.upload.FormFile type
     *
     * @throws SubmitFilesException
     */
    public void uploadFileToSession(Long sessionID, File file, String fileDescription, Integer userID)
	    throws SubmitFilesException;

    /**
     * Get a the details for a single file uploaded by a learner
     *
     * @param detailId
     * @return SubmissionDetails
     */
    public SubmissionDetails getSubmissionDetail(Long detailId);

    /**
     * This method returns a list of files that were uploaded by the given
     * <code>User<code> for given <code>contentID</code>.
     *
     * This method is used in the learning enviornment for displaying
     * the files being uploaded by the given user, as the user
     * uploads them one by one.
     *
     * @param userID
     *            The <code>user_id</code> of the <code>User</code>
     * @param sessionID
     *            The <code>session_id</code> to be looked up
     * @param includeRemovedFiles
     *            Should files removed in monitor be included.
     * @return List The list of required objects.
     */
    public List<FileDetailsDTO> getFilesUploadedByUser(Integer userID, Long sessionID, Locale currentLocale,
	    boolean includeRemovedFiles);

    /**
     * This method returns a SortedMap of all files that were submitted users within a given <code>sessionID</code>.
     *
     * @param sessionID
     *            The <code>session_id</code> to be looked up
     * @return SortedMap, the key is UserDTO, the value is a List of FileDetailsDTO objects
     */
    public SortedMap<SubmitUserDTO, List<FileDetailsDTO>> getFilesUploadedBySession(Long sessionID,
	    Locale currentLocale);

    /**
     * Updates the marks for a file, and also allows a file to be uploaded
     *
     * @param reportID
     * @param marks
     * @param comments
     * @param marksFileInputStream
     * @param marksFileName
     */
    public void updateMarks(Long reportID, Float marks, String comments, MultipartFile file, Long SessionID)
	    throws InvalidParameterException, RepositoryCheckedException;

    /**
     * Removes the marks file from a report
     *
     * @param reportID
     * @param markFileUUID
     * @param markFileVersionID
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    public void removeMarkFile(Long reportID, Long markFileUUID, Long markFileVersionID, Long sessionID)
	    throws InvalidParameterException, RepositoryCheckedException;

    /**
     * Mark the original file uploaded by a learner as deleted. Does not delete the file
     * from the content repository.
     *
     * @param detailID
     */
    public void removeLearnerFile(Long detailID, UserDTO monitor);

    /**
     * Mark a deleted original file as not deleted. Undoes what removeLearnerFile().
     *
     * @param detailID
     */
    public void restoreLearnerFile(Long detailID, UserDTO monitor);

    public FileDetailsDTO getFileDetails(Long detailID, Locale currentLocale);

    /**
     * Get SubmitFilesSession instance according to the given session id.
     *
     * @param sessionID
     * @return
     */
    public SubmitFilesSession getSessionById(Long sessionID);

    /**
     * Release marks and comments information to learners, for a special session.
     *
     * @param sessionID
     */
    public void releaseMarksForSession(Long sessionID);

    /**
     * Notify learners about marks being released, via email.
     */
    public int notifyLearnersOnMarkRelease(long sessionID);

    /**
     * When learner finish submission, it invokes this function and will remark the <code>finished</code> field.
     *
     * @param sessionID
     * @param userID
     */
    public void finishSubmission(Long sessionID, Integer userID);

    /**
     * Create the default content for the given contentID. These default data will copy from default record in Tool
     * Content database table.
     *
     * @return The SubmitFilesContent with default content and given contentID
     */
    public SubmitFilesContent createDefaultContent(Long contentID);

    /**
     * This method retrieves the default content id.
     *
     * @param toolSignature
     *            The tool signature which is defined in lams_tool table.
     * @return the default content id
     */
    public Long getToolDefaultContentIdBySignature(String toolSignature);

    /**
     * This method retrieves a list of SubmitFileSession from the contentID.
     *
     * @param contentID
     * @return a list of SubmitFileSession
     */
    // public List getSubmitFilesSessionsByContentID(Long contentID);
    public List<SubmitFilesSession> getSubmitFilesSessionByContentID(Long contentID);

    /**
     * Save or update tool content into database.
     *
     * @param persistContent
     *            The <code>SubmitFilesContent</code> to be updated
     */
    public void saveOrUpdateContent(SubmitFilesContent persistContent);

    /**
     * Create refection entry into notebook tool.
     *
     * @param sessionId
     * @param notebook_tool
     * @param tool_signature
     * @param userId
     * @param entryText
     */
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText);

    /**
     * Get reflection entry from notebook tool.
     *
     * @param sessionId
     * @param idType
     * @param signature
     * @param userID
     * @return
     */
    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);

    /**
     * @param notebookEntry
     */
    public void updateEntry(NotebookEntry notebookEntry);

    public List<SubmitFilesSession> getSessionsByContentID(Long toolContentID);

    // *************************************************************
    // get SubmitUser methods
    // *************************************************************

    /**
     * Get learner by given <code>toolSessionID</code> and <code>userID</code>.
     *
     * @param sessionID
     * @param userID
     * @return
     */
    public SubmitUser getSessionUser(Long sessionID, Integer userID);

    public SubmitUser getContentUser(Long contentId, Integer userID);

    /**
     * Create new user
     *
     * @param userDto
     * @param sessionID
     * @return
     */
    public SubmitUser createSessionUser(UserDTO userDto, Long sessionID);

    public SubmitUser createContentUser(UserDTO user, Long contentId);

    /**
     * Get information of all users who have submitted file.
     *
     * @return The user information list
     */
    public List<SubmitUser> getUsersBySession(Long sessionID);

    /**
     * get user by UID
     *
     * @param uid
     * @return
     */
    public SubmitUser getUserByUid(Long uid);

    /**
     * Get a paged, optionally sorted and filtered, list of users.
     * Will return List<[SubmitUser, Integer1, Integer2, String], [SubmitUser, Integer1, Integer2, String], ... ,
     * [SubmitUser, Integer1, Integer2, String]>
     * where Integer1 is the number of files uploaded, Integer2 is the number of files marked
     * and String is the notebook entry. No notebook entries needed? Will return null in their place.
     *
     * @return
     */
    List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting, String searchString,
	    boolean getNotebookEntries);

    /**
     * Get the number of users that would be returned by getUsersForTablesorter() if it was not paged. Supports
     * filtering.
     *
     * @return
     */
    int getCountUsersBySession(Long sessionId, String searchString);

    /**
     * Get the basic statistics for all the sessions for one activity.
     *
     * @param contentId
     * @return
     */
    List<StatisticDTO> getStatisticsBySession(final Long contentId);

    /**
     * Get the leader statistics for all the sessions for one activity.
     *
     * @param contentId
     * @return
     */
    List<StatisticDTO> getLeaderStatisticsBySession(final Long contentId);

    public IEventNotificationService getEventNotificationService();

    /**
     * Gets a message from resource bundle. Same as <code><fmt:message></code> in JSP pages.
     *
     * @param key
     *            key of the message
     * @param args
     *            arguments for the message
     * @return message content
     */
    String getLocalisedMessage(String key, Object[] args);

    /**
     * Set specified user as a leader. Also the previous leader (if any) is marked as non-leader.
     */
    SubmitUser checkLeaderSelectToolForSessionLeader(SubmitUser user, Long toolSessionID);

    /**
     * Create a new user in database.
     */
    void createUser(SubmitUser submitUser);

    boolean isUserGroupLeader(Long userId, Long toolSessionId);

    void copyLearnerContent(SubmitUser fromUser, SubmitUser toUser);

    void changeLeaderForGroup(long toolSessionId, int leaderUserId);
}
