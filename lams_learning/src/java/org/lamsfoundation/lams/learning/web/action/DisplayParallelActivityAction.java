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

import java.util.*;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;

import org.lamsfoundation.lams.learningdesign.*;
import org.lamsfoundation.lams.lesson.*;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.ParallelActivityMappingStrategy;

/** 
 * Action class to display a ParallelActivity.
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/DisplayParallelActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 * @struts:action-forward name="displayParallel" path=".parallelActivity"
 * 
 */
public class DisplayParallelActivityAction extends ActivityAction {
	

	/**
	 * Gets a parallel activity from the request (attribute) and forwards to
	 * the display JSP.
	 */
	public ActionForward execute(ActionMapping mapping,
	                             ActionForm actionForm,
	                             HttpServletRequest request,
	                             HttpServletResponse response) 
	{
		ActivityForm form = (ActivityForm)actionForm;
		ActivityMapping actionMappings = getActivityMapping();
		actionMappings.setActivityMappingStrategy(new ParallelActivityMappingStrategy());
		
		LearnerProgress learnerProgress = getLearnerProgress(request, form);
		Activity activity = getActivity(request, form, learnerProgress);
		if (!(activity instanceof ParallelActivity)) {
		    log.error(className+": activity not ParallelActivity "+activity.getActivityId());
			return mapping.findForward(ActivityMapping.ERROR);
		}

		ParallelActivity parallelActivity = (ParallelActivity)activity;

		form.setActivityId(activity.getActivityId());

		List activityURLs = new ArrayList();
		Set subActivities = parallelActivity.getActivities();
		Iterator i = subActivities.iterator();
		while (i.hasNext()) {
			Activity subActivity = (Activity)i.next();
			ActivityURL activityURL = new ActivityURL(); 
			String url = actionMappings.getActivityURL(subActivity, learnerProgress);
			activityURL.setUrl(url);
			activityURLs.add(activityURL);
		}
		if (activityURLs.size() == 0) {
		    log.error(className+": No sub-activity URLs for activity "+activity.getActivityId());
			return mapping.findForward(ActivityMapping.ERROR);
		}
		form.setActivityURLs(activityURLs);
		
		return mapping.findForward("displayParallel");
	}

}