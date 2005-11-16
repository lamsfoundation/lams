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

package org.lamsfoundation.lams.learning.web.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.action.ActivityAction;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.WebUtil;


/**
 * 
 * @author Jacky Fang
 * @since  2005-3-10
 * @version
 * 
 */
public class LearningWebUtil
{
    
	private static Logger log = Logger.getLogger(LearningWebUtil.class);
    //---------------------------------------------------------------------
    // Class level constants - session attributes
    //---------------------------------------------------------------------
	public static final String PARAM_USER_ID = "userId";
	public static final String PARAM_LESSON_ID = "lessonId";
	public static final String ATTR_USER_DATA = "user";
	public static final String ATTR_LESSON_DATA ="lesson";
	public static final String PARAM_ACTIVITY_ID = "activityId";
	public static final String PARAM_PROGRESS_ID = "progressId";
    
    /**
     * Helper method to retrieve the user data. We always load up from http
     * session first to optimize the performance. If no session cache available,
     * we load it from data source.
     * @param request A standard Servlet HttpServletRequest class.
     * @param servletContext the servlet container that has all resources
     * @param surveyService the service facade of survey tool
     * @return the user data value object
     */
    public static User getUserData(HttpServletRequest request, ServletContext servletContext)
    {
        //retrieve complete user data from http session
        User currentUser = (User) request.getSession()
                                              .getAttribute(ATTR_USER_DATA);
        if(log.isDebugEnabled()&&currentUser!=null)
            log.debug("user retrieved from http session:"+currentUser.getUserId());
        
        //if no session cache available, retrieve it from data source
        if (currentUser == null)
        {
            int userId = WebUtil.readIntParam(request,PARAM_USER_ID);
            currentUser = LearnerServiceProxy.getUserManagementService(servletContext)
            								 .getUserById(new Integer(userId));
            if(log.isDebugEnabled()&&currentUser!=null)
                log.debug("user retrieved from database:"+currentUser.getUserId());
            
            //create session cache
            request.getSession().setAttribute(ATTR_USER_DATA, currentUser);
        }
        return currentUser;
    }
    
    /**
     * Helper method to get lesson. 
     * 
     * @param request A standard Servlet HttpServletRequest class.
     * @param learnerService leaner service facade.
     * @param servletContext the servlet container that has all resources
     * @return The requested lesson.
     */
    public static Lesson getLessonData(HttpServletRequest request, ServletContext servletContext)
    {
        Lesson lesson = (Lesson)request.getAttribute(ATTR_LESSON_DATA);
        
        if(lesson ==null)
        {
            //initialize service object
            ILearnerService learnerService = LearnerServiceProxy.getLearnerService(servletContext);
            long lessonId = WebUtil.readLongParam(request,PARAM_LESSON_ID);
            lesson = learnerService.getLesson(new Long(lessonId));
            request.getSession().setAttribute(ATTR_LESSON_DATA,lesson);
        }
        return lesson;
    }
    
    /**
     * Helper method to get session bean. 
     * TODO resolve the duplicate code in activity action.
     * @param request A standard Servlet HttpServletRequest class.
     * @param servletContext the servlet container that has all resources
     * @return The requested lesson.
     */
    public static SessionBean getSessionBean(HttpServletRequest request,ServletContext servletContext)
    {
        SessionBean bean = (SessionBean)request.getSession().getAttribute(SessionBean.NAME);
        
        //create new one from database
        if(bean ==null)
        {
            //initialize service object
            ILearnerService learnerService = LearnerServiceProxy.getLearnerService(servletContext);

            User currentLearner = getUserData(request, servletContext);
            Lesson lesson = getLessonData(request,servletContext);
            LearnerProgress progress = learnerService.getProgress(currentLearner,lesson);
            bean = new SessionBean(currentLearner,lesson,progress);
            
            request.getSession().setAttribute(SessionBean.NAME,bean);
            request.getSession().setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,progress);
        }
        return bean;
    }

	/** 
	 * Get the current learner progress. The http session attributes are checked
	 * first, if not in request then a new LearnerProgress is loaded by id using
	 * the LearnerService. The LearnerProgress is also stored in the
	 * session so that the Flash requests don't have to reload it.
	 */
	public static LearnerProgress getLearnerProgressByID(HttpServletRequest request,ServletContext servletContext) {
		//TODO need to be retrieved from proper cache when caching done properly.
	    LearnerProgress learnerProgress = (LearnerProgress)request.getSession().getAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE);
		
		if (learnerProgress == null) 
		{
            //initialize service object
            ILearnerService learnerService = LearnerServiceProxy.getLearnerService(servletContext);
		    
		    long learnerProgressId = WebUtil.readLongParam(request,LearningWebUtil.PARAM_PROGRESS_ID);
		    
		    learnerProgress = learnerService.getProgressById(new Long(learnerProgressId));

            request.getSession().setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,learnerProgress);
		}
		return learnerProgress;
	}
	/** 
	 * Get the current learner progress. The http session attributes are checked
	 * first, if not in request then a new LearnerProgress is loaded by user
	 * and lesson using the LearnerService. The LearnerProgress is also stored 
	 * in the session so that the Flash requests don't have to reload it.
	 */
	public static LearnerProgress getLearnerProgressByUser(HttpServletRequest request,ServletContext servletContext) {
		
	    LearnerProgress learnerProgress = (LearnerProgress)request.getSession().getAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE);
		
		if (learnerProgress == null) 
		{
            //initialize service object
            ILearnerService learnerService = LearnerServiceProxy.getLearnerService(servletContext);
		    
            User currentLearner = getUserData(request, servletContext);
            Lesson lesson = getLessonData(request,servletContext);
            
            learnerProgress = learnerService.getProgress(currentLearner,lesson);

            request.getSession().setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,learnerProgress);
		}
		return learnerProgress;
	}

    /**
     * Get the activity from request. We assume there is a parameter coming in
     * if there is no activity can be found in the http request. Then the 
     * activity id parameter is used to retrieve from database.
     * @param request
     * @return
     */
    public static Activity getActivityFromRequest(HttpServletRequest request,
                                                  ILearnerService learnerService)
    {
        Activity activity = (Activity)request.getAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE);
        
        if(activity == null)
        {
            long activityId = WebUtil.readLongParam(request,PARAM_ACTIVITY_ID);
            
            activity = learnerService.getActivity(new Long(activityId));
            
        }
        return activity;
    }
    
    /**
     * Put an activity into the request. Calls LearnerService to get the activity, to ensure 
     * that it is a "real" activity, not one of the cglib proxies. 
     * activity.
     * @param request
     * @param activity
     */
    public static void putActivityInRequest(HttpServletRequest request, Activity activity,
                                                  ILearnerService learnerService)
    {
        Activity realActivity = learnerService.getActivity(activity.getActivityId());
        request.setAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE, realActivity);
    }

}
