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



package org.lamsfoundation.lams.tool.wiki.web.forms;

/**
 *
 */
public class MonitoringForm extends WikiPageForm {

    private static final long serialVersionUID = 9096908688391850595L;

    boolean teacherVisible;
    Long toolSessionID;
    String contentFolderID;

    // editing message page.
    Long messageUID;
    String messageBody;
    boolean messageHidden;
    String mode;

    public String getMessageBody() {
	return messageBody;
    }

    public void setMessageBody(String messageBody) {
	this.messageBody = messageBody;
    }

    public Long getMessageUID() {
	return messageUID;
    }

    public void setMessageUID(Long messageUID) {
	this.messageUID = messageUID;
    }

    public Long getToolSessionID() {
	return toolSessionID;
    }

    public void setToolSessionID(Long toolSessionID) {
	this.toolSessionID = toolSessionID;
    }

    public boolean isTeacherVisible() {
	return teacherVisible;
    }

    public void setTeacherVisible(boolean visible) {
	this.teacherVisible = visible;
    }

    public boolean isMessageHidden() {
	return messageHidden;
    }

    public void setMessageHidden(boolean messageHidden) {
	this.messageHidden = messageHidden;
    }

    public String getMode() {
	return mode;
    }

    public void setMode(String mode) {
	this.mode = mode;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }
}
