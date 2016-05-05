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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.taskList.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;

/**
 *
 * TaskList Form.
 * 
 * @struts.form name="taskListForm"
 *
 *              User: Dapeng.Ni
 */
public class TaskListForm extends ActionForm {
    private static final long serialVersionUID = 3599879328307492312L;

    private static Logger logger = Logger.getLogger(TaskListForm.class.getName());

    //Forum fields
    private String sessionMapID;
    private String contentFolderID;
    private int currentTab;
    private FormFile offlineFile;
    private FormFile onlineFile;

    private TaskList taskList;

    public TaskListForm() {
	taskList = new TaskList();
	taskList.setTitle("Shared TaskList");
	currentTab = 1;
    }

    public void setTaskList(TaskList taskList) {
	this.taskList = taskList;
	//set Form special varaible from given forum
	if (taskList == null) {
	    logger.error("Initial TaskListForum failed by null value of TaskList.");
	}
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	String param = mapping.getParameter();
	//if it is start page, all data read out from database or current session
	//so need not reset checkbox to refresh value!
	if (!StringUtils.equals(param, "start") && !StringUtils.equals(param, "initPage")) {
	    taskList.setLockWhenFinished(false);
	    taskList.setSequentialOrder(false);
	    taskList.setMinimumNumberTasks(0);
	    taskList.setAllowContributeTasks(false);
	    taskList.setMonitorVerificationRequired(false);
	    taskList.setDefineLater(false);
	    taskList.setReflectOnActivity(false);
	}
    }

    public int getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(int currentTab) {
	this.currentTab = currentTab;
    }

    public FormFile getOfflineFile() {
	return offlineFile;
    }

    public void setOfflineFile(FormFile offlineFile) {
	this.offlineFile = offlineFile;
    }

    public FormFile getOnlineFile() {
	return onlineFile;
    }

    public void setOnlineFile(FormFile onlineFile) {
	this.onlineFile = onlineFile;
    }

    public TaskList getTaskList() {
	return taskList;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

}
