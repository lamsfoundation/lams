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

package org.lamsfoundation.lams.monitoring.dto;

import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;

/**
 * DTO used to return the activity details needed for the contribute activities list
 *
 * @author Fiona Malikoff
 */
public class ContributeActivityDTO {

    private String title;
    private String description;
    private Long activityID;
    private Integer activityTypeID;
    private Integer orderID;
    private Long parentActivityID;
    private Vector<ContributeActivityDTO> childActivities;
    private Vector<ContributeEntry> contributeEntries;

    /** Warning: This does not set childActivities or contribution details !!!!!! */
    public ContributeActivityDTO(Activity activity) {
	this.title = activity.getTitle();
	this.description = activity.getDescription();
	this.activityID = activity.getActivityId();
	this.activityTypeID = activity.getActivityTypeId();
	this.orderID = activity.getOrderId();
	if (activity.getParentActivity() != null) {
	    this.parentActivityID = activity.getParentActivity().getActivityId();
	}
	this.contributeEntries = new Vector<>();
    }

    /**
     * @return Returns the activityID.
     */
    public Long getActivityID() {
	return activityID;
    }

    /**
     * @return Returns the activityTypeID.
     */
    public Integer getActivityTypeID() {
	return activityTypeID;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return Returns the orderID.
     */
    public Integer getOrderID() {
	return orderID;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;

    }

    public Vector<ContributeEntry> getContributeEntries() {
	return contributeEntries;
    }

    public void setChildActivities(Vector<ContributeActivityDTO> childActivities) {
	this.childActivities = childActivities;
    }

    public Vector<ContributeActivityDTO> getChildActivities() {
	return childActivities;
    }

    public Long getParentActivityID() {
	return parentActivityID;
    }

    public ContributeEntry addContribution(Integer contributionType, String url) {
	ContributeEntry entry = new ContributeEntry();
	entry.setContributionType(contributionType);
	entry.setURL(url);
	contributeEntries.add(entry);
	return entry;
    }

    public class ContributeEntry {
	private Boolean isRequired;
	private Boolean isComplete = false;

	private Integer contributionType;
	private String url;

	/**
	 * @return Returns the contributionType.
	 */
	public Integer getContributionType() {
	    return contributionType;
	}

	public void setContributionType(Integer contributionType) {
	    this.contributionType = contributionType;
	    boolean isReq = (contributionType != null) && (contributionType.equals(ContributionTypes.PERMISSION_GATE)
		    || contributionType.equals(ContributionTypes.PASSWORD_GATE)
		    || contributionType.equals(ContributionTypes.CHOSEN_GROUPING)
		    || contributionType.equals(ContributionTypes.CHOSEN_BRANCHING)
		    || contributionType.equals(ContributionTypes.CONTENT_EDITED)
		    || contributionType.equals(ContributionTypes.CONTRIBUTION));

	    this.isRequired = new Boolean(isReq);
	}

	/**
	 * @return Returns the isRequired.
	 */
	public Boolean getIsRequired() {
	    return isRequired;
	}

	public String getURL() {
	    return url;
	}

	public void setURL(String url) {
	    this.url = url;
	}

	public Boolean getIsComplete() {
	    return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
	    this.isComplete = isComplete;
	}
    }
}
