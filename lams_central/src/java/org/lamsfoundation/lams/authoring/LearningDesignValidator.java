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

public class LearningDesignValidator {
	
	protected Logger log = Logger.getLogger(LearningDesignValidator.class);
	protected LearningDesignDAO learningDesignDAO = null;
	private Vector listOfValidationErrorDTOs = null;
	private FlashMessage flashMessage;
	
	public LearningDesignValidator(LearningDesignDAO learningDesignDAO)
	{
		this.learningDesignDAO = learningDesignDAO;
		listOfValidationErrorDTOs = new Vector();
	}
	
	public FlashMessage validateLearningDesign(LearningDesign learningDesign)
	{
		
		boolean valid = false;
	
		checkTransitionForActivities(learningDesign.getTransitions());
		
		Set topLevelActivities = learningDesign.getParentActivities();
		
		checkActivitiesForTransition(topLevelActivities);
		//check for input/output transition
		checkTopLevelActivitiesForInputTransition(topLevelActivities);
		checkTopLevelActivitiesForOutputTransition(topLevelActivities);
		checkIfGroupingRequired(learningDesign.getActivities());
		validateOptionalActivity(topLevelActivities); //havent tested
		validateGroupingIfGroupingIsApplied(topLevelActivities); //havent tested
		
		//if the size of the vector is > 0, then design is invalid, otherwise flag the design valid.
		if (listOfValidationErrorDTOs.size() > 0)
		{
			flashMessage = new FlashMessage("storeLearningDesign", new StoreLearningDesignResultsDTO(valid,listOfValidationErrorDTOs), FlashMessage.ERROR);
		}
		else
		{
			valid = true;
			learningDesign.setValidDesign(new Boolean(true));
			learningDesignDAO.insert(learningDesign);
			flashMessage = new FlashMessage("storeLearningDesign", new StoreLearningDesignResultsDTO(valid, learningDesign.getLearningDesignId()));			
		}
		return flashMessage;
		
	}
	
	private void checkActivitiesForTransition(Set topLevelActivities)
	{
		//if one activity, then shouldnt have any transitions
		int numOfActivities = topLevelActivities.size();
		
		Iterator i = topLevelActivities.iterator();
			
		while (i.hasNext())
		{
			Activity activity = (Activity)i.next();
			Transition inputTransition = activity.getTransitionTo();
			Transition outputTransition = activity.getTransitionFrom();
			
			if(numOfActivities > 1)
			{
				if (inputTransition == null && outputTransition == null)
				{
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.LD_ACTIVITY_TRANSITION_ERROR, activity.getActivityUIID()));
				}
			}
			else if (numOfActivities == 1)
			{
				if (inputTransition != null || outputTransition != null)
				{
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.LD_ACTIVITY_TRANSITION_ERROR, activity.getActivityUIID()));
				}
			}
		}
		
	}
	
	private void checkTransitionForActivities(Set transitions)
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
	
	private int countNumOfActivitiesWithNoInputTransition(Set topLevelActivities)
	{
		int numOfActivitiesWithNoInputTransition = 0;
		Iterator i = topLevelActivities.iterator();
		while (i.hasNext())
		{
			Activity activity = (Activity)i.next();
			Transition inputTransition = activity.getTransitionTo();
			if (inputTransition == null)
			{
				numOfActivitiesWithNoInputTransition = numOfActivitiesWithNoInputTransition + 1;
			}
		}
		
		return numOfActivitiesWithNoInputTransition;
		
	}
	
	//only one should have an input transition
	private void checkTopLevelActivitiesForInputTransition(Set topLevelActivities)
	{
		int numOfActivitiesWithNoInputTransition = countNumOfActivitiesWithNoInputTransition(topLevelActivities);
		if (numOfActivitiesWithNoInputTransition > 1)
		{
			Iterator i = topLevelActivities.iterator();
			while (i.hasNext())
			{
				Activity activity = (Activity)i.next();
				Transition inputTransition = activity.getTransitionTo();
				if (inputTransition == null)
				{					
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE1, activity.getActivityUIID()));
				}
			}
		}
		
		//if exactly one top level activity has no input transition then just return an empty list.
		if (numOfActivitiesWithNoInputTransition == 0)
			listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE2));
		
		
	}
	
	private int countNumOfActivitiesWithNoOutputTransition(Set topLevelActivities)
	{
		int numOfActivitiesWithNoOutputTransition = 0;
		Iterator i = topLevelActivities.iterator();
		while (i.hasNext())
		{
			Activity activity = (Activity)i.next();
			Transition outputTransition = activity.getTransitionFrom();
			if (outputTransition == null)
			{
				numOfActivitiesWithNoOutputTransition = numOfActivitiesWithNoOutputTransition+ 1;
			}
		}
		
		return numOfActivitiesWithNoOutputTransition;
		
	}
	
	//only one should have an output transition
	private void checkTopLevelActivitiesForOutputTransition(Set topLevelActivities)
	{
		int numOfActivitiesWithNoOutputTransition = countNumOfActivitiesWithNoOutputTransition(topLevelActivities);
		
		if (numOfActivitiesWithNoOutputTransition > 1)
		{
			Iterator i = topLevelActivities.iterator();
			while (i.hasNext())
			{
				Activity activity = (Activity)i.next();
				Transition outputTransition = activity.getTransitionFrom();
				if (outputTransition == null)
				{
					numOfActivitiesWithNoOutputTransition =+ 1;
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE1, activity.getActivityUIID()));
				}
			}
		}
		//if more than one activity is missing an input transition, return the list;
		if (numOfActivitiesWithNoOutputTransition == 0)
			listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE2));
	
			
	}
	
	private void checkIfGroupingRequired(Set activities)
	{
		Integer groupingSupportType;
		Iterator i = activities.iterator();
		while (i.hasNext())
		{
			Activity activity = (Activity)i.next();
			groupingSupportType = activity.getGroupingSupportType();
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
	}
	
	
	private void validateOptionalActivity(Set parentActivities)
	{
		Iterator parentActivityIterator = parentActivities.iterator();
		
		while (parentActivityIterator.hasNext())
		{
			Activity parentActivity = (Activity)parentActivityIterator.next();
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
	}
	
	//if grouping is selected then grouping exists
	private void validateGroupingIfGroupingIsApplied(Set parentActivities)
	{
		Iterator i = parentActivities.iterator();
		while (i.hasNext())
		{
			Activity activity = (Activity)i.next();
			if(activity.getApplyGrouping().booleanValue()) //if grouping is applied, ensure grouping exists
			{				
				if (activity.getGrouping() == null)
				{
					listOfValidationErrorDTOs.add(new ValidationErrorDTO(ValidationErrorDTO.GROUPING_SELECTED_ERROR, activity.getActivityUIID()));
				}
			}
			
		}
	}
	
	
}
