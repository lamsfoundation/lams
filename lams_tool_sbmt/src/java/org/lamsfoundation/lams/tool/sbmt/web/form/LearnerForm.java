package org.lamsfoundation.lams.tool.sbmt.web.form;

import org.springframework.web.multipart.MultipartFile;

/**
 * Learner Form.
 */
public class LearnerForm {

    public static int DESCRIPTION_LENGTH = 5000;

    private String description;
    private MultipartFile file;

    private String sessionMapID;

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public MultipartFile getFile() {
	return file;
    }

    public void setFile(MultipartFile file) {
	this.file = file;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

}
