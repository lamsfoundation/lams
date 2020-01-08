package org.lamsfoundation.lams.monitoring.web.form;

import java.util.Collection;
import java.util.Date;

import org.lamsfoundation.lams.learningdesign.GateActivity;

public class GateForm {

    private GateActivity gate;

    private Long activityId;

    private Integer waitingLearners;

    private Integer totalLearners;

    private Date startingTime;

    private Date endingTime;

    private boolean activityCompletionBased;

    private Collection waitingLearnerList;

    private Collection forbiddenLearnerList;

    private Collection allowedToPassLearnerList;

    private String userId;

    private String scheduleDate;

    public GateActivity getGate() {
	return gate;
    }

    public void setGate(GateActivity gate) {
	this.gate = gate;
    }

    public Long getActivityId() {
	return activityId;
    }

    public void setActivityId(Long activityId) {
	this.activityId = activityId;
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

    public boolean isActivityCompletionBased() {
	return activityCompletionBased;
    }

    public void setActivityCompletionBased(boolean activityCompletionBased) {
	this.activityCompletionBased = activityCompletionBased;
    }

    public Collection getWaitingLearnerList() {
	return waitingLearnerList;
    }

    public void setWaitingLearnerList(Collection waitingLearnerList) {
	this.waitingLearnerList = waitingLearnerList;
    }

    public Collection getForbiddenLearnerList() {
	return forbiddenLearnerList;
    }

    public void setForbiddenLearnerList(Collection forbiddenLearnerList) {
	this.forbiddenLearnerList = forbiddenLearnerList;
    }

    public Collection getAllowedToPassLearnerList() {
	return allowedToPassLearnerList;
    }

    public void setAllowedToPassLearnerList(Collection allowedToPassLearnerList) {
	this.allowedToPassLearnerList = allowedToPassLearnerList;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getScheduleDate() {
	return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
	this.scheduleDate = scheduleDate;
    }

}
