/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.peerreview.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;

/**
 * Peerreview
 *
 * @author Dapeng Ni
 */
public class Peerreview implements Cloneable {

    private static final Logger log = Logger.getLogger(Peerreview.class);

    // key
    private Long uid;

    // tool contentID
    private Long contentId;

    private String title;

    private String instructions;

    private int maximumRates;

    private int minimumRates;

    private Set<LearnerItemRatingCriteria> ratingCriterias;

    // advance

    private boolean lockWhenFinished;

    private boolean defineLater;

    private boolean contentInUse;

    // general infomation
    private Date created;

    private Date updated;

    private PeerreviewUser createdBy;

    private int maximumRatesPerUser;

    private boolean showRatingsLeftForUser;
    
    private boolean showRatingsLeftByUser;

    private boolean reflectOnActivity;

    private String reflectInstructions;
    
    private boolean selfReview;

    private boolean notifyUsersOfResults;
    
    // **********************************************************
    // Function method for Peerreview
    // **********************************************************
    public static Peerreview newInstance(Peerreview defaultContent, Long contentId) {
	Peerreview toContent = new Peerreview();
	toContent = (Peerreview) defaultContent.clone();
	toContent.setContentId(contentId);

	// reset contentId
	if (toContent.getRatingCriterias() != null) {
	    Set<LearnerItemRatingCriteria> criterias = toContent.getRatingCriterias();
	    for (LearnerItemRatingCriteria criteria : criterias) {
		criteria.setToolContentId(contentId);
	    }
	}

	return toContent;
    }

    @Override
    public Object clone() {

	Peerreview peerreview = null;
	try {
	    peerreview = (Peerreview) super.clone();
	    peerreview.setUid(null);

	    // clone ReourceUser as well
	    if (createdBy != null) {
		peerreview.setCreatedBy((PeerreviewUser) createdBy.clone());
	    }

	    // clone ratingCriterias as well
	    if (ratingCriterias != null) {
		Set<LearnerItemRatingCriteria> newCriterias = new HashSet<LearnerItemRatingCriteria>();
		for (LearnerItemRatingCriteria criteria : ratingCriterias) {
		    LearnerItemRatingCriteria newCriteria = (LearnerItemRatingCriteria) criteria.clone();
		    // just clone old file without duplicate it in repository
		    newCriterias.add(newCriteria);
		}
		peerreview.ratingCriterias = newCriterias;
	    }

	} catch (CloneNotSupportedException e) {
	    Peerreview.log.error("When clone " + Peerreview.class + " failed");
	}

	return peerreview;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Peerreview)) {
	    return false;
	}

	final Peerreview genericEntity = (Peerreview) o;

	return new EqualsBuilder().append(uid, genericEntity.uid).append(title, genericEntity.title)
		.append(instructions, genericEntity.instructions).append(created, genericEntity.created)
		.append(updated, genericEntity.updated).append(createdBy, genericEntity.createdBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(created).append(updated)
		.append(createdBy).toHashCode();
    }

    /**
     * Updates the modification data for this entity.
     */
    public void updateModificationData() {

	long now = System.currentTimeMillis();
	if (created == null) {
	    this.setCreated(new Date(now));
	}
	this.setUpdated(new Date(now));
    }

    // **********************************************************
    // get/set methods
    // **********************************************************
    /**
     * Returns the object's creation date
     *
     * @return date
     */
    public Date getCreated() {
	return created;
    }

    /**
     * Sets the object's creation date
     *
     * @param created
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * Returns the object's date of last update
     *
     * @return date updated
     */
    public Date getUpdated() {
	return updated;
    }

    /**
     * Sets the object's date of last update
     *
     * @param updated
     */
    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    /**
     * @return Returns the userid of the user who created the Share peerreview.
     *
     */
    public PeerreviewUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Share peerreview.
     */
    public void setCreatedBy(PeerreviewUser createdBy) {
	this.createdBy = createdBy;
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
     * @return Returns the title.
     *
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return Returns the lockWhenFinish.
     *
     */
    public boolean getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished
     *            Set to true to lock the peerreview for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the instructions set by the teacher.
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @return
     */
    public int getMaximumRates() {
	return maximumRates;
    }

    public void setMaximumRates(int maximumRate) {
	this.maximumRates = maximumRate;
    }

    /**
     * @return
     */
    public int getMinimumRates() {
	return minimumRates;
    }

    public void setMinimumRates(int minimumRates) {
	this.minimumRates = minimumRates;
    }

    /**
     *
     * @return
     */
    public Set<LearnerItemRatingCriteria> getRatingCriterias() {
	return ratingCriterias;
    }

    public void setRatingCriterias(Set<LearnerItemRatingCriteria> ratingCriterias) {
	this.ratingCriterias = ratingCriterias;
    }

    /**
     * @return
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * @return
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @return
     */
    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    /**
     * @return
     */
    public int getMaximumRatesPerUser() {
	return maximumRatesPerUser;
    }

    public void setMaximumRatesPerUser(int maximumRatesPerUser) {
	this.maximumRatesPerUser = maximumRatesPerUser;
    }

    /**
     * @return
     */
    public boolean isShowRatingsLeftForUser() {
	return showRatingsLeftForUser;
    }

    public void setShowRatingsLeftForUser(boolean showRatingsLeftForUser) {
	this.showRatingsLeftForUser = showRatingsLeftForUser;
    }

    /**
     * @return
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     * @return
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public boolean isSelfReview() {
        return selfReview;
    }

    public void setSelfReview(boolean selfReview) {
        this.selfReview = selfReview;
    }
    
    public boolean isNotifyUsersOfResults() {
        return notifyUsersOfResults;
    }

    public void setNotifyUsersOfResults(boolean notifyUsersOfResults) {
        this.notifyUsersOfResults = notifyUsersOfResults;
    }

    public boolean isShowRatingsLeftByUser() {
        return showRatingsLeftByUser;
    }

    public void setShowRatingsLeftByUser(boolean showRatingsLeftByUser) {
        this.showRatingsLeftByUser = showRatingsLeftByUser;
    }
}
