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
    }
    
    /** full constructor */
    public ChosenGrouping(Long groupingId, Set groups, Set activities)
    {
        super(groupingId, groups, activities);
    }

    /**
     * This type of grouping doesn't have groups other than learner groups.
     * So it always return <code>true</code>.
     * @see org.lamsfoundation.lams.learningdesign.Grouping#isLearnerGroup(org.lamsfoundation.lams.learningdesign.Group)
     */
    protected boolean isLearnerGroup(Group group)
    {
        return true;
    }

}
