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
	private Integer contributionTypeID[];
	private Integer orderID;
	
	public MonitoringActivityDTO(Activity activity){
		this.title = activity.getTitle();
		this.description = activity.getDescription();
		this.activityID = activity.getActivityId();
		this.activityTypeID = activity.getActivityTypeId();
		this.contributionTypeID = new Integer[1];
		this.orderID = activity.getOrderId();		
	}
	public MonitoringActivityDTO(Activity activity,Integer[] contributionTypeID){
		this.title = activity.getTitle();
		this.description = activity.getDescription();
		this.activityID = activity.getActivityId();
		this.activityTypeID = activity.getActivityTypeId();
		this.contributionTypeID = contributionTypeID;
		this.orderID = activity.getOrderId();		
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
	 * @return Returns the contributionTypeID.
	 */
	public Integer[] getContributionTypeID() {
		return contributionTypeID!=null?contributionTypeID:new Integer[1];
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
}
