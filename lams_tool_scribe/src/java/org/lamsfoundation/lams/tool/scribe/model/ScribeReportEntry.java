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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Anthony Sukkar
 *
 *
 *
 */
@Entity
@Table(name = "tl_lascrb11_report_entry")
public class ScribeReportEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "entry_text")
    private String entryText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scribe_heading_uid")
    private ScribeHeading scribeHeading;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scribe_session_uid")
    private ScribeSession scribeSession;

    public String getEntryText() {
	return entryText;
    }

    public void setEntryText(String entryText) {
	this.entryText = entryText;
    }

    public ScribeHeading getScribeHeading() {
	return scribeHeading;
    }

    public void setScribeHeading(ScribeHeading scribeHeading) {
	this.scribeHeading = scribeHeading;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public ScribeSession getScribeSession() {
	return scribeSession;
    }

    public void setScribeSession(ScribeSession scribeSession) {
	this.scribeSession = scribeSession;
    }
}
