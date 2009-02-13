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

/* $Id$ */
package org.lamsfoundation.lams.planner.dto;

import java.util.List;

public class PedagogicalPlannerTemplateDTO {
    private String sequenceTitle;
    private Boolean sendInPortions;
    private Integer activitiesInPortion;
    private Long submitDelay;
    private List<PedagogicalPlannerActivityDTO> activities;
    private Long learningDesignID;
    private Integer activitySupportingPlannerCount = 0;

    public String getSequenceTitle() {
	return sequenceTitle;
    }

    public void setSequenceTitle(String sequenceTitle) {
	this.sequenceTitle = sequenceTitle;
    }

    public Boolean getSendInPortions() {
	return sendInPortions;
    }

    public void setSendInPortions(Boolean sendInPortions) {
	this.sendInPortions = sendInPortions;
    }

    public Integer getActivitiesInPortion() {
	return activitiesInPortion;
    }

    public void setActivitiesInPortion(Integer activitiesInPortion) {
	this.activitiesInPortion = activitiesInPortion;
    }

    public Long getSubmitDelay() {
	return submitDelay;
    }

    public void setSubmitDelay(Long submitDelay) {
	this.submitDelay = submitDelay;
    }

    public List<PedagogicalPlannerActivityDTO> getActivities() {
	return activities;
    }

    public void setActivities(List<PedagogicalPlannerActivityDTO> activities) {
	this.activities = activities;
    }

    public Long getLearningDesignID() {
	return learningDesignID;
    }

    public void setLearningDesignID(Long learningDesignID) {
	this.learningDesignID = learningDesignID;
    }

    public Integer getActivitySupportingPlannerCount() {
	return activitySupportingPlannerCount;
    }

    public void setActivitySupportingPlannerCount(Integer activitySupportingPlannerCount) {
	this.activitySupportingPlannerCount = activitySupportingPlannerCount;
    }
}