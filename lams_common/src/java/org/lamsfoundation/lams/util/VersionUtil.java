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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.util;

/**
 * General utility functions relating to parsing version strings.
 *
 */
public class VersionUtil {

    /**
     * Extract the three possible parts of the ServerVersionNumber.
     * 
     * @return Long[4]
     */
    public static Long[] extractSystemVersionParts() throws NumberFormatException {
	return VersionUtil.extractVersionParts(Configuration.get(ConfigurationKeys.SERVER_VERSION_NUMBER));
    }

    /**
     * Extract the three possible parts of a version number. Should only be applied to data in the format
     * nn.nn.nn.nnnnnn,
     * such as the ServerVersionNumber value.
     * 
     * @return Long[4]
     */
    public static Long[] extractVersionParts(String versionString) throws NumberFormatException {
	Long versionParts[] = new Long[4];
	if (versionString != null) {
	    String split[] = versionString.split("[\\.]");
	    if (split.length > 0) {
		versionParts[0] = Long.parseLong(split[0]);
		if (split.length > 1) {
		    versionParts[1] = Long.parseLong(split[1]);
		    if (split.length > 2) {
			versionParts[2] = Long.parseLong(split[2]);
			if (split.length > 3) {
			    versionParts[3] = Long.parseLong(split[3]);
			}
		    }
		}
	    }
	}
	return versionParts;
    }

    public static boolean isSameOrLaterVersion(String versionOneString, String versionTwoString,
	    boolean compareOnlyFirstPart) throws NumberFormatException {
	Long[] versionTwo = VersionUtil.extractVersionParts(versionTwoString);
	Long[] versionOne = VersionUtil.extractVersionParts(versionOneString);
	int compareRes = VersionUtil.checkVersionPart(versionTwo[0], versionOne[0]);
	if (compareRes < 0) {
	    return false;
	}
	if (compareRes > 0) {
	    return true;
	}

	compareRes = VersionUtil.checkVersionPart(versionTwo[1], versionOne[1]);
	if (compareRes < 0) {
	    return false;
	}
	if (compareRes > 0) {
	    return true;
	}

	compareRes = VersionUtil.checkVersionPart(versionTwo[2], versionOne[2]);
	if (compareRes < 0) {
	    return false;
	}
	if (compareRes > 0) {
	    return true;
	}

	return (compareOnlyFirstPart || VersionUtil.checkVersionPart(versionTwo[3], versionOne[3]) >= 0);
    }

    /**
     * Is the supplied version string the same as the current version? The comparison is done to the internal
     * server version, not the version displayed on the login screen. Splits the version into its three component part
     * for comparison.
     * 
     * @param versionString
     *            String to be compared to the current Server version.
     * @param compareOnlyFirstPart
     *            Set to true to only compare the Major and Minor version numbers (e.g. 2.0.4), set to false to compare
     *            the date part.
     */
    public static boolean isSameOrLaterVersionAsServer(String versionString, boolean compareOnlyFirstPart)
	    throws NumberFormatException {
	String serverVersion = Configuration.get(ConfigurationKeys.SERVER_VERSION_NUMBER);
	return VersionUtil.isSameOrLaterVersion(versionString, serverVersion, compareOnlyFirstPart);
    }

    private static int checkVersionPart(Long version1, Long version2) {
	if (version1 == null && version2 == null) {
	    return 0;
	}
	if (version1 != null && version2 == null) {
	    return 1;
	}
	return version1.compareTo(version2);
    }

}
