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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.rating.dao.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.rating.dao.IRatingCommentDAO;
import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.rating.model.RatingComment;

public class RatingCommentDAO extends LAMSBaseDAO implements IRatingCommentDAO {
    private static final String FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM = "FROM " + RatingComment.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=? AND r.itemId=?";

    private static final String FIND_COMMENTS_BY_CRITERIA_AND_ITEM = "SELECT r.itemId, r.learner.userId, r.comment, CONCAT(r.learner.firstName, ' ', r.learner.lastName), r.postedDate "
	    + "FROM " + RatingComment.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=:ratingCriteriaId AND r.itemId=:itemId";

    private static final String FIND_COMMENTS_BY_CRITERIA_AND_ITEMS = "SELECT r.itemId, r.learner.userId, r.comment, CONCAT(r.learner.firstName, ' ', r.learner.lastName), r.postedDate "
	    + "FROM " + RatingComment.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=:ratingCriteriaId AND r.itemId IN (:itemIds)";

    private static final String FIND_COMMENTS_BY_CRITERIA_AND_ITEMS_AND_USER = "SELECT r.itemId, r.learner.userId, r.comment "
	    + "FROM " + RatingComment.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=:ratingCriteriaId AND r.itemId IN (:itemIds) AND r.learner.userId=:userId";

    private static final String FIND_COMMENTS_BY_CRITERIA = "SELECT r.itemId, r.learner.userId, r.comment " + "FROM "
	    + RatingComment.class.getName() + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.toolSessionId=?";

    private static final String FIND_RELATED_COMMENT_BY_CRITERIA_AND_USER = "SELECT r.itemId, r.learner.userId, r.comment FROM "
	    + RatingComment.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=:ratingCriteriaId AND r.learner.userId=:userId";

    @SuppressWarnings("unchecked")
    @Override
    public List<RatingCommentDTO> getCommentsByCriteriaAndItem(Long ratingCriteriaId, Long toolSessionId, Long itemId) {
	String queryText = FIND_COMMENTS_BY_CRITERIA_AND_ITEM;

	if (toolSessionId != null) {
	    queryText += " AND r.toolSessionId=:toolSessionId";
	}
	Query query = getSession().createQuery(queryText).setParameter("ratingCriteriaId", ratingCriteriaId)
		.setParameter("itemId", itemId);
	if (toolSessionId != null) {
	    query.setParameter("toolSessionId", toolSessionId);
	}
	List<Object[]> results = query.getResultList();

	return convertIntoCommentDtos(results);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RatingCommentDTO> getCommentsByCriteriaAndItems(Long ratingCriteriaId, Long toolSessionId,
	    Collection<Long> itemIds) {
	String queryText = FIND_COMMENTS_BY_CRITERIA_AND_ITEMS;

	if (toolSessionId != null) {
	    queryText += " AND r.toolSessionId=:toolSessionId";
	}
	Query query = getSession().createQuery(queryText).setParameter("ratingCriteriaId", ratingCriteriaId)
		.setParameterList("itemIds", itemIds);
	if (toolSessionId != null) {
	    query.setParameter("toolSessionId", toolSessionId);
	}
	List<Object[]> results = query.getResultList();
	return convertIntoCommentDtos(results);
    }

    @Override
    public List<RatingCommentDTO> getCommentsByCriteriaAndItemsAndUser(Long ratingCriteriaId, Collection<Long> itemIds,
	    Integer userId) {

	List<Object[]> results = getSession().createQuery(FIND_COMMENTS_BY_CRITERIA_AND_ITEMS_AND_USER)
		.setParameter("ratingCriteriaId", ratingCriteriaId).setParameterList("itemIds", itemIds)
		.setParameter("userId", userId).list();

	return convertIntoCommentDtos(results);
    }

    @Override
    public List<RatingCommentDTO> getCommentsByCriteria(Long ratingCriteriaId, Long toolSessionId) {
	List<Object[]> results = (doFind(FIND_COMMENTS_BY_CRITERIA, new Object[] { ratingCriteriaId, toolSessionId }));

	return convertIntoCommentDtos(results);
    }

    @Override
    public List<RatingCommentDTO> getRelatedCommentByCriteriaAndUser(Long ratingCriteriaId, Integer userId) {
	List<Object[]> results = getSession().createQuery(FIND_RELATED_COMMENT_BY_CRITERIA_AND_USER)
		.setParameter("ratingCriteriaId", ratingCriteriaId).setParameter("userId", userId).list();

	return convertIntoCommentDtos(results);
    }

    /*
     * Converts DB results presentation into list of RatingCommentDTO.
     *
     * @param results
     *
     * @return
     */
    private List<RatingCommentDTO> convertIntoCommentDtos(List<Object[]> results) {

	//populate RatingCommentDTOs list
	List<RatingCommentDTO> commentDtos = new LinkedList<>();
	for (Object[] result : results) {
	    Long itemId = (Long) result[0];
	    Long userId = ((Integer) result[1]).longValue();
	    String comment = (String) result[2];

	    //in case username and postedDate is also fetched from DB
	    String userName = "";
	    Date postedDate = null;
	    if (result.length > 3) {
		userName = (String) result[3];
		postedDate = (Date) result[4];
	    }

	    RatingCommentDTO commentDto = new RatingCommentDTO(itemId, userId, userName, comment, postedDate);
	    commentDtos.add(commentDto);
	}

	return commentDtos;
    }

    @Override
    public RatingComment getComment(Long ratingCriteriaId, Integer userId, Long itemId) {
	List<RatingComment> list = doFind(FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM,
		new Object[] { ratingCriteriaId, userId, itemId });
	if (list.size() > 0) {
	    return list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public List<RatingComment> getCommentsByContentAndUser(Long contentId, Integer userId) {
	final String FIND_RATINGS_BY_USER = "FROM " + RatingComment.class.getName()
		+ " AS r where r.ratingCriteria.toolContentId=? AND r.learner.userId=?";

	return doFind(FIND_RATINGS_BY_USER, new Object[] { contentId, userId });
    }
}