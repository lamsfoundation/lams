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

package org.lamsfoundation.lams.learning.web.util;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;

/**
 * @author daveg
 */
public class ActivityMappingStrategy implements Serializable {
    private static final long serialVersionUID = -6418428916496098347L;
    private static Logger log = Logger.getLogger(ActivityMappingStrategy.class);

    private static final String PROGRESS_BROKEN_ACTION = "/progressBroken.jsp";

    /**
     * Returns the struts action used to display the specified activity.
     *
     * @param activity,
     *            Activity to be displayed
     * @param progress,
     *            LearnerProgress for the activity, used to check activity status
     * @return String representing a struts action
     */
    protected String getActivityAction(Activity activity) {
	String strutsAction = null;
	if (activity == null) {
	    strutsAction = PROGRESS_BROKEN_ACTION;
	} else if (activity.isSystemToolActivity() || activity.isToolActivity()) {
	    strutsAction = "/LoadToolActivity.do";
	} else if (activity.isParallelActivity()) {
	    strutsAction = "/DisplayParallelActivity.do";
	} else if (activity.isOptionsActivity()) {
	    strutsAction = "/DisplayOptionsActivity.do";
	} else {
	    // unexpected type
	    log.error("Unexpected activity type " + activity);
	    strutsAction = PROGRESS_BROKEN_ACTION;
	}
	return strutsAction;
    }

    /**
     * Returns the struts action for displaying the parallel wait.
     */
    protected String getWaitingAction() {
	return "/partialCompleted.jsp";
    }

    /**
     * Returns the struts action for displaying lesson complete.
     */
    protected String getLessonCompleteAction() {
	return "/LessonComplete.do";
    }

    /**
     * Returns the struts action for triggering the window to close.
     */
    protected String getCloseWindowAction() {
	return "/close.jsp";
    }

    /**
     * Returns the struts action for the error message that appears when the learner progress is broken, say be live
     * edit.
     */
    protected String getProgressBrokenAction() {
	return PROGRESS_BROKEN_ACTION;
    }

    /**
     * Returns the struts action for the "Complete Activity" call. This calls the complete activity action class, which
     * may go
     * to a waiting screen (if currently setting up lock gates for live edit) or the next activity.
     */
    protected String getCompleteActivityAction() {
	return "/CompleteActivity.do";
    }

    /**
     * Get the "bootstrap" activity action. This is the action called when the user first joins a lesson, and sets up
     * the necessary request details based on the user's progress details.
     */
    protected String getDisplayActivityAction() {
	return "/DisplayActivity.do";
    }
}
