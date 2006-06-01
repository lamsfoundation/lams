/****************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.learning.progress;

import java.util.LinkedList;
import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.ParallelWaitActivity;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * The Progress Engine controls how a learner progresses
 * through a sequence.
 *
 * @author  chris, Jacky
 */
public class ProgressEngine
{
	
    private IActivityDAO activityDAO;

    /**
     * Holds a list of completed activity ids in one recursive move to next
     * task calculation. For example, if a parallel activity A has two children
     * B,C. Assume B has been finished first. The complete of C will also
     * result in the completion of A. Therefore, <code>completedActivityList</code>
     * will hold B and C.
     */
    private List completedActivityList = new LinkedList();
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
                                             Activity completedActivity,
                                             LearnerProgress learnerProgress) throws ProgressException
    {
        learnerProgress.setProgressState(completedActivity,
                                         LearnerProgress.ACTIVITY_COMPLETED);
        completedActivityList.add(completedActivity.getActivityId());
        Transition transition = completedActivity.getTransitionFrom();

        if (transition != null)
            return progressCompletedActivity(completedActivity,
                                             learnerProgress,
                                             transition);
        else
            return progressParentActivity(learner,
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
     * We setup the progress data for a completed activity. This happens when
     * we find a transition to progress to. It should setup all activity states
     * that allow web layer to calculate the url to move one to.
     * 
     * @param completedActivity the activity finished either by user or the 
     * 	lams. In terms of lams completed activity, it would be 
     *  <code>ParallelActivity</code>, <code>SequenceActivity</code>, 
     *  <code>OptionsActivity</code> and other system driven activities. 
     *  Whereas user activity will be mostly tool activities.
     * @param learnerProgress the progress we based on.
     * @param transition the transition we progress to.
     * @return the learner progress data we calculated.
     */
    private LearnerProgress progressCompletedActivity(Activity completedActivity,
                                                      LearnerProgress learnerProgress,
                                                      Transition transition)
    {
        learnerProgress.setPreviousActivity(completedActivity);
        
        populateCurrentCompletedActivityList(learnerProgress);
        
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
     * Calculate the progress data for a parent activity if we can not find the
     * transition for a completed activity. Most likely, the completed activity
     * is in the leaf node of an activity hierarchy. And we need to travesal the
     * activity hierarchy upwards to find the progress information.
     * 
     * @param learner the current learner.
     * @param lesson the lesson that current learner progress belongs to.
     * @param completedActivity the activity finished either by user or the 
     * 	lams. In terms of lams completed activity, it would be 
     *  <code>ParallelActivity</code>, <code>SequenceActivity</code>, 
     *  <code>OptionsActivity</code> and other system driven activities. 
     *  Whereas user activity will be mostly tool activities.
     * @param learnerProgress the progress we based on.
     * @return the learner progress data we calculated.
     * @throws ProgressException
     */
    private LearnerProgress progressParentActivity(User learner,
                                                   Activity completedActivity,
                                                   LearnerProgress learnerProgress) throws ProgressException
    {
        Activity parent = completedActivity.getParentActivity();
        
        if (parent != null)
        {
            if(!(parent.isComplexActivity()))
                throw new ProgressException("Parent activity is always expected" +
                		" to the complex activity. But activity type"+
                		parent.getActivityTypeId()+" has been found");
            //move to next activity within parent if not all children are completed.
            
            ComplexActivity complexParent = (ComplexActivity) activityDAO.getActivityByActivityId(parent.getActivityId(),ComplexActivity.class);
            if (!complexParent.areChildrenCompleted(learnerProgress))
            {
                Activity nextActivity = complexParent.getNextActivityByParent(completedActivity);
                
                
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
                    // learnerProgress.setNextActivity(null);
                    populateCurrentCompletedActivityList(learnerProgress);
                }
                else
                {
                    learnerProgress.setParallelWaiting(false);
                    learnerProgress.setNextActivity(nextActivity);
                    learnerProgress.setProgressState(nextActivity,
                                                     LearnerProgress.ACTIVITY_ATTEMPTED);
                    populateCurrentCompletedActivityList(learnerProgress);
                }
            }
            //recurvisely call back to calculateProgress to calculate completed
            //parent activity.
            else
                calculateProgress(learner, parent, learnerProgress);
        }
        //lesson is meant to be completed if there is no transition and no parent.
        else
            learnerProgress.setLessonComplete(true);

        return learnerProgress;
    }

    /**
     * The helper method to setup the completed activity list since the last
     * transition. 
     * @param learnerProgress
     */
    private void populateCurrentCompletedActivityList(LearnerProgress learnerProgress)
    {
        learnerProgress.setCurrentCompletedActivitiesList(completedActivityList);
        completedActivityList.clear();
    }
    
    /**
     * The next valid is valid if it is not null activity or if it is a parallel
     * waiting activity.
     * @param nextActivity the next activity we progress to.
     * @return is the next activity valid.
     */
    private boolean isNextActivityValid(Activity nextActivity)
    {
        return !nextActivity.isNull()||isParallelWaitActivity(nextActivity);
    }

    /**
     * Check up the object type to see whether it is a parallel waiting 
     * activity.
     * @param nextActivity the next activity we progress to.
     * @return is the next activity the type of parallel activity.
     */
    private boolean isParallelWaitActivity(Activity nextActivity)
    {
        return nextActivity.getActivityTypeId().intValue()==ParallelWaitActivity.PARALLEL_WAIT_ACTIVITY_TYPE;
    }

	public void setActivityDAO(IActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}
}