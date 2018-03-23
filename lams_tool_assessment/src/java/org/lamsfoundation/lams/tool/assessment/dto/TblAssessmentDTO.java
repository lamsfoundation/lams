package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.Set;

import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;

public class TblAssessmentDTO {
    private String activityTitle;
    private Assessment assessment;
    private int attemptedLearnersNumber;
    private Set<AssessmentQuestion> questions;

    public String getActivityTitle() {
	return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
	this.activityTitle = activityTitle;
    }

    public Assessment getAssessment() {
	return assessment;
    }

    public void setAssessment(Assessment assessment) {
	this.assessment = assessment;
    }

    public int getAttemptedLearnersNumber() {
	return attemptedLearnersNumber;
    }

    public void setAttemptedLearnersNumber(int attemptedLearnersNumber) {
	this.attemptedLearnersNumber = attemptedLearnersNumber;
    }

    public Set<AssessmentQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Set<AssessmentQuestion> questions) {
	this.questions = questions;
    }

}
