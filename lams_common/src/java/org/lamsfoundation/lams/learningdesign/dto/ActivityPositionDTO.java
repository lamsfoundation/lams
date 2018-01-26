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


package org.lamsfoundation.lams.learningdesign.dto;

/**
 * Marks activity position within Learning Design. It can be extended to find out exact position (for example, 4th
 * activity of total 10).
 *
 * @author Marcin Cieslak
 */
public class ActivityPositionDTO {
    private Boolean last;
    private Boolean first;
    private Integer activityCount;

    public Boolean getLast() {
	return last;
    }

    public void setLast(Boolean last) {
	this.last = last;
    }

    public Boolean getFirst() {
	return first;
    }

    public void setFirst(Boolean first) {
	this.first = first;
    }

    public Integer getActivityCount() {
	return activityCount;
    }

    public void setActivityCount(Integer activityCount) {
	this.activityCount = activityCount;
    }
}