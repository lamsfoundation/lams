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

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;


/**
 * The progress calculation strategy for parallel activity.
 * 
 * @author Jacky Fang 2005-2-24
 * 
 */
public class ParallelActivityStrategy extends ActivityStrategy
{
    /**
     * Regarding incomplete parallel activity, the next activity will always
     * be a waiting activity, which will finally translated into waiting
     * message.
     * 
     * @see org.lamsfoundation.lams.lesson.ActivityStrategy#getNextActivityByParent(Activity, Activity)
     */
    public Activity getNextActivityByParent(Activity activity, Activity currentChild)
    {
        return new ParallelWaitActivity();
    }

    /**
     * Return the completion status of children activities within a parallel
     * activity. A parallel activity is marked as complete if all children
     * activities are completed.
     * 
     * @see org.lamsfoundation.lams.lesson.ActivityStrategy#isComplete(int, org.lamsfoundation.lams.learningdesign.ComplexActivity)
     */
    protected boolean isComplete(int numOfCompletedActivities, ComplexActivity complexActivity)
    {
        ParallelActivity parallelActivity = (ParallelActivity)complexActivity;
        return numOfCompletedActivities==parallelActivity.getActivities().size()?true:false;
    }

}
