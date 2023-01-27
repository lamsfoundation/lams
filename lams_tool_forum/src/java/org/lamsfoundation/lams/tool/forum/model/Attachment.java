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

package org.lamsfoundation.lams.tool.forum.model;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * @author conradb
 *
 *         A Wrapper class for uploaded files. An Attachment cannot exist independently
 *         and must belong to a Forum.
 */
@Entity
@Table(name = "tl_lafrum11_attachment")
public class Attachment implements Cloneable, Comparable<Attachment> {
    private static final Logger log = Logger.getLogger(Attachment.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "file_uuid")
    private Long fileUuid;

    @Column(name = "file_version_id")
    private Long fileVersionId;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "create_date")
    private Date created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_uid")
    private Message message;

    @Transient
    private String fileDisplayUuid;

    //Default contruction method
    public Attachment() {

    }

//  **********************************************************
    //		Function method for Attachment
//  **********************************************************
    @Override
    public Object clone() {
	Object obj = null;
	try {
	    obj = super.clone();
	    ((Attachment) obj).setUid(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + Attachment.class + " failed");
	}

	return obj;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Attachment)) {
	    return false;
	}

	final Attachment genericEntity = (Attachment) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid)
		.append(this.fileVersionId, genericEntity.fileVersionId).append(this.fileName, genericEntity.fileName)
		.append(this.fileType, genericEntity.fileType).append(this.created, genericEntity.created).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(fileVersionId).append(fileName).append(fileType).append(created)
		.toHashCode();
    }

//  **********************************************************
    //		get/set methods
//  **********************************************************
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long version) {
	this.fileVersionId = version;
    }

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String type) {
	this.fileType = type;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	this.fileName = name;
    }

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long uuid) {
	this.fileUuid = uuid;
    }

    public Date getCreated() {
	return created;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    public Message getMessage() {
	return message;
    }

    public void setMessage(Message message) {
	this.message = message;
    }

    public String getFileDisplayUuid() {
	return fileDisplayUuid;
    }

    public void setFileDisplayUuid(String fileDisplayUuid) {
	this.fileDisplayUuid = fileDisplayUuid;
    }

    @Override
    public int compareTo(Attachment o) {
	if (o == null) {
	    return 1;
	}
	if (this.getUid() != null && o.getUid() != null) {
	    return (int) (this.getUid() - o.getUid());
	} else {
	    return this.getUid() == null ? -1 : 1;
	}
    }
}