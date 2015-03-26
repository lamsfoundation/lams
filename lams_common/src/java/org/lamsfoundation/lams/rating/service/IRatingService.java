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

import java.util.List;

import org.lamsfoundation.lams.rating.dto.RatingDTO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.ToolActivityRatingCriteria;

public interface IRatingService {

//    Long createRating(Long id, Integer idType, String signature, Integer userID, String title, String entry);
//
//    TreeMap<Long, List<Rating>> getEntryByLesson(Integer userID, Integer idType);
//
//    List<Rating> getEntry(Long id, Integer idType, String signature, Integer userID);
//
//    List<Rating> getEntry(Long id, Integer idType, String signature);
//
//    List<Rating> getEntry(Long id, Integer idType, Integer userID);
//    
//    List<Rating> getEntry(Integer userID);
//
//    List<Rating> getEntry(Integer userID, Integer idType);
//
//    List<Rating> getEntry(Integer userID, Long lessonID);
//
//    Rating getEntry(Long uid);
//
//    void updateEntry(Long uid, String title, String entry);
//
//    void updateEntry(Rating rating);

    void saveOrUpdateRating(Rating rating);
    
    void saveOrUpdateRatingCriteria(RatingCriteria criteria);
    
    void deleteRatingCriteria(Long ratingCriteriaId);
    
    List<RatingCriteria> getCriteriasByToolContentId(Long toolContentId);
    
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
    
    RatingDTO rateItem(Long ratingCriteriaId, Integer userId, Long itemId, float ratingFloat);
    
    RatingDTO getRatingDTOByUser(Long ratingCriteriaId, Long itemId, Integer userId);

//    IUserManagementService getUserManagementService();
//
//    MessageService getMessageService();
}
