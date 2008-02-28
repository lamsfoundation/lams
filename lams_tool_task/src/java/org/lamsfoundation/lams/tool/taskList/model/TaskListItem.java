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
package org.lamsfoundation.lams.tool.taskList.model;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * TaskList
 * @author Dapeng Ni
 *
 * @hibernate.class  table="tl_latask10_taskList_item"
 *
 */
public class TaskListItem  implements Cloneable{
	private static final Logger log = Logger.getLogger(TaskListItem.class);
	
	private Long uid;
	
	private String title;

	private String description;
	private int sequenceId;
	
	private String initialItem;

	private String organizationXml;

	private boolean isHide;
	private boolean isCreateByAuthor;
	
	private Date createDate;
	private TaskListUser createBy;
	
	private boolean isRequired;
	private boolean isCommentsAllowed;
	private boolean isChildTask;
	
	private String parentTaskName;
	
	//***********************************************
	//DTO fields:
	private boolean complete;
	
    public Object clone(){
    	TaskListItem obj = null;
		try {
			obj = (TaskListItem) super.clone();
			((TaskListItem)obj).setUid(null);
  			//clone ReourceUser as well
  			if(this.createBy != null)
  				((TaskListItem)obj).setCreateBy((TaskListUser) this.createBy.clone());
  			
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + TaskListItem.class + " failed");
		}
		
		return obj;
	}	
//    **********************************************************
//		Get/Set methods
//	  **********************************************************
		/**
		 * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
		 * @return Returns the uid.
		 */
		public Long getUid() {
			return uid;
		}
		/**
		 * @param uid The uid to set.
		 */
		public void setUid(Long userID) {
			this.uid = userID;
		}

		/**
		 * @hibernate.property column="description"
		 * @return
		 */
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * @hibernate.property column="init_item"
		 * @return
		 */
		public String getInitialItem() {
			return initialItem;
		}
		public void setInitialItem(String initialItem) {
			this.initialItem = initialItem;
		}

		/**
		 * @hibernate.property
	     *	column="organization_xml"
	     *  length="65535"
		 * @return
		 */
		public String getOrganizationXml() {
			return organizationXml;
		}
		public void setOrganizationXml(String organizationXml) {
			this.organizationXml = organizationXml;
		}
		
		/**
		 * @hibernate.property column="title" length="255"
		 * @return
		 */
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}

		/**
	     * @hibernate.many-to-one
	     *     cascade="none"
	     * 		column="create_by"
		 * 
		 * @return
		 */
		public TaskListUser getCreateBy() {
			return createBy;
		}
		public void setCreateBy(TaskListUser createBy) {
			this.createBy = createBy;
		}
		
		/**
		 * @hibernate.property column="create_date" 
		 * @return
		 */
		public Date getCreateDate() {
			return createDate;
		}
		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
		
		/**
		 * @hibernate.property column="create_by_author" 
		 * @return
		 */
		public boolean isCreateByAuthor() {
			return isCreateByAuthor;
		}
		public void setCreateByAuthor(boolean isCreateByAuthor) {
			this.isCreateByAuthor = isCreateByAuthor;
		}
		
		/**
		 * @hibernate.property column="is_hide" 
		 * @return
		 */
		public boolean isHide() {
			return isHide;
		}
		public void setHide(boolean isHide) {
			this.isHide = isHide;
		}
	    
		public void setComplete(boolean complete) {
			this.complete=complete;
		}
		public boolean isComplete() {
			return complete;
		}
		
		/**
	     * @hibernate.property  column="sequence_id" 
	     * @return
	     */
		public int getSequenceId() {
			return sequenceId;
		}
		public void setSequenceId(int sequenceId) {
			this.sequenceId = sequenceId;
		}
		
		/**
		 * @hibernate.property column="is_required" 
		 * @return
		 */
		public boolean isRequired() {
			return isRequired;
		}
		public void setRequired(boolean isRequired) {
			this.isRequired = isRequired;
		}
		
		/**
		 * @hibernate.property column="is_comments_allowed" 
		 * @return
		 */
		public boolean isCommentsAllowed() {
			return isCommentsAllowed;
		}
		public void setCommentsAllowed(boolean isCommentsAllowed) {
			this.isCommentsAllowed = isCommentsAllowed;
		}
		
		/**
		 * @hibernate.property column="is_child_task" 
		 * @return
		 */
		public boolean isChildTask() {
			return isChildTask;
		}
		public void setChildTask(boolean isChildTask) {
			this.isChildTask = isChildTask;
		}
		
		/**
		 * @hibernate.property column="parent_task_name"
		 * @return
		 */
		public String getParentTaskName() {
			return parentTaskName;
		}
		public void setParentTaskName(String parentTaskName) {
			this.parentTaskName = parentTaskName;
		}

}
