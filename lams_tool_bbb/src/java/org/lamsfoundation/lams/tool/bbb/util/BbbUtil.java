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


package org.lamsfoundation.lams.tool.bbb.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.lamsfoundation.lams.util.WebUtil;

public class BbbUtil {

    public static String getMeetingKey(Long toolSessionId, String attendeePassword) {

	/*
	 * We hash the attendee's password to be used as part of the meetingKey.
	 * The main reason for this is so we can keep this unique in case there are many
	 * other instances that use the same BBB server
	 */
	return "bbb_" + DigestUtils.shaHex(attendeePassword) + "-" + toolSessionId;
    }

    public static String getReturnURL(HttpServletRequest request) {
	return WebUtil.getBaseServerURL() + "endMeeting.do";
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
