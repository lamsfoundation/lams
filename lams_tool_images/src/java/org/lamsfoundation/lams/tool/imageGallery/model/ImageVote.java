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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * ImageVote
 *
 * @author Andrey Balan
 *
 *
 */
public class ImageVote implements Cloneable {

    private static final Logger log = Logger.getLogger(ImageVote.class);

    private Long uid;
    private boolean isVoted;
    private ImageGalleryUser createBy;
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

	return new EqualsBuilder().append(this.uid, genericEntity.uid).append(this.isVoted, genericEntity.isVoted)
		.append(this.createBy, genericEntity.createBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(isVoted).append(createBy).toHashCode();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    /**
     *
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     * @return
     */
    public ImageGalleryUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(ImageGalleryUser createBy) {
	this.createBy = createBy;
    }

    /**
     *
     * @return
     */
    public boolean isVoted() {
	return isVoted;
    }

    public void setVoted(boolean isVoted) {
	this.isVoted = isVoted;
    }

    /**
     *
     * @return
     */
    public ImageGalleryItem getImageGalleryItem() {
	return imageGalleryItem;
    }

    public void setImageGalleryItem(ImageGalleryItem item) {
	this.imageGalleryItem = item;
    }

}
