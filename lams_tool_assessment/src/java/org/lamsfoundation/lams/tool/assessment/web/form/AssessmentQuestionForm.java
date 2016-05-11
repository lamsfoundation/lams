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

package org.lamsfoundation.lams.tool.assessment.web.form;

import org.apache.struts.action.ActionForm;

/**
 * Assessment Question Form.
 *
 * @struts.form name="assessmentQuestionForm"
 * @author Andrey Balan
 */
public class AssessmentQuestionForm extends ActionForm {
    private static final long serialVersionUID = 4900738305713649389L;

    private String questionIndex;
    private String sessionMapID;
    private String contentFolderID;

    // tool access mode;
    private String mode;

    private short questionType;
    private String title;
    private String question;
    private String defaultGrade;
    private String penaltyFactor;
    private boolean answerRequired;
    private String generalFeedback;
    private String feedback;
    private boolean multipleAnswersAllowed;
    private boolean incorrectAnswerNullifiesMark;
    private String feedbackOnCorrect;
    private String feedbackOnPartiallyCorrect;
    private String feedbackOnIncorrect;
    private boolean shuffle;
    private boolean caseSensitive;
    private boolean correctAnswer;
    private boolean allowRichEditor;
    private int maxWordsLimit;
    private int minWordsLimit;
    private boolean hedgingJustificationEnabled;

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    public String getMode() {
	return mode;
    }

    public void setMode(String mode) {
	this.mode = mode;
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

    public String getQuestionIndex() {
	return questionIndex;
    }

    public void setQuestionIndex(String questionIndex) {
	this.questionIndex = questionIndex;
    }

    public short getQuestionType() {
	return questionType;
    }

    public void setQuestionType(short type) {
	this.questionType = type;
    }

    public String getDefaultGrade() {
	return defaultGrade;
    }

    public void setDefaultGrade(String defaultGrade) {
	this.defaultGrade = defaultGrade;
    }

    public String getPenaltyFactor() {
	return penaltyFactor;
    }

    public void setPenaltyFactor(String penaltyFactor) {
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

    public boolean isCorrectAnswer() {
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
}
