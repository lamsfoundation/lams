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

package org.lamsfoundation.lams.tool.taskList.dao.hibernate;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemVisitDAO;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemVisitLog;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of <code>TaskListItemVisitDAO</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.dao.TaskListItemVisitDAO
 */
@Repository
public class TaskListItemVisitDAOHibernate extends LAMSBaseDAO implements TaskListItemVisitDAO {

    private static final String FIND_BY_ITEM_AND_USER = "from " + TaskListItemVisitLog.class.getName()
	    + " as r where r.user.userId = ? and r.taskListItem.uid=?";

    private static final String FIND_BY_ITEM_BYSESSION = "from " + TaskListItemVisitLog.class.getName()
	    + " as r where r.sessionId = ? and r.taskListItem.uid=?";

    private static final String FIND_COUNT_COMPLETED_TASKS_BY_USER = "select count(*) from "
	    + TaskListItemVisitLog.class.getName()
	    + " as r where r.complete=true and r.sessionId=? and  r.user.userId =?";

    private static final String FIND_COUNT_COMPLETED_TASKS_BY_SESSION_AND_ITEM = "select count(*) from "
	    + TaskListItemVisitLog.class.getName()
	    + " as r where r.complete=true and r.sessionId=? and  r.taskListItem.uid =?";

    private static final String SQL_QUERY_DATES_BY_USER_SESSION = "SELECT MIN(access_date) start_date, MAX(access_date) end_date "
	    + " FROM tl_latask10_item_log WHERE user_uid = :userUid and session_id = :sessionId";
    
    @Override
    public TaskListItemVisitLog getTaskListItemLog(Long itemUid, Long userId) {
	List list = doFind(FIND_BY_ITEM_AND_USER, new Object[] { userId, itemUid });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (TaskListItemVisitLog) list.get(0);
    }

    @Override
    public int getCountCompletedTasksByUser(Long toolSessionId, Long userUid) {
	List list = doFind(FIND_COUNT_COMPLETED_TASKS_BY_USER, new Object[] { toolSessionId, userUid });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    public int getCountCompletedTasksBySessionAndItem(Long toolSessionId, Long itemUid) {
	List list = doFind(FIND_COUNT_COMPLETED_TASKS_BY_SESSION_AND_ITEM, new Object[] { toolSessionId, itemUid });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TaskListItemVisitLog> getTaskListItemLogBySession(Long sessionId, Long itemUid) {
	return (List<TaskListItemVisitLog>) doFind(FIND_BY_ITEM_BYSESSION, new Object[] { sessionId, itemUid });
    }

    @Override
    public Object[] getDateRangeOfTasks(Long userUid, Long sessionId) {
	NativeQuery query = (NativeQuery) getSession().createNativeQuery(SQL_QUERY_DATES_BY_USER_SESSION.toString())
		.setParameter("userUid", userUid).setParameter("sessionId", sessionId);
	Object[] values = (Object[]) query.list().get(0);
	return values;
    }
}
