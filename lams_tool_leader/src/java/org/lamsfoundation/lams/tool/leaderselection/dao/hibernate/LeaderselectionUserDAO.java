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


package org.lamsfoundation.lams.tool.leaderselection.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.leaderselection.dao.ILeaderselectionUserDAO;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the LeaderselectionUser objects - Hibernate specific code.
 */
@Repository
public class LeaderselectionUserDAO extends LAMSBaseDAO implements ILeaderselectionUserDAO {

    private static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + LeaderselectionUser.class.getName()
	    + " as f" + " where user_id=? and f.leaderselectionSession.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_UID = "from " + LeaderselectionUser.class.getName() + " where uid=?";

    private static final String FIND_BY_SESSION_ID = "from " + LeaderselectionUser.class.getName()
	    + " as u where u.leaderselectionSession.sessionId=?";

    @Override
    public LeaderselectionUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
	List list = doFind(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID, new Object[] { userId, toolSessionId });
	if (list == null || list.isEmpty()) {
	    return null;
	}
	return (LeaderselectionUser) list.get(0);
    }
    
    @Override
    public LeaderselectionUser getByUserIdAndContentId(Long userId, Long toolContentId) {
	final String SQL_QUERY_FIND_BY_USER_ID_CONTENT_ID = "from " + LeaderselectionUser.class.getName()
		    + " as user where user.userId=? and user.leaderselectionSession.leaderselection.toolContentId=?";
	
	List list = doFind(SQL_QUERY_FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, toolContentId });
	if (list == null || list.isEmpty()) {
	    return null;
	}
	return (LeaderselectionUser) list.get(0);
    }

    @Override
    public void saveOrUpdate(LeaderselectionUser leaderselectionUser) {
	getSession().saveOrUpdate(leaderselectionUser);
	getSession().flush();
    }

    @Override
    public LeaderselectionUser getByUID(Long uid) {
	List list = doFind(SQL_QUERY_FIND_BY_UID, new Object[] { uid });
	if (list == null || list.isEmpty()) {
	    return null;
	}
	return (LeaderselectionUser) list.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LeaderselectionUser> getBySessionId(Long sessionId) {
	return (List<LeaderselectionUser>) doFind(FIND_BY_SESSION_ID, sessionId);
    }
}
