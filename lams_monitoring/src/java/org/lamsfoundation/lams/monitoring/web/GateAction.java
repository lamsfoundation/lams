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
package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SynchGateActivity;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceException;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * <p>The action servlet that allows the teacher to view the status of sync 
 * gate, scheduling gate and permission gate. The teacher can also force the 
 * gate to open through this servlet.</p>
 * 
 * <p>Regarding view gate status, followings contents should be shown by
 *  calling this action servlet:
 * <li>1.View the status of an sync gate, the lams should show how many
 * 		 learners are waiting and the size of the total class.</li>
 * <li>2.View the status of the permission gate, the lams shows the number
 * 		 of the learners waiting in front of the gates. </li>
 * <li>3.View the status of the schedule gate, the lams shows the gate 
 * 		 status. If the schedule has been triggerred. The teacher should 
 * 		 be able to change the trigger.</li>
 * </p>
 * 
 * @author Jacky Fang
 * @since  2005-4-15
 * @version 1.1
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action name="GateForm"
 * 				  path="/gate" 
 *                parameter="method" 
 * 				  scope="session"
 *                validate="false"
 * @struts.action-exception key="error.system.monitor" scope="request"
 *                          type="org.lamsfoundation.lams.monitoring.service.MonitoringServiceException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.web.util.CustomStrutsExceptionHandler"
 * @struts:action-forward name="viewSynchGate" path=".viewSynchGate"
 * @struts:action-forward name="viewPermissionGate" path=".viewPermissionGate"
 * @struts:action-forward name="viewScheduleGate" path=".viewScheduleGate"
 * ----------------XDoclet Tags--------------------
 * 
 */
public class GateAction extends LamsDispatchAction
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	// private static Logger log = Logger.getLogger(GateAction.class);
	
	private IMonitoringService monitoringService;
	private ILearnerService learnerService;
    //---------------------------------------------------------------------
    // Class level constants - Struts forward
    //---------------------------------------------------------------------
    private static final String VIEW_SYNCH_GATE = "viewSynchGate";
    private static final String VIEW_PERMISSION_GATE = "viewPermissionGate";
    private static final String VIEW_SCHEDULE_GATE="viewScheduleGate";
	private static final String ACTIVITY_FORM_FIELD = "activityId";
	private static final String TOTAL_LEARNERS_FORM_FIELD = "totalLearners";

    //---------------------------------------------------------------------
    // Struts Dispatch Method
    //---------------------------------------------------------------------
    /**
     * <p>The dispatch method that allows the teacher to view the status of the 
     * gate. It is expecting the caller passed in lesson id and gate activity
     * id as http parameter. Otherwise, the utility method will generate some
     * exception.</p>
     * 
     * <p>Based on the lesson id and gate activity id, it sets up the gate form
     * to show the waiting learners and the total waiting learners. Regarding
     * schedule gate, it also shows the estimated gate opening time and gate
     * closing time.</p>
     * 
     * <b>Note:</b> gate form attribute <code>waitingLearners</code> got setup
     * after the view is dispatch to ensure there won't be casting exception
     * occur if the activity id is not a gate by chance.
     * 
     * 
     * @param mapping An ActionMapping class that will be used by the Action 
     * class to tell the ActionServlet where to send the end-user.
     *
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet 
     * 		   indicating where the user is to go next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward viewGate(ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        DynaActionForm gateForm = (DynaActionForm)form;
        
        long lessonId = WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID);
        
        // if this is the initial call then activity id will be in the request, otherwise
        // get it from the form (if being called from openGate.jsp
        Long gateIdLong = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);
        if ( gateIdLong == null ) {
        	gateIdLong = (Long)gateForm.get(ACTIVITY_FORM_FIELD);
        }
        long gateId = gateIdLong != null ? gateIdLong.longValue() : -1 ;
        
        this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
        this.learnerService = MonitoringServiceProxy.getLearnerService(getServlet().getServletContext());
        
        Activity gate = monitoringService.getActivityById(gateId);
        
        //setup the total learners
        int totalLearners = learnerService.getActiveLearnersByLesson(lessonId).size();
        gateForm.set(TOTAL_LEARNERS_FORM_FIELD,new Integer(totalLearners));
        gateForm.set(ACTIVITY_FORM_FIELD,gateIdLong);        
        
        return findViewByGateType(mapping, gateForm, gate);
    }
    /**
     * Open the gate if is closed.
     * 
     * @param mapping An ActionMapping class that will be used by the Action 
     * class to tell the ActionServlet where to send the end-user.
     *
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet 
     * 		   indicating where the user is to go next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward openGate(ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());

        DynaActionForm gateForm = (DynaActionForm)form;
        
        GateActivity gate = monitoringService.openGate((Long)gateForm.get(ACTIVITY_FORM_FIELD));
            
        return findViewByGateType(mapping, gateForm, gate);
    }

    //---------------------------------------------------------------------
    // Helper Methods
    //---------------------------------------------------------------------
    /**
     * Dispatch view the according to the gate type.
     * 
     * @param mapping An ActionMapping class that will be used by the Action 
     * class to tell the ActionServlet where to send the end-user.
     * @param gateForm The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param permissionGate the gate acitivty object
     * @return An ActionForward class that will be returned to the ActionServlet 
     * 		   indicating where the user is to go next.
     */
    private ActionForward findViewByGateType(ActionMapping mapping, 
                                             DynaActionForm gateForm, 
                                             Activity gate)
    {
        //dispatch the view according to the type of the gate.
    	if ( gate != null ) {
	        if(gate.isSynchGate())
	            return viewSynchGate(mapping,gateForm,(SynchGateActivity)gate);
	        else if(gate.isScheduleGate())
	            return viewScheduleGate(mapping,gateForm,(ScheduleGateActivity)gate);
	        else if(gate.isPermissionGate())
	            return viewPermissionGate(mapping,gateForm,(PermissionGateActivity)gate);
	        else
	            throw new MonitoringServiceException("Invalid gate activity. " +
	            		"gate id ["+gate.getActivityId()+"] - the type ["+
	            		gate.getActivityTypeId()+"] is not a gate type");
    	} else {
    		throw new MonitoringServiceException("Gate activity missing. " +
        		"gate id ["+gate.getActivityId()+"]");
    	}
    }



    /**
     * Set up the form attributes specific to the permission gate and navigate
     * to the permission gate view.
     * @param mapping An ActionMapping class that will be used by the Action 
     * class to tell the ActionServlet where to send the end-user.
     * @param gateForm The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param permissionGate the gate acitivty object
     * @return An ActionForward class that will be returned to the ActionServlet 
     * 		   indicating where the user is to go next.
     */
    private ActionForward viewPermissionGate(ActionMapping mapping,
                                             DynaActionForm gateForm,
                                             PermissionGateActivity permissionGate)
    {
        gateForm.set("gate",permissionGate);
        gateForm.set("waitingLearners",new Integer(permissionGate.getWaitingLearners().size()));
        return mapping.findForward(VIEW_PERMISSION_GATE);
    }

    /**
     * Set up the form attributes specific to the schedule gate and navigate
     * to the schedule gate view.
     * 
     * @param mapping An ActionMapping class that will be used by the Action 
     * class to tell the ActionServlet where to send the end-user.
     * @param gateForm The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param permissionGate the gate acitivty object
     * @return An ActionForward class that will be returned to the ActionServlet 
     * 		   indicating where the user is to go next.
     */
    private ActionForward viewScheduleGate(ActionMapping mapping, 
                                           DynaActionForm gateForm,
                                           ScheduleGateActivity scheduleGate)
    {
        gateForm.set("gate",scheduleGate);
        gateForm.set("waitingLearners",new Integer(scheduleGate.getWaitingLearners().size()));
        gateForm.set("startingTime",scheduleGate.getGateStartDateTime());
        gateForm.set("endingTime",scheduleGate.getGateEndDateTime());
        
        return mapping.findForward(VIEW_SCHEDULE_GATE);
    }

    /**
     * Set up the form attributes specific to the synch gate and navigate
     * to the synch gate view.
     * 
     * @param mapping An ActionMapping class that will be used by the Action 
     * class to tell the ActionServlet where to send the end-user.
     * @param gateForm The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param permissionGate the gate acitivty object
     * @return An ActionForward class that will be returned to the ActionServlet 
     * 		   indicating where the user is to go next.
     */
    private ActionForward viewSynchGate(ActionMapping mapping,
                                        DynaActionForm gateForm,
                                        SynchGateActivity synchgate)
    {
        gateForm.set("gate",synchgate);
        gateForm.set("waitingLearners",new Integer(synchgate.getWaitingLearners().size()));
        return mapping.findForward(VIEW_SYNCH_GATE);
    }

}
