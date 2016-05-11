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
import org.lamsfoundation.lams.learningdesign.strategy.ParallelActivityStrategy;

/**
 * @author Manpreet Minhas
 *
 */
public class ParallelActivity extends ComplexActivity implements Serializable {

    /** full constructor */
    public ParallelActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom,
	    String languageFile, Boolean stopAfterActivity, Set inputActivities, Set activities,
	    Activity defaultActivity, Set branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, activities, defaultActivity,
		branchActivityEntries);
	super.activityStrategy = new ParallelActivityStrategy(this);
    }

    /** default constructor */
    public ParallelActivity() {
	super.activityStrategy = new ParallelActivityStrategy(this);
    }

    /** minimal constructor */
    public ParallelActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Set activities) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom, activities);
	super.activityStrategy = new ParallelActivityStrategy(this);
    }

    /**
     * Makes a copy of the ParallelActivity for authoring, preview and monitoring enviornment
     *
     * @return ParallelActivity Returns a deep-copy of the originalActivity
     */
    @Override
    public Activity createCopy(int uiidOffset) {

	ParallelActivity newParallelActivity = new ParallelActivity();
	copyToNewComplexActivity(newParallelActivity, uiidOffset);
	return newParallelActivity;
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

}
