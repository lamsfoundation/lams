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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.util.WebUtil;

public class DimdimUtil {

    public static String getMeetingKey(Long toolSessionId) {
	return "dimdim_" + toolSessionId;
    }

    public static String getReturnURL(HttpServletRequest request) {
	return WebUtil.getBaseServerURL() + "endMeeting.do";
    }

    // get result -- standard version
    private static Pattern patternResult = Pattern.compile("result:\"(.*?)\"");
    
    public static String getResult(String json) {
	Matcher matcher = patternResult.matcher(json);
	matcher.find();
	return matcher.group(1);
    }

    // standard version
    private static Pattern patternURL = Pattern.compile("url:\"(.*?)\"");
    
    public static String getURL(String response) throws Exception {
	Matcher matcher = patternURL.matcher(response);
	matcher.find();
	return matcher.group(1);
    }
}
