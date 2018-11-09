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


package org.lamsfoundation.lams.rating.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("2")
public class AuthoredItemRatingCriteria extends ToolActivityRatingCriteria implements Cloneable, Serializable {
    /** Holds value of property itemId. */
    @Column(name = "item_id")
    private Long itemId;

    @Override
    public Object clone() {
	AuthoredItemRatingCriteria newCriteria = (AuthoredItemRatingCriteria) super.clone();
	return newCriteria;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("ratingCriteriaId", getRatingCriteriaId()).toString();
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    @Override
    public boolean isNull() {
	return false;
    }

    /**
     * Getter for property itemId.
     *
     * @return Value of property itemId.
     */
    public Long getItemId() {
	return this.itemId;
    }

    /**
     * Setter for property itemId.
     *
     * @param itemId
     *            New value of property itemId.
     */
    public void setItemId(Long itemId) {
	this.itemId = itemId;
    }
}
