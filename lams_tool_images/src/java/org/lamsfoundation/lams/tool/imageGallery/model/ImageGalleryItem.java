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

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * ImageGallery
 *
 *
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

    private String fileName;

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
    public void setUid(Long userID) {
	this.uid = userID;
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
     *
     */
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets image sequence number.
     *
     * @param sequenceId
     *            image sequence number
     */
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     *
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
    public boolean isHide() {
	return isHide;
    }

    public void setHide(boolean isHide) {
	this.isHide = isHide;
    }

    /**
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
    
    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", uid).append(" title", title).toString();
    }
}
