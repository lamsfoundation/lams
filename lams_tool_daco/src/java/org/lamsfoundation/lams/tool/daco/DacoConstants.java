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
/* $Id$ */
package org.lamsfoundation.lams.tool.daco;

public class DacoConstants {
	public static final String TOOL_SIGNATURE = "ladaco10";
	public static final String DACO_SERVICE = "dacoService";
	public static final String TOOL_CONTENT_HANDLER_NAME = "dacoToolContentHandler";

	public static final int COMPLETED = 1;

	// daco type;
	public static final String QUESTION_TYPE = "questionType";
	public static final short QUESTION_TYPE_TEXTFIELD = 1;
	public static final short QUESTION_TYPE_TEXTAREA = 2;
	public static final short QUESTION_TYPE_NUMBER = 3;
	public static final short QUESTION_TYPE_DATE = 4;
	public static final short QUESTION_TYPE_FILE = 5;
	public static final short QUESTION_TYPE_IMAGE = 6;
	public static final short QUESTION_TYPE_RADIO = 7;
	public static final short QUESTION_TYPE_DROPDOWN = 8;
	public static final short QUESTION_TYPE_CHECKBOX = 9;
	public static final short QUESTION_TYPE_LONGLAT = 10;

	// longitude/latitude maps
	public static final String[] LONGLAT_MAPS_LIST = new String[] { "Google Maps", "Google Earth", "Geabios", "Open Street Map",
			"Multimap" };

	// answer options
	public static final int INIT_ANSWER_OPTION_COUNT = 3;
	public static final int ANSWER_OPTION_MINIMUM_COUNT = 2;
	public static final String ANSWER_OPTION_DESC_PREFIX = "answerOptionItemDesc";
	public static final String ANSWER_OPTION_COUNT = "answerOptionCount";
	public static final String ANSWER_OPTION_SELECT = "label.authoring.basic.answeroption.select";

	// image extensions
	public static final String[] IMAGE_EXTENSIONS = new String[] { "GIF", "JPG", "PNG" };

	// format for the date question
	public static final String DATE_PART_DELIMETER = "-";
	public static final String DATE_FORMAT = "dd" + DacoConstants.DATE_PART_DELIMETER + "MM" + DacoConstants.DATE_PART_DELIMETER
			+ "yyyy";
	// for action forward name
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String DEFINE_LATER = "definelater";
	public static final String RUN_OFFLINE = "runOffline";

	// record operations
	public static final String RECORD_OPERATION_ADD = "add";
	public static final String RECORD_OPERATION_EDIT = "edit";

	// learning views
	public static final String LEARNING_VIEW_HORIZONTAL = "horizontal";
	public static final String LEARNING_VIEW_VERTICAL = "vertical";
	// for parameters' name
	public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";
	public static final String PARAM_TOOL_SESSION_ID = "toolSessionID";
	public static final String PARAM_FILE_VERSION_ID = "fileVersionId";
	public static final String PARAM_FILE_UUID = "fileUuid";
	public static final String PARAM_QUESTION_INDEX = "questionIndex";
	public static final String PARAM_RECORD_INDEX = "recordIndex";
	public static final String PARAM_DACO_QUESTION_UID = "questionUid";
	public static final String PARAM_RUN_OFFLINE = "runOffline";
	public static final String PARAM_TITLE = "title";
	public static final String PARAM_QUESTION_REQUIRED = "questionRequired";
	public static final String PARAM_LONGLAT_MAPS_SELECTED = "longlatMapsSelected";
	public static final String PARAM_REMOVE_INDEX = "removeIndex";

	// for request attribute name
	public static final String ATTR_LEARNING_CURRENT_TAB = "learningCurrentTab";
	public static final String ATTR_TOOL_CONTENT_ID = "toolContentID";
	public static final String ATTR_TOOL_SESSION_ID = "toolSessionID";
	public static final String ATTR_QUESTION_LIST = "questionList";
	public static final String ATTR_RECORD_LIST = "recordList";
	public static final String ATT_ATTACHMENT_LIST = "instructionAttachmentList";
	public static final String ATTR_DELETED_QUESTION_LIST = "deleteDacoList";
	public static final String ATTR_DELETED_ATTACHMENT_LIST = "deletedAttachmmentList";
	public static final String ATTR_DELETED_QUESTION_ATTACHMENT_LIST = "deletedQuestionAttachmmentList";
	public static final String ATTR_QUESTION_REVIEW_URL = "dacoQuestionReviewUrl";
	public static final String ATTR_DACO = "daco";
	public static final String ATTR_NEXT_ACTIVITY_URL = "nextActivityUrl";
	public static final String ATTR_SUMMARY_LIST = "summaryList";
	public static final String ATTR_USER_LIST = "userList";
	public static final String ATTR_FINISH_LOCK = "finishedLock";
	public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
	public static final String ATTR_DACO_FORM = "dacoForm";
	public static final String ATTR_ADD_QUESTION_TYPE = "addType";
	public static final String ATTR_FILE_TYPE_FLAG = "fileTypeFlag";
	public static final String ATTR_TITLE = "title";
	public static final String ATTR_USER_FINISHED = "userFinished";
	public static final String ATTR_ANSWER_OPTION_LIST = "answerOptionList";
	public static final String ATTR_REFLECTION_ON = "reflectOn";
	public static final String ATTR_REFLECTION_INSTRUCTION = "reflectInstructions";
	public static final String ATTR_REFLECTION_ENTRY = "reflectEntry";
	public static final String ATTR_REFLECT_LIST = "reflectList";
	public static final String ATTR_USER_UID = "userUid";
	public static final String ATTR_RECORD_OPERATION_SUCCESS = "recordOperationSuccess";
	public static final String ATTR_DISPLAYED_RECORD_NUMBER = "displayedRecordNumber";
	public static final String ATTR_LEARNING_VIEW = "learningView";

	// error message keys
	public static final String ERROR_MSG_DESC_BLANK = "error.question.desc.blank";
	public static final String ERROR_MSG_FILE_BLANK = "error.question.file.blank";
	public static final String ERROR_MSG_UPLOAD_FAILED = "error.upload.failed";
	public static final String ERROR_MSG_MAX_BLANK = "error.question.max.blank";
	public static final String ERROR_MSG_MAX_NUMBER_INT = "error.question.max.number.int";
	public static final String ERROR_MSG_MAX_NUMBER_FLOAT = "error.question.max.number.float";
	public static final String ERROR_MSG_MAX_NEGATIVE = "error.question.max.negative";
	public static final String ERROR_MSG_MAX_TOOHIGH = "error.question.max.toohigh";
	public static final String ERROR_MSG_MAX_TOOHIGH_ANSWEROPTION = "error.question.max.toohigh.answeroption";
	public static final String ERROR_MSG_MAX_TOOLOW = "error.question.max.toolow";
	public static final String ERROR_MSG_MIN_BLANK = "error.question.min.blank";
	public static final String ERROR_MSG_MIN_NUMBER_INT = "error.question.min.number.int";
	public static final String ERROR_MSG_MIN_NUMBER_FLOAT = "error.question.min.number.float";
	public static final String ERROR_MSG_MIN_NEGATIVE = "error.question.min.negative";
	public static final String ERROR_MSG_MIN_TOOHIGH = "error.question.min.toohigh";
	public static final String ERROR_MSG_MIN_TOOHIGH_ANSWEROPTION = "error.question.min.toohigh.answeroption";
	public static final String ERROR_MSG_MIN_TOOLOW = "error.question.min.toolow";
	public static final String ERROR_MSG_MIN_TOOHIGH_MAX = "error.question.min.toohigh.max";
	public static final String ERROR_MSG_ANSWEROPTION_REPEAT = "error.question.answeroption.repeat";
	public static final String ERROR_MSG_ANSWEROPTION_NOTENOUGH = "error.question.answeroption.notenough";
	public static final String ERROR_MSG_RECORDLIMIT_MIN_TOOHIGH_MAX = "error.recordlimit.min.toohigh.max";

	public static final String ERROR_MSG_RECORD_BLANK = "error.record.blank";
	public static final String ERROR_MSG_RECORD_TEXTAREA_LONG = "error.record.textarea.long";
	public static final String ERROR_MSG_RECORD_NUMBER_MIN = "error.record.number.min";
	public static final String ERROR_MSG_RECORD_NUMBER_MAX = "error.record.number.max";
	public static final String ERROR_MSG_RECORD_NUMBER_FLOAT = "error.record.number.float";
	public static final String ERROR_MSG_RECORD_DATE_DAY_BLANK = "error.record.date.day.blank";
	public static final String ERROR_MSG_RECORD_DATE_DAY_INT = "error.record.date.day.int";
	public static final String ERROR_MSG_RECORD_DATE_DAY_LIMIT = "error.record.date.day.limit";
	public static final String ERROR_MSG_RECORD_DATE_MONTH_BLANK = "error.record.date.month.blank";
	public static final String ERROR_MSG_RECORD_DATE_MONTH_INT = "error.record.date.month.int";
	public static final String ERROR_MSG_RECORD_DATE_MONTH_LIMIT = "error.record.date.month.limit";
	public static final String ERROR_MSG_RECORD_DATE_YEAR_BLANK = "error.record.date.year.blank";
	public static final String ERROR_MSG_RECORD_DATE_YEAR_INT = "error.record.date.year.int";
	public static final String ERROR_MSG_RECORD_IMAGE_FORMAT = "error.record.image.format";
	public static final String ERROR_MSG_RECORD_CHECKBOX_MIN = "error.record.checkbox.min";
	public static final String ERROR_MSG_RECORD_CHECKBOX_MAX = "error.record.checkbox.max";
	public static final String ERROR_MSG_RECORD_LONGITUDE_BLANK = "error.record.longlat.longitude.blank";
	public static final String ERROR_MSG_RECORD_LONGITUDE_FLOAT = "error.record.longlat.longitude.float";
	public static final String ERROR_MSG_RECORD_LATITUDE_BLANK = "error.record.longlat.latitude.blank";
	public static final String ERROR_MSG_RECORD_LATITUDE_FLOAT = "error.record.longlat.latitude.float";
	public static final String ERROR_MSG_RECORD_NOTENOUGH = "error.record.notenough";
	public static final String ERROR_MSG_RECORD_TOOMUCH = "error.record.toomuch";

	public static final String PAGE_EDITABLE = "isPageEditable";
	public static final String MODE_AUTHOR_SESSION = "author_session";
}