/*
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
*/

package org.lamsfoundation.lams.learning.service;

import java.util.List;

import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.tool.service.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
/**
 *
 * @author chris
 */
public interface ILearnerService
{



    /**
     * Returns a list of all the active Lessons a User is a Learner in.
     * @param User the learner
     * @return List of Lessons
     * @throws LearnerServiceException in case of problems.
     */
    public List getActiveLessons(User learner);


    
    /**
     * Gets the lesson object for the given key.
     *
     */
    public Lesson getLesson(Long lessonID);

    
 
    /**
     * Joins a User to a a new lesson as a learner
     * @param learner the Learner
     * @param lessionID identifies the Lesson to start
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress joinLesson(User learner, Lesson lesson) throws ProgressException,LamsToolServiceException;
    

    /**
     * Returns the current progress data of the User.
     * @param learner the Learner
     * @param lesson the Lesson to get progress from.
     * @return LearnerProgess contains the learner's progress for the lesson.
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress getProgress(User learner, Lesson lesson);
    
    
    /**
     * Marks an activity as attempted. Called when a user selects an OptionsActivity.
     * @param learner the Learner
     * @param lesson the Lesson to get progress from.
     * @param activity the activity being attempted.
     * @return LearnerProgress
     */
    public LearnerProgress chooseActivity(User learner, Lesson lesson, Activity activity);

    
    /**
     * Calculates learner progress and returns the data required to be displayed to the learner (including URL(s)).
     * @param completedActivityID identifies the activity just completed
     * @param learner the Learner
     * @param lesson the Lesson in progress.
     * @return the bean containing the display data for the Learner
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress calculateProgress(Activity completedActivity, User learner, Lesson lesson) throws ProgressException;

    
    /**
     * Marks an activity as complete and calculates the next URL. This method is for
     * tools to redirect the client on complete.
     * @param toolSessionId, session ID for completed tool
     * @return the URL for the next activity
     * @throws LearnerServiceException in case of problems.
     */
    public String completeToolActivity(long toolSessionId);
    
}
