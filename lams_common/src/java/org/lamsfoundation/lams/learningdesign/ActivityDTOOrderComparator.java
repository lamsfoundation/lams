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

import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;

/**
 * The activity order comparator used for sorted set. Order id is used as
 * the primary comparing criteria as it is unique within a complex activity.
 * If they are the same, activity id are used for comparison to ensure the
 * sorted set won't treat two activities with the same order id as the
 * same activity.
 *
 * @author Dapneg Ni
 */
public class ActivityDTOOrderComparator implements Comparator<AuthoringActivityDTO>, Serializable {

    /**
     * Compare the order.
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(AuthoringActivityDTO activity1, AuthoringActivityDTO activity2) {

	if (activity1.getOrderID() == null || activity2.getOrderID() == null) {
	    return activity1.getActivityID().compareTo(activity2.getActivityID());
	}

	int orderDiff = activity1.getOrderID().compareTo(activity2.getOrderID());
	//return order id compare result if they are not the same
	if (orderDiff != 0) {
	    return orderDiff;
	    //if order id are the same, compare activity id.
	} else {
	    return activity1.getActivityID().compareTo(activity2.getActivityID());
	}
    }

}
