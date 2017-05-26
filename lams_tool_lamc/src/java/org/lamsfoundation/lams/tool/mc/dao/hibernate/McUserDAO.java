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

import org.hibernate.FlushMode;
import org.hibernate.Query;
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

    private static final String CALC_MARK_STATS_FOR_SESSION = "select max(mu.lastAttemptTotalMark), min(mu.lastAttemptTotalMark), avg(mu.lastAttemptTotalMark)"
	    + " from McQueUsr mu where mu.mcSessionId = :mcSessionUid";

    private static final String GET_USER_BY_USER_ID_SESSION = "from mcQueUsr in class McQueUsr where mcQueUsr.queUsrId=:queUsrId and mcQueUsr.mcSessionId=:mcSessionUid";

    @Override
    public McQueUsr getMcUserByUID(Long uid) {
	return (McQueUsr) this.getSession().get(McQueUsr.class, uid);
    }

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
	getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
	this.getSession().delete(mcUser);
    }

    /** Get the max, min and average mark (in that order) for a session */
    @Override
    public Integer[] getMarkStatisticsForSession(Long sessionUid) {
	Object[] stats = (Object[]) getSessionFactory().getCurrentSession().createQuery(CALC_MARK_STATS_FOR_SESSION)
		.setLong("mcSessionUid", sessionUid.longValue()).uniqueResult();

	if (stats != null) {
	    if (stats[2] instanceof Float) {
		return new Integer[] { (Integer) stats[0], (Integer) stats[1],
			new Integer(((Float) stats[2]).intValue()) };
	    } else if (stats[2] instanceof Double) {
		return new Integer[] { (Integer) stats[0], (Integer) stats[1],
			new Integer(((Double) stats[2]).intValue()) };
	    }
	}

	return null;

    }

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

}
