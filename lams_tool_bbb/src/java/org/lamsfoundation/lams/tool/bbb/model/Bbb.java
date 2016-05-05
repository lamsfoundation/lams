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
/* $Id$ */

package org.lamsfoundation.lams.tool.bbb.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;

/**
 *
 *
 * @hibernate.class table="tl_labbb10_bbb"
 */

public class Bbb implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 579733009969321015L;

    private static final Logger logger = Logger.getLogger(Bbb.class);

    private IToolContentHandler toolContentHandler;

    // Persistent Fields

    private Long uid;

    private Date createDate;

    private Date updateDate;

    private Long createBy;

    private String title;

    private String instructions;

    private boolean lockOnFinished;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private boolean contentInUse;

    private boolean defineLater;

    private Long toolContentId;

    private Set<BbbSession> bbbSessions;

    // Property accessors
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     *
     */

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="create_date"
     *
     */

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     * @hibernate.property column="update_date"
     *
     */

    public Date getUpdateDate() {
	return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     * @hibernate.property column="create_by" length="20"
     *
     */

    public Long getCreateBy() {
	return this.createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    /**
     * @hibernate.property column="title" length="255"
     *
     */

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.property column="instructions" length="65535"
     *
     */

    public String getInstructions() {
	return this.instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @hibernate.property column="lock_on_finished" length="1"
     *
     */

    public boolean isLockOnFinished() {
	return this.lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    /**
     * @hibernate.property column="reflect_on_activity" length="1"
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     * @hibernate.property column="reflect_instructions" length="65535"
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     * @hibernate.property column="content_in_use" length="1"
     *
     */

    public boolean isContentInUse() {
	return this.contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * @hibernate.property column="define_later" length="1"
     *
     */

    public boolean isDefineLater() {
	return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @hibernate.property column="tool_content_id" length="20"
     *
     */

    public Long getToolContentId() {
	return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="bbb_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.bbb.model.BbbSession"
     *
     */
    public Set<BbbSession> getBbbSessions() {
	return this.bbbSessions;
    }

    public void setBbbSessions(Set<BbbSession> bbbSessions) {
	this.bbbSessions = bbbSessions;
    }

    /**
     * toString
     *
     * @return String
     */
    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("title").append("='").append(getTitle()).append("' ");
	buffer.append("instructions").append("='").append(getInstructions()).append("' ");
	buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if ((other == null)) {
	    return false;
	}
	if (!(other instanceof Bbb)) {
	    return false;
	}
	Bbb castOther = (Bbb) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Bbb newInstance(Bbb fromContent, Long toContentId, IToolContentHandler bbbToolContentHandler) {
	Bbb toContent = new Bbb();
	fromContent.toolContentHandler = bbbToolContentHandler;
	toContent = (Bbb) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    @Override
    protected Object clone() {

	Bbb bbb = null;
	try {
	    bbb = (Bbb) super.clone();
	    bbb.setUid(null);

	    // create an empty set for the bbbSession
	    bbb.bbbSessions = new HashSet<BbbSession>();

	} catch (CloneNotSupportedException cnse) {
	    logger.error("Error cloning " + Bbb.class);
	}
	return bbb;
    }

    public IToolContentHandler getToolContentHandler() {
	return toolContentHandler;
    }

    public void setToolContentHandler(IToolContentHandler toolContentHandler) {
	this.toolContentHandler = toolContentHandler;
    }
}
