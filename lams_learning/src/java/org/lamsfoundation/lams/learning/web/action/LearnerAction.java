/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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
 * ****************************************************************
 */

/* $$Id$$ */	
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
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dto.ProgressActivityDTO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.dto.LearnerProgressDTO;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;


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
 * 							handler="org.lamsfoundation.lams.learning.util.CustomStrutsExceptionHandler"
 * @struts:action-forward name="displayActivity" path="/DisplayActivity.do"
 * @struts:action-forward name="welcome" path=".welcome"
 * @struts:action-forward name="exit" path="/exit.do"
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
    private static final String EXIT = "exit";
    
	/** Handle an exception - either thrown by the service or by the web layer. Allows the exception
	 * to be logged properly and ensure that an actual message goes back to Flash.
	 * 
	 * @param e
	 * @param methodKey
	 * @param learnerService
	 * @return
	 */
	protected FlashMessage handleException(Exception e, String methodKey, ILearnerService learnerService) {
		log.error("Exception thrown "+methodKey,e);
		String[] msg = new String[1];
		msg[0] = e.getMessage();
		return new FlashMessage(methodKey,
				learnerService.getMessageService().getMessage("error.system.error", msg),
				FlashMessage.CRITICAL_ERROR);
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

        FlashMessage message = null;
    	try {
	
	        //get user and lesson based on request.
	        Integer learner = LearningWebUtil.getUserId();
	        long lessonID = WebUtil.readLongParam(request,LearningWebUtil.PARAM_LESSON_ID);
	
	        
	        if(log.isDebugEnabled())
	            log.debug("The learner ["+learner+"] is joining the lesson ["+lessonID+"]");
	
	        //join user to the lesson on the server
	        LearnerProgress learnerProgress = learnerService.joinLesson(learner,lessonID);
	        
	        if(log.isDebugEnabled())
	            log.debug("The learner ["+learner+"] joined lesson. The"
	                      +"porgress data is:"+learnerProgress.toString());
	        
	        LearningWebUtil.setLearnerProgress(learnerProgress);
	
	        //serialize a acknowledgement flash message with the path of display next
	        //activity
	        message = new FlashMessage("joinLesson", mapping.findForward(DISPLAY_ACTIVITY).getPath());
	
    	} catch (Exception e ) {
    		message = handleException(e, "joinLesson", learnerService);
    	}
    	
        String wddxPacket = WDDXProcessor.serialize(message);
        if(log.isDebugEnabled())
            log.debug("Sending Lesson joined acknowledge message to flash:"+wddxPacket);
        
        //we hand over the control to flash. 
        response.getWriter().print(wddxPacket);
        return null;
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
        //initialize service object
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

        FlashMessage message = null;
    	try {
	
	        LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByUser(request,getServlet().getServletContext());
	        
	        if(log.isDebugEnabled())
	            log.debug("Exiting lesson, lesson id is: "+learnerProgress.getLesson().getLessonId());
	        
	        learnerService.exitLesson(learnerProgress.getLearnerProgressId());
	        
	        //send acknowledgment to flash as it is triggerred by flash
	        message = new FlashMessage("exitLesson",mapping.findForward(EXIT).getPath());

    	} catch (Exception e ) {
    		message = handleException(e, "exitLesson", learnerService);
    	}
    	
        String wddxPacket = WDDXProcessor.serialize(message);
        if(log.isDebugEnabled())
            log.debug("Sending Exit Lesson acknowledge message to flash:"+wddxPacket);
        response.getWriter().print(wddxPacket);
        return null;
    }
    
    /**
     * Gets the basic lesson details (name, descripton, etc) for a lesson. Contains a LessonDTO.
     * Takes a single parameter lessonID
     */
    public ActionForward getLesson(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException,
                                                                          ServletException
    {

        //initialize service object
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

        FlashMessage message = null;
    	try {

    		Long lessonID = WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID);

            if(log.isDebugEnabled())
                log.debug("get lesson..."+lessonID);

            LessonDTO dto = learnerService.getLessonData(lessonID);
	        
	        //send acknowledgment to flash as it is triggerred by flash
	        message = new FlashMessage("getLesson",dto);

    	} catch (Exception e ) {
    		message = handleException(e, "getLesson", learnerService);
    	}
    	
        String wddxPacket = WDDXProcessor.serialize(message);
        if(log.isDebugEnabled())
            log.debug("Sending getLesson data message to flash:"+wddxPacket);
        response.getWriter().print(wddxPacket);
        return null;
    }
    
    /**
     * <p>The struts dispatch action to retrieve the progress data from the 
     * server and tailor it into the object struture that expected by flash.
     * A wddx packet with object data struture is sent back in the end of this 
     * call. It is used to construct or restore the flash learner progress
     * bar</p>
     * 
     * <p>Gets the most recent copy from the database - not the cached version.
     * That way if the cached version has problems, at least we start off right!
     * </p>
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
        
        FlashMessage message = null;
    	try {
	
	        //SessionBean sessionBean = LearningWebUtil.getSessionBean(request,getServlet().getServletContext());
    		
	        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
		    long learnerProgressId = WebUtil.readLongParam(request,LearningWebUtil.PARAM_PROGRESS_ID);
		    LearnerProgressDTO learnerProgress = learnerService.getProgressDTOById(new Long(learnerProgressId));
	        
	        message = new FlashMessage("getFlashProgressData",learnerProgress);

    	} catch (Exception e ) {
    		message = handleException(e, "getFlashProgressData", LearnerServiceProxy.getLearnerService(getServlet().getServletContext()));
    	}
    	
        String wddxPacket = WDDXProcessor.serialize(message);
        if(log.isDebugEnabled())
            log.debug("Sending learner progress data to flash:"+wddxPacket);
        response.getWriter().print(wddxPacket);

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

        FlashMessage message = null;
    	try {
	
	        //get parameter
	        long activityId = WebUtil.readLongParam(request,LearningWebUtil.PARAM_ACTIVITY_ID);
	        
	        //initialize service object
	        ActivityMapping activityMapping = LearnerServiceProxy.getActivityMapping(this.getServlet().getServletContext());
	        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	
	        //getting requested object according to coming parameters
	        Integer learnerId = LearningWebUtil.getUserId();
	        User learner = LearnerServiceProxy.getUserManagementService(getServlet().getServletContext()).getUserById(learnerId);

	        Activity requestedActivity = learnerService.getActivity(new Long(activityId));
	        
	        //preparing tranfer object for flash
	        LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByUser(request,getServlet().getServletContext());
	        ProgressActivityDTO activityDTO = new ProgressActivityDTO(new Long(activityId),
	                 activityMapping.calculateActivityURLForProgressView(learnerProgress.getLesson().getLessonId(),learner,requestedActivity));
	        //send data back to flash.
	        message = new FlashMessage("getLearnerActivityURL",activityDTO);

    	} catch (Exception e ) {
    		message = handleException(e, "getLearnerActivityURL", LearnerServiceProxy.getLearnerService(getServlet().getServletContext()));
    	}
    	
        String wddxPacket = WDDXProcessor.serialize(message);
        if(log.isDebugEnabled())
            log.debug("Sending learner activity url data to flash:"+wddxPacket);
        
        response.getWriter().print(wddxPacket);
        return null;
    }
}