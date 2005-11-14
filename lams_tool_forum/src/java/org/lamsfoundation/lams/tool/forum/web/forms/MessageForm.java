package org.lamsfoundation.lams.tool.forum.web.forms;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;
import org.lamsfoundation.lams.tool.forum.persistence.Message;

/**
 *
 * 	Message Form.
 *	@struts.form name="messageForm"
 *
 * User: conradb
 * Date: 10/06/2005
 * Time: 15:44:47
 */
public class MessageForm extends ValidatorForm {
	private static final long serialVersionUID = -9054365604649146734L;
	private static Logger logger = Logger.getLogger(ForumForm.class.getName());
	
    protected Message message;
    protected Long forumId;
    protected Long parentId;
    protected Long topicId;
    
    //attachment file name
    private String attachmentName;
    //Message attachment file
    private FormFile attachmentFile;
    
    public MessageForm() {
    	message = new Message();
    }
    /**
     * MessageForm validation method from STRUCT interface.
     * 
     */
    public ActionErrors validate(ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        try{
            if ("".equals(message.getSubject())) {
               ActionMessage error = new ActionMessage("error.valueReqd");
               errors.add("message.subject", error);
            }
            if ("".equals(message.getBody())) {
            	ActionMessage error = new ActionMessage("error.valueReqd");
               errors.add("message.body", error);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return errors;
    }
    
    //-------------------------get/set methods----------------
    public void setMessage(Message message) {
    	this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getForumId() {
        return this.forumId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getTopicId() {
        return this.topicId;
    }


	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public FormFile getAttachmentFile() {
		return attachmentFile;
	}

	public void setAttachmentFile(FormFile attachmentFile) {
		this.attachmentFile = attachmentFile;
	}
}
