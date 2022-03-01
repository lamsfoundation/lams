/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.confidencelevel.VsaAnswerDTO;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.util.FileUtilException;

/**
 * This interface defines all the service available for self contained tool module from lams. Any service that would be
 * used by other lams module such as, lams_learning etc, should not appear in this interface.
 *
 * @author chris
 * @author Jacky Fang
 * @author Ozgur Demirtas 24/06/2005
 */
public interface ILamsToolService {
    public static final String LEADER_SELECTION_TOOL_SIGNATURE = "lalead11";

    Tool getToolByID(Long toolId);

    Tool getToolBySignature(final String toolSignature);

    long getToolDefaultContentIdBySignature(final String toolSignature);

    String generateUniqueContentFolder() throws FileUtilException, IOException;

    /**
     * Return content folder (unique to each learner and lesson) which is used for storing user generated content. It's
     * been used by CKEditor.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String getLearnerContentFolder(Long toolSessionId, Long userId);

    void saveOrUpdateTool(Tool tool);

    Tool getPersistToolBySignature(final String toolSignature);

    /**
     * Get the tool session object using the toolSessionId
     *
     * @param toolSessionId
     * @return
     */
    ToolSession getToolSession(Long toolSessionId);

    /**
     * Marks an tool session as complete and calculates the next activity against the learning design. This method is
     * for tools to redirect the client on complete.
     *
     * Do not change learnerId to Integer (to match the other calls) as all the tools expect this to be a Long.
     *
     * @param toolSessionId
     *            , session ID for completed tool
     * @param learnerId
     *            the learner who is completing the tool session.
     * @return the URL for the next activity
     * @throws LearnerServiceException
     *             in case of problems.
     */
    String completeToolSession(Long toolSessionId, Long learnerId);

    /**
     * Checks whether specified activity is the last one in the learning design.
     */
    boolean isLastActivity(Long toolSessionId);

    /**
     * Method for updating an activity mark that tools can call
     *
     * @param mark
     * @param feedback
     * @param userID
     * @param toolSessionID
     */
    void updateActivityMark(Double mark, String feedback, Integer userID, Long toolSessionID,
	    Boolean markedInGradebook);

    /**
     * Delete user activity mark and updates aggregates
     */
    void removeActivityMark(Integer userID, Long toolSessionID);

    /**
     * Allows the tool to ask whether or not the activity is grouped and therefore it should expect more than one tool
     * session.
     *
     * @param toolContentID
     * @return
     */
    Boolean isGroupedActivity(long toolContentID);

    Group getGroup(long toolSessionId);

    /**
     * Audit log the teacher has started editing activity in monitor.
     *
     * @param toolContentID
     */
    void auditLogStartEditingActivityInMonitor(long toolContentID);

    /**
     * Assign tool an ActivityEvaluation (it is an equivalent of doing it manually on authoring canvas). It also updates
     * gradebook marks for all participating learners from the lesson.
     *
     * @param toolContentId
     * @param toolOutputDefinition
     */
    void setActivityEvaluation(Long toolContentId, String toolOutputDefinition);

    /**
     * Gets the concreted tool output (not the definition) from a tool. This method is called by target tool in order to
     * get data from source tool.
     */
    ToolOutput getToolInput(Long requestingToolContentId, Integer assigmentId, Integer learnerId);

    /**
     * Get tool's ActivityEvaluation that has been set in authoring.
     *
     * @param toolContentId
     * @return
     */
    String getActivityEvaluation(Long toolContentId);

    /**
     * Returns leader's UserId from the nearest Leader Selection Tool (the nearest to the specified activity) , and null
     * if no Leader Selection Tools available.
     *
     * @param toolSessionId
     *            sessionId of the specified activity
     * @param learnerId
     *            userId (used to get appropriate Leader Selection Tool's session)
     * @return
     */
    Long getLeaderUserId(Long toolSessionId, Integer learnerId);

    /**
     * Returns all leaders available in the nearest leader selection tool (that is all leaders in all sessions).
     *
     * @param toolSessionId
     * @param learnerId
     * @return
     */
    Set<Long> getAllLeaderUserIds(Long toolSessionId, Integer learnerId);

    /**
     * Returns leaders' userIds for all tool sessions from the given Leader Selection Tool.
     */
    Set<Long> getLeaderUserId(Long leaderSelectionActivityId);

    Long getNearestLeaderSelectionToolContentId(long toolSessionId);

    /**
     * Returns all activities that precede specified activity and produce confidence levels.
     *
     * @param toolContentId
     *            toolContentId of the specified activity
     * @return
     */
    Set<ToolActivity> getActivitiesProvidingConfidenceLevels(Long toolContentId);

    /**
     * Returns all activities that precede specified activity and can provide VSA answers.
     *
     * @param toolContentId
     *            toolContentId of the specified activity
     * @return
     */
    Set<ToolActivity> getActivitiesProvidingVsaAnswers(Long toolContentId);

    /**
     * Returns confidence levels from the specified activity.
     *
     * @param activityUiid
     *            activityUiid of the activity with confidence levels
     * @param requestorUserId
     *            userId of the requesting user. we need it in order to get confidence level providing tool's sessionId
     * @param requestorToolSessionId
     *            toolSessionId of the activity that calls this method
     * @return
     */
    List<ConfidenceLevelDTO> getConfidenceLevelsByActivity(Integer confidenceLevelActivityUiid, Integer requestorUserId,
	    Long requestorToolSessionId);

    /**
     * Returns VSA answers from the specified activity.
     *
     * @param activityUiid
     *            activityUiid of the activity providing VSA answers
     * @param requestorUserId
     *            userId of the requesting user. we need it in order to get tool's sessionId providing VSA answers
     * @param requestorToolSessionId
     *            toolSessionId of the activity that calls this method
     * @return
     */
    Collection<VsaAnswerDTO> getVsaAnswersFromAssessment(Integer activityUiidProvidingVsaAnswers,
	    Integer requestorUserId, Long requestorToolSessionId);

    /**
     * Returns VS answers which require allocation
     */
    Map<QbToolQuestion, Map<String, Integer>> getUnallocatedVSAnswers(long toolContentId);

    /**
     * Recalculate marks for all activities that use specified QbQuestion with given answer
     *
     * @return whether there is a learner who used the given answer
     */
    boolean recalculateMarksForVsaQuestion(Long qbQuestionUid, String answer);

    /**
     * Get a count of all the users that would be returned by getUsersForActivity(Long toolSessionId);
     */
    Integer getCountUsersForActivity(Long toolSessionId);
    
    /**
     * Updates TBL iRAT/tRAT activity with questions from matching tRAT/iRAT activity
     */
    boolean syncRatQuestions(long toolContentId, List<Long> newQuestionUids);
}