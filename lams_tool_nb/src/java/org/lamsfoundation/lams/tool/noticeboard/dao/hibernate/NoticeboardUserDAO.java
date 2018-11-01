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


package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardUser;
import org.springframework.stereotype.Repository;

/**
 * @author mtruong
 *         <p>
 *         Hibernate implementation for database access to Noticeboard users (learners) for the noticeboard tool.
 *         </p>
 */
@Repository
public class NoticeboardUserDAO extends LAMSBaseDAO implements INoticeboardUserDAO {
    private static final String FIND_NB_USER_BY_SESSION = "from " + NoticeboardUser.class.getName()
	    + " as nb where nb.userId=:userId and nb.nbSession.nbSessionId=:sessionId";

    private static final String COUNT_USERS_IN_SESSION = "select nu.userId from NoticeboardUser nu where nu.nbSession= :nbSession";

    /**
     * @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#getNbUserByID(java.lang.Long)
     */
    @Override
    public NoticeboardUser getNbUser(Long userId, Long sessionId) {
	String query = "from NoticeboardUser user where user.userId=? and user.nbSession.nbSessionId=?";
	Object[] values = new Object[2];
	values[0] = userId;
	values[1] = sessionId;
	List users = doFind(query, values);
	if (users != null && users.size() == 0) {
	    return null;
	} else {
	    return (NoticeboardUser) users.get(0);
	}
    }

    /**
     * @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#getNbUserBySession(java.lang.Long,
     *      java.lang.Long)
     */
    @Override
    public NoticeboardUser getNbUserBySession(Long userId, Long sessionId) {
	List usersReturned = getSessionFactory().getCurrentSession().createQuery(FIND_NB_USER_BY_SESSION)
		.setParameter("userId", userId.longValue())
		.setParameter("sessionId", sessionId.longValue()).list();

	if (usersReturned != null && usersReturned.size() > 0) {
	    NoticeboardUser nb = (NoticeboardUser) usersReturned.get(0);
	    return nb;
	} else {
	    return null;
	}

    }

    /**
     * @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#saveNbUser(org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardUser)
     */
    @Override
    public void saveNbUser(NoticeboardUser nbUser) {
	this.getSession().save(nbUser);
    }

    /**
     * @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#updateNbUser(org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardUser)
     */
    @Override
    public void updateNbUser(NoticeboardUser nbUser) {
	this.getSession().update(nbUser);
    }

    /**
     * @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO#getNumberOfUsers((org.lamsfoundation.lams.
     *      tool.noticeboard.NoticeboardSession)
     */
    @Override
    public int getNumberOfUsers(NoticeboardSession nbSession) {
	return (doFindByNamedParam(COUNT_USERS_IN_SESSION, new String[] { "nbSession" }, new Object[] { nbSession }))
		.size();
    }

    @Override
    public List getNbUsersBySession(Long sessionId) {
	String query = "from NoticeboardUser user where user.nbSession.nbSessionId=?";
	return doFind(query, sessionId);
    }
}