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

package org.lamsfoundation.lams.tool.mc.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.AnswerDTO;
import org.lamsfoundation.lams.tool.mc.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.dto.ToolOutputDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Interface that defines the contract that all MCQ service provider must follow.
 *
 * @author Ozgur Demirtas
 */
public interface IMcService {

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

    void createMc(McContent mcContent) throws McApplicationException;

    McContent getMcContent(Long toolContentId) throws McApplicationException;

    void setDefineLater(String strToolContentID, boolean value);

    void updateQuestion(McQueContent mcQueContent) throws McApplicationException;

    McQueContent getQuestionByDisplayOrder(final Long displayOrder, final Long mcContentUid)
	    throws McApplicationException;

    McQueUsr createMcUser(Long toolSessionId) throws McApplicationException;

    McQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionUid) throws McApplicationException;

    void saveUserAttempt(McQueUsr user, List<AnswerDTO> answerDtos);

    void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt) throws McApplicationException;

    void removeMcQueContent(McQueContent mcQueContent) throws McApplicationException;

    void saveOrUpdateMcQueContent(McQueContent mcQueContent) throws McApplicationException;

    /**
     * persists the questions
     */
    McContent createQuestions(List<McQuestionDTO> questionDTOs, McContent content);

    void releaseQuestionsFromCache(McContent content);

    List refreshQuestionContent(final Long mcContentId) throws McApplicationException;

    List getAllQuestionsSorted(final long mcContentId) throws McApplicationException;

    McQueContent getQuestionByUid(Long uid);

    McQueUsr getMcUserByUID(Long uid) throws McApplicationException;

    List<McUserMarkDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy, String sortOrder,
	    String searchString);

    int getCountPagedUsersBySession(Long sessionId, String searchString);

    String getLocalizedMessage(String key);

    List<McQueContent> getQuestionsByContentUid(final Long mcContentId) throws McApplicationException;

    McSession getMcSessionById(Long mcSessionId) throws McApplicationException;

    void updateMc(McContent mc) throws McApplicationException;

    void updateMcQueUsr(McQueUsr mcQueUsr) throws McApplicationException;

    List<McOptionDTO> getOptionDtos(Long mcQueContentId) throws McApplicationException;

    List<McUsrAttempt> getFinalizedUserAttempts(final McQueUsr user) throws McApplicationException;

    List findOptionsByQuestionUid(Long mcQueContentId) throws McApplicationException;

    void updateMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException;

    McUsrAttempt getUserAttemptByQuestion(Long queUsrUid, Long mcQueContentId) throws McApplicationException;
    
    List<ToolOutputDTO> getLearnerMarksByContentId(Long toolContentId);

    void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;

    boolean existsSession(Long toolSessionId);

    void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException;

    void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException;

    IToolVO getToolBySignature(String toolSignature) throws McApplicationException;

    long getToolDefaultContentIdBySignature(String toolSignature) throws McApplicationException;

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);

    void updateEntry(NotebookEntry notebookEntry);

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
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     * 
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
    String getActivityEvaluation(Long toolContentId);
    
    void setActivityEvaluation(Long toolContentId, String toolOutputDefinition);

    /**
     * @param mcContent
     * @param user
     *            user; pass null if there is no need to populate previous answers
     * @return
     */
    List<AnswerDTO> getAnswersFromDatabase(McContent mcContent, McQueUsr user);

    /**
     * Returns userMarksDtos grouped by sessions. Used *only* for export portfolio.
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

    void recalculateUserAnswers(McContent content, Set<McQueContent> oldQuestions, List<McQuestionDTO> questionDTOs,
	    List<McQuestionDTO> deletedQuestions);

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
