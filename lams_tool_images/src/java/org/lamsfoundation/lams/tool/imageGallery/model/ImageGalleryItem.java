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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.imageGallery.dto.ImageGalleryAttachment;

/**
 * ImageGallery item
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laimag10_imagegallery_item")
public class ImageGalleryItem implements Cloneable {
    private static final Logger log = Logger.getLogger(ImageGalleryItem.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "sequence_id")
    private int sequenceId;

    @Column(name = "is_hide")
    private boolean isHide;

    @Column(name = "create_by_author")
    private boolean isCreateByAuthor;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private ImageGalleryUser createBy;

    @Column(name = "original_file_uuid")
    private Long originalFileUuid;

    @Column(name = "original_image_width")
    private int originalImageWidth;

    @Column(name = "original_image_height")
    private int originalImageHeight;

    @Column(name = "medium_file_uuid")
    private Long mediumFileUuid;

    @Column(name = "medium_image_width")
    private int mediumImageWidth;

    @Column(name = "medium_image_height")
    private int mediumImageHeight;

    @Column(name = "thumbnail_file_uuid")
    private Long thumbnailFileUuid;

    @Column(name = "file_name")
    private String fileName;

    // *************** NON Persist Fields  ********************
    @Transient
    private String attachmentLocalUrl;
    @Transient
    private ImageGalleryAttachment originalFile;
    @Transient
    private ImageGalleryAttachment mediumFile;
    @Transient
    private ImageGalleryAttachment thumbnailFile;
    @Transient
    private String originalFileDisplayUuid;
    @Transient
    private String mediumFileDisplayUuid;
    @Transient
    private String thumbnailFileDisplayUuid;

    @Transient
    private String titleEscaped;
    @Transient
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

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", uid).append(" title", title).toString();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    public Long getUid() {
	return uid;
    }

    public void setUid(Long userID) {
	this.uid = userID;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

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

    public ImageGalleryUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(ImageGalleryUser createBy) {
	this.createBy = createBy;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public boolean isCreateByAuthor() {
	return isCreateByAuthor;
    }

    public void setCreateByAuthor(boolean isCreateByAuthor) {
	this.isCreateByAuthor = isCreateByAuthor;
    }

    public boolean isHide() {
	return isHide;
    }

    public void setHide(boolean isHide) {
	this.isHide = isHide;
    }

    public Long getOriginalFileUuid() {
	return originalFileUuid;
    }

    public void setOriginalFileUuid(Long originalFileUuid) {
	this.originalFileUuid = originalFileUuid;
    }

    public int getOriginalImageWidth() {
	return originalImageWidth;
    }

    public void setOriginalImageWidth(int originalImageWidth) {
	this.originalImageWidth = originalImageWidth;
    }

    public int getOriginalImageHeight() {
	return originalImageHeight;
    }

    public void setOriginalImageHeight(int originalImageHeight) {
	this.originalImageHeight = originalImageHeight;
    }

    public Long getMediumFileUuid() {
	return mediumFileUuid;
    }

    public void setMediumFileUuid(Long mediumFileUuid) {
	this.mediumFileUuid = mediumFileUuid;
    }

    public int getMediumImageWidth() {
	return mediumImageWidth;
    }

    public void setMediumImageWidth(int mediumImageWidth) {
	this.mediumImageWidth = mediumImageWidth;
    }

    public int getMediumImageHeight() {
	return mediumImageHeight;
    }

    public void setMediumImageHeight(int mediumImageHeight) {
	this.mediumImageHeight = mediumImageHeight;
    }

    public Long getThumbnailFileUuid() {
	return thumbnailFileUuid;
    }

    public void setThumbnailFileUuid(Long thumbnailFileUuid) {
	this.thumbnailFileUuid = thumbnailFileUuid;
    }

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

    public String getOriginalFileDisplayUuid() {
        return originalFileDisplayUuid;
    }

    public void setOriginalFileDisplayUuid(String originalFileDisplayUuid) {
        this.originalFileDisplayUuid = originalFileDisplayUuid;
    }

    public String getMediumFileDisplayUuid() {
        return mediumFileDisplayUuid;
    }

    public void setMediumFileDisplayUuid(String mediumFileDisplayUuid) {
        this.mediumFileDisplayUuid = mediumFileDisplayUuid;
    }

    public String getThumbnailFileDisplayUuid() {
        return thumbnailFileDisplayUuid;
    }

    public void setThumbnailFileDisplayUuid(String thumbnailFileDisplayUuid) {
        this.thumbnailFileDisplayUuid = thumbnailFileDisplayUuid;
    }
}