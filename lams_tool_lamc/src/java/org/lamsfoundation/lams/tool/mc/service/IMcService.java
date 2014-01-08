/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General License for more details.
 * 
 * You should have received a copy of the GNU General License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McCandidateAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McLearnerAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUploadedFile;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author Ozgur Demirtas
 * 
 *         Interface that defines the contract that all MCQ service provider must follow.
 */
public interface IMcService {
    
    /**
     * @param user
     * @param toolSessionId
     * @return
     */
    boolean isUserGroupLeader(McQueUsr user, Long toolSessionId);
    
    /**
     * Set specified user as a leader. Also the previous leader (if any) is marked as non-leader.
     * 
     * @param userId
     * @param toolSessionID
     */
    McQueUsr checkLeaderSelectToolForSessionLeader(McQueUsr user, Long toolSessionID);
    
    /**
     * Check user has the same answers logs as group leader. If not - creates missing ones. 
     * 
     * @param user
     * @param leader
     */
    void copyAnswersFromLeader(McQueUsr user, McQueUsr leader);
    
    void configureContentRepository() throws McApplicationException;

    void createMc(McContent mcContent) throws McApplicationException;

    McContent retrieveMc(Long toolContentId) throws McApplicationException;

    void createMcQue(McQueContent mcQueContent) throws McApplicationException;

    void updateMcQueContent(McQueContent mcQueContent) throws McApplicationException;

    List retrieveMcQueContentsByToolContentId(long mcContentId) throws McApplicationException;

    McQueContent getQuestionByDisplayOrder(final Long displayOrder, final Long mcContentUid)
	    throws McApplicationException;

    void createMcSession(McSession mcSession) throws McApplicationException;

    McQueUsr createMcUser(Long toolSessionId) throws McApplicationException;

    McQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionUid) throws McApplicationException;

    McQueUsr retrieveMcQueUsr(Long userId) throws McApplicationException;

    void saveUserAttempt(McQueUsr user, List<McLearnerAnswersDTO> selectedQuestionAndCandidateAnswersDTO);

    void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt) throws McApplicationException;

    McQueContent retrieveMcQueContentByUID(Long uid) throws McApplicationException;

    void removeMcQueContent(McQueContent mcQueContent) throws McApplicationException;

    McQueContent getMcQueContentByUID(Long uid) throws McApplicationException;

    void saveOrUpdateMcQueContent(McQueContent mcQueContent) throws McApplicationException;

    void removeQuestionContentByMcUid(final Long mcContentUid) throws McApplicationException;

    McOptsContent getMcOptionsContentByUID(Long uid) throws McApplicationException;

    void resetAllQuestions(final Long mcContentUid) throws McApplicationException;

    List refreshQuestionContent(final Long mcContentId) throws McApplicationException;

    List getAllQuestionEntriesSorted(final long mcContentId) throws McApplicationException;

    McQueContent getQuestionByUid(Long uid) throws McApplicationException;

    void removeMcOptionsContentByQueId(Long mcQueContentId) throws McApplicationException;

    void removeMcOptionsContent(McOptsContent mcOptsContent);

    McQueContent getQuestionContentByQuestionText(final String question, final Long mcContentUid);

    void removeMcQueContentByUID(Long uid) throws McApplicationException;

    McQueUsr getMcUserByUID(Long uid) throws McApplicationException;

    List<McQueContent> getAllQuestionEntries(final Long mcContentId) throws McApplicationException;

    McSession getMcSessionById(Long mcSessionId) throws McApplicationException;

    McContent retrieveMcBySessionId(Long mcSessionId) throws McApplicationException;

    void updateMc(McContent mc) throws McApplicationException;

    void updateMcSession(McSession mcSession) throws McApplicationException;

    void updateMcQueUsr(McQueUsr mcQueUsr) throws McApplicationException;

    List<McCandidateAnswersDTO> populateCandidateAnswersDTO(Long mcQueContentId) throws McApplicationException;

    McSession getMcSessionByUID(Long uid) throws McApplicationException;

    List<McUsrAttempt> getFinalizedUserAttempts(final McQueUsr user) throws McApplicationException;

    void deleteMc(McContent mc) throws McApplicationException;

    void deleteMcById(Long mcId) throws McApplicationException;

    void deleteMcQueUsr(McQueUsr mcQueUsr) throws McApplicationException;

    List findMcOptionsContentByQueId(Long mcQueContentId) throws McApplicationException;

    void saveMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException;

    McOptsContent getOptionContentByOptionText(final String option, final Long mcQueContentUid);

    void updateMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException;

    void deleteMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException;

    void deleteMcOptionsContentByUID(Long uid) throws McApplicationException;

    void saveMcContent(McContent mc) throws McApplicationException;

    boolean studentActivityOccurredGlobal(McContent mcContent) throws McApplicationException;

    McUsrAttempt getUserAttemptByQuestion(Long queUsrUid, Long mcQueContentId)
	    throws McApplicationException;

    void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;

    void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException;

    void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException;

    void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException;

    boolean existsSession(Long toolSessionId);

    void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException;

    void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException;

    IToolVO getToolBySignature(String toolSignature) throws McApplicationException;

    long getToolDefaultContentIdBySignature(String toolSignature) throws McApplicationException;

    McQueContent getToolDefaultQuestionContent(long contentId) throws McApplicationException;

    ITicket getRepositoryLoginTicket() throws McApplicationException;

    void deleteFromRepository(Long uuid, Long versionID);

    NodeKey uploadFileToRepository(InputStream stream, String fileName) throws McApplicationException;

    InputStream downloadFile(Long uuid, Long versionID) throws McApplicationException;

    void persistFile(String uuid, boolean isOnlineFile, String fileName, McContent mcContent)
	    throws McApplicationException;

    List getNextAvailableDisplayOrder(final long mcContentId) throws McApplicationException;

    NodeKey uploadFile(InputStream istream, String filename, String contentType, String fileType)
	    throws RepositoryCheckedException;

    NodeKey copyFile(Long uuid) throws RepositoryCheckedException;

    List findMcOptionCorrectByQueId(Long mcQueContentId) throws McApplicationException;

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);

    void updateEntry(NotebookEntry notebookEntry);

    void persistFile(McContent content, McUploadedFile file) throws McApplicationException;

    void removeFile(Long submissionId) throws McApplicationException;

    List<McUploadedFile> retrieveMcUploadedFiles(McContent mcContent) throws McApplicationException;

    /**
     * Return the top, lowest and average mark for all learners for one particular tool session.
     * 
     * @param request
     * @return top mark, lowest mark, average mark in that order
     */
    Integer[] getMarkStatistics(McSession mcSession);

    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     * 
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
    
    /**
     * @param mcContent
     * @param user user; pass null if there is no need to populate previous answers
     * @return
     */
    List<McLearnerAnswersDTO> buildLearnerAnswersDTOList(McContent mcContent, McQueUsr user);
    
    /**
     * Returns userMarksDtos grouped by sessions.
     * 
     * @param mcContent
     * @param isFullAttemptDetailsRequired
     *            if true populates complete user attempt history including option select history, used only for export
     *            and spreadsheet. If not only total mark calculated
     * @return
     */
    List<McSessionMarkDTO> buildGroupsMarkData(McContent mcContent, boolean isFullAttemptDetailsRequired);
    
    /**
     * prepareSessionDataSpreadsheet
     * 
     * @param mcContent
     * 
     * @return data to write out
     */
    byte[] prepareSessionDataSpreadsheet(McContent mcContent) throws IOException;
    
    void changeUserAttemptMark(Long userAttemptUid, Integer newMark);
    
    /**
     * 
     * returns reflection data for all sessions
     * 
     * @param mcContent
     * @param userID
     * @param mcService
     * @return
     */
    List<ReflectionDTO> getReflectionList(McContent mcContent, Long userID);

}
