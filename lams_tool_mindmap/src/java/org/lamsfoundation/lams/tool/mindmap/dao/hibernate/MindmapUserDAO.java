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
import org.lamsfoundation.lams.tool.mindmap.dao.IMindmapUserDAO;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the MindmapUser objects - Hibernate specific code.
 */
@Repository
public class MindmapUserDAO extends LAMSBaseDAO implements IMindmapUserDAO {

    public static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + MindmapUser.class.getName() + " as f"
	    + " where user_id=? and f.mindmapSession.sessionId=?";

    public static final String SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID = "from " + MindmapUser.class.getName()
	    + " as f where login_name=? and f.mindmapSession.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_UID = "from " + MindmapUser.class.getName() + " where uid=?";

    @Override
    public MindmapUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
	List list = doFind(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID, new Object[] { userId, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (MindmapUser) list.get(0);
    }

    @Override
    public MindmapUser getByLoginNameAndSessionId(String loginName, Long toolSessionId) {

	List list = doFind(SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID, new Object[] { loginName, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (MindmapUser) list.get(0);

    }

    @Override
    public void saveOrUpdate(MindmapUser mindmapUser) {
	getSession().saveOrUpdate(mindmapUser);
	getSession().flush();
    }

    @Override
    public MindmapUser getByUID(Long uid) {
	List list = doFind(SQL_QUERY_FIND_BY_UID, new Object[] { uid });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (MindmapUser) list.get(0);
    }
}
