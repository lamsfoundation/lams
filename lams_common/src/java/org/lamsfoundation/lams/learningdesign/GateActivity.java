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
/* $Id$ */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @hibernate.class
 */
public abstract class GateActivity extends SimpleActivity implements Serializable, ISystemToolActivity {

    public static final int LEARNER_GATE_LEVEL = 1;

    public static final int GROUP_GATE_LEVEL = 2;

    public static final int CLASS_GATE_LEVEL = 3;

    /** persistent field */
    private SystemTool systemTool;

    /** persistent field */
    private Integer gateActivityLevelId;

    /** persistent field */
    private Boolean gateOpen;

    /**
     * The learners who are waiting at the gate.
     */
    private Set<User> waitingLearners;
    /**
     * The learners who passed the gate.
     */
    private Set<User> allowedToPassLearners;
    /**
     * The learners who reached the gate.
     */
    private Set<GateUser> allGateUsers;

    /** full constructor */
    public GateActivity(Long activityId, Integer id, String description, String title, Integer xcoord, Integer ycoord,
	    Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary, Activity parentActivity,
	    Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign, Grouping grouping,
	    Integer activityTypeId, Transition transitionTo, Transition transitionFrom, String languageFile,
	    Boolean stopAfterActivity, Set inputActivities, Integer gateActivityLevelId, Set waitingLearners,
	    SystemTool sysTool, Set branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, branchActivityEntries);
	this.gateActivityLevelId = gateActivityLevelId;
	this.waitingLearners = waitingLearners;
	systemTool = sysTool;
    }

    /** default constructor */
    public GateActivity() {
    }

    /** minimal constructor */
    public GateActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Integer gateActivityLevelId, Set waitingLearners) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom);
	this.gateActivityLevelId = gateActivityLevelId;
	this.waitingLearners = waitingLearners;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="gate_activity_level_id"
     * 
     */
    public Integer getGateActivityLevelId() {
	return gateActivityLevelId;
    }

    public void setGateActivityLevelId(Integer gateActivityLevelId) {
	this.gateActivityLevelId = gateActivityLevelId;
    }

    /**
     * @return Returns the waitingLearners.
     */
    public Set getWaitingLearners() {
	if (waitingLearners == null) {
	    this.setWaitingLearners(new HashSet());
	}
	return waitingLearners;
    }

    /**
     * @param waitingLearners
     *            The waitingLearners to set.
     */
    public void setWaitingLearners(Set waitingLearners) {
	this.waitingLearners = waitingLearners;
    }

    public Boolean getGateOpen() {
	return gateOpen;
    }

    public void setGateOpen(Boolean gateOpen) {
	this.gateOpen = gateOpen;
    }

    // ---------------------------------------------------------------------
    // Domain service methods
    // ---------------------------------------------------------------------

    /**
     * Add a learner into the waiting or allowed to pass list.
     * 
     * @param learner
     *            the new learner.
     * @param isAllowedToPass
     *            is the learner allowed to pass the gate
     */
    public void addLeaner(User learner, boolean isAllowedToPass) {
	boolean add = true;
	GateUser gateUser = new GateUser();
	gateUser.setUser(learner);

	if (getWaitingLearners().contains(learner)) {
	    if (isAllowedToPass) {
		gateUser.setAllowedToPass(false);
		getAllGateUsers().remove(gateUser);
		getWaitingLearners().remove(learner);
	    } else {
		add = false;
	    }
	} else if (getAllowedToPassLearners().contains(learner)) {
	    // Once a learner was allowed to pass, he/she may never be forbidden back.
	    add = false;
	}

	if (add) {
	    gateUser.setAllowedToPass(isAllowedToPass);
	    getAllGateUsers().add(gateUser);
	    if (isAllowedToPass) {
		getAllowedToPassLearners().add(learner);
	    } else {
		getWaitingLearners().add(learner);
	    }
	}
    }

    /**
     * Delegate to strategy class to calculate whether we should open the gate for this learner.
     * 
     * @param learner
     *            the learner who wants to go through the gate.
     * @return the gate is open or closed.
     */
    public boolean shouldOpenGateFor(User learner, List lessonLearners) {
	// by default, we close the gate
	if (getGateOpen() == null) {
	    this.setGateOpen(Boolean.FALSE);
	}

	return ((GateActivityStrategy) simpleActivityStrategy).shouldOpenGateFor(learner, lessonLearners);
    }

    /**
     * Force the gate to open, irrespective of the strategy. Used for preview. Opens the gate and clears any "waiting"
     * list.
     * 
     * @return the gate is open or closed.
     */
    public boolean forceGateOpen() {
	setGateOpen(Boolean.TRUE);
	getAllGateUsers().clear();
	return true;
    }

    // ---------------------------------------------------------------------
    // Helper methods
    // ---------------------------------------------------------------------
    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    public SystemTool getSystemTool() {
	return systemTool;
    }

    public void setSystemTool(SystemTool systemTool) {
	this.systemTool = systemTool;
    }

    protected void copyToNewActivity(GateActivity newActivity, int uiidOffset) {

	super.copyToNewActivity(newActivity, uiidOffset);
	newActivity.setSystemTool(this.getSystemTool());
    }

    public Set<User> getAllowedToPassLearners() {
	if (allowedToPassLearners == null) {
	    this.setAllowedToPassLearners(new HashSet<User>());
	}
	return allowedToPassLearners;
    }

    public void setAllowedToPassLearners(Set<User> allowedToPassLearners) {
	this.allowedToPassLearners = allowedToPassLearners;
    }

    public Set<GateUser> getAllGateUsers() {
	if (allGateUsers == null) {
	    this.setAllGateUsers(new HashSet<GateUser>());
	}
	return allGateUsers;
    }

    public void setAllGateUsers(Set<GateUser> allLearners) {
	allGateUsers = allLearners;
    }

}
