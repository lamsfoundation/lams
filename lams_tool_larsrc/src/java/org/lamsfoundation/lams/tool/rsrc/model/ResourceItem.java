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

package org.lamsfoundation.lams.tool.rsrc.model;

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
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;

/**
 * Resource
 *
 * @author Dapeng Ni
 */
@Entity
@Table(name = "tl_larsrc11_resource_item")
public class ResourceItem implements Cloneable {
    private static final Logger log = Logger.getLogger(ResourceItem.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    // Resource Type:1=URL,2=File,3=Website
    @Column(name = "item_type")
    private short type;

    @Column
    private String title;

    @Column
    private String instructions;

    @Column
    private String url;

    @Column(name = "init_item")
    private String initialItem;

    @Column(name = "file_uuid")
    private Long fileUuid;

    @Column(name = "file_version_id")
    private Long fileVersionId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "is_hide")
    private boolean isHide;

    @Column(name = "create_by_author")
    private boolean isCreateByAuthor;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private ResourceUser createBy;

    @Column(name = "is_allow_rating")
    private boolean allowRating;

    @Column(name = "is_allow_comments")
    private boolean allowComments;

    // ******************************* DTO fields ***********
    @Transient
    private boolean complete;
    @Transient
    private ItemRatingDTO ratingDTO;

    @Override
    public Object clone() {
	ResourceItem obj = null;
	try {
	    obj = (ResourceItem) super.clone();
	    obj.setUid(null);
	    // clone ReourceUser as well
	    if (this.createBy != null) {
		obj.setCreateBy((ResourceUser) this.createBy.clone());
	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ResourceItem.class + " failed");
	}

	return obj;
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

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long crUuid) {
	this.fileUuid = crUuid;
    }

    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long crVersionId) {
	this.fileVersionId = crVersionId;
    }

    public String getInitialItem() {
	return initialItem;
    }

    public void setInitialItem(String initialItem) {
	this.initialItem = initialItem;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String description) {
	this.instructions = description;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public ResourceUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(ResourceUser createBy) {
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

    public short getType() {
	return type;
    }

    public void setType(short type) {
	this.type = type;
    }

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String type) {
	this.fileType = type;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	this.fileName = name;
    }

    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    public boolean isComplete() {
	return complete;
    }

    public ItemRatingDTO getRatingDTO() {
	return ratingDTO;
    }

    public void setRatingDTO(ItemRatingDTO ratingDTO) {
	this.ratingDTO = ratingDTO;
    }

    public boolean isAllowRating() {
	return allowRating;
    }

    public void setAllowRating(boolean allowRating) {
	this.allowRating = allowRating;
    }

    public boolean isAllowComments() {
	return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
	this.allowComments = allowComments;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", uid).append(" type", type).append(" title", title).toString();
    }

}
