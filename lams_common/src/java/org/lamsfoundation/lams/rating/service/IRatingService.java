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

package org.lamsfoundation.lams.rating.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.rating.RatingException;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.rating.dto.StyledCriteriaRatingDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingCriteria;

import com.fasterxml.jackson.databind.node.ArrayNode;

public interface IRatingService {

    void saveOrUpdateRating(Rating rating);

    /**
     * Save a group of ratings as the new ratings for this criteria, marking any existing ratings NULL.
     * Returns the number of "real" ratings, which should be newRatings.size.
     *
     * @return
     */
    public int rateItems(RatingCriteria ratingCriteria, Long toolSessionId, Integer userId,
	    Map<Long, Float> newRatings);

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

    /**
     * Save an already set up rating criteria. Only used when there will only ever be one anonymous criteria, like in
     * Share Resources
     *
     * @param criteria
     * @return
     */
    LearnerItemRatingCriteria saveLearnerItemRatingCriteria(Long toolContentId, String title, Integer orderId,
	    int ratingStyle, boolean withComments, int minWordsInComment) throws RatingException;

    /**
     * Delete all the rating criteria linked to a tool content. This allows you to delete criteria created with
     * saveToolStarRatingCriteria
     *
     * @param toolContentId
     * @return
     */
    public int deleteAllRatingCriterias(Long toolContentId);

    List<RatingCriteria> getCriteriasByToolContentId(Long toolContentId);

    RatingCriteria getCriteriaByCriteriaId(Long ratingCriteriaId);

    @SuppressWarnings("rawtypes")
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

    ItemRatingCriteriaDTO rateItem(RatingCriteria criteria, Long toolSessionId, Integer userId, Long itemId,
	    float ratingFloat);

    void commentItem(RatingCriteria ratingCriteria, Long toolSessionId, Integer userId, Long itemId, String comment);

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
    List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Long toolSessionId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId);

    /**
     * Used by tools to get the ratings and comments relating to their items. To be used within SQL and supply the
     * toolContentId as :toolContentId,
     * criteria id as :ratingCriteriaId and current user id as :userId
     * If getByUser == true then returns data for all users, as left by the current user, otherwise gives the data for
     * the current user as left by other users
     * See Peer Review for example usage.
     */
    String getRatingSelectJoinSQL(Integer ratingStyle, boolean getByUser);

    /**
     * Convert the raw data from the database to StyledCriteriaRatingDTO and StyleRatingDTO. The rating service expects
     * its own fields
     * to be first in each Object array, and the last item in the array to be an item description (eg formatted user's
     * name)
     * Will go back to the database for the justification comment that would apply to hedging.
     */
    StyledCriteriaRatingDTO convertToStyledDTO(RatingCriteria ratingCriteria, Long currentUser,
	    boolean includeCurrentUser, List<Object[]> rawDataRows);

    /**
     * Convert the raw data from the database to JSON rows similar to StyleRatingDTO. The rating service expects its own
     * fields
     * to be first in each Object array, and the last item in the array to be an item description (eg formatted user's
     * name)
     * Will go back to the database for the justification comment that would apply to hedging.
     */
    ArrayNode convertToStyledJSON(RatingCriteria ratingCriteria, Long toolSessionId, Long currentUserId,
	    boolean includeCurrentUser, List<Object[]> rawDataRows, boolean needRatesPerUser);

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
     * Returns number of items rated by specified user in a current activity, for a particular criteria. It counts
     * comments as ratings
     * iff it is a comment rating. This method is applicable only for RatingCriterias of LEARNER_ITEM_CRITERIA_TYPE
     * type.
     *
     * @param toolContentId
     * @param userId
     * @return
     */
    int getCountItemsRatedByUserByCriteria(final Long criteriaId, final Integer userId);

    /**
     * Removes all ratings and comments left by the specified user.
     *
     * @param contentId
     * @param userId
     * @return
     */
    void removeUserCommitsByContent(final Long contentId, final Integer userId);

    /**
     * Count how many users rated and commented each item, limiting them to a single session if toolSessionId is not
     * null
     * Used only if the tool's request it.
     *
     * @param contentId
     * @param itemIds
     * @param excludeUserId
     * @return
     */
    Map<Long, Long> countUsersRatedEachItem(final Long contentId, final Long toolSessionId,
	    final Collection<Long> itemIds, Integer excludeUserId);

    Map<Long, Long> countUsersRatedEachItemByCriteria(final Long criteriaId, final Long toolSessionId,
	    final Collection<Long> itemIds, Integer excludeUserId);

    /**
     * Get all the raw ratings for a combination of criteria and item ids. Used by Peer Review to do SPA analysis.
     */
    List getRatingsByCriteriasAndItems(Collection<Long> ratingCriteriaIds, Collection<Long> itemIds);

    List<RatingCommentDTO> getCommentsByCriteriaAndItem(Long ratingCriteriaId, Long toolSessionId, Long itemId);

    List<String> getRubricsColumnHeaders(int groupId);

    int getNextRatingCriteriaGroupId();
}