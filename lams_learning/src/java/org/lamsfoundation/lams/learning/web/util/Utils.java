/*
 * Created on 14/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;

import org.lamsfoundation.lams.lesson.*;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;

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
	 * Note that the URL could also be a wait message or a jsp to clear the frames.
	 * @param activity, the Activity to be displayed
	 * @param progress, the LearnerProgress associated with the Activity and learner
	 */
	public static ActivityURL generateActivityURL(Activity activity, LearnerProgress progress) {
		ActivityURL activityURL = new ActivityURL();
		activityURL.setTitle("activity "+activity.getActivityId());
		activityURL.setDescription("description for activity with id "+activity.getActivityId());
		activityURL.setComplete(progress.getProgressState(activity) == LearnerProgress.ACTIVITY_COMPLETED);
		
		// TODO: should not be using instanceof
		if (activity instanceof ComplexActivity) {
			// if activity is complex need to return a LAMS url
			// TODO: get contextRoot from context
			String contextRoot = "/lams_learning";
			String strutsAction = "/Activity.do";
			String query = "?method=display"+
				"&activityId="+activity.getActivityId();
			String url = contextRoot+strutsAction+query;
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
					String strutsAction = "/Activity.do";
					String query = "?method=display"+
						"&activityId="+activity.getActivityId();
					String url = contextRoot+strutsAction+query;
					activityURL.setUrl(url);
				}
			}
		}
		return activityURL;
	}
	

	/**
	 * Generates an ActivityURL for the next Activity using it's progress. The URL
	 * is for the client and so includes hostname etc.
	 * Note that this method always returns a LAMS URLs, if a ToolActivity is next
	 * the URL will be the action for displaying the tool.
	 * Note that the URL could also be a wait message or a jsp to clear the frames.
	 * @param previousProgress, the previous LearnerProgress before an activity was
	 * completed.
	 * @param progress, the current LearnerProgress.
	 */
	public static ActivityURL generateNextActivityURL(LearnerProgress previousProgress, LearnerProgress progress) {
		ActivityURL activityURL = null;
		String urlPath = null;
		
		if (progress.isLessonComplete()) {
			activityURL = generateMessageURL("lessonComplete.do", "Lesson Complete");
		}
		else {
			Activity nextActivity = progress.getNextActivity();
			Activity currentActivity = progress.getCurrentActivity();
			Activity previousActivity = previousProgress.getCurrentActivity();
			if (nextActivity == null) {
				if (previousActivity == currentActivity) {
					if (previousActivity instanceof ParallelActivity) {
						activityURL = generateMessageURL("parallelWait.do", "Wait");
					}
					else {
						activityURL = generateActivityURL(currentActivity, progress);
					}
				}
				else {
					if (previousActivity instanceof ParallelActivity) {
						//activityURL = generateMessageURL("requestDisplay.do", null);
						activityURL = generateActivityURL(currentActivity, progress);
						try {
							String nextUrl = activityURL.getUrl();
							nextUrl = URLEncoder.encode(nextUrl, "UTF-8");
							String url = "requestDisplay.do?url="+nextUrl;
							activityURL.setUrl(url);
						}
						catch (UnsupportedEncodingException e) {
							// TODO: log error, throw runtime exception
						}
					}
					else {
						activityURL = generateActivityURL(currentActivity, progress);
					}
				}
			}
			else {
				activityURL = generateActivityURL(nextActivity, progress);
			}
		}

		return activityURL;
	}

	/**
	 * Generates an ActivityURL for a message.
	 * @param path, the message path (eg. requestDisplay.do)
	 * @param title, title for the URL
	 */
	private static ActivityURL generateMessageURL(String path, String title) {
		ActivityURL activityURL = new ActivityURL();
		activityURL.setUrl(path);
		activityURL.setTitle(title);
		
		return activityURL;
	}
	

	/**
	 * Generates an ActivityURL for a Tool Activity. The URL is for the the tool
	 * (not for the tool loading page) and so includes toolSessionId and all other
	 * required data.
	 */
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
