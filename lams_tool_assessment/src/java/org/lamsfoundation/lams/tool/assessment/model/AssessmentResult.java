/****************************************************************
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.assessment.model;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.assessment.util.AssessmentQuestionResultComparator;

/**
 * Assessment Result
 *
 * @author Andrey Balan
 */
public class AssessmentResult {

    private Long uid;
    private Assessment assessment;
    private Date startDate;
    //date when user has started activity (pressed start button) that has time limitation
    private Date timeLimitLaunchedDate;
    //indicates the latest retry
    private boolean isLatest;
    private Date finishDate;
    private AssessmentUser user;
    private Long sessionId;
    private int maximumGrade;
    private float grade;
    private Set<AssessmentQuestionResult> questionResults;

    // DTO fields:
    private Date timeTaken;
    private String overallFeedback;

    public AssessmentResult() {
	questionResults = new TreeSet<AssessmentQuestionResult>(new AssessmentQuestionResultComparator());
    }

    /**
     *
     * @return Returns the result Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     * @return
     */
    public Assessment getAssessment() {
	return assessment;
    }

    public void setAssessment(Assessment assessment) {
	this.assessment = assessment;
    }

    /**
     *
     * @return
     */
    public AssessmentUser getUser() {
	return user;
    }

    public void setUser(AssessmentUser user) {
	this.user = user;
    }

    /**
     *
     * @return
     */
    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }
    
    /**
    *
    * @return
    */
   public Date getTimeLimitLaunchedDate() {
	return timeLimitLaunchedDate;
   }

   public void setTimeLimitLaunchedDate(Date timeLimitLaunchedDate) {
	this.timeLimitLaunchedDate = timeLimitLaunchedDate;
   }

    /**
     *
     * @return
     */
    public boolean isLatest() {
	return isLatest;
    }

    public void setLatest(boolean isLatest) {
	this.isLatest = isLatest;
    }

    /**
     *
     * @return
     */
    public Date getFinishDate() {
	return finishDate;
    }

    public void setFinishDate(Date finishDate) {
	this.finishDate = finishDate;
    }

    /**
     *
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     *
     * @return
     */
    public int getMaximumGrade() {
	return maximumGrade;
    }

    public void setMaximumGrade(int maximumGrade) {
	this.maximumGrade = maximumGrade;
    }

    /**
     * Overall assessment grade (incl all penalties)
     *
     *
     * @return
     */
    public float getGrade() {
	return grade;
    }

    /**
     * Overall assessment grade (incl all penalties)
     *
     * @param grade
     */
    public void setGrade(float grade) {
	this.grade = grade;
    }

    /**
     *
     *
     *
     *
     *
     * @return a set of answerOptions to this AssessmentQuestion.
     */
    public Set<AssessmentQuestionResult> getQuestionResults() {
	return questionResults;
    }

    /**
     * @param answerOptions
     *            answerOptions to set.
     */
    public void setQuestionResults(Set<AssessmentQuestionResult> questionResults) {
	this.questionResults = questionResults;
    }

    public Date getTimeTaken() {
	return timeTaken;
    }

    public void setTimeTaken(Date timeTaken) {
	this.timeTaken = timeTaken;
    }

    public String getOverallFeedback() {
	return overallFeedback;
    }

    public void setOverallFeedback(String overallFeedback) {
	this.overallFeedback = overallFeedback;
    }
}
