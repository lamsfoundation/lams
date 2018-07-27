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

package org.lamsfoundation.lams.rating;

import java.io.Serializable;
import java.util.Comparator;

import org.lamsfoundation.lams.rating.model.RatingCriteria;

/**
 * The ratingCriteria order comparator used for sorted set. Order id is used as the primary comparing criteria as it is
 * unique within a complex ratingCriteria. If they are the same, ratingCriteria id are used for comparison to ensure the
 * sorted set won't treat two activities with the same order id as the same ratingCriteria. If either of the
 * ratingCriteria ids are null (activities are not yet saved in the database) use the ratingCriteria ui id. The ui ids
 * will compare nulls if required, with null being lower than not null. Two nulls will equal true.
 *
 * @author andreyb
 */
public class RatingCriteriaOrderComparator implements Comparator, Serializable {

    @Override
    public int compare(Object o1, Object o2) {
	RatingCriteria ratingCriteria1 = (RatingCriteria) o1;
	RatingCriteria ratingCriteria2 = (RatingCriteria) o2;

	if (ratingCriteria1.getOrderId() == null || ratingCriteria2.getOrderId() == null) {
	    return compareRatingCriteriaId(ratingCriteria1, ratingCriteria2);
	}

	int orderDiff = ratingCriteria1.getOrderId().compareTo(ratingCriteria2.getOrderId());
	// return order id compare result if they are not the same
	if (orderDiff != 0) {
	    return orderDiff;
	    // if order id are the same, compare ratingCriteria id.
	} else {
	    return compareRatingCriteriaId(ratingCriteria1, ratingCriteria2);
	}
    }

    private int compareRatingCriteriaId(RatingCriteria ratingCriteria1, RatingCriteria ratingCriteria2) {
	if (ratingCriteria1.getRatingCriteriaId() == null && ratingCriteria2.getRatingCriteriaId() == null) {
	    return 0;
	}
	if (ratingCriteria1.getRatingCriteriaId() == null) {
	    return -1;
	}
	if (ratingCriteria2.getRatingCriteriaId() == null) {
	    return 1;
	}
	return ratingCriteria1.getRatingCriteriaId().compareTo(ratingCriteria2.getRatingCriteriaId());
    }
}
