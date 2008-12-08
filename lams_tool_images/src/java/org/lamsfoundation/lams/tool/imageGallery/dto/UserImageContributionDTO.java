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
package org.lamsfoundation.lams.tool.imageGallery.dto;  

import java.util.Set;

import org.lamsfoundation.lams.tool.imageGallery.model.ImageComment;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryUser;
 
/**
 * List contains following element: <br>
 * 
 * <li>session_name</li>
 * <li>user</li>
 * <li>ImageGalleryItem.rating</li>
 * <li>isVotedForThisImage</li>
 * <li>ImageGalleryItem.comments</li>
 * 
 * @author Andrey Balan
 * 
 */
public class UserImageContributionDTO {

    private String sessionName;
    private float averageRating;
    private int numberRatings;  
    private int numberOfVotes;
    
    private ImageGalleryUser user;
    private int rating;
    private boolean isVotedForThisImage;
    
    private Set<ImageComment> comments;
    
    //only for export needs
    private ImageGalleryItem image;

    public UserImageContributionDTO() {
    }

    /**
     * Contruction method for monitoring summary function.
     * 
     * @param sessionName
     * @param item
     * @param isInitGroup
     */
    public UserImageContributionDTO(String sessionName, ImageGalleryUser user) {
	this.sessionName = sessionName;
	this.user = user;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }
    
    /**
     * Returns image average rating.
     * 
     * @return image average rating
     */
    public float getAverageRating() {
	return averageRating;
    }

    /**
     * Sets image average rating.
     * 
     * @param averageRating
     *                image average rating
     */
    public void setAverageRating(float averageRating) {
	this.averageRating = averageRating;
    }
    
    /**
     * Returns image sequence number.
     * 
     * @return image sequence number
     */
    public int getNumberRatings() {
	return numberRatings;
    }

    /**
     * Sets image number of rates.
     * 
     * @param numberRates
     *                image number of rates
     */
    public void setNumberRatings(int numberRatings) {
	this.numberRatings = numberRatings;
    }
    
    /**
     * Returns image sequence number.
     * 
     * @return image sequence number
     */
    public int getNumberOfVotes() {
	return numberOfVotes;
    }

    /**
     * Sets image number of rates.
     * 
     * @param numberRates
     *                image number of rates
     */
    public void setNumberOfVotes(int numberOfVotes) {
	this.numberOfVotes = numberOfVotes;
    }

    public ImageGalleryUser getUser() {
	return user;
    }

    public void setUser(ImageGalleryUser user) {
	this.user = user;
    }

    public int getRating() {
	return rating;
    }

    public void setRating(int rating) {
	this.rating = rating;
    }
    
    public boolean isVotedForThisImage() {
	return isVotedForThisImage;
    }

    public void setVotedForThisImage(boolean isVotedForThisImage) {
	this.isVotedForThisImage = isVotedForThisImage;
    }    

    public Set<ImageComment> getComments() {
	return comments;
    }

    public void setComments(Set<ImageComment> comments) {
	this.comments = comments;
    }
    
    public ImageGalleryItem getImage() {
	return image;
    }

    public void setImage(ImageGalleryItem image) {
	this.image = image;
    }

}

 