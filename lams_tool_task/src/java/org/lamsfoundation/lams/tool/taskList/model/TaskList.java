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
/* $Id$ */
package org.lamsfoundation.lams.tool.taskList.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.taskList.util.TaskListToolContentHandler;

/**
 * The main entity class of TaskList tool. Contains all the data related to the whole tool.
 * 
 * @author Dapeng Ni
 * @author Andrey Balan
 *
 * @hibernate.class  table="tl_latask10_taskList"
 */
public class TaskList implements Cloneable{
	
	private static final Logger log = Logger.getLogger(TaskList.class);
	
	//key 
	private Long uid;
	//tool contentID
	private Long contentId;
	private String title;
	private String instructions;
	
	//advance
	private boolean lockWhenFinished;
	private boolean sequentialOrder;
	private boolean allowContributeTasks;
	private boolean monitorVerificationRequired;

	private boolean runOffline;
	private boolean defineLater;
	private boolean contentInUse;
	//instructions
	private String onlineInstructions;
	private String offlineInstructions;
	private Set attachments;
	//conditions
	private Set conditions;
	
	//general infomation
	private Date created;
	private Date updated;
	private TaskListUser createdBy;
	
	//taskList Items
	private Set taskListItems;
	
	private boolean reflectOnActivity;
	private String reflectInstructions;
	
	//*************** NON Persist Fields ********************
	private IToolContentHandler toolContentHandler;

	private List<TaskListAttachment> onlineFileList;
	private List<TaskListAttachment> offlineFileList;
	
	/**
	 * Default contruction method. 
	 */
  	public TaskList(){
  		attachments = new HashSet();
  		conditions = new HashSet();
  		taskListItems = new HashSet();
  	}
  	
  	//  **********************************************************
  	//		Function method for TaskList
  	//  **********************************************************
  	
	public static TaskList newInstance(TaskList defaultContent, Long contentId, TaskListToolContentHandler taskListToolContentHandler) {
		TaskList toContent = new TaskList();
		defaultContent.toolContentHandler = taskListToolContentHandler;
		toContent = (TaskList) defaultContent.clone();
		toContent.setContentId(contentId);
		
		//reset user info as well
		if(toContent.getCreatedBy() != null){
			toContent.getCreatedBy().setTaskList(toContent);
			Set<TaskListItem> items = toContent.getTaskListItems();
			for(TaskListItem item:items){
				item.setCreateBy(toContent.getCreatedBy());
			}
		}
		return toContent;
	}
	
  	public Object clone(){
  		
  		TaskList taskList = null;
  		try{
  			taskList = (TaskList) super.clone();
  			taskList.setUid(null);
  			//clone taskListItems
  			if(taskListItems != null){
  				Iterator iter = taskListItems.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					TaskListItem item = (TaskListItem)iter.next(); 
  					TaskListItem newItem = (TaskListItem) item.clone();
  					//just clone old file without duplicate it in repository
					set.add(newItem);
  				}
  				taskList.taskListItems = set;
  			}
  			
  			//clone attachment
  			if(attachments != null){
  				Iterator iter = attachments.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					TaskListAttachment file = (TaskListAttachment)iter.next(); 
  					TaskListAttachment newFile = (TaskListAttachment) file.clone();
  					//just clone old file without duplicate it in repository
  					
					set.add(newFile);
  				}
  				taskList.attachments = set;
  			}
  			//clone conditions
  			if(conditions != null){
  				Iterator iter = conditions.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					TaskListCondition condition = (TaskListCondition)iter.next(); 
  					TaskListCondition newCondition = (TaskListCondition) condition.clone();
  					
  		  			//picking up all the taskListItems that condition had
  		  			if(condition.getTaskListItems() != null){
  		  				Set set2 = new HashSet();
  		  				newCondition.setTaskListItems(set2);
//		  				Iterator iter2 = taskListItems.iterator();  		  				
//  		  				while(iter.hasNext()){
//  		  					TaskListItem item = (TaskListItem)iter.next(); 
//  		  					TaskListItem newItem = (TaskListItem) item.clone();
//  		  					//just clone old file without duplicate it in repository
//  							set.add(newItem);
//  		  				}
  		  				
  		  			}
  					
  					
  					
					set.add(newCondition);
  				}
  				taskList.conditions = set;
  			}
  			//clone ReourceUser as well
  			if(this.createdBy != null){
  				taskList.setCreatedBy((TaskListUser) this.createdBy.clone());
  			}
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + TaskList.class + " failed");
		}
  		
  		return taskList;
  	}
  	
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TaskList))
			return false;

		final TaskList genericEntity = (TaskList) o;

      	return new EqualsBuilder()
      	.append(this.uid,genericEntity.uid)
      	.append(this.title,genericEntity.title)
      	.append(this.instructions,genericEntity.instructions)
      	.append(this.onlineInstructions,genericEntity.onlineInstructions)
      	.append(this.offlineInstructions,genericEntity.offlineInstructions)
      	.append(this.created,genericEntity.created)
      	.append(this.updated,genericEntity.updated)
      	.append(this.createdBy,genericEntity.createdBy)
      	.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uid).append(title)
		.append(instructions).append(onlineInstructions)
		.append(offlineInstructions).append(created)
		.append(updated).append(createdBy)
		.toHashCode();
	}
	
	/**
	 * Updates the modification data for this entity.
	 */
	public void updateModificationData() {
	
		long now = System.currentTimeMillis();
		if (created == null) {
			this.setCreated (new Date(now));
		}
		this.setUpdated(new Date(now));
	}

	/**
	 * Method to support exporting.
	 */
	public void toDTO(){
		onlineFileList = new ArrayList<TaskListAttachment>();
		offlineFileList = new ArrayList<TaskListAttachment>();
		Set<TaskListAttachment> fileSet = this.getAttachments();
		if(fileSet != null){
			for(TaskListAttachment file:fileSet){
				if(StringUtils.equalsIgnoreCase(file.getFileType(),IToolContentHandler.TYPE_OFFLINE))
					offlineFileList.add(file);
				else
					onlineFileList.add(file);
			}
		}
	}
	
	//**********************************************************
	// Get/set methods
	//**********************************************************
	
	/**
	 * Returns the object's creation date
	 *
	 * @return date
	 * @hibernate.property column="create_date"
	 */
	public Date getCreated() {
      return created;
	}

	/**
	 * Sets the object's creation date
	 *
	 * @param created
	 */
	public void setCreated(Date created) {
	    this.created = created;
	}

	/**
	 * Returns the object's date of last update
	 *
	 * @return date updated
	 * @hibernate.property column="update_date"
	 */
	public Date getUpdated() {
        return updated;
	}

	/**
	 * Sets the object's date of last update
	 *
	 * @param updated
	 */
	public void setUpdated(Date updated) {
        this.updated = updated;
	}

    /**
     * Returns id of a user created the taskList.
     * 
     * @return id of a user
     *
     * @hibernate.many-to-one
     *      cascade="save-update"
     * 		column="create_by"
     *
     */
    public TaskListUser getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets id of a user created the taskList.
     * 
     * @param createdBy id of a user
     */
    public void setCreatedBy(TaskListUser createdBy) {
        this.createdBy = createdBy;
    }

	/**
	 * Returns <code>TaskList</code> id.
	 * 
	 * @return tasklist id
	 * 
	 * @hibernate.id column="uid" generator-class="native"
	 */
	public Long getUid() {
		return uid;
	}

	/**
	 * Sets <code>TaskList</code> id.
	 * 
	 * @param uid tasklist id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * Returns the tasklist title.
	 * 
	 * @return tasklist title.
	 *
	 * @hibernate.property
	 * 		column="title"
	 *
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the tasklist title
	 * 
	 * @param title tasklist title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 *  Returns either the tasklist should run offline.
	 * 
	 * @return runOffline flag
	 *
	 * @hibernate.property 
	 * 		column="run_offline"
	 */
	public boolean getRunOffline() {
		return runOffline;
	}
    
	/**
	 * Sets if the tasklist should run offline.
	 * 
	 * @param runOffline The forceOffLine to set.
	 */
	public void setRunOffline(boolean forceOffline) {
		this.runOffline = forceOffline;
	}

    /**
     * Returns tasklist instructions set by teacher.
     * 
     * @return tasklist instructions set by teacher
     *
     * @hibernate.property
     * 		column="instructions"
     *      type="text"
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets tasklist instructions. Usually done by teacher.
     * 
     * @param instructions tasklist instructions
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * Returns tasklist onlineInstructions set by teacher.
     * 
     * @return tasklist onlineInstructions set by teacher
     *
     * @hibernate.property
     * 		column="online_instructions"
     *      type="text"
     */
    public String getOnlineInstructions() {
        return onlineInstructions;
    }

    /**
     * Sets tasklist instructions. Usually done by teacher.
     * 
     * @param onlineInstructions tasklist onlineInstructions
     */
    public void setOnlineInstructions(String onlineInstructions) {
        this.onlineInstructions = onlineInstructions;
    }

    /**
     * Returns tasklist offlineInstructions set by teacher.
     * 
     * @return tasklist offlineInstructions set by teacher
     *
     * @hibernate.property
     * 		column="offline_instructions"
     *      type="text"
     */
    public String getOfflineInstructions() {
        return offlineInstructions;
    }

    /**
     * Sets tasklist offlineInstructions. Usually done by teacher.
     * 
     * @param instructions tasklist offlineInstructions
     */
    public void setOfflineInstructions(String offlineInstructions) {
        this.offlineInstructions = offlineInstructions;
    }

	/**
	 * Returns a set of Attachments belong to this tasklist.
     *
     * @return a set of Attachments belong to this tasklist.
     *
     * @hibernate.set   lazy="true"
     * 					cascade="all"
     * 					inverse="false"
     * 					order-by="create_date desc"
     * @hibernate.collection-key column="taskList_uid"
     * @hibernate.collection-one-to-many
     * 			class="org.lamsfoundation.lams.tool.taskList.model.TaskListAttachment"
     */
	public Set getAttachments() {
		return attachments;
	}

    /**
     * Sets a set of Attachments belong to this tasklist
     * 
     * @param attachments The attachments to set
     */
    public void setAttachments(Set attachments) {
		this.attachments = attachments;
	}
    
	/**
	 * Returns a set of conditions belong to this tasklist.
     *
     * @return a set of conditions belong to this tasklist.
     *
     * @hibernate.set   lazy="true"
     * 					cascade="all"
     * 					inverse="false"
     * 					order-by="sequence_id asc"
     * @hibernate.collection-key column="taskList_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.taskList.model.TaskListCondition"
     */
	public Set getConditions() {
		return conditions;
	}

    /**
     * Sets a set of Conditions belong to this tasklist
     * 
     * @param conditions set of conditions to set
     */
    public void setConditions(Set conditions) {
		this.conditions = conditions;
	}

	/**
	 * Return set of TaskListItems
	 * 
	 * @return set of TaskListItems
	 * 
	 * @hibernate.set lazy="true"
	 *                inverse="false"
	 *                cascade="all"
	 *                order-by="sequence_id asc"
	 * @hibernate.collection-key column="taskList_uid"
	 * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.taskList.model.TaskListItem"
	 */
	public Set getTaskListItems() {
		return taskListItems;
	}
	
	/**
	 * Sets set of TaskListItems.
	 * 
	 * @param taskListItems set of TaskListItems
	 */
	public void setTaskListItems(Set taskListItems) {
		this.taskListItems= taskListItems;
	}

	/**
	 * Checks whether this tasklist is in use.
	 *
	 * @return 
	 * 
	 * @hibernate.property  column="content_in_use"
	 */
	public boolean isContentInUse() {
		return contentInUse;
	}

	/**
	 * Sets whether this tasklist in use or not.
	 * 
	 * @param contentInUse whether this tasklist in use or not
	 */
	public void setContentInUse(boolean contentInUse) {
		this.contentInUse = contentInUse;
	}
	
	/**
	 * Returns whether this tasklist should be defined later.
	 * 
	 * @return whether this tasklist should be defined later
	 * 
	 * @hibernate.property column="define_later"
	 */
	public boolean isDefineLater() {
		return defineLater;
	}

	/**
	 * Sets whether this tasklist should be defined later or not.
	 * 
	 * @param defineLater boolean described whether this tasklist should be defined later or not
	 */
	public void setDefineLater(boolean defineLater) {
		this.defineLater = defineLater;
	}
	
	/**
	 * Returns ContentId
	 * 
	 * @return ContentId
	 * 
	 * @hibernate.property column="content_id" unique="true" 
	 */
	public Long getContentId() {
		return contentId;
	}

	/**
	 * Sets ContentId.
	 * 
	 * @param contentId ContentId
	 */
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	
    /**
     * Returns if the tasklist should be locked after being finished or not.
     * 
     * @return if the tasklist should be locked after being finished or not
     *
     * @hibernate.property
     * 		column="lock_when_finished"
     */
    public boolean getLockWhenFinished() {
        return lockWhenFinished;
    }
    
    /**
     * Set if the tasklist should be locked after being finished or not.
     * 
     * @param lockWhenFinished boolean describing should the tasklist be locked after being finished or not
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
        this.lockWhenFinished = lockWhenFinished;
    }
	
	/**
	 * Returns if learners are allowed to contribute tasks.
	 *
	 * @return whether learners are allowed to contribute tasks
	 * 
	 * @hibernate.property column="allow_contribute_tasks"
	 */
	public boolean isAllowContributeTasks() {
		return allowContributeTasks;
	}
	
	/**
	 * Sets whether learners are allowed to contribute tasks.
	 * 
	 * @param allowContributeTasks boolean describing whether learners are allowed to contribute tasks
	 */
	public void setAllowContributeTasks(boolean allowContributeTasks) {
		this.allowContributeTasks = allowContributeTasks;
	}
	
	/**
	 * Returns whether the learners should be verified by monitor before they can finish tasklist.
	 * 
	 * @return whether the learners should be verified by monitor before they can finish tasklist
	 * 
	 * @hibernate.property column="is_monitor_verification_required"
	 */
	public boolean isMonitorVerificationRequired() {
		return monitorVerificationRequired;
	}
	
	/**
	 * Sets whether the learners should be verified by monitor before they can finish tasklist.
	 * 
	 * @param monitorVerificationRequired boolean describing whether the learners should be verified by monitor before they can finish tasklist
	 */
	public void setMonitorVerificationRequired(boolean monitorVerificationRequired) {
		this.monitorVerificationRequired = monitorVerificationRequired;
	}
	
	/**
	 * Returns if the tasks should be done in a sequential order.
	 * 
	 * @return if the tasks should be done in a sequential order
	 * 
	 * @hibernate.property column="is_sequential_order"
	 */
	public boolean isSequentialOrder() {
		return sequentialOrder;
	}
	
	/**
	 * Sets if the tasks should be done in a sequential order.
	 * 
	 * @param sequentialOrder if the tasks should be done in a sequential order
	 */
	public void setSequentialOrder(boolean sequentialOrder) {
		this.sequentialOrder = sequentialOrder;
	}
	
	/**
	 * For display use
	 * @return
	 */
	public List<TaskListAttachment> getOfflineFileList() {
		return offlineFileList;
	}
	public void setOfflineFileList(List<TaskListAttachment> offlineFileList) {
		this.offlineFileList = offlineFileList;
	}
	public List<TaskListAttachment> getOnlineFileList() {
		return onlineFileList;
	}
	public void setOnlineFileList(List<TaskListAttachment> onlineFileList) {
		this.onlineFileList = onlineFileList;
	}
	public void setToolContentHandler(IToolContentHandler toolContentHandler) {
		this.toolContentHandler = toolContentHandler;
	}

	/**
	 * @hibernate.property column="reflect_instructions"
	 * @return
	 */	
	public String getReflectInstructions() {
		return reflectInstructions;
	}
	public void setReflectInstructions(String reflectInstructions) {
		this.reflectInstructions = reflectInstructions;
	}
	/**
	 * @hibernate.property column="reflect_on_activity"
	 * @return
	 */		
	public boolean isReflectOnActivity() {
		return reflectOnActivity;
	}
	public void setReflectOnActivity(boolean reflectOnActivity) {
		this.reflectOnActivity = reflectOnActivity;
	}
}
