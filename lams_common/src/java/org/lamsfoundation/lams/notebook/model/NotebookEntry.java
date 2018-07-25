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



package org.lamsfoundation.lams.notebook.model;

import java.util.Date;

import org.lamsfoundation.lams.usermanagement.User;

/**
 *
 */
public class NotebookEntry implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 653296132134948803L;

    private Long uid;

    private Long externalID;

    private Integer externalIDType;

    private String externalSignature;

    private User user;

    private String title;

    private String entry;

    private Date createDate;

    private Date lastModified;

    private String lessonName;

    public NotebookEntry() {
    }

    public NotebookEntry(Long externalID, Integer externalIDType, String externalSignature, User user, String title,
	    String entry, Date createDate) {
	this.externalID = externalID;
	this.externalIDType = externalIDType;
	this.externalSignature = externalSignature;
	this.user = user;
	this.title = title;
	this.entry = entry;
	this.createDate = createDate;
    }

    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     */
    public Long getExternalID() {
	return externalID;
    }

    public void setExternalID(Long externalID) {
	this.externalID = externalID;
    }

    /**
     *
     */
    public Integer getExternalIDType() {
	return externalIDType;
    }

    public void setExternalIDType(Integer externalIDType) {
	this.externalIDType = externalIDType;
    }

    /**
     *
     */
    public String getExternalSignature() {
	return externalSignature;
    }

    public void setExternalSignature(String externalSignature) {
	this.externalSignature = externalSignature;
    }

    /**
     *
    *
     */
    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    /**
     *
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     */
    public String getEntry() {
	return entry;
    }

    public void setEntry(String entry) {
	this.entry = entry;
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

    /**
     *
     */
    public Date getLastModified() {
	return lastModified;
    }

    public void setLastModified(Date lastModified) {
	this.lastModified = lastModified;
    }

    public void setLessonName(String name) {
	this.lessonName = name;
    }

    public String getLessonName() {
	return this.lessonName;
    }
}
