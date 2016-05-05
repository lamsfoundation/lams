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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $Id$ */

package org.lamsfoundation.lams.tool.scribe.model;

import org.apache.log4j.Logger;

/**
 * @author Anthony Sukkar
 *
 * @hibernate.class table="tl_lascrb11_heading"
 *
 */
public class ScribeHeading implements java.io.Serializable, Comparable<ScribeHeading>, Cloneable {

    private static final long serialVersionUID = -5643334348072895714L;

    private static final Logger log = Logger.getLogger(ScribeHeading.class);

    // Properties
    private Long uid;

    private Scribe scribe;

    private String headingText;

    private int displayOrder;

    public ScribeHeading() {
    }

    public ScribeHeading(int displayOrder) {
	this.displayOrder = displayOrder;
    }

    // Getters / Setters

    /**
     * @hibernate.property column="heading" type="text"
     */
    public String getHeadingText() {
	return headingText;
    }

    public void setHeadingText(String headingText) {
	this.headingText = headingText;
    }

    /**
     * 
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="scribe_uid"
     */
    public Scribe getScribe() {
	return scribe;
    }

    public void setScribe(Scribe scribe) {
	this.scribe = scribe;
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * 
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * 
     * @hibernate.property column="display_order"
     */
    public int getDisplayOrder() {
	return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }

    @Override
    public Object clone() {
	Object obj = null;
	try {
	    obj = super.clone();
	    ((ScribeHeading) obj).setUid(null);
	} catch (CloneNotSupportedException e) {
	    log.error("Failed to clone " + ScribeHeading.class);
	}

	return obj;
    }

    @Override
    public int compareTo(ScribeHeading o) {
	int returnValue;

	returnValue = new Integer(this.displayOrder).compareTo(new Integer(o.displayOrder));

	if (!this.equals(o) && returnValue == 0) {
	    // something is wrong.  return -1 to maintain natural ordering.
	    log.debug("Encountered two different scribe headings with equal displayOrder");
	    returnValue = -1;
	}

	return returnValue;
    }

}
