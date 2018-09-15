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

import java.util.Map;

import org.lamsfoundation.lams.web.form.TextSearchForm;

/**
 * Form responsible for representing <code>TaskListCondition</code> objects on a view layer.
 *
 * @author Andrey Balan
 *
 *
 */
public class TaskListConditionForm extends TextSearchForm {

    //tool access mode;
    private String mode;
    private String sessionMapID;

    private String sequenceId;
    private String name;
//    private String taskListItemName;
    private Map<String, String> possibleItems;
    private String[] selectedItems;

    public TaskListConditionForm() {
	super();
    }

    /**
     * Returns TaskListCondition name.
     *
     * @return TaskListCondition name
     */
    public String getName() {
	return name;
    }

    /**
     * Sets TaskListCondition title.
     *
     * @param title
     *            TaskListCondition title
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Returns TaskListCondition sequence Id.
     *
     * @return TaskListCondition sequence Id
     */
    public String getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets TaskListCondition sequence Id.
     *
     * @param sequenceId
     *            TaskListCondition sequence Id
     */
    public void setSequenceId(String sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     * Returns current SessionMapID.
     *
     * @return current SessionMapID
     */
    public String getSessionMapID() {
	return sessionMapID;
    }

    /**
     * Sets current SessionMapID.
     *
     * @param sessionMapID
     *            current SessionMapID
     */
    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    /**
     * Returns working mode.
     *
     * @return working mode
     */
    public String getMode() {
	return mode;
    }

    /**
     * Returns working mode.
     *
     * @param mode
     *            working mode
     */
    public void setMode(String mode) {
	this.mode = mode;
    }

//	/**
//	 * Returns <code>TaskLiskItem</code> name.
//	 *
//	 * @return <code>TaskLiskItem</code> name
//	 */
//	public String getTaskListItemName() {
//		return taskListItemName;
//	}
//	/**
//	 * Sets <code>TaskLiskItem</code> name.
//	 *
//	 * @param taskListItemName <code>TaskLiskItem</code> name
//	 */
//	public void setTaskListItemName(String taskListItemName) {
//		this.taskListItemName = taskListItemName;
//	}

    public Map<String, String> getPossibleItems() {
	return possibleItems;
    }

    public void setPossibleItems(Map<String, String> lvBeans) {
	this.possibleItems = lvBeans;
    }

    public String[] getSelectedItems() {
	return selectedItems;
    }

    public void setSelectedItems(String[] selectedItems) {
	this.selectedItems = selectedItems;
    }

}
