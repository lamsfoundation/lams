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

import org.apache.commons.codec.binary.Hex;

/**
 * @version
 *
 *          <p>
 *          <a href="HashUtil.java.html"><i>View Source</i></a>
 *          </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 *         Created at 16:25:56 on 20/06/2006
 */
public class HashUtil {

    public static String sha1(String plaintext) {
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA1");
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

}
