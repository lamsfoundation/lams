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
		return extractVersionParts(Configuration.get(ConfigurationKeys.SERVER_VERSION_NUMBER));
	}
	
	/** 
	 * Extract the three possible parts of a version number. Should only be applied to data in the format nn.nn.nn.nnnnnn, 
	 * such as the ServerVersionNumber value.
	 * 
	 * @return Long[4]
	 */
	public static Long[] extractVersionParts(String versionString) throws NumberFormatException{
		Long versionParts[] = new Long[4];
		if ( versionString != null ) {
			String split[] = versionString.split("[\\.]");
			if ( split.length > 0 ) {
				versionParts[0] = Long.parseLong(split[0]);
				if ( split.length > 1 ) {
					versionParts[1] = Long.parseLong(split[1]);
					if ( split.length > 2 ) {
						versionParts[2] = Long.parseLong(split[2]);
						if ( split.length > 3 ) {
							versionParts[3] = Long.parseLong(split[3]);
						}
					}
				}
			}
		}
		return versionParts;
	}

	/**
	 * Is the supplied version string the same as the current version? The comparison is done to the internal
	 * server version, not the version displayed on the login screen. Splits the version into its three component part
	 * for comparison.
	 * 
	 * @param versionString String to be compared to the current Server version.
	 * @param compareOnlyFirstPart Set to true to only compare the Major and Minor version numbers (e.g. 2.0.4), set to false to compare the date part.
	 */
	public static boolean isSameOrLaterVersionAsServer(String versionString, boolean compareOnlyFirstPart) throws NumberFormatException {
		Long[] serverVersion = extractSystemVersionParts();
		Long[] compareVersion = extractVersionParts(versionString);
		return  checkCompare(serverVersion[0],compareVersion[0]) && checkCompare(serverVersion[1],compareVersion[1]) &&
				checkCompare(serverVersion[2],compareVersion[2]) &&
				( compareOnlyFirstPart || checkCompare(serverVersion[3],compareVersion[3]));
	}

	private static boolean checkCompare(Long version1, Long version2) {
		return (version1 == null && version2 == null ) || ( version1 != null && version2 != null && version1.compareTo(version2) >= 0 );
	}

}
