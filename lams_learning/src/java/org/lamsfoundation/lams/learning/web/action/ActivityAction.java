//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.*;

import java.util.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.service.TestLearnerService;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;

import com.lamsinternational.lams.usermanagement.*;
import com.lamsinternational.lams.lesson.*;
import com.lamsinternational.lams.learningdesign.*;

/** 
 * MyEclipse Struts
 * Creation date: 01-12-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/Activity" name="activityForm" parameter="method"
 *                validate="false" scope="request"
 * 
 * -- Load one or more URLs to display an activity
 * @struts:action-forward name="displayToolActivity" path="/DisplayToolActivity.do"
 * @struts:action-forward name="displayParallelActivity" path="/DisplayParallelActivity.do"
 * @struts:action-forward name="displayOptionsActivity" path="/DisplayOptionsActivity.do"
 * -- Wait message for gate activity
 * @struts:action-forward name="displayGateActivity" path="/DisplayGateActivity.do"
 * 
 * -- Tell browser to request next activity. Needed because the parallel
 * -- activity frames need to be cleared by the client.
 * @struts:action-forward name="requestDisplay" path=".requestDisplay"
 * 
 * -- "Complete other activity" message
 * @struts:action-forward name="parallelWait" path=".parallelWait" redirect="true"
 * -- Sequence complete message
 * @struts:action-forward name="lessonComplete" path=".lessonComplete" redirect="true"
 * 
 */
public class ActivityAction extends DispatchAction {
	
	/**
	 * Gets the session bean from session.
	 * @return SessionBean for this request, null if no session.
	 */
	protected SessionBean getSessionBean(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		SessionBean sessionBean = (SessionBean)session.getAttribute(SessionBean.NAME);
		return sessionBean;
	}
	
	/**
	 * Sets the session bean for this session.
	 */
	protected void setSessionBean(SessionBean sessionBean, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.setAttribute(SessionBean.NAME, sessionBean);
	}
	
	/**
	 * Temporary method for testing. To use point the browser to /Activity.do?method=test
	 */
	public ActionForward test(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm activityForm = (ActivityForm) form;
		
		SessionBean sessionBean = new SessionBean();
		setSessionBean(sessionBean, request);

		TestLearnerService learnerService = (TestLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		learnerService.setRequest(request);
		learnerService.clearProgress();
		//activityForm.setActivityId(new Long(1));
		
		return displayCurrent(mapping, form, request, response);
	}

	/**
	 * Loads an activity using the activityId and forwards onto the required display
	 * action (displayToolActivity, displayParallelActivity, etc.).
	 */
	public ActionForward display(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm activityForm = (ActivityForm) form;
		
		// TODO: should we use a page forward or throw an exception for no session?
		SessionBean sessionBean = getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward("noSessionError");
		}

		TestLearnerService learnerService = (TestLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		learnerService.setRequest(request);
		
		// Get learner
		User learner = sessionBean.getLeaner();
		// Get lesson
		Lesson lesson = sessionBean.getLesson();
		// Get learnerProgress
		LearnerProgress progress = sessionBean.getLearnerProgress();
		if (progress == null) {
			progress = learnerService.getProgress(learner, lesson);
			sessionBean.setLearnerProgress(progress);
			setSessionBean(sessionBean, request);
		}
		
		// Find requested activity
		Long activityId = activityForm.getActivityId();
		Activity activity = getActivity(activityId.longValue(), progress);
		if (activity == null) {
			// TODO: log error
			return mapping.findForward("error");
		}
		
		ActionForward forward = displayActivity(activity, progress, mapping, activityForm, request, response);
		
		return forward;
	}

	/**
	 * Loads the current activity and forwards onto the required display
	 * action (same as for display method).
	 */
	public ActionForward displayCurrent(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm activityForm = (ActivityForm) form;
		
		// TODO: should we use a page forward or throw an exception for no session?
		SessionBean sessionBean = getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward("noSessionError");
		}

		TestLearnerService learnerService = (TestLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		learnerService.setRequest(request);
		
		// Get learner
		User learner = sessionBean.getLeaner();
		// Get lesson
		Lesson lesson = sessionBean.getLesson();
		// Get learnerProgress
		LearnerProgress progress = sessionBean.getLearnerProgress();
		if (progress == null) {
			progress = learnerService.getProgress(learner, lesson);
			sessionBean.setLearnerProgress(progress);
			setSessionBean(sessionBean, request);
		}
		
		// Get current activity
		Activity activity = progress.getCurrentActivity();
		
		ActionForward forward = null;
		// TODO: If null need to check for lesson complete or not started
		if (activity == null) {
			if (progress.isLessonComplete()) {
				forward = mapping.findForward("lessonComplete");
			}
			else {
				// TODO: log error
				return mapping.findForward("error");
			}
		}
		else {
			forward = displayActivity(activity, progress, mapping, activityForm, request, response);
		}
		
		return forward;
	}

	/**
	 * Sets the current activity as complete and uses the progress engine to find
	 * the next activity (may be null). Note that the activity being completed may be
	 * part of a parallel activity.
	 * Forwards onto the required display action (displayToolActivity,
	 * displayParallelActivity, etc.).
	 */
	public ActionForward complete(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws LearnerServiceException {
		ActivityForm activityForm = (ActivityForm) form;

		// TODO: should we use a page forward or throw an exception for no session?
		SessionBean sessionBean = getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward("noSessionError");
		}

		TestLearnerService learnerService = (TestLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		learnerService.setRequest(request);
		
		// Get learner
		User learner = sessionBean.getLeaner();
		// Get lesson
		Lesson lesson = sessionBean.getLesson();
		// Get learnerProgress
		LearnerProgress progress = sessionBean.getLearnerProgress();
		if (progress == null) {
			progress = learnerService.getProgress(learner, lesson);
		}

		// Find requested activity
		// The activity must be a current activity
		// TODO: or can the activity be non-current and not complete?
		Long activityId = activityForm.getActivityId();
		Activity activity = getActivity(activityId.longValue(), progress);
		
		// if not current then error
		if (activity == null) {
			// TODO: log error
			return mapping.findForward("error");
		}
		
		// Set activity as complete
		LearnerProgress nextProgress = learnerService.calculateProgress(activity.getActivityId().longValue(), learner, lesson);

		//ActionForward forward = displayNextActivity(activity, nextProgress, mapping, activityForm, request, response);
		ActionForward forward = displayNextActivity(progress, nextProgress, mapping, activityForm, request, response);
		sessionBean.setLearnerProgress(nextProgress);
		setSessionBean(sessionBean, request);
		
		return forward;
	}


	/**
	 * Display the next activity in progress.
	 * The just finished activity is needed to display the current progress because
	 * ParallelActivity may require that the client re-request the activity or display
	 * a "patially complete" message.
	 * @param previousProgress, the progress before the activity was completed
	 * @param progress, the current progress
	 */
	private ActionForward displayNextActivity(LearnerProgress previousProgress, LearnerProgress progress,
			ActionMapping mapping, ActivityForm activityForm, HttpServletRequest request, HttpServletResponse response) throws LearnerServiceException {

		ActionForward forward = null;

		if (progress.isLessonComplete()) {
			forward = mapping.findForward("lessonComplete");
		}
		else {
			Activity nextActivity = progress.getNextActivity();
			Activity currentActivity = progress.getCurrentActivity();
			Activity previousActivity = previousProgress.getCurrentActivity();
			if (nextActivity == null) {
				if (previousActivity == currentActivity) {
					if (previousActivity instanceof ParallelActivity) {
						forward = mapping.findForward("parallelWait");
					}
					else {
						nextActivity = currentActivity;
						forward = displayActivity(nextActivity, progress, mapping, activityForm, request, response);
					}
				}
				else {
					if (previousActivity instanceof ParallelActivity) {
						nextActivity = currentActivity;
						forward = mapping.findForward("requestDisplay");
					}
					else {
						nextActivity = currentActivity;
						forward = displayActivity(nextActivity, progress, mapping, activityForm, request, response);
					}
				}
			}
			else {
				forward = displayActivity(nextActivity, progress, mapping, activityForm, request, response);
			}
			if (nextActivity != null) request.setAttribute("activityId", nextActivity.getActivityId());
		}

		return forward;
	}
	
	
	/**
	 * Returns an ActionForward to display an activity. The forward returned is
	 * displayToolActivity for a ToolActivity, displayParallelActivity for a
	 * ParallelActivity and displayOptionsActivity for an OptionsActivity. The
	 * activity ID is also set as a request attribute (read by DisplayActivity).
	 */
	private ActionForward displayActivity(Activity activity, LearnerProgress progress,
			ActionMapping mapping, ActivityForm activityForm, HttpServletRequest request, HttpServletResponse response) {
		String forwardName = null;

		// This should not be done with instanceof, perhaps should use the class name
		if (activity instanceof ComplexActivity) {
			if (activity instanceof OptionsActivity) forwardName = "displayOptionsActivity";
			else if (activity instanceof ParallelActivity) forwardName = "displayParallelActivity";
		}
		else if (activity instanceof SimpleActivity) {
			forwardName = "displayToolActivity";
		}

		Long activityId = activity.getActivityId();
		request.setAttribute("activityId", activityId);
		
		ActionForward forward = mapping.findForward(forwardName);
		return forward;
	}
	
	/** A quick method to get an activity from within a progress. This method is
	 * temporary.
	 */
	private Activity getActivity(long activityId, LearnerProgress progress) {
		Set activities = progress.getLesson().getLearningDesign().getActivities();
		Iterator i = activities.iterator();
		while (i.hasNext()) {
			Activity activity = (Activity)i.next();
			if (activity.getActivityId().longValue() == activityId) {
				return activity;
			}
		}
		return null;
	}

}