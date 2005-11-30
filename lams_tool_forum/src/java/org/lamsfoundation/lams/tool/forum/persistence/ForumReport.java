/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Date;

/**
 *
 * @hibernate.class  table="tl_lafrum11_report"
 *
 */
public class ForumReport {
	
	private Long uid;
	private Float mark;
	private String comment;
	private Date dateMarksReleased;
    /**
     * @hibernate.id column="uid" generator-class="native"
     */
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * @hibernate.property column="comment"  type="text"
	 * @return
	 */
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @hibernate.property column="release_date" 
	 * @return
	 */
	public Date getDateMarksReleased() {
		return dateMarksReleased;
	}
	public void setDateMarksReleased(Date dateMarksReleased) {
		this.dateMarksReleased = dateMarksReleased;
	}
	/**
	 * @hibernate.property column="mark" 
	 * @return
	 */
	public Float getMark() {
		return mark;
	}
	public void setMark(Float mark) {
		this.mark = mark;
	}

}
