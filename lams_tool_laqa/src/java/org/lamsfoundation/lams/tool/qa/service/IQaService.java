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
package org.lamsfoundation.lams.tool.qa.service;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
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
import org.lamsfoundation.lams.tool.qa.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.qa.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.qa.util.QaApplicationException;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * This interface define the contract that all Q/A service provider must follow.
 * 
 * @author Ozgur Demirtas
 */
public interface IQaService {
    
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
    
    /**
     * Get users by given toolSessionID.
     * 
     * @param toolSessionID
     * @return
     */
    List<QaQueUsr> getUsersBySession(Long toolSessionID);
    
    /**
     * Return the qa object according to the requested content id.
     * 
     * @param toolContentId
     *                the tool content id
     * @return the qa object
     */

    QaContent getQa(long toolContentId) throws QaApplicationException;

    void saveOrUpdateQa(QaContent qa) throws QaApplicationException;

    int getTotalNumberOfUsers(QaContent qa) throws QaApplicationException;

    int countSessionComplete(QaContent qa) throws QaApplicationException;

    void updateQaQueUsr(QaQueUsr qaQueUsr) throws QaApplicationException;

    QaQueUsr loadQaQueUsr(Long userId) throws QaApplicationException;

    QaUsrResp getResponseByUserAndQuestion(final Long queUsrId, final Long qaQueContentId)
	    throws QaApplicationException;
    
    /**
     * Creates or updates response with answer submitted by user.
     * 
     * @param newAnswer
     * @param toolSessionID
     * @param questionDisplayOrder
     */
    void updateResponseWithNewAnswer(String newAnswer, String toolSessionID, Long questionDisplayOrder);

    void createQaQue(QaQueContent qaQuestion) throws QaApplicationException;

    void removeQaQueContent(QaQueContent qaQuestion) throws QaApplicationException;

    void createQaUsrResp(QaUsrResp qaUsrResp) throws QaApplicationException;

    void updateUserResponse(QaUsrResp resp) throws QaApplicationException;

    QaUsrResp getResponseById(Long responseId) throws QaApplicationException;

    QaQueContent getQuestionContentByQuestionText(final String question, Long contentUid)
	    throws QaApplicationException;

    QaQueContent getQuestionByContentAndDisplayOrder(Long displayOrder, Long contentUid)
	    throws QaApplicationException;

    List getAllQuestionEntriesSorted(final long qaContentId) throws QaApplicationException;

    void saveOrUpdateQaQueContent(QaQueContent qaQuestion) throws QaApplicationException;

    /**
     * Return the qa session object according to the requested session id.
     * 
     * @param qaSessionId
     *                qa session id
     * @return the qa session object
     */
    QaSession retrieveQaSession(long qaSessionId) throws QaApplicationException;

    QaSession getSessionById(long qaSessionId) throws QaApplicationException;

    void createQaSession(QaSession qaSession) throws QaApplicationException;

    List getSessionNamesFromContent(QaContent qaContent) throws QaApplicationException;

    void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    List getSessionsFromContent(QaContent qaContent) throws QaApplicationException;

    QaQueUsr createUser(Long toolSessionID) throws QaApplicationException;

    void updateQaSession(QaSession qaSession) throws QaApplicationException;

    QaQueUsr getQaQueUsrById(long qaQueUsrId) throws QaApplicationException;

    void updateQa(QaContent qa) throws QaApplicationException;

    void createQa(QaContent qa) throws QaApplicationException;

    void updateResponseVisibility(Long responseUid, boolean visible);

    QaContent retrieveQaBySession(long qaSessionId) throws QaApplicationException;

    QaQueUsr getUserByIdAndSession(final Long queUsrId, final Long qaSessionId) throws QaApplicationException;

    void removeUserResponse(QaUsrResp resp) throws QaApplicationException;

    List getAllQuestionEntries(final Long uid) throws QaApplicationException;

    List getUserBySessionOnly(final QaSession qaSession) throws QaApplicationException;

    /**
     * copyToolContent(Long fromContentId, Long toContentId) return void
     * 
     * @param fromContentId
     * @param toContentId
     */
    void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;

    boolean isStudentActivityOccurredGlobal(QaContent qaContent) throws QaApplicationException;

    /**
     * createToolSession(Long toolSessionId,String toolSessionName, Long
     * toolContentId)
     * 
     * It is also defined here since in development we want to be able call it
     * directly from the web-layer instead of it being called by the container.
     * 
     * @param toolSessionId
     * @param toolContentId
     */
    void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException;

    /**
     * leaveToolSession(Long toolSessionId, Long learnerId)
     * 
     * It is also defined here since in development we want to be able call it
     * directly from our web-layer instead of it being called by the container.
     * 
     * @param toolSessionId
     * @param toolContentId
     */
    String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException;

    IToolVO getToolBySignature(String toolSignature) throws QaApplicationException;

    long getToolDefaultContentIdBySignature(String toolSignature) throws QaApplicationException;

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);

    /**
     * Get the LAMS audit service. Needed as the web layer controls the staff
     * updating of an answer, so the log entry must be made by the web layer.
     */
    IAuditService getAuditService();

    void updateEntry(NotebookEntry notebookEntry);

    QaContent getQaContentBySessionId(Long sessionId);

    /**
     * Creates an unique name for a QaCondition. It consists of the tool output
     * definition name and a unique positive integer number.
     * 
     * @param existingConditions
     *                existing conditions; required to check if a condition with
     *                the same name does not exist.
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
     * Saves the entire set of QaWizardCategories (including the child cognitive
     * skills and questions)
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
    
    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     * 
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
    
    /**
     * Return content folder (unique to each learner and lesson) which is used for storing user generated content.
     * It's been used by CKEditor.
     * 
     * @param toolSessionId
     * @param userId
     * @return
     */
    String getLearnerContentFolder(Long toolSessionId, Long userId);
        
    AverageRatingDTO rateResponse(Long responseId, Long userId, Long toolSessionID, float rating);
    
    AverageRatingDTO getAverageRatingDTOByResponse(Long responseId);
    
    /**
     * Return reflection data for all sessions.
     * 
     * @param content
     * @param userID
     * @return
     */
    List<ReflectionDTO> getReflectList(QaContent content, String userID);
    
    /**
     * notifyTeachersOnResponseSubmit
     * 
     * @param sessionId
     */
    void notifyTeachersOnResponseSubmit(Long sessionId);

}
