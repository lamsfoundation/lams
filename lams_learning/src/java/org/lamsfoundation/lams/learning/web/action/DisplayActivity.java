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
 * Action class to display an activity.
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/DisplayActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 */
public class DisplayActivity extends ActivityAction {
    
    protected static String className = "DisplayActivity";

	/** 
	 * Gets an activity from the request (attribute) and forwards onto a
	 * display action using the ActionMappings class. If no activity is
	 * in request then use the current activity in learnerProgress.
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm form = (ActivityForm) actionForm;
		ActionMappings actionMappings = getActionMappings();
		
		SessionBean sessionBean = ActivityAction.getSessionBean(request);
		if (sessionBean == null) {
			// forward to the no session error page
			return mapping.findForward(actionMappings.NO_SESSION_ERROR);
		}
		
		// Get learner
		User learner = sessionBean.getLeaner();
		Lesson lesson = sessionBean.getLesson();
		
		LearnerProgress learnerProgress = getLearnerProgress(request, form);
		Activity activity = getActivity(request, form, learnerProgress);
		
		if (activity == null) {
		    /*log.error(className+": No activity in request or session");
			return mapping.findForward(actionMappings.ERROR);*/
		    // Get current activity from learnerProgress
		    activity = learnerProgress.getCurrentActivity();
		}
	    setActivity(request, activity);
		
		ActionForward forward = actionMappings.getActivityForward(activity, learnerProgress, false);
		return forward;
	}
	
}