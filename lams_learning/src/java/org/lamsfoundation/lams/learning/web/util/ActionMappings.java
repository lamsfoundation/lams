/*
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
*/

package org.lamsfoundation.lams.learning.web.util;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ForwardingActionForward;
import org.apache.struts.action.RedirectingActionForward;
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
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.service.LamsToolServiceException;

/**
 * This class contains the standard struts action mappings for errors as
 * well as methods that get required Action/URL to display an Activity or
 * LearnerProgress.
 * 
 * In order to return a URL this class needs to know the baseURL. This can
 * be set using in the application context.
 * 
 * @author daveg
 *
 */
public class ActionMappings {

    public static final String ERROR = "error";
    public static final String NO_SESSION_ERROR = "noSessionError";
    public static final String DOUBLE_SUBMIT_ERROR = "doubleSubmitError";
    
    private ILamsToolService toolService;
    private String baseURL;


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
	public String getActivityURL(Activity activity, LearnerProgress progress) {
	    String activityURL = null;
		
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
	public String getProgressURL(LearnerProgress progress) {
		String activityURL = null;

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
	

	/**
	 * Generates an ActivityURL for a Tool Activity. The URL is for the tool and
	 * not for the tool loading page. The URL also includes toolSessionId and all
	 * other required data.
	 * @param activity, the ToolActivity to be displayed
	 * @param progress, the current LearnerProgress, used to get activity status
	 */
	public String getToolURL(ToolActivity activity, LearnerProgress progress) {
		Tool tool = activity.getTool();
		String url = tool.getLearnerUrl();

		ToolSession toolSession;
		try {
			// Get tool session using learner and activity
			toolSession = toolService.getToolSession(progress.getUser(), activity);
		}
		catch (LamsToolServiceException e) {
			return null;
		}
		
		// Append toolSessionId to tool URL
		Long toolSessionId = toolSession.getToolSessionId();
		url += "?toolSessionId="+toolSessionId;
		
		return url;
	}
	
	
	/**
	 * Returns the struts action used to display the specified activity.
	 * @param activity, Activity to be displayed
	 * @param progress, LearnerProgress for the activity, used to check activity status
	 * @return String representing a struts action
	 */
	protected String getActivityAction(Activity activity, LearnerProgress progress) {
		String strutsAction = null;
		
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
	
	/**
	 * Creates a URL for a struts action for an activity.
	 * @param strutsAction, the struts action path.
	 * @param activity, the activity the action is for.
	 * @param useContext, if true prepends the server and context to the URL.
	 */
	protected String strutsActionToURL(String strutsAction, Activity activity, boolean useContext) {
		String query = "?activityId="+activity.getActivityId();
		String url = strutsAction+query;
		if (useContext) {
			String lamsUrl = getLamsURL();
		    url = lamsUrl+url;
		}

		return url;
	}
	
	private String getLamsURL() {
		return baseURL;
	}
	
	protected ActionForward strutsActionToForward(String strutsAction, Activity activity, boolean redirect) {
		ActionForward actionForward;
		if (redirect) {
			String activityURL = strutsActionToURL(strutsAction, activity, false);
			//String path = activityURL.getUrl();
			actionForward = new RedirectingActionForward(activityURL);
		}
		else {
		    actionForward = new ForwardingActionForward(strutsAction);
		}
		
		return actionForward;
	}
	
	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}
	
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
}
