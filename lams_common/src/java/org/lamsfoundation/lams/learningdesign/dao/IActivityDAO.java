/*
 * Created on Dec 3, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;

/**
 * @author MMINHAS
 *
 */
public interface IActivityDAO extends IBaseDAO{
	
	/**
	 * @param activityId The activityId of the activity
	 * @return Activity populated Activity object
	 */
	public Activity getActivityByActivityId(Long activityId);
	
	/**
	 * @param id The id of the activity
	 * @return Activity populated Activity object
	 */
	public List getActivityByID(Integer id);
	
	/**
	 * @param parentActivityId The activityId of the parent activity
	 * @return Activity populated Activity object
	 */
	public List getActivitiesByParentActivityId(Long parentActivityId);
	
	/**
	 * @return List of all the activities
	 */
	public List getAllActivities();
	
	/**
	 * @param learningDesignId The id of the learningDesign
	 * @return List of all the activities 
	 */	
	public List getActivitiesByLearningDesignId(Long learningDesignId);	
	
}
