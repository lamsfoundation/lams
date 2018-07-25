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


package org.lamsfoundation.lams.tool.kaltura.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.kaltura.service.KalturaService;

/**
 *
 */

public class Kaltura implements java.io.Serializable, Cloneable {

    public static final int TYPE_LEARNER_CONTRIBUTION_LIMIT_UNLIMITED = -1;

    private static final long serialVersionUID = 579733009969321015L;

    static Logger log = Logger.getLogger(KalturaService.class.getName());

    // Fields
    private Long uid;

    private Date createDate;

    private Date updateDate;

    private KalturaUser createdBy;

    private String title;

    private String instructions;

    private boolean lockOnFinished;

    private boolean allowContributeVideos;

    private boolean allowSeeingOtherUsersRecordings;

    private int learnerContributionLimit;

    private boolean allowComments;

    private boolean allowRatings;

    private boolean contentInUse;

    private boolean defineLater;

    private Date submissionDeadline;

    private Long toolContentId;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private Set kalturaSessions;

    private Set kalturaItems;

    // Constructors

    /** default constructor */
    public Kaltura() {
	kalturaItems = new HashSet();
    }

    // Property accessors
    /**
     *
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     *
     *
     */
    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     *
     *
     */
    public KalturaUser getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(KalturaUser createdBy) {
	this.createdBy = createdBy;
    }

    /**
     *
     *
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     *
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     *
     */
    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    /**
     *
     */
    public boolean isAllowContributeVideos() {
	return allowContributeVideos;
    }

    public void setAllowContributeVideos(boolean allowContributeVideos) {
	this.allowContributeVideos = allowContributeVideos;
    }

    /**
     *
     */
    public boolean isAllowSeeingOtherUsersRecordings() {
	return allowSeeingOtherUsersRecordings;
    }

    public void setAllowSeeingOtherUsersRecordings(boolean allowSeeingOtherUsersRecordings) {
	this.allowSeeingOtherUsersRecordings = allowSeeingOtherUsersRecordings;
    }

    /**
     *
     * @return learnerContributionLimit can be from 1 to 10 or -1 in case of unlimited
     */
    public int getLearnerContributionLimit() {
	return learnerContributionLimit;
    }

    public void setLearnerContributionLimit(int learnerContributionLimit) {
	this.learnerContributionLimit = learnerContributionLimit;
    }

    /**
     *
     */
    public boolean isAllowComments() {
	return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
	this.allowComments = allowComments;
    }

    /**
     *
     */
    public boolean isAllowRatings() {
	return allowRatings;
    }

    public void setAllowRatings(boolean allowRatings) {
	this.allowRatings = allowRatings;
    }

    /**
     *
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     *
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    /**
     *
     *
     * @return date submissionDeadline
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    /**
     *
     */
    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     *
     * @return
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
     * @return
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     *
     *
     *
     */
    public Set getKalturaSessions() {
	return kalturaSessions;
    }

    public void setKalturaSessions(Set kalturaSessions) {
	this.kalturaSessions = kalturaSessions;
    }

    /**
     *
     *
     *
     */
    public Set getKalturaItems() {
	return kalturaItems;
    }

    public void setKalturaItems(Set kalturaItems) {
	this.kalturaItems = kalturaItems;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("title").append("='").append(getTitle()).append("' ");
	buffer.append("instructions").append("='").append(getInstructions()).append("' ");
	buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (other == null) {
	    return false;
	}
	if (!(other instanceof Kaltura)) {
	    return false;
	}
	Kaltura castOther = (Kaltura) other;

	return this.getUid() == castOther.getUid()
		|| this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid());
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Kaltura newInstance(Kaltura fromContent, Long toContentId) {
	Kaltura toContent = new Kaltura();
	toContent = (Kaltura) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());

	//reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setKaltura(toContent);
	    Set<KalturaItem> items = toContent.getKalturaItems();
	    for (KalturaItem item : items) {
		item.setCreatedBy(toContent.getCreatedBy());
	    }
	}

	return toContent;
    }

    @Override
    protected Object clone() {

	Kaltura kaltura = null;
	try {
	    kaltura = (Kaltura) super.clone();
	    kaltura.setUid(null);

	    if (kalturaItems != null) {
		Iterator iter = kalturaItems.iterator();
		Set<KalturaItem> set = new HashSet<KalturaItem>();
		while (iter.hasNext()) {
		    KalturaItem item = (KalturaItem) iter.next();
		    KalturaItem newItem = (KalturaItem) item.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newItem);
		}
		kaltura.kalturaItems = set;
	    }

	    // clone KalturaUser as well
	    if (createdBy != null) {
		kaltura.setCreatedBy((KalturaUser) createdBy.clone());
	    }

	    // create an empty set for the kalturaSession
	    kaltura.kalturaSessions = new HashSet();

	} catch (CloneNotSupportedException cnse) {
	    Kaltura.log.error("Error cloning " + Kaltura.class);
	}
	return kaltura;
    }

}
