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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.usermanagement.User;

@Entity
public abstract class GateActivity extends SimpleActivity implements Serializable, ISystemToolActivity {
    private static final long serialVersionUID = -5436654342587065425L;

    public static final int LEARNER_GATE_LEVEL = 1;

    public static final int GROUP_GATE_LEVEL = 2;

    public static final int CLASS_GATE_LEVEL = 3;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "system_tool_id")
    private SystemTool systemTool;

    @Column(name = "gate_activity_level_id")
    private Integer gateActivityLevelId;

    @Column(name = "gate_open_flag")
    private Boolean gateOpen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gate_open_user")
    private User gateOpenUser;

    @Column(name = "gate_open_time")
    private Date gateOpenTime;

    @Column(name = "gate_stop_at_preceding_activity")
    private boolean gateStopAtPrecedingActivity;

    /**
     * The learners who passed the gate.
     */
    @ManyToMany
    @JoinTable(name = "lams_gate_allowed_learners", joinColumns = @JoinColumn(name = "activity_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> allowedToPassLearners = new HashSet<>();

    /** full constructor */
    public GateActivity(Long activityId, Integer id, String description, String title, Integer xcoord, Integer ycoord,
	    Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary, Activity parentActivity,
	    Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign, Grouping grouping,
	    Integer activityTypeId, Transition transitionTo, Transition transitionFrom, String languageFile,
	    Boolean stopAfterActivity, Set<Activity> inputActivities, Integer gateActivityLevelId, SystemTool sysTool,
	    Set<BranchActivityEntry> branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, branchActivityEntries);
	this.gateActivityLevelId = gateActivityLevelId;
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
	    Transition transitionFrom, Integer gateActivityLevelId) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom);
	this.gateActivityLevelId = gateActivityLevelId;
    }

    public Integer getGateActivityLevelId() {
	return gateActivityLevelId;
    }

    public void setGateActivityLevelId(Integer gateActivityLevelId) {
	this.gateActivityLevelId = gateActivityLevelId;
    }

    public Boolean getGateOpen() {
	return gateOpen;
    }

    public void setGateOpen(Boolean gateOpen) {
	this.gateOpen = gateOpen;
    }

    public User getGateOpenUser() {
	return gateOpenUser;
    }

    public void setGateOpenUser(User gateOpenUser) {
	this.gateOpenUser = gateOpenUser;
    }

    public Date getGateOpenTime() {
	return gateOpenTime;
    }

    public void setGateOpenTime(Date gateOpenTime) {
	this.gateOpenTime = gateOpenTime;
    }
    // ---------------------------------------------------------------------
    // Domain service methods
    // ---------------------------------------------------------------------

    /**
     * Delegate to strategy class to calculate whether we should open the gate for this learner.
     *
     * @param learner
     *            the learner who wants to go through the gate.
     * @return the gate is open or closed.
     */
    public boolean shouldOpenGateFor(User learner, int expectedLearnerCount, int waitingLearnerCount) {
	// by default, we close the gate
	if (getGateOpen() == null) {
	    this.setGateOpen(false);
	}
	return ((GateActivityStrategy) simpleActivityStrategy).shouldOpenGateFor(learner, expectedLearnerCount,
		waitingLearnerCount);
    }

    // ---------------------------------------------------------------------
    // Helper methods
    // ---------------------------------------------------------------------
    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    @Override
    public SystemTool getSystemTool() {
	return systemTool;
    }

    @Override
    public void setSystemTool(SystemTool systemTool) {
	this.systemTool = systemTool;
    }

    protected void copyToNewActivity(GateActivity newActivity, int uiidOffset) {

	super.copyToNewActivity(newActivity, uiidOffset);
	newActivity.setSystemTool(this.getSystemTool());
    }

    public Set<User> getAllowedToPassLearners() {
	return allowedToPassLearners;
    }

    public void setAllowedToPassLearners(Set<User> allowedToPassLearners) {
	this.allowedToPassLearners = allowedToPassLearners;
    }

    public boolean isGateStopAtPrecedingActivity() {
	return gateStopAtPrecedingActivity;
    }

    public void setGateStopAtPrecedingActivity(boolean stopAtPrecedingActivity) {
	this.gateStopAtPrecedingActivity = stopAtPrecedingActivity;
    }
}