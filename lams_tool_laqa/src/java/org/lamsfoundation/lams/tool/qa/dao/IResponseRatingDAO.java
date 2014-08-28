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
package org.lamsfoundation.lams.tool.qa.dao;

import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.tool.qa.ResponseRating;
import org.lamsfoundation.lams.tool.qa.dto.AverageRatingDTO;

/**
 * DAO interface for <code>ResponseRating</code>.
 * 
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.qa.ResponseRating
 */
public interface IResponseRatingDAO {
    
    /**
     * Return responseRating by the given imageUid and userId.
     * 
     * @param responseId
     * @param userId
     * @return
     */
    public ResponseRating getRatingByResponseAndUser(Long responseId, Long userId);

    /**
     * Return list of responseRating by the the given imageUid.
     * 
     * @param ResponseId
     * @param userId
     * @return
     */
    public List<ResponseRating> getRatingsByResponse(Long responseId);
    
    /**
     * Generic method to save an object - handles both update and insert.
     * 
     * @param o
     *            the object to save
     */
    public void saveObject(Object o);
    
    AverageRatingDTO getAverageRatingDTOByResponse(Long responseId);
    
    public void removeResponseRating(ResponseRating rating);
    
    public List<ResponseRating> getRatingsByUser(Long userUid);
    
    Map<Long, AverageRatingDTO> getAverageRatingDTOByQuestionAndSession(Long questionUid, Long qaSessionId);
    
    Map<Long, AverageRatingDTO> getAverageRatingDTOByUserAndContentId(Long userUid, Long contentId);
}
