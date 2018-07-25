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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.tool.SystemTool;

/**
 * @author Mitchell Seaton
 * @version 2.1
 *
 *
 */
public class ChosenBranchingActivity extends BranchingActivity implements Serializable {

    private static final long serialVersionUID = 2735761434798827294L;

    /** full constructor */
    public ChosenBranchingActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
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
    public ChosenBranchingActivity() {
	super();
    }

    /** minimal constructor */
    public ChosenBranchingActivity(Long activityId, java.util.Date createDateTime,
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

	ChosenBranchingActivity newBranchingActivity = new ChosenBranchingActivity();
	copyBranchingFields(newBranchingActivity);
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

}
