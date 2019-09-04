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

package org.lamsfoundation.lams.tool.qa.web.form;

import org.lamsfoundation.lams.tool.qa.QaAppConstants;

/**
 * @author Ozgur Demirtas
 */
public class QaLearningForm implements QaAppConstants {

    protected String answer;
    protected String currentQuestionIndex;
    protected String submitAnswersContent;
    protected String getNextQuestion;
    protected String getPreviousQuestion;
    protected String endLearning;
    protected String refreshAnswers;
    protected String submitReflection;
    protected String updateReflection;
    protected String forwardtoReflection;

    protected String totalQuestionCount;
    protected String sessionMapID;
    protected String toolSessionID;
    protected String questionIndex;
    protected String userID;
    protected String entryText;
    protected String viewAllResults;
    protected String viewAll;
    protected String redoQuestions;
    protected String responseId;

    protected String btnCombined;
    protected String btnGetPrevious;
    protected String btnDone;
    protected String btnGetNext;

    protected String requestLearningReport;
    protected String requestLearningReportProgress;
    protected String requestLearningReportViewOnly;

    /**
     * @return Returns the btnCombined.
     */
    public String getBtnCombined() {
	return btnCombined;
    }

    /**
     * @param btnCombined
     *            The btnCombined to set.
     */
    public void setBtnCombined(String btnCombined) {
	this.btnCombined = btnCombined;
    }

    /**
     * @return Returns the btnDone.
     */
    public String getBtnDone() {
	return btnDone;
    }

    /**
     * @param btnDone
     *            The btnDone to set.
     */
    public void setBtnDone(String btnDone) {
	this.btnDone = btnDone;
    }

    /**
     * @return Returns the btnGetNext.
     */
    public String getBtnGetNext() {
	return btnGetNext;
    }

    /**
     * @param btnGetNext
     *            The btnGetNext to set.
     */
    public void setBtnGetNext(String btnGetNext) {
	this.btnGetNext = btnGetNext;
    }

    /**
     * @return Returns the btnGetPrevious.
     */
    public String getBtnGetPrevious() {
	return btnGetPrevious;
    }

    /**
     * @param btnGetPrevious
     *            The btnGetPrevious to set.
     */
    public void setBtnGetPrevious(String btnGetPrevious) {
	this.btnGetPrevious = btnGetPrevious;
    }

    /**
     * reset user actions in learning mode
     *
     * @param qaAuthoringForm
     *            return void
     */

    public void resetUserActions() {
	this.getNextQuestion = null;
	this.getPreviousQuestion = null;
	this.endLearning = null;
	this.submitReflection = null;
	this.forwardtoReflection = null;
    }

    public void resetAll() {
	this.submitAnswersContent = null;
	this.getNextQuestion = null;
	this.getPreviousQuestion = null;
	this.endLearning = null;
	this.submitReflection = null;
	this.forwardtoReflection = null;

	this.viewAllResults = null;
	this.viewAll = null;
	this.redoQuestions = null;

	this.btnCombined = null;
	this.btnGetPrevious = null;
	this.btnDone = null;
	this.btnGetNext = null;

	this.requestLearningReport = null;
	this.requestLearningReportProgress = null;
	this.requestLearningReportViewOnly = null;
    }

    /**
     * @return Returns the currentQuestionIndex.
     */
    public String getCurrentQuestionIndex() {
	return currentQuestionIndex;
    }

    /**
     * @param currentQuestionIndex
     *            The currentQuestionIndex to set.
     */
    public void setCurrentQuestionIndex(String currentQuestionIndex) {
	this.currentQuestionIndex = currentQuestionIndex;
    }

    /**
     * @return Returns the answer.
     */
    public String getAnswer() {
	return answer;
    }

    /**
     * @param answer
     *            The answer to set.
     */
    public void setAnswer(String answer) {
	this.answer = answer;
    }

    /**
     * @return Returns the getNextQuestion.
     */
    public String getGetNextQuestion() {
	return getNextQuestion;
    }

    /**
     * @param getNextQuestion
     *            The getNextQuestion to set.
     */
    public void setGetNextQuestion(String getNextQuestion) {
	this.getNextQuestion = getNextQuestion;
    }

    /**
     * @return Returns the getPreviousQuestion.
     */
    public String getGetPreviousQuestion() {
	return getPreviousQuestion;
    }

    /**
     * @param getPreviousQuestion
     *            The getPreviousQuestion to set.
     */
    public void setGetPreviousQuestion(String getPreviousQuestion) {
	this.getPreviousQuestion = getPreviousQuestion;
    }

    /**
     * @return Returns the submitAnswersContent.
     */
    public String getSubmitAnswersContent() {
	return submitAnswersContent;
    }

    /**
     * @param submitAnswersContent
     *            The submitAnswersContent to set.
     */
    public void setSubmitAnswersContent(String submitAnswersContent) {
	this.submitAnswersContent = submitAnswersContent;
    }

    /**
     * @return Returns the endLearning.
     */
    public String getEndLearning() {
	return endLearning;
    }

    /**
     * @param endLearning
     *            The endLearning to set.
     */
    public void setEndLearning(String endLearning) {
	this.endLearning = endLearning;
    }

    /**
     * @return Returns the responseId.
     */
    public String getResponseId() {
	return responseId;
    }

    /**
     * @param responseId
     *            The responseId to set.
     */
    public void setResponseId(String responseId) {
	this.responseId = responseId;
    }

    /**
     * @return Returns the viewAllResults.
     */
    public String getViewAllResults() {
	return viewAllResults;
    }

    /**
     * @param viewAllResults
     *            The viewAllResults to set.
     */
    public void setViewAllResults(String viewAllResults) {
	this.viewAllResults = viewAllResults;
    }

    /**
     * @return Returns the toolSessionID.
     */
    public String getToolSessionID() {
	return toolSessionID;
    }

    /**
     * @param toolSessionID
     *            The toolSessionID to set.
     */
    public void setToolSessionID(String toolSessionID) {
	this.toolSessionID = toolSessionID;
    }

    /**
     * @return Returns the sessionMapID.
     */
    public String getSessionMapID() {
	return sessionMapID;
    }

    /**
     * @param sessionMapID
     *            The sessionMapID to set.
     */
    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    /**
     * @return Returns the userID.
     */
    public String getUserID() {
	return userID;
    }

    /**
     * @param userID
     *            The userID to set.
     */
    public void setUserID(String userID) {
	this.userID = userID;
    }

    protected String mode;

    /**
     * @return Returns the mode.
     */
    public String getMode() {
	return mode;
    }

    /**
     * @param mode
     *            The mode to set.
     */
    public void setMode(String mode) {
	this.mode = mode;
    }

    /**
     * @return Returns the questionIndex.
     */
    public String getQuestionIndex() {
	return questionIndex;
    }

    /**
     * @param questionIndex
     *            The questionIndex to set.
     */
    public void setQuestionIndex(String questionIndex) {
	this.questionIndex = questionIndex;
    }

    /**
     * @return Returns the totalQuestionCount.
     */
    public String getTotalQuestionCount() {
	return totalQuestionCount;
    }

    /**
     * @param totalQuestionCount
     *            The totalQuestionCount to set.
     */
    public void setTotalQuestionCount(String totalQuestionCount) {
	this.totalQuestionCount = totalQuestionCount;
    }

    /**
     * @return Returns the requestLearningReport.
     */
    public String getRequestLearningReport() {
	return requestLearningReport;
    }

    /**
     * @param requestLearningReport
     *            The requestLearningReport to set.
     */
    public void setRequestLearningReport(String requestLearningReport) {
	this.requestLearningReport = requestLearningReport;
    }

    /**
     * @return Returns the requestLearningReportProgress.
     */
    public String getRequestLearningReportProgress() {
	return requestLearningReportProgress;
    }

    /**
     * @param requestLearningReportProgress
     *            The requestLearningReportProgress to set.
     */
    public void setRequestLearningReportProgress(String requestLearningReportProgress) {
	this.requestLearningReportProgress = requestLearningReportProgress;
    }

    /**
     * @return Returns the requestLearningReportViewOnly.
     */
    public String getRequestLearningReportViewOnly() {
	return requestLearningReportViewOnly;
    }

    /**
     * @param requestLearningReportViewOnly
     *            The requestLearningReportViewOnly to set.
     */
    public void setRequestLearningReportViewOnly(String requestLearningReportViewOnly) {
	this.requestLearningReportViewOnly = requestLearningReportViewOnly;
    }

    /**
     * @return Returns the redoQuestions.
     */
    public String getRedoQuestions() {
	return redoQuestions;
    }

    /**
     * @param redoQuestions
     *            The redoQuestions to set.
     */
    public void setRedoQuestions(String redoQuestions) {
	this.redoQuestions = redoQuestions;
    }

    /**
     * @return Returns the refreshAnswers.
     */
    public String getRefreshAnswers() {
	return refreshAnswers;
    }

    /**
     * @param refreshAnswers
     *            The refreshAnswers to set.
     */
    public void setRefreshAnswers(String refreshAnswers) {
	this.refreshAnswers = refreshAnswers;
    }

    /**
     * @return Returns the entryText.
     */
    public String getEntryText() {
	return entryText;
    }

    /**
     * @param entryText
     *            The entryText to set.
     */
    public void setEntryText(String entryText) {
	this.entryText = entryText;
    }

    /**
     * @return Returns the forwardtoReflection.
     */
    public String getForwardtoReflection() {
	return forwardtoReflection;
    }

    /**
     * @param forwardtoReflection
     *            The forwardtoReflection to set.
     */
    public void setForwardtoReflection(String forwardtoReflection) {
	this.forwardtoReflection = forwardtoReflection;
    }

    /**
     * @return Returns the submitReflection.
     */
    public String getSubmitReflection() {
	return submitReflection;
    }

    /**
     * @param submitReflection
     *            The submitReflection to set.
     */
    public void setSubmitReflection(String submitReflection) {
	this.submitReflection = submitReflection;
    }

    /**
     * @return Returns the viewAll.
     */
    public String getViewAll() {
	return viewAll;
    }

    /**
     * @param viewAll
     *            The viewAll to set.
     */
    public void setViewAll(String viewAll) {
	this.viewAll = viewAll;
    }

    /**
     * @return Returns the updateReflection.
     */
    public String getUpdateReflection() {
	return updateReflection;
    }

    /**
     * @param updateReflection
     *            The updateReflection to set.
     */
    public void setUpdateReflection(String updateReflection) {
	this.updateReflection = updateReflection;
    }

}
