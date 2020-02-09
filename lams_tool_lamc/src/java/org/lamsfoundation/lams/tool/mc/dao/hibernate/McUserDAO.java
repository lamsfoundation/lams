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

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO;
import org.lamsfoundation.lams.tool.mc.dto.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.model.McQueUsr;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Hibernate implementation for database access to Mc users (learners) for the mc tool.
 *         </p>
 */
@Repository
public class McUserDAO extends LAMSBaseDAO implements IMcUserDAO {

    private static final String GET_USER_BY_USER_ID_SESSION = "from mcQueUsr in class McQueUsr where mcQueUsr.queUsrId=:queUsrId and mcQueUsr.mcSession.uid=:mcSessionUid";

    private static final String LOAD_MARKS_FOR_SESSION = "SELECT last_attempt_total_mark "
	    + " FROM tl_lamc11_que_usr usr " + " JOIN tl_lamc11_session sess ON usr.mc_session_id = sess.uid "
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
	return this.getSession().get(McQueUsr.class, uid);
    }

    @Override
    public McQueUsr getMcUserByContentId(Long userId, Long contentId) {
	final String GET_USER_BY_USER_ID_AND_CONTENT_ID = "from " + McQueUsr.class.getName()
		+ " user where user.queUsrId=:userId and user.mcSession.mcContent.mcContentId=:contentId";

	return (McQueUsr) getSessionFactory().getCurrentSession().createQuery(GET_USER_BY_USER_ID_AND_CONTENT_ID)
		.setParameter("userId", userId).setParameter("contentId", contentId).uniqueResult();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public McQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionUid) {

	List<?> list = getSessionFactory().getCurrentSession().createQuery(GET_USER_BY_USER_ID_SESSION)
		.setParameter("queUsrId", queUsrId).setParameter("mcSessionUid", mcSessionUid).list();

	if (list != null && list.size() > 0) {
	    McQueUsr usr = (McQueUsr) list.get(0);
	    return usr;
	}
	return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> getUsersWithPortraitsBySessionID(Long sessionId) {
	final String LOAD_USERS_WITH_PORTRAITS_BY_SESSION_ID = "SELECT user.user_id, luser.portrait_uuid portraitId FROM tl_lamc11_que_usr user  "
		+ " INNER JOIN tl_lamc11_session session ON user.mc_session_id=session.uid"
		+ " INNER JOIN lams_user luser ON luser.user_id = user.que_usr_id"
		+ " WHERE session.mc_session_id = :sessionId";

	NativeQuery<Object[]> query = getSession().createNativeQuery(LOAD_USERS_WITH_PORTRAITS_BY_SESSION_ID);
	query.setParameter("sessionId", sessionId);
	List<Object[]> list = query.list();

	ArrayList<Object[]> userDtos = new ArrayList<>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userId = ((Number) element[0]).longValue();
		Long portraitId = element[1] == null ? null : ((Number) element[1]).longValue();

		Object[] userDto = new Object[2];
		userDto[0] = userId;
		userDto[0] = portraitId;
		userDtos.add(userDto);
	    }

	}

	return userDtos;
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

    final String LOAD_USERS_SELECT = "SELECT user.uid, user.que_usr_id, user.fullname, user.last_attempt_total_mark ";
    final String LOAD_USERS_FROM = " FROM tl_lamc11_que_usr user ";
    final String LOAD_USERS_JOINWHERE = " JOIN tl_lamc11_session session on user.mc_session_id = session.uid "
	    + " WHERE session.mc_session_id = :sessionId "
	    + " AND (user.fullname LIKE CONCAT('%', :searchString, '%')) "
	    + " ORDER BY CASE WHEN :sortBy='userName' THEN user.fullname "
	    + " WHEN :sortBy='total' THEN user.last_attempt_total_mark END ";

    @SuppressWarnings("unchecked")
    @Override
    public List<McUserMarkDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString, IUserManagementService userManagementService) {

	String[] portraitStrings = userManagementService.getPortraitSQL("user.que_usr_id");

	StringBuilder bldr = new StringBuilder(LOAD_USERS_SELECT).append(portraitStrings[0]).append(LOAD_USERS_FROM)
		.append(portraitStrings[1]).append(LOAD_USERS_JOINWHERE).append(sortOrder);

	NativeQuery<Object[]> query = getSession().createSQLQuery(bldr.toString());
	query.setParameter("sessionId", sessionId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	query.setParameter("sortBy", sortBy);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	ArrayList<McUserMarkDTO> userDtos = new ArrayList<>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userUid = ((Number) element[0]).longValue();
		Long userId = ((Number) element[1]).longValue();
		String fullName = (String) element[2];
		Integer totalMark = element[3] == null ? 0 : ((Number) element[3]).intValue();
		String portraitId = (String) element[4];

		McUserMarkDTO userDto = new McUserMarkDTO();
		userDto.setQueUsrId(userUid.toString());
		userDto.setUserId(userId.toString());
		userDto.setFullName(fullName);
		userDto.setTotalMark(new Long(totalMark));
		userDto.setPortraitId(portraitId);
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

	Query<?> query = getSession().createQuery(LOAD_USERS_ORDERED_BY_NAME);
	query.setParameter("sessionId", sessionId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	List<?> list = query.list();

	if ((list == null) || (list.size() == 0)) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object[] getStatsMarksBySession(Long sessionId) {

	Query<?> query = getSession().createSQLQuery(FIND_MARK_STATS_FOR_SESSION)
		.addScalar("min_grade", FloatType.INSTANCE).addScalar("avg_grade", FloatType.INSTANCE)
		.addScalar("max_grade", FloatType.INSTANCE);
	query.setParameter("sessionId", sessionId);
	List<?> list = query.list();
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (Object[]) list.get(0);
	}
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object[] getStatsMarksForLeaders(Long toolContentId) {

	Query<?> query = getSession().createSQLQuery(FIND_MARK_STATS_FOR_LEADERS)
		.addScalar("min_grade", FloatType.INSTANCE).addScalar("avg_grade", FloatType.INSTANCE)
		.addScalar("max_grade", FloatType.INSTANCE).addScalar("num_complete", IntegerType.INSTANCE);
	query.setParameter("toolContentId", toolContentId);
	List<?> list = query.list();
	if ((list == null) || (list.size() == 0)) {
	    return null;
	} else {
	    return (Object[]) list.get(0);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Number> getRawUserMarksBySession(Long sessionId) {

	NativeQuery<Number> query = getSession().createNativeQuery(LOAD_MARKS_FOR_SESSION);
	query.setParameter("sessionId", sessionId);
	List<Number> list = query.list();
	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Number> getRawLeaderMarksByToolContentId(Long toolContentId) {

	NativeQuery<Number> query = getSession().createNativeQuery(LOAD_MARKS_FOR_LEADERS);
	query.setParameter("toolContentId", toolContentId);
	List<Number> list = query.list();
	return list;
    }

}
