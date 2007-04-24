/******************************************************************************
 * LamstwoUtils.java
 * 
 * Copyright (c) 2007 LAMS Foundation
 * Licensed under the Educational Community License version 1.0
 * 
 * A copy of the Educational Community License has been included in this 
 * distribution and is available at: http://www.opensource.org/licenses/ecl1.php
 * 
 *****************************************************************************/

package org.lamsfoundation.lams.integrations.sakai.logic.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class LamstwoUtils {

	public static String generateAuthenticationHash(String serverID, String serverKey,
			String username, String datetime) {
		String plaintext = datetime.toLowerCase().trim()
				+ username.toLowerCase().trim() + serverID.toLowerCase().trim()
				+ serverKey.toLowerCase().trim();

		return sha1(plaintext);
	}
	
	public static String generateUserAuthenticationHash(String timestamp, String username, String serverID, String serverKey) {
		String plaintext = timestamp + username + serverID + serverKey;
		return sha1(plaintext);
	}

	public static String sha1(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return new String(Hex.encodeHex(md.digest(str.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
