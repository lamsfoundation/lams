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
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Mitchell Seaton
 * @version 2.1
 *
 * @hibernate.class
 */
public class GroupBranchingActivity extends BranchingActivity implements Serializable {

    private static final long serialVersionUID = 7426228060060498158L;

    /** full constructor */
    public GroupBranchingActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom,
	    String languageFile, Integer startXcoord, Integer startYcoord, Integer endXcoord, Integer endYcoord,
	    Boolean stopAfterActivity, Set inputActivities, Set activities, Activity defaultActivity,
	    SystemTool systemTool, Set branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, startXcoord, startYcoord, endXcoord, endYcoord, stopAfterActivity,
		inputActivities, activities, defaultActivity, systemTool, branchActivityEntries);
    }

    /** default constructor */
    public GroupBranchingActivity() {
	super();
    }

    /** minimal constructor */
    public GroupBranchingActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Set activities) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom, activities);
    }

    /**
     * Makes a copy of the BranchingActivity for authoring, preview and monitoring environment
     *
     * @return BranchingActivity Returns a deep-copy of the originalActivity
     */
    @Override
    public Activity createCopy(int uiidOffset) {

	GroupBranchingActivity newBranchingActivity = new GroupBranchingActivity();
	copyBranchingFields(newBranchingActivity);
	copyToNewComplexActivity(newBranchingActivity, uiidOffset);
	return newBranchingActivity;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    /**
     * Validate the branching activity. A Grouping must be applied to the branching activity.
     *
     * @return error message key
     */
    @Override
    public Vector validateActivity(MessageService messageService) {
	Vector listOfValidationErrors = new Vector();
	if (getActivities() == null || getActivities().size() == 0) {
	    listOfValidationErrors
		    .add(new ValidationErrorDTO(ValidationErrorDTO.BRANCHING_ACTIVITY_MUST_HAVE_A_BRANCH_ERROR_CODE,
			    messageService.getMessage(ValidationErrorDTO.BRANCHING_ACTIVITY_MUST_HAVE_A_BRANCH),
			    this.getActivityUIID()));
	}

	if (getGrouping() == null) {
	    listOfValidationErrors.add(new ValidationErrorDTO(ValidationErrorDTO.BRANCHING_ACTVITY_GROUPING_ERROR_CODE,
		    messageService.getMessage(ValidationErrorDTO.BRANCHING_ACTVITY_GROUPING), this.getActivityUIID()));
	} else {
	    Set<Group> groups = getGrouping().getGroups();
	    if (groups == null || groups.size() == 0) {
		listOfValidationErrors
			.add(new ValidationErrorDTO(ValidationErrorDTO.BRANCHING_ACTVITY_GROUPING_ERROR_CODE,
				messageService.getMessage(ValidationErrorDTO.BRANCHING_ACTVITY_GROUPING),
				this.getActivityUIID()));
	    } else {
		for (Group group : groups) {
		    boolean foundEntry = false;
		    if (group.getBranchActivities() != null) {
			Iterator iter = group.getBranchActivities().iterator();
			while (iter.hasNext() && !foundEntry) {
			    BranchActivityEntry entry = (BranchActivityEntry) iter.next();
			    foundEntry = entry.getBranchingActivity().equals(this);
			}
		    }
		    if (!foundEntry) {
			listOfValidationErrors.add(new ValidationErrorDTO(
				ValidationErrorDTO.BRANCHING_ACTVITY_MUST_HAVE_ALL_GROUPS_ALLOCATED_ERROR_CODE,
				messageService.getMessage(
					ValidationErrorDTO.BRANCHING_ACTVITY_MUST_HAVE_ALL_GROUPS_ALLOCATED),
				this.getActivityUIID()));
			break;
		    }
		}
	    }
	}
	return listOfValidationErrors;
    }

}
