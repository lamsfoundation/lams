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
package org.lamsfoundation.integration.util;

/**
 *  Constants used for blackboard integration
 *  
 *  @author <a href="mailto:lfoxton@melcoe.mq.edu.au">Luke Foxton</a>
 */
public class Constants {
    
	public static final String DEFAULT_LANGUAGE="en";
	public static final String DEFAULT_COUNTRY="US";
	
    public static final String PARAM_USER_ID = "uid";
    public static final String PARAM_SERVER_ID = "sid";
    public static final String PARAM_TIMESTAMP = "ts";
    public static final String PARAM_HASH = "hash";
    //public static final String PARAM_URL = "url";
    public static final String PARAM_METHOD = "method";
    public static final String PARAM_LEARNING_SESSION_ID = "lsid";
    public static final String PARAM_LEARNING_DESIGN_ID = "ldid";
    public static final String PARAM_COURSE_ID = "course_id";

    
    public static final String SERVLET_LOGIN_REQUEST = "/lams/LoginRequest";
    public static final String SERVLET_ACTION_REQUEST = "/LamsActionRequest";
    
    public static final String URLDECODER_CODING = "US-ASCII";
    
    public static final String METHOD_AUTHOR = "author";
    public static final String METHOD_MONITOR = "monitor";
    public static final String METHOD_LEARNER = "learner";
    
	// XML format constnats
	public static final String ELEM_FOLDER 			= "Folder";
	public static final String ELEM_LEARNING_DESIGN = "LearningDesign";
	public static final String ATTR_NAME			= "name";
	public static final String ATTR_RESOURCE_ID		= "resourceId";

	public static final String SETTING_LAMS_SERVER_URL = "lamsServerUrl";
	public static final String SETTING_SERVER_ID = "serverId";
	public static final String SETTING_SECRET_KEY = "secretKey";
	public static final String SETTING_REQUEST_SRC = "webctRequestSource";
	public static final String SETTING_DB_NAME = "dbName";
	public static final String SETTING_DB_HOST = "dbHost";
	public static final String SETTING_DB_PORT = "dbPort";
	public static final String SETTING_DB_USER = "dbUser";
	public static final String SETTING_DB_PASS = "dbPass";
    
}
