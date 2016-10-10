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

package org.lamsfoundation.lams.tool.peerreview.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.dao.PeerreviewUserDAO;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;

public class PeerreviewUserDAOHibernate extends LAMSBaseDAO implements PeerreviewUserDAO {

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

    private static final String GET_COUNT_USERS_FOR_SESSION_EXCLUDE_USER = "SELECT COUNT(*) FROM "
	    + PeerreviewUser.class.getName() + " AS r WHERE r.session.sessionId=? AND r.userId!=?";

    private static final String GET_COUNT_USERS_FOR_SESSION = "SELECT COUNT(*) FROM " + PeerreviewUser.class.getName()
	    + " AS r WHERE r.session.sessionId=?";

    private static final String LOAD_USERS_FOR_SESSION_LIMIT_OLD = "FROM user in class PeerreviewUser "
	    + "WHERE user.session.sessionId=:toolSessionId AND user.userId!=:excludeUserId order by ";

    @Override
    public PeerreviewUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List list = find(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (PeerreviewUser) list.get(0);
    }

    @Override
    public PeerreviewUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List list = find(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (PeerreviewUser) list.get(0);
    }

    @Override
    public List<PeerreviewUser> getBySessionID(Long sessionId) {
	return find(FIND_BY_SESSION_ID, sessionId);
    }

    @Override
    public List<Long> getUserIdsBySessionID(Long sessionId) {
	return find(GET_USERIDS_BY_SESSION_ID, sessionId);
    }

    @Override
    public List<PeerreviewUser> getByContentId(Long toolContentId) {
	return find(FIND_BY_CONTENT_ID, toolContentId);
    }

    @Override
    public int getCountUsersBySession(final Long toolSessionId, final Long excludeUserId) {

	List list = find(GET_COUNT_USERS_FOR_SESSION_EXCLUDE_USER, new Object[] { toolSessionId, excludeUserId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    public int getCountUsersBySession(final Long toolSessionId) {

	List list = find(GET_COUNT_USERS_FOR_SESSION, new Object[] { toolSessionId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    public List<PeerreviewUser> getUsersForTablesorter(final Long toolSessionId, final Long excludeUserId, int page,
	    int size, int sorting) {
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

	return getSession().createQuery(LOAD_USERS_FOR_SESSION_LIMIT_OLD + sortingOrder)
		.setLong("toolSessionId", toolSessionId.longValue()).setLong("excludeUserId", excludeUserId.longValue())
		.setFirstResult(page * size).setMaxResults(size).list();
    }

    private static final String LOAD_USERS_FOR_SESSION_LIMIT = "FROM user in class PeerreviewUser "
	    + "WHERE user.session.sessionId=:toolSessionId AND user.userId!=:excludeUserId order by ";

    private static final String CREATE_USERS = "INSERT into tl_laprev11_user (user_id, login_name, first_name, last_name, session_finished, session_uid) "
	    + " SELECT user.user_id, user.login, user.first_name, user.last_name, 0, :session_uid "
	    + " FROM lams_user user " + " JOIN lams_user_group ug ON ug.user_id = user.user_id "
	    + " JOIN lams_group grp ON grp.group_id = ug.group_id "
	    + " JOIN lams_tool_session session ON session.group_id = grp.group_id "
	    + " WHERE session.tool_session_id = :tool_session_id  " + " AND NOT EXISTS " + " ( " + " SELECT pu.user_id "
	    + " FROM tl_laprev11_user pu " + " WHERE pu.session_uid = :session_uid AND pu.user_id = user.user_id "
	    + " )";

    @Override
    public int createUsersForSession(final PeerreviewSession session) {

	SQLQuery query = getSession().createSQLQuery(CREATE_USERS);
	query.setLong("session_uid", session.getUid()).setLong("tool_session_id", session.getSessionId());
	return query.executeUpdate();
    }
    
    // column order is very important. The potential itemId must be first, followed by rating.*,
    // and the user's first name and last name must be the last two columns or the DTO conversion will fail.
    // See PeerreviewServiceImpl.getUsersRatingsCommentsByCriteriaId
    private static final String FIND_USER_RATINGS_COMMENTS1 = "SELECT user.user_id, rating.*, user.first_name, user.last_name "
	    + " FROM tl_laprev11_peerreview p "
	    + " JOIN tl_laprev11_session sess ON p.content_id = :toolContentId AND p.uid = sess.peerreview_uid  "
	    + " JOIN tl_laprev11_user user ON user.session_uid = sess.uid "
	    + " LEFT JOIN ( ";
    private static final String FIND_USER_RATINGS_COMMENTS2 = " ) rating ON user.user_id = rating.item_id ";
    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getRatingsComments(Long toolContentId, RatingCriteria criteria, Long userId, Integer page,
    Integer size, int sorting, boolean getByUser, IRatingService coreRatingService) {
	String sortingOrder = "";
	switch (sorting) {
	    case PeerreviewConstants.SORT_BY_NO:
		sortingOrder = "ORDER BY user.user_id";
		break;
	    case PeerreviewConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = "ORDER BY user.first_name ASC";
		break;
	    case PeerreviewConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = "ORDER BY user.first_name DESC";
		break;
	    case PeerreviewConstants.SORT_BY_AVERAGE_RESULT_ASC:
		sortingOrder = criteria.isCommentRating() ? "ORDER BY rating.comment ASC" : "ORDER BY rating.average_rating ASC";
		break;
	    case PeerreviewConstants.SORT_BY_AVERAGE_RESULT_DESC:
		sortingOrder = criteria.isCommentRating() ? "ORDER BY rating.comment DESC" : "ORDER BY rating.average_rating DESC";
	}

    	StringBuilder bldr =  new StringBuilder(FIND_USER_RATINGS_COMMENTS1);
    	bldr.append(coreRatingService.getRatingSelectJoinSQL(criteria.getRatingStyle(), getByUser));
    	bldr.append(FIND_USER_RATINGS_COMMENTS2);
    	if ( ! getByUser) 
    	    bldr.append("WHERE user.user_id = :userId ");
    	
    	bldr.append(sortingOrder);
    	
	String queryString = bldr.toString();
	Query query = getSession().createSQLQuery(queryString)
		.setLong("toolContentId", toolContentId)
		.setLong("ratingCriteriaId", criteria.getRatingCriteriaId());
	if ( queryString.contains(":userId") ) {
		query.setLong("userId", userId);
	}
	if ( page != null && size != null ) {
	    query.setFirstResult(page * size).setMaxResults(size);
	}
	return (List<Object[]>) query.list();
    }

}
