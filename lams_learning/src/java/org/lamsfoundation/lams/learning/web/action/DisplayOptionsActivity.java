/*
 * Created on 14/01/2005
 *

 */
package org.lamsfoundation.lams.learning.web.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.OptionsActivityForm;
import org.lamsfoundation.lams.learning.web.util.Utils;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;

/**
 * @author daveg
 *
 * XDoclet definition:
 * 
 * @struts:action path="/DisplayOptionsActivity" name="optionsActivityForm"
 *                input="/Activity.do" validate="false" scope="request"
 * 
 * @struts:action-forward name="displayOptions" path=".optionsActivity"
 * 
 */
public class DisplayOptionsActivity extends ActivityAction {
	

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		OptionsActivityForm form = (OptionsActivityForm)actionForm;
		
		LearnerProgress learnerProgress = getLearnerProgress(request, form);
		Activity activity = getActivity(request, form, learnerProgress);
		if (!(activity instanceof OptionsActivity)) {
		    log.error(className+": activity not OptionsActivity "+activity.getActivityId());
			return mapping.findForward(ActionMappings.ERROR);
		}

		OptionsActivity optionsActivity = (OptionsActivity)activity;

		form.setActivityId(activity.getActivityId());

		List activityURLs = new ArrayList();
		// TODO: Need to get order somehow
		Set subActivities = optionsActivity.getActivities();
		Iterator i = subActivities.iterator();
		int completedCount = 0;
		while (i.hasNext()) {
			Activity subActivity = (Activity)i.next();
			ActivityURL url = Utils.getActivityURL(subActivity, learnerProgress);
			activityURLs.add(url);
			if (learnerProgress.getProgressState(subActivity) == LearnerProgress.ACTIVITY_COMPLETED) {
				completedCount++;
			}
		}
		form.setActivityURLs(activityURLs);
		if (completedCount >= optionsActivity.getMinNumberOfOptions().intValue()) {
			form.setFinished(true);
		}
		
		this.saveToken(request);
		
		String forward = "displayOptions";
		return mapping.findForward(forward);
	}
	
}
