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
package org.lamsfoundation.lams.tool.peerreview.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewUserDAO;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;

public class PeerreviewUserDAOHibernate extends BaseDAOHibernate implements PeerreviewUserDAO {

    private static final String FIND_BY_USER_ID_CONTENT_ID = "FROM " + PeerreviewUser.class.getName()
	    + " AS u WHERE u.userId =? AND u.peerreview.contentId=?";
    private static final String FIND_BY_USER_ID_SESSION_ID = "FROM " + PeerreviewUser.class.getName()
	    + " AS u WHERE u.userId =? AND u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "FROM " + PeerreviewUser.class.getName()
	    + " AS u WHERE u.session.sessionId=?";
    private static final String GET_USERIDS_BY_SESSION_ID = "SELECT userId FROM " + PeerreviewUser.class.getName()
	    + " AS u WHERE u.session.sessionId=?";
    
    private static final String FIND_BY_CONTENT_ID = "FROM " + PeerreviewUser.class.getName()
	    + " AS u WHERE u.session.peerreview.contentId=?";

    private static final String GET_COUNT_USERS_FOR_SESSION = "SELECT COUNT(*) FROM " + PeerreviewUser.class.getName()
	    + " AS r WHERE r.session.sessionId=? AND r.userId!=?";

    private static final String LOAD_USERS_FOR_SESSION_LIMIT = "FROM user in class PeerreviewUser "
	    + "WHERE user.session.sessionId=:toolSessionId AND user.userId!=:excludeUserId order by ";

    @Override
    public PeerreviewUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List list = this.getHibernateTemplate().find(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0)
	    return null;
	return (PeerreviewUser) list.get(0);
    }

    @Override
    public PeerreviewUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List list = this.getHibernateTemplate().find(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0)
	    return null;
	return (PeerreviewUser) list.get(0);
    }

    @Override
    public List<PeerreviewUser> getBySessionID(Long sessionId) {
	return this.getHibernateTemplate().find(FIND_BY_SESSION_ID, sessionId);
    }
    
    @Override
    public List<Long> getUserIdsBySessionID(Long sessionId) {
	return this.getHibernateTemplate().find(GET_USERIDS_BY_SESSION_ID, sessionId);
    }

    @Override
    public List<PeerreviewUser> getByContentId(Long toolContentId) {
	return this.getHibernateTemplate().find(FIND_BY_CONTENT_ID, toolContentId);
    }

    @Override
    public int getCountUsersBySession(final Long toolSessionId, final Long excludeUserId) {

	List list = getHibernateTemplate().find(GET_COUNT_USERS_FOR_SESSION,
		new Object[] { toolSessionId, excludeUserId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    public List<PeerreviewUser> getUsersForTablesorter(final Long toolSessionId, final Long excludeUserId,
	    int page, int size, int sorting) {
	String sortingOrder = "";
	switch (sorting) {
	case PeerreviewConstants.SORT_BY_NO:
	    sortingOrder = "user.userId";
	    break;
	case PeerreviewConstants.SORT_BY_USERNAME_ASC:
	    sortingOrder = "user.firstName ASC";
	    break;
	case PeerreviewConstants.SORT_BY_USERNAME_DESC:
	    sortingOrder = "user.firstName DESC";
	    break;
	}

	return getSession().createQuery(LOAD_USERS_FOR_SESSION_LIMIT + sortingOrder)
		.setLong("toolSessionId", toolSessionId.longValue())
		.setLong("excludeUserId", excludeUserId.longValue()).setFirstResult(page * size).setMaxResults(size)
		.list();
    }

}
