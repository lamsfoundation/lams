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

/* $$Id$$ */

package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ForumUserDao extends HibernateDaoSupport {

    private static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + ForumUser.class.getName() + " as f"
	    + " where user_id=? and f.session.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_USER_ID = "from " + ForumUser.class.getName() + " as f"
	    + " where user_id=? and session_id is null";

    private static final String SQL_QUERY_FIND_BY_SESSION_ID = "from " + ForumUser.class.getName() + " as f "
	    + " where f.session.sessionId=?";

    public List<ForumUser> getBySessionId(Long sessionID) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_SESSION_ID, sessionID);
    }

    public void save(ForumUser forumUser) {
	this.getHibernateTemplate().save(forumUser);
    }

    public ForumUser getByUserIdAndSessionId(Long userId, Long sessionId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID,
		new Object[] { userId, sessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (ForumUser) list.get(0);
    }

    public ForumUser getByUserId(Long userId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID, userId);

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (ForumUser) list.get(0);
    }

    public ForumUser getByUid(Long userUid) {
	return (ForumUser) this.getHibernateTemplate().get(ForumUser.class, userUid);
    }

    @SuppressWarnings("unchecked")
    /**
     * Will return List<[ForumUser, String], [ForumUser, String], ... , [ForumUser, String]>
     * where the String is the notebook entry. No notebook entries needed? Will return "null" in their place.
     */
    public List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries, ICoreNotebookService coreNotebookService) {
	String sortingOrder;
	boolean sortOnMessage;
	switch (sorting) {
	    case ForumConstants.SORT_BY_USER_NAME_ASC:
		sortingOrder = "user.last_name ASC, user.first_name ASC";
		sortOnMessage = false;
		break;
	    case ForumConstants.SORT_BY_USER_NAME_DESC:
		sortingOrder = "user.last_name DESC, user.first_name DESC";
		sortOnMessage = false;
		break;
	    case ForumConstants.SORT_BY_LAST_POSTING_ASC:
		sortingOrder = " MAX(message.update_date) ASC";
		sortOnMessage = true;
		break;
	    case ForumConstants.SORT_BY_LAST_POSTING_DESC:
		sortingOrder = " MAX(message.update_date) DESC";
		sortOnMessage = true;
		break;
	    case ForumConstants.SORT_BY_NUMBER_OF_POSTS_ASC:
		sortingOrder = " COUNT(message.uid) ASC";
		sortOnMessage = true;
		break;
	    case ForumConstants.SORT_BY_NUMBER_OF_POSTS_DESC:
		sortingOrder = " COUNT(message.uid) DESC";
		sortOnMessage = true;
		break;
	    case ForumConstants.SORT_BY_MARKED_ASC:
		sortingOrder = " AVG(report.mark) ASC";
		sortOnMessage = true;
		break;
	    case ForumConstants.SORT_BY_MARKED_DESC:
		sortingOrder = " AVG(report.mark) DESC";
		sortOnMessage = true;
		break;
	    case ForumConstants.SORT_BY_NO:
	    default:
		sortingOrder = "user.uid";
		sortOnMessage = false;
	}

	// If the session uses notebook, then get the SQL to join across to get the entries
	String[] notebookEntryStrings = null;
	if (getNotebookEntries) {
	    notebookEntryStrings = coreNotebookService.getNotebookEntrySQLStrings(sessionId.toString(),
		    ForumConstants.TOOL_SIGNATURE, "user.user_id");
	}

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();

	queryText.append("SELECT user.* ");
	queryText.append(notebookEntryStrings != null ? notebookEntryStrings[0] : ", NULL notebookEntry");
	queryText.append(" FROM tl_lafrum11_forum_user user ");
	queryText.append(
		" JOIN tl_lafrum11_tool_session session ON user.session_id = session.uid and session.session_id = :sessionId");

	if (sortOnMessage) {
	    queryText.append(" LEFT JOIN tl_lafrum11_message message ON message.create_by = user.uid");
	    if (sorting == ForumConstants.SORT_BY_MARKED_ASC || sorting == ForumConstants.SORT_BY_MARKED_DESC) {
		queryText.append(" LEFT JOIN tl_lafrum11_report report ON report.uid = message.report_id");
	    }
	}

	// If using notebook, add the notebook join
	if (notebookEntryStrings != null) {
	    queryText.append(notebookEntryStrings[1]);
	}

	// If filtering by name add a name based where clause
	buildNameSearch(queryText, searchString);

	if (sortOnMessage) {
	    queryText.append(" GROUP BY user.user_id");
	}

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	SQLQuery query = getSession().createSQLQuery(queryText.toString());
	query.addEntity("user", ForumUser.class).addScalar("notebookEntry", Hibernate.STRING)
		.setLong("sessionId", sessionId.longValue()).setFirstResult(page * size).setMaxResults(size);
	return query.list();

    }

    private void buildNameSearch(StringBuilder queryText, String searchString) {
	if (!StringUtils.isBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		queryText.append(" AND (user.first_name LIKE '%").append(escToken)
			.append("%' OR user.last_name LIKE '%").append(escToken).append("%' OR user.login_name LIKE '%")
			.append(escToken).append("%')");
	    }
	}
    }

    @SuppressWarnings("rawtypes")
    public int getCountUsersBySession(final Long sessionId, String searchString) {

	StringBuilder queryText = new StringBuilder("SELECT count(*) FROM tl_lafrum11_forum_user user ");
	queryText.append(
		" JOIN tl_lafrum11_tool_session session ON user.session_id = session.uid and session.session_id = :sessionId");
	buildNameSearch(queryText, searchString);

	List list = getSession().createSQLQuery(queryText.toString()).setLong("sessionId", sessionId.longValue())
		.list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    public void delete(ForumUser user) {
	this.getHibernateTemplate().delete(user);
    }

}
