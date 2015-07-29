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

package org.lamsfoundation.lams.rating.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingCriteria;

public interface IRatingService {

    void saveOrUpdateRating(Rating rating);

    /**
     * Read modified rating criterias from request, then update existing ones/add new ones/delete removed ones. Used on
     * saving content action in authoring.
     * 
     * @param request
     * @param oldCriterias
     *            criterias stored in the DB
     * @param toolContentId
     */
    void saveRatingCriterias(HttpServletRequest request, Collection<RatingCriteria> oldCriterias, Long toolContentId);

    List<RatingCriteria> getCriteriasByToolContentId(Long toolContentId);

    RatingCriteria getCriteriaByCriteriaId(Long ratingCriteriaId);

    RatingCriteria getCriteriaByCriteriaId(Long ratingCriteriaId, Class clasz);
    
    /**
     * Checks if comments are enabled (i.e. if comments' criteria is available).
     * 
     * @param toolContentId
     * @return
     */
    boolean isCommentsEnabled(Long toolContentId);
    
    /**
     * If comments enabled then there might be commentsMinWords limit set. Returns its value or 0 otherwise.
     * 
     * @param toolContentId
     * @return
     */
    int getCommentsMinWordsLimit(Long toolContentId);

    /**
     * Return Rating by the given itemId and userId.
     * 
     * @param itemId
     * @param userId
     * @return
     */
    Rating getRatingByItemAndUser(Long itemId, Integer userId);

    /**
     * Return list of imageRatings by the the given itemId.
     * 
     * @param itemId
     * @return
     */
    List<Rating> getRatingsByItem(Long itemId);

    ItemRatingCriteriaDTO rateItem(RatingCriteria criteria, Integer userId, Long itemId, float ratingFloat);

    void commentItem(RatingCriteria ratingCriteria, Integer userId, Long itemId, String comment);

    /**
     * Returns results for all items. If result is needed for only one item provide provide it as a single element in a
     * itemIds list.
     * 
     * @param contentId
     * @param itemIds
     * @param isCommentsByOtherUsersRequired
     *            whether required just comment from the current user or by all users
     * @param userId
     * @return
     */
    List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId);
    
    /**
     * Returns item DTO with all corresponding ratings and comments. Doesn't contain average and total amount of rates.
     * 
     * @param contentId
     * @param itemId
     * @return
     */
    ItemRatingDTO getRatingCriteriaDtoWithActualRatings(Long contentId, Long itemId);

    /**
     * Returns number of images rated by specified user in a current activity. It counts comments as ratings. This
     * method is applicable only for RatingCriterias of LEARNER_ITEM_CRITERIA_TYPE type.
     * 
     * @param toolContentId
     * @param userId
     * @return
     */
    int getCountItemsRatedByUser(final Long toolContentId, final Integer userId);
    
    /**
     * Count how many users rated and commented each item.
     * 
     * @param contentId
     * @param itemIds
     * @param excludeUserId
     * @return
     */
    Map<Long, Long> countUsersRatedEachItem(final Long contentId, final Collection<Long> itemIds, Integer excludeUserId);

}
