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

package org.lamsfoundation.lams.tool.wookie.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.wookie.service.WookieService;

/**
 * @hibernate.class table="tl_lawook10_wookie"
 */

public class Wookie implements java.io.Serializable, Cloneable {
    
    private static final long serialVersionUID = 579733009969321015L;

    static Logger log = Logger.getLogger(WookieService.class.getName());

    // Fields
    /**
     * 
     */
    private Long uid;

    private Date createDate;

    private Date updateDate;

    private Integer createBy;

    private String title;

    private String instructions;

    private boolean lockOnFinished;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private boolean contentInUse;

    private boolean defineLater;

    private Long toolContentId;

    private Set<WookieSession> wookieSessions;
    
    // Wookie parameters
    String widgetAuthorUrl;
    Integer widgetHeight;
    Integer widgetWidth;
    Boolean widgetMaximise;
    String widgetIdentifier;

    // Property accessors
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * 
     */

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="create_date"
     * 
     */

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     * @hibernate.property column="update_date"
     * 
     */

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     * @hibernate.property column="create_by" length="20"
     * 
     */

    public Integer getCreateBy() {
	return createBy;
    }

    public void setCreateBy(Integer createBy) {
	this.createBy = createBy;
    }

    /**
     * @hibernate.property column="title" length="255"
     * 
     */

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.property column="instructions" length="65535"
     * 
     */

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @hibernate.property column="lock_on_finished" length="1"
     * 
     */

    public boolean isLockOnFinished() {
	return lockOnFinished;
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
     * @hibernate.property column="content_in_use" length="1"
     * 
     */

    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * @hibernate.property column="define_later" length="1"
     * 
     */

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @hibernate.property column="tool_content_id" length="20"
     * 
     */

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="wookie_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.wookie.model.WookieSession"
     * 
     */

    public Set<WookieSession> getWookieSessions() {
	return wookieSessions;
    }

    public void setWookieSessions(Set<WookieSession> wookieSessions) {
	this.wookieSessions = wookieSessions;
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
	if (this == other) {
	    return true;
	}
	if (other == null) {
	    return false;
	}
	if (!(other instanceof Wookie)) {
	    return false;
	}
	Wookie castOther = (Wookie) other;

	return this.getUid() == castOther.getUid() || this.getUid() != null && castOther.getUid() != null
		&& this.getUid().equals(castOther.getUid());
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Wookie newInstance(Wookie fromContent, Long toContentId) {
	Wookie toContent = new Wookie();
	toContent = (Wookie) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    @Override
    protected Object clone() {

	Wookie wookie = null;
	try {
	    wookie = (Wookie) super.clone();
	    wookie.setUid(null);

	    // create an empty set for the wookieSession
	    wookie.wookieSessions = new HashSet<WookieSession>();

	} catch (CloneNotSupportedException cnse) {
	    Wookie.log.error("Error cloning " + Wookie.class);
	}
	return wookie;
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
     * @hibernate.property column="widget_author_url" length="511"
     * @return
     */
    public String getWidgetAuthorUrl() {
        return widgetAuthorUrl;
    }

    public void setWidgetAuthorUrl(String widgetAuthorUrl) {
        this.widgetAuthorUrl = widgetAuthorUrl;
    }
    
    /**
     * @hibernate.property column="widget_height" 
     * @return
     */
    public Integer getWidgetHeight() {
        return widgetHeight;
    }

    public void setWidgetHeight(Integer widgetHeight) {
        this.widgetHeight = widgetHeight;
    }

    /**
     * @hibernate.property column="widget_width" 
     * @return
     */
    public Integer getWidgetWidth() {
        return widgetWidth;
    }

    public void setWidgetWidth(Integer widgetWidth) {
        this.widgetWidth = widgetWidth;
    }

    /**
     * @hibernate.property column="widget_maximise" 
     * @return
     */
    public Boolean getWidgetMaximise() {
        return widgetMaximise;
    }

    public void setWidgetMaximise(Boolean widgetMaximise) {
        this.widgetMaximise = widgetMaximise;
    }

    /**
     * @hibernate.property column="widget_identifier"  length="511"
     * @return
     */
    public String getWidgetIdentifier() {
        return widgetIdentifier;
    }

    public void setWidgetIdentifier(String widgetIdentifier) {
        this.widgetIdentifier = widgetIdentifier;
    }
}
