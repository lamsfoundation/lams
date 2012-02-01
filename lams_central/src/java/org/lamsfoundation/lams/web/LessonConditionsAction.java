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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.LessonService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This Action takes care of operations on lesson conditional release based on preceding lesson completion.
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

    private static LessonService lessonService;

    /**
     * Prepares data for thickbox displayed on Index page.
     */
    public ActionForward getIndexLessonConditions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, false);
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

	return mapping.findForward(LessonConditionsAction.FORWARD_INDEX_LESSON_CONDITION);
    }

    /**
     * Removes given lesson from dependecies and displays updated list in thickbox.
     */
    public ActionForward removeLessonDependency(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long lessonId = WebUtil.readLongParam(request, CentralConstants.PARAM_LESSON_ID, false);
	Long removedPrecedingLessonId = WebUtil.readLongParam(request, PARAM_PRECEDING_LESSON_ID, false);

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

    private LessonService getLessonService() {
	if (LessonConditionsAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    LessonConditionsAction.lessonService = (LessonService) ctx.getBean("lessonService");
	}
	return LessonConditionsAction.lessonService;
    }
}
