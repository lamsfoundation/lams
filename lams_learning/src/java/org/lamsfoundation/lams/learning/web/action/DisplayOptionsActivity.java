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

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;

/**
 * Action class to display an OptionsActivity.
 * 
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
	

	/**
	 * Gets an options activity from the request (attribute) and forwards to
	 * the display JSP.
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		OptionsActivityForm form = (OptionsActivityForm)actionForm;
		ActionMappings actionMappings = getActionMappings();
		
		LearnerProgress learnerProgress = getLearnerProgress(request, form);
		Activity activity = getActivity(request, form, learnerProgress);
		if (!(activity instanceof OptionsActivity)) {
		    log.error(className+": activity not OptionsActivity "+activity.getActivityId());
			return mapping.findForward(actionMappings.ERROR);
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
			ActivityURL activityURL = actionMappings.getActivityURL(subActivity, learnerProgress);
			activityURL.setTitle(subActivity.getTitle());
			activityURL.setDescription(subActivity.getDescription());
			if (learnerProgress.getProgressState(subActivity) == LearnerProgress.ACTIVITY_COMPLETED) {
			    activityURL.setComplete(true);
				completedCount++;
			}
			activityURLs.add(activityURL);
		}
		form.setActivityURLs(activityURLs);
		if (completedCount >= optionsActivity.getMinNumberOfOptions().intValue()) {
			form.setFinished(true);
		}
		form.setMinimum(optionsActivity.getMinNumberOfOptions().intValue());
		form.setMaximum(optionsActivity.getMaxNumberOfOptions().intValue());
		
		this.saveToken(request);
		
		String forward = "displayOptions";
		return mapping.findForward(forward);
	}
	
}
