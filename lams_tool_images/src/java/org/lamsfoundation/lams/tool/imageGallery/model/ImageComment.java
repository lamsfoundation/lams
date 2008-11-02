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

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * ImageComment
 * 
 * @author Andrey Balan
 * 
 * @hibernate.class table="tl_laimag10_image_comment"
 */
public class ImageComment implements Cloneable {

    private static final Logger log = Logger.getLogger(ImageComment.class);

    private Long uid;
    private String comment;
    private ImageGalleryUser createBy;
    private Date createDate;

    // **********************************************************
    // Function method for TaskListItemComment
    // **********************************************************

    public Object clone() {
	ImageComment imageComment = null;
	try {
	    imageComment = (ImageComment) super.clone();
	    ((ImageComment) imageComment).setUid(null);

	    // clone ImageGalleryUser as well
	    if (this.createBy != null) {
		imageComment.setCreateBy((ImageGalleryUser) this.createBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ImageComment.class + " failed");
	}

	return imageComment;
    }

    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (!(o instanceof ImageComment))
	    return false;

	final ImageComment genericEntity = (ImageComment) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid).append(this.comment, genericEntity.comment)
		.append(this.createBy, genericEntity.createBy).isEquals();
    }

    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(comment).append(createBy).toHashCode();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    /**
     * @hibernate.property column="create_date"
     * @return
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

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
     * @hibernate.property column="comment" type="text"
     * @return
     */
    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }
}
