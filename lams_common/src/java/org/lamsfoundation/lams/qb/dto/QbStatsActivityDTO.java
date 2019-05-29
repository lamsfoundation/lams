package org.lamsfoundation.lams.qb.dto;

import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.model.QbQuestion;

public class QbStatsActivityDTO {
    private ToolActivity activity;
    private QbQuestion qbQuestion;
    private String monitorURL;
    private Integer participantCount;
    private Double difficultyIndex;
    private Double discriminationIndex;
    private Double pointBiserial;

    public ToolActivity getActivity() {
	return activity;
    }

    public void setActivity(ToolActivity activity) {
	this.activity = activity;
    }

    public QbQuestion getQbQuestion() {
	return qbQuestion;
    }

    public void setQbQuestion(QbQuestion qbQuestion) {
	this.qbQuestion = qbQuestion;
    }

    public String getMonitorURL() {
	return monitorURL;
    }

    public void setMonitorURL(String monitorURL) {
	this.monitorURL = monitorURL;
    }

    public Integer getParticipantCount() {
	return participantCount;
    }

    public void setParticipantCount(Integer testParticipantCount) {
	this.participantCount = testParticipantCount;
    }

    public Double getDifficultyIndex() {
	return difficultyIndex;
    }

    public void setDifficultyIndex(Double difficultyIndex) {
	this.difficultyIndex = difficultyIndex;
    }

    public Double getDiscriminationIndex() {
	return discriminationIndex;
    }

    public void setDiscriminationIndex(Double discriminationIndex) {
	this.discriminationIndex = discriminationIndex;
    }

    public Double getPointBiserial() {
	return pointBiserial;
    }

    public void setPointBiserial(Double pointBiserial) {
	this.pointBiserial = pointBiserial;
    }

}