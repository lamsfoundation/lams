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

package org.lamsfoundation.lams.tool.mdlesn.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.mdlesn.dao.IMdlLessonUserDAO;
import org.lamsfoundation.lams.tool.mdlesn.model.MdlLessonUser;

/**
 * DAO for accessing the MdlLessonUser objects - Hibernate specific code.
 */
public class MdlLessonUserDAO extends BaseDAO implements IMdlLessonUserDAO {

    public static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + MdlLessonUser.class.getName() + " as f"
	    + " where user_id=? and f.mdlLessonSession.sessionId=?";

    public static final String SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID = "from " + MdlLessonUser.class.getName()
	    + " as f where login_name=? and f.mdlLessonSession.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_UID = "from " + MdlLessonUser.class.getName() + " where uid=?";

    public MdlLessonUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID,
		new Object[] { userId, toolSessionId });

	if (list == null || list.isEmpty())
	    return null;

	return (MdlLessonUser) list.get(0);
    }

    public MdlLessonUser getByLoginNameAndSessionId(String loginName, Long toolSessionId) {

	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID,
		new Object[] { loginName, toolSessionId });

	if (list == null || list.isEmpty())
	    return null;

	return (MdlLessonUser) list.get(0);

    }

    public void saveOrUpdate(MdlLessonUser mdlLessonUser) {
	this.getHibernateTemplate().saveOrUpdate(mdlLessonUser);
	this.getHibernateTemplate().flush();
    }

    public MdlLessonUser getByUID(Long uid) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_UID, new Object[] { uid });

	if (list == null || list.isEmpty())
	    return null;

	return (MdlLessonUser) list.get(0);
    }
}
