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


package org.lamsfoundation.lams.tool.spreadsheet.dto;

import java.util.ArrayList;
import java.util.List;

import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetSession;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;

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
    public Summary(SpreadsheetSession session, Spreadsheet spreadsheet, List<SpreadsheetUser> users) {
	this.sessionId = session.getSessionId();
	this.sessionName = session.getSessionName();
	this.spreadsheet = spreadsheet;
	if (users != null) {
	    this.users = users;
	} else {
	    this.users = new ArrayList<SpreadsheetUser>();
	}
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