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

package org.lamsfoundation.lams.tool.qa.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.lamsfoundation.lams.tool.qa.model.QaUsrResp;

/**
 * <p>
 * DTO that holds learner flow decision properties and some other view-only properties
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class GeneralLearnerFlowDTO implements Comparable {

    protected Integer totalQuestionCount;

    protected String activityInstructions;

    protected String activityTitle;

    protected String countSessionComplete;

    protected String userName;

    protected String toolSessionID;

    protected String sessionMapID;

    protected String toolContentID;

    protected Integer currentQuestionIndex;

    protected String questionListingMode;

    protected String currentAnswer;

    protected String remainingQuestionCount;

    protected String teacherViewOnly;

    protected String notebookEntriesVisible;

    protected String reflection;

    protected String reflectionSubject;

    protected String notebookEntry;

    protected String userNameVisible;

    protected boolean showOtherAnswers;

    protected String requestLearningReport;

    protected String requestLearningReportProgress;

    protected String requestLearningReportViewOnly;

    protected Map mapAnswers;

    protected Map mapAnswersPresentable;

    protected Map mapQuestions;

    protected Map<Integer, QaQuestionDTO> mapQuestionContentLearner;

    protected Set<QaQueContent> questions;

    protected List<QaUsrResp> userResponses;

    protected String initialScreen;

    protected String lockWhenFinished;

    protected boolean noReeditAllowed;

    protected String userUid;

    protected String usernameVisible;

    protected String allowRichEditor;

    protected String useSelectLeaderToolOuput;

    protected boolean allowRateAnswers;

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
     * @return Returns the currentAnswer.
     */
    public String getCurrentAnswer() {
	return currentAnswer;
    }

    /**
     * @param currentAnswer
     *            The currentAnswer to set.
     */
    public void setCurrentAnswer(String currentAnswer) {
	this.currentAnswer = currentAnswer;
    }

    protected String userFeedback;

    /**
     * @return Returns the mapQuestionContentLearner.
     */
    public Map<Integer, QaQuestionDTO> getMapQuestionContentLearner() {
	return mapQuestionContentLearner;
    }

    /**
     * @param mapQuestionContentLearner
     *            The mapQuestionContentLearner to set.
     */
    public void setMapQuestionContentLearner(Map<Integer, QaQuestionDTO> mapQuestionContentLearner) {
	this.mapQuestionContentLearner = mapQuestionContentLearner;
    }

    /**
     * @return Returns the userFeedback.
     */
    public String getUserFeedback() {
	return userFeedback;
    }

    /**
     * @param userFeedback
     *            The userFeedback to set.
     */
    public void setUserFeedback(String userFeedback) {
	this.userFeedback = userFeedback;
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
     * @return Returns the countSessionComplete.
     */
    public String getCountSessionComplete() {
	return countSessionComplete;
    }

    /**
     * @param countSessionComplete
     *            The countSessionComplete to set.
     */
    public void setCountSessionComplete(String countSessionComplete) {
	this.countSessionComplete = countSessionComplete;
    }

    /**
     * @return Returns the questionListingMode.
     */
    public String getQuestionListingMode() {
	return questionListingMode;
    }

    /**
     * @param questionListingMode
     *            The questionListingMode to set.
     */
    public void setQuestionListingMode(String questionListingMode) {
	this.questionListingMode = questionListingMode;
    }

    @Override
    public int compareTo(Object o) {
	GeneralLearnerFlowDTO gneralLearnerFlowDTO = (GeneralLearnerFlowDTO) o;

	if (gneralLearnerFlowDTO == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("remainingQuestionCount: ", remainingQuestionCount)
		.append("totalQuestionCount : ", totalQuestionCount)
		.append("activityInstructions: ", activityInstructions).append("teacherViewOnly: ", teacherViewOnly)
		.append("lockWhenFinished: ", lockWhenFinished).append("activityTitle: ", activityTitle)
		.append("countSessionComplete: ", countSessionComplete).append("toolSessionID: ", toolSessionID)
		.append("currentQuestionIndex: ", currentQuestionIndex)
		.append("questionListingMode: ", questionListingMode)
		.append("userNameVisible: ", userNameVisible).append("requestLearningReport: ", requestLearningReport)
		.append("requestLearningReportProgress: ", requestLearningReportProgress)
		.append("requestLearningReportViewOnly: ", requestLearningReportViewOnly)
		.append("mapAnswers: ", mapAnswers).append("mapQuestions: ", mapQuestions)
		.append("mapQuestionContentLearner: ", mapQuestionContentLearner).toString();
    }

    /**
     * @param currentQuestionIndex
     *            The currentQuestionIndex to set.
     */
    public void setCurrentQuestionIndex(Integer currentQuestionIndex) {
	this.currentQuestionIndex = currentQuestionIndex;
    }

    /**
     * @param totalQuestionCount
     *            The totalQuestionCount to set.
     */
    public void setTotalQuestionCount(Integer totalQuestionCount) {
	this.totalQuestionCount = totalQuestionCount;
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
     * @return Returns the currentQuestionIndex.
     */
    public Integer getCurrentQuestionIndex() {
	return currentQuestionIndex;
    }

    /**
     * @return Returns the totalQuestionCount.
     */
    public Integer getTotalQuestionCount() {
	return totalQuestionCount;
    }

    /**
     * @return Returns the userNameVisible.
     */
    public String getUserNameVisible() {
	return userNameVisible;
    }

    /**
     * @param userNameVisible
     *            The userNameVisible to set.
     */
    public void setUserNameVisible(String userNameVisible) {
	this.userNameVisible = userNameVisible;
    }

    /**
     * @return Returns the mapAnswers.
     */
    public Map getMapAnswers() {
	return mapAnswers;
    }

    /**
     * @param mapAnswers
     *            The mapAnswers to set.
     */
    public void setMapAnswers(Map mapAnswers) {
	this.mapAnswers = mapAnswers;
    }

    /**
     * @return Returns the mapQuestions.
     */
    public Map getMapQuestions() {
	return mapQuestions;
    }

    /**
     * @param mapQuestions
     *            The mapQuestions to set.
     */
    public void setMapQuestions(Map mapQuestions) {
	this.mapQuestions = mapQuestions;
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
     * @return Returns the questions.
     */
    public List<QaUsrResp> getUserResponses() {
	return userResponses;
    }

    /**
     * @param questions
     *            The questions to set.
     */
    public void setUserResponses(List<QaUsrResp> userResponses) {
	this.userResponses = userResponses;
    }

    /**
     * @return Returns the questions.
     */
    public Set<QaQueContent> getQuestions() {
	return questions;
    }

    /**
     * @param questions
     *            The questions to set.
     */
    public void setQuestions(Set<QaQueContent> questions) {
	this.questions = questions;
    }

    /**
     * @return Returns the mapAnswersPresentable.
     */
    public Map getMapAnswersPresentable() {
	return mapAnswersPresentable;
    }

    /**
     * @param mapAnswersPresentable
     *            The mapAnswersPresentable to set.
     */
    public void setMapAnswersPresentable(Map mapAnswersPresentable) {
	this.mapAnswersPresentable = mapAnswersPresentable;
    }

    /**
     * @return Returns the reflection.
     */
    public String getReflection() {
	return reflection;
    }

    /**
     * @param reflection
     *            The reflection to set.
     */
    public void setReflection(String reflection) {
	this.reflection = reflection;
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
     * @return Returns the notebookEntry.
     */
    public String getNotebookEntry() {
	return notebookEntry;
    }

    /**
     * @param notebookEntry
     *            The notebookEntry to set.
     */
    public void setNotebookEntry(String notebookEntry) {
	this.notebookEntry = notebookEntry;
    }

    /**
     * @return Returns the notebookEntriesVisible.
     */
    public String getNotebookEntriesVisible() {
	return notebookEntriesVisible;
    }

    /**
     * @param notebookEntriesVisible
     *            The notebookEntriesVisible to set.
     */
    public void setNotebookEntriesVisible(String notebookEntriesVisible) {
	this.notebookEntriesVisible = notebookEntriesVisible;
    }

    /**
     * @return Returns the userName.
     */
    public String getUserName() {
	return userName;
    }

    /**
     * @param userName
     *            The userName to set.
     */
    public void setUserName(String userName) {
	this.userName = userName;
    }

    /**
     * @return Returns the teacherViewOnly.
     */
    public String getTeacherViewOnly() {
	return teacherViewOnly;
    }

    /**
     * @param teacherViewOnly
     *            The teacherViewOnly to set.
     */
    public void setTeacherViewOnly(String teacherViewOnly) {
	this.teacherViewOnly = teacherViewOnly;
    }

    /**
     * @return Returns the remainingQuestionCount.
     */
    public String getRemainingQuestionCount() {
	return remainingQuestionCount;
    }

    /**
     * @param remainingQuestionCount
     *            The remainingQuestionCount to set.
     */
    public void setRemainingQuestionCount(String remainingQuestionCount) {
	this.remainingQuestionCount = remainingQuestionCount;
    }

    /**
     * @return Returns the initialScreen.
     */
    public String getInitialScreen() {
	return initialScreen;
    }

    /**
     * @param initialScreen
     *            The initialScreen to set.
     */
    public void setInitialScreen(String initialScreen) {
	this.initialScreen = initialScreen;
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
    public boolean getNoReeditAllowed() {
	return noReeditAllowed;
    }

    /**
     * @param noReeditAllowed
     *            The noReeditAllowed to set.
     */
    public void setNoReeditAllowed(boolean noReeditAllowed) {
	this.noReeditAllowed = noReeditAllowed;
    }

    /**
     * @return Returns the showOtherAnswers.
     */
    public boolean getShowOtherAnswers() {
	return showOtherAnswers;
    }

    /**
     * @param showOtherAnswers
     *            The showOtherAnswers to set.
     */
    public void setShowOtherAnswers(boolean showOtherAnswers) {
	this.showOtherAnswers = showOtherAnswers;
    }

    /**
     * @return Returns the userUid.
     */
    public String getUserUid() {
	return userUid;
    }

    /**
     * @param userUid
     *            The userUid to set.
     */
    public void setUserUid(String userUid) {
	this.userUid = userUid;
    }

    public String getAllowRichEditor() {
	return allowRichEditor;
    }

    public void setAllowRichEditor(String allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    public String getUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(String useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    public boolean isAllowRateAnswers() {
	return allowRateAnswers;
    }

    public void setAllowRateAnswers(boolean allowRateAnswers) {
	this.allowRateAnswers = allowRateAnswers;
    }
}
