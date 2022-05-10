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

package org.lamsfoundation.lams.tool.imageGallery.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;

/**
 * ImageGallery
 */
@Entity
@Table(name = "tl_laimag10_imagegallery")
public class ImageGallery implements Serializable, Cloneable {
    private static final Logger log = Logger.getLogger(ImageGallery.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "content_id")
    private Long contentId;

    @Column
    private String title;

    @Column
    private String instructions;

    @Column(name = "next_image_title")
    private Long nextImageTitle;

    // advance

    @Column(name = "allow_vote")
    private boolean allowVote;

    @Column(name = "allow_share_images")
    private boolean allowShareImages;

    @Column(name = "lock_on_finished")
    private boolean lockWhenFinished;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "allow_rank")
    private boolean allowRank;

    @Column(name = "maximum_rates")
    private int maximumRates;

    @Column(name = "minimum_rates")
    private int minimumRates;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("order_id ASC")
    @JoinColumn(name = "tool_content_id", referencedColumnName = "content_id")
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<LearnerItemRatingCriteria> ratingCriterias = new HashSet<>();

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column(name = "image_submit_notify")
    private boolean notifyTeachersOnImageSumbit;

    // general information

    @Column(name = "create_date")
    private Date created;

    @Column(name = "update_date")
    private Date updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "create_by")
    private ImageGalleryUser createdBy;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("create_date DESC")
    @JoinColumn(name = "imageGallery_uid")
    private Set<ImageGalleryItem> imageGalleryItems = new HashSet<>();

    /**
     * Default contructor
     */
    public ImageGallery() {
	nextImageTitle = 1L;
    }

    // **********************************************************
    // Function method for ImageGallery
    // **********************************************************
    public static ImageGallery newInstance(ImageGallery defaultContent, Long contentId) {
	ImageGallery toContent = new ImageGallery();
	toContent = (ImageGallery) defaultContent.clone();
	toContent.setContentId(contentId);

	// reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setImageGallery(toContent);
	    Set<ImageGalleryItem> items = toContent.getImageGalleryItems();
	    for (ImageGalleryItem item : items) {
		item.setCreateBy(toContent.getCreatedBy());
	    }
	}

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

	ImageGallery imageGallery = null;
	try {
	    imageGallery = (ImageGallery) super.clone();
	    imageGallery.setUid(null);

	    // clone imageGalleryItems as well
	    if (imageGalleryItems != null) {
		Set<ImageGalleryItem> newItems = new HashSet<>();
		for (ImageGalleryItem item : imageGalleryItems) {
		    ImageGalleryItem newItem = (ImageGalleryItem) item.clone();
		    // just clone old file without duplicate it in repository
		    newItems.add(newItem);
		}
		imageGallery.imageGalleryItems = newItems;
	    }

	    // clone ImageGalleryUser as well
	    if (createdBy != null) {
		imageGallery.setCreatedBy((ImageGalleryUser) createdBy.clone());
	    }

	    // clone ratingCriterias as well
	    if (ratingCriterias != null) {
		Set<LearnerItemRatingCriteria> newCriterias = new HashSet<>();
		for (LearnerItemRatingCriteria criteria : ratingCriterias) {
		    LearnerItemRatingCriteria newCriteria = (LearnerItemRatingCriteria) criteria.clone();
		    // just clone old file without duplicate it in repository
		    newCriterias.add(newCriteria);
		}
		imageGallery.ratingCriterias = newCriterias;
	    }

	} catch (CloneNotSupportedException e) {
	    ImageGallery.log.error("When clone " + ImageGallery.class + " failed");
	}

	return imageGallery;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof ImageGallery)) {
	    return false;
	}

	final ImageGallery genericEntity = (ImageGallery) o;

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
     * @return Returns the userid of the user who created the Share imageGallery.
     *
     */
    public ImageGalleryUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Share imageGallery.
     */
    public void setCreatedBy(ImageGalleryUser createdBy) {
	this.createdBy = createdBy;
    }

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
     *            Set to true to lock the imageGallery for finished users.
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
     * @return Returns the next condecutive integer for constructing image title.
     */
    public Long getNextImageTitle() {
	return nextImageTitle;
    }

    public void setNextImageTitle(Long nextImageTitle) {
	this.nextImageTitle = nextImageTitle;
    }

    public Set<ImageGalleryItem> getImageGalleryItems() {
	return imageGalleryItems;
    }

    public void setImageGalleryItems(Set<ImageGalleryItem> imageGalleryItems) {
	this.imageGalleryItems = imageGalleryItems;
    }

    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    public boolean isAllowShareImages() {
	return allowShareImages;
    }

    public void setAllowShareImages(boolean allowShareImages) {
	this.allowShareImages = allowShareImages;
    }

    public boolean isAllowVote() {
	return allowVote;
    }

    public void setAllowVote(boolean allowVote) {
	this.allowVote = allowVote;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public boolean isAllowRank() {
	return allowRank;
    }

    public void setAllowRank(boolean allowRank) {
	this.allowRank = allowRank;
    }

    public int getMaximumRates() {
	return maximumRates;
    }

    public void setMaximumRates(int maximumRate) {
	this.maximumRates = maximumRate;
    }

    public int getMinimumRates() {
	return minimumRates;
    }

    public void setMinimumRates(int minimumRates) {
	this.minimumRates = minimumRates;
    }

    public Set<LearnerItemRatingCriteria> getRatingCriterias() {
	return ratingCriterias;
    }

    public void setRatingCriterias(Set<LearnerItemRatingCriteria> ratingCriterias) {
	this.ratingCriterias = ratingCriterias;
    }

    public boolean isNotifyTeachersOnImageSumbit() {
	return notifyTeachersOnImageSumbit;
    }

    public void setNotifyTeachersOnImageSumbit(boolean notifyTeachersOnImageSumbit) {
	this.notifyTeachersOnImageSumbit = notifyTeachersOnImageSumbit;
    }
}
