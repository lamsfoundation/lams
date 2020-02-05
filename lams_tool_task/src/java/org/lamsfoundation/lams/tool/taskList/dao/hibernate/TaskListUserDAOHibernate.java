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

package org.lamsfoundation.lams.tool.taskList.dao.hibernate;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.taskList.dao.TaskListUserDAO;
import org.lamsfoundation.lams.tool.taskList.dto.TaskListUserDTO;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of <code>TaskListUserDAO</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.dao.TaskListUserDAO
 */
@Repository
public class TaskListUserDAOHibernate extends LAMSBaseDAO implements TaskListUserDAO {

    private static final String FIND_BY_USER_ID_CONTENT_ID = "from " + TaskListUser.class.getName()
	    + " as u where u.userId =? and u.taskList.contentId=?";
    private static final String FIND_BY_USER_ID_SESSION_ID = "from " + TaskListUser.class.getName()
	    + " as u where u.userId =? and u.session.sessionId=?";
    private static final String FIND_BY_SESSION_ID = "from " + TaskListUser.class.getName()
	    + " as u where u.session.sessionId=?";

    @Override
    public TaskListUser getUserByUserIDAndSessionID(Long userID, Long sessionId) {
	List list = this.doFind(FIND_BY_USER_ID_SESSION_ID, new Object[] { userID, sessionId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (TaskListUser) list.get(0);
    }

    @Override
    public TaskListUser getUserByUserIDAndContentID(Long userId, Long contentId) {
	List list = this.doFind(FIND_BY_USER_ID_CONTENT_ID, new Object[] { userId, contentId });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (TaskListUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TaskListUser> getBySessionID(Long sessionId) {
	return (List<TaskListUser>) this.doFind(FIND_BY_SESSION_ID, sessionId);
    }

    public static final String LOAD_USERS_SELECT = "SELECT user.uid, CONCAT(user.last_name, ' ', user.first_name), user.is_verified_by_monitor, visitLog.taskList_item_uid";
    public static final String LOAD_USERS_FROM = " FROM tl_latask10_user user";
    public static final String LOAD_USERS_JOINS = " INNER JOIN tl_latask10_session session"
	    + " ON user.session_uid=session.uid" +

	    " LEFT OUTER JOIN tl_latask10_item_log visitLog " + " ON visitLog.user_uid = user.uid"
	    + " 	AND visitLog.complete = 1" +

	    " WHERE session.session_id = :sessionId "
	    + " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
	    + " ORDER BY CONCAT(user.last_name, ' ', user.first_name) ";

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    public Collection<TaskListUserDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString, IUserManagementService userManagementService) {

	String[] portraitStrings = userManagementService.getPortraitSQL("user.user_id");

	StringBuilder bldr = new StringBuilder(LOAD_USERS_SELECT).append(portraitStrings[0]).append(LOAD_USERS_FROM)
		.append(portraitStrings[1]).append(LOAD_USERS_JOINS).append(sortOrder);

	NativeQuery query = getSession().createNativeQuery(bldr.toString());
	query.setParameter("sessionId", sessionId);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	//group by userId as long as it returns all completed visitLogs for each user
	HashMap<Long, TaskListUserDTO> userIdToUserDto = new LinkedHashMap<>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userId = ((Number) element[0]).longValue();
		String fullName = (String) element[1];
		boolean isVerifiedByMonitor = element[2] == null ? false : (Boolean) element[2];
		Long completedTaskUid = element[3] == null ? 0 : ((Number) element[3]).longValue();
		String portraitId = (String) element[4];

		TaskListUserDTO userDto = (userIdToUserDto.get(userId) == null) ? new TaskListUserDTO()
			: userIdToUserDto.get(userId);
		userDto.setUserId(userId);
		userDto.setFullName(fullName);
		userDto.setVerifiedByMonitor(isVerifiedByMonitor);
		userDto.getCompletedTaskUids().add(completedTaskUid);
		userDto.setPortraitId(portraitId);

		userIdToUserDto.put(userId, userDto);
	    }
	}

	return userIdToUserDto.values();
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    public Collection<TaskListUserDTO> getPagedUsersBySessionAndItem(Long sessionId, Long taskListItemUid, int page,
	    int size, String sortBy, String sortOrder, String searchString) {

	String LOAD_USERS = "SELECT user.user_id, CONCAT(user.last_name, ' ', user.first_name), visitLog.complete, visitLog.access_date"
		+ " FROM tl_latask10_user user" + " INNER JOIN tl_latask10_session session"
		+ " ON user.session_uid=session.uid" +

		" LEFT OUTER JOIN tl_latask10_item_log visitLog " + " ON visitLog.user_uid = user.uid"
		+ "   AND visitLog.taskList_item_uid = :taskListItemUid" +

		" WHERE session.session_id = :sessionId "
		+ " AND (CONCAT(user.last_name, ' ', user.first_name) LIKE CONCAT('%', :searchString, '%')) "
		+ " ORDER BY " + " CASE "
		+ " WHEN :sortBy='userName' THEN CONCAT(user.last_name, ' ', user.first_name) "
		+ " WHEN :sortBy='completed' THEN visitLog.complete "
		+ " WHEN :sortBy='accessDate' THEN visitLog.access_date " + " END " + sortOrder;

	NativeQuery query = getSession().createNativeQuery(LOAD_USERS);
	query.setParameter("sessionId", sessionId);
	query.setParameter("taskListItemUid", taskListItemUid);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	query.setParameter("sortBy", sortBy);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	Collection<TaskListUserDTO> userDtos = new LinkedList<>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Long userId = ((Number) element[0]).longValue();
		String fullName = (String) element[1];
		boolean isCompleted = element[2] == null ? false : (Boolean) element[2];
		Date accessDate = element[3] == null ? null : new Date(((Timestamp) element[3]).getTime());

		TaskListUserDTO userDto = new TaskListUserDTO();
		userDto.setUserId(userId);
		userDto.setFullName(fullName);
		userDto.setCompleted(isCompleted);
		userDto.setAccessDate(accessDate);
		;

		userDtos.add(userDto);
	    }
	}

	return userDtos;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "deprecation" })
    public int getCountPagedUsersBySession(Long sessionId, String searchString) {

	String LOAD_USERS_ORDERED_BY_NAME = "SELECT COUNT(*) FROM " + TaskListUser.class.getName() + " user"
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

}
