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

package org.lamsfoundation.lams.tool.daco;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DacoConstants {
    // system-wide constants
    public static final String TOOL_SIGNATURE = "ladaco10";

    public static final String DACO_SERVICE = "dacoService";

    public static final int SESSION_COMPLETED = 1;

    // question types
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
    public static final String[] LONGLAT_MAPS_LIST = new String[] { "Google Maps", "Geabios", "Open Street Map",
	    "Multimap" };

    // for date parsing
    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // answer options' constants
    public static final int INIT_ANSWER_OPTION_COUNT = 3;

    public static final int ANSWER_OPTION_MINIMUM_COUNT = 2;

    public static final String ANSWER_OPTION_DESC_PREFIX = "answerOptionItemDesc";

    public static final String ANSWER_OPTION_COUNT = "answerOptionCount";

    // image question valid extensions
    public static final String[] IMAGE_EXTENSIONS = new String[] { "GIF", "JPG", "PNG" };

    // action forward names
    public static final String SUCCESS = "success";

    public static final String ERROR = "error";

    public static final String DEFINE_LATER = "definelater";

    // record operations
    public static final String RECORD_OPERATION_ADD = "add";

    public static final String RECORD_OPERATION_EDIT = "edit";

    // question summary columns
    public static final short QUESTION_DB_NUMBER_SUMMARY_QUESTION_UID = 0;

    public static final short QUESTION_DB_NUMBER_SUMMARY_SUM = 1;

    public static final short QUESTION_DB_NUMBER_SUMMARY_AVERAGE = 2;

    public static final short QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_QUESTION_UID = 0;

    public static final short QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_ANSWER = 1;

    public static final short QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_QUESTION_TYPE = 2;

    public static final short QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_COUNT = 3;

    // learning views
    public static final String LEARNING_VIEW_HORIZONTAL = "horizontal";

    public static final String LEARNING_VIEW_VERTICAL = "vertical";

    // monitoring summary query match & user data return
    public static final Long MONITORING_SUMMARY_MATCH_ALL = null;

    public static final Long MONITORING_SUMMARY_MATCH_NONE = -1L;

    public static final String USER_UID = "userUid";

    public static final String USER_ID = "userId";

    public static final String USER_FULL_NAME = "userFullName";

    public static final String NOTEBOOK_ENTRY = "notebookEntry";

    public static final String RECORD_COUNT = "recordCount";

    public static final String PORTRAIT_ID = "portraitId";

    // for parameters' name

    public static final String PARAM_FILE_VERSION_ID = "fileVersionId";

    public static final String PARAM_FILE_UUID = "fileUuid";

    public static final String PARAM_QUESTION_INDEX = "questionIndex";

    public static final String PARAM_RECORD_INDEX = "recordIndex";

    public static final String PARAM_QUESTION_UID = "questionUid";

    public static final String PARAM_LONGLAT_MAPS_SELECTED = "longlatMapsSelected";

    public static final String PARAM_ANSWER_OPTION_INDEX = "removeIndex";

    // for request attribute name
    public static final String ATTR_USER = "user";

    public static final String ATTR_MONITORING_SUMMARY = "monitoringSummary";

    public static final String ATTR_MONITORING_CURRENT_TAB = "monitoringCurrentTab";

    public static final String ATTR_LEARNING_CURRENT_TAB = "learningCurrentTab";

    public static final String ATTR_QUESTION_LIST = "questionList";

    public static final String ATTR_RECORD_LIST = "recordList";

    public static final String ATTR_DELETED_QUESTION_LIST = "deleteDacoList";

    public static final String ATTR_QUESTION_REVIEW_URL = "dacoQuestionReviewUrl";

    public static final String ATTR_DACO = "daco";

    public static final String ATTR_USER_LIST = "userList";

    public static final String ATTR_FINISH_LOCK = "finishedLock";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

    public static final String ATTR_DACO_FORM = "dacoForm";

    public static final String ATTR_USER_FINISHED = "userFinished";

    public static final String ATTR_ANSWER_OPTION_LIST = "answerOptionList";

    public static final String ATTR_REFLECTION_ENTRY = "reflectEntry";

    public static final String ATTR_RECORD_OPERATION_SUCCESS = "recordOperationSuccess";

    public static final String ATTR_DISPLAYED_RECORD_NUMBER = "displayedRecordNumber";

    public static final String ATTR_LEARNING_VIEW = "learningView";

    public static final String ATTR_QUESTION_SUMMARIES = "questionSummaries";

    public static final String ATTR_SESSION_SUMMARIES = "sessionSummaries";

    public static final String ATTR_TOTAL_RECORD_COUNT = "totalRecordCount";

    public static final String ATTR_IS_GROUPED_ACTIVITY = "isGroupedActivity";

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

    public static final String ERROR_MSG_DIGITSDECIMAL_INT = "error.question.digitsdecimal.int";

    public static final String ERROR_MSG_DIGITSDECIMAL_NONNEGATIVE = "error.question.digitsdecimal.nonnegative";

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

    public final static String LEARNER_NUM_POSTS_DEFINITION_NAME = "learner.number.of.posts";

    // for export to spreadsheet

    public static final String EXPORT_TO_SPREADSHEET_FILE_NAME = "Data_Collection_Export.xlsx";

    public static final String KEY_LABEL_EXPORT_FILE_SHEET = "label.export.file.sheet";

    public static final String KEY_LABEL_LEARNING_LONGLAT_LATITUDE_UNIT = "label.learning.longlat.latitude.unit";

    public static final String KEY_LABEL_LEARNING_LONGLAT_LATITUDE = "label.learning.longlat.latitude";

    public static final String KEY_LABEL_LEARNING_LONGLAT_LONGITUDE_UNIT = "label.learning.longlat.longitude.unit";

    public static final String KEY_LABEL_LEARNING_LONGLAT_LONGITUDE = "label.learning.longlat.longitude";

    public static final String KEY_LABEL_EXPORT_FILE_USER = "label.export.file.user";

    public static final String KEY_LABEL_EXPORT_FILE_DATE = "label.export.file.date";

    public static final String KEY_LABEL_EXPORT_FILE_TITLE = "label.export.file.title";

    public static final String KEY_LABEL_EXPORT_FILE_ANSWER_DATE = "label.export.file.answer.date";

    // paging and sorting
    public static final String ATTR_SORT = "sort";
    public static final int SORT_BY_NO = 0;
    public static final int SORT_BY_USER_NAME_ASC = 1;
    public static final int SORT_BY_USER_NAME_DESC = 2;
    public static final int SORT_BY_NUM_RECORDS_ASC = 3;
    public static final int SORT_BY_NUM_RECORDS_DESC = 4;
}