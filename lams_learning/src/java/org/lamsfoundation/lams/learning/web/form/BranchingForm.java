package org.lamsfoundation.lams.learning.web.form;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;

public class BranchingForm {

    private Long activityID;
    private Long progressID;
    private Boolean previewLesson;
    private Boolean showNextButton;
    private Boolean showFinishButton;
    private String title;
    private String type;
    private List<ActivityURL> activityURLs;

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

    public Boolean getPreviewLesson() {
	return previewLesson;
    }

    public void setPreviewLesson(Boolean previewLesson) {
	this.previewLesson = previewLesson;
    }

    public Boolean isShowNextButton() {
	return showNextButton;
    }

    public void setShowNextButton(Boolean showNextButton) {
	this.showNextButton = showNextButton;
    }

    public Boolean isShowFinishButton() {
	return showFinishButton;
    }

    public void setShowFinishButton(Boolean showFinishButton) {
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

    public List<ActivityURL> getActivityURLs() {
	return activityURLs;
    }

    public void setActivityURLs(List<ActivityURL> activityURLs) {
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
