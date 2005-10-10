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
     * This method creates a deep copy of the Grouping 
     * @return ChosenGrouping The deep copied Grouping object
     */
    public Grouping createCopy()
    {
    	ChosenGrouping chosenGrouping = new ChosenGrouping();
    	chosenGrouping.setMaxNumberOfGroups(this.getMaxNumberOfGroups());
    	chosenGrouping.setGroupingUIID(this.getGroupingUIID());
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
