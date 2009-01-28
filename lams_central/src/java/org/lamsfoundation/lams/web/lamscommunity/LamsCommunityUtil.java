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

/* $Id$ */
package org.lamsfoundation.lams.web.lamscommunity;

import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.lamsfoundation.lams.util.HashUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class LamsCommunityUtil {


    public static final String LAMS_COMMUNITY_URL = "http://lamscommunity.org";
    public static final String LAMS_COMMUNITY_SSO_URL = "http://lamscommunity.org/lams/x/sso";
    public static final String LAMS_COMMUNITY_AUTH_URL = "http://lamscommunity.org/lams/x/auth";

    
    
    public static final String PARAM_HASH = "hs";
    public static final String PARAM_SERVER_ID = "sid";
    public static final String PARAM_TIMESTAMP = "ts";
    public static final String PARAM_LC_USERNAME = "un";
    public static final String PARAM_LC_PASSWORD = "ps";
    public static final String PARAM_LC_USER_TOKEN = "tk";
    

    public static String createAuthenticationHash(String timestamp, String username, String password, String serverId, String serverKey) {
	String hash = "";
	if (serverId != null && serverKey != null) {
	    hash = hash(timestamp + username + serverId + password + serverKey);
	}
	return hash;
    }
    
    public static String hash(String string)
    {
	return HashUtil.sha1(string);
    }
    
    public static String encryptAuthenticationInfo(String timestamp, String username, String password, String serverId,
	    String serverKey) throws Exception{
	String hash = "";
	if (serverId != null && serverKey != null) {
	    hash = encrypt(timestamp +","+  username +","+ URLEncoder.encode(password, "UTF8"), serverKey);
	}
	return hash;
    }

    public static String encrypt(String text, String password) throws Exception{

	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

	//setup key
	byte[] keyBytes = new byte[16];
	byte[] b = password.getBytes("UTF-8");
	int len = b.length;
	if (len > keyBytes.length)
	    len = keyBytes.length;
	System.arraycopy(b, 0, keyBytes, 0, len);

	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

	//the below may make this less secure, hard code byte array the IV in both java and .net clients
	IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

	cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
	byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
	BASE64Encoder encoder = new BASE64Encoder();
	return encoder.encode(results);
    }
    
    
}
