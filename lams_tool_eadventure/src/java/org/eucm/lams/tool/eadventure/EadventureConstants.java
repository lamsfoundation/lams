/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.eucm.lams.tool.eadventure;

public class EadventureConstants {

    public enum ExpresionOp {
	AND, OR, EMPTY
    }

    public enum ParamType {
	BOOLEAN, STRING, INTEGER
    }

    public static final String[] IntergerOp = { "==", "!=", "<", "<=", ">", ">=" };

    public static final String[] StringOp = { "==", "!=" };

    public static final String[] BooleanOp = { "==", "!=" };

    public static final String TOOL_SIGNATURE = "eueadv10";

    public static final String RESOURCE_SERVICE = "eadventureService";

    public static final String TOOL_CONTENT_HANDLER_NAME = "eadventureToolContentHandler";

    public static final int COMPLETED = 1;

    // eadventure type;
    public static final short RESOURCE_TYPE_URL = 1;

    public static final short RESOURCE_TYPE_FILE = 2;

    public static final short RESOURCE_TYPE_WEBSITE = 3;

    public static final short RESOURCE_TYPE_LEARNING_OBJECT = 4;

    // for action forward name
    public static final String SUCCESS = "success";

    public static final String ERROR = "error";

    public static final String DEFINE_LATER = "definelater";

    // for parameters' name

    public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";

    public static final String PARAM_TOOL_SESSION_ID = "toolSessionID";

    public static final String PARAM_FILE_VERSION_ID = "fileVersionId";

    public static final String PARAM_FILE_UUID = "fileUuid";

    public static final String PARAM_ITEM_INDEX = "itemIndex";

    public static final String PARAM_RESOURCE_ITEM_UID = "itemUid";

    public static final String PARAM_CURRENT_INSTRUCTION_INDEX = "insIdx";

    public static final String PARAM_OPEN_URL_POPUP = "popupUrl";

    public static final String PARAM_TITLE = "title";

    public static final String PARAM_CONDITION_POSITION = "position";

    public static final String PARAM_EXPRESSION_POSITION = "positionExpression";

    public static final String PARAM_VAR_NAME = "paramName";

    // for request attribute name
    public static final String ATTR_EXPRS_INFO = "exprsInfo";

    public static final String ATTR_ERROR_IN_CONDITION = "notallowDefineCondition";

    public static final String ATTR_EXPR_SELECTED_TYPE = "type";

    public static final String ATTR_GAME_DELETE = "gameDelete";

    public static final String ATTR_OPEN_SAVED_GAME = "savedGame";

    public static final String ATTR_EDIT_CONDITION = "editCondition";

    public static final String ATTR_CONDITION_LIST = "conditionsList";

    public static final String ATTR_EXPRESSION_LIST = "expressionList";

    public static final String ATTR_PARAMS_LIST = "paramsList";

    public static final String ATTR_DELETED_CONDITION_LIST = "deletedConditionsList";

    public static final String ATTR_DELETED_EXPRESSION_LIST = "deletedExpressionList";

    public static final String ATTR_DELETED_PARAMS_LIST = "deletedParamsList";

    public static final String ATTR_CHANGE_FILE = "newFile";

    public static final String ATTR_HAS_FILE = "hasFile";

    public static final String ATTR_LOCAL_URL = "localURL";

    public static final String ATTR_TOOL_CONTENT_ID = "toolContentID";

    public static final String ATTR_TOOL_SESSION_ID = "toolSessionID";

    public static final String ATTR_INSTRUCTION_LIST = "instructionList";

    public static final String ATTR_RESOURCE_ITEM_LIST = "eadventureList";

    public static final String ATTR_RESOURCE_PARAM_LIST = "eadventureParamList";

    public static final String ATTR_DELETED_RESOURCE_ITEM_LIST = "deleteEadventureList";

    public static final String ATT_LEARNING_OBJECT = "cpPackage";

    public static final String ATTR_RESOURCE_REVIEW_URL = "eadventureItemReviewUrl";

    public static final String ATTR_EADVENTURE = "eadventure";

    public static final String ATTR_RUN_AUTO = "runAuto";

    public static final String ATTR_RESOURCE_ITEM_UID = "itemUid";

    public static final String ATTR_NEXT_ACTIVITY_URL = "nextActivityUrl";

    public static final String ATTR_SUMMARY_LIST = "summaryList";

    public static final String ATTR_USER_LIST = "userList";

    public static final String ATTR_RESOURCE_INSTRUCTION = "instructions";

    public static final String ATTR_FINISH_LOCK = "finishedLock";

    public static final String ATTR_LOCK_ON_FINISH = "lockOnFinish";

    public static final String ATTR_DEFINE_COMPLETED = "defineCompleted";

    public static final String ATTR_COMPLETED = "completed";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

    public static final String ATTR_RESOURCE_FORM = "eadventureForm";

    public static final String ATTR_GAME_FORM = "eadventureGameForm";

    public static final String ATTR_ADD_RESOURCE_TYPE = "addType";

    public static final String ATTR_FILE_TYPE_FLAG = "fileTypeFlag";

    public static final String ATTR_TITLE = "title";

    public static final String ATTR_INSTRUCTIONS = "instructions";

    public static final String ATTR_USER_FINISHED = "userFinished";

    public static final String ATTR_USER_ID = "userID";

    public static final String ATTR_USER_FNAME = "userFName";

    public static final String ATTR_USER_LNAME = "userLName";

    // error message keys
    public static final String ERROR__EXPRESSION_NO_FIRST_VAR = "error.expression.not.selected.first.var";

    public static final String ERROR__EXPRESSION_NO_SECOND_VAR = "error.expression.not.selected.second.var";

    public static final String ERROR__EXPRESSION_NO_OPERATOR = "error.expression.not.selected.operator";

    public static final String ERROR_MSG_TITLE_BLANK = "error.resource.game.title.blank";

    public static final String ERROR_NAME_CONDITION_BLANK = "error.condition.name.blank";

    public static final String ERROR_NAME_VALUE_BLANK = "error.condition.expression.name.blank";

    public static final String ERROR_MSG_NAME_CONTAINS_WRONG_SYMBOL = "error.condition.contains.wrong.symbol";

    public static final String ERROR_VALUE_NOT_BOOLEAN = "error.condition.value.introduce.not.boolean";

    public static final String ERROR_SAME_VARS_NAME = "error.condition.var.same.name.vars";

    public static final String ERROR_NOT_EAD_ADD_COND = "error.condition.not.ead.added";

    public static final String ERROR_NOT_EAD_ADD = "error.eadventure.not.ead.added";

    public static final String ERROR_NOT_EQ_VARS_TYPE = "error.condition.var.not.eq.type";

    public static final String ERROR_MSG_NO_EXPRESSIONS = "error.condition.no.expressions";

    public static final String ERROR_MSG_NAME_DUPLICATED = "error.condition.name.duplicated";

    public static final String ERROR_VALUE_NOT_ALPHA = "error.condition.value.introduce.not.alpha";

    public static final String ERROR_VALUE_NOT_NUMERIC = "error.condition.value.introduce.not.numeric";

    public static final String ERROR_MSG_URL_BLANK = "error.resource.item.url.blank";

    public static final String ERROR_MSG_DESC_BLANK = "error.resource.item.desc.blank";

    public static final String ERROR_MSG_FILE_BLANK = "error.resource.item.file.blank";

    public static final String ERROR_MSG_INVALID_URL = "error.resource.item.invalid.url";

    public static final String ERROR_MSG_UPLOAD_FAILED = "error.upload.failed";

    public static final String PAGE_EDITABLE = "isPageEditable";

    public static final String MODE_AUTHOR_SESSION = "author_session";

    public static final String ATTR_REFLECTION_ON = "reflectOn";

    public static final String ATTR_REFLECTION_INSTRUCTION = "reflectInstructions";

    public static final String ATTR_REFLECTION_ENTRY = "reflectEntry";

    public static final String ATTR_REFLECT_LIST = "reflectList";

    public static final String ATTR_USER_UID = "userUid";

    public static final String DEFUALT_PROTOCOL_REFIX = "http://";

    public static final String ALLOW_PROTOCOL_REFIX = new String("[http://|https://|ftp://|nntp://]");

    public static final String EVENT_NAME_NOTIFY_TEACHERS_ON_ASSIGMENT_SUBMIT = "notify_teachers_on_assigment_submit";

    public static final String SHARED_ITEMS_DEFINITION_NAME = "shared.items.output.definition.eadventure";

    public final static String PARAMETERS_FILE_NAME = "ead-parameters.xml";

    public final static String PARAMS_TYPE_STRING = "string";
    public final static String PARAMS_TYPE_INTEGER = "integer";
    public final static String PARAMS_TYPE_BOOLEAN = "boolean";

    public final static String VAR_NAME_SCORE = "score";
    public final static String VAR_NAME_REPORT = "report";
    public final static String VAR_NAME_COMPLETED = "game-completed";
    public final static String VAR_NAME_TOTAL_TIME = "total-time";
    public final static String VAR_NAME_REAL_TIME = "real-time";

}
