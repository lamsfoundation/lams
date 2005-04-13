/*
 * Created on Mar 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dto;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MonitoringActivityDTO {
	
	private String title;
	private String description;
	private Long activityID;
	private Integer activityTypeID;	
	private Integer orderID;
	private Integer contributionType;
	private Boolean isRequired;
	
	
	public MonitoringActivityDTO(Activity activity){
		this.title = activity.getTitle();
		this.description = activity.getDescription();
		this.activityID = activity.getActivityId();
		this.activityTypeID = activity.getActivityTypeId();		
		this.orderID = activity.getOrderId();		
	}	
	public MonitoringActivityDTO(Activity activity,Integer contributionType,Boolean required){
		this.title = activity.getTitle();
		this.description = activity.getDescription();
		this.activityID = activity.getActivityId();
		this.activityTypeID = activity.getActivityTypeId();
		this.contributionType = contributionType;
		this.orderID = activity.getOrderId();		
		this.isRequired = required;
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
