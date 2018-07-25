/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.tool.gmap.util;

public interface GmapConstants {
    public static final String TOOL_SIGNATURE = "lagmap10";

    // Gmap session status
    public static final Integer SESSION_NOT_STARTED = new Integer(0);
    public static final Integer SESSION_IN_PROGRESS = new Integer(1);
    public static final Integer SESSION_COMPLETED = new Integer(2);

    public static final String AUTHORING_DEFAULT_TAB = "1";
    public static final String AUTH_SESSION_ID_COUNTER = "authoringSessionIdCounter";
    public static final String AUTH_SESSION_ID = "authoringSessionId";

    public static final int MONITORING_SUMMARY_MAX_MESSAGES = 5;

    // Attribute names
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_SESSION_MAP = "sessionMap";
    public static final String ATTR_GMAP_KEY = "gmapKey";
    public static final String ATTR_USER_DTO = "gmapUserDTO";
    public static final String ATTR_USER = "gmapUser";

    public static final String ATTR_USER_ID = "userId";
    public static final String ATTR_PORTRAIT_ID = "portraitId";
    public static final String ATTR_USER_FULLNAME = "fullName";
    public static final String ATTR_NUM_MARKERS = "numMarkers";
    public static final String ATTR_USER_REFLECTION = "reflection";
    
    public static final int SORT_BY_NO = 1;
    public static final int SORT_BY_USERNAME_ASC = 2;
    public static final int SORT_BY_USERNAME_DESC = 3;

    // Parameter names
    public static final String PARAM_PARENT_PAGE = "parentPage";

    static final String FILTER_REPLACE_TEXT = "***";
}
