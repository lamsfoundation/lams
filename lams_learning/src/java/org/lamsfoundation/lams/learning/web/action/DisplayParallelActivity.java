//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.*;
import java.util.*;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.Utils;

import org.lamsfoundation.lams.learningdesign.*;
import org.lamsfoundation.lams.lesson.*;

/** 
 * MyEclipse Struts
 * Creation date: 01-12-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/DisplayParallelActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 * @struts:action-forward name="displayParallel" path=".parallelActivity"
 * 
 */
public class DisplayParallelActivity extends DisplayActivity {
	
	/**
	 * Returns an ActionForward to display an activity based on its type. The form bean
	 * also has its values set for display. Note that this method is over-ridden by the
	 * DisplayOptionsActivity sub-class.
	 */
	protected ActionForward displayActivity(Activity activity, LearnerProgress progress,
			ActionMapping mapping, ActivityForm form, HttpServletRequest request, HttpServletResponse response) {
		String forward = null;

		if (!(activity instanceof ParallelActivity)) {
			// error
			return mapping.findForward("error");
		}

		ParallelActivity parallelActivity = (ParallelActivity)activity;

		forward = "displayParallel";

		form.setActivityId(activity.getActivityId());
		List activityURLs = new ArrayList();
		// TODO: Need to get order somehow
		Set subActivities = parallelActivity.getActivities();
		Iterator i = subActivities.iterator();
		while (i.hasNext()) {
			Activity subActivity = (Activity)i.next();
			ActivityURL url = Utils.generateActivityURL(subActivity, progress);
			activityURLs.add(url);
		}
		form.setActivityURLs(activityURLs);
		
		if (activityURLs.size() == 0) {
			// TODO: error
		}
		
		return mapping.findForward(forward);
	}

}