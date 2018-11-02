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

/**
 * The activity name comparator used for sorted set, sorting on the activity's name then order id
 * then activity id. This comparator will impose orderings that are inconsistent with equals.
 *
 * Only use for classes that are in the Activity hierarchy.
 *
 * Uses the ActivityOrderComparator for reuse purposes only - if the name is the same in both
 * activities then it calls ActivityOrderComparator to do the other comparisons. Do not assume that
 * it will always extend the ActivityOrderComparator.
 */
public class ActivityTitleComparator extends ActivityOrderComparator {

    /**
     * Compare the order.
     *
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Activity activity1, Activity activity2) {
	int orderDiff = 0;

	if (activity1.getTitle() != null) {
	    if (activity2.getTitle() == null) {
		orderDiff = 1;
	    } else {
		orderDiff = activity1.getTitle().compareTo(activity2.getTitle());
	    }
	}

	return orderDiff != 0 ? orderDiff : super.compare(activity1, activity2);
    }
}