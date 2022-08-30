package org.lamsfoundation.lams.lesson.dto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ActivityTimeLimitDTO {
    private Long toolContentId;
    private String activityTitle;
    private LocalDateTime absoluteTimeLimit;

    public ActivityTimeLimitDTO(Long toolContentId, String activityTitle, LocalDateTime absoluteTimeLimit) {
	this.toolContentId = toolContentId;
	this.activityTitle = activityTitle;
	this.absoluteTimeLimit = absoluteTimeLimit;
    }

    public Long getToolContentId() {
	return toolContentId;
    }

    public String getActivityTitle() {
	return activityTitle;
    }

    public long getSecondsLeft() {
	return ChronoUnit.SECONDS.between(LocalDateTime.now(), absoluteTimeLimit);
    }
}