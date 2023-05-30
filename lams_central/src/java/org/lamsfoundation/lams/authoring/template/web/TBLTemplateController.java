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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.template.*;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.AuthoringJsonTags;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Team Based Learning template. There are two versions - the full blown one that is accessed from authoring and one
 * that allows just sections of a TBL sequence to be created for the LAMS TBL system. Which bits are created are
 * controlled by a series of checkboxes (set to true and hidden in the full authoring version). The LAMS TBL version
 * also has the option for timed gates, but the standard version uses permission gates.
 */
@Controller
@RequestMapping("authoring/template/tbl")
public class TBLTemplateController extends LdTemplateController {

    private static Logger log = Logger.getLogger(TBLTemplateController.class);
    private static String templateCode = "TBL";
    private static final DateFormat LESSON_SCHEDULING_DATETIME_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm");
    private static final Pattern IRAT_QUESTION_DISPLAY_ORDER_PATTERN = Pattern.compile("question(\\d+)");

    /**
     * Sets up the CKEditor stuff
     */
    @Override
    @RequestMapping("/init")
    public String init(HttpServletRequest request) throws Exception {
	request.setAttribute("questionNumber", "1");
	boolean isConfigurableVersion = WebUtil.readBooleanParam(request, "configure", false);
	String sessionMapID = WebUtil.readStrParam(request, "sessionMapID", true);
	if (sessionMapID != null) {
	    request.setAttribute("sessionMapID", sessionMapID);
	}
	HttpSession session = request.getSession();
	UserDTO userDTO = (UserDTO) session.getAttribute(AttributeNames.USER);
	if (userDTO != null) {
	    List<QbCollection> qbCollections = qbService.getUserCollections(userDTO.getUserID());
	    request.setAttribute("qbCollections", qbCollections);
	}

	if (isConfigurableVersion) {
	    return super.init(request, "authoring/template/tbl/tbloptional");
	} else {
	    return super.init(request);
	}
    }

    @Override
    protected ObjectNode createLearningDesign(HttpServletRequest request, HttpSession httpSession) throws Exception {
	TBLData data = new TBLData(request);
	if (data.getErrorMessages() != null && data.getErrorMessages().size() > 0) {
	    ObjectNode restRequestJSON = JsonNodeFactory.instance.objectNode();
	    restRequestJSON.set("errors", JsonUtil.readArray(data.getErrorMessages()));
	    return restRequestJSON;
	}

	UserDTO userDTO = (UserDTO) httpSession.getAttribute(AttributeNames.USER);
	Integer workspaceFolderID = workspaceManagementService.getUserWorkspaceFolder(userDTO.getUserID())
		.getResourceID().intValue();
	AtomicInteger maxUIID = new AtomicInteger();
	int order = 0;

	// create activities
	ArrayNode activities = JsonNodeFactory.instance.arrayNode();
	ArrayNode groupings = JsonNodeFactory.instance.arrayNode();

	Integer[] firstActivityInRowPosition = new Integer[] { 40,
		40 }; // the very first activity, all other locations can be calculated from here if needed!
	String activityTitle = null;
	Integer[] currentActivityPosition = null;
	Integer groupingUIID = null;

	// Welcome
	if (data.useIntroduction) {
	    activityTitle = data.getText("boilerplate.introduction.title");
	    Long welcomeToolContentId = createNoticeboardToolContent(userDTO, activityTitle,
		    data.getText("boilerplate.introduction.instructions"), null);
	    activities.add(createNoticeboardActivity(maxUIID, order++, firstActivityInRowPosition, welcomeToolContentId,
		    data.contentFolderID, null, null, null, activityTitle));
	    currentActivityPosition = calcPositionNextRight(firstActivityInRowPosition);
	} else {
	    currentActivityPosition = firstActivityInRowPosition;
	}

	// Grouping
	activityTitle = data.getText("boilerplate.grouping.title");
	ObjectNode[] groupingJSONs = createGroupingActivity(maxUIID, order++, currentActivityPosition,
		data.groupingType, data.numLearners, data.numGroups, activityTitle, null, data.getUIBundle(),
		data.getFormatter());
	activities.add(groupingJSONs[0]);
	groupings.add(groupingJSONs[1]);
	groupingUIID = groupingJSONs[1].get("groupingUIID").asInt();

	Long qbCollectionUid = data.qbCollectionUid;

	if (data.useIRATRA) {
	    // Stop!
	    currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	    activityTitle = data.getText("boilerplate.before.ira.gate");
	    if (data.useScheduledGates && data.iraStartOffset != null) {
		activities.add(createScheduledGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition),
			activityTitle, null, data.iraStartOffset, true));
	    } else {
		activities.add(
			createGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition), activityTitle,
				activityTitle, true));
	    }

	    // iRA Test - Assessment
	    currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	    activityTitle = data.getText("boilerplate.ira.title");
	    ArrayNode testQuestionsArray = JsonUtil.readArray(data.testQuestions.values());

	    Long iRAToolContentId = authoringService.createTblAssessmentToolContent(userDTO, activityTitle,
		    data.getText("boilerplate.ira.instructions"), null, false, true, data.shuffleIRAT,
		    data.confidenceLevelEnable, false, false, false, qbCollectionUid, testQuestionsArray);
	    ObjectNode iraActivityJSON = createAssessmentActivity(maxUIID, order++, currentActivityPosition,
		    iRAToolContentId, data.contentFolderID, groupingUIID, null, null, activityTitle);
	    activities.add(iraActivityJSON);

	    // Leader Selection
	    firstActivityInRowPosition = calcPositionBelow(firstActivityInRowPosition);
	    currentActivityPosition = firstActivityInRowPosition;
	    activityTitle = data.getText("boilerplate.leader.title");
	    Long leaderSelectionToolContentId = createLeaderSelectionToolContent(userDTO, activityTitle,
		    data.getText("boilerplate.leader.instructions"));
	    activities.add(createLeaderSelectionActivity(maxUIID, order++, currentActivityPosition,
		    leaderSelectionToolContentId, data.contentFolderID, groupingUIID, null, null, activityTitle));

	    // Stop!
	    currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	    activityTitle = data.getText("boilerplate.before.tra.gate");
	    if (data.useScheduledGates && data.traStartOffset != null) {
		activities.add(createScheduledGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition),
			activityTitle, null, data.traStartOffset, true));
	    } else {
		activities.add(
			createGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition), activityTitle,
				activityTitle, true));
	    }

	    // tRA Test
	    currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	    activityTitle = data.getText("boilerplate.tra.title");
	    Integer confidenceLevelsActivityUIID = data.confidenceLevelEnable ? JsonUtil.optInt(iraActivityJSON,
		    AuthoringJsonTags.ACTIVITY_UIID) : null;
	    Long tRAToolContentId = createScratchieToolContent(userDTO, activityTitle,
		    data.getText("boilerplate.tra.instructions"), false, true, confidenceLevelsActivityUIID,
		    qbCollectionUid, testQuestionsArray);
	    activities.add(createScratchieActivity(maxUIID, order++, currentActivityPosition, tRAToolContentId,
		    data.contentFolderID, groupingUIID, null, null, activityTitle));

	}

	// Application Exercises - multiple exercises with gates between each exercise. There may also be a grouped
	// notebook after an exercise if indicated by the user. Each Application Exercise will have one or more questions.
	// Start a new row so that they group nicely together
	int noticeboardCount = 0;
	if (data.useApplicationExercises) {

	    for (AppExData applicationExercise : data.applicationExercises.values()) {

		// Start a new row for each application exercise and potentially the notebook.
		firstActivityInRowPosition = calcPositionBelow(firstActivityInRowPosition);
		currentActivityPosition = firstActivityInRowPosition;

		//  Gate before Application Exercise
		String gateTitleBeforeAppEx = data.getText("boilerplate.before.app.ex",
			new String[] { applicationExercise.title });
		if (data.useScheduledGates && data.aeStartOffset != null) {
		    activities.add(
			    createScheduledGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition),
				    gateTitleBeforeAppEx, null, data.aeStartOffset, true));
		} else {
		    activities.add(createGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition),
			    gateTitleBeforeAppEx, gateTitleBeforeAppEx, true));
		}

		// Application Exercise
		int assessmentNumber = 1;
		String applicationExerciseTitle = applicationExercise.title;
		currentActivityPosition = calcPositionNextRight(currentActivityPosition);

		if (applicationExercise.assessments == null) {
		    // it is doKumaran type AE
		    Long aetoolContentId = createDokumaranToolContent(userDTO, applicationExerciseTitle,
			    applicationExercise.description, applicationExercise.dokuInstructions, false,
			    applicationExercise.dokuGalleryWalkEnabled, applicationExercise.dokuGalleryWalkReadOnly,
			    applicationExercise.dokuGalleryWalkInstructions, null);
		    activities.add(createDokumaranActivity(maxUIID, order++, currentActivityPosition, aetoolContentId,
			    data.contentFolderID, groupingUIID, null, null, applicationExerciseTitle));
		} else {
		    // it is Assessment type AE
		    ArrayNode questionsJSONArray = JsonNodeFactory.instance.arrayNode();
		    for (Assessment exerciseQuestion : applicationExercise.assessments.values()) {
			questionsJSONArray.add(exerciseQuestion.getAsObjectNode(assessmentNumber));
			assessmentNumber++;
		    }

		    Long aetoolContentId = authoringService.createTblAssessmentToolContent(userDTO,
			    applicationExerciseTitle,
			    StringUtils.isBlank(applicationExercise.description) ? data.getText(
				    "boilerplate.ae.instructions") : applicationExercise.description, null, true, true,
			    false, false, true, true, true, qbCollectionUid, questionsJSONArray);
		    activities.add(createAssessmentActivity(maxUIID, order++, currentActivityPosition, aetoolContentId,
			    data.contentFolderID, groupingUIID, null, null, applicationExerciseTitle));
		}

		// Optional Gate / Noticeboard. Don't add the extra gate in LAMS TBL or we will get too many scheduled gates to manage
		if (applicationExercise.useNoticeboard) {
		    noticeboardCount++;
		    String noticeboardCountAsString = Integer.toString(noticeboardCount);

		    if (!data.useScheduledGates) {
			currentActivityPosition = calcPositionNextRight(currentActivityPosition);
			String gateTitle = data.getText("boilerplate.before.app.ex.noticeboard",
				new String[] { noticeboardCountAsString });
			activities.add(
				createGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition), gateTitle,
					gateTitle, true));
		    }
		    currentActivityPosition = calcPositionNextRight(currentActivityPosition);
		    String notebookTitle = data.getText("boilerplate.grouped.ae.noticeboard.num",
			    new String[] { noticeboardCountAsString });
		    Long aeNoticeboardContentId = createNoticeboardToolContent(userDTO, notebookTitle,
			    applicationExercise.noticeboardInstructions, null);
		    activities.add(
			    createNoticeboardActivity(maxUIID, order++, currentActivityPosition, aeNoticeboardContentId,
				    data.contentFolderID, groupingUIID, null, null, notebookTitle));
		}
	    }

	}

	firstActivityInRowPosition = calcPositionBelow(firstActivityInRowPosition);
	currentActivityPosition = firstActivityInRowPosition;

	if (data.usePeerReview) {
	    // Peer Review - optional. Start by eliminating all criterias with no title. Then if any are left
	    // we create the gate before the Peer Review and the Peer Review tool data and activity
	    ArrayNode criterias = JsonNodeFactory.instance.arrayNode();
	    for (PeerReviewCriteria criteria : data.peerReviewCriteria.values()) {
		if (criteria.getTitle() != null && criteria.getTitle().length() > 0) {
		    criterias.add(criteria.getAsObjectNode());
		}
	    }
	    if (criterias.size() > 0) {
		// Stop!
		String gateTitle = data.getText("boilerplate.before.peer.review");
		activities.add(createGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition), gateTitle,
			gateTitle, true));

		currentActivityPosition = calcPositionNextRight(currentActivityPosition);
		String peerReviewTitle = data.getText("boilerplate.peerreview");
		Long prtoolContentId = createPeerReviewToolContent(userDTO, peerReviewTitle,
			data.getText("boilerplate.peerreview.instructions"), null, criterias);
		activities.add(createPeerReviewActivity(maxUIID, order++, currentActivityPosition, prtoolContentId,
			data.contentFolderID, groupingUIID, null, null, peerReviewTitle));
		currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	    }
	}

	if (data.useReflection) {
	    // Stop!
	    activities.add(
		    createGateActivity(maxUIID, order++, calcGateOffset(currentActivityPosition), null, null, true));

	    // Individual Reflection
	    currentActivityPosition = calcPositionNextRight(currentActivityPosition);
	    activityTitle = data.getText("boilerplate.individual.reflection.title");
	    Long reflectionToolContentId = createNotebookToolContent(userDTO, activityTitle,
		    data.getText("boilerplate.individual.reflection.instructions"), false, false);
	    activities.add(createNotebookActivity(maxUIID, order++, currentActivityPosition, reflectionToolContentId,
		    data.contentFolderID, null, null, null, activityTitle));

	}

	// fill in required LD data and send it to the LearningDesignRestServlet
	ArrayNode transitions = createTransitions(maxUIID, activities);

	return saveLearningDesign(templateCode, data.sequenceTitle, "", workspaceFolderID, data.contentFolderID,
		maxUIID.get(), activities, transitions, groupings, null);

    }

    class AppExData {
	String title = "Fix me";
	SortedMap<Integer, Assessment> assessments;

	String description;
	String dokuInstructions;
	boolean dokuGalleryWalkEnabled;
	boolean dokuGalleryWalkReadOnly;
	String dokuGalleryWalkInstructions;

	boolean useNoticeboard = false;
	String noticeboardInstructions;
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

	/* The error messages are to be sorted by tab, so store them separately and join together */ List<String> lessonDetailsErrors = new ArrayList<>();
	List<String> ratErrors = new ArrayList<>();
	List<String> applicationExerciseErrors = new ArrayList<>();
	List<String> peerReviewErrors = new ArrayList<>();

	/* Fields from form */ String contentFolderID;
	String sequenceTitle;
	Integer groupingType;
	Integer numLearners;
	Integer numGroups;

	Long qbCollectionUid;

	Long iraGateStartOffset = null;
	Long traGateStartOffset = null;
	Long aeGateStartOffset = null;

	// used to configure which parts to use for the TBL system
	boolean useFirstGateBeforeGrouping = false;
	boolean useIntroduction = false;
	boolean useIRATRA = false;
	boolean useApplicationExercises = false;
	boolean usePeerReview = false;
	boolean useReflection = false;

	boolean useScheduledGates = false;
	Long startTime = null;
	Long iraStartOffset = null;
	Long traStartOffset = null;
	Long aeStartOffset = null;

	SortedMap<Integer, ObjectNode> testQuestions;
	boolean confidenceLevelEnable;
	boolean shuffleIRAT;
	SortedMap<Integer, AppExData> applicationExercises;
	SortedMap<Integer, PeerReviewCriteria> peerReviewCriteria;

	int questionOffset = 8;
	int assessmentOffset = 10;

	TBLData(HttpServletRequest request) {
	    super(request, templateCode);

//	    // Debugging .....String name = (String) parameterNames.nextElement();
//	    for ( Map.Entry<String, String[]>  paramEntry : request.getParameterMap().entrySet() ) {
//		StringBuffer debugStr = new StringBuffer("Parameter name ").append(paramEntry.getKey()).append(" values ");
//		for ( String value : paramEntry.getValue() ) {
//		    debugStr.append(value).append(", ");
//		}
//		log.debug(debugStr.toString());
//	    }

	    contentFolderID = getTrimmedString(request, "contentFolderID", false);

	    sequenceTitle = getTrimmedString(request, "sequenceTitle", false);

	    groupingType = WebUtil.readIntParam(request, "grouping");
	    numLearners = WebUtil.readIntParam(request, "numLearners", true);
	    numGroups = WebUtil.readIntParam(request, "numGroups", true);

	    qbCollectionUid = WebUtil.readLongParam(request, "qbCollectionUid", true);

	    testQuestions = new TreeMap<>();
	    applicationExercises = new TreeMap<>();
	    peerReviewCriteria = new TreeMap<>();

	    useIntroduction = WebUtil.readBooleanParam(request, "introduction", false);
	    useIRATRA = WebUtil.readBooleanParam(request, "iratra", false);
	    useApplicationExercises = WebUtil.readBooleanParam(request, "appex", false);
	    usePeerReview = WebUtil.readBooleanParam(request, "preview", false);
	    useReflection = WebUtil.readBooleanParam(request, "reflect", false);

	    String start = WebUtil.readStrParam(request, "startLogistic", true);
	    if (start != null) {
		// we are using the TBL version of LAMS to start the lessons, so we need to use schedule gates
		// and set the timings of the gates. Need to take into account if the lesson is started now or in the future.
		useScheduledGates = true;
		String dateTimeString = WebUtil.readStrParam(request, "lessonStartDatetime", true);
		if (start.equals("startNow") || dateTimeString == null || dateTimeString.length() == 0) {
		    startTime = System.currentTimeMillis();
		} else {
		    try {
			Date startDate = TBLTemplateController.LESSON_SCHEDULING_DATETIME_FORMAT.parse(dateTimeString);
			startTime = startDate.getTime();
		    } catch (ParseException e) {
			log.error("Unable to parse date from " + dateTimeString + ". Leaving start date as now.");
			startTime = System.currentTimeMillis();
		    }
		}
		iraStartOffset = getOffsetFromRequest(request, "iraLogistic", "iraScheduled", "iraStartDatetime");
		traStartOffset = getOffsetFromRequest(request, "traLogistic", "traScheduled", "traStartDatetime");
		aeStartOffset = getOffsetFromRequest(request, "aeLogistic", "aeScheduled", "aeStartDatetime");
	    }

	    // Process the Multiple Choice Questions that go to IRA and TRA & Peer Review fields
	    TreeMap<Integer, Integer> correctAnswers = new TreeMap<>();
	    Enumeration parameterNames = request.getParameterNames();
	    while (parameterNames.hasMoreElements()) {
		String name = (String) parameterNames.nextElement();
		if (useIRATRA && name.startsWith("question")) {
		    Matcher questionDisplayOrderMather = IRAT_QUESTION_DISPLAY_ORDER_PATTERN.matcher(name);
		    questionDisplayOrderMather.find();
		    int questionDisplayOrder = Integer.parseInt(questionDisplayOrderMather.group(1));
		    ObjectNode question = testQuestions.get(questionDisplayOrder);
		    if (question == null) {
			// init iRAT question
			boolean isMarkHedging = WebUtil.readBooleanParam(request,
				"question" + questionDisplayOrder + "markHedging", false);
			String title = getTrimmedString(request, "question" + questionDisplayOrder + "title", false);
			String text = getTrimmedString(request, "question" + questionDisplayOrder, true);
			String[] learningOutcomes = request.getParameterValues(
				"question" + questionDisplayOrder + "learningOutcome");
			Long collectionUid = WebUtil.readLongParam(request,
				"question" + questionDisplayOrder + "collection", true);
			String uuid = getTrimmedString(request, "question" + questionDisplayOrder + "uuid", false);
			Integer mark = WebUtil.readIntParam(request, "question" + questionDisplayOrder + "mark", true);
			question = processTestQuestion(
				isMarkHedging ? QbQuestion.TYPE_MARK_HEDGING : QbQuestion.TYPE_MULTIPLE_CHOICE, text,
				title, questionDisplayOrder, mark, uuid, learningOutcomes, collectionUid);
		    }

		    int correctIndex = name.indexOf("correct");
		    if (correctIndex > 0) { // question1correct
			Integer correctValue = WebUtil.readIntParam(request, name);
			correctAnswers.put(questionDisplayOrder, correctValue);
			continue;
		    }

		    int optionIndex = name.indexOf("option");
		    if (optionIndex > 0) {
			Integer optionDisplayOrder = Integer.valueOf(name.substring(optionIndex + 6));
			String optionText = getTrimmedString(request, name, true);
			processTestOption(question, optionDisplayOrder, optionText);
		    }

		} else if (usePeerReview && name.startsWith("peerreview")) {
		    processInputPeerReviewRequestField(name, request);
		}
	    }

	    confidenceLevelEnable = WebUtil.readBooleanParam(request, "confidenceLevelEnable", false);
	    shuffleIRAT = WebUtil.readBooleanParam(request, "shuffleIRAT", false);
	    if (useIRATRA) {
		if (testQuestions.size() == 0) {
		    addValidationErrorMessage("authoring.error.rat.not.blank", null, ratErrors);
		}
		updateCorrectAnswers(correctAnswers);
		redoDisplayOrder();
	    }

	    if (useApplicationExercises) {
		processApplicationExercises(request);
	    }

	    validate();

	    errorMessages.addAll(lessonDetailsErrors);
	    errorMessages.addAll(ratErrors);
	    errorMessages.addAll(applicationExerciseErrors);
	    errorMessages.addAll(peerReviewErrors);
	}

	// Reset the display order or it confuses matters in the MCQ tool. Display order may skip numbers if the user
	// deletes questions on the screen before saving.
	private void redoDisplayOrder() {
	    SortedMap<Integer, ObjectNode> oldTestQuestions = testQuestions;
	    testQuestions = new TreeMap<>();
	    int newDisplayOrder = 1;
	    for (Map.Entry<Integer, ObjectNode> oldQuestionEntry : oldTestQuestions.entrySet()) {
		ObjectNode question = oldQuestionEntry.getValue();
		question.put(RestTags.DISPLAY_ORDER, newDisplayOrder);
		testQuestions.put(newDisplayOrder, question);
		newDisplayOrder++;
	    }
	}

	private Long getOffsetFromRequest(HttpServletRequest request, String radioButtonField,
		String radioButtonScheduledString, String dateField) {
	    String radioValue = WebUtil.readStrParam(request, radioButtonField, true);
	    if (radioButtonScheduledString.equals(radioValue)) {
		String dateTimeString = WebUtil.readStrParam(request, dateField);
		if (dateTimeString != null) {
		    try {
			long offset =
				TBLTemplateController.LESSON_SCHEDULING_DATETIME_FORMAT.parse(dateTimeString).getTime()
					- this.startTime;
			return offset > 0 ? offset / 60000 : 0; // from ms to minutes
		    } catch (ParseException e) {
			log.error("Unable to parse date from " + dateField
				+ ". Leaving gate as a permission gate to be opened manually.");
		    }
		}
	    }
	    return null;
	}

	// Assessment entries can't be deleted or rearranged so the count should always line up with numAppEx and numAssessments
	private void processApplicationExercises(HttpServletRequest request) {
	    int numAppEx = WebUtil.readIntParam(request, "numAppEx");
	    for (int i = 1; i <= numAppEx; i++) {
		String appexDiv = "divappex" + i;
		AppExData newAppex = new AppExData();
		newAppex.title = WebUtil.readStrParam(request, appexDiv + "Title", true);
		newAppex.description = WebUtil.readStrParam(request, appexDiv + "Description", true);

		boolean isDoku = WebUtil.readBooleanParam(request, appexDiv + "IsDoku", false);

		if (isDoku) {
		    newAppex.dokuInstructions = WebUtil.readStrParam(request, appexDiv + "dokuInstructions", true);
		    newAppex.dokuGalleryWalkEnabled = WebUtil.readBooleanParam(request,
			    appexDiv + "dokuGalleryWalkEnabled", false);
		    newAppex.dokuGalleryWalkReadOnly = WebUtil.readBooleanParam(request,
			    appexDiv + "dokuGalleryWalkReadOnly", false);
		    newAppex.dokuGalleryWalkInstructions = WebUtil.readStrParam(request,
			    appexDiv + "dokuGalleryWalkInstructions", true);
		} else {
		    newAppex.assessments = processAssessments(request, i, newAppex.title);
		}

		if (isDoku ? StringUtils.isNotBlank(newAppex.description) || StringUtils.isNotBlank(
			newAppex.dokuInstructions) : newAppex.assessments != null) {
		    newAppex.useNoticeboard = WebUtil.readBooleanParam(request, appexDiv + "NB", false);
		    if (newAppex.useNoticeboard) {
			newAppex.noticeboardInstructions = getTrimmedString(request, appexDiv + "NBEntry", true);
			if (newAppex.noticeboardInstructions == null) {
			    addValidationErrorMessage("authoring.error.application.exercise.needs.noticeboard.text",
				    new Object[] { "\"" + newAppex.title + "\"" }, applicationExerciseErrors);
			}
		    }
		    applicationExercises.put(i, newAppex);
		}
	    }
	}

	private SortedMap<Integer, Assessment> processAssessments(HttpServletRequest request, int appexNumber,
		String appexTitle) {
	    SortedMap<Integer, Assessment> applicationExercises = new TreeMap<>();
	    Integer numAssessments = WebUtil.readIntParam(request, "numAssessments" + appexNumber, true);
	    if (numAssessments == null) {
		// Application Exercise has been deleted
		return null;
	    }

	    for (int i = 1; i <= numAssessments; i++) {
		String assessmentPrefix = new StringBuilder("divass").append(appexNumber).append("assessment").append(i)
			.toString();
		String questionText = getTrimmedString(request, assessmentPrefix, true);
		String questionTitle = getTrimmedString(request, assessmentPrefix + "title", true);
		String questionUuid = getTrimmedString(request, assessmentPrefix + "uuid", false);
		String markAsString = getTrimmedString(request, assessmentPrefix + "mark", false);
		String[] learningOutcomes = request.getParameterValues(assessmentPrefix + "learningOutcome");
		String collectionUid = getTrimmedString(request, assessmentPrefix + "collection", false);
		Assessment assessment = new Assessment();
		if (!(questionTitle == null && questionText == null)) {
		    assessment.setTitle(questionTitle);
		    assessment.setText(questionText);
		    assessment.setType(WebUtil.readStrParam(request, assessmentPrefix + "type"));
		    assessment.setRequired(true);
		    assessment.setUuid(questionUuid);

		    Integer mark = -1;
		    if (markAsString != null) {
			try {
			    mark = Integer.parseInt(markAsString);
			} catch (Exception e) {
			}
		    }
		    if (mark < 1) {
			addValidationErrorMessage("authoring.error.application.exercise.mark.one.or.more",
				new String[] { "\"" + appexTitle + "\"", "\"" + questionTitle + "\"" },
				applicationExerciseErrors);
		    } else {
			assessment.setDefaultGrade(mark);
		    }

		    if (assessment.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE) {
			assessment.setMultipleAnswersAllowed(
				WebUtil.readBooleanParam(request, assessmentPrefix + "multiAllowed", false));
			String optionPrefix = new StringBuilder("divass").append(appexNumber).append("assmcq").append(i)
				.append("option").toString();
			for (int o = 1, order = 1; o <= MAX_OPTION_COUNT; o++) {
			    String answer = getTrimmedString(request, optionPrefix + o, true);
			    if (answer != null) {
				String grade = request.getParameter(optionPrefix + o + "grade");
				try {
				    assessment.getAnswers()
					    .add(new AssessMCAnswer(order++, answer, Float.valueOf(grade)));
				} catch (Exception e) {
				    log.error("Error parsing " + grade + " for float", e);
				    addValidationErrorMessage(
					    "authoring.error.application.exercise.not.blank.and.grade",
					    new Object[] { "\"" + appexTitle + "\"", i }, applicationExerciseErrors);
				}
			    }
			}
		    }

		    if (learningOutcomes != null && learningOutcomes.length > 0) {
			assessment.setLearningOutcomes(Arrays.asList(learningOutcomes));
		    }

		    if (collectionUid != null) {
			assessment.setCollectionUid(Long.valueOf(collectionUid));
		    }

		    applicationExercises.put(i, assessment);
		}
	    }
	    return applicationExercises;
	}

	void processInputPeerReviewRequestField(String name, HttpServletRequest request) {
	    int fieldIndex = name.indexOf("EnableComments");
	    if (fieldIndex > 0) { // peerreview1EnableComments
		Integer criteriaNumber = Integer.valueOf(name.substring(10, fieldIndex));
		findCriteriaInMap(criteriaNumber).setCommentsEnabled(WebUtil.readBooleanParam(request, name, false));
	    } else {
		fieldIndex = name.indexOf("MinWordsLimit");
		if (fieldIndex > 0) {
		    Integer criteriaNumber = Integer.valueOf(name.substring(10, fieldIndex));
		    findCriteriaInMap(criteriaNumber).setCommentsMinWordsLimit(
			    WebUtil.readIntParam(request, name, true));
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

	void processTestOption(ObjectNode question, Integer optionDisplayOrder, String optionText) {
	    if (optionDisplayOrder != null && optionText != null) {
		ObjectNode newOption = JsonNodeFactory.instance.objectNode();
		newOption.put(RestTags.DISPLAY_ORDER, optionDisplayOrder);
		newOption.put(RestTags.CORRECT, false);
		newOption.put(RestTags.ANSWER_TEXT, optionText);
		((ArrayNode) question.get(RestTags.ANSWERS)).add(newOption);
	    }
	}

	ObjectNode processTestQuestion(int questionType, String questionText, String questionTitle,
		Integer questionDisplayOrder, Integer mark, String questionUuid, String[] learningOutcomes,
		Long collectionUid) {

	    ObjectNode question = JsonNodeFactory.instance.objectNode();
	    question.put("type", questionType);
	    question.set(RestTags.ANSWERS, JsonNodeFactory.instance.arrayNode());
	    question.put(RestTags.DISPLAY_ORDER, questionDisplayOrder);
	    testQuestions.put(questionDisplayOrder, question);

	    if (questionTitle != null) {
		question.put(RestTags.QUESTION_TITLE, questionTitle);
	    }

	    if (questionText != null) {
		question.put(RestTags.QUESTION_TEXT, questionText);
		// default title just in case - used for scratchie. Should be replaced with a user value
		if (!question.has(RestTags.QUESTION_TITLE)) {
		    question.put(RestTags.QUESTION_TITLE, "Q" + questionDisplayOrder.toString());
		}
	    }

	    if (mark != null) {
		question.put("defaultGrade", mark);
	    }

	    if (questionUuid != null) {
		question.put(RestTags.QUESTION_UUID, questionUuid);
	    }

	    if (learningOutcomes != null && learningOutcomes.length > 0) {
		try {
		    ArrayNode learningOutcomesJSON = JsonUtil.readArray(learningOutcomes);
		    question.set(RestTags.LEARNING_OUTCOMES, learningOutcomesJSON);
		} catch (IOException e) {
		    log.error("Error while processing learning outcomes for question: " + question.get(
			    RestTags.QUESTION_TITLE));
		}
	    }

	    if (collectionUid != null) {
		question.put(RestTags.COLLECTION_UID, collectionUid);
	    }

	    return question;
	}

	void updateCorrectAnswers(TreeMap<Integer, Integer> correctAnswers) {
	    for (Entry<Integer, ObjectNode> entry : testQuestions.entrySet()) {
		Integer questionNumber = entry.getKey();
		ObjectNode question = entry.getValue();
//		if (!question.has(RestTags.QUESTION_TEXT)) {
//		    Object param = question.has(RestTags.QUESTION_TITLE) ? question.get(RestTags.QUESTION_TITLE)
//			    : questionNumber;
//		    addValidationErrorMessage("authoring.error.question.num", new Object[] { param }, ratErrors);
//		}

		Integer correctAnswerDisplay = correctAnswers.get(entry.getKey());
		if (correctAnswerDisplay == null) {
		    Object param = question.has(RestTags.QUESTION_TITLE)
			    ? question.get(RestTags.QUESTION_TITLE)
			    : questionNumber;
		    addValidationErrorMessage("authoring.error.question.correct.num", new Object[] { param },
			    ratErrors);
		} else {

		    ArrayNode answers = (ArrayNode) question.get(RestTags.ANSWERS);
		    if (answers == null || answers.size() == 0) {
			Object param = question.has(RestTags.QUESTION_TITLE)
				? question.get(RestTags.QUESTION_TITLE)
				: questionNumber;
			addValidationErrorMessage("authoring.error.question.must.have.answer.num",
				new Object[] { param }, ratErrors);
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
			    Object param = question.has(RestTags.QUESTION_TITLE)
				    ? question.get(RestTags.QUESTION_TITLE)
				    : questionNumber;
			    addValidationErrorMessage("authoring.error.question.correct.num", new Object[] { param },
				    ratErrors);
			}
		    }
		}
	    }
	}

	// do any additional validation not included in other checking
	void validate() {
	    if (contentFolderID == null) {
		addValidationErrorMessage("authoring.error.content.id", null, lessonDetailsErrors);
	    }
	    if (sequenceTitle == null || !ValidationUtil.isOrgNameValid(sequenceTitle)) {
		lessonDetailsErrors.add(
			new StringBuilder(TextUtil.getText(appBundle, "authoring.section.lessondetails")).append(": ")
				.append(TextUtil.getText(appBundle, "authoring.fla.title.validation.error"))
				.toString());
	    }

	    if (useApplicationExercises) {
		validateApplicationExercises();
	    }
	    // grouping is enforced on the front end by the layout & by requiring the groupingType
	    // questions are validated in updateCorrectAnswers
	}

	private void validateApplicationExercises() {
	    if (applicationExercises.size() == 0) {
		return;
	    }
	    for (Map.Entry<Integer, AppExData> appExEntry : applicationExercises.entrySet()) {
		AppExData appEx = appExEntry.getValue();
		if (StringUtils.isBlank(appEx.description) && StringUtils.isBlank(appEx.dokuInstructions) && (
			appEx.assessments == null)) {
		    addValidationErrorMessage("authoring.error.application.exercise.num",
			    new String[] { "\"" + appEx.title + "\"" }, applicationExerciseErrors);
		} else if (appEx.assessments != null) {
		    for (Map.Entry<Integer, Assessment> assessmentEntry : appEx.assessments.entrySet()) {
			assessmentEntry.getValue()
				.validate(applicationExerciseErrors, appBundle, formatter, appExEntry.getKey(),
					"\"" + appEx.title + "\"", assessmentEntry.getKey());
		    }
		}
	    }
	}

    }

    // Create the application exercise form on the screen. Not in LdTemplateController as this is specific to TBL
    // and hence the jsp is in the tbl template folder, not the tool template folder
    @RequestMapping("/createApplicationExercise")
    public String createApplicationExercise(@RequestParam String type) {
	return "/authoring/template/tbl/appex" + (type.equals("doku") ? "Doku" : "Assessment");
    }

}