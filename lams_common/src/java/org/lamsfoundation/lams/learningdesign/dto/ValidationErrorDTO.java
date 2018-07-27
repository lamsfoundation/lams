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

package org.lamsfoundation.lams.learningdesign.dto;

import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.learningdesign.LearningDesign;

public class ValidationErrorDTO {

    /** Struts Message Resource related variables */
    public static final String CONFIG_PARAM = "org.lamsfoundation.lams.applicationResources"; // Code:
    public static final String OTHER_ERROR_KEY = "validation.error.other"; // O
    public static final String TRANSITION_ERROR_KEY = "validation.error.transitionNoActivityBeforeOrAfter"; // T
    public static final String ACTIVITY_TRANSITION_ERROR_KEY = "validation.error.activityWithNoTransition"; // AT
    public static final String INPUT_TRANSITION_ERROR_TYPE1_KEY = "validation.error.inputTransitionType1"; // IT
    public static final String INPUT_TRANSITION_ERROR_TYPE2_KEY = "validation.error.inputTransitionType2";
    public static final String OUTPUT_TRANSITION_ERROR_TYPE1_KEY = "validation.error.outputTransitionType1"; // OT
    public static final String OUTPUT_TRANSITION_ERROR_TYPE2_KEY = "validation.error.outputTransitionType2";
    public static final String GROUPING_REQUIRED_ERROR_KEY = "validation.error.GroupingRequired"; // GR
    public static final String GROUPING_NOT_REQUIRED_ERROR_KEY = "validation.error.GroupingNotRequired"; // GNR
    public static final String GROUPING_SELECTED_ERROR_KEY = "validation.error.GroupingSelected"; // GS
    public static final String GROUPING_NOT_USED_ERROR_KEY = "validation.error.GroupingNotUsed"; // GNU
    public static final String OPTIONAL_ACTIVITY_ERROR_KEY = "validation.error.OptionalActivity"; // OA
    public static final String OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_KEY = "validation.error.OptionalActivityOrderId"; // OAOI
    public static final String FLOATING_ACTIVITY_ERROR_KEY = "validation.error.FloatingActivity"; // FLA
    public static final String FLOATING_ACTIVITY_MAX_ERROR_KEY = "validation.error.FloatingActivity.maximum"; // FLA
    public static final String SCHEDULE_GATE_ERROR_TYPE1_KEY = "validation.error.illegalScheduleGateOffsetsType1"; // SG
    public static final String SCHEDULE_GATE_ERROR_TYPE2_KEY = "validation.error.illegalScheduleGateOffsetsType2";
    public static final String CONDITION_GATE_ACTVITY_CONDITION = "validation.error.conditionGateMustHaveACondition"; // CGC
    public static final String CONDITION_GATE_ACTVITY_TOOLINPUT = "validation.error.conditionGateMustHaveAnInputToolActivity"; // CGI
    public static final String GROUPING_ACTIVITY_MISSING_GROUPING_KEY = "validation.error.grouping.missing"; // GM
    public static final String GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_KEY = "validation.error.group.count.mismatch"; // GC
    public static final String FIRST_ACTIVITY_ERROR_KEY = "validation.error.first.activity"; // FIA
    public static final String TITLE_CHARACTERS_ERROR_KEY = "validation.error.title.characters";

    public static final String BRANCHING_ACTIVITY_MUST_HAVE_A_BRANCH = "validation.error.branching.must.have.a.branch"; // BB
    public static final String BRANCHING_ACTIVITY_MUST_HAVE_DEFAULT_BRANCH = "validation.error.toolBranchingMustHaveDefaultBranch"; // BDB
    public static final String SEQUENCE_ACTIVITY_MUST_HAVE_FIRST_ACTIVITY = "validation.error.sequenceActivityMustHaveFirstActivity"; // SFA
    public static final String BRANCHING_ACTVITY_GROUPING = "validation.error.groupedBranchingMustHaveAGrouping"; //BGG
    public static final String BRANCHING_ACTVITY_MUST_HAVE_ALL_GROUPS_ALLOCATED = "validation.error.groupedBranchingMustHaveBranchForGroup"; //BGM
    public static final String BRANCH_CONDITION_INVALID = "validation.error.toolBranchingConditionInvalid"; // BCOND
    public static final String BRANCHING_ACTVITY_TOOLINPUT = "validation.error.toolBranchingMustHaveAnInputToolActivity"; // BTI
    public static final String BRANCHING_ACTVITY_TOOLCONDITION = "validation.error.toolBranchingMustHaveACondition"; // BTC

    public static final String OTHER_ERROR_CODE = "O";
    public static final String TRANSITION_ERROR_CODE = "T";
    public static final String ACTIVITY_TRANSITION_ERROR_CODE = "AT";
    public static final String INPUT_TRANSITION_ERROR_CODE = "IT";
    public static final String OUTPUT_TRANSITION_ERROR_CODE = "OT1";
    public static final String GROUPING_REQUIRED_ERROR_CODE = "GR";
    public static final String GROUPING_NOT_REQUIRED_ERROR_CODE = "GNR";
    public static final String GROUPING_SELECTED_ERROR_CODE = "GS";
    public static final String GROUPING_NOT_USED_ERROR_CODE = "GNU";
    public static final String OPTIONAL_ACTIVITY_ERROR_CODE = "OA";
    public static final String OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_CODE = "OAOI";
    public static final String FLOATING_ACTIVITY_ERROR_CODE = "FLA";
    public static final String FLOATING_ACTIVITY_MAX_ERROR_CODE = "FAMX";
    public static final String SCHEDULE_GATE_ERROR_CODE = "SG1";
    public static final String CONDITION_GATE_ACTVITY_CONDITION_ERROR_CODE = "CGC";
    public static final String CONDITION_GATE_ACTVITY_TOOLINPUT_ERROR_CODE = "CGI";
    public static final String GROUPING_ACTIVITY_MISSING_GROUPING_ERROR_CODE = "GM";
    public static final String GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_ERROR_CODE = "GC";
    public static final String BRANCHING_ACTIVITY_MUST_HAVE_A_BRANCH_ERROR_CODE = "BB";
    public static final String BRANCHING_ACTIVITY_MUST_HAVE_DEFAULT_BRANCH_ERROR_CODE = "BDB";
    public static final String SEQUENCE_ACTIVITY_MUST_HAVE_FIRST_ACTIVITY_ERROR_CODE = "SFA";
    public static final String BRANCHING_ACTVITY_GROUPING_ERROR_CODE = "BGG";
    public static final String BRANCHING_ACTVITY_MUST_HAVE_ALL_GROUPS_ALLOCATED_ERROR_CODE = "BGM";
    public static final String BRANCH_CONDITION_INVALID_ERROR_CODE = "BCOND";
    public static final String BRANCHING_ACTVITY_TOOLINPUT_ERROR_CODE = "BTI";
    public static final String BRANCHING_ACTVITY_TOOLCONDITION_ERROR_CODE = "BTC";
    public static final String FIRST_ACTIVITY_ERROR_CODE = "FIA";
    public static final String TITLE_CHARACTERS_ERROR_CODE = "TCH";

    private static MessageResources resources = MessageResources.getMessageResources(ValidationErrorDTO.CONFIG_PARAM);
    /**
     * Rule: Other (Covers any cases that are not covered by another code)
     */
    public static final String OTHER_ERROR = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.OTHER_ERROR_KEY);

    /**
     * Rule: Each transition must have an activity before and after the transition
     */
    public static final String TRANSITION_ERROR = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.TRANSITION_ERROR_KEY);

    /**
     * Rule: Exactly one top level activity will have no input transition
     * If more than one activity is missing an input transition
     */
    public static final String INPUT_TRANSITION_ERROR_TYPE1 = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE1_KEY);

    /**
     * Rule: Exactly one top level activity will have no input transition
     * If no activities are missing their input transitions
     */
    public static final String INPUT_TRANSITION_ERROR_TYPE2 = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.INPUT_TRANSITION_ERROR_TYPE2_KEY);

    /**
     * Rule: Exactly one top level activity will have no output transition
     * If more than one activity is missing the output transition.
     */
    public static final String OUTPUT_TRANSITION_ERROR_TYPE1 = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE1_KEY);

    /**
     * Rule: Exactly one top level activity will have no output transition
     * If no activities are missing their output transitions.
     */
    public static final String OUTPUT_TRANSITION_ERROR_TYPE2 = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.OUTPUT_TRANSITION_ERROR_TYPE2_KEY);

    /**
     * Rule: If grouping is required then a grouping must have been applied to the activity
     */
    public static final String GROUPING_REQUIRED_ERROR = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.GROUPING_REQUIRED_ERROR_KEY);

    /**
     * Rule: If grouping is not supported/required, then a grouping has not been applied to the activity.
     */
    public static final String GROUPING_NOT_REQUIRED_ERROR = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.GROUPING_NOT_REQUIRED_ERROR_KEY);

    /**
     * Rule: If grouping is selected, then grouping exists
     */
    public static final String GROUPING_SELECTED_ERROR = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.GROUPING_SELECTED_ERROR_KEY);

    /**
     * Rule: An optional activity must contain one or more activities
     */
    public static final String OPTIONAL_ACTIVITY_ERROR = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.OPTIONAL_ACTIVITY_ERROR_KEY);

    /**
     * Rule: An optional activity must have valid order ids - the order ids should be
     * sequential starting at 1 and must have no duplicates.
     */
    public static final String OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.OPTIONAL_ACTIVITY_ORDER_ID_INVALID_ERROR_KEY);

    /**
     * Rule: For any learning design that has more than one activity then each activity should
     * have at least one input or one output transition.
     */
    public static final String ACTIVITY_TRANSITION_ERROR = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.ACTIVITY_TRANSITION_ERROR_KEY);

    /**
     * Rule: For Schedule Gates (Activity) if start and end time offsets are set they should not be equal.
     */
    public static final String SCHEDULE_GATE_ERROR_TYPE1 = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.SCHEDULE_GATE_ERROR_TYPE1_KEY);
    /**
     * Rule: A condition gate must have at least condition defined.
     */
    public static final String CONDITION_GATE_CONDITION_ERROR = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.CONDITION_GATE_ACTVITY_CONDITION_ERROR_CODE);
    /**
     * Rule: A condition gate must have an input tool.
     */
    public static final String CONDITION_GATE_TOOLINPUT_ERROR = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.CONDITION_GATE_ACTVITY_TOOLINPUT_ERROR_CODE);

    /**
     * Rule: For Schedule Gates (Activity) if start and end time offsets are set then the start time offset should be
     * greater than the end time offset.
     */
    public static final String SCHEDULE_GATE_ERROR_TYPE2 = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.SCHEDULE_GATE_ERROR_TYPE2_KEY);

    /**
     * Rule: For Grouping Activities there must be a grouping.
     */
    public static final String GROUPING_ACTIVITY_MISSING_GROUPING = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.GROUPING_ACTIVITY_MISSING_GROUPING_KEY); // GM

    /**
     * Rule: For Grouping Activities the number of groups must be less than or equal to the desired number.
     */
    public static final String GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH = ValidationErrorDTO.resources
	    .getMessage(ValidationErrorDTO.GROUPING_ACTIVITY_GROUP_COUNT_MISMATCH_KEY); // GC

    private Integer UIID;
    private String message; //the validation message explaining what the problem is
    private String code; // unique code representing the validation error message

    public ValidationErrorDTO(String code, String message, Integer UIID) {
	this.code = code;
	this.message = message;
	this.UIID = UIID;

    }

    public ValidationErrorDTO(String code, String message) {
	this.code = code;
	this.message = message;
    }

    public ValidationErrorDTO(LearningDesign learningDesign) {

    }

    public ValidationErrorDTO() {
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
