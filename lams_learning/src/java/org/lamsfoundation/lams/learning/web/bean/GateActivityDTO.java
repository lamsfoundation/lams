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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.learning.web.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.BranchActivityEntryDTO;
import org.lamsfoundation.lams.learningdesign.dto.LibraryActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.ProgressActivityDTO;
import org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * DTO wrapping a normal Gate Activity class, with an extra "calculated" field added for the learning module's gate screen.
 *
 */
public class GateActivityDTO {

	private List expectedLearners;
	private GateActivity gateActivity;
	private boolean allowToPass;

	public GateActivityDTO(GateActivity gateActivity, List lessonLearners, boolean allowToPass) {
		this.gateActivity = gateActivity;
		expectedLearners = lessonLearners;
		this.allowToPass = allowToPass;
	}

	/**
	  * Temporary value of the expected number of learners. This may change every time
	  * the gate is knocked, and is NOT persisted to the database. It is calculated when this 
	  * DTO is created by the knockGate() method in LearnerService.
	  */
	public List getExpectedLearners() {
		return expectedLearners;
	}

	public void setExpectedLearners(List tempExpectedLearnerCount) {
		expectedLearners = tempExpectedLearnerCount;
	}

	@Override
	public boolean equals(Object other) {
		return gateActivity.equals(other);
	}

	public Integer getActivityCategoryID() {
		return gateActivity.getActivityCategoryID();
	}

	public Long getActivityId() {
		return gateActivity.getActivityId();
	}

	public Integer getActivityTypeId() {
		return gateActivity.getActivityTypeId();
	}

	public Integer getActivityUIID() {
		return gateActivity.getActivityUIID();
	}

	public Set getAllToolActivities() {
		return gateActivity.getAllToolActivities();
	}

	public Boolean getApplyGrouping() {
		return gateActivity.getApplyGrouping();
	}

	public Set<AuthoringActivityDTO> getAuthoringActivityDTOSet(ArrayList<BranchActivityEntryDTO> branchMappings,
			String languageCode) {
		return gateActivity.getAuthoringActivityDTOSet(branchMappings, languageCode);
	}

	public Date getCreateDateTime() {
		return gateActivity.getCreateDateTime();
	}

	public String getDescription() {
		return gateActivity.getDescription();
	}

	public Integer getGateActivityLevelId() {
		return gateActivity.getGateActivityLevelId();
	}

	public Boolean getGateOpen() {
		return gateActivity.getGateOpen();
	}

	public Group getGroupFor(User learner) {
		return gateActivity.getGroupFor(learner);
	}

	public Grouping getGrouping() {
		return gateActivity.getGrouping();
	}

	public Integer getGroupingSupportType() {
		return gateActivity.getGroupingSupportType();
	}

	public Integer getGroupingUIID() {
		return gateActivity.getGroupingUIID();
	}

	public String getHelpText() {
		return gateActivity.getHelpText();
	}

	public Set getInputActivities() {
		return gateActivity.getInputActivities();
	}

	public ArrayList<Integer> getInputActivityUIIDs() {
		return gateActivity.getInputActivityUIIDs();
	}

	public String getLanguageFile() {
		return gateActivity.getLanguageFile();
	}

	public LearningDesign getLearningDesign() {
		return gateActivity.getLearningDesign();
	}

	public LearningLibrary getLearningLibrary() {
		return gateActivity.getLearningLibrary();
	}

	public Activity getLibraryActivity() {
		return gateActivity.getLibraryActivity();
	}

	public LibraryActivityDTO getLibraryActivityDTO(String languageCode) {
		return gateActivity.getLibraryActivityDTO(languageCode);
	}

	public String getLibraryActivityUiImage() {
		return gateActivity.getLibraryActivityUiImage();
	}

	public Integer getOrderId() {
		return gateActivity.getOrderId();
	}

	public Activity getParentActivity() {
		return gateActivity.getParentActivity();
	}

	public Activity getParentBranch() {
		return gateActivity.getParentBranch();
	}

	public Integer getParentUIID() {
		return gateActivity.getParentUIID();
	}

	public ProgressActivityDTO getProgressActivityData() {
		return gateActivity.getProgressActivityData();
	}

	public Boolean getReadOnly() {
		return gateActivity.getReadOnly();
	}

	public SimpleActivityStrategy getSimpleActivityStrategy() {
		return gateActivity.getSimpleActivityStrategy();
	}

	public SystemTool getSystemTool() {
		return gateActivity.getSystemTool();
	}

	public String getTitle() {
		return gateActivity.getTitle();
	}

	public Integer getToolInputActivityUIID() {
		return gateActivity.getToolInputActivityUIID();
	}

	public Transition getTransitionFrom() {
		return gateActivity.getTransitionFrom();
	}

	public Transition getTransitionTo() {
		return gateActivity.getTransitionTo();
	}

	public Set getWaitingLearners() {
		return gateActivity.getWaitingLearners();
	}

	public Integer getXcoord() {
		return gateActivity.getXcoord();
	}

	public Integer getYcoord() {
		return gateActivity.getYcoord();
	}

	@Override
	public int hashCode() {
		return gateActivity.hashCode();
	}

	public boolean isActivityReadOnly() {
		return gateActivity.isActivityReadOnly();
	}

	public boolean isGateActivity() {
		return gateActivity.isGateActivity();
	}

	public boolean isSystemToolActivity() {
		return gateActivity.isSystemToolActivity();
	}

	@Override
	public String toString() {
		return gateActivity.toString();
	}

	/** Get the wrapped up gate activity - the web layer shouldn't use this call, it should use one of the other methods to get
	 * the gate's details. */
	public GateActivity getGateActivity() {
		return gateActivity;
	}

	/** Set the wrapped up gate activity */
	public void setGateActivity(GateActivity gateActivity) {
		this.gateActivity = gateActivity;
	}

	public boolean isPermissionGate() {
		return gateActivity.isPermissionGate();
	}

	public boolean isScheduleGate() {
		return gateActivity.isScheduleGate();
	}

	public boolean isSynchGate() {
		return gateActivity.isSynchGate();
	}

	public boolean isConditionGate() {
		return gateActivity.isConditionGate();
	}

	public boolean isSystemGate() {
		return gateActivity.isSystemGate();
	}

	public boolean getAllowToPass() {
		return getGateOpen() || allowToPass;
	}

	public void setAllowToPass(boolean allowToPass) {
		this.allowToPass = allowToPass;
	}
}