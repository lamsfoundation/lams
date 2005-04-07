/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learning.web.util.LessonLearnerDataManager;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;


/**
 * 
 * <p>The action servlet that triggers the system driven grouping
 * (random grouping) and allows the learner to view the result of the grouping.
 * </p>
 * 
 * @author Jacky Fang
 * @since  2005-3-29
 * @version 1.1
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action name = "GroupingForm"
 * 				  path="/grouping" 
 *                parameter="method" 
 *                validate="false"
 * @struts.action-exception key="error.system.learner" scope="request"
 *                          type="org.lamsfoundation.lams.learning.service.LearnerServiceException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.util.CustomStrutsExceptionHandler"
 * @struts:action-forward name="viewGrouping" path="/grouping.do?method=viewGrouping"
 * @struts:action-forward name="showGroup" path=".grouping"
 * ----------------XDoclet Tags--------------------
 * 
 */
public class GroupingAction extends LamsDispatchAction
{

    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(GroupingAction.class);
    //---------------------------------------------------------------------
    // Class level constants - Session Attributes
    //---------------------------------------------------------------------
	private static final String GROUPS = "groups";
	
    //---------------------------------------------------------------------
    // Class level constants - Struts forward
    //---------------------------------------------------------------------
    private static final String VIEW_GROUPING = "viewGrouping";
    private static final String SHOW_GROUP = "showGroup";
    
    //---------------------------------------------------------------------
    // Struts Dispatch Method
    //---------------------------------------------------------------------    
    /**
     * Perform the grouping for the users who are currently running the lesson.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return 
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward performGrouping(ActionMapping mapping,
                                         ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByID(request,
                                                                             getServlet().getServletContext());
        validateLearnerProgress(learnerProgress);
        
        //initialize service object
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

        
        List currentLearners = LessonLearnerDataManager.getAllLessonLearners(getServlet().getServletContext(),
                                                                             learnerProgress.getLesson().getLessonId().longValue(),
                                                                             learnerService);
        learnerService.performGrouping((GroupingActivity)learnerProgress.getNextActivity(),
                                       currentLearners);
        
        request.setAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE,learnerProgress.getNextActivity());
        request.getSession().setAttribute(LearningWebUtil.ATTR_USER_DATA, 
                                          learnerProgress.getUser());
        request.getSession().setAttribute(LearningWebUtil.ATTR_LESSON_DATA,
                                          learnerProgress.getLesson());
        
        return mapping.findForward(VIEW_GROUPING);
    }

    /**
     * Load up the grouping information and forward to the jsp page to display
     * all the groups and members.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward viewGrouping(ActionMapping mapping,
                                      ActionForm form,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        //initialize service object
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
        //get current user and lesson data via http. It ensures they are availabe
        //in the http session. If not, we assume parameters are coming from 
        //request or falsh and we can create learner and lesson objects.
        User learner = LearningWebUtil.getUserData(request,getServlet().getServletContext());
        Lesson lesson = LearningWebUtil.getLessonData(request,getServlet().getServletContext());
        
        Activity groupingActivity = LearningWebUtil.getActivityFromRequest(request,learnerService);
        
        List groups = new ArrayList(((GroupingActivity)groupingActivity).getCreateGrouping()
                                    									.getGroups());
        request.getSession().setAttribute(GROUPS,groups);
        request.setAttribute(LearningWebUtil.PARAM_ACTIVITY_ID,
                             groupingActivity.getActivityId());
        
        return mapping.findForward(SHOW_GROUP);
    }
    
    /**
     * Complete the current tool activity and forward to the url of next activity
     * in the learning design.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward completeActivity(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByUser(request,
                                                                                 getServlet().getServletContext());
        //initialize service object
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
        Activity groupingActivity = LearningWebUtil.getActivityFromRequest(request,learnerService);

        
        String nextActivityUrl = learnerService.completeActivity(learnerProgress.getUser(),
                                                                  groupingActivity,
                                                                  learnerProgress.getLesson());
        
		response.sendRedirect(nextActivityUrl);
		
        return null;
    }
    //---------------------------------------------------------------------
    // Helper method
    //---------------------------------------------------------------------
    /**
     * @param learnerProgress
     */
    private void validateLearnerProgress(LearnerProgress learnerProgress)
    {
        if(learnerProgress ==null)
            throw new LearnerServiceException("Can't perform grouping without knowing" +
            		" the learner progress.");
        
        if(!isNextActivityValid(learnerProgress))
            throw new LearnerServiceException("Error in progress engine. Getting "
                                              +learnerProgress.getNextActivity().toString()
                                              +" where it should be grouping activity");
    }


    /**
     * Validate the next activity within the learner progress. It should not
     * be null and it should be the grouping activity.
     * @param learnerProgress the learner progress for current learner.
     * @return whether the next activity is valid.
     */
    private boolean isNextActivityValid(LearnerProgress learnerProgress)
    {
        return learnerProgress.getNextActivity()!=null&&(learnerProgress.getNextActivity() instanceof GroupingActivity);
    }

}
