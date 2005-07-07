package org.lamsfoundation.lams.tool.forum.web.forms;

import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.forum.persistence.Message;

/**
 *
 * 	Message Form.
 *	@struts.form name="messageForm"
 *
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 10/06/2005
 * Time: 15:44:47
 * To change this template use File | Settings | File Templates.
 */
public class MessageForm extends ValidatorForm {
    protected Message message;
    protected Long forumId;
    protected Long parentId;
    protected Long topicId;
    private static Logger logger = Logger.getLogger(org.lamsfoundation.lams.tool.forum.web.ForumForm.class.getName());

    public MessageForm() {
        this.message = new Message();
    }

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

    public ActionErrors validate(ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        ActionError ae;
        try{
            if ("".equals(message.getSubject())) {
               ActionError error = new ActionError("error.valueReqd");
               errors.add("message.subject", error);
            }
            if ("".equals(message.getBody())) {
               ActionError error = new ActionError("error.valueReqd");
               errors.add("message.body", error);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return errors;
    }

}
