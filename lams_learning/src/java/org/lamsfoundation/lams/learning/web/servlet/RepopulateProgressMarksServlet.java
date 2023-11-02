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

package org.lamsfoundation.lams.learning.web.servlet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ListingLearningDesignProcessor;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

@SuppressWarnings("serial")
public class RepopulateProgressMarksServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(RepopulateProgressMarksServlet.class);

    @Autowired
    private IActivityDAO activityDAO;
    @Autowired
    private ILogEventService logEventService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ILearnerFullService learnerService;

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	String header = "";
	StringBuilder errorBuilder = new StringBuilder("");
	StringBuilder auditLogBuilder = new StringBuilder("");
	UserDTO userDTO = null;

	PrintWriter out = response.getWriter();
	Long lessonId = null;
	try {

	    lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    Integer restrictToLearnerId = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, true);
	    Boolean gradebookAll = WebUtil.readBooleanParam(request, "gradebookAll", false);
	    HttpSession ss = SessionManager.getSession();
	    userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);

	    Lesson lesson = lessonService.getLesson(lessonId);
	    if (lesson == null) {
		log.error("RepopulateProgressMarksServlet: lesson not found " + lessonId);
		out.println("ERROR: lesson not found " + lessonId);
		return;
	    }

	    header = new StringBuilder("Learner progress and gradebook marks to be repopulated for lesson ")
		    .append(lessonId).append(" ").append(lesson.getLessonName())
		    .append(".\n----------------------------------------------------------------------------------\n\n")
		    .toString();

	    ListingLearningDesignProcessor processor = new  ListingLearningDesignProcessor(lesson.getLearningDesign(),
		    activityDAO);
	    processor.parseLearningDesign();
	    ArrayList<Activity> activityList = processor.getActivityList();

	    auditLogBuilder.append("Full log:\n\nUpdating progress for the following activities: ");
	    for (Activity activity : activityList) {
		auditLogBuilder.append(activity.getActivityId()).append(" ").append(activity.getTitle()).append("; ");
	    }
	    auditLogBuilder.append("\n\n");

	    if (restrictToLearnerId == null) {
		Set<LearnerProgress> progresses = lesson.getLearnerProgresses();
		for (LearnerProgress learnerProgress : progresses) {
		    processLearner(errorBuilder, auditLogBuilder, lesson, activityList, learnerProgress, gradebookAll);
		}
	    } else {
		LearnerProgress learnerProgress = learnerService.getProgress(restrictToLearnerId, lessonId);
		if (learnerProgress != null) {
		    processLearner(errorBuilder, auditLogBuilder, lesson, activityList, learnerProgress, gradebookAll);
		} else {
		    log.error("RepopulateProgressMarksServlet: learner's progress not found " + restrictToLearnerId);
		    out.println("ERROR: progress for learner " + restrictToLearnerId + " not found");
		    return;
		}
	    }

	} catch (Throwable e) {

	    log.error("Error: " + e.getMessage() + e.getCause(), e);
	    errorBuilder.append("Error occured ").append(e.getMessage());
	}

	String errors = errorBuilder.append("\n").toString();
	String msg;
	if (errors.length() > 1) {
	    msg = new StringBuilder(header).append("Errors occured. Some data may be been updated.\n").append(errors)
		    .append(auditLogBuilder.toString()).toString();
	} else {
	    msg = new StringBuilder(header).append("Successful run, no errors\n").append(auditLogBuilder.toString())
		    .toString();
	}
	logEventService.logEvent(LogEvent.TYPE_MARK_UPDATED, userDTO != null ? userDTO.getUserID() : null, null,
		lessonId, null, msg);
	out.println(msg);
	return;
    }

    private void processLearner(StringBuilder errorBuilder, StringBuilder auditLogBuilder, Lesson lesson,
	    ArrayList<Activity> activityList, LearnerProgress learnerProgress, boolean updateGradebookForAll) {
	try {
	    String messages[] = learnerService.recalcProgressForLearner(lesson, activityList, learnerProgress,
		    updateGradebookForAll);
	    auditLogBuilder.append(messages[0]);
	    errorBuilder.append(messages[1]);
	} catch (Throwable e) {
	    log.error("Error thrown while processing " + learnerProgress.getUser().getLogin(), e);
	    String msg = new StringBuilder("Error occured while processing user ")
		    .append(learnerProgress.getUser().getLogin()).append(" ")
		    .append(learnerProgress.getUser().getFullName())
		    .append(". Proceeding entries in log for this user may or may not have worked. Error was ")
		    .append(e.getMessage()).append("\n").toString();
	    auditLogBuilder.append(msg);
	    errorBuilder.append(msg);
	}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }
}