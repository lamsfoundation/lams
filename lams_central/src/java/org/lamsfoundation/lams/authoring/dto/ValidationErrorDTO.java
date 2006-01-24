package org.lamsfoundation.lams.authoring.dto;

import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.learningdesign.LearningDesign;


public class ValidationErrorDTO {
	
	/** Struts Message Resource related variables */
	private static final String CONFIG_PARAM = "org.lamsfoundation.lams.applicationResources";
	private static final String OTHER_ERROR_KEY = "flash.validation.error.other";
	private static final String TRANSITION_ERROR_KEY = "flash.validation.error.transitionNoActivityBeforeOrAfter";
	private static final String ACTIVITY_TRANSITION_ERROR_KEY= "flash.validation.error.activityWithNoTransition";
	private static final String INPUT_TRANSITION_ERROR_TYPE1_KEY = "flash.validation.error.inputTransitionType1";
	private static final String INPUT_TRANSITION_ERROR_TYPE2_KEY = "flash.validation.error.inputTransitionType2";
	private static final String OUTPUT_TRANSITION_ERROR_TYPE1_KEY = "flash.validation.error.outputTransitionType1";
	private static final String OUTPUT_TRANSITION_ERROR_TYPE2_KEY = "flash.validation.error.outputTransitionType2";
	private static final String GROUPING_REQUIRED_ERROR_KEY = "flash.validation.error.GroupingRequired";
	private static final String GROUPING_NOT_REQUIRED_ERROR_KEY = "flash.validation.error.GroupingNotRequired";
	private static final String GROUPING_SELECTED_ERROR_KEY = "flash.validation.error.GroupingSelected";
	private static final String OPTIONAL_ACTIVITY_ERROR_KEY = "flash.validation.error.OptionalActivity";
	private static final String OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_KEY = "flash.validation.error.OptionalActivityOrderId";
		
	private static MessageResources resources = MessageResources.getMessageResources(CONFIG_PARAM);
	/**
	 * Rule: Other (Covers any cases that are not covered by another code)
	 */
	public static final String OTHER_ERROR = resources.getMessage(OTHER_ERROR_KEY);
	
	/**
	 * Rule: Each transition must have an activity before and after the transition
	 */
	public static final String TRANSITION_ERROR = resources.getMessage(TRANSITION_ERROR_KEY);
	
	/**
	 * Rule: Exactly one top level activity will have no input transition
	 * If more than one activity is missing an input transition
	 */
	public static final String INPUT_TRANSITION_ERROR_TYPE1 = resources.getMessage(INPUT_TRANSITION_ERROR_TYPE1_KEY);
	
	/**
	 * Rule: Exactly one top level activity will have no input transition
	 * If no activities are missing their input transitions
	 */
	public static final String INPUT_TRANSITION_ERROR_TYPE2= resources.getMessage(INPUT_TRANSITION_ERROR_TYPE2_KEY);
	
	/**
	 * Rule: Exactly one top level activity will have no output transition
	 * If more than one activity is missing the output transition.
	 */
	public static final String OUTPUT_TRANSITION_ERROR_TYPE1 = resources.getMessage(OUTPUT_TRANSITION_ERROR_TYPE1_KEY);
	
	/**
	 * Rule: Exactly one top level activity will have no output transition
	 * If no activities are missing their output transitions.
	 */
	public static final String OUTPUT_TRANSITION_ERROR_TYPE2 = resources.getMessage(OUTPUT_TRANSITION_ERROR_TYPE2_KEY);
	
	/**
	 * Rule: If grouping is required then a grouping must have been applied to the activity
	 */
	public static final String GROUPING_REQUIRED_ERROR = resources.getMessage(GROUPING_REQUIRED_ERROR_KEY);
	
	/**
	 * Rule: If grouping is not supported/required, then a grouping has not been applied to the activity.
	 */
	public static final String GROUPING_NOT_REQUIRED_ERROR = resources.getMessage(GROUPING_NOT_REQUIRED_ERROR_KEY);
	
	/**
	 * Rule: If grouping is selected, then grouping exists
	 */
	public static final String GROUPING_SELECTED_ERROR = resources.getMessage(GROUPING_SELECTED_ERROR_KEY);
	
	/**
	 * Rule: An optional activity must contain one or more activities
	 */
	public static final String OPTIONAL_ACTIVITY_ERROR = resources.getMessage(OPTIONAL_ACTIVITY_ERROR_KEY);
	
	/**
	 * Rule: An optional activity must have valid order ids - the order ids should be 
	 * sequential starting at 1 and must have no duplicates.
	 */
	public static final String OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR = resources.getMessage(OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_KEY);
	
	/**
	 * Rule: For any learning design that has more than one activity then each activity should 
	 * have at least one input or one output transition. 
	 */
	public static final String ACTIVITY_TRANSITION_ERROR = resources.getMessage(ACTIVITY_TRANSITION_ERROR_KEY);

	private Integer UIID;
	private String message; //the validation message explaining what the problem is
	
	
	public ValidationErrorDTO(String message, Integer UIID)
	{
		this.message = message;
		this.UIID = UIID;
	}
	
	public ValidationErrorDTO(String message)
	{
		this.message = message;
	}

	public ValidationErrorDTO(LearningDesign learningDesign)
	{
		
	}
	
	public ValidationErrorDTO()
	{		
	}
	
	
	public String getMessage() {
		return message;
	}
	public Integer getUIID() {
		return UIID;
	}
	
	

}
