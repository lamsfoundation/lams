package org.lamsfoundation.lams.tool.forum.forms;

import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.apache.struts.validator.ValidatorForm;

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
}
