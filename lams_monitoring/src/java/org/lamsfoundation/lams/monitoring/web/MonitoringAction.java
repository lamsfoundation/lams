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

package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * <p>The action servlet that provide all the monitoring functionalities. It
 * interact with the teacher via flash and JSP monitoring interface.</p>
 * 
 * @author Jacky Fang
 * @since  2005-4-15
 * @version 1.1
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/monitoring" 
 *                parameter="method" 
 *                validate="false"
 * @struts.action-exception key="error.system.monitor" scope="request"
 *                          type="org.lamsfoundation.lams.monitoring.service.MonitoringServiceException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.web.util.CustomStrutsExceptionHandler"
 * @struts:action-forward name="scheduler" path="/TestScheduler.jsp"
 * @struts.action-forward name = "success" path = "/index.jsp"
 * 
 * ----------------XDoclet Tags--------------------
 */
public class MonitoringAction extends LamsDispatchAction
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(MonitoringAction.class);
	
	private IMonitoringService monitoringService;
    //---------------------------------------------------------------------
    // Class level constants - Struts forward
    //---------------------------------------------------------------------
    private static final String SCHEDULER = "scheduler";

    /** If you want the output given as a jsp, set the request parameter "jspoutput" to 
     * some value other than an empty string (e.g. 1, true, 0, false, blah). 
     * If you want it returned as a stream (ie for Flash), do not define this parameter
     */  
	public static String USE_JSP_OUTPUT = "jspoutput";
	
	/** Output the supplied WDDX packet. If the request parameter USE_JSP_OUTPUT
	 * is set, then it sets the session attribute "parameterName" to the wddx packet string.
	 * If  USE_JSP_OUTPUT is not set, then the packet is written out to the 
	 * request's PrintWriter.
	 *   
	 * @param mapping action mapping (for the forward to the success jsp)
	 * @param request needed to check the USE_JSP_OUTPUT parameter
	 * @param response to write out the wddx packet if not using the jsp
	 * @param wddxPacket wddxPacket or message to be sent/displayed
	 * @param parameterName session attribute to set if USE_JSP_OUTPUT is set
	 * @throws IOException
	 */
	private ActionForward outputPacket(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response,
	        		String wddxPacket, String parameterName) throws IOException {
	    String useJSP = WebUtil.readStrParam(request, USE_JSP_OUTPUT, true);
	    if ( useJSP != null && useJSP.length() >= 0 ) {
		    request.getSession().setAttribute(parameterName,wddxPacket);
		    return mapping.findForward("success");
	    } else {
	        PrintWriter writer = response.getWriter();
	        writer.println(wddxPacket);
	        return null;
	    }
	}
	
    private UserDTO getUser() throws IOException {
    	HttpSession ss = SessionManager.getSession();
    	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
    	if ( user != null ) {
    		return user;
    	}
    	
    	throw new IOException("Unable to get user. User in session manager is "+user);
    }

    /**
	 * @param wddxPacket
	 * @return
	 */
	private String extractURL(String wddxPacket) {
		String url = null;
    	String previousString = "<var name='activityURL'><string>";
    	int index = wddxPacket.indexOf(previousString);
    	if ( index > -1 && index+previousString.length() < wddxPacket.length() ) {
    		url = wddxPacket.substring(index+previousString.length());
    		index = url.indexOf("</string>");
    		url = url.substring(0,index);
    	}
    	// replace any &amp; with &
    	url = url.replace("&amp;","&");
    	url = WebUtil.convertToFullURL(url);
    	
		return url;
	}

    
    //---------------------------------------------------------------------
    // Struts Dispatch Method
    //---------------------------------------------------------------------
    /**
     * This STRUTS action method will initialize a lesson for specific learning design with the given lesson title
     * and lesson description.<p>
     * If initialization is successed, this method will return a WDDX message which includes the ID of new lesson.
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
    public ActionForward initializeLesson(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws IOException,
                                                                          ServletException
    {

        this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
        FlashMessage flashMessage = null;
    	
    	try {
    		String title = WebUtil.readStrParam(request,"lessonName");
    		if ( title == null ) title = "lesson";
    		String desc = WebUtil.readStrParam(request,"lessonDescription");
    		if ( desc == null ) desc = "description";
    		long ldId = WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNINGDESIGN_ID);
    		Integer userId = new Integer(WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID));
    		Lesson newLesson = monitoringService.initializeLesson(title,desc,ldId,userId);
    		
    		flashMessage = new FlashMessage("initializeLesson",newLesson.getLessonId());
		} catch (Exception e) {
			flashMessage = new FlashMessage("initializeLesson",
					e.getMessage(),
					FlashMessage.ERROR);
		}
		
		String message =  flashMessage.serializeMessage();
		
        return outputPacket(mapping,request,response,message,"details");
    }

    /**
     * The Struts dispatch method that starts a lesson that has been created
     * beforehand. Most likely, the request to start lesson should be triggered
     * by the flash component. This method will delegate to the Spring service
     * bean to complete all the steps for starting a lesson. Finally, a wddx
     * acknowledgement message will be serialized and sent back to the flash
     * component.
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
    public ActionForward startLesson(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
        long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
    	FlashMessage flashMessage = null;
    	
    	try {
    		monitoringService.startLesson(lessonId);
    		flashMessage = new FlashMessage("startLesson",Boolean.TRUE);
		} catch (Exception e) {
			flashMessage = new FlashMessage("startLesson",
					"Invalid lessonID :" +  lessonId,
					FlashMessage.ERROR);
		}
		
		String message =  flashMessage.serializeMessage();
		
        return outputPacket(mapping,request,response,message,"details");
        
        //return mapping.findForward(SCHEDULER);
    }
    /**
     * The Struts dispatch method that starts a lesson on schedule that has been created
     * beforehand.
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
     * @throws  
     */
    public ActionForward startOnScheduleLesson(ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) throws IOException,
    		ServletException 
    		{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
    	String dateStr = WebUtil.readStrParam(request, MonitoringConstants.PARAM_LESSON_START_DATE);
    	FlashMessage flashMessage = null;
    	
    	try {
    		Date startDate = DateFormat.getInstance().parse(dateStr);
    		monitoringService.startLessonOnSchedule(lessonId,startDate);
    		flashMessage = new FlashMessage("startOnScheduleLesson",Boolean.TRUE);
    	} catch(ParseException e){
    		flashMessage = new FlashMessage("startOnScheduleLesson",
    				"Invalid lesson start datetime format:" +  dateStr,
    				FlashMessage.ERROR);
    	}catch (Exception e) {
    		flashMessage = new FlashMessage("startOnScheduleLesson",
    				"Invalid lessonID :" +  lessonId,
    				FlashMessage.ERROR);
    	}
    	
    	String message =  flashMessage.serializeMessage();
    	
    	return outputPacket(mapping,request,response,message,"details");
    	
    	//return mapping.findForward(SCHEDULER);
    }
    
    /**
     * The Struts dispatch method that finsh a lesson on schedule that has been started
     * beforehand.
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
     * @throws  
     */
    public ActionForward finishOnScheduleLesson(ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) throws IOException,
    		ServletException 
    		{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
    	String dateStr = WebUtil.readStrParam(request, MonitoringConstants.PARAM_LESSON_FINISH_DATE);
    	FlashMessage flashMessage = null;
    	
    	try {
    		Date finishDate = DateFormat.getInstance().parse(dateStr);
    		monitoringService.finishLessonOnSchedule(lessonId,finishDate);
    		flashMessage = new FlashMessage("finishOnScheduleLesson",Boolean.TRUE);
    	} catch(ParseException e){
    		flashMessage = new FlashMessage("finishOnScheduleLesson",
    				"Invalid lesson finish datetime format:" +  dateStr,
    				FlashMessage.ERROR);
    	}catch (Exception e) {
    		flashMessage = new FlashMessage("finishOnScheduleLesson",
    				"Invalid lessonID :" +  lessonId,
    				FlashMessage.ERROR);
    	}
    	
    	String message =  flashMessage.serializeMessage();
    	
    	return outputPacket(mapping,request,response,message,"details");
    }
    
    /**
     * The Struts dispatch method to archive a lesson.  A wddx
     * acknowledgement message will be serialized and sent back to the flash
     * component.
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
    public ActionForward archiveLesson(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
    	FlashMessage flashMessage = null;
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	long lessonId = WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID);
    	
    	try {
    		monitoringService.archiveLesson(lessonId);
    		flashMessage = new FlashMessage("archiveLesson",Boolean.TRUE);
		} catch (Exception e) {
			flashMessage = new FlashMessage("archiveLesson",
					"Invalid lessonID :" +  lessonId,
					FlashMessage.ERROR);
		}
		
		String message =  flashMessage.serializeMessage();
		
        return outputPacket(mapping,request,response,message,"details");
    }
    /**
     * The purpose of suspending is to hide the lesson from learners temporarily. 
     * It doesn't make any sense to suspend a created or a not started (ie scheduled) 
     * lesson as they will not be shown on the learner interface anyway! If the teacher 
     * tries to suspend a lesson that is not in the STARTED_STATE, then an error should 
     * be returned to Flash. 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward suspendLesson(ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) throws IOException,
    		ServletException
    		{
    	FlashMessage flashMessage = null;
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	long lessonId = WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID);
    	
    	try {
    		monitoringService.suspendLesson(lessonId);
    		flashMessage = new FlashMessage("suspendLesson",Boolean.TRUE);
    	} catch (Exception e) {
    		flashMessage = new FlashMessage("suspendLesson",
    				"Error occurs :" +  e.getMessage(),
    				FlashMessage.ERROR);
    	}
    	
    	String message =  flashMessage.serializeMessage();
    	
    	return outputPacket(mapping,request,response,message,"details");
    }
    /**
     * Unsuspend a lesson which state must be Lesson.SUPSENDED_STATE. Otherwise a error message will return to 
     * flash client.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward unsuspendLesson(ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response) throws IOException,
    		ServletException
    		{
    	FlashMessage flashMessage = null;
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	long lessonId = WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID);
    	
    	try {
    		monitoringService.unsuspendLesson(lessonId);
    		flashMessage = new FlashMessage("unsuspendLesson",Boolean.TRUE);
    	} catch (Exception e) {
    		flashMessage = new FlashMessage("unsuspendLesson",
    				"Error occurs :" +  e.getMessage(),
    				FlashMessage.ERROR);
    	}
    	
    	String message =  flashMessage.serializeMessage();
    	
    	return outputPacket(mapping,request,response,message,"details");
    		}
    /**
     * <P>
     * The STRUTS action will send back a WDDX message after marking the lesson by the given lesson ID
     * as <code>Lesson.DISABLED_STATE</code> status.
     * </P>
     * <P>
     * This action need a lession ID as input.
     * </P>
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeLesson(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                                 ServletException{
    	FlashMessage flashMessage = null;
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	long lessonId = WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID);
    	
    	try {
    		monitoringService.removeLesson(lessonId);
    		flashMessage = new FlashMessage("removeLesson",Boolean.TRUE);
		} catch (Exception e) {
			flashMessage = new FlashMessage("removeLesson",
					"Invalid lessonID :" +  lessonId,
					FlashMessage.ERROR);
		}
		String message =  flashMessage.serializeMessage();
    	return outputPacket(mapping,request,response,message,"details");
    	
    }
    /**
     * <P>
     * </P>
     * <P>
     * This action need a lession ID, Learner ID and Activity ID as input. Activity ID is optional, if it is 
     * null, all activities for this learner will complete to as end as possible.
     * </P>
     * @param form 
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward forceComplete(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                                 ServletException{
    	FlashMessage flashMessage = null;
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	//get parameters
    	Long activityId = null;
    	String actId = request.getParameter(AttributeNames.PARAM_ACTIVITY_ID);
    	if(actId != null)
    		try{
    			activityId = new Long(Long.parseLong(actId));
    		}catch(Exception e){
    			activityId = null;
    		}
    	long lessonId = WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID);
    	Integer learnerId = new Integer(WebUtil.readIntParam(request,MonitoringConstants.PARAM_LEARNER_ID));
    	
    	//force complete
    	try {
    		String message = monitoringService.forceCompleteLessonByUser(learnerId,lessonId,activityId);
    		flashMessage = new FlashMessage("removeLesson",message);
		} catch (Exception e) {
			flashMessage = new FlashMessage("forceComplete",
					"Error occurs :" +  e.toString(),
					FlashMessage.ERROR);
		}
		String message =  flashMessage.serializeMessage();
    	return outputPacket(mapping,request,response,message,"details");
    	
    }
    public ActionForward getAllLessons(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response)throws IOException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	String wddxPacket = monitoringService.getAllLessonsWDDX();
    	return outputPacket(mapping, request, response, wddxPacket, "details");
    }
    public ActionForward getLessonDetails(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Long lessonID = new Long(WebUtil.readLongParam(request,"lessonID"));
    	String wddxPacket = monitoringService.getLessonDetails(lessonID);
    	return outputPacket(mapping, request, response, wddxPacket, "details");
    }
    public ActionForward getLessonLearners(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Long lessonID = new Long(WebUtil.readLongParam(request,"lessonID"));
    	String wddxPacket = monitoringService.getLessonLearners(lessonID);
    	return outputPacket(mapping, request, response, wddxPacket, "details");
    }
    public ActionForward getLearningDesignDetails(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Long lessonID = new Long(WebUtil.readLongParam(request,"lessonID"));
    	String wddxPacket = monitoringService.getLearningDesignDetails(lessonID);
    	return outputPacket(mapping, request, response, wddxPacket, "details");
    }
    public ActionForward getAllLearnersProgress(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Long lessonID = new Long(WebUtil.readLongParam(request,"lessonID"));
    	String wddxPacket = monitoringService.getAllLearnersProgress(lessonID);
    	return outputPacket(mapping, request, response, wddxPacket, "details");
    }
    public ActionForward getAllContributeActivities(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Long lessonID = new Long(WebUtil.readLongParam(request,"lessonID"));
    	String wddxPacket = monitoringService.getAllContributeActivities(lessonID);
    	return outputPacket(mapping, request, response, wddxPacket, "details");
    }
    
    public ActionForward getLearnerActivityURL(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException,LamsToolServiceException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Integer userID = new Integer(WebUtil.readIntParam(request,"userID"));
    	Long activityID = new Long(WebUtil.readLongParam(request,"activityID"));
    	//Show learner in monitor in a single call: extract URL and redirect it rather than returning the WDDX packet
    	
    	String wddxPacket = monitoringService.getLearnerActivityURL(activityID,userID);
    	String url = extractURL(wddxPacket);
    	response.sendRedirect(response.encodeRedirectURL(url));
    	
    	return null;
    }
    public ActionForward getActivityContributionURL(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());    	
    	Long activityID = new Long(WebUtil.readLongParam(request,"activityID"));
    	String wddxPacket = monitoringService.getActivityContributionURL(activityID);
    	return outputPacket(mapping, request, response, wddxPacket, "details");
    }
    public ActionForward moveLesson(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Long lessonID = new Long(WebUtil.readLongParam(request,"lessonID"));
    	Integer userID = new Integer(WebUtil.readIntParam(request,"userID"));
    	Integer targetWorkspaceFolderID = new Integer(WebUtil.readIntParam(request,"folderID"));
    	String wddxPacket = monitoringService.moveLesson(lessonID,targetWorkspaceFolderID,userID);
    	return outputPacket(mapping, request, response, wddxPacket, "details");
    }
    public ActionForward renameLesson(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Long lessonID = new Long(WebUtil.readLongParam(request,"lessonID"));
    	Integer userID = new Integer(WebUtil.readIntParam(request,"userID"));
    	String name = WebUtil.readStrParam(request,"name"); 
    	String wddxPacket = monitoringService.renameLesson(lessonID,name,userID);
    	return outputPacket(mapping, request, response, wddxPacket, "details");
    }
    
    public ActionForward checkGateStatus(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        
        this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
        Long activityID = new Long(WebUtil.readLongParam(request, "activityID"));
        Long lessonID = new Long(WebUtil.readLongParam(request, "lessonID"));
        String wddxPacket = monitoringService.checkGateStatus(activityID, lessonID);
       // request.setAttribute(USE_JSP_OUTPUT, "1");
        return outputPacket(mapping, request, response, wddxPacket, "details");
        
    }
    
    public ActionForward releaseGate(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
        Long activityID = new Long(WebUtil.readLongParam(request, "activityID"));
        String wddxPacket = monitoringService.releaseGate(activityID);
       // request.setAttribute(USE_JSP_OUTPUT, "1");
        return outputPacket(mapping, request, response, wddxPacket, "details");
    }

}
