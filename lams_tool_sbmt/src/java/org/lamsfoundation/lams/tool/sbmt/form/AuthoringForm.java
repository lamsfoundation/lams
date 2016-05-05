package org.lamsfoundation.lams.tool.sbmt.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;

/**
 *
 * @author Dapeng.Ni
 *
 */
public class AuthoringForm extends ValidatorForm {

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

    private int limitUploadNumber;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private boolean notifyLearnersOnMarkRelease;

    private boolean notifyTeachersOnFileSubmit;

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	lockOnFinished = false;
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

	limitUpload = content.isLimitUpload();
	limitUploadNumber = content.getLimitUploadNumber();

	reflectOnActivity = content.isReflectOnActivity();
	reflectInstructions = content.getReflectInstructions();
	setNotifyLearnersOnMarkRelease(content.isNotifyLearnersOnMarkRelease());
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

    public int getLimitUploadNumber() {
	return limitUploadNumber;
    }

    public void setLimitUploadNumber(int limitUploadNumber) {
	this.limitUploadNumber = limitUploadNumber;
    }

    public boolean isNotifyLearnersOnMarkRelease() {
	return notifyLearnersOnMarkRelease;
    }

    public void setNotifyLearnersOnMarkRelease(boolean notifyLearnersOnMarkRelease) {
	this.notifyLearnersOnMarkRelease = notifyLearnersOnMarkRelease;
    }

    public boolean isNotifyTeachersOnFileSubmit() {
	return notifyTeachersOnFileSubmit;
    }

    public void setNotifyTeachersOnFileSubmit(boolean notifyTeachersOnFileSubmit) {
	this.notifyTeachersOnFileSubmit = notifyTeachersOnFileSubmit;
    }
}
