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

package org.lamsfoundation.lams.journal.model;

/**
 * @hibernate.class table="lams_journal_entry"
 */
public class JournalEntry implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 653296132134948803L;

	private Long uid;

	private Long lessionID;

	private Long toolSessionID;

	private String toolSignature;

	private Long userID;

	private String title;

	private String entry;

	/**
	 * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
	 */
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public void setLessionID(Long lessionID) {
		this.lessionID = lessionID;
	}

	/**
	 * @hibernate.property column="lesson_id" length="20"
	 * 
	 */
	public Long getLessionID() {
		return lessionID;
	}

	/**
	 * @hibernate.property column="tool_session_id"
	 */
	public Long getToolSessionID() {
		return toolSessionID;
	}

	public void setToolSessionID(Long toolSessionID) {
		this.toolSessionID = toolSessionID;
	}

	/**
	 * @hibernate.property column="toolSignature"
	 */
	public String getToolSignature() {
		return toolSignature;
	}

	public void setToolSignature(String toolSignature) {
		this.toolSignature = toolSignature;
	}

	/**
	 * @hibernate.property column="user_id"
	 */
	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
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
	 * @hibernate.property column="entry"
	 */
	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

}
