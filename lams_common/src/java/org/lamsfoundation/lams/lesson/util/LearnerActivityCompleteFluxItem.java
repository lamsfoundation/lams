package org.lamsfoundation.lams.lesson.util;

import java.util.Objects;

/**
 * Flux item emitted on learner moving from one activity to another by himself
 */
public class LearnerActivityCompleteFluxItem {
    private long lessonId;
    private int userId;
    private Long activityId;

    public LearnerActivityCompleteFluxItem(long lessonId, int userId, Long activityId) {
	this.lessonId = lessonId;
	this.userId = userId;
	this.activityId = activityId;
    }

    public long getLessonId() {
	return lessonId;
    }

    public Long getActivityId() {
	return activityId;
    }

    public int getUserId() {
	return userId;
    }

    @Override
    public int hashCode() {
	return Objects.hash(activityId, lessonId, userId);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof LearnerActivityCompleteFluxItem)) {
	    return false;
	}
	LearnerActivityCompleteFluxItem other = (LearnerActivityCompleteFluxItem) obj;
	return Objects.equals(activityId, other.activityId) && lessonId == other.lessonId && userId == other.userId;
    }

    @Override
    public String toString() {
	return "LearnerActivityCompleteFluxItem [lessonId=" + lessonId + ", userId=" + userId + ", activityId="
		+ activityId + "]";
    }
}