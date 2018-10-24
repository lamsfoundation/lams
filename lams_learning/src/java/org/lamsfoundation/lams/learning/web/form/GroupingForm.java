package org.lamsfoundation.lams.learning.web.form;

public class GroupingForm {

    private String title;
    private Boolean previewLesson;
    private Long activityID;

//    used for updating the the progress bar
    private Long lessonID;
    private Integer version;
    private String progressSummary;

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Boolean getPreviewLesson() {
	return previewLesson;
    }

    public void setPreviewLesson(Boolean previewLesson) {
	this.previewLesson = previewLesson;
    }

    public Long getActivityID() {
	return activityID;
    }

    public void setActivityID(Long activityID) {
	this.activityID = activityID;
    }

    public Long getLessonID() {
	return lessonID;
    }

    public void setLessonID(Long lessonID) {
	this.lessonID = lessonID;
    }

    public Integer getVersion() {
	return version;
    }

    public void setVersion(Integer version) {
	this.version = version;
    }

    public String getProgressSummary() {
	return progressSummary;
    }

    public void setProgressSummary(String progressSummary) {
	this.progressSummary = progressSummary;
    }

}