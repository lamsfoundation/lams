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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Hex;

/**
 * Utilities for hashing passwords.
 *
 * @author Fei Yang, Marcin Cieslak
 */
public class HashUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static final int SALT_BYTE_LENGTH = 32;
    public static final int SALT_HEX_LENGTH = HashUtil.SALT_BYTE_LENGTH * 2;
    public static final int SHA1_HEX_LENGTH = 40;
    public static final int SHA256_HEX_LENGTH = 64;

    public static String sha1(String plaintext) {
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA-1");
	    return new String(Hex.encodeHex(md.digest(plaintext.getBytes())));
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

    public static String sha256(String plaintext) {
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    return new String(Hex.encodeHex(md.digest(plaintext.getBytes())));
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

    public static String sha256(String password, String salt) {
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    String plaintext = salt + password;
	    return new String(Hex.encodeHex(md.digest(plaintext.getBytes())));
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

    public static String md5(String plaintext) {
	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    return new String(Hex.encodeHex(md.digest(plaintext.getBytes())));
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

    public static String salt() {
	return HashUtil.salt(HashUtil.SALT_BYTE_LENGTH);
    }

    public static String salt(int length) {
	byte[] salt = new byte[length];
	HashUtil.secureRandom.nextBytes(salt);
	return Hex.encodeHexString(salt);
    }
}