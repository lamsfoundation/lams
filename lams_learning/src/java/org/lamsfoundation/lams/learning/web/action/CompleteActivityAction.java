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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author daveg
 *
 *         XDoclet definition:
 *
 *
 *
 */
public class CompleteActivityAction extends ActivityAction {

    protected static String className = "CompleteActivity";
    private static IntegrationService integrationService = null;

    /**
     * Sets the current activity as complete and uses the progress engine to find the next activity (may be null).
     *
     * Called when completing an optional activity, or triggered by completeToolSession (via a tool call). The activity
     * to be marked as complete must
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	ActivityMapping actionMappings = LearningWebUtil.getActivityMapping(this.getServlet().getServletContext());

	ICoreLearnerService learnerService = getLearnerService();

	Integer learnerId = LearningWebUtil.getUserId();
	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);

	// This must get the learner progress from the progress id, not cached from the request,
	// otherwise we may be using an old version of a lesson while a teacher is starting a
	// live edit, and then the lock flag can't be checked correctly.
	LearnerProgress progress = learnerService
		.getProgressById(WebUtil.readLongParam(request, LearningWebUtil.PARAM_PROGRESS_ID, true));

	// if user has already completed the lesson - we need to let integrations servers know to come and pick up
	// updated marks (as it won't happen at lessoncomplete.jsp page)
	if (progress.isComplete()) {
	    String lessonFinishCallbackUrl = getIntegrationService().getLessonFinishCallbackUrl(progress.getUser(),
		    progress.getLesson());
	    if (lessonFinishCallbackUrl != null) {
		request.setAttribute("lessonFinishUrl", lessonFinishCallbackUrl);
	    }
	    if (progress.getLesson().getAllowLearnerRestart()) {
	    request.setAttribute("lessonID", progress.getLesson().getLessonId());
	}
	}

	ActionForward forward = null;
	// Set activity as complete
	try {
	    forward = LearningWebUtil.completeActivity(request, response, actionMappings, progress, activity, learnerId,
		    learnerService, false);
	} catch (LearnerServiceException e) {
	    return mapping.findForward("error");
	}
	return forward;
    }

    private IntegrationService getIntegrationService() {
	if (CompleteActivityAction.integrationService == null) {
	    CompleteActivityAction.integrationService = (IntegrationService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext()).getBean("integrationService");
	}
	return CompleteActivityAction.integrationService;
    }
}