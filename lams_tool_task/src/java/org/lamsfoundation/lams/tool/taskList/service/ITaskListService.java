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
package org.lamsfoundation.lams.tool.taskList.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.taskList.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.taskList.dto.Summary;
import org.lamsfoundation.lams.tool.taskList.dto.TaskSummary;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListCondition;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListSession;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;

/**
 * Interface that defines the contract that all TaskLisk service providers must follow.
 * 
 * @author Dapeng.Ni
 * @author Andrey Balan
 */
public interface ITaskListService {
	
	
	/**
	 * @param toolSessionId
	 * @param userUid
	 * @return
	 */
	public int getNumTasksCompletedByUser(Long toolSessionId, Long userUid);
	
	/**
	 * Get <code>TaskList</code> by toolContentID.
	 * 
	 * @param contentId
	 * @return
	 */
	TaskList getTaskListByContentId(Long contentId);
	
	/**
	 * Get a cloned copy of  tool default tool content (TaskList) and assign the toolContentId of that copy as the 
	 * given <code>contentId</code> 
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
	 * Upload instruciton file into repository.
	 * 
	 * @param file
	 * @param type
	 * @return
	 * @throws UploadTaskListFileException
	 */
	TaskListAttachment uploadInstructionFile(FormFile file, String type) throws UploadTaskListFileException;
	
	
	/**
	 * Upload tasklistItem file to repository.
	 * 
	 * @param uploadFile
	 * @param fileType
	 * @param userLogin
	 * @return
	 * @throws UploadTaskListFileException
	 */
	TaskListItemAttachment uploadTaskListItemFile(FormFile uploadFile, String fileType, TaskListUser user) throws UploadTaskListFileException; 
	
	//********** for user methods *************
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
	 * Get user by UID
	 * 
	 * @param uid
	 * @return
	 */
	TaskListUser getUser(Long uid);

	//********** Repository methods ***********************
	/**
	 * Delete file from the repository.
	 * 
	 * @param fileUuid
	 * @param fileVersionId
	 * @throws TaskListException
	 */
	void deleteFromRepository(Long fileUuid, Long fileVersionId) throws TaskListException ;

	/**
	 * Save or update taskList into database.
	 * 
	 * @param TaskList
	 */
	void saveOrUpdateTaskList(TaskList TaskList);
	
	/**
	 * Delete reource attachment(i.e., offline/online instruction file) from database. This method does not
	 * delete the file from repository.
	 * 
	 * @param attachmentUid
	 */
	void deleteTaskListAttachment(Long attachmentUid);
	
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
	 * Return all resource items within the given toolSessionID.
	 * 
	 * @param sessionId
	 * @return
	 */
	List<TaskListItem> getTaskListItemsBySessionId(Long sessionId);
	
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
	 * @param item current TaskListItem
	 * @return
	 */
	public void saveOrUpdateTaskListItem(TaskListItem item);
	
	/**
	 * Fill in taskListItemList's complete flags.
	 * 
	 * @param taskListItemList
	 * @param user
	 */
	void retrieveComplete(SortedSet<TaskListItem> taskListItemList, TaskListUser user);
	
	/**
	 * Mark taskListItem as completed.
	 * 
	 * @param taskListItemUid
	 * @param userId
	 * @param sessionId
	 */
	void setItemComplete(Long taskListItemUid, Long userId , Long sessionId);
	
	
	/**
	 * Creates a new TaskListItemVisitLog describing access to specifeid taskListItem.
	 * 
	 * @param taskListItemUid Uid of the specified taskListItem
	 * @param userId Id of a user who accessed this taskListItem
	 * @param sessionId id of a session during which it occured
	 */
	void setItemAccess(Long taskListItemUid, Long userId, Long sessionId);
	
	/**
	 * Get taskList toolSession by toolSessionId
	 * 
	 * @param sessionId
	 * @return
	 */
	TaskListSession getTaskListSessionBySessionId(Long sessionId);

	/**
	 * Save or update taskList session.
	 * 
	 * @param resSession
	 */
	void saveOrUpdateTaskListSession(TaskListSession resSession);
	
	/**
	 * If success return next activity's url, otherwise return null.
	 * 
	 * @param toolSessionId
	 * @param userId
	 * @return
	 */
	String finishToolSession(Long toolSessionId, Long userId)  throws TaskListException;

	/**
	 * Create refection entry into notebook tool.
	 * 
	 * @param sessionId
	 * @param notebook_tool
	 * @param tool_signature
	 * @param userId
	 * @param entryText
	 */
	public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId, String entryText);
	
	/**
	 * Get reflection entry from notebook tool.
	 * 
	 * @param sessionId
	 * @param idType
	 * @param signature
	 * @param userID
	 * @return
	 */
	public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);

	/**
	 * @param notebookEntry
	 */
	public void updateEntry(NotebookEntry notebookEntry);
	
	/**
	 * Get Reflect DTO list grouped by sessionID.
	 * 
	 * @param contentId
	 * @return
	 */
	Map<Long, Set<ReflectDTO>> getReflectList(Long contentId);


	//********** Export methods ***********************
	
	/**
	 * Make an export for the specified learner.
	 *  
	 * @param sessionId
	 * @param learner user which portfolio is being exported
	 *
	 * @return
	 */
	public List<TaskSummary> exportForLearner(Long contentUid, TaskListUser learner);
	
	/**
	 * Make an export for the whole TaskList.
	 * 
	 * @param contentUid
	 * @return
	 */
	public List<TaskSummary> exportForTeacher(Long contentUid);
	
	/**
	 * Return summary for the specified TaskList. Used in monitoring.
	 * 
	 * @param contentId specified TaskList uid
	 * @return
	 */
	Summary getSummary(Long contentId);
	
	/**
	 * Return task summary for the specified TaskListItem. Used in monitoring.
	 * 
	 * @param contentId toolContenId
	 * @param taskListItemUid specified TaskListItem uid
	 * @return
	 */
	TaskSummary getTaskSummary(Long contentId, Long taskListItemUid);
	
}

