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

import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;

import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;



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
		
		SessionBean sessionBean = LearningWebUtil.getSessionBean(request,getServlet().getServletContext());
				
		if(log.isDebugEnabled())
		    log.debug("Entering display activity: the session bean is"
		              + sessionBean.toString());
		
		ActionForward forward =actionMappings.getProgressForward(sessionBean.getLearnerProgress(),false,request);
	
		if(log.isDebugEnabled())
		    log.debug(forward.toString());
		    
		return 	forward;
	}
}