/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.integration.security;

import java.util.Date;

import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.util.IntegrationConstants;
import org.lamsfoundation.lams.util.HashUtil;

/**
 * <p>
 * <a href="Authenticator.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class Authenticator {
    /**
     * Checks hash. Hash is expected to be constructed using the following formula [ts + uid + method + serverID +
     * serverKey]. (Note: all lower case before hashing)
     *
     * @param map
     * @param datetime
     * @param username
     * @param method
     * @param hashValue
     * @throws AuthenticationException
     */
    public static void authenticate(ExtServer map, String datetime, String username, String method, String hashValue)
	    throws AuthenticationException {

	if (map == null) {
	    throw new AuthenticationException("The third party server is not configured on LAMS server");
	}
	if (map.getDisabled()) {
	    throw new AuthenticationException("The third party server is disabled");
	}

	String plaintext = datetime.toLowerCase().trim() + username.toLowerCase().trim() + method.toLowerCase().trim()
		+ map.getServerid().toLowerCase().trim() + map.getServerkey().toLowerCase().trim();
	Authenticator.checkHash(plaintext, hashValue);
    }

    /**
     * Checks hash for LoginRequest calls. Differs from the method above in a way that datetime is expected to contain
     * real value and it must be quite recent one.
     *
     * @param map
     * @param datetime
     * @param username
     * @param method
     * @param lsid
     *            this parameter provided only if coming from learnerStrictAuth
     * @param hashValue
     * @throws AuthenticationException
     */
    public static void authenticateLoginRequest(ExtServer map, String datetime, String username, String method,
	    String lsid, String hashValue) throws AuthenticationException {

	if (map == null) {
	    throw new AuthenticationException("The third party server is not configured on LAMS server");
	}
	if (map.getDisabled()) {
	    throw new AuthenticationException("The third party server is disabled");
	}

	// check if there is datetime check and if so if it isn't too old
	if (map.getTimeToLiveLoginRequestEnabled()) {
	    long datetimeParam;
	    try {
		datetimeParam = Long.parseLong(datetime);
	    } catch (NumberFormatException e) {
		throw new AuthenticationException(
			"The third party server provided wrong format of datetime, datetime = " + datetime, e);
	    }

	    int timeToLiveLoginRequest = map.getTimeToLiveLoginRequest();
	    // sum up request time and maximum allowed request's time to live
	    Date requestTimePlusTimeToLive = new Date(datetimeParam + timeToLiveLoginRequest * 60 * 1000);
	    Date requestTimeMinusTimeToLive = new Date(datetimeParam - timeToLiveLoginRequest * 60 * 1000);
	    Date now = new Date();
	    if (requestTimePlusTimeToLive.before(now) || requestTimeMinusTimeToLive.after(now)) {
		throw new AuthenticationException("Request is not in the time range of " + timeToLiveLoginRequest
			+ " minutes. Please, contact sysadmin.");
	    }
	}

	//learnerStrictAuth hash [ts + uid + method + lsid + serverID + serverKey]
	//otherwise [ts + uid + method + serverID + serverKey]
	String plaintext = datetime.toLowerCase().trim() + username.toLowerCase().trim() + method.toLowerCase().trim()
		+ (IntegrationConstants.METHOD_LEARNER_STRICT_AUTHENTICATION.equals(method) ? lsid.toLowerCase().trim()
			: "")
		+ map.getServerid().toLowerCase().trim() + map.getServerkey().toLowerCase().trim();
	Authenticator.checkHash(plaintext, hashValue);
    }

    public static void authenticate(ExtServer map, String datetime, String username, String hashValue)
	    throws AuthenticationException {
	if (map == null) {
	    throw new AuthenticationException("The third party server is not configured on LAMS server");
	}
	if (map.getDisabled()) {
	    throw new AuthenticationException("The third party server is disabled");
	}

	String plaintext = datetime.toLowerCase().trim() + username.toLowerCase().trim()
		+ map.getServerid().toLowerCase().trim() + map.getServerkey().toLowerCase().trim();
	Authenticator.checkHash(plaintext, hashValue);
    }

    public static void authenticate(ExtServer map, String datetime, String hashValue) throws AuthenticationException {
	if (map == null) {
	    throw new AuthenticationException("The third party server is not configured on LAMS server");
	}
	if (map.getDisabled()) {
	    throw new AuthenticationException("The third party server is disabled");
	}

	String plaintext = datetime.toLowerCase().trim() + map.getServerid().toLowerCase().trim()
		+ map.getServerkey().toLowerCase().trim();
	Authenticator.checkHash(plaintext, hashValue);
    }

    public static void checkHash(String plaintext, String hashValue) throws AuthenticationException {
	if (!hashValue.equals(HashUtil.sha1(plaintext))) {
	    throw new AuthenticationException("Authentication failed!");
	}
    }
}