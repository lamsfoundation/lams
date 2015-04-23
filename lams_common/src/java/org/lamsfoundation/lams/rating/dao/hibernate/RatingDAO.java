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

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.rating.dao.IRatingDAO;
import org.lamsfoundation.lams.rating.dto.RatingCriteriaDTO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.ToolActivityRatingCriteria;

public class RatingDAO extends BaseDAO implements IRatingDAO {

    private static final String FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=? AND r.itemId=?";

    private static final String FIND_USER_RATING_VALUE = "SELECT r.rating FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=? AND r.itemId=?";

    private static final String FIND_RATING_BY_CRITERIA_AND_USER = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=?";

    private static final String FIND_RATING_AVERAGE_BY_ITEM = "SELECT AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName() + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.itemId=?";

    private static final String COUNT_ITEMS_RATED_BY_ACTIVITY_AND_USER = "SELECT COUNT(DISTINCT r.itemId) FROM  "
	    + Rating.class.getName()
	    + " AS r, "
	    + ToolActivityRatingCriteria.class.getName()
	    + " AS cr "
	    + " WHERE r.ratingCriteria.ratingCriteriaId = cr.ratingCriteriaId AND cr.toolContentId = ? AND r.learner.userId =?";

    @Override
    public void saveOrUpdate(Object object) {
	this.getHibernateTemplate().saveOrUpdate(object);
	this.getHibernateTemplate().flush();
    }

    @Override
    public Rating getRating(Long ratingCriteriaId, Integer userId, Long itemId) {
	List<Rating> list = getHibernateTemplate().find(FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM,
		new Object[] { ratingCriteriaId, userId, itemId });
	if (list.size() > 0) {
	    return (Rating) list.get(0);
	} else {
	    return null;
	}
    }

    private Rating getRating(Long ratingCriteriaId, Integer userId) {
	List<Rating> list = getHibernateTemplate().find(FIND_RATING_BY_CRITERIA_AND_USER,
		new Object[] { ratingCriteriaId, userId });
	if (list.size() > 0) {
	    return (Rating) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public RatingCriteriaDTO getRatingAverageDTOByItem(Long ratingCriteriaId, Long itemId) {
	List<Object[]> list = getHibernateTemplate().find(FIND_RATING_AVERAGE_BY_ITEM,
		new Object[] { ratingCriteriaId, itemId });
	Object[] results = list.get(0);

	Object averageRatingObj = (results[0] == null) ? 0 : results[0];
	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);
	String averageRating = numberFormat.format(averageRatingObj);

	String numberOfVotes = (results[1] == null) ? "0" : String.valueOf(results[1]);
	return new RatingCriteriaDTO(averageRating, numberOfVotes);
    }

    @Override
    public RatingCriteriaDTO getRatingAverageDTOByUser(Long ratingCriteriaId, Long itemId, Integer userId) {

	RatingCriteriaDTO criteriaDto = getRatingAverageDTOByItem(ratingCriteriaId, itemId);

	List list = getHibernateTemplate().find(FIND_USER_RATING_VALUE,
		new Object[] { ratingCriteriaId, userId, itemId });
	String userRating;
	if (list.size() > 0) {
	    userRating = ((Float) list.get(0)).toString();

	    // user didn't leave his rating yet
	} else {
	    userRating = "";

	}
	criteriaDto.setUserRating(userRating);
	criteriaDto.setItemId(itemId);

	return criteriaDto;
    }

    @Override
    public Rating get(Long uid) {
	if (uid != null) {
	    Object o = getHibernateTemplate().get(Rating.class, uid);
	    return (Rating) o;
	} else {
	    return null;
	}
    }

    @Override
    public int getCountItemsRatedByActivityAndUser(Long toolContentId, Integer userId) {

	List list = getHibernateTemplate().find(COUNT_ITEMS_RATED_BY_ACTIVITY_AND_USER,
		new Object[] { toolContentId, userId });
	if (list == null || list.size() == 0) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }
}
