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


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;

/** 
 * Action class to forward the user to a Tool using an intermediate loading page. Can handle
 * regular tools + grouping and gates (system tools). Displays the activity that is in the 
 * request. This allows it to show any arbitrary activity, not just the current activity.
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/LoadToolActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 * @struts:action-forward name="displayTool" path=".loadToolActivity"
 * @struts:action-forward name="previewDefineLater" path=".previewDefineLater"
 * 
 */
public class LoadToolActivityAction extends ActivityAction {

	public static final String DEFINE_LATER = "previewDefineLater";
	public static final String PARAM_ACTIVITY_TITLE = "activityTitle";
	public static final String PARAM_ACTIVITY_URL = "activityURL";

	/**
	 * Gets an activity from the request (attribute) and forwards onto a
	 * loading page.
	 */
	public ActionForward execute(ActionMapping mapping,
	                             ActionForm actionForm,
	                             HttpServletRequest request,
	                             HttpServletResponse response) 
	{
		setupProgressString(actionForm, request);

		ActivityForm form = (ActivityForm)actionForm;
		ActivityMapping actionMappings = LearnerServiceProxy.getActivityMapping(this.getServlet().getServletContext());
		
		ILearnerService learnerService = getLearnerService();
		LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request,learnerService);
		Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);
				
		form.setActivityId(activity.getActivityId());
		
		if (activity.isToolActivity() || activity.isSystemToolActivity() ) {

			String url = actionMappings.getLearnerToolURL(learnerProgress.getLesson(),activity, learnerProgress.getUser());

			if ( activity.getDefineLater() && learnerProgress.getLesson().isPreviewLesson() ) {
				// preview define later
				request.setAttribute(PARAM_ACTIVITY_TITLE, activity.getTitle());
				request.setAttribute(PARAM_ACTIVITY_URL, url);
				return mapping.findForward("previewDefineLater");
			} else {
				// normal case
				form.addActivityURL(new ActivityURL(activity.getActivityId(),url));
			}
			
		} else {
		    log.error(className+": activity not ToolActivity");
			return mapping.findForward(ActivityMapping.ERROR);
		}
		
		return mapping.findForward("displayTool");
	}


}