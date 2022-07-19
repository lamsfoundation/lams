package org.lamsfoundation.lams.lesson.util;

/**
 * Flux item emitted on learner joining a lesson, for the first time or not
 */
public class LearnerLessonJoinFluxItem {
    private long lessonId;
    private int userId;

    public LearnerLessonJoinFluxItem(long lessonId, int userId) {
	this.lessonId = lessonId;
	this.userId = userId;
    }

    public long getLessonId() {
	return lessonId;
    }

    public int getUserId() {
	return userId;
    }
}