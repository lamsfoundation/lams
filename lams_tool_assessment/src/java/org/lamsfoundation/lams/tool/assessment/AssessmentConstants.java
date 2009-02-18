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
package org.lamsfoundation.lams.tool.assessment;

public class AssessmentConstants {
    public static final String TOOL_SIGNATURE = "laasse10";

    public static final String ASSESSMENT_SERVICE = "assessmentService";

    public static final String TOOL_CONTENT_HANDLER_NAME = "assessmentToolContentHandler";

    public static final int COMPLETED = 1;
    
    public static final int INITIAL_OPTIONS_NUMBER = 3;
    
    public static final int INITIAL_UNITS_NUMBER = 2;
    
    public static final int INITIAL_OVERALL_FEEDBACK_NUMBER = 3;

    // question type;
    public static final short QUESTION_TYPE_MULTIPLE_CHOICE = 1;

    public static final short QUESTION_TYPE_MATCHING_PAIRS = 2;
    
    public static final short QUESTION_TYPE_SHORT_ANSWER = 3;

    public static final short QUESTION_TYPE_NUMERICAL = 4;

    public static final short QUESTION_TYPE_TRUE_FALSE = 5;

    public static final short QUESTION_TYPE_ESSAY = 6;
    
    public static final short QUESTION_TYPE_ORDERING = 7;

    // for action forward name
    public static final String SUCCESS = "success";

    public static final String ERROR = "error";

    public static final String DEFINE_LATER = "definelater";

    // for parameters' name
    public static final String PARAM_TOOL_CONTENT_ID = "toolContentID";

    public static final String PARAM_TOOL_SESSION_ID = "toolSessionID";

    public static final String PARAM_FILE_VERSION_ID = "fileVersionId";

    public static final String PARAM_FILE_UUID = "fileUuid";

    public static final String PARAM_QUESTION_INDEX = "questionIndex";

    public static final String PARAM_QUESTION_UID = "questionUid";
    
    public static final String PARAM_OPTION_INDEX = "optionIndex";

    public static final String PARAM_RUN_OFFLINE = "runOffline";

    public static final String PARAM_OPEN_URL_POPUP = "popupUrl";

    public static final String PARAM_TITLE = "title";

    // for request attribute name
    public static final String ATTR_TOOL_CONTENT_ID = "toolContentID";

    public static final String ATTR_TOOL_SESSION_ID = "toolSessionID";
    
    public static final String ATTR_OPTION_LIST = "optionList";
    
    public static final String ATTR_OPTION_COUNT = "optionCount";
    
    public static final String ATTR_OPTION_ANSWER_PREFIX = "optionAnswer";
    
    public static final String ATTR_OPTION_GRADE_PREFIX = "optionGrade";
    
    public static final String ATTR_OPTION_ACCEPTED_ERROR_PREFIX = "optionAcceptedError";
    
    public static final String ATTR_OPTION_FEEDBACK_PREFIX = "optionFeedback";
    
    public static final String ATTR_OPTION_SEQUENCE_ID_PREFIX = "optionSequenceId";
    
    public static final String ATTR_OPTION_QUESTION_PREFIX = "optionQuestion";
    
    public static final String ATTR_QUESTION_TYPE = "questionType";

    public static final String ATTR_QUESTION_LIST = "questionList";

    public static final String ATT_ATTACHMENT_LIST = "instructionAttachmentList";

    public static final String ATTR_DELETED_QUESTION_LIST = "deleteAssessmentList";

    public static final String ATTR_DELETED_ATTACHMENT_LIST = "deletedAttachmmentList";

    public static final String ATTR_DELETED_QUESTION_ATTACHMENT_LIST = "deletedQuestionAttachmmentList";
    
    public static final String ATTR_UNIT_LIST = "unitList";
    
    public static final String ATTR_UNIT_COUNT = "unitCount";
    
    public static final String ATTR_UNIT_UNIT_PREFIX = "unitUnit";
    
    public static final String ATTR_UNIT_MULTIPLIER_PREFIX = "unitMultiplier";
    
    public static final String ATTR_UNIT_SEQUENCE_ID_PREFIX = "unitSequenceId";
    
    public static final String ATTR_OVERALL_FEEDBACK_LIST = "overallFeedbackList";
    
    public static final String ATTR_OVERALL_FEEDBACK_COUNT = "overallFeedbackCount";
    
    public static final String ATTR_OVERALL_FEEDBACK_GRADE_BOUNDARY_PREFIX = "overallFeedbackGradeBoundary";
    
    public static final String ATTR_OVERALL_FEEDBACK_FEEDBACK_PREFIX = "overallFeedbackFeedback";
    
    public static final String ATTR_OVERALL_FEEDBACK_SEQUENCE_ID_PREFIX = "overallFeedbackSequenceId";

    public static final String ATT_LEARNING_OBJECT = "cpPackage";

    public static final String ATTR_ASSESSMENT = "assessment";

    public static final String ATTR_QUESTION_UID = "questionUid";

    public static final String ATTR_NEXT_ACTIVITY_URL = "nextActivityUrl";

    public static final String ATTR_SUMMARY_LIST = "summaryList";

    public static final String ATTR_USER_LIST = "userList";

    public static final String ATTR_FINISH_LOCK = "finishedLock";

    public static final String ATTR_LOCK_ON_FINISH = "lockOnFinish";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

    public static final String ATTR_ASSESSMENT_FORM = "assessmentForm";

    public static final String ATTR_ADD_ASSESSMENT_TYPE = "addType";

    public static final String ATTR_FILE_TYPE_FLAG = "fileTypeFlag";

    public static final String ATTR_TITLE = "title";

    public static final String ATTR_INSTRUCTIONS = "instructions";

    public static final String ATTR_USER_FINISHED = "userFinished";
    
    public static final String ATTR_PAGE_NUMBER = "pageNumber";
    
    public static final String ATTR_PAGED_QUESTIONS = "pagedQuestions";

    // error message keys
    public static final String ERROR_MSG_QUESTION_NAME_BLANK = "error.question.name.blank";

    public static final String ERROR_MSG_QUESTION_TEXT_BLANK = "error.question.text.blank";
    
    public static final String ERROR_MSG_DEFAULT_GRADE_WRONG_FORMAT = "error.default.grade.wrong.format";
    
    public static final String ERROR_MSG_PENALTY_FACTOR_WRONG_FORMAT = "error.penalty.factor.wrong.format";
    
    public static final String ERROR_MSG_NOT_ENOUGH_OPTIONS = "error.not.enough.options";

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
}
