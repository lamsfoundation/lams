//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.2/xslt/JavaClass.xsl

package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.*;

import java.util.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.Utils;

import org.lamsfoundation.lams.learningdesign.*;
import org.lamsfoundation.lams.lesson.*;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;

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
public class DisplayParallelActivity extends ActivityAction {
	

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm form = (ActivityForm)actionForm;
		
		LearnerProgress learnerProgress = getLearnerProgress(request, form);
		Activity activity = getActivity(request, form, learnerProgress);
		if (!(activity instanceof ParallelActivity)) {
		    log.error(className+": activity not ParallelActivity "+activity.getActivityId());
			return mapping.findForward(ActionMappings.ERROR);
		}

		ParallelActivity parallelActivity = (ParallelActivity)activity;

		form.setActivityId(activity.getActivityId());

		List activityURLs = new ArrayList();
		// TODO: Need to get order somehow
		Set subActivities = parallelActivity.getActivities();
		Iterator i = subActivities.iterator();
		while (i.hasNext()) {
			Activity subActivity = (Activity)i.next();
			ActivityURL url = Utils.getActivityURL(subActivity, learnerProgress);
			activityURLs.add(url);
		}
		if (activityURLs.size() == 0) {
		    log.error(className+": No sub-activity URLs for activity "+activity.getActivityId());
			return mapping.findForward(ActionMappings.ERROR);
		}
		form.setActivityURLs(activityURLs);
		
		String forward = "displayParallel";
		return mapping.findForward(forward);
	}

}