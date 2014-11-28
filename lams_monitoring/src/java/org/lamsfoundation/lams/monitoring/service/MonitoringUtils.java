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
package org.lamsfoundation.lams.monitoring.service;

import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;


public class MonitoringUtils {
    
    private static final String CODE = "jbdnuteywk";
    
    /**
     * Constructs learner URL using API provided by tinyurl.com 
     * 
     * @param lessonId
     * @param selfsignup
     * @return
     */
    public static String encodeLessonId(Long lessonId) {
	
	String encodedLessonId = lessonId.toString();
	encodedLessonId = encodedLessonId.replace('0', CODE.charAt(0));
	encodedLessonId = encodedLessonId.replace('1', CODE.charAt(1));
	encodedLessonId = encodedLessonId.replace('2', CODE.charAt(2));
	encodedLessonId = encodedLessonId.replace('3', CODE.charAt(3));
	encodedLessonId = encodedLessonId.replace('4', CODE.charAt(4));
	encodedLessonId = encodedLessonId.replace('5', CODE.charAt(5));
	encodedLessonId = encodedLessonId.replace('6', CODE.charAt(6));
	encodedLessonId = encodedLessonId.replace('7', CODE.charAt(7));
	encodedLessonId = encodedLessonId.replace('8', CODE.charAt(8));
	encodedLessonId = encodedLessonId.replace('9', CODE.charAt(9));
	
	return encodedLessonId;
    }

}
