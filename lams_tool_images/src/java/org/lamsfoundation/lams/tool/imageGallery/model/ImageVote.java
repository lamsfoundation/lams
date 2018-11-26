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

package org.lamsfoundation.lams.tool.imageGallery.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * ImageVote
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laimag10_image_vote")
public class ImageVote implements Cloneable {
    private static final Logger log = Logger.getLogger(ImageVote.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "is_voted")
    private boolean voted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private ImageGalleryUser createBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageGallery_item_uid")
    private ImageGalleryItem imageGalleryItem;

    // **********************************************************
    // Function method for ImageRating
    // **********************************************************

    @Override
    public Object clone() {
	ImageVote imageComment = null;
	try {
	    imageComment = (ImageVote) super.clone();
	    imageComment.setUid(null);

	    // clone ImageGalleryUser as well
	    if (this.createBy != null) {
		imageComment.setCreateBy((ImageGalleryUser) this.createBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ImageVote.class + " failed");
	}

	return imageComment;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof ImageVote)) {
	    return false;
	}

	final ImageVote genericEntity = (ImageVote) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid).append(this.voted, genericEntity.voted)
		.append(this.createBy, genericEntity.createBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(voted).append(createBy).toHashCode();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public ImageGalleryUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(ImageGalleryUser createBy) {
	this.createBy = createBy;
    }

    public boolean isVoted() {
	return voted;
    }

    public void setVoted(boolean voted) {
	this.voted = voted;
    }

    public ImageGalleryItem getImageGalleryItem() {
	return imageGalleryItem;
    }

    public void setImageGalleryItem(ImageGalleryItem item) {
	this.imageGalleryItem = item;
    }

}
