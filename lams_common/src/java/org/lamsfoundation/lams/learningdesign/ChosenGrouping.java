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
    public ChosenGrouping(Long groupingId, Integer groupingTypeId, Set groups, Set activities)
    {
        super(groupingId, groupingTypeId, groups, activities);
    }
    
}
