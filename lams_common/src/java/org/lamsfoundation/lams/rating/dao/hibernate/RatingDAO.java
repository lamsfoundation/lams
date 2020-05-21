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

package org.lamsfoundation.lams.rating.dao.hibernate;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.rating.dao.IRatingDAO;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingComment;
import org.lamsfoundation.lams.rating.model.RatingCriteria;

public class RatingDAO extends LAMSBaseDAO implements IRatingDAO {

    private static final String FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=? AND r.itemId=?";

    private static final String FIND_RATING_BY_CRITERIA_AND_USER = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=?";

    private static final String FIND_RATINGS_BY_ITEM = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=? AND r.toolSessionId=? AND r.itemId=?";

    private static final String FIND_RATINGS_BY_USER = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=? AND r.learner.userId=?";

    private static final String FIND_RATINGS_BY_USER_CRITERIA = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=?";

    private static final String FIND_RATINGS_BY_ITEM_CRITERIA = "FROM " + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId IN (:ratingCriteriaIds) AND r.itemId IN (:itemIds)";

    private static final String FIND_RATING_AVERAGE_BY_ITEM = "SELECT AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.toolSessionId=? AND r.itemId=?";

    private static final String FIND_RATING_AVERAGE_BY_CONTENT_AND_ITEM = "SELECT r.itemId, r.ratingCriteria.ratingCriteriaId, AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=? AND r.itemId=? GROUP BY r.ratingCriteria.ratingCriteriaId";

    private static final String FIND_RATING_AVERAGE_BY_CONTENT_AND_ITEMS = "SELECT r.itemId, r.ratingCriteria.ratingCriteriaId, AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=:contentId AND r.itemId IN (:itemIds) GROUP BY r.itemId, r.ratingCriteria.ratingCriteriaId";

    private static final String FIND_RATING_AVERAGE_BY_CONTENT_AND_SESSION_AND_ITEM = "SELECT r.itemId, r.ratingCriteria.ratingCriteriaId, AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=? AND r.toolSessionId=? AND r.itemId=? GROUP BY r.ratingCriteria.ratingCriteriaId";

    private static final String FIND_RATING_AVERAGE_BY_CONTENT_AND_SESSION_AND_ITEMS = "SELECT r.itemId, r.ratingCriteria.ratingCriteriaId, AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=:contentId AND r.toolSessionId=:toolSessionId AND r.itemId IN (:itemIds) GROUP BY r.itemId, r.ratingCriteria.ratingCriteriaId";

    private static final String FIND_RATING_AVERAGE_BY_CONTENT_ID = "SELECT r.itemId, r.ratingCriteria.ratingCriteriaId, AVG(r.rating), COUNT(*) FROM "
	    + Rating.class.getName()
	    + " AS r where r.ratingCriteria.toolContentId=? GROUP BY r.itemId, r.ratingCriteria.ratingCriteriaId";

    // Used by tools to get the ratings and comments relating to their items, as submitted by a particular user.
    // To be used within SQL and supply the userId as :userId and criteriaId as :ratingCriteriaId
    // See Peer Review for example usage. Special version for comment style as there is no entry in the lams_rating table for a comment style rating.
    private static final String TOOL_SELECT_LEFT_JOIN_BY_USER_STANDARD = "SELECT r.item_id, ANY_VALUE(rc.comment) comment, ANY_VALUE(r2.rating) rating, AVG(r.rating) average_rating, COUNT(r.rating) count_vote "
	    + " FROM lams_rating r  "
	    + " LEFT JOIN lams_rating r2  ON r2.rating_criteria_id = r.rating_criteria_id AND r.item_id = r2.item_id AND r2.user_id = :userId "
	    + " LEFT JOIN lams_rating_comment rc ON rc.rating_criteria_id = r.rating_criteria_id AND rc.item_id = r2.item_id AND rc.user_id = :userId "
	    + " WHERE r.rating_criteria_id = :ratingCriteriaId " + " GROUP BY r.item_id";

    // Used by tools to get the ratings and comments relating to their items. To be used within SQL and supply criteriaId as :ratingCriteriaId
    // See Peer Review for example usage.
    private static final String TOOL_SELECT_LEFT_JOIN_BY_USER_COMMENT = "SELECT r.item_id, r.comment "
	    + "     FROM lams_rating_comment r WHERE r.rating_criteria_id = :ratingCriteriaId AND r.user_id = :userId";

    // Same as TOOL_SELECT_LEFT_JOIN_BY_USER_STANDARD except that it returns the average results for a single user (:userId), as left by other users
    private static final String TOOL_SELECT_LEFT_JOIN_FOR_USER_STANDARD = "SELECT DISTINCT r.item_id, rc.comment, NULL rating, calc.average_rating, calc.count_vote "
	    + "      FROM lams_rating r"
	    + "      JOIN lams_rating_criteria c ON r.item_id = :userId AND c.tool_content_id = :toolContentId "
	    + "      AND c.rating_criteria_id = :ratingCriteriaId AND c.rating_criteria_id = r.rating_criteria_id "
	    + "      LEFT JOIN lams_rating_comment rc ON rc.rating_criteria_id = r.rating_criteria_id AND rc.item_id = r.item_id and rc.user_id = r.user_id "
	    + "      LEFT JOIN ( "
	    + "                SELECT r2.item_id, AVG(r2.rating) average_rating, COUNT(*) count_vote  "
	    + "                FROM lams_rating r2 WHERE r2.rating_criteria_id = :ratingCriteriaId AND r2.item_id = :userId "
	    + "                GROUP BY r2.item_id  " + "                ) calc ON calc.item_id = r.item_id ";

    // Same as TOOL_SELECT_LEFT_JOIN_BY_USER_COMMENT except that it returns all the comments results for a single user (:userId), as left by other users
    private static final String TOOL_SELECT_LEFT_JOIN_FOR_USER_COMMENT = "SELECT r.item_id, r.comment"
	    + "     FROM lams_rating_comment r "
	    + "     WHERE r.item_id = :userId AND r.rating_criteria_id = :ratingCriteriaId";

//    private static final String COUNT_ITEMS_RATED_BY_ACTIVITY_AND_USER = "SELECT COUNT(DISTINCT r.itemId)+(SELECT COUNT(comment) FROM "
//	    + RatingComment.class.getName()
//	    + " AS comment "
//	    + " WHERE comment.ratingCriteria.toolContentId = :toolContentId AND comment.learner.userId =:userId AND comment.itemId =:itemId AND cr.commentsEnabled IS TRUE ) FROM  "
//	    + Rating.class.getName()
//	    + " AS r "
//	    + " WHERE r.ratingCriteria.toolContentId = :toolContentId AND r.learner.userId =:userId";

    @Override
    public void saveOrUpdate(Object object) {
	getSession().saveOrUpdate(object);
	getSession().flush();
    }

    @Override
    public void delete(Object object) {
	getSession().delete(object);
	getSession().flush();
    }

    @Override
    public Rating getRating(Long ratingCriteriaId, Integer userId, Long itemId) {
	List<Rating> list = doFind(FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM,
		new Object[] { ratingCriteriaId, userId, itemId });
	if (list.size() > 0) {
	    return list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public List<Rating> getRatingsByItem(Long contentId, Long toolSessionId, Long itemId) {
	return super.find(FIND_RATINGS_BY_ITEM, new Object[] { contentId, toolSessionId, itemId });
    }

    // method is not used at the moment
    private Rating getRating(Long ratingCriteriaId, Integer userId) {
	List<Rating> list = doFind(FIND_RATING_BY_CRITERIA_AND_USER, new Object[] { ratingCriteriaId, userId });
	if (list.size() > 0) {
	    return list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public List<Rating> getRatingsByUser(Long contentId, Integer userId) {
	return doFind(FIND_RATINGS_BY_USER, new Object[] { contentId, userId });
    }

    @Override
    public List<Rating> getRatingsByUserCriteria(Long criteriaId, Integer userId) {
	return doFind(FIND_RATINGS_BY_USER_CRITERIA, new Object[] { criteriaId, userId });
    }

    @Override
    public ItemRatingCriteriaDTO getRatingAverageDTOByItem(Long ratingCriteriaId, Long toolSessionId, Long itemId) {
	List<Object[]> list = doFind(FIND_RATING_AVERAGE_BY_ITEM,
		new Object[] { ratingCriteriaId, toolSessionId, itemId });
	Object[] results = list.get(0);

	Object averageRatingObj = (results[0] == null) ? 0 : results[0];

	String numberOfVotes = (results[1] == null) ? "0" : String.valueOf(results[1]);
	return new ItemRatingCriteriaDTO((Number) averageRatingObj, numberOfVotes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getRatingAverageByContentAndItem(Long contentId, Long itemId) {
	return doFind(FIND_RATING_AVERAGE_BY_CONTENT_AND_ITEM, new Object[] { contentId, itemId });
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getRatingAverageByContentAndItems(Long contentId, Collection<Long> itemIds) {
	return getSession().createQuery(FIND_RATING_AVERAGE_BY_CONTENT_AND_ITEMS).setParameter("contentId", contentId)
		.setParameterList("itemIds", itemIds).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getRatingAverageByContentAndItem(Long contentId, Long toolSessionId, Long itemId) {
	return doFind(FIND_RATING_AVERAGE_BY_CONTENT_AND_SESSION_AND_ITEM,
		new Object[] { contentId, toolSessionId, itemId });
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getRatingAverageByContentAndItems(Long contentId, Long toolSessionId,
	    Collection<Long> itemIds) {
	return getSession().createQuery(FIND_RATING_AVERAGE_BY_CONTENT_AND_SESSION_AND_ITEMS)
		.setParameter("contentId", contentId).setParameter("toolSessionId", toolSessionId)
		.setParameterList("itemIds", itemIds).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getRatingAverageByContent(Long contentId) {
	return doFind(FIND_RATING_AVERAGE_BY_CONTENT_ID, new Object[] { contentId });
    }

    @Override
    public Rating get(Long uid) {
	if (uid != null) {
	    Object o = super.find(Rating.class, uid);
	    return (Rating) o;
	} else {
	    return null;
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getCountItemsRatedByUser(final Long toolContentId, final Integer userId) {

	// unions don't work in HQL so doing 2 separate DB queries (http://stackoverflow.com/a/3940445)
	String FIND_ITEM_IDS_RATED_BY_USER = "SELECT DISTINCT r.itemId FROM  " + Rating.class.getName() + " AS r "
		+ " WHERE r.ratingCriteria.toolContentId = :toolContentId AND r.learner.userId =:userId";

	String FIND_ITEM_IDS_COMMENTED_BY_USER = "SELECT DISTINCT comment.itemId FROM " + RatingComment.class.getName()
		+ " AS comment "
		+ " WHERE comment.ratingCriteria.toolContentId = :toolContentId AND comment.learner.userId =:userId AND comment.ratingCriteria.commentsEnabled IS TRUE";

	List<Long> ratedItemIds = this.getSession().createQuery(FIND_ITEM_IDS_RATED_BY_USER)
		.setParameter("toolContentId", toolContentId).setParameter("userId", userId).list();

	List<Long> commentedItemIds = this.getSession().createQuery(FIND_ITEM_IDS_COMMENTED_BY_USER)
		.setParameter("toolContentId", toolContentId).setParameter("userId", userId).list();

	Set<Long> unionItemIds = new HashSet<>(ratedItemIds);
	unionItemIds.addAll(commentedItemIds);

	return unionItemIds.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getCountItemsRatedByUserByCriteria(final Long criteriaId, final Integer userId) {

	// unions don't work in HQL so doing 2 separate DB queries (http://stackoverflow.com/a/3940445)
	String FIND_ITEM_IDS_RATED_BY_USER = "SELECT DISTINCT r.itemId FROM  " + Rating.class.getName() + " AS r "
		+ " WHERE r.ratingCriteria.ratingCriteriaId = :criteriaId AND r.learner.userId =:userId";

	String FIND_ITEM_IDS_COMMENTED_BY_USER = "SELECT DISTINCT comment.itemId FROM " + RatingComment.class.getName()
		+ " AS comment "
		+ " WHERE comment.ratingCriteria.ratingCriteriaId = :criteriaId AND comment.learner.userId =:userId AND comment.ratingCriteria.commentsEnabled IS TRUE";

	List<Long> ratedItemIds = this.getSession().createQuery(FIND_ITEM_IDS_RATED_BY_USER)
		.setParameter("criteriaId", criteriaId).setParameter("userId", userId).list();

	List<Long> commentedItemIds = this.getSession().createQuery(FIND_ITEM_IDS_COMMENTED_BY_USER)
		.setParameter("criteriaId", criteriaId).setParameter("userId", userId).list();

	Set<Long> unionItemIds = new HashSet<>(ratedItemIds);
	unionItemIds.addAll(commentedItemIds);

	return unionItemIds.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Long, Long> countUsersRatedEachItem(final Long contentId, final Long toolSessionId,
	    final Collection<Long> itemIds, Integer excludeUserId) {

	HashMap<Long, Long> itemIdToRatedUsersCountMap = new HashMap<>();
	if (itemIds.isEmpty()) {
	    return itemIdToRatedUsersCountMap;
	}

	// unions don't work in HQL so doing 2 separate DB queries (http://stackoverflow.com/a/3940445)
	String FIND_ITEMID_USERID_PAIRS_BY_CONTENT_AND_ITEMS = "SELECT r.itemId, r.learner.userId FROM "
		+ Rating.class.getName()
		+ " AS r where r.ratingCriteria.toolContentId=:contentId AND r.itemId IN (:itemIds)"
		+ " AND r.toolSessionId=:toolSessionId";

	String FIND_ITEMID_USERID_COMMENT_PAIRS_BY_CONTENT_AND_ITEMS = "SELECT comment.itemId, comment.learner.userId FROM "
		+ RatingComment.class.getName()
		+ " AS r where comment.ratingCriteria.toolContentId=:contentId AND comment.itemId IN (:itemIds) AND comment.ratingCriteria.commentsEnabled IS TRUE"
		+ " AND comment.toolSessionId=:toolSessionId";

	List<Object[]> ratedItemObjs = getSession().createQuery(FIND_ITEMID_USERID_PAIRS_BY_CONTENT_AND_ITEMS)
		.setParameter("contentId", contentId).setParameter("toolSessionId", toolSessionId)
		.setParameterList("itemIds", itemIds).list();

	List<Object[]> commentedItemObjs = getSession()
		.createQuery(FIND_ITEMID_USERID_COMMENT_PAIRS_BY_CONTENT_AND_ITEMS).setParameter("contentId", contentId)
		.setParameterList("itemIds", itemIds).setParameter("toolSessionId", toolSessionId).list();

	return createUsersRatedEachItem(itemIds, excludeUserId, itemIdToRatedUsersCountMap, ratedItemObjs,
		commentedItemObjs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Long, Long> countUsersRatedEachItemByCriteria(final Long criteriaId, final Long toolSessionId,
	    final Collection<Long> itemIds, Integer excludeUserId) {

	HashMap<Long, Long> itemIdToRatedUsersCountMap = new HashMap<>();
	if (itemIds.isEmpty()) {
	    return itemIdToRatedUsersCountMap;
	}

	// unions don't work in HQL so doing 2 separate DB queries (http://stackoverflow.com/a/3940445)
	String FIND_ITEMID_USERID_PAIRS_BY_CONTENT_AND_ITEMS = "SELECT r.itemId, r.learner.userId FROM "
		+ Rating.class.getName()
		+ " AS r where r.ratingCriteria.ratingCriteriaId=:criteriaId AND r.itemId IN (:itemIds)"
		+ " AND r.toolSessionId=:toolSessionId";

	String FIND_ITEMID_USERID_COMMENT_PAIRS_BY_CONTENT_AND_ITEMS = "SELECT comment.itemId, comment.learner.userId FROM "
		+ RatingComment.class.getName()
		+ " AS r where comment.ratingCriteria.ratingStyle=0 AND comment.ratingCriteria.ratingCriteriaId=:criteriaId "
		+ " AND comment.itemId IN (:itemIds) AND comment.ratingCriteria.commentsEnabled IS TRUE"
		+ " AND comment.toolSessionId=:toolSessionId";

	List<Object[]> ratedItemObjs = getSession().createQuery(FIND_ITEMID_USERID_PAIRS_BY_CONTENT_AND_ITEMS)
		.setParameter("criteriaId", criteriaId).setParameter("toolSessionId", toolSessionId)
		.setParameterList("itemIds", itemIds).list();

	List<Object[]> commentedItemObjs = getSession()
		.createQuery(FIND_ITEMID_USERID_COMMENT_PAIRS_BY_CONTENT_AND_ITEMS)
		.setParameter("criteriaId", criteriaId).setParameterList("itemIds", itemIds)
		.setParameter("toolSessionId", toolSessionId).list();

	return createUsersRatedEachItem(itemIds, excludeUserId, itemIdToRatedUsersCountMap, ratedItemObjs,
		commentedItemObjs);
    }

    private Map<Long, Long> createUsersRatedEachItem(final Collection<Long> itemIds, Integer excludeUserId,
	    HashMap<Long, Long> itemIdToRatedUsersCountMap, List<Object[]> ratedItemObjs,
	    List<Object[]> commentedItemObjs) {
	for (Long itemId : itemIds) {
	    HashSet<Integer> userIds = new HashSet<>();

	    //put all corresponding userIds into the userIds set
	    for (Object[] ratedItemObj : ratedItemObjs) {
		Long itemIdIter = (Long) ratedItemObj[0];
		Integer userIdIter = (Integer) ratedItemObj[1];

		if (itemIdIter.equals(itemId)) {
		    userIds.add(userIdIter);
		}
	    }

	    //put all corresponding userIds into the userIds set
	    for (Object[] commentedItemObj : commentedItemObjs) {
		Long itemIdIter = (Long) commentedItemObj[0];
		Integer userIdIter = (Integer) commentedItemObj[1];

		if (itemIdIter.equals(itemId)) {
		    userIds.add(userIdIter);
		}
	    }

	    //exclude user that is doing comment now
	    userIds.remove(excludeUserId);

	    //count how many userIds is rated and commented item
	    itemIdToRatedUsersCountMap.put(itemId, new Long(userIds.size()));
	}

	return itemIdToRatedUsersCountMap;
    }

    /**
     * Used by tools to get the ratings and comments relating to their items. To be used within SQL and supply the
     * toolContentId as :toolContentId.
     * If getAllValues == true then returns data for all users (monitoring), otherwise just the data for a single user.
     * See Peer Review for example usage.
     */
    @Override
    public String getRatingSelectJoinSQL(Integer ratingStyle, boolean getByUser) {
	if (ratingStyle == RatingCriteria.RATING_STYLE_COMMENT) {
	    return getByUser ? TOOL_SELECT_LEFT_JOIN_BY_USER_COMMENT : TOOL_SELECT_LEFT_JOIN_FOR_USER_COMMENT;
	} else {
	    return getByUser ? TOOL_SELECT_LEFT_JOIN_BY_USER_STANDARD : TOOL_SELECT_LEFT_JOIN_FOR_USER_STANDARD;
	}
    }

    /**
     * Get all the raw ratings for a combination of criteria and item ids. Used by Peer Review to do SPA analysis.
     */
    @Override
    public List getRatingsByCriteriasAndItems(Collection<Long> ratingCriteriaIds, Collection<Long> itemIds) {
	return getSession().createQuery(FIND_RATINGS_BY_ITEM_CRITERIA)
		.setParameterList("ratingCriteriaIds", ratingCriteriaIds).setParameterList("itemIds", itemIds).list();
    }

}
