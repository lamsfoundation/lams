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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.learning.web.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learning.web.util.ParallelActivityMappingStrategy;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.web.action.LamsAction;

/**
 * Action class to display a ParallelActivity.
 *
 * @author daveg
 */
public class DisplayParallelActivityAction extends ActivityAction {

    /**
     * Gets a parallel activity from the request (attribute) and forwards to the display JSP.
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ActivityForm form = (ActivityForm) actionForm;
	ILearnerFullService learnerService = getLearnerService();

	ActivityMapping actionMappings = LearnerServiceProxy.getActivityMapping(this.getServlet().getServletContext());

	actionMappings.setActivityMappingStrategy(new ParallelActivityMappingStrategy());

	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);
	if (!(activity instanceof ParallelActivity)) {
	    LamsAction.log.error(LamsAction.className + ": activity not ParallelActivity " + activity.getActivityId());
	    return mapping.findForward(ActivityMapping.ERROR);
	}

	ParallelActivity parallelActivity = (ParallelActivity) activity;

	form.setActivityID(activity.getActivityId());

	List activityURLs = new ArrayList();

	for (Iterator i = parallelActivity.getActivities().iterator(); i.hasNext();) {
	    Activity subActivity = (Activity) i.next();
	    ActivityURL activityURL = new ActivityURL();
	    String url = actionMappings.getActivityURL(subActivity);
	    activityURL.setUrl(url);
	    activityURLs.add(activityURL);
	}
	if (activityURLs.size() == 0) {
	    LamsAction.log
		    .error(LamsAction.className + ": No sub-activity URLs for activity " + activity.getActivityId());
	    return mapping.findForward(ActivityMapping.ERROR);
	}
	form.setActivityURLs(activityURLs);

	return mapping.findForward("displayParallel");
    }
}