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

package org.lamsfoundation.lams.rating.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Nullable;

/**
 * Base class for all RatingCriterias. If you add another subclass, you must update
 * RatingCriteriaDAO.getRatingCriteriaByRatingCriteriaId() and add a ACTIVITY_TYPE constant.
 *
 * Criteria Styles:
 *
 * Comment: This supports the old style Peer Review comments, with every reviewer leaving one comment for each item/user
 * being reviewed for a set of (star) criteria.
 * Not support in the in Peer Review once it supports Ranking, Hedging, etc but kept for compatibility with existing
 * data.
 * Could be included in new style Peer Review with user interface changes.
 *
 * Star: The original style of comment, with up to 5 stars being used. For every star criteria, each reviewer leaves
 * one star rating (in lams_rating) for each item/user and possibly one comment (in lams_rating_comment) for each
 * item/user.
 *
 * Ranking: Ordering of the top 1, top 2, top 3, top 4, top 5 or all items/users. For every ranking criteria, each
 * reviewer leaves
 * one ranking (in lams_rating) for [ 1/2/3/4/5/all ] items/users and no comments.
 *
 * Hedging: Allocating out marks across items/users. For every hedging criteria, each reviewer leaves
 * one ranking (in lams_rating) for 1 or more items/users and 0 or 1 justification comment overall (in
 * lams_rating_comment).
 * That is, for Hedging a reviewer makes one justification comment, whereas in Star the reviewer makes one comment for
 * each user/item being reviewed.
 *
 */

@Entity
@Table(name = "lams_rating_criteria")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "rating_criteria_type_id", discriminatorType = DiscriminatorType.INTEGER)
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

    /**
     * What style to use for doing the rating? Stored in Int member
     * If Style = RATING_STYLE_COMMENT then the entry is for a comment criteria, with the data in RatingComment.
     * If Style = RATING_STYLE_STAR then maxRating is 5, which lines up with the number of stars and comments are
     * allowed.
     * If Style = RATING_STYLE_RANKING then maxRating will be 1 through 5 or -1 for rank all.
     * If Style = RATING_STYLE_HEDGING then maxRating contains the sum total mark to which the hedged ratings should add
     * up.
     * If Style = RATING_STYLE_RUBRICS then maxRating is the same as number of columns, starting with 1
     * The comments table is also used to store the rating justification for hedging.
     */
    public static final int RATING_STYLE_COMMENT = 0;
    public static final int RATING_STYLE_STAR = 1;
    public static final int RATING_STYLE_RANKING = 2;
    public static final int RATING_STYLE_HEDGING = 3;
    public static final int RATING_STYLE_RUBRICS = 4;

    // The star rating can never be higher than RATING_STYLE_STAR_DEFAULT_MAX - it is capped in RatingService.rateItem, RatingService.rateItems
    public static final int RATING_STYLE_STAR_DEFAULT_MAX = 5;
    public static final float RATING_STYLE_STAR_DEFAULT_MAX_AS_FLOAT = 5f;
    public static final int RATING_STYLE_RANKING_DEFAULT_MAX = 5;
    public static final int RATING_STYLE_RUBRICS_DEFAULT_MAX = 5;
    public static final int RATING_RANK_ALL = -1;

    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------

    @Id
    @Column(name = "rating_criteria_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingCriteriaId;

    /** Title of the ratingCriteria */
    @Column
    private String title;

    /**
     * Indicates the order in which the activities appear inside complex activities. Starts from 0, 1 and so on.
     */
    @Column(name = "order_id")
    private Integer orderId;

    /** The type of ratingCriteria */
    @Column(name = "rating_criteria_type_id", insertable = false, updatable = false)
    private Integer ratingCriteriaTypeId;

    @Column(name = "rating_criteia_group_id")
    private Integer ratingCriteriaGroupId; // ID shared between all criteria in the same group, for example all rows in the same rubrics criteria

    @Column(name = "comments_enabled")
    private boolean commentsEnabled; // comments for RATING_STYLE_COMMENT, RATING_STYLE_STAR justification for RATING_STYLE_HEDGING

    @Column(name = "comments_min_words_limit")
    private int commentsMinWordsLimit;

    @Column(name = "rating_style")
    private Integer ratingStyle; // see comments above for RATING_STYLE

    @Column(name = "max_rating")
    private Integer maxRating; // see comments above for RATING_STYLE

    @Column(name = "minimum_rates")
    private Integer minimumRates; // Minimum number of people for whom one user may rate this criteria. Used for RATING_STYLE_STAR.

    @Column(name = "maximum_rates")
    private Integer maximumRates; // Minimum number of people for whom one user may rate this criteria. Used for RATING_STYLE_STAR.

    // ---------------------------------------------------------------------
    // Object constructors
    // ---------------------------------------------------------------------

    /*
     * For the createDateTime fields, if the value is null, then it will default to the current time.
     */

    /** full constructor */
    public RatingCriteria(Long ratingCriteriaId, String title, Integer orderId, Date createDateTime,
	    Integer ratingCriteriaTypeId, Integer ratingStyle, Integer maxRating, Integer minimumRates,
	    Integer maximumRates) {
	this.ratingCriteriaId = ratingCriteriaId;
	this.title = title;
	this.orderId = orderId;
	this.ratingCriteriaTypeId = ratingCriteriaTypeId;
	this.ratingStyle = ratingStyle;
	this.maxRating = maxRating;
	this.minimumRates = minimumRates;
	this.maximumRates = maximumRates;
    }

    /** default constructor */
    public RatingCriteria() {
	this.ratingStyle = RATING_STYLE_STAR;
	this.maxRating = RATING_STYLE_STAR_DEFAULT_MAX;
	this.minimumRates = 0;
	this.maximumRates = 0;
    }

    /** minimal constructor */
    public RatingCriteria(Long ratingCriteriaId, Date createDateTime, RatingCriteria parentRatingCriteria,
	    Integer ratingCriteriaTypeId) {
	this.ratingCriteriaId = ratingCriteriaId;
	this.ratingCriteriaTypeId = ratingCriteriaTypeId;
	this.ratingStyle = RATING_STYLE_STAR;
	this.maxRating = RATING_STYLE_STAR_DEFAULT_MAX;
	this.minimumRates = 0;
	this.maximumRates = 0;
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

    // RatingStyle must be set before calling this method
    public static Integer getDefaultMaxRating(int ratingStyle) {
	switch (ratingStyle) {
	    case RatingCriteria.RATING_STYLE_STAR:
		return RatingCriteria.RATING_STYLE_STAR_DEFAULT_MAX;
	    case RatingCriteria.RATING_STYLE_RANKING:
		return RatingCriteria.RATING_STYLE_RANKING_DEFAULT_MAX;
	    case RatingCriteria.RATING_STYLE_HEDGING:
	    case RatingCriteria.RATING_STYLE_COMMENT:
		return 0;
	    case RatingCriteria.RATING_STYLE_RUBRICS:
		return RATING_STYLE_RUBRICS_DEFAULT_MAX;
	}
	return null;
    }

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
    // RatingStyle  checking methods
    // ---------------------------------------------------------------------
    /**
     * Is it a Comment?
     */
    public boolean isCommentRating() {
	return getRatingStyle().intValue() == RatingCriteria.RATING_STYLE_COMMENT;
    }

    /**
     * Is it a Star Rating?
     */
    public boolean isStarStyleRating() {
	return getRatingStyle().intValue() == RatingCriteria.RATING_STYLE_STAR;
    }

    /**
     * Is it a Ranking?
     */
    public boolean isRankingStyleRating() {
	return getRatingStyle().intValue() == RatingCriteria.RATING_STYLE_RANKING;
    }

    /**
     * Is it a Hedging Mark?
     */
    public boolean isHedgeStyleRating() {
	return getRatingStyle().intValue() == RatingCriteria.RATING_STYLE_HEDGING;
    }

    /**
     * Is it Rubrics?
     */
    public boolean isRubricsStyleRating() {
	return getRatingStyle().intValue() == RatingCriteria.RATING_STYLE_RUBRICS;
    }

    // ---------------------------------------------------------------------
    // Data Transfer object creation methods
    // ---------------------------------------------------------------------

    public Integer getRatingStyle() {
	return ratingStyle;
    }

    public void setRatingStyle(Integer ratingStyle) {
	this.ratingStyle = ratingStyle;
    }

    public Integer getMaxRating() {
	return maxRating;
    }

    public void setMaxRating(Integer maxRating) {
	this.maxRating = maxRating;
    }

    public Integer getMinimumRates() {
	return minimumRates;
    }

    public void setMinimumRates(Integer minimumRates) {
	this.minimumRates = minimumRates;
    }

    public Integer getMaximumRates() {
	return maximumRates;
    }

    public void setMaximumRates(Integer maximumRates) {
	this.maximumRates = maximumRates;
    }

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