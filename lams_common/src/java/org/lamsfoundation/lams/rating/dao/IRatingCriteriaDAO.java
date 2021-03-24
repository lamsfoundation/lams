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

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.rating.model.RatingCriteria;

public interface IRatingCriteriaDAO extends IBaseDAO {

    void saveOrUpdate(RatingCriteria criteria);

    void deleteRatingCriteria(Long ratingCriteriaId);

    List<RatingCriteria> getByToolContentId(Long toolContentId);

    RatingCriteria getByRatingCriteriaId(Long ratingCriteriaId);

    RatingCriteria getByRatingCriteriaId(Long ratingCriteriaId, Class clasz);

    /**
     * Checks if comments are enabled (i.e. if comments' criteria is available).
     *
     * @param toolContentId
     * @return
     */
    boolean isCommentsEnabledForToolContent(Long toolContentId);

    /**
     * If comments enabled then there might be commentsMinWords limit set. Returns its value or 0 otherwise.
     *
     * @param toolContentId
     * @return
     */
    int getCommentsMinWordsLimitForToolContent(Long toolContentId);

    int getNextRatingCriteriaGroupId();

    List<String> getRubricsColumnHeaders(int groupId);
}