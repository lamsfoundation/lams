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

import org.lamsfoundation.lams.tool.taskList.model.TaskListItemVisitLog;

/**
 * DAO interface for <code>TaskListItemVisit</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.model.TaskListItemVisit
 */
public interface TaskListItemVisitDAO extends DAO {

    /**
     * Returns TaskListItemVisitLog which corresponds to specified taskListItemUid and userId.
     *
     * @param itemUid
     *            specified taskListItemUid
     * @param userId
     *            specified userId
     * @return TaskListItemVisitLog wich corresponds to specified taskListItemUid and userId
     */
    TaskListItemVisitLog getTaskListItemLog(Long itemUid, Long userId);

    /**
     * Return list of taskListItemVisitLogs which corresponds to specified sessionId and itemUid
     *
     * @param sessionId
     *            specified sessionId
     * @param itemUid
     *            specified itemUid
     * @return list of taskListItemVisitLogs which corresponds to specified sessionId and itemUid
     */
    List<TaskListItemVisitLog> getTaskListItemLogBySession(Long sessionId, Long itemUid);

    /**
     *
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    int getCountCompletedTasksByUser(Long toolSessionId, Long userId);

    int getCountCompletedTasksBySessionAndItem(Long toolSessionId, Long itemUid);

    public Object[] getDateRangeOfTasks(Long userUid, Long sessionId);
}
