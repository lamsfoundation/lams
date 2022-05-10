package org.lamsfoundation.lams.web.qb;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.service.IOutcomeService;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionExporter;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exports and imports IMS QTI questions.
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/imsqti")
public class ImsQtiController {
    private static Logger log = Logger.getLogger(ImsQtiController.class);

    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;

    @Autowired
    private IQbService qbService;

    @Autowired
    private IUserManagementService userManagementService;

    @Autowired
    private IOutcomeService outcomeService;

    /**
     * Parses questions extracted from IMS QTI file and adds them as new QB questions.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/saveQTI", produces = "text/plain", method = RequestMethod.POST)
    @ResponseBody
    public String saveQTI(HttpServletRequest request, @RequestParam long collectionUid,
	    @RequestParam(defaultValue = "") String contentFolderID) throws UnsupportedEncodingException {

	Question[] questions = QuestionParser.parseQuestionChoiceForm(request);
	Set<String> collectionUUIDs = null;
	StringBuilder qbQuestionUidsString = new StringBuilder();

	for (Question question : questions) {

	    String uuid = question.getQbUUID();
	    QbQuestion qbQuestion = null;

	    // try to match the question to an existing QB question in DB
	    if (uuid != null) {
		qbQuestion = qbService.getQuestionByUUID(UUID.fromString(uuid));
		if (qbQuestion != null) {
		    // found an existing question with same UUID
		    // now check if it is in the collection already
		    if (collectionUUIDs == null) {
			// get UUIDs of collection questions as strings
			collectionUUIDs = qbService.getCollectionQuestions(collectionUid).stream()
				.filter(q -> q.getUuid() != null)
				.collect(Collectors.mapping(q -> q.getUuid().toString(), Collectors.toSet()));
		    }

		    if (collectionUUIDs.contains(uuid)) {
			if (log.isDebugEnabled()) {
			    log.debug("Found existing question. Name: " + qbQuestion.getName() + ", uid: "
				    + qbQuestion.getUid());
			}
		    } else {
			qbService.addQuestionToCollection(collectionUid, qbQuestion.getQuestionId(), false);
			collectionUUIDs.add(uuid);

			if (log.isDebugEnabled()) {
			    log.debug("Added to collection an existing question. Name: " + qbQuestion.getName()
				    + ", uid: " + qbQuestion.getUid());
			}
		    }
		}
	    }

	    if (qbQuestion == null) {

		qbQuestion = new QbQuestion();
		qbQuestion.setUuid(uuid);
		qbQuestion.setName(question.getTitle());
		qbQuestion.setContentFolderId(
			StringUtils.isBlank(contentFolderID) ? FileUtil.generateUniqueContentFolderID()
				: contentFolderID);
		qbQuestion.setDescription(QuestionParser.processHTMLField(question.getText(), false,
			qbQuestion.getContentFolderId(), question.getResourcesFolderPath()));
		qbQuestion.setFeedback(QuestionParser.processHTMLField(question.getFeedback(), false,
			qbQuestion.getContentFolderId(), question.getResourcesFolderPath()));
		qbQuestion.setPenaltyFactor(0);
		int questionId = qbService.generateNextQuestionId();
		qbQuestion.setQuestionId(questionId);
		qbQuestion.setVersion(1);

		Integer questionMark = question.getScore();
		boolean isMultipleChoice = Question.QUESTION_TYPE_MULTIPLE_CHOICE.equals(question.getType());
		boolean isMarkHedgingType = Question.QUESTION_TYPE_MARK_HEDGING.equals(question.getType());
		boolean isVsaType = Question.QUESTION_TYPE_FILL_IN_BLANK.contentEquals(question.getType());

		// options are different depending on the type
		if (isMultipleChoice || isMarkHedgingType || isVsaType) {

		    // setting answers is very similar in both types, so they were put together here
		    if (isMarkHedgingType) {
			qbQuestion.setType(QbQuestion.TYPE_MARK_HEDGING);

		    } else if (isMultipleChoice) {
			qbQuestion.setType(QbQuestion.TYPE_MULTIPLE_CHOICE);
			qbQuestion.setMultipleAnswersAllowed(false);
			qbQuestion.setShuffle(false);
			qbQuestion.setPrefixAnswersWithLetters(false);

		    } else if (isVsaType) {
			qbQuestion.setType(QbQuestion.TYPE_VERY_SHORT_ANSWERS);
			qbQuestion.setCaseSensitive(false);

			if (question.getAnswers().size() == 1) {
			    // add missing incorrect answer
			    // as the correct answer always has to be present
			    Answer incorrectAnswer = new Answer();
			    incorrectAnswer.setDisplayOrder(2);
			    incorrectAnswer.setScore(0f);
			    question.getAnswers().add(incorrectAnswer);
			}
		    }

		    String correctAnswer = null;
		    if (question.getAnswers() != null) {
			TreeSet<QbOption> optionList = new TreeSet<>();
			int orderId = 0;
			for (Answer answer : question.getAnswers()) {
			    String answerText = QuestionParser.processHTMLField(answer.getText(), false,
				    contentFolderID, question.getResourcesFolderPath());
			    if ((correctAnswer != null) && correctAnswer.equals(answerText)) {
				log.warn("Skipping an answer with same text as the correct answer: " + answerText);
				continue;
			    }
			    QbOption option = new QbOption();
			    if (isVsaType && answerText != null) {
				// convert comma-separated answers to ones accepted by QB VSA questions
				answerText = Stream.of(answerText.split(",")).map(String::strip)
					.collect(Collectors.joining("\r\n"));
			    }
			    option.setName(answerText);
			    option.setDisplayOrder(orderId++);
			    option.setFeedback(answer.getFeedback());
			    option.setQbQuestion(qbQuestion);

			    if ((answer.getScore() != null) && (answer.getScore() > 0) && (correctAnswer == null)) {
				if (questionMark == null) {
				    // whatever the correct answer holds, it becomes the question score
				    questionMark = Double.valueOf(Math.ceil(answer.getScore())).intValue();
				}
				// 100% goes to the correct answer
				option.setMaxMark(1);
				correctAnswer = answerText;
			    } else {
				option.setMaxMark(0);
			    }

			    optionList.add(option);
			}

			qbQuestion.setQbOptions(new ArrayList<>(optionList));
		    }

		    if (correctAnswer == null) {
			log.warn("No correct answer found for question: " + question.getText());
			continue;
		    }

		} else if (Question.QUESTION_TYPE_MULTIPLE_RESPONSE.equals(question.getType())) {
		    qbQuestion.setType(QbQuestion.TYPE_MULTIPLE_CHOICE);
		    qbQuestion.setMultipleAnswersAllowed(true);
		    qbQuestion.setShuffle(false);
		    qbQuestion.setPrefixAnswersWithLetters(false);

		    if (question.getAnswers() != null) {
			float totalScore = 0;
			for (Answer answer : question.getAnswers()) {
			    if ((answer.getScore() != null) && (answer.getScore() > 0)) {
				// the question score information is stored as sum of answer scores
				totalScore += answer.getScore();
			    }
			}
			if (questionMark == null) {
			    questionMark = Double.valueOf(Math.round(totalScore)).intValue();
			}

			TreeSet<QbOption> optionList = new TreeSet<>();
			int orderId = 1;
			for (Answer answer : question.getAnswers()) {
			    String answerText = answer.getText();
			    QbOption qbOption = new QbOption();
			    qbOption.setName(answerText);
			    qbOption.setDisplayOrder(orderId++);
			    qbOption.setFeedback(answer.getFeedback());
			    qbOption.setQbQuestion(qbQuestion);

			    if ((answer.getScore() != null) && (answer.getScore() > 0)) {
				// set the factor of score for correct answers
				qbOption.setMaxMark(answer.getScore() / totalScore);
			    } else {
				qbOption.setMaxMark(0);
			    }

			    optionList.add(qbOption);
			}

			qbQuestion.setQbOptions(new ArrayList<>(optionList));
		    }

		} else if (Question.QUESTION_TYPE_TRUE_FALSE.equals(question.getType())) {
		    qbQuestion.setType(QbQuestion.TYPE_TRUE_FALSE);

		    if (question.getAnswers() == null) {
			log.warn("Answers missing from true-false question: " + question.getText());
			continue;
		    } else {
			for (Answer answer : question.getAnswers()) {
			    if ((answer.getScore() != null) && (answer.getScore() > 0)) {
				qbQuestion.setCorrectAnswer(Boolean.parseBoolean(answer.getText()));
				if (questionMark == null) {
				    questionMark = Double.valueOf(Math.ceil(answer.getScore())).intValue();
				}
			    }
			    if (!StringUtils.isBlank(answer.getFeedback())) {
				// set feedback for true/false answers
				if (Boolean.parseBoolean(answer.getText())) {
				    qbQuestion.setFeedbackOnCorrect(answer.getFeedback());
				} else {
				    qbQuestion.setFeedbackOnIncorrect(answer.getFeedback());
				}
			    }
			}
		    }
		} else if (Question.QUESTION_TYPE_MATCHING.equals(question.getType())) {
		    qbQuestion.setType(QbQuestion.TYPE_MATCHING_PAIRS);
		    qbQuestion.setShuffle(true);

		    if (question.getAnswers() != null) {
			// the question score information is stored as sum of answer scores
			float totalScore = 0;
			for (Answer answer : question.getAnswers()) {
			    if ((answer.getScore() != null) && (answer.getScore() > 0)) {
				totalScore += answer.getScore();
			    }
			}
			if (questionMark == null) {
			    questionMark = Double.valueOf(Math.round(totalScore)).intValue();
			}

			TreeSet<QbOption> optionList = new TreeSet<>();
			int orderId = 1;
			for (int answerIndex = 0; answerIndex < question.getAnswers().size(); answerIndex++) {
			    // QTI allows answers without a match, but LAMS assessment tool does not
			    Integer matchAnswerIndex = question.getMatchMap() == null ? null
				    : question.getMatchMap().get(answerIndex);
			    Answer matchAnswer = (matchAnswerIndex == null) || (question.getMatchAnswers() == null)
				    ? null
				    : question.getMatchAnswers().get(matchAnswerIndex);
			    if (matchAnswer != null) {
				Answer answer = question.getAnswers().get(answerIndex);
				String answerText = answer.getText();
				QbOption assessmentAnswer = new QbOption();
				assessmentAnswer.setMatchingPair(answerText);
				assessmentAnswer.setName(matchAnswer.getText());
				assessmentAnswer.setDisplayOrder(orderId++);
				assessmentAnswer.setFeedback(answer.getFeedback());
				assessmentAnswer.setQbQuestion(qbQuestion);

				optionList.add(assessmentAnswer);
			    }
			}

			qbQuestion.setQbOptions(new ArrayList<>(optionList));
		    }
		} else if (Question.QUESTION_TYPE_ESSAY.equals(question.getType())) {
		    qbQuestion.setType(QbQuestion.TYPE_ESSAY);
		    qbQuestion.setAllowRichEditor(false);

		} else {
		    log.warn("Unknow QTI question type: " + question.getType());
		    continue;
		}

		qbQuestion.setMaxMark(questionMark == null ? 1 : questionMark);
		userManagementService.save(qbQuestion);

		if (question.getLearningOutcomes() != null && !question.getLearningOutcomes().isEmpty()) {
		    for (String learningOutcomeText : question.getLearningOutcomes()) {
			learningOutcomeText = learningOutcomeText.strip();
			List<Outcome> learningOutcomes = userManagementService.findByProperty(Outcome.class, "name",
				learningOutcomeText);
			Outcome learningOutcome = null;
			if (learningOutcomes.isEmpty()) {
			    learningOutcome = outcomeService.createOutcome(learningOutcomeText,
				    ImsQtiController.getUserDTO().getUserID());
			} else {
			    learningOutcome = learningOutcomes.get(0);
			}

			OutcomeMapping outcomeMapping = new OutcomeMapping();
			outcomeMapping.setOutcome(learningOutcome);
			outcomeMapping.setQbQuestionId(questionId);
			userManagementService.save(outcomeMapping);
		    }
		}

		qbService.addQuestionToCollection(collectionUid, qbQuestion.getQuestionId(), false);
	    }

	    qbQuestionUidsString.append(qbQuestion.getUid()).append(',');

	    if (log.isDebugEnabled()) {
		log.debug("Imported QTI question. Name: " + qbQuestion.getName() + ", uid: " + qbQuestion.getUid());
	    }
	}

	String qbQuestionUids = null;
	if (qbQuestionUidsString.length() > 0
		&& qbQuestionUidsString.charAt(qbQuestionUidsString.length() - 1) == ',') {
	    qbQuestionUids = qbQuestionUidsString.substring(0, qbQuestionUidsString.length() - 1);
	} else {
	    qbQuestionUids = qbQuestionUidsString.toString();
	}
	return qbQuestionUids;
    }

    /**
     * Exports QB question as IMS QTI package.
     */
    @RequestMapping(path = "/exportQuestionAsQTI", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void exportQuestionAsQTI(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam long qbQuestionUid) {
	QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);
	List<QbQuestion> qbQuestions = new LinkedList<>();
	qbQuestions.add(qbQuestion);

	String fileTitle = qbQuestion.getName();
	exportQTI(request, response, qbQuestions, fileTitle);
    }

    /**
     * Exports all questions from QB Collection as IMS QTI package.
     */
    @RequestMapping(path = "/exportCollectionAsQTI", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void exportCollectionAsQTI(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam long collectionUid) {
	List<QbQuestion> qbQuestions = qbService.getCollectionQuestions(collectionUid);

	QbCollection collection = qbService.getCollectionByUid(collectionUid);
	String fileTitle = collection.getName();

	exportQTI(request, response, qbQuestions, fileTitle);
    }

    /**
     * Prepares QB questions for QTI packing.
     */
    private void exportQTI(HttpServletRequest request, HttpServletResponse response, List<QbQuestion> qbQuestions,
	    String fileTitle) {
	List<Question> questions = new LinkedList<>();
	for (QbQuestion qbQuestion : qbQuestions) {
	    Question question = new Question();
	    List<Answer> answers = new ArrayList<>();

	    switch (qbQuestion.getType()) {

		case QbQuestion.TYPE_MULTIPLE_CHOICE:

		    if (qbQuestion.isMultipleAnswersAllowed()) {
			question.setType(Question.QUESTION_TYPE_MULTIPLE_RESPONSE);
			int correctAnswerCount = 0;

			for (QbOption assessmentAnswer : qbQuestion.getQbOptions()) {
			    if (assessmentAnswer.getMaxMark() > 0) {
				correctAnswerCount++;
			    }
			}

			Float correctAnswerScore = correctAnswerCount > 0
				? Integer.valueOf(100 / correctAnswerCount).floatValue()
				: null;
			int incorrectAnswerCount = qbQuestion.getQbOptions().size() - correctAnswerCount;
			Float incorrectAnswerScore = incorrectAnswerCount > 0
				? Integer.valueOf(-100 / incorrectAnswerCount).floatValue()
				: null;

			for (QbOption assessmentAnswer : qbQuestion.getQbOptions()) {
			    Answer answer = new Answer();
			    boolean isCorrectAnswer = assessmentAnswer.getMaxMark() > 0;

			    answer.setText(assessmentAnswer.getName());
			    answer.setScore(isCorrectAnswer ? correctAnswerScore : incorrectAnswerScore);
			    answer.setFeedback(isCorrectAnswer ? qbQuestion.getFeedbackOnCorrect()
				    : qbQuestion.getFeedbackOnIncorrect());

			    answers.add(answer);
			}

		    } else {
			question.setType(Question.QUESTION_TYPE_MULTIPLE_CHOICE);

			for (QbOption qbOption : qbQuestion.getQbOptions()) {
			    Answer answer = new Answer();
			    boolean isCorrectAnswer = qbOption.getMaxMark() == 1F;

			    answer.setText(qbOption.getName());
			    answer.setScore(
				    isCorrectAnswer ? Integer.valueOf(qbQuestion.getMaxMark()).floatValue() : 0);
			    answer.setFeedback(isCorrectAnswer ? qbQuestion.getFeedbackOnCorrect()
				    : qbQuestion.getFeedbackOnIncorrect());

			    answers.add(answer);
			}
		    }
		    break;

		case QbQuestion.TYPE_VERY_SHORT_ANSWERS:
		    question.setType(Question.QUESTION_TYPE_FILL_IN_BLANK);

		    for (QbOption qbOption : qbQuestion.getQbOptions()) {
			Answer answer = new Answer();
			answer.setText(qbOption.getName());
			boolean isCorrectAnswer = qbOption.getMaxMark() == 1F;
			answer.setScore(isCorrectAnswer ? Integer.valueOf(qbQuestion.getMaxMark()).floatValue() : 0);

			answers.add(answer);
		    }
		    break;

		case QbQuestion.TYPE_TRUE_FALSE:
		    question.setType(Question.QUESTION_TYPE_TRUE_FALSE);
		    boolean isTrueCorrect = qbQuestion.getCorrectAnswer();

		    // true/false question is basically the same for QTI, just with special answers
		    Answer trueAnswer = new Answer();
		    trueAnswer.setText("True");
		    trueAnswer.setScore(isTrueCorrect ? Integer.valueOf(qbQuestion.getMaxMark()).floatValue() : 0);
		    trueAnswer.setFeedback(
			    isTrueCorrect ? qbQuestion.getFeedbackOnCorrect() : qbQuestion.getFeedbackOnIncorrect());
		    answers.add(trueAnswer);

		    Answer falseAnswer = new Answer();
		    falseAnswer.setText("False");
		    falseAnswer.setScore(!isTrueCorrect ? Integer.valueOf(qbQuestion.getMaxMark()).floatValue() : 0);
		    falseAnswer.setFeedback(
			    !isTrueCorrect ? qbQuestion.getFeedbackOnCorrect() : qbQuestion.getFeedbackOnIncorrect());
		    answers.add(falseAnswer);
		    break;

		case QbQuestion.TYPE_MATCHING_PAIRS:
		    question.setType(Question.QUESTION_TYPE_MATCHING);

		    int answerIndex = 0;
		    float score = qbQuestion.getMaxMark() / qbQuestion.getQbOptions().size();
		    question.setMatchAnswers(new ArrayList<Answer>(qbQuestion.getQbOptions().size()));
		    question.setMatchMap(new TreeMap<Integer, Integer>());
		    for (QbOption assessmentAnswer : qbQuestion.getQbOptions()) {
			Answer answer = new Answer();

			answer.setText(assessmentAnswer.getMatchingPair());
			answer.setScore(score);
			answer.setFeedback(assessmentAnswer.getFeedback());
			answers.add(answer);

			Answer matchingAnswer = new Answer();
			matchingAnswer.setText(assessmentAnswer.getName());
			question.getMatchAnswers().add(matchingAnswer);
			question.getMatchMap().put(answerIndex, answerIndex);
			answerIndex++;
		    }

		    break;

		case QbQuestion.TYPE_ESSAY:
		    // not much to do with essay
		    question.setType(Question.QUESTION_TYPE_ESSAY);
		    answers = null;
		    break;

		case QbQuestion.TYPE_MARK_HEDGING:

		    question.setType(Question.QUESTION_TYPE_MARK_HEDGING);

		    for (QbOption assessmentAnswer : qbQuestion.getQbOptions()) {
			Answer answer = new Answer();
			boolean isCorrectAnswer = assessmentAnswer.isCorrect();

			answer.setText(assessmentAnswer.getName());
			answer.setScore(isCorrectAnswer ? Integer.valueOf(qbQuestion.getMaxMark()).floatValue() : 0);
			answer.setFeedback(isCorrectAnswer ? qbQuestion.getFeedbackOnCorrect()
				: qbQuestion.getFeedbackOnIncorrect());

			answers.add(answer);
		    }
		    break;

		default:
		    continue;
	    }

	    question.setTitle(qbQuestion.getName());
	    if (qbQuestion.getUuid() != null) {
		// UUID in QTI question label is LAMS custom idea
		question.setLabel(QuestionParser.UUID_LABEL_PREFIX + qbQuestion.getUuid());
	    }
	    question.setText(qbQuestion.getDescription());
	    question.setFeedback(qbQuestion.getFeedback());
	    question.setAnswers(answers);

	    questions.add(question);
	}

	QuestionExporter exporter = new QuestionExporter(fileTitle, questions.toArray(Question.QUESTION_ARRAY_TYPE));
	exporter.exportQTIPackage(request, response);
    }

    private static UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}