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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */  
 
/* $Id$ */  
package org.lamsfoundation.lams.tool.notebook.core;

public class TestFrameworkConstants {
	
	/** the host name on which the Selenium Server resides */
	public static final String SERVER_HOST = "localhost";
	/** the port on which the Selenium Server is listening */
	public static final int SERVER_PORT = 5555;
	/** 
	 * the command string used to launch the browser, e.g. "*firefox", "*iexplore" or
	 * "c:\\program files\\internet explorer\\iexplore.exe"
	 */	
	public static final String BROWSER = "*firefox";
	public static final String WEB_APP_HOST = "http://127.0.0.1:8080";
	/**
	 * the starting URL including just a domain name. We'll start the browser
	 * pointing at the Selenium resources on this URL,
	 */	
	public static final String WEB_APP_DIR = "/lams/";
	public static final String USER_LOGIN = "mmm";
	public static final String USER_PASSWORD = "mmm";

}
