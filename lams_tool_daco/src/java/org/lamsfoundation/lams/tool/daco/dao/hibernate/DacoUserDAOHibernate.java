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
package org.lamsfoundation.lams.tool.daco.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dao.DacoUserDAO;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;

public class DacoUserDAOHibernate extends BaseDAOHibernate implements DacoUserDAO {

    private static final String FIND_BY_USER_ID_AND_CONTENT_ID = "from " + DacoUser.class.getName()
	    + " as u where u.userId =? and u.daco.contentId=?";
    private static final String FIND_BY_USER_ID_AND_SESSION_ID = "from " + DacoUser.class.getName()
	    + " as u where u.userId =? and u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "from " + DacoUser.class.getName()
	    + " as u where u.session.sessionId=?";
    private static final String FIND_BY_CONTENT_ID = "from " + DacoUser.class.getName()
	    + " as u where u.daco.contentId=?";

    @Override
    public DacoUser getUserByUserIdAndSessionId(Long userID, Long sessionId) {
	List list = this.getHibernateTemplate().find(DacoUserDAOHibernate.FIND_BY_USER_ID_AND_SESSION_ID,
		new Object[] { userID, sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (DacoUser) list.get(0);
    }

    @Override
    public DacoUser getUserByUserIdAndContentId(Long userId, Long contentId) {
	List list = this.getHibernateTemplate().find(DacoUserDAOHibernate.FIND_BY_USER_ID_AND_CONTENT_ID,
		new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (DacoUser) list.get(0);
    }

    @Override
    public List<DacoUser> getBySessionId(Long sessionId) {
	return this.getHibernateTemplate().find(DacoUserDAOHibernate.FIND_BY_SESSION_ID, sessionId);
    }

    // support sorting by name or user id, not by record number
    @Override
    public List<DacoUser> getBySessionId(Long sessionId, int sorting) {
	String sortingOrder;
	switch (sorting) {
	    case DacoConstants.SORT_BY_USER_NAME_ASC:
		sortingOrder = " ORDER BY u.lastName ASC, u.firstName ASC";
		break;
	    case DacoConstants.SORT_BY_USER_NAME_DESC:
		sortingOrder = " ORDER BY u.lastName DESC, u.firstName DESC";
		break;
	    case DacoConstants.SORT_BY_NO:
	    default:
		sortingOrder = " ORDER BY u.uid";
	}
	return this.getHibernateTemplate().find(DacoUserDAOHibernate.FIND_BY_SESSION_ID + sortingOrder, sessionId);
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Will return List<[DacoUser, Integer (record count), String (notebook entry)], [DacoUser, Integer, String], ... ,
     * [DacoUser, Integer, String]>
     * where the String is the notebook entry. No notebook entries needed? Will return "null" in their place.
     */
    public List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries, ICoreNotebookService coreNotebookService) {

	String sortingOrder;
	switch (sorting) {
	    case DacoConstants.SORT_BY_USER_NAME_ASC:
		sortingOrder = "user.last_name ASC, user.first_name ASC";
		break;
	    case DacoConstants.SORT_BY_USER_NAME_DESC:
		sortingOrder = "user.last_name DESC, user.first_name DESC";
		break;
	    case DacoConstants.SORT_BY_NUM_RECORDS_ASC:
		sortingOrder = "record_count ASC";
		break;
	    case DacoConstants.SORT_BY_NUM_RECORDS_DESC:
		sortingOrder = "record_count DESC";
		break;
	    case DacoConstants.SORT_BY_NO:
	    default:
		sortingOrder = "user.uid";
	}

	// If the session uses notebook, then get the SQL to join across to get the entries
	String[] notebookEntryStrings = null;
	if (getNotebookEntries) {
	    notebookEntryStrings = coreNotebookService.getNotebookEntrySQLStrings(sessionId.toString(),
		    DacoConstants.TOOL_SIGNATURE, "user.user_id");
	}

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();

	queryText.append("SELECT user.* ");
	queryText.append(notebookEntryStrings != null ? notebookEntryStrings[0] : ", NULL notebookEntry");
	queryText.append(", COUNT(DISTINCT(record_id)) record_count");
	queryText.append(" FROM tl_ladaco10_users user ");
	queryText.append(
		" JOIN tl_ladaco10_sessions sess on user.session_uid = sess.uid and sess.session_id = :sessionId");

	// If filtering by name add a name based where clause
	buildNameSearch(queryText, searchString);

	queryText.append(" LEFT JOIN tl_ladaco10_answers ans ON ans.user_uid = user.uid");

	// If using notebook, add the notebook join
	if (notebookEntryStrings != null) {
	    queryText.append(notebookEntryStrings[1]);
	}

	queryText.append(" GROUP BY user.uid");

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	SQLQuery query = getSession().createSQLQuery(queryText.toString());
	query.addEntity("user", DacoUser.class).addScalar("record_count", Hibernate.INTEGER)
		.addScalar("notebookEntry", Hibernate.STRING).setLong("sessionId", sessionId.longValue())
		.setFirstResult(page * size).setMaxResults(size);
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

    @Override
    @SuppressWarnings("rawtypes")
    /** Return the number of potential users in a session for the tablesorter */
    public int getCountUsersBySession(final Long sessionId, String searchString) {

	StringBuilder queryText = new StringBuilder("SELECT count(*) FROM tl_ladaco10_users user ");
	queryText.append(
		" JOIN tl_ladaco10_sessions sess on user.session_uid = sess.uid and sess.session_id = :sessionId");
	buildNameSearch(queryText, searchString);

	List list = getSession().createSQLQuery(queryText.toString()).setLong("sessionId", sessionId.longValue())
		.list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

}