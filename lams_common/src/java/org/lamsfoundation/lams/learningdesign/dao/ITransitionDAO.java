/*
 * Created on Dec 6, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao;



import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Transition;

/**
 * @author MMINHAS
 */
public interface ITransitionDAO extends IBaseDAO {
	
	/**
	 * @param ID The id of the required Transition
	 * @return Returns the list of Transition objects with id = ID
	 */
	public List getTransitionById(Integer ID);
	
	/**
	 * @param transitionID The transitionID of the required Transition
	 * @return Returns the Transition object corresponding to the transitionID
	 */
	public Transition getTransitionByTransitionID(Long transitionID);
	
	/**
	 * @param toActivityID The to_activity_id of the required Transition
	 * @return Returns the list of Transition objects where to_activity_id = activityID
	 */
	public Transition getTransitionByToActivityID(Long toActivityID);
	
	/**
	 * @param fromActivityID The from_activity_id of the required Transition
	 * @return Returns the list of Transition objects where from_activity_id = activityID
	 */
	public Transition getTransitionByfromActivityID(Long fromActivityID);
	
	public List getTransitionsByLearningDesignID(Long learningDesignID);
	
	/**
	 * This method returns the next activity corresponding to the given
	 * activityID.
	 * 
	 * @param fromActivityID The activityID of the activity, whose
	 * 						 next activity has to be fetched
	 * @return Activity The activity that comes after fromActivityID 
	 */
	public Activity getNextActivity(Long fromActivityID);

}
