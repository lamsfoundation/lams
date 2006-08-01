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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */

package org.lamsfoundation.lams.notebook.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.notebook.dao.INotebookEntryDAO;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;

public class NotebookEntryDAO extends BaseDAO implements INotebookEntryDAO {
	
	public static final String SQL_QUERY_FIND_ENTRY_BY_EXTERNAL_ID = "from " + NotebookEntry.class.getName() 
							+ " where external_id=? and external_id_type=? and user_id=?";
	
	public void saveOrUpdate(NotebookEntry notebookEntry) {
		this.getHibernateTemplate().saveOrUpdate(notebookEntry);
		this.getHibernateTemplate().flush();
	}

	public List get(String id, Long idType, Long userID) {
		return getHibernateTemplate().find(SQL_QUERY_FIND_ENTRY_BY_EXTERNAL_ID, new Object[]{id, idType, userID});
	}

	public NotebookEntry get(Long uid) {
		if (uid != null) {
			Object o = getHibernateTemplate().get(NotebookEntry.class, uid);
			return (NotebookEntry)o;
		} else {
			return null;
		}
	}
}
