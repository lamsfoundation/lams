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
    
    /**
     * This type of grouping doesn't have groups other than learner groups.
     * So it always return <code>true</code>.
     * @see org.lamsfoundation.lams.learningdesign.Grouping#isLearnerGroup(org.lamsfoundation.lams.learningdesign.Group)
     */
    public boolean isLearnerGroup(Group group)
    {
        return true;
    }
    /**
     * This method creates a deep copy of the Grouping passed
     * as an argument.
     * 
     * @param originalGrouping The Grouping object to be deep copied
     * @return RandomGrouping The deep copied Grouping object
     */
    public static RandomGrouping createCopy(RandomGrouping originalGrouping){
    	RandomGrouping randomGrouping = new RandomGrouping();
    	
    	randomGrouping.setMaxNumberOfGroups(originalGrouping.getMaxNumberOfGroups());
    	randomGrouping.setGroupingUIID(originalGrouping.getGroupingUIID());
    	
    	randomGrouping.setNumberOfGroups(originalGrouping.getNumberOfGroups());
    	randomGrouping.setLearnersPerGroup(originalGrouping.getLearnersPerGroup());    	
    	return randomGrouping;
    }

}
