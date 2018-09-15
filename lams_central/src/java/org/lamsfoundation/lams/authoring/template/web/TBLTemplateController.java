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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.authoring.template.web;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.template.AssessMCAnswer;
import org.lamsfoundation.lams.authoring.template.Assessment;
import org.lamsfoundation.lams.authoring.template.PeerReviewCriteria;
import org.lamsfoundation.lams.authoring.template.TemplateData;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.AuthoringJsonTags;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * A Team Based Learning template.
 */
@Controller
@RequestMapping("authoring/template/tbl")
public class TBLTemplateController extends LdTemplateController {

    private static Logger log = Logger.getLogger(TBLTemplateController.class);
    private static String templateCode = "TBL";

    /**
     * Sets up the CKEditor stuff
     */
    @Override
    @RequestMapping("/init")
    public String init(HttpServletRequest request) throws Exception {
	request.setAttribute("questionNumber", "1");
	return super.init(request);
    }

    @Override
    protected ObjectNode createLearningDesign(HttpServletRequest request) throws Exception {

	TBLData data = new TBLData(request);
	if (data.getErrorMessages() != null && data.getErrorMessages().size() > 0) {
	    ObjectNode restRequestJSON = JsonNodeFactory.instance.objectNode();
	    restRequestJSON.set("errors", JsonUtil.readArray(data.getErrorMessages()));
	    return restRequestJSON;
	}

	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer workspaceFolderID = workspaceManagementService.getUserWorkspaceFolder(userDTO.getUserID())
		.getResourceID().intValue();
	AtomicInteger maxUIID = new AtomicInteger();
	int order = 0;

	// create activities
	ArrayNode activities = JsonNodeFactory.instance.arrayNode();
	ArrayNode groupings = JsonNodeFactory.instance.arrayNode();

	Integer[] firstActivityInRowPosition = new Integer[] { 20, 125 }; // the very first activity, all other locations can be calculated from here if needed!

	// Welcome
	String activityTitle = data.getText("boilerplate.introduction.title");
	Long welcomeToolContentId = createNoticeboardToolContent(userDTO, activityTitle,
		data.getText("boilerplate.introduction.instructions"), null);
	activities.add(createNoticeboardActivity(maxUIID, order++, firstActivityInRowPosition, welcomeToolContentId,
		data.contentFolderID, null, null, null, activityTitle));

	// Grouping
	Integer[] currentActivityPosition = calcPositionNextRight(firstActivityInRowPosition);
	ObjectNode[] groupingJSONs = createGroupingActivity(maxUIID, order++, currentActivityPosition,
		data.groupingType, data.numLearners, data.numGroups, null, null, data.getUIBundle(),
		data.getFormatter());
	activities.add(groupingJSONs[0]);
	groupings.add(groupingJSONs[1]);
	Integer groupingUIID = groupingJSONs[1].get("groupingUIID").asInt();

	// Stop!
	currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	activities.add(createGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition)));

	// iRA Test - MCQ
	currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	activityTitle = data.getText("boilerplate.ira.title");
	Long iRAToolContentId = createMCQToolContent(userDTO, activityTitle,
		data.getText("boilerplate.ira.instructions"), false, data.confidenceLevelEnable,
		JsonUtil.readArray(data.testQuestions.values()));
	ObjectNode iraActivityJSON = createMCQActivity(maxUIID, order++, currentActivityPosition, iRAToolContentId,
		data.contentFolderID, groupingUIID, null, null, activityTitle);
	activities.add(iraActivityJSON);

	// Stop!
	currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	activities.add(createGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition)));

	// Leader Selection
	firstActivityInRowPosition = calcPositionBelow(firstActivityInRowPosition);
	activityTitle = data.getText("boilerplate.leader.title");
	Long leaderSelectionToolContentId = createLeaderSelectionToolContent(userDTO, activityTitle,
		data.getText("boilerplate.leader.instructions"));
	activities.add(createLeaderSelectionActivity(maxUIID, order++, firstActivityInRowPosition,
		leaderSelectionToolContentId, data.contentFolderID, groupingUIID, null, null, activityTitle));

	// tRA Test
	currentActivityPosition = calcPositionNextRight(firstActivityInRowPosition);
	activityTitle = data.getText("boilerplate.tra.title");
	Integer confidenceLevelsActivityUIID = data.confidenceLevelEnable
		? JsonUtil.optInt(iraActivityJSON, AuthoringJsonTags.ACTIVITY_UIID)
		: null;
	Long tRAToolContentId = createScratchieToolContent(userDTO, activityTitle,
		data.getText("boilerplate.tra.instructions"), false, confidenceLevelsActivityUIID,
		JsonUtil.readArray(data.testQuestions.values()));
	activities.add(createScratchieActivity(maxUIID, order++, currentActivityPosition, tRAToolContentId,
		data.contentFolderID, groupingUIID, null, null, activityTitle));

	// Stop!
	currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	activities.add(createGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition)));

	// Application Exercise - all the questions go into one exercise
	firstActivityInRowPosition = calcPositionBelow(firstActivityInRowPosition);
	currentActivityPosition = firstActivityInRowPosition;
	ArrayNode questionsJSONArray = JsonNodeFactory.instance.arrayNode();
	int displayOrder = 1;
	for (Assessment exerciseQuestion : data.applicationExercises.values()) {
	    String applicationExerciseTitle = data.getText("boilerplate.ae.application.exercise.num",
		    new String[] { Integer.toString(displayOrder) });
	    exerciseQuestion.setTitle(applicationExerciseTitle);
	    questionsJSONArray.add(exerciseQuestion.getAsObjectNode(displayOrder));
	    displayOrder++;
	}
	String overallAssessmentTitle = data.getText("boilerplate.ae.application.exercise.num", new String[] { "" });
	Long aetoolContentId = createAssessmentToolContent(userDTO, overallAssessmentTitle,
		data.getText("boilerplate.ae.instructions"), null, true, questionsJSONArray);
	activities.add(createAssessmentActivity(maxUIID, order++, currentActivityPosition, aetoolContentId,
		data.contentFolderID, groupingUIID, null, null, overallAssessmentTitle));

	// Peer Review - optional. Start by eliminating all criterias with no title. Then if any are left
	// we create the tool data and activity
	currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	ArrayNode criterias = JsonNodeFactory.instance.arrayNode();
	for (PeerReviewCriteria criteria : data.peerReviewCriteria.values()) {
	    if (criteria.getTitle() != null && criteria.getTitle().length() > 0) {
		criterias.add(criteria.getAsObjectNode());
	    }
	}
	if (criterias.size() > 0) {
	    String peerReviewTitle = data.getText("boilerplate.peerreview");
	    Long prtoolContentId = createPeerReviewToolContent(userDTO, peerReviewTitle,
		    data.getText("boilerplate.peerreview.instructions"), null, criterias);
	    activities.add(createPeerReviewActivity(maxUIID, order++, currentActivityPosition, prtoolContentId,
		    data.contentFolderID, groupingUIID, null, null, peerReviewTitle));
	    displayOrder++;
	    currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	}

	// Stop!
	activities.add(createGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition)));

	// Individual Reflection
	currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	activityTitle = data.getText("boilerplate.individual.reflection.title");
	Long reflectionToolContentId = createNotebookToolContent(userDTO, activityTitle,
		data.getText("boilerplate.individual.reflection.instructions"), false, false);
	activities.add(createNotebookActivity(maxUIID, order++, currentActivityPosition, reflectionToolContentId,
		data.contentFolderID, null, null, null, activityTitle));

	ArrayNode transitions = createTransitions(maxUIID, activities);

	// fill in required LD data and send it to the LearningDesignRestServlet
	return saveLearningDesign(templateCode, data.sequenceTitle, "", workspaceFolderID, data.contentFolderID,
		maxUIID.get(), activities, transitions, groupings, null);

    }

    /**
     * TBLData contains all the data we need for the current instance. Created for each call to ensure that we have the
     * correct locale, messages, etc for this particular call.
     *
     * The complex types are converted straight to JSON objects ready for sending to the tools. No need to process
     * twice!
     *
     * For the test questions need to meet the spec: The questions entry should be ArrayNode containing JSON objects,
     * which in turn must contain "questionText", "displayOrder" (Integer) and a ArrayNode "answers". The options entry
     * should be ArrayNode containing JSON objects, which in turn must contain "answerText", "displayOrder" (Integer),
     * "correctOption" (Boolean).
     */
    class TBLData extends TemplateData {
	/* Fields from form */
	String contentFolderID;
	String sequenceTitle;
	Integer groupingType;
	Integer numLearners;
	Integer numGroups;
	SortedMap<Integer, ObjectNode> testQuestions;
	boolean confidenceLevelEnable;
	SortedMap<Integer, Assessment> applicationExercises;
	SortedMap<Integer, PeerReviewCriteria> peerReviewCriteria;

	int questionOffset = 8;
	int assessmentOffset = 10;

	TBLData(HttpServletRequest request) {
	    super(request, templateCode);

	    contentFolderID = getTrimmedString(request, "contentFolderID", false);

	    sequenceTitle = getTrimmedString(request, "sequenceTitle", false);

	    groupingType = WebUtil.readIntParam(request, "grouping");
	    numLearners = WebUtil.readIntParam(request, "numLearners", true);
	    numGroups = WebUtil.readIntParam(request, "numGroups", true);

	    testQuestions = new TreeMap<Integer, ObjectNode>();
	    applicationExercises = new TreeMap<Integer, Assessment>();
	    peerReviewCriteria = new TreeMap<Integer, PeerReviewCriteria>();

	    TreeMap<Integer, Integer> correctAnswers = new TreeMap<Integer, Integer>();
	    Enumeration parameterNames = request.getParameterNames();
	    while (parameterNames.hasMoreElements()) {
		String name = (String) parameterNames.nextElement();
		if (name.startsWith("question")) {
		    int correctIndex = name.indexOf("correct");
		    if (correctIndex > 0) { // question1correct
			Integer questionDisplayOrder = Integer.valueOf(name.substring(questionOffset, correctIndex));
			Integer correctValue = WebUtil.readIntParam(request, name);
			correctAnswers.put(questionDisplayOrder, correctValue);
		    } else {
			int optionIndex = name.indexOf("option");
			if (optionIndex > 0) {
			    Integer questionDisplayOrder = Integer.valueOf(name.substring(questionOffset, optionIndex));
			    Integer optionDisplayOrder = Integer.valueOf(name.substring(optionIndex + 6));
			    processTestQuestion(name, null, questionDisplayOrder, optionDisplayOrder,
				    getTrimmedString(request, name, true));
			} else {
			    Integer questionDisplayOrder = Integer.valueOf(name.substring(questionOffset));
			    processTestQuestion(name, getTrimmedString(request, name, true), questionDisplayOrder, null,
				    null);
			}
		    }
		} else if (name.startsWith("peerreview")) {
		    processInputPeerReviewRequestField(name, request);
		}
	    }
	    confidenceLevelEnable = WebUtil.readBooleanParam(request, "confidenceLevelEnable", false);
	    updateCorrectAnswers(correctAnswers);

	    processAssessments(request);

	    validate();

	}

	// Assessment entries can't be deleted or rearranged so the count should always line up with numAssessments
	private void processAssessments(HttpServletRequest request) {
	    int numAssessments = WebUtil.readIntParam(request, "numAssessments");
	    for (int i = 1; i <= numAssessments; i++) {
		String assessmentPrefix = "assessment" + i;
		String questionText = getTrimmedString(request, assessmentPrefix, true);
		Assessment assessment = new Assessment();
		if (questionText != null) {
		    assessment.setQuestionText(questionText);
		    assessment.setType(WebUtil.readStrParam(request, assessmentPrefix + "type"));
		    assessment.setRequired(true);
		    if (assessment.getType() == Assessment.ASSESSMENT_QUESTION_TYPE_MULTIPLE_CHOICE) {
			String optionPrefix = "assmcq" + i;
			optionPrefix = optionPrefix + "option";
			for (int o = 1, order = 0; o <= MAX_OPTION_COUNT; o++) {
			    String answer = getTrimmedString(request, optionPrefix + o, true);
			    if (answer != null) {
				String grade = request.getParameter(optionPrefix + o + "grade");
				try {
				    assessment.getAnswers()
					    .add(new AssessMCAnswer(order++, answer, Float.valueOf(grade)));
				} catch (Exception e) {
				    log.error("Error parsing " + grade + " for float", e);
				    addValidationErrorMessage(
					    "authoring.error.application.exercise.not.blank.and.grade", new Object[i]);
				}
			    }
			}
		    }
		    applicationExercises.put(i, assessment);
		}
	    }
	}

	void processInputPeerReviewRequestField(String name, HttpServletRequest request) {
	    log.debug("process peer review " + name + " order " + name.substring(10));
	    int fieldIndex = name.indexOf("EnableComments");
	    if (fieldIndex > 0) { // peerreview1EnableComments
		Integer criteriaNumber = Integer.valueOf(name.substring(10, fieldIndex));
		findCriteriaInMap(criteriaNumber).setCommentsEnabled(WebUtil.readBooleanParam(request, name, false));
	    } else {
		fieldIndex = name.indexOf("MinWordsLimit");
		if (fieldIndex > 0) {
		    Integer criteriaNumber = Integer.valueOf(name.substring(10, fieldIndex));
		    findCriteriaInMap(criteriaNumber)
			    .setCommentsMinWordsLimit(WebUtil.readIntParam(request, name, true));
		} else {
		    Integer criteriaNumber = Integer.valueOf(name.substring(10));
		    findCriteriaInMap(criteriaNumber).setTitle(getTrimmedString(request, name, true));
		}
	    }
	}

	private PeerReviewCriteria findCriteriaInMap(Integer criteriaNumber) {
	    PeerReviewCriteria criteria = peerReviewCriteria.get(criteriaNumber);
	    if (criteria == null) {
		criteria = new PeerReviewCriteria(criteriaNumber);
		peerReviewCriteria.put(criteriaNumber, criteria);
	    }
	    return criteria;
	}

	void processTestQuestion(String name, String questionText, Integer questionDisplayOrder,
		Integer optionDisplayOrder, String optionText) {

	    ObjectNode question = testQuestions.get(questionDisplayOrder);
	    if (question == null) {
		question = JsonNodeFactory.instance.objectNode();
		question.set(RestTags.ANSWERS, JsonNodeFactory.instance.arrayNode());
		question.put(RestTags.DISPLAY_ORDER, questionDisplayOrder);
		testQuestions.put(questionDisplayOrder, question);
	    }

	    if (questionText != null) {
		question.put(RestTags.QUESTION_TEXT, questionText);
		question.put(RestTags.QUESTION_TITLE, "Q" + questionDisplayOrder.toString()); // only used for
											      // Scratchie, not MCQ but
											      // won't hurt MCQ by being
											      // there!
	    }

	    if (optionDisplayOrder != null && optionText != null) {
		ObjectNode newOption = JsonNodeFactory.instance.objectNode();
		newOption.put(RestTags.DISPLAY_ORDER, optionDisplayOrder);
		newOption.put(RestTags.CORRECT, false);
		newOption.put(RestTags.ANSWER_TEXT, optionText);
		((ArrayNode) question.get(RestTags.ANSWERS)).add(newOption);
	    }

	}

	void updateCorrectAnswers(TreeMap<Integer, Integer> correctAnswers) {
	    for (Entry<Integer, ObjectNode> entry : testQuestions.entrySet()) {
		Integer questionNumber = entry.getKey();
		ObjectNode question = entry.getValue();
		if (!question.has(RestTags.QUESTION_TEXT)) {
		    addValidationErrorMessage("authoring.error.question.num", new Integer[] { questionNumber });
		}

		Integer correctAnswerDisplay = correctAnswers.get(entry.getKey());
		if (correctAnswerDisplay == null) {
		    addValidationErrorMessage("authoring.error.question.correct.num", new Integer[] { questionNumber });
		} else {

		    ArrayNode answers = (ArrayNode) question.get(RestTags.ANSWERS);
		    if (answers == null || answers.size() == 0) {
			addValidationErrorMessage("authoring.error.question.must.have.answer.num",
				new Integer[] { questionNumber });
		    } else {
			boolean correctAnswerFound = false; // may not exist as the user didn't put any text in!
			for (int i = 0; i < answers.size(); i++) {
			    ObjectNode option = (ObjectNode) answers.get(i);
			    if (correctAnswerDisplay.equals(option.get(RestTags.DISPLAY_ORDER).asInt())) {
				option.remove(RestTags.CORRECT);
				option.put(RestTags.CORRECT, true);
				correctAnswerFound = true;
			    }
			}
			if (!correctAnswerFound) {
			    addValidationErrorMessage("authoring.error.question.correct.num",
				    new Integer[] { questionNumber });
			}
		    }
		}
	    }
	}

	// do any additional validation not included in other checking
	void validate() {
	    if (contentFolderID == null) {
		addValidationErrorMessage("authoring.error.content.id", null);
	    }
	    if (sequenceTitle == null || !ValidationUtil.isOrgNameValid(sequenceTitle)) {
		addValidationErrorMessage("authoring.fla.title.validation.error", null);
	    }
	    if (applicationExercises.size() == 0) {
		addValidationErrorMessage("authoring.error.application.exercise.num", new Integer[] { 1 });
	    } else {
		for (Map.Entry<Integer, Assessment> assessmentEntry : applicationExercises.entrySet()) {
		    List<String> errors = assessmentEntry.getValue().validate(appBundle, formatter,
			    assessmentEntry.getKey());
		    if (errors != null) {
			errorMessages.addAll(errors);
		    }
		}
	    }
	    // grouping is enforced on the front end by the layout & by requiring the groupingType
	    // questions are validated in updateCorrectAnswers
	}

    }

}
