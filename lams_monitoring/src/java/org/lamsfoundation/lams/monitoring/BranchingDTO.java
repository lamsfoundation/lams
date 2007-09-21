package org.lamsfoundation.lams.monitoring;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;

/** Represents an overall branching activity. Used by the Teacher Chosen Grouping screen for 
 * allocating learners to branches, and on the other branching screens to view the current branch -> group mappings
 */
public class BranchingDTO implements Serializable, Comparable {
	
	private Long branchActivityId;
	private String branchActivityName;
	private SortedSet<BranchDTO> branches;
	
	public String toString() {
		return new ToStringBuilder(this)
			.append("branchActivityId", branchActivityId)
			.append("branchActivityName", branchActivityName)
			.append("branches", branches)
			.toString();
	}
	
	public int compareTo(Object other) {
		BranchingDTO otherBranch = (BranchingDTO) other;
		return new CompareToBuilder()
			.append(branchActivityId, otherBranch.branchActivityId)
			.append(branchActivityName, otherBranch.branchActivityName)
			.toComparison();
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
