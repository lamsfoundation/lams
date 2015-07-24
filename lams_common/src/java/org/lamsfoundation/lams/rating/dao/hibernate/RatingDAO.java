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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.rating.dao.IRatingDAO;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingComment;

public class RatingDAO extends BaseDAO implements IRatingDAO {

    private static final String FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=? AND r.itemId=?";

    private static final String FIND_USER_RATING_VALUE = "SELECT r.rating FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=? AND r.itemId=?";

    private static final String FIND_RATING_BY_CRITERIA_AND_USER = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=?";
    
    private static final String FIND_RATINGS_BY_ITEM = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=? AND r.itemId=?";

    private static final String FIND_RATINGS_BY_USER = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=? AND r.learner.userId=?";

    private static final String FIND_RATING_AVERAGE_BY_ITEM = "SELECT AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName() + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.itemId=?";
    
    private static final String FIND_RATING_AVERAGE_BY_CONTENT_AND_ITEM = "SELECT r.itemId, r.ratingCriteria.ratingCriteriaId, AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName() + " AS r where r.ratingCriteria.toolContentId=? AND r.itemId=? GROUP BY r.ratingCriteria.ratingCriteriaId";
    
    private static final String FIND_RATING_AVERAGE_BY_CONTENT_AND_ITEMS = "SELECT r.itemId, r.ratingCriteria.ratingCriteriaId, AVG(r.rating), COUNT(*) FROM "
		+ Rating.class.getName()
		+ " AS r where r.ratingCriteria.toolContentId=:contentId AND r.itemId IN (:itemIds) GROUP BY r.itemId, r.ratingCriteria.ratingCriteriaId";

    private static final String FIND_RATING_AVERAGE_BY_CONTENT_ID = "SELECT r.itemId, r.ratingCriteria.ratingCriteriaId, AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=? GROUP BY r.itemId, r.ratingCriteria.ratingCriteriaId";

//    private static final String COUNT_ITEMS_RATED_BY_ACTIVITY_AND_USER = "SELECT COUNT(DISTINCT r.itemId)+(SELECT COUNT(comment) FROM "
//	    + RatingComment.class.getName()
//	    + " AS comment "
//	    + " WHERE comment.ratingCriteria.toolContentId = :toolContentId AND comment.learner.userId =:userId AND comment.itemId =:itemId AND cr.commentsEnabled IS TRUE ) FROM  "
//	    + Rating.class.getName()
//	    + " AS r "
//	    + " WHERE r.ratingCriteria.toolContentId = :toolContentId AND r.learner.userId =:userId";

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
    
    @Override
    public List<Rating> getRatingsByItem(Long contentId, Long itemId) {
	return getHibernateTemplate().find(FIND_RATINGS_BY_ITEM, new Object[] { contentId, itemId });
    }

    // method is not used at the moment
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
    public List<Rating> getRatingsByUser(Long contentId, Integer userId) {
	return getHibernateTemplate().find(FIND_RATINGS_BY_USER, new Object[] { contentId, userId });
    }

    @Override
    public ItemRatingCriteriaDTO getRatingAverageDTOByItem(Long ratingCriteriaId, Long itemId) {
	List<Object[]> list = getHibernateTemplate().find(FIND_RATING_AVERAGE_BY_ITEM,
		new Object[] { ratingCriteriaId, itemId });
	Object[] results = list.get(0);

	Object averageRatingObj = (results[0] == null) ? 0 : results[0];
	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);
	String averageRating = numberFormat.format(averageRatingObj);

	String numberOfVotes = (results[1] == null) ? "0" : String.valueOf(results[1]);
	return new ItemRatingCriteriaDTO(averageRating, numberOfVotes);
    }

    @Override
    public List<Object[]> getRatingAverageByContentAndItem(Long contentId, Long itemId) {
	return getHibernateTemplate().find(FIND_RATING_AVERAGE_BY_CONTENT_AND_ITEM,
		new Object[] { contentId, itemId });
    }
    
    @Override
    public List<Object[]> getRatingAverageByContentAndItems(Long contentId, Collection<Long> itemIds) {
	return  getSession().createQuery(FIND_RATING_AVERAGE_BY_CONTENT_AND_ITEMS)
	    .setLong("contentId", contentId).setParameterList("itemIds", itemIds).list();
    }

    @Override
    public List<Object[]> getRatingAverageByContent(Long contentId) {
	return getHibernateTemplate().find(FIND_RATING_AVERAGE_BY_CONTENT_ID, new Object[] { contentId });
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
    public int getCountItemsRatedByUser(final Long toolContentId, final Integer userId) {

	// unions don't work in HQL so doing 2 separate DB queries (http://stackoverflow.com/a/3940445)
	String FIND_ITEM_IDS_RATED_BY_USER = "SELECT DISTINCT r.itemId FROM  " + Rating.class.getName() + " AS r "
		+ " WHERE r.ratingCriteria.toolContentId = :toolContentId AND r.learner.userId =:userId";

	String FIND_ITEM_IDS_COMMENTED_BY_USER = "SELECT DISTINCT comment.itemId FROM "
		+ RatingComment.class.getName()
		+ " AS comment "
		+ " WHERE comment.ratingCriteria.toolContentId = :toolContentId AND comment.learner.userId =:userId AND comment.ratingCriteria.commentsEnabled IS TRUE";

	List<Long> ratedItemIds = this.getSession().createQuery(FIND_ITEM_IDS_RATED_BY_USER)
		.setLong("toolContentId", toolContentId).setInteger("userId", userId).list();

	List<Long> commentedItemIds = this.getSession().createQuery(FIND_ITEM_IDS_COMMENTED_BY_USER)
		.setLong("toolContentId", toolContentId).setInteger("userId", userId).list();

	Set<Long> unionItemIds = new HashSet<Long>(ratedItemIds);
	unionItemIds.addAll(commentedItemIds);

	return unionItemIds.size();
    }
}
