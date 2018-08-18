package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUnit;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;

public class QuestionDTO {

    // ============= immutable properties copied from AssessmentQuestion question =============

    private Long uid;

    private short type;

    private String title;

    private String question;

    private int sequenceId;

    private int defaultGrade;

    private float penaltyFactor;

    private boolean answerRequired;

    private String generalFeedback;

    private String feedback;

    private boolean multipleAnswersAllowed;

    private boolean incorrectAnswerNullifiesMark;

    private String feedbackOnCorrect;

    private String feedbackOnPartiallyCorrect;

    private String feedbackOnIncorrect;

    // only one of shuffle and prefixAnswersWithLetters should be on. Both may be off
    private boolean shuffle;
    private boolean prefixAnswersWithLetters;

    private boolean caseSensitive;

    private boolean correctAnswer;

    private boolean allowRichEditor;

    private Set<AssessmentUnit> units;

    private int maxWordsLimit;

    private int minWordsLimit;

    private boolean hedgingJustificationEnabled;

    private boolean correctAnswersDisclosed;

    private boolean groupsAnswersDisclosed;

    // ============= variable properties =============

    private String answerString;

    private float answerFloat;

    private boolean answerBoolean;

    private String questionFeedback;

    private boolean responseSubmitted;

    private int grade;

    private float mark;

    private float penalty;

    private float answerTotalGrade;

    private Set<OptionDTO> optionDtos;

    private Set<OptionDTO> matchingPairOptions;

    private List<Object[]> questionResults;

    private int confidenceLevel;

    public QuestionDTO(AssessmentQuestion question) {
	this.uid = question.getUid();
	this.type = question.getType();
	this.title = question.getTitle();
	this.question = question.getQuestion();
	this.sequenceId = question.getSequenceId();
	this.defaultGrade = question.getDefaultGrade();
	this.penaltyFactor = question.getPenaltyFactor();
	this.answerRequired = question.isAnswerRequired();
	this.generalFeedback = question.getGeneralFeedback();
	this.feedback = question.getFeedback();
	this.multipleAnswersAllowed = question.isMultipleAnswersAllowed();
	this.incorrectAnswerNullifiesMark = question.isIncorrectAnswerNullifiesMark();
	this.feedbackOnCorrect = question.getFeedbackOnCorrect();
	this.feedbackOnPartiallyCorrect = question.getFeedbackOnPartiallyCorrect();
	this.feedbackOnIncorrect = question.getFeedbackOnIncorrect();
	this.shuffle = question.isShuffle();
	this.prefixAnswersWithLetters = question.isPrefixAnswersWithLetters();
	this.caseSensitive = question.isCaseSensitive();
	this.correctAnswer = question.getCorrectAnswer();
	this.allowRichEditor = question.isAllowRichEditor();
	this.units = question.getUnits();
	this.maxWordsLimit = question.getMaxWordsLimit();
	this.minWordsLimit = question.getMinWordsLimit();
	this.hedgingJustificationEnabled = question.isHedgingJustificationEnabled();
	this.correctAnswersDisclosed = question.isCorrectAnswersDisclosed();
	this.groupsAnswersDisclosed = question.isGroupsAnswersDisclosed();

	optionDtos = new TreeSet<OptionDTO>(new SequencableComparator());
	for (AssessmentQuestionOption option : question.getOptions()) {
	    optionDtos.add(new OptionDTO(option));
	}
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long userID) {
	this.uid = userID;
    }

    public short getType() {
	return type;
    }

    public void setType(short type) {
	this.type = type;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getQuestion() {
	return question;
    }

    public void setQuestion(String question) {
	this.question = question;
    }

    public int getSequenceId() {
	return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    public int getDefaultGrade() {
	return defaultGrade;
    }

    public void setDefaultGrade(int defaultGrade) {
	this.defaultGrade = defaultGrade;
    }

    public float getPenaltyFactor() {
	return penaltyFactor;
    }

    public void setPenaltyFactor(float penaltyFactor) {
	this.penaltyFactor = penaltyFactor;
    }

    public boolean isAnswerRequired() {
	return answerRequired;
    }

    public void setAnswerRequired(boolean answerRequired) {
	this.answerRequired = answerRequired;
    }

    public String getGeneralFeedback() {
	return generalFeedback;
    }

    public void setGeneralFeedback(String generalFeedback) {
	this.generalFeedback = generalFeedback;
    }

    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    public boolean isMultipleAnswersAllowed() {
	return multipleAnswersAllowed;
    }

    public void setMultipleAnswersAllowed(boolean multipleAnswersAllowed) {
	this.multipleAnswersAllowed = multipleAnswersAllowed;
    }

    public boolean isIncorrectAnswerNullifiesMark() {
	return incorrectAnswerNullifiesMark;
    }

    public void setIncorrectAnswerNullifiesMark(boolean incorrectAnswerNullifiesMark) {
	this.incorrectAnswerNullifiesMark = incorrectAnswerNullifiesMark;
    }

    public String getFeedbackOnCorrect() {
	return feedbackOnCorrect;
    }

    public void setFeedbackOnCorrect(String feedbackOnCorrect) {
	this.feedbackOnCorrect = feedbackOnCorrect;
    }

    public String getFeedbackOnPartiallyCorrect() {
	return feedbackOnPartiallyCorrect;
    }

    public void setFeedbackOnPartiallyCorrect(String feedbackOnPartiallyCorrect) {
	this.feedbackOnPartiallyCorrect = feedbackOnPartiallyCorrect;
    }

    public String getFeedbackOnIncorrect() {
	return feedbackOnIncorrect;
    }

    public void setFeedbackOnIncorrect(String feedbackOnIncorrect) {
	this.feedbackOnIncorrect = feedbackOnIncorrect;
    }

    public boolean isShuffle() {
	return shuffle;
    }

    public void setShuffle(boolean shuffle) {
	this.shuffle = shuffle;
    }

    public boolean isCaseSensitive() {
	return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
	this.caseSensitive = caseSensitive;
    }

    public boolean getCorrectAnswer() {
	return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
	this.correctAnswer = correctAnswer;
    }

    public boolean isAllowRichEditor() {
	return allowRichEditor;
    }

    public void setAllowRichEditor(boolean allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    public Set<AssessmentUnit> getUnits() {
	return units;
    }

    public void setUnits(Set<AssessmentUnit> units) {
	this.units = units;
    }

    public int getMaxWordsLimit() {
	return maxWordsLimit;
    }

    public void setMaxWordsLimit(int maxWordsLimit) {
	this.maxWordsLimit = maxWordsLimit;
    }

    public int getMinWordsLimit() {
	return minWordsLimit;
    }

    public void setMinWordsLimit(int minWordsLimit) {
	this.minWordsLimit = minWordsLimit;
    }

    public boolean isHedgingJustificationEnabled() {
	return hedgingJustificationEnabled;
    }

    public void setHedgingJustificationEnabled(boolean hedgingJustificationEnabled) {
	this.hedgingJustificationEnabled = hedgingJustificationEnabled;
    }

    public boolean isCorrectAnswersDisclosed() {
	return correctAnswersDisclosed;
    }

    public boolean isGroupsAnswersDisclosed() {
	return groupsAnswersDisclosed;
    }
    // ============= variable properties =============

    public String getAnswerString() {
	return answerString;
    }

    public void setAnswerString(String answerString) {
	this.answerString = answerString;
    }

    public float getAnswerFloat() {
	return answerFloat;
    }

    public void setAnswerFloat(float answerFloat) {
	this.answerFloat = answerFloat;
    }

    public boolean getAnswerBoolean() {
	return answerBoolean;
    }

    public void setAnswerBoolean(boolean answerBoolean) {
	this.answerBoolean = answerBoolean;
    }

    public void setQuestionFeedback(String questionFeedback) {
	this.questionFeedback = questionFeedback;
    }

    public String getQuestionFeedback() {
	return questionFeedback;
    }

    public int getGrade() {
	return grade;
    }

    public void setGrade(int grade) {
	this.grade = grade;
    }

    public Float getMark() {
	return mark;
    }

    public void setMark(Float mark) {
	this.mark = mark;
    }

    public Float getPenalty() {
	return penalty;
    }

    public void setPenalty(Float penalty) {
	this.penalty = penalty;
    }

    public Set<OptionDTO> getMatchingPairOptions() {
	return matchingPairOptions;
    }

    public void setMatchingPairOptions(Set<OptionDTO> matchingPairOptions) {
	this.matchingPairOptions = matchingPairOptions;
    }

    public List<Object[]> getQuestionResults() {
	return questionResults;
    }

    public void setQuestionResults(List<Object[]> questionResults2) {
	this.questionResults = questionResults2;
    }

    public int getConfidenceLevel() {
	return confidenceLevel;
    }

    public void setConfidenceLevel(int confidenceLevel) {
	this.confidenceLevel = confidenceLevel;
    }

    public boolean isResponseSubmitted() {
	return responseSubmitted;
    }

    public void setResponseSubmitted(boolean responseSubmitted) {
	this.responseSubmitted = responseSubmitted;
    }

    public float getAnswerTotalGrade() {
	return answerTotalGrade;
    }

    public void setAnswerTotalGrade(float answerTotalGrade) {
	this.answerTotalGrade = answerTotalGrade;
    }

    public Set<OptionDTO> getOptionDtos() {
	return optionDtos;
    }

    public void setOptionDtos(Set<OptionDTO> optionDtos) {
	this.optionDtos = optionDtos;
    }

    public boolean isPrefixAnswersWithLetters() {
	return prefixAnswersWithLetters;
    }

    public void setPrefixAnswersWithLetters(boolean prefixAnswersWithLetters) {
	this.prefixAnswersWithLetters = prefixAnswersWithLetters;
    }
}
