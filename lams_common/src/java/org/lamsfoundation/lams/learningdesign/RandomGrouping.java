/*
 * RandomGrouping.java
 *
 * Created on 14 January 2005, 11:27
 */

package org.lamsfoundation.lams.learningdesign;

import java.util.Set;
/**
 * Grouping formed by random assignment
 * @author chris
 */
public class RandomGrouping extends Grouping
{
    /** nullable persistent field */
    private Integer numberOfGroups;
    
    /** nullable persistent field */
    private Integer learnersPerGroup;
    
    
    /**
     *            
     *
     */
    public Integer getNumberOfGroups()
    {
        return this.numberOfGroups;
    }
    
    public void setNumberOfGroups(Integer numberOfGroups)
    {
        this.numberOfGroups = numberOfGroups;
    }
    
    /**
     *            
     *
     */
    public Integer getLearnersPerGroup()
    {
        return this.learnersPerGroup;
    }
    
    public void setLearnersPerGroup(Integer learnersPerGroup)
    {
        this.learnersPerGroup = learnersPerGroup;
    }
    
    /** Creates a new instance of RandomGrouping */
    public RandomGrouping()
    {
    }
    
    /** full constructor */
    public RandomGrouping(Long groupingId, Set groups, Set activities, Integer numberOfGroups, Integer learnersPerGroup)
    {
        super(groupingId, groups, activities);
        this.learnersPerGroup = learnersPerGroup;
        this.numberOfGroups = numberOfGroups;
    }
    
}
