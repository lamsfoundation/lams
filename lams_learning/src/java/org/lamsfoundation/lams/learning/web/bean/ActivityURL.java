/*
 * Created on 13/01/2005
 *
 */
package org.lamsfoundation.lams.learning.web.bean;

/**
 * Display bean for an activity. Holds the URL used to display the activity along
 * with title, description and a completion flag.
 * 
 * @author daveg
 *
 */
public class ActivityURL {
	
	private Long activityId;
	private String url;
	private String title;
	private String description;
	private boolean complete;

	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
