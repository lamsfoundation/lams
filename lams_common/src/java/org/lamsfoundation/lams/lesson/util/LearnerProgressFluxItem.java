package org.lamsfoundation.lams.lesson.util;

/**
 * Flux item emitted on learner moving from one activity to another by himself
 */
public class LearnerProgressFluxItem {
    private long lessonId;
    private long activityId;
    private int userId;

    public LearnerProgressFluxItem(long lessonId, long activityId, int userId) {
	this.lessonId = lessonId;
	this.activityId = activityId;
	this.userId = userId;
    }

    public long getLessonId() {
	return lessonId;
    }

    public long getActivityId() {
	return activityId;
    }

    public int getUserId() {
	return userId;
    }
}