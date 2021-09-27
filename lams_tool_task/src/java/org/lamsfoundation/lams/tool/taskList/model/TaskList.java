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

package org.lamsfoundation.lams.tool.taskList.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

/**
 * The main entity class of TaskList tool. Contains all the data related to the whole tool.
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_latask10_tasklist")
public class TaskList implements Cloneable {

    private static final Logger log = Logger.getLogger(TaskList.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "content_id")
    private Long contentId;

    @Column
    private String title;

    @Column
    private String instructions;

    @Column(name = "lock_when_finished")
    private boolean lockWhenFinished;

    @Column(name = "is_sequential_order")
    private boolean sequentialOrder;

    @Column(name = "minimum_number_tasks")
    private int minimumNumberTasks;

    @Column(name = "allow_contribute_tasks")
    private boolean allowContributeTasks;

    @Column(name = "is_monitor_verification_required")
    private boolean monitorVerificationRequired;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("sequence_id ASC")
    @JoinColumn(name = "taskList_uid")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<TaskListCondition> conditions = new HashSet<TaskListCondition>();

    @Column(name = "create_date")
    private Date created;

    @Column(name = "update_date")
    private Date updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "create_by")
    private TaskListUser createdBy;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "taskList_uid")
    @OrderBy("sequence_id ASC")
    private Set<TaskListItem> taskListItems = new HashSet<TaskListItem>();

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    //*************** NON Persist Fields ********************
    @Transient
    private String minimumNumberTasksErrorStr;

    public static TaskList newInstance(TaskList defaultContent, Long contentId) {
	TaskList toContent = new TaskList();
	toContent = (TaskList) defaultContent.clone();
	toContent.setContentId(contentId);

	//reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setTaskList(toContent);
	    Set<TaskListItem> items = toContent.getTaskListItems();
	    for (TaskListItem item : items) {
		item.setCreateBy(toContent.getCreatedBy());
	    }
	}
	return toContent;
    }

    @Override
    public Object clone() {

	TaskList taskList = null;
	try {
	    taskList = (TaskList) super.clone();
	    taskList.setUid(null);

	    //clone taskListItems
	    if (taskListItems != null) {
		Iterator<TaskListItem> iter = taskListItems.iterator();
		Set<TaskListItem> set = new HashSet<TaskListItem>();
		while (iter.hasNext()) {
		    TaskListItem item = iter.next();
		    TaskListItem newItem = (TaskListItem) item.clone();
		    //just clone old file without duplicate it in repository
		    set.add(newItem);
		}
		taskList.taskListItems = set;
	    }

	    //clone conditions
	    if (conditions != null) {

		// create map of pairs: sequenceId that corresponds to appropreate
		// taskListItem. Will need then for making ties beetween conditions'
		// taskListItems and real ones.
		HashMap<Integer, TaskListItem> taskListItemsSeq = new HashMap<Integer, TaskListItem>();
		for (Object itemObject : taskList.taskListItems) {
		    TaskListItem item = (TaskListItem) itemObject;
		    taskListItemsSeq.put(item.getSequenceId(), item);
		}

		Set<TaskListCondition> newConditions = new HashSet<TaskListCondition>();
		Iterator<TaskListCondition> conds = conditions.iterator();
		while (conds.hasNext()) {
		    TaskListCondition condition = conds.next();
		    TaskListCondition newCondition = (TaskListCondition) condition.clone();

		    //picking up all the taskListItems that condition had
		    if (condition.getTaskListItems() != null) {
			Set<TaskListItem> condTaskListItems = new HashSet<TaskListItem>();

			Iterator<TaskListItem> iterCondItems = condition.getTaskListItems().iterator();
			while (iterCondItems.hasNext()) {
			    TaskListItem item = iterCondItems.next();
			    condTaskListItems.add(taskListItemsSeq.get(item.getSequenceId()));
			}
			newCondition.setTaskListItems(condTaskListItems);
		    }
		    newConditions.add(newCondition);
		}
		taskList.conditions = newConditions;
	    }

	    //clone ResourceUser as well
	    if (this.createdBy != null) {
		taskList.setCreatedBy((TaskListUser) this.createdBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + TaskList.class + " failed");
	}

	return taskList;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof TaskList)) {
	    return false;
	}

	final TaskList genericEntity = (TaskList) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid).append(this.title, genericEntity.title)
		.append(this.instructions, genericEntity.instructions).append(this.created, genericEntity.created)
		.append(this.updated, genericEntity.updated).append(this.createdBy, genericEntity.createdBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(created).append(updated)
		.append(createdBy).toHashCode();
    }

    /**
     * Updates the modification data for this entity.
     */
    public void updateModificationData() {

	long now = System.currentTimeMillis();
	if (created == null) {
	    this.setCreated(new Date(now));
	}
	this.setUpdated(new Date(now));
    }

    //**********************************************************
    // Get/set methods
    //**********************************************************

    /**
     * Returns the object's creation date
     *
     * @return date
     *
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
     *
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
     */
    public TaskListUser getCreatedBy() {
	return createdBy;
    }

    /**
     * Sets id of a user created the taskList.
     *
     * @param createdBy
     *            id of a user
     */
    public void setCreatedBy(TaskListUser createdBy) {
	this.createdBy = createdBy;
    }

    /**
     * Returns <code>TaskList</code> id.
     *
     * @return tasklist id
     */
    public Long getUid() {
	return uid;
    }

    /**
     * Sets <code>TaskList</code> id.
     *
     * @param uid
     *            tasklist id
     */
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * Returns the tasklist title.
     *
     * @return tasklist title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * Sets the tasklist title
     *
     * @param title
     *            tasklist title
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Returns tasklist instructions set by teacher.
     *
     * @return tasklist instructions set by teacher
     */
    public String getInstructions() {
	return instructions;
    }

    /**
     * Sets tasklist instructions. Usually done by teacher.
     *
     * @param instructions
     *            tasklist instructions
     */
    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * Returns a set of conditions belong to this tasklist.
     *
     * @return a set of conditions belong to this tasklist.
     */
    public Set<TaskListCondition> getConditions() {
	return conditions;
    }

    /**
     * Sets a set of Conditions belong to this tasklist
     *
     * @param conditions
     *            set of conditions to set
     */
    public void setConditions(Set<TaskListCondition> conditions) {
	this.conditions = conditions;
    }

    /**
     * Return set of TaskListItems
     *
     * @return set of TaskListItems
     */
    public Set<TaskListItem> getTaskListItems() {
	return taskListItems;
    }

    /**
     * Sets set of TaskListItems.
     *
     * @param taskListItems
     *            set of TaskListItems
     */
    public void setTaskListItems(Set<TaskListItem> taskListItems) {
	this.taskListItems = taskListItems;
    }

    /**
     * Checks whether this tasklist is in use.
     *
     * @return
     *
     *
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    /**
     * Sets whether this tasklist in use or not.
     *
     * @param contentInUse
     *            whether this tasklist in use or not
     */
    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * Returns whether this tasklist should be defined later.
     *
     * @return whether this tasklist should be defined later
     *
     *
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    /**
     * Sets whether this tasklist should be defined later or not.
     *
     * @param defineLater
     *            boolean described whether this tasklist should be defined later or not
     */
    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * Returns ContentId
     *
     * @return ContentId
     *
     *
     */
    public Long getContentId() {
	return contentId;
    }

    /**
     * Sets ContentId.
     *
     * @param contentId
     *            ContentId
     */
    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    /**
     * Returns if the tasklist should be locked after being finished or not.
     *
     * @return if the tasklist should be locked after being finished or not
     */
    public boolean getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * Set if the tasklist should be locked after being finished or not.
     *
     * @param lockWhenFinished
     *            boolean describing should the tasklist be locked after being finished or not
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * Returns if learners are allowed to contribute tasks.
     *
     * @return whether learners are allowed to contribute tasks
     *
     *
     */
    public boolean isAllowContributeTasks() {
	return allowContributeTasks;
    }

    /**
     * Sets whether learners are allowed to contribute tasks.
     *
     * @param allowContributeTasks
     *            boolean describing whether learners are allowed to contribute tasks
     */
    public void setAllowContributeTasks(boolean allowContributeTasks) {
	this.allowContributeTasks = allowContributeTasks;
    }

    /**
     * Returns whether the learners should be verified by monitor before they can finish tasklist.
     *
     * @return whether the learners should be verified by monitor before they can finish tasklist
     *
     *
     */
    public boolean isMonitorVerificationRequired() {
	return monitorVerificationRequired;
    }

    /**
     * Sets whether the learners should be verified by monitor before they can finish tasklist.
     *
     * @param monitorVerificationRequired
     *            boolean describing whether the learners should be verified by monitor before they can finish tasklist
     */
    public void setMonitorVerificationRequired(boolean monitorVerificationRequired) {
	this.monitorVerificationRequired = monitorVerificationRequired;
    }

    /**
     * Returns if the tasks should be done in a sequential order.
     *
     * @return if the tasks should be done in a sequential order
     *
     *
     */
    public boolean isSequentialOrder() {
	return sequentialOrder;
    }

    /**
     * Sets if the tasks should be done in a sequential order.
     *
     * @param sequentialOrder
     *            if the tasks should be done in a sequential order
     */
    public void setSequentialOrder(boolean sequentialOrder) {
	this.sequentialOrder = sequentialOrder;
    }

    /**
     * Returns the minimum number of tasks needed to be completed to finish this activity.
     *
     * @return the minimum number of tasks needed to be completed to finish this activity
     *
     *
     */
    public int getMinimumNumberTasks() {
	return minimumNumberTasks;
    }

    /**
     * Sets the minimum number of tasks needed to be completed to finish this activity.
     *
     * @param numberTasksToComplete
     *            the minimum number of tasks needed to be completed to finish this activity
     */
    public void setMinimumNumberTasks(int minimumNumberTasks) {
	this.minimumNumberTasks = minimumNumberTasks;
    }

    /**
     *
     * @return
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
     * @return
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     *
     * @return date submissionDeadline
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    /**
     * Returns the number of tasks needed to be completed to finish this activity.
     *
     * @return the number of tasks needed to be completed to finish this activity
     *
     */
    public String getMinimumNumberTasksErrorStr() {
	return minimumNumberTasksErrorStr;
    }

    /**
     * Sets the number of tasks needed to be completed to finish this activity.
     *
     * @param numberTasksToComplete
     *            the number of tasks needed to be completed to finish this activity
     */
    public void setMinimumNumberTasksErrorStr(String minimumNumberTasksErrorStr) {
	this.minimumNumberTasksErrorStr = minimumNumberTasksErrorStr;
    }

}
