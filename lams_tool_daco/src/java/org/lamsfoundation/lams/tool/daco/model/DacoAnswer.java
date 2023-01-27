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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * DacoAnswer
 *
 * @author Marcin Cieslak
 *
 *
 */
@Entity
@Table(name = "tl_ladaco10_answers")
public class DacoAnswer implements Cloneable {
    private static final Logger log = Logger.getLogger(DacoQuestion.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private DacoUser user;

    @Column(name = "record_id")
    private Integer recordId;

    @Column
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_uid")
    private DacoQuestion question;

    @Column(name = "file_uuid")
    private Long fileUuid;

    @Column(name = "file_version_id")
    private Long fileVersionId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "create_date")
    private Date createDate;

    @Transient
    private String fileDisplayUuid;

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

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uuid) {
	uid = uuid;
    }

    public DacoUser getUser() {
	return user;
    }

    public void setUser(DacoUser user) {
	this.user = user;
    }

    public Integer getRecordId() {
	return recordId;
    }

    public void setRecordId(Integer recordId) {
	this.recordId = recordId;
    }

    public String getAnswer() {
	return answer;
    }

    public void setAnswer(String answer) {
	this.answer = answer;
    }

    public DacoQuestion getQuestion() {
	return question;
    }

    public void setQuestion(DacoQuestion question) {
	this.question = question;
    }

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long crUuid) {
	fileUuid = crUuid;
    }

    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long crVersionId) {
	fileVersionId = crVersionId;
    }

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String type) {
	fileType = type;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	fileName = name;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public String getFileDisplayUuid() {
	return fileDisplayUuid;
    }

    public void setFileDisplayUuid(String fileDisplayUuid) {
	this.fileDisplayUuid = fileDisplayUuid;
    }
}