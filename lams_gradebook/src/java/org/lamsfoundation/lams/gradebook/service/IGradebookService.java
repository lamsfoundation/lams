/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.gradebook.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.dto.GBLessonGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradebookGridRowDTO;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.ExcelCell;

public interface IGradebookService {

    /**
     * Gets all the activity rows for a lesson, with the mark for each activity being the average for all users in the
     * lesson
     *
     * @param lesson
     * @return
     */
    List<GradebookGridRowDTO> getGBActivityRowsForLesson(Long lessonId);

    /**
     * Gets all the activity rows for a user, with the mark for the activity being the user's individual mark
     *
     * @param
     * @param learner
     * @return
     */
    List<GradebookGridRowDTO> getGBActivityRowsForLearner(Long lessonId, Integer userId);

    /**
     * Gets the GBActivityDTO list for an activity, which provides the marks for all users in an activity
     *
     * @param lesson
     * @param activity
     * @param groupId
     * @return
     */
    List<GBUserGridRowDTO> getGBUserRowsForActivity(Lesson lesson, ToolActivity activity, Long groupId, int page,
	    int size, String sortBy, String sortOrder, String searchString);

    /**
     * Gets the user rows and the user's entire lesson mark for all users in a lesson
     *
     * @param lesson
     * @return
     */
    List<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson);

    /**
     * Gets the user rows containing only users' names. Do proper paging on DB side.
     *
     * @param lesson
     * @param page
     * @param size
     * @param sortOrder
     * @param searchString
     * @return
     */
    List<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson, int page, int size, String sortBy, String sortOrder,
	    String searchString);

//    /**
//     * Gets the user rows containing only users' names. Do proper paging on DB side.
//     *
//     * @param lesson
//     * @param page
//     * @param size
//     * @param sortOrder
//     * @param searchString
//     * @return
//     */
//    List<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson, int page, int size, String sortOrder,
//	    String searchString);

    int getCountUsersByLesson(Long lessonId, String searchString);

    /**
     * Gets the user rows for specified organisation
     *
     * @param organisation
     * @return
     */
    ArrayList<GBUserGridRowDTO> getGBUserRowsForOrganisation(Organisation organisation, int page, int size,
	    String sortOrder, String searchString);

    int getCountUsersByOrganisation(Integer orgId, String searchString);
    
    /**
     * Updates all user marks in specified activity. It recalculates all UserActivityGradebooks and UserLessonGradebooks.
     * 
     * @param activity
     */
    void updateUserMarksForActivity(Activity activity);

    /**
     * Updates a user's lesson mark, this will make it desynchronised with the aggregated marks from the activities
     *
     * @param lesson
     * @param learner
     * @param mark
     */
    void updateUserLessonGradebookMark(Lesson lesson, User learner, Double mark);

    /**
     * If specified activity is set to produce ToolOutput, calculates and stores mark to gradebook.
     *
     * @param toolActivity
     * @param progress
     */
    void updateUserActivityGradebookMark(Lesson lesson, Activity activity, User learner);

    /**
     * Updates a user's activity mark, this will automatically add up all the user's activity marks for a lesson and set
     * the lesson mark too
     *
     * @param lesson
     * @param learner
     * @param activity
     * @param mark
     * @param markedInGradebook
     * @param isAuditLogRequired
     *            should this event be logged with audit service
     */
    void updateUserActivityGradebookMark(Lesson lesson, User learner, Activity activity, Double mark,
	    Boolean markedInGradebook, boolean isAuditLogRequired);

    /**
     * Updates the user's feedback for an activity
     *
     * @param activity
     * @param learner
     * @param feedback
     */
    void updateUserActivityGradebookFeedback(Activity activity, User learner, String feedback);

    /**
     * Updates the user's feedback for a lesson
     *
     * @param lesson
     * @param learner
     * @param feedback
     */
    void updateUserLessonGradebookFeedback(Lesson lesson, User learner, String feedback);

    /**
     * Toggle on/off marks released option
     *
     * @param lessonId
     */
    void toggleMarksReleased(Long lessonId);

    /**
     * Gets the lesson row dtos for a given organisation
     *
     * @param organisation
     * @param user
     *            user which results is requested
     * @param viewer
     *            user who view gradebook. We display list of lessons based on his rights.
     * @param view
     * @return
     */
    List<GBLessonGridRowDTO> getGBLessonRows(Organisation organisation, User user, User viewer, GBGridView view,
	    int page, int size, String sortBy, String sortOrder, String searchString);

    /**
     * Gets a gradebook lesson mark/feedback for a given user and lesson
     *
     * @param lessonID
     * @param userID
     * @return
     */
    GradebookUserLesson getGradebookUserLesson(Long lessonID, Integer userID);

    /**
     * Gets a gradebook lesson mark/feedback for all users in a given lesson
     *
     * @param lessonID
     * @return
     */
    List<GradebookUserLesson> getGradebookUserLesson(Long lessonID);

    /**
     * Gets a gradebook activity mark/feedback for a given activity and user
     *
     * @param activityID
     * @param userID
     * @return
     */
    GradebookUserActivity getGradebookUserActivity(Long activityID, Integer userID);

    /**
     * Returns the average mark for a given activity. Activity can be grouped - then supply according groupId to receive
     * AverageMarkForGroupedActivity.
     *
     * @param activityID
     * @param groupID
     *            return AverageMarkForActivity if groupId is null and AverageMarkForGroupedActivity if groupId is
     *            specified
     * @return
     */
    Double getAverageMarkForActivity(Long activityID, Long groupID);

    /**
     * Returns the average mark for a lesson
     *
     * @param lessonID
     * @return
     */
    Double getMedianMarkForLesson(Long lessonID);

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
     * Get an activity from the db by id
     *
     * @param activityID
     * @return
     */
    Activity getActivityById(Long activityID);

    /**
     * Get a language label
     *
     * @param key
     * @return
     */
    String getMessage(String key);

    /**
     * Get the summary data in a 2s array for an excel export
     *
     * @param lesson
     * @return
     */
    LinkedHashMap<String, ExcelCell[][]> exportLessonGradebook(Lesson lesson);

    /**
     * Get the summary data for course in order to create excel export
     *
     * @param userId
     * @param organisationId
     * @return
     */
    LinkedHashMap<String, ExcelCell[][]> exportCourseGradebook(Integer userId, Integer organisationId);

    /**
     * Get the summary data for selected lessons in order to create excel export
     *
     * @param userId
     * @param organisationId
     * @return
     */
    LinkedHashMap<String, ExcelCell[][]> exportSelectedLessonsGradebook(Integer userId, Integer organisationId,
	    String[] lessonIds);
}