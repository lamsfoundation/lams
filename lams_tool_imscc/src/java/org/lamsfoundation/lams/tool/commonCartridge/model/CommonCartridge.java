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

package org.lamsfoundation.lams.tool.commonCartridge.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

/**
 * CommonCartridge
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laimsc11_commoncartridge")
public class CommonCartridge implements Cloneable {
    private static final Logger log = Logger.getLogger(CommonCartridge.class);

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

    // advance

    @Column(name = "allow_auto_run")
    private boolean runAuto;

    @Column(name = "mini_view_commonCartridge_number")
    private int miniViewCommonCartridgeNumber;

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
    private CommonCartridgeUser createdBy;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "commonCartridge_uid")
    @OrderBy("create_date DESC")
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<CommonCartridgeItem> commonCartridgeItems = new HashSet<>();;

    @Column(name = "reflect_on_activity")
    private Boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    // *************** NON Persist Fields ********************

    @Transient
    private String miniViewNumberStr;

    // **********************************************************
    // Function method for CommonCartridge
    // **********************************************************
    public static CommonCartridge newInstance(CommonCartridge defaultContent, Long contentId) {
	CommonCartridge toContent = new CommonCartridge();
	toContent = (CommonCartridge) defaultContent.clone();
	toContent.setContentId(contentId);

	// reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setCommonCartridge(toContent);
	    Set<CommonCartridgeItem> items = toContent.getCommonCartridgeItems();
	    for (CommonCartridgeItem item : items) {
		item.setCreateBy(toContent.getCreatedBy());
	    }
	}
	return toContent;
    }

    @Override
    public Object clone() {
	CommonCartridge commonCartridge = null;

	try {
	    commonCartridge = (CommonCartridge) super.clone();
	    commonCartridge.setUid(null);
	    if (commonCartridgeItems != null) {
		Iterator<CommonCartridgeItem> iter = commonCartridgeItems.iterator();
		Set<CommonCartridgeItem> set = new HashSet<>();
		while (iter.hasNext()) {
		    CommonCartridgeItem item = iter.next();
		    CommonCartridgeItem newItem = (CommonCartridgeItem) item.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newItem);
		}
		commonCartridge.commonCartridgeItems = set;
	    }
	    // clone ReourceUser as well
	    if (createdBy != null) {
		commonCartridge.setCreatedBy((CommonCartridgeUser) createdBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + CommonCartridge.class + " failed");
	}

	return commonCartridge;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof CommonCartridge)) {
	    return false;
	}

	final CommonCartridge genericEntity = (CommonCartridge) o;

	return new EqualsBuilder().append(uid, genericEntity.uid).append(title, genericEntity.title)
		.append(instructions, genericEntity.instructions).append(created, genericEntity.created)
		.append(updated, genericEntity.updated).append(createdBy, genericEntity.createdBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(created).append(updated)
		.append(createdBy).toHashCode();
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
     * @return Returns the userid of the user who created the Share commonCartridge.
     */
    public CommonCartridgeUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Share commonCartridge.
     */
    public void setCreatedBy(CommonCartridgeUser createdBy) {
	this.createdBy = createdBy;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
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

    public Set<CommonCartridgeItem> getCommonCartridgeItems() {
	return commonCartridgeItems;
    }

    public void setCommonCartridgeItems(Set<CommonCartridgeItem> commonCartridgeItems) {
	this.commonCartridgeItems = commonCartridgeItems;
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

    public int getMiniViewCommonCartridgeNumber() {
	return miniViewCommonCartridgeNumber;
    }

    public void setMiniViewCommonCartridgeNumber(int minViewCommonCartridgeNumber) {
	miniViewCommonCartridgeNumber = minViewCommonCartridgeNumber;
    }

    public boolean isRunAuto() {
	return runAuto;
    }

    public void setRunAuto(boolean runAuto) {
	this.runAuto = runAuto;
    }

    /**
     * For display use
     *
     * @return
     */
    public String getMiniViewNumberStr() {
	return miniViewNumberStr;
    }

    public void setMiniViewNumberStr(String minViewNumber) {
	miniViewNumberStr = minViewNumber;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    // LDEV-4744 The JSTL pages require this to be a primitive boolean
    public Boolean getReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(Boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }
}