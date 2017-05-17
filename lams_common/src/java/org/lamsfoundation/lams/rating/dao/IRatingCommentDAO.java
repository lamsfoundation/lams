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

package org.lamsfoundation.lams.rating.dao;

import java.util.Collection;
import java.util.List;

import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.rating.model.RatingComment;

public interface IRatingCommentDAO {

    /**
     * Returns list of RatingCommentDTO which hold only minimum information required for displaying in Rating.tag
     * Created in order to lighten the data passed from DB.
     *
     * @param ratingCriteriaId
     * @return
     */
    List<RatingCommentDTO> getCommentsByCriteria(Long ratingCriteriaId, Long toolSessionId);

    /**
     * Constructs ItemRatingCriteriaDTO for rating criteria that allows comments.
     *
     * @param ratingCriteriaId
     * @param itemId
     * @param userId
     * @return
     */
    List<RatingCommentDTO> getCommentsByCriteriaAndItem(Long ratingCriteriaId, Long toolSessionId, Long itemId);

    /**
     * Constructs ItemRatingCriteriaDTO for rating criteria that allows comments. Returns comments only for itemIds
     * supplied.
     *
     * @param ratingCriteriaId
     * @param itemIds
     * @return
     */
    List<RatingCommentDTO> getCommentsByCriteriaAndItems(Long ratingCriteriaId, Long toolSessionId, Collection<Long> itemIds);

    List<RatingCommentDTO> getCommentsByCriteriaAndItemsAndUser(Long ratingCriteriaId, Collection<Long> itemIds,
	    Integer userId);

    /**
     * Get the comment relating to a ranking/hedging/star criteria, rather than the comment for a comment criteria. 
     */
    List<RatingCommentDTO> getRelatedCommentByCriteriaAndUser(Long ratingCriteriaId, Integer userId);

    RatingComment getComment(Long ratingCriteriaId, Integer userId, Long itemId);
    
    List<RatingComment> getCommentsByContentAndUser(Long contentId, Integer userId);

}
