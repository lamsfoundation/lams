/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.learningdesign;

import java.util.Set;
/**
 * <p>Grouping formed by random assignment. The random grouping can done in two 
 * ways:</p>
 * <li>1. Randomly group learners by the number of groups set by author. </li>
 * <li>2. Randomly group learners by the number of learners in a group.</li>
 * 
 * <p><b>Note:</b> Only one way can be chosen by the author to carry out the
 * random grouping.</p>
 * 
 * @author chris
 * @author Jacky Fang
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
        super.grouper = new RandomGrouper();
    }
    
    /** full constructor */
    public RandomGrouping(Long groupingId, Set groups, Set activities, Integer numberOfGroups, Integer learnersPerGroup)
    {
        super(groupingId, groups, activities,new RandomGrouper());
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
