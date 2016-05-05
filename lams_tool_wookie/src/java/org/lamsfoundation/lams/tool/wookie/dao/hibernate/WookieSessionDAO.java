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

package org.lamsfoundation.lams.tool.wookie.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.wookie.dao.IWookieSessionDAO;
import org.lamsfoundation.lams.tool.wookie.model.WookieSession;

/**
 * DAO for accessing the WookieSession objects - Hibernate specific code.
 */
public class WookieSessionDAO extends BaseDAO implements IWookieSessionDAO {

    public static final String SQL_QUERY_FIND_BY_SESSION_ID = "from " + WookieSession.class.getName()
	    + " where session_id=?";

    @Override
    public void saveOrUpdate(WookieSession session) {
	this.getHibernateTemplate().saveOrUpdate(session);
	this.getHibernateTemplate().flush();
    }

    @Override
    @SuppressWarnings("unchecked")
    public WookieSession getBySessionId(Long toolSessionId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_SESSION_ID, toolSessionId);
	if (list == null || list.isEmpty()) {
	    return null;
	}
	return (WookieSession) list.get(0);
    }

    @Override
    public void deleteBySessionID(Long toolSessionID) {
	WookieSession session = getBySessionId(toolSessionID);
	if (session != null) {
	    this.getHibernateTemplate().delete(session);
	    this.getHibernateTemplate().flush();
	}
    }
}
