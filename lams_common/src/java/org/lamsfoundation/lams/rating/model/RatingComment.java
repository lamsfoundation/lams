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


package org.lamsfoundation.lams.rating.model;

import java.util.Date;

import org.lamsfoundation.lams.usermanagement.User;

/**
 */
public class RatingComment implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 4831819420875651676L;

    private Long uid;

    private RatingCriteria ratingCriteria;

    private Long itemId;

    private User learner;

    private String comment;

    private Date postedDate;
    
    private Long toolSessionId;

    public RatingComment() {
	this.postedDate = new Date();
    }

    public RatingComment(Long itemId, RatingCriteria ratingCriteria, User learner, String comment) {
	this.itemId = itemId;
	this.ratingCriteria = ratingCriteria;
	this.learner = learner;
	this.comment = comment;
	this.postedDate = new Date();
    }

    public RatingComment(Long itemId, RatingCriteria ratingCriteria, User learner, Long toolSessionId, String comment) {
	this.itemId = itemId;
	this.ratingCriteria = ratingCriteria;
	this.learner = learner;
	this.toolSessionId = toolSessionId;
	this.comment = comment;
	this.postedDate = new Date();
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

    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getComment() {
	return this.comment;
    }

    public Date getPostedDate() {
	return postedDate;
    }

    public void setPostedDate(Date postedDate) {
	this.postedDate = postedDate;
    }
    
    public Long getToolSessionId() {
	return toolSessionId;
    }

    public void setToolSessionId(Long toolSessionId) {
	this.toolSessionId = toolSessionId;
    }

}
