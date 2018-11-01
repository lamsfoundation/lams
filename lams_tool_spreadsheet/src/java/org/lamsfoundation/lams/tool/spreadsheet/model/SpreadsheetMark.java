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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.spreadsheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * SpreadsheetMark
 * 
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_lasprd10_spreadsheet_mark")
public class SpreadsheetMark {
    private static Logger log = Logger.getLogger(SpreadsheetMark.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private Float marks;
    
    @Column
    private String comments;
    
    @Column(name = "date_marks_released")
    private Date dateMarksReleased;

    /** default constructor */
    public SpreadsheetMark() {
    }

    /** full constructor */
    public SpreadsheetMark(String comments, Float marks, Date dateMarksReleased) {
	this.comments = comments;
	this.marks = marks;
	this.dateMarksReleased = dateMarksReleased;
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Float getMarks() {
	return this.marks;
    }

    public void setMarks(Float marks) {
	this.marks = marks;
    }

    public String getComments() {
	return this.comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public Date getDateMarksReleased() {
	return this.dateMarksReleased;
    }

    public void setDateMarksReleased(Date dateMarksReleased) {
	this.dateMarksReleased = dateMarksReleased;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("reportID", getUid()).append("comments", getComments())
		.append("marks", getMarks()).append("dateMarksReleased", getDateMarksReleased()).toString();
    }

    @Override
    public Object clone() {

	Object obj = null;
	try {
	    obj = super.clone();
	    //never clone key!
	    ((SpreadsheetMark) obj).setUid(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + SpreadsheetMark.class + " failed");
	}
	return obj;
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof SpreadsheetMark)) {
	    return false;
	}
	SpreadsheetMark castOther = (SpreadsheetMark) other;
	return new EqualsBuilder().append(this.getUid(), castOther.getUid())
		.append(this.getComments(), castOther.getComments()).append(this.getMarks(), castOther.getMarks())
		.append(this.getDateMarksReleased(), castOther.getDateMarksReleased()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getUid()).append(getComments()).append(getMarks())
		.append(getDateMarksReleased()).toHashCode();
    }

}
