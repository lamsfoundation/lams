package org.lamsfoundation.lams.tool.sbmt.web.form;

/**
 * Learner Form.
 */
public class LearnerForm {

    public static int DESCRIPTION_LENGTH = 5000;

    private String description;
    private String tmpFileUploadId;

    private String sessionMapID;

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getTmpFileUploadId() {
	return tmpFileUploadId;
    }

    public void setTmpFileUploadId(String tmpFileUploadId) {
	this.tmpFileUploadId = tmpFileUploadId;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

}
