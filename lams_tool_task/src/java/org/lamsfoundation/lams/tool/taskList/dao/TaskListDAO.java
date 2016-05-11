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

import org.lamsfoundation.lams.tool.taskList.model.TaskList;

/**
 * DAO interface for <code>TaskList</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.model.TaskList
 */
public interface TaskListDAO extends DAO {

    /**
     * Returns <code>TaskList</code> with the specified contentId.
     * 
     * @param contentId
     *            specified contentId
     * @return TaskList with the specified contentId
     */
    TaskList getByContentId(Long contentId);

    /**
     * Returns <code>TaskList</code> with the specified taskListUid.
     * 
     * @param taskListUid
     *            specified taskListUid
     * @return TaskList with the specified taskListUid
     */
    TaskList getByUid(Long taskListUid);

    /**
     * Delete specified taskList.
     * 
     * @param taskList
     *            specified taskList
     */
    void delete(TaskList taskList);

}
