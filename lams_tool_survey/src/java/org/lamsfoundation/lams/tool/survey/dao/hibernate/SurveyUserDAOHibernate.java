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
package org.lamsfoundation.lams.tool.survey.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dao.SurveyUserDAO;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;

public class SurveyUserDAOHibernate extends BaseDAOHibernate implements SurveyUserDAO {

    private static final String FIND_BY_USER_ID_CONTENT_ID = "FROM " + SurveyUser.class.getName()
	    + " AS u WHERE u.userId =? AND u.survey.contentId=?";
    private static final String FIND_BY_USER_ID_SESSION_ID = "FROM " + SurveyUser.class.getName()
	    + " AS u WHERE u.userId =? AND u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "FROM " + SurveyUser.class.getName()
	    + " AS u WHERE u.session.sessionId=?";
    private static final String GET_COUNT_FINISHED_USERS_FOR_SESSION = "SELECT COUNT(*) FROM "
	    + SurveyUser.class.getName()
	    + " AS u WHERE u.session.sessionId=? AND (u.sessionFinished is true OR u.responseFinalized is true)";

    public SurveyUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List list = this.getHibernateTemplate().find(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0)
	    return null;
	return (SurveyUser) list.get(0);
    }

    public SurveyUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List list = this.getHibernateTemplate().find(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0)
	    return null;
	return (SurveyUser) list.get(0);
    }

    public List<SurveyUser> getBySessionID(Long sessionId) {
	return this.getHibernateTemplate().find(FIND_BY_SESSION_ID, sessionId);
    }

    public int getCountFinishedUsers(Long sessionId) {
	List list = getHibernateTemplate().find(GET_COUNT_FINISHED_USERS_FOR_SESSION, new Object[] { sessionId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    private static final String FIND_USER_ANSWERS_BY_SESSION_ID = "SELECT user.*, answer.answer_choices answerChoices , answer.answer_text answerText"
	    + " FROM tl_lasurv11_user user "
	    + " JOIN tl_lasurv11_session session ON user.session_uid = session.uid and session.session_id = :sessionId "
	    + " LEFT JOIN tl_lasurv11_answer answer ON user.uid = answer.user_uid and answer.question_uid = :questionId ";

    @SuppressWarnings("unchecked")
    /** Will return List<[SurveyUser, String, String], [SurveyUser, String, String], ... , [SurveyUser, String, String]>
     * where the first String is answer choices (for multiple choice) and the second String is the answer text for
     * free entry choice.
     */
    public List<Object[]> getUsersForTablesorter(final Long sessionId, final Long questionId, int page, int size,
	    int sorting, String searchString) {
	String sortingOrder;
	switch (sorting) {
	case SurveyConstants.SORT_BY_NAME_ASC:
	    sortingOrder = "user.last_name ASC, user.first_name ASC";
	    break;
	case SurveyConstants.SORT_BY_NAME_DESC:
	    sortingOrder = "user.last_name DESC, user.first_name DESC";
	    break;
	default:
	    sortingOrder = "user.uid";
	}

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder(FIND_USER_ANSWERS_BY_SESSION_ID);

	// If filtering by name add a name based where clause
	buildNameSearch(searchString, queryText);

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	SQLQuery query = getSession().createSQLQuery(queryText.toString());
	query.addEntity("user", SurveyUser.class)
		.addScalar("answerChoices", Hibernate.STRING)
		.addScalar("answerText", Hibernate.STRING)
		.setLong("sessionId", sessionId.longValue())
		.setLong("questionId", questionId.longValue())
		.setFirstResult(page * size).setMaxResults(size);
	return query.list();
    }

    private void buildNameSearch(String searchString, StringBuilder sqlBuilder) {
	if (!StringUtils.isBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		sqlBuilder.append(" WHERE (user.first_name LIKE '%").append(escToken)
			.append("%' OR user.last_name LIKE '%").append(escToken)
			.append("%' OR user.login_name LIKE '%").append(escToken).append("%') ");
	    }
	}
    }

    @SuppressWarnings("rawtypes")
    public int getCountUsersBySession(final Long sessionId, String searchString) {

	StringBuilder queryText = new StringBuilder("SELECT count(*) FROM tl_lasurv11_user user ");
	queryText
		.append(" JOIN tl_lasurv11_session session ON user.session_uid = session.uid and session.session_id = :sessionId");
	buildNameSearch(searchString, queryText);

	List list = getSession().createSQLQuery(queryText.toString()).setLong("sessionId", sessionId.longValue())
		.list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

}
