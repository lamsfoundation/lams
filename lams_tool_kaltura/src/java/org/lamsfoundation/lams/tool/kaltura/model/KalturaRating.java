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


package org.lamsfoundation.lams.tool.kaltura.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * KalturaRating
 *
 * @author Andrey Balan
 *
 *
 */
public class KalturaRating implements Cloneable {

    private static final Logger log = Logger.getLogger(KalturaRating.class);

    private Long uid;
    private float rating;
    private KalturaUser createBy;
    private KalturaItem kalturaItem;

    // **********************************************************
    // Function method for ImageRating
    // **********************************************************

    @Override
    public Object clone() {
	KalturaRating rating = null;
	try {
	    rating = (KalturaRating) super.clone();
	    rating.setUid(null);

	    // clone ImageGalleryUser as well
	    if (this.createBy != null) {
		rating.setCreateBy((KalturaUser) this.createBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + KalturaRating.class + " failed");
	}

	return rating;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof KalturaRating)) {
	    return false;
	}

	final KalturaRating genericEntity = (KalturaRating) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid).append(this.rating, genericEntity.rating)
		.append(this.createBy, genericEntity.createBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(rating).append(createBy).toHashCode();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    /**
     *
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     * @return
     */
    public KalturaUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(KalturaUser createBy) {
	this.createBy = createBy;
    }

    /**
     *
     * @return
     */
    public float getRating() {
	return rating;
    }

    public void setRating(float rating) {
	this.rating = rating;
    }

    /**
     *
     * @return
     */
    public KalturaItem getKalturaItem() {
	return kalturaItem;
    }

    public void setKalturaItem(KalturaItem item) {
	this.kalturaItem = item;
    }

}
