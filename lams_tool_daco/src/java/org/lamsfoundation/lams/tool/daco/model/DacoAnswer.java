/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.tool.daco.model;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * DacoAnswer
 *
 * @author Marcin Cieslak
 *
 *
 */
public class DacoAnswer implements Cloneable {
    private static final Logger log = Logger.getLogger(DacoQuestion.class);

    private Long uid;

    private DacoUser user;

    private Integer recordId;

    private String answer;

    private DacoQuestion question;

    private Long fileUuid;

    private Long fileVersionId;

    private String fileName;

    private String fileType;

    private Date createDate;

    @Override
    public Object clone() {
	DacoAnswer cloned = null;
	try {
	    cloned = (DacoAnswer) super.clone();
	    cloned.setUid(null);
	} catch (CloneNotSupportedException e) {
	    DacoAnswer.log.error("Cloning " + DacoQuestion.class + " failed");
	}

	return cloned;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    /**
     *
     * @return Returns the answer ID.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uuid) {
	uid = uuid;
    }

    /**
     *
     * @return
     */
    public DacoUser getUser() {
	return user;
    }

    public void setUser(DacoUser user) {
	this.user = user;
    }

    /**
     *
     * @return Returns the record ID.
     */
    public Integer getRecordId() {
	return recordId;
    }

    public void setRecordId(Integer recordId) {
	this.recordId = recordId;
    }

    /**
     *
     * @return Returns the answer.
     */
    public String getAnswer() {
	return answer;
    }

    public void setAnswer(String answer) {
	this.answer = answer;
    }

    /**
     *
     * @return
     */
    public DacoQuestion getQuestion() {
	return question;
    }

    public void setQuestion(DacoQuestion question) {
	this.question = question;
    }

    /**
     *
     *
     * @return
     */
    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long crUuid) {
	fileUuid = crUuid;
    }

    /**
     *
     * @return
     */
    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long crVersionId) {
	fileVersionId = crVersionId;
    }

    /**
     *
     */
    public String getFileType() {
	return fileType;
    }

    public void setFileType(String type) {
	fileType = type;
    }

    /**
     *
     */
    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	fileName = name;
    }

    /**
     *
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }
}