package org.lamsfoundation.lams.gradebook.service;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

public interface IGradebookService {

    /**
     * Gets all available gradebookUserActivity objects for the specified activity
     *
     * @param activityID
     * @param userID
     * @return
     */
    List<GradebookUserActivity> getGradebookUserActivities(Long activityId);

    GradebookUserActivity getGradebookUserActivity(Long activityID, Integer userID);

    /**
     * If specified activity is set to produce ToolOutput, calculates and stores mark to gradebook.
     *
     * @param toolActivity
     * @param progress
     */
    void updateGradebookUserActivityMark(Lesson lesson, Activity activity, User learner);

    /**
     * Method for updating an activity mark that tools can call
     *
     * @param mark
     * @param feedback
     * @param userID
     * @param toolSessionID
     */
    void updateGradebookUserActivityMark(Double mark, String feedback, Integer userID, Long toolSessionID,
	    Boolean markedInGradebook);

    /**
     * Updates all user marks in specified activity. It recalculates all UserActivityGradebooks and
     * UserLessonGradebooks.
     *
     * @param activity
     */
    void recalculateGradebookMarksForActivity(Activity activity);

    /**
     * Recalculates total marks for all users in a lesson. Then stores that mark in a gradebookUserLesson. Doesn't
     * affect anyhow gradebookUserActivity objects. If total mark is positive but there is no gradebookUserLesson
     * available - throws exception.
     *
     * @param lessonId
     * @throws Exception
     */
    void recalculateTotalMarksForLesson(Long lessonId) throws Exception;

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

    void removeLearnerFromLesson(Long lessonId, Integer learnerId);

    void removeActivityMark(Long toolContentID);

    /**
     * Delete user activity mark and updates aggregates
     */
    void removeActivityMark(Integer userID, Long toolSessionID);

    void archiveLearnerMarks(Long lessonId, Integer learnerId, Date archiveDate);

}
