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
import org.lamsfoundation.lams.learning.web.util.ActionMappings;

/** 
 * Action class to forward the user to a Tool using an intermediate loading page.
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/DisplayLoadToolActivity" name="activityForm"
 *                validate="false" scope="request"
 * 
 * @struts:action-forward name="displayTool" path=".loadToolActivity"
 * 
 */
public class DisplayLoadToolActivity extends ActivityAction {

	/**
	 * Gets an activity from the request (attribute) and forwards onto a
	 * loading page.
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm form = (ActivityForm)actionForm;
		ActionMappings actionMappings = getActionMappings();
		
		LearnerProgress learnerProgress = getLearnerProgress(request, form);
		Activity activity = getActivity(request, form, learnerProgress);
		if (!(activity instanceof ToolActivity)) {
		    log.error(className+": activity not ToolActivity");
			return mapping.findForward(ActionMappings.ERROR);
		}
		
		ToolActivity toolActivity = (ToolActivity)activity;

		form.setActivityId(activity.getActivityId());
		
		List activityURLs = new ArrayList();
		String url = actionMappings.getToolURL(toolActivity, learnerProgress);
		ActivityURL activityURL = new ActivityURL();
		activityURL.setUrl(url);
		activityURLs.add(activityURL);
		form.setActivityURLs(activityURLs);
		
		String forward = "displayTool";
		return mapping.findForward(forward);
	}

}