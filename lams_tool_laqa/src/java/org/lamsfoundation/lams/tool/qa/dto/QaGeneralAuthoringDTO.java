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

/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.dto;


/**
 * <p>
 * DTO that holds authoring properties for authoring jsps
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class QaGeneralAuthoringDTO implements Comparable {
    protected String toolContentID;
    protected String currentTab;

    protected String activityTitle;
    protected String activityInstructions;
    protected String usernameVisible;
    protected String allowRateAnswers;
    protected String showOtherAnswers;
    protected String reflect;
    protected String questionsSequenced;
    protected String lockWhenFinished;
    protected String editActivityEditMode;
    protected String reflectionSubject;
    protected Boolean allowRichEditor;
    protected Boolean useSelectLeaderToolOuput;
    protected int maximumRates;
    protected int minimumRates;

    protected String httpSessionID;

    protected String userExceptionQuestionsDuplicate;

    protected String contentFolderID;
    protected String editableQuestionText;
    protected String editableQuestionFeedback;

    /**
     * @return Returns the userExceptionQuestionsDuplicate.
     */
    public String getUserExceptionQuestionsDuplicate() {
	return userExceptionQuestionsDuplicate;
    }

    /**
     * @param userExceptionQuestionsDuplicate
     *            The userExceptionQuestionsDuplicate to set.
     */
    public void setUserExceptionQuestionsDuplicate(String userExceptionQuestionsDuplicate) {
	this.userExceptionQuestionsDuplicate = userExceptionQuestionsDuplicate;
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
     * @return Returns the editActivityEditMode.
     */
    public String getEditActivityEditMode() {
	return editActivityEditMode;
    }

    /**
     * @param editActivityEditMode
     *            The editActivityEditMode to set.
     */
    public void setEditActivityEditMode(String editActivityEditMode) {
	this.editActivityEditMode = editActivityEditMode;
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

    public int compareTo(Object o) {
	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = (QaGeneralAuthoringDTO) o;

	if (qaGeneralAuthoringDTO == null)
	    return 1;
	else
	    return 0;
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
     * @return Returns the allowRateAnswers.
     */
    public String getAllowRateAnswers() {
	return allowRateAnswers;
    }

    /**
     * @param allowRateAnswers
     *            The allowRateAnswers to set.
     */
    public void setAllowRateAnswers(String allowRateAnswers) {
	this.allowRateAnswers = allowRateAnswers;
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
     * @return Returns the lockWhenFinished.
     */
    public String getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished
     *            The lockWhenFinished to set.
     */
    public void setLockWhenFinished(String lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the showOtherAnswers.
     */
    public String getShowOtherAnswers() {
	return showOtherAnswers;
    }

    /**
     * @param showOtherAnswers
     *            The showOtherAnswers to set.
     */
    public void setShowOtherAnswers(String showOtherAnswers) {
	this.showOtherAnswers = showOtherAnswers;
    }

    public Boolean getAllowRichEditor() {
	return allowRichEditor;
    }

    public void setAllowRichEditor(Boolean allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    public Boolean getUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(Boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }
    
    /**
     * @return
     */
    public int getMaximumRates() {
	return maximumRates;
    }

    public void setMaximumRates(int maximumRates) {
	this.maximumRates = maximumRates;
    }

    /**
     * @return
     */
    public int getMinimumRates() {
	return minimumRates;
    }

    public void setMinimumRates(int minimumRates) {
	this.minimumRates = minimumRates;
    }

}
