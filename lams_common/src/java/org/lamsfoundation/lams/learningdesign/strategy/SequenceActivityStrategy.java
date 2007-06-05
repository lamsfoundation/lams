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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
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
 * @author Jacky Fang, Fiona Malikoff
 */
public class SequenceActivityStrategy extends ComplexActivityStrategy
{
	protected SequenceActivity sequenceActivity = null;
	
	public SequenceActivityStrategy(SequenceActivity sequenceActivity) {
		this.sequenceActivity = sequenceActivity;
	}

    /**
     * <p>Return the next activity for a incomplete sequence activity. 
     * 
     * <p>Normally all the activities in a sequence activity will be linked
     * via transitions, so we are only interested in child activities that
     * start a series of linked activities.
     * 
     * <p>This will return the next such activity (ie one that doesn't 
     * have in input transition) with the order id greater the 
     * currentChild. If the currentChild is the NullActivity then 
     * it will return the first (and normally the only) such child.</p>
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#getNextActivityByParent(Activity, Activity)
     */
	public Activity getNextActivityByParent(ComplexActivity parent, Activity currentChild)
    {
        Set children = new TreeSet(new ActivityOrderComparator());
        children.addAll(parent.getActivities());
        
        Activity inputChild = currentChild;
        if ( inputChild != null ) {
        	if ( inputChild.isNull() ) {
        		inputChild = null;
        	} else if ( inputChild.getOrderId() == null ) {
        		inputChild = null;
        	}
        }
        
        for(Iterator i=children.iterator();i.hasNext();)
        {
            Activity curChild = (Activity)i.next();
            if(  ( inputChild==null || curChild.getOrderId().longValue() > currentChild.getOrderId().longValue() ) ) {
            	// we are past the 'currentChild' so look for an activity with no input transition
            	if ( curChild.getTransitionTo() == null ) 
            		return curChild;
            }
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
