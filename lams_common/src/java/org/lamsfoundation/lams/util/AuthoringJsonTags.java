/*******************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * ============================================================= License
 * Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2.0 as published by the
 * Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

package org.lamsfoundation.lams.util;

/**
 * @author Manpreet Minhas The tags used in SVG Authoring.
 */
public interface AuthoringJsonTags {
    /* General Tags */
    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";
    public static final String HELP_TEXT = "helpText";
    public static final String XCOORD = "xCoord";
    public static final String YCOORD = "yCoord";
    public static final String GROUPINGS = "groupings";
    public static final String TRANSITIONS = "transitions";
    public static final String ACTIVITIES = "activities";
    public static final String BRANCH_MAPPINGS = "branchMappings";
    public static final String ANNOTATIONS = "annotations";

    /* Learning Library specific tags */
    public static final String LEARNING_LIBRARY_ID = "learningLibraryID";
    public static final String LIB_ACTIVITIES = "templateActivities";
    public static final String LIB_PACKAGE = "libraries";
    public static final String DESIGN_PACKAGE = "designs";

    /* Activity specific tags */
    public static final String ACTIVITY_ID = "activityID";
    public static final String ACTIVITY_UIID = "activityUIID";
    public static final String ACTIVITY_TITLE = "activityTitle";

    public static final String PARENT_ACTIVITY_ID = "parentActivityID";
    public static final String PARENT_UIID = "parentUIID";

    public static final String ACTIVITY_TYPE_ID = "activityTypeID";
    public static final String ORDER_ID = "orderID";

    public static final String ACTIVITY_CATEGORY_ID = "activityCategoryID";

    public static final String LIBRARY_IMAGE = "libraryActivityUIImage";
    public static final String LIBRARY_ACTIVITY = "libraryActivityID";

    public static final String APPLY_GROUPING = "applyGrouping";
    public static final String GROUPING_SUPPORT_TYPE = "groupingSupportType";
    public static final String STOP_AFTER_ACTIVITY = "stopAfterActivity";
    public static final String INPUT_ACTIVITIES = "inputActivities"; // not
    // used
    // yet
    public static final String INPUT_TOOL_ACTIVITY_UIID = "toolActivityUIID";

    /** FloatingActivity specific tags */
    public static final String MAX_ACTIVITIES = "maxActivities";

    /** OptionsActivity specific tags */
    public static final String MAX_OPTIONS = "maxOptions";
    public static final String MIN_OPTIONS = "minOptions";
    public static final String OPTIONS_INSTRUCTIONS = "optionsInstructions";

    /** ToolActivity specific tags */
    public static final String TOOL_ID = "toolID";
    public static final String TOOL_CONTENT_ID = "toolContentID";
    public static final String TOOL_CONTENT_IDS = "toolContentIDs";

    /** GateActivity specific tags */
    public static final String GATE_ACTIVITY_LEVEL_ID = "gateActivityLevelID";
    public static final String GATE_START_DATE = "gateStartDateTime";
    public static final String GATE_END_DATE = "gateEndDateTime";
    public static final String GATE_START_OFFSET = "gateStartTimeOffset";
    public static final String GATE_END_OFFSET = "gateEndTimeOffset";
    public static final String GATE_ACTIVITY_COMPLETION_BASED = "gateActivityCompletionBased";
    public static final String GATE_PASSWORD = "gatePassword";

    /** Grouping Activity specific tags */
    public static final String CREATE_GROUPING_ID = "createGroupingID";
    public static final String CREATE_GROUPING_UIID = "createGroupingUIID";

    /** Grouping specific tags */
    public static final String GROUPING_ID = "groupingID";
    public static final String GROUPING_UIID = "groupingUIID";
    public static final String GROUPING_TYPE_ID = "groupingTypeID";
    public static final String LEARNERS_PER_GROUP = "learnersPerGroup";
    public static final String MAX_NUMBER_OF_GROUPS = "maxNumberOfGroups";
    public static final String NUMBER_OF_GROUPS = "numberOfGroups";
    public static final String STAFF_GROUP_ID = "staffGroupID";
    public static final String GROUPING_DTO = "groupingDTO";
    public static final String GROUPS = "groups";
    public static final String EQUAL_NUMBER_OF_LEARNERS_PER_GROUP = "equalNumberOfLearnersPerGroup";
    public static final String VIEW_STUDENTS_BEFORE_SELECTION = "viewStudentsBeforeSelection";

    public static final String GROUP_ID = "groupID";
    public static final String GROUP_NAME = "groupName";
    public static final String GROUP_USER_LIST = "userList";
    public static final String GROUP_UIID = "groupUIID";

    /** Transition specific tags */
    public static final String TRANSITION_ID = "transitionID";
    public static final String TRANSITION_UIID = "transitionUIID";
    public static final String TRANSITION_TO = "to_activity_id";
    public static final String TRANSITION_FROM = "from_activity_id";
    public static final String TO_ACTIVITY_UIID = "toUIID";
    public static final String FROM_ACTIVITY_UIID = "fromUIID";
    public static final String TRANSITION_TYPE = "transitionType";

    /** Tool Specific tags */
    public static final String TOOL_DISPLAY_NAME = "displayName";
    public static final String TOOl_AUTH_URL = "toolAuthoringURL";
    public static final String AUTH_URL = "authoringURLS";

    /** LearningDesign specific tags */
    public static final String LEARNING_DESIGN_ID = "learningDesignID";
    public static final String LEARNING_DESIGN_UIID = "learningDesignUIID";
    public static final String FIRST_ACTIVITY_ID = "firstActivityID";
    public static final String FIRST_ACTIVITY_UIID = "firstActivityUIID";
    public static final String FLOATING_ACTIVITY_ID = "floatingActivityID";
    public static final String FLOATING_ACTIVITY_UIID = "floatingActivityUIID";
    public static final String ANNOTATION_UIID = "annotationUIID";

    public static final String MAX_ID = "maxID";
    public static final String VALID_DESIGN = "validDesign";
    public static final String READ_ONLY = "readOnly";
    public static final String EDIT_OVERRIDE_LOCK = "editOverrideLock";
    public static final String DATE_READ_ONLY = "dateReadOnly";
    public static final String USER_ID = "userID";
    public static final String ORIGINAL_USER_ID = "originalUserID";
    public static final String EDIT_OVERRIDE_USER_ID = "editOverrideUserID";

    public static final String COPY_TYPE = "copyTypeID";
    public static final String CREATION_DATE = "createDateTime";
    public static final String LAST_MODIFIED_DATE = "lastModifiedDateTime";
    public static final String VERSION = "version";
    public static final String ORIGINAL_DESIGN_ID = "originalLearningDesignID";
    public static final String WORKSPACE_FOLDER_ID = "workspaceFolderID";
    public static final String DURATION = "duration";
    public static final String LICENCE_ID = "licenseID";
    public static final String LICENSE_TEXT = "licenseText";
    public static final String CONTENT_FOLDER_ID = "contentFolderID";
    public static final String SAVE_MODE = "saveMode";
    public static final String DESIGN_TYPE = "designType";

    /** ComplexActivity specific tags */
    public static final String CHILD_ACTIVITIES = "childActivities";
    public static final String DEFAULT_ACTIVITY_UIID = "defaultActivityUIID";

    /** Notebook Specific Tags */
    public static final String EXTERNAL_ID = "externalID";
    public static final String EXTERNAL_ID_TYPE = "externalIDType";
    public static final String EXTERNAL_SIG = "externalSignature";
    public static final String ENTRY = "entry";

    /** Annotation Specific Tags */
    public static final String START_XCOORD = "startXCoord";
    public static final String START_YCOORD = "startYCoord";
    public static final String END_XCOORD = "endXCoord";
    public static final String END_YCOORD = "endYCoord";
    public static final String COLOR = "color";
    public static final String SIZE = "size";

    /** Branch Mapping and Tool Condition Tags */
    public static final String BRANCH_ACTIVITY_ENTRY_ID = "entryID";
    public static final String BRANCH_ACTIVITY_ENTRY_UIID = "entryUIID";
    public static final String BRANCH_SEQUENCE_ACTIVITY_UIID = "sequenceActivityUIID";
    public static final String BRANCH_ACTIVITY_UIID = "branchingActivityUIID";
    public static final String BRANCH_CONDITION = "condition";
    public static final String BRANCH_GATE_OPENS_WHEN_CONDITION_MET = "gateOpenWhenConditionMet";
    public static final String BRANCH_GATE_ACTIVITY_UIID = "gateActivityUIID";
    // reuse GROUP_UIID for the Group field
    public static final String CONDITION_ID = "conditionID";
    public static final String CONDITION_UIID = "conditionUIID";
    // reuse ORDER_ID for the OrderId field
    public static final String CONDITION_NAME = "name";
    public static final String CONDITION_DISPLAY_NAME = "displayName";
    public static final String CONDITION_TYPE = "type";
    public static final String CONDITION_START_VALUE = "startValue";
    public static final String CONDITION_END_VALUE = "endValue";
    public static final String CONDITION_EXACT_MATCH_VALUE = "exactMatchValue";

    /** LD Import specific tags */
    public static final String LEARNING_DESIGN_TO_IMPORT_ID = "learningDesignIDToImport";
    public static final String CREATE_NEW_LEARNING_DESIGN = "createNewLearningDesign";

    /** Tool adapters specific tags */
    public static final String CUSTOM_CSV = "customCSV";

    /** Evaluation tool output tag */
    public static final String TOOL_OUTPUT_DEFINITION = "gradebookToolOutputDefinitionName";
    public static final String TOOL_OUTPUT_WEIGHT = "gradebookToolOutputWeight";
}
