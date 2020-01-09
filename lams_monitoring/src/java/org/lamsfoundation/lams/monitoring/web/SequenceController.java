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

package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The action servlet that provides the support for the Sequence activities. At present, this is only a basic view
 * screen that lists the user's in the sequence.
 *
 * @author Fiona Malikoff
 */
@Controller
public class SequenceController {

    @Autowired
    private IMonitoringFullService monitoringService;

    public static final String VIEW_SEQUENCE = "viewSequence";
    public static final String PARAM_LEARNERS = "learners";
    public static final String PARAM_LOCAL_FILES = "localFiles";

    /**
     * Display the view screen.
     */
    @RequestMapping("/sequence")
    public String viewSequence(HttpServletRequest request, HttpServletResponse response) {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

	SequenceActivity activity = (SequenceActivity) monitoringService.getActivityById(activityId,
		SequenceActivity.class);
	return viewSequence(activity, lessonId, false, request, monitoringService);
    }

    protected String viewSequence(SequenceActivity activity, Long lessonId, boolean useLocalFiles,
	    HttpServletRequest request, IMonitoringService monitoringService) {
	// in general the progress engine expects the activity and lesson id to be in the request,
	// so follow that standard.
	request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activity.getActivityId());
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
	request.setAttribute(AttributeNames.PARAM_TITLE, activity.getTitle());
	request.setAttribute(SequenceController.PARAM_LOCAL_FILES, useLocalFiles);

	// only show the group names if this is a group based branching activity - the names
	// are meaningless for chosen and tool based branching
	List<User> learners = monitoringService.getLearnersAttemptedOrCompletedActivity(activity);
	request.setAttribute(SequenceController.PARAM_LEARNERS, learners);
	return "viewSequence";
    }

}
