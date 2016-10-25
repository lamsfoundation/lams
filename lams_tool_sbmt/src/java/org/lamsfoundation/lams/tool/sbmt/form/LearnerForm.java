package org.lamsfoundation.lams.tool.sbmt.form;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

/**
 *
 * Learner Form.
 *
 *
 */
public class LearnerForm extends ValidatorForm {

    public static int DESCRIPTION_LENGTH = 5000;

    private String description;
    private FormFile file;

    private String sessionMapID;

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public FormFile getFile() {
	return file;
    }

    public void setFile(FormFile file) {
	this.file = file;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

}
