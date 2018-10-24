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

/* ActionForm for the Authoring environment*/
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.service.IQaService;

/**
 * @author Ozgur Demirtas
 */
public class QaAuthoringForm extends QaLearningForm implements QaAppConstants {
    protected String addContent;
    protected String removeContent;
    protected String removeAllContent;
    protected String submitAllContent;
    protected String submitTabDone;
    protected String currentTab;

    protected String choice;
    protected String choiceBasic;
    protected String choiceAdvanced;
    protected String choiceInstructions;

    /* basic content */
    protected String title;
    protected String instructions;
    protected String questionIndex;
    protected String isRemoveContent;

    protected String toolContentID;
    /* advanced content */
    protected String reportTitle;
    protected String monitoringReportTitle;
    protected String endLearningMessage;
    protected String usernameVisible;
    protected String allowRateAnswers;
    protected String notifyTeachersOnResponseSubmit;
    protected String showOtherAnswers;
    protected String questionsSequenced;
    protected String lockWhenFinished;
    protected String noReeditAllowed;
    protected String reflect;
    protected String reflectionSubject;
    protected int maximumRates;
    protected int minimumRates;

    /* proxy controllers for Monitoring tabs */
    protected String summaryMonitoring;
    protected String instructionsMonitoring;
    protected String editActivityMonitoring;
    protected String statsMonitoring;

    protected String edit;
    private String contentFolderID;
    private String addSingleQuestion;
    private String editableQuestionIndex;
    protected String editableQuestionText;
    private String feedback;
    private boolean required;
    private int minWordsLimit;
    private String editQuestionBoxRequest;

    protected IQaService qaService;

    protected boolean allowRichEditor;
    protected boolean useSelectLeaderToolOuput;

    public void resetUserAction() {
	this.addContent = null;
	this.removeContent = null;
	this.removeAllContent = null;
	this.submitAllContent = null;
	this.submitTabDone = null;

	this.summaryMonitoring = null;
	this.instructionsMonitoring = null;
	this.editActivityMonitoring = null;
	this.statsMonitoring = null;
	this.edit = null;
	this.allowRichEditor = false;
	this.useSelectLeaderToolOuput = false;
    }

    public void reset() {
	this.addContent = null;
	this.removeContent = null;
	this.removeAllContent = null;
	this.submitAllContent = null;
	this.submitTabDone = null;

	this.choice = null;
	this.choiceBasic = null;
	this.choiceAdvanced = null;
	this.choiceInstructions = null;

	this.title = null;
	this.instructions = null;
	this.questionIndex = null;
	this.isRemoveContent = null;

	this.endLearningMessage = null;
	this.reportTitle = null;
	this.monitoringReportTitle = null;
	this.questionsSequenced = null;
	this.lockWhenFinished = null;
	this.noReeditAllowed = null;
	this.reflect = null;
	this.allowRichEditor = false;
	this.useSelectLeaderToolOuput = false;

	this.summaryMonitoring = null;
	this.instructionsMonitoring = null;
	this.editActivityMonitoring = null;
	this.statsMonitoring = null;
	this.edit = null;
	this.toolContentID = null;
	this.currentTab = null;
    }

    public void resetRadioBoxes() {
	this.usernameVisible = OPTION_OFF;
	this.allowRateAnswers = OPTION_OFF;
	this.notifyTeachersOnResponseSubmit = OPTION_OFF;
	this.questionsSequenced = OPTION_OFF;
	this.lockWhenFinished = OPTION_OFF;
	this.noReeditAllowed = OPTION_OFF;
	this.reflect = OPTION_OFF;
	this.allowRichEditor = false;
	this.required = false;
	this.useSelectLeaderToolOuput = false;
    }

    /**
     * @return Returns the isRemoveContent.
     */
    public String getIsRemoveContent() {
	return isRemoveContent;
    }

    /**
     * @param isRemoveContent
     *            The isRemoveContent to set.
     */
    public void setIsRemoveContent(String isRemoveContent) {
	this.isRemoveContent = isRemoveContent;
    }

    /**
     * @return Returns the questionIndex.
     */
    @Override
    public String getQuestionIndex() {
	return questionIndex;
    }

    /**
     * @param questionIndex
     *            The questionIndex to set.
     */
    @Override
    public void setQuestionIndex(String questionIndex) {
	this.questionIndex = questionIndex;
    }

    /**
     * @return Returns the addContent.
     */
    public String getAddContent() {
	return addContent;
    }

    /**
     * @param addContent
     *            The addContent to set.
     */
    public void setAddContent(String addContent) {
	this.addContent = addContent;
    }

    /**
     * @return Returns the removeContent.
     */
    public String getRemoveContent() {
	return removeContent;
    }

    /**
     * @param removeContent
     *            The removeContent to set.
     */
    public void setRemoveContent(String removeContent) {
	this.removeContent = removeContent;
    }

    /**
     * @return Returns the removeAllContent.
     */
    public String getRemoveAllContent() {
	return removeAllContent;
    }

    /**
     * @param removeAllContent
     *            The removeAllContent to set.
     */
    public void setRemoveAllContent(String removeAllContent) {
	this.removeAllContent = removeAllContent;
    }

    /**
     * @return Returns the submitAllContent.
     */
    public String getSubmitAllContent() {
	return submitAllContent;
    }

    /**
     * @param submitAllContent
     *            The submitAllContent to set.
     */
    public void setSubmitAllContent(String submitAllContent) {
	this.submitAllContent = submitAllContent;
    }

    /**
     * @return Returns the instructions.
     */
    public String getInstructions() {
	return instructions;
    }

    /**
     * @param instructions
     *            The instructions to set.
     */
    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return Returns the choiceAdvanced.
     */
    public String getChoiceAdvanced() {
	return choiceAdvanced;
    }

    /**
     * @param choiceAdvanced
     *            The choiceAdvanced to set.
     */
    public void setChoiceAdvanced(String choiceAdvanced) {
	this.choiceAdvanced = choiceAdvanced;
    }

    /**
     * @return Returns the choiceBasic.
     */
    public String getChoiceBasic() {
	return choiceBasic;
    }

    /**
     * @param choiceBasic
     *            The choiceBasic to set.
     */
    public void setChoiceBasic(String choiceBasic) {
	this.choiceBasic = choiceBasic;
    }

    /**
     * @return Returns the choiceInstructions.
     */
    public String getChoiceInstructions() {
	return choiceInstructions;
    }

    /**
     * @param choiceInstructions
     *            The choiceInstructions to set.
     */
    public void setChoiceInstructions(String choiceInstructions) {
	this.choiceInstructions = choiceInstructions;
    }

    /**
     * @return Returns the choice.
     */
    public String getChoice() {
	return choice;
    }

    /**
     * @param choice
     *            The choice to set.
     */
    public void setChoice(String choice) {
	this.choice = choice;
    }

    /**
     * @return Returns the reportTitle.
     */
    public String getReportTitle() {
	return reportTitle;
    }

    /**
     * @param reportTitle
     *            The reportTitle to set.
     */
    public void setReportTitle(String reportTitle) {
	this.reportTitle = reportTitle;
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
     * @return Returns the notifyTeachersOnResponseSubmit.
     */
    public String getNotifyTeachersOnResponseSubmit() {
	return notifyTeachersOnResponseSubmit;
    }

    /**
     * @param notifyTeachersOnResponseSubmit
     *            The notifyTeachersOnResponseSubmit to set.
     */
    public void setNotifyTeachersOnResponseSubmit(String notifyTeachersOnResponseSubmit) {
	this.notifyTeachersOnResponseSubmit = notifyTeachersOnResponseSubmit;
    }

    /**
     * @return Returns the submitTabDone.
     */
    public String getSubmitTabDone() {
	return submitTabDone;
    }

    /**
     * @param submitTabDone
     *            The submitTabDone to set.
     */
    public void setSubmitTabDone(String submitTabDone) {
	this.submitTabDone = submitTabDone;
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

    /**
     * @return Returns the endLearningMessage.
     */
    public String getEndLearningMessage() {
	return endLearningMessage;
    }

    /**
     * @param endLearningMessage
     *            The endLearningMessage to set.
     */
    public void setEndLearningMessage(String endLearningMessage) {
	this.endLearningMessage = endLearningMessage;
    }

    /**
     * @return Returns the monitoringReportTitle.
     */
    public String getMonitoringReportTitle() {
	return monitoringReportTitle;
    }

    /**
     * @param monitoringReportTitle
     *            The monitoringReportTitle to set.
     */
    public void setMonitoringReportTitle(String monitoringReportTitle) {
	this.monitoringReportTitle = monitoringReportTitle;
    }

    /**
     * @return Returns the editActivityMonitoring.
     */
    public String getEditActivityMonitoring() {
	return editActivityMonitoring;
    }

    /**
     * @param editActivityMonitoring
     *            The editActivityMonitoring to set.
     */
    public void setEditActivityMonitoring(String editActivityMonitoring) {
	this.editActivityMonitoring = editActivityMonitoring;
    }

    /**
     * @return Returns the instructionsMonitoring.
     */
    public String getInstructionsMonitoring() {
	return instructionsMonitoring;
    }

    /**
     * @param instructionsMonitoring
     *            The instructionsMonitoring to set.
     */
    public void setInstructionsMonitoring(String instructionsMonitoring) {
	this.instructionsMonitoring = instructionsMonitoring;
    }

    /**
     * @return Returns the statsMonitoring.
     */
    public String getStatsMonitoring() {
	return statsMonitoring;
    }

    /**
     * @param statsMonitoring
     *            The statsMonitoring to set.
     */
    public void setStatsMonitoring(String statsMonitoring) {
	this.statsMonitoring = statsMonitoring;
    }

    /**
     * @return Returns the summaryMonitoring.
     */
    public String getSummaryMonitoring() {
	return summaryMonitoring;
    }

    /**
     * @param summaryMonitoring
     *            The summaryMonitoring to set.
     */
    public void setSummaryMonitoring(String summaryMonitoring) {
	this.summaryMonitoring = summaryMonitoring;
    }

    /**
     * @return Returns the edit.
     */
    public String getEdit() {
	return edit;
    }

    /**
     * @param edit
     *            The edit to set.
     */
    public void setEdit(String edit) {
	this.edit = edit;
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
     * @return Returns the qaService.
     */
    public IQaService getQaService() {
	return qaService;
    }

    /**
     * @param qaService
     *            The qaService to set.
     */
    public void setQaService(IQaService qaService) {
	this.qaService = qaService;
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
     * @return Returns the addSingleQuestion.
     */
    public String getAddSingleQuestion() {
	return addSingleQuestion;
    }

    /**
     * @param addSingleQuestion
     *            The addSingleQuestion to set.
     */
    public void setAddSingleQuestion(String addSingleQuestion) {
	this.addSingleQuestion = addSingleQuestion;
    }

    /**
     * @return Returns the editableQuestionIndex.
     */
    public String getEditableQuestionIndex() {
	return editableQuestionIndex;
    }

    /**
     * @param editableQuestionIndex
     *            The editableQuestionIndex to set.
     */
    public void setEditableQuestionIndex(String editableQuestionIndex) {
	this.editableQuestionIndex = editableQuestionIndex;
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
     * @return Returns the feedback.
     */
    public String getFeedback() {
	return feedback;
    }

    /**
     * @param feedback
     *            The feedback to set.
     */
    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    /**
     * @return Returns the editQuestionBoxRequest.
     */
    public String getEditQuestionBoxRequest() {
	return editQuestionBoxRequest;
    }

    /**
     * @param editQuestionBoxRequest
     *            The editQuestionBoxRequest to set.
     */
    public void setEditQuestionBoxRequest(String editQuestionBoxRequest) {
	this.editQuestionBoxRequest = editQuestionBoxRequest;
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
     * @return Returns the noReeditAllowed.
     */
    public String getNoReeditAllowed() {
	return noReeditAllowed;
    }

    /**
     * @param noReeditAllowed
     *            The noReeditAllowed to set.
     */
    public void setNoReeditAllowed(String noReeditAllowed) {
	this.noReeditAllowed = noReeditAllowed;
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

    public boolean isAllowRichEditor() {
	return allowRichEditor;
    }

    public void setAllowRichEditor(boolean allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    public boolean isUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    public boolean isRequired() {
	return required;
    }

    public void setRequired(boolean required) {
	this.required = required;
    }

    public void setMinWordsLimit(int minWordsLimit) {
	this.minWordsLimit = minWordsLimit;
    }

    public int getMinWordsLimit() {
	return minWordsLimit;
    }
}
