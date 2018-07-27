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
import java.util.Set;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.strategy.BranchingActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Mitchell Seaton
 * @version 2.1
 *
 *
 */
abstract public class BranchingActivity extends ComplexActivity implements Serializable, ISystemToolActivity {

    // types are used on the URLS to determine which type of branch is expected
    // the code should always then check against the activity to make sure it is correct
    public static final String CHOSEN_TYPE = "chosen";
    public static final String GROUP_BASED_TYPE = "group";
    public static final String TOOL_BASED_TYPE = "tool";

    private SystemTool systemTool;
    private Integer startXcoord;
    private Integer startYcoord;
    private Integer endXcoord;
    private Integer endYcoord;

    /** full constructor */
    public BranchingActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom,
	    String languageFile, Integer startXcoord, Integer startYcoord, Integer endXcoord, Integer endYcoord,
	    Boolean stopAfterActivity, Set inputActivities, Set activities, Activity defaultActivity,
	    SystemTool systemTool, Set branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, activities, defaultActivity,
		branchActivityEntries);
	super.activityStrategy = new BranchingActivityStrategy(this);
	this.systemTool = systemTool;
	this.startXcoord = startXcoord;
	this.startYcoord = startYcoord;
	this.endXcoord = endXcoord;
	this.endYcoord = endYcoord;
    }

    /** default constructor */
    public BranchingActivity() {
	super.activityStrategy = new BranchingActivityStrategy(this);
    }

    /** minimal constructor */
    public BranchingActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Set activities) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom, activities);
	super.activityStrategy = new BranchingActivityStrategy(this);
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    @Override
    public boolean isNull() {
	return false;
    }

    /**
     *
     */
    public Integer getEndXcoord() {
	return endXcoord;
    }

    public void setEndXcoord(Integer endXcoord) {
	this.endXcoord = endXcoord;
    }

    /**
     *
     */
    public Integer getEndYcoord() {
	return endYcoord;
    }

    public void setEndYcoord(Integer endYcoord) {
	this.endYcoord = endYcoord;
    }

    /**
     *
     */
    public Integer getStartXcoord() {
	return startXcoord;
    }

    public void setStartXcoord(Integer startXcoord) {
	this.startXcoord = startXcoord;
    }

    /**
     *
     */
    public Integer getStartYcoord() {
	return startYcoord;
    }

    public void setStartYcoord(Integer startYcoord) {
	this.startYcoord = startYcoord;
    }

    /**
     *
     *
     */
    @Override
    public SystemTool getSystemTool() {
	return systemTool;
    }

    @Override
    public void setSystemTool(SystemTool systemTool) {
	this.systemTool = systemTool;
    }

    public void copyBranchingFields(BranchingActivity newBranchingActivity) {
	newBranchingActivity.systemTool = this.systemTool;
	newBranchingActivity.startXcoord = this.startXcoord;
	newBranchingActivity.startYcoord = this.startYcoord;
	newBranchingActivity.endXcoord = this.endXcoord;
	newBranchingActivity.endYcoord = this.endYcoord;
	newBranchingActivity.defaultActivity = this.defaultActivity;
    }

    /**
     * Validate the branching activity. All branching activities should have at least one branch and the default
     * activity must be set for a tool based branch.
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
	return listOfValidationErrors;
    }

}
