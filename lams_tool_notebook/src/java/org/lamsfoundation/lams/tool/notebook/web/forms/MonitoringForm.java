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

package org.lamsfoundation.lams.tool.notebook.web.forms;

import org.apache.struts.action.ActionForm;

/**
 *
 */
public class MonitoringForm extends ActionForm {

	private static final long serialVersionUID = 9096908688391850595L;
	
	String dispatch;
	boolean teacherVisible;
	Long toolSessionID;
	
	// editing message page.
	Long messageUID;
	String messageBody;	
	boolean messageHidden;
	
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
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
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

}
