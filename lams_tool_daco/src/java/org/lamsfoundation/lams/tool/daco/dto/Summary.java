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
package org.lamsfoundation.lams.tool.daco.dto;

import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;

/**
 * List contains following element: <br>
 * 
 * <li>session_id</li>
 * <li>session_name</li>
 * <li>DacoQuestion.uid</li>
 * <li>DacoQuestion.question_type</li>
 * <li>DacoQuestion.create_by_author</li>
 * <li>DacoQuestion.is_hide</li>
 * <li>DacoQuestion.title</li>
 * <li>User.login_name</li>
 * <li>count(question_uid)</li>
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class Summary {

	private Long sessionId;
	private String sessionName;
	private Long questionUid;
	private short questionType;
	private boolean questionCreateByAuthor;
	private boolean questionHide;
	private String questionTitle;
	private String username;
	private int viewNumber;

	// following is used for export portfolio programs:
	private String attachmentLocalUrl;

	// true: initial group question, false, belong to some group.
	private boolean isInitGroup;

	public Summary() {
	}

	/**
	 * Contruction method for monitoring summary function.
	 * 
	 * <B>Don't not set isInitGroup and viewNumber fields</B>
	 * 
	 * @param sessionName
	 * @param question
	 * @param isInitGroup
	 */
	public Summary(Long sessionId, String sessionName, DacoQuestion question) {
		this.sessionId = sessionId;
		this.sessionName = sessionName;
		if (question != null) {
			questionUid = question.getUid();
			questionType = question.getType();
			questionCreateByAuthor = question.isCreateByAuthor();
			questionHide = question.isHide();
			username = question.getCreateBy() == null ? "" : question.getCreateBy().getLoginName();
		}
		else {
			questionUid = new Long(-1);
		}
	}

	/**
	 * Contruction method for export profolio function.
	 * 
	 * <B>Don't not set sessionId and viewNumber fields</B>
	 * 
	 * @param sessionName
	 * @param question
	 * @param isInitGroup
	 */
	public Summary(String sessionName, DacoQuestion question, boolean isInitGroup) {
		this.sessionName = sessionName;
		if (question != null) {
			questionUid = question.getUid();
			questionType = question.getType();
			questionCreateByAuthor = question.isCreateByAuthor();
			questionHide = question.isHide();
			username = question.getCreateBy() == null ? "" : question.getCreateBy().getLoginName();
		}
		else {
			questionUid = new Long(-1);
		}
		this.isInitGroup = isInitGroup;
	}

	public boolean isQuestionCreateByAuthor() {
		return questionCreateByAuthor;
	}

	public void setQuestionCreateByAuthor(boolean questionCreateByAuthor) {
		this.questionCreateByAuthor = questionCreateByAuthor;
	}

	public boolean isQuestionHide() {
		return questionHide;
	}

	public void setQuestionHide(boolean questionHide) {
		this.questionHide = questionHide;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public short getQuestionType() {
		return questionType;
	}

	public void setQuestionType(short questionType) {
		this.questionType = questionType;
	}

	public Long getQuestionUid() {
		return questionUid;
	}

	public void setQuestionUid(Long questionUid) {
		this.questionUid = questionUid;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getViewNumber() {
		return viewNumber;
	}

	public void setViewNumber(int viewNumber) {
		this.viewNumber = viewNumber;
	}

	public boolean isInitGroup() {
		return isInitGroup;
	}

	public void setInitGroup(boolean isInitGroup) {
		this.isInitGroup = isInitGroup;
	}

	public String getAttachmentLocalUrl() {
		return attachmentLocalUrl;
	}

	public void setAttachmentLocalUrl(String attachmentLocalUrl) {
		this.attachmentLocalUrl = attachmentLocalUrl;
	}

}