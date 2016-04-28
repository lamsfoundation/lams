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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.assessment.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.assessment.dto.SessionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummary;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.ExcelCell;

/**
 * @author Andrey Balan
 * 
 *         Interface that defines the contract that all ShareAssessment service provider must follow.
 */
public interface IAssessmentService {
    
    /**
     * @param user
     * @param toolSessionId
     * @return
     */
    boolean isUserGroupLeader(AssessmentUser user, Long toolSessionId);
    
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
    void copyAnswersFromLeader(AssessmentUser user, AssessmentUser leader) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException;
    
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

    /**
     * Get list of assessment questions by given assessmentUid. These assessment questions must be created by author.
     * 
     * @param assessmentUid
     * @return
     */
    List getAuthoredQuestions(Long assessmentUid);

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(AssessmentUser assessmentUser);

    /**
     * Get user by given userID and toolContentID.
     * 
     * @param long1
     * @return
     */
    AssessmentUser getUserByIDAndContent(Long userID, Long contentId);

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
    AssessmentSession getAssessmentSessionBySessionId(Long sessionId);
    
    /**
     * Save or update assessment result.
     * 
     * @param assessmentResult
     */
    void setAttemptStarted(Assessment assessment, List<Set<AssessmentQuestion>> pagedQuestions,
	    AssessmentUser assessmentUser, Long toolSessionId);

    /**
     * Store user answers into DB. It can be autosave and non-autosave requests.
     * 
     * @param assessmentUid
     * @param userId
     * @param pagedQuestions
     * @param singleMarkHedgingQuestionUid - if provided - means only that current single MarkHedging question needs to be stored
     * @param isAutosave indicates whether it's autosave request
     * 
     * @return whether storing results is allowed, false otherwise
     */
    boolean storeUserAnswers(Long assessmentUid, Long userId, List<Set<AssessmentQuestion>> pagedQuestions,
	    Long singleMarkHedgingQuestionUid, boolean isAutosave);
    
    /**
     * Return the latest result (it can be unfinished).
     * 
     * @param assessmentUid
     * @param userId
     * @return
     */
    AssessmentResult getLastAssessmentResult(Long assessmentUid, Long userId);
    
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
    
    /**
     * Return the best *finished* result grade.
     * 
     * @param sessionId
     * @param userId
     * @return
     */
    Float getBestTotalScoreByUser(Long sessionId, Long userId);
    
    /**
     * Return the first *finished* result grade.
     * 
     * @param sessionId
     * @param userId
     * @return
     */
    Float getFirstTotalScoreByUser(Long sessionId, Long userId);
    
    /**
     * Return the average score of all *finished* result scores.
     * 
     * @param sessionId
     * @param userId
     * @return
     */
    Float getAvergeTotalScoreByUser(Long sessionId, Long userId);

    /**
     * Return the latest *finished* result grade.
     * 
     * @param assessmentUid
     * @param userId
     * @return
     */
    Integer getLastFinishedAssessmentResultTimeTaken(Long assessmentUid, Long userId);
    
    /**
     * Return the latest *finished* result (the same as the method above). But previously evicting it from the cache. It
     * might be useful in cases when we modify result and the use it during one request.
     * 
     * @param assessmentUid
     * @param userId
     * @return
     */
    AssessmentResult getLastFinishedAssessmentResultNotFromChache(Long assessmentUid, Long userId);
    
    /**
     * Return number of finished results. I.e. don't count the last not-yet-finished result (it can be autosave one).
     * 
     * @param assessmentUid
     * @param userId
     * @return
     */
    int getAssessmentResultCount(Long assessmentUid, Long userId);
    
    AssessmentQuestionResult getAssessmentQuestionResultByUid(Long questionResultUid);
    
    List<Object[]> getAssessmentQuestionResultList(Long assessmentUid, Long userId, Long questionUid);
    
    /**
     * Returns question result mark from the last finished assessment result, and null if not available.
     * 
     * @param assessmentUid
     * @param userId
     * @param questionSequenceId
     * @return
     */
    Float getQuestionResultMark(Long assessmentUid, Long userId, int questionSequenceId);
    
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
     * Returns sessionDtos containing only session ids and session names.
     * 
     * @param contentId
     * @return
     */
    List<SessionDTO> getSessionDtos(Long contentId);
    
    AssessmentResult getUserMasterDetail(Long sessionId, Long userId);
    
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
    Map<Long, QuestionSummary> getQuestionSummaryForExport(Assessment assessment);
    
    /**
     * Prepares data for Summary excel export 
     * 
     * @param assessment
     * @param sessionDtos
     * @param showUserNames
     * @return
     */
    LinkedHashMap<String, ExcelCell[][]> exportSummary(Assessment assessment, List<SessionDTO> sessionDtos, boolean showUserNames);
    
    void changeQuestionResultMark(Long questionResultUid, float newMark);

    void notifyTeachersOnAttemptCompletion(Long sessionId, String userName);
    
    /**
     * Get a message from the language files with the given key
     * @param key
     * @return
     */
    String getMessage(String key);
    
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
     * Return content folder (unique to each learner and lesson) which can be used for storing user generated content.
     * Currently is used for CKEditor.
     * 
     * @param toolSessionId
     * @param userId
     * @return
     */
    String getLearnerContentFolder(Long toolSessionId, Long userId);
    
    /**
     * Recalculate marks after editing content from monitoring.
     * 
     * @param assessment
     * @param oldQuestions
     * @param newQuestions
     * @param deletedQuestions
     * @param oldReferences
     * @param newReferences
     * @param deletedReferences
     */
    void recalculateUserAnswers(Assessment assessment, Set<AssessmentQuestion> oldQuestions,
	    Set<AssessmentQuestion> newQuestions, List<AssessmentQuestion> deletedQuestions,
	    Set<QuestionReference> oldReferences, Set<QuestionReference> newReferences,
	    List<QuestionReference> deletedReferences);
    
    /**
     * Recalculate mark for leader and sets it to all members of a group. Authentication check: user must be either lesson stuff or group manager.
     * 
     * @param requestUserDTO
     * @param lessonId
     */
    void recalculateMarkForLesson(UserDTO requestUserDTO, Long lessonId);

    void releaseQuestionsAndReferencesFromCache(Assessment assessment);
}


