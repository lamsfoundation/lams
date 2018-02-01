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


package org.lamsfoundation.lams.learning.web.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Action class to display an activity. This is used when UI calls starts of the learning process. It is needed to put
 * the learner progress and the activity in the request, on which LoadToolActivityAction relies. If you try to go
 * straight to LoadToolActivityAction then the data won't be in the request.
 *
 * Request values: lessonID (mandatory), InitialDisplay (optional - Set to "true" for normal display, set to "false"
 * when you want it to assume it is inside parallel frameset. Defaults to true).
 */
public class DisplayActivityAction extends ActivityAction {

    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(DisplayActivityAction.class);

    public static final String PARAM_INITIAL_DISPLAY = "initialDisplay";

    /**
     * Gets an activity from the request (attribute) and forwards onto a display action using the ActionMappings class.
     * If no activity is in request then use the current activity in learnerProgress.
     *
     * @throws UnsupportedEncodingException
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws UnsupportedEncodingException {
	ILearnerFullService learnerService = getLearnerService();

	// UI can only send the lessonID as that is all it has...
	Integer learnerId = LearningWebUtil.getUserId();
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	// hack until UI changes its url - current sending progressId
	if (lessonId == null) {
	    lessonId = WebUtil.readLongParam(request, "progressId");
	}
	LearnerProgress learnerProgress = learnerService.getProgress(learnerId, lessonId);

	// Normally this is used to display the initial page, so initialDisplay=true. But if called from the
	// special handling for completed activities (ie close window) then we need to set it to false.
	boolean displayParallelFrames = WebUtil.readBooleanParam(request, DisplayActivityAction.PARAM_INITIAL_DISPLAY,
		true);

	ActivityMapping actionMappings = LearnerServiceProxy.getActivityMapping(getServlet().getServletContext());
	ActionForward forward = actionMappings.getProgressForward(learnerProgress, false, displayParallelFrames,
		request, learnerService);

	if (DisplayActivityAction.log.isDebugEnabled()) {
	    DisplayActivityAction.log.debug(forward);
	}

	return forward;
    }
}