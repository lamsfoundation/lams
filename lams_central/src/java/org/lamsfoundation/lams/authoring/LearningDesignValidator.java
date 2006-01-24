package org.lamsfoundation.lams.authoring;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.authoring.dto.StoreLearningDesignResultsDTO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.lamsfoundation.lams.util.wddx.FlashMessage;

/**
 * The LearningDesignValidator class contains methods which applies validation rules
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
public class LearningDesignValidator {
	
	//protected Logger log = Logger.getLogger(LearningDesignValidator.class);
	protected LearningDesignDAO learningDesignDAO = null;
	private Vector listOfValidationErrorDTOs = null;
	private FlashMessage flashMessage;
	
	/*
	 * Default constructor
	 * initialises the list of validation messages.
	 */
	public LearningDesignValidator(LearningDesignDAO learningDesignDAO)
	{
		this.learningDesignDAO = learningDesignDAO;
		listOfValidationErrorDTOs = new Vector();
	}

	/**
	 * This method calls other validation methods which apply the validation 
	 * rules to determine whether or not the learning design is valid.
	 *
	 * @param learningDesign
	 * @return
	 */
	public FlashMessage validateLearningDesign(LearningDesign learningDesign)
	{
		boolean valid = true;
		
		validateActivityTransitionRules(learningDesign.getParentActivities(), learningDesign.getTransitions());
		validateGeneral(learningDesign.getActivities());
		
		if (listOfValidationErrorDTOs.size() > 0)
		{
			valid = false;
			flashMessage = new FlashMessage("storeLearningDesignDetails", new StoreLearningDesignResultsDTO(valid,listOfValidationErrorDTOs, learningDesign.getLearningDesignId()), FlashMessage.ERROR);
		}
		else
		{
			learningDesign.setValidDesign(new Boolean(valid));
			learningDesignDAO.insert(learningDesign);
			flashMessage = new FlashMessage("storeLearningDesignDetails", new StoreLearningDesignResultsDTO(valid, learningDesign.getLearningDesignId()));			
		}
		return flashMessage;
		
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
		Vector noInputTransition = new Vector(); //a list to hold the activities which have no input transition
		Vector noOuputTransition = new Vector(); //a list to hold the activities which have no output transition
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
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE2));
			
			if (noInputTransition.size() > 1)
			{
				//there is more than one activity with no input transitions
				Iterator noInputTransitionIterator = noInputTransition.iterator();
				while (noInputTransitionIterator.hasNext())
				{
					Activity a = (Activity)noInputTransitionIterator.next();
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE1, a.getActivityUIID()));
				}
			}
			
			if (noOuputTransition.size() == 0)
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE2));
			if (noOuputTransition.size() > 1)
			{
				//there is more than one activity with no output transitions
				Iterator noOutputTransitionIterator = noOuputTransition.iterator();
				while (noOutputTransitionIterator.hasNext())
				{
					Activity a = (Activity)noOutputTransitionIterator.next();
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE1, a.getActivityUIID()));					
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
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.TRANSITION_ERROR, transition.getTransitionUIID()));
			else if (toActivity == null)
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.TRANSITION_ERROR, transition.getTransitionUIID()));
			
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
		//if one activity, then shouldnt have any transitions
		Transition inputTransition = activity.getTransitionTo();
		Transition outputTransition = activity.getTransitionFrom();
		
		if(numOfActivities > 1)
		{
			if (inputTransition == null && outputTransition == null)
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR, activity.getActivityUIID()));
			
		}
		if (numOfActivities == 1)
		{	
			if (inputTransition != null || outputTransition != null)				
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR, activity.getActivityUIID()));
			
		}
	
	}
	
	/**
	 * This method will call all other validation methods.
	 * 
	 * @param activities
	 */
	private void validateGeneral(Set activities)
	{
		Iterator activityIterator = activities.iterator();
		while (activityIterator.hasNext())
		{
			Activity activity = (Activity)activityIterator.next();
			checkIfGroupingRequired(activity);
			validateGroupingIfGroupingIsApplied(activity);	
			validateOptionalActivity(activity);
			validateOptionsActivityOrderId(activity);			
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
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_REQUIRED_ERROR, activity.getActivityUIID()));
				}
			}
			else if(groupingSupportType.intValue() == Grouping.GROUPING_SUPPORT_NONE)
			{
				Grouping grouping = activity.getGrouping();
				if (grouping != null)
				{
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_NOT_REQUIRED_ERROR, activity.getActivityUIID()));
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
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.OPTIONAL_ACTIVITY_ERROR, optionsActivity.getActivityUIID()));
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
	private void validateOptionsActivityOrderId(Activity parentActivity)
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
				listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR, optionsActivity.getActivityUIID()));
			
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
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_SELECTED_ERROR, activity.getActivityUIID()));
				}
			}
		
	}
	

		
}
