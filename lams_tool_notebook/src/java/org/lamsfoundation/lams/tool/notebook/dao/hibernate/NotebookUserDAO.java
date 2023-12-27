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

package org.lamsfoundation.lams.tool.notebook.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookUserDAO;
import org.lamsfoundation.lams.tool.notebook.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the NotebookUser objects - Hibernate specific code.
 */
@Repository
public class NotebookUserDAO extends LAMSBaseDAO implements INotebookUserDAO {

    public static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + NotebookUser.class.getName() + " as f"
	    + " where user_id=? and f.notebookSession.sessionId=?";

    public static final String SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID = "from " + NotebookUser.class.getName()
	    + " as f where login_name=? and f.notebookSession.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_UID = "from " + NotebookUser.class.getName() + " where uid=?";

    @Override
    public NotebookUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
	List list = doFind(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID, new Object[] { userId, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (NotebookUser) list.get(0);
    }

    @Override
    public NotebookUser getByLoginNameAndSessionId(String loginName, Long toolSessionId) {

	List list = doFind(SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID, new Object[] { loginName, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (NotebookUser) list.get(0);

    }

    @Override
    public void saveOrUpdate(NotebookUser notebookUser) {
	getSession().saveOrUpdate(notebookUser);
	getSession().flush();
    }

    @Override
    public NotebookUser getByUID(Long uid) {
	List list = doFind(SQL_QUERY_FIND_BY_UID, new Object[] { uid });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (NotebookUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Will return List<[NotebookUser, String, Date]> where the String is the notebook entry and the modified date.
     */
    public List<Object[]> getUsersEntriesDates(final Long sessionId, Integer page, Integer size, int sorting, String searchString,
	    IUserManagementService userManagementService) {
	String sortingOrder;
	switch (sorting) {
	    case NotebookConstants.SORT_BY_USERNAME_ASC:
		sortingOrder = "user.last_name ASC, user.first_name ASC";
		break;
	    case NotebookConstants.SORT_BY_USERNAME_DESC:
		sortingOrder = "user.last_name DESC, user.first_name DESC";
		break;
	    case NotebookConstants.SORT_BY_DATE_ASC:
		sortingOrder = "user.notebook_entry_modified_date ASC";
		break;
	    case NotebookConstants.SORT_BY_DATE_DESC:
		sortingOrder = "user.notebook_entry_modified_date DESC";
		break;
	    case NotebookConstants.SORT_BY_COMMENT_ASC:
		sortingOrder = "user.teachers_comment ASC";
		break;
	    case NotebookConstants.SORT_BY_COMMENT_DESC:
		sortingOrder = "user.teachers_comment DESC";
		break;
	    default:
		sortingOrder = "user.last_name, user.first_name";
	}

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();
	queryText.append("SELECT user.* ");
	queryText.append(portraitStrings[0]);
	queryText.append(" FROM tl_lantbk11_user user ");
	queryText.append(
		" JOIN tl_lantbk11_session session ON user.notebook_session_uid = session.uid and session.session_id = :sessionId");
	queryText.append(portraitStrings[1]);

	// If filtering by name add a name based where clause
	buildNameSearch(searchString, queryText);

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	NativeQuery<Object[]> query = getSession().createNativeQuery(queryText.toString());
	query.addEntity("user", NotebookUser.class).addScalar("portraitId", StringType.INSTANCE).setParameter("sessionId", sessionId.longValue());

	if (size != null) {
	    query.setMaxResults(size);
	    if (page != null) {
		query.setFirstResult(page * size);
	    }
	}

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
    @SuppressWarnings("rawtypes")
    public int getCountUsersBySession(final Long sessionId, String searchString) {

	StringBuilder queryText = new StringBuilder("SELECT count(*) FROM tl_lantbk11_user user ");
	queryText.append(
		" JOIN tl_lantbk11_session session ON user.notebook_session_uid = session.uid and session.session_id = :sessionId");
	buildNameSearch(searchString, queryText);

	List list = getSession().createSQLQuery(queryText.toString()).setParameter("sessionId", sessionId.longValue())
		.list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @SuppressWarnings("rawtypes")
    public int getStatistics(final Long sessionId, String searchString) {

	StringBuilder queryText = new StringBuilder("SELECT count(*) FROM tl_lantbk11_user user ");
	queryText.append(
		" JOIN tl_lantbk11_session session ON user.notebook_session_uid = session.uid and session.session_id = :sessionId");
	buildNameSearch(searchString, queryText);

	List list = getSession().createSQLQuery(queryText.toString()).setParameter("sessionId", sessionId.longValue())
		.list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    private static final String GET_STATISTICS = "SELECT session.session_id sessionId, session.session_name sessionName, "
	    + " COUNT(user.uid) numLearners, SUM(user.finishedActivity) numLearnersFinished "
	    + " FROM tl_lantbk11_notebook notebook, tl_lantbk11_session session, tl_lantbk11_user user "
	    + " WHERE notebook.tool_content_id = :contentId and notebook.uid = session.notebook_uid AND user.notebook_session_uid = session.uid "
	    + " GROUP BY session.session_id";

    @Override
    @SuppressWarnings("unchecked")
    public List<StatisticDTO> getStatisticsBySession(final Long contentId) {
	NativeQuery<StatisticDTO> query = getSession().createNativeQuery(GET_STATISTICS);
	query.addScalar("sessionId", LongType.INSTANCE).addScalar("sessionName", StringType.INSTANCE)
		.addScalar("numLearners", IntegerType.INSTANCE).addScalar("numLearnersFinished", IntegerType.INSTANCE)
		.setParameter("contentId", contentId)
		.setResultTransformer(Transformers.aliasToBean(StatisticDTO.class));
	return query.list();
    }

}
