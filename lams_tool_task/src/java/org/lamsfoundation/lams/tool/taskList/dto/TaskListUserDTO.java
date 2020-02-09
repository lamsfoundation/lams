package org.lamsfoundation.lams.tool.taskList.dto;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class TaskListUserDTO {
    private Long userId;
    private String portraitId;
    private String fullName;
    private boolean verifiedByMonitor;

    private Set<Long> completedTaskUids = new LinkedHashSet<>();
    private boolean completed;
    // item complete date. Used in monitoring summary page
    private Date accessDate;

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userID) {
	this.userId = userID;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public boolean isVerifiedByMonitor() {
	return verifiedByMonitor;
    }

    public void setVerifiedByMonitor(boolean verifiedByMonitor) {
	this.verifiedByMonitor = verifiedByMonitor;
    }

    public Set<Long> getCompletedTaskUids() {
	return completedTaskUids;
    }

    public void setCompletedTaskUids(Set<Long> completedTaskUids) {
	this.completedTaskUids = completedTaskUids;
    }

    public boolean isCompleted() {
	return completed;
    }

    public void setCompleted(boolean completed) {
	this.completed = completed;
    }

    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }

    public String getPortraitId() {
	return portraitId;
    }

    public void setPortraitId(String portraitId) {
	this.portraitId = portraitId;
    }

}
