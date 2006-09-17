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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p> DTO that holds question and candidate answers for the learner environment
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McLearnerAnswersDTO implements Comparable
{
    protected String question;
    
    protected String displayOrder;
    
    protected String questionUid;
    
    protected String weight;
    
    protected String mark;
    
    protected Map candidateAnswers;
    
    protected Map candidateAnswerUids;
    
    protected String attemptCorrect;
    
    protected String feedbackIncorrect;
    
    protected String feedbackCorrect;
    
    
    /**
     * @return Returns the candidateAnswers.
     */
    public Map getCandidateAnswers() {
        return candidateAnswers;
    }
    /**
     * @param candidateAnswers The candidateAnswers to set.
     */
    public void setCandidateAnswers(Map candidateAnswers) {
        this.candidateAnswers = candidateAnswers;
    }
    /**
     * @return Returns the question.
     */
    public String getQuestion() {
        return question;
    }
    /**
     * @param question The question to set.
     */
    public void setQuestion(String question) {
        this.question = question;
    }
    /**
     * @return Returns the weight.
     */
    public String getWeight() {
        return weight;
    }
    /**
     * @param weight The weight to set.
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }
    
    
    
	public String toString() {
        return new ToStringBuilder(this)
            .append("question: ", getQuestion())
            .append("displayOrder: ", getDisplayOrder())
            .append("candidateAnswers: ", getCandidateAnswers())
            .toString();
    }

    
	public int compareTo(Object o)
    {
	    McLearnerAnswersDTO mcLearnerAnswersDTO = (McLearnerAnswersDTO) o;
     
        if (mcLearnerAnswersDTO == null)
        	return 1;
		else
			return 0;
    }
	
    /**
     * @return Returns the displayOrder.
     */
    public String getDisplayOrder() {
        return displayOrder;
    }
    /**
     * @param displayOrder The displayOrder to set.
     */
    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }
    /**
     * @return Returns the questionUid.
     */
    public String getQuestionUid() {
        return questionUid;
    }
    /**
     * @param questionUid The questionUid to set.
     */
    public void setQuestionUid(String questionUid) {
        this.questionUid = questionUid;
    }
    /**
     * @return Returns the candidateAnswerUids.
     */
    public Map getCandidateAnswerUids() {
        return candidateAnswerUids;
    }
    /**
     * @param candidateAnswerUids The candidateAnswerUids to set.
     */
    public void setCandidateAnswerUids(Map candidateAnswerUids) {
        this.candidateAnswerUids = candidateAnswerUids;
    }
    /**
     * @return Returns the feedbackCorrect.
     */
    public String getFeedbackCorrect() {
        return feedbackCorrect;
    }
    /**
     * @param feedbackCorrect The feedbackCorrect to set.
     */
    public void setFeedbackCorrect(String feedbackCorrect) {
        this.feedbackCorrect = feedbackCorrect;
    }
    /**
     * @return Returns the attemptCorrect.
     */
    public String getAttemptCorrect() {
        return attemptCorrect;
    }
    /**
     * @param attemptCorrect The attemptCorrect to set.
     */
    public void setAttemptCorrect(String attemptCorrect) {
        this.attemptCorrect = attemptCorrect;
    }
    /**
     * @return Returns the feedbackIncorrect.
     */
    public String getFeedbackIncorrect() {
        return feedbackIncorrect;
    }
    /**
     * @param feedbackIncorrect The feedbackIncorrect to set.
     */
    public void setFeedbackIncorrect(String feedbackIncorrect) {
        this.feedbackIncorrect = feedbackIncorrect;
    }
    /**
     * @return Returns the mark.
     */
    public String getMark() {
        return mark;
    }
    /**
     * @param mark The mark to set.
     */
    public void setMark(String mark) {
        this.mark = mark;
    }
}
