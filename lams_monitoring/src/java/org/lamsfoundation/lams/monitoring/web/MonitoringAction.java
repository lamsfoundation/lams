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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
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
	
    //---------------------------------------------------------------------
    // Struts Dispatch Method
    //---------------------------------------------------------------------
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

        monitoringService.startLesson(lessonId);

        //TODO add the wddx acknowledgement code.
        
        //return mapping.findForward(SCHEDULER);
        return null;
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
        this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());

        long lessonId = WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID);
        monitoringService.archiveLesson(lessonId);

        //TODO add the wddx acknowledgement code.
        
        //return mapping.findForward(SCHEDULER);
        return null;
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
    	String wddxPacket = monitoringService.getLearnerActivityURL(activityID,userID);
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

}
