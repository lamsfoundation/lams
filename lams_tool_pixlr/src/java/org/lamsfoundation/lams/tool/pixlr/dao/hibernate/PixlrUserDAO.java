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


package org.lamsfoundation.lams.tool.pixlr.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.pixlr.dao.IPixlrUserDAO;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrUser;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the PixlrUser objects - Hibernate specific code.
 */
@Repository
public class PixlrUserDAO extends LAMSBaseDAO implements IPixlrUserDAO {

    public static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + PixlrUser.class.getName() + " as f"
	    + " where user_id=? and f.pixlrSession.sessionId=?";

    public static final String SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID = "from " + PixlrUser.class.getName()
	    + " as f where login_name=? and f.pixlrSession.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_UID = "from " + PixlrUser.class.getName() + " where uid=?";

    @Override
    @SuppressWarnings("unchecked")
    public PixlrUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
	List list = doFind(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID, new Object[] { userId, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (PixlrUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PixlrUser getByLoginNameAndSessionId(String loginName, Long toolSessionId) {

	List list = doFind(SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID, new Object[] { loginName, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (PixlrUser) list.get(0);

    }

    @Override
    public void saveOrUpdate(PixlrUser pixlrUser) {
	getSession().saveOrUpdate(pixlrUser);
	getSession().flush();
    }

    @Override
    @SuppressWarnings("unchecked")
    public PixlrUser getByUID(Long uid) {
	List list = doFind(SQL_QUERY_FIND_BY_UID, new Object[] { uid });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (PixlrUser) list.get(0);
    }
}
