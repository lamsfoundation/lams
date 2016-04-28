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
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.strategy.SequenceActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Manpreet Minhas
 *
 */
public class SequenceActivity extends ComplexActivity implements Serializable, ISystemToolActivity {

    private static Logger log = Logger.getLogger(SequenceActivity.class);

    private Set<BranchActivityEntry> branchEntries;
    private SystemTool systemTool;

    /** full constructor */
    public SequenceActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom,
	    String languageFile, Boolean stopAfterActivity, Set inputActivities, Set activities,
	    Activity defaultActivity, Set<BranchActivityEntry> branchEntries, SystemTool systemTool,
	    Set branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, activities, defaultActivity,
		branchActivityEntries);
	super.activityStrategy = new SequenceActivityStrategy(this);
	this.branchEntries = branchEntries;
	this.systemTool = systemTool;
    }

    /** default constructor */
    public SequenceActivity() {
	super.activityStrategy = new SequenceActivityStrategy(this);
    }

    /** minimal constructor */
    public SequenceActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, SortedSet activities) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom, activities);
	super.activityStrategy = new SequenceActivityStrategy(this);
    }

    /**
     * Makes a copy of the SequenceActivity for authoring, preview and monitoring environment
     * 
     * @return SequenceActivity Returns a deep-copy of the originalActivity
     */
    public Activity createCopy(int uiidOffset) {
	SequenceActivity newSequenceActivity = new SequenceActivity();
	copyToNewComplexActivity(newSequenceActivity, uiidOffset);
	newSequenceActivity.defaultActivity = this.defaultActivity;
	newSequenceActivity.systemTool = this.systemTool;

	if (this.getBranchEntries() != null && this.getBranchEntries().size() > 0) {
	    newSequenceActivity.setBranchEntries(new HashSet());
	    Iterator iter = this.getBranchEntries().iterator();
	    while (iter.hasNext()) {
		BranchActivityEntry oldEntry = (BranchActivityEntry) iter.next();
		BranchActivityEntry newEntry = new BranchActivityEntry(null,
			LearningDesign.addOffset(oldEntry.getEntryUIID(), uiidOffset), newSequenceActivity,
			oldEntry.getBranchingActivity(), oldEntry.getGroup());
		if (oldEntry.getCondition() != null) {
		    BranchCondition newCondition = oldEntry.getCondition().clone(uiidOffset);
		    newEntry.setCondition(newCondition);

		}
		newSequenceActivity.getBranchEntries().add(newEntry);
	    }
	}

	return newSequenceActivity;
    }

    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    public boolean isNull() {
	return false;
    }

    /**
     *
     *
     */
    public SystemTool getSystemTool() {
	return systemTool;
    }

    public void setSystemTool(SystemTool systemTool) {
	this.systemTool = systemTool;
    }

    /**
     * Get the set of the branch to group mappings used for this branching activity. The set contains
     * BranchActivityEntry entries
     * 
     *
     *
     *
     */
    public Set<BranchActivityEntry> getBranchEntries() {
	return branchEntries;
    }

    public void setBranchEntries(Set branchEntries) {
	this.branchEntries = branchEntries;
    }

    /** Get the groups related to this sequence activity, related via the BranchEntries. */
    public SortedSet<Group> getGroupsForBranch() {

	Set<BranchActivityEntry> mappingEntries = getBranchEntries();
	TreeSet<Group> sortedGroups = new TreeSet<Group>();

	if (mappingEntries != null) {
	    Iterator mappingIter = mappingEntries.iterator();
	    while (mappingIter.hasNext()) {
		// Not all the BranchEntries have groups - when in preview, if the user selects a different
		// branch to their expected branch then a group may not exist yet.
		Group group = ((BranchActivityEntry) mappingIter.next()).getGroup();
		if (group != null) {
		    sortedGroups.add(group);
		}
	    }
	}

	return sortedGroups;
    }

    /**
     * For chosen and tool based branching, we expect there to be only one group per branch, so get the single group. If
     * there is more than one group, put out a warning and return the first group for this sequence activity, related
     * via the BranchEntries
     */
    public Group getSoleGroupForBranch() {

	SortedSet<Group> groups = getGroupsForBranch();
	if (groups.size() > 1) {
	    log.warn("Branch " + this + " has more than one group. This is unexpected. Using only the first group.");
	}
	Iterator iter = groups.iterator();
	if (iter.hasNext()) {
	    return (Group) iter.next();
	}

	return null;
    }

    /**
     * Validate the sequence activity. If the sequence is part of an optional activity then there must be children. If a
     * sequence is part of branching then it may be empty but if there is any child activities then the default activity
     * (the first activity in the sequence) must be set.
     * 
     * @return error message key
     */
    public Vector validateActivity(MessageService messageService) {
	Vector listOfValidationErrors = new Vector();
	if (getActivities() != null && getActivities().size() > 0 && getDefaultActivity() == null) {
	    listOfValidationErrors.add(
		    new ValidationErrorDTO(ValidationErrorDTO.SEQUENCE_ACTIVITY_MUST_HAVE_FIRST_ACTIVITY_ERROR_CODE,
			    messageService.getMessage(ValidationErrorDTO.SEQUENCE_ACTIVITY_MUST_HAVE_FIRST_ACTIVITY),
			    this.getActivityUIID()));
	}
	return listOfValidationErrors;
    }

    /**
     * Remove the entry mapping the given group to this branch. Need to remove it from the branch end, as it is the
     * branch end that has the all-delete-orphan cascade (LDEV-1766)
     */
    public void removeGroupFromBranch(Group group) {
	if (getBranchEntries() != null) {
	    Iterator iter = getBranchEntries().iterator();
	    while (iter.hasNext()) {
		BranchActivityEntry object = (BranchActivityEntry) iter.next();
		if (object.getGroup().equals(group)) {
		    iter.remove();
		}
	    }
	}
    }

}
