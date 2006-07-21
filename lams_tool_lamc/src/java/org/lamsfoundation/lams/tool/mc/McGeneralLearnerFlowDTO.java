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
package org.lamsfoundation.lams.tool.mc;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;



/**
 * <p> DTO that holds learner flow decision properties and some other view-only properties
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McGeneralLearnerFlowDTO implements Comparable
{
    protected String retries;
    
    protected String learnerMark;
    
    protected String learnerMarkAtLeast;
    
    protected String totalQuestionCount;
    
    protected String passMark;
    
    protected String passMarkApplicable;
    
    protected String userPassed;
    
    protected String userOverPassMark;
    
    protected String reportTitleLearner;
    
    protected String activityInstructions;
    
    protected String activityTitle;
    
    protected String learnerBestMark;
    
    protected String currentQuestionIndex;
    
    protected String countSessionComplete;
    
    protected String topMark;
    
    protected String lowestMark;

    protected String averageMark;
    
    protected String learnerProgress;
    
    protected String learnerProgressUserId;
    
    protected Map mapQueAttempts;
	
    protected Map mapQueCorrectAttempts;
	
    protected Map mapQueIncorrectAttempts;
    
    protected Map mapGeneralOptionsContent;
    
    protected Map mapQuestionsContent;
    
    protected String toolSessionId;

        
    /**
     * @return Returns the toolSessionId.
     */
    public String getToolSessionId() {
        return toolSessionId;
    }
    /**
     * @param toolSessionId The toolSessionId to set.
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
     * @param mapGeneralOptionsContent The mapGeneralOptionsContent to set.
     */
    public void setMapGeneralOptionsContent(Map mapGeneralOptionsContent) {
        this.mapGeneralOptionsContent = mapGeneralOptionsContent;
    }
    /**
     * @return Returns the averageMark.
     */
    public String getAverageMark() {
        return averageMark;
    }
    /**
     * @param averageMark The averageMark to set.
     */
    public void setAverageMark(String averageMark) {
        this.averageMark = averageMark;
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
     * @return Returns the lowestMark.
     */
    public String getLowestMark() {
        return lowestMark;
    }
    /**
     * @param lowestMark The lowestMark to set.
     */
    public void setLowestMark(String lowestMark) {
        this.lowestMark = lowestMark;
    }
    /**
     * @return Returns the topMark.
     */
    public String getTopMark() {
        return topMark;
    }
    /**
     * @param topMark The topMark to set.
     */
    public void setTopMark(String topMark) {
        this.topMark = topMark;
    }
    /**
     * @return Returns the learnerBestMark.
     */
    public String getLearnerBestMark() {
        return learnerBestMark;
    }
    /**
     * @param learnerBestMark The learnerBestMark to set.
     */
    public void setLearnerBestMark(String learnerBestMark) {
        this.learnerBestMark = learnerBestMark;
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
     * @return Returns the learnerMark.
     */
    public String getLearnerMark() {
        return learnerMark;
    }
    /**
     * @param learnerMark The learnerMark to set.
     */
    public void setLearnerMark(String learnerMark) {
        this.learnerMark = learnerMark;
    }
    /**
     * @return Returns the learnerMarkAtLeast.
     */
    public String getLearnerMarkAtLeast() {
        return learnerMarkAtLeast;
    }
    /**
     * @param learnerMarkAtLeast The learnerMarkAtLeast to set.
     */
    public void setLearnerMarkAtLeast(String learnerMarkAtLeast) {
        this.learnerMarkAtLeast = learnerMarkAtLeast;
    }
    /**
     * @return Returns the passMark.
     */
    public String getPassMark() {
        return passMark;
    }
    /**
     * @param passMark The passMark to set.
     */
    public void setPassMark(String passMark) {
        this.passMark = passMark;
    }
    /**
     * @return Returns the passMarkApplicable.
     */
    public String getPassMarkApplicable() {
        return passMarkApplicable;
    }
    /**
     * @param passMarkApplicable The passMarkApplicable to set.
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
     * @param reportTitleLearner The reportTitleLearner to set.
     */
    public void setReportTitleLearner(String reportTitleLearner) {
        this.reportTitleLearner = reportTitleLearner;
    }
    /**
     * @return Returns the totalQuestionCount.
     */
    public String getTotalQuestionCount() {
        return totalQuestionCount;
    }
    /**
     * @param totalQuestionCount The totalQuestionCount to set.
     */
    public void setTotalQuestionCount(String totalQuestionCount) {
        this.totalQuestionCount = totalQuestionCount;
    }
    /**
     * @return Returns the userOverPassMark.
     */
    public String getUserOverPassMark() {
        return userOverPassMark;
    }
    /**
     * @param userOverPassMark The userOverPassMark to set.
     */
    public void setUserOverPassMark(String userOverPassMark) {
        this.userOverPassMark = userOverPassMark;
    }
    
	public int compareTo(Object o)
    {
	    McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = (McGeneralLearnerFlowDTO) o;
     
        if (mcGeneralLearnerFlowDTO == null)
        	return 1;
		else
			return 0;
    }

	public String toString() {
        return new ToStringBuilder(this)
            .append("retries: ", retries)
            .append("learnerMark : ", learnerMark)
            .append("learnerMarkAtLeast: ", learnerMarkAtLeast)
            .append("totalQuestionCount: ", totalQuestionCount)
            .append("passMark: ", passMark)
            .append("passMarkApplicable: ", passMarkApplicable)
            .append("userPassed: ", userPassed)
            .append("userOverPassMark: ", userOverPassMark)
            .append("reportTitleLearner: ", reportTitleLearner)
            .append("activityInstructions: ", activityInstructions)
            .append("activityTitle: ", activityTitle)
            .toString();
    }

	
    /**
     * @return Returns the retries.
     */
    public String getRetries() {
        return retries;
    }
    /**
     * @param retries The retries to set.
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
     * @param activityTitle The activityTitle to set.
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
     * @param userPassed The userPassed to set.
     */
    public void setUserPassed(String userPassed) {
        this.userPassed = userPassed;
    }    
    /**
     * @return Returns the currentQuestionIndex.
     */
    public String getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    /**
     * @param currentQuestionIndex The currentQuestionIndex to set.
     */
    public void setCurrentQuestionIndex(String currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }
    /**
     * @return Returns the mapQueAttempts.
     */
    public Map getMapQueAttempts() {
        return mapQueAttempts;
    }
    /**
     * @param mapQueAttempts The mapQueAttempts to set.
     */
    public void setMapQueAttempts(Map mapQueAttempts) {
        this.mapQueAttempts = mapQueAttempts;
    }
    /**
     * @return Returns the mapQueCorrectAttempts.
     */
    public Map getMapQueCorrectAttempts() {
        return mapQueCorrectAttempts;
    }
    /**
     * @param mapQueCorrectAttempts The mapQueCorrectAttempts to set.
     */
    public void setMapQueCorrectAttempts(Map mapQueCorrectAttempts) {
        this.mapQueCorrectAttempts = mapQueCorrectAttempts;
    }
    /**
     * @return Returns the mapQueIncorrectAttempts.
     */
    public Map getMapQueIncorrectAttempts() {
        return mapQueIncorrectAttempts;
    }
    /**
     * @param mapQueIncorrectAttempts The mapQueIncorrectAttempts to set.
     */
    public void setMapQueIncorrectAttempts(Map mapQueIncorrectAttempts) {
        this.mapQueIncorrectAttempts = mapQueIncorrectAttempts;
    }
    /**
     * @return Returns the learnerProgress.
     */
    public String getLearnerProgress() {
        return learnerProgress;
    }
    /**
     * @param learnerProgress The learnerProgress to set.
     */
    public void setLearnerProgress(String learnerProgress) {
        this.learnerProgress = learnerProgress;
    }
    /**
     * @return Returns the learnerProgressUserId.
     */
    public String getLearnerProgressUserId() {
        return learnerProgressUserId;
    }
    /**
     * @param learnerProgressUserId The learnerProgressUserId to set.
     */
    public void setLearnerProgressUserId(String learnerProgressUserId) {
        this.learnerProgressUserId = learnerProgressUserId;
    }
    /**
     * @return Returns the mapQuestionsContent.
     */
    public Map getMapQuestionsContent() {
        return mapQuestionsContent;
    }
    /**
     * @param mapQuestionsContent The mapQuestionsContent to set.
     */
    public void setMapQuestionsContent(Map mapQuestionsContent) {
        this.mapQuestionsContent = mapQuestionsContent;
    }
}
