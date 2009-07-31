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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.wookie.util;

import java.io.File;

import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;

public interface WookieConstants {
    public static final String TOOL_SIGNATURE = "lawook10";

    // Wookie session status
    public static final Integer SESSION_NOT_STARTED = new Integer(0);
    public static final Integer SESSION_IN_PROGRESS = new Integer(1);
    public static final Integer SESSION_COMPLETED = new Integer(2);

    public static final String AUTHORING_DEFAULT_TAB = "1";
    public static final String ATTACHMENT_LIST = "attachmentList";
    public static final String DELETED_ATTACHMENT_LIST = "deletedAttachmentList";
    public static final String AUTH_SESSION_ID_COUNTER = "authoringSessionIdCounter";
    public static final String AUTH_SESSION_ID = "authoringSessionId";

    public static final int MONITORING_SUMMARY_MAX_MESSAGES = 5;

    // Attribute names
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_SESSION_MAP = "sessionMap";
    public static final String ATTR_WIDGET_LIST = "widgetList";
    public static final String ATTR_WIDGET_PAGES = "widgetPages";
    public static final String ATTR_WIDGET_URL = "widgetURL";

    // Parameter names
    public static final String PARAM_PARENT_PAGE = "parentPage";

    static final String FILTER_REPLACE_TEXT = "***";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";
    
    public static final String DEFAULT_IMAGE_FILE_NAME = "blank.jpg";
    
    // error message keys
    public static final String ERROR_MSG_NOT_ALLOWED_FORMAT = "error.resource.image.not.alowed.format";
    public static final String ERROR_MSG_FILE_BLANK = "error.resource.item.file.blank";
    public static final String ERROR_MSG_FILE_UPLOAD = "error.file.upload.failed";
    
    public static final String LAMS_WWW_PIXLR_FOLDER_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/www/images/wookie/";
    public static final String LAMS_PIXLR_BASE_DIR = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator + FileUtil.LAMS_WWW_DIR
	+ File.separator + "images" + File.separator + "wookie";
    
    
    public static final int WIDGETS_PER_PAGE = 8;
    
    public static final String RELATIVE_URL_WIDGET_LIST = "/advertise"; 
    public static final String RELATIVE_URL_WIDGET_SERVICE = "/WidgetServiceServlet";
    
    public static final String PARAM_ALL = "all";
    public static final String PARAM_TRUE = "true";
    public static final String PARAM_PAGE_COUNT = "pageCount";
    public static final String PARAM_PAGE_NUMBER = "pageNumber";
    
    public static final String PARAM_KEY_REQUEST_ID = "requestid";
    public static final String PARAM_KEY_API_KEY = "api_key";
    public static final String PARAM_KEY_SERVICE_TYPE = "servicetype";
    public static final String PARAM_KEY_WIDGIT_ID = "widgetid";
    public static final String PARAM_KEY_USER_ID = "userid";
    public static final String PARAM_KEY_SHARED_DATA_KEY = "shareddatakey";
    public static final String PARAM_KEY_PARTICIPANT_ID = "participant_id";
    public static final String PARAM_KEY_PARTICIPANT_DISPLAY_NAME = "participant_display_name";
    public static final String PARAM_KEY_PARTICIPANT_THUMBNAIL_URL = "participant_thumbnail_url";
    public static final String PARAM_KEY_PROPERTY_NAME = "propertyname";
    public static final String PARAM_KEY_PROPERTY_VALUE = "propertyvalue";
    
    public static final String PARAM_VALUE_GET_WIDGET= "getwidget";
    public static final String PARAM_VALUE_ADD_PARTICIPANT = "addparticipant";
    public static final String PARAM_VALUE_SET_PERSONAL_PROPERTY = "setpersonalproperty";
    public static final String PARAM_VALUE_PROPERTY_NAME_MODERATOR = "moderator";
    public static final String PARAM_VALUE_PROPERTY_VALUE_TRUE = "true";
    
    public static final String XML_WIDGETS = "widgets";
    public static final String XML_WIDGET = "widget";
    public static final String XML_IDENTIFIER = "identifier";
    public static final String XML_TITLE = "title";
    public static final String XML_DESCRIPTION = "description";
    public static final String XML_ICON = "icon";
    public static final String XML_PARAMETER = "parameter";
    public static final String XML_MESSAGE = "message";
    public static final String XML_COMPLETED = "completed";
    
}
