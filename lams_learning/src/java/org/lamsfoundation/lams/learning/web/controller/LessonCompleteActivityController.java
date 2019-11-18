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

import java.io.UnsupportedEncodingException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Action class run when the learner finishes a lesson.
 */
@Controller
public class LessonCompleteActivityController {

    public static final String RELEASED_LESSONS_REQUEST_ATTRIBUTE = "releasedLessons";

    @Autowired
    private ILearnerFullService learnerService;

    @Autowired
    private IIntegrationService integrationService;

    @Autowired
    private ILessonService lessonService;

    /**
     * Gets an activity from the request (attribute) and forwards onto a display action using the ActionMappings class.
     * If no activity is in request then use the current activity in learnerProgress.
     */

    @RequestMapping("/LessonComplete")
    public String execute(HttpServletRequest request) throws UnsupportedEncodingException {
	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Lesson lesson = learnerProgress.getLesson();
	Set<Lesson> releasedLessons = lessonService.getReleasedSucceedingLessons(lesson.getLessonId(),
		learnerProgress.getUser().getUserId());
	if (!releasedLessons.isEmpty()) {
	    StringBuilder releasedLessonNames = new StringBuilder();
	    for (Lesson releasedLesson : releasedLessons) {
		releasedLessonNames.append(releasedLesson.getLessonName()).append(", ");
	    }
	    releasedLessonNames.delete(releasedLessonNames.length() - 2, releasedLessonNames.length());
	    request.setAttribute(RELEASED_LESSONS_REQUEST_ATTRIBUTE, releasedLessonNames.toString());
	}

	//let non-LTI integrations server know to come and pick up updated marks (it will happen at lessoncomplete.jsp page)
	String lessonFinishCallbackUrl = integrationService.getLessonFinishCallbackUrl(learnerProgress.getUser(),
		lesson);
	if (lessonFinishCallbackUrl != null) {
	    request.setAttribute("lessonFinishUrl", lessonFinishCallbackUrl);
	}
	request.setAttribute("lessonID", lesson.getLessonId());
	request.setAttribute("gradebookOnComplete", lesson.getGradebookOnComplete());

	return "lessonComplete";
    }
}