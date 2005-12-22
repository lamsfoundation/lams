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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * <p>An action servlet to support the dummy monitoring page dummy.jsp.</p>
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/dummy" 
 * 				  name="DummyForm"
 *                parameter="method" 
 *                validate="false"
 * @struts.action-exception key="error.system.monitor" scope="request"
 *                          type="org.lamsfoundation.lams.monitoring.service.MonitoringServiceException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.web.util.CustomStrutsExceptionHandler"
 * @struts:action-forward name="detail" path="/dummyDetail.jsp"
 * @struts:action-forward name="start" path="/dummyStart.jsp"
 * @struts:action-forward name="started" path="/dummyStarted.jsp"
 * @struts:action-forward name="control" path="/dummyControlFrame.jsp"
 * 
 * ----------------XDoclet Tags--------------------
 */
public class DummyMonitoringAction extends LamsDispatchAction
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private IMonitoringService monitoringService;
    private IUserManagementService usermanageService;

    //---------------------------------------------------------------------
    // Class level constants - session attributes
    //---------------------------------------------------------------------
	// input parameters
    // output parameters
    private static final String CONTROL_FORWARD = "control";
    private static final String DETAIL_FORWARD = "detail";
    private static final String START_LESSON_FORWARD = "start";
    private static final String LESSON_STARTED_FORWARD = "started";

    private static final String LESSON_PARAMETER = "lesson";
    private static final String LESSONS_PARAMETER = "lessons";
    private static final String ACTIVITIES_PARAMETER = "activities";
    private static final String ORGS_PARAMETER = "organisations";
    private static final String DESIGNS_PARAMETER = "designs";
    
    
    /** Default method for this action. Gets all the current lessons and forwards to the control page */
    public ActionForward unspecified(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response)throws IOException{

    	setupServices();

    	User user = getUser();
    	List lessons = monitoringService.getAllLessons(user.getUserId());
	    request.getSession().setAttribute(LESSONS_PARAMETER,lessons);
    	return mapping.findForward(CONTROL_FORWARD);

    }
    
    /** Sets up the "create a lesson" screen, forwarding to dummyStart.jsp */
    public ActionForward initStartScreen(ActionMapping mapping,
                                         ActionForm form,
                                         HttpServletRequest request,
                                         HttpServletResponse response)throws IOException{
    	setupServices();

    	User user = getUser();
    	
    	List designs = monitoringService.getLearningDesigns(new Long(user.getUserId().longValue()));
	    request.getSession().setAttribute(DESIGNS_PARAMETER,designs);
    	
    	List organisations = monitoringService.getOrganisationsUsers(user.getUserId());
	    request.getSession().setAttribute(ORGS_PARAMETER,organisations);
	    
    	return mapping.findForward(START_LESSON_FORWARD);
    }

    //---------------------------------------------------------------------
    // Struts Dispatch Method
    //---------------------------------------------------------------------
    /**
     * The Struts dispatch method that initialised and start a lesson.
     * It will start a lesson with the current user as the staff and learner.
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
            HttpServletResponse response)throws IOException{
    	
    	setupServices();
    	
    	// set up all the data needed
    	DummyForm dummyForm = (DummyForm) form;

    	// Must have User, rather than UserDTO as the createLessonClassForLesson must have 
    	// a proper user objcet in the staffs list.
        User user = getUser();

        Long ldId = dummyForm.getLearningDesignId();
    	if ( ldId == null )
    		throw new IOException("Learning design id must be set");
    	
        Integer organisationId = dummyForm.getOrganisationId();
    	if ( organisationId == null )
    		throw new IOException("Organisation must be set");
        Organisation organisation = usermanageService.getOrganisationById(organisationId);
    	if ( organisation == null )
    		throw new IOException("Organisation cannot be found. Id was "+organisationId);

    	String title = dummyForm.getTitle();
        if ( title == null ) title = "lesson";
        String desc = dummyForm.getDesc(); 
        if ( desc == null ) desc = "description";

        // initialize the lesson
        Lesson testLesson = monitoringService.initializeLesson(title,desc,ldId.longValue(),user.getUserId());

        // create the lesson class - add all the users in this organisation to the lesson class
        // add user as staff
        LinkedList learners = new LinkedList();
        Iterator iter = organisation.getUsers().iterator();
        learners.add(user);
        while (iter.hasNext()) {
			User element = (User) iter.next();
			learners.add(element);
		}

        LinkedList staffs = new LinkedList();
        staffs.add(user);
        
        testLesson = monitoringService.createLessonClassForLesson(testLesson.getLessonId().longValue(),
        		organisation,
				learners,
                staffs);

        // start the lesson.
        this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
        monitoringService.startLesson(testLesson.getLessonId().longValue());

    	return mapping.findForward(LESSON_STARTED_FORWARD);
    }

    /**
     * The Struts dispatch method to archive a lesson.  Forwards to the control
     * list jsp.
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
        this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());

        long lessonId = WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID);
        monitoringService.archiveLesson(lessonId);

        return unspecified(mapping, form, request, response);
    }

    /**
     * The Struts dispatch method that lists a lesson's details. Forwards to the detail screen
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
    
    public ActionForward getLesson(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException{
    	
    	setupServices();

    	User user = getUser();
    	Long lessonId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID));
    	List lessons = monitoringService.getAllLessons(user.getUserId());
    	
    	Iterator iter = lessons.iterator();
		boolean found = false;
    	while (iter.hasNext() && !found) {
			Lesson element = (Lesson) iter.next();
			if ( element.getLessonId().equals(lessonId) ) {
				// this is nasty code but it will do for the dummy screen.
			    // can't just use the activities out of the learning design
				// as we get CGLIB objects! If this was permanent code,
				// then we would generate the data properly in the service level.
				Set activities = element.getLearningDesign().getActivities();
				Set activityDTOSet= new HashSet();
				Set processedActivityIds = new HashSet();

				Iterator actIterator = activities.iterator();
				while (actIterator.hasNext()) {
					Activity activity = (Activity) actIterator.next();
					Set dtos = activity.getAuthoringActivityDTOSet();
					Iterator dtoIterator = dtos.iterator();
					while (dtoIterator.hasNext()) {
						AuthoringActivityDTO dto= (AuthoringActivityDTO) dtoIterator.next();
						if ( ! processedActivityIds.contains(dto.getActivityID()) ) {
							activityDTOSet.add(dto);
							processedActivityIds.add(dto.getActivityID());
						}
					}
				}
				// now that all the activities are loaded, we can copy them into our own set.
				
				
				// add desired lesson and activities to the session
			    request.getSession().setAttribute(LESSON_PARAMETER,element);
			    request.getSession().setAttribute(ACTIVITIES_PARAMETER, activityDTOSet);
			}
		}
    	return mapping.findForward(DETAIL_FORWARD);

    }

    private void setupServices() {
    	
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
    	this.usermanageService= (IUserManagementService) wac.getBean("userManagementService");

    }
    private User getUser() throws IOException {
    	HttpSession ss = SessionManager.getSession();
    	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
    	if ( user != null ) {
    		return usermanageService.getUserById(user.getUserID());
    	}
    	throw new IOException("Unable to get user. User in session manager is "+user);
    }
    
    public ActionForward gotoLearnerActivityURL(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException,LamsToolServiceException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
    	Long activityID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_ACTIVITY_ID));
    	String wddxPacket = monitoringService.getLearnerActivityURL(activityID,userID);
    	String url = extractURL(wddxPacket);
    	response.sendRedirect(response.encodeRedirectURL(url));
    	return null;
    }

    public ActionForward gotoMonitoringActivityURL(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException,LamsToolServiceException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Long activityID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_ACTIVITY_ID));
    	String wddxPacket = monitoringService.getActivityMonitorURL(activityID);
    	String url = extractURL(wddxPacket);
    	response.sendRedirect(response.encodeRedirectURL(url));
    	return null;
    }
    public ActionForward gotoDefineLaterActivityURL(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException,LamsToolServiceException{
    	this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Long activityID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_ACTIVITY_ID));
    	String wddxPacket = monitoringService.getActivityDefineLaterURL(activityID);
    	String url = extractURL(wddxPacket);
    	response.sendRedirect(response.encodeRedirectURL(url));
    	return null;
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

 /*    public ActionForward getLessonDetails(ActionMapping mapping,
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
*/
}
