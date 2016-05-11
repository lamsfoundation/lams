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


package org.lamsfoundation.lams.tool.scratchie.dto;

import java.util.ArrayList;
import java.util.List;

import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;

/**
 * List contains following element: <br>
 *
 * <li>session_id</li>
 * <li>session_name</li>
 * <li>ScratchieItem.uid</li>
 * <li>
 * ScratchieItem.item_type</li>
 * <li>ScratchieItem.create_by_author</li>
 * <li>
 * ScratchieItem.is_hide</li>
 * <li>ScratchieItem.title</li>
 * <li>User.login_name</li>
 * <li>count(scratchie_item_uid)</li>
 *
 * @author Andrey Balan
 */
public class Summary {

    private Long sessionId;
    private String sessionName;
    private Long itemUid;
    private short itemType;
    private String itemTitle;
    private List<String> itemInstructions = new ArrayList<String>();
    private int viewNumber;

    // following is used for export portfolio programs:
    private String url;
    private Long fileUuid;
    private Long fileVersionId;
    private String fileName;
    private String attachmentLocalUrl;

    // true: initial group item, false, belong to some group.
    private boolean isInitGroup;

    public Summary() {
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
    public Summary(Long sessionId, String sessionName, ScratchieItem item, boolean isInitGroup) {
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	if (item != null) {
	    this.itemUid = item.getUid();
	    // TODO maybe a,b,c,d ?
	    this.itemTitle = item.getDescription();
	} else {
	    this.itemUid = new Long(-1);
	}
	this.isInitGroup = isInitGroup;
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
