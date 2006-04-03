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
package org.lamsfoundation.lams.learningdesign.strategy;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.NullActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;

/**
 * Progress calculation strategy for sequence activity.
 * 
 * @author Jacky Fang 2005-2-24
 * @version 1.1
 */
public class SequenceActivityStrategy extends ComplexActivityStrategy
{
	protected SequenceActivity sequenceActivity = null;
	
	public SequenceActivityStrategy(SequenceActivity sequenceActivity) {
		this.sequenceActivity = sequenceActivity;
	}

    /**
     * <p>Return the next activity for a incomplete options activity. 
     * 
     * <p>For a sequence activity, the activity should be the next activity in the
     * children activity set ordered by activity id.</p>
     * 
     * Pre-condition: the parent must have some incomplete children
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#getNextActivityByParent(Activity, Activity)
     */
    public Activity getNextActivityByParent(ComplexActivity parent, Activity currentChild)
    {
        Set children = new TreeSet(new ActivityOrderComparator());
        children.addAll(parent.getActivities());
        
        for(Iterator i=children.iterator();i.hasNext();)
        {
            Activity curChild = (Activity)i.next();
            //if no current child, we should return the first one.
            if(currentChild==null || currentChild.isNull())
                return curChild;
            
            if(curChild.getActivityId().longValue()==currentChild.getActivityId().longValue())
                return (Activity)i.next();
        }
        return new NullActivity();
    }

    /**
     * Implementation for sequence activity. It returns true if all children
     * of this sequence activity appear in the completed activities set from
     * current learner progress.
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#isComplete(int, org.lamsfoundation.lams.learningdesign.ComplexActivity)
     */
    protected boolean isComplete(int numOfCompletedActivities)
    {
    	if ( sequenceActivity != null ) {
    		return numOfCompletedActivities==sequenceActivity.getActivities().size()?true:false;
    	} 
    	return true;
    }

    /** 
     * Get the strategy's activity as a Complex Activity.  
     */
    protected ComplexActivity getComplexActivity() {
    	return sequenceActivity;
    }
}
