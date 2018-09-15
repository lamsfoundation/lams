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

package org.lamsfoundation.lams.learning.web.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

/**
 * Action class to display an activity. This is used when UI calls starts of the learning process. It is needed to put
 * the learner progress and the activity in the request, on which LoadToolActivityAction relies. If you try to go
 * straight to LoadToolActivityAction then the data won't be in the request.
 *
 * Request values: lessonID (mandatory), InitialDisplay (optional - Set to "true" for normal display, set to "false"
 * when you want it to assume it is inside parallel frameset. Defaults to true).
 *
 */
@Controller
public class DisplayActivityController {

    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(DisplayActivityController.class);

    public static final String PARAM_INITIAL_DISPLAY = "initialDisplay";

    @Autowired
    @Qualifier("learnerService")
    private ILearnerFullService learnerService;

    @Autowired
    private WebApplicationContext applicationContext;

    /**
     * Gets an activity from the request (attribute) and forwards onto a display action using the ActionMappings class.
     * If no activity is in request then use the current activity in learnerProgress.
     *
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/DisplayActivity")
    public String execute(HttpServletRequest request) throws UnsupportedEncodingException {

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
	boolean displayParallelFrames = WebUtil.readBooleanParam(request,
		DisplayActivityController.PARAM_INITIAL_DISPLAY, true);

	ActivityMapping actionMappings = LearnerServiceProxy.getActivityMapping(applicationContext.getServletContext());
	String forward = actionMappings.getProgressForward(learnerProgress, false, displayParallelFrames, request,
		learnerService);

	if (DisplayActivityController.log.isDebugEnabled()) {
	    DisplayActivityController.log.debug(forward);
	}

	return forward;
    }
}