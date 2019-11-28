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

package org.lamsfoundation.lams.tool.mc.web.form;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.planner.PedagogicalPlannerActivitySpringForm;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.util.AuthoringUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class McPedagogicalPlannerForm extends PedagogicalPlannerActivitySpringForm {

    private static Logger logger = Logger.getLogger(McPedagogicalPlannerForm.class);

    @Autowired
    @Qualifier("lamcMessageService")
    private MessageService messageService;

    private List<String> question;
    private List<Integer> candidateAnswerCount;
    private String candidateAnswersString;
    private List<String> correct;

    public MultiValueMap<String, String> validate(HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	boolean allEmpty = true;

	if (question != null && !question.isEmpty()) {
	    int questionIndex = 1;
	    for (String item : question) {
		if (item != null || !StringUtils.isEmpty(item)) {
		    try {
			List<McOptionDTO> candidateAnswerList = extractCandidateAnswers(request, questionIndex);
			if (candidateAnswerList != null) {
			    boolean answersEmpty = true;
			    String correctAnswerBlankError = null;
			    for (McOptionDTO answer : candidateAnswerList) {
				if (answer != null && !StringUtils.isEmpty(answer.getCandidateAnswer())) {
				    allEmpty = false;
				    answersEmpty = false;
				} else if (McAppConstants.CORRECT.equals(answer.getCorrect())) {
				    correctAnswerBlankError = "error.pedagogical.planner.empty.answer.selected";
				}
			    }
			    if (!answersEmpty && correctAnswerBlankError != null) {
				errorMap.add("GLOBAL",
					messageService.getMessage("error.pedagogical.planner.empty.answer.selected"));
			    }
			}
		    } catch (UnsupportedEncodingException e) {
			McPedagogicalPlannerForm.logger.error(e.getMessage());
			return errorMap;
		    }
		    questionIndex++;
		}
	    }
	}
	if (allEmpty) {
	    errorMap.clear();
	    errorMap.add("GLOBAL", messageService.getMessage("questions.none.submitted"));
	    question = null;
	    setCandidateAnswersString("");
	} else if (!errorMap.isEmpty()) {
	    StringBuilder candidateAnswersBuilder = new StringBuilder();
	    Map<String, String[]> paramMap = request.getParameterMap();
	    setCandidateAnswerCount(new ArrayList<Integer>(getQuestionCount()));
	    for (String key : paramMap.keySet()) {
		if (key.startsWith(McAppConstants.CANDIDATE_ANSWER_PREFIX)) {
		    String[] param = paramMap.get(key);
		    String answer = param[0];
		    candidateAnswersBuilder.append(key).append('=').append(answer).append('&');
		}
	    }
	    setCandidateAnswersString(candidateAnswersBuilder.toString());
	    for (int questionIndex = 1; questionIndex <= getQuestionCount(); questionIndex++) {
		Object param = paramMap.get(McAppConstants.CANDIDATE_ANSWER_COUNT + questionIndex);
		int count = NumberUtils.toInt(((String[]) param)[0]);
		getCandidateAnswerCount().add(count);
	    }
	}

	setValid(errorMap.isEmpty());
	return errorMap;
    }

    public void fillForm(McContent mcContent, IMcService mcService) {
	if (mcContent != null) {
	    List<McQuestionDTO> questionDtos = AuthoringUtil.buildDefaultQuestions(mcContent);

	    StringBuilder candidateAnswersBuilder = new StringBuilder();
	    setCandidateAnswerCount(new ArrayList<Integer>(questionDtos.size()));
	    for (int questionIndex = 1; questionIndex <= questionDtos.size(); questionIndex++) {
		McQuestionDTO item = questionDtos.get(questionIndex - 1);
		int questionDisplayOrder = item.getDisplayOrder();
		String questionText = item.getQuestion();
		setQuestion(questionDisplayOrder - 1, questionText);
		List<McOptionDTO> candidateAnswers = item.getOptionDtos();

		for (int candidateAnswerIndex = 1; candidateAnswerIndex <= candidateAnswers
			.size(); candidateAnswerIndex++) {

		    McOptionDTO candidateAnswer = candidateAnswers.get(candidateAnswerIndex - 1);

		    candidateAnswersBuilder.append(McAppConstants.CANDIDATE_ANSWER_PREFIX).append(questionDisplayOrder)
			    .append('-').append(candidateAnswerIndex).append('=')
			    .append(candidateAnswer.getCandidateAnswer()).append('&');
		    if (candidateAnswer.getCorrect().equals(McAppConstants.CORRECT)) {
			setCorrect(questionDisplayOrder - 1, String.valueOf(candidateAnswerIndex));
		    }
		    getCandidateAnswerCount().add(candidateAnswers.size());
		}
	    }
	    setCandidateAnswersString(candidateAnswersBuilder.toString());
	}
    }

    public void setQuestion(int number, String Questions) {
	if (question == null) {
	    question = new ArrayList<>();
	}
	while (number >= question.size()) {
	    question.add(null);
	}
	question.set(number, Questions);
    }

    public String getQuestion(int number) {
	if (question == null || number >= question.size()) {
	    return null;
	}
	return question.get(number);
    }

    public Integer getQuestionCount() {
	return question == null ? 0 : question.size();
    }

    public boolean removeQuestion(int number) {
	if (question == null || number >= question.size()) {
	    return false;
	}
	question.remove(number);
	return true;
    }

    public String getCandidateAnswersString() {
	return candidateAnswersString;
    }

    public void setCandidateAnswersString(String candidateAnswers) {
	candidateAnswersString = candidateAnswers;
    }

    public List<McOptionDTO> extractCandidateAnswers(HttpServletRequest request, int questionIndex)
	    throws UnsupportedEncodingException {
	Map<String, String[]> paramMap = request.getParameterMap();
	String[] param = paramMap.get(McAppConstants.CANDIDATE_ANSWER_COUNT + questionIndex);

	int count = NumberUtils.toInt(param[0]);
	int correct = Integer.parseInt(getCorrect(questionIndex - 1));
	List<McOptionDTO> candidateAnswerList = new ArrayList<>();
	for (int index = 1; index <= count; index++) {
	    param = paramMap.get(McAppConstants.CANDIDATE_ANSWER_PREFIX + questionIndex + "-" + index);
	    String answer = param[0];
	    if (answer != null) {
		McOptionDTO candidateAnswer = new McOptionDTO();
		candidateAnswer.setCandidateAnswer(answer);
		if (index == correct) {
		    candidateAnswer.setCorrect(McAppConstants.CORRECT);
		}
		candidateAnswerList.add(candidateAnswer);
	    }
	}
	return candidateAnswerList;
    }

    public String getCorrect(int number) {
	if (correct == null || number >= correct.size()) {
	    return null;
	}
	return correct.get(number);
    }

    public void setCorrect(int number, String correct) {
	if (this.correct == null) {
	    this.correct = new ArrayList<>();
	}
	while (number >= this.correct.size()) {
	    this.correct.add(null);
	}
	this.correct.set(number, correct);
    }

    public List<Integer> getCandidateAnswerCount() {

	return candidateAnswerCount;
    }

    public void setCandidateAnswerCount(List<Integer> candidateAnswerCount) {

	this.candidateAnswerCount = candidateAnswerCount;
    }

    public List<String> getQuestionList() {
	return question;
    }
}