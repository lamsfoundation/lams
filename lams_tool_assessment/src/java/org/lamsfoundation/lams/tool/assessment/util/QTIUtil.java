package org.lamsfoundation.lams.tool.assessment.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;

public class QTIUtil {
    private static Logger log = Logger.getLogger(QTIUtil.class);

    public static List<Question> exportQTI(SortedSet<AssessmentQuestion> questionList) {
	List<Question> questions = new LinkedList<>();
	for (AssessmentQuestion assessmentQuestion : questionList) {
	    Question question = new Question();
	    List<Answer> answers = new ArrayList<>();

	    switch (assessmentQuestion.getType()) {

		case AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE:

		    if (assessmentQuestion.isMultipleAnswersAllowed()) {
			question.setType(Question.QUESTION_TYPE_MULTIPLE_RESPONSE);
			int correctAnswerCount = 0;

			for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			    if (assessmentAnswer.getGrade() > 0) {
				correctAnswerCount++;
			    }
			}

			Float correctAnswerScore = correctAnswerCount > 0
				? Integer.valueOf(100 / correctAnswerCount).floatValue()
				: null;
			int incorrectAnswerCount = assessmentQuestion.getOptions().size() - correctAnswerCount;
			Float incorrectAnswerScore = incorrectAnswerCount > 0
				? Integer.valueOf(-100 / incorrectAnswerCount).floatValue()
				: null;

			for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			    Answer answer = new Answer();
			    boolean isCorrectAnswer = assessmentAnswer.getGrade() > 0;

			    answer.setText(assessmentAnswer.getOptionString());
			    answer.setScore(isCorrectAnswer ? correctAnswerScore : incorrectAnswerScore);
			    answer.setFeedback(isCorrectAnswer ? assessmentQuestion.getFeedbackOnCorrect()
				    : assessmentQuestion.getFeedbackOnIncorrect());

			    answers.add(assessmentAnswer.getSequenceId(), answer);
			}

		    } else {
			question.setType(Question.QUESTION_TYPE_MULTIPLE_CHOICE);

			for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			    Answer answer = new Answer();
			    boolean isCorrectAnswer = assessmentAnswer.getGrade() == 1F;

			    answer.setText(assessmentAnswer.getOptionString());
			    answer.setScore(
				    isCorrectAnswer ? Integer.valueOf(assessmentQuestion.getDefaultGrade()).floatValue()
					    : 0);
			    answer.setFeedback(isCorrectAnswer ? assessmentQuestion.getFeedbackOnCorrect()
				    : assessmentQuestion.getFeedbackOnIncorrect());

			    answers.add(assessmentAnswer.getSequenceId(), answer);
			}
		    }
		    break;

		case AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER:
		    question.setType(Question.QUESTION_TYPE_FILL_IN_BLANK);

		    for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			// only answer which has more than 0% is considered a correct one
			if (assessmentAnswer.getGrade() > 0) {
			    Answer answer = new Answer();
			    answer.setText(assessmentAnswer.getOptionString());
			    answer.setScore(Integer.valueOf(assessmentQuestion.getDefaultGrade()).floatValue());

			    answers.add(answer);
			}
		    }
		    break;

		case AssessmentConstants.QUESTION_TYPE_TRUE_FALSE:
		    question.setType(Question.QUESTION_TYPE_TRUE_FALSE);
		    boolean isTrueCorrect = assessmentQuestion.getCorrectAnswer();

		    // true/false question is basically the same for QTI, just with special answers
		    Answer trueAnswer = new Answer();
		    trueAnswer.setText("True");
		    trueAnswer.setScore(
			    isTrueCorrect ? Integer.valueOf(assessmentQuestion.getDefaultGrade()).floatValue() : 0);
		    trueAnswer.setFeedback(isTrueCorrect ? assessmentQuestion.getFeedbackOnCorrect()
			    : assessmentQuestion.getFeedbackOnIncorrect());
		    answers.add(trueAnswer);

		    Answer falseAnswer = new Answer();
		    falseAnswer.setText("False");
		    falseAnswer.setScore(
			    !isTrueCorrect ? Integer.valueOf(assessmentQuestion.getDefaultGrade()).floatValue() : 0);
		    falseAnswer.setFeedback(!isTrueCorrect ? assessmentQuestion.getFeedbackOnCorrect()
			    : assessmentQuestion.getFeedbackOnIncorrect());
		    answers.add(falseAnswer);
		    break;

		case AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS:
		    question.setType(Question.QUESTION_TYPE_MATCHING);

		    int answerIndex = 0;
		    float score = assessmentQuestion.getDefaultGrade() / assessmentQuestion.getOptions().size();
		    question.setMatchAnswers(new ArrayList<Answer>(assessmentQuestion.getOptions().size()));
		    question.setMatchMap(new TreeMap<Integer, Integer>());
		    for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			Answer answer = new Answer();

			answer.setText(assessmentAnswer.getQuestion());
			answer.setScore(score);
			answer.setFeedback(assessmentAnswer.getFeedback());
			answers.add(answer);

			Answer matchingAnswer = new Answer();
			matchingAnswer.setText(assessmentAnswer.getOptionString());
			question.getMatchAnswers().add(matchingAnswer);
			question.getMatchMap().put(answerIndex, answerIndex);
			answerIndex++;
		    }

		    break;

		case AssessmentConstants.QUESTION_TYPE_ESSAY:
		    // not much to do with essay
		    question.setType(Question.QUESTION_TYPE_ESSAY);
		    answers = null;
		    break;

		case AssessmentConstants.QUESTION_TYPE_MARK_HEDGING:

		    question.setType(Question.QUESTION_TYPE_MARK_HEDGING);

		    for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			Answer answer = new Answer();
			boolean isCorrectAnswer = assessmentAnswer.isCorrect();

			answer.setText(assessmentAnswer.getOptionString());
			answer.setScore(
				isCorrectAnswer ? Integer.valueOf(assessmentQuestion.getDefaultGrade()).floatValue() : 0);
			answer.setFeedback(isCorrectAnswer ? assessmentQuestion.getFeedbackOnCorrect()
				: assessmentQuestion.getFeedbackOnIncorrect());

			answers.add(assessmentAnswer.getSequenceId(), answer);
		    }
		    break;

		default:
		    continue;
	    }

	    question.setTitle(assessmentQuestion.getTitle());
	    question.setText(assessmentQuestion.getQuestion());
	    question.setFeedback(assessmentQuestion.getGeneralFeedback());
	    question.setAnswers(answers);

	    questions.add(question);
	}
	
	return questions;
    }

    public static void saveQTI(HttpServletRequest request, SortedSet<AssessmentQuestion> questionList,
	    String contentFolderID) throws UnsupportedEncodingException {
	Question[] questions = QuestionParser.parseQuestionChoiceForm(request);
	for (Question question : questions) {
	    AssessmentQuestion assessmentQuestion = new AssessmentQuestion();
	    int maxSeq = 0;
	    if ((questionList != null) && (questionList.size() > 0)) {
		AssessmentQuestion last = questionList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    assessmentQuestion.setSequenceId(maxSeq);
	    assessmentQuestion.setTitle(question.getTitle());
	    assessmentQuestion.setQuestion(QuestionParser.processHTMLField(question.getText(), false, contentFolderID,
		    question.getResourcesFolderPath()));
	    assessmentQuestion.setGeneralFeedback(QuestionParser.processHTMLField(question.getFeedback(), false,
		    contentFolderID, question.getResourcesFolderPath()));
	    assessmentQuestion.setPenaltyFactor(0);

	    int questionGrade = 1;

	    // options are different depending on the type
	    if (Question.QUESTION_TYPE_MULTIPLE_CHOICE.equals(question.getType())
		    || Question.QUESTION_TYPE_FILL_IN_BLANK.equals(question.getType())
		    || Question.QUESTION_TYPE_MARK_HEDGING.equals(question.getType())) {
		boolean isMultipleChoice = Question.QUESTION_TYPE_MULTIPLE_CHOICE.equals(question.getType());
		boolean isMarkHedgingType = Question.QUESTION_TYPE_MARK_HEDGING.equals(question.getType());

		// setting answers is very similar in both types, so they were put together here
		if (isMarkHedgingType) {
		    assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_MARK_HEDGING);

		} else if (isMultipleChoice) {
		    assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE);
		    assessmentQuestion.setMultipleAnswersAllowed(false);
		    assessmentQuestion.setShuffle(false);
		    assessmentQuestion.setPrefixAnswersWithLetters(false);

		} else {
		    assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER);
		    assessmentQuestion.setCaseSensitive(false);
		}

		String correctAnswer = null;
		if (question.getAnswers() != null) {
		    TreeSet<AssessmentQuestionOption> optionList = new TreeSet<>(new SequencableComparator());
		    int orderId = 0;
		    for (Answer answer : question.getAnswers()) {
			String answerText = QuestionParser.processHTMLField(answer.getText(), false, contentFolderID,
				question.getResourcesFolderPath());
			if ((correctAnswer != null) && correctAnswer.equals(answerText)) {
			    log.warn("Skipping an answer with same text as the correct answer: " + answerText);
			    continue;
			}
			AssessmentQuestionOption assessmentAnswer = new AssessmentQuestionOption();
			assessmentAnswer.setOptionString(answerText);
			assessmentAnswer.setSequenceId(orderId++);
			assessmentAnswer.setFeedback(answer.getFeedback());

			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // for fill in blanks question all answers are correct and get full grade
			    if (!isMultipleChoice && !isMarkHedgingType || correctAnswer == null) {
				// whatever the correct answer holds, it becomes the question score
				questionGrade = Double.valueOf(Math.ceil(answer.getScore())).intValue();
				// 100% goes to the correct answer
				assessmentAnswer.setGrade(1);
				correctAnswer = answerText;
			    } else {
				log.warn("Choosing only first correct answer, despite another one was found: "
					+ answerText);
				assessmentAnswer.setGrade(0);
			    }
			} else {
			    assessmentAnswer.setGrade(0);
			}

			optionList.add(assessmentAnswer);
		    }

		    assessmentQuestion.setOptions(optionList);
		}

		if (correctAnswer == null) {
		    log.warn("No correct answer found for question: " + question.getText());
		    continue;
		}

	    } else if (Question.QUESTION_TYPE_MULTIPLE_RESPONSE.equals(question.getType())) {
		assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE);
		assessmentQuestion.setMultipleAnswersAllowed(true);
		assessmentQuestion.setShuffle(false);
		assessmentQuestion.setPrefixAnswersWithLetters(false);

		if (question.getAnswers() != null) {
		    float totalScore = 0;
		    for (Answer answer : question.getAnswers()) {
			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // the question score information is stored as sum of answer scores
			    totalScore += answer.getScore();
			}
		    }
		    questionGrade = Double.valueOf(Math.round(totalScore)).intValue();

		    TreeSet<AssessmentQuestionOption> optionList = new TreeSet<>(new SequencableComparator());
		    int orderId = 1;
		    for (Answer answer : question.getAnswers()) {
			String answerText = answer.getText();
			AssessmentQuestionOption assessmentAnswer = new AssessmentQuestionOption();
			assessmentAnswer.setOptionString(answerText);
			assessmentAnswer.setSequenceId(orderId++);
			assessmentAnswer.setFeedback(answer.getFeedback());

			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // set the factor of score for correct answers
			    assessmentAnswer.setGrade(answer.getScore() / totalScore);
			} else {
			    assessmentAnswer.setGrade(0);
			}

			optionList.add(assessmentAnswer);
		    }

		    assessmentQuestion.setOptions(optionList);
		}

	    } else if (Question.QUESTION_TYPE_TRUE_FALSE.equals(question.getType())) {
		assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_TRUE_FALSE);

		if (question.getAnswers() == null) {
		    log.warn("Answers missing from true-false question: " + question.getText());
		    continue;
		} else {
		    for (Answer answer : question.getAnswers()) {
			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    assessmentQuestion.setCorrectAnswer(Boolean.parseBoolean(answer.getText()));
			    questionGrade = Double.valueOf(Math.ceil(answer.getScore())).intValue();
			}
			if (!StringUtils.isBlank(answer.getFeedback())) {
			    // set feedback for true/false answers
			    if (Boolean.parseBoolean(answer.getText())) {
				assessmentQuestion.setFeedbackOnCorrect(answer.getFeedback());
			    } else {
				assessmentQuestion.setFeedbackOnIncorrect(answer.getFeedback());
			    }
			}
		    }
		}
	    } else if (Question.QUESTION_TYPE_MATCHING.equals(question.getType())) {
		assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS);
		assessmentQuestion.setShuffle(true);

		if (question.getAnswers() != null) {
		    // the question score information is stored as sum of answer scores
		    float totalScore = 0;
		    for (Answer answer : question.getAnswers()) {
			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    totalScore += answer.getScore();
			}
		    }
		    questionGrade = Double.valueOf(Math.round(totalScore)).intValue();

		    TreeSet<AssessmentQuestionOption> optionList = new TreeSet<>(new SequencableComparator());
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
			    AssessmentQuestionOption assessmentAnswer = new AssessmentQuestionOption();
			    assessmentAnswer.setQuestion(answerText);
			    assessmentAnswer.setOptionString(matchAnswer.getText());
			    assessmentAnswer.setSequenceId(orderId++);
			    assessmentAnswer.setFeedback(answer.getFeedback());

			    optionList.add(assessmentAnswer);
			}
		    }

		    assessmentQuestion.setOptions(optionList);
		}
	    } else if (Question.QUESTION_TYPE_ESSAY.equals(question.getType())) {
		assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_ESSAY);
		assessmentQuestion.setAllowRichEditor(false);

	    } else if (Question.QUESTION_TYPE_ESSAY.equals(question.getType())) {
		assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE);
		assessmentQuestion.setShuffle(false);
		assessmentQuestion.setPrefixAnswersWithLetters(false);

		String correctAnswer = null;
		if (question.getAnswers() != null) {
		    TreeSet<AssessmentQuestionOption> optionList = new TreeSet<>(new SequencableComparator());
		    int orderId = 1;
		    for (Answer answer : question.getAnswers()) {
			String answerText = QuestionParser.processHTMLField(answer.getText(), false, contentFolderID,
				question.getResourcesFolderPath());
			if ((correctAnswer != null) && correctAnswer.equals(answerText)) {
			    log.warn("Skipping an answer with same text as the correct answer: " + answerText);
			    continue;
			}
			AssessmentQuestionOption assessmentAnswer = new AssessmentQuestionOption();
			assessmentAnswer.setOptionString(answerText);
			assessmentAnswer.setSequenceId(orderId++);
			assessmentAnswer.setFeedback(answer.getFeedback());

			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // for fill in blanks question all answers are correct and get full grade
			    if (correctAnswer == null) {
				// whatever the correct answer holds, it becomes the question score
				questionGrade = Double.valueOf(Math.ceil(answer.getScore())).intValue();
				// 100% goes to the correct answer
				assessmentAnswer.setGrade(1);
				correctAnswer = answerText;
			    } else {
				log.warn("Choosing only first correct answer, despite another one was found: "
					+ answerText);
				assessmentAnswer.setGrade(0);
			    }
			} else {
			    assessmentAnswer.setGrade(0);
			}

			optionList.add(assessmentAnswer);
		    }

		    assessmentQuestion.setOptions(optionList);
		}

		if (correctAnswer == null) {
		    log.warn("No correct answer found for question: " + question.getText());
		    continue;
		}

	    } else {
		log.warn("Unknow QTI question type: " + question.getType());
		continue;
	    }

	    assessmentQuestion.setDefaultGrade(questionGrade);

	    questionList.add(assessmentQuestion);
	    if (log.isDebugEnabled()) {
		log.debug("Added question: " + assessmentQuestion.getTitle());
	    }
	}
    }
}
