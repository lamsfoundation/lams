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



package org.lamsfoundation.lams.rating.model;

import org.lamsfoundation.lams.usermanagement.User;

/**
 */
public class Rating implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 4831819420875651676L;

    private Long uid;

    private RatingCriteria ratingCriteria;

    private Long itemId;

    private User learner;

    private Float rating;

    public Rating() {
    }

    public Rating(Long itemId, RatingCriteria ratingCriteria, User learner, Float rating) {
	this.itemId = itemId;
	this.ratingCriteria = ratingCriteria;
	this.learner = learner;
	this.rating = rating;
    }

    /**
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     */
    public Long getItemId() {
	return itemId;
    }

    public void setItemId(Long itemId) {
	this.itemId = itemId;
    }

    /**
     */
    public RatingCriteria getRatingCriteria() {
	return ratingCriteria;
    }

    public void setRatingCriteria(RatingCriteria ratingCriteria) {
	this.ratingCriteria = ratingCriteria;
    }

    /**
     */
    public User getLearner() {
	return learner;
    }

    public void setLearner(User learner) {
	this.learner = learner;
    }

    /**
     */
    public void setRating(Float rating) {
	this.rating = rating;
    }

    public Float getRating() {
	return this.rating;
    }
}
