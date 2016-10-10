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



package org.lamsfoundation.lams.rating.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.StyledRatingDTO;
import org.lamsfoundation.lams.rating.model.Rating;

public interface IRatingDAO {

    void saveOrUpdate(Object object);
    
    void delete(Object object);

    Rating getRating(Long ratingCriteriaId, Integer userId, Long itemId);

    List<Rating> getRatingsByItem(Long contentId, Long itemId);

    List<Rating> getRatingsByUser(Long contentId, Integer userId);

    List<Rating> getRatingsByUserCriteria(Long criteriaId, Integer userId);

    /**
     * Returns rating statistics by particular item
     *
     * @param itemId
     * @return
     */
    ItemRatingCriteriaDTO getRatingAverageDTOByItem(Long ratingCriteriaId, Long itemId);

    List<Object[]> getRatingAverageByContentAndItem(Long contentId, Long itemId);

    /**
     * Returns rating statistics for specified itemIds.
     *
     * @param contentId
     * @param itemIds
     * @return
     */
    List<Object[]> getRatingAverageByContentAndItems(Long contentId, Collection<Long> itemIds);

    /**
     * Returns rating statistics for entire tool.
     *
     * @param contentId
     * @return
     */
    List<Object[]> getRatingAverageByContent(Long contentId);

    Rating get(Long uid);

    /**
     * Returns number of images rated by specified user in a current activity. It counts comments as ratings. This
     * method
     * is applicable only for RatingCriterias of LEARNER_ITEM_CRITERIA_TYPE type.
     *
     * @param toolContentId
     * @param userId
     * @return
     */
    int getCountItemsRatedByUser(final Long toolContentId, final Integer userId);

    /**
     * Returns number of items rated by specified user in a current activity, for a particular criteria. It counts comments as ratings
     * iff it is a comment rating. This method is applicable only for RatingCriterias of LEARNER_ITEM_CRITERIA_TYPE type.
     *
     * @param toolContentId
     * @param userId
     * @return
     */
    int getCountItemsRatedByUserByCriteria(final Long criteriaId, final Integer userId);

    /**
     * Count how many users rated and commented each item.
     *
     * @param contentId
     * @param itemIds
     * @param excludeUserId
     * @return
     */
    Map<Long, Long> countUsersRatedEachItem(final Long contentId, final Collection<Long> itemIds,
	    Integer excludeUserId);

    /**
     * Count how many users rated and commented each item for a particular criteria.
     *
     * @param contentId
     * @param itemIds
     * @param excludeUserId
     * @return
     */
    Map<Long, Long> countUsersRatedEachItemByCriteria(final Long criteriaId, final Collection<Long> itemIds,
	    Integer excludeUserId);
    
    /**
     *  Used by tools to get the ratings and comments relating to their items. To be used within SQL and supply the toolContentId as :toolContentId.
     *  See Peer Review for example usage.
     */
    String getRatingSelectJoinSQL(Integer ratingStyle, boolean getByUser);

}
