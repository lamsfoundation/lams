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
import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
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
 * @struts:action path="/CompleteActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 */
public class CompleteActivity extends ActivityAction {
    
    protected static String className = "CompleteActivity";
	
	/**
	 * Sets the current activity as complete and uses the progress engine to find
	 * the next activity (may be null). Note that the activity being completed may be
	 * part of a parallel activity.
	 * Forwards onto the required display action (displayToolActivity,
	 * displayParallelActivity, etc.).
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm form = (ActivityForm)actionForm;
		ActionMappings actionMappings = getActionMappings();
		
		SessionBean sessionBean = ActivityAction.getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward(actionMappings.NO_SESSION_ERROR);
		}
		
		// check token
		if (!this.isTokenValid(request, true)) {
			// didn't come here from options page
		    log.info(className+": No valid token in request");
			return mapping.findForward(actionMappings.DOUBLE_SUBMIT_ERROR);
		}
		
		// Get learner
		User learner = sessionBean.getLeaner();
		Lesson lesson = sessionBean.getLesson();
		
		LearnerProgress progress = getLearnerProgress(request, form);
		Activity activity = getActivity(request, form, progress);
		
		if (activity == null) {
		    log.error(className+": No activity in request or session");
			return mapping.findForward(actionMappings.ERROR);
		}

		ILearnerService learnerService = getLearnerService(request);
		
		// Set activity as complete
		try {
			progress = learnerService.calculateProgress(activity, learner, lesson);
		}
		catch (ProgressException e) {
			return mapping.findForward("error");
		}
		request.setAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE, progress.getNextActivity());

		// Save progress in session for Flash request
		sessionBean.setLearnerProgress(progress);
		setSessionBean(sessionBean, request);

		ActionForward forward = actionMappings.getNextActivityForward(progress, true);
		
		return forward;
	}

}
