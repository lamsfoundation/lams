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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.scratchie.model;

import java.util.Date;

/**
 * ScratchieBurningQuestion
 *
 * @author Andrey Balan
 *
 * @hibernate.class table="tl_lascrt11_burning_question"
 *
 */
public class ScratchieBurningQuestion {

    private Long uid;
    private ScratchieItem scratchieItem;
    private Date accessDate;
    private Long sessionId;
    private String question;
    private boolean generalQuestion;

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="access_date"
     * @return
     */
    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }

    /**
     * @hibernate.many-to-one column="scratchie_item_uid" cascade="none"
     * @return
     */
    public ScratchieItem getScratchieItem() {
	return scratchieItem;
    }

    public void setScratchieItem(ScratchieItem scratchieItem) {
	this.scratchieItem = scratchieItem;
    }

    /**
     * @hibernate.property column="session_id"
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     * @hibernate.property column="question" type="text"
     * @return
     */
    public String getQuestion() {
	return question;
    }

    public void setQuestion(String question) {
	this.question = question;
    }

    /**
     * @hibernate.property column="general_question"
     * @return
     */
    public boolean isGeneralQuestion() {
	return generalQuestion;
    }

    public void setGeneralQuestion(boolean isGeneralQuestion) {
	this.generalQuestion = isGeneralQuestion;
    }

}
