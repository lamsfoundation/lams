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


package org.lamsfoundation.lams.tool.scribe.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.scribe.service.ScribeService;

/**
 *
 */

public class Scribe implements java.io.Serializable, Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 579733009969321015L;

    static Logger log = Logger.getLogger(ScribeService.class.getName());

    // Fields
    /**
     * 
     */
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

    private boolean autoSelectScribe;

    private Long toolContentId;

    private Set scribeSessions;

    private Set scribeHeadings;

    private boolean showAggregatedReports;

    // Property accessors
    /**
     *
     * 
     */

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     * 
     */

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     *
     * 
     */

    public Date getUpdateDate() {
	return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     *
     * 
     */

    public Long getCreateBy() {
	return this.createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    /**
     *
     * 
     */

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     * 
     */

    public String getInstructions() {
	return this.instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     *
     * 
     */

    public boolean isLockOnFinished() {
	return this.lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    /**
     *
     * 
     */
    public boolean isAutoSelectScribe() {
	return autoSelectScribe;
    }

    public void setAutoSelectScribe(boolean autoSelectScribe) {
	this.autoSelectScribe = autoSelectScribe;
    }

    /**
     *
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     *
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
     * 
     */

    public boolean isContentInUse() {
	return this.contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     *
     * 
     */

    public boolean isDefineLater() {
	return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     *
     * 
     */

    public Long getToolContentId() {
	return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     *
     *
     *
     * 
     */

    public Set getScribeSessions() {
	return this.scribeSessions;
    }

    public void setScribeSessions(Set scribeSessions) {
	this.scribeSessions = scribeSessions;
    }

    /**
     *
     *
     *
     */
    public Set getScribeHeadings() {
	return scribeHeadings;
    }

    public void setScribeHeadings(Set scribeHeadings) {
	this.scribeHeadings = scribeHeadings;
    }

    /**
     *
     * 
     */

    public boolean isShowAggregatedReports() {
	return this.showAggregatedReports;
    }

    public void setShowAggregatedReports(boolean showAggregatedReports) {
	this.showAggregatedReports = showAggregatedReports;
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
	if (!(other instanceof Scribe)) {
	    return false;
	}
	Scribe castOther = (Scribe) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Scribe newInstance(Scribe fromContent, Long toContentId) {
	Scribe toContent = new Scribe();
	toContent = (Scribe) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    @Override
    protected Object clone() {

	Scribe scribe = null;
	try {
	    scribe = (Scribe) super.clone();
	    scribe.setUid(null);

	    if (scribeHeadings != null) {
		// create copy of headings
		Iterator iter = scribeHeadings.iterator();
		Set<ScribeHeading> set = new HashSet<ScribeHeading>();
		while (iter.hasNext()) {
		    ScribeHeading originalHeading = (ScribeHeading) iter.next();
		    ScribeHeading newHeading = (ScribeHeading) originalHeading.clone();
		    newHeading.setScribe(scribe);
		    set.add(newHeading);
		}
		scribe.scribeHeadings = set;

	    }

	    // create an empty set for the scribeSession
	    scribe.scribeSessions = new HashSet();

	} catch (CloneNotSupportedException cnse) {
	    log.error("Error cloning " + Scribe.class);
	}
	return scribe;
    }
}