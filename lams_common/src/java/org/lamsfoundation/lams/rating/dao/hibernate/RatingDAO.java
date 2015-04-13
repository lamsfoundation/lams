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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */

package org.lamsfoundation.lams.rating.dao.hibernate;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.rating.dao.IRatingDAO;
import org.lamsfoundation.lams.rating.dto.RatingDTO;
import org.lamsfoundation.lams.rating.model.Rating;

public class RatingDAO extends LAMSBaseDAO implements IRatingDAO {

    private static final String FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=? AND r.itemId=?";
    private static final String FIND_RATING_VALUE = "SELECT r.rating FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=? AND r.itemId=?";
    private static final String FIND_RATING_BY_CRITERIA_AND_USER = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=?";
    private static final String FIND_RATING_AVERAGE_BY_ITEM = "SELECT AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName() + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.itemId=?";

    @Override
    public void saveOrUpdate(Rating rating) {
	getSession().saveOrUpdate(rating);
	getSession().flush();
    }

    @Override
    public Rating getRating(Long ratingCriteriaId, Integer userId, Long itemId) {
	List<Rating> list = (List<Rating>) doFind(FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM, new Object[] { ratingCriteriaId, userId,
		itemId });
	if (list.size() > 0) {
	    return (Rating) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public Rating getRating(Long ratingCriteriaId, Integer userId) {
	List<Rating> list = (List<Rating>) doFind(FIND_RATING_BY_CRITERIA_AND_USER, new Object[] { ratingCriteriaId, userId });
	if (list.size() > 0) {
	    return (Rating) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public RatingDTO getRatingAverageDTOByItem(Long ratingCriteriaId, Long itemId) {
	List<Object[]> list = (List<Object[]>) doFind(FIND_RATING_AVERAGE_BY_ITEM, new Object[] { ratingCriteriaId, itemId });
	Object[] results = list.get(0);

	Object averageRatingObj = (results[0] == null) ? 0 : results[0];
	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);
	String averageRating = numberFormat.format(averageRatingObj);

	String numberOfVotes = (results[1] == null) ? "0" : String.valueOf(results[1]);
	return new RatingDTO(averageRating, numberOfVotes);
    }

    @Override
    public RatingDTO getRatingAverageDTOByUser(Long ratingCriteriaId, Long itemId, Integer userId) {

	RatingDTO ratingDTO = getRatingAverageDTOByItem(ratingCriteriaId, itemId);

	Float userRating = 0F;
	List list = doFind(FIND_RATING_VALUE, new Object[] { ratingCriteriaId, userId, itemId });
	if (list.size() > 0) {
	    userRating = (Float) list.get(0);
	}
	ratingDTO.setUserRating(userRating.toString());
	ratingDTO.setItemId(itemId);

	return ratingDTO;
    }

    public Rating get(Long uid) {
	if (uid != null) {
	    Object o = super.find(Rating.class, uid);
	    return (Rating) o;
	} else {
	    return null;
	}
    }
}
