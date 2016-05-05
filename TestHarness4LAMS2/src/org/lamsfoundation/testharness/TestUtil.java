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
package org.lamsfoundation.testharness;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * @author Fei Yang, Marcin Cieslak
 */
public class TestUtil {
    private static final Logger log = Logger.getLogger(TestUtil.class);
    protected static final char NAME_SEPERATOR = '_';

    private static String machineName;

    public static String buildName(String testName, String simpleName) {
	return TestUtil.getMachineName() + TestUtil.NAME_SEPERATOR + testName + TestUtil.NAME_SEPERATOR + simpleName;
    }

    public static String buildName(String testName, String simpleName, int maxLength) {
	return TestUtil.truncate(TestUtil.buildName(TestUtil.truncate(testName, 1, true), simpleName), maxLength, true);
    }

    public static int generateRandomNumber(int length) {
	return new Random().nextInt(length);
    }

    public static String getMachineName() {
	try {
	    if (TestUtil.machineName == null) {
		return TestUtil.machineName = InetAddress.getLocalHost().getHostName();
	    } else {
		return TestUtil.machineName;
	    }
	} catch (UnknownHostException e) {
	    return "UnknownHost";
	}
    }

    private static String truncate(String name, int length, boolean leftToRight) {
	if (name.length() <= length) {
	    return name;
	}
	if (leftToRight) {
	    return name.substring(name.length() - length);
	} else {
	    return name.substring(0, length);
	}
    }
}