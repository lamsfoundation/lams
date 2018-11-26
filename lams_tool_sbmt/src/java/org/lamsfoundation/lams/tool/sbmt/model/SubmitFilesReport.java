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

package org.lamsfoundation.lams.tool.sbmt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

@Entity
@Table(name = "tl_lasbmt11_report")
public class SubmitFilesReport implements Serializable, Cloneable {
    private static final long serialVersionUID = -3415065437595925246L;
    private static Logger log = Logger.getLogger(SubmitFilesReport.class);

    @Id
    @Column(name = "report_id")
    private Long reportID;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "report")
    @JoinColumn(name = "report_id")
    private SubmissionDetails details;

    @Column
    private String comments;

    @Column
    private Float marks;

    @Column(name = "mark_file_uuid")
    private Long markFileUUID;

    @Column(name = "mark_file_name")
    private String markFileName;

    @Column(name = "mark_file_version_id")
    private Long markFileVersionID;

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("reportID", getReportID()).append("comments", getComments())
		.append("marks", getMarks()).toString();
    }

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

    // ***********************************************************
    // Get / Set methods
    // ***********************************************************

    public Long getReportID() {
	return this.reportID;
    }

    public void setReportID(Long reportID) {
	this.reportID = reportID;
    }

    public SubmissionDetails getDetails() {
	return details;
    }

    public void setDetails(SubmissionDetails details) {
	this.details = details;
    }

    public String getComments() {
	return this.comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public Float getMarks() {
	return this.marks;
    }

    public void setMarks(Float marks) {
	this.marks = marks;
    }

    public Long getMarkFileUUID() {
	return markFileUUID;
    }

    public void setMarkFileUUID(Long markFileUUID) {
	this.markFileUUID = markFileUUID;
    }

    public String getMarkFileName() {
	return markFileName;
    }

    public void setMarkFileName(String markFileName) {
	this.markFileName = markFileName;
    }

    public Long getMarkFileVersionID() {
	return markFileVersionID;
    }

    public void setMarkFileVersionID(Long markFileVersionID) {
	this.markFileVersionID = markFileVersionID;
    }
}
