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
package org.lamsfoundation.lams.tool.spreadsheet.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.util.SpreadsheetWebUtils;

/**
 * List contains following element: <br>
 * 
 * <li>session_id</li>
 * <li>session_name</li>
 * <li>SpreadsheetItem.uid</li>
 * <li>SpreadsheetItem.item_type</li>
 * <li>SpreadsheetItem.create_by_author</li>
 * <li>SpreadsheetItem.is_hide</li>
 * <li>SpreadsheetItem.title</li>
 * <li>User.login_name</li>
 * <li>count(spreadsheet_item_uid)</li>
 * 
 * @author Andrey Balan
 */
public class Summary {

	private Long sessionId;
	private String sessionName;
	private Spreadsheet spreadsheet;
    private List<SpreadsheetUser> users; 	
	
	public Summary(){}
	/**
	 * Contruction method for monitoring summary function. 
	 * 
	 * <B>Don't not set isInitGroup and viewNumber fields</B>
	 * @param sessionName
	 * @param item
	 * @param isInitGroup
	 */
	public Summary(String sessionName, Spreadsheet spreadsheet, List<SpreadsheetUser> users){
		this.sessionName = sessionName;
		this.spreadsheet = spreadsheet;
		if (users != null) {
			this.users = users;
		} else {
			this.users = new ArrayList<SpreadsheetUser>();
		}
	}
//	/**
//	 * Contruction method for export profolio function. 
//	 * 
//	 * <B>Don't not set sessionId and viewNumber fields</B>
//	 * @param sessionName
//	 * @param item
//	 * @param isInitGroup
//	 */
//	public Summary(Long sessionId, String sessionName, SpreadsheetItem item,boolean isInitGroup){
//		this.sessionId = sessionId;
//		this.sessionName = sessionName;
//		if(item != null){
//			this.itemUid = item.getUid();
//			this.itemType = item.getType();
//			this.itemCreateByAuthor = item.isCreateByAuthor();
//			this.itemHide = item.isHide();
//			this.itemTitle = item.getTitle();
//			this.username = item.getCreateBy() == null?"":item.getCreateBy().getLoginName();
//			this.url = SpreadsheetWebUtils.protocol(item.getUrl());
//			this.fileName = item.getFileName();
//			this.fileUuid = item.getFileUuid();
//			this.fileVersionId = item.getFileVersionId();
//
//			for (SpreadsheetItemInstruction instruction : (Set<SpreadsheetItemInstruction>)item.getItemInstructions()) {
//				itemInstructions.add(instruction.getDescription());
//			}
//		}else
//			this.itemUid = new Long(-1);
//		this.isInitGroup = isInitGroup;
//	}

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
	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}
	public void setSpreadsheet(Spreadsheet spreadsheet) {
		this.spreadsheet = spreadsheet;
	}
	public List<SpreadsheetUser> getUsers() {
		return users;
	}
	public void setUsers(List<SpreadsheetUser> users) {
		this.users = users;
	}
}
