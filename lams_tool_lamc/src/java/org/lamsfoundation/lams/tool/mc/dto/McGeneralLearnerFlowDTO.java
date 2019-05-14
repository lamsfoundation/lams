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

package org.lamsfoundation.lams.tool.mc.dto;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.lamsfoundation.lams.tool.mc.model.McUsrAttempt;

/**
 * <p>
 * DTO that holds learner flow decision properties and some other view-only properties
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class McGeneralLearnerFlowDTO implements Comparable {
    protected String retries;

    protected Integer learnerMark;

    protected String userName;

    protected Integer totalQuestionCount;

    protected Integer passMark;

    protected String passMarkApplicable;

    protected String userPassed;

    protected String userOverPassMark;

    protected String reportTitleLearner;

    protected String activityInstructions;

    protected String activityTitle;

    protected Integer currentQuestionIndex;

    protected String countSessionComplete;

    protected Integer topMark;

    protected String reflection;

    protected String reflectionSubject;

    protected String notebookEntry;

    protected String notebookEntriesVisible;

    protected Integer lowestMark;

    protected Integer averageMark;

    protected String showMarks;

    protected String displayAnswers;
    protected String displayFeedbackOnly;

    protected Map mapGeneralOptionsContent;

    protected List<McQueContent> questions;

    protected String toolSessionId;

    protected String totalCountReached;

    protected Integer questionIndex;

    protected Integer totalMarksPossible;

    protected Map mapFeedbackContent;

    protected Map<String, McUsrAttempt> attemptMap;

    /**
     * @return Returns the mapFeedbackContent.
     */
    public Map getMapFeedbackContent() {
	return mapFeedbackContent;
    }

    /**
     * @param mapFeedbackContent
     *            The mapFeedbackContent to set.
     */
    public void setMapFeedbackContent(Map mapFeedbackContent) {
	this.mapFeedbackContent = mapFeedbackContent;
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
     * @return Returns the totalCountReached.
     */
    public String getTotalCountReached() {
	return totalCountReached;
    }

    /**
     * @param totalCountReached
     *            The totalCountReached to set.
     */
    public void setTotalCountReached(String totalCountReached) {
	this.totalCountReached = totalCountReached;
    }

    /**
     * @return Returns the toolSessionId.
     */
    public String getToolSessionId() {
	return toolSessionId;
    }

    /**
     * @param toolSessionId
     *            The toolSessionId to set.
     */
    public void setToolSessionId(String toolSessionId) {
	this.toolSessionId = toolSessionId;
    }

    /**
     * @return Returns the mapGeneralOptionsContent.
     */
    public Map getMapGeneralOptionsContent() {
	return mapGeneralOptionsContent;
    }

    /**
     * @param mapGeneralOptionsContent
     *            The mapGeneralOptionsContent to set.
     */
    public void setMapGeneralOptionsContent(Map mapGeneralOptionsContent) {
	this.mapGeneralOptionsContent = mapGeneralOptionsContent;
    }

    /**
     * @return Returns the averageMark.
     */
    public Integer getAverageMark() {
	return averageMark;
    }

    /**
     * @param averageMark
     *            The averageMark to set.
     */
    public void setAverageMark(Integer averageMark) {
	this.averageMark = averageMark;
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
     * @return Returns the lowestMark.
     */
    public Integer getLowestMark() {
	return lowestMark;
    }

    /**
     * @param lowestMark
     *            The lowestMark to set.
     */
    public void setLowestMark(Integer lowestMark) {
	this.lowestMark = lowestMark;
    }

    /**
     * @return Returns the topMark.
     */
    public Integer getTopMark() {
	return topMark;
    }

    /**
     * @param topMark
     *            The topMark to set.
     */
    public void setTopMark(Integer topMark) {
	this.topMark = topMark;
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
     * @return Returns the learnerMark.
     */
    public Integer getLearnerMark() {
	return learnerMark;
    }

    /**
     * @param learnerMark
     *            The learnerMark to set.
     */
    public void setLearnerMark(Integer learnerMark) {
	this.learnerMark = learnerMark;
    }

    /**
     * @return Returns the passMark.
     */
    public Integer getPassMark() {
	return passMark;
    }

    /**
     * @param passMark
     *            The passMark to set.
     */
    public void setPassMark(Integer passMark) {
	this.passMark = passMark;
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
     * @return Returns the reportTitleLearner.
     */
    public String getReportTitleLearner() {
	return reportTitleLearner;
    }

    /**
     * @param reportTitleLearner
     *            The reportTitleLearner to set.
     */
    public void setReportTitleLearner(String reportTitleLearner) {
	this.reportTitleLearner = reportTitleLearner;
    }

    /**
     * @return Returns the totalQuestionCount.
     */
    public Integer getTotalQuestionCount() {
	return totalQuestionCount;
    }

    /**
     * @param totalQuestionCount
     *            The totalQuestionCount to set.
     */
    public void setTotalQuestionCount(Integer totalQuestionCount) {
	this.totalQuestionCount = totalQuestionCount;
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

    @Override
    public int compareTo(Object o) {
	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = (McGeneralLearnerFlowDTO) o;

	if (mcGeneralLearnerFlowDTO == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("retries: ", retries)
		.append("mapGeneralOptionsContent: ", mapGeneralOptionsContent).append("learnerMark : ", learnerMark)
		.append("totalQuestionCount: ", totalQuestionCount).append("passMark: ", passMark)
		.append("passMarkApplicable: ", passMarkApplicable).append("userPassed: ", userPassed)
		.append("userOverPassMark: ", userOverPassMark).append("reportTitleLearner: ", reportTitleLearner)
		.append("activityInstructions: ", activityInstructions).append("activityTitle: ", activityTitle)
		.append("displayAnswers: ", displayAnswers).append("displayFeedbackOnly: ", displayFeedbackOnly)
		.append("reflection: ", reflection).append("reflectionSubject: ", reflectionSubject)
		.append("notebookEntry: ", notebookEntry).append("notebookEntriesVisible: ", notebookEntriesVisible)
		.toString();
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
     * @return Returns the userPassed.
     */
    public String getUserPassed() {
	return userPassed;
    }

    /**
     * @param userPassed
     *            The userPassed to set.
     */
    public void setUserPassed(String userPassed) {
	this.userPassed = userPassed;
    }

    /**
     * @return Returns the currentQuestionIndex.
     */
    public Integer getCurrentQuestionIndex() {
	return currentQuestionIndex;
    }

    /**
     * @param currentQuestionIndex
     *            The currentQuestionIndex to set.
     */
    public void setCurrentQuestionIndex(Integer currentQuestionIndex) {
	this.currentQuestionIndex = currentQuestionIndex;
    }

    /**
     * @return Returns the questions.
     */
    public List<McQueContent> getQuestions() {
	return questions;
    }

    /**
     * @param questions
     *            The questions to set.
     */
    public void setQuestions(List<McQueContent> questions) {
	this.questions = questions;
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
     * @return Returns the totalMarksPossible.
     */
    public Integer getTotalMarksPossible() {
	return totalMarksPossible;
    }

    /**
     * @param totalMarksPossible
     *            The totalMarksPossible to set.
     */
    public void setTotalMarksPossible(Integer totalMarksPossible) {
	this.totalMarksPossible = totalMarksPossible;
    }

    /**
     * If using for a display screen then showMarks controls whether or not to show the average and top marks for the
     * session. If using for the "get answers" screens, then this controls whether or not to show the marks for each
     * questionDescription - this allows us to NOT show the marks if ALL of the questions have a mark of 1.
     *
     * @return Returns the showMarks.
     */
    public String getShowMarks() {
	return showMarks;
    }

    /**
     * See getShowMarks() for the meaning of "showMarks".
     *
     * @param showMarks
     *            The showMarks to set.
     */
    public void setShowMarks(String showMarks) {
	this.showMarks = showMarks;
    }

    /**
     * Displays answers on the screen
     *
     * @return Returns the displayAnswers.
     */
    public String getDisplayAnswers() {
	return displayAnswers;
    }

    /**
     * See getDisplayAnswers() for the meaning of "displayAnswers".
     *
     * @param displayAnswers
     *            The displayAnswers to set.
     */
    public void setDisplayAnswers(String displayAnswers) {
	this.displayAnswers = displayAnswers;
    }

    public String getDisplayFeedbackOnly() {
	return displayFeedbackOnly;
    }

    public void setDisplayFeedbackOnly(String displayFeedbackOnly) {
	this.displayFeedbackOnly = displayFeedbackOnly;
    }

    /**
     * @return Returns the attemptMap.
     */
    public Map<String, McUsrAttempt> getAttemptMap() {
	return attemptMap;
    }

    /**
     * @param attemptMap
     *            The attemptMap to set.
     */
    public void setAttemptMap(Map<String, McUsrAttempt> attemptMap) {
	this.attemptMap = attemptMap;
    }
}
