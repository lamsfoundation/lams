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

package org.lamsfoundation.lams.learning.web.form;

/**
 * @author daveg
 */
public class OptionsActivityForm extends ActivityForm {

    private String title;
    private String description;
    private int minimum;
    private int maximum;
    private boolean minimumLimitReached;
    private boolean hasCompletedActivities;
    private Long progressID;
    private boolean maxActivitiesReached;

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public int getMinimum() {
	return minimum;
    }

    public void setMinimum(int minimum) {
	this.minimum = minimum;
    }

    public int getMaximum() {
	return maximum;
    }

    public void setMaximum(int maximum) {
	this.maximum = maximum;
    }

    public boolean isMinimumLimitReached() {
	return minimumLimitReached;
    }

    public void setMinimumLimitReached(boolean minimumLimitReached) {
	this.minimumLimitReached = minimumLimitReached;
    }
    
    public boolean isHasCompletedActivities() {
	return hasCompletedActivities;
    }

    public void setHasCompletedActivities(boolean hasCompletedActivities) {
	this.hasCompletedActivities = hasCompletedActivities;
    }

    public Long getProgressID() {
	return progressID;
    }

    public void setProgressID(Long progressID) {
	this.progressID = progressID;
    }

    public boolean isMaxActivitiesReached() {
	return maxActivitiesReached;
    }

    public void setMaxActivitiesReached(boolean maxActivitiesReached) {
	this.maxActivitiesReached = maxActivitiesReached;
    }
}
