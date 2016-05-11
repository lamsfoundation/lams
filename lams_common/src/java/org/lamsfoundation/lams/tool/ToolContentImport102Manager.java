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


package org.lamsfoundation.lams.tool;

/**
 * Tools that support this interface can import data from a LAMS 1.0.2.
 *
 * Tool do *not* need to implement this interface as part of the tool contract however
 * many of the initial tools build for LAMS 2.0 do support this interface to
 * allow 1.0.2 designs to be imported into 2.0.
 *
 * Any tool implementing a signature listed in the lams_tool_import_support table
 * as supporting one of the 1.0.2 tool must implement this interface otherwise
 * an exception will be thrown.
 */
import java.util.Hashtable;

import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

public interface ToolContentImport102Manager {

    public static final String TAGS_TYPE_AUTHORING = "authoring";
    public static final String TAGS_JOURNAL = "journal";
    public static final String TAGS_NOTICEBOARD = "noticeboard";
    public static final String TAGS_MESSAGEBOARD = "messageboard";
    public static final String TAGS_CHAT = "chat";
    public static final String TAGS_RPT_SUBMIT = "reportsubmission";
    public static final String TAGS_GROUPING = "group";
    public static final String TAGS_GROUPREPORTING = "groupreporting";
    public static final String TAGS_RANKING = "ranking";
    public static final String TAGS_QUESTIONANSWER = "qa";
    public static final String TAGS_SIMPLE_ASSESSMENT = "simpleassessment";
    public static final String TAGS_URLCONTENT = "urlcontent";
    public static final String TAGS_HTMLNOTICBOARD = "htmlnb";
    public static final String TAGS_SURVEY = "survey";

    // the following tools haven't supported for a while or can't be supported by 
    // import as they are tied to files which won't be in the import, 
    // so we won't support them in the import
    // public static final String TAGS_LOMS = "loms"; 
    // public static final String TAGS_IMAGEGALLERY = "imagegallery"; 
    // public static final String TAGS_IMAGERANKING = "imageranking";
    // public static final String TAGS_RPT_MARK= "reportmarking";
    // public static final String TAGS_SINGLE_RESOURCE = "singleresource"; 
    // public static final String TAGS_FILECONTENT = "filecontent"; 

    public static final String CONTENT_BODY = "body"; // used
    public static final String CONTENT_SHOW_USER = "contentShowUser"; // boolean
    public static final String CONTENT_TITLE = "title"; // used
    public static final String CONTENT_ID = "id"; // used
    public static final String CONTENT_REUSABLE = "isReusable"; //type boolean

    // contentType is based on the content class used - doesn't persist
    public static final String CONTENT_TYPE = "contentType";
    public static final String CONTENT_NUMGROUPS = "number_groups";
    public static final String CONTENT_MINNUM_GROUP = "min_number_in_group";
    public static final String CONTENT_MAXNUM_GROUP = "max_number_in_group";

    // Ranking tool tags
    public static final String CONTENT_VOTE_MAXCHOOSE = "maxChoose";
    public static final String CONTENT_VOTE_MINCHOOSE = "minChoose";
    public static final String CONTENT_VOTE_METHOD = "voteMethod"; // equal vote or preferential
    public static final String CONTENT_VOTE_NOMINATIONS = "nominations";
    public static final String CONTENT_VOTE_ALLOW_POLL_NOMINATIONS = "nominatePollTime"; // allow nomination or not (Boolean)
    public static final String CONTENT_VOTE_PROGRESSIVE_DISPLAY = "progressive_display";

    // url content
    public static final String CONTENT_URL_MIN_NUMBER_COMPLETE = "minNumberComplete";
    public static final String CONTENT_URL_RUNTIME_STAFF_SUBMIT_URL = "runtimeSubmissionStaffURL";
    public static final String CONTENT_URL_RUNTIME_LEARNER_SUBMIT_URL = "runtimeSubmissionLearnerURL";
    public static final String CONTENT_URL_RUNTIME_STAFF_SUBMIT_FILE = "runtimeSubmissionStaffFile";
    public static final String CONTENT_URL_RUNTIME_LEARNER_SUBMIT_FILE = "runtimeSubmissionLearnerFile";
    public static final String CONTENT_URL_URLS = "urls";

    public static final String CONTENT_URL_URL_SHOWBUTTONS = "showbuttons";
    public static final String CONTENT_URL_URL_VIEW_ORDER = "order";
    public static final String CONTENT_URL_URL_URL = "url";
    public static final String CONTENT_URL_URL_DOWNLOAD = "download"; // boolean: Author prefers that the content is downloaded. Only applicable to file content.
    public static final String CONTENT_URL_URL_TYPE = "resourcetype"; // see URLContent TYPE_* fields
    public static final String CONTENT_URL_URL_INSTRUCTION_ARRAY = "instructions";
    public static final String CONTENT_URL_INSTRUCTION = "instruction";

    public static final String URL_RESOURCE_TYPE_WEBSITE = "internalurl"; // Uploaded website - will be missing its files.
    public static final String URL_RESOURCE_TYPE_FILE = "file"; // Uploaded file - will be missing its files.
    public static final String URL_RESOURCE_TYPE_URL = "externalurl"; // URL - will be okay

    // message content
    public static final String CONTENT_MB_TERMINATION_TYPE = "terminationType"; // type string
    public static final String CONTENT_MB_DURATION_DAYS = "durationInDays"; // type string
    public static final String CONTENT_MB_POSTING_NOTIFIED = "isPostingNotified"; // type boolean
    public static final String CONTENT_MB_POSTING_MODERATED = "isPostingModerated"; // type boolean
    public static final String CONTENT_MB_NEW_TOPIC_ALLOWED = "isNewTopicAllowed"; //type boolean
    public static final String CONTENT_MB_TOPICS = "topics"; // array

    public static final String CONTENT_MB_TOPIC_SUBJECT = "subject"; // string
    public static final String CONTENT_MB_TOPIC_MESSAGE = "message"; // string 
    public static final String CONTENT_MB_TOPIC_NUMBER = "number"; // number

    // Simple Questions content
    public static final String CONTENT_Q_SHOW_FEEDBACK = "showfeedback"; // boolean
    public static final String CONTENT_Q_ALLOW_REDO = "allowredo"; // integer
    public static final String CONTENT_Q_MIN_PASSMARK = "minpassmark"; // integer
    public static final String CONTENT_Q_SHOW_TOP_USERNAMES = "showtopusernames"; // boolean
    public static final String CONTENT_Q_ORDER = "order"; // integer
    public static final String CONTENT_Q_QUESTION_INFO = "questionanswers"; // string
    public static final String CONTENT_Q_QUESTION = "question"; // string
    public static final String CONTENT_Q_FEEDBACK = "feedback"; // string
    public static final String CONTENT_Q_CANDIDATES = "candidates"; // array of string 
    public static final String CONTENT_Q_ANSWER = "answer"; // string 

    // Survey content
    public static final String CONTENT_SURVEY_TEXTBOX_ENABLED = "isTextBoxEnabled"; // boolean
    public static final String CONTENT_SURVEY_QUESTION_TYPE = "questionType"; // string	
    public static final String CONTENT_SURVEY_OPTIONAL = "isOptional"; // boolean
    public static final String CONTENT_SURVEY_QUESTIONS = "questions"; // string
    public static final String CONTENT_SURVEY_QUESTION = "question"; // string
    public static final String CONTENT_SURVEY_CANDIDATES = "candidates"; // array of maps, each map contain order and answer
    public static final String CONTENT_SURVEY_ORDER = "order"; // integer	
    public static final String CONTENT_SURVEY_ANSWER = "answer"; // string 
    public static final String CONTENT_SURVEY_COMPLETION_MESSAGE = "summary"; // string 

    public static final String CONTENT_SURVEY_TYPE_SINGLE = "simpleChoice";
    public static final String CONTENT_SURVEY_TYPE_MULTIPLE = "choiceMultiple";
    public static final String CONTENT_SURVEY_TYPE_TEXTENTRY = "textEntry";

    // for file upload - SingleResource, HTMLNoticeboard, Image tools
    public static final String DIRECTORY_NAME = "directoryName";

    /**
     * Import some 1.0.2 data, where the importValues is a map of the fields from the
     * 1.0.2 content record. The keys are WDDX tags, values are the WDDX values as strings.
     * The tools should call WDDXProcessor.convertToInteger(identifier, value) and similar
     * methods to convert the values to a valid type.
     *
     * Does not set the "reflective" fields.
     *
     * @param toolContentId
     *            new tool content id - will not be null.
     * @param user
     *            user importing the data - will not be null.
     * @param importValues
     *            map of values to import - will not be null.
     * @throws ToolException
     *             an error occurs
     */
    public void import102ToolContent(Long toolContentId, UserDTO creator, Hashtable importValues) throws ToolException;

    /**
     * Set the "reflective" fields on an existing piece of content. Note: Most / All of LAMS 2.0 tools
     * do not support the title entry for reflection, so any text in that field will be
     * lost during the import. Only the description is kept.
     *
     * @param title
     *            heading to use above reflective entry box - may be null
     * @param description
     *            default contents for the reflective entry box - may be null
     */
    public void setReflectiveData(Long toolContentId, String title, String description)
	    throws ToolException, DataMissingException;

}
