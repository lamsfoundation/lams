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


package org.lamsfoundation.lams.tool.taskList.web.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.planner.PedagogicalPlannerActivityForm;

/**
 *
 */
public class TaskListPedagogicalPlannerForm extends PedagogicalPlannerActivityForm {
    private List<String> taskListItem;

    @Override
    public ActionMessages validate() {
	ActionMessages errors = new ActionMessages();
	boolean valid = true;
	boolean allEmpty = true;
	if (taskListItem != null && !taskListItem.isEmpty()) {
	    for (String item : taskListItem) {
		if (!StringUtils.isEmpty(item)) {
		    allEmpty = false;
		    break;
		}
	    }
	}
	if (allEmpty) {
	    ActionMessage error = new ActionMessage("authoring.msg.no.tasks.save");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	    valid = false;
	    taskListItem = null;
	}

	setValid(valid);
	return errors;
    }

    public void fillForm(TaskList taskList) {
	if (taskList != null) {
	    setToolContentID(taskList.getContentId());

	    taskListItem = new ArrayList<String>();
	    Set<TaskListItem> items = taskList.getTaskListItems();
	    if (items != null) {
		int topicIndex = 0;
		for (TaskListItem item : items) {
		    setTaskListItem(topicIndex++, item.getTitle());
		}
	    }
	}
    }

    public void setTaskListItem(int number, String TaskListItems) {
	if (taskListItem == null) {
	    taskListItem = new ArrayList<String>();
	}
	while (number >= taskListItem.size()) {
	    taskListItem.add(null);
	}
	taskListItem.set(number, TaskListItems);
    }

    public String getTaskListItem(int number) {
	if (taskListItem == null || number >= taskListItem.size()) {
	    return null;
	}
	return taskListItem.get(number);
    }

    public Integer getTaskListItemCount() {
	return taskListItem == null ? 0 : taskListItem.size();
    }

    public boolean removeTaskListItem(int number) {
	if (taskListItem == null || number >= taskListItem.size()) {
	    return false;
	}
	taskListItem.remove(number);
	return true;
    }
}