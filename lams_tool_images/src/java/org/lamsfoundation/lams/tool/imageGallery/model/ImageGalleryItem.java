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
 * @author Dapeng Ni
 * 
 * @hibernate.class table="tl_laimag10_imageGallery_item"
 * 
 */
public class ImageGalleryItem implements Cloneable {
    private static final Logger log = Logger.getLogger(ImageGalleryItem.class);

    private Long uid;

    private String title;

    private String description;

    private int sequenceId;

    private String url;

    private boolean openUrlNewWindow;

    private Long fileUuid;

    private Long fileVersionId;

    private String fileName;

    private String fileType;

    private boolean isHide;
    private boolean isCreateByAuthor;

    private Date createDate;
    private ImageGalleryUser createBy;

    // ***********************************************
    // DTO fields:
    private boolean complete;

    @Override
    public Object clone() {
	ImageGalleryItem obj = null;
	try {
	    obj = (ImageGalleryItem) super.clone();
	    (obj).setUid(null);
	    // clone ReourceUser as well
	    if (this.createBy != null) {
		(obj).setCreateBy((ImageGalleryUser) this.createBy.clone());
	    }

	} catch (CloneNotSupportedException e) {
	    ImageGalleryItem.log.error("When clone " + ImageGalleryItem.class + " failed");
	}

	return obj;
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
     * @hibernate.property column="file_uuid"
     * 
     * @return
     */
    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long crUuid) {
	this.fileUuid = crUuid;
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
     * @hibernate.property column="description"
     * @return
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Returns taskListItem sequence number.
     * 
     * @return taskListItem sequence number
     * 
     * @hibernate.property column="sequence_id"
     */
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets taskListItem sequence number.
     * 
     * @param sequenceId
     *                taskListItem sequence number
     */
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
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
     * @hibernate.property column="url" length="65535"
     * @return
     */
    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
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
     * @hibernate.property column="open_url_new_window"
     * @return
     */
    public boolean isOpenUrlNewWindow() {
	return openUrlNewWindow;
    }

    public void setOpenUrlNewWindow(boolean openUrlNewWindow) {
	this.openUrlNewWindow = openUrlNewWindow;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    public boolean isComplete() {
	return complete;
    }
}
