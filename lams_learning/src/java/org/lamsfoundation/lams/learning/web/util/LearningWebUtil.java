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
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
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
	private static final String PARAM_USER_ID = "userId";
    private static final String PARAM_LESSON_ID = "lessonId";
    private static final String ATTR_USERDATA = "user";
    
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
                                              .getAttribute(ATTR_USERDATA);
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
            request.getSession().setAttribute(ATTR_USERDATA, currentUser);
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
        //initialize service object
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(servletContext);

        long lessonId = WebUtil.readLongParam(request,PARAM_LESSON_ID);
        return learnerService.getLesson(new Long(lessonId));
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
        }
        return bean;
    }

}
