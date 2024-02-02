package org.lamsfoundation.lams.admin.web.form;

/**
 * Superclass for ExtServerForm and LtiConsumerForm, holding LTI consumers' and ExtServers' shared properties.
 *
 * @author Andrey Balan
 */
public abstract class ExtServerCommonForm {

    private Integer sid = -1;

    private String serverid;

    private String serverkey;

    private String servername;

    private String serverdesc;

    private String prefix;

    private String lessonFinishUrl;

    private boolean disabled = false;

    private Boolean liveEditEnabled;

    private Boolean enableLessonNotifications;

    /**
     * Should Learner start the lesson from the beginning each time he enters it. Content is not removed, LessonProgress
     * is deleted, not archived.
     */
    private Boolean forceLearnerRestart;

    /**
     * Should Learners be allowed to restart the lesson after finishing it. Content is not removed, LessonProgress is
     * archived and then deleted.
     */
    private Boolean allowLearnerRestart;

    private boolean startInMonitor;

    /**
     * Should learners be displayed activity gradebook on lesson complete.
     */
    private Boolean gradebookOnComplete;

    public Integer getSid() {
	return sid;
    }

    public void setSid(Integer sid) {
	this.sid = sid;
    }

    public String getServerid() {
	return serverid;
    }

    public void setServerid(String serverid) {
	this.serverid = serverid;
    }

    public String getServerkey() {
	return serverkey;
    }

    public void setServerkey(String serverkey) {
	this.serverkey = serverkey;
    }

    public String getServername() {
	return servername;
    }

    public void setServername(String servername) {
	this.servername = servername;
    }

    public String getServerdesc() {
	return serverdesc;
    }

    public void setServerdesc(String serverdesc) {
	this.serverdesc = serverdesc;
    }

    public String getPrefix() {
	return prefix;
    }

    public void setPrefix(String prefix) {
	this.prefix = prefix;
    }

    public String getLessonFinishUrl() {
	return lessonFinishUrl;
    }

    public void setLessonFinishUrl(String lessonFinishUrl) {
	this.lessonFinishUrl = lessonFinishUrl;
    }

    public boolean isDisabled() {
	return disabled;
    }

    public void setDisabled(boolean disabled) {
	this.disabled = disabled;
    }

    public Boolean getLiveEditEnabled() {
	return liveEditEnabled;
    }

    public void setLiveEditEnabled(Boolean liveEditEnabled) {
	this.liveEditEnabled = liveEditEnabled;
    }

    public Boolean getEnableLessonNotifications() {
	return enableLessonNotifications;
    }

    public void setEnableLessonNotifications(Boolean enableLessonNotifications) {
	this.enableLessonNotifications = enableLessonNotifications;
    }

    public Boolean getForceLearnerRestart() {
	return forceLearnerRestart;
    }

    public void setForceLearnerRestart(Boolean forceLearnerRestart) {
	this.forceLearnerRestart = forceLearnerRestart;
    }

    public Boolean getAllowLearnerRestart() {
	return allowLearnerRestart;
    }

    public void setAllowLearnerRestart(Boolean allowLearnerRestart) {
	this.allowLearnerRestart = allowLearnerRestart;
    }

    public boolean isStartInMonitor() {
	return startInMonitor;
    }

    public void setStartInMonitor(boolean startInMonitor) {
	this.startInMonitor = startInMonitor;
    }

    public Boolean getGradebookOnComplete() {
	return gradebookOnComplete;
    }

    public void setGradebookOnComplete(Boolean gradebookOnComplete) {
	this.gradebookOnComplete = gradebookOnComplete;
    }
}