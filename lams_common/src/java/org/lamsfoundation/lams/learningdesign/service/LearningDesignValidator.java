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
package org.lamsfoundation.lams.learningdesign.service;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.util.MessageService;

/**
 * Validates a learning design. Encapsulates all the validation rules that are "cross activity" for a learning design as well
 * as calling any activity specific validation functionality. 
 * 
 * Most of the rules will never trigger as the GUI client will stop them occurring, but they are checked again here just in case. 
 * 
 * Users can save invalid design - mainly for things like missing transitions. But LAMS won't run an invalid design.
 * 
 * @author Fiona Malikoff
 */
public class LearningDesignValidator {

	MessageService messageService;
	LearningDesign learningDesign;
	Vector<ValidationErrorDTO> errors;
	
	LearningDesignValidator(LearningDesign learningDesign, MessageService messageService) {
		this.messageService = messageService;
		this.learningDesign = learningDesign;
	}
	
	/** Run the validation */
	public Vector<ValidationErrorDTO> validate() {
		errors = new Vector<ValidationErrorDTO>();		// initialises the list of validation messages.
		
		validateActivityTransitionRules(learningDesign.getParentActivities(), learningDesign.getTransitions());

		Set activities = learningDesign.getActivities();
		Iterator activityIterator = activities.iterator();
		while (activityIterator.hasNext())
		{
			Activity activity = (Activity)activityIterator.next();
			checkIfGroupingRequired(activity);
			validateGroupingIfGroupingIsApplied(activity);	
			validateOptionalActivity(activity);
			validateOptionsActivityOrderId(activity);
			validateGroupingActivity(activity);
			Vector<ValidationErrorDTO> activityErrors = activity.validateActivity(messageService);
			if(activityErrors != null && !activityErrors.isEmpty())
				errors.addAll(activityErrors);
		}

		cleanupValidationErrors();
		return errors;
	}
	/**
	 * Cleans up multiple and redundant error messages in the list.
	 * @param errors	List of errors to cleanup.
	 */
	private void cleanupValidationErrors() {
		Iterator it = errors.iterator();
		while(it.hasNext()) {
			cleanupTransitionErrors(it);
		}
	}
	
	private void cleanupTransitionErrors(Iterator topIt) {
		ValidationErrorDTO nextError;
		ValidationErrorDTO currentError = (ValidationErrorDTO) topIt.next();
		Iterator it = errors.iterator();
		
		while(it.hasNext()) {
			nextError = (ValidationErrorDTO) it.next();
		
			if(currentError.getCode().equals(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_CODE) && 
					(nextError.getCode().equals(ValidationErrorDTO.INPUT_TRANSITION_ERROR_CODE) ||
					 nextError.getCode().equals(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_CODE)))
				if(currentError.getUIID().equals(nextError.getUIID())) {
					topIt.remove();
					return;
				}
			else if(currentError.getCode().equals(ValidationErrorDTO.INPUT_TRANSITION_ERROR_CODE)) 
					if(nextError.getCode().equals(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_CODE))
						if(currentError.getUIID().equals(nextError.getUIID()))
							it.remove();
			else if(currentError.getCode().equals(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_CODE))
					if(nextError.getCode().equals(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_CODE))
						if(currentError.getUIID().equals(nextError.getUIID()))
							it.remove();
			
		}			   
			
		
	}
	
	/**
	 * Perform transition related validations.
	 * 
	 * All activities with no input transitions are added to the vector
	 * <code>noInputTransition</code>. If the size of this list is greater 
	 * than 1 (which violates the rule of having exactly one top level activity
	 * with no input transition), then a ValidationErrorDTO will be created
	 * for each activity with no input transition.
	 * Similarly, the same concept applies for activities with no output transition.
	 * 
	 * @param topLevelActivities
	 * @param transitions
	 */
	private void validateActivityTransitionRules(Set topLevelActivities, Set transitions)
	{
		validateTransitions(transitions);
		Vector<Activity> noInputTransition = new Vector<Activity>(); //a list to hold the activities which have no input transition
		Vector<Activity> noOuputTransition = new Vector<Activity>(); //a list to hold the activities which have no output transition
		int numOfTopLevelActivities = topLevelActivities.size();
		Iterator activityIterator = topLevelActivities.iterator();
		
		while (activityIterator.hasNext())
		{
			Activity activity = (Activity)activityIterator.next();
			checkActivityForTransition(activity, numOfTopLevelActivities);
			if (activity.getTransitionFrom() == null)
				noOuputTransition.add(activity);
			if (activity.getTransitionTo() == null)
				noInputTransition.add(activity);
		}
		
		if (numOfTopLevelActivities > 0)
		{
			if (noInputTransition.size() == 0)
				errors.add(new ValidationErrorDTO(ValidationErrorDTO.INPUT_TRANSITION_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE2_KEY)));
			
			if (noInputTransition.size() > 1)
			{
				//there is more than one activity with no input transitions
				Iterator noInputTransitionIterator = noInputTransition.iterator();
				while (noInputTransitionIterator.hasNext())
				{
					Activity a = (Activity)noInputTransitionIterator.next();
					errors.add(new ValidationErrorDTO(ValidationErrorDTO.INPUT_TRANSITION_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE1_KEY), a.getActivityUIID()));
				}
			}
			
			if (noOuputTransition.size() == 0)
				errors.add(new ValidationErrorDTO(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_CODE,messageService.getMessage(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE2_KEY)));
			if (noOuputTransition.size() > 1)
			{
				//there is more than one activity with no output transitions
				Iterator noOutputTransitionIterator = noOuputTransition.iterator();
				while (noOutputTransitionIterator.hasNext())
				{
					Activity a = (Activity)noOutputTransitionIterator.next();
					errors.add(new ValidationErrorDTO(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE1_KEY), a.getActivityUIID()));					
				}
			}
		}
	
	}
	
	/**
	 * This method checks if each transition in the learning design has an activity
	 * before and after the transition.
	 * 
	 * If there exists a transition which does not have an activity before or after it,
	 * the ValidationErrorDTO is added to the list of validation messages.
	 * @param transitions the set of transitions to iterate through and validate
	 */
	private void validateTransitions(Set transitions)
	{
		Iterator i = transitions.iterator();
		while (i.hasNext())
		{
			Transition transition = (Transition)i.next();
			Activity fromActivity = transition.getFromActivity();
			Activity toActivity = transition.getToActivity();
			if (fromActivity == null)
				errors.add(new ValidationErrorDTO(ValidationErrorDTO.TRANSITION_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.TRANSITION_ERROR_KEY), transition.getTransitionUIID()));
			else if (toActivity == null)
				errors.add(new ValidationErrorDTO(ValidationErrorDTO.TRANSITION_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.TRANSITION_ERROR_KEY), transition.getTransitionUIID()));
			
		}
		
	}
	
	/**
	 * For any learning design that has more than one activity then each activity should have at least an input
	 * or output transition. If there is only one activity in the learning design, then that activity should
	 * not have any transitions.
	 * This method will check if there is an activity that exists that has no transitions at all (if there is
	 * more than one activity in the learning design)
	 * @param activity The Activity to validate
	 * @param numOfActivities The number of activities in the learning design.
	 */
	private void checkActivityForTransition(Activity activity, int numOfActivities)
	{
		//if one activity, then shouldn't have any transitions
		Transition inputTransition = activity.getTransitionTo();
		Transition outputTransition = activity.getTransitionFrom();
		
		if(numOfActivities > 1)
		{
			if (inputTransition == null && outputTransition == null)
				errors.add(new ValidationErrorDTO(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_KEY), activity.getActivityUIID()));
			
		}
		if (numOfActivities == 1)
		{	
			if (inputTransition != null || outputTransition != null)				
				errors.add(new ValidationErrorDTO(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_KEY), activity.getActivityUIID()));
			
		}
	
	}
	
	/**
	 * If grouping support type is set to <code>GROUPING_SUPPORT_REQUIRED</code>, 
	 * then the activity is validated to ensure that the grouping exists.
	 * If grouping support type is set to <code>GROUPING_SUPPORT_NONE</code>
	 * then the activity is validated to ensure that the grouping does not exist.
	 * 
	 * If any validation fails, the message will be added to the list of validation
	 * messages.
	 * 
	 * @param activity
	 */
	private void checkIfGroupingRequired(Activity activity)
	{
		
			Integer groupingSupportType = activity.getGroupingSupportType();
			if (groupingSupportType.intValue() == Grouping.GROUPING_SUPPORT_REQUIRED)
			{
				//make sure activity has been assigned a grouping
				Grouping grouping = activity.getGrouping();
				if (grouping == null)
				{
					errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_REQUIRED_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.GROUPING_REQUIRED_ERROR_KEY), activity.getActivityUIID()));
				}
			}
			else if(groupingSupportType.intValue() == Grouping.GROUPING_SUPPORT_NONE)
			{
				Grouping grouping = activity.getGrouping();
				if (grouping != null)
				{
					errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_NOT_REQUIRED_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.GROUPING_NOT_REQUIRED_ERROR_KEY), activity.getActivityUIID()));
				}
			}
				
	}
	
	/**
	 * If this activity is an OptionalActivity, then it must contain one or more
	 * activities.
	 * 
	 * @param parentActivity
	 */
	private void validateOptionalActivity(Activity parentActivity)
	{
			
			if (parentActivity.isOptionsActivity())
			{
				//get the child activities and check how many there are.
				OptionsActivity optionsActivity = (OptionsActivity)parentActivity;
				Set childActivities = optionsActivity.getActivities();
				int numOfChildActivities = childActivities.size();
				if(numOfChildActivities == 0)
				{
					errors.add(new ValidationErrorDTO(ValidationErrorDTO.OPTIONAL_ACTIVITY_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.OPTIONAL_ACTIVITY_ERROR_KEY), optionsActivity.getActivityUIID()));
				}
				
				
			}
		
	}
	
	/**
	 * If this activity is an GroupingActivity and the grouping has some groups, then the number of groups must no exceed the desired number
	 * of groups..
	 * 
	 * @param parentActivity
	 */
	private void validateGroupingActivity(Activity activity)
	{
			
			if (activity.isGroupingActivity())
			{
				//get the child activities and check how many there are.
				GroupingActivity groupingActivity = (GroupingActivity)activity;
				Grouping grouping = groupingActivity.getCreateGrouping();
				if ( grouping == null ) {
					errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_ACTIVITY_MISSING_GROUPING_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.GROUPING_ACTIVITY_MISSING_GROUPING_KEY), activity.getActivityUIID()));
				}
				Integer numGroupsInteger = null;
				if ( grouping.isRandomGrouping() ) {
					RandomGrouping random = (RandomGrouping) grouping;
					numGroupsInteger = random.getNumberOfGroups();
				} else {
					numGroupsInteger = grouping.getMaxNumberOfGroups();
				}
				int maxNumGroups = numGroupsInteger == null ? 0 : numGroupsInteger.intValue(); 
				if ( grouping.getGroups() != null && grouping.getGroups().size() > maxNumGroups) {
					errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_KEY), activity.getActivityUIID()));
				}
			}
		
	}

	/**
	 * This method ensures that the order id of the optional activities
	 * start from 1, are sequential and do not contain any duplicates.
	 * It will iterate through the child activities of the OptionalActivity,
	 * and compare the current activity order id with the previous activity order id.
	 * The currentActivityId should be 1 greater than the previous activity order id.
	 * @param parentActivity
	 */
	private void validateOptionsActivityOrderId(Activity parentActivity)
	{
		Integer thisActivityOrderId = null;
		Integer previousActivityOrderId = null;
		boolean validOrderId = true;
		if(parentActivity.isOptionsActivity())
		{
			OptionsActivity optionsActivity = (OptionsActivity)parentActivity;
			Set childActivities = optionsActivity.getActivities(); //childActivities should be sorted according to order id (using the activityOrderComparator)
			Iterator i = childActivities.iterator();
			while (i.hasNext() && validOrderId)
			{
				Activity childActivity = (Activity)i.next();
				thisActivityOrderId= childActivity.getOrderId();
				if (previousActivityOrderId != null)
				{
					//compare the two numbers
					if (thisActivityOrderId==null ) {
						validOrderId = false;
					} else if ( thisActivityOrderId.longValue() != (previousActivityOrderId.longValue() + 1)) {
						validOrderId = false;
					}
					
				}
				else
				{
					//this is the first activity, since the previousActivityId is null
					if(thisActivityOrderId==null || thisActivityOrderId.longValue()!= 1)
						validOrderId = false;
				}
				previousActivityOrderId = thisActivityOrderId; 
			}
			
			if (!validOrderId)
				errors.add(new ValidationErrorDTO(ValidationErrorDTO.OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_KEY), optionsActivity.getActivityUIID()));
			
		}	
	}

	
	/**
	 * If applyGrouping is set, then the grouping must exist
	 * @param activity
	 */
	private void validateGroupingIfGroupingIsApplied(Activity activity)
	{
			if(activity.getApplyGrouping().booleanValue()) //if grouping is applied, ensure grouping exists
			{				
				if (activity.getGrouping() == null)
				{
					errors.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_SELECTED_ERROR_CODE, messageService.getMessage(ValidationErrorDTO.GROUPING_SELECTED_ERROR_KEY), activity.getActivityUIID()));
				}
			}
		
	}
}
