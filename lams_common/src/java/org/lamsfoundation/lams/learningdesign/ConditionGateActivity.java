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
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.strategy.ConditionGateActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.util.MessageService;

/**
 *  Gate activity that is based on tools' output and conditions.
 * @author Marcin Cieslak
 * @hibernate.class
 */
public class ConditionGateActivity extends GateActivity implements Serializable {
	/**
	 * List of the branch entries which open the gate.
	 */
	private Set<BranchActivityEntry> openingGateBranchEntries = new HashSet<BranchActivityEntry>();

	/** full constructor */
	public ConditionGateActivity(Long activityId, Integer id, String description, String title, Integer xcoord, Integer ycoord,
			Integer orderId, Boolean defineLater, java.util.Date createDateTime, LearningLibrary learningLibrary,
			Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
			Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom, String languageFile,
			Boolean stopAfterActivity, Set inputActivities, Integer gateActivityLevelId, Set waitingLearners, SystemTool sysTool) {
		super(activityId, id, description, title, xcoord, ycoord, orderId, defineLater, createDateTime, learningLibrary,
				parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
				transitionFrom, languageFile, stopAfterActivity, inputActivities, gateActivityLevelId, waitingLearners, sysTool);
		super.simpleActivityStrategy = new ConditionGateActivityStrategy(this);
	}

	/** default constructor */
	public ConditionGateActivity() {
		super.simpleActivityStrategy = new ConditionGateActivityStrategy(this);
	}

	/** minimal constructor */
	public ConditionGateActivity(Long activityId, Boolean defineLater, java.util.Date createDateTime,
			org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
			org.lamsfoundation.lams.learningdesign.Activity parentActivity,
			org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
			org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
			Transition transitionFrom, Integer gateActivityLevelId, Set waitingLearners) {
		super(activityId, defineLater, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
				transitionTo, transitionFrom, gateActivityLevelId, waitingLearners);
		super.simpleActivityStrategy = new ConditionGateActivityStrategy(this);
	}

	/**
	 * Makes a copy of the PermissionGateActivity for authoring, preview and monitoring enviornment 
	 * @return PermissionGateActivity Returns a deep-copy of the originalActivity
	 */
	@Override
	public Activity createCopy(int uiidOffset) {
		ConditionGateActivity newConditionGateActivity = new ConditionGateActivity();
		copyToNewActivity(newConditionGateActivity, uiidOffset);
		newConditionGateActivity.setGateOpen(new Boolean(false));
		newConditionGateActivity.setGateActivityLevelId(this.getGateActivityLevelId());
		return newConditionGateActivity;

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
	}

	/**
	 * @see org.lamsfoundation.lams.util.Nullable#isNull()
	 */
	public boolean isNull() {
		return false;
	}

	public Set<BranchActivityEntry> getOpeningGateBranchEntries() {
		return openingGateBranchEntries;
	}

	public void setOpeningGateBranchEntries(Set<BranchActivityEntry> openingGateConditions) {
		openingGateBranchEntries = openingGateConditions;
	}

	@Override
	public Vector validateActivity(MessageService messageService) {
		Vector listOfValidationErrors = new Vector();

		if (getInputActivities() == null || getInputActivities().size() == 0) {
			listOfValidationErrors.add(new ValidationErrorDTO(ValidationErrorDTO.CONDITION_GATE_ACTVITY_TOOLINPUT_ERROR_CODE,
					messageService.getMessage(ValidationErrorDTO.CONDITION_GATE_ACTVITY_TOOLINPUT), this.getActivityUIID()));
		}

		boolean conditionsExist = false;
		if (getOpeningGateBranchEntries() != null) {
			for (BranchActivityEntry entry : getOpeningGateBranchEntries()) {
				BranchCondition condition = entry.getCondition();
				if (condition == null) {
					listOfValidationErrors.add(new ValidationErrorDTO(ValidationErrorDTO.BRANCH_CONDITION_INVALID_ERROR_CODE,
							messageService.getMessage(ValidationErrorDTO.BRANCH_CONDITION_INVALID), this.getActivityUIID()));
				}
				else {
					conditionsExist = true;
					if (!condition.isValid()) {
						listOfValidationErrors.add(new ValidationErrorDTO(ValidationErrorDTO.BRANCH_CONDITION_INVALID_ERROR_CODE,
								messageService.getMessage(ValidationErrorDTO.BRANCH_CONDITION_INVALID), this.getActivityUIID()));
					}
				}
			}
		}

		if (!conditionsExist) {
			listOfValidationErrors.add(new ValidationErrorDTO(ValidationErrorDTO.CONDITION_GATE_ACTVITY_CONDITION_ERROR_CODE,
					messageService.getMessage(ValidationErrorDTO.CONDITION_GATE_ACTVITY_CONDITION), this.getActivityUIID()));
		}
		return listOfValidationErrors;
	}
}