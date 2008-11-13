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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.imageGallery.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * ImageGallery
 * 
 * @hibernate.class table="tl_laimag10_imageGallery_item"
 * 
 * @author Andrey Balan
 * 
 */
public class ImageGalleryItem implements Cloneable {

    private static final Logger log = Logger.getLogger(ImageGalleryItem.class);

    private Long uid;

    private String title;

    private String description;

    private int sequenceId;

    private boolean isHide;
    private boolean isCreateByAuthor;

    private Date createDate;
    private ImageGalleryUser createBy;
    
    private Long originalFileUuid;
    
    private Long mediumFileUuid;
    
    private Long thumbnailFileUuid;

    private Long fileVersionId;

    private String fileName;

    private String fileType;
    
    //Set of user comments
    private Set comments;
    
    private float averageRating;
    
    private int numberRatings;

    // ***********************************************
    // DTO fields:
    // ***********************************************	
    private boolean complete;
    
    /**
     * Default contruction method.
     * 
     */
    public ImageGalleryItem() {
	comments = new HashSet();
    }    

    @Override
    public Object clone() {
	ImageGalleryItem image = null;
	try {
	    image = (ImageGalleryItem) super.clone();
	    image.setUid(null);
	    // clone ImageGalleryUser as well
	    if (this.createBy != null) {
		image.setCreateBy((ImageGalleryUser) this.createBy.clone());
	    }

	    // clone set of ImageComment
	    if (comments != null) {
		Iterator iter = comments.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    ImageComment comment = (ImageComment) iter.next();
		    ImageComment newComment = (ImageComment) comment.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newComment);
		}
		image.comments = set;
	    }	

	} catch (CloneNotSupportedException e) {
	    ImageGalleryItem.log.error("When clone " + ImageGalleryItem.class + " failed");
	}

	return image;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *                The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     * @hibernate.property column="title" length="255"
     * @return
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.property column="description" type="text"
     * @return
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Returns image sequence number.
     * 
     * @return image sequence number
     * 
     * @hibernate.property column="sequence_id"
     */
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets image sequence number.
     * 
     * @param sequenceId
     *                image sequence number
     */
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     * @hibernate.many-to-one cascade="none" column="create_by"
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
     * @hibernate.property column="create_by_author"
     * @return
     */
    public boolean isCreateByAuthor() {
	return isCreateByAuthor;
    }

    public void setCreateByAuthor(boolean isCreateByAuthor) {
	this.isCreateByAuthor = isCreateByAuthor;
    }

    /**
     * @hibernate.property column="is_hide"
     * @return
     */
    public boolean isHide() {
	return isHide;
    }

    public void setHide(boolean isHide) {
	this.isHide = isHide;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    public boolean isComplete() {
	return complete;
    }
    
    /**
     * @hibernate.property column="original_file_uuid"
     * 
     * @return
     */
    public Long getOriginalFileUuid() {
	return originalFileUuid;
    }

    public void setOriginalFileUuid(Long originalFileUuid) {
	this.originalFileUuid = originalFileUuid;
    }
    
    /**
     * @hibernate.property column="medium_file_uuid"
     * 
     * @return
     */
    public Long getMediumFileUuid() {
	return mediumFileUuid;
    }

    public void setMediumFileUuid(Long mediumFileUuid) {
	this.mediumFileUuid = mediumFileUuid;
    }

    /**
     * @hibernate.property column="thumbnail_file_uuid"
     * 
     * @return
     */
    public Long getThumbnailFileUuid() {
	return thumbnailFileUuid;
    }

    public void setThumbnailFileUuid(Long thumbnailFileUuid) {
	this.thumbnailFileUuid = thumbnailFileUuid;
    }

    /**
     * @hibernate.property column="file_version_id"
     * @return
     */
    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long crVersionId) {
	this.fileVersionId = crVersionId;
    }

    /**
     * @hibernate.property column="file_type"
     */
    public String getFileType() {
	return fileType;
    }

    public void setFileType(String type) {
	this.fileType = type;
    }

    /**
     * @hibernate.property column="file_name"
     */
    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	this.fileName = name;
    }

    /**
     * @hibernate.set lazy="true" cascade="all" inverse="false" order-by="create_date asc"
     * @hibernate.collection-key column="imageGallery_item_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.imageGallery.model.ImageComment"
     * 
     * @return a set of Comments to this Image.
     */
    public Set getComments() {
	return comments;
    }

    public void setComments(Set comments) {
	this.comments = comments;
    }
    
    /**
     * Returns image average rating.
     * 
     * @return image average rating
     * 
     * @hibernate.property column="average_rating"
     */
    public float getAverageRating() {
	return averageRating;
    }

    /**
     * Sets image average rating.
     * 
     * @param averageRating
     *                image average rating
     */
    public void setAverageRating(float averageRating) {
	this.averageRating = averageRating;
    }
    
    /**
     * Returns image sequence number.
     * 
     * @return image sequence number
     * 
     * @hibernate.property column="number_ratings"
     */
    public int getNumberRatings() {
	return numberRatings;
    }

    /**
     * Sets image number of rates.
     * 
     * @param numberRates
     *                image number of rates
     */
    public void setNumberRatings(int numberRatings) {
	this.numberRatings = numberRatings;
    }
}
