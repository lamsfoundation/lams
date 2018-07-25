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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupUser;
import org.lamsfoundation.lams.learningdesign.dao.IGroupUserDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.lesson.util.LessonComparator;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This Action takes care of operations on lesson conditional release based on preceding lesson completion.
 *
 * @author Marcin Cieslak
 *
 *
 *
 */
public class LessonConditionsAction extends DispatchAction {
    private static final Logger logger = Logger.getLogger(LessonConditionsAction.class);

    private static final String FORWARD_INDEX_LESSON_CONDITION = "indexLessonConditions";

    private static final String PARAM_PRECEDING_LESSONS = "precedingLessons";
    private static final String PARAM_PRECEDING_LESSON_ID = "precedingLessonId";
    private static final String PARAM_AVAILABLE_LESSONS = "availableLessons";
    private static final String PARAM_LESSON_START_DATE = "lessonStartDate";
    private static final String PARAM_LESSON_DAYS_TO_FINISH = "lessonDaysToFinish";
    private static final String PARAM_INDIVIDUAL_FINISH = "lessonIndividualFinish";

    private static ILessonService lessonService;
    private static IMonitoringService monitoringService;
    private static IGroupUserDAO groupUserDAO;
    private static ISecurityService securityService;

    /**
     * Prepares data for thickbox displayed on Index page.
     *
     * @throws IOException
     *
     * @throws InvalidParameterException
     */
    @SuppressWarnings("unchecked")
    public ActionForward getIndexLessonConditions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, false);
	if (!getSecurityService().isLessonMonitor(lessonId, getUser().getUserID(), "show lesson conditions", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	Lesson lesson = getLessonService().getLesson(lessonId);
	List<IndexLessonBean> precedingLessons = new ArrayList<IndexLessonBean>(lesson.getPrecedingLessons().size());
	for (Lesson precedingLesson : lesson.getPrecedingLessons()) {
	    IndexLessonBean precedingLessonBean = new IndexLessonBean(precedingLesson.getLessonId(),
		    precedingLesson.getLessonName());
	    precedingLessons.add(precedingLessonBean);
	}
	request.setAttribute(CentralConstants.PARAM_LESSON_ID, lesson.getLessonId());
	request.setAttribute(CentralConstants.PARAM_TITLE, lesson.getLessonName());
	request.setAttribute(LessonConditionsAction.PARAM_PRECEDING_LESSONS, precedingLessons);

	Set<Lesson> organisationLessons = new TreeSet<Lesson>(new LessonComparator());
	organisationLessons.addAll(lesson.getOrganisation().getLessons());
	List<IndexLessonBean> availableLessons = new ArrayList<IndexLessonBean>(organisationLessons.size());
	for (Lesson organisationLesson : organisationLessons) {
	    if (!lessonId.equals(organisationLesson.getLessonId())
		    && !lesson.getPrecedingLessons().contains(organisationLesson)
		    && !Lesson.REMOVED_STATE.equals(organisationLesson.getLessonStateId())
		    && !Lesson.FINISHED_STATE.equals(organisationLesson.getLessonStateId())) {
		IndexLessonBean availableLessonBean = new IndexLessonBean(organisationLesson.getLessonId(),
			organisationLesson.getLessonName());
		availableLessons.add(availableLessonBean);
	    }
	}
	request.setAttribute(LessonConditionsAction.PARAM_AVAILABLE_LESSONS, availableLessons);

	Date endDate = lesson.getScheduleEndDate();
	if (endDate == null) {
	    Integer daysToLessonFinish = lesson.getScheduledNumberDaysToLessonFinish();
	    if (daysToLessonFinish != null) {
		request.setAttribute(LessonConditionsAction.PARAM_LESSON_DAYS_TO_FINISH, daysToLessonFinish);
		request.setAttribute(LessonConditionsAction.PARAM_INDIVIDUAL_FINISH, true);
	    }
	} else {
	    Date startDate = (lesson.getStartDateTime() == null) ? lesson.getScheduleStartDate()
		    : lesson.getStartDateTime();
	    long daysToLessonFinish = Duration.between(startDate.toInstant(), endDate.toInstant()).toDays();

	    request.setAttribute(LessonConditionsAction.PARAM_LESSON_START_DATE, startDate);
	    request.setAttribute(LessonConditionsAction.PARAM_LESSON_DAYS_TO_FINISH, daysToLessonFinish);
	    request.setAttribute(LessonConditionsAction.PARAM_INDIVIDUAL_FINISH, false);
	}

	request.setAttribute(CentralConstants.PARAM_EDIT, lesson.getUser().getUserId().equals(getUser().getUserID()));

	return mapping.findForward(LessonConditionsAction.FORWARD_INDEX_LESSON_CONDITION);
    }

    /**
     * Removes given lesson from dependecies and displays updated list in thickbox.
     *
     * @throws IOException
     *
     * @throws InvalidParameterException
     */
    public ActionForward removeLessonDependency(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, false);
	if (!getSecurityService().isLessonOwner(lessonId, getUser().getUserID(), "remove lesson dependency", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not the owner of the lesson");
	    return null;
	}

	Long removedPrecedingLessonId = WebUtil.readLongParam(request, LessonConditionsAction.PARAM_PRECEDING_LESSON_ID,
		false);

	Lesson lesson = getLessonService().getLesson(lessonId);
	Iterator<Lesson> precedingLessonIter = lesson.getPrecedingLessons().iterator();
	while (precedingLessonIter.hasNext()) {
	    if (precedingLessonIter.next().getLessonId().equals(removedPrecedingLessonId)) {
		precedingLessonIter.remove();
		break;
	    }
	}

	// after operation, display contents again
	return getIndexLessonConditions(mapping, form, request, response);
    }

    /**
     * Adds given lesson to dependecies and displays updated list in thickbox.
     *
     * @throws IOException
     *
     * @throws InvalidParameterException
     */
    public ActionForward addLessonDependency(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, false);
	if (!getSecurityService().isLessonOwner(lessonId, getUser().getUserID(), "add lesson dependency", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not the owner of the lesson");
	    return null;
	}

	Long addedPrecedingLessonId = WebUtil.readLongParam(request, LessonConditionsAction.PARAM_PRECEDING_LESSON_ID,
		false);
	Lesson lesson = getLessonService().getLesson(lessonId);
	Lesson addedPrecedingLesson = getLessonService().getLesson(addedPrecedingLessonId);
	if (addedPrecedingLesson == null) {
	    throw new IllegalArgumentException("Preceding lesson with ID: " + lessonId + " does not exist.");
	}

	lesson.getPrecedingLessons().add(addedPrecedingLesson);

	// after operation, display contents again
	return getIndexLessonConditions(mapping, form, request, response);
    }

    /**
     * Sets lesson finish date, either for individuals or lesson as a whole. If days<=0, schedule is removed.
     *
     * @throws IOException
     */
    public ActionForward setDaysToLessonFinish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, false);
	if (!getSecurityService().isLessonOwner(lessonId, getUser().getUserID(), "set days to lesson finish", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not the owner of the lesson");
	    return null;
	}

	int daysToLessonFinish = WebUtil.readIntParam(request, LessonConditionsAction.PARAM_LESSON_DAYS_TO_FINISH,
		false);
	boolean individualFinish = WebUtil.readBooleanParam(request, LessonConditionsAction.PARAM_INDIVIDUAL_FINISH,
		false);
	ActionMessages errors = new ActionMessages();

	Lesson lesson = getLessonService().getLesson(lessonId);
	HttpSession session = SessionManager.getSession();
	UserDTO currentUser = (UserDTO) session.getAttribute(AttributeNames.USER);
	try {
	    // (re)schedule the lesson or remove it when set for individual finish of daysToLessonFinish=0
	    getMonitoringService().finishLessonOnSchedule(lessonId, individualFinish ? 0 : daysToLessonFinish,
		    currentUser.getUserID());
	    // if finish date is individual, the field below is filled
	    lesson.setScheduledNumberDaysToLessonFinish(
		    individualFinish && (daysToLessonFinish > 0) ? daysToLessonFinish : null);

	    // iterate through each GroupLearner and set/remove individual lesson finish date, if needed
	    Group learnerGroup = lesson.getLessonClass().getLearnersGroup();
	    if (learnerGroup != null) {
		for (User learner : learnerGroup.getUsers()) {
		    GroupUser groupUser = getGroupUserDAO().getGroupUser(lesson, learner.getUserId());
		    if (groupUser != null) {

			if (individualFinish && (daysToLessonFinish > 0)) {
			    // set individual finish date based on start date
			    LearnerProgress learnerProgress = getLessonService()
				    .getUserProgressForLesson(learner.getUserId(), lessonId);
			    if ((learnerProgress == null) || (learnerProgress.getStartDate() == null)) {
				if (groupUser.getScheduledLessonEndDate() != null) {
				    LessonConditionsAction.logger.warn("Improper DB value: User with ID "
					    + learner.getUserId()
					    + " has scheduledLessonEndDate set but has not started the lesson yet.");
				}
			    } else {
				// set new finish date according to moment when user joined the lesson
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(learnerProgress.getStartDate());
				calendar.add(Calendar.DATE, daysToLessonFinish);
				Date endDate = calendar.getTime();
				groupUser.setScheduledLessonEndDate(endDate);

				if (LessonConditionsAction.logger.isDebugEnabled()) {
				    LessonConditionsAction.logger
					    .debug("Reset time limit for user: " + learner.getLogin() + " in lesson: "
						    + lesson.getLessonId() + " to " + endDate);
				}
			    }
			} else if (groupUser.getScheduledLessonEndDate() != null) {
			    // remove individual finish date
			    groupUser.setScheduledLessonEndDate(null);

			    if (LessonConditionsAction.logger.isDebugEnabled()) {
				LessonConditionsAction.logger.debug("Remove time limit for user: " + learner.getLogin()
					+ " in lesson: " + lesson.getLessonId());
			    }
			}
		    }
		}
	    }
	} catch (Exception e) {
	    LessonConditionsAction.logger.error(e);
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage("error.conditions.box.finish.date", new Object[] { e.getMessage() }));
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	}

	// after operation, display contents again
	return getIndexLessonConditions(mapping, form, request, response);
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private ILessonService getLessonService() {
	if (LessonConditionsAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    LessonConditionsAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return LessonConditionsAction.lessonService;
    }

    private IMonitoringService getMonitoringService() {
	if (LessonConditionsAction.monitoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    LessonConditionsAction.monitoringService = (IMonitoringService) ctx.getBean("monitoringService");
	}
	return LessonConditionsAction.monitoringService;
    }

    private ISecurityService getSecurityService() {
	if (LessonConditionsAction.securityService == null) {
	    WebApplicationContext webContext = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    LessonConditionsAction.securityService = (ISecurityService) webContext.getBean("securityService");
	}

	return LessonConditionsAction.securityService;
    }

    private IGroupUserDAO getGroupUserDAO() {
	if (LessonConditionsAction.groupUserDAO == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    LessonConditionsAction.groupUserDAO = (IGroupUserDAO) ctx.getBean("groupUserDAO");
	}
	return LessonConditionsAction.groupUserDAO;
    }
}