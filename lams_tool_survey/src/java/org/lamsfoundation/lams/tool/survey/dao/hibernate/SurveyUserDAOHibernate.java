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

package org.lamsfoundation.lams.tool.survey.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dao.SurveyUserDAO;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.stereotype.Repository;

@Repository
public class SurveyUserDAOHibernate extends LAMSBaseDAO implements SurveyUserDAO {

    private static final String FIND_BY_USER_ID_CONTENT_ID = "FROM " + SurveyUser.class.getName()
	    + " AS u WHERE u.userId =? AND u.survey.contentId=?";
    private static final String FIND_BY_USER_ID_SESSION_ID = "FROM " + SurveyUser.class.getName()
	    + " AS u WHERE u.userId =? AND u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "FROM " + SurveyUser.class.getName()
	    + " AS u WHERE u.session.sessionId=?";
    private static final String GET_COUNT_FINISHED_USERS_FOR_SESSION = "SELECT COUNT(*) FROM "
	    + SurveyUser.class.getName()
	    + " AS u WHERE u.session.sessionId=? AND (u.sessionFinished is true OR u.responseFinalized is true)";

    @Override
    public SurveyUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List<?> list = this.doFind(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (SurveyUser) list.get(0);
    }

    @Override
    public SurveyUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List<?> list = this.doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (SurveyUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SurveyUser> getBySessionID(Long sessionId) {
	return (List<SurveyUser>) this.doFind(FIND_BY_SESSION_ID, sessionId);
    }

    @Override
    public int getCountFinishedUsers(Long sessionId) {
	List<?> list = doFind(GET_COUNT_FINISHED_USERS_FOR_SESSION, new Object[] { sessionId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    private static final String FIND_USER_ANSWERS_BY_SESSION_ID_SELECT = "SELECT user.*, answer.answer_choices answerChoices , answer.answer_text answerText";
    private static final String FIND_USER_ANSWERS_BY_SESSION_ID_FROM = " FROM tl_lasurv11_user user "
	    + " JOIN tl_lasurv11_session session ON user.session_uid = session.uid and session.session_id = :sessionId "
	    + " LEFT JOIN tl_lasurv11_answer answer ON user.uid = answer.user_uid and answer.question_uid = :questionId ";

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Will return List<[SurveyUser, String, String], [SurveyUser, String, String], ... , [SurveyUser, String, String]>
     * where the first String is answer choices (for multiple choice) and the second String is the answer text for
     * free entry choice.
     */
    public List<Object[]> getUsersForTablesorter(final Long sessionId, final Long questionId, int page, int size,
	    int sorting, String searchString, IUserManagementService userManagementService) {
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

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder(FIND_USER_ANSWERS_BY_SESSION_ID_SELECT).append(portraitStrings[0])
		.append(FIND_USER_ANSWERS_BY_SESSION_ID_FROM).append(portraitStrings[1]);

	// If filtering by name add a name based where clause
	buildNameSearch(searchString, queryText);

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	NativeQuery<Object[]> query = getSession().createNativeQuery(queryText.toString());
	query.addEntity("user", SurveyUser.class).addScalar("answerChoices", StringType.INSTANCE)
		.addScalar("answerText", StringType.INSTANCE).addScalar("portraitId", StringType.INSTANCE)
		.setParameter("sessionId", sessionId.longValue()).setParameter("questionId", questionId.longValue())
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
    @SuppressWarnings("rawtypes")
    public int getCountUsersBySession(final Long sessionId, String searchString) {

	StringBuilder queryText = new StringBuilder("SELECT count(*) FROM tl_lasurv11_user user ");
	queryText.append(
		" JOIN tl_lasurv11_session session ON user.session_uid = session.uid and session.session_id = :sessionId");
	buildNameSearch(searchString, queryText);

	List list = getSession().createNativeQuery(queryText.toString())
		.setParameter("sessionId", sessionId.longValue()).list();
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    /**
     * Will return List<[SurveyUser, String (notebook entry)], [SurveyUser, String (notebook entry)], ... , [SurveyUser,
     * String (notebook entry)]>
     */
    public List<Object[]> getUserReflectionsForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, ICoreNotebookService coreNotebookService,
	    IUserManagementService userManagementService) {
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

	// If the session uses notebook, then get the sql to join across to get the entries
	String[] notebookEntryStrings = coreNotebookService.getNotebookEntrySQLStrings(sessionId.toString(),
		SurveyConstants.TOOL_SIGNATURE, "user.user_id");

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	// Basic select for the user records
	StringBuilder queryText = new StringBuilder();
	queryText.append("SELECT user.* ");
	queryText.append(notebookEntryStrings[0]);
	queryText.append(portraitStrings[0]);
	queryText.append(" FROM tl_lasurv11_user user ");
	queryText.append(
		" JOIN tl_lasurv11_session session ON user.session_uid = session.uid and session.session_id = :sessionId ");

	// Add the notebook & portrait join
	queryText.append(notebookEntryStrings[1]);
	queryText.append(portraitStrings[1]);

	// If filtering by name add a name based where clause
	buildNameSearch(searchString, queryText);

	// Now specify the sort based on the switch statement above.
	queryText.append(" ORDER BY " + sortingOrder);

	NativeQuery<Object[]> query = getSession().createNativeQuery(queryText.toString());
	query.addEntity("user", SurveyUser.class).addScalar("notebookEntry", StringType.INSTANCE)
		.addScalar("portraitId", StringType.INSTANCE).setParameter("sessionId", sessionId.longValue())
		.setFirstResult(page * size).setMaxResults(size);
	return query.list();
    }

    private static final String GET_STATISTICS = "SELECT session.*, COUNT(*) numUsers "
	    + "  FROM tl_lasurv11_session session, tl_lasurv11_survey survey, tl_lasurv11_user user "
	    + "  WHERE survey.content_id = :contentId and session.survey_uid = survey.uid  and user.session_uid = session.uid "
	    + "  GROUP BY session.uid";

    @Override
    @SuppressWarnings("unchecked")
    /** Returns < [surveySession, numUsers] ... [surveySession, numUsers]> */
    public List<Object[]> getStatisticsBySession(final Long contentId) {

	NativeQuery<Object[]> query = getSession().createNativeQuery(GET_STATISTICS);
	query.addEntity(SurveySession.class).addScalar("numUsers", IntegerType.INSTANCE).setParameter("contentId",
		contentId);
	return query.list();
    }

}
