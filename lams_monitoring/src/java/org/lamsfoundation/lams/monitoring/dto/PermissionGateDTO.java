package org.lamsfoundation.lams.monitoring.dto;

import org.lamsfoundation.lams.learningdesign.Activity;

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

    public PermissionGateDTO(Activity activity) {
	this.title = activity.getTitle();
	this.activityID = activity.getActivityId();
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

}
