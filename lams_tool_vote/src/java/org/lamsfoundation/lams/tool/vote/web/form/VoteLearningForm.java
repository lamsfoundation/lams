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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;

/**
 * <p>
 * ActionForm for the Learning environment
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class VoteLearningForm implements VoteAppConstants {
    /**
     *
     */
    private static final long serialVersionUID = -7165633875677826696L;
    protected String optionCheckBoxSelected;
    protected String questionIndex;
    protected String optionIndex;
    protected String optionValue;
    protected String checked;

    protected String userEntry;
    protected String dispatch;
    protected String toolContentID;

    protected String useSelectLeaderToolOuput;
    protected String maxNominationCount;
    protected String minNominationCount;
    protected String allowTextEntry;
    protected String showResults;
    protected String lockOnFinish;
    protected String activityRetries;
    protected String activityTitle;
    protected String activityInstructions;

    protected String continueOptions;
    protected String nextOptions;
    protected String castVotes;

    protected String continueOptionsCombined;
    protected String redoQuestions;
    protected String viewSummary;
    protected String viewAnswers;
    protected String learnerFinished;
    protected String redoQuestionsOk;
    protected String donePreview;
    protected String doneLearnerProgress;
    protected String viewAllResults;

    protected String responseId;
    protected String method;
    protected String answer;
    protected String submitAnswersContent;
    protected String getNextQuestion;
    protected String getPreviousQuestion;
    protected String refreshVotes;

    protected String nominationsSubmited;
    protected String revisitingUser;
    protected String maxNominationCountReached;
    protected String minNominationCountReached;
    protected int castVoteCount;

    protected Map<String, String> mapGeneralCheckedOptionsContent;

    protected String entryText;
    protected String userID;
    protected String toolSessionID;
    protected String learningMode;
    protected String toolContentUID;
    protected String previewOnly;
    protected String reportViewOnly;

    protected boolean isUserLeader;
    protected String groupLeaderName;
    protected String groupLeaderUserId;

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

    /** The check boxes selected on the first voting screen */
    protected String[] checkedVotes;

    public void reset() {
	checkedVotes = new String[0];
    }

    public void resetUserActions() {
	this.getNextQuestion = null;
	this.getPreviousQuestion = null;
	this.viewAllResults = null;
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
     * @return Returns the method.
     */
    public String getMethod() {
	return method;
    }

    /**
     * @param method
     *            The method to set.
     */
    public void setMethod(String method) {
	this.method = method;
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

    public void resetCommands() {
	this.setContinueOptions(null);
	this.setNextOptions(null);
	this.setContinueOptionsCombined(null);
	this.setRedoQuestions(null);
	this.setViewSummary(null);
	this.setViewAnswers(null);
	this.setRedoQuestionsOk(null);
	this.setLearnerFinished(null);
	this.setDonePreview(null);
	this.setDoneLearnerProgress(null);
	this.setRefreshVotes(null);
    }

    public void resetParameters() {
	this.setOptionCheckBoxSelected(null);
	this.setQuestionIndex(null);
	this.setOptionIndex(null);
	this.setChecked(null);
	this.setOptionValue(null);
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
     * @return Returns the checked.
     */
    public String getChecked() {
	return checked;
    }

    /**
     * @param checked
     *            The checked to set.
     */
    public void setChecked(String checked) {
	this.checked = checked;
    }

    /**
     * @return Returns the optionCheckBoxSelected.
     */
    public String getOptionCheckBoxSelected() {
	return optionCheckBoxSelected;
    }

    /**
     * @param optionCheckBoxSelected
     *            The optionCheckBoxSelected to set.
     */
    public void setOptionCheckBoxSelected(String optionCheckBoxSelected) {
	this.optionCheckBoxSelected = optionCheckBoxSelected;
    }

    /**
     * @return Returns the refreshVotes.
     */
    public String getRefreshVotes() {
	return refreshVotes;
    }

    /**
     * @param refreshVotes
     *            The refreshVotes to set.
     */
    public void setRefreshVotes(String refreshVotes) {
	this.refreshVotes = refreshVotes;
    }

    /**
     * @return Returns the optionIndex.
     */
    public String getOptionIndex() {
	return optionIndex;
    }

    /**
     * @param optionIndex
     *            The optionIndex to set.
     */
    public void setOptionIndex(String optionIndex) {
	this.optionIndex = optionIndex;
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
     * @return Returns the viewSummary.
     */
    public String getViewSummary() {
	return viewSummary;
    }

    /**
     * @param viewSummary
     *            The viewSummary to set.
     */
    public void setViewSummary(String viewSummary) {
	this.viewSummary = viewSummary;
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
     * @return Returns the optionValue.
     */
    public String getOptionValue() {
	return optionValue;
    }

    /**
     * @param optionValue
     *            The optionValue to set.
     */
    public void setOptionValue(String optionValue) {
	this.optionValue = optionValue;
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
     * @return Returns the redoQuestionsOk.
     */
    public String getRedoQuestionsOk() {
	return redoQuestionsOk;
    }

    /**
     * @param redoQuestionsOk
     *            The redoQuestionsOk to set.
     */
    public void setRedoQuestionsOk(String redoQuestionsOk) {
	this.redoQuestionsOk = redoQuestionsOk;
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
     * @return Returns the donePreview.
     */
    public String getDonePreview() {
	return donePreview;
    }

    /**
     * @param donePreview
     *            The donePreview to set.
     */
    public void setDonePreview(String donePreview) {
	this.donePreview = donePreview;
    }

    /**
     * @return Returns the doneLearnerProgress.
     */
    public String getDoneLearnerProgress() {
	return doneLearnerProgress;
    }

    /**
     * @param doneLearnerProgress
     *            The doneLearnerProgress to set.
     */
    public void setDoneLearnerProgress(String doneLearnerProgress) {
	this.doneLearnerProgress = doneLearnerProgress;
    }

    /**
     * @return Returns the userEntry.
     */
    public String getUserEntry() {
	return userEntry;
    }

    /**
     * @param userEntry
     *            The userEntry to set.
     */
    public void setUserEntry(String userEntry) {
	this.userEntry = userEntry;
    }

    /**
     * @return Returns the dispatch.
     */
    public String getDispatch() {
	return dispatch;
    }

    /**
     * @param dispatch
     *            The dispatch to set.
     */
    public void setDispatch(String dispatch) {
	this.dispatch = dispatch;
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
     * @return Returns the activityRetries.
     */
    public String getActivityRetries() {
	return activityRetries;
    }

    /**
     * @param activityRetries
     *            The activityRetries to set.
     */
    public void setActivityRetries(String activityRetries) {
	this.activityRetries = activityRetries;
    }

    /**
     * @return Returns the lockOnFinish.
     */
    public String getLockOnFinish() {
	return lockOnFinish;
    }

    /**
     * @param lockOnFinish
     *            The lockOnFinish to set.
     */
    public void setLockOnFinish(String lockOnFinish) {
	this.lockOnFinish = lockOnFinish;
    }

    /**
     * @return Returns the useSelectLeaderToolOuput.
     */
    public String getUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    /**
     * @param allowTextEntry
     *            The useSelectLeaderToolOuput to set.
     */
    public void setUseSelectLeaderToolOuput(String useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    /**
     * @return Returns the allowTextEntry.
     */
    public String getAllowTextEntry() {
	return allowTextEntry;
    }

    /**
     * @param allowTextEntry
     *            The allowTextEntry to set.
     */
    public void setAllowTextEntry(String allowTextEntry) {
	this.allowTextEntry = allowTextEntry;
    }

    /**
     * @return Returns the maxNominationCount.
     */
    public String getMaxNominationCount() {
	return maxNominationCount;
    }

    /**
     * @param maxNominationCount
     *            The maxNominationCount to set.
     */
    public void setMaxNominationCount(String maxNominationCount) {
	this.maxNominationCount = maxNominationCount;
    }

    /**
     * @return Returns the minNominationCount.
     */
    public String getMinNominationCount() {
	return minNominationCount;
    }

    /**
     * @param minNominationCount
     *            The minNominationCount to set.
     */
    public void setMinNominationCount(String minNominationCount) {
	this.minNominationCount = minNominationCount;
    }

    /**
     * @return Returns the nominationsSubmited.
     */
    public String getNominationsSubmited() {
	return nominationsSubmited;
    }

    /**
     * @param nominationsSubmited
     *            The nominationsSubmited to set.
     */
    public void setNominationsSubmited(String nominationsSubmited) {
	this.nominationsSubmited = nominationsSubmited;
    }

    /**
     * @return Returns the revisitingUser.
     */
    public String getRevisitingUser() {
	return revisitingUser;
    }

    /**
     * @param revisitingUser
     *            The revisitingUser to set.
     */
    public void setRevisitingUser(String revisitingUser) {
	this.revisitingUser = revisitingUser;
    }

    /**
     * @return Returns the castVoteCount.
     */
    public int getCastVoteCount() {
	return castVoteCount;
    }

    /**
     * @param castVoteCount
     *            The castVoteCount to set.
     */
    public void setCastVoteCount(int castVoteCount) {
	this.castVoteCount = castVoteCount;
    }

    /**
     * @return Returns the castVotes.
     */
    public String getCastVotes() {
	return castVotes;
    }

    /**
     * @param castVotes
     *            The castVotes to set.
     */
    public void setCastVotes(String castVotes) {
	this.castVotes = castVotes;
    }

    /**
     * @return Returns the maxNominationCountReached.
     */
    public String getMaxNominationCountReached() {
	return maxNominationCountReached;
    }

    /**
     * @param maxNominationCountReached
     *            The maxNominationCountReached to set.
     */
    public void setMaxNominationCountReached(String maxNominationCountReached) {
	this.maxNominationCountReached = maxNominationCountReached;
    }

    /**
     * @return Returns the minNominationCountReached.
     */
    public String getMinNominationCountReached() {
	return minNominationCountReached;
    }

    /**
     * @param minNominationCountReached
     *            The minNominationCountReached to set.
     */
    public void setMinNominationCountReached(String minNominationCountReached) {
	this.minNominationCountReached = minNominationCountReached;
    }

    /** Get the votes based on the checkboxes */
    public String[] getCheckedVotes() {
	return checkedVotes;
    }

    /** Get the votes based on the checkboxes as a collection */
    public Collection<String> votesAsCollection() {
	ArrayList<String> votes = new ArrayList<>();
	if (checkedVotes != null) {
	    for (String vote : checkedVotes) {
		votes.add(vote);
	    }
	}
	return votes;
    }

    /** Set the votes based on the checkboxes */
    public void setCheckedVotes(String[] checkedVotes) {
	this.checkedVotes = checkedVotes;
    }

    /**
     * @return Returns the mapGeneralCheckedOptionsContent.
     */
    public Map<String, String> getMapGeneralCheckedOptionsContent() {
	return mapGeneralCheckedOptionsContent;
    }

    /**
     * @param mapGeneralCheckedOptionsContent
     *            The mapGeneralCheckedOptionsContent to set.
     */
    public void setMapGeneralCheckedOptionsContent(Map<String, String> mapGeneralCheckedOptionsContent) {
	this.mapGeneralCheckedOptionsContent = mapGeneralCheckedOptionsContent;
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

    /**
     * @return Returns the reportViewOnly.
     */
    public String getReportViewOnly() {
	return reportViewOnly;
    }

    /**
     * @param reportViewOnly
     *            The reportViewOnly to set.
     */
    public void setReportViewOnly(String reportViewOnly) {
	this.reportViewOnly = reportViewOnly;
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
     * @return Returns the previewOnly.
     */
    public String getPreviewOnly() {
	return previewOnly;
    }

    /**
     * @param previewOnly
     *            The previewOnly to set.
     */
    public void setPreviewOnly(String previewOnly) {
	this.previewOnly = previewOnly;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityInstructions: ", activityInstructions)
		.append("activityTitle: ", activityTitle).append("revisitingUser: ", revisitingUser)
		.append("userEntry: ", userEntry).append("castVoteCount: ", castVoteCount)
		.append("maxNominationCountReached: ", maxNominationCountReached)
		.append("toolSessionID: ", toolSessionID).append("learningMode: ", learningMode)
		.append("toolContentID: ", toolContentID).append("nominationsSubmited: ", nominationsSubmited)
		.append("toolContentUID: ", toolContentUID).append("previewOnly: ", previewOnly)
		.append("reportViewOnly: ", reportViewOnly)
		.append("mapGeneralCheckedOptionsContent: ", mapGeneralCheckedOptionsContent).toString();
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

    public String getShowResults() {
	return showResults;
    }

    public void setShowResults(String showResults) {
	this.showResults = showResults;
    }

    public String getGroupLeaderName() {
	return groupLeaderName;
    }

    public void setGroupLeaderName(String groupLeaderName) {
	this.groupLeaderName = groupLeaderName;
    }

    public String getGroupLeaderUserId() {
	return groupLeaderUserId;
    }

    public void setGroupLeaderUserId(String groupLeaderUserId) {
	this.groupLeaderUserId = groupLeaderUserId;
    }

    public boolean isUserLeader() {
	return isUserLeader;
    }

    public void setIsUserLeader(boolean isUserLeader) {
	this.isUserLeader = isUserLeader;
    }

}
