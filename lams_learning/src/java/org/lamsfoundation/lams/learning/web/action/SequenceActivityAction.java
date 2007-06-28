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

/* $$Id$$ */	
package org.lamsfoundation.lams.learning.web.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.form.OptionsActivityForm;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Action class to display a sequence activity.
 * 
 * Normally this will display the first activity inside a sequence activity.
 * If there are no activities within the sequence activity then it will display
 * an "empty" message.
 * 
 * @struts:action path="/SequenceActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 * @struts:action-forward name="empty" path=".sequenceActivityEmpty"
 * 
 */
public class SequenceActivityAction extends ActivityAction {
	

	/**
	 * Gets an sequence activity from the request (attribute) and forwards to
	 * either the first activity in the sequence activity or the "empty" JSP.
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {

		ActivityForm form = (ActivityForm)actionForm;
		ActivityMapping actionMappings = LearningWebUtil.getActivityMapping(this.getServlet().getServletContext());
		Integer learnerId = LearningWebUtil.getUserId();

		ICoreLearnerService learnerService = getLearnerService();
		LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
		Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);
		if (!(activity instanceof SequenceActivity)) {
		    log.error(className+": activity not SequenceActivity "+activity.getActivityId());
			return mapping.findForward(ActivityMapping.ERROR);
		}

		ActionForward forward = null;
		SequenceActivity sequenceActivity = (SequenceActivity)activity;
        Activity firstActivityInSequence = sequenceActivity.getFirstActivityInSequenceActivity();
        
        if ( firstActivityInSequence != null && ! firstActivityInSequence.isNull() ) {
			// Set the first activity as the current activity and display it
			learnerProgress = learnerService.chooseActivity(learnerId, learnerProgress.getLesson().getLessonId(), firstActivityInSequence);
			forward = actionMappings.getActivityForward(firstActivityInSequence, learnerProgress, true);
			LearningWebUtil.putActivityInRequest(request, firstActivityInSequence, learnerService);
		} else {
			request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activity.getActivityId());
			request.setAttribute(AttributeNames.PARAM_TITLE, activity.getTitle());
			request.setAttribute(AttributeNames.PARAM_LESSON_ID, learnerProgress.getLesson().getLessonId());
			request.setAttribute(AttributeNames.PARAM_LEARNER_PROGRESS_ID, learnerProgress.getLearnerProgressId());
			setupProgressString(form, request);
			forward = mapping.findForward("empty");
		}

        LearningWebUtil.putLearnerProgressInRequest(request,learnerProgress);
        return forward;
       	
	}
	
}
