/*
 * Created on Mar 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.strategy;

import java.util.ArrayList;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GroupingActivityStrategy extends SimpleActivityStrategy {

	/**
	 *  (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy#getContributionType(org.lamsfoundation.lams.learningdesign.Activity)
	 */
	public Integer[] getContributionType(Activity activity) {
		ArrayList contributionType = new ArrayList();		
		GroupingActivity groupingActivity =(GroupingActivity)activity;
		if(groupingActivity.getCreateGrouping().getGroupingTypeId()== Grouping.CHOSEN_GROUPING_TYPE)
			contributionType.add(new Integer(CHOSEN_GROUPING));		
		return (Integer[])contributionType.toArray(new Integer[contributionType.size()]);
	}

}
