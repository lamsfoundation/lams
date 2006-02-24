package org.lamsfoundation.lams.learningdesign.service;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.lamsfoundation.lams.util.MessageService;

/**
 * The LearningDesignService class contains methods which applies validation rules
 * to determine the validity of a learning design. For the validation rules, please
 * see the AuthoringDesignDoc in lams_documents.
 * 
 * If no errors are found, a learning design is considered valid, it will set the valid_design_flag to true.
 * If validation fails, the validation messages will be returned in the response packet. The validation
 * messages are a list of ValidationErrorDTO objects.
 * 
 * @author mtruong
 *
 */
public class LearningDesignService implements ILearningDesignService{
	
	//protected Logger log = Logger.getLogger(LearningDesignService.class);
	protected MessageService messageService;
	
	/*
	 * Default constructor
	 * 
	 */
	public LearningDesignService()
	{
	}

	/**********************************************
	 * Setter/Getter Methods
	 * *******************************************/
	/**
	 * Set i18n MessageService
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * Get i18n MessageService
	 */
	public MessageService getMessageService() {
		return this.messageService;
	}
	
	/**********************************************
	 * Service Methods
	 * *******************************************/
	
	/**
	 * This method calls other validation methods which apply the validation 
	 * rules to determine whether or not the learning design is valid.
	 *
	 * @param learningDesign
	 * @return list of validation errors
	 */
	public Vector validateLearningDesign(LearningDesign learningDesign)
	{

		Vector listOfValidationErrorDTOs = new Vector();		// initialises the list of validation messages.
		
		validateActivityTransitionRules(learningDesign.getParentActivities(), learningDesign.getTransitions(), listOfValidationErrorDTOs);
		validateGeneral(learningDesign.getActivities(), listOfValidationErrorDTOs);
		
		return listOfValidationErrorDTOs;
		
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
	private void validateActivityTransitionRules(Set topLevelActivities, Set transitions, Vector listOfValidationErrorDTOs)
	{
		validateTransitions(transitions, listOfValidationErrorDTOs);
		Vector noInputTransition = new Vector(); //a list to hold the activities which have no input transition
		Vector noOuputTransition = new Vector(); //a list to hold the activities which have no output transition
		int numOfTopLevelActivities = topLevelActivities.size();
		Iterator activityIterator = topLevelActivities.iterator();
		
		while (activityIterator.hasNext())
		{
			Activity activity = (Activity)activityIterator.next();
			checkActivityForTransition(activity, numOfTopLevelActivities, listOfValidationErrorDTOs);
			if (activity.getTransitionFrom() == null)
				noOuputTransition.add(activity);
			if (activity.getTransitionTo() == null)
				noInputTransition.add(activity);
		}
		
		if (numOfTopLevelActivities > 0)
		{
			if (noInputTransition.size() == 0)
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE2_KEY)));
			
			if (noInputTransition.size() > 1)
			{
				//there is more than one activity with no input transitions
				Iterator noInputTransitionIterator = noInputTransition.iterator();
				while (noInputTransitionIterator.hasNext())
				{
					Activity a = (Activity)noInputTransitionIterator.next();
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE1_KEY), a.getActivityUIID()));
				}
			}
			
			if (noOuputTransition.size() == 0)
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE2_KEY)));
			if (noOuputTransition.size() > 1)
			{
				//there is more than one activity with no output transitions
				Iterator noOutputTransitionIterator = noOuputTransition.iterator();
				while (noOutputTransitionIterator.hasNext())
				{
					Activity a = (Activity)noOutputTransitionIterator.next();
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE1_KEY), a.getActivityUIID()));					
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
	private void validateTransitions(Set transitions, Vector listOfValidationErrorDTOs)
	{
		Iterator i = transitions.iterator();
		while (i.hasNext())
		{
			Transition transition = (Transition)i.next();
			Activity fromActivity = transition.getFromActivity();
			Activity toActivity = transition.getToActivity();
			if (fromActivity == null)
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.TRANSITION_ERROR_KEY), transition.getTransitionUIID()));
			else if (toActivity == null)
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.TRANSITION_ERROR_KEY), transition.getTransitionUIID()));
			
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
	private void checkActivityForTransition(Activity activity, int numOfActivities, Vector listOfValidationErrorDTOs)
	{
		//if one activity, then shouldnt have any transitions
		Transition inputTransition = activity.getTransitionTo();
		Transition outputTransition = activity.getTransitionFrom();
		
		if(numOfActivities > 1)
		{
			if (inputTransition == null && outputTransition == null)
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_KEY), activity.getActivityUIID()));
			
		}
		if (numOfActivities == 1)
		{	
			if (inputTransition != null || outputTransition != null)				
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_KEY), activity.getActivityUIID()));
			
		}
	
	}
	
	/**
	 * This method will call all other validation methods.
	 * 
	 * @param activities
	 */
	private void validateGeneral(Set activities, Vector listOfValidationErrorDTOs)
	{
		Iterator activityIterator = activities.iterator();
		while (activityIterator.hasNext())
		{
			Activity activity = (Activity)activityIterator.next();
			checkIfGroupingRequired(activity, listOfValidationErrorDTOs);
			validateGroupingIfGroupingIsApplied(activity, listOfValidationErrorDTOs);	
			validateOptionalActivity(activity, listOfValidationErrorDTOs);
			validateOptionsActivityOrderId(activity, listOfValidationErrorDTOs);
			Vector activityErrors = (Vector)activity.validateActivity(messageService);
			if(activityErrors != null && !activityErrors.isEmpty())
				listOfValidationErrorDTOs.addAll(activityErrors);
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
	private void checkIfGroupingRequired(Activity activity, Vector listOfValidationErrorDTOs)
	{
		
			Integer groupingSupportType = activity.getGroupingSupportType();
			if (groupingSupportType.intValue() == Grouping.GROUPING_SUPPORT_REQUIRED)
			{
				//make sure activity has been assigned a grouping
				Grouping grouping = activity.getGrouping();
				if (grouping == null)
				{
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.GROUPING_REQUIRED_ERROR_KEY), activity.getActivityUIID()));
				}
			}
			else if(groupingSupportType.intValue() == Grouping.GROUPING_SUPPORT_NONE)
			{
				Grouping grouping = activity.getGrouping();
				if (grouping != null)
				{
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.GROUPING_NOT_REQUIRED_ERROR_KEY), activity.getActivityUIID()));
				}
			}
				
	}
	
	/**
	 * If this activity is an OptionalActivity, then it must contain one or more
	 * activities.
	 * 
	 * @param parentActivity
	 */
	private void validateOptionalActivity(Activity parentActivity, Vector listOfValidationErrorDTOs)
	{
			
			if (parentActivity.isOptionsActivity())
			{
				//get the child activities and check how many there are.
				OptionsActivity optionsActivity = (OptionsActivity)parentActivity;
				Set childActivities = optionsActivity.getActivities();
				int numOfChildActivities = childActivities.size();
				if(numOfChildActivities == 0)
				{
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.OPTIONAL_ACTIVITY_ERROR_KEY), optionsActivity.getActivityUIID()));
				}
				
				
			}
		
	}
	
	/**
	 * This method ensures that the order id of the optional activities
	 * start from 1, are sequential and do not contain any duplicates.
	 * It will iterate through the child activities of the OptionalActivity,
	 * and compare the current activity id with the previous activity id.
	 * The currentActivityId should be 1 greater than the previous activity id.
	 * @param parentActivity
	 */
	private void validateOptionsActivityOrderId(Activity parentActivity, Vector listOfValidationErrorDTOs)
	{
		Long thisActivityId = null;
		Long previousActivityId = null;
		boolean validOrderId = true;
		if(parentActivity.isOptionsActivity())
		{
			OptionsActivity optionsActivity = (OptionsActivity)parentActivity;
			Set childActivities = optionsActivity.getActivities(); //childActivities should be sorted according to order id (using the activityOrderComparator)
			Iterator i = childActivities.iterator();
			while (i.hasNext())
			{
				Activity childActivity = (Activity)i.next();
				thisActivityId= childActivity.getActivityId();
				if (previousActivityId != null)
				{
					//compare the two numbers
					if (thisActivityId.longValue() != (previousActivityId.longValue() + 1))
						validOrderId = validOrderId && false;
					
				}
				else
				{
					//this is the first activity, since the previousActivityId is null
					if(thisActivityId.longValue()!= 1)
						validOrderId = validOrderId && false;
				}
				previousActivityId = thisActivityId; 
			}
			
			if (!validOrderId)
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_KEY), optionsActivity.getActivityUIID()));
			
		}	
	}

	
	/**
	 * If applyGrouping is set, then the grouping must exist
	 * @param activity
	 */
	private void validateGroupingIfGroupingIsApplied(Activity activity, Vector listOfValidationErrorDTOs)
	{
			if(activity.getApplyGrouping().booleanValue()) //if grouping is applied, ensure grouping exists
			{				
				if (activity.getGrouping() == null)
				{
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(messageService.getMessage(ValidationErrorDTO.GROUPING_SELECTED_ERROR), activity.getActivityUIID()));
				}
			}
		
	}
	

		
}
