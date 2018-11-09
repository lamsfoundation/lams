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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Mitchell Seaton
 */
@Entity
@DiscriminatorValue("10")
public class ChosenBranchingActivity extends BranchingActivity implements Serializable {

    private static final long serialVersionUID = 2735761434798827294L;

    /** default constructor */
    public ChosenBranchingActivity() {
	super();
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