//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.*;

import java.util.*;

import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.service.DummyLearnerService;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;

import org.lamsfoundation.lams.usermanagement.*;
import org.lamsfoundation.lams.web.action.Action;
import org.lamsfoundation.lams.lesson.*;
import org.lamsfoundation.lams.learningdesign.*;

/** 
 * MyEclipse Struts
 * Creation date: 01-12-2005
 * 
 */
public class ActivityAction extends Action {
	
	protected static final String ACTIVITY_REQUEST_ATTRIBUTE = "activity";
	protected static final String LEARNER_PROGRESS_REQUEST_ATTRIBUTE = "learnerprogress";
	
	/**
	 * Gets the session bean from session.
	 * @return SessionBean for this request, null if no session.
	 */
	protected static SessionBean getSessionBean(HttpServletRequest request) {
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
	protected static void setSessionBean(SessionBean sessionBean, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.setAttribute(SessionBean.NAME, sessionBean);
	}
	
	protected ILearnerService getLearnerService(HttpServletRequest request) {
		DummyLearnerService learnerService = (DummyLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		learnerService.setRequest(request);
		return learnerService;
	}
	
	protected LearnerProgress getLearnerProgress(HttpServletRequest request, ActivityForm form) {
		LearnerProgress learnerProgress = (LearnerProgress)request.getAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE);
		if (learnerProgress == null) {
			SessionBean sessionBean = getSessionBean(request);
			User learner = sessionBean.getLeaner();
			Lesson lesson = sessionBean.getLesson();
			
			//TestLearnerService learnerService = (TestLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
			//learnerService.setRequest(request);
			ILearnerService learnerService = getLearnerService(request);
			learnerProgress = learnerService.getProgress(learner, lesson);
			
			// Save progress in session for Flash request
			sessionBean.setLearnerProgress(learnerProgress);
			setSessionBean(sessionBean, request);
		}
		return learnerProgress;
	}
	
	protected void setLearnerProgress(HttpServletRequest request, LearnerProgress learnerProgress) {
		request.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE, learnerProgress);

		SessionBean sessionBean = getSessionBean(request);
		// Save progress in session for Flash request
		sessionBean.setLearnerProgress(learnerProgress);
		setSessionBean(sessionBean, request);
	}
	
	protected Activity getActivity(HttpServletRequest request, ActivityForm form, LearnerProgress learnerProgress) {
		Activity activity = (Activity)request.getAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE);
		if (activity == null) {
			Long activityId = form.getActivityId();
			if (activityId == null) {
				// TODO: should this be current or next?
				activity = learnerProgress.getCurrentActivity();
			}
			else {
				activity = getActivity(activityId.longValue(), learnerProgress);
			}
		}
		return activity;
	}
	
	protected void setActivity(HttpServletRequest request, Activity activity) {
		request.setAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE, activity);
	}
	
	
	/** TODO: replace method
	 * A quick method to get an activity from within a progress. This method is
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