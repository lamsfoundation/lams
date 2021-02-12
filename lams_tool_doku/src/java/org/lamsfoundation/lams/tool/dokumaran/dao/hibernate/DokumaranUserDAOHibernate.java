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

package org.lamsfoundation.lams.tool.dokumaran.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.dokumaran.dao.DokumaranUserDAO;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;
import org.springframework.stereotype.Repository;

@Repository
public class DokumaranUserDAOHibernate extends LAMSBaseDAO implements DokumaranUserDAO {

    private static final String FIND_BY_USER_ID_CONTENT_ID = "from " + DokumaranUser.class.getName()
	    + " as u where u.userId =? and u.dokumaran.contentId=?";
    private static final String FIND_BY_USER_ID_SESSION_ID = "from " + DokumaranUser.class.getName()
	    + " as u where u.userId =? and u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "from " + DokumaranUser.class.getName()
	    + " as u where u.session.sessionId=?";
    private static final String FIND_LEADERS_BY_SESSION_ID = "from " + DokumaranUser.class.getName()
	    + " as u where u.session.sessionId=? AND u.leader=1";

    @Override
    public DokumaranUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List<?> list = this.doFind(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (DokumaranUser) list.get(0);
    }

    @Override
    public DokumaranUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List<?> list = this.doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (DokumaranUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DokumaranUser> getBySessionID(Long sessionId) {
	return this.doFind(FIND_BY_SESSION_ID, sessionId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DokumaranUser> getLeadersBySessionId(Long sessionId) {
	return this.doFind(FIND_LEADERS_BY_SESSION_ID, sessionId);
    }

}
