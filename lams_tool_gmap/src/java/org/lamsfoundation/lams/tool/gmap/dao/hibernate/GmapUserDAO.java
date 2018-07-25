/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.tool.gmap.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapUserDAO;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;
import org.lamsfoundation.lams.tool.gmap.util.GmapConstants;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the GmapUser objects - Hibernate specific code.
 */
@Repository
public class GmapUserDAO extends LAMSBaseDAO implements IGmapUserDAO {

    public static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + GmapUser.class.getName() + " as f"
	    + " where user_id=? and f.gmapSession.sessionId=?";

    public static final String SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID = "from " + GmapUser.class.getName()
	    + " as f where login_name=? and f.gmapSession.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_UID = "from " + GmapUser.class.getName() + " where uid=?";
    
    @Override
    public GmapUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
	List list = doFind(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID, new Object[] { userId, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (GmapUser) list.get(0);
    }

    @Override
    public GmapUser getByLoginNameAndSessionId(String loginName, Long toolSessionId) {

	List list = doFind(SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID, new Object[] { loginName, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (GmapUser) list.get(0);

    }

    @Override
    public void saveOrUpdate(GmapUser gmapUser) {
	getSession().saveOrUpdate(gmapUser);
	getSession().flush();
    }

    @Override
    public GmapUser getByUID(Long uid) {
	List list = doFind(SQL_QUERY_FIND_BY_UID, new Object[] { uid });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (GmapUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Will return List<[SubmitUser, Integer1, Integer2, String], [SubmitUser, Integer1, Integer2, String], ... ,
     * [SubmitUser, Integer1, Integer2, String]>
     * where Integer1 is the number of files uploaded, Integer2 is the number of files marked
     * and String is the notebook entry. No notebook entries needed? Will return null in their place.
     */
    public List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries, ICoreNotebookService coreNotebookService,
	    IUserManagementService userManagementService) {
	String sortingOrder;
	switch (sorting) {
	    case GmapConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = "user.last_name ASC, user.first_name ASC";
		break;
	    case GmapConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = "user.last_name DESC, user.first_name DESC";
		break;
	    default:
		sortingOrder = "user.last_name, user.first_name";
	}

	// If the session uses notebook, then get the sql to join across to get the entries
	String[] notebookEntryStrings = null;
	if (getNotebookEntries) {
	    notebookEntryStrings = coreNotebookService.getNotebookEntrySQLStrings(sessionId.toString(),
		    GmapConstants.TOOL_SIGNATURE, "user.user_id");
	}

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");
	
	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();
	queryText.append("SELECT user.user_id, user.first_name, user.last_name ")
		.append(portraitStrings[0])
		.append(notebookEntryStrings != null ? notebookEntryStrings[0] : ", NULL notebookEntry")
		.append(" FROM tl_lagmap10_user user ")
		.append(portraitStrings[1])
		.append(" JOIN tl_lagmap10_session session ON user.gmap_session_uid = session.uid ");

	// If using notebook, add the notebook join
	if (notebookEntryStrings != null) {
	    queryText.append(notebookEntryStrings[1]);
	}

	queryText.append(" WHERE session.session_id = :sessionId");

	// If filtering by name add a name based where clause
	buildNameSearch(searchString, queryText);

	// finally group by user for the marker counts
	queryText.append(" GROUP BY user.uid");

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	SQLQuery query = getSession().createSQLQuery(queryText.toString());
	query.addScalar("user_id", IntegerType.INSTANCE)
		.addScalar("first_name", StringType.INSTANCE)
		.addScalar("last_name", StringType.INSTANCE)
		.addScalar("portraitId", IntegerType.INSTANCE)
		.addScalar("notebookEntry", StringType.INSTANCE)
		.setLong("sessionId", sessionId.longValue()).setFirstResult(page * size).setMaxResults(size);
	return query.list(); 
    }

    private void buildNameSearch(String searchString, StringBuilder sqlBuilder) {
	if (!StringUtils.isBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		sqlBuilder.append(" AND (user.first_name LIKE '%").append(escToken)
			.append("%' OR user.last_name LIKE '%").append(escToken).append("%' OR user.login_name LIKE '%")
			.append(escToken).append("%') ");
	    }
	}
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public int getCountUsersBySession(final Long sessionUid, String searchString) {

	StringBuilder queryText = new StringBuilder(
		"SELECT count(*) FROM tl_lagmap10_user user WHERE user.gmap_session_uid = :sessionUid ");
	buildNameSearch(searchString, queryText);

	List list = getSession().createSQLQuery(queryText.toString()).setLong("sessionUid", sessionUid).list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }
}
