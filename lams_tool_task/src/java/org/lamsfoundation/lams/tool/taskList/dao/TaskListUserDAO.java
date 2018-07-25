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

package org.lamsfoundation.lams.tool.taskList.dao;

import java.util.Collection;
import java.util.List;

import org.lamsfoundation.lams.tool.taskList.dto.TaskListUserDTO;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 * DAO interface for <code>TaskListUser</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.model.TaskListUser
 */
public interface TaskListUserDAO extends DAO {

    /**
     * Returns user with the specified userID and sessionId.
     *
     * @param userID
     *            specified userID
     * @param sessionId
     *            specified sessionId
     * @return user with the specified userID and sessionId
     */
    TaskListUser getUserByUserIDAndSessionID(Long userID, Long sessionId);

    /**
     * Returns user with the specified userID and contentId.
     *
     * @param userId
     *            specified userID
     * @param contentId
     *            specified contentId
     * @return user with the specified userID and contentId
     */
    TaskListUser getUserByUserIDAndContentID(Long userId, Long contentId);

    /**
     * Returns list of users corresponds to specified sessionId.
     *
     * @param sessionId
     *            specified sessionId
     * @return list of users corresponds to specified sessionId
     */
    List<TaskListUser> getBySessionID(Long sessionId);

    /**
     * Returns paged users for jqGrid based on sessionId.
     *
     * @param sessionId
     * @param page
     * @param size
     * @param sortBy
     * @param sortOrder
     * @param searchString
     * @return
     */
    Collection<TaskListUserDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString, IUserManagementService userManagementService);

    /**
     * Returns paged users for jqGrid based on sessionId and taskListItemUid.
     *
     * @param sessionId
     * @param taskListItemUid
     * @param page
     * @param size
     * @param sortBy
     * @param sortOrder
     * @param searchString
     * @return
     */
    Collection<TaskListUserDTO> getPagedUsersBySessionAndItem(Long sessionId, Long taskListItemUid, int page, int size,
	    String sortBy, String sortOrder, String searchString);

    /**
     * Returns total number of users in a specified session.
     *
     * @param sessionId
     * @param searchString
     * @return
     */
    int getCountPagedUsersBySession(Long sessionId, String searchString);
}
