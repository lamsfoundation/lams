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

package org.lamsfoundation.lams.learning.command.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Stores a command to be sent to a learner via websocket.
 */
public class Command implements Serializable {

    private Long uid;

    private Long lessonId;

    private String userName;

    private Date createDate;

    private String commandText;

    public Command() {
    }

    public Command(Long lessonId, String userName, String jsonCommand) {
	this.lessonId = lessonId;
	this.userName = userName;
	this.createDate = new Date();
	this.commandText = jsonCommand;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getLessonId() {
	return lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public String getCommandText() {
	return commandText;
    }

    public void setCommandText(String commandText) {
	this.commandText = commandText;
    }
}