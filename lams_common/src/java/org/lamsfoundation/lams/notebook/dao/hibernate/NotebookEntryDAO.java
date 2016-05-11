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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */



package org.lamsfoundation.lams.notebook.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.notebook.dao.INotebookEntryDAO;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.springframework.stereotype.Repository;

@Repository
public class NotebookEntryDAO extends LAMSBaseDAO implements INotebookEntryDAO {

    private static final String SQL_QUERY_FIND_ENTRY_BY_EXTERNAL_ID_SIG = "from " + NotebookEntry.class.getName()
	    + " where external_id=? and external_id_type=? and external_signature=? and user_id=?"
	    + " order by create_date desc";
    private static final String SQL_QUERY_FIND_ENTRY_BY_EXTERNAL_ID_SIG_ALL = "from " + NotebookEntry.class.getName()
	    + " where external_id=? and external_id_type=? and external_signature=?"
	    + " order by user_id asc, create_date desc";
    private static final String SQL_QUERY_FIND_ENTRY_BY_EXTERNAL_ID = "from " + NotebookEntry.class.getName()
	    + " where external_id=? and external_id_type=? and user_id=?" + " order by create_date desc";
    private static final String SQL_QUERY_FIND_ENTRY_BY_USER_ID = "from " + NotebookEntry.class.getName()
	    + " where user_id=?";
    private static final String SQL_QUERY_FIND_ENTRY_BY_EXTERNAL_ID_TYPE = "from " + NotebookEntry.class.getName()
	    + " where user_id=? and external_id_type=?" + " order by external_signature desc, create_date desc";

    @Override
    public void saveOrUpdate(NotebookEntry notebookEntry) {
	insertOrUpdate(notebookEntry);
	this.getSession().flush();
    }

    @Override
    public List<NotebookEntry> get(Long id, Integer idType, String signature, Integer userID) {
	return (List<NotebookEntry>) (doFind(NotebookEntryDAO.SQL_QUERY_FIND_ENTRY_BY_EXTERNAL_ID_SIG,
		new Object[] { id, idType, signature, userID }));
    }

    @Override
    public List<NotebookEntry> get(Long id, Integer idType, String signature) {
	return (List<NotebookEntry>) (doFind(NotebookEntryDAO.SQL_QUERY_FIND_ENTRY_BY_EXTERNAL_ID_SIG_ALL,
		new Object[] { id, idType, signature }));
    }

    @Override
    public List<NotebookEntry> get(Long id, Integer idType, Integer userID) {
	return (List<NotebookEntry>) (doFind(NotebookEntryDAO.SQL_QUERY_FIND_ENTRY_BY_EXTERNAL_ID,
		new Object[] { id, idType, userID }));
    }

    @Override
    public NotebookEntry get(Long uid) {
	if (uid != null) {
	    Object o = getSession().get(NotebookEntry.class, uid);
	    return (NotebookEntry) o;
	} else {
	    return null;
	}
    }

    @Override
    public List<NotebookEntry> get(Integer userID) {
	return (List<NotebookEntry>) (doFind(NotebookEntryDAO.SQL_QUERY_FIND_ENTRY_BY_USER_ID, userID));
    }

    @Override
    public List<NotebookEntry> get(Integer userID, Integer idType) {
	return (List<NotebookEntry>) (doFind(NotebookEntryDAO.SQL_QUERY_FIND_ENTRY_BY_EXTERNAL_ID_TYPE,
		new Object[] { userID, idType }));
    }

    @Override
    public List<NotebookEntry> get(Integer userID, Long lessonID) {
	// TODO need to write hql query for lessionID and userID
	return null;
    }

    @Override
    public void delete(NotebookEntry notebookEntry) {
	getSession().delete(notebookEntry);
    }
}