/*
 * Created on 14/01/2005
 *
 */
package org.lamsfoundation.lams.learning.web.util;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ForwardingActionForward;
import org.apache.struts.action.RedirectingActionForward;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;

import org.lamsfoundation.lams.lesson.*;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;

/**
 * @author daveg
 *
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
	public static ActivityURL getActivityURL(Activity activity, LearnerProgress progress) {
		ActivityURL activityURL = new ActivityURL();
		activityURL.setTitle("activity "+activity.getActivityId());
		activityURL.setDescription("description for activity with id "+activity.getActivityId());
		activityURL.setComplete(progress.getProgressState(activity) == LearnerProgress.ACTIVITY_COMPLETED);
		activityURL.setActivityId(activity.getActivityId());

		ActivityAction activityAction = getActivityAction(activity, progress);
		// TODO: don't hardcode
		String context = "/lams_learning";
		String strutsAction = activityAction.getStrutsAction();
		String query = "?activityId="+activity.getActivityId();
		String url = context+strutsAction+query;
		
		activityURL.setUrl(url);
		
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
	public static ActivityURL getNextActivityURL(LearnerProgress previousProgress, LearnerProgress progress) {
		ActivityURL activityURL = null;

		ActivityAction activityAction = getNextActivityAction(previousProgress, progress);
		Activity activity = activityAction.getActivity();

		// TODO: don't hardcode
		String context = "/lams_learning";
		String strutsAction = activityAction.getStrutsAction();
		String query = (activity == null)? "" : "?activityId="+activity.getActivityId();
		String url = context+strutsAction+query;
		
		activityURL = new ActivityURL();
		if (activity != null) {
			activityURL.setActivityId(activity.getActivityId());
		}
		activityURL.setUrl(url);
		
		return activityURL;
	}

	/**
	 * Generates an ActivityURL for a message.
	 * @param path, the message path (eg. requestDisplay.do)
	 * @param title, title for the URL
	 */
	private static ActivityURL getMessageURL(String path, String title) {
		ActivityURL activityURL = new ActivityURL();
		activityURL.setUrl(path);
		activityURL.setTitle(title);
		
		return activityURL;
	}
	

	/** TODO: getToolURL()
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
		//activityURL.setUrl("toolTest.jsp?activityId="+activity.getActivityId()+"&progressState="+progress.getProgressState(activity));
		activityURL.setUrl("/lams_learning/test/DummyTool.do?method=display&activityId="+activity.getActivityId()+"&progressState="+progress.getProgressState(activity));

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
	/*public static String getNextActivityForward(LearnerProgress previousProgress, LearnerProgress progress) {
		String forward = null;
		
		if (progress.isLessonComplete()) {
			forward = "lessonComplete";
		}
		else {
			Activity nextActivity = progress.getNextActivity();
			Activity currentActivity = progress.getCurrentActivity();
			Activity previousActivity = previousProgress.getCurrentActivity();
			if (nextActivity == null) {
				if (previousActivity == currentActivity) {
					if (previousActivity instanceof ParallelActivity) {
						forward = "parallelWait";
					}
					else {
						forward = getActivityForward(currentActivity, progress);
					}
				}
				else {
					if (previousActivity instanceof ParallelActivity) {
						forward = "requestDisplay";
					}
					else {
						forward = getActivityForward(currentActivity, progress);
					}
				}
			}
			else {
				forward = getActivityForward(nextActivity, progress);
			}
		}

		return forward;
	}*/
	
	/*private static String getActivityForward(Activity activity, LearnerProgress progress) {
		//return "displayActivity";
		String forwardName = null;
		// This should not be done with instanceof, perhaps should use the class name
		if (activity instanceof ComplexActivity) {
			if (activity instanceof OptionsActivity) forwardName = "displayOptionsActivity";
			else if (activity instanceof ParallelActivity) forwardName = "displayParallelActivity";
		}
		else if (activity instanceof SimpleActivity) {
			forwardName = "displayToolActivity";
		}
		return forwardName;
	}*/
	
	public static ActionForward getActivityForward(Activity activity, LearnerProgress progress, boolean redirect) {
		ActivityAction activityAction = getActivityAction(activity, progress);
		String strutsAction = activityAction.getStrutsAction();
		ActionForward actionForward;
		if (redirect) {
			// TODO: don't hardcode
			//String context = "/lams_learning";
			String context = "";
			String path = context+strutsAction+"?activityId="+activity.getActivityId();
			actionForward = new RedirectingActionForward(path);
		}
		else {
			actionForward = new ForwardingActionForward(strutsAction);
		}
		return actionForward;
	}
	
	public static ActionForward getNextActivityForward(LearnerProgress previousProgress, LearnerProgress progress, boolean redirect) {
		ActivityAction activityAction = getNextActivityAction(previousProgress, progress);
		String strutsAction = activityAction.getStrutsAction();
		ActionForward actionForward;
		if (redirect) {
			Activity activity = activityAction.getActivity();
			// TODO: don't hardcode
			//String context = "/lams_learning";
			String context = "";
			String query = (activity == null)? "" : "?activityId="+activity.getActivityId();
			String path = context+strutsAction+query;
			actionForward = new RedirectingActionForward(path);
		}
		else {
			actionForward = new ForwardingActionForward(strutsAction);
		}
		return actionForward;
	}
	
	

	private static ActivityAction getActivityAction(Activity activity, LearnerProgress progress) {
		ActivityAction activityAction = null;
		
		// TODO: should this use instanceof?
		if (activity instanceof ComplexActivity) {
			activityAction = new ActivityAction("/DisplayActivity.do", activity);
		}
		else if (activity instanceof SimpleActivity) {
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
					activityAction = new ActivityAction("/DisplayActivity.do", activity);
				}
			}
		}
		return activityAction;
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
	private static ActivityAction getNextActivityAction(LearnerProgress previousProgress, LearnerProgress progress) {
		ActivityAction activityAction = null;
		
		if (progress.isLessonComplete()) {
			activityAction = new ActivityAction("/lessonComplete.do", null);
		}
		else {
			Activity nextActivity = progress.getNextActivity();
			Activity currentActivity = progress.getCurrentActivity();
			Activity previousActivity = previousProgress.getCurrentActivity();
			if (nextActivity == null) {
				if (previousActivity == currentActivity) {
					if (previousActivity instanceof ParallelActivity) {
						activityAction = new ActivityAction("/parallelWait.do", null);
					}
					else {
						activityAction = getActivityAction(currentActivity, progress);
					}
				}
				else {
					if (previousActivity instanceof ParallelActivity) {
						//strutsAction = getActivityAction(currentActivity, progress);
						activityAction = new ActivityAction("/requestDisplay.do", currentActivity);
					}
					else {
						activityAction = getActivityAction(currentActivity, progress);
					}
				}
			}
			else {
				activityAction = getActivityAction(currentActivity, progress);
			}
		}

		return activityAction;
	}
	
	private static class ActivityAction {
		private String strutsAction;
		private Activity activity;
		
		public ActivityAction(String strutsAction, Activity activity) {
			this.strutsAction = strutsAction;
			this.activity = activity;
		}
		
		public Activity getActivity() {
			return activity;
		}
		public void setActivity(Activity activity) {
			this.activity = activity;
		}
		public String getStrutsAction() {
			return strutsAction;
		}
		public void setStrutsAction(String strutsAction) {
			this.strutsAction = strutsAction;
		}
	}
	
}
