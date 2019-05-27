package org.lamsfoundation.lams.qb.dto;

import org.lamsfoundation.lams.learningdesign.ToolActivity;

public class QbStatsActivityDTO {
    public ToolActivity activity;
    public Integer participantCount;
    public Double difficultyIndex;
    public Double discriminationIndex;
    public Double pointBiserial;

    public ToolActivity getActivity() {
	return activity;
    }

    public void setActivity(ToolActivity activity) {
	this.activity = activity;
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