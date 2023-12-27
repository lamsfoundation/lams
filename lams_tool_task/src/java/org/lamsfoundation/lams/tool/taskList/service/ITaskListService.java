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

package org.lamsfoundation.lams.tool.taskList.service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.tool.taskList.dto.SessionDTO;
import org.lamsfoundation.lams.tool.taskList.dto.TaskListUserDTO;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListSession;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.util.MessageService;

/**
 * Interface that defines the contract that all TaskLisk service providers must follow.
 *
 * @author Andrey Balan
 */
public interface ITaskListService extends ICommonToolService {

    /**
     * Returns number of tasks completed by user. Used in <code>TaskListOutputFactory</code>.
     *
     * @param toolSessionId
     * @param userUid
     * @return
     */
    int getNumTasksCompletedByUser(Long toolSessionId, Long userUid);

    /**
     * Checks current condition for matching. This condition belongs to the taskList from this particular
     * toolSession.Used in <code>TaskListOutputFactory</code>.
     *
     * @param conditionName
     *            name of a condition
     * @param toolSessionId
     *            session Id contains particular taskList
     * @param userUid
     *            user for whom this condition is being checked
     * @return
     */
    boolean checkCondition(String conditionName, Long toolSessionId, Long userUid);

    /**
     * Get <code>TaskList</code> by toolContentID.
     *
     * @param contentId
     * @return
     */
    TaskList getTaskListByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (TaskList) and assign the toolContentId of that copy as the given
     * <code>contentId</code>
     *
     * @param contentId
     * @return
     * @throws TaskListException
     */
    TaskList getDefaultContent(Long contentId) throws TaskListException;

    /**
     * Get list of taskList items by given taskListUid. These taskList items must be created by author.
     *
     * @param taskListUid
     * @return
     */
    List getAuthoredItems(Long taskListUid);

    /**
     * Upload tasklistItem file to repository.
     *
     * @param uploadFile
     * @param userLogin
     * @return
     * @throws UploadTaskListFileException
     */
    TaskListItemAttachment uploadTaskListItemFile(File uploadFile, TaskListUser user)
	    throws UploadTaskListFileException;

    /**
     * Returns Message service. It makes available to have access to message resources files.
     *
     * @return MessageService
     */
    MessageService getMessageService();

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(TaskListUser taskListUser);

    /**
     * Get user by given userID and toolContentID.
     *
     * @param long1
     * @return
     */
    TaskListUser getUserByIDAndContent(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     *
     * @param long1
     * @param sessionId
     * @return
     */
    TaskListUser getUserByIDAndSession(Long long1, Long sessionId);

    /**
     * Get user list by sessionId and itemUid
     *
     * @param sessionId
     * @param uid
     * @return
     */
    List<TaskListUser> getUserListBySessionItem(Long sessionId, Long itemUid);

    /**
     * Get user list by sessionId. (thus users belonging to one group)
     *
     * @param sessionId
     * @return
     */
    List<TaskListUser> getUserListBySessionId(Long sessionId);

    /**
     * Get user by UID
     *
     * @param uid
     * @return
     */
    TaskListUser getUser(Long uid);

    // ********** Repository methods ***********************

    /**
     * Save or update taskList into database.
     *
     * @param TaskList
     */
    void saveOrUpdateTaskList(TaskList TaskList);

    /**
     * Delete resoruce item from database.
     *
     * @param uid
     */
    void deleteTaskListItem(Long uid);

    /**
     * Delete tasklist condition from the database.
     *
     * @param uid
     */
    void deleteTaskListCondition(Long uid);

    /**
     * Get taskList which is relative with the special toolSession.
     *
     * @param sessionId
     * @return
     */
    TaskList getTaskListBySessionId(Long sessionId);

    /**
     * Get taskList with the specified itemUid.
     *
     * @param itemUid
     * @return
     */
    TaskListItem getTaskListItemByUid(Long itemUid);

    /**
     * Save/update current TaskListItem.
     *
     * @param item
     *            current TaskListItem
     * @return
     */
    void saveOrUpdateTaskListItem(TaskListItem item);

    /**
     * Fill in taskListItemList's complete flags.
     *
     * @param taskListItemList
     * @param user
     */
    void retrieveComplete(Set<TaskListItem> taskListItemList, TaskListUser user);

    /**
     * Mark taskListItem as completed.
     *
     * @param taskListItemUid
     * @param userId
     * @param sessionId
     */
    void setItemComplete(Long taskListItemUid, Long userId, Long sessionId);

    /**
     * Creates a new TaskListItemVisitLog describing access to specifeid taskListItem.
     *
     * @param taskListItemUid
     *            Uid of the specified taskListItem
     * @param userId
     *            Id of a user who accessed this taskListItem
     * @param sessionId
     *            id of a session during which it occured
     */
    void setItemAccess(Long taskListItemUid, Long userId, Long sessionId);

    /**
     * Get all available sessions for contentId.
     *
     * @param contentId
     * @return
     */
    List<TaskListSession> getSessionsByContentId(Long contentId);

    /**
     * Get taskList toolSession by toolSessionId
     *
     * @param sessionId
     * @return
     */
    TaskListSession getSessionBySessionId(Long sessionId);

    /**
     * Save or update taskList session.
     *
     * @param resSession
     */
    void saveOrUpdateSession(TaskListSession resSession);

    /**
     * If success return next activity's url, otherwise return null.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws TaskListException;

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
	    String sortOrder, String searchString);

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

    /**
     * Return summary list for the specified TaskList. Used in monitoring.
     *
     * @param contentId
     *            specified TaskList uid
     * @return
     */
    List<SessionDTO> getSummary(Long contentId);

    /**
     * Get localized message.
     *
     * @param key
     * @return
     */
    String getMessage(String key);

}
