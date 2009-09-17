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
 
/* $Id$ */ 
package org.lamsfoundation.lams.cloud.service; 

import org.lamsfoundation.lams.gradebook.GradebookUserActivity;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
 
public interface ICloudService {


    /**
     * Updates a user's lesson mark, this will make it desynchronised with the
     * aggregated marks from the activities
     * 
     * @param lesson
     * @param learner
     * @param mark
     */
    public void updateUserLessonGradebookMark(Lesson lesson, User learner, Double mark);
    
    /**
     * Updates a user's activity mark, this will automatically add up all the 
     * user's activity marks for a lesson and set the lesson mark too
     * 
     * @param lesson
     * @param learner
     * @param activity
     * @param mark
     */
    public void updateUserActivityGradebookMark(Lesson lesson, User learner, Activity activity, Double mark, Boolean markedInGradebook);
    
    /**
     * Updates the user's feedback for an activity
     * 
     * @param activity
     * @param learner
     * @param feedback
     */
    public void updateUserActivityGradebookFeedback(Activity activity, User learner, String feedback);
    
    /**
     * Updates the user's feedback for a lesson
     * 
     * @param lesson
     * @param learner
     * @param feedback
     */
    public void updateUserLessonGradebookFeedback(Lesson lesson, User learner, String feedback);
    
    /**
     * Gets a gradebook lesson mark/feedback for a given user and lesson
     * 
     * @param lessonID
     * @param userID
     * @return
     */
    public GradebookUserLesson getGradebookUserLesson(Long lessonID, Integer userID);
    
    /**
     * Gets a gradebook activity mark/feedback for a given activity and user
     * 
     * @param activityID
     * @param userID
     * @return
     */
    public GradebookUserActivity getGradebookUserActivity(Long activityID, Integer userID);
    
    /**
     * Returns the average mark for a given activity
     * 
     * @param activityID
     * @return
     */
    public Double getAverageMarkForActivity(Long activityID);
    
    /**
     * Returns the average mark for a lesson
     * 
     * @param lessonID
     * @return
     */
    public Double getAverageMarkForLesson(Long lessonID);
    
    /**
     * Method for updating an activity mark that tools can call
     * 
     * @param mark
     * @param feedback
     * @param userID
     * @param toolSessionID
     */
    public void updateActivityMark(Double mark, String feedback, Integer userID, Long toolSessionID, Boolean markedInGradebook);
    
    /**
     * Get an activity from the db by id
     * 
     * @param activityID
     * @return
     */
    public Activity getActivityById(Long activityID);
    
    /**
     * Get a language label
     * @param key
     * @return
     */
    public String getMessage(String key);
    
}
 
