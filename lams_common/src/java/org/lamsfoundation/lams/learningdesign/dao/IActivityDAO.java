/*
 * Created on Dec 3, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;

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
	 * @param uiID The internal id (flash generated) of the activity 
	 * 		  being looked for 
	 * @param design The learning_design_id of the design 
	 * 		  whose first activity we are fetching
	 * @return Activity The populated activity object matching the specified criteria 
	 */
	public Activity getActivityByUIID(Integer uiID, LearningDesign design);
	
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
