/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.lesson;

import java.io.Serializable;
import java.util.Iterator;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;


/**
 * Typical implementation of strategy pattern avoid complicated conditional
 * logic to process activity object hierarchy. It is responsible for all
 * polymorphism caculation regarding different type of activity. 
 * 
 * @author Jacky Fang 2005-2-23
 * 
 */
public abstract class ActivityStrategy implements Serializable
{
    /**
     * <p>Check up all children completion status for a complex activity. </p>
     * Precondition: the activity should a complex activity that has children.
     * 
     * @param activity the complex activity
     * @param learnerProgress the progress data that record what has been 
     * 						  completed
     * @return true if all children are completed.
     */
    public boolean areChildrenCompleted(Activity activity, LearnerProgress learnerProgress)
    {
        ComplexActivity complexActivity = (ComplexActivity)activity;
        
        int numOfCompletedActivities=0;
        for(Iterator i = complexActivity.getActivities().iterator();i.hasNext();)
        {
            Activity currentActivity = (Activity)i.next();
            if(learnerProgress.getCompletedActivities().contains(currentActivity))
                numOfCompletedActivities++;
        }
        
        return isComplete(numOfCompletedActivities,complexActivity);

    }

    /**
     * Check up the children activity completion status against number of
     * activities that has been completed within target complex activity. 
     * @param numOfCompletedActivities the number of completed activities in the 	
     * 								   progress data
     * @return true if the completion condition is met.
     */
    protected abstract boolean isComplete(int numOfCompletedActivities,ComplexActivity complexActivity);


    /**
     * This method get next activity that should be progressed against the
     * requested incomplete parent activity.
     * @param parent The requested incomplete parent activity.
     * @param currentChild the current children we have just completed.
     * 
     * @return the activity we should progress to.
     */
    public abstract Activity getNextActivityByParent(Activity parent, Activity currentChild);
    
}
