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

import org.lamsfoundation.lams.learningdesign.LearningDesign;

public class ValidationErrorDTO {

    /** Message Resource related variables */
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
    public static final String PASSWORD_GATE_BLANK_ERROR_KEY = "validation.error.blankPasswordGatePassword"; // SG
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
    public static final String PASSWORD_GATE_ERROR_CODE = "PG";
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
