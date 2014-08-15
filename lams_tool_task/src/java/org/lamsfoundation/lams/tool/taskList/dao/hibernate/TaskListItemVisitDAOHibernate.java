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
package org.lamsfoundation.lams.tool.taskList.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemVisitDAO;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemVisitLog;

/**
 * Hibernate implementation of <code>TaskListItemVisitDAO</code>.
 * 
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.dao.TaskListItemVisitDAO
 */
public class TaskListItemVisitDAOHibernate extends BaseDAOHibernate implements TaskListItemVisitDAO{
	
	private static final String FIND_BY_ITEM_AND_USER = "from " + TaskListItemVisitLog.class.getName()
			+ " as r where r.user.userId = ? and r.taskListItem.uid=?";

	private static final String FIND_BY_ITEM_BYSESSION = "from " + TaskListItemVisitLog.class.getName()
			+ " as r where r.sessionId = ? and r.taskListItem.uid=?";
	
	private static final String FIND_TASKS_COMPLETED_COUNT_BY_USER = "select count(*) from " + TaskListItemVisitLog.class.getName() 
			+ " as r where r.complete=true and r.sessionId=? and  r.user.userId =?";

//	private static final String FIND_SUMMARY = "select v.taskListItem.uid, count(v.taskListItem) from  "
//		+ TaskListItemVisitLog.class.getName() + " as v , "
//		+ TaskListSession.class.getName() + " as s, "
//		+ TaskList.class.getName() + "  as r "
//		+" where v.sessionId = s.sessionId "
//		+" and s.taskList.uid = r.uid "
//		+" and r.contentId =? "
//		+" group by v.sessionId, v.taskListItem.uid ";
	
    /**
     * {@inheritDoc}
     */
	public TaskListItemVisitLog getTaskListItemLog(Long itemUid,Long userId){
		List list = getHibernateTemplate().find(FIND_BY_ITEM_AND_USER,new Object[]{userId,itemUid});
		if(list == null || list.size() ==0)
			return null;
		return (TaskListItemVisitLog) list.get(0);
	}

    /**
     * {@inheritDoc}
     */
	public int getTasksCompletedCountByUser(Long toolSessionId ,Long userUid) {
		List list = getHibernateTemplate().find(FIND_TASKS_COMPLETED_COUNT_BY_USER, new Object[]{toolSessionId, userUid});
		if(list == null || list.size() ==0)
			return 0;
		return ((Number) list.get(0)).intValue();
	}

//	public Map<Long,Integer> getSummary(Long contentId) {
//
//		// Note: Hibernate 3.1 query.uniqueResult() returns Integer, Hibernate 3.2 query.uniqueResult() returns Long
//		List<Object[]> result =  getHibernateTemplate().find(FIND_SUMMARY,contentId);
//		Map<Long,Integer>  summaryList = new HashMap<Long,Integer> (result.size());
//		for(Object[] list : result){
//			if ( list[1] != null ) {
//				summaryList.put((Long)list[0],new Integer(((Number)list[1]).intValue()));
//			} 
//		}
//		return summaryList;
//		
//	}

    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	public List<TaskListItemVisitLog> getTaskListItemLogBySession(Long sessionId, Long itemUid) {
		
		return (List<TaskListItemVisitLog>) getHibernateTemplate().find(FIND_BY_ITEM_BYSESSION,new Object[]{sessionId,itemUid});
	}

}
