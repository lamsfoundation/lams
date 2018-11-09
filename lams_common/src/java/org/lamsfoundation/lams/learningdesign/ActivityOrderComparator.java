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

import java.util.Comparator;

/**
 * The activity order comparator used for sorted set. Order id is used as
 * the primary comparing criteria as it is unique within a complex activity.
 * If they are the same, activity id are used for comparison to ensure the
 * sorted set won't treat two activities with the same order id as the
 * same activity. If either of the activity ids are null (activities are not
 * yet saved in the database) use the activity ui id. The ui ids will compare
 * nulls if required, with null being lower than not null. Two nulls will equal true.
 *
 * Note: this comparator may impose orderings that are inconsistent with equals as it
 * compares order id before activity id. But in practise all sensible cases should
 * come out with the same ordering.
 *
 * @author dgarth, Jacky Fang
 */
public class ActivityOrderComparator implements Comparator<Activity> {

    /**
     * Compare the order.
     */
    @Override
    public int compare(Activity activity1, Activity activity2) {
	if (activity1.getOrderId() == null || activity2.getOrderId() == null) {
	    return compareActivityId(activity1, activity2);
	}

	int orderDiff = activity1.getOrderId().compareTo(activity2.getOrderId());
	//return order id compare result if they are not the same
	if (orderDiff != 0) {
	    return orderDiff;
	    //if order id are the same, compare activity id.
	} else {
	    return compareActivityId(activity1, activity2);
	}
    }

    private int compareActivityId(Activity activity1, Activity activity2) {
	if (activity1.getActivityId() == null || activity2.getActivityId() == null) {
	    return compareActivityUIID(activity1, activity2);
	}
	return activity1.getActivityId().compareTo(activity2.getActivityId());
    }

    private int compareActivityUIID(Activity activity1, Activity activity2) {
	if (activity1.getActivityUIID() == null && activity2.getActivityUIID() == null) {
	    return 0;
	}
	if (activity1.getActivityUIID() == null) {
	    return -1;
	}
	if (activity2.getActivityUIID() == null) {
	    return 1;
	}
	return activity1.getActivityUIID().compareTo(activity2.getActivityUIID());
    }
}