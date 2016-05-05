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

package org.lamsfoundation.lams.tool.scribe.model;

import java.io.Serializable;

/**
 * @author Anthony Sukkar
 *
 * @hibernate.class table="tl_lascrb11_report_entry"
 *
 */
public class ScribeReportEntry implements Serializable {

    /**
     * TODO problems generating serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private Long uid;

    private String entryText;

    private ScribeHeading scribeHeading;

    private ScribeSession scribeSession;

    // Getters / Setters

    /**
     * @hibernate.property column="entry_text" type="text"
     */
    public String getEntryText() {
	return entryText;
    }

    public void setEntryText(String entryText) {
	this.entryText = entryText;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="scribe_heading_uid"
     */
    public ScribeHeading getScribeHeading() {
	return scribeHeading;
    }

    public void setScribeHeading(ScribeHeading scribeHeading) {
	this.scribeHeading = scribeHeading;
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="scribe_session_uid"
     */
    public ScribeSession getScribeSession() {
	return scribeSession;
    }

    public void setScribeSession(ScribeSession scribeSession) {
	this.scribeSession = scribeSession;
    }
}
