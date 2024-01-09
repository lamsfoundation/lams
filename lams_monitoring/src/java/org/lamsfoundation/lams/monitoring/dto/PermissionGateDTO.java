package org.lamsfoundation.lams.monitoring.dto;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * DTO used to return the activity details needed for the contribute activities list
 *
 * @author Andrey Balan
 */
public class PermissionGateDTO {
    private String title;
    private String url;
    private Long activityID;
    private Integer activityTypeID;
    private Integer orderID;
    private int waitingLearnersCount;
    private boolean complete;
    private String openUser;
    private Date openTime;

    public PermissionGateDTO(PermissionGateActivity gate) {
	this.title = gate.getTitle();
	this.activityID = gate.getActivityId();
	this.openTime = gate.getGateOpenTime();
	User openUser = gate.getGateOpenUser();
	if (openUser != null) {
	    this.openUser = openUser.getFullName();
	}
    }

    /**
     * @return Returns the activityID.
     */
    public Long getActivityID() {
	return activityID;
    }

    /**
     * @return Returns the activityTypeID.
     */
    public Integer getActivityTypeID() {
	return activityTypeID;
    }

    /**
     * @return Returns the description.
     */
    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    /**
     * @return Returns the orderID.
     */
    public Integer getOrderID() {
	return orderID;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;

    }

    public int getWaitingLearnersCount() {
	return waitingLearnersCount;
    }

    public void setWaitingLearnersCount(int waitingLearnersCount) {
	this.waitingLearnersCount = waitingLearnersCount;
    }

    public boolean isComplete() {
	return complete;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    public String getOpenUser() {
	return openUser;
    }

    public Date getOpenTime() {
	return openTime;
    }
}