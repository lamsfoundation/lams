package org.lamsfoundation.lams.monitoring.dto;

import java.io.Serializable;
import java.util.SortedSet;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;

/**
 * Represents a single branch within a branching activity. Used by the Teacher Chosen Grouping screen for allocating
 * learners to branches, and on the other branching screens to view the current branch -> group mappings
 * <p>
 * A branch is equivalent to a SequenceActivity within a BranchingActivity
 */
public class BranchDTO implements Serializable, Comparable {

    private Long branchId;
    private String branchName;
    private SortedSet<Group> groups;

    public BranchDTO(SequenceActivity activity, SortedSet<Group> groups) {
	this.branchId = activity.getActivityId();
	this.branchName = activity.getTitle();
	this.groups = groups;
    }

    /** Get the activity id for the sequence activity that is equivalent to this branch */
    public Long getBranchId() {
	return branchId;
    }

    public void setBranchId(Long branchId) {
	this.branchId = branchId;
    }

    /** The branch name is the title of the sequence activity that is equivalent to this branch */
    public String getBranchName() {
	return branchName;
    }

    public void setBranchName(String branchName) {
	this.branchName = branchName;
    }

    /**
     * Get the groups currently assigned to this branch. For a teacher chosen branching activity, there will be one
     * group per branch.
     *
     * @return
     */
    public SortedSet<Group> getGroups() {
	return groups;
    }

    public void setGroups(SortedSet<Group> groups) {
	this.groups = groups;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("branchName", branchName).append("branchId", branchId)
		.append("groups", groups).toString();
    }

    @Override
    public int compareTo(Object other) {
	BranchDTO otherBranch = (BranchDTO) other;
	return new CompareToBuilder().append(branchName, otherBranch.branchName).append(branchId, otherBranch.branchId)
		.toComparison();
    }

}
