/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.qa.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.tool.qa.model.QaContent;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.web.form.QaPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pedagogicalPlanner")
public class QaPedagogicalPlannerController {

    @Autowired
    private IQaService qaService;

    @RequestMapping("")
    protected String unspecified(QaPedagogicalPlannerForm pedagogicalPlannerForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return initPedagogicalPlannerForm(pedagogicalPlannerForm, request);
    }

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(QaPedagogicalPlannerForm pedagogicalPlannerForm,
	    HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaContent qaContent = qaService.getQaContent(toolContentID);
	pedagogicalPlannerForm.fillForm(qaContent);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	pedagogicalPlannerForm.setContentFolderID(contentFolderId);
	return "authoring/pedagogicalPlannerForm";
    }

    @RequestMapping(value = "/saveOrUpdatePedagogicalPlannerForm", method = RequestMethod.POST)
    public String saveOrUpdatePedagogicalPlannerForm(QaPedagogicalPlannerForm pedagogicalPlannerForm,
	    HttpServletRequest request) throws IOException {

	MultiValueMap<String, String> errorMap = pedagogicalPlannerForm.validate();
	if (errorMap.isEmpty()) {

	    QaContent qaContent = qaService.getQaContent(pedagogicalPlannerForm.getToolContentID());

	    int questionIndex = 0;
	    String question = null;

	    do {
		question = pedagogicalPlannerForm.getQuestion(questionIndex);
		if (StringUtils.isEmpty(question)) {
		    pedagogicalPlannerForm.removeQuestion(questionIndex);
		} else {
		    if (questionIndex < qaContent.getQaQueContents().size()) {
			QaQueContent qaQuestion = qaService.getQuestionByContentAndDisplayOrder(questionIndex + 1,
				qaContent.getUid());
			qaQuestion.getQbQuestion().setName(question);
			qaService.saveOrUpdate(qaQuestion.getQbQuestion());

		    } else {
			QaQueContent qaQuestion = new QaQueContent();
			qaQuestion.setDisplayOrder(questionIndex + 1);
			qaQuestion.setToolContentId(qaContent.getQaContentId());

			QbQuestion qbQuestion = new QbQuestion();
			qbQuestion.setAnswerRequired(false);
			qbQuestion.setName(question);
			qaService.saveOrUpdate(qbQuestion);

			qaQuestion.setQbQuestion(qbQuestion);
			qaQuestion.setQaContent(qaContent);
			qaService.saveOrUpdate(qaQuestion);
		    }
		    questionIndex++;
		}
	    } while (question != null);
	    if (questionIndex < qaContent.getQaQueContents().size()) {
		qaService.removeQuestionsFromCache(qaContent);
		qaService.removeQaContentFromCache(qaContent);
		for (; questionIndex < qaContent.getQaQueContents().size(); questionIndex++) {
		    QaQueContent qaQuestion = qaService.getQuestionByContentAndDisplayOrder(questionIndex + 1,
			    qaContent.getUid());
		    qaService.removeQuestion(qaQuestion);
		}
	    }
	} else {
	    request.setAttribute("errorMap", errorMap);
	}
	return "authoring/pedagogicalPlannerForm";
    }

    @RequestMapping("/createPedagogicalPlannerQuestion")
    public String createPedagogicalPlannerQuestion(QaPedagogicalPlannerForm pedagogicalPlannerForm) {
	pedagogicalPlannerForm.setQuestion(pedagogicalPlannerForm.getQuestionCount().intValue(), "");
	return "authoring/pedagogicalPlannerForm";
    }

}