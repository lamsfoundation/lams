//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.*;

import java.util.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;

import org.lamsfoundation.lams.usermanagement.*;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;
import org.lamsfoundation.lams.learningdesign.*;
import org.lamsfoundation.lams.lesson.*;

/** 
 * MyEclipse Struts
 * Creation date: 01-12-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/DisplayActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 */
public class DisplayActivity extends ActivityAction {
    
    protected static String className = "DisplayActivity";

	/** display
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
		
		// Get learner
		User learner = sessionBean.getLeaner();
		Lesson lesson = sessionBean.getLesson();
		
		LearnerProgress learnerProgress = getLearnerProgress(request, form);
		Activity activity = getActivity(request, form, learnerProgress);
		
		if (activity == null) {
		    log.error(className+": No activity in request or session");
			return mapping.findForward(ActionMappings.ERROR);
		}
		
		ActionForward forward = displayActivity(activity, learnerProgress, mapping, form, request, response);
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
		
		ActionForward forward = mapping.findForward(forwardName);
		return forward;
	}
	
	
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