/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.gradebook.util;

/**
 * Constants for gradebook module
 *
 * @author lfoxton
 *
 */
public class GradebookConstants {

    public static final String MODULE_NAME = "gradebook";

    // Parameters
    public static final String PARAM_LOGIN = "login";
    public static final String PARAM_ID = "id";
    public static final String PARAM_MARK = "mark";
    public static final String PARAM_FEEDBACK = "feedback";
    public static final String PARAM_USERID = "userID";
    public static final String PARAM_ROW_NAME = "rowName";
    public static final String PARAM_TIME_TAKEN = "timeTaken";
    public static final String PARAM_AVG_TIME_TAKEN = "avgTimeTaken";
    public static final String PARAM_AVG_MARK = "avgMark";
    public static final String PARAM_VIEW = "view";
    public static final String PARAM_START_DATE = "startDate";
    public static final String PARAM_GROUP_ID = "groupId";
    public static final String PARAM_SEQUENCE = "sequence";

    // Sort
    public static final String SORT_DESC = "desc";
    public static final String SORT_ASC = "asc";

    // Search
    public static final String SEARCH_EQUALS = "eq";
    public static final String SEARCH_NOT_EQUALS = "ne";
    public static final String SEARCH_BEGINS_WITH = "bw";
    public static final String SEARCH_ENDS_WITH = "ew";
    public static final String SEARCH_CONTAINS = "cn";

    // Views
    public static final String VIEW_MON_ACTIVITY = "monActivityView";
    public static final String VIEW_MON_USER = "monUserView";
    public static final String VIEW_MON_COURSE = "monCourse";
    public static final String VIEW_LRN_COURSE = "lrnCourse";
    public static final String VIEW_LRN_ACTIVITY = "lrnActivity";
    public static final String VIEW_LIST = "listView";

    // XML Elemetns
    public static final String ELEMENT_ROWS = "rows";
    public static final String ELEMENT_PAGE = "page";
    public static final String ELEMENT_TOTAL = "total";
    public static final String ELEMENT_RECORDS = "records";
    public static final String ELEMENT_ROW = "row";
    public static final String ELEMENT_ID = "id";
    public static final String ELEMENT_CELL = "cell";

    // Misc
    public static final String CONTENT_TYPE_TEXTXML = "text/xml";
    public static final String CONTENT_TYPE_TEXTPLAIN = "text/plain";
    public static final String CELL_EMPTY = "-";
    public static final String UTF8 = "UTF8";

}
