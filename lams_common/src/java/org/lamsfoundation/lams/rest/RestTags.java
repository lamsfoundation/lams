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

package org.lamsfoundation.lams.rest;

/** Commonly used JSON tags for the calling the REST servlets */
public class RestTags {

    public static String AUTH = "auth";
    public static String LEARNING_DESIGN = "ld";

    /* Learning Design Related */
    public static String WORKSPACE_ID = "workspaceFolderID";
    public static String COPY_TYPE_ID = "copyTypeID";
    public static String TITLE = "title";
    public static String DESCRIPTION = "description";
    public static String MAX_ID = "maxID";
    public static String READ_ONLY = "readOnly";
    public static String EDIT_OVERRIDE_LOCK = "editOverrideLock";
    public static String CONTENT_FOLDER_ID = "contentFolderID";
    public static String SAVE_MODE = "saveMode";
    public static String VALID_DESIGN = "validDesign";
    public static String ACTIVITIES = "activities";
    public static String TRANSITIONS = "transitions";
    public static String GROUPINGS = "groupings";

    /* Tool related - also TITLE but that is defined above. */
    public static String INSTRUCTIONS = "instructions";
    public static String LOCK_WHEN_FINISHED = "lockWhenFinished";
    public static String REFLECT_ON_ACTIVITY = "reflectOnActivity";
    public static String REFLECT_INSTRUCTIONS = "reflectInstructions";
    public static String ALLOW_RICH_TEXT_EDITOR = "allowRichEditor";
    public static String USE_SELECT_LEADER_TOOL_OUTPUT = "useSelectLeaderToolOuput";
    public static String MINIMUM_RATES = "minimumRates";
    public static String MAXIMUM_RATES = "maximumRates";
    public static String ENABLE_CONFIDENCE_LEVELS = "enableConfidenceLevels";
    public static String CONFIDENCE_LEVELS_ACTIVITY_UIID = "confidenceLevelsActivityUiid";

    public static String QUESTIONS = "questions";
    public static String QUESTION_TEXT = "questionText";
    public static String QUESTION_TITLE = "questionTitle";
    public static String QUESTION_UUID = "questionUuid";
    public static String COLLECTION_UID = "collectionUid";
    public static String ANSWERS = "answers";
    public static String ANSWER_TEXT = "answerText";
    public static String DISPLAY_ORDER = "displayOrder";
    public static String CORRECT = "correct";
    public static String LEARNING_OUTCOMES = "learningOutcomes";
}
