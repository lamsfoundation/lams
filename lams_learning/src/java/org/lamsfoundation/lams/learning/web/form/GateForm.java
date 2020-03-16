package org.lamsfoundation.lams.learning.web.form;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.GateActivity;

public class GateForm {

    private GateActivity gate;
    private Long activityID;
    private Long lessonID;
    private Integer waitingLearners;
    private Integer totalLearners;
    private Date startingTime;
    private Date endingTime;
    private Date reachDate;
    private Long remainTime;
    private Long startOffset;
    private Boolean previewLesson;
    private String key;

//    used for updating the the progress bar
    private Integer version;
    private String progressSummary;
    private Boolean monitorCanOpenGate;

    public GateActivity getGate() {
	return gate;
    }

    public void setGate(GateActivity gate) {
	this.gate = gate;
    }

    public Long getActivityID() {
	return activityID;
    }

    public void setActivityID(Long activityID) {
	this.activityID = activityID;
    }

    public Long getLessonID() {
	return lessonID;
    }

    public void setLessonID(Long lessonID) {
	this.lessonID = lessonID;
    }

    public Integer getWaitingLearners() {
	return waitingLearners;
    }

    public void setWaitingLearners(Integer waitingLearners) {
	this.waitingLearners = waitingLearners;
    }

    public Integer getTotalLearners() {
	return totalLearners;
    }

    public void setTotalLearners(Integer totalLearners) {
	this.totalLearners = totalLearners;
    }

    public Date getStartingTime() {
	return startingTime;
    }

    public void setStartingTime(Date startingTime) {
	this.startingTime = startingTime;
    }

    public Date getEndingTime() {
	return endingTime;
    }

    public void setEndingTime(Date endingTime) {
	this.endingTime = endingTime;
    }

    public Date getReachDate() {
	return reachDate;
    }

    public void setReachDate(Date reachDate) {
	this.reachDate = reachDate;
    }

    public Long getRemainTime() {
	return remainTime;
    }

    public void setRemainTime(Long remainTime) {
	this.remainTime = remainTime;
    }

    public Long getStartOffset() {
	return startOffset;
    }

    public void setStartOffset(Long startOffset) {
	this.startOffset = startOffset;
    }

    public Boolean getPreviewLesson() {
	return previewLesson;
    }

    public void setPreviewLesson(Boolean previewLesson) {
	this.previewLesson = previewLesson;
    }

    public Integer getVersion() {
	return version;
    }

    public void setVersion(Integer version) {
	this.version = version;
    }

    public String getProgressSummary() {
	return progressSummary;
    }

    public void setProgressSummary(String progressSummary) {
	this.progressSummary = progressSummary;
    }

    public Boolean getMonitorCanOpenGate() {
	return monitorCanOpenGate;
    }

    public void setMonitorCanOpenGate(Boolean monitorCanOpenGate) {
	this.monitorCanOpenGate = monitorCanOpenGate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}