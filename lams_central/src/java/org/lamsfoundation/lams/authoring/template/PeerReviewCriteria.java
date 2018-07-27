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

package org.lamsfoundation.lams.authoring.template;

import org.lamsfoundation.lams.rating.model.RatingCriteria;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/** Simple survey question used for parsing survey data before conversion to JSON */
public class PeerReviewCriteria {

//    Defined in lams_common RatingCriteria
//    public static final int TOOL_ACTIVITY_CRITERIA_TYPE = 1;
//    public static final int AUTHORED_ITEM_CRITERIA_TYPE = 2;
//    public static final int LEARNER_ITEM_CRITERIA_TYPE = 3;
//    public static final int LESSON_CRITERIA_TYPE = 4;
//    public static final int RATING_STYLE_COMMENT = 0;
//    public static final int RATING_STYLE_STAR = 1;
//    public static final int RATING_STYLE_RANKING = 2;
//    public static final int RATING_STYLE_HEDGING = 3;
//    public static final int RATING_STYLE_STAR_DEFAULT_MAX = 5;
//    public static final int RATING_STYLE_RANKING_DEFAULT_MAX = 5;
//    public static final int RATING_RANK_ALL = -1;

    private String title;
    private Integer orderId;
//    private Integer ratingCriteriaTypeId;
    private Boolean commentsEnabled; // comments for RATING_STYLE_COMMENT, RATING_STYLE_STAR justification for RATING_STYLE_HEDGING
    private Integer commentsMinWordsLimit;
//    private Integer ratingStyle; // see comments above for RATING_STYLE
//    private Integer maxRating; // see comments above for RATING_STYLE
//    private Integer minimumRates; // Minimum number of people for whom one user may rate this criteria. Used for RATING_STYLE_STAR.
//    private Integer maximumRates; // Minimum number of people for whom one user may rate this criteria. Used for RATING_STYLE_STAR.

    public PeerReviewCriteria(Integer orderId) {
	this.orderId = orderId;
    }

    public String getTitle() {
	return title;
    }

    public Integer getOrderId() {
	return orderId;
    }

    public Boolean getCommentsEnabled() {
	return commentsEnabled;
    }

    public Integer getCommentsMinWordsLimit() {
	return commentsMinWordsLimit;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    public void setCommentsEnabled(Boolean commentsEnabled) {
	this.commentsEnabled = commentsEnabled;
    }

    public void setCommentsMinWordsLimit(Integer commentsMinWordsLimit) {
	this.commentsMinWordsLimit = commentsMinWordsLimit;
    }

    public ObjectNode getAsObjectNode() {
	ObjectNode obj = JsonNodeFactory.instance.objectNode().put("title", title).put("orderId", orderId)
		.put("ratingStyle", RatingCriteria.RATING_STYLE_STAR)
		.put("commentsEnabled", commentsEnabled != null ? commentsEnabled : false);
	if (commentsEnabled != null && commentsEnabled) {
	    obj.put("minWordsInComment", commentsMinWordsLimit != null ? commentsMinWordsLimit : 1);
	}
	return obj;
    }

}
