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
import java.util.Iterator;
import java.util.Vector;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.strategy.ConditionGateActivityStrategy;
import org.lamsfoundation.lams.util.MessageService;

/**
 * Gate activity that is based on tools' output and conditions.
 *
 * @author Marcin Cieslak
 *
 */
@Entity
@DiscriminatorValue("14")
public class ConditionGateActivity extends GateActivity implements Serializable {
    private static final long serialVersionUID = 2054132139360279827L;

    /** default constructor */
    public ConditionGateActivity() {
	super.simpleActivityStrategy = new ConditionGateActivityStrategy(this);
    }

    /**
     * Makes a copy of the PermissionGateActivity for authoring, preview and monitoring enviornment
     *
     * @return PermissionGateActivity Returns a deep-copy of the originalActivity
     */
    @Override
    public Activity createCopy(int uiidOffset) {
	ConditionGateActivity newConditionGateActivity = new ConditionGateActivity();
	copyToNewActivity(newConditionGateActivity, uiidOffset);
	newConditionGateActivity.setGateOpen(false);
	newConditionGateActivity.setGateActivityLevelId(this.getGateActivityLevelId());
	newConditionGateActivity.setGateStopAtPrecedingActivity(this.isGateStopAtPrecedingActivity());

	if ((this.getBranchActivityEntries() != null) && (this.getBranchActivityEntries().size() > 0)) {
	    Iterator<BranchActivityEntry> iter = this.getBranchActivityEntries().iterator();
	    while (iter.hasNext()) {
		BranchActivityEntry oldEntry = iter.next();
		BranchActivityEntry newEntry = new BranchActivityEntry(null,
			LearningDesign.addOffset(oldEntry.getEntryUIID(), uiidOffset), null, newConditionGateActivity,
			null, oldEntry.getGateOpenWhenConditionMet());
		if (oldEntry.getCondition() != null) {
		    BranchCondition newCondition = oldEntry.getCondition().clone(uiidOffset);
		    newEntry.setCondition(newCondition);

		}
		newConditionGateActivity.getBranchActivityEntries().add(newEntry);
	    }
	}

	return newConditionGateActivity;

    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    @Override
    public boolean isNull() {
	return false;
    }

    @Override
    public Vector<ValidationErrorDTO> validateActivity(MessageService messageService) {
	Vector<ValidationErrorDTO> listOfValidationErrors = new Vector<ValidationErrorDTO>();

	if ((getInputActivities() == null) || (getInputActivities().size() == 0)) {
	    listOfValidationErrors
		    .add(new ValidationErrorDTO(ValidationErrorDTO.CONDITION_GATE_ACTVITY_TOOLINPUT_ERROR_CODE,
			    messageService.getMessage(ValidationErrorDTO.CONDITION_GATE_ACTVITY_TOOLINPUT),
			    this.getActivityUIID()));
	}

	boolean conditionsExist = false;
	if (getBranchActivityEntries() != null) {
	    for (BranchActivityEntry entry : getBranchActivityEntries()) {
		BranchCondition condition = entry.getCondition();
		if (condition == null) {
		    listOfValidationErrors
			    .add(new ValidationErrorDTO(ValidationErrorDTO.BRANCH_CONDITION_INVALID_ERROR_CODE,
				    messageService.getMessage(ValidationErrorDTO.BRANCH_CONDITION_INVALID),
				    this.getActivityUIID()));
		} else {
		    conditionsExist = true;
		    if (!condition.isValid()) {
			listOfValidationErrors
				.add(new ValidationErrorDTO(ValidationErrorDTO.BRANCH_CONDITION_INVALID_ERROR_CODE,
					messageService.getMessage(ValidationErrorDTO.BRANCH_CONDITION_INVALID),
					this.getActivityUIID()));
		    }
		}
	    }
	}

	if (!conditionsExist) {
	    listOfValidationErrors
		    .add(new ValidationErrorDTO(ValidationErrorDTO.CONDITION_GATE_ACTVITY_CONDITION_ERROR_CODE,
			    messageService.getMessage(ValidationErrorDTO.CONDITION_GATE_ACTVITY_CONDITION),
			    this.getActivityUIID()));
	}
	return listOfValidationErrors;
    }
}