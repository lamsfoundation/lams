package org.lamsfoundation.lams.tool.forum.forms;

import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.FileUtils;
import org.apache.struts.upload.FormFile;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.validator.ValidatorForm;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 *
 * 	Message Form.
 *	@struts.form name="forumForm"
 *  Created by IntelliJ IDEA.
 * User: conradb
 * Date: 10/06/2005
 * Time: 15:44:36
 * To change this template use File | Settings | File Templates.
 */
public class ForumForm extends ValidatorForm {
    protected Forum forum;
    private FormFile offlineFile;
    private FormFile onlineFile;
    protected Map attachments;
    private static Logger logger = Logger.getLogger(ForumForm.class.getName());

    public ForumForm() {
        this.forum = new Forum();
        this.forum.setTitle("New Forum");
        this.attachments = new HashMap();
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

    public void setAttachments(Map attachments) {
      this.attachments = attachments;
    }

    public Map getAttachments() {
        return this.attachments;
    }

    public ActionErrors validate(ActionMapping mapping,
            javax.servlet.http.HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);
		try{
           // if ("".equals(forum.getTitle())) {
             //   ActionError error = new ActionError("error.valueReqd");
			   // errors.add("forum.title", error);
           // }
			if (onlineFile != null && !(onlineFile.getFileName().trim().equals(""))) {
				    if (onlineFile.getFileSize() > ForumConstants.MAX_FILE_SIZE) {
					    ActionError ae = new ActionError("error.inputFileTooLarge");
					    errors.add("onlineFile", ae);
			} else {
                        String fileName = onlineFile.getFileName();
					    Attachment attachment = new Attachment();
					    File afile = FileUtils.getFile(fileName, onlineFile.getInputStream());
                        attachment.setName(fileName);
					    attachment.setContentType(onlineFile.getContentType());
					    attachment.setData(FileUtils.getBytes(afile));
                        attachment.setType(Attachment.TYPE_ONLINE);
                        attachments.put(fileName, attachment);
                        onlineFile = null;
				    }
			}
			if (offlineFile != null && !(offlineFile.getFileName().trim().equals(""))) {
				    if (offlineFile.getFileSize() > ForumConstants.MAX_FILE_SIZE) {
					    ActionError ae = new ActionError("error.inputFileTooLarge");
					    errors.add("offlineFile", ae);
				    } else {
                        String fileName = offlineFile.getFileName();
					    Attachment attachment = new Attachment();
					    File afile = FileUtils.getFile(fileName, offlineFile.getInputStream());
                        attachment.setName(fileName);
					    attachment.setContentType(offlineFile.getContentType());
					    attachment.setData(FileUtils.getBytes(afile));
                        attachment.setType(Attachment.TYPE_OFFLINE);
                        attachments.put(fileName, attachment);
                        offlineFile = null;
				    }
		    }
		} catch (Exception e) {
			logger.error("", e);
		}
		return errors;
	}

}
