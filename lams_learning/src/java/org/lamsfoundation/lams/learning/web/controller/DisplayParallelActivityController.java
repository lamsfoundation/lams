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

package org.lamsfoundation.lams.learning.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learning.web.util.ParallelActivityMappingStrategy;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Action class to display a ParallelActivity.
 *
 * @author daveg
 */
@Controller
public class DisplayParallelActivityController {
    private static Logger log = Logger.getLogger(DisplayParallelActivityController.class);

    @Autowired
    private ILearnerFullService learnerService;
    @Autowired
    private ActivityMapping activityMapping;

    /**
     * Gets a parallel activity from the request (attribute) and forwards to the display JSP.
     */
    @RequestMapping("/DisplayParallelActivity")
    public String execute(@ModelAttribute ActivityForm form, HttpServletRequest request, HttpServletResponse response) {
	activityMapping.setActivityMappingStrategy(new ParallelActivityMappingStrategy());

	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);
	if (!(activity instanceof ParallelActivity)) {
	    log.error("activity not ParallelActivity " + activity.getActivityId());
	    return "error";
	}

	ParallelActivity parallelActivity = (ParallelActivity) activity;

	form.setActivityID(activity.getActivityId());

	List<ActivityURL> activityURLs = new ArrayList<>();
	for (Iterator<Activity> i = parallelActivity.getActivities().iterator(); i.hasNext();) {
	    Activity subActivity = i.next();
	    ActivityURL activityURL = new ActivityURL();
	    String url = activityMapping.getActivityURL(subActivity);
	    activityURL.setUrl(url);
	    activityURLs.add(activityURL);
	}
	if (activityURLs.size() == 0) {
	    log.error("No sub-activity URLs for activity " + activity.getActivityId());
	    return "error";
	}
	form.setActivityURLs(activityURLs);

	return "parallelActivity";
    }
}