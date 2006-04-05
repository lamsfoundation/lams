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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.form.OptionsActivityForm;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;

/**
 * Action class to display an OptionsActivity.
 * 
 * @author daveg
 *
 * XDoclet definition:
 * 
 * @struts:action path="/DisplayOptionsActivity" name="optionsActivityForm"
 *                input="/Activity.do" validate="false" scope="request"
 * 
 * @struts:action-forward name="displayOptions" path=".optionsActivity"
 * 
 */
public class DisplayOptionsActivityAction extends ActivityAction {
	

	/**
	 * Gets an options activity from the request (attribute) and forwards to
	 * the display JSP.
	 */
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		OptionsActivityForm form = (OptionsActivityForm)actionForm;
		ActivityMapping actionMappings = getActivityMapping();
		
		LearnerProgress learnerProgress = getLearnerProgress(request);
		Activity activity = LearningWebUtil.getActivityFromRequest(request, getLearnerService());
		if (!(activity instanceof OptionsActivity)) {
		    log.error(className+": activity not OptionsActivity "+activity.getActivityId());
			return mapping.findForward(ActivityMapping.ERROR);
		}

		OptionsActivity optionsActivity = (OptionsActivity)activity;

		form.setActivityId(activity.getActivityId());

		List activityURLs = new ArrayList();
		Set subActivities = optionsActivity.getActivities();
		Iterator i = subActivities.iterator();
		int completedCount = 0;
		while (i.hasNext()) {
			Activity subActivity = (Activity)i.next();
			ActivityURL activityURL = new ActivityURL();
			String url = actionMappings.getActivityURL(subActivity, learnerProgress);
			activityURL.setUrl(url);
			activityURL.setActivityId(subActivity.getActivityId());
			activityURL.setTitle(subActivity.getTitle());
			activityURL.setDescription(subActivity.getDescription());
			if (learnerProgress.getProgressState(subActivity) == LearnerProgress.ACTIVITY_COMPLETED) {
			    activityURL.setComplete(true);
				completedCount++;
			}
			activityURLs.add(activityURL);
		}
		form.setActivityURLs(activityURLs);
		if (completedCount >= optionsActivity.getMinNumberOfOptions().intValue()) {
			form.setFinished(true);
		}
		form.setMinimum(optionsActivity.getMinNumberOfOptions().intValue());
		form.setMaximum(optionsActivity.getMaxNumberOfOptions().intValue());
		
		this.saveToken(request);
		
		String forward = "displayOptions";
		return mapping.findForward(forward);
	}
	
}
