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

package org.lamsfoundation.lams.learning.progress;


import org.lamsfoundation.lams.learningdesign.*;
import org.lamsfoundation.lams.lesson.*;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * The Progress Engine controls how a learner progresses
 * through a sequence.
 *
 * @author  chris
 */
public class ProgressEngine
{
    
    /**
     * Creates a new instance of ProgressEngine.
     */
    public ProgressEngine()
    {
    }
    
    /**
     * Method determines next step for a learner based on the activity
     * they have just completed.
     * @param learner The <CODE>User</CODE> who is progressing through the <CODE>Lesson</CODE>.
     * @param completedActivity The <CODE>Activity</CODE> the learner has just completed.
     * @param lesson The <CODE>Lesson</CODE> the learner needs progress for.
     * @return Progress The VO that contains the data needed to send 
     * the learner to the next step.
     * @throws ProgressException if progress cannot be calculated successfully.
     */
    public LearnerProgress calculateProgress(User learner, 
                                             Lesson lesson, 
                                             Activity completedActivity,
                                             LearnerProgress learnerProgress) throws ProgressException
    {
        learnerProgress.setProgressState(completedActivity,LearnerProgress.ACTIVITY_COMPLETED);
        
        Transition transition = completedActivity.getTransitionTo();
        
        if(transition !=null)
            return progressCompletedActivity(completedActivity, learnerProgress, transition);
    	else
    	{
    	    Activity parent = completedActivity.getParentActivity();
    	    //if(parent!=null)
    	        
    	        
    	}
    	return null;
    }
    
    /**
     * @param completedActivity
     * @param learnerProgress
     * @param transition
     * @return
     */
    private LearnerProgress progressCompletedActivity(Activity completedActivity, LearnerProgress learnerProgress, Transition transition)
    {
        learnerProgress.setPreviousActivity(completedActivity);
        learnerProgress.setCurrentActivity(transition.getActivityByToActivityId());
        learnerProgress.setNextActivity(transition.getActivityByToActivityId());
        learnerProgress.setProgressState(transition.getActivityByToActivityId(),
                                         LearnerProgress.ACTIVITY_ATTEMPTED);
        return learnerProgress;
    }

    /**
     * Method determines the start point for a learner when they begin a Lesson.
     * @param learner the <CODE>User</CODE> who is starting the <CODE>Lesson</CODE>.
     * @param lesson the <CODE>Lesson</CODE> the learner is starting.
     * @throws ProgressException if the start point cannot be calculated successfully.
     */
    public void setUpStartPoint(User learner, Lesson lesson,
                                LearnerProgress progress) throws ProgressException
    {
        
        LearningDesign ld = lesson.getLearningDesign();
        
        progress.setCurrentActivity(ld.getFirstActivity());
        
        progress.setNextActivity(ld.getFirstActivity());
    
        progress.setProgressState(ld.getFirstActivity(),LearnerProgress.ACTIVITY_ATTEMPTED);
    }
    
}
