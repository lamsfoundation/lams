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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.SortComparator;
import org.lamsfoundation.lams.qb.model.QbToolAnswer;

/**
 * Assessment Result
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laasse10_assessment_result")
public class AssessmentResult {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "start_date")
    private Date startDate;

    //date when user has started activity (pressed start button) that has time limitation
    @Column(name = "time_limit_launched_date")
    private LocalDateTime timeLimitLaunchedDate;

    //indicates the latest retry
    @Column
    private Boolean latest;

    @Column(name = "finish_date")
    private Date finishDate;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "maximum_grade")
    private int maximumGrade;

    @Column
    private float grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_uid")
    private Assessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private AssessmentUser user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "result_uid")
    @SortComparator(QbToolAnswer.QbToolAnswerComparator.class)
    private Set<AssessmentQuestionResult> questionResults = new TreeSet<>();

    // *************** NON Persist Fields ********************
    @Transient
    private Date timeTaken;
    @Transient
    private String overallFeedback;

    /**
     * @return Returns the result Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Assessment getAssessment() {
	return assessment;
    }

    public void setAssessment(Assessment assessment) {
	this.assessment = assessment;
    }

    public AssessmentUser getUser() {
	return user;
    }

    public void setUser(AssessmentUser user) {
	this.user = user;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public LocalDateTime getTimeLimitLaunchedDate() {
	return timeLimitLaunchedDate;
    }

    public void setTimeLimitLaunchedDate(LocalDateTime timeLimitLaunchedDate) {
	this.timeLimitLaunchedDate = timeLimitLaunchedDate;
    }

    public Boolean isLatest() {
	return latest;
    }

    public void setLatest(Boolean latest) {
	this.latest = latest;
    }

    public Date getFinishDate() {
	return finishDate;
    }

    public void setFinishDate(Date finishDate) {
	this.finishDate = finishDate;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

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
