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

/* $Id$ */
package org.lamsfoundation.lams.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.LessonService;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
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
 * @struts.action path="/lessonConditions" parameter="method" validate="false"
 * @struts.action-forward name="indexLessonConditions" path="/indexLessonConditions.jsp"
 */
public class LessonConditionsAction extends DispatchAction {
    private static final Logger logger = Logger.getLogger(LessonConditionsAction.class);

    private static final String FORWARD_INDEX_LESSON_CONDITION = "indexLessonConditions";

    private static final String PARAM_PRECEDING_LESSONS = "precedingLessons";
    private static final String PARAM_PRECEDING_LESSON_ID = "precedingLessonId";
    private static final String PARAM_AVAILABLE_LESSONS = "availableLessons";

    private static LessonService lessonService;

    /**
     * Prepares data for thickbox displayed on Index page.
     * 
     * @throws InvalidParameterException
     */
    @SuppressWarnings("unchecked")
    public ActionForward getIndexLessonConditions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidParameterException {

	Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, false);
	Lesson lesson = getLessonService().getLesson(lessonId);
	if (lesson == null) {
	    throw new InvalidParameterException("Lesson with ID: " + lessonId + " does not exist.");
	}

	List<IndexLessonBean> precedingLessons = new ArrayList<IndexLessonBean>(lesson.getPrecedingLessons().size());
	for (Lesson precedingLesson : lesson.getPrecedingLessons()) {
	    IndexLessonBean precedingLessonBean = new IndexLessonBean(precedingLesson.getLessonId(),
		    precedingLesson.getLessonName());
	    precedingLessons.add(precedingLessonBean);
	}
	request.setAttribute(CentralConstants.PARAM_LESSON_ID, lesson.getLessonId());
	request.setAttribute(CentralConstants.PARAM_TITLE, lesson.getLessonName());
	request.setAttribute(LessonConditionsAction.PARAM_PRECEDING_LESSONS, precedingLessons);

	Set<Lesson> organisationLessons = lesson.getOrganisation().getLessons();
	List<IndexLessonBean> availableLessons = new ArrayList<IndexLessonBean>(organisationLessons.size());
	for (Lesson organisationLesson : organisationLessons) {
	    if (!lessonId.equals(organisationLesson.getLessonId())
		    && !lesson.getPrecedingLessons().contains(organisationLesson)) {
		IndexLessonBean availableLessonBean = new IndexLessonBean(organisationLesson.getLessonId(),
			organisationLesson.getLessonName());
		availableLessons.add(availableLessonBean);
	    }
	}
	request.setAttribute(LessonConditionsAction.PARAM_AVAILABLE_LESSONS, availableLessons);

	request.setAttribute(CentralConstants.PARAM_EDIT, canEdit(request, lesson));

	return mapping.findForward(LessonConditionsAction.FORWARD_INDEX_LESSON_CONDITION);
    }

    /**
     * Removes given lesson from dependecies and displays updated list in thickbox.
     * 
     * @throws InvalidParameterException
     */
    public ActionForward removeLessonDependency(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidParameterException {
	Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, false);
	Long removedPrecedingLessonId = WebUtil.readLongParam(request,
		LessonConditionsAction.PARAM_PRECEDING_LESSON_ID, false);

	Lesson lesson = getLessonService().getLesson(lessonId);
	if (lesson == null) {
	    throw new InvalidParameterException("Lesson with ID: " + lessonId + " does not exist.");
	}
	if (!canEdit(request, lesson)) {
	    throw new SecurityException("Current user can not edit lesson conditions");
	}

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
     * @throws InvalidParameterException
     */
    public ActionForward addLessonDependency(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InvalidParameterException {
	Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, false);
	Long addedPrecedingLessonId = WebUtil.readLongParam(request, LessonConditionsAction.PARAM_PRECEDING_LESSON_ID,
		false);

	Lesson lesson = getLessonService().getLesson(lessonId);
	if (lesson == null) {
	    throw new InvalidParameterException("Lesson with ID: " + lessonId + " does not exist.");
	}
	if (!canEdit(request, lesson)) {
	    throw new SecurityException("Current user can not edit lesson conditions");
	}
	Lesson addedPrecedingLesson = getLessonService().getLesson(addedPrecedingLessonId);
	if (addedPrecedingLesson == null) {
	    throw new InvalidParameterException("Preceding lesson with ID: " + lessonId + " does not exist.");
	}

	lesson.getPrecedingLessons().add(addedPrecedingLesson);

	// after operation, display contents again
	return getIndexLessonConditions(mapping, form, request, response);
    }

    /**
     * Checks if user is allowed to edit lesson conditions.
     */
    private boolean canEdit(HttpServletRequest request, Lesson lesson) {
	HttpSession session = SessionManager.getSession();
	UserDTO currentUser = (UserDTO) session.getAttribute(AttributeNames.USER);
	if (currentUser == null) {
	    throw new DataMissingException("User DTO missing from session.");
	}
	return currentUser.getUserID().equals(lesson.getUser().getUserId());
    }

    private LessonService getLessonService() {
	if (LessonConditionsAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    LessonConditionsAction.lessonService = (LessonService) ctx.getBean("lessonService");
	}
	return LessonConditionsAction.lessonService;
    }
}
