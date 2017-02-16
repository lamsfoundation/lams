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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
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

    private static final String GET_COUNT_USERS_FOR_SESSION_EXCLUDE_USER = "SELECT COUNT(*) FROM "
	    + PeerreviewUser.class.getName() + " AS u WHERE u.session.sessionId=? AND u.userId!=? AND u.hidden=0";

    private static final String GET_COUNT_USERS_FOR_SESSION = "SELECT COUNT(*) FROM " + PeerreviewUser.class.getName()
	    + " AS u WHERE u.session.sessionId=?";

    @Override
    public PeerreviewUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List list = find(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (PeerreviewUser) list.get(0);
    }
    
    @Override
    public PeerreviewUser getUserByUid(Long userUid) {
	return (PeerreviewUser) this.find(PeerreviewUser.class, userUid);
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
	    + " JOIN tl_laprev11_session sess ON p.content_id = :toolContentId AND p.uid = sess.peerreview_uid AND sess.session_id = :toolSessionId  "
	    + " JOIN tl_laprev11_user user ON user.session_uid = sess.uid AND user.hidden = 0 "
	    + " LEFT JOIN ( ";
    private static final String FIND_USER_RATINGS_COMMENTS2 = " ) rating ON user.user_id = rating.item_id ";
    
    private void buildNameSearch(String searchString, StringBuilder sqlBuilder, boolean whereDone) {
	if (!StringUtils.isBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		sqlBuilder.append(whereDone ? " AND ( " : " WHERE ( ")
			.append("user.first_name LIKE '%").append(escToken)
			.append("%' OR user.last_name LIKE '%").append(escToken).append("%' OR user.login_name LIKE '%")
			.append(escToken).append("%') ");
	    }
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getRatingsComments(Long toolContentId, Long toolSessionId, RatingCriteria criteria, Long userId, Integer page,
    Integer size, int sorting, String searchString, boolean getByUser, IRatingService coreRatingService) {
	String sortingOrder = "";
	switch (sorting) {
	    case PeerreviewConstants.SORT_BY_NO:
		sortingOrder = " ORDER BY user.user_id";
		break;
	    case PeerreviewConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = " ORDER BY user.first_name ASC";
		break;
	    case PeerreviewConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = " ORDER BY user.first_name DESC";
		break;
	    case PeerreviewConstants.SORT_BY_AVERAGE_RESULT_ASC:
		if ( criteria.isCommentRating() ) {
		    sortingOrder = " ORDER BY rating.comment ASC" ;
		} else {
		    sortingOrder = " ORDER BY rating.average_rating ASC, rating.comment ASC ";
		}
		break;
	    case PeerreviewConstants.SORT_BY_AVERAGE_RESULT_DESC:
		if ( criteria.isCommentRating() ) {
		    sortingOrder = " ORDER BY rating.comment DESC" ;
		} else {
		    sortingOrder = " ORDER BY rating.average_rating DESC, rating.comment ASC ";
		}
		break;
	}

    	StringBuilder bldr =  new StringBuilder(FIND_USER_RATINGS_COMMENTS1);
    	bldr.append(coreRatingService.getRatingSelectJoinSQL(criteria.getRatingStyle(), getByUser));
    	bldr.append(FIND_USER_RATINGS_COMMENTS2);
    	if ( ! getByUser) 
    	    bldr.append("WHERE user.user_id = :userId ");
    	
    	buildNameSearch(searchString, bldr, ! getByUser);

    	bldr.append(sortingOrder);
    	
	String queryString = bldr.toString();
	Query query = getSession().createSQLQuery(queryString)
		.setLong("toolContentId", toolContentId)
		.setLong("toolSessionId", toolSessionId)
		.setLong("ratingCriteriaId", criteria.getRatingCriteriaId());
	if ( queryString.contains(":userId") ) {
		query.setLong("userId", userId);
	}
	if ( page != null && size != null ) {
	    query.setFirstResult(page * size).setMaxResults(size);
	}
	return (List<Object[]>) query.list();
    }

    // Part of this and the next method should be moved out to RatingDAO in common but not sure on usage at present so leave it here for now. The code is 
    // used in peer review monitoring and probably wouldn't be used by other tools.
    // when joining to comments, item_id = :itemId picks up the comments for star and comment styles, while item_id = rating_criteria_id
    // picks up the justification comments left for hedging (which is one comment per user for all ratings for this criteria)
    private static final String SELECT_ALL_RATINGS_COMMENTS_LEFT_FOR_ITEM = "SELECT user.user_id, rc.comment, r.rating, user.first_name, user.last_name "
	    + " FROM tl_laprev11_peerreview p "
	    + " JOIN tl_laprev11_session sess ON p.content_id = :toolContentId AND p.uid = sess.peerreview_uid AND sess.session_id = :toolSessionId "
	    + " JOIN tl_laprev11_user user ON user.session_uid = sess.uid AND user.hidden = 0 "
	    + " LEFT JOIN ( SELECT rating, user_id FROM lams_rating "
	    + "    WHERE rating_criteria_id = :ratingCriteriaId AND item_id = :itemId) r ON r.user_id = user.user_id "
	    + " LEFT JOIN ( SELECT item_id, comment, user_id FROM lams_rating_comment "
	    + "    WHERE rating_criteria_id = :ratingCriteriaId AND (item_id = :itemId || item_id = rating_criteria_id) ) rc ON rc.user_id = user.user_id " 
	    + " WHERE r.rating IS NOT NULL OR rc.comment IS NOT NULL";
    @Override
    public List<Object[]> getDetailedRatingsComments(Long toolContentId, Long toolSessionId, Long criteriaId, Long itemId ) {
	Query query = getSession().createSQLQuery(SELECT_ALL_RATINGS_COMMENTS_LEFT_FOR_ITEM)
		.setLong("toolContentId", toolContentId)
		.setLong("toolSessionId", toolSessionId)
		.setLong("ratingCriteriaId", criteriaId)
		.setLong("itemId", itemId);
	return (List<Object[]>) query.list();
    }

    private static final String COUNT_COMMENTS_FOR_SESSION = "SELECT user.user_id, rating.comment_count, user.first_name, user.last_name  "
    	    + " FROM tl_laprev11_peerreview p "
	    + " JOIN tl_laprev11_session sess ON p.content_id = :toolContentId AND p.uid = sess.peerreview_uid AND sess.session_id = :toolSessionId  "
	    + " JOIN tl_laprev11_user user ON user.session_uid = sess.uid AND user.hidden = 0 "
	    + " LEFT JOIN ( "
	    + " 	SELECT r.item_id, count(r.comment)  comment_count "
	    + " 	FROM lams_rating_comment r "
	    + "		WHERE r.rating_criteria_id = :ratingCriteriaId "
	    + " 	GROUP BY r.item_id ) rating ON user.user_id = rating.item_id ";
    @Override
    public List<Object[]> getCommentsCounts(Long toolContentId, Long toolSessionId, RatingCriteria criteria,
	    Integer page, Integer size, int sorting, String searchString) {
	String sortingOrder = "";
	switch (sorting) {
	    case PeerreviewConstants.SORT_BY_NO:
		sortingOrder = " ORDER BY user.user_id";
		break;
	    case PeerreviewConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = " ORDER BY user.first_name ASC";
		break;
	    case PeerreviewConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = " ORDER BY user.first_name DESC";
		break;
	    case PeerreviewConstants.SORT_BY_AVERAGE_RESULT_ASC:
		sortingOrder = " ORDER BY rating.comment_count ASC";
		break;
	    case PeerreviewConstants.SORT_BY_AVERAGE_RESULT_DESC:
		sortingOrder = " ORDER BY rating.comment_count DESC";
	}

    	StringBuilder bldr =  new StringBuilder(COUNT_COMMENTS_FOR_SESSION);
    	buildNameSearch(searchString, bldr, false);
    	bldr.append(sortingOrder);
    	
	String queryString = bldr.toString();
	Query query = getSession().createSQLQuery(queryString)
		.setLong("toolContentId", toolContentId)
		.setLong("toolSessionId", toolSessionId)
		.setLong("ratingCriteriaId", criteria.getRatingCriteriaId());
	if ( page != null && size != null ) {
	    query.setFirstResult(page * size).setMaxResults(size);
	}
	return (List<Object[]>) query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Will return List<[user.user_id, user.first_name, user.last_name, notebook entry, notebook date]>
     */
    public List<Object[]> getUserNotebookEntriesForTablesorter(final Long toolSessionId, int page, int size, int sorting,
	    String searchString, ICoreNotebookService coreNotebookService) {

	String sortingOrder;
	switch (sorting) {
	    case PeerreviewConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = " ORDER BY user.first_name ASC";
		break;
	    case PeerreviewConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = " ORDER BY user.first_name DESC";
		break;
	    case PeerreviewConstants.SORT_BY_NOTEBOOK_ENTRY_ASC:
		sortingOrder = " ORDER BY notebookEntry ASC";
		break;
	    case PeerreviewConstants.SORT_BY_NOTEBOOK_ENTRY_DESC:
		sortingOrder = " ORDER BY notebookEntry DESC";
		break;
	    case PeerreviewConstants.SORT_BY_NO:
	    default:
		sortingOrder = " ORDER BY user.user_id";
	}

	String[] notebookEntryStrings = coreNotebookService.getNotebookEntrySQLStrings(toolSessionId.toString(),
		    PeerreviewConstants.TOOL_SIGNATURE, "user.user_id", true);
	
	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();

	queryText.append("SELECT user.user_id, user.first_name, user.last_name ")
		.append(notebookEntryStrings[0])
		.append(" FROM tl_laprev11_user user ")
		.append(" JOIN tl_laprev11_session session ON session.session_id = :toolSessionId AND user.session_uid = session.uid");

	queryText.append(notebookEntryStrings[1]);

    	buildNameSearch(searchString, queryText, false);

	// Now specify the sort based on the switch statement above.
	queryText.append(sortingOrder);

	SQLQuery query = getSession().createSQLQuery(queryText.toString());
	query.addScalar("user_id", IntegerType.INSTANCE)
		.addScalar("first_name", StringType.INSTANCE)
		.addScalar("last_name", StringType.INSTANCE)
		.addScalar("notebookEntry", StringType.INSTANCE)
		.addScalar("notebookModifiedDate", TimestampType.INSTANCE)
		.setLong("toolSessionId", toolSessionId.longValue())
		.setFirstResult(page * size).setMaxResults(size);
	return query.list();

    }
    
    @Override
    public List<Object[]> getPagedUsers(Long toolSessionId, Integer page, Integer size, int sorting,
	    String searchString) {

	String GET_USERS_FOR_SESSION = "SELECT user.uid, user.hidden, CONCAT(user.firstName, ' ', user.lastName) FROM "
		+ PeerreviewUser.class.getName() + " user WHERE user.session.sessionId = :toolSessionId ";
	
	String sortingOrder = "";
	switch (sorting) {
	    case PeerreviewConstants.SORT_BY_NO:
		sortingOrder = " ORDER BY user.uid";
		break;
	    case PeerreviewConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = " ORDER BY user.firstName ASC";
		break;
	    case PeerreviewConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = " ORDER BY user.firstName DESC";
		break;
	}

    	StringBuilder bldr =  new StringBuilder(GET_USERS_FOR_SESSION);
	if (!StringUtils.isBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		bldr.append(" AND ( ").append("user.firstName LIKE '%").append(escToken)
			.append("%' OR user.lastName LIKE '%").append(escToken).append("%' OR user.loginName LIKE '%")
			.append(escToken).append("%') ");
	    }
	}
    	bldr.append(sortingOrder);
    	
	String queryString = bldr.toString();
	Query query = getSession().createQuery(queryString)
		.setLong("toolSessionId", toolSessionId);
	if ( page != null && size != null ) {
	    query.setFirstResult(page * size).setMaxResults(size);
	}
	return (List<Object[]>) query.list();
    }
}
