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
package org.lamsfoundation.lams.tool.assessment.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
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

    public AssessmentUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List list = doFind(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0)
	    return null;
	return (AssessmentUser) list.get(0);
    }

    public AssessmentUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List list = doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0)
	    return null;
	return (AssessmentUser) list.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<AssessmentUser> getBySessionID(Long sessionId) {
	return (List<AssessmentUser>) this.doFind(FIND_BY_SESSION_ID, sessionId);
    }
    
    @Override
    public List<AssessmentUserDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy, String sortOrder,
	    String searchString) {

	String LOAD_USERS_ORDERED_BY_NAME = "SELECT DISTINCT user.user_id, user.last_name, user.first_name, result.grade" +
		    " FROM tl_laasse10_user user" + 
		    " INNER JOIN tl_laasse10_session session" +
		    " ON user.session_uid=session.uid" +
		    " LEFT OUTER JOIN (" +
		    " 	  SELECT * FROM ( " +
		    "         SELECT res.user_uid, res.grade " +
		    "         FROM tl_laasse10_assessment_result res" + 
		    "         WHERE (res.finish_date IS NOT NULL) ORDER BY res.start_date DESC" +
		    "     ) latest_res GROUP BY latest_res.user_uid" +
		    " ) result" +
		    " ON result.user_uid = user.uid" +
		    " WHERE session.session_id = :sessionId " +
		    " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +
		    " ORDER BY " + 
		    " CASE " +
			" WHEN :sortBy='userName' THEN CONCAT(user.last_name, ' ', user.first_name) " +
			" WHEN :sortBy='total' THEN result.grade " +
		    " END " + sortOrder;
	
	SQLQuery query = getSession().createSQLQuery(LOAD_USERS_ORDERED_BY_NAME);
	query.setLong("sessionId", sessionId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setString("sortBy", sortBy);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();
	
	ArrayList<AssessmentUserDTO> userDtos = new ArrayList<AssessmentUserDTO>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {
		
		Long userId = ((Number) element[0]).longValue();
		String firstName = (String) element[1];
		String lastName = (String) element[2];
		float grade = ((Number) element[3]).floatValue();

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setUserId(userId);
		userDto.setFirstName(firstName);
		userDto.setLastName(lastName);
		userDto.setGrade(grade);
		userDtos.add(userDto);
	    }
	    
	}

	return userDtos;
    }
    
    @Override
    public int getCountUsersBySession(Long sessionId, String searchString) {

	String LOAD_USERS_ORDERED_BY_NAME = "SELECT COUNT(*) FROM " + AssessmentUser.class.getName() +" user"
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
    
    @Override
    public List<AssessmentUserDTO> getPagedUsersBySessionAndQuestion(Long sessionId, Long questionUid, int page, int size, String sortBy, String sortOrder,
	    String searchString) {

	String LOAD_USERS_ORDERED_BY_NAME = "SELECT DISTINCT question_result.uid, user.last_name, user.first_name, question_result.mark" +
		    " FROM tl_laasse10_user user" + 
		    " INNER JOIN tl_laasse10_session session" +
		    " ON user.session_uid=session.uid" +
		    
		    " LEFT OUTER JOIN (" +
		    " 	  SELECT * FROM ( " +
		    "         SELECT res.uid, res.user_uid " +
		    "         FROM tl_laasse10_assessment_result res" + 
		    "         WHERE (res.finish_date IS NOT NULL) ORDER BY res.start_date DESC" +
		    "     ) latest_res GROUP BY latest_res.user_uid" +
		    " ) result" +
		    " ON result.user_uid = user.uid" +
		    
		    " INNER JOIN (" +
		    "     SELECT question_res.uid, question_res.result_uid, question_res.mark " +
		    "     FROM tl_laasse10_question_result question_res" + 
		    "     WHERE question_res.assessment_question_uid =:questionUid" +
		    " ) question_result" +
		    " ON result.uid=question_result.result_uid" +
		    
		    " WHERE session.session_id = :sessionId " +
		    " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) " +
		    " ORDER BY " + 
		    " CASE " +
			" WHEN :sortBy='userName' THEN CONCAT(user.last_name, ' ', user.first_name) " +
			" WHEN :sortBy='grade' THEN question_result.mark " +
		    " END " + sortOrder;
	
	SQLQuery query = getSession().createSQLQuery(LOAD_USERS_ORDERED_BY_NAME);
	query.setLong("sessionId", sessionId);
	query.setLong("questionUid", questionUid);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setString("sortBy", sortBy);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();
	
	ArrayList<AssessmentUserDTO> userDtos = new ArrayList<AssessmentUserDTO>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {
		
		Long questionResultUid = ((Number) element[0]).longValue();
		String firstName = (String) element[1];
		String lastName = (String) element[2];
		float grade = ((Number) element[3]).floatValue();

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setQuestionResultUid(questionResultUid);;
		userDto.setFirstName(firstName);
		userDto.setLastName(lastName);
		userDto.setGrade(grade);
		userDtos.add(userDto);
	    }
	    
	}

	return userDtos;
    }

}
