/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.usermanagement.User;


/**
 * <p>The random grouping algorithm implementation. It allocates user into the 
 * groups according to the attributes acceptted from user interface: 
 * <code>numberOfGroups</code> and <code>learnersPerGroup</code>. If any of 
 * these two attribute has be set, the random grouping will be done 
 * correspondently.</p>
 * 
 * <p>In the case of random grouping by number of groups, the number of groups
 * will be fixed, and learners are evenly allocated into the groups. For
 * example, there are 10 users and <code>numberOfGroups=3</code>, the number
 * of learners in three groups will 4,3,3. And if there are 1 user and
 * <code>numberOfGroups=3</code>, all three groups still will be created 
 * with learner number 1,0,0.</p>
 * 
 * <p>In the case of random grouping by learners per group, groups are only
 * created if the total learner number exceeds the existing group capacity. 
 * Learners are also evenly allocated into the groups.For example, if there is 
 * 5 users and <code>learnerPerGroup=3</code>, only 2 groups will be created.
 * </p>
 * 
 * @author Jacky Fang
 * @since  2005-3-24
 * @version 1.1
 * 
 */
public class RandomGrouper implements Grouper , Serializable
{
    //---------------------------------------------------------------------
    // Grouping algorithm Implementation Method
    //---------------------------------------------------------------------
    /**
     * Do the grouping for single new learner.
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping, org.lamsfoundation.lams.usermanagement.User)
     */
    public void doGrouping(Grouping randomGrouping, User learner)
    {
        //convert the single user into a list.
        List learners = new ArrayList();
        learners.add(learner);
        //delegate to do grouping for a list of learners.
        doGrouping(randomGrouping,learners);
    }
    
    /**
     * Do the grouping for a list of new learners.
     * @see org.lamsfoundation.lams.learningdesign.Grouper#doGrouping(org.lamsfoundation.lams.learningdesign.Grouping, java.util.List)
     */
    public void doGrouping(Grouping randomGrouping, List learners)
    {
        //calculate how many new groups needs to be created.
        int numOfGroupsTobeCreated =0;
        if(randomGrouping.getGroups().size()==0)
            numOfGroupsTobeCreated = calculateNumOfNewGroups((RandomGrouping)randomGrouping,
                                                             learners,
                                                             true);
        else
            numOfGroupsTobeCreated = calculateNumOfNewGroups((RandomGrouping)randomGrouping,
                                                             learners,
                                                             false);
        //create new groups
        createGroups((RandomGrouping)randomGrouping,numOfGroupsTobeCreated);
        //join the new learners into these groups.
        joinGroups(randomGrouping, learners);
    }
    //---------------------------------------------------------------------
    // Helper Methods - doGrouping
    //---------------------------------------------------------------------
    /**
     * Compute the number of new groups needs to be created based on passed
     * in grouping. It figures out group by number of groups or group by
     * learner per group automatically.
     * 
     * @param randomGrouping the grouping we used to group learner
     * @param learners the list of learners need to be grouped
     * @return the number required new group.
     */
    private int calculateNumOfNewGroups(RandomGrouping randomGrouping, 
                                        List learners,
                                        boolean isInitialization)
    {
        if(randomGrouping.getNumberOfGroups()!=null)
            return getNewGroupsByNumberOfGroups(randomGrouping, isInitialization);
        else if(randomGrouping.getLearnersPerGroup()!=null)
            return getNewGroupsByLearnerPerGroup(randomGrouping,learners);
        //TODO need to be changed to customized exception.
        throw new RuntimeException("At least one random grouping algorithm" +
        		"needs to be defined.");
    }

    /**
     * Create new groups and insert them into the grouping.
     * @param randomGrouping the requested grouping. 
     * @param numOfGroupsTobeCreated the number new groups need to be created.
     */
    private void createGroups(RandomGrouping randomGrouping, 
                              int numOfGroupsTobeCreated)
    {
        for(int i=0;i<numOfGroupsTobeCreated;i++)
        {
            randomGrouping.getGroups().add(Group.createLearnerGroup(randomGrouping,
                                                                    new HashSet()));
        }
    }
    
    /**
     * Allocate the learner into groups within the specified grouping.
     * @param randomGrouping the specified grouping.
     * @param learners  the list of learners need to be grouped
     */
    private void joinGroups(Grouping randomGrouping, List learners)
    {
        //go through all learners select a group for them to join.
        for(Iterator i=learners.iterator();i.hasNext();)
        {
            User learner = (User)i.next();
            if(randomGrouping.doesLearnerExist(learner))
            {
                Group selectedGroup = selectGroupToJoin((RandomGrouping)randomGrouping);
                selectedGroup.getUsers().add(learner);
            }
        }
    }

    //---------------------------------------------------------------------
    // Helper Methods - joinGroups
    //---------------------------------------------------------------------
    /**
     * Always choose the group with the least member to join. Therefore, we 
     * ensure the members are evenly allocated into groups.
     * @param randomGrouping the specified grouping.
     * @return the desirable group
     */
    private Group selectGroupToJoin(RandomGrouping randomGrouping)
    {
        return randomGrouping.getGroupWithLeastMember();
    }
    
    //---------------------------------------------------------------------
    // Helper Methods - calculateNumOfNewGroups
    //---------------------------------------------------------------------
    /**
     * Calculate number of new groups according to doGrouping by learner per
     * group.
     * @param randomGrouping the requested grouping.
     * @param learners the learners need to be grouped.
     * @return the number of new groups need to be created.
     */
    private int getNewGroupsByLearnerPerGroup(RandomGrouping randomGrouping,
                                              List learners)
    {
        int newLearners =0;
        for(Iterator i = learners.iterator();i.hasNext();)
        {
            User learner = (User)i.next();
            if(randomGrouping.getGroupBy(learner).isNull())
                newLearners++;
        }
        
        double totalRequiredGroups = Math.ceil(((double)(randomGrouping.getLearners().size()+newLearners))
        						  /randomGrouping.getLearnersPerGroup().doubleValue());
        int existingGroups = randomGrouping.getGroups().size(); 
        
        return (int)totalRequiredGroups - existingGroups;
    }

    /**
     * Calculate number of new groups according to doGrouping by number of 
     * groups.
     * @param randomGrouping the requested grouping.
     * @param isInitialization is this the first we do grouping.
     * @return the number of new groups need to be created.
     */
    private int getNewGroupsByNumberOfGroups(RandomGrouping randomGrouping, boolean isInitialization)
    {
        if(isInitialization)
            return randomGrouping.getNumberOfGroups().intValue();
        else 
            return 0;
    }

}
