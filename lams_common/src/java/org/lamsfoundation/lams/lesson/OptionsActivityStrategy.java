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
import org.lamsfoundation.lams.learningdesign.OptionsActivity;


/**
 * 
 * Progress calculation strategy for options activity.
 * @author Jacky Fang 2005-2-24
 * 
 */
public class OptionsActivityStrategy extends ActivityStrategy
{

    /**
     * Return the next activity for a incomplete options activity. In terms of 
     * incomplete option activity, the next activity will always be the option
     * activity itself so as to display the options page again when the user
     * finish one option.
     *  
     * @see org.lamsfoundation.lams.lesson.ActivityStrategy#getNextActivityByParent(Activity, Activity)
     */
    public Activity getNextActivityByParent(Activity parent, Activity currentChild)
    {
        return parent;
    }

    /**
     * Return the completion status of children activities within a options 
     * activity. A option activity is marked as complete if the maximum
     * number of options requirement is met.
     * 
     * @see org.lamsfoundation.lams.lesson.ActivityStrategy#isComplete(int, org.lamsfoundation.lams.learningdesign.ComplexActivity)
     */
    protected boolean isComplete(int numOfCompletedActivities, ComplexActivity complexActivity)
    {
        OptionsActivity optionsActivity = (OptionsActivity)complexActivity;
        return numOfCompletedActivities>=optionsActivity.getMaxNumberOfOptions().intValue()?true:false;
    }

}
