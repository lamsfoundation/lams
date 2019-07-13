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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Mitchell Seaton
 */
@Entity
@DiscriminatorValue("12")
public class ToolBranchingActivity extends BranchingActivity implements Serializable {

    private static final long serialVersionUID = 8343443197068061495L;

    @Column(name = "branching_ordered_asc")
    private Boolean branchingOrderedAsc;

    /** default constructor */
    public ToolBranchingActivity() {
	super();
    }

    /**
     * Makes a copy of the BranchingActivity for authoring, preview and monitoring environment
     *
     * @return BranchingActivity Returns a deep-copy of the originalActivity
     */
    @Override
    public Activity createCopy(int uiidOffset) {

	ToolBranchingActivity newBranchingActivity = new ToolBranchingActivity();
	copyBranchingFields(newBranchingActivity);
	newBranchingActivity.branchingOrderedAsc = this.branchingOrderedAsc;
	copyToNewComplexActivity(newBranchingActivity, uiidOffset);

	// Any grouping attached to a teacher chosen branching was either a runtime grouping
	// because we are running a runtime copy of a design, or some rubbish left from a bug in authoring.
	// We won't actually want this group, so remove references to it and they will be set up
	// again when a lesson is started.
	newBranchingActivity.setGrouping(null);
	newBranchingActivity.setGroupingUIID(null);
	newBranchingActivity.setApplyGrouping(false);

	return newBranchingActivity;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    /**
     * Validate the tool based branching activity. All branching activities should have at least one branch and the
     * default activity must be set for a tool based branch and all conditions must be valid (as determined by
     * ToolCondition).
     *
     * @return error message key
     */
    @Override
    public Vector<ValidationErrorDTO> validateActivity(MessageService messageService) {
	Vector<ValidationErrorDTO> listOfValidationErrors = new Vector<>();

	if (getDefaultActivity() == null) {
	    listOfValidationErrors.add(
		    new ValidationErrorDTO(ValidationErrorDTO.BRANCHING_ACTIVITY_MUST_HAVE_DEFAULT_BRANCH_ERROR_CODE,
			    messageService.getMessage(ValidationErrorDTO.BRANCHING_ACTIVITY_MUST_HAVE_DEFAULT_BRANCH),
			    this.getActivityUIID()));
	}

	if (getInputActivities() == null || getInputActivities().size() == 0) {
	    listOfValidationErrors.add(new ValidationErrorDTO(ValidationErrorDTO.BRANCHING_ACTVITY_TOOLINPUT_ERROR_CODE,
		    messageService.getMessage(ValidationErrorDTO.BRANCHING_ACTVITY_TOOLINPUT), this.getActivityUIID()));
	}

	if (getActivities() == null || getActivities().size() == 0) {
	    listOfValidationErrors
		    .add(new ValidationErrorDTO(ValidationErrorDTO.BRANCHING_ACTIVITY_MUST_HAVE_A_BRANCH_ERROR_CODE,
			    messageService.getMessage(ValidationErrorDTO.BRANCHING_ACTIVITY_MUST_HAVE_A_BRANCH),
			    this.getActivityUIID()));
	} else {
	    boolean conditionsExist = false;
	    Iterator<Activity> actIterator = getActivities().iterator();
	    while (actIterator.hasNext()) {
		SequenceActivity branch = (SequenceActivity) actIterator.next();
		if (branch.getBranchEntries() != null) {
		    Iterator<BranchActivityEntry> condIterator = branch.getBranchEntries().iterator();
		    while (condIterator.hasNext()) {
			BranchActivityEntry entry = condIterator.next();
			BranchCondition condition = entry.getCondition();
			if (condition == null) {
			    listOfValidationErrors
				    .add(new ValidationErrorDTO(ValidationErrorDTO.BRANCH_CONDITION_INVALID_ERROR_CODE,
					    messageService.getMessage(ValidationErrorDTO.BRANCH_CONDITION_INVALID),
					    this.getActivityUIID()));
			} else {
			    conditionsExist = true;
			    if (!condition.isValid()) {
				listOfValidationErrors.add(
					new ValidationErrorDTO(ValidationErrorDTO.BRANCH_CONDITION_INVALID_ERROR_CODE,
						messageService.getMessage(ValidationErrorDTO.BRANCH_CONDITION_INVALID),
						this.getActivityUIID()));
			    }
			}
		    }
		}
	    }
	    if (!conditionsExist) {
		listOfValidationErrors
			.add(new ValidationErrorDTO(ValidationErrorDTO.BRANCHING_ACTVITY_TOOLCONDITION_ERROR_CODE,
				messageService.getMessage(ValidationErrorDTO.BRANCHING_ACTVITY_TOOLCONDITION),
				this.getActivityUIID()));
	    }
	}

	return listOfValidationErrors;
    }

    public Boolean getBranchingOrderedAsc() {
	return branchingOrderedAsc;
    }

    public void setBranchingOrderedAsc(Boolean branchingOrderedAsc) {
	this.branchingOrderedAsc = branchingOrderedAsc;
    }
}