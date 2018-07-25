package org.lamsfoundation.lams.monitoring.dto;

import java.io.Serializable;
import java.util.SortedSet;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents an overall branching activity. Used by the Teacher Chosen Grouping screen for allocating learners to
 * branches, and on the other branching screens to view the current branch -> group mappings
 */
public class BranchingDTO implements Serializable, Comparable {

    private Long branchActivityId;
    private String branchActivityName;
    private SortedSet<BranchDTO> branches;

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("branchActivityId", branchActivityId)
		.append("branchActivityName", branchActivityName).append("branches", branches).toString();
    }

    @Override
    public int compareTo(Object other) {
	BranchingDTO otherBranch = (BranchingDTO) other;
	return new CompareToBuilder().append(branchActivityId, otherBranch.branchActivityId)
		.append(branchActivityName, otherBranch.branchActivityName).toComparison();
    }

    public Long getBranchActivityId() {
	return branchActivityId;
    }

    public void setBranchActivityId(Long branchActivityId) {
	this.branchActivityId = branchActivityId;
    }

    public String getBranchActivityName() {
	return branchActivityName;
    }

    public void setBranchActivityName(String branchActivityName) {
	this.branchActivityName = branchActivityName;
    }

    public SortedSet<BranchDTO> getBranches() {
	return branches;
    }

    public void setBranches(SortedSet<BranchDTO> branches) {
	this.branches = branches;
    }

}
