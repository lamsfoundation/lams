package org.lamsfoundation.lams.qb.dto;

import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.model.QbQuestion;

public class QbStatsDTO {
    public static class QbStatsActivityDTO {
	public ToolActivity activity;
	public Integer average;

	public ToolActivity getActivity() {
	    return activity;
	}

	public void setActivity(ToolActivity activity) {
	    this.activity = activity;
	}

	public Integer getAverage() {
	    return average;
	}

	public void setAverage(Integer average) {
	    this.average = average;
	}
    }

    private QbQuestion question;
    private Map<Long, Long> answersRaw;
    private Map<Long, Integer> answersPercent;
    private String answersJSON;
    private List<QbStatsActivityDTO> activities;
    private List<QbQuestion> versions;
    private Map<String, Long> burningQuestions;

    public QbQuestion getQuestion() {
	return question;
    }

    public void setQuestion(QbQuestion question) {
	this.question = question;
    }

    public Map<Long, Long> getAnswersRaw() {
	return answersRaw;
    }

    public void setAnswersRaw(Map<Long, Long> answers) {
	this.answersRaw = answers;
    }

    public Map<Long, Integer> getAnswersPercent() {
	return answersPercent;
    }

    public void setAnswersPercent(Map<Long, Integer> answersPercent) {
	this.answersPercent = answersPercent;
    }

    public String getAnswersJSON() {
	return answersJSON;
    }

    public void setAnswersJSON(String answersJSON) {
	this.answersJSON = answersJSON;
    }

    public List<QbStatsActivityDTO> getActivities() {
	return activities;
    }

    public void setActivities(List<QbStatsActivityDTO> activities) {
	this.activities = activities;
    }

    public List<QbQuestion> getVersions() {
	return versions;
    }

    public void setVersions(List<QbQuestion> versions) {
	this.versions = versions;
    }

    public Map<String, Long> getBurningQuestions() {
	return burningQuestions;
    }

    public void setBurningQuestions(Map<String, Long> burningQuestions) {
	this.burningQuestions = burningQuestions;
    }
}