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
 * @author  chris, Jacky
 */
public class ProgressEngine
{

    /**
     * Method determines next step for a learner based on the activity
     * they have just completed.
     * @param learner The <CODE>User</CODE> who is progressing through the <CODE>Lesson</CODE>.
     * @param completedActivity The <CODE>Activity</CODE> the learner has just completed.
     * @param lesson The <CODE>Lesson</CODE> the learner needs progress for.
     * @param learnerProgress
     * @return Progress The VO that contains the data needed to send 
     * the learner to the next step.
     * @throws ProgressException if progress cannot be calculated successfully.
     */
    public LearnerProgress calculateProgress(User learner,
                                             Lesson lesson,
                                             Activity completedActivity,
                                             LearnerProgress learnerProgress) throws ProgressException
    {
        learnerProgress.setProgressState(completedActivity,
                                         LearnerProgress.ACTIVITY_COMPLETED);

        Transition transition = completedActivity.getTransitionFrom();

        if (transition != null)
            return progressCompletedActivity(completedActivity,
                                             learnerProgress,
                                             transition);
        else
            return progressParentActivity(learner,
                                          lesson,
                                          completedActivity,
                                          learnerProgress);
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
        
        if(ld.getFirstActivity()==null)
            throw new ProgressException("Could not find first activity for " 
                                        +"learning design ["+ld.getTitle()+"], id["
                                        +ld.getLearningDesignId().longValue()
                                        +"]");
        
        progress.setCurrentActivity(ld.getFirstActivity());
        progress.setNextActivity(ld.getFirstActivity());
        progress.setProgressState(ld.getFirstActivity(),LearnerProgress.ACTIVITY_ATTEMPTED);
    }
    /**
     * 
     * @param completedActivity
     * @param learnerProgress
     * @param transition
     * @return
     */
    private LearnerProgress progressCompletedActivity(Activity completedActivity,
                                                      LearnerProgress learnerProgress,
                                                      Transition transition)
    {
        learnerProgress.setPreviousActivity(completedActivity);
        learnerProgress.setCurrentActivity(transition.getToActivity());
        
        //we set the next activity to be the first child activity if it
        //is a sequence activity.
        if(transition.getToActivity().isSequenceActivity())
        {
            Activity firstActivityInSequence = 
                ((SequenceActivity)transition.getToActivity()).getFirstActivityInSequenceActivity();
            learnerProgress.setNextActivity(firstActivityInSequence);
        }
        //set next activity as the activity follows the transition.
        else
            learnerProgress.setNextActivity(transition.getToActivity());
        learnerProgress.setProgressState(transition.getToActivity(),
                                         LearnerProgress.ACTIVITY_ATTEMPTED);
        learnerProgress.setParallelWaiting(false);
        return learnerProgress;
    }

    /**
     * @param learner
     * @param lesson
     * @param completedActivity
     * @param learnerProgress
     * @return
     * @throws ProgressException
     */
    private LearnerProgress progressParentActivity(User learner,
                                                   Lesson lesson,
                                                   Activity completedActivity,
                                                   LearnerProgress learnerProgress) throws ProgressException
    {
        Activity parent = completedActivity.getParentActivity();
        if (parent != null)
        {
            //move to next activity within parent if not all children are completed.
            if (!parent.areChildrenCompleted(learnerProgress))
            {
                Activity nextActivity = parent.getNextActivityByParent(completedActivity);
                
                
                if (!isNextActivityValid(nextActivity))
                    throw new ProgressException("Error occurred in progress engine."
                            + " Unexpected Null activity received when progressing"
                            + " to the next activity within a incomplete parent activity:"
                            + " Parent activity id ["
                            + parent.getActivityId()
                            + "]");
                
                if(isParallelWaitActivity(nextActivity))
                {
                    learnerProgress.setParallelWaiting(true);
                    learnerProgress.setNextActivity(null);
                }
                else
                {
                    learnerProgress.setParallelWaiting(false);
                    learnerProgress.setNextActivity(nextActivity);
                    learnerProgress.setProgressState(nextActivity,
                                                     LearnerProgress.ACTIVITY_ATTEMPTED);
                }
            }
            //recurvisely call back to calculateProgress to calculate completed
            //parent activity.
            else
                calculateProgress(learner, lesson, parent, learnerProgress);
        }
        //lesson is meant to be completed if there is no transition and no parent.
        else
            learnerProgress.setLessonComplete(true);

        return learnerProgress;
    }

    /**
     * @param nextActivity
     * @return
     */
    private boolean isNextActivityValid(Activity nextActivity)
    {
        return !nextActivity.isNull()||isParallelWaitActivity(nextActivity);
    }

    private boolean isParallelWaitActivity(Activity nextActivity)
    {
        return nextActivity.getActivityTypeId().intValue()==ParallelWaitActivity.PARALLEL_WAIT_ACTIVITY_TYPE;
    }
}