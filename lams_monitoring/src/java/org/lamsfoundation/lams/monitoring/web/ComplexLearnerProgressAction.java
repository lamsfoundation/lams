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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */ 
 
/* $Id$ */ 
package org.lamsfoundation.lams.monitoring.web; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.support.WebApplicationContextUtils;
 
/**
 * @author jliew
 *
 * @struts:action path="/complexProgress" 
 *                validate="false"
 * @struts.action-forward name = "complexProgress" path = ".complexProgress"
 * @struts.action-forward name = "parallelProgress" path = "/parallelProgress.jsp"
 */
public class ComplexLearnerProgressAction extends Action {

	private static Logger log = Logger.getLogger(ComplexLearnerProgressAction.class);

	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
		
		Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, false);
		Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, false);
		Integer userID = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false);
		
		IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
		Activity activity = monitoringService.getActivityById(activityID);
		
		IUserManagementService userService = (IUserManagementService)WebApplicationContextUtils
			.getRequiredWebApplicationContext(getServlet().getServletContext()).getBean("userManagementService");
		User learner = (User)userService.findById(User.class, userID);
		
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	
		if (activity.isOptionsActivity() || activity.isBranchingActivity()) {
			
			HashMap<Long, Boolean> startedMap = new HashMap<Long, Boolean>();
			HashMap<Long, String> urlMap = new HashMap<Long, String>();
			
			ComplexActivity complexActivity = (ComplexActivity)activity;
			Set subActivities = complexActivity.getActivities();
			Iterator i = subActivities.iterator();
			
			// iterate through each optional or branching activity
			while (i.hasNext()) {
				Activity a = (Activity)i.next();
				List<User> users = monitoringService.getLearnersHaveAttemptedActivity(a);
				startedMap.put(a.getActivityId(), ( users.contains(learner) ? true : false ) );
				if (a.isSequenceActivity()) {
					
					request.setAttribute("hasSequenceActivity", true);
					// map learner progress urls of each activity in the sequence
					// make sure have castable object, not a CGLIB class
					SequenceActivity sequenceActivity = (SequenceActivity) monitoringService.getActivityById(a.getActivityId(), SequenceActivity.class); 
					Set set = sequenceActivity.getActivities();
					Iterator iterator = set.iterator();
					while (iterator.hasNext()) {
						Activity child = (Activity)iterator.next();
						users = monitoringService.getLearnersHaveAttemptedActivity(child);
						boolean hasAttempted = users.contains(learner);
						startedMap.put(child.getActivityId(), ( hasAttempted ? true : false ) );
						if (hasAttempted) {
							// learner progress url
							urlMap.put(child.getActivityId(), 
								monitoringService.getLearnerActivityURL(lessonID, child.getActivityId(), userID, user.getUserID()));
						}
					}
				}
			}
			
			// learner progress urls for children of the sequence activities
			request.setAttribute("urlMap", urlMap);
			// boolean flags for whether an activity is started
			request.setAttribute("startedMap", startedMap);
			// set of child activities
			request.setAttribute("subActivities", subActivities);
			// main activity title
			request.setAttribute("activityTitle", activity.getTitle());
			
			return mapping.findForward("complexProgress");
		} else if (activity.isParallelActivity()) {
			ArrayList<String> urls = new ArrayList<String>();
			ParallelActivity parallelActivity = (ParallelActivity)activity;
			Set parallels = parallelActivity.getActivities();
			Iterator i = parallels.iterator();
			while (i.hasNext()) {
				Activity a = (Activity)i.next();
				// get learner progress url for this parallel activity
				urls.add(monitoringService.getLearnerActivityURL(lessonID, a.getActivityId(), userID, user.getUserID()));
			}
			request.setAttribute("parallelUrls", urls);
			return mapping.findForward("parallelProgress");
		}
		
		return null;
	}
}
 