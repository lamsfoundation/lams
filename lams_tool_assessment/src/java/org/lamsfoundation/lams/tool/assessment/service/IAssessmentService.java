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

package org.lamsfoundation.lams.tool.assessment.service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentResultDTO;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.dto.GradeStatsDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummary;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.excel.ExcelSheet;

import reactor.core.publisher.Flux;

/**
 * Interface that defines the contract that all ShareAssessment service provider must follow.
 *
 * @author Andrey Balan
 */
public interface IAssessmentService extends ICommonToolService {

    /**
     * @param user
     * @param toolSessionId
     * @return
     */
    boolean isUserGroupLeader(Long userId, Long toolSessionId);

    /**
     * Set specified user as a leader. Also the previous leader (if any) is marked as non-leader.
     *
     * @param userId
     * @param toolSessionID
     */
    AssessmentUser checkLeaderSelectToolForSessionLeader(AssessmentUser user, Long toolSessionID);

    /**
     * Check user has the same answers logs as group leader. If not - creates missing ones.
     *
     * @param user
     * @param leader
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    void copyAnswersFromLeader(AssessmentUser user, AssessmentUser leader)
	    throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException;

    /**
     * Stores date when user has started activity with time limit.
     */
    LocalDateTime launchTimeLimit(long toolContentId, int userId);

    /**
     * @param assessment
     * @param groupLeader
     * @return whether the time limit is exceeded already
     */
    boolean checkTimeLimitExceeded(long assessmentUid, int userId);

    /**
     * Get users by given toolSessionID.
     *
     * @param toolSessionID
     * @return
     */
    List<AssessmentUser> getUsersBySession(Long toolSessionID);

    List<AssessmentUserDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy, String sortOrder,
	    String searchString);

    int getCountUsersBySession(Long sessionId, String searchString);

    int getCountUsersByContentId(Long contentId);

    int getCountLessonLearnersByContentId(long contentId);

    int getCountLearnersWithFinishedCurrentAttempt(long contentId);

    List<AssessmentUserDTO> getPagedUsersBySessionAndQuestion(Long sessionId, Long questionUid, int page, int size,
	    String sortBy, String sortOrder, String searchString);

    /**
     * Get <code>Assessment</code> by toolContentID.
     *
     * @param contentId
     * @return
     */
    Assessment getAssessmentByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (Assessment) and assign the toolContentId of that copy as the
     * given <code>contentId</code>
     *
     * @param contentId
     * @return
     * @throws AssessmentApplicationException
     */
    Assessment getDefaultContent(Long contentId) throws AssessmentApplicationException;

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(AssessmentUser assessmentUser);

    /**
     * Get user created current Assessment.
     *
     * @param long1
     * @return
     */
    AssessmentUser getUserCreatedAssessment(Long userID, Long contentId);

    /**
     * Get user by given userID and toolContentID.
     *
     * @param long1
     * @return
     */
    AssessmentUser getUserByIdAndContent(Long userID, Long contentId);

    AssessmentUser getUserByLoginAndContent(String login, Long contentId);

    /**
     * Get user by sessionID and UserID
     *
     * @param long1
     * @param sessionId
     * @return
     */
    AssessmentUser getUserByIDAndSession(Long long1, Long sessionId);

    /**
     * Save or update assessment into database.
     *
     * @param Assessment
     */
    void saveOrUpdateAssessment(Assessment Assessment);

    /**
     * Update assessment question into database.
     */
    void updateAssessmentQuestion(AssessmentQuestion question);

    /**
     * Delete resoruce question from database.
     *
     * @param uid
     */
    void deleteAssessmentQuestion(Long uid);

    void deleteQuestionReference(Long uid);

    /**
     * Get assessment which is relative with the special toolSession.
     *
     * @param sessionId
     * @return
     */
    Assessment getAssessmentBySessionId(Long sessionId);

    /**
     * Get assessment toolSession by toolSessionId
     *
     * @param sessionId
     * @return
     */
    AssessmentSession getSessionBySessionId(Long sessionId);

    /**
     * Get all assessment toolSessions by toolContentId
     *
     * @param toolContentId
     * @return
     */
    List<AssessmentSession> getSessionsByContentId(Long toolContentId);

    /**
     * Create new assessment result object.
     */
    void setAttemptStarted(Assessment assessment, AssessmentUser assessmentUser, Long toolSessionId,
	    List<Set<QuestionDTO>> pagedQuestionDtos);

    void storeSingleMarkHedgingQuestion(Assessment assessment, Long userId, List<Set<QuestionDTO>> pagedQuestions,
	    Long singleMarkHedgingQuestionUid)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

    /**
     * Store user answers into DB. It can be autosave and non-autosave requests.
     *
     * @param assessment
     * @param userId
     * @param pagedQuestions
     * @param isAutosave
     *            indicates whether it's autosave request
     *
     * @return whether storing results is allowed, false otherwise
     */
    boolean storeUserAnswers(Assessment assessment, Long userId, List<Set<QuestionDTO>> pagedQuestions,
	    boolean isAutosave) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

    Flux<String> getCompletionChartsDataFlux(long toolContentId);

    void loadupLastAttempt(Long assessmentUid, Long userId, List<Set<QuestionDTO>> pagedQuestionDtos);

    /**
     * Return the latest result (it can be unfinished).
     *
     * @param assessmentUid
     * @param userId
     * @return
     */
    AssessmentResult getLastAssessmentResult(Long assessmentUid, Long userId);

    /**
     * Checks whether the last attempt started by user is finished.
     *
     * @param user
     * @return true if user has finished it, false otherwise
     */
    Boolean isLastAttemptFinishedByUser(AssessmentUser user);

    /**
     * Return the latest *finished* result.
     *
     * @param assessmentUid
     * @param userId
     * @return
     */
    AssessmentResult getLastFinishedAssessmentResult(Long assessmentUid, Long userId);

    /**
     * Return the latest *finished* result grade.
     *
     * @param assessmentUid
     * @param userId
     * @return
     */
    Float getLastTotalScoreByUser(Long assessmentUid, Long userId);

    List<AssessmentUserDTO> getLastTotalScoresByContentId(Long toolContentId);

    /**
     * Return the best *finished* result grade.
     *
     * @param sessionId
     * @param userId
     * @return
     */
    Float getBestTotalScoreByUser(Long sessionId, Long userId);

    List<AssessmentUserDTO> getBestTotalScoresByContentId(Long toolContentId);

    /**
     * Return the first *finished* result grade.
     *
     * @param sessionId
     * @param userId
     * @return
     */
    Float getFirstTotalScoreByUser(Long sessionId, Long userId);

    List<AssessmentUserDTO> getFirstTotalScoresByContentId(Long toolContentId);

    /**
     * Return the average score of all *finished* result scores.
     *
     * @param sessionId
     * @param userId
     * @return
     */
    Float getAvergeTotalScoreByUser(Long sessionId, Long userId);

    List<AssessmentUserDTO> getAverageTotalScoresByContentId(Long toolContentId);

    /**
     * Return the latest *finished* result grade.
     *
     * @param assessmentUid
     * @param userId
     * @return
     */
    Integer getLastFinishedAssessmentResultTimeTaken(Long assessmentUid, Long userId);

    /**
     * Count how many last attempts selected specified option.
     */
    int countAttemptsPerOption(Long toolContentId, Long optionUid, boolean finishedAttemptsOnly);

    /**
     * Return number of finished results. I.e. don't count the last not-yet-finished result (it can be autosave one).
     *
     * @param assessmentUid
     * @param userId
     * @return
     */
    int getAssessmentResultCount(Long assessmentUid, Long userId);

    /**
     * Checks whether anyone has attempted this assessment.
     */
    boolean isAssessmentAttempted(Long assessmentUid);

    AssessmentQuestionResult getAssessmentQuestionResultByUid(Long questionResultUid);

    List<Object[]> getAssessmentQuestionResultList(Long assessmentUid, Long userId, Long questionUid);

    /**
     * Returns question result mark from the last finished assessment result, and null if not available.
     *
     * @param assessmentUid
     * @param userId
     * @param questionDisplayOrder
     * @return
     */
    Float getQuestionResultMark(Long assessmentUid, Long userId, int questionDisplayOrder);

    Long createNotebookEntry(Long sessionId, Integer userId, String entryText);

    NotebookEntry getEntry(Long sessionId, Integer userId);

    void updateEntry(NotebookEntry notebookEntry);

    List<ReflectDTO> getReflectList(Long contentId);

    /**
     * If success return next activity's url, otherwise return null.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws AssessmentApplicationException;

    /**
     * Set userFinished to false
     *
     * @param toolSessionId
     * @param userId
     */
    void unsetSessionFinished(Long toolSessionId, Long userId);

    /**
     * Returns sessionDtos containing only session ids and session names.
     *
     * @param contentId
     * @return
     */
    List<GradeStatsDTO> getSessionDtos(Long contentId, boolean includeStatistics);

    GradeStatsDTO getStatsDtoForActivity(Long contentId);

    /**
     * Prepares question results to be displayed in "Learner Summary" table. Shows all of them in case there is at least
     * one random question present, and just questions from the question list if no random questions.
     */
    AssessmentResultDTO getUserMasterDetail(Long sessionId, Long userId);

    /**
     * Return user summary. This summary contains list of all attempts made by user.
     *
     * @param contentId
     * @param userId
     * @return
     */
    UserSummary getUserSummary(Long contentId, Long userId, Long sessionId);

    /**
     * For summary tab
     *
     * @param contentId
     * @param questionUid
     * @return
     */
    QuestionSummary getQuestionSummary(Long contentId, Long questionUid);

    /**
     * For export purposes
     *
     * @param contentId
     * @param questionUid
     * @return
     */
    Map<Long, QuestionSummary> getQuestionSummaryForExport(Assessment assessment, boolean finishedAttemptsOnly);

    /**
     * Prepares data for Summary excel export
     */
    List<ExcelSheet> exportSummary(Assessment assessment, long toolContentId);

    /**
     * Gets the basic statistics for the grades for the Leaders when an Assessment is done using
     * Group Leaders. So the averages, etc are for the whole Assessment, not for a Group.
     */
    GradeStatsDTO getStatsDtoForLeaders(Long contentId);

    /**
     * Prepares data for the marks summary graph on the statistics page
     *
     */
    List<Float> getMarksArray(Long sessionId);

    List<Float> getMarksArrayByContentId(Long toolContentId);

    /**
     * Prepares data for the marks summary graph on the statistics page, using the grades for the Leaders
     * when an Assessment is done using Group Leaders. So the grades are for the whole Assessment, not for a Group.
     *
     */
    List<Float> getMarksArrayForLeaders(Long contentId);

    void changeQuestionResultMark(Long questionResultUid, float newMark);

    void notifyTeachersOnAttemptCompletion(Long sessionId, String userName);

    /**
     * Get a message from the language files with the given key
     *
     * @param key
     * @return
     */
    String getMessage(String key);

    String getMessage(String key, Object[] args);

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
     * Return content folder (unique to each learner and lesson) which can be used for storing user generated content.
     * Currently is used for CKEditor.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String getLearnerContentFolder(Long toolSessionId, Long userId);

    /**
     * Recalculate marks after editing content from monitoring
     */
    void recalculateUserAnswers(final Long assessmentUid, final Long toolContentId,
	    Set<AssessmentQuestion> oldQuestions, Set<AssessmentQuestion> newQuestions,
	    Set<QuestionReference> oldReferences, Set<QuestionReference> newReferences);

    void releaseFromCache(Object object);

    String getPortraitId(Long userId);

    AssessmentQuestion getAssessmentQuestionByUid(Long questionUid);

    /**
     * Sends a websocket command to learners who have assessment results open
     * to refresh page because new data is available
     */
    void notifyLearnersOnAnswerDisclose(long toolContentId);

    void setConfigValue(String key, String value);

    String getConfigValue(String key);

    Collection<User> getAllGroupUsers(Long toolSessionId);

    Grouping getGrouping(long toolContentId);

    List<User> getPossibleIndividualTimeLimitUsers(long toolContentId, String searchString);

    Map<Integer, List<String[]>> getAnsweredQuestionsByUsers(long toolContentId);

    void changeLeaderForGroup(long toolSessionId, long leaderUserId);
}