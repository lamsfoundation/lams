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
import org.lamsfoundation.lams.lesson.NullActivity;
import org.lamsfoundation.lams.lesson.ParallelWaitActivity;

/**
 * This class replaces the activity display actions in ActionMappings so
 * that ToolActivity has a loading page.
 * 
 * @author daveg
 *
 */
public class ActionMappingsWithToolWait extends ActionMappings {

	/**
	 * Creates a Struts ActionForward to display an activity.
	 * @param activity, the Activity to be displayed
	 * @param progress, the LearnerProgress associated with the Activity and learner
	 */
	public ActionForward getActivityForward(Activity activity, LearnerProgress progress, boolean redirect) {
	    ActionForward actionForward = null;

	    String strutsAction = getActivityAction(activity, progress);
		actionForward = strutsActionToForward(strutsAction, activity, redirect);
		
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
	public ActionForward getNextActivityForward(LearnerProgress progress, boolean redirect) {
	    return super.getNextActivityForward(progress, redirect);
	    /*ActionForward actionForward = null;

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

		return actionForward;*/
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
		
	    // use LAMS action
	    String strutsAction = null;
	    strutsAction = getActivityAction(activity, progress);
		activityURL = strutsActionToURL(strutsAction, activity, true);
		
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
	public ActivityURL getNextActivityURL(LearnerProgress previousProgress, LearnerProgress progress) {
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
			String strutsAction = getActivityAction(nextActivity, progress);
		    activityURL = strutsActionToURL(strutsAction, nextActivity, true);
		}
		
		return activityURL;
	}
	
	
	/**
	 * Returns the struts action used to display the specified activity.
	 * @param activity, Activity to be displayed
	 * @param progress, LearnerProgress for the activity
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
			    strutsAction = "/DisplayLoadToolActivity.do";
			}
		}
		return strutsAction;
	}
	
}
