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
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy;

/**
 * @author Manpreet Minhas
 *
 */
@Entity
public abstract class SimpleActivity extends Activity implements Serializable {
    private static final long serialVersionUID = 2784194319972928196L;

    @Transient
    protected SimpleActivityStrategy simpleActivityStrategy;

    /** full constructor */
    public SimpleActivity(Long activityId, Integer id, String description, String title, Integer xcoord, Integer ycoord,
	    Integer orderId, Date createDateTime, LearningLibrary learningLibrary, Activity parentActivity,
	    Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign, Grouping grouping,
	    Integer activityTypeId, Transition transitionTo, Transition transitionFrom, String languageFile,
	    Boolean stopAfterActivity, Set<Activity> inputActivities, Set<BranchActivityEntry> branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, branchActivityEntries);
    }

    /** default constructor */
    public SimpleActivity() {
    }

    /** minimal constructor */
    public SimpleActivity(Long activityId, Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom);
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    public SimpleActivityStrategy getSimpleActivityStrategy() {
	return simpleActivityStrategy;
    }

}
