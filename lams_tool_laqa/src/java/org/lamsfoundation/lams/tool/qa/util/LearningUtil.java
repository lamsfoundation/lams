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

package org.lamsfoundation.lams.tool.qa.util;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.dto.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.model.QaContent;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.lamsfoundation.lams.tool.qa.model.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.model.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.web.form.QaLearningForm;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Keeps all operations needed for Learning mode.
 *
 * @author Ozgur Demirtas
 */
public class LearningUtil implements QaAppConstants {

    public static void saveFormRequestData(HttpServletRequest request, QaLearningForm qaLearningForm) {
	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");
	qaLearningForm.setUserID(userID);

	String sessionMapID = request.getParameter("sessionMapID");
	qaLearningForm.setSessionMapID(sessionMapID);

	String totalQuestionCount = request.getParameter("totalQuestionCount");
	qaLearningForm.setTotalQuestionCount(totalQuestionCount);
    }

    public static GeneralLearnerFlowDTO buildGeneralLearnerFlowDTO(IQaService service, QaContent qaContent) {
	GeneralLearnerFlowDTO generalLearnerFlowDTO = new GeneralLearnerFlowDTO();
	generalLearnerFlowDTO.setActivityTitle(qaContent.getTitle());
	generalLearnerFlowDTO.setActivityInstructions(qaContent.getInstructions());

	if (qaContent.isQuestionsSequenced()) {
	    generalLearnerFlowDTO.setQuestionListingMode(QUESTION_LISTING_MODE_SEQUENTIAL);
	} else {
	    generalLearnerFlowDTO.setQuestionListingMode(QUESTION_LISTING_MODE_COMBINED);
	}

	generalLearnerFlowDTO.setUserNameVisible(qaContent.isUsernameVisible());
	generalLearnerFlowDTO.setShowOtherAnswers(qaContent.isShowOtherAnswers());
	generalLearnerFlowDTO.setAllowRichEditor(new Boolean(qaContent.isAllowRichEditor()).toString());
	generalLearnerFlowDTO
		.setUseSelectLeaderToolOuput(new Boolean(qaContent.isUseSelectLeaderToolOuput()).toString());
	generalLearnerFlowDTO.setTotalQuestionCount(new Integer(qaContent.getQaQueContents().size()));

	//check if allow rate answers is ON and also that there is at least one non-comments rating criteria available
	generalLearnerFlowDTO.setAllowRateAnswers(service.isRatingsEnabled(qaContent));

	//create mapQuestions
	Map<Integer, QaQuestionDTO> mapQuestions = new TreeMap<Integer, QaQuestionDTO>();
	for (QaQueContent question : qaContent.getQaQueContents()) {
	    int displayOrder = question.getDisplayOrder();
	    if (displayOrder != 0) {
		//add the question to the questions Map in the displayOrder
		QaQuestionDTO questionDTO = new QaQuestionDTO(question);
		mapQuestions.put(displayOrder, questionDTO);
	    }
	}
	generalLearnerFlowDTO.setMapQuestionContentLearner(mapQuestions);

	return generalLearnerFlowDTO;
    }

    /**
     */
    public static void populateAnswers(Map sessionMap, QaContent qaContent, QaQueUsr user,
	    Map<Integer, QaQuestionDTO> mapQuestions, GeneralLearnerFlowDTO generalLearnerFlowDTO,
	    IQaService qaService) {

	//create mapAnswers
	Map<String, String> mapAnswers = (Map<String, String>) sessionMap.get(MAP_ALL_RESULTS_KEY);
	if (mapAnswers == null) {
	    mapAnswers = new TreeMap<String, String>(new QaComparator());

	    // get responses from DB
	    Map<String, String> mapAnswersFromDb = new TreeMap<String, String>();
	    for (QaQueContent question : qaContent.getQaQueContents()) {
		Long questionUid = question.getUid();
		QaUsrResp response = qaService.getResponseByUserAndQuestion(user.getQueUsrId(), questionUid);

		String answer;
		if (response == null) {
		    answer = null;
		} else if (!user.isResponseFinalized() && response.getAnswerAutosaved() != null) {
		    answer = response.getAnswerAutosaved();
		} else {
		    answer = response.getAnswer();
		}
		mapAnswersFromDb.put(String.valueOf(question.getDisplayOrder()), answer);
	    }

	    // maybe we have come in from the review screen, if so get the answers from db.
	    if (mapAnswersFromDb.size() > 0) {
		mapAnswers.putAll(mapAnswersFromDb);
	    } else {
		for (Map.Entry pairs : mapQuestions.entrySet()) {
		    mapAnswers.put(pairs.getKey().toString(), "");
		}
	    }
	}
	String currentAnswer = mapAnswers.get("1");
	generalLearnerFlowDTO.setCurrentQuestionIndex(new Integer(1));
	generalLearnerFlowDTO.setCurrentAnswer(currentAnswer);
	sessionMap.put(MAP_SEQUENTIAL_ANSWERS_KEY, mapAnswers);
	generalLearnerFlowDTO.setMapAnswers(mapAnswers);
	sessionMap.put(MAP_ALL_RESULTS_KEY, mapAnswers);
    }
}
