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

import org.apache.commons.lang.builder.ToStringBuilder;

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
    protected String activeModule;
    protected String defineLaterInEditMode;
    protected String showAuthoringTabs;
    protected String monitoringOriginatedDefineLater;
    protected String targetMode;
    protected String defaultQuestionContent;
    protected String defaultContentIdStr;

    protected String activityTitle;
    protected String activityInstructions;
    protected String usernameVisible;
    protected String allowRateAnswers;
    protected String showOtherAnswers;
    protected String reflect;
    protected String synchInMonitor;
    protected String questionsSequenced;
    protected String lockWhenFinished;
    protected String editActivityEditMode;
    protected String reflectionSubject;
    protected Boolean allowRichEditor;
    protected Boolean useSelectLeaderToolOuput;

    protected String httpSessionID;
    protected String requestedModule;
    protected String isDefineLater;

    protected String sbmtSuccess;
    protected String userExceptionQuestionsDuplicate;

    protected String contentFolderID;
    protected String editableQuestionText;
    protected String editableQuestionFeedback;

    public String toString() {
	return new ToStringBuilder(this).append("toolContentID: ", toolContentID)
		.append("contentFolderID: ", contentFolderID).append("httpSessionID: ", httpSessionID)
		.append("currentTab: ", currentTab).append("activeModule: ", activeModule)
		.append("defineLaterInEditMode: ", defineLaterInEditMode)
		.append("showAuthoringTabs: ", showAuthoringTabs)
		.append("monitoringOriginatedDefineLater: ", monitoringOriginatedDefineLater)
		.append("targetMode: ", targetMode).append("defaultQuestionContent: ", defaultQuestionContent)
		.append("defaultContentIdStr: ", defaultContentIdStr).append("activityTitle: ", activityTitle)
		.append("activityInstructions: ", activityInstructions).append("reflect: ", reflect)
		.append("usernameVisible: ", usernameVisible).append("allowRateAnswers: ", allowRateAnswers)
		.append("showOtherAnswers: ", showOtherAnswers).append("synchInMonitor: ", synchInMonitor)
		.append("questionsSequenced: ", questionsSequenced).append("lockWhenFinished: ", lockWhenFinished)
		.append("editActivityEditMode: ", editActivityEditMode)
		.append("reflectionSubject: ", reflectionSubject).append("requestedModule: ", requestedModule)
		.append("isDefineLater: ", isDefineLater).append("sbmtSuccess: ", sbmtSuccess)
		.append("userExceptionQuestionsDuplicate: ", userExceptionQuestionsDuplicate).toString();
    }

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
     * @return Returns the isDefineLater.
     */
    public String getIsDefineLater() {
	return isDefineLater;
    }

    /**
     * @param isDefineLater
     *            The isDefineLater to set.
     */
    public void setIsDefineLater(String isDefineLater) {
	this.isDefineLater = isDefineLater;
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
     * @return Returns the targetMode.
     */
    public String getTargetMode() {
	return targetMode;
    }

    /**
     * @param targetMode
     *            The targetMode to set.
     */
    public void setTargetMode(String targetMode) {
	this.targetMode = targetMode;
    }

    /**
     * @return Returns the monitoringOriginatedDefineLater.
     */
    public String getMonitoringOriginatedDefineLater() {
	return monitoringOriginatedDefineLater;
    }

    /**
     * @param monitoringOriginatedDefineLater
     *            The monitoringOriginatedDefineLater to set.
     */
    public void setMonitoringOriginatedDefineLater(String monitoringOriginatedDefineLater) {
	this.monitoringOriginatedDefineLater = monitoringOriginatedDefineLater;
    }

    /**
     * @return Returns the activeModule.
     */
    public String getActiveModule() {
	return activeModule;
    }

    /**
     * @param activeModule
     *            The activeModule to set.
     */
    public void setActiveModule(String activeModule) {
	this.activeModule = activeModule;
    }

    /**
     * @return Returns the defineLaterInEditMode.
     */
    public String getDefineLaterInEditMode() {
	return defineLaterInEditMode;
    }

    /**
     * @param defineLaterInEditMode
     *            The defineLaterInEditMode to set.
     */
    public void setDefineLaterInEditMode(String defineLaterInEditMode) {
	this.defineLaterInEditMode = defineLaterInEditMode;
    }

    /**
     * @return Returns the showAuthoringTabs.
     */
    public String getShowAuthoringTabs() {
	return showAuthoringTabs;
    }

    /**
     * @param showAuthoringTabs
     *            The showAuthoringTabs to set.
     */
    public void setShowAuthoringTabs(String showAuthoringTabs) {
	this.showAuthoringTabs = showAuthoringTabs;
    }

    public int compareTo(Object o) {
	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = (QaGeneralAuthoringDTO) o;

	if (qaGeneralAuthoringDTO == null)
	    return 1;
	else
	    return 0;
    }

    /**
     * @return Returns the defaultContentIdStr.
     */
    public String getDefaultContentIdStr() {
	return defaultContentIdStr;
    }

    /**
     * @param defaultContentIdStr
     *            The defaultContentIdStr to set.
     */
    public void setDefaultContentIdStr(String defaultContentIdStr) {
	this.defaultContentIdStr = defaultContentIdStr;
    }

    /**
     * @return Returns the defaultQuestionContent.
     */
    public String getDefaultQuestionContent() {
	return defaultQuestionContent;
    }

    /**
     * @param defaultQuestionContent
     *            The defaultQuestionContent to set.
     */
    public void setDefaultQuestionContent(String defaultQuestionContent) {
	this.defaultQuestionContent = defaultQuestionContent;
    }

    /**
     * @return Returns the requestedModule.
     */
    public String getRequestedModule() {
	return requestedModule;
    }

    /**
     * @param requestedModule
     *            The requestedModule to set.
     */
    public void setRequestedModule(String requestedModule) {
	this.requestedModule = requestedModule;
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
     * @return Returns the synchInMonitor.
     */
    public String getSynchInMonitor() {
	return synchInMonitor;
    }

    /**
     * @param synchInMonitor
     *            The synchInMonitor to set.
     */
    public void setSynchInMonitor(String synchInMonitor) {
	this.synchInMonitor = synchInMonitor;
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
     * @return Returns the sbmtSuccess.
     */
    public String getSbmtSuccess() {
	return sbmtSuccess;
    }

    /**
     * @param sbmtSuccess
     *            The sbmtSuccess to set.
     */
    public void setSbmtSuccess(String sbmtSuccess) {
	this.sbmtSuccess = sbmtSuccess;
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

}
