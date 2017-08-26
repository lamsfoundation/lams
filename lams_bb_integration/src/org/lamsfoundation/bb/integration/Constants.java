/****************************************************************
 * Copyright (C) 2007 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.bb.integration;

/**
 * Constants used for blackboard integration
 * 
 * @author <a href="mailto:lfoxton@melcoe.mq.edu.au">Luke Foxton</a>
 */
public class Constants {

    public static final String PARAM_USER_ID = "uid";
    public static final String PARAM_SERVER_ID = "sid";
    public static final String PARAM_TIMESTAMP = "ts";
    public static final String PARAM_HASH = "hash";
    // public static final String PARAM_URL = "url";
    public static final String PARAM_METHOD = "method";
    public static final String PARAM_LESSON_ID = "lsId";
    public static final String PARAM_LEARNING_DESIGN_ID = "ldid";
    public static final String PARAM_COURSE_ID = "course_id";
    public static final String PARAM_FOLDER_ID = "folderId";
    public static final String PARAM_IS_USER_DETAILS_REQUIRED = "isUserDetailsRequired";

    public static final String SERVLET_LOGIN_REQUEST = "/lams/LoginRequest";
    public static final String SERVLET_ACTION_REQUEST = "/LamsActionRequest";

    public static final String URLDECODER_CODING = "US-ASCII";

    public static final String METHOD_AUTHOR = "author";
    public static final String METHOD_MONITOR = "monitor";
    public static final String METHOD_LEARNER = "learner";
    
    public static final String GRADEBOOK_LINEITEM_TYPE = "LAMS grades";
    public static final int GRADEBOOK_POINTS_POSSIBLE = 100;

    // XML format constnats
    public static final String ELEM_FOLDER = "Folder";
    public static final String ELEM_LEARNING_DESIGN = "LearningDesign";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_RESOURCE_ID = "resourceId";

}
