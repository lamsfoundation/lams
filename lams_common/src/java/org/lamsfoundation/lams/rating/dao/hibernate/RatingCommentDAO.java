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

/* $Id$ */
package org.lamsfoundation.lams.rating.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.rating.dao.IRatingCommentDAO;
import org.lamsfoundation.lams.rating.dto.RatingCriteriaDTO;
import org.lamsfoundation.lams.rating.model.RatingComment;

public class RatingCommentDAO extends BaseDAO implements IRatingCommentDAO {
    private static final String FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM = "FROM " + RatingComment.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.learner.userId=? AND r.itemId=?";

    private static final String FIND_COMMENTS_BY_CRITERIA_AND_ITEM = "FROM " + RatingComment.class.getName()
	    + " AS r where r.ratingCriteria.ratingCriteriaId=? AND r.itemId=?";

    private List<RatingComment> getCommentsByCriteriaAndItem(Long ratingCriteriaId, Long itemId) {
	return (List<RatingComment>) (getHibernateTemplate().find(FIND_COMMENTS_BY_CRITERIA_AND_ITEM, new Object[] {
		ratingCriteriaId, itemId }));
    }

    @Override
    public RatingComment getRatingComment(Long ratingCriteriaId, Integer userId, Long itemId) {
	List<RatingComment> list = getHibernateTemplate().find(FIND_RATING_BY_CRITERIA_AND_USER_AND_ITEM,
		new Object[] { ratingCriteriaId, userId, itemId });
	if (list.size() > 0) {
	    return (RatingComment) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public RatingCriteriaDTO getCommentsRatingDTO(Long ratingCriteriaId, Long itemId, Integer userId) {

	List<RatingComment> ratingComments = getCommentsByCriteriaAndItem(ratingCriteriaId, itemId);

	RatingCriteriaDTO criteriaDto = new RatingCriteriaDTO();
	criteriaDto.setItemId(itemId);
	criteriaDto.setRatingComments(ratingComments);
	return criteriaDto;
    }
}
