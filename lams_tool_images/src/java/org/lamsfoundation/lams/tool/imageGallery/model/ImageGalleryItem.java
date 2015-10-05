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
 * @hibernate.class table="tl_laimag10_imagegallery_item"
 * 
 * @author Andrey Balan
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
    
    private int originalImageWidth;
    
    private int originalImageHeight;
    
    private Long mediumFileUuid;
    
    private int mediumImageWidth;
    
    private int mediumImageHeight;
    
    private Long thumbnailFileUuid;

    private Long fileVersionId;

    private String fileName;

    private String fileType;

    // *************** NON Persist Fields  ********************
    private String attachmentLocalUrl;

    private ImageGalleryAttachment originalFile;

    private ImageGalleryAttachment mediumFile;

    private ImageGalleryAttachment thumbnailFile;
    
    private String titleEscaped;

    private String descriptionEscaped;

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
     * @hibernate.property column="original_image_width"
     * 
     * @return
     */
    public int getOriginalImageWidth() {
	return originalImageWidth;
    }

    public void setOriginalImageWidth(int originalImageWidth) {
	this.originalImageWidth = originalImageWidth;
    }
    
    /**
     * @hibernate.property column="original_image_height"
     * 
     * @return
     */
    public int getOriginalImageHeight() {
	return originalImageHeight;
    }

    public void setOriginalImageHeight(int originalImageHeight) {
	this.originalImageHeight = originalImageHeight;
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
     * @hibernate.property column="medium_image_width"
     * 
     * @return
     */
    public int getMediumImageWidth() {
	return mediumImageWidth;
    }

    public void setMediumImageWidth(int mediumImageWidth) {
	this.mediumImageWidth = mediumImageWidth;
    }
    
    /**
     * @hibernate.property column="medium_image_height"
     * 
     * @return
     */
    public int getMediumImageHeight() {
	return mediumImageHeight;
    }

    public void setMediumImageHeight(int mediumImageHeight) {
	this.mediumImageHeight = mediumImageHeight;
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
    
    // *************** NON Persist Fields ********************
    
    public ImageGalleryAttachment getOriginalFile() {
	return originalFile;
    }

    public void setOriginalFile(ImageGalleryAttachment originalFile) {
	this.originalFile = originalFile;
    }
    
    public ImageGalleryAttachment getMediumFile() {
	return mediumFile;
    }

    public void setMediumFile(ImageGalleryAttachment mediumFile) {
	this.mediumFile = mediumFile;
    }

    public ImageGalleryAttachment getThumbnailFile() {
	return thumbnailFile;
    }

    public void setThumbnailFile(ImageGalleryAttachment thumbnailFile) {
	this.thumbnailFile = thumbnailFile;
    }
    
    public String getAttachmentLocalUrl() {
        return attachmentLocalUrl;
    }
    public void setAttachmentLocalUrl(String attachmentLocalUrl) {
        this.attachmentLocalUrl = attachmentLocalUrl;
    }
    
    public String getTitleEscaped() {
	return titleEscaped;
    }

    public void setTitleEscaped(String titleEscaped) {
	this.titleEscaped = titleEscaped;
    }

    public String getDescriptionEscaped() {
	return descriptionEscaped;
    }

    public void setDescriptionEscaped(String descriptionEscaped) {
	this.descriptionEscaped = descriptionEscaped;
    }
}
