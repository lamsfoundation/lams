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

package org.lamsfoundation.lams.tool.bbb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;

public class BbbUtil {

    public static String getMeetingKey(Long toolSessionId) {
	return "bbb_" + RandomPasswordGenerator.nextPassword(12) + "-" + toolSessionId;
    }

    public static String getReturnURL(HttpServletRequest request) {
	String protocol;
	if (request.isSecure()) {
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

    // helper functions to extract info from XML response.
    
    // get result -- standard version
    private static Pattern patternResult = Pattern.compile("result:\"(.*?)\"");
    
    public static String getResult(String json) {
	Matcher matcher = patternResult.matcher(json);
	matcher.find();
	return matcher.group(1);
    }
    
    public static String getResponse(String response) throws Exception {
    	
    	if (response.contains(Constants.RESPONSE_SUCCESS)) {
    		return Constants.RESPONSE_SUCCESS;
    	} else {
    		return Constants.RESPONSE_FAIL;
    		
    	}
    	
    	
    }
    
}
