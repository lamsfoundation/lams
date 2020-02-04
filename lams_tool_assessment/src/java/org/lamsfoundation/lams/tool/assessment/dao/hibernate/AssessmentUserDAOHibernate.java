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
import java.util.UUID;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentUserDAO;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentUserDAOHibernate extends LAMSBaseDAO implements AssessmentUserDAO {

    private static final String FIND_BY_USER_ID_SESSION_ID = "from " + AssessmentUser.class.getName()
	    + " as u where u.userId =? and u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "from " + AssessmentUser.class.getName()
	    + " as u where u.session.sessionId=?";

    private static final String LOAD_MARKS_FOR_SESSION = "SELECT grade FROM tl_laasse10_assessment_result "
	    + " WHERE finish_date IS NOT NULL AND latest = 1 AND session_id = :sessionId";
    private static final String FIND_MARK_STATS_FOR_SESSION = "SELECT MIN(grade) min_grade, AVG(grade) avg_grade, MAX(grade) max_grade FROM tl_laasse10_assessment_result "
	    + " WHERE finish_date IS NOT NULL AND latest = 1 AND session_id = :sessionId";

    private static final String LOAD_MARKS_FOR_LEADERS = "SELECT r.grade FROM tl_laasse10_assessment_result r "
	    + " JOIN tl_laasse10_session s ON r.session_id = s.session_id AND r.user_uid = s.group_leader_uid "
	    + " JOIN tl_laasse10_assessment a ON s.assessment_uid = a.uid "
	    + " WHERE r.finish_date IS NOT NULL AND r.latest = 1 AND a.content_id = :toolContentId";
    private static final String FIND_MARK_STATS_FOR_LEADERS = "SELECT MIN(grade) min_grade, AVG(grade) avg_grade, MAX(grade) max_grade, COUNT(grade) num_complete "
	    + " FROM tl_laasse10_assessment_result r "
	    + " JOIN tl_laasse10_session s ON r.session_id = s.session_id AND r.user_uid = s.group_leader_uid "
	    + " JOIN tl_laasse10_assessment a ON s.assessment_uid = a.uid "
	    + " WHERE r.finish_date IS NOT NULL AND r.latest = 1 AND a.content_id = :toolContentId";

    @SuppressWarnings("rawtypes")
    @Override
    public AssessmentUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List list = doFind(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (AssessmentUser) list.get(0);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public AssessmentUser getUserCreatedAssessment(Long userId, Long contentId) {
	final String FIND_BY_USER_ID_CONTENT_ID = "from " + AssessmentUser.class.getName()
		+ " as u where u.userId =? and u.assessment.contentId=?";

	List list = doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (AssessmentUser) list.get(0);
    }

    @Override
    public AssessmentUser getUserByIdAndContent(Long userId, Long contentId) {
	final String FIND_BY_USER_ID_CONTENT_ID = "from " + AssessmentUser.class.getName()
		+ " as u where u.userId =? and u.session.assessment.contentId=?";

	List list = doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (AssessmentUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AssessmentUser> getBySessionID(Long sessionId) {
	return this.doFind(FIND_BY_SESSION_ID, sessionId);
    }

    private static String LOAD_USERS_ORDERED_BY_SESSION_SELECT = "SELECT DISTINCT user.user_id, user.last_name, user.first_name, user.login_name, result.grade ";
    private static String LOAD_USERS_ORDERED_BY_SESSION_FROM = " FROM tl_laasse10_user user  ";
    private static String LOAD_USERS_ORDERED_BY_SESSION_JOIN = " INNER JOIN tl_laasse10_session session"
	    + " ON user.session_uid=session.uid  LEFT OUTER JOIN tl_laasse10_assessment_result result "
	    + " ON result.user_uid = user.uid  	AND result.finish_date IS NOT NULL"
	    + " 	AND result.latest = 1  WHERE session.session_id = :sessionId "
	    + " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) ";
    private static String LOAD_USERS_ORDERED_ORDER_BY_NAME = "ORDER BY (CONCAT(user.last_name, ' ', user.first_name)) ";
    private static String LOAD_USERS_ORDERED_ORDER_BY_TOTAL = "ORDER BY result.grade ";

    @SuppressWarnings("unchecked")
    @Override
    public List<AssessmentUserDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString, IUserManagementService userManagementService) {
	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	StringBuilder bldr = new StringBuilder(LOAD_USERS_ORDERED_BY_SESSION_SELECT).append(portraitStrings[0])
		.append(LOAD_USERS_ORDERED_BY_SESSION_FROM).append(portraitStrings[1])
		.append(LOAD_USERS_ORDERED_BY_SESSION_JOIN);
	if ("total".equalsIgnoreCase(sortBy)) {
	    bldr.append(LOAD_USERS_ORDERED_ORDER_BY_TOTAL);
	} else {
	    bldr.append(LOAD_USERS_ORDERED_ORDER_BY_NAME);
	}
	bldr.append(sortOrder);

	NativeQuery<Object[]> query = getSession().createNativeQuery(bldr.toString());
	query.setParameter("sessionId", sessionId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	ArrayList<AssessmentUserDTO> userDtos = new ArrayList<>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userId = ((Number) element[0]).longValue();
		String firstName = (String) element[1];
		String lastName = (String) element[2];
		String login = (String) element[3];
		float grade = element[4] == null ? 0 : ((Number) element[4]).floatValue();
		byte[] portraitId = (byte[]) element[5];

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setUserId(userId);
		userDto.setFirstName(firstName);
		userDto.setLastName(lastName);
		userDto.setLogin(login);
		userDto.setGrade(grade);
		userDto.setPortraitId(portraitId == null ? null : UUID.nameUUIDFromBytes(portraitId).toString());
		userDtos.add(userDto);
	    }

	}

	return userDtos;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int getCountUsersBySession(Long sessionId, String searchString) {
	final String LOAD_USERS_ORDERED_BY_NAME = "SELECT COUNT(*) FROM " + AssessmentUser.class.getName() + " user"
		+ " WHERE user.session.sessionId = :sessionId "
		+ " AND (CONCAT(user.lastName, ' ', user.firstName) LIKE CONCAT('%', :searchString, '%')) ";

	Query query = getSession().createQuery(LOAD_USERS_ORDERED_BY_NAME);
	query.setParameter("sessionId", sessionId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	List list = query.list();

	if ((list == null) || (list.size() == 0)) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

    @Override
    public int getCountUsersByContentId(Long contentId) {
	final String LOAD_USERS_ORDERED_BY_NAME = "SELECT COUNT(*) FROM " + AssessmentUser.class.getName() + " user"
		+ " WHERE user.session.assessment.contentId = :contentId ";

	Query<Number> query = getSession().createQuery(LOAD_USERS_ORDERED_BY_NAME, Number.class);
	query.setParameter("contentId", contentId);
	return query.uniqueResult().intValue();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object[] getStatsMarksBySession(Long sessionId) {
	Query query = getSession().createNativeQuery(FIND_MARK_STATS_FOR_SESSION)
		.addScalar("min_grade", FloatType.INSTANCE).addScalar("avg_grade", FloatType.INSTANCE)
		.addScalar("max_grade", FloatType.INSTANCE);
	query.setParameter("sessionId", sessionId);
	List list = query.list();
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (Object[]) list.get(0);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] getStatsMarksForLeaders(Long toolContentId) {
	NativeQuery<Object[]> query = getSession().createNativeQuery(FIND_MARK_STATS_FOR_LEADERS)
		.addScalar("min_grade", FloatType.INSTANCE).addScalar("avg_grade", FloatType.INSTANCE)
		.addScalar("max_grade", FloatType.INSTANCE).addScalar("num_complete", IntegerType.INSTANCE);
	query.setParameter("toolContentId", toolContentId);
	List list = query.list();
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (Object[]) list.get(0);
	}
    }

    private static String LOAD_USERS_ORDERED_BY_SESSION_QUESTION_SELECT = "SELECT DISTINCT question_result.uid, user.last_name, user.first_name, user.login_name, question_result.mark";
    private static String LOAD_USERS_ORDERED_BY_SESSION_QUESTION_FROM = " FROM tl_laasse10_user user";
    private static String LOAD_USERS_ORDERED_BY_SESSION_QUESTION_JOIN = " INNER JOIN tl_laasse10_session session"
	    + " ON user.session_uid=session.uid" +

	    " LEFT OUTER JOIN tl_laasse10_assessment_result result " + " ON result.user_uid = user.uid"
	    + " 	AND result.finish_date IS NOT NULL" + " 	AND result.latest = 1" +

	    " INNER JOIN lams_qb_tool_answer qbToolAnswer " + " ON qbToolAnswer.tool_question_uid = :questionUid " +

	    " INNER JOIN tl_laasse10_question_result question_result " + " ON result.uid=question_result.result_uid"
	    + " 	AND question_result.uid = qbToolAnswer.answer_uid" +

	    " WHERE session.session_id = :sessionId "
	    + " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) ";
    private static String LOAD_USERS_ORDERED_ORDER_BY_RESULT = "ORDER BY question_result.mark ";

    @SuppressWarnings("unchecked")
    @Override
    public List<AssessmentUserDTO> getPagedUsersBySessionAndQuestion(Long sessionId, Long questionUid, int page,
	    int size, String sortBy, String sortOrder, String searchString,
	    IUserManagementService userManagementService) {

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	StringBuilder bldr = new StringBuilder(LOAD_USERS_ORDERED_BY_SESSION_QUESTION_SELECT).append(portraitStrings[0])
		.append(LOAD_USERS_ORDERED_BY_SESSION_QUESTION_FROM).append(portraitStrings[1])
		.append(LOAD_USERS_ORDERED_BY_SESSION_QUESTION_JOIN);
	if ("grade".equalsIgnoreCase(sortBy)) {
	    bldr.append(LOAD_USERS_ORDERED_ORDER_BY_RESULT);
	} else {
	    bldr.append(LOAD_USERS_ORDERED_ORDER_BY_NAME);
	}
	bldr.append(sortOrder);

	NativeQuery<Object[]> query = getSession().createNativeQuery(bldr.toString());
	query.setParameter("sessionId", sessionId);
	query.setParameter("questionUid", questionUid);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	ArrayList<AssessmentUserDTO> userDtos = new ArrayList<>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long questionResultUid = ((Number) element[0]).longValue();
		String firstName = (String) element[1];
		String lastName = (String) element[2];
		String login = (String) element[3];
		float grade = element[4] == null ? 0 : ((Number) element[4]).floatValue();
		byte[] portraitId = (byte[]) element[5];

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setQuestionResultUid(questionResultUid);
		userDto.setFirstName(firstName);
		userDto.setLastName(lastName);
		userDto.setLogin(login);
		userDto.setGrade(grade);
		userDto.setPortraitId(portraitId == null ? null : UUID.nameUUIDFromBytes(portraitId).toString());
		userDtos.add(userDto);
	    }

	}

	return userDtos;
    }

    @Override
    public List<Number> getRawUserMarksBySession(Long sessionId) {
	@SuppressWarnings("unchecked")
	NativeQuery<Number> query = getSession().createNativeQuery(LOAD_MARKS_FOR_SESSION);
	query.setParameter("sessionId", sessionId);
	List<Number> list = query.list();
	return list;
    }

    @Override
    public List<Number> getRawLeaderMarksByToolContentId(Long toolContentId) {
	@SuppressWarnings("unchecked")
	NativeQuery<Number> query = getSession().createNativeQuery(LOAD_MARKS_FOR_LEADERS);
	query.setParameter("toolContentId", toolContentId);
	List<Number> list = query.list();
	return list;
    }

}
