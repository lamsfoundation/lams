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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
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

/**
 */
@Entity
@DiscriminatorValue("1")
public class ToolActivityRatingCriteria extends RatingCriteria implements Serializable, Cloneable {

    private static final long serialVersionUID = 6459453751049962314L;
    /** Holds value of property toolContentId. */
    @Column(name="tool_content_id")
    private Long toolContentId;

    @Override
    public Object clone() {
	ToolActivityRatingCriteria newCriteria = (ToolActivityRatingCriteria) super.clone();
	newCriteria.setToolContentId(null);
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
     * Getter for property toolContentId.
     *
     * @return Value of property toolContentId.
     */
    public Long getToolContentId() {
	return this.toolContentId;
    }

    /**
     * Setter for property toolContentId.
     *
     * @param toolContentId
     *            New value of property toolContentId.
     */
    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

}
