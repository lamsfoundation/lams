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
 
/* $Id$ */  
package org.lamsfoundation.lams.tool.imageGallery.model;  

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
 
/**
 * ImageRating
 * 
 * @author Andrey Balan
 * 
 * @hibernate.class table="tl_laimag10_image_rating"
 */
public class ImageRating implements Cloneable {

    private static final Logger log = Logger.getLogger(ImageRating.class);

    private Long uid;
    private int rating;
    private ImageGalleryUser createBy;
    private ImageGalleryItem imageGalleryItem;

    // **********************************************************
    // Function method for ImageRating
    // **********************************************************

    public Object clone() {
	ImageRating imageComment = null;
	try {
	    imageComment = (ImageRating) super.clone();
	    ((ImageRating) imageComment).setUid(null);

	    // clone ImageGalleryUser as well
	    if (this.createBy != null) {
		imageComment.setCreateBy((ImageGalleryUser) this.createBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ImageRating.class + " failed");
	}

	return imageComment;
    }

    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (!(o instanceof ImageRating))
	    return false;

	final ImageRating genericEntity = (ImageRating) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid).append(this.rating, genericEntity.rating)
		.append(this.createBy, genericEntity.createBy).isEquals();
    }

    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(rating).append(createBy).toHashCode();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.many-to-one column="create_by" cascade="none"
     * @return
     */
    public ImageGalleryUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(ImageGalleryUser createBy) {
	this.createBy = createBy;
    }

    /**
     * @hibernate.property column="rating"
     * @return
     */
    public int getRating() {
	return rating;
    }

    public void setRating(int rating) {
	this.rating = rating;
    }
    
    /**
     * @hibernate.many-to-one column="imageGallery_item_uid" cascade="none"
     * @return
     */
    public ImageGalleryItem getImageGalleryItem() {
	return imageGalleryItem;
    }

    public void setImageGalleryItem(ImageGalleryItem item) {
	this.imageGalleryItem = item;
    }    
    
}

 