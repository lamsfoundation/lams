/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
		if(contributionType.equals(SimpleActivityStrategy.DEFINE_LATER) ||
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
		return activityID!=null?activityID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the activityTypeID.
	 */
	public Integer getActivityTypeID() {
		return activityTypeID!=null?activityTypeID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description!=null?description:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the orderID.
	 */
	public Integer getOrderID() {
		return orderID!=null?orderID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title!=null?title:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the contributionType.
	 */
	public Integer getContributionType() {
		return contributionType!=null?contributionType:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the isRequired.
	 */
	public Boolean getIsRequired() {
		return isRequired!=null?isRequired:new Boolean(false);
	}
}
