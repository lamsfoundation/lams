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
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.web.form.OptionsActivityForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.dto.ActivityPositionDTO;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Action class to display an OptionsActivity.
 *
 * @author daveg
 */
@Controller
public class DisplayOptionsActivityController {
    private static Logger log = Logger.getLogger(DisplayOptionsActivityController.class);

    public static final String RELEASED_LESSONS_REQUEST_ATTRIBUTE = "releasedLessons";

    @Autowired
    private ILearnerFullService learnerService;
    @Autowired
    private ActivityMapping activityMapping;

    /**
     * Gets an options activity from the request (attribute) and forwards to the display JSP.
     */
    @RequestMapping("/DisplayOptionsActivity")
    public String execute(@ModelAttribute OptionsActivityForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);

	long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Activity activity = learnerService.getActivity(activityId);
	if (!(activity instanceof OptionsActivity)) {
	    log.error("activity not OptionsActivity " + activity.getActivityId());
	    return "error";
	}

	OptionsActivity optionsActivity = (OptionsActivity) activity;

	form.setActivityID(activity.getActivityId());

	List<ActivityURL> activityURLs = new ArrayList<>();
	Set<Activity> optionsChildActivities = optionsActivity.getActivities();
	Iterator<Activity> i = optionsChildActivities.iterator();
	int completedActivitiesCount = 0;
	while (i.hasNext()) {
	    Activity optionsChildActivity = i.next();
	    ActivityURL activityURL = activityMapping.getActivityURL(learnerProgress, optionsChildActivity, false, false);

	    if (activityURL.isComplete()) {
		completedActivitiesCount++;

		//create list of activityURLs of all children activities
		if (optionsChildActivity.isSequenceActivity()) {
		    activityURL.setUrl(null);
		    
		    // activity is loaded as proxy due to lazy loading and in order to prevent quering DB we just re-initialize
		    // it here again
		    SequenceActivity optionsChildActivityInit;
		    Hibernate.initialize(activity);
		    if (optionsChildActivity instanceof HibernateProxy) {
			optionsChildActivityInit = (SequenceActivity) ((HibernateProxy) optionsChildActivity).getHibernateLazyInitializer()
				.getImplementation();
		    } else {
			optionsChildActivityInit = (SequenceActivity) optionsChildActivity;
		    }
		    
		    List<ActivityURL> childActivities = new ArrayList<>();
		    for (Activity sequenceChildActivity : optionsChildActivityInit.getActivities()) {
			ActivityURL sequenceActivityURL = activityMapping.getActivityURL(
				learnerProgress, sequenceChildActivity, false, false);
			childActivities.add(sequenceActivityURL);
		    }
		    activityURL.setChildActivities(childActivities);
		}
	    }
	    activityURLs.add(activityURL);
	}
	form.setActivityURLs(activityURLs);

	if (completedActivitiesCount >= optionsActivity.getMinNumberOfOptionsNotNull().intValue()) {
	    form.setMinimumLimitReached(true);
	}
	
	if (completedActivitiesCount > 0) {
	    form.setHasCompletedActivities(true);
	}

	if (completedActivitiesCount >= optionsActivity.getMaxNumberOfOptionsNotNull().intValue()) {
	    form.setMaxActivitiesReached(true);
	}

	int minNumberOfOptions = optionsActivity.getMinNumberOfOptions() == null ? 0 : optionsActivity.getMinNumberOfOptions();
	form.setMinimum(minNumberOfOptions);
	int maxNumberOfOptions = optionsActivity.getMaxNumberOfOptions() == null ? 0 : optionsActivity.getMaxNumberOfOptions();
	form.setMaximum(maxNumberOfOptions);
	form.setDescription(optionsActivity.getDescription());
	form.setTitle(optionsActivity.getTitle());
	form.setLessonID(learnerProgress.getLesson().getLessonId());
	form.setProgressID(learnerProgress.getLearnerProgressId());

	//find activity position within Learning Design and stores it as request attribute.
	ActivityPositionDTO positionDTO = learnerService.getActivityPosition(form.getActivityID());
	if (positionDTO != null) {
	    request.setAttribute(AttributeNames.ATTR_ACTIVITY_POSITION, positionDTO);
	}

	// lessonId needed for the progress bar
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, learnerProgress.getLesson().getLessonId());

	return "optionsActivity";
    }
}