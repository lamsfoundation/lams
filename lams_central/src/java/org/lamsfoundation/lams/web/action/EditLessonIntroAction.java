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

package org.lamsfoundation.lams.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Edit lesson intro page.
 */
public class EditLessonIntroAction extends DispatchAction {

    private static ILessonService lessonService;

    /**
     * Edit lesson intro page.
     */
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws IOException, ServletException {

	Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = getLessonService().getLesson(lessonId);
	req.setAttribute("lesson", lesson);
	req.setAttribute("displayDesignImage", lesson.isDisplayDesignImage());
	req.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, lesson.getLearningDesign().getContentFolderID());

	return mapping.findForward("editLessonIntro");
    }

    /**
     * Save lesson intro page.
     */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
	    throws IOException, ServletException {

	Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = getLessonService().getLesson(lessonId);
	String lessonName = WebUtil.readStrParam(req, "lessonName");
	String lessonDescription = WebUtil.readStrParam(req, "lessonDescription");
	boolean displayDesignImage = WebUtil.readBooleanParam(req, "displayDesignImage", false);

	//sore lesson in DB
	lesson.setLessonName(lessonName);
	lesson.setLessonDescription(lessonDescription);
	lesson.setDisplayDesignImage(displayDesignImage);
	getLessonService().saveLesson(lesson);

	return null;
    }

    private ILessonService getLessonService() {
	if (lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return lessonService;
    }
}