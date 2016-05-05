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

/* $Id$ */
package org.lamsfoundation.lams.comments.web;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.lamsfoundation.lams.comments.Comment;

/**
 * Comment Form.
 */
public class CommentForm extends ValidatorForm {
    private static final long serialVersionUID = -9054365604649146734L;
    private static Logger logger = Logger.getLogger(CommentForm.class.getName());

    protected Comment comment;
    protected String sessionMapID;
    protected Long parentId;
    protected Long topicId;

    public CommentForm() {
	comment = new Comment();
    }

    /**
     * MessageForm validation method from STRUCT interface.
     *
     */
    @Override
    public ActionErrors validate(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
	ActionErrors errors = new ActionErrors();
	try {
	    if (StringUtils.isBlank(comment.getBody())) {
		ActionMessage error = new ActionMessage("error.body.required");
		errors.add("message.body", error);
	    }
	} catch (Exception e) {
	    CommentForm.logger.error("", e);
	}
	return errors;
    }

    // -------------------------get/set methods----------------
    public void setComment(Comment comment) {
	this.comment = comment;
    }

    public Comment getComment() {
	return comment;
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

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

}
