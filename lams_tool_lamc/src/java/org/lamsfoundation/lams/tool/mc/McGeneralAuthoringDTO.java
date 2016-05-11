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


package org.lamsfoundation.lams.tool.mc;

import java.util.Map;

/**
 * DTO that holds authoring properties for authoring jsps
 *
 * @author Ozgur Demirtas
 */
public class McGeneralAuthoringDTO implements Comparable {
    protected String toolContentID;
    protected String currentTab;

    protected String activityTitle;
    protected String activityInstructions;
    protected String usernameVisible;
    protected String reflect;
    protected String questionsSequenced;
    protected String randomize;
    protected String displayAnswers;
    protected String reflectionSubject;
    protected String showMarks;

    protected String httpSessionID;

    protected String contentFolderID;
    protected String editableQuestionText;
    protected String editableQuestionFeedback;
    protected String sln;
    protected String retries;

    protected Map marksMap;
    protected String markValue;
    protected Map correctMap;

    protected String passMarkValue;
    protected Map passMarksMap;

    protected String totalMarks;

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

    @Override
    public int compareTo(Object o) {
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = (McGeneralAuthoringDTO) o;

	if (mcGeneralAuthoringDTO == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

    /**
     * @return Returns the activityInstructions.
     */
    public String getActivityInstructions() {
	return activityInstructions;
    }

    /**
     * @param activityInstructions
     *            The activityInstructions to set.
     */
    public void setActivityInstructions(String activityInstructions) {
	this.activityInstructions = activityInstructions;
    }

    /**
     * @return Returns the activityTitle.
     */
    public String getActivityTitle() {
	return activityTitle;
    }

    /**
     * @param activityTitle
     *            The activityTitle to set.
     */
    public void setActivityTitle(String activityTitle) {
	this.activityTitle = activityTitle;
    }

    /**
     * @return Returns the questionsSequenced.
     */
    public String getQuestionsSequenced() {
	return questionsSequenced;
    }

    /**
     * @param questionsSequenced
     *            The questionsSequenced to set.
     */
    public void setQuestionsSequenced(String questionsSequenced) {
	this.questionsSequenced = questionsSequenced;
    }

    /**
     * @return Returns the usernameVisible.
     */
    public String getUsernameVisible() {
	return usernameVisible;
    }

    /**
     * @param usernameVisible
     *            The usernameVisible to set.
     */
    public void setUsernameVisible(String usernameVisible) {
	this.usernameVisible = usernameVisible;
    }

    /**
     * @return Returns the currentTab.
     */
    public String getCurrentTab() {
	return currentTab;
    }

    /**
     * @param currentTab
     *            The currentTab to set.
     */
    public void setCurrentTab(String currentTab) {
	this.currentTab = currentTab;
    }

    /**
     * @return Returns the contentFolderID.
     */
    public String getContentFolderID() {
	return contentFolderID;
    }

    /**
     * @param contentFolderID
     *            The contentFolderID to set.
     */
    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    /**
     * @return Returns the editableQuestionText.
     */
    public String getEditableQuestionText() {
	return editableQuestionText;
    }

    /**
     * @param editableQuestionText
     *            The editableQuestionText to set.
     */
    public void setEditableQuestionText(String editableQuestionText) {
	this.editableQuestionText = editableQuestionText;
    }

    /**
     * @return Returns the editableQuestionFeedback.
     */
    public String getEditableQuestionFeedback() {
	return editableQuestionFeedback;
    }

    /**
     * @param editableQuestionFeedback
     *            The editableQuestionFeedback to set.
     */
    public void setEditableQuestionFeedback(String editableQuestionFeedback) {
	this.editableQuestionFeedback = editableQuestionFeedback;
    }

    /**
     * @return Returns the reflect.
     */
    public String getReflect() {
	return reflect;
    }

    /**
     * @param reflect
     *            The reflect to set.
     */
    public void setReflect(String reflect) {
	this.reflect = reflect;
    }

    /**
     * @return Returns the reflectionSubject.
     */
    public String getReflectionSubject() {
	return reflectionSubject;
    }

    /**
     * @param reflectionSubject
     *            The reflectionSubject to set.
     */
    public void setReflectionSubject(String reflectionSubject) {
	this.reflectionSubject = reflectionSubject;
    }

    /**
     * @return Returns the sln.
     */
    public String getSln() {
	return sln;
    }

    /**
     * @param sln
     *            The sln to set.
     */
    public void setSln(String sln) {
	this.sln = sln;
    }

    /**
     * @return Returns the retries.
     */
    public String getRetries() {
	return retries;
    }

    /**
     * @param retries
     *            The retries to set.
     */
    public void setRetries(String retries) {
	this.retries = retries;
    }

    /**
     * @return Returns the marksMap.
     */
    public Map getMarksMap() {
	return marksMap;
    }

    /**
     * @param marksMap
     *            The marksMap to set.
     */
    public void setMarksMap(Map marksMap) {
	this.marksMap = marksMap;
    }

    /**
     * @return Returns the markValue.
     */
    public String getMarkValue() {
	return markValue;
    }

    /**
     * @param markValue
     *            The markValue to set.
     */
    public void setMarkValue(String markValue) {
	this.markValue = markValue;
    }

    /**
     * @return Returns the correctMap.
     */
    public Map getCorrectMap() {
	return correctMap;
    }

    /**
     * @param correctMap
     *            The correctMap to set.
     */
    public void setCorrectMap(Map correctMap) {
	this.correctMap = correctMap;
    }

    /**
     * @return Returns the passMarkValue.
     */
    public String getPassMarkValue() {
	return passMarkValue;
    }

    /**
     * @param passMarkValue
     *            The passMarkValue to set.
     */
    public void setPassMarkValue(String passMarkValue) {
	this.passMarkValue = passMarkValue;
    }

    /**
     * @return Returns the passMarksMap.
     */
    public Map getPassMarksMap() {
	return passMarksMap;
    }

    /**
     * @param passMarksMap
     *            The passMarksMap to set.
     */
    public void setPassMarksMap(Map passMarksMap) {
	this.passMarksMap = passMarksMap;
    }

    /**
     * @return Returns the totalMarks.
     */
    public String getTotalMarks() {
	return totalMarks;
    }

    /**
     * @param totalMarks
     *            The totalMarks to set.
     */
    public void setTotalMarks(String totalMarks) {
	this.totalMarks = totalMarks;
    }

    /**
     * @return Returns the showMarks.
     */
    public String getShowMarks() {
	return showMarks;
    }

    /**
     * @param showMarks
     *            The showMarks to set.
     */
    public void setShowMarks(String showMarks) {
	this.showMarks = showMarks;
    }

    /**
     * @return Returns the randomize.
     */
    public String getRandomize() {
	return randomize;
    }

    /**
     * @param randomize
     *            The randomize to set.
     */
    public void setRandomize(String randomize) {
	this.randomize = randomize;
    }

    /**
     * @return Returns the displayAnswers.
     */
    public String getDisplayAnswers() {
	return displayAnswers;
    }

    /**
     * @param randomize
     *            The randomize to set.
     */
    public void setDisplayAnswers(String displayAnswers) {
	this.displayAnswers = displayAnswers;
    }
}
