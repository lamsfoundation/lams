/***************************************************************************
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
 * ************************************************************************
 */
/* $Id$ */
package org.lamsfoundation.lams.rating.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Nullable;

/**
 * Base class for all RatingCriterias. If you add another subclass, you must update
 * RatingCriteriaDAO.getRatingCriteriaByRatingCriteriaId() and add a ACTIVITY_TYPE constant.
 */
public abstract class RatingCriteria implements Serializable, Nullable, Comparable, Cloneable {

    private static final Logger log = Logger.getLogger(RatingCriteria.class);
    private static final long serialVersionUID = -2282899640789455260L;
    // ---------------------------------------------------------------------
    // Class Level Constants
    // ---------------------------------------------------------------------
    /*
     * static final variables indicating the type of activities available for a LearningDesign. As new types of
     * activities are added, these constants must be updated, as well as
     * RatingCriteriaDAO.getRatingCriteriaByRatingCriteriaId()
     *
     * OPTIONS_WITH_SEQUENCES_TYPE is set up just to support Flash. The server treads OptionsRatingCriteria and
     * OptionalSequenceRatingCriteria the same.
     */
    /* **************************************************************** */
    public static final int TOOL_ACTIVITY_CRITERIA_TYPE = 1;
    public static final int AUTHORED_ITEM_CRITERIA_TYPE = 2;
    public static final int LEARNER_ITEM_CRITERIA_TYPE = 3;
    public static final int LESSON_CRITERIA_TYPE = 4;
    /** *************************************************************** */

    /**
     * Entries for an ratingCriteria in a language property file
     */
    public static final String I18N_TITLE = "rating.criteria.title";
    public static final String I18N_DESCRIPTION = "rating.criteria.description";
    public static final String I18N_HELP_TEXT = "rating.criteria.helptext";

    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------

    /** identifier field */
    private Long ratingCriteriaId;

    /** Title of the ratingCriteria */
    private String title;

    /**
     * Indicates the order in which the activities appear inside complex activities. Starts from 0, 1 and so on.
     */
    private Integer orderId;

    /** The type of ratingCriteria */
    private Integer ratingCriteriaTypeId;

    private boolean commentsEnabled;

    private int commentsMinWordsLimit;

    // ---------------------------------------------------------------------
    // Object constructors
    // ---------------------------------------------------------------------

    /*
     * For the createDateTime fields, if the value is null, then it will default to the current time.
     */

    /** full constructor */
    public RatingCriteria(Long ratingCriteriaId, String title, Integer orderId, Date createDateTime,
	    Integer ratingCriteriaTypeId) {
	this.ratingCriteriaId = ratingCriteriaId;
	this.title = title;
	this.orderId = orderId;
	this.ratingCriteriaTypeId = ratingCriteriaTypeId;
    }

    /** default constructor */
    public RatingCriteria() {
    }

    /** minimal constructor */
    public RatingCriteria(Long ratingCriteriaId, Date createDateTime, RatingCriteria parentRatingCriteria,
	    Integer ratingCriteriaTypeId) {
	this.ratingCriteriaId = ratingCriteriaId;
	this.ratingCriteriaTypeId = ratingCriteriaTypeId;
    }

    public static RatingCriteria getRatingCriteriaInstance(int ratingCriteriaType) {
	// the default constructors don't set up the ratingCriteria type
	// so we need to do that manually
	// also default to a sensible category type
	RatingCriteria ratingCriteria = null;
	switch (ratingCriteriaType) {
	    case TOOL_ACTIVITY_CRITERIA_TYPE:
		ratingCriteria = new ToolActivityRatingCriteria();
		break;
	    case AUTHORED_ITEM_CRITERIA_TYPE:
		ratingCriteria = new AuthoredItemRatingCriteria();
		break;
	    case LEARNER_ITEM_CRITERIA_TYPE:
		ratingCriteria = new LearnerItemRatingCriteria();
		break;
	    case LESSON_CRITERIA_TYPE:
	    default:
		ratingCriteria = new LessonRatingCriteria();
		break;
	}
	ratingCriteria.setRatingCriteriaTypeId(new Integer(ratingCriteriaType));
	return ratingCriteria;
    }

    // ---------------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------------
    public Long getRatingCriteriaId() {
	return ratingCriteriaId;
    }

    public void setRatingCriteriaId(Long ratingCriteriaId) {
	this.ratingCriteriaId = ratingCriteriaId;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    public Integer getRatingCriteriaTypeId() {
	return ratingCriteriaTypeId;
    }

    public void setRatingCriteriaTypeId(Integer ratingCriteriaTypeId) {
	this.ratingCriteriaTypeId = ratingCriteriaTypeId;
    }

    public boolean isCommentsEnabled() {
	return commentsEnabled;
    }

    public void setCommentsEnabled(boolean commentsEnabled) {
	this.commentsEnabled = commentsEnabled;
    }

    public int getCommentsMinWordsLimit() {
	return commentsMinWordsLimit;
    }

    public void setCommentsMinWordsLimit(int commentsMinWordsLimit) {
	this.commentsMinWordsLimit = commentsMinWordsLimit;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("ratingCriteriaId", ratingCriteriaId).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (!(other instanceof RatingCriteria)) {
	    return false;
	}
	RatingCriteria castOther = (RatingCriteria) other;
	return new EqualsBuilder().append(this.getOrderId(), castOther.getOrderId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getOrderId()).toHashCode();
    }

    // ---------------------------------------------------------------------
    // Service Methods
    // ---------------------------------------------------------------------

    // ---------------------------------------------------------------------
    // RatingCriteria Type checking methods
    // ---------------------------------------------------------------------
    /**
     * Check up whether an ratingCriteria is tool ratingCriteria or not.
     *
     * @return is this ratingCriteria a tool ratingCriteria?
     */
    public boolean isToolActivityRatingCriteria() {
	return getRatingCriteriaTypeId().intValue() == RatingCriteria.TOOL_ACTIVITY_CRITERIA_TYPE;
    }

    /**
     * Check up whether an ratingCriteria is authored ratingCriteria or not.
     *
     * @return is this ratingCriteria authored?
     */
    public boolean isAuthoredItemRatingCriteria() {
	return getRatingCriteriaTypeId().intValue() == RatingCriteria.AUTHORED_ITEM_CRITERIA_TYPE;
    }

    public boolean isLearnerItemRatingCriteria() {
	return getRatingCriteriaTypeId().intValue() == RatingCriteria.LEARNER_ITEM_CRITERIA_TYPE;
    }

    public boolean isLessonRatingCriteria() {
	return getRatingCriteriaTypeId().intValue() == RatingCriteria.LESSON_CRITERIA_TYPE;
    }

    // ---------------------------------------------------------------------
    // Data Transfer object creation methods
    // ---------------------------------------------------------------------

    @Override
    public Object clone() {
	RatingCriteria criteria = null;
	try {
	    criteria = (RatingCriteria) super.clone();
	    criteria.setRatingCriteriaId(null);
	} catch (CloneNotSupportedException e) {
	    RatingCriteria.log.error("When clone " + RatingCriteria.class + " failed");
	}

	return criteria;
    }

    @Override
    public int compareTo(Object o) {

	if ((o != null) && o instanceof RatingCriteria) {
	    RatingCriteria anotherCriteria = (RatingCriteria) o;
	    return orderId - anotherCriteria.getOrderId();
	} else {
	    return 1;
	}
    }

}