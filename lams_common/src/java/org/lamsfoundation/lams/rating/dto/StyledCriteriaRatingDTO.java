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


package org.lamsfoundation.lams.rating.dto;

import java.util.List;

import org.lamsfoundation.lams.rating.model.RatingCriteria;

/** 
 * Use in conjunction with the new AuthoringRatingAllStyleCriteria tag to manage a mix of stars/hedging/ranking, as used in Peer Review.
 * 
 * @author Fiona Malikoff
 *
 */
public class StyledCriteriaRatingDTO {

     //common properties
    private RatingCriteria ratingCriteria;
    
    // all the ratings done by the current user, for a set of items ids.
    private List<StyledRatingDTO> ratingDtos;

    private String justificationComment;
    private Integer countRatedItems;
    
    // ratings which are in the same group
    private List<StyledCriteriaRatingDTO> criteriaGroup;
    
    public StyledCriteriaRatingDTO() {
    }

    public RatingCriteria getRatingCriteria() {
        return ratingCriteria;
    }

    public void setRatingCriteria(RatingCriteria ratingCriteria) {
        this.ratingCriteria = ratingCriteria;
    }

    public List<StyledRatingDTO> getRatingDtos() {
        return ratingDtos;
    }

    public void setRatingDtos(List<StyledRatingDTO> ratingDtos) {
        this.ratingDtos = ratingDtos;
    }

    public String getJustificationComment() {
        return justificationComment;
    }

    public void setJustificationComment(String justificationComment) {
        this.justificationComment = justificationComment;
    }

    public Integer getCountRatedItems() {
        return countRatedItems;
    }

    public void setCountRatedItems(Integer countRatedItems) {
        this.countRatedItems = countRatedItems;
    }

    public List<StyledCriteriaRatingDTO> getCriteriaGroup() {
        return criteriaGroup;
    }

    public void setCriteriaGroup(List<StyledCriteriaRatingDTO> criteriaGroup) {
        this.criteriaGroup = criteriaGroup;
    }
}