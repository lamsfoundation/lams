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
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign.dto;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 */
public class MonitoringActivityDTO {
	
	private String title;
	private String description;
	private Long activityID;
	private Integer activityTypeID;	
	private Integer orderID;
	private Integer contributionType;
	private Boolean isRequired;
	
	public MonitoringActivityDTO(){
		
	}
	public MonitoringActivityDTO(Activity activity,Integer contributionType){
		this.title = activity.getTitle();
		this.description = activity.getDescription();
		this.activityID = activity.getActivityId();
		this.activityTypeID = activity.getActivityTypeId();
		this.contributionType = contributionType;
		this.orderID = activity.getOrderId();		
		this.isRequired = new Boolean(calculateIsRequired(contributionType));
	}
	private boolean calculateIsRequired(Integer contributionType){
		if(contributionType!=null && contributionType.equals(SimpleActivityStrategy.DEFINE_LATER) ||
				contributionType.equals(SimpleActivityStrategy.PERMISSION_GATE)||
				contributionType.equals(SimpleActivityStrategy.CHOSEN_GROUPING))
			return true;
		else
			return false;
		
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
	/**
	 * @return Returns the contributionType.
	 */
	public Integer getContributionType() {
		return contributionType;
	}
	/**
	 * @return Returns the isRequired.
	 */
	public Boolean getIsRequired() {
		return isRequired;
	}
}
