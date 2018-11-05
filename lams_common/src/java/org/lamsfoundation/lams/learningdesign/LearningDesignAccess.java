/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Marks when an user used (opened, saved etc.) a learning design the last time.
 *
 * @author Marcin Cieslak
 *
 */
@Entity
@Table(name = "lams_learning_design_access")
public class LearningDesignAccess implements Serializable {
    private static final long serialVersionUID = -5315312828344350729L;

    @Embeddable
    public static class LearningDesignAccessPrimaryKey implements Serializable {
	private static final long serialVersionUID = 7198235946869480320L;

	@Column(name = "learning_design_id")
	private Long learningDesignId;

	@Column(name = "user_id")
	private Integer userId;

	public LearningDesignAccessPrimaryKey() {
	}

	public LearningDesignAccessPrimaryKey(Long learningDesignId, Integer userId) {
	    this.learningDesignId = learningDesignId;
	    this.userId = userId;
	}
    }

    @EmbeddedId
    private LearningDesignAccessPrimaryKey id;

    @Column(name = "access_date")
    private Date accessDate;

    @Transient
    String title;

    @Transient
    Integer workspaceFolderId;

    public LearningDesignAccessPrimaryKey getId() {
	return id;
    }

    public void setId(LearningDesignAccessPrimaryKey id) {
	this.id = id;
    }

    public Long getLearningDesignId() {
	return id.learningDesignId;
    }

    public Integer getUserId() {
	return id.userId;
    }

    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Integer getWorkspaceFolderId() {
	return workspaceFolderId;
    }

    public void setWorkspaceFolderId(Integer workspaceFolderId) {
	this.workspaceFolderId = workspaceFolderId;
    }
}