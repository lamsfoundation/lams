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

import org.lamsfoundation.lams.tool.taskList.model.TaskListSession;

/**
 * DAO interface for <code>TaskListSession</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.model.TaskListSession
 */
public interface TaskListSessionDAO extends DAO {

    /**
     * Returns <code>TaskListSession</code> with the specified sessionId
     * 
     * @param sessionId
     *            specified sessionId
     * @return TaskListSession with the specified sessionId
     */
    TaskListSession getSessionBySessionId(Long sessionId);

    /**
     * Returns <code>TaskListSession</code> wich corresponds to the tool with the specified toolContentId
     * 
     * @param toolContentId
     *            specified toolContentId
     * @return TaskListSession wich corresponds to the tool with the specified toolContentId
     */
    List<TaskListSession> getByContentId(Long toolContentId);

    /**
     * Delete specified session.
     * 
     * @param session
     *            specified session
     */
    void delete(TaskListSession session);

    /**
     * Delete session with the specified toolSessionId.
     * 
     * @param toolSessionId
     *            specified toolSessionId
     */
    void deleteBySessionId(Long toolSessionId);

}
