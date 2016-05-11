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

package org.lamsfoundation.lams.tool.taskList.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;

/**
 * DAO interface for <code>TaskListItem</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.model.TaskListItem
 */
public interface TaskListItemDAO extends DAO {

    /**
     * Return all taskList items which is uploaded by author in given taskListUid.
     * 
     * @param taskListUid
     * @return all taskList items which is uploaded by author in given taskListUid
     */
    List getAuthoringItems(Long taskListUid);

    /**
     * Return taskListItem with the specified taskListItemUid.
     * 
     * @param taskListItemUid
     *            specified taskListItemUid
     * @return taskListItem with the specified taskListItemUid
     */
    TaskListItem getByUid(Long taskListItemUid);

}
