/*
 * ChosenGrouping.java
 *
 * Created on 14 January 2005, 11:30
 */

package org.lamsfoundation.lams.learningdesign;

import java.util.Set;
/**
 * Grouping formed by staff members choice
 * @author chris
 */
public class ChosenGrouping extends Grouping
{
    
    /** Creates a new instance of ChosenGrouping */
    public ChosenGrouping()
    {
        super.grouper = new ChosenGrouper();
    }
    
    /** full constructor */
    public ChosenGrouping(Long groupingId, Set groups, Set activities)
    {
        super(groupingId, groups, activities,new ChosenGrouper());
    }
    
    /**
     * This method creates a deep copy of the Grouping passed
     * as an argument.
     * 
     * @param originalGrouping The Grouping object to be deep copied
     * @return ChosenGrouping The deep copied Grouping object
     */
    public static ChosenGrouping createCopy(ChosenGrouping originalGrouping){
    	ChosenGrouping chosenGrouping = new ChosenGrouping();
    	chosenGrouping.setMaxNumberOfGroups(originalGrouping.getMaxNumberOfGroups());
    	chosenGrouping.setGroupingUIID(originalGrouping.getGroupingUIID());
    	return chosenGrouping;
    }

    /**
     * This type of grouping doesn't have groups other than learner groups.
     * So it always return <code>true</code>.
     * @see org.lamsfoundation.lams.learningdesign.Grouping#isLearnerGroup(org.lamsfoundation.lams.learningdesign.Group)
     */
    public boolean isLearnerGroup(Group group)
    {
        return true;
    }

}
