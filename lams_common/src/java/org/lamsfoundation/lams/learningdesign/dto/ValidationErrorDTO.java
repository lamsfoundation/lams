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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign.dto;

import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.learningdesign.LearningDesign;


public class ValidationErrorDTO {
	
	/** Struts Message Resource related variables */
	public static final String CONFIG_PARAM = "org.lamsfoundation.lams.applicationResources";								// Code:
	public static final String OTHER_ERROR_KEY = "validation.error.other";													// O
	public static final String TRANSITION_ERROR_KEY = "validation.error.transitionNoActivityBeforeOrAfter";					// T
	public static final String ACTIVITY_TRANSITION_ERROR_KEY= "validation.error.activityWithNoTransition";					// AT
	public static final String INPUT_TRANSITION_ERROR_TYPE1_KEY = "validation.error.inputTransitionType1";					// IT
	public static final String INPUT_TRANSITION_ERROR_TYPE2_KEY = "validation.error.inputTransitionType2";  				
	public static final String OUTPUT_TRANSITION_ERROR_TYPE1_KEY = "validation.error.outputTransitionType1";				// OT
	public static final String OUTPUT_TRANSITION_ERROR_TYPE2_KEY = "validation.error.outputTransitionType2";				
	public static final String GROUPING_REQUIRED_ERROR_KEY = "validation.error.GroupingRequired";							// GR
	public static final String GROUPING_NOT_REQUIRED_ERROR_KEY = "validation.error.GroupingNotRequired";					// GNR
	public static final String GROUPING_SELECTED_ERROR_KEY = "validation.error.GroupingSelected";							// GS
	public static final String OPTIONAL_ACTIVITY_ERROR_KEY = "validation.error.OptionalActivity";							// OA
	public static final String OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_KEY = "validation.error.OptionalActivityOrderId";	// OAOI
	public static final String SCHEDULE_GATE_ERROR_TYPE1_KEY = "validation.error.illegalScheduleGateOffsetsType1";			// SG
	public static final String SCHEDULE_GATE_ERROR_TYPE2_KEY = "validation.error.illegalScheduleGateOffsetsType2";			
	public static final String GROUPING_ACTIVITY_MISSING_GROUPING_KEY = "validation.error.grouping.missing";				// GM
	public static final String GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_KEY = "validation.error.group.count.mismatch";		// GC
	public static final String BRANCHING_ACTIVITY_MUST_HAVE_DEFAULT_BRANCH = "validation.error.branching.must.have.default.branch"; 		// BB
	
	public static final String OTHER_ERROR_CODE = "O";
	public static final String TRANSITION_ERROR_CODE = "T";
	public static final String ACTIVITY_TRANSITION_ERROR_CODE = "AT";
	public static final String INPUT_TRANSITION_ERROR_CODE = "IT";
	public static final String OUTPUT_TRANSITION_ERROR_CODE = "OT1";
	public static final String GROUPING_REQUIRED_ERROR_CODE = "GR";
	public static final String GROUPING_NOT_REQUIRED_ERROR_CODE = "GNR";
	public static final String GROUPING_SELECTED_ERROR_CODE = "GS";
	public static final String OPTIONAL_ACTIVITY_ERROR_CODE = "OA";
	public static final String OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_CODE = "OAOI";
	public static final String SCHEDULE_GATE_ERROR_CODE = "SG1";
	public static final String GROUPING_ACTIVITY_MISSING_GROUPING_ERROR_CODE = "GM";
	public static final String GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_ERROR_CODE = "GC";
	public static final String BRANCHING_ACTIVITY_MUST_HAVE_DEFAULT_BRANCH_ERROR_CODE = "BB";
	
	
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

	/**
	 * Rule: For Schedule Gates (Activity) if start and end time offsets are set they should not be equal.
	 */
	public static final String SCHEDULE_GATE_ERROR_TYPE1 = resources.getMessage(SCHEDULE_GATE_ERROR_TYPE1_KEY);
	
	
	/**
	 * Rule: For Schedule Gates (Activity) if start and end time offsets are set then the start time offset should be 
	 * greater than the end time offset.
	 */
	public static final String SCHEDULE_GATE_ERROR_TYPE2 = resources.getMessage(SCHEDULE_GATE_ERROR_TYPE2_KEY);
	
	/**
	 * Rule: For Grouping Activities there must be a grouping.
	 */
	public static final String GROUPING_ACTIVITY_MISSING_GROUPING = resources.getMessage(GROUPING_ACTIVITY_MISSING_GROUPING_KEY); 	// GM

	/**
	 * Rule: For Grouping Activities the number of groups must be less than or equal to the desired number.
	 */
	public static final String GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH = resources.getMessage(GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_KEY); 	// GC

	private Integer UIID;
	private String message; //the validation message explaining what the problem is
	private String code;	// unique code representing the validation error message
	
	public ValidationErrorDTO(String code, String message, Integer UIID)
	{
		this.code = code;
		this.message =  message;
		this.UIID = UIID;
		
		
	}
	
	public ValidationErrorDTO(String code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public ValidationErrorDTO(LearningDesign learningDesign)
	{
		
	}
	
	public ValidationErrorDTO()
	{		
	}
	
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public Integer getUIID() {
		return UIID;
	}
	
	

}
