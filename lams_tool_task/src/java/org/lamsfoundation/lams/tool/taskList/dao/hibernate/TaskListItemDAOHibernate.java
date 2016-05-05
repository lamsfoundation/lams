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

import org.lamsfoundation.lams.tool.taskList.dao.TaskListItemDAO;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;

/**
 * Hibernate implementation of <code>TaskListItemDAO</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.dao.TaskListItemDAO
 */
public class TaskListItemDAOHibernate extends BaseDAOHibernate implements TaskListItemDAO {

    private static final String FIND_AUTHORING_ITEMS = "from " + TaskListItem.class.getName()
	    + " where taskList_uid = ? order by sequence_Id asc";

    /**
     * {@inheritDoc}
     */
    @Override
    public List getAuthoringItems(Long taskListUid) {
	return this.getHibernateTemplate().find(FIND_AUTHORING_ITEMS, taskListUid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskListItem getByUid(Long taskListItemUid) {
	return (TaskListItem) this.getObject(TaskListItem.class, taskListItemUid);
    }

}
