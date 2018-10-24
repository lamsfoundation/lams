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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.lamsfoundation.lams.rating.model.RatingCriteria;

public class ItemRatingCriteriaDTO {

    //common properties
    private RatingCriteria ratingCriteria;

    //rating properties
    private String userRating;
    private String averageRating;
    private String numberOfVotes;
    private Number averageRatingAsNumber;

    //used in case of filling with all rating
    private List<RatingDTO> ratingDtos;

    public ItemRatingCriteriaDTO() {
    }

    public ItemRatingCriteriaDTO(Number rating, String numberOfVotes) {
	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);
	this.averageRatingAsNumber = rating;
	this.averageRating = numberFormat.format(rating);
	this.numberOfVotes = numberOfVotes;
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

    public List<RatingDTO> getRatingDtos() {
	return ratingDtos;
    }

    public void setRatingDtos(List<RatingDTO> ratingDtos) {
	this.ratingDtos = ratingDtos;
    }

    public Number getAverageRatingAsNumber() {
	return averageRatingAsNumber;
    }

    public void setAverageRatingAsNumber(Number averageRatingAsFloat) {
	this.averageRatingAsNumber = averageRatingAsFloat;
    }

}
