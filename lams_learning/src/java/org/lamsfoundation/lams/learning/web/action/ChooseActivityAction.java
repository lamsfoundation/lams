/*
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
*/

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author daveg
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/ChooseActivity" name="activityForm"
 *                validate="false" scope="request"
 *  
 */
public class ChooseActivityAction extends ActivityAction {

    protected static String className = "ChooseActivity";
    
	/**
	 * Gets an activity from the request (attribute) and forwards onto the required
	 * jsp (SingleActivity or ParallelActivity).
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm form = (ActivityForm) actionForm;
		ActivityMapping actionMappings = getActivityMapping();
		
		SessionBean sessionBean = getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward(ActivityMapping.NO_SESSION_ERROR);
		}
		
		// check token
		if (!this.isTokenValid(request, true)) {
			// didn't come here from options page
		    log.info(className+": No valid token in request");
			return mapping.findForward(ActivityMapping.DOUBLE_SUBMIT_ERROR);
		}
		
		// Get learner
		User learner = sessionBean.getLearner();
		Lesson lesson = sessionBean.getLesson();
		
		LearnerProgress progress = getLearnerProgress(request);
		Activity activity = LearningWebUtil.getActivityFromRequest(request, getLearnerService());
		
		if (activity == null) {
		    log.error(className+": No activity in request or session");
			return mapping.findForward(ActivityMapping.ERROR);
		}

		ILearnerService learnerService = getLearnerService();
		learnerService.chooseActivity(learner, lesson, activity);
		
		ActionForward forward = actionMappings.getActivityForward(activity, progress, true);
		return forward;
	}

}
