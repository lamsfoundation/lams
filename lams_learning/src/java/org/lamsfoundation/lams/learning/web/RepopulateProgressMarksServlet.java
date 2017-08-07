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

package org.lamsfoundation.lams.learning.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignProcessor;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SuppressWarnings("serial")
public class RepopulateProgressMarksServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(RepopulateProgressMarksServlet.class);

    private static IAuditService auditService;
    private static ILessonService lessonService;
    private static ICoreLearnerService learnerService;

    @Override
    public void init() throws ServletException {
	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	RepopulateProgressMarksServlet.auditService = (IAuditService) ctx.getBean("auditService");
	RepopulateProgressMarksServlet.lessonService = (ILessonService) ctx.getBean("lessonService");
	RepopulateProgressMarksServlet.learnerService = (ICoreLearnerService) ctx.getBean("learnerService");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	String header="";
	StringBuilder errorBuilder = new StringBuilder("");
	StringBuilder auditLogBuilder = new StringBuilder("");
	UserDTO userDTO = null;

	PrintWriter out = response.getWriter();
	try {

	    Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
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

	    header = new StringBuilder(
		    "Learner progress and gradebook marks to be repopulated for lesson ").append(lessonId).append(" ")
		    .append(lesson.getLessonName())
		    .append(".\n----------------------------------------------------------------------------------\n\n").toString();

	    ActivitiesToCheckProcessor processor = new ActivitiesToCheckProcessor(lesson.getLearningDesign(),
		    learnerService.getActivityDAO());
	    processor.parseLearningDesign();
	    ArrayList<Activity> activityList = processor.getActivityList();

	    auditLogBuilder.append("Full log:\n\nUpdating progress for the following activities: ");
	    for (Activity activity : activityList) {
		auditLogBuilder.append(activity.getActivityId()).append(" ").append(activity.getTitle()).append("; ");
	    }
	    auditLogBuilder.append("\n\n");

	    if (restrictToLearnerId == null) {
		Set<LearnerProgress> progresses = (Set<LearnerProgress>) lesson.getLearnerProgresses();
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
	    msg =  new StringBuilder(header).append("Errors occured. Some data may be been updated.\n").append(errors).append(auditLogBuilder.toString()).toString();
	} else {
	    msg =  new StringBuilder(header).append("Successful run, no errors\n").append(auditLogBuilder.toString()).toString();
	}
	auditService.log(userDTO, "RepopulateProgressMarksServlet",msg);
	out.println(msg);
	return;
    }

    private void processLearner(StringBuilder errorBuilder, StringBuilder auditLogBuilder, Lesson lesson,
	    ArrayList<Activity> activityList, LearnerProgress learnerProgress, boolean updateGradebookForAll) {
	try {
	    String messages[] = learnerService.recalcProgressForLearner(lesson, activityList, learnerProgress, updateGradebookForAll);
	    auditLogBuilder.append(messages[0]);
	    errorBuilder.append(messages[1]);
	} catch ( Throwable e ) {
	    log.error("Error thrown while processing "+learnerProgress.getUser().getLogin(), e);
	    String msg = new StringBuilder("Error occured while processing user ")
	    	.append(learnerProgress.getUser().getLogin()).append(" ").append(learnerProgress.getUser().getFullName()).append(". Proceeding entries in log for this user may or may not have worked. Error was ")
	    	.append(e.getMessage()).append("\n")
	    	.toString();
	    auditLogBuilder.append(msg);
	    errorBuilder.append(msg);
	}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	doGet(request, response);
    }

    class ActivitiesToCheckProcessor extends LearningDesignProcessor {

	ArrayList<Activity> activityList;

	public ActivitiesToCheckProcessor(LearningDesign design, IActivityDAO activityDAO) {
	    super(design, activityDAO);
	    activityList = new ArrayList<Activity>();
	}

	@Override
	public boolean startComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
	    return true;
	}

	@Override
	public void endComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
	    activityList.add(activity);
	}

	@Override
	public void startSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
	}

	@Override
	public void endSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
	    activityList.add(activity);
	}

	public ArrayList<Activity> getActivityList() {
	    return activityList;
	}
    }
}