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


package org.lamsfoundation.lams.tool.taskList.dto;

import java.util.List;

import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListSession;

/**
 * List contains following element: <br>
 *
 * <li>session_id</li>
 * <li>isMonitorVerificationRequired</li>
 * <li>taskListItems</li>
 * <li>visitNumbers</li>
 *
 * @author Andrey Balan
 */
public class SessionDTO {

    private Long sessionId;
    private String sessionName;

    private List<TaskListItem> taskListItems;

    private int[] visitNumbers;

    public SessionDTO(TaskListSession session) {
	this.sessionId = session.getSessionId();
	this.sessionName = session.getSessionName();
    }

    /**
     * Contruction method for monitoring summary function.
     */
    public SessionDTO(Long sessionId, String sessionName, List<TaskListItem> taskListItems, int[] visitNumbers) {
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	this.taskListItems = taskListItems;
	this.visitNumbers = visitNumbers;
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

    public List<TaskListItem> getTaskListItems() {
	return taskListItems;
    }

    public void setTaskListItems(List<TaskListItem> taskListItems) {
	this.taskListItems = taskListItems;
    }

    public int[] getVisitNumbers() {
	return visitNumbers;
    }

    public void setVisitNumbers(int[] visitNumbers) {
	this.visitNumbers = visitNumbers;
    }

}
