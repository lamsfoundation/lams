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

package org.lamsfoundation.lams.tool.spreadsheet.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StringType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetUserDAO;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.stereotype.Repository;

@Repository
public class SpreadsheetUserDAOHibernate extends LAMSBaseDAO implements SpreadsheetUserDAO {

    private static final String FIND_BY_USER_ID_CONTENT_ID = "from " + SpreadsheetUser.class.getName()
	    + " as u where u.userId =? and u.spreadsheet.contentId=?";
    private static final String FIND_BY_USER_ID_SESSION_ID = "from " + SpreadsheetUser.class.getName()
	    + " as u where u.userId =? and u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "from " + SpreadsheetUser.class.getName()
	    + " as u where u.session.sessionId=?";

    @Override
    public SpreadsheetUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List<?> list = this.doFind(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (SpreadsheetUser) list.get(0);
    }

    @Override
    public SpreadsheetUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List<?> list = this.doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (SpreadsheetUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SpreadsheetUser> getBySessionID(Long sessionId) {
	return (List<SpreadsheetUser>) this.doFind(FIND_BY_SESSION_ID, sessionId);
    }

    @Override
    /**
     * Will return List<[SpreadsheetUser, String], [SpreadsheetUser, String], ... , [SpreadsheetUser, String]>
     * where the String is the notebook entry. No notebook entries needed? Will return in their place.
     */
    public List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries, ICoreNotebookService coreNotebookService,
	    IUserManagementService userManagementService) {
	String sortingOrder;
	switch (sorting) {
	    case SpreadsheetConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = "user.last_name ASC, user.first_name ASC";
		break;
	    case SpreadsheetConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = "user.last_name DESC, user.first_name DESC";
		break;
	    case SpreadsheetConstants.SORT_BY_MARKED_ASC:
		sortingOrder = " mark.marks ASC";
		break;
	    case SpreadsheetConstants.SORT_BY_MARKED_DESC:
		sortingOrder = " mark.marks DESC";
		break;
	    default:
		sortingOrder = "user.last_name, user.first_name";
	}

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	// If the session uses notebook, then get the sql to join across to get the entries
	String[] notebookEntryStrings = null;
	if (getNotebookEntries) {
	    notebookEntryStrings = coreNotebookService.getNotebookEntrySQLStrings(sessionId.toString(),
		    SpreadsheetConstants.TOOL_SIGNATURE, "user.user_id");
	}

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();
	queryText.append("SELECT user.* ");
	queryText.append(notebookEntryStrings != null ? notebookEntryStrings[0] : ", NULL notebookEntry");
	queryText.append(portraitStrings[0]);
	queryText.append(" FROM tl_lasprd10_user user ");
	queryText.append(
		" JOIN tl_lasprd10_session session ON user.session_uid = session.uid and session.session_id = :sessionId");
	queryText.append(portraitStrings[1]);

	// If sorting by mark then join to mark
	if (sorting == SpreadsheetConstants.SORT_BY_MARKED_ASC || sorting == SpreadsheetConstants.SORT_BY_MARKED_DESC) {
	    queryText.append(
		    " LEFT JOIN tl_lasprd10_user_modified_spreadsheet ms on user.user_modified_spreadsheet_uid=ms.uid ");
	    queryText.append(" LEFT JOIN tl_lasprd10_spreadsheet_mark mark on ms.mark_id = mark.uid ");
	}

	// If using notebook, add the notebook join
	if (notebookEntryStrings != null) {
	    queryText.append(notebookEntryStrings[1]);
	}

	// If filtering by name add a name based where clause
	buildNameSearch(searchString, queryText);

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	@SuppressWarnings("unchecked")
	NativeQuery<Object[]> query = getSession().createNativeQuery(queryText.toString());
	query.addEntity("user", SpreadsheetUser.class).addScalar("notebookEntry", StringType.INSTANCE)
		.addScalar("portraitId", StringType.INSTANCE).setParameter("sessionId", sessionId)
		.setFirstResult(page * size).setMaxResults(size);
	return query.list();
    }

    private void buildNameSearch(String searchString, StringBuilder sqlBuilder) {
	if (!StringUtils.isBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		sqlBuilder.append(" WHERE (user.first_name LIKE '%").append(escToken)
			.append("%' OR user.last_name LIKE '%").append(escToken).append("%' OR user.login_name LIKE '%")
			.append(escToken).append("%') ");
	    }
	}
    }

    @Override
    public int getCountUsersBySession(final Long sessionId, String searchString) {
	StringBuilder queryText = new StringBuilder("SELECT count(*) FROM tl_lasprd10_user user ");
	queryText.append(
		" JOIN tl_lasprd10_session session ON user.session_uid = session.uid and session.session_id = :sessionId");
	buildNameSearch(searchString, queryText);

	@SuppressWarnings("rawtypes")
	List list = getSession().createNativeQuery(queryText.toString()).setParameter("sessionId", sessionId).list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

}
