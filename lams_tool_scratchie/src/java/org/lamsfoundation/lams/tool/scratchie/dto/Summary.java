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
 * ScratchieSession DTO
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