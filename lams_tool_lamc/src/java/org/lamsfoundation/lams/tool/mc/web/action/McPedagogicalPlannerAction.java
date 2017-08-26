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


package org.lamsfoundation.lams.tool.mc.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.tool.mc.web.form.McPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class McPedagogicalPlannerAction extends LamsDispatchAction {

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return initPedagogicalPlannerForm(mapping, form, request, response);
    }

    public ActionForward initPedagogicalPlannerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	McPedagogicalPlannerForm plannerForm = (McPedagogicalPlannerForm) form;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = getMcService().getMcContent(toolContentID);
	plannerForm.fillForm(mcContent, getMcService());
	return mapping.findForward(McAppConstants.SUCCESS);
    }

    public ActionForward saveOrUpdatePedagogicalPlannerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {

	McPedagogicalPlannerForm plannerForm = (McPedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate(request);

	if (errors.isEmpty()) {
	    McContent mcContent = getMcService().getMcContent(plannerForm.getToolContentID());
	    int questionIndex = 1;
	    String question = null;

	    do {
		question = plannerForm.getQuestion(questionIndex - 1);
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
			McQueContent mcQueContent = getMcService().getQuestionByDisplayOrder((long) questionIndex,
				mcContent.getUid());
			mcQueContent.setQuestion(question);
			int candidateAnswerDTOIndex = 0;
			Set<McOptsContent> candidateAnswers = mcQueContent.getMcOptionsContents();
			Iterator<McOptsContent> candidateAnswerIter = candidateAnswers.iterator();
			while (candidateAnswerIter.hasNext()) {
			    McOptsContent candidateAnswer = candidateAnswerIter.next();
			    if (candidateAnswerDTOIndex >= candidateAnswerDTOList.size()) {
				candidateAnswerIter.remove();
			    } else {
				McOptionDTO answerDTO = candidateAnswerDTOList.get(candidateAnswerDTOIndex);
				candidateAnswer.setCorrectOption(McAppConstants.CORRECT.equals(answerDTO.getCorrect()));
				candidateAnswer.setMcQueOptionText(answerDTO.getCandidateAnswer());
				getMcService().updateMcOptionsContent(candidateAnswer);
			    }
			    candidateAnswerDTOIndex++;
			}
			getMcService().saveOrUpdateMcQueContent(mcQueContent);
		    } else {
			McQueContent mcQueContent = new McQueContent();
			mcQueContent.setDisplayOrder(questionIndex);
			mcQueContent.setMcContent(mcContent);
			mcQueContent.setMcContentId(mcContent.getMcContentId());
			mcQueContent.setQuestion(question);
			mcQueContent.setMark(McAppConstants.QUESTION_DEFAULT_MARK);
			Set<McOptsContent> candidateAnswers = mcQueContent.getMcOptionsContents();
			for (int candidateAnswerDTOIndex = 0; candidateAnswerDTOIndex < candidateAnswerDTOList
				.size(); candidateAnswerDTOIndex++) {
			    McOptionDTO answerDTO = candidateAnswerDTOList.get(candidateAnswerDTOIndex);
			    McOptsContent candidateAnswer = new McOptsContent(candidateAnswerDTOIndex + 1,
				    McAppConstants.CORRECT.equals(answerDTO.getCorrect()),
				    answerDTO.getCandidateAnswer(), mcQueContent);
			    candidateAnswer.setMcQueContentId(mcQueContent.getMcContentId());
			    candidateAnswers.add(candidateAnswer);
			}
			getMcService().saveOrUpdateMcQueContent(mcQueContent);
			mcContent.getMcQueContents().add(mcQueContent);
		    }
		    questionIndex++;
		}
	    } while (questionIndex <= plannerForm.getQuestionCount());
	    for (; questionIndex <= mcContent.getMcQueContents().size(); questionIndex++) {
		McQueContent mcQueContent = getMcService().getQuestionByDisplayOrder((long) questionIndex,
			mcContent.getUid());
		mcContent.getMcQueContents().remove(mcQueContent);
		getMcService().removeMcQueContent(mcQueContent);
	    }
	    plannerForm.fillForm(mcContent, getMcService());
	} else {
	    saveErrors(request, errors);
	}
	return mapping.findForward(McAppConstants.SUCCESS);
    }

    public ActionForward createPedagogicalPlannerQuestion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	McPedagogicalPlannerForm plannerForm = (McPedagogicalPlannerForm) form;
	int questionDisplayOrder = plannerForm.getQuestionCount().intValue() + 1;
	plannerForm.setCandidateAnswerCount(new ArrayList<Integer>(plannerForm.getQuestionCount()));
	Map<String, String[]> paramMap = request.getParameterMap();
	for (int questionIndex = 1; questionIndex < questionDisplayOrder; questionIndex++) {
	    String[] param = paramMap.get(McAppConstants.CANDIDATE_ANSWER_COUNT + questionIndex);
	    int count = NumberUtils.toInt(param[0]);
	    plannerForm.getCandidateAnswerCount().add(count);
	}
	plannerForm.setQuestion(questionDisplayOrder - 1, "");
	plannerForm.getCandidateAnswerCount().add(McAppConstants.CANDIDATE_ANSWER_DEFAULT_COUNT);
	plannerForm.setCorrect(questionDisplayOrder - 1, "1");
	return mapping.findForward(McAppConstants.SUCCESS);
    }

    private IMcService getMcService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return McServiceProxy.getMcService(getServlet().getServletContext());
    }

}