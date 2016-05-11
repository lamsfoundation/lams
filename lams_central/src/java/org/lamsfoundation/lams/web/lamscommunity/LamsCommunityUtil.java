/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.web.lamscommunity;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.lamsfoundation.lams.config.Registration;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class LamsCommunityUtil {

    public static final String LAMS_COMMUNITY_URL = "http://lamscommunity.org";
    public static final String LAMS_COMMUNITY_SSO_URL = "http://lamscommunity.org/lams/x/sso";
    public static final String LAMS_COMMUNITY_AUTH_URL = "http://lamscommunity.org/lams/x/auth";
    public static final String LAMS_COMMUNITY_IMPORT_URL = "http://lamscommunity.org/lams/x/auth";
    public static final String LAMS_COMMUNITY_EXPORT_URL = "http://lamscommunity.org/lams/x/auth";

    public static final String PARAM_HASH = "hs";
    public static final String PARAM_SERVER_ID = "sid";
    public static final String PARAM_TIMESTAMP = "ts";
    public static final String PARAM_LC_USERNAME = "un";
    public static final String PARAM_LC_PASSWORD = "ps";
    public static final String PARAM_LC_USER_TOKEN = "tk";
    public static final String PARAM_DEST = "dest";
    public static final String PARAM_RETURN_URL = "returnURL";

    public static String createAuthenticationHash(String timestamp, String username, String password, String serverId,
	    String serverKey) {
	String hash = "";
	if (serverId != null && serverKey != null) {
	    hash = LamsCommunityUtil.hash(timestamp + username + serverId + password + serverKey);
	}
	return hash;
    }

    public static String hash(String string) {
	return HashUtil.sha1(string);
    }

    public static String encryptAuthenticationInfo(String timestamp, String username, String password, String serverId,
	    String serverKey) throws Exception {
	String hash = "";
	if (serverId != null && serverKey != null) {
	    hash = LamsCommunityUtil.encrypt(timestamp + "," + username + "," + URLEncoder.encode(password, "UTF8"),
		    serverKey);
	}
	return hash;
    }

    public static String encrypt(String text, String password) throws Exception {

	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

	//setup key
	byte[] keyBytes = new byte[16];
	byte[] b = password.getBytes("UTF-8");
	int len = b.length;
	if (len > keyBytes.length) {
	    len = keyBytes.length;
	}
	System.arraycopy(b, 0, keyBytes, 0, len);

	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

	//the below may make this less secure, hard code byte array the IV in both java and .net clients
	IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

	cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
	byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
	byte[] encodedBytes = Base64.encodeBase64(results);
	String encodedString = new String(encodedBytes);

	return encodedString;
    }

    /**
     * Appends the required authentication info and hash to a url
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String appendAuthInfoToURL(String url, User user) throws Exception {
	Registration reg = Configuration.getRegistration();
	String serverID;
	String serverKey;
	if (reg != null && reg.getServerID() != null && reg.getServerKey() != null) {
	    serverID = reg.getServerID();
	    serverKey = reg.getServerKey();
	} else {
	    throw new Exception("Attempt to authenticate in lams community without registration");
	}

	String timestamp = "" + new Date().getTime();
	String hash = LamsCommunityUtil.createAuthenticationHash(timestamp, user.getLamsCommunityUsername(),
		user.getLamsCommunityToken(), serverID, serverKey);

	url += "&" + PARAM_LC_USERNAME + "=" + URLEncoder.encode(user.getLamsCommunityUsername(), "UTF8");
	url += "&" + PARAM_HASH + "=" + hash;
	url += "&" + PARAM_SERVER_ID + "=" + serverID;
	url += "&" + PARAM_TIMESTAMP + "=" + timestamp;

	return url;

    }

    public static HashMap<String, String> getAuthParams() throws Exception {
	HashMap<String, String> ret = new HashMap<String, String>();
	Registration reg = Configuration.getRegistration();
	String serverID;
	String serverKey;
	if (reg != null && reg.getServerID() != null && reg.getServerKey() != null) {
	    serverID = reg.getServerID();
	    serverKey = reg.getServerKey();
	} else {
	    throw new Exception("Attempt to authenticate in lams community without registration");
	}

	UserDTO userDTO = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	String timestamp = "" + new Date().getTime();
	String hash = LamsCommunityUtil.createAuthenticationHash(timestamp, userDTO.getLamsCommunityUsername(),
		userDTO.getLamsCommunityToken(), serverID, serverKey);

	ret.put(PARAM_LC_USERNAME, URLEncoder.encode(userDTO.getLamsCommunityUsername(), "UTF8"));
	ret.put(PARAM_HASH, hash);
	ret.put(PARAM_SERVER_ID, serverID);
	ret.put(PARAM_TIMESTAMP, timestamp);
	return ret;
    }

}
