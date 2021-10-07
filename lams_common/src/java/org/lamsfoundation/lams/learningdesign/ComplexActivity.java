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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.SortComparator;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.BranchActivityEntryDTO;
import org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy;
import org.lamsfoundation.lams.lesson.LearnerProgress;

@Entity
public abstract class ComplexActivity extends Activity implements Serializable {
    private static final long serialVersionUID = -6397701814181313412L;

    @Transient
    protected ComplexActivityStrategy activityStrategy;

    @OneToMany(mappedBy = "parentActivity", fetch = FetchType.EAGER)
    @Cascade(CascadeType.SAVE_UPDATE)
    @SortComparator(ActivityOrderComparator.class)
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<Activity> activities = new TreeSet<>(new ActivityOrderComparator());

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_activity_id")
    protected Activity defaultActivity;

    public ComplexActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom,
	    String languageFile, Boolean stopAfterActivity, Set<Activity> inputActivities, Set<Activity> activities,
	    Activity defaultActivity, Set<BranchActivityEntry> branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, branchActivityEntries);
	this.activities = activities;
    }

    /** default constructor */
    public ComplexActivity() {
	super();
    }

    /** minimal constructor */
    public ComplexActivity(Long activityId, Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, LearningDesign learningDesign, Grouping grouping, Integer activityTypeId,
	    Transition transitionTo, Transition transitionFrom, Set<Activity> activities) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom);
	this.activities = activities;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    public Set<Activity> getActivities() {
	return this.activities;
    }

    /**
     * Get the first activity in the sequence,or the default branch for a branching activity.
     * <p>
     * A Sequence activity is like a little learning design, and while is it being drawn all the the contained
     * activities may not have transitions between them. So Authoring needs to know what the first activity is!
     * <p>
     * A tool based branching activity has to have a default branch in case the conditions don't match to any other
     * branch.
     */
    public Activity getDefaultActivity() {
	return defaultActivity;
    }

    public void setDefaultActivity(Activity defaultActivity) {
	this.defaultActivity = defaultActivity;
    }

    public void setActivities(Set<Activity> activities) {
	this.activities = activities;
    }

    public void addActivity(Activity activity) {
	this.getActivities().add(activity);
    }

    /**
     * Return the requested child activity based on the id.
     *
     * @param activityId
     *            the requested activity id.
     * @return the child activity.
     */
    public Activity getChildActivityById(long activityId) {
	for (Iterator<Activity> i = this.activities.iterator(); i.hasNext();) {
	    Activity child = i.next();
	    if (child.getActivityId().longValue() == activityId) {
		return child;
	    }
	}
	return new NullActivity();
    }

    /**
     * Delegate to activity strategy to check up the status of all children.
     *
     * @param learnerProgress
     *            the progress data that record what has been completed
     * @return true if all children are completed.
     */
    public boolean areChildrenCompleted(LearnerProgress learnerProgress) {
	return activityStrategy.areChildrenCompleted(learnerProgress);
    }

    /**
     * <p>
     * Delegate to activity strategy to calculate what is the next activity within the parent activity.
     * </p>
     *
     * <b>Note:</b> The logic of what is the next activity here is progress engine specific now. Please see the
     * <code>ActivityStrategy</code> for details explanation of what is next.
     *
     * @return the next activity within a parent activity
     */
    public Activity getNextActivityByParent(Activity currentChild) {
	return activityStrategy.getNextActivityByParent(this, currentChild);
    }

    /**
     * Get all the tool activities in this activity. Called by Activity.getAllToolActivities(). Recursively get tool
     * activity from its children.
     */
    @Override
    protected void getToolActivitiesInActivity(SortedSet<ToolActivity> toolActivities) {
	for (Iterator<Activity> i = this.getActivities().iterator(); i.hasNext();) {
	    Activity child = i.next();
	    child.getToolActivitiesInActivity(toolActivities);
	}
    }

    @Override
    public Set<AuthoringActivityDTO> getAuthoringActivityDTOSet(ArrayList<BranchActivityEntryDTO> branchMappings,
	    String languageCode) {
	Set<AuthoringActivityDTO> dtoSet = new TreeSet<>(new ActivityDTOOrderComparator());
	dtoSet.add(new AuthoringActivityDTO(this, branchMappings, languageCode)); // add parent activity

	// add the DTO for all child activities
	for (Iterator<Activity> i = this.getActivities().iterator(); i.hasNext();) {
	    Activity child = i.next();
	    dtoSet.addAll(child.getAuthoringActivityDTOSet(branchMappings, languageCode));
	}

	return dtoSet;
    }

    public ComplexActivityStrategy getComplexActivityStrategy() {
	return activityStrategy;
    }

    protected void copyToNewComplexActivity(ComplexActivity newComplex, int uiidOffset) {
	copyToNewActivity(newComplex, uiidOffset);
	newComplex.setDefaultActivity(this.getDefaultActivity());
    }
}