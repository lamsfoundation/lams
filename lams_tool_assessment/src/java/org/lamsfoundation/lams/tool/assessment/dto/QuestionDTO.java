package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestionUnit;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;

public class QuestionDTO implements Comparable<QuestionDTO>{

    // ============= immutable properties copied from AssessmentQuestion question =============

    private Long uid;
    
    private Long qbQuestionUid;

    private Integer type;

    private String title;

    private String question;

    private int displayOrder;

    private int maxMark;

    private float penaltyFactor;

    private boolean answerRequired;

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

    private List<QbQuestionUnit> units;

    private int maxWordsLimit;

    private int minWordsLimit;

    private boolean hedgingJustificationEnabled;

    private boolean correctAnswersDisclosed;

    private boolean groupsAnswersDisclosed;

    // ============= variable properties =============
    
    private String titleEscaped;

    private String answerString;

    private float answerFloat;

    private boolean answerBoolean;

    private String questionFeedback;

    private boolean responseSubmitted;

    private float mark;

    private float penalty;

    private float optionMaxMark;

    private Set<OptionDTO> optionDtos;

    private Set<OptionDTO> matchingPairOptions;

    private List<Object[]> questionResults;

    private int confidenceLevel;

    public QuestionDTO(AssessmentQuestion question) {
	this.uid = question.getUid();
	this.type = question.getQbQuestion().getType();
	this.title = question.getQbQuestion().getName();
	this.question = question.getQbQuestion().getDescription();
	this.displayOrder = question.getDisplayOrder();
	this.maxMark = question.getQbQuestion().getMaxMark();
	this.penaltyFactor = question.getQbQuestion().getPenaltyFactor();
	this.answerRequired = question.getQbQuestion().isAnswerRequired();
	this.feedback = question.getQbQuestion().getFeedback();
	this.multipleAnswersAllowed = question.getQbQuestion().isMultipleAnswersAllowed();
	this.incorrectAnswerNullifiesMark = question.getQbQuestion().isIncorrectAnswerNullifiesMark();
	this.feedbackOnCorrect = question.getQbQuestion().getFeedbackOnCorrect();
	this.feedbackOnPartiallyCorrect = question.getQbQuestion().getFeedbackOnPartiallyCorrect();
	this.feedbackOnIncorrect = question.getQbQuestion().getFeedbackOnIncorrect();
	this.shuffle = question.getQbQuestion().isShuffle();
	this.prefixAnswersWithLetters = question.getQbQuestion().isPrefixAnswersWithLetters();
	this.caseSensitive = question.getQbQuestion().isCaseSensitive();
	this.correctAnswer = question.getQbQuestion().getCorrectAnswer();
	this.allowRichEditor = question.getQbQuestion().isAllowRichEditor();
	this.units = question.getQbQuestion().getUnits();
	this.maxWordsLimit = question.getQbQuestion().getMaxWordsLimit();
	this.minWordsLimit = question.getQbQuestion().getMinWordsLimit();
	this.hedgingJustificationEnabled = question.getQbQuestion().isHedgingJustificationEnabled();
	this.correctAnswersDisclosed = question.isCorrectAnswersDisclosed();
	this.groupsAnswersDisclosed = question.isGroupsAnswersDisclosed();

	optionDtos = new TreeSet<>();
	for (QbOption option : question.getQbQuestion().getQbOptions()) {
	    optionDtos.add(new OptionDTO(option));
	}
    }
    
    @Override
    public int compareTo(QuestionDTO anotherQuestion) {
	return displayOrder - anotherQuestion.getDisplayOrder();
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long userID) {
	this.uid = userID;
    }
    
    public Long getQbQuestionUid() {
	return qbQuestionUid;
    }

    public void setQbQuestionUid(Long qaQuestionId) {
	this.qbQuestionUid = qaQuestionId;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
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

    public int getDisplayOrder() {
	return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }

    public int getMaxMark() {
	return maxMark;
    }

    public void setMaxMark(int maxMark) {
	this.maxMark = maxMark;
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

    public List<QbQuestionUnit> getUnits() {
	return units;
    }

    public void setUnits(List<QbQuestionUnit> units) {
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

    public float getOptionMaxMark() {
	return optionMaxMark;
    }

    public void setOptionMaxMark(float optionMaxMark) {
	this.optionMaxMark = optionMaxMark;
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
    
    public String getTitleEscaped() {
	return titleEscaped;
    }

    public void setTitleEscaped(String titleEscaped) {
	this.titleEscaped = titleEscaped;
    }
}
