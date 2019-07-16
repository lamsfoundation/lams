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

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.lamsfoundation.lams.learningdesign.dto.GroupDTO;
import org.lamsfoundation.lams.tool.GroupedToolSession;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Nullable;

@Entity
@Table(name = "lams_group")
public class Group implements Serializable, Nullable, Comparable<Group> {
    private static final long serialVersionUID = 5654513153596816881L;

    public final static int STAFF_GROUP_ORDER_ID = 2;
    public final static String NAME_OF_STAFF_GROUP = "Staff Group";

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "order_id")
    private int orderId;

    /**
     * Authoring generated value. Unique per LearningDesign.
     */
    @Column(name = "group_ui_id")
    private Integer groupUIID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grouping_id", updatable = false)
    private Grouping grouping;

    @ManyToMany
    @JoinTable(name = "lams_user_group", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @BatchSize(size = 20)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "sessionGroup")
    @BatchSize(size = 20)
    private Set<GroupedToolSession> toolSessions = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<BranchActivityEntry> branchActivities = new HashSet<>();

    // ---------------------------------------------------------------------
    // Object creation Methods
    // ---------------------------------------------------------------------

    /** full constructor */
    public Group(Long groupId, String groupName, int orderId, Integer groupUIID, Grouping grouping, Set<User> users,
	    Set<GroupedToolSession> toolSessions, Set<BranchActivityEntry> branchActivities) {
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
     * Creation Constructor for initializing learner group without tool sessions The order is generated using
     * synchornize method on grouping. If a group of this name already exists, returns null.
     *
     * @param grouping
     *            the grouping this group belongs to.
     * @param users
     *            the users in this group.
     * @return the new learner group
     */
    public static Group createLearnerGroup(Grouping grouping, String groupName, Set<User> users) {
	int nextOrderId = grouping.getNextGroupOrderIdCheckName(groupName);
	if (nextOrderId > -1) {
	    return new Group(null, groupName, nextOrderId, null, grouping, users, new HashSet<GroupedToolSession>(),
		    null);
	}
	return null;
    }

    /**
     * Creation Constructor for initializing learner group with tool sessions The order is generated using synchornize
     * method on grouping. If a group of this name already exists, returns null.
     *
     * @param grouping
     *            the grouping this group belongs to.
     * @param name
     *            of this group
     * @param users
     *            the users in this group.
     * @param toolSessions
     *            all tool sessions included in this group
     * @return the new learner group
     */
    public static Group createLearnerGroupWithToolSession(Grouping grouping, String groupName, Set<User> users,
	    Set<GroupedToolSession> toolSessions) {
	int nextOrderId = grouping.getNextGroupOrderIdCheckName(groupName);
	if (nextOrderId > -1) {
	    return new Group(null, groupName, nextOrderId, null, grouping, users, toolSessions, null);
	}
	return null;
    }

    /**
     * Creation constructor for initializing staff group. The order is created using default constant.
     *
     * @param grouping
     *            the grouping this group belongs to.
     * @param name
     *            of this group
     * @param staffs
     *            the users in this group.
     *
     * @return the new staff group.
     */
    public static Group createStaffGroup(Grouping grouping, String groupName, Set<User> staffs) {
	return new Group(null, groupName, Group.STAFF_GROUP_ORDER_ID, null, grouping, staffs,
		new HashSet<GroupedToolSession>(), null);
    }

    /** default constructor */
    public Group() {
    }
    // ---------------------------------------------------------------------
    // Field Access Methods
    // ---------------------------------------------------------------------

    public Long getGroupId() {
	return this.groupId;
    }

    public String getGroupName() {
	return groupName;
    }

    public void setGroupName(String groupName) {
	this.groupName = groupName;
    }

    public void setGroupId(Long groupId) {
	this.groupId = groupId;
    }

    public int getOrderId() {
	return this.orderId;
    }

    public void setOrderId(int orderId) {
	this.orderId = orderId;
    }

    public Integer getGroupUIID() {
	return this.groupUIID;
    }

    public void setGroupUIID(Integer uiId) {
	this.groupUIID = uiId;
    }

    public org.lamsfoundation.lams.learningdesign.Grouping getGrouping() {
	return this.grouping;
    }

    public void setGrouping(org.lamsfoundation.lams.learningdesign.Grouping grouping) {
	this.grouping = grouping;
    }

    public Set<User> getUsers() {
	return this.users;
    }

    public void setUsers(Set<User> userGroups) {
	this.users = userGroups;
    }

    public Set<GroupedToolSession> getToolSessions() {
	return this.toolSessions;
    }

    public void setToolSessions(Set<GroupedToolSession> toolSessions) {
	this.toolSessions = toolSessions;
    }

    /**
     * Maps the branch activities appropriate for this Group. Normally there is only one branch per branching activity
     * that is applicable to a group, but this may be changed in the future. If the group is applied to multiple
     * branching activities, then there will be multiple branches - one for each branching activity.
     */
    public Set<BranchActivityEntry> getBranchActivities() {
	return this.branchActivities;
    }

    public void setBranchActivities(Set<BranchActivityEntry> branchActivities) {
	this.branchActivities = branchActivities;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("groupId", getGroupId()).append("groupName", getGroupName())
		.append("groupUIID", getGroupUIID()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof Group)) {
	    return false;
	}
	Group castOther = (Group) other;
	return new EqualsBuilder().append(this.getOrderId(), castOther.getOrderId())
		.append(this.getGroupId(), castOther.getGroupId()).append(this.getGroupName(), castOther.getGroupName())
		.append(this.getGroupUIID(), castOther.getGroupUIID()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getGroupId()).append(getGroupName()).append(getOrderId())
		.append(getGroupUIID()).toHashCode();
    }

    /**
     * Sort the groups using order id.
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Group group) {
	return new CompareToBuilder().append(this.getOrderId(), group.getOrderId())
		.append(this.getGroupId(), group.getGroupId()).append(this.getGroupName(), group.getGroupName())
		.append(this.getGroupUIID(), group.getGroupUIID()).toComparison();
    }

    // ---------------------------------------------------------------------
    // Field Access Methods
    // ---------------------------------------------------------------------
    /**
     * Return whether the target user is in this group or not.
     *
     * @param learner
     *            the target user
     * @return boolean value to indicate whether the user is in.
     */
    public boolean hasLearner(User learner) {
	return this.getUsers().contains(learner);
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    @Override
    public boolean isNull() {
	return false;
    }

    public GroupDTO getGroupDTO(boolean setupUserList) {
	return new GroupDTO(this, setupUserList);
    }

    /**
     * May this group be deleted or a user from this group deleted? It should not be deleted if there are tool sessions
     * attached
     */
    public boolean mayBeDeleted() {
	return getToolSessions().size() == 0;
    }

    /**
     * May this group be deleted or a user from this group deleted? It should not be deleted if there are tool sessions
     * attached
     */
    public boolean isUsedForActivity() {
	return getToolSessions().size() == 0;
    }

    /**
     * Create a copy of this group, without copying the users or tool sessions. Copies any group to branch mappings,
     * updating the group but not the activity.
     */
    public Group createCopy(Grouping newGrouping) {
	Group newGroup = new Group(null, this.getGroupName(), this.getOrderId(), this.getGroupUIID(), newGrouping, null,
		null, null);
	return newGroup;
    }

    /**
     * Allocate this group to the given branch, in a branching activity. This creates the BranchActivityEntry record and
     * adds it to the branchActivities set. EntryUIID will only be populated if this is called from authoring
     */
    public BranchActivityEntry allocateBranchToGroup(Integer entryUIID, SequenceActivity branch,
	    BranchingActivity branchingActivity) {
	BranchActivityEntry entry = new BranchActivityEntry(null, entryUIID, branch, branchingActivity, this);
	if (getBranchActivities() == null) {
	    setBranchActivities(new HashSet<BranchActivityEntry>());
	}
	getBranchActivities().add(entry);
	return entry;
    }

    /**
     * Remove the branch with which this group is associated. Actually calls the SequenceActivity to do the removal as
     * it is the SequenceActivity that has the hibernate cascade. So its the activity that must be saved, not the group.
     * Method left here to make it easier to find.
     */
    public void removeGroupFromBranch(SequenceActivity branch) {
	branch.removeGroupFromBranch(this);
    }
}
