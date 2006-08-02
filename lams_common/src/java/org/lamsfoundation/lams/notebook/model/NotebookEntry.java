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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */

package org.lamsfoundation.lams.notebook.model;

import java.util.Date;

/**
 * @hibernate.class table="lams_notebook_entry"
 */
public class NotebookEntry implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 653296132134948803L;

	private Long uid;

	private Long externalID;

	private Integer externalIDType;

	private String externalSignature;

	private Integer userID;

	private String title;

	private String entry;
	
	private Date createDate;
	
	private Date lastModified;
	
	public NotebookEntry() {}
	
	public NotebookEntry(Long externalID, Integer externalIDType, String externalSignature, Integer userID, String title, String entry, Date createDate) {
		this.externalID = externalID;
		this.externalIDType = externalIDType;
		this.externalSignature = externalSignature;
		this.userID = userID;
		this.title = title;
		this.entry = entry;
		this.createDate = createDate;
	}

	/**
	 * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
	 */
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
	
	/**
	 * @hibernate.property column="external_id"
	 */
	public Long getExternalID() {
		return externalID;
	}

	public void setExternalID(Long externalID) {
		this.externalID = externalID;
	}
	
	/**
	 * @hibernate.property column="external_id_type"
	 */
	public Integer getExternalIDType() {
		return externalIDType;
	}

	public void setExternalIDType(Integer externalIDType) {
		this.externalIDType = externalIDType;
	}
	
	/**
	 * @hibernate.property column="external_signature"
	 */
	public String getExternalSignature() {
		return externalSignature;
	}

	public void setExternalSignature(String externalSignature) {
		this.externalSignature = externalSignature;
	}	

	/**
	 * @hibernate.property column="user_id"
	 */
	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	/**
	 * @hibernate.property column="title"
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @hibernate.property column="entry" type="text"
	 */
	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	/**
	 * @hibernate.property column="create_date"
	 */
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @hibernate.property column="last_modified"
	 */
	public Date getUpdateDate() {
		return lastModified;
	}

	public void setUpdateDate(Date updateDate) {
		this.lastModified = updateDate;
	}
}
