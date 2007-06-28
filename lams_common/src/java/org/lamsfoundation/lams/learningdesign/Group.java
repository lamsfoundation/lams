/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.GroupDTO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Nullable;


/** 
 *        @hibernate.class
 *         table="lams_group"
 *     
*/
public class Group implements Serializable,Nullable,Comparable {

    public final static int STAFF_GROUP_ORDER_ID = 1;
    public final static String NAME_OF_STAFF_GROUP = "Staff Group";
    
    /** identifier field */
    private Long groupId;
    
    private String groupName;

    /** persistent field */
    private int orderId;

	/** FLASH generated value. Unique per LearningDesign.
	 * Required by flash only.*/
    private Integer groupUIID;

    /** persistent field */
    private Grouping grouping;

    /** persistent field */
    private Set users;

    /** persistent field */
    private Set toolSessions;

    /** persistent field */
	private Set branchActivities;

	//---------------------------------------------------------------------
    // Object creation Methods
    //---------------------------------------------------------------------
    
    /** full constructor */
    public Group(Long groupId, String groupName, int orderId, Integer groupUIID, Grouping grouping, Set users, Set toolSessions, Set branchActivities) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.orderId = orderId;
        this.groupUIID = groupUIID;
        this.grouping = grouping;
        this.users = users;
        this.toolSessions = toolSessions;
        this.branchActivities = branchActivities;
    }

    /**
     * Creation Constructor for initializing learner group without tool sessions
     * The order is generated using synchornize method on grouping. If a group of
     * this name already exists, returns null.
     * 
     * @param grouping the grouping this group belongs to.
     * @param users the users in this group.
     * @return the new learner group
     */
    public static Group createLearnerGroup(Grouping grouping, String groupName, Set users)
    {
    	int nextOrderId = grouping.getNextGroupOrderIdCheckName(groupName);
    	if ( nextOrderId > -1 ) {
    		return new Group(null,groupName,nextOrderId,null,grouping,users,new HashSet(),null);
    	}
    	return null;
    }
  
    /**
     * Creation Constructor for initializing learner group with tool sessions
     * The order is generated using synchornize method on grouping. If a group of
     * this name already exists, returns null.
     * 
     * @param grouping the grouping this group belongs to.
     * @param name of this group
     * @param users the users in this group.
     * @param toolSessions all tool sessions included in this group
     * @return the new learner group
     */
    public static Group createLearnerGroupWithToolSession(Grouping grouping, String groupName, Set users,Set toolSessions)
    {
    	int nextOrderId = grouping.getNextGroupOrderIdCheckName(groupName);
    	if ( nextOrderId > -1 ) {
    		return new Group(null,groupName,nextOrderId,null,grouping,users,toolSessions,null);
    	}
    	return null;
    }
    
    /**
     * Creation constructor for initializing staff group. The order is created
     * using default constant.
     * @param grouping the grouping this group belongs to.
     * @param name of this group
     * @param staffs the users in this group.
     * 
     * @return the new staff group. 
     */
    public static Group createStaffGroup(Grouping grouping,  String groupName, Set staffs)
    {
        return new Group(null,groupName, STAFF_GROUP_ORDER_ID,null,grouping,staffs,new HashSet(),null);
    }
    
    /** default constructor */
    public Group() {
    }
    //---------------------------------------------------------------------
    // Field Access Methods
    //---------------------------------------------------------------------

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="group_id"
     *         
     */
    public Long getGroupId() {
        return this.groupId;
    }
    /**
     * @hibernate.property
     * 	 column="group_name"
     * 	 
     * @return
     */
    public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /** 
     *            @hibernate.property
     *             column="order_id"
     *             length="6"
     *             not-null="true"
     *         
     */
    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

	/**
	 * @hibernate.property column="group_ui_id" length="11"
	 */
	public Integer getGroupUIID() {
		return this.groupUIID;
	}

	public void setGroupUIID(Integer uiId) {
		this.groupUIID = uiId;
	}

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="grouping_id"         
     *         
     */
    public org.lamsfoundation.lams.learningdesign.Grouping getGrouping() {
        return this.grouping;
    }

    public void setGrouping(org.lamsfoundation.lams.learningdesign.Grouping grouping) {
        this.grouping = grouping;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * 				  table = "lams_user_group"
     * @hibernate.collection-key column="group_id"
     * @hibernate.collection-many-to-many
     *            class="org.lamsfoundation.lams.usermanagement.User"
     */
    public Set getUsers() {
        return this.users;
    }

    public void setUsers(Set userGroups) {
        this.users = userGroups;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="group_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.tool.ToolSession"
     *         
     */
    public Set getToolSessions() {
        return this.toolSessions;
    }

    public void setToolSessions(Set toolSessions) {
        this.toolSessions = toolSessions;
    }

	/** Maps the branch activities appropriate for this Group. Normally there is only one branch
	 * per branching activity that is applicable to a group, but this may be changed in the future.
	 * If the group is applied to multiple branching activities, then there will be multiple
	 * branches - one for each branching activity. 
	 * 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="all-delete-orphane"
     *            @hibernate.collection-key
     *             column="group_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.learningdesign.GroupBranchActivityEntry"
     *         
     */
	public Set getBranchActivities() {
		return this.branchActivities;
	}
	public void setBranchActivities(Set branchActivities) {
		this.branchActivities = branchActivities;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("groupId", getGroupId())
            .append("groupName", getGroupName())
            .append("groupUIID", getGroupUIID())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof Group) ) return false;
        Group castOther = (Group) other;
        return new EqualsBuilder()
        	.append(this.getGroupId(), castOther.getGroupId())
        	.append(this.getGroupName(), castOther.getGroupName())
            .append(this.getOrderId(), castOther.getOrderId())
            .append(this.getGroupUIID(), castOther.getGroupUIID())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
        	.append(getGroupId())
        	.append(getGroupName())
            .append(getOrderId())
            .append(getGroupUIID())
            .toHashCode();
    }
    
    /**
     * Sort the groups using order id.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {
        Group group = (Group)o;
        
        return this.orderId - group.orderId;
    }

    //---------------------------------------------------------------------
    // Field Access Methods
    //---------------------------------------------------------------------
    /**
     * Return whether the target user is in this group or not.
     * @param learner the target user
     * @return boolean value to indicate whether the user is in.
     */
    public boolean hasLearner(User learner)
    {
    	return this.getUsers().contains(learner);
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    public boolean isNull()
    {
        return false;
    }
    
    public GroupDTO getGroupDTO(){
    	return new GroupDTO(this);
    }
    
    /** May this group be deleted or a user from this group deleted? It should not be 
     * deleted if there are tool sessions attached */
    public boolean mayBeDeleted() {
    	return getToolSessions().size() == 0;
    }

    /** Create a copy of this group, without copying the users or tool sessions. 
     * Copies any group to branch mappings, updating the group but not the activity. */
    @SuppressWarnings("unchecked")
	public Group createCopy(Grouping newGrouping) {
    	
    	Group newGroup = new Group(null, this.getGroupName(), this.getOrderId(), this.getGroupUIID(), 
    			newGrouping, null, null, null);
    	
    	if ( this.getBranchActivities() != null && this.getBranchActivities().size() > 0) {
    		newGroup.setBranchActivities(new HashSet());
			Iterator iter = this.getBranchActivities().iterator();
			while ( iter.hasNext() ) {
				GroupBranchActivityEntry oldEntry = (GroupBranchActivityEntry) iter.next();
				GroupBranchActivityEntry newEntry = new GroupBranchActivityEntry(null, 
						oldEntry.getEntryUIID(), newGroup, oldEntry.getBranchSequenceActivity(), oldEntry.getBranchingActivity());
				newGroup.getBranchActivities().add(newEntry);
			}
    	}

    	return newGroup;
    }
}
