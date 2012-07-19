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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.scratchie.model;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Scratchie
 * 
 * @author Andrey Balan
 * 
 * @hibernate.class table="tl_lascrt11_scratchie_item"
 * 
 */
public class ScratchieItem implements Cloneable {
    private static final Logger log = Logger.getLogger(ScratchieItem.class);

    private Long uid;

    private String description;

    private boolean correct;

    private Integer orderId;

    private boolean isCreateByAuthor;

    private Date createDate;
    private ScratchieUser createBy;

    // ***********************************************
    // DTO fields:
    private boolean scratched;
    private Date scratchedDate;

    public Object clone() {
	ScratchieItem obj = null;
	try {
	    obj = (ScratchieItem) super.clone();
	    
	    ((ScratchieItem) obj).setUid(null);
	    // clone ReourceUser as well
	    if (this.createBy != null)
		((ScratchieItem) obj).setCreateBy((ScratchieUser) this.createBy.clone());

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ScratchieItem.class + " failed");
	}

	return obj;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     * @hibernate.property column="description"
     * @return
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @hibernate.many-to-one cascade="none" column="create_by"
     * 
     * @return
     */
    public ScratchieUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(ScratchieUser createBy) {
	this.createBy = createBy;
    }

    /**
     * @hibernate.property column="create_date"
     * @return
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     * @hibernate.property column="create_by_author"
     * @return
     */
    public boolean isCreateByAuthor() {
	return isCreateByAuthor;
    }

    public void setCreateByAuthor(boolean isCreateByAuthor) {
	this.isCreateByAuthor = isCreateByAuthor;
    }

    /**
     * @hibernate.property column="correct"
     * @return
     */
    public boolean isCorrect() {
	return correct;
    }

    public void setCorrect(boolean correctScratchie) {
	this.correct = correctScratchie;
    }
    
    /**
     * @hibernate.property column="order_id"
     * @return
     */
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setScratched(boolean complete) {
	this.scratched = complete;
    }

    public boolean isScratched() {
	return scratched;
    }
    
    public Date getScratchedDate() {
	return scratchedDate;
    }

    public void setScratchedDate(Date scratchedDate) {
	this.scratchedDate = scratchedDate;
    }
}
