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
/* $Id: */

package org.lamsfoundation.lams.tool.dimdim.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class DimdimUtil {

    public static String generateMeetingKey() {
	return new Long(System.currentTimeMillis()).toString() + "-" + (new Random().nextInt());
    }

    public static String getReturnURL(HttpServletRequest request) {
	String protocol = request.getProtocol();
	if (protocol.startsWith("HTTPS")) {
	    protocol = "https://";
	} else {
	    protocol = "http://";
	}

	String path = protocol + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	if (!path.endsWith("/")) {
	    path = path + "/";
	}

	path += "endMeeting.do";

	return path;
    }

    // helper functions to extract info from json response.


    // get return code -- enterprise version
    private static Pattern patternCode = Pattern.compile("code:\"(.*?)\"");

    public static String getCode(String json) {
	Matcher matcher = patternCode.matcher(json);
	matcher.find();
	return matcher.group(1);
    }
    
    // get result -- standard version
    private static Pattern patternResult = Pattern.compile("result:\"(.*?)\"");
    
    public static String getResult(String json) {
	Matcher matcher = patternResult.matcher(json);
	matcher.find();
	return matcher.group(1);
    }

    // enterprise version
    private static Pattern patternEnterpriseURL = Pattern.compile("data:\\{text:\"(.*?)\"");

    public static String getEnterpriseURL(String json) {
	Matcher matcherUrl = patternEnterpriseURL.matcher(json);
	matcherUrl.find();
	return matcherUrl.group(1);
    }

    // standard version
    private static Pattern patternStandardURL = Pattern.compile("url:\"(.*?)\"");
    
    public static String getStandardURL(String response) throws Exception {
	Matcher matcher = patternStandardURL.matcher(response);
	matcher.find();
	return matcher.group(1);
    }
}
