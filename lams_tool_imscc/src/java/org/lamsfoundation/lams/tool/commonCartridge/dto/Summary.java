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


package org.lamsfoundation.lams.tool.commonCartridge.dto;

import java.util.ArrayList;
import java.util.List;

import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;

/**
 * List contains following element: <br>
 *
 * <li>session_id</li>
 * <li>session_name</li>
 * <li>CommonCartridgeItem.uid</li>
 * <li>CommonCartridgeItem.item_type</li>
 * <li>CommonCartridgeItem.create_by_author</li>
 * <li>CommonCartridgeItem.is_hide</li>
 * <li>CommonCartridgeItem.title</li>
 * <li>User.login_name</li>
 * <li>count(commonCartridge_item_uid)</li>
 *
 * @author Andrey Balan
 */
public class Summary {

    private Long sessionId;
    private String sessionName;
    private Long itemUid;
    private short itemType;
    private boolean itemCreateByAuthor;
    private boolean itemHide;
    private String itemTitle;
    private List<String> itemInstructions = new ArrayList<String>();
    private String username;
    private int viewNumber;

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
    public Summary(Long sessionId, String sessionName, CommonCartridgeItem item) {
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	if (item != null) {
	    this.itemUid = item.getUid();
	    this.itemType = item.getType();
	    this.itemCreateByAuthor = item.isCreateByAuthor();
	    this.itemHide = item.isHide();
	    this.itemTitle = item.getTitle();
	    this.username = item.getCreateBy() == null ? "" : item.getCreateBy().getLoginName();
	} else {
	    this.itemUid = new Long(-1);
	}
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

    public int getViewNumber() {
	return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
	this.viewNumber = viewNumber;
    }

    public boolean isInitGroup() {
	return isInitGroup;
    }

    public void setInitGroup(boolean isInitGroup) {
	this.isInitGroup = isInitGroup;
    }

    public List<String> getItemInstructions() {
	return itemInstructions;
    }

    public void setItemInstructions(List<String> itemInstructions) {
	this.itemInstructions = itemInstructions;
    }
}