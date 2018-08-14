package org.lamsfoundation.lams.learning.web.form;

import java.util.List;

public class BranchingForm {

    private Long activityID;
    private Long progressID;
    private Long previewLesson;
    private boolean showNextButton;
    private boolean showFinishButton;
    private String title;
    private String type;
    private List<String> activityURLs;

//	used for updating the the progress bar
    private Long lessonID;
    private Integer version;
    private String progressSummary;

    public Long getActivityID() {
	return activityID;
    }

    public void setActivityID(Long activityID) {
	this.activityID = activityID;
    }

    public Long getProgressID() {
	return progressID;
    }

    public void setProgressID(Long progressID) {
	this.progressID = progressID;
    }

    public Long getPreviewLesson() {
	return previewLesson;
    }

    public void setPreviewLesson(Long previewLesson) {
	this.previewLesson = previewLesson;
    }

    public boolean isShowNextButton() {
	return showNextButton;
    }

    public void setShowNextButton(boolean showNextButton) {
	this.showNextButton = showNextButton;
    }

    public boolean isShowFinishButton() {
	return showFinishButton;
    }

    public void setShowFinishButton(boolean showFinishButton) {
	this.showFinishButton = showFinishButton;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public List<String> getActivityURLs() {
	return activityURLs;
    }

    public void setActivityURLs(List<String> activityURLs) {
	this.activityURLs = activityURLs;
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
