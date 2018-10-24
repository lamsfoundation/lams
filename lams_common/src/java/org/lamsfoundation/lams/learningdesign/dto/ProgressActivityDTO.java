/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.learningdesign.dto;

/**
 * The data transfer object to allow Authoring to view an activity from learner
 * progress bar or monitoring UI component.
 *
 * @author Jacky Fang
 * @since 2005-3-15
 * @version 1.1
 *
 */
public class ProgressActivityDTO {
    //---------------------------------------------------------------------
    // attributes
    //---------------------------------------------------------------------
    private Long activityID;
    private String activityURL;

    //---------------------------------------------------------------------
    // Construtors
    //---------------------------------------------------------------------
    /**
     * Minimum constructor
     */
    public ProgressActivityDTO(Long activityId) {
	this(activityId, null);
    }

    /**
     * Full constructor
     */
    public ProgressActivityDTO(Long activityId, String activityURL) {
	this.activityID = activityId;
	this.activityURL = activityURL;
    }

    /**
     * @return Returns the activityID.
     */
    public Long getActivityID() {
	return activityID;
    }

    /**
     * @return Returns the activityURL.
     */
    public String getActivityURL() {
	return activityURL;
    }

    /**
     * @param activityURL
     *            The activityURL to set.
     */
    public void setActivityURL(String activityURL) {
	this.activityURL = activityURL;
    }
}
