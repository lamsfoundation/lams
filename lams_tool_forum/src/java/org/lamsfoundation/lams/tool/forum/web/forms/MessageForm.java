/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.forum.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.forum.model.Attachment;
import org.lamsfoundation.lams.tool.forum.model.Message;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * Message Form.
 *
 *
 *
 * User: conradb Date: 10/06/2005 Time: 15:44:47
 */
public class MessageForm {
    private static final long serialVersionUID = -9054365604649146734L;
    private static Logger logger = Logger.getLogger(MessageForm.class.getName());

    protected Message message;
    protected String sessionMapID;
    protected Long forumId;
    protected Long parentId;
    protected Long topicId;

    private boolean hasAttachment;
    // attachment file name
    private String attachmentName;
    // Message attachment file
    private MultipartFile attachmentFile;

    public MessageForm() {
	message = new Message();
    }

    /**
     * MessageForm validation method from STRUCT interface.
     *
     */
    public MultiValueMap<String, String> validate(HttpServletRequest request, MessageService messageService) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<String, String>();

	try {
	    if (StringUtils.isBlank(message.getSubject())) {
		errorMap.add("message.subject", messageService.getMessage("error.subject.required"));
	    }
	    boolean isTestHarness = Boolean.valueOf(request.getParameter("testHarness"));
	    if (!isTestHarness && StringUtils.isBlank(message.getBody())) {
		errorMap.add("message.body", messageService.getMessage("error.body.required"));
	    }

	    // validate item size
	    boolean largeFile = true;
	    if (request.getRequestURI().indexOf("/learning/") != -1) {
		if ((this.getAttachmentFile() != null)
			&& FileUtil.isExecutableFile(this.getAttachmentFile().getOriginalFilename())) {
		    errorMap.add("message.attachments", messageService.getMessage("error.attachment.executable"));
		}
		largeFile = false;
	    }

	    FileValidatorUtil.validateFileSize(this.getAttachmentFile(), largeFile);

	} catch (Exception e) {
	    MessageForm.logger.error("", e);
	}
	return errorMap;
    }

    // -------------------------get/set methods----------------
    public void setMessage(Message message) {
	this.message = message;
	if (message != null) {
	    if ((message.getAttachments() != null) && (message.getAttachments().size() > 0)) {
		hasAttachment = true;
		attachmentName = ((Attachment) message.getAttachments().iterator().next()).getFileName();
	    } else {
		hasAttachment = false;
		attachmentName = null;
	    }
	}
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

    public MultipartFile getAttachmentFile() {
	return attachmentFile;
    }

    public void setAttachmentFile(MultipartFile attachmentFile) {
	this.attachmentFile = attachmentFile;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public boolean isHasAttachment() {
	return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
	this.hasAttachment = hasAttachment;
    }
}
