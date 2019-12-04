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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionExporter;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
     * Parses questions extracted from IMS QTI file and adds them as new QB questions.
     */
    @RequestMapping(path = "/saveQTI", produces = "text/plain")
    @ResponseBody
    public String saveQTI(HttpServletRequest request, @RequestParam long collectionUid,
	    @RequestParam(defaultValue = "") String contentFolderID) throws UnsupportedEncodingException {

	Question[] questions = QuestionParser.parseQuestionChoiceForm(request);
	Set<String> collectionUUIDs = null;
	StringBuilder qbQuestionUidsString = new StringBuilder();

	for (Question question : questions) {

	    String uuid = question.getQbUUID();

	    // try to match the question to an existing QB question in DB
	    if (uuid != null) {
		QbQuestion qbQuestion = qbService.getQuestionByUUID(UUID.fromString(uuid));
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
			    log.debug("Skipping an existing question. Name: " + qbQuestion.getName() + ", uid: "
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
		    continue;
		}
	    }

	    QbQuestion qbQuestion = new QbQuestion();
	    qbQuestion.setUuid(uuid);
	    qbQuestion.setName(question.getTitle());
	    qbQuestion.setDescription(QuestionParser.processHTMLField(question.getText(), false, contentFolderID,
		    question.getResourcesFolderPath()));
	    qbQuestion.setFeedback(QuestionParser.processHTMLField(question.getFeedback(), false, contentFolderID,
		    question.getResourcesFolderPath()));
	    qbQuestion.setPenaltyFactor(0);
	    int questionId = qbService.generateNextQuestionId();
	    qbQuestion.setQuestionId(questionId);
	    qbQuestion.setVersion(1);

	    int questionMark = 1;

	    // options are different depending on the type
	    if (Question.QUESTION_TYPE_MULTIPLE_CHOICE.equals(question.getType())
		    || Question.QUESTION_TYPE_FILL_IN_BLANK.equals(question.getType())
		    || Question.QUESTION_TYPE_MARK_HEDGING.equals(question.getType())) {
		boolean isMultipleChoice = Question.QUESTION_TYPE_MULTIPLE_CHOICE.equals(question.getType());
		boolean isMarkHedgingType = Question.QUESTION_TYPE_MARK_HEDGING.equals(question.getType());

		// setting answers is very similar in both types, so they were put together here
		if (isMarkHedgingType) {
		    qbQuestion.setType(QbQuestion.TYPE_MARK_HEDGING);

		} else if (isMultipleChoice) {
		    qbQuestion.setType(QbQuestion.TYPE_MULTIPLE_CHOICE);
		    qbQuestion.setMultipleAnswersAllowed(false);
		    qbQuestion.setShuffle(false);
		    qbQuestion.setPrefixAnswersWithLetters(false);

		} else {
		    qbQuestion.setType(QbQuestion.TYPE_VERY_SHORT_ANSWERS);
		    qbQuestion.setCaseSensitive(false);
		}

		String correctAnswer = null;
		if (question.getAnswers() != null) {
		    TreeSet<QbOption> optionList = new TreeSet<>();
		    int orderId = 0;
		    for (Answer answer : question.getAnswers()) {
			String answerText = QuestionParser.processHTMLField(answer.getText(), false, contentFolderID,
				question.getResourcesFolderPath());
			if ((correctAnswer != null) && correctAnswer.equals(answerText)) {
			    log.warn("Skipping an answer with same text as the correct answer: " + answerText);
			    continue;
			}
			QbOption option = new QbOption();
			option.setName(answerText);
			option.setDisplayOrder(orderId++);
			option.setFeedback(answer.getFeedback());
			option.setQbQuestion(qbQuestion);

			if ((answer.getScore() != null) && (answer.getScore() > 0) && (correctAnswer == null)) {
			    // whatever the correct answer holds, it becomes the question score
			    questionMark = Double.valueOf(Math.ceil(answer.getScore())).intValue();
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
		    questionMark = Double.valueOf(Math.round(totalScore)).intValue();

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
			    questionMark = Double.valueOf(Math.ceil(answer.getScore())).intValue();
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
		    questionMark = Double.valueOf(Math.round(totalScore)).intValue();

		    TreeSet<QbOption> optionList = new TreeSet<>();
		    int orderId = 1;
		    for (int answerIndex = 0; answerIndex < question.getAnswers().size(); answerIndex++) {
			// QTI allows answers without a match, but LAMS assessment tool does not
			Integer matchAnswerIndex = question.getMatchMap() == null ? null
				: question.getMatchMap().get(answerIndex);
			Answer matchAnswer = (matchAnswerIndex == null) || (question.getMatchAnswers() == null) ? null
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

	    } else if (Question.QUESTION_TYPE_ESSAY.equals(question.getType())) {
		qbQuestion.setType(QbQuestion.TYPE_MULTIPLE_CHOICE);
		qbQuestion.setShuffle(false);
		qbQuestion.setPrefixAnswersWithLetters(false);

		String correctAnswer = null;
		if (question.getAnswers() != null) {
		    TreeSet<QbOption> optionList = new TreeSet<>();
		    int orderId = 1;
		    for (Answer answer : question.getAnswers()) {
			String answerText = QuestionParser.processHTMLField(answer.getText(), false, contentFolderID,
				question.getResourcesFolderPath());
			if ((correctAnswer != null) && correctAnswer.equals(answerText)) {
			    log.warn("Skipping an answer with same text as the correct answer: " + answerText);
			    continue;
			}
			QbOption option = new QbOption();
			option.setName(answerText);
			option.setDisplayOrder(orderId++);
			option.setFeedback(answer.getFeedback());
			option.setQbQuestion(qbQuestion);

			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // for fill in blanks question all answers are correct and get full mark
			    if (correctAnswer == null) {
				// whatever the correct answer holds, it becomes the question score
				questionMark = Double.valueOf(Math.ceil(answer.getScore())).intValue();
				// 100% goes to the correct answer
				option.setMaxMark(1);
				correctAnswer = answerText;
			    } else {
				log.warn("Choosing only first correct answer, despite another one was found: "
					+ answerText);
				option.setMaxMark(0);
			    }
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

	    } else {
		log.warn("Unknow QTI question type: " + question.getType());
		continue;
	    }

	    qbQuestion.setMaxMark(questionMark);
	    userManagementService.save(qbQuestion);

	    qbService.addQuestionToCollection(collectionUid, qbQuestion.getQuestionId(), false);

	    qbQuestionUidsString.append(qbQuestion.getQuestionId()).append(',');

	    if (log.isDebugEnabled()) {
		log.debug("Imported QTI question. Name: " + qbQuestion.getName() + ", uid: " + qbQuestion.getUid());
	    }
	}

	String qbQuestionUids = null;
	if (qbQuestionUidsString.length() > 0 && qbQuestionUidsString.charAt(qbQuestionUidsString.length() - 1) == ',') {
	    qbQuestionUids = qbQuestionUidsString.substring(0, qbQuestionUidsString.length() - 1);
	} else {
	    qbQuestionUids = qbQuestionUidsString.toString();
	}
	return qbQuestionUids;
    }

    /**
     * Exports QB question as IMS QTI package.
     */
    @RequestMapping("/exportQuestionAsQTI")
    public String exportQuestionAsQTI(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam long qbQuestionUid) {
	QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);
	List<QbQuestion> qbQuestions = new LinkedList<>();
	qbQuestions.add(qbQuestion);

	String fileTitle = qbQuestion.getName();
	exportQTI(request, response, qbQuestions, fileTitle);
	return null;
    }

    /**
     * Exports all questions from QB Collection as IMS QTI package.
     */
    @RequestMapping("/exportCollectionAsQTI")
    public String exportCollectionAsQTI(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam long collectionUid) {
	List<QbQuestion> qbQuestions = qbService.getCollectionQuestions(collectionUid);

	QbCollection collection = qbService.getCollectionByUid(collectionUid);
	String fileTitle = collection.getName();

	exportQTI(request, response, qbQuestions, fileTitle);
	return null;
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
}
