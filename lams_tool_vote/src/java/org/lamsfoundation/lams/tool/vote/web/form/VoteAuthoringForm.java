/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.vote.web.form;

import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;

/**
 * <p>
 * ActionForm for the Authoring environment
 * </p>
 *
 * @author Ozgur Demirtas
 *
 */
public class VoteAuthoringForm extends VoteLearningForm implements VoteAppConstants {
    /**
     *
     */
    private static final long serialVersionUID = 6118856765992233606L;
    /* form controllers */
    protected String addOptionContent;
    protected String currentTab;

    protected String questionIndex;
    protected String optIndex;
    protected String optionIndex;
    protected String selectedIndex;
    protected String deletableOptionIndex;

    protected String removeOptionContent;

    protected String lockOnFinish;
    protected String allowText;
    protected String showResults;
    protected String useSelectLeaderToolOuput;

    protected String maxNominationCount;
    protected String minNominationCount;

    protected String addContent;
    protected String removeContent;
    protected String removeAllContent;
    protected String submitAllContent;
    protected String submitTabDone;

    /* tab controller, these may go away once the Flash wraps the jsp */
    protected String choice;
    protected String choiceBasic;
    protected String choiceAdvanced;
    protected String choiceInstructions;

    protected String submit;

    /* basic content */
    protected String title;
    protected String instructions;

    protected String isRemoveContent;
    protected String toolContentID;
    protected String editableNominationIndex;

    /* proxy controllers for Monitoring tabs */
    protected String summaryMonitoring;
    protected String instructionsMonitoring;
    protected String editActivityMonitoring;
    protected String statsMonitoring;

    protected String edit;
    protected String exceptionMaxNominationInvalid;
    protected String httpSessionID;
    protected IVoteService voteService;

    private String contentFolderID;
    private String addSingleQuestion;
    private String editableQuestionIndex;
    private String editQuestionBoxRequest;

    protected Integer assignedDataFlowObject;
    private Short maxInputs;

    public Integer getAssignedDataFlowObject() {
	return assignedDataFlowObject;
    }

    public void setAssignedDataFlowObject(Integer assignedDataFlowObject) {
	this.assignedDataFlowObject = assignedDataFlowObject;
    }

    public void resetUserAction() {
	addOptionContent = null;
	removeOptionContent = null;

	addContent = null;
	removeContent = null;
	removeAllContent = null;
	submitAllContent = null;
	submitTabDone = null;

	summaryMonitoring = null;
	instructionsMonitoring = null;
	editActivityMonitoring = null;
	statsMonitoring = null;
	edit = null;
	submit = null;
    }

    public void reset() {
	addOptionContent = null;
	removeOptionContent = null;

	addContent = null;
	removeContent = null;
	removeAllContent = null;
	submitAllContent = null;
	submitTabDone = null;

	choice = null;
	choiceBasic = null;
	choiceAdvanced = null;
	choiceInstructions = null;

	title = null;
	instructions = null;
	questionIndex = null;
	optIndex = null;
	optionIndex = null;
	selectedIndex = null;
	deletableOptionIndex = null;
	isRemoveContent = null;
	toolContentID = null;

	useSelectLeaderToolOuput = null;
	lockOnFinish = null;
	allowText = null;
	showResults = null;
	maxNominationCount = null;
	minNominationCount = null;

	summaryMonitoring = null;
	instructionsMonitoring = null;
	editActivityMonitoring = null;
	statsMonitoring = null;
	edit = null;
	submit = null;

	maxInputs = 0;
    }

    public void resetRadioBoxes() {
	lockOnFinish = "0";
	allowText = "0";
	showResults = "0";
	useSelectLeaderToolOuput = "0";
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
     * @return Returns the addOptionContent.
     */
    public String getAddOptionContent() {
	return addOptionContent;
    }

    /**
     * @param addOptionContent
     *            The addOptionContent to set.
     */
    public void setAddOptionContent(String addOptionContent) {
	this.addOptionContent = addOptionContent;
    }

    /**
     * @return Returns the removeOptionContent.
     */
    public String getRemoveOptionContent() {
	return removeOptionContent;
    }

    /**
     * @param removeOptionContent
     *            The removeOptionContent to set.
     */
    public void setRemoveOptionContent(String removeOptionContent) {
	this.removeOptionContent = removeOptionContent;
    }

    /**
     * @return Returns the optionIndex.
     */
    @Override
    public String getOptionIndex() {
	return optionIndex;
    }

    /**
     * @param optionIndex
     *            The optionIndex to set.
     */
    @Override
    public void setOptionIndex(String optionIndex) {
	this.optionIndex = optionIndex;
    }

    /**
     * @return Returns the selectedIndex.
     */
    public String getSelectedIndex() {
	return selectedIndex;
    }

    /**
     * @param selectedIndex
     *            The selectedIndex to set.
     */
    public void setSelectedIndex(String selectedIndex) {
	this.selectedIndex = selectedIndex;
    }

    /**
     * @return Returns the deletableOptionIndex.
     */
    public String getDeletableOptionIndex() {
	return deletableOptionIndex;
    }

    /**
     * @param deletableOptionIndex
     *            The deletableOptionIndex to set.
     */
    public void setDeletableOptionIndex(String deletableOptionIndex) {
	this.deletableOptionIndex = deletableOptionIndex;
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
     * @return Returns the submit.
     */
    public String getSubmit() {
	return submit;
    }

    /**
     * @param submit
     *            The submit to set.
     */
    public void setSubmit(String submit) {
	this.submit = submit;
    }

    /**
     * @return Returns the optIndex.
     */
    public String getOptIndex() {
	return optIndex;
    }

    /**
     * @param optIndex
     *            The optIndex to set.
     */
    public void setOptIndex(String optIndex) {
	this.optIndex = optIndex;
    }

    /**
     * @return Returns the lockOnFinish.
     */
    @Override
    public String getLockOnFinish() {
	return lockOnFinish;
    }

    /**
     * @param lockOnFinish
     *            The lockOnFinish to set.
     */
    @Override
    public void setLockOnFinish(String lockOnFinish) {
	this.lockOnFinish = lockOnFinish;
    }

    /**
     * @return Returns the allowText.
     */
    public String getAllowText() {
	return allowText;
    }

    /**
     * @param allowText
     *            The allowText to set.
     */
    public void setAllowText(String allowText) {
	this.allowText = allowText;
    }

    /**
     * @return Returns the maxNominationCount.
     */
    @Override
    public String getMaxNominationCount() {
	return maxNominationCount;
    }

    /**
     * @param maxNominationCount
     *            The maxNominationCount to set.
     */
    @Override
    public void setMaxNominationCount(String maxNominationCount) {
	this.maxNominationCount = maxNominationCount;
    }

    /**
     * @return Returns the minNominationCount.
     */
    @Override
    public String getMinNominationCount() {
	return minNominationCount;
    }

    /**
     * @param minNominationCount
     *            The minNominationCount to set.
     */
    @Override
    public void setMinNominationCount(String minNominationCount) {
	this.minNominationCount = minNominationCount;
    }

    /**
     * @return Returns the exceptionMaxNominationInvalid.
     */
    public String getExceptionMaxNominationInvalid() {
	return exceptionMaxNominationInvalid;
    }

    /**
     * @param exceptionMaxNominationInvalid
     *            The exceptionMaxNominationInvalid to set.
     */
    public void setExceptionMaxNominationInvalid(String exceptionMaxNominationInvalid) {
	this.exceptionMaxNominationInvalid = exceptionMaxNominationInvalid;
    }

    /**
     * @return Returns the toolContentID.
     */
    @Override
    public String getToolContentID() {
	return toolContentID;
    }

    /**
     * @param toolContentID
     *            The toolContentID to set.
     */
    @Override
    public void setToolContentID(String toolContentID) {
	this.toolContentID = toolContentID;
    }

    /**
     * @return Returns the voteService.
     */
    public IVoteService getVoteService() {
	return voteService;
    }

    /**
     * @param voteService
     *            The voteService to set.
     */
    public void setVoteService(IVoteService voteService) {
	this.voteService = voteService;
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
     * @return Returns the useSelectLeaderToolOuput.
     */
    @Override
    public String getUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    /**
     * @param useSelectLeaderToolOuput
     *            The useSelectLeaderToolOuput to set.
     */
    @Override
    public void setUseSelectLeaderToolOuput(String useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
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
     * @return Returns the editableNominationIndex.
     */
    public String getEditableNominationIndex() {
	return editableNominationIndex;
    }

    /**
     * @param editableNominationIndex
     *            The editableNominationIndex to set.
     */
    public void setEditableNominationIndex(String editableNominationIndex) {
	this.editableNominationIndex = editableNominationIndex;
    }

    @Override
    public String getShowResults() {
	return showResults;
    }

    @Override
    public void setShowResults(String showResults) {
	this.showResults = showResults;
    }

    public Short getMaxInputs() {
	return maxInputs;
    }

    public void setMaxInputs(Short maxInputs) {
	this.maxInputs = maxInputs;
    }
}
