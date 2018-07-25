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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Comparator;

import org.lamsfoundation.lams.util.AlphanumComparator;

/**
 * The grouping comparator used for any ordered collection. Order is done on the alphabetic order of the group name.
 * Input objects must be of type Group.
 */
public class GroupComparator implements Comparator<Group>, Serializable {

    private static final long serialVersionUID = 4127022514966256114L;

    /**
     * Compare the group names
     */
    @Override
    public int compare(Group grp1, Group grp2) {
	String grp1Name = grp1 != null ? grp1.getGroupName() : "";

	String grp2Name = grp2 != null ? grp2.getGroupName() : "";

	AlphanumComparator comparator = new AlphanumComparator();
	return comparator.compare(grp1Name, grp2Name);
    }

}
