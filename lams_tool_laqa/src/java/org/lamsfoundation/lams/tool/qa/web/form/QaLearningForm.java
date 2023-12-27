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
    protected String refreshAnswers;

    protected String totalQuestionCount;
    protected String sessionMapID;
    protected String toolSessionID;
    protected String questionIndex;
    protected String userID;
    protected String responseId;

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

}
