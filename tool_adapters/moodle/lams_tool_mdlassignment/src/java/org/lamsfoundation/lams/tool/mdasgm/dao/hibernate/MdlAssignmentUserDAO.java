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

package org.lamsfoundation.lams.tool.mdasgm.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.mdasgm.dao.IMdlAssignmentUserDAO;
import org.lamsfoundation.lams.tool.mdasgm.model.MdlAssignmentUser;

/**
 * DAO for accessing the MdlAssignmentUser objects - Hibernate specific code.
 */
public class MdlAssignmentUserDAO extends BaseDAO implements IMdlAssignmentUserDAO {

    public static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + MdlAssignmentUser.class.getName() + " as f"
	    + " where user_id=? and f.mdlAssignmentSession.sessionId=?";

    public static final String SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID = "from " + MdlAssignmentUser.class.getName()
	    + " as f where login_name=? and f.mdlAssignmentSession.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_UID = "from " + MdlAssignmentUser.class.getName() + " where uid=?";

    public MdlAssignmentUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID,
		new Object[] { userId, toolSessionId });

	if (list == null || list.isEmpty())
	    return null;

	return (MdlAssignmentUser) list.get(0);
    }

    public MdlAssignmentUser getByLoginNameAndSessionId(String loginName, Long toolSessionId) {

	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID,
		new Object[] { loginName, toolSessionId });

	if (list == null || list.isEmpty())
	    return null;

	return (MdlAssignmentUser) list.get(0);

    }

    public void saveOrUpdate(MdlAssignmentUser mdlAssignmentUser) {
	this.getHibernateTemplate().saveOrUpdate(mdlAssignmentUser);
	this.getHibernateTemplate().flush();
    }

    public MdlAssignmentUser getByUID(Long uid) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_UID, new Object[] { uid });

	if (list == null || list.isEmpty())
	    return null;

	return (MdlAssignmentUser) list.get(0);
    }
}
