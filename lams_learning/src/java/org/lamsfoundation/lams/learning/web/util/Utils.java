/*
 * Created on 14/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.web.util;

import org.lamsfoundation.lams.learning.web.bean.ActivityURL;

import com.lamsinternational.lams.lesson.*;
import com.lamsinternational.lams.learningdesign.Activity;
import com.lamsinternational.lams.learningdesign.ComplexActivity;
import com.lamsinternational.lams.learningdesign.GateActivity;
import com.lamsinternational.lams.learningdesign.GroupingActivity;
import com.lamsinternational.lams.learningdesign.SimpleActivity;
import com.lamsinternational.lams.learningdesign.ToolActivity;

/**
 * @author kevin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Utils {

	/**
	 * Generates an ActivityURL for an Activity using it's progress. The URL is for
	 * the client and so includes hostname etc.
	 * Note that this method always returns LAMS URLs, if a ToolActivity is passed
	 * in the URL will be the action for displaying the tool.
	 */
	public static ActivityURL generateActivityURL(Activity activity, LearnerProgress progress) {
		ActivityURL activityURL = new ActivityURL();
		activityURL.setTitle("activity "+activity.getActivityId());
		activityURL.setDescription("description for activity with id "+activity.getActivityId());
		activityURL.setComplete(progress.getProgressState(activity) == LearnerProgress.ACTIVITY_COMPLETED);
		// TODO: should not be using instanceof
		if (activity instanceof ComplexActivity) {
			// if activity is complex need to return a LAMS url
			String contextRoot = "/lams_learning";
			String strutsAction = "/Activity.do?method=display";
			String properties = "&activityId="+activity.getActivityId();
			String url = contextRoot+strutsAction+properties;
			activityURL.setUrl(url);
		}
		else if (activity instanceof SimpleActivity) {
			//if (progress.activityCompleted(activity)) {
			if (activity == null) {
			}
			else {
				if (activity instanceof GroupingActivity) {
					// this probably means a wait URL
				}
				if (activity instanceof GateActivity) {
					// not completed so return wait URL
				}
				if (activity instanceof ToolActivity) {
					// get tool URL
					activityURL.setActivityId(activity.getActivityId());
					//activityURL.setUrl("toolTest.jsp?activityId="+activity.getActivityId()+"&progressState="+progress.getProgressState(activity));
					String contextRoot = "/lams_learning";
					String strutsAction = "/Activity.do?method=display";
					String properties = "&activityId="+activity.getActivityId();
					String url = contextRoot+strutsAction+properties;
					activityURL.setUrl(url);
				}
			}
		}
		return activityURL;
	}
	
	public static ActivityURL getToolURL(ToolActivity activity, LearnerProgress progress) {
		ActivityURL activityURL = new ActivityURL();
		activityURL.setTitle("activity "+activity.getActivityId());
		activityURL.setDescription("description for activity with id "+activity.getActivityId());
		activityURL.setComplete(progress.getProgressState(activity) == LearnerProgress.ACTIVITY_COMPLETED);

		activityURL.setActivityId(activity.getActivityId());
		activityURL.setUrl("toolTest.jsp?activityId="+activity.getActivityId()+"&progressState="+progress.getProgressState(activity));

		return activityURL;
	}
	
}
