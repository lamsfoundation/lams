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

package org.lamsfoundation.lams.tool.qa.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.rating.ToolRatingManager;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaConfigItem;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaWizardCategory;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.util.QaApplicationException;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * This interface define the contract that all Q/A service provider must follow.
 *
 * @author Ozgur Demirtas
 */
public interface IQaService extends ToolRatingManager {

    /**
     * @param user
     * @param toolSessionId
     * @return
     */
    boolean isUserGroupLeader(QaQueUsr user, Long toolSessionId);

    /**
     * Set specified user as a leader. Also the previous leader (if any) is marked as non-leader.
     *
     * @param userId
     * @param toolSessionID
     */
    QaQueUsr checkLeaderSelectToolForSessionLeader(QaQueUsr user, Long toolSessionID);

    /**
     * Check user has the same answers logs as group leader. If not - creates missing ones.
     *
     * @param user
     * @param leader
     */
    void copyAnswersFromLeader(QaQueUsr user, QaQueUsr leader);

    void setDefineLater(String strToolContentID, boolean value);

    /**
     * Get users by given toolSessionID.
     *
     * @param toolSessionID
     * @return
     */
    List<QaQueUsr> getUsersBySessionId(Long toolSessionID);

    /**
     * Return the qa object according to the requested content id.
     *
     * @param toolContentId
     *            the tool content id
     * @return the qa object
     */

    QaContent getQaContent(long toolContentId);

    void saveOrUpdateQaContent(QaContent qa);

    void updateUser(QaQueUsr qaQueUsr);

    List<QaUsrResp> getResponsesByUserUid(final Long userUid);

    QaUsrResp getResponseByUserAndQuestion(final Long queUsrId, final Long qaQueContentId);

    List<QaUsrResp> getResponseBySessionAndQuestion(final Long qaSessionId, final Long questionId);

    List<QaUsrResp> getResponsesForTablesorter(final Long toolContentId, final Long qaSessionId, final Long questionId,
	    final Long excludeUserId, int page, int size, int sorting, String searchString);

    int getCountResponsesBySessionAndQuestion(final Long qaSessionId, final Long questionId, final Long excludeUserId,
	    String searchString);

    /**
     * Creates or updates response with answer submitted by user.
     *
     * @param newAnswer
     * @param toolSessionID
     * @param questionDisplayOrder
     * @param isAutosave
     *            whether it's requested by autosave feature
     */
    void updateResponseWithNewAnswer(String newAnswer, String toolSessionID, Long questionDisplayOrder,
	    boolean isAutosave);

    void createQuestion(QaQueContent qaQuestion);

    void removeQuestion(QaQueContent qaQuestion);

    void createUserResponse(QaUsrResp qaUsrResp);

    void updateUserResponse(QaUsrResp resp);

    QaUsrResp getResponseById(Long responseId);

    QaQueContent getQuestionByContentAndDisplayOrder(Long displayOrder, Long contentUid);

    QaQueContent getQuestionByUid(Long questionUid);

    List<QaQueContent> getAllQuestionEntriesSorted(final long qaContentId);

    void saveOrUpdateQuestion(QaQueContent qaQuestion);

    /**
     * Return the qa session object according to the requested session id.
     *
     * @param qaSessionId
     *            qa session id
     * @return the qa session object
     */
    QaSession getSessionById(long qaSessionId);

    void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    QaQueUsr createUser(Long toolSessionID, Integer userId);

    void updateSession(QaSession qaSession);

    void updateQaContent(QaContent qa);

    void createQaContent(QaContent qa);

    void updateResponseVisibility(Long responseUid, boolean visible);

    QaQueUsr getUserByIdAndSession(final Long queUsrId, final Long qaSessionId);

    void removeUserResponse(QaUsrResp resp);

    List getAllQuestionEntries(final Long uid);

    void recalculateUserAnswers(QaContent content, Set<QaQueContent> oldQuestions, List<QaQuestionDTO> questionDTOs,
	    List<QaQuestionDTO> deletedQuestions);

    /**
     * copyToolContent(Long fromContentId, Long toContentId) return void
     *
     * @param fromContentId
     * @param toContentId
     */
    void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;

    /**
     * checks the paramter content in the user responses table
     *
     * @param qa
     * @return boolean
     * @throws QaApplicationException
     */
    boolean isStudentActivityOccurredGlobal(QaContent qaContent);

    /**
     * createToolSession(Long toolSessionId,String toolSessionName, Long toolContentId)
     *
     * It is also defined here since in development we want to be able call it directly from the web-layer instead of it
     * being called by the container.
     *
     * @param toolSessionId
     * @param toolContentId
     */
    void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException;

    /**
     * leaveToolSession(Long toolSessionId, Long learnerId)
     *
     * It is also defined here since in development we want to be able call it directly from our web-layer instead of it
     * being called by the container.
     *
     * @param toolSessionId
     * @param toolContentId
     */
    String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException;

    IToolVO getToolBySignature(String toolSignature);

    long getToolDefaultContentIdBySignature(String toolSignature);

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);

    /**
     * Get the LAMS audit service. Needed as the web layer controls the staff updating of an answer, so the log entry
     * must be made by the web layer.
     */
    IAuditService getAuditService();

    void updateEntry(NotebookEntry notebookEntry);

    QaContent getQaContentBySessionId(Long sessionId);

    /**
     * Creates an unique name for a QaCondition. It consists of the tool output definition name and a unique positive
     * integer number.
     *
     * @param existingConditions
     *            existing conditions; required to check if a condition with the same name does not exist.
     * @return unique QaCondition name
     */
    String createConditionName(Collection<QaCondition> existingConditions);

    void deleteCondition(QaCondition condition);

    QaCondition createDefaultComplexCondition(QaContent qaContent);

    /**
     * Gets the qa config item with the given key
     *
     * @param configKey
     * @return
     */
    QaConfigItem getConfigItem(String configKey);

    /**
     * Saves or updates a qa config item
     *
     * @param configItem
     */
    void saveOrUpdateConfigItem(QaConfigItem configItem);

    /**
     * Gets the set of wizard categories from the database
     *
     * @return
     */
    SortedSet<QaWizardCategory> getWizardCategories();

    /**
     * Saves the entire set of QaWizardCategories (including the child cognitive skills and questions)
     *
     * @param categories
     */
    void saveOrUpdateQaWizardCategories(SortedSet<QaWizardCategory> categories);

    /**
     * Deletes a wizard category from the db
     *
     * @param uid
     */
    void deleteWizardCategoryByUID(Long uid);

    /**
     * Deletes a wizard cognitive skill from the db
     *
     * @param uid
     */
    void deleteWizardSkillByUID(Long uid);

    /**
     * Deletes a wizard question from the db
     *
     * @param uid
     */
    void deleteWizardQuestionByUID(Long uid);

    /**
     * Deletes all categories, sub skills and sub questions
     */
    void deleteAllWizardCategories();

    void removeQuestionsFromCache(QaContent qaContent);

    void removeQaContentFromCache(QaContent qaContent);

    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     *
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);

    /**
     * Return content folder (unique to each learner and lesson) which is used for storing user generated content. It's
     * been used by CKEditor.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String getLearnerContentFolder(Long toolSessionId, Long userId);

    /**
     *
     * Takes the tool session id as the main input.
     */

    /**
     * Return username and reflections for a sessions. Paged. Used for monitoring.
     * Will return List<[username (String), fullname(String), String (notebook entry)]>
     *
     * @param content
     * @param userID
     * @return
     */
    List<Object[]> getUserReflectionsForTablesorter(Long toolSessionId, int page, int size, int sorting,
	    String searchString);

    int getCountUsersBySessionWithSearch(Long toolSessionId, String searchString);

    /**
     * notifyTeachersOnResponseSubmit
     *
     * @param sessionId
     */
    void notifyTeachersOnResponseSubmit(Long sessionId);
}