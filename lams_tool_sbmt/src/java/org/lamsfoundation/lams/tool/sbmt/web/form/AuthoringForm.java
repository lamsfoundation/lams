package org.lamsfoundation.lams.tool.sbmt.web.form;

import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesContent;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dapeng.Ni
 */
public class AuthoringForm {

    private Long toolContentID;

    private String contentFolderID;

    // control fields
    private String sessionMapID;

    private String currentTab;

    // basic input fields
    private String title;

    private String instructions;

    private boolean lockOnFinished;

    // file and display fields

    private boolean limitUpload;

    private boolean minLimitUpload;

    private boolean useSelectLeaderToolOuput;

    private int limitUploadNumber;

    private Integer minLimitUploadNumber;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private boolean notifyTeachersOnFileSubmit;

    public void reset(HttpServletRequest request) {
	lockOnFinished = false;
	useSelectLeaderToolOuput = false;
	limitUpload = false;
	reflectOnActivity = false;
    }

    public void initContentValue(SubmitFilesContent content) {
	if (content == null) {
	    return;
	}

	// copy attribute
	toolContentID = content.getContentID();
	title = content.getTitle();
	instructions = content.getInstruction();
	lockOnFinished = content.isLockOnFinished();
	useSelectLeaderToolOuput = content.isUseSelectLeaderToolOuput();
	limitUpload = content.isLimitUpload();
	limitUploadNumber = content.getLimitUploadNumber();
	minLimitUploadNumber = content.getMinLimitUploadNumber();
	minLimitUpload = minLimitUploadNumber != null;

	reflectOnActivity = content.isReflectOnActivity();
	reflectInstructions = content.getReflectInstructions();
	setNotifyTeachersOnFileSubmit(content.isNotifyTeachersOnFileSubmit());
    }

    // **************************************************
    // Get / Set method
    // **************************************************
    public String getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(String currentTab) {
	this.currentTab = currentTab;
    }

    public Long getToolContentID() {
	return toolContentID;
    }

    public void setToolContentID(Long toolContentID) {
	this.toolContentID = toolContentID;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    public boolean isLimitUpload() {
	return limitUpload;
    }

    public void setLimitUpload(boolean limitUpload) {
	this.limitUpload = limitUpload;
    }

    public boolean isMinLimitUpload() {
	return minLimitUpload;
    }

    public void setMinLimitUpload(boolean minLimitUpload) {
	this.minLimitUpload = minLimitUpload;
    }

    public int getLimitUploadNumber() {
	return limitUploadNumber;
    }

    public void setLimitUploadNumber(int limitUploadNumber) {
	this.limitUploadNumber = limitUploadNumber;
    }

    public Integer getMinLimitUploadNumber() {
	return minLimitUploadNumber;
    }

    public void setMinLimitUploadNumber(Integer minLimitUploadNumber) {
	this.minLimitUploadNumber = minLimitUploadNumber;
    }

    public boolean isNotifyTeachersOnFileSubmit() {
	return notifyTeachersOnFileSubmit;
    }

    public void setNotifyTeachersOnFileSubmit(boolean notifyTeachersOnFileSubmit) {
	this.notifyTeachersOnFileSubmit = notifyTeachersOnFileSubmit;
    }

    public boolean isUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }
}