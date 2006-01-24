package org.lamsfoundation.lams.authoring.dto;

import org.lamsfoundation.lams.learningdesign.LearningDesign;

public class ValidationErrorDTO {
	
	/**
	 * Rule: Other (Covers any cases that are not covered by another code)
	 */
	public static final Integer OTHER_ERROR = new Integer(0);
	
	/**
	 * Rule: Each transition must have an activity before and after the transition
	 */
	public static final Integer TRANSITION_ERROR = new Integer(1);
	
	/**
	 * Rule: Exactly one top level activity will have no input transition
	 * If more than one activity is missing an input transition
	 */
	public static final Integer INPUT_TRANSITION_ERROR_TYPE1 = new Integer(2);
	
	/**
	 * Rule: Exactly one top level activity will have no input transition
	 * If no activities are missing their input transitions
	 */
	public static final Integer INPUT_TRANSITION_ERROR_TYPE2= new Integer(3);
	
	/**
	 * Rule: Exactly one top level activity will have no output transition
	 * If more than one activity is missing the output transition.
	 */
	public static final Integer OUTPUT_TRANSITION_ERROR_TYPE1 = new Integer(4);
	
	/**
	 * Rule: Exactly one top level activity will have no output transition
	 * If no activities are missing their output transitions.
	 */
	public static final Integer OUTPUT_TRANSITION_ERROR_TYPE2 = new Integer(5);
	
	/**
	 * Rule: If grouping is required then a grouping must have been applied to the activity
	 */
	public static final Integer GROUPING_REQUIRED_ERROR = new Integer(6);
	
	/**
	 * Rule: If grouping is not supported/required, then a grouping has not been applied to the activity.
	 */
	public static final Integer GROUPING_NOT_REQUIRED_ERROR = new Integer(7);
	
	/**
	 * Rule: If grouping is selected, then grouping exists
	 */
	public static final Integer GROUPING_SELECTED_ERROR = new Integer(8);
	
	/**
	 * Rule: An optional activity must contain one or more activities
	 */
	public static final Integer OPTIONAL_ACTIVITY_ERROR = new Integer(9);
	
	/**
	 * Rule: An optional activity must have valid order ids - the order ids should be 
	 * sequential starting at 1 and must have no duplicates.
	 */
	public static final Integer OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR = new Integer(10);
	
	/**
	 * Rule: For any learning design that has more than one activity then each activity should 
	 * have at least one input or one output transition. 
	 */
	public static final Integer LD_ACTIVITY_TRANSITION_ERROR = new Integer(11);
	
	private Integer code;
	private Integer UIID;
	private String message;
	
	public ValidationErrorDTO(Integer code, Integer UIID, String message)
	{
		this.code = code;
		this.UIID = UIID;
		this.message = message;
	}
	
	public ValidationErrorDTO(Integer code, Integer UIID)
	{
		this.code = code;
		this.UIID = UIID;
	}
	
	public ValidationErrorDTO(Integer code)
	{
		this.code = code;
	}
	
	public ValidationErrorDTO(LearningDesign learningDesign)
	{
		
	}
	
	public ValidationErrorDTO()
	{		
	}
	
	public Integer getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public Integer getUIID() {
		return UIID;
	}
	
	

}
