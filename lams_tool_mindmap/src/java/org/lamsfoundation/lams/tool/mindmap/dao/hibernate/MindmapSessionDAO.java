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

package org.lamsfoundation.lams.tool.mindmap.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapSessionDAO;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the MindmapSession objects - Hibernate specific code.
 */
@Repository
public class MindmapSessionDAO extends LAMSBaseDAO implements IMindmapSessionDAO {

    public static final String SQL_QUERY_FIND_BY_SESSION_ID = "from " + MindmapSession.class.getName()
	    + " where session_id = ?";

    @Override
    public void saveOrUpdate(MindmapSession session) {
	getSession().saveOrUpdate(session);
	getSession().flush();
    }

    @Override
    public MindmapSession getBySessionId(Long toolSessionId) {
	List list = doFindCacheable(SQL_QUERY_FIND_BY_SESSION_ID, toolSessionId);
	if (list == null || list.isEmpty()) {
	    return null;
	}
	return (MindmapSession) list.get(0);
    }

    @Override
    public void deleteBySessionID(Long toolSessionID) {
	MindmapSession session = getBySessionId(toolSessionID);
	if (session != null) {
	    getSession().delete(session);
	    getSession().flush();
	}
    }

}
