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

/**
 * Survey Item Form.
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class AnswerForm {
    private String sessionMapID;
    private Integer questionSeqID;
    private int position;
    private int currentIdx;
    private Long userID;

    public int getPosition() {
	return position;
    }

    public void setPosition(int position) {
	this.position = position;
    }

    public Integer getQuestionSeqID() {
	return questionSeqID;
    }

    public void setQuestionSeqID(Integer questionSeqID) {
	this.questionSeqID = questionSeqID;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public int getCurrentIdx() {
	return currentIdx;
    }

    public void setCurrentIdx(int currentIdx) {
	this.currentIdx = currentIdx;
    }

    public void setUserID(Long userID) {
	this.userID = userID;
    }

    public Long getUserID() {
	return userID;
    }
}
