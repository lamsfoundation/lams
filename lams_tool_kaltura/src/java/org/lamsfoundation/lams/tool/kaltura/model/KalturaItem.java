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


package org.lamsfoundation.lams.tool.kaltura.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.kaltura.dto.AverageRatingDTO;

/**
 * KalturaItem
 *
 *
 *
 * @author Andrey Balan
 *
 */
public class KalturaItem implements Cloneable {

    private static final Logger log = Logger.getLogger(KalturaItem.class);

    private Long uid;

    private String title;

    private String entryId;

    private int duration;

    private int sequenceId;

    private boolean isHidden;

    private boolean isCreateByAuthor;

    private Date createDate;

    private KalturaUser createdBy;

    private Long kalturaUid;

    private Long mark;

    //Set of user comments
    private Set comments;

    // ***********************************************
    // Non persistant fields:

    private AverageRatingDTO averageRatingDto;

    private Set<KalturaComment> groupComments;

    /**
     * Default contruction method.
     *
     */
    public KalturaItem() {
	comments = new HashSet();
	groupComments = new HashSet<KalturaComment>();
    }

    @Override
    public Object clone() {
	KalturaItem item = null;
	try {
	    item = (KalturaItem) super.clone();
	    item.setUid(null);
	    // clone ImageGalleryUser as well
	    if (this.createdBy != null) {
		item.setCreatedBy((KalturaUser) this.createdBy.clone());
	    }

	    // clone set of ImageComment
	    if (comments != null) {
		Iterator iter = comments.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    KalturaComment comment = (KalturaComment) iter.next();
		    KalturaComment newComment = (KalturaComment) comment.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newComment);
		}
		item.comments = set;
	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + KalturaItem.class + " failed");
	}

	return item;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof KalturaItem)) {
	    return false;
	}

	final KalturaItem genericEntity = (KalturaItem) o;

	return new EqualsBuilder().append(this.getUid(), genericEntity.getUid())
		.append(this.getSequenceId(), genericEntity.getSequenceId()).isEquals();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     *
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     * @return duration of the corresponding Kaltura video
     */
    public int getDuration() {
	return duration;
    }

    /**
     * @param duration
     *            duration of the Kaltura video
     */
    public void setDuration(int duration) {
	this.duration = duration;
    }

    public Date getDurationDate() {
	return new Date(duration * 1000);
    }

    /**
     *
     * @return
     */
    public String getEntryId() {
	return entryId;
    }

    public void setEntryId(String entryId) {
	this.entryId = entryId;
    }

    /**
     * Returns item sequence number.
     *
     * @return item sequence number
     *
     *
     */
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets item sequence number.
     *
     * @param sequenceId
     *            item sequence number
     */
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     *
     *
     * @return
     */
    public KalturaUser getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(KalturaUser createdBy) {
	this.createdBy = createdBy;
    }

    /**
     *
     * @return
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     *
     * @return
     */
    public boolean isCreateByAuthor() {
	return isCreateByAuthor;
    }

    public void setCreateByAuthor(boolean isCreateByAuthor) {
	this.isCreateByAuthor = isCreateByAuthor;
    }

    /**
     *
     * @return
     */
    public Long getKalturaUid() {
	return kalturaUid;
    }

    public void setKalturaUid(Long kalturaUid) {
	this.kalturaUid = kalturaUid;
    }

    /**
     *
     * @return
     */
    public Long getMark() {
	return mark;
    }

    public void setMark(Long mark) {
	this.mark = mark;
    }

    /**
     *
     * @return
     */
    public boolean isHidden() {
	return isHidden;
    }

    public void setHidden(boolean isHidden) {
	this.isHidden = isHidden;
    }

    /**
     *
     *
     *
     *
     * @return a set of Comments to this Image.
     */
    public Set getComments() {
	return comments;
    }

    public void setComments(Set comments) {
	this.comments = comments;
    }

    public AverageRatingDTO getAverageRatingDto() {
	return averageRatingDto;
    }

    public void setAverageRatingDto(AverageRatingDTO averageRatingDto) {
	this.averageRatingDto = averageRatingDto;
    }

    public Set<KalturaComment> getGroupComments() {
	return groupComments;
    }

    public void setGroupComments(Set<KalturaComment> groupComments) {
	this.groupComments = groupComments;
    }
}
