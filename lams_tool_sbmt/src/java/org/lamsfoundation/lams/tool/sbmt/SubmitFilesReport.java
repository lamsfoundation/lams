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


package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 *
 * @serial -3415065437595925246L
 */
public class SubmitFilesReport implements Serializable, Cloneable {

    private static final long serialVersionUID = -3415065437595925246L;

    private static Logger log = Logger.getLogger(SubmitFilesReport.class);

    /** identifier field */
    private Long reportID;

    /** persistent field */
    private String comments;

    /** nullable persistent field */
    private Float marks;

    /** persistent field */
    private Long markFileUUID;

    /** persistent field */
    private String markFileName;

    /** persistent field */
    private Long markFileVersionID;

    /** full constructor */
    public SubmitFilesReport(String comments, Float marks) {
	this.comments = comments;
	this.marks = marks;
    }

    /** default constructor */
    public SubmitFilesReport() {
    }

    /**
     *
     *
     */
    public Long getReportID() {
	return this.reportID;
    }

    public void setReportID(Long reportID) {
	this.reportID = reportID;
    }

    /**
     *
     */
    public String getComments() {
	return this.comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    /**
     *
     */
    public Float getMarks() {
	return this.marks;
    }

    public void setMarks(Float marks) {
	this.marks = marks;
    }

    /**
     *
     */
    public Long getMarkFileUUID() {
	return markFileUUID;
    }

    public void setMarkFileUUID(Long markFileUUID) {
	this.markFileUUID = markFileUUID;
    }

    /**
     *
     */
    public String getMarkFileName() {
	return markFileName;
    }

    public void setMarkFileName(String markFileName) {
	this.markFileName = markFileName;
    }

    /**
     *
     */
    public Long getMarkFileVersionID() {
	return markFileVersionID;
    }

    public void setMarkFileVersionID(Long markFileVersionID) {
	this.markFileVersionID = markFileVersionID;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("reportID", getReportID()).append("comments", getComments())
		.append("marks", getMarks()).toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {

	Object obj = null;
	try {
	    obj = super.clone();
	    //never clone key!
	    ((SubmitFilesReport) obj).setReportID(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + SubmitFilesReport.class + " failed");
	}
	return obj;
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof SubmitFilesReport)) {
	    return false;
	}
	SubmitFilesReport castOther = (SubmitFilesReport) other;
	return new EqualsBuilder().append(this.getReportID(), castOther.getReportID())
		.append(this.getComments(), castOther.getComments()).append(this.getMarks(), castOther.getMarks())
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getReportID()).append(getComments()).append(getMarks()).toHashCode();
    }
}
