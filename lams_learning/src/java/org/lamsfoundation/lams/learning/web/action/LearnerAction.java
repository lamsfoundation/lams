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

package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learning.web.util.LessonLearnerDataManager;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dto.ProgressActivityDTO;



import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.usermanagement.User;

import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;


/** 
 * 
 * <p>The action servlet that interacts with learner to start a lams learner
 * module, join a user to the lesson and allows a user to exit a lesson.</p>
 * 
 * <p>It is also responsible for the interaction between lams server and 
 * flash. Flash will call method implemented in this class to get progress
 * data or trigger a lams server calculation here</p>
 * 
 * <b>Note:</b>It needs to extend the <code>LamsDispatchAction</code> which has
 * been customized to accomodate struts features to solve duplicate 
 * submission problem.
 * 
 * @author Jacky Fang
 * @since 3/03/2005
 * @version 1.1
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/learner" 
 *                parameter="method" 
 *                validate="false"
 * @struts.action-exception key="error.system.learner" scope="request"
 *                          type="org.lamsfoundation.lams.learning.service.LearnerServiceException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.util.CustomStrutsExceptionHandler"
 * @struts:action-forward name="displayActivity" path="/DisplayActivity.do"
 * @struts:action-forward name="welcome" path=".welcome"
 * 
 * ----------------XDoclet Tags--------------------
 * 
 */
public class LearnerAction extends LamsDispatchAction 
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(LearnerAction.class);
	
    //---------------------------------------------------------------------
    // Class level constants - Struts forward
    //---------------------------------------------------------------------
    private static final String DISPLAY_ACTIVITY = "displayActivity";
    private static final String WELCOME = "welcome";
    /**
     * <p>The Struts dispatch method that retrieves all active lessons for a 
     * requested user from flash. The returned is structured as dto format 
     * rather than the whole lesson domain object and it is serialized into
     * a wddx packet so as to be sent back to flash.</p>
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     *
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getActiveLessons(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        //initialize service object
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

        //get learner.
        User learner = LearningWebUtil.getUserData(request, getServlet().getServletContext());
        if(log.isDebugEnabled())
            log.debug("Getting active lessons for leaner:"+learner.getFullName()+"["+learner.getUserId()+"]");

        LessonDTO [] lessons = learnerService.getActiveLessonsFor(learner);
        
        String activeLessons = WDDXProcessor.serialize(new FlashMessage("getActiveLessons",lessons));
        
        if(log.isDebugEnabled())
            log.debug("Sending flash active lessons message:"+activeLessons);
        
        response.getWriter().print(activeLessons);
        
        //don't need to return a action forward because it sent the wddx packet
        //back already.
        return null;
    }

    /**
     * <p>The structs dispatch action that joins a learner into a lesson. The
     * learner could either start a lesson or resume a lesson.</p>
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     * 
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * 
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward joinLesson(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        //initialize service object
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

        //get user and lesson based on request.
        User learner = LearningWebUtil.getUserData(request, getServlet().getServletContext());
        Lesson lesson = LearningWebUtil.getLessonData(request,getServlet().getServletContext());

        
        if(log.isDebugEnabled())
            log.debug("The learner ["+learner.getUserId()+"],["+learner.getFullName()
                      +"is joining the lesson ["+lesson.getLessonId()+"],["+lesson.getLessonName()+"]");

        //join user to the lesson on the server
        LearnerProgress learnerProgress = learnerService.joinLesson(learner,lesson);
        
        if(log.isDebugEnabled())
            log.debug("The learner ["+learner.getUserId()+"] joined lesson. The"
                      +"porgress data is:"+learnerProgress.toString());
        
        
        LessonLearnerDataManager.cacheLessonUser(getServlet().getServletContext(),
                                                 lesson,learner);
        //setup session attributes
        //request.getSession().setAttribute(SessionBean.NAME,new SessionBean(learner,
        //                                                                   lesson,
        //                                                                   learnerProgress));
        request.getSession().setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,learnerProgress);

        //serialize a acknowledgement flash message with the path of display next
        //activity
        String lessonJoined = WDDXProcessor.serialize(new FlashMessage("joinLesson",
                                                                       mapping.findForward(DISPLAY_ACTIVITY).getPath()));

        if(log.isDebugEnabled())
            log.debug("Sending Lesson joined acknowledge message to flash:"+lessonJoined);

        //we hand over the control to flash. 
        response.getWriter().print(lessonJoined);

        //TODO this should return null once flash side is done, it should be 
        //called by flash side
        return mapping.findForward(DISPLAY_ACTIVITY);
    }
    


    /**
     * <p>Exit the current lesson that is running in the leaner window. It 
     * expects lesson id passed as parameter from flash component.
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     * 
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * 
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward exitLesson(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        if(log.isDebugEnabled())
            log.debug("Exiting lesson...");

        //initialize service object
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

        //SessionBean sessionBean = LearningWebUtil.getSessionBean(request,getServlet().getServletContext());
        LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByUser(request,getServlet().getServletContext());
        
        if(log.isDebugEnabled())
            log.debug("Lesson id is: "+learnerProgress.getLesson().getLessonId());
        
        learnerService.exitLesson(learnerProgress);
        
        LessonLearnerDataManager.removeLessonUserFromCache(getServlet().getServletContext(),
                                                           learnerProgress.getLesson(),
                                                           learnerProgress.getUser());
        //send acknowledgment to flash as it is triggerred by flash
        String lessonExitted = WDDXProcessor.serialize(new FlashMessage("exitLesson",null));
        if(log.isDebugEnabled())
            log.debug("Sending Exit Lesson acknowledge message to flash:"+lessonExitted);
        response.getWriter().print(lessonExitted);
        
        //forward to welcome page
        return mapping.findForward(WELCOME);
    }
    
    /**
     * <p>The struts dispatch action to retrieve the progress data from the 
     * server and tailor it into the object struture that expected by flash.
     * A wddx packet with object data struture is sent back in the end of this 
     * call. It is used to construct or restore the flash learner progress
     * bar</p>
     * 
     * <p>As this process is expensive, the server is only expecting this call
     * whenever is necessary. For example, starting, resuming and restoring
     * a new lesson. And it should not happen every time that learner is
     * progressing to next activity.</p>
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     * 
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getFlashProgressData(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        if(log.isDebugEnabled())
            log.debug("Getting Flash progress data...");
        
        //SessionBean sessionBean = LearningWebUtil.getSessionBean(request,getServlet().getServletContext());
        LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByID(request,getServlet().getServletContext());
        
        String progressData = WDDXProcessor.serialize(new FlashMessage("getFlashProgressData",
                                                                       learnerProgress.getLearnerProgressData()));

        if(log.isDebugEnabled())
            log.debug("Sending learner progress data to flash:"+progressData);
        response.getWriter().print(progressData);

        //don't need to return a action forward because it sent the wddx packet
        //back already.
        return null;
    }
   
    /**
     * <p>The struts dispatch action to view the activity. This will be called 
     * by flash progress bar to check up the activity component. The lams side 
     * will calculate the url and send a flash message back to the 
     * flash component.</p>
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     * 
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getLearnerActivityURL(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws IOException,
                                                                          	ServletException
    {
        if(log.isDebugEnabled())
            log.debug("Getting url for learner activity...");
        //get parameter
        long activityId = WebUtil.readLongParam(request,LearningWebUtil.PARAM_ACTIVITY_ID);
        
        //initialize service object
        ActivityMapping activityMapping = LearnerServiceProxy.getActivityMapping(this.getServlet().getServletContext());
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

        //getting requested object according to coming parameters
        User learner = LearningWebUtil.getUserData(request, getServlet().getServletContext());
        Activity requestedActivity = learnerService.getActivity(new Long(activityId));
        
        //preparing tranfer object for flash
        ProgressActivityDTO activityDTO = new ProgressActivityDTO(new Long(activityId),
                                                                  activityMapping.calculateActivityURLForProgressView(learner,requestedActivity));
        //send data back to flash.
        String learnerActivityURL = WDDXProcessor.serialize(new FlashMessage("getLearnerActivityURL",
                                                                             activityDTO));

        if(log.isDebugEnabled())
            log.debug("Sending learner activity url data to flash:"+learnerActivityURL);
        
        //don't need to return a action forward because it sent the wddx packet
        //back already.
        return null;
    }
}