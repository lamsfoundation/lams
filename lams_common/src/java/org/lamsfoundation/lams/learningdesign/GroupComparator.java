/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.collections.comparators.NullComparator;

/**
 * The grouping comparator used for any ordered collection. Order is done on the alphabetic order of the group name.
 * Input objects must be of type Group.
 */
public class GroupComparator implements Comparator, Serializable {

	private static final long serialVersionUID = 4127022514966256114L;

	/**
     * Compare the group names
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        Group grp1 = (Group)o1;
        String grp1Name = grp1 != null ? grp1.getGroupName() : null;
        
        Group grp2 = (Group)o2;
        String grp2Name = grp2!= null ? grp2.getGroupName() : null;

        NullComparator comparator = new NullComparator(false);
        return comparator.compare(grp1Name,grp2Name);
    }
    
}
