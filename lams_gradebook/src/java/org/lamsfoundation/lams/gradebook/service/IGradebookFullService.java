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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.dto.GBLessonGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradebookGridRowDTO;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.quartz.SchedulerException;

public interface IGradebookFullService extends IGradebookService {

    static final DateFormat RELEASE_MARKS_EMAIL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Gets all the activity rows for a lesson, with the mark for each activity being the average for all users in the
     * lesson. Only set escapeTitles to false if the output is *not* going to a webpage, but is instead going to a
     * spreadsheet.
     *
     * @param lesson
     * @return
     */
    List<GradebookGridRowDTO> getGBActivityRowsForLesson(Long lessonId, TimeZone userTimezone, boolean escapeTitles);

    List<GradebookGridRowDTO> getGBActivityArchiveRowsForLearner(Long activityId, Integer userId,
	    TimeZone userTimezone);

    /**
     * Gets all the activity rows for a user, with the mark for the activity being the user's individual mark
     *
     * @param
     * @param learner
     * @return
     */
    List<GradebookGridRowDTO> getGBActivityRowsForLearner(Long lessonId, Integer userId, TimeZone userTimezone);

    /**
     * Gets the GBActivityDTO list for an activity, which provides the marks for all users in an activity
     *
     * @param lesson
     * @param activity
     * @param groupId
     * @return
     */
    List<GBUserGridRowDTO> getGBUserRowsForActivity(Lesson lesson, ToolActivity activity, Long groupId, int page,
	    int size, String sortBy, String sortOrder, String searchString, TimeZone userTimezone);

    /**
     * Gets the user rows and the user's entire lesson mark for all users in a lesson
     *
     * @param lesson
     * @return
     */
    List<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson, TimeZone userTimezone);

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
	    String searchString, TimeZone userTimezone);

    /**
     * Returns output for gradebook on lesson complete.
     */
    List<GradebookGridRowDTO> getGBLessonComplete(Long lessonId, Integer userId);

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
     * Updates all user marks in specified activity. It recalculates all UserActivityGradebooks and
     * UserLessonGradebooks.
     *
     * @param activity
     */
    @Override
    void recalculateGradebookMarksForActivity(Activity activity);

    /**
     * Recalculates total marks for all users in a lesson. Then stores that mark in a gradebookUserLesson. Doesn't
     * affect anyhow gradebookUserActivity objects. If total mark is positive but there is no gradebookUserLesson
     * available - throws exception.
     *
     * @param lessonId
     * @throws Exception
     */
    @Override
    void recalculateTotalMarksForLesson(Long lessonId) throws Exception;

    /**
     * Updates a user's lesson mark, this will make it desynchronised with the aggregated marks from the activities
     *
     * @param lesson
     * @param learner
     * @param mark
     */
    void updateUserLessonGradebookMark(Lesson lesson, User learner, Double mark);

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
    void updateGradebookUserActivityMark(Lesson lesson, User learner, Activity activity, Double mark,
	    Boolean markedInGradebook, boolean isAuditLogRequired);

    /**
     * Updates the user's feedback for an activity
     *
     * @param activity
     * @param learner
     * @param feedback
     */
    void updateGradebookUserActivityFeedback(Activity activity, User learner, String feedback);

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
     */
    void toggleMarksReleased(Long lessonId);

    boolean releaseMarks(Long lessonId, int userId);

    Date getReleaseMarksScheduleDate(long lessonId, int currentUserId);

    void scheduleReleaseMarks(long lessonId, int currentUserId, boolean sendEmails, Date scheduleDate)
	    throws SchedulerException;

    String getReleaseMarksEmailContent(long lessonId, int userID);

    void sendReleaseMarksEmails(long lessonId, Collection<Integer> recipientIDs,
	    IEventNotificationService eventNotificationService);

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
    List<GBLessonGridRowDTO> getGBLessonRows(Organisation organisation, User user, User viewer, boolean isGroupManager,
	    GBGridView view, int page, int size, String sortBy, String sortOrder, String searchString,
	    TimeZone userTimeZone);

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
    Double getAverageMarkForLesson(Long lessonID);

    /**
     * Get an activity from the db by id
     *
     * @param activityID
     * @return
     */
    Activity getActivityById(Long activityID);

    @Override
    void removeLearnerFromLesson(Long lessonId, Integer learnerId);

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
    List<ExcelSheet> exportLessonGradebook(Lesson lesson);

    /**
     * Get the summary data for course in order to create excel export
     *
     * @param userId
     * @param organisationId
     * @return
     */
    List<ExcelSheet> exportCourseGradebook(Integer userId, Integer organisationId);

    /**
     * Get the summary data for selected lessons in order to create excel export
     *
     * @param userId
     * @param organisationId
     * @return
     */
    List<ExcelSheet> exportSelectedLessonsGradebook(Integer userId, Integer organisationId, String[] lessonIds,
	    boolean simplified);

    /**
     * Get the raw overall marks for a lesson for charting purposes
     *
     * @param lessonId
     * @return
     */
    List<Number> getMarksArray(Long lessonId);

    /** Will the marks caculation take into account weighting? */
    boolean isWeightedMarks(Long lessonId);

    /** Get a summary of the weightings */
    List<String[]> getWeights(LearningDesign design);
}