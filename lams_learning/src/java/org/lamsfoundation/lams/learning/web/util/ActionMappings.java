/*
 * Created on 8/02/2005
 *
 */
package org.lamsfoundation.lams.learning.web.util;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ForwardingActionForward;
import org.apache.struts.action.RedirectingActionForward;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.LessonCompleteActivity;
import org.lamsfoundation.lams.learningdesign.NullActivity;
import org.lamsfoundation.lams.lesson.ParallelWaitActivity;

/**
 * This class contains the standard struts action mappings for errors as
 * well as methods that get required Action/URL to display an Activity or
 * LearnerProgress. 
 * 
 * @author daveg
 *
 */
public class ActionMappings {

    public static final String ERROR = "error";
    public static final String NO_SESSION_ERROR = "noSessionError";
    public static final String DOUBLE_SUBMIT_ERROR = "doubleSubmitError";


	/**
	 * Creates a Struts ActionForward to display an activity.
	 * @param activity, the Activity to be displayed
	 * @param progress, the LearnerProgress associated with the Activity and learner
	 */
	public ActionForward getActivityForward(Activity activity, LearnerProgress progress, boolean redirect) {
	    ActionForward actionForward = null;

	    String strutsAction = getActivityAction(activity, progress);
		if (activity instanceof ToolActivity) {
		    // always use redirect false for a ToolActivity as ToolDisplayActivity
		    // does it's own redirect
			actionForward = strutsActionToForward(strutsAction, activity, false);
		}
		else {
			actionForward = strutsActionToForward(strutsAction, activity, redirect);
		}
		
		return actionForward;
	}

	/**
	 * Creates a Struts ActionForward to display a next activity. If the
	 * previous activity was a ParallelActivity then the frames will be
	 * cleared.
	 * @param progress, the LearnerProgress associated with the Activity and learner
	 * @param redirect, If true a RedirectActionForward is used
	 * @return
	 */
	public ActionForward getProgressForward(LearnerProgress progress, boolean redirect) {
	    ActionForward actionForward = null;

		Activity nextActivity = progress.getNextActivity();
		Activity previousActivity = progress.getPreviousActivity();
		Activity currentActivity = progress.getCurrentActivity();
		
		if (previousActivity instanceof ParallelActivity) {
		    // clear frameset
		    String strutsAction = "/requestDisplay.do";
		    actionForward = strutsActionToForward(strutsAction, nextActivity, redirect);
		}
		else {
			actionForward = getActivityForward(nextActivity, progress, redirect);
		}

		return actionForward;
	}
	
	/**
	 * Generates an ActivityURL for an Activity using it's progress. The URL is for
	 * the client and so includes hostname etc.
	 * Note that the URL could also be a wait message or a jsp to clear the frames.
	 * @param activity, the Activity to be displayed
	 * @param progress, the LearnerProgress associated with the Activity and learner
	 */
	public ActivityURL getActivityURL(Activity activity, LearnerProgress progress) {
	    ActivityURL activityURL = null;
		
		// TODO: remove instanceof
		if (activity instanceof ToolActivity) {
		    activityURL = getToolURL((ToolActivity)activity, progress);
		}
		else {
		    // use LAMS action
		    String strutsAction = null;
		    strutsAction = getActivityAction(activity, progress);
			activityURL = strutsActionToURL(strutsAction, activity, true);
		}
		
		return activityURL;
	}
	
	/**
	 * Generates an ActivityURL for the next Activity using it's progress. The URL
	 * is for the client and so includes hostname etc.
	 * Note that this method always returns a LAMS URLs, if a ToolActivity is next
	 * the URL will be the action for displaying the tool.
	 * Note that the URL could also be a wait message or a jsp to clear the frames.
	 * @param progress, the current LearnerProgress.
	 */
	public ActivityURL getProgressURL(LearnerProgress progress) {
		ActivityURL activityURL = null;

		Activity nextActivity = progress.getNextActivity();
		Activity previousActivity = progress.getPreviousActivity();
		Activity currentActivity = progress.getCurrentActivity();
		
		if (previousActivity instanceof ParallelActivity) {
		    // clear frameset
		    String strutsAction = "/requestDisplay.do";
		    activityURL = strutsActionToURL(strutsAction, nextActivity, true);
		}
		else {
			if (nextActivity instanceof ToolActivity) {
			    activityURL = getToolURL((ToolActivity)nextActivity, progress);
			}
			else {
				String strutsAction = getActivityAction(nextActivity, progress);
			    activityURL = strutsActionToURL(strutsAction, nextActivity, true);
			}
		}
		
		return activityURL;
	}
	

	/** TODO: getToolURL()
	 * Generates an ActivityURL for a Tool Activity. The URL is for the tool and
	 * not for the tool loading page. The URL also includes toolSessionId and all
	 * other required data.
	 * @param activity, the ToolActivity to be displayed
	 * @param progress, the current LearnerProgress, used to get activity status
	 */
	public ActivityURL getToolURL(ToolActivity activity, LearnerProgress progress) {
		ActivityURL activityURL = new ActivityURL();
		activityURL.setTitle("activity "+activity.getActivityId());
		activityURL.setDescription("description for activity with id "+activity.getActivityId());
		activityURL.setComplete(progress.getProgressState(activity) == LearnerProgress.ACTIVITY_COMPLETED);

		activityURL.setActivityId(activity.getActivityId());
		activityURL.setUrl("/lams_learning/test/DummyTool.do?method=display&activityId="+activity.getActivityId()+"&progressState="+progress.getProgressState(activity));

		return activityURL;
	}
	
	
	/**
	 * Returns the struts action used to display the specified activity.
	 * @param activity, Activity to be displayed
	 * @param progress, LearnerProgress for the activity, used to check activity status
	 * @return String representing a struts action
	 */
	protected String getActivityAction(Activity activity, LearnerProgress progress) {
		String strutsAction = null;
		
		// TODO: remove instanceof
		if (activity instanceof NullActivity) {
		    if (activity instanceof ParallelWaitActivity) {
		        strutsAction = "/parallelWait.do";
		    }
		    else if (activity instanceof LessonCompleteActivity) {
		        strutsAction = "/lessonComplete.do";
		    }
		}
		else if (activity instanceof ComplexActivity) {
		    if (activity instanceof ParallelActivity) {
		        strutsAction = "/DisplayParallelActivity.do";
		    }
		    else if (activity instanceof OptionsActivity) {
		        strutsAction = "/DisplayOptionsActivity.do";
		    }
		}
		else if (activity instanceof SimpleActivity) {
			if (activity instanceof GroupingActivity) {
				// this probably means a wait URL
			}
			if (activity instanceof GateActivity) {
				// not completed so return wait URL
			}
			if (activity instanceof ToolActivity) {
				// get tool URL
				//activityAction = new ActivityAction("/DisplayActivity.do", activity);
			    strutsAction = "/DisplayToolActivity.do";
			}
		}
		return strutsAction;
	}
	
	protected ActivityURL strutsActionToURL(String strutsAction, Activity activity, boolean useContext) {
	    ActivityURL activityURL = new ActivityURL();
	    activityURL.setActivityId(activity.getActivityId());
	    // TODO: don't hardcode
		String query = "?activityId="+activity.getActivityId();
		String url = strutsAction+query;
		if (useContext) {
			String context = "/lams_learning";
		    url = context+url;
		}
		activityURL.setUrl(url);
		
		return activityURL;
	}
	
	protected ActionForward strutsActionToForward(String strutsAction, Activity activity, boolean redirect) {
		ActionForward actionForward;
		if (redirect) {
			ActivityURL activityURL = strutsActionToURL(strutsAction, activity, false);
			String path = activityURL.getUrl();
			actionForward = new RedirectingActionForward(path);
		}
		else {
		    actionForward = new ForwardingActionForward(strutsAction);
		}
		
		return actionForward;
	}
	
}
