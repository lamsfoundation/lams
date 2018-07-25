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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.taskList.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO object used in a monitoring. Contains information for group.
 *
 * @author Andrey Balan
 */
public class GroupSummary {

    //Group information.
    private String sessionName;

    private List<TaskListItemVisitLogSummary> taskListItemVisitLogSummaries;

    public GroupSummary() {
	taskListItemVisitLogSummaries = new ArrayList<TaskListItemVisitLogSummary>();
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public List<TaskListItemVisitLogSummary> getTaskListItemVisitLogSummaries() {
	return taskListItemVisitLogSummaries;
    }

    public void setTaskListItemVisitLogSummaries(List<TaskListItemVisitLogSummary> taskListItemVisitLogSummaries) {
	this.taskListItemVisitLogSummaries = taskListItemVisitLogSummaries;
    }

}
