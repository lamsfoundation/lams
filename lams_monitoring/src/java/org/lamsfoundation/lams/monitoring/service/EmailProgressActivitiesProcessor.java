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


package org.lamsfoundation.lams.monitoring.service;

import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections.ArrayStack;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignProcessor;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;
import org.lamsfoundation.lams.monitoring.dto.EmailProgressActivityDTO;

/**
 * Generate a hierarchy of activities to report the learner progress in an email.
 *
 * Note: The collections used by this class are not synchronized.
 *
 * @author Fiona Malikoff
 */
public class EmailProgressActivitiesProcessor extends LearningDesignProcessor {

    Vector<EmailProgressActivityDTO> activityList;
    ArrayStack activityListStack;
    Vector<EmailProgressActivityDTO> currentActivityList;
    Map<Long, Integer> numberOfUsersInActivity;
    int depth;

    public EmailProgressActivitiesProcessor(LearningDesign design, IActivityDAO activityDAO, Map<Long, Integer> numberOfUsersInActivity) {
	super(design, activityDAO);
	this.numberOfUsersInActivity = numberOfUsersInActivity;
	this.activityList = new Vector<EmailProgressActivityDTO>();
	this.activityListStack = new ArrayStack(5);
	this.currentActivityList = activityList;
	depth=0;
    }

    public Vector<EmailProgressActivityDTO> getActivityList() {
	return activityList;
    }

    @Override
    public boolean startComplexActivity(ComplexActivity activity) {

	Integer numLearners = numberOfUsersInActivity.get(activity.getActivityId());
	currentActivityList.add(new EmailProgressActivityDTO(activity, depth, numLearners != null ? numLearners : 0));

	if ( activity.isParallelActivity() ) {
	    return false;
	} else {
	    // Create a new current activity list, putting the old current one on the stack.
	    activityListStack.push(currentActivityList);
	    currentActivityList = new Vector<EmailProgressActivityDTO>();
	    depth++;
	    return true;
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void endComplexActivity(ComplexActivity activity) {
	if (!activity.isParallelActivity()) {
	    Vector<EmailProgressActivityDTO> topActivityList = (Vector<EmailProgressActivityDTO>) activityListStack
		    .pop();
	    topActivityList.addAll(currentActivityList);
	    currentActivityList = topActivityList;
	    depth--;
	}
    }

   @Override
    public void startSimpleActivity(SimpleActivity activity) {
	Integer numLearners = numberOfUsersInActivity.get(activity.getActivityId());
	currentActivityList.add(new EmailProgressActivityDTO(activity, depth, numLearners != null ? numLearners : 0));
    }

    @Override
    public void endSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
	// nothing to do - everything done by the start
    }
}