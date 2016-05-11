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


package org.lamsfoundation.lams.tool.notebook.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookDAO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;

/**
 * DAO for accessing the Notebook objects - Hibernate specific code.
 */
public class NotebookDAO extends BaseDAO implements INotebookDAO {

    private static final String FIND_FORUM_BY_CONTENTID = "from Notebook notebook where notebook.toolContentId=?";

    @Override
    public Notebook getByContentId(Long toolContentId) {
	List list = getHibernateTemplate().find(NotebookDAO.FIND_FORUM_BY_CONTENTID, toolContentId);
	if (list != null && list.size() > 0) {
	    return (Notebook) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public void saveOrUpdate(Notebook notebook) {
	this.getHibernateTemplate().saveOrUpdate(notebook);
	this.getHibernateTemplate().flush();
    }

    @Override
    public void releaseFromCache(Object o) {
	getSession().evict(o);

    }
}
