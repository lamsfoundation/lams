/*
 * Created on 4/02/2005
 *
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
import org.lamsfoundation.lams.learning.web.util.Utils;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;

/**
 * @author daveg
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/ChooseActivity" name="activityForm"
 *                validate="false" scope="request"
 *  
 */
public class ChooseActivity extends ActivityAction {

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
		
		SessionBean sessionBean = ActivityAction.getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward(ActionMappings.NO_SESSION_ERROR);
		}
		
		// check token
		if (!this.isTokenValid(request, true)) {
			// didn't come here from options page
		    log.info(className+": No valid token in request");
			return mapping.findForward(ActionMappings.DOUBLE_SUBMIT_ERROR);
		}
		
		// Get learner
		User learner = sessionBean.getLeaner();
		Lesson lesson = sessionBean.getLesson();
		
		LearnerProgress progress = getLearnerProgress(request, form);
		Activity activity = getActivity(request, form, progress);
		
		if (activity == null) {
		    log.error(className+": No activity in request or session");
			return mapping.findForward(ActionMappings.ERROR);
		}

		ILearnerService learnerService = getLearnerService(request);
		learnerService.chooseActivity(learner, lesson, activity);
		
		ActionForward forward = Utils.getActivityForward(activity, progress, true);
		return forward;
	}

}
