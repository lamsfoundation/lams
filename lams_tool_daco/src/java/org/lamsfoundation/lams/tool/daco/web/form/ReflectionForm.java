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

package org.lamsfoundation.lams.tool.daco.web.form;

import org.apache.log4j.Logger;

/**
 *
 * Reflection Form.
 *
 *
 */
public class ReflectionForm {
    private static final long serialVersionUID = -9054365604649146735L;
    private static Logger logger = Logger.getLogger(ReflectionForm.class.getName());

    private Integer userId;
    private Long sessionId;
    private String entryText;
    private String sessionMapID;

    public String getEntryText() {
	return entryText;
    }

    public void setEntryText(String entryText) {
	this.entryText = entryText;
    }

    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userUid) {
	userId = userUid;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionMapID) {
	sessionId = sessionMapID;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

}
