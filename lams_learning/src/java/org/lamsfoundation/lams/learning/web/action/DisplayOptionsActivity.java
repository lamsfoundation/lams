/*
 * Created on 14/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.web.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.form.OptionsActivityForm;
import org.lamsfoundation.lams.learning.web.util.Utils;

import com.lamsinternational.lams.learningdesign.Activity;
import com.lamsinternational.lams.learningdesign.OptionsActivity;
import com.lamsinternational.lams.lesson.LearnerProgress;

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
public class DisplayOptionsActivity extends DisplayActivity {
	
	/**
	 * Returns an ActionForward to display an activity and sets the form bean values.
	 */
	protected ActionForward displayActivity(Activity activity, LearnerProgress progress,
			ActionMapping mapping, ActivityForm activityForm, HttpServletRequest request, HttpServletResponse response) {
		String forward = null;

		if (!(activity instanceof OptionsActivity)) {
			// error
			return mapping.findForward("error");
		}
		
		OptionsActivityForm form = (OptionsActivityForm)activityForm;
		
		forward = "displayOptions";

		OptionsActivity optionsActivity = (OptionsActivity)activity;
		form.setActivityId(activity.getActivityId());
		form.setTitle(activity.getTitle());
		form.setDescription(activity.getDescription());
		form.setMinimum(optionsActivity.getMinNumberOfOptions().intValue());
		form.setMaximum(optionsActivity.getMaxNumberOfOptions().intValue());

		List activityURLs = new ArrayList();
		// TODO: Need to get order somehow
		Set subActivities = optionsActivity.getActivities();
		Iterator i = subActivities.iterator();
		int completedCount = 0;
		while (i.hasNext()) {
			Activity subActivity = (Activity)i.next();
			ActivityURL url = Utils.generateActivityURL(subActivity, progress);
			activityURLs.add(url);
			if (progress.getProgressState(subActivity) == LearnerProgress.ACTIVITY_COMPLETED) {
				completedCount++;
			}
		}
		form.setActivityURLs(activityURLs);
		if (completedCount >= optionsActivity.getMinNumberOfOptions().intValue()) {
			form.setFinished(true);
		}
		
		if (activityURLs.size() == 0) {
			// TODO: error
		}
		
		return mapping.findForward(forward);
	}
	
}
