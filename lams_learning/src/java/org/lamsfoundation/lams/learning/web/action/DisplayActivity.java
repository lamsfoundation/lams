//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.*;
import java.util.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;

import com.lamsinternational.lams.usermanagement.*;
import com.lamsinternational.lams.learningdesign.*;
import com.lamsinternational.lams.lesson.*;

/** 
 * MyEclipse Struts
 * Creation date: 01-12-2005
 *  
 */
public abstract class DisplayActivity extends Action {
	
	protected SessionBean getSessionBean(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		SessionBean sessionBean = (SessionBean)session.getAttribute(SessionBean.NAME);
		return sessionBean;
	}

	/** display
	 * Gets an activity from the request (attribute) and forwards onto the required
	 * jsp (SingleActivity or ParallelActivity).
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm displayForm = (ActivityForm) form;
		
		// TODO: should we use a page forward or throw an exception for no session?
		SessionBean sessionBean = getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward("noSessionError");
		}
		
		// Get learner
		User learner = sessionBean.getLeaner();
		// Get learnerProgress
		LearnerProgress progress = sessionBean.getLearnerProgress();

		Long activityId = (Long)request.getAttribute("activityId");
		if (activityId == null) {
			// check the request for an activityId
			activityId = displayForm.getActivityId();
			if (activityId == null) {
				return mapping.findForward("error");
			}
		}
		
		// Find requested activity
		// May need special processing if activity is not current
		Activity activity = getActivity(activityId.longValue(), progress);
		
		ActionForward forward = displayActivity(activity, progress, mapping, displayForm, request, response);
		return forward;
	}
	
	/**
	 * Returns an ActionForward to display an activity based on its type. The form bean
	 * also has its values set for display. Note that this method is over-ridden by the
	 * DisplayOptionsActivity sub-class.
	 */
	protected abstract ActionForward displayActivity(Activity activity, LearnerProgress progress,
			ActionMapping mapping, ActivityForm form, HttpServletRequest request, HttpServletResponse response);

	
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