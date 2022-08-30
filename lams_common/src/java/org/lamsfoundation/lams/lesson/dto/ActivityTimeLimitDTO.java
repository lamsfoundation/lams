package org.lamsfoundation.lams.lesson.dto;

import java.time.LocalDateTime;

public class ActivityTimeLimitDTO {
    private Long activityId;
    private String activityTitle;
    private LocalDateTime absoluteTimeLimit;

    public ActivityTimeLimitDTO(Long activityId, String activityTitle, LocalDateTime absoluteTimeLimit) {
	this.activityId = activityId;
	this.activityTitle = activityTitle;
	this.absoluteTimeLimit = absoluteTimeLimit;
    }

    public Long getActivityId() {
	return activityId;
    }

    public String getActivityTitle() {
	return activityTitle;
    }

    public LocalDateTime getAbsoluteTimeLimit() {
	return absoluteTimeLimit;
    }
}