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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.util.List;

import org.lamsfoundation.lams.usermanagement.User;




/**
 * This is interface that defines the contract for performing grouping 
 * algorithm.
 * 
 * @author Jacky Fang
 * @since  2005-3-24
 * @version 1.1
 * 
 */
public interface Grouper
{
    /**
     * Do the grouping for a list of learners that the teacher requested.
     * @param grouping the grouping that is used to perform groups creation.
     * @param name for this group
     * @param learners the list of learners that the teacher requested.
     */
	public void doGrouping(Grouping grouping, String groupName, List learners);
	
	/**
	 * Do the grouping for a single learner.
	 * @param grouping the grouping that is used to perform groups creation.
	 * @param name for this group
	 * @param learner the learner teacher want to add.
	 */
	public void doGrouping(Grouping grouping, String groupName, User learner);
    
}
