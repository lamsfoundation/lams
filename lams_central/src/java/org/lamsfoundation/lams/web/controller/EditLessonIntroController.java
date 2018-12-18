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

package org.lamsfoundation.lams.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Edit lesson intro page.
 */
@Controller
@RequestMapping("/editLessonIntro")
public class EditLessonIntroController {

    @Autowired
    private ILessonService lessonService;

    /**
     * Edit lesson intro page.
     */
    @RequestMapping("/edit")
    public String edit(HttpServletRequest req) throws IOException, ServletException {

	Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonId);
	req.setAttribute("lesson", lesson);
	req.setAttribute("displayDesignImage", lesson.isDisplayDesignImage());
	req.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, lesson.getLearningDesign().getContentFolderID());

	return "editLessonIntro";
    }

    /**
     * Save lesson intro page.
     */
    @RequestMapping(path = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(HttpServletRequest req) throws IOException, ServletException {

	Long lessonId = WebUtil.readLongParam(req, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonId);
	String lessonDescription = WebUtil.readStrParam(req, "lessonDescription");
	boolean displayDesignImage = WebUtil.readBooleanParam(req, "displayDesignImage", false);

	// store lesson in DB
	lesson.setLessonDescription(lessonDescription);
	lesson.setDisplayDesignImage(displayDesignImage);
	lessonService.saveLesson(lesson);

	return null;
    }
}