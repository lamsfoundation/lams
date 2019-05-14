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

package org.lamsfoundation.lams.tool.mc.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.web.form.McPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedagogicalPlanner")
public class McPedagogicalPlannerController {

    @Autowired
    private IMcService mcService;

    @Autowired
    @Qualifier("lamcMessageService")
    private static MessageService messageService;

    protected String unspecified(@ModelAttribute McPedagogicalPlannerForm plannerForm, HttpServletRequest request) {
	return initPedagogicalPlannerForm(plannerForm, request);
    }

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(@ModelAttribute McPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) {

	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(toolContentID);
	plannerForm.fillForm(mcContent, mcService);
	return "authoring/pedagogicalPlannerForm";
    }

    @RequestMapping("/saveOrUpdatePedagogicalPlannerForm")
    public String saveOrUpdatePedagogicalPlannerForm(@ModelAttribute McPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) throws IOException {

	MultiValueMap<String, String> errorMap = plannerForm.validate(request);

	if (errorMap.isEmpty()) {
	    McContent mcContent = mcService.getMcContent(plannerForm.getToolContentID());
	    int questionIndex = 1;
	    String question = null;

	    do {
		question = plannerForm.getDescription(questionIndex - 1);
		List<McOptionDTO> candidateAnswerDTOList = plannerForm.extractCandidateAnswers(request, questionIndex);
		boolean removeQuestion = true;
		if (!StringUtils.isEmpty(question)) {
		    if (candidateAnswerDTOList != null) {
			for (McOptionDTO answer : candidateAnswerDTOList) {
			    if (answer != null && !StringUtils.isEmpty(answer.getCandidateAnswer())) {
				removeQuestion = false;
				break;
			    }
			}
		    }
		}
		if (removeQuestion) {
		    plannerForm.removeQuestion(questionIndex - 1);
		} else {
		    if (questionIndex <= mcContent.getMcQueContents().size()) {
			McQueContent mcQueContent = mcService.getQuestionByDisplayOrder(questionIndex,
				mcContent.getUid());
			mcQueContent.setDescription(question);
			int candidateAnswerDTOIndex = 0;
			List<QbOption> candidateAnswers = mcQueContent.getQbQuestion().getQbOptions();
			Iterator<QbOption> candidateAnswerIter = candidateAnswers.iterator();
			while (candidateAnswerIter.hasNext()) {
			    QbOption candidateAnswer = candidateAnswerIter.next();
			    if (candidateAnswerDTOIndex >= candidateAnswerDTOList.size()) {
				candidateAnswerIter.remove();
			    } else {
				McOptionDTO answerDTO = candidateAnswerDTOList.get(candidateAnswerDTOIndex);
				candidateAnswer.setCorrect(McAppConstants.CORRECT.equals(answerDTO.getCorrect()));
				candidateAnswer.setName(answerDTO.getCandidateAnswer());
				mcService.updateQbOption(candidateAnswer);
			    }
			    candidateAnswerDTOIndex++;
			}
			mcService.saveOrUpdateMcQueContent(mcQueContent);
		    } else {
			McQueContent mcQueContent = new McQueContent();
			mcQueContent.setQbQuestion(new QbQuestion());
			// TODO Set questionDescription ID and version
			mcQueContent.setDisplayOrder(questionIndex);
			mcQueContent.setMcContent(mcContent);
			mcQueContent.setDescription(question);
			mcQueContent.setMark(McAppConstants.QUESTION_DEFAULT_MARK);
			List<QbOption> candidateAnswers = mcQueContent.getQbQuestion().getQbOptions();
			for (int candidateAnswerDTOIndex = 0; candidateAnswerDTOIndex < candidateAnswerDTOList
				.size(); candidateAnswerDTOIndex++) {
			    McOptionDTO answerDTO = candidateAnswerDTOList.get(candidateAnswerDTOIndex);
			    QbOption qbOption = new QbOption();
			    qbOption.setName(answerDTO.getCandidateAnswer());
			    qbOption.setCorrect(McAppConstants.CORRECT.equals(answerDTO.getCorrect()));
			    qbOption.setDisplayOrder(candidateAnswerDTOIndex + 1);
			    candidateAnswers.add(qbOption);
			}
			mcService.saveOrUpdateMcQueContent(mcQueContent);
			mcContent.getMcQueContents().add(mcQueContent);
		    }
		    questionIndex++;
		}
	    } while (questionIndex <= plannerForm.getQuestionCount());
	    for (; questionIndex <= mcContent.getMcQueContents().size(); questionIndex++) {
		McQueContent mcQueContent = mcService.getQuestionByDisplayOrder(questionIndex, mcContent.getUid());
		mcContent.getMcQueContents().remove(mcQueContent);
		mcService.removeMcQueContent(mcQueContent);
	    }
	    plannerForm.fillForm(mcContent, mcService);
	} else {
	    request.setAttribute("errorMap", errorMap);
	}
	return "authoring/pedagogicalPlannerForm";
    }

    @RequestMapping("/createPedagogicalPlannerQuestion")
    public String createPedagogicalPlannerQuestion(@ModelAttribute McPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) {

	int questionDisplayOrder = plannerForm.getQuestionCount().intValue() + 1;
	plannerForm.setCandidateAnswerCount(new ArrayList<Integer>(plannerForm.getQuestionCount()));
	Map<String, String[]> paramMap = request.getParameterMap();
	for (int questionIndex = 1; questionIndex < questionDisplayOrder; questionIndex++) {
	    String[] param = paramMap.get(McAppConstants.CANDIDATE_ANSWER_COUNT + questionIndex);
	    int count = NumberUtils.toInt(param[0]);
	    plannerForm.getCandidateAnswerCount().add(count);
	}
	plannerForm.setDescription(questionDisplayOrder - 1, "");
	plannerForm.getCandidateAnswerCount().add(McAppConstants.CANDIDATE_ANSWER_DEFAULT_COUNT);
	plannerForm.setCorrect(questionDisplayOrder - 1, "1");

	return "authoring/pedagogicalPlannerForm";
    }

}