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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Action class run when the learner finishes a lesson.
 *
 * XDoclet definition:
 *
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/LessonComplete" name="activityForm"
 *                validate="false" scope="request"
 * @struts:action-forward name="lessonComplete" path=".lessonComplete"
 *
 *
 *                        ----------------XDoclet Tags--------------------
 */
public class LessonCompleteActivityAction extends ActivityAction {

    private static IntegrationService integrationService = null;
    private static ILessonService lessonService = null;

    /**
     * Gets an activity from the request (attribute) and forwards onto a display action using the ActionMappings class.
     * If no activity is in request then use the current activity in learnerProgress.
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws UnsupportedEncodingException {
	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, getLearnerService());
	LearningWebUtil.setupProgressInRequest((ActivityForm) actionForm, request, learnerProgress);

	Set<Lesson> releasedLessons = getLessonService().getReleasedSucceedingLessons(
		learnerProgress.getLesson().getLessonId(), learnerProgress.getUser().getUserId());
	if (!releasedLessons.isEmpty()) {
	    StringBuilder releasedLessonNames = new StringBuilder();
	    for (Lesson releasedLesson : releasedLessons) {
		releasedLessonNames.append(releasedLesson.getLessonName()).append(", ");
	    }
	    releasedLessonNames.delete(releasedLessonNames.length() - 2, releasedLessonNames.length());
	    request.setAttribute(ActivityAction.RELEASED_LESSONS_REQUEST_ATTRIBUTE, releasedLessonNames.toString());
	}

	//checks for lessonFinishUrl parameter
	String lessonFinishCallbackUrl = getIntegrationService().getLessonFinishCallbackUrl(learnerProgress.getUser(),
		learnerProgress.getLesson());
	if (lessonFinishCallbackUrl != null) {
	    request.setAttribute("lessonFinishUrl", lessonFinishCallbackUrl);
	}

	return mapping.findForward("lessonComplete");
    }

    private IntegrationService getIntegrationService() {
	if (integrationService == null) {
	    integrationService = (IntegrationService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext()).getBean("integrationService");
	}
	return integrationService;
    }

    private ILessonService getLessonService() {
	if (lessonService == null) {
	    lessonService = (ILessonService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext()).getBean("lessonService");
	}
	return lessonService;
    }
}