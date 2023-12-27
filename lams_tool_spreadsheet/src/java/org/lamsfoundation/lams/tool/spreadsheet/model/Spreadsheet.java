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

package org.lamsfoundation.lams.tool.spreadsheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;

/**
 * Spreadsheet
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_lasprd10_spreadsheet")
public class Spreadsheet implements Cloneable {
    private static final Logger log = Logger.getLogger(Spreadsheet.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "content_id")
    private Long contentId;

    @Column
    private String title;

    @Column
    private String instructions;

    @Column
    private String code;

    // advance

    @Column(name = "is_learner_allowed_to_save")
    private boolean isLearnerAllowedToSave;

    @Column(name = "is_marking_enabled")
    private boolean isMarkingEnabled;

    @Column(name = "lock_on_finished")
    private boolean lockWhenFinished;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    // general infomation

    @Column(name = "create_date")
    private Date created;

    @Column(name = "update_date")
    private Date updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "create_by")
    private SpreadsheetUser createdBy;

    // **********************************************************
    // Function method for Spreadsheet
    // **********************************************************
    public static Spreadsheet newInstance(Spreadsheet defaultContent, Long contentId) {
	Spreadsheet toContent = new Spreadsheet();
	toContent = (Spreadsheet) defaultContent.clone();
	toContent.setContentId(contentId);

	// reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setSpreadsheet(toContent);
	}
	return toContent;
    }

    @Override
    public Object clone() {

	Spreadsheet spreadsheet = null;
	try {
	    spreadsheet = (Spreadsheet) super.clone();
	    spreadsheet.setUid(null);
	    // clone ReourceUser as well
	    if (this.createdBy != null) {
		spreadsheet.setCreatedBy((SpreadsheetUser) this.createdBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + Spreadsheet.class + " failed");
	}

	return spreadsheet;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Spreadsheet)) {
	    return false;
	}

	final Spreadsheet genericEntity = (Spreadsheet) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid).append(this.title, genericEntity.title)
		.append(this.instructions, genericEntity.instructions).append(this.code, genericEntity.code)
		.append(this.created, genericEntity.created).append(this.updated, genericEntity.updated)
		.append(this.createdBy, genericEntity.createdBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(code).append(code)
		.append(created).append(updated).append(createdBy).toHashCode();
    }

    /**
     * Updates the modification data for this entity.
     */
    public void updateModificationData() {

	long now = System.currentTimeMillis();
	if (created == null) {
	    this.setCreated(new Date(now));
	}
	this.setUpdated(new Date(now));
    }

    // **********************************************************
    // get/set methods
    // **********************************************************
    /**
     * Returns the object's creation date
     *
     * @return date
     *
     */
    public Date getCreated() {
	return created;
    }

    /**
     * Sets the object's creation date
     *
     * @param created
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * Returns the object's date of last update
     *
     * @return date updated
     *
     */
    public Date getUpdated() {
	return updated;
    }

    /**
     * Sets the object's date of last update
     *
     * @param updated
     */
    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    /**
     * @return Returns the userid of the user who created the Share spreadsheet.
     */
    public SpreadsheetUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Share spreadsheet.
     */
    public void setCreatedBy(SpreadsheetUser createdBy) {
	this.createdBy = createdBy;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return Returns whether learner is allowed to save spreadsheet.
     */
    public boolean isLearnerAllowedToSave() {
	return isLearnerAllowedToSave;
    }

    /**
     * @param isLearnerAllowedToSave
     *            boolean describing whether learner is allowed to save spreadsheet or not
     */
    public void setLearnerAllowedToSave(boolean isLearnerAllowedToSave) {
	this.isLearnerAllowedToSave = isLearnerAllowedToSave;
    }

    /**
     * @return Returns whether the marking is enabled.
     */
    public boolean isMarkingEnabled() {
	return isMarkingEnabled;
    }

    /**
     * @param isMarkingEnabled
     *            boolean describing wwhether the marking is enabled
     */
    public void setMarkingEnabled(boolean isMarkingEnabled) {
	this.isMarkingEnabled = isMarkingEnabled;
    }

    /**
     * @return Returns the lockWhenFinish.
     */
    public boolean getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished
     *            Set to true to lock the spreadsheet for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the instructions set by the teacher.
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @return Returns spreadsheet code.
     */
    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
	// this.code = javaScriptEscape(code);;
    }

    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    private static String javaScriptEscape(String input) {
	StringBuffer filtered = new StringBuffer();

	for (char c : input.toCharArray()) {
	    switch (c) {
		case '\'':
		    filtered.append("\\'");
		    break;

		case '"':
		    filtered.append("\\\"");
		    break;

		case '\n':
		case '\r':
		    break;

		default:
		    filtered.append(c);
	    }
	}
	return filtered.toString();
    }
}
