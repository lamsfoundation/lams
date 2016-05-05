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
package org.lamsfoundation.lams.tool.commonCartridge.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * CommonCartridge
 *
 * @author Andrey Balan
 *
 * @hibernate.class table="tl_laimsc11_commoncartridge"
 *
 */
public class CommonCartridge implements Cloneable {

    private static final Logger log = Logger.getLogger(CommonCartridge.class);

    // key
    private Long uid;

    // tool contentID
    private Long contentId;

    private String title;

    private String instructions;

    // advance

    private boolean runAuto;

    private int miniViewCommonCartridgeNumber;

    private boolean lockWhenFinished;

    private boolean defineLater;

    private boolean contentInUse;

    // general infomation
    private Date created;

    private Date updated;

    private CommonCartridgeUser createdBy;

    // commonCartridge Items
    private Set commonCartridgeItems;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    // *************** NON Persist Fields ********************

    private String miniViewNumberStr;

    /**
     * Default contruction method.
     *
     */
    public CommonCartridge() {
	commonCartridgeItems = new HashSet();
    }

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
		Iterator iter = commonCartridgeItems.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    CommonCartridgeItem item = (CommonCartridgeItem) iter.next();
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
	    CommonCartridge.log.error("When clone " + CommonCartridge.class + " failed");
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
     * @hibernate.property column="create_date"
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
     * @hibernate.property column="update_date"
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
     *
     * @hibernate.many-to-one cascade="save-update" column="create_by"
     *
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
     * @return Returns the title.
     *
     * @hibernate.property column="title"
     *
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
     * @return Returns the lockWhenFinish.
     *
     * @hibernate.property column="lock_on_finished"
     *
     */
    public boolean getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished
     *            Set to true to lock the commonCartridge for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the instructions set by the teacher.
     *
     * @hibernate.property column="instructions" type="text"
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     *
     *
     * @hibernate.set lazy="true" inverse="false" cascade="all" order-by="create_date desc"
     * @hibernate.collection-key column="commonCartridge_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem"
     *
     * @return
     */
    public Set getCommonCartridgeItems() {
	return commonCartridgeItems;
    }

    public void setCommonCartridgeItems(Set commonCartridgeItems) {
	this.commonCartridgeItems = commonCartridgeItems;
    }

    /**
     * @hibernate.property column="content_in_use"
     * @return
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * @hibernate.property column="define_later"
     * @return
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @hibernate.property column="content_id" unique="true"
     * @return
     */
    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    /**
     * @hibernate.property column="mini_view_commonCartridge_number"
     * @return
     */
    public int getMiniViewCommonCartridgeNumber() {
	return miniViewCommonCartridgeNumber;
    }

    public void setMiniViewCommonCartridgeNumber(int minViewCommonCartridgeNumber) {
	miniViewCommonCartridgeNumber = minViewCommonCartridgeNumber;
    }

    /**
     * @hibernate.property column="allow_auto_run"
     * @return
     */
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

    /**
     * @hibernate.property column="reflect_instructions"
     * @return
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     * @hibernate.property column="reflect_on_activity"
     * @return
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }
}
