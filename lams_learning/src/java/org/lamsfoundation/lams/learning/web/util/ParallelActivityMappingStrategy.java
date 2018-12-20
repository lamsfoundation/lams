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

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;

/**
 * @author daveg
 */
public class ParallelActivityMappingStrategy extends ActivityMappingStrategy {
    private static final long serialVersionUID = 2433891791486019818L;

    /**
     * Returns the struts action used to display the specified activity.
     *
     * @param activity,
     *            Activity to be displayed
     * @param progress,
     *            LearnerProgress for the activity, used to check activity status
     * @return String representing a struts action
     */
    protected String getActivityAction(Activity activity, LearnerProgress progress) {
	String strutsAction = null;
	if (progress.getProgressState(activity) == LearnerProgress.ACTIVITY_COMPLETED) {
	    strutsAction = "/partialCompleted.jsp";
	} else {
	    strutsAction = super.getActivityAction(activity);
	}
	return strutsAction;
    }

}
