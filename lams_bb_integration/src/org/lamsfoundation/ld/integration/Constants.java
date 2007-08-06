/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.ld.integration;

/**
 *  @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class Constants {
    
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
    
    //  Conf file constants
	//public static final String CONF_SERVER_ADDRRESS = "lamstwo.serverAddr";
	//public static final String CONF_SERVER_ID		= "lamstwo.serverId";
	//public static final String CONF_SERVER_KEY		= "lamstwo.serverKey";
	//public static final String CONF_REQUEST_SOURCE	= "lamstwo.requestSrc";
	
	// XML format constnats
	public static final String ELEM_FOLDER 			= "Folder";
	public static final String ELEM_LEARNING_DESIGN = "LearningDesign";
	public static final String ATTR_NAME			= "name";
	public static final String ATTR_RESOURCE_ID		= "resourceId";
    
}
