package org.lamsfoundation.lams.learning.web.form;

public class NotebookForm {

    private Long uid;
    private String title;
    private String entry;
    private Long lessonID;
    private Long currentLessonID;
    private String signature;

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getEntry() {
	return entry;
    }

    public void setEntry(String entry) {
	this.entry = entry;
    }

    public Long getLessonID() {
	return lessonID;
    }

    public void setLessonID(Long lessonID) {
	this.lessonID = lessonID;
    }

    public Long getCurrentLessonID() {
	return currentLessonID;
    }

    public void setCurrentLessonID(Long currentLessonID) {
	this.currentLessonID = currentLessonID;
    }

    public String getSignature() {
	return signature;
    }

    public void setSignature(String signature) {
	this.signature = signature;
    }

}