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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.daco.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * @author Dapeng Ni
 * 
 * A Wrapper class for uploaded files. An Attachment cannot exist independently and must belong to a Daco.
 * 
 * 
 * @hibernate.class table="tl_ladaco10_attachments"
 * 
 */
public class DacoAttachment implements Cloneable {
	private static final Logger log = Logger.getLogger(DacoAttachment.class);

	private Long uid;
	private Long fileUuid;
	private Long fileVersionId;
	private String fileType;
	private String fileName;
	private Date created;

	// Default contruction method
	public DacoAttachment() {

	}

	// **********************************************************
	// Function method for Attachment
	// **********************************************************
	@Override
	public Object clone() {
		Object obj = null;
		try {
			obj = super.clone();
			((DacoAttachment) obj).setUid(null);
		}
		catch (CloneNotSupportedException e) {
			DacoAttachment.log.error("When clone " + DacoAttachment.class + " failed");
		}

		return obj;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DacoAttachment)) {
			return false;
		}

		final DacoAttachment genericEntity = (DacoAttachment) o;

		return new EqualsBuilder().append(uid, genericEntity.uid).append(fileVersionId, genericEntity.fileVersionId).append(
				fileName, genericEntity.fileName).append(fileType, genericEntity.fileType).append(created, genericEntity.created)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(uid).append(fileVersionId).append(fileName).append(fileType).append(created)
				.toHashCode();
	}

	// **********************************************************
	// get/set methods
	// **********************************************************
	/**
	 * @hibernate.id column="uid" generator-class="native"
	 */
	public Long getUid() {
		return uid;
	}

	private void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * @hibernate.property column="file_version_id"
	 * 
	 */
	public Long getFileVersionId() {
		return fileVersionId;
	}

	public void setFileVersionId(Long version) {
		fileVersionId = version;
	}

	/**
	 * @hibernate.property column="file_type"
	 */
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String type) {
		fileType = type;
	}

	/**
	 * @hibernate.property column="file_name"
	 */
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String name) {
		fileName = name;
	}

	/**
	 * @hibernate.property column="file_uuid"
	 * @return
	 */
	public Long getFileUuid() {
		return fileUuid;
	}

	public void setFileUuid(Long uuid) {
		fileUuid = uuid;
	}

	/**
	 * @hibernate.property column="create_date"
	 * @return
	 */
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
