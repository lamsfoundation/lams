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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ForwardingActionForward;
import org.apache.struts.action.RedirectingActionForward;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.web.action.ActivityAction;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.service.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.WebUtil;

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
public class ActivityMapping implements Serializable
{

    /* These are global struts forwards. */
    public static final String ERROR = "error";
    public static final String NO_SESSION_ERROR = "noSessionError";
    public static final String NO_ACCESS_ERROR = "noAccessError";
    public static final String DOUBLE_SUBMIT_ERROR = "doubleSubmitError";

    private ActivityMappingStrategy activityMappingStrategy = new ActivityMappingStrategy();

    private ILamsToolService toolService;
    private String baseURL;

    /**
     * Creates a Struts ActionForward to display an activity.
     * @param activity, the Activity to be displayed
     * @param progress, the LearnerProgress associated with the Activity and learner
     */
    public ActionForward getActivityForward(Activity activity,
                                            LearnerProgress progress,
                                            boolean redirect)
    {
        ActionForward actionForward = null;

        //String strutsAction = getActivityAction(activity, progress);
        String strutsAction = this.activityMappingStrategy.getActivityAction(activity,
                                                                             progress);
        if (activity instanceof ToolActivity)
        {
            // always use redirect false for a ToolActivity as ToolDisplayActivity
            // does it's own redirect
            actionForward = strutsActionToForward(strutsAction, activity, false);
        }
        else
        {
            actionForward = strutsActionToForward(strutsAction,
                                                  activity,
                                                  redirect);
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
    public ActionForward getProgressForward(LearnerProgress progress,
                                            boolean redirect,
                                            HttpServletRequest request)
    {
        ActionForward actionForward = null;

        // TODO: lesson complete client request to clear frames
        if (progress.isLessonComplete())
        {
            // If lesson complete forward to lesson complete action. This action will
            // cause a client request to clear ALL frames.
            String strutsAction = this.getActivityMappingStrategy()
                                      .getLessonCompleteAction();
            actionForward = this.strutsActionToForward(strutsAction,
                                                       null,
                                                       redirect);
        }
        else
        {

            if (progress.isParallelWaiting())
            {
                // progress is waiting, goto waiting page
                String strutsAction = this.getActivityMappingStrategy()
                                          .getWaitingAction();
                actionForward = this.strutsActionToForward(strutsAction,
                                                           null,
                                                           redirect);
            }
            else
            {
                // display next activity
                if (progress.getPreviousActivity() instanceof ParallelActivity)
                {
                    // if previous activity was a parallel activity then we need to
                    // clear frames.
                    String strutsAction = "/requestDisplay.do";
                    String activityURL = this.getActivityURL(progress.getNextActivity(),
                                                             progress);
                    strutsAction += "?url=" + activityURL;
                    actionForward = strutsActionToForward(strutsAction,
                                                          null,
                                                          redirect);
                }
                else
                {
                    actionForward = getActivityForward(progress.getNextActivity(),
                                                       progress,
                                                       redirect);
                    //setup activity into request for display
                    request.setAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE,
                                         progress.getNextActivity());
                }
            }
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
    public String getActivityURL(Activity activity, LearnerProgress progress)
    {
        String strutsAction = this.activityMappingStrategy.getActivityAction(activity,
                                                                             progress);
        return strutsActionToURL(strutsAction, activity, true);
    }

    /**
     * Generates an ActivityURL for the next Activity using it's progress. The URL
     * is for the client and so includes hostname etc.
     * Note that this method always returns a LAMS URLs, if a ToolActivity is next
     * the URL will be the action for displaying the tool.
     * Note that the URL could also be a wait message or a jsp to clear the frames.
     * @param progress, the current LearnerProgress.
     * @throws UnsupportedEncodingException
     */
    public String getProgressURL(LearnerProgress progress) throws UnsupportedEncodingException
    {
        String activityURL = null;

        // TODO: lesson complete
        if (progress.isLessonComplete())
        {
            // If lesson complete forward to lesson complete action. This action will
            // cause a client request to clear ALL frames.
            String strutsAction = this.getActivityMappingStrategy()
                                      .getLessonCompleteAction();
            activityURL = strutsActionToURL(strutsAction, null, true);
        }
        else
        {
            //Activity currentActivity = progress.getCurrentActivity();

            if (progress.isParallelWaiting())
            {
                // progress is waiting, goto waiting page
                String strutsAction = this.getActivityMappingStrategy()
                                          .getWaitingAction();
                activityURL = strutsActionToURL(strutsAction, null, true);
            }
            else
            {
                // display next activity
                activityURL = this.getActivityURL(progress.getNextActivity(),
                                                  progress);
                if (progress.getPreviousActivity() instanceof ParallelActivity)
                {
                    // if previous activity was a parallel activity then we need to
                    // clear frames.
                    String strutsAction = "/requestDisplay.do";
                    String redirectURL = strutsActionToURL(strutsAction,
                                                           null,
                                                           true);
                    activityURL = URLEncoder.encode(activityURL, "UTF-8");
                    activityURL = redirectURL + "?url=" + activityURL;
                }
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
    public String getLearnerToolURL(ToolActivity activity, User learner)
    {

        ToolSession toolSession;
        try
        {
            // Get tool session using learner and activity
            toolSession = toolService.getToolSessionByLearner(learner, activity);
        }
        catch (LamsToolServiceException e)
        {
            //TODO define an exception at web layer
            throw new LearnerServiceException(e.getMessage());
        }
        // Append toolSessionId to tool URL		
        return activity.getTool().getLearnerUrl() + "&"
                + WebUtil.PARAM_TOOL_SESSION_ID + "="
                + toolSession.getToolSessionId().toString() + "&"
                + WebUtil.PARAM_MODE + "=" + ToolAccessMode.LEARNER;
    }

    /**
     * Creates a URL for a struts action and an activity.
     * @param strutsAction, the struts action path.
     * @param activity, the activity the action is for.
     * @param useContext, if true prepends the server and context to the URL.
     */
    protected String strutsActionToURL(String strutsAction,
                                       Activity activity,
                                       boolean useContext)
    {
        String url = strutsAction;

        if (activity != null)
        {
            String query = "?activityId=" + activity.getActivityId();
            url += query;
        }
        if (useContext)
        {
            String lamsUrl = getLamsURL();
            url = lamsUrl + url;
        }

        return url;
    }

    private String getLamsURL()
    {
        return baseURL;
    }

    /**
     * Creates a Struts ActionForward for an action and activity.
     * @param strutsAction, the struts action
     * @param activity, activity that is being displayed
     * @param redirect, should the action be a client redirect
     * @return
     */
    protected ActionForward strutsActionToForward(String strutsAction,
                                                  Activity activity,
                                                  boolean redirect)
    {
        ActionForward actionForward;
        if (redirect)
        {
            String activityURL = strutsActionToURL(strutsAction,
                                                   activity,
                                                   false);
            actionForward = new RedirectingActionForward(activityURL);
            actionForward.setName(WebUtil.getStrutsForwardNameFromPath(strutsAction));
        }
        else
        {
            actionForward = new ForwardingActionForward(strutsAction);
            actionForward.setName(WebUtil.getStrutsForwardNameFromPath(strutsAction));

        }

        return actionForward;
    }

    /**
     * Calculate the activity url for progress view at learner side.
     * @param learner the learner who owns the progress data
     * @param activity the activity the learner want to view
     * @return the url for that tool.
     */
    public String calculateActivityURLForProgressView(User learner,
                                                      Activity activity)
    {

        if (activity instanceof ToolActivity)
        {
            return getLearnerToolURL(((ToolActivity) activity), learner);
        }
        else if (activity instanceof GroupingActivity)
            //TODO need to be changed when group action servlet is done
            return "/viewGrouping.do?";

        throw new LearnerServiceException("Fails to get the progress url view"
                + " for activity[" + activity.getActivityId().longValue() + "]");
    }

    public void setToolService(ILamsToolService toolService)
    {
        this.toolService = toolService;
    }

    public void setBaseURL(String baseURL)
    {
        this.baseURL = baseURL;
    }

    public ActivityMappingStrategy getActivityMappingStrategy()
    {
        return activityMappingStrategy;
    }

    public void setActivityMappingStrategy(ActivityMappingStrategy activityMappingStrategy)
    {
        this.activityMappingStrategy = activityMappingStrategy;
    }
}