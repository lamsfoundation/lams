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

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;



/** 
 * Action class to display an activity.
 * 
 * XDoclet definition:
 * 
 * ----------------XDoclet Tags-------------------- 
 * @struts:action path="/DisplayActivity" name="activityForm"
 *                validate="false" scope="request"
 * @struts:action-forward name="displayParallelActivity" path="/DisplayParallelActivity.do"
 * @struts:action-forward name="displayOptionsActivity" path="/DisplayOptionsActivity.do"
 * @struts:action-forward name="loadToolActivity" path="/LoadToolActivity.do"
 * @struts:action-forward name="parallelWait" path="/parallelWait.do"
 * @struts:action-forward name="lessonComplete" path="/lessonComplete.do"
 * 
 * 
 * ----------------XDoclet Tags--------------------
 */
public class DisplayActivityAction extends ActivityAction {
    
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(DisplayActivityAction.class);
	
	/** 
	 * Gets an activity from the request (attribute) and forwards onto a
	 * display action using the ActionMappings class. If no activity is
	 * in request then use the current activity in learnerProgress.
	 */
	public ActionForward execute(ActionMapping mapping,
	                             ActionForm actionForm,
	                             HttpServletRequest request,
	                             HttpServletResponse response) 
	{
		
		ActivityMapping actionMappings = LearnerServiceProxy.getActivityMapping(this.getServlet().getServletContext());
		
		//SessionBean sessionBean = LearningWebUtil.getSessionBean(request,getServlet().getServletContext());
		LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByID(request,
		                                                                     getServlet().getServletContext());		
		String progressSummary = getProgressSummary(learnerProgress);			
		
		if(log.isDebugEnabled())
		    log.debug("Entering display activity: progress summary is "+progressSummary);
		
		ActivityForm activityForm = (ActivityForm) actionForm;
		activityForm.setProgressSummary(progressSummary);
		ActionForward forward =actionMappings.getProgressForward(learnerProgress,false,request,getLearnerService());
	
		if(log.isDebugEnabled())
		    log.debug(forward.toString());
		    
		return 	forward;
	}

	private String getProgressSummary(LearnerProgress learnerProgress) {
		StringBuffer progressSummary = new StringBuffer(100);
		if ( learnerProgress == null  ) {
			progressSummary.append("attempted=&completed=&current=");
		} else {
			progressSummary.append("attempted=");
			boolean first = true;
			for ( Object obj : learnerProgress.getAttemptedActivities() ) {
				Activity activity = (Activity ) obj;
				if ( ! first ) {
					progressSummary.append("_");
				} else {
					first = false;
				}
				progressSummary.append(activity.getActivityId());
			}
			
			progressSummary.append("&completed=");
			first = true;
			for ( Object obj : learnerProgress.getCompletedActivities() ) {
				Activity activity = (Activity ) obj;
				if ( ! first ) {
					progressSummary.append("_");
				} else {
					first = false;
				}
				progressSummary.append(activity.getActivityId());
			}

			progressSummary.append("&current=");
			Activity currentActivity = learnerProgress.getCurrentActivity();
			if ( currentActivity != null ) {
				progressSummary.append(currentActivity.getActivityId());
			}
		}
		return progressSummary.toString();
	}
	

}