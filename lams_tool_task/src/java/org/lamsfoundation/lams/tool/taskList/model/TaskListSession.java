/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.taskList.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_latask10_session")
public class TaskListSession {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "session_name")
    private String sessionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskList_uid")
    private TaskList taskList;

    @Column(name = "session_start_date")
    private Date sessionStartDate;

    @Column(name = "session_end_date")
    private Date sessionEndDate;

    @Column
    private int status;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_uid")
    @OrderBy("create_date DESC")
    private Set<TaskListItem> taskListItems = new HashSet<>();

//  **********************************************************
    //		Get/Set methods
//  **********************************************************
    /**
     *
     * @return Returns the ID.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uuid) {
	this.uid = uuid;
    }

    public Date getSessionEndDate() {
	return sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    public Date getSessionStartDate() {
	return sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public TaskList getTaskList() {
	return taskList;
    }

    public void setTaskList(TaskList taskList) {
	this.taskList = taskList;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     *
     * @return Returns the session name
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
     *
     * @param sessionName
     *            The session name to set.
     */
    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public Set<TaskListItem> getTaskListItems() {
	return taskListItems;
    }

    public void setTaskListItems(Set<TaskListItem> taskListItems) {
	this.taskListItems = taskListItems;
    }

}
