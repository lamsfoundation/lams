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

package org.lamsfoundation.lams.tool.assessment.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentUserDAO;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentUserDAOHibernate extends LAMSBaseDAO implements AssessmentUserDAO {

    private static final String FIND_BY_USER_ID_CONTENT_ID = "from " + AssessmentUser.class.getName()
	    + " as u where u.userId =? and u.assessment.contentId=?";
    private static final String FIND_BY_USER_ID_SESSION_ID = "from " + AssessmentUser.class.getName()
	    + " as u where u.userId =? and u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "from " + AssessmentUser.class.getName()
	    + " as u where u.session.sessionId=?";

    private static final String LOAD_MARKS_FOR_SESSION = "SELECT grade FROM tl_laasse10_assessment_result "
	    + " WHERE finish_date IS NOT NULL AND latest = 1 AND session_id = :sessionId";
    private static final String FIND_MARK_STATS_FOR_SESSION = "SELECT MIN(grade) min_grade, AVG(grade) avg_grade, MAX(grade) max_grade FROM tl_laasse10_assessment_result "
	    + " WHERE finish_date IS NOT NULL AND latest = 1 AND session_id = :sessionId";


    @Override
    public AssessmentUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List list = doFind(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (AssessmentUser) list.get(0);
    }

    @Override
    public AssessmentUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List list = doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (AssessmentUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AssessmentUser> getBySessionID(Long sessionId) {
	return (List<AssessmentUser>) this.doFind(FIND_BY_SESSION_ID, sessionId);
    }

    private static String LOAD_USERS_ORDERED_BY_SESSION = "SELECT DISTINCT user.user_id, user.last_name, user.first_name, user.login_name, result.grade"
		+ " FROM tl_laasse10_user user  INNER JOIN tl_laasse10_session session"
		+ " ON user.session_uid=session.uid  LEFT OUTER JOIN tl_laasse10_assessment_result result "
		+ " ON result.user_uid = user.uid  	AND result.finish_date IS NOT NULL"
		+ " 	AND result.latest = 1  WHERE session.session_id = :sessionId "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) ";
    private static String LOAD_USERS_ORDERED_ORDER_BY_NAME = "ORDER BY (CONCAT(user.last_name, ' ', user.first_name)) ";
    private static String LOAD_USERS_ORDERED_ORDER_BY_TOTAL = "ORDER BY result.grade ";

    @Override
    public List<AssessmentUserDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {

	StringBuilder bldr = new StringBuilder(LOAD_USERS_ORDERED_BY_SESSION);
	if ( "total".equalsIgnoreCase(sortBy) )
	    bldr.append(LOAD_USERS_ORDERED_ORDER_BY_TOTAL);
	else
	    bldr.append(LOAD_USERS_ORDERED_ORDER_BY_NAME);
	bldr.append(sortOrder);
	
	SQLQuery query = getSession().createSQLQuery(bldr.toString());
	query.setLong("sessionId", sessionId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	ArrayList<AssessmentUserDTO> userDtos = new ArrayList<AssessmentUserDTO>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userId = ((Number) element[0]).longValue();
		String firstName = (String) element[1];
		String lastName = (String) element[2];
		String login = (String) element[3];
		float grade = element[4] == null ? 0 : ((Number) element[4]).floatValue();

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setUserId(userId);
		userDto.setFirstName(firstName);
		userDto.setLastName(lastName);
		userDto.setLogin(login);
		userDto.setGrade(grade);
		userDtos.add(userDto);
	    }

	}

	return userDtos;
    }

    @Override
    public int getCountUsersBySession(Long sessionId, String searchString) {

	String LOAD_USERS_ORDERED_BY_NAME = "SELECT COUNT(*) FROM " + AssessmentUser.class.getName() + " user"
		+ " WHERE user.session.sessionId = :sessionId "
		+ " AND (CONCAT(user.lastName, ' ', user.firstName) LIKE CONCAT('%', :searchString, '%')) ";

	Query query = getSession().createQuery(LOAD_USERS_ORDERED_BY_NAME);
	query.setLong("sessionId", sessionId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	List list = query.list();

	if ((list == null) || (list.size() == 0)) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] getStatsMarksBySession(Long sessionId) {

	Query query = getSession().createSQLQuery(FIND_MARK_STATS_FOR_SESSION)
		.addScalar("min_grade", FloatType.INSTANCE)
		.addScalar("avg_grade", FloatType.INSTANCE)
		.addScalar("max_grade", FloatType.INSTANCE);
	query.setLong("sessionId", sessionId);
	List list = query.list();
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (Object[]) list.get(0);
	}
    }

    private static String LOAD_USERS_ORDERED_BY_SESSION_QUESTION = "SELECT DISTINCT question_result.uid, user.last_name, user.first_name, user.login_name, question_result.mark"
		+ " FROM tl_laasse10_user user" + " INNER JOIN tl_laasse10_session session"
		+ " ON user.session_uid=session.uid" +

		" LEFT OUTER JOIN tl_laasse10_assessment_result result " + " ON result.user_uid = user.uid"
		+ " 	AND result.finish_date IS NOT NULL" + " 	AND result.latest = 1" +

		" INNER JOIN tl_laasse10_question_result question_result " + " ON result.uid=question_result.result_uid"
		+ " 	AND question_result.assessment_question_uid = :questionUid" +

		" WHERE session.session_id = :sessionId "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) ";
    private static String LOAD_USERS_ORDERED_ORDER_BY_RESULT = "ORDER BY question_result.mark ";

    @Override
    public List<AssessmentUserDTO> getPagedUsersBySessionAndQuestion(Long sessionId, Long questionUid, int page,
	    int size, String sortBy, String sortOrder, String searchString) {

	StringBuilder bldr = new StringBuilder(LOAD_USERS_ORDERED_BY_SESSION_QUESTION);
	if ( "grade".equalsIgnoreCase(sortBy) )
	    bldr.append(LOAD_USERS_ORDERED_ORDER_BY_RESULT);
	else
	    bldr.append(LOAD_USERS_ORDERED_ORDER_BY_NAME);
	bldr.append(sortOrder);

	SQLQuery query = getSession().createSQLQuery(bldr.toString());
	query.setLong("sessionId", sessionId);
	query.setLong("questionUid", questionUid);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	ArrayList<AssessmentUserDTO> userDtos = new ArrayList<AssessmentUserDTO>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long questionResultUid = ((Number) element[0]).longValue();
		String firstName = (String) element[1];
		String lastName = (String) element[2];
		String login = (String) element[3];
		float grade = element[4] == null ? 0 : ((Number) element[4]).floatValue();

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setQuestionResultUid(questionResultUid);
		;
		userDto.setFirstName(firstName);
		userDto.setLastName(lastName);
		userDto.setLogin(login);
		userDto.setGrade(grade);
		userDtos.add(userDto);
	    }

	}

	return userDtos;
    }

    @Override
    public List<Number> getRawUserMarksBySession(Long sessionId) {

	SQLQuery query = getSession().createSQLQuery(LOAD_MARKS_FOR_SESSION);
	query.setLong("sessionId", sessionId);
	List<Number> list = query.list();
	return list;
    }

}
