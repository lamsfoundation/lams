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
package org.lamsfoundation.lams.gradebook.service; 

import java.util.List;

import org.lamsfoundation.lams.gradebook.dto.GBLessonGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradebookGridRowDTO;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
 
public interface IGradebookService {

    
    
    /**
     * Gets all the activity rows for a lesson, with the mark for each activity
     * being the average for all users in the lesson
     * 
     * @param lesson 
     * @return
     */
    public List<GradebookGridRowDTO> getGBActivityRowsForLesson(Lesson lesson);
    
    /**
     * Gets all the activity rows for a user, with the mark for the activity 
     * being the user's individual mark
     * 
     * @param 
     * @param learner 
     * @return
     */
    public List<GradebookGridRowDTO> getGBActivityRowsForLearner(Lesson lesson, User learner);
    
    /**
     * Gets the GBActivityDTO list for an activity, which provides the marks
     * for all users in an activity
     * 
     * @param lesson
     * @param activity
     * @param groupId
     * @return
     */
    public List<GBUserGridRowDTO> getGBUserRowsForActivity(Lesson lesson, ToolActivity activity, Long groupId);
    
    /**
     * Gets the user rows and the user's entire lesson mark for all users 
     * in a lesson
     * @param lesson
     * @return
     */
    public List<GBUserGridRowDTO> getGBUserRowsForLesson(Lesson lesson);

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
    public void updateUserActivityGradebookMark(Lesson lesson, User learner, Activity activity, Double mark);
    
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
     * Gets the lesson row dtos for a given organisation
     * 
     * @param organisation
     * @return
     */
    public List<GBLessonGridRowDTO> getGBLessonRows(Organisation organisation, User user, GBGridView view);
    
}
 
