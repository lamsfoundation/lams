package org.lamsfoundation.lams.tool.forum.web.forms;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.util.UploadFileUtil;

/**
 *
 * 	Message Form.
 *	@struts.form name="forumForm"
 *
 * User: conradb
 * Date: 10/06/2005
 * Time: 15:44:36
 */
public class ForumForm extends ValidatorForm {
	private static final long serialVersionUID = -6054354910960460120L;
    private int currentTab;
    private FormFile offlineFile;
    private FormFile onlineFile;
    private Forum forum;

    private static Logger logger = Logger.getLogger(ForumForm.class.getName());

    public ForumForm() {
        this.forum = new Forum();
        this.forum.setTitle("New Forum");
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public Forum getForum() {
        return forum;
    }

    public void setOnlineFile(FormFile onlineFile) {
        this.onlineFile = onlineFile;
    }

    public FormFile getOnlineFile() {
        return onlineFile;
    }

    public void setOfflineFile(FormFile offlineFile) {
        this.offlineFile = offlineFile;
    }

    public FormFile getOfflineFile() {
        return offlineFile;
    }


    private float convertToMeg( int numBytes ) {
        return numBytes != 0 ? numBytes / 1024 / 1024 : 0;
    }
    
    public ActionErrors validate(ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        ActionError ae;
        try{
            if ("".equals(forum.getTitle())) {
               ActionError error = new ActionError("error.valueReqd");
               errors.add("forum.title", error);
            }
            if (onlineFile != null && !(onlineFile.getFileName().trim().equals("")) &&
                    convertToMeg(onlineFile.getFileSize()) > UploadFileUtil.getMaxFileSize()) {
                    ae = new ActionError("error.inputFileTooLarge");
                    errors.add("onlineFile", ae);
            }
            if (offlineFile != null && !(offlineFile.getFileName().trim().equals("")) && 
                    convertToMeg(offlineFile.getFileSize()) > UploadFileUtil.getMaxFileSize()) {
                    ae = new ActionError("error.inputFileTooLarge");
                    errors.add("offlineFile", ae);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return errors;
    }

	public int getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(int currentTab) {
		this.currentTab = currentTab;
	}
}
