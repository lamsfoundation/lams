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


package org.lamsfoundation.lams.tool.commonCartridge.model;

import java.util.Date;

/**
 * CommonCartridge
 * 
 * @author Andrey Balan
 */
public class CommonCartridgeItemVisitLog {

    private Long uid;
    private CommonCartridgeUser user;
    private CommonCartridgeItem commonCartridgeItem;
    private boolean complete;
    private Date accessDate;
    private Long sessionId;

    /**
     *
     * @return
     */
    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }

    /**
     *
     *
     * @return
     */
    public CommonCartridgeItem getCommonCartridgeItem() {
	return commonCartridgeItem;
    }

    public void setCommonCartridgeItem(CommonCartridgeItem item) {
	this.commonCartridgeItem = item;
    }

    /**
     *
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     * @return
     */
    public CommonCartridgeUser getUser() {
	return user;
    }

    public void setUser(CommonCartridgeUser user) {
	this.user = user;
    }

    /**
     *
     * @return
     */
    public boolean isComplete() {
	return complete;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    /**
     *
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

}
