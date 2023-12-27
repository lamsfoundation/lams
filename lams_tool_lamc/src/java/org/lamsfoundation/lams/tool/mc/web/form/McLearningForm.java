/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.web.form;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.mc.service.IMcService;

/**
 * @author Ozgur Demirtas
 *
 *         ActionForm for the Learning environment
 */
public class McLearningForm {
    protected String continueOptions;
    protected String nextOptions;
    protected String continueOptionsCombined;
    protected String redoQuestions;
    protected String viewAnswers;
    protected String learnerFinished;

    protected String[] checkedCa;
    protected String[] sequentialCheckedCa;

    protected Long userID;
    protected String toolContentID;
    protected String toolContentUID;
    protected String toolSessionID;

    protected String learningMode;
    protected String currentQuestionIndex;

    /** put tghese under SessionMap */
    protected String userOverPassMark;
    protected String passMarkApplicable;

    protected String entryText;

    protected Integer questionIndex;
    protected String nextQuestionSelected;
    protected String httpSessionID;

    protected String sessionId;

    public IMcService mcService;

    public void resetCa(HttpServletRequest request) {
	checkedCa = new String[0];
    }

    public void resetCommands() {
	this.setContinueOptions(null);
	this.setNextOptions(null);
	this.setContinueOptionsCombined(null);
	this.setRedoQuestions(null);
	this.setViewAnswers(null);
	this.setLearnerFinished(null);
    }

    public void resetParameters() {
	this.setNextQuestionSelected(null);
    }

    /**
     * @return Returns the continueOptions.
     */
    public String getContinueOptions() {
	return continueOptions;
    }

    /**
     * @param continueOptions
     *            The continueOptions to set.
     */
    public void setContinueOptions(String continueOptions) {
	this.continueOptions = continueOptions;
    }

    /**
     * @return Returns the continueOptionsCombined.
     */
    public String getContinueOptionsCombined() {
	return continueOptionsCombined;
    }

    /**
     * @param continueOptionsCombined
     *            The continueOptionsCombined to set.
     */
    public void setContinueOptionsCombined(String continueOptionsCombined) {
	this.continueOptionsCombined = continueOptionsCombined;
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
     * @return Returns the viewAnswers.
     */
    public String getViewAnswers() {
	return viewAnswers;
    }

    /**
     * @param viewAnswers
     *            The viewAnswers to set.
     */
    public void setViewAnswers(String viewAnswers) {
	this.viewAnswers = viewAnswers;
    }

    /**
     * @return Returns the nextOptions.
     */
    public String getNextOptions() {
	return nextOptions;
    }

    /**
     * @param nextOptions
     *            The nextOptions to set.
     */
    public void setNextOptions(String nextOptions) {
	this.nextOptions = nextOptions;
    }

    /**
     * @return Returns the learnerFinished.
     */
    public String getLearnerFinished() {
	return learnerFinished;
    }

    /**
     * @param learnerFinished
     *            The learnerFinished to set.
     */
    public void setLearnerFinished(String learnerFinished) {
	this.learnerFinished = learnerFinished;
    }

    /**
     * @return Returns the userOverPassMark.
     */
    public String getUserOverPassMark() {
	return userOverPassMark;
    }

    /**
     * @param userOverPassMark
     *            The userOverPassMark to set.
     */
    public void setUserOverPassMark(String userOverPassMark) {
	this.userOverPassMark = userOverPassMark;
    }

    /**
     * @return Returns the passMarkApplicable.
     */
    public String getPassMarkApplicable() {
	return passMarkApplicable;
    }

    /**
     * @param passMarkApplicable
     *            The passMarkApplicable to set.
     */
    public void setPassMarkApplicable(String passMarkApplicable) {
	this.passMarkApplicable = passMarkApplicable;
    }

    /**
     * @return Returns the checkedCa.
     */
    public String[] getCheckedCa() {
	return checkedCa;
    }

    /**
     * @param checkedCa
     *            The checkedCa to set.
     */
    public void setCheckedCa(String[] checkedCa) {
	this.checkedCa = checkedCa;
    }

    /**
     * @return Returns the learningMode.
     */
    public String getLearningMode() {
	return learningMode;
    }

    /**
     * @param learningMode
     *            The learningMode to set.
     */
    public void setLearningMode(String learningMode) {
	this.learningMode = learningMode;
    }

    /**
     * @return Returns the toolContentUID.
     */
    public String getToolContentUID() {
	return toolContentUID;
    }

    /**
     * @param toolContentUID
     *            The toolContentUID to set.
     */
    public void setToolContentUID(String toolContentUID) {
	this.toolContentUID = toolContentUID;
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
     * @return Returns the nextQuestionSelected.
     */
    public String getNextQuestionSelected() {
	return nextQuestionSelected;
    }

    /**
     * @param nextQuestionSelected
     *            The nextQuestionSelected to set.
     */
    public void setNextQuestionSelected(String nextQuestionSelected) {
	this.nextQuestionSelected = nextQuestionSelected;
    }

    /**
     * @return Returns the questionIndex.
     */
    public Integer getQuestionIndex() {
	return questionIndex;
    }

    /**
     * @param questionIndex
     *            The questionIndex to set.
     */
    public void setQuestionIndex(Integer questionIndex) {
	this.questionIndex = questionIndex;
    }

    /**
     * @return Returns the toolContentID.
     */
    public String getToolContentID() {
	return toolContentID;
    }

    /**
     * @param toolContentID
     *            The toolContentID to set.
     */
    public void setToolContentID(String toolContentID) {
	this.toolContentID = toolContentID;
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
     * @return Returns the httpSessionID.
     */
    public String getHttpSessionID() {
	return httpSessionID;
    }

    /**
     * @param httpSessionID
     *            The httpSessionID to set.
     */
    public void setHttpSessionID(String httpSessionID) {
	this.httpSessionID = httpSessionID;
    }

    /**
     * @return Returns the sequentialCheckedCa.
     */
    public String[] getSequentialCheckedCa() {
	return sequentialCheckedCa;
    }

    /**
     * @param sequentialCheckedCa
     *            The sequentialCheckedCa to set.
     */
    public void setSequentialCheckedCa(String[] sequentialCheckedCa) {
	this.sequentialCheckedCa = sequentialCheckedCa;
    }

    /**
     * @return Returns the mcService.
     */
    public IMcService getMcService() {
	return mcService;
    }

    /**
     * @param mcService
     *            The mcService to set.
     */
    public void setMcService(IMcService mcService) {
	this.mcService = mcService;
    }

    /**
     * @return Returns the userID.
     */
    public Long getUserID() {
	return userID;
    }

    /**
     * @param userID
     *            The userID to set.
     */
    public void setUserID(Long userID) {
	this.userID = userID;
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
     * @return Returns the sessionId.
     */
    public String getSessionId() {
	return sessionId;
    }

    /**
     * @param sessionId
     *            The sessionId to set.
     */
    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }
}
