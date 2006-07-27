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
    
    protected String toolSessionID;
    
    protected String httpSessionID;
    
    protected String toolContentID;
    
    protected Integer currentQuestionIndex;
    
    protected String questionListingMode;
    
    protected String currentAnswer;
    
    protected String reportTitleLearner;
    
    protected String endLearningMessage;
    
    protected String userNameVisible;
    
    protected Map mapAnswers;
    
    protected Map mapQuestions;
    
    protected Map mapQuestionContentLearner;
    
    
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
}
