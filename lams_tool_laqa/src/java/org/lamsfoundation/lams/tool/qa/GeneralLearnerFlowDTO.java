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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;




/**
 * <p> DTO that holds learner flow decision properties and some other view-only properties
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class GeneralLearnerFlowDTO implements Comparable
{
    protected String activityOffline;
    
    protected Integer totalQuestionCount;
    
    protected String activityInstructions;
    
    protected String activityTitle;
    
    protected String countSessionComplete;
    
    protected String userName;
    
    protected String toolSessionID;
    
    protected String httpSessionID;
    
    protected String toolContentID;
    
    protected Integer currentQuestionIndex;
    
    protected String questionListingMode;
    
    protected String currentAnswer;
    
    protected String notebookEntriesVisible;
    
    protected String reflection;
    
    protected String reflectionSubject;
    
    protected String notebookEntry;
    
    protected String reportTitleLearner;
    
    protected String endLearningMessage;
    
    protected String userNameVisible;
    
	protected String requestLearningReport;

	protected String requestLearningReportProgress;
	
	protected String requestLearningReportViewOnly;

    protected Map mapAnswers;
    
    protected Map mapAnswersPresentable;
    
    protected Map mapQuestions;
    
    protected Map mapQuestionContentLearner;
    
    protected List listMonitoredAnswersContainerDTO;
    
    protected String currentMonitoredToolSession;
    
    
    /**
     * @return Returns the currentMonitoredToolSession.
     */
    public String getCurrentMonitoredToolSession() {
        return currentMonitoredToolSession;
    }
    /**
     * @param currentMonitoredToolSession The currentMonitoredToolSession to set.
     */
    public void setCurrentMonitoredToolSession(
            String currentMonitoredToolSession) {
        this.currentMonitoredToolSession = currentMonitoredToolSession;
    }
    /**
     * @return Returns the currentAnswer.
     */
    public String getCurrentAnswer() {
        return currentAnswer;
    }
    /**
     * @param currentAnswer The currentAnswer to set.
     */
    public void setCurrentAnswer(String currentAnswer) {
        this.currentAnswer = currentAnswer;
    }
    protected String userFeedback;
    
    
    
    /**
     * @return Returns the mapQuestionContentLearner.
     */
    public Map getMapQuestionContentLearner() {
        return mapQuestionContentLearner;
    }
    /**
     * @param mapQuestionContentLearner The mapQuestionContentLearner to set.
     */
    public void setMapQuestionContentLearner(Map mapQuestionContentLearner) {
        this.mapQuestionContentLearner = mapQuestionContentLearner;
    }
    /**
     * @return Returns the userFeedback.
     */
    public String getUserFeedback() {
        return userFeedback;
    }
    /**
     * @param userFeedback The userFeedback to set.
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
     * @param activityInstructions The activityInstructions to set.
     */
    public void setActivityInstructions(String activityInstructions) {
        this.activityInstructions = activityInstructions;
    }
    /**
     * @return Returns the activityOffline.
     */
    public String getActivityOffline() {
        return activityOffline;
    }
    /**
     * @param activityOffline The activityOffline to set.
     */
    public void setActivityOffline(String activityOffline) {
        this.activityOffline = activityOffline;
    }
    /**
     * @return Returns the activityTitle.
     */
    public String getActivityTitle() {
        return activityTitle;
    }
    /**
     * @param activityTitle The activityTitle to set.
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
     * @param countSessionComplete The countSessionComplete to set.
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
     * @param questionListingMode The questionListingMode to set.
     */
    public void setQuestionListingMode(String questionListingMode) {
        this.questionListingMode = questionListingMode;
    }
    
	public int compareTo(Object o)
    {
	    GeneralLearnerFlowDTO gneralLearnerFlowDTO = (GeneralLearnerFlowDTO) o;
     
        if (gneralLearnerFlowDTO == null)
        	return 1;
		else
			return 0;
    }

	
	public String toString() {
        return new ToStringBuilder(this)
            .append("activityOffline: ", activityOffline)
            .append("totalQuestionCount : ", totalQuestionCount)
            .append("activityInstructions: ", activityInstructions)
            .append("activityTitle: ", activityTitle)
            .append("countSessionComplete: ", countSessionComplete)
            .append("toolSessionID: ", toolSessionID)
            .append("currentQuestionIndex: ", currentQuestionIndex)
            .append("questionListingMode: ", questionListingMode)
            .append("reportTitleLearner: ", reportTitleLearner)            
            .append("userNameVisible: ", userNameVisible)
            .append("requestLearningReport: ", requestLearningReport)            
            .append("requestLearningReportProgress: ", requestLearningReportProgress)            
            .append("requestLearningReportViewOnly: ", requestLearningReportViewOnly)            
            .append("mapAnswers: ", mapAnswers)            
            .append("mapQuestions: ", mapQuestions)            
            .append("mapQuestionContentLearner: ", mapQuestionContentLearner)            
            .append("listMonitoredAnswersContainerDTO: ", listMonitoredAnswersContainerDTO)            
            .append("currentMonitoredToolSession: ", currentMonitoredToolSession)
            .toString();
    }
    

    /**
     * @param currentQuestionIndex The currentQuestionIndex to set.
     */
    public void setCurrentQuestionIndex(Integer currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }
    /**
     * @param totalQuestionCount The totalQuestionCount to set.
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
     * @param toolContentID The toolContentID to set.
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
     * @param toolSessionID The toolSessionID to set.
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
     * @return Returns the endLearningMessage.
     */
    public String getEndLearningMessage() {
        return endLearningMessage;
    }
    /**
     * @param endLearningMessage The endLearningMessage to set.
     */
    public void setEndLearningMessage(String endLearningMessage) {
        this.endLearningMessage = endLearningMessage;
    }
    /**
     * @return Returns the reportTitleLearner.
     */
    public String getReportTitleLearner() {
        return reportTitleLearner;
    }
    /**
     * @param reportTitleLearner The reportTitleLearner to set.
     */
    public void setReportTitleLearner(String reportTitleLearner) {
        this.reportTitleLearner = reportTitleLearner;
    }
    /**
     * @return Returns the userNameVisible.
     */
    public String getUserNameVisible() {
        return userNameVisible;
    }
    /**
     * @param userNameVisible The userNameVisible to set.
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
     * @param mapAnswers The mapAnswers to set.
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
     * @param mapQuestions The mapQuestions to set.
     */
    public void setMapQuestions(Map mapQuestions) {
        this.mapQuestions = mapQuestions;
    }
    
    /**
     * @return Returns the httpSessionID.
     */
    public String getHttpSessionID() {
        return httpSessionID;
    }
    /**
     * @param httpSessionID The httpSessionID to set.
     */
    public void setHttpSessionID(String httpSessionID) {
        this.httpSessionID = httpSessionID;
    }    
    /**
     * @return Returns the requestLearningReport.
     */
    public String getRequestLearningReport() {
        return requestLearningReport;
    }
    /**
     * @param requestLearningReport The requestLearningReport to set.
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
     * @param requestLearningReportProgress The requestLearningReportProgress to set.
     */
    public void setRequestLearningReportProgress(
            String requestLearningReportProgress) {
        this.requestLearningReportProgress = requestLearningReportProgress;
    }
    
    /**
     * @return Returns the requestLearningReportViewOnly.
     */
    public String getRequestLearningReportViewOnly() {
        return requestLearningReportViewOnly;
    }
    /**
     * @param requestLearningReportViewOnly The requestLearningReportViewOnly to set.
     */
    public void setRequestLearningReportViewOnly(
            String requestLearningReportViewOnly) {
        this.requestLearningReportViewOnly = requestLearningReportViewOnly;
    }

    /**
     * @return Returns the listMonitoredAnswersContainerDTO.
     */
    public List getListMonitoredAnswersContainerDTO() {
        return listMonitoredAnswersContainerDTO;
    }
    /**
     * @param listMonitoredAnswersContainerDTO The listMonitoredAnswersContainerDTO to set.
     */
    public void setListMonitoredAnswersContainerDTO(
            List listMonitoredAnswersContainerDTO) {
        this.listMonitoredAnswersContainerDTO = listMonitoredAnswersContainerDTO;
    }

    /**
     * @return Returns the mapAnswersPresentable.
     */
    public Map getMapAnswersPresentable() {
        return mapAnswersPresentable;
    }
    /**
     * @param mapAnswersPresentable The mapAnswersPresentable to set.
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
     * @param reflection The reflection to set.
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
     * @param reflectionSubject The reflectionSubject to set.
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
     * @param notebookEntry The notebookEntry to set.
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
     * @param notebookEntriesVisible The notebookEntriesVisible to set.
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
     * @param userName The userName to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
