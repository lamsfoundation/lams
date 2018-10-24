/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.survey.web.form;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;

/**
 * Survey Item Form.
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class QuestionForm {
    private String itemIndex;

    //1: single or multiple choice question;3:open text question
    private short itemType;
    private String sessionMapID;
    private String contentFolderID;

    //tool access mode;
    private String mode;

    private SurveyQuestion question;

    public QuestionForm() {
	question = new SurveyQuestion();
    }

    public void reset(HttpServletRequest request) {
	if (question != null) {
	    question.setAppendText(false);
	    question.setOptional(false);
	}
    }

    public String getItemIndex() {
	return itemIndex;
    }

    public void setItemIndex(String itemIndex) {
	this.itemIndex = itemIndex;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getMode() {
	return mode;
    }

    public void setMode(String mode) {
	this.mode = mode;
    }

    public SurveyQuestion getQuestion() {
	return question;
    }

    public void setQuestion(SurveyQuestion question) {
	this.question = question;
    }

    public short getItemType() {
	return itemType;
    }

    public void setItemType(short itemType) {
	this.itemType = itemType;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }
}
