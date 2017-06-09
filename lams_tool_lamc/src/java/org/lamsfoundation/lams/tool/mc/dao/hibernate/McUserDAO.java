/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO;
import org.lamsfoundation.lams.tool.mc.dto.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Hibernate implementation for database access to Mc users (learners) for the mc tool.
 *         </p>
 */
@Repository
public class McUserDAO extends LAMSBaseDAO implements IMcUserDAO {

    private static final String GET_USER_BY_USER_ID_SESSION = "from mcQueUsr in class McQueUsr where mcQueUsr.queUsrId=:queUsrId and mcQueUsr.mcSessionId=:mcSessionUid";

    private static final String LOAD_MARKS_FOR_SESSION = "SELECT last_attempt_total_mark "
	    + " FROM tl_lamc11_que_usr usr "
	    + " JOIN tl_lamc11_session sess ON usr.mc_session_id = sess.uid "
	    + " WHERE responseFinalised = 1 AND sess.mc_session_id = :sessionId";
    private static final String FIND_MARK_STATS_FOR_SESSION = "SELECT MIN(last_attempt_total_mark) min_grade, AVG(last_attempt_total_mark) avg_grade, "
    	    + " MAX(last_attempt_total_mark) max_grade FROM tl_lamc11_que_usr usr "
	    + " JOIN tl_lamc11_session sess ON usr.mc_session_id = sess.uid "
	    + " WHERE responseFinalised = 1 AND sess.mc_session_id = :sessionId";

    private static final String LOAD_MARKS_FOR_LEADERS = "SELECT usr.last_attempt_total_mark "
    	    + " FROM tl_lamc11_que_usr usr "
    	    + " JOIN tl_lamc11_session sess ON usr.mc_session_id = sess.uid AND usr.uid = sess.mc_group_leader_uid "
    	    + " JOIN tl_lamc11_content mcq ON sess.mc_content_id = mcq.uid  "
    	    + " WHERE responseFinalised = 1 AND mcq.content_id = :toolContentId";
    private static final String FIND_MARK_STATS_FOR_LEADERS = "SELECT MIN(usr.last_attempt_total_mark) min_grade, AVG(usr.last_attempt_total_mark) avg_grade,  "
    	    + " MAX(usr.last_attempt_total_mark) max_grade, COUNT(usr.last_attempt_total_mark) num_complete  "
    	    + " FROM tl_lamc11_que_usr usr "
    	    + " JOIN tl_lamc11_session sess ON usr.mc_session_id = sess.uid AND usr.uid = sess.mc_group_leader_uid "
    	    + " JOIN tl_lamc11_content mcq ON sess.mc_content_id = mcq.uid  "
    	    + " WHERE responseFinalised = 1 AND mcq.content_id = :toolContentId";

    @Override
    public McQueUsr getMcUserByUID(Long uid) {
	return (McQueUsr) this.getSession().get(McQueUsr.class, uid);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public McQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionUid) {

	List list = getSessionFactory().getCurrentSession().createQuery(GET_USER_BY_USER_ID_SESSION)
		.setLong("queUsrId", queUsrId.longValue()).setLong("mcSessionUid", mcSessionUid.longValue()).list();

	if (list != null && list.size() > 0) {
	    McQueUsr usr = (McQueUsr) list.get(0);
	    return usr;
	}
	return null;
    }

    @Override
    public void saveMcUser(McQueUsr mcUser) {
	this.getSession().save(mcUser);
    }

    @Override
    public void updateMcUser(McQueUsr mcUser) {
	this.getSession().update(mcUser);
    }

    @Override
    public void removeMcUser(McQueUsr mcUser) {
	this.getSession().delete(mcUser);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<McUserMarkDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {

	final String LOAD_USERS = "SELECT DISTINCT user.uid, user.fullname, user.lastAttemptTotalMark " + "FROM "
		+ McQueUsr.class.getName() + " user " + "WHERE user.mcSession.mcSessionId = :sessionId "
		+ " AND (user.fullname LIKE CONCAT('%', :searchString, '%')) " + " ORDER BY " + " CASE "
		+ " 	WHEN :sortBy='userName' THEN user.fullname "
		+ " 	WHEN :sortBy='total' THEN user.lastAttemptTotalMark " + " END " + sortOrder;

	Query query = getSession().createQuery(LOAD_USERS);
	query.setLong("sessionId", sessionId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setString("searchString", searchString);
	query.setString("sortBy", sortBy);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	ArrayList<McUserMarkDTO> userDtos = new ArrayList<McUserMarkDTO>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userId = ((Number) element[0]).longValue();
		String fullName = (String) element[1];
		Integer totalMark = element[2] == null ? 0 : ((Number) element[2]).intValue();

		McUserMarkDTO userDto = new McUserMarkDTO();
		userDto.setQueUsrId(userId.toString());
		userDto.setFullName(fullName);
		userDto.setTotalMark(new Long(totalMark));
		userDtos.add(userDto);
	    }

	}

	return userDtos;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int getCountPagedUsersBySession(Long sessionId, String searchString) {

	String LOAD_USERS_ORDERED_BY_NAME = "SELECT COUNT(*) FROM " + McQueUsr.class.getName() + " user"
		+ " WHERE user.mcSession.mcSessionId = :sessionId "
		+ " AND (user.fullname LIKE CONCAT('%', :searchString, '%')) ";

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

    @SuppressWarnings("rawtypes")
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

    @SuppressWarnings("rawtypes")
    @Override
    public Object[] getStatsMarksForLeaders(Long toolContentId) {

	Query query = getSession().createSQLQuery(FIND_MARK_STATS_FOR_LEADERS)
		.addScalar("min_grade", FloatType.INSTANCE)
		.addScalar("avg_grade", FloatType.INSTANCE)
		.addScalar("max_grade", FloatType.INSTANCE)
		.addScalar("num_complete", IntegerType.INSTANCE);
	query.setLong("toolContentId", toolContentId);
	List list = query.list();
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (Object[]) list.get(0);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Number> getRawUserMarksBySession(Long sessionId) {

	SQLQuery query = getSession().createSQLQuery(LOAD_MARKS_FOR_SESSION);
	query.setLong("sessionId", sessionId);
	List<Number> list = query.list();
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Number> getRawLeaderMarksByToolContentId(Long toolContentId) {

	SQLQuery query = getSession().createSQLQuery(LOAD_MARKS_FOR_LEADERS);
	query.setLong("toolContentId", toolContentId);
	List<Number> list = query.list();
	return list;
    }

}
