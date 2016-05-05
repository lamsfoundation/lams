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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.rsrc.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemInstruction;
import org.lamsfoundation.lams.tool.rsrc.util.ResourceWebUtils;

/**
 * List contains following element: <br>
 *
 * <li>ResourceItem.uid</li>
 * <li>
 * ResourceItem.item_type</li>
 * <li>ResourceItem.create_by_author</li>
 * <li>
 * ResourceItem.is_hide</li>
 * <li>ResourceItem.title</li>
 * <li>User.login_name</li>
 * <li>count(resource_item_uid)</li>
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class ResourceItemDTO {

    private Long itemUid;
    private short itemType;
    private boolean itemCreateByAuthor;
    private boolean itemHide;
    private String itemTitle;
    private List<String> itemInstructions = new ArrayList<String>();
    private String username;
    private int viewNumber;

    // following is used for export portfolio programs:
    private String url;
    private Long fileUuid;
    private Long fileVersionId;
    private String fileName;
    private String attachmentLocalUrl;

    // true: initial group item, false, belong to some group.
    private boolean isInitGroup;

    public ResourceItemDTO() {
    }

    /**
     * Contruction method for monitoring summary function.
     *
     * <B>Don't not set isInitGroup and viewNumber fields</B>
     *
     * @param sessionName
     * @param item
     * @param isInitGroup
     */
    public ResourceItemDTO(ResourceItem item) {
	if (item != null) {
	    this.itemUid = item.getUid();
	    this.itemType = item.getType();
	    this.itemCreateByAuthor = item.isCreateByAuthor();
	    this.itemHide = item.isHide();
	    this.itemTitle = item.getTitle();
	    this.username = item.getCreateBy() == null ? "" : item.getCreateBy().getLoginName();
	    this.url = ResourceWebUtils.protocol(item.getUrl());
	    this.fileName = item.getFileName();
	    this.fileUuid = item.getFileUuid();
	    this.fileVersionId = item.getFileVersionId();
	} else {
	    this.itemUid = new Long(-1);
	}
    }

    /**
     * Contruction method for export profolio function.
     *
     * <B>Don't not set sessionId and viewNumber fields</B>
     *
     * @param sessionName
     * @param item
     * @param isInitGroup
     */
    public ResourceItemDTO(ResourceItem item, boolean isInitGroup) {
	if (item != null) {
	    this.itemUid = item.getUid();
	    this.itemType = item.getType();
	    this.itemCreateByAuthor = item.isCreateByAuthor();
	    this.itemHide = item.isHide();
	    this.itemTitle = item.getTitle();
	    this.username = item.getCreateBy() == null ? "" : item.getCreateBy().getLoginName();
	    this.url = ResourceWebUtils.protocol(item.getUrl());
	    this.fileName = item.getFileName();
	    this.fileUuid = item.getFileUuid();
	    this.fileVersionId = item.getFileVersionId();

	    for (ResourceItemInstruction instruction : (Set<ResourceItemInstruction>) item.getItemInstructions()) {
		itemInstructions.add(instruction.getDescription());
	    }
	} else {
	    this.itemUid = new Long(-1);
	}
	this.isInitGroup = isInitGroup;
    }

    public boolean isItemCreateByAuthor() {
	return itemCreateByAuthor;
    }

    public void setItemCreateByAuthor(boolean itemCreateByAuthor) {
	this.itemCreateByAuthor = itemCreateByAuthor;
    }

    public boolean isItemHide() {
	return itemHide;
    }

    public void setItemHide(boolean itemHide) {
	this.itemHide = itemHide;
    }

    public String getItemTitle() {
	return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
	this.itemTitle = itemTitle;
    }

    public short getItemType() {
	return itemType;
    }

    public void setItemType(short itemType) {
	this.itemType = itemType;
    }

    public Long getItemUid() {
	return itemUid;
    }

    public void setItemUid(Long itemUid) {
	this.itemUid = itemUid;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public int getViewNumber() {
	return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
	this.viewNumber = viewNumber;
    }

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long fileUuid) {
	this.fileUuid = fileUuid;
    }

    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long fileVersionId) {
	this.fileVersionId = fileVersionId;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public boolean isInitGroup() {
	return isInitGroup;
    }

    public void setInitGroup(boolean isInitGroup) {
	this.isInitGroup = isInitGroup;
    }

    public String getAttachmentLocalUrl() {
	return attachmentLocalUrl;
    }

    public void setAttachmentLocalUrl(String attachmentLocalUrl) {
	this.attachmentLocalUrl = attachmentLocalUrl;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public List<String> getItemInstructions() {
	return itemInstructions;
    }

    public void setItemInstructions(List<String> itemInstructions) {
	this.itemInstructions = itemInstructions;
    }

}
