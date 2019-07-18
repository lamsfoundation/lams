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
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.mc.dto.LeaderResultsDTO;
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.dto.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.dto.SessionDTO;
import org.lamsfoundation.lams.tool.mc.dto.ToolOutputDTO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.lamsfoundation.lams.tool.mc.model.McQueUsr;
import org.lamsfoundation.lams.tool.mc.model.McSession;
import org.lamsfoundation.lams.tool.mc.model.McUsrAttempt;
import org.lamsfoundation.lams.tool.service.ICommonToolService;

/**
 * Interface that defines the contract that all MCQ service provider must follow.
 *
 * @author Ozgur Demirtas
 */
public interface IMcService extends ICommonToolService {

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

    void createMc(McContent mcContent);

    McContent getMcContent(Long toolContentId);

    void setDefineLater(String strToolContentID, boolean value);

    McQueContent getQuestionByDisplayOrder(final Integer displayOrder, final Long mcContentUid);

    McQueUsr createMcUser(Long toolSessionId) throws McApplicationException;

    McQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionUid) throws McApplicationException;

    Long getPortraitId(Long userId);

    void saveUserAttempt(McQueUsr user, List<AnswerDTO> answerDtos);

    void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt) throws McApplicationException;

    /**
     * Count how many attempts done to this option
     *
     * @param optionUid
     * @return
     */
    int getAttemptsCountPerOption(Long optionUid, Long mcQueContentUid);

    /**
     * Checks whether anyone has attempted this assessment.
     */
    boolean isMcContentAttempted(Long mcContentUid);

    void removeMcQueContent(McQueContent mcQueContent) throws McApplicationException;

    void saveOrUpdateMcQueContent(McQueContent mcQueContent);

    /**
     * persists the questions
     */
    McContent createQuestions(List<McQuestionDTO> questionDTOs, McContent content);

    void releaseQuestionsFromCache(McContent content);

    List getAllQuestionsSorted(final long mcContentId);

    McQueContent getQuestionByUid(Long uid);

    McQueUsr getMcUserByUID(Long uid) throws McApplicationException;

    McQueUsr getMcUserByContentId(Long userId, Long contentId);

    List<McUserMarkDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy, String sortOrder,
	    String searchString);

    int getCountPagedUsersBySession(Long sessionId, String searchString);

    String getLocalizedMessage(String key);

    List<McQueContent> getQuestionsByContentUid(final Long mcContentId);

    McSession getMcSessionById(Long mcSessionId) throws McApplicationException;

    void updateMc(McContent mc) throws McApplicationException;

    void updateMcQueUsr(McQueUsr mcQueUsr) throws McApplicationException;

    List<McOptionDTO> getOptionDtos(Long mcQueContentId) throws McApplicationException;

    List<McUsrAttempt> getFinalizedUserAttempts(final McQueUsr user);

    List<QbOption> findOptionsByQuestionUid(Long mcQueContentId) throws McApplicationException;

    void updateQbOption(QbOption option) throws McApplicationException;

    McUsrAttempt getUserAttemptByQuestion(Long queUsrUid, Long mcQueContentId);

    List<ToolOutputDTO> getLearnerMarksByContentId(Long toolContentId);

    void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;

    boolean existsSession(Long toolSessionId);

    void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException;

    void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException;

    Tool getToolBySignature(String toolSignature) throws McApplicationException;

    long getToolDefaultContentIdBySignature(String toolSignature) throws McApplicationException;

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);

    void updateEntry(NotebookEntry notebookEntry);

    /**
     * Return the top, lowest and average mark for all learners for one particular tool session.
     *
     * @param request
     * @return lowest mark, average mark, top mark in that order
     */
    Object[] getMarkStatistics(McSession mcSession);

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third questionDescription contains the word Koala and hence
     * the need for
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

    void recalculateUserAnswers(McContent content, Set<McQueContent> oldQuestions, List<McQuestionDTO> questionDTOs);

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

    /**
     * Gets the basic statistics for the grades for the Leaders when an Assessment is done using
     * Group Leaders. So the averages, etc are for the whole Assessment, not for a Group.
     *
     * @param contentId
     * @return
     */
    LeaderResultsDTO getLeaderResultsDTOForLeaders(Long contentId);

    /**
     * Prepares data for the marks summary graph on the statistics page
     *
     * @param assessment
     * @param sessionDtos
     * @return
     */
    List<Number> getMarksArray(Long sessionId);

    /**
     * Prepares data for the marks summary graph on the statistics page, using the grades for the Leaders
     * when an Assessment is done using Group Leaders. So the grades are for the whole Assessment, not for a Group.
     *
     * @param assessment
     * @param sessionDtos
     * @return
     */
    List<Number> getMarksArrayForLeaders(Long contentId);

    /**
     * Contains the session id and name for a session. If includeStatistics is true also includes the number of
     * learners, max min and average of marks for the session.
     */
    List<SessionDTO> getSessionDtos(Long contentId, boolean includeStatistics);

    /**
     * Checks if data in DTO is the same as in the corresponding QB questionDescription and options in DB.
     * Returns one of statuses from IQbService.QUESTION_MODIFIED_*
     */
    int isQbQuestionModified(McQuestionDTO questionDTO);
}