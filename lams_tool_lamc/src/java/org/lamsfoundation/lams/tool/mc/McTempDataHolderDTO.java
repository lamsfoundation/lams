/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that holds temporary data
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class McTempDataHolderDTO implements Comparable {
    protected Integer learnerMark;
    protected Integer totalMarksPossible;
    protected Integer displayOrder;

    @Override
    public int compareTo(Object o) {
	McTempDataHolderDTO mcTempDataHolderDTO = (McTempDataHolderDTO) o;

	if (mcTempDataHolderDTO == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("learnerMark: ", learnerMark)
		.append("totalMarksPossible : ", totalMarksPossible).append("displayOrder : ", displayOrder).toString();
    }

    /**
     * @return Returns the learnerMark.
     */
    public Integer getLearnerMark() {
	return learnerMark;
    }

    /**
     * @param learnerMark
     *            The learnerMark to set.
     */
    public void setLearnerMark(Integer learnerMark) {
	this.learnerMark = learnerMark;
    }

    /**
     * @return Returns the totalMarksPossible.
     */
    public Integer getTotalMarksPossible() {
	return totalMarksPossible;
    }

    /**
     * @param totalMarksPossible
     *            The totalMarksPossible to set.
     */
    public void setTotalMarksPossible(Integer totalMarksPossible) {
	this.totalMarksPossible = totalMarksPossible;
    }

    /**
     * @return Returns the displayOrder.
     */
    public Integer getDisplayOrder() {
	return displayOrder;
    }

    /**
     * @param displayOrder
     *            The displayOrder to set.
     */
    public void setDisplayOrder(Integer displayOrder) {
	this.displayOrder = displayOrder;
    }
}
