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


/** 
 * Use in conjunction with the new AuthoringRatingAllStyleCriteria tag to manage a mix of stars/hedging/ranking, as used in Peer Review.
 * 
 * @author Fiona Malikoff
 *
 */
public class StyledRatingDTO {

    private Long itemId; 
    private String itemDescription; // user readable version of the item, usually the name of the learner that has been rated
    private String itemDescription2; // system detail for addition description content, usually the portrait id of the learner that has been rated.
    
    private String userRating; // rating left by the current user
    private String averageRating; // average of ratings by all users
    private String numberOfVotes; // number of "all users" used to calculate average
    
    private String comment; // comment left by the current user

    public StyledRatingDTO(Long itemId) {
	this.itemId = itemId;
    }
    
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getItemDescription2() {
	return itemDescription2;
    }

    public void setItemDescription2(String itemDescription2) {
	this.itemDescription2 = itemDescription2;
    }
    


}
