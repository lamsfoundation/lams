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
package org.lamsfoundation.lams.rating.dto;

import java.util.List;

import org.lamsfoundation.lams.rating.model.RatingComment;
import org.lamsfoundation.lams.rating.model.RatingCriteria;

public class RatingCriteriaDTO {

    //common properties
    private Long itemId;
    private RatingCriteria ratingCriteria;

    //rating properties
    private String userRating;
    private String averageRating;
    private String numberOfVotes;
    
    //comments rating properties
    private List<RatingComment> ratingComments;
    private int commentsMinWordsLimit;

    public RatingCriteriaDTO() {
    }
    
    public RatingCriteriaDTO(String rating, String numberOfVotes) {
	this.averageRating = rating;
	this.numberOfVotes = numberOfVotes;
    }

    public Long getItemId() {
	return itemId;
    }

    public void setItemId(Long itemId) {
	this.itemId = itemId;
    }
    
    public RatingCriteria getRatingCriteria() {
	return ratingCriteria;
    }

    public void setRatingCriteria(RatingCriteria ratingCriteria) {
	this.ratingCriteria = ratingCriteria;
    }
    
    public String getUserRating() {
	return userRating;
    }

    public void setUserRating(String userRating) {
	this.userRating = userRating;
    }

    public String getAverageRating() {
	return averageRating;
    }

    public void setAverageRating(String averageRating) {
	this.averageRating = averageRating;
    }

    public String getNumberOfVotes() {
	return numberOfVotes;
    }

    public void setNumberOfVotes(String numberOfVotes) {
	this.numberOfVotes = numberOfVotes;
    }
    
    public List<RatingComment> getRatingComments() {
	return ratingComments;
    }

    public void setRatingComments(List<RatingComment> ratingComments) {
	this.ratingComments = ratingComments;
    }
    
    public int getCommentsMinWordsLimit() {
	return commentsMinWordsLimit;
    }

    public void setCommentsMinWordsLimit(int commentsMinWordsLimit) {
	this.commentsMinWordsLimit = commentsMinWordsLimit;
    }
  
}
