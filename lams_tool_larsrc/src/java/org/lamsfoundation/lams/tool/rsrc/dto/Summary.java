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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.rsrc.dto;

import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;

/**
 * List contains following element: <br>
 * 
 * <li>session_id</li>
 * <li>session_name</li>
 * <li>ResourceItem.uid</li>
 * <li>ResourceItem.item_type</li>
 * <li>ResourceItem.create_by_author</li>
 * <li>ResourceItem.is_hide</li>
 * <li>ResourceItem.title</li>
 * <li>User.login_name</li>
 * <li>count(resource_item_uid)</li>
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class Summary {

	private Long sessionId;
	private String sessionName;
	private Long itemUid;
	private short itemType;
	private boolean itemCreateByAuthor;
	private boolean itemHide;
	private String itemTitle;
	private String username;
	private int viewNumber;
	
	//following is used for export portfolio programs:
	private String url;
	private Long fileUuid;
	private Long fileVersionId;
	//true: initial group item, false, belong to some group.
	private boolean isInitGroup;
	public Summary(){}
	/**
	 * Contruction method for monitoring summary function. 
	 * 
	 * <B>Don't not set isInitGroup and viewNumber fields</B>
	 * @param sessionName
	 * @param item
	 * @param isInitGroup
	 */
	public Summary(Long sessionId, String sessionName, ResourceItem item){
		this.sessionId = sessionId;
		this.sessionName = sessionName;
		this.itemUid = item.getUid();
		this.itemType = item.getType();
		this.itemCreateByAuthor = item.isCreateByAuthor();
		this.itemHide = item.isHide();
		this.itemTitle = item.getTitle();
		this.username = item.getCreateBy() == null?"":item.getCreateBy().getLoginName();
		this.url = item.getUrl();
		this.fileUuid = item.getFileUuid();
		this.fileVersionId = item.getFileVersionId();
	}
	/**
	 * Contruction method for export profolio function. 
	 * 
	 * <B>Don't not set sessionId and viewNumber fields</B>
	 * @param sessionName
	 * @param item
	 * @param isInitGroup
	 */
	public Summary(String sessionName, ResourceItem item,boolean isInitGroup){
		this.sessionName = sessionName;
		this.itemUid = item.getUid();
		this.itemType = item.getType();
		this.itemCreateByAuthor = item.isCreateByAuthor();
		this.itemHide = item.isHide();
		this.itemTitle = item.getTitle();
		this.username = item.getCreateBy() == null?"":item.getCreateBy().getLoginName();
		this.url = item.getUrl();
		this.fileUuid = item.getFileUuid();
		this.fileVersionId = item.getFileVersionId();
		
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
	
	
	
}
