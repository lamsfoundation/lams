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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.lamsfoundation.lams.usermanagement.User;

/**
 * The actual rating left by a user
 */
@Entity
@Table(name = "lams_rating")
public class Rating implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 4831819420875651676L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating_criteria_id")
    private RatingCriteria ratingCriteria;

    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User learner;

    @Column
    private Float rating;

    @Column(name = "tool_session_id")
    private Long toolSessionId;

    public Rating() {
    }

    public Rating(Long itemId, RatingCriteria ratingCriteria, User learner, Float rating) {
	this.itemId = itemId;
	this.ratingCriteria = ratingCriteria;
	this.learner = learner;
	this.rating = rating;
    }

    public Rating(Long itemId, RatingCriteria ratingCriteria, User learner, Long toolSessionId, Float rating) {
	this.itemId = itemId;
	this.ratingCriteria = ratingCriteria;
	this.learner = learner;
	this.toolSessionId = toolSessionId;
	this.rating = rating;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
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

    public User getLearner() {
	return learner;
    }

    public void setLearner(User learner) {
	this.learner = learner;
    }

    public void setRating(Float rating) {
	this.rating = rating;
    }

    public Float getRating() {
	return this.rating;
    }

    public Long getToolSessionId() {
	return toolSessionId;
    }

    public void setToolSessionId(Long toolSessionId) {
	this.toolSessionId = toolSessionId;
    }
}
