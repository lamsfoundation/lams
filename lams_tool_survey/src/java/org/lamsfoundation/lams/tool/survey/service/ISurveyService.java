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

package org.lamsfoundation.lams.tool.survey.service;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.tool.survey.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;
import org.lamsfoundation.lams.tool.survey.model.SurveyCondition;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;

/**
 * @author Dapeng.Ni
 *
 *         Interface that defines the contract that all Survey service provider must follow.
 */
public interface ISurveyService extends ICommonToolService {

    // ******************************************************************************************
    // Content methods
    // ******************************************************************************************
    /**
     * Get <code>Survey</code> by toolContentID.
     *
     * @param contentId
     * @return
     */
    Survey getSurveyByContentId(Long contentId);

    /**
     * Get survey which is relative with the special toolSession.
     *
     * @param sessionId
     * @return
     */
    Survey getSurveyBySessionId(Long sessionId);

    /**
     * Get a cloned copy of tool default tool content (Survey) and assign the toolContentId of that copy as the given
     * <code>contentId</code>
     *
     * @param contentId
     * @return
     * @throws SurveyApplicationException
     */
    Survey getDefaultContent(Long contentId) throws SurveyApplicationException;

    /**
     * Save or update survey into database.
     *
     * @param Survey
     */
    void saveOrUpdateSurvey(Survey Survey);

    // ******************************************************************************************
    // *************** Instruction file methods **********************
    // ******************************************************************************************

    // ******************************************************************************************
    // *************** Questions and Answers methods **********************
    // ******************************************************************************************
    SurveyQuestion getQuestion(Long questionUid);

    /**
     * Delete resoruce item from database.
     *
     * @param uid
     */
    void deleteQuestion(Long uid);

    /**
     * Get a learner's all answers in a survey.
     *
     * @param sessionId
     * @param userUid
     * @return
     */
    List<AnswerDTO> getQuestionAnswers(Long sessionId, Long userUid);

    /**
     * Get question's answer with response percentage infromation.
     *
     * @param sessionId
     * @param questionUid
     * @return
     */
    AnswerDTO getQuestionResponse(Long sessionId, Long questionUid);

    List<String> getOpenResponsesForTablesorter(final Long qaSessionId, final Long questionId, int page, int size,
	    int sorting);

    int getCountResponsesBySessionAndQuestion(final Long qaSessionId, final Long questionId);

    /**
     * Commit answers for a group of question together.
     *
     * @param answerList
     */
    void updateAnswerList(List<SurveyAnswer> answerList);

    // ******************************************************************************************
    // ********** user methods *************
    // ******************************************************************************************
    /**
     * Create a new user in database.
     */
    void createUser(SurveyUser surveyUser);

    /**
     * Get user by given userID and toolContentID.
     *
     * @param long1
     * @return
     */
    SurveyUser getUserByIDAndContent(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     *
     * @param long1
     * @param sessionId
     * @return
     */
    SurveyUser getUserByIDAndSession(Long long1, Long sessionId);

    /**
     * Get user by UID
     *
     * @param uid
     * @return
     */
    SurveyUser getUser(Long uid);

    /**
     * Get all users under one tool session.
     *
     * @param sessionId
     * @return
     */
    List<SurveyUser> getSessionUsers(Long sessionId);

    /**
     * Get number of finished users.
     *
     * @param sessionId
     * @return
     */
    int getCountFinishedUsers(Long sessionId);

    // ******************************************************************************************
    // ********** Session methods ***********************
    // ******************************************************************************************

    /**
     * Get survey toolSession by toolSessionId
     *
     * @param sessionId
     * @return
     */
    SurveySession getSurveySessionBySessionId(Long sessionId);

    /**
     * Save or update survey session.
     *
     * @param resSession
     */
    void saveOrUpdateSurveySession(SurveySession resSession);

    /**
     * If success return next activity's url, otherwise return null.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws SurveyApplicationException;

    /**
     * Mark user as finalized response which will signify he has submitted response.
     *
     * @param user
     */
    void setResponseFinalized(Long userUid);

    // ******************************************************************************************
    // Monitoring summary /statistic methods
    // ******************************************************************************************

    /**
     * Return a map which sorted by SurveySession and list of questions for this session.
     */

    SortedMap<SurveySession, List<AnswerDTO>> getSummary(Long contentId);

    /**
     * Return a map which sorted by SurveySesson and a list of total user count in this session.
     *
     * @param contentId
     * @return
     */
    SortedMap<SurveySession, Integer> getStatistic(Long contentId);

    // This export for exporting Excel format file in Survey monitoring summary page:
    SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> exportBySessionId(Long toolSessionID);

    void notifyTeachersOnAnswerSumbit(Long sessionId, SurveyUser surveyUser);

    /**
     * Creates an unique name for a SurveyCondition. It consists of the tool output definition name and a unique
     * positive integer number.
     *
     * @param existingConditions
     *            existing conditions; required to check if a condition with the same name does not exist.
     * @return unique SurveyCondition name
     */
    String createConditionName(Collection<SurveyCondition> existingConditions);

    void deleteCondition(SurveyCondition condition);

    /**
     * Gets the Answer information for the monitoring listAnswers tablesorter. Will return List<[SurveyUser, String,
     * String], [SurveyUser, String, String], ... , [SurveyUser, String, String]> where the first String is answer
     * choices (for multiple choice) and the second String is the answer text for free entry choice.
     */
    List<Object[]> getQuestionAnswersForTablesorter(Long sessionId, Long questionId, int page, int size, int sorting,
	    String searchString);

    int getCountUsersBySession(final Long sessionId, String searchString);

}
