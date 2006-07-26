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
public class McGeneralLearnerFlowDTO implements Comparable
{
    protected String activityOffline;
    
    protected Integer totalQuestionCount;
    
    protected String activityInstructions;
    
    protected String activityTitle;
    
    protected String countSessionComplete;
    
    protected String toolSessionId;
    
    protected Integer currentQuestionIndex;
    
    protected String questionListingMode;
    
    protected String currentAnswer;
    
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
    
    protected Map mapQuestionContentLearner;
    
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
            .append("activityOffline: ", activityOffline)
            .append("totalQuestionCount : ", totalQuestionCount)
            .append("activityInstructions: ", activityInstructions)
            .append("activityTitle: ", activityTitle)
            .append("countSessionComplete: ", countSessionComplete)
            .append("toolSessionId: ", toolSessionId)
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
}
