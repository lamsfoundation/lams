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


package org.lamsfoundation.lams.rating;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.RatingCriteria;

/**
 * Tool interface that defines the contract regarding Rating and RatingCriteria manipulation.
 *
 * @author Andrey Balan
 */
public interface ToolRatingManager {

    List<RatingCriteria> getRatingCriterias(Long toolContentId);

    boolean isCommentsEnabled(Long toolContentId);

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
     * Returns results for all items. If result is needed for only one item provide provide it as a single element in a
     * itemIds list.
     *
     * @param contentId
     * @param itemIds
     * @param isCommentsByOtherUsersRequired
     *            whether required just comment from the current user or by all users
     *            results.
     * @param userId
     * @return
     */
    List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Long toolSessionId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId);

    /**
     * Returns number of images rated by specified user in a current activity. It counts comments as ratings.
     *
     * @param toolContentId
     * @param userId
     * @return
     */
    int getCountItemsRatedByUser(Long toolContentId, Integer userId);

}