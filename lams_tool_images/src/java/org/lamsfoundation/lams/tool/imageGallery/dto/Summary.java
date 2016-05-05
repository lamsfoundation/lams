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
package org.lamsfoundation.lams.tool.imageGallery.dto;

import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;

/**
 * List contains following element: <br>
 *
 * <li>session_id</li>
 * <li>session_name</li>
 * <li>ImageGalleryItem.uid</li>
 * <li>ImageGalleryItem.item_type</li>
 * <li>ImageGalleryItem.create_by_author</li>
 * <li>ImageGalleryItem.is_hide</li>
 * <li>ImageGalleryItem.title</li>
 * <li>User.login_name</li>
 * <li>count(imageGallery_item_uid)</li>
 *
 * @author Andrey Balan
 *
 * @version $Revision$
 */
public class Summary {

    private Long sessionId;
    private String sessionName;
    private Long itemUid;
    private ImageGalleryItem item;
    private boolean itemCreateByAuthor;
    private boolean itemHide;
    private String itemTitle;
    private String username;
    private int numberOfVotes;
    private ItemRatingDTO itemRatingDto;

    // following is used for export portfolio programs:
    private Long fileUuid;
    private Long fileVersionId;
    private String fileName;
    private String attachmentLocalUrl;

    // true: initial group item, false, belong to some group.
    private boolean isInitGroup;

    public Summary() {
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
    public Summary(Long sessionId, String sessionName, ImageGalleryItem item) {
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	if (item != null) {
	    this.itemUid = item.getUid();
	    this.item = item;
	    this.itemCreateByAuthor = item.isCreateByAuthor();
	    this.itemHide = item.isHide();
	    this.itemTitle = item.getTitle();
	    this.username = item.getCreateBy() == null ? ""
		    : item.getCreateBy().getFirstName() + " " + item.getCreateBy().getLastName();
	    this.fileName = item.getFileName();
	    this.fileUuid = item.getOriginalFileUuid();
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
    public Summary(Long sessionId, String sessionName, ImageGalleryItem item, boolean isInitGroup) {
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	if (item != null) {
	    this.itemUid = item.getUid();
	    this.item = item;
	    this.itemCreateByAuthor = item.isCreateByAuthor();
	    this.itemHide = item.isHide();
	    this.itemTitle = item.getTitle();
	    this.username = item.getCreateBy() == null ? ""
		    : item.getCreateBy().getFirstName() + " " + item.getCreateBy().getLastName();
	    this.fileName = item.getFileName();
	    this.fileUuid = item.getOriginalFileUuid();
	    this.fileVersionId = item.getFileVersionId();

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

    public Long getItemUid() {
	return itemUid;
    }

    public void setItemUid(Long itemUid) {
	this.itemUid = itemUid;
    }

    public ImageGalleryItem getItem() {
	return item;
    }

    public void setItem(ImageGalleryItem item) {
	this.item = item;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public int getNumberOfVotes() {
	return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
	this.numberOfVotes = numberOfVotes;
    }

    /**
     * @return itemRatingDto
     */
    public ItemRatingDTO getItemRatingDto() {
	return itemRatingDto;
    }

    /**
     * @param itemRatingDto
     */
    public void setItemRatingDto(ItemRatingDTO itemRatingDto) {
	this.itemRatingDto = itemRatingDto;
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

}
