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


package org.lamsfoundation.lams.tool.imageGallery.dto;

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
    private int numberOfVotesForImage;

    private ImageGalleryUser user;
    private boolean isVotedForThisImage;

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
     * Returns image number of votes.
     *
     * @return image sequence number
     */
    public int getNumberOfVotesForImage() {
	return numberOfVotesForImage;
    }

    /**
     * Sets image number of votes.
     *
     * @param numberRates
     *            image number of rates
     */
    public void setNumberOfVotesForImage(int numberOfVotesForImage) {
	this.numberOfVotesForImage = numberOfVotesForImage;
    }

    public ImageGalleryUser getUser() {
	return user;
    }

    public void setUser(ImageGalleryUser user) {
	this.user = user;
    }

    public boolean isVotedForThisImage() {
	return isVotedForThisImage;
    }

    public void setVotedForThisImage(boolean isVotedForThisImage) {
	this.isVotedForThisImage = isVotedForThisImage;
    }
}