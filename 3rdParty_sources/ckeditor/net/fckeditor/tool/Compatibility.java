/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2004-2010 Frederico Caldeira Knabben
 * 
 * == BEGIN LICENSE ==
 * 
 * Licensed under the terms of any of the following licenses at your
 * choice:
 * 
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 * 
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 * 
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 * 
 * == END LICENSE ==
 */
package net.fckeditor.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.fckeditor.FCKeditor;

/**
 * Checks browser compatibility with the {@link FCKeditor editor}.
 * 
 *
 */
public class Compatibility {

	/**
	 * Checks if a browser's user agent string is compatible with the FCKeditor.
	 * 
	 * @param userAgentString
	 *            the user agent string of a browser
	 * @return <code>true</code> if compatible, else <code>false</code>
	 */
	public static boolean check(final String userAgentString) {
		if (Utils.isEmpty(userAgentString))
			return false;

		String userAgentStr = userAgentString.toLowerCase();

		// IE 5.5+, check special keys like 'Opera' and 'mac', because there are
		// some
		// other browsers, containing 'MSIE' in there agent string!
		if (userAgentStr.indexOf("opera") < 0
				&& userAgentStr.indexOf("mac") < 0
				&& getBrowserVersion(userAgentStr, ".*msie ([\\d]+.[\\d]+).*") >= 5.5f)
			return true;

		// for all gecko based browsers
		if (getBrowserVersion(userAgentStr, ".*rv:([\\d]+.[\\d]+).*") > 1.7f)
			return true;

		// Opera 9.5+
		if (getBrowserVersion(userAgentStr, "opera/([\\d]+.[\\d]+).*") >= 9.5f
				|| getBrowserVersion(userAgentStr, ".*opera ([\\d]+.[\\d]+)") >= 9.5f)
			return true;

		// Safari 3+
		if (getBrowserVersion(userAgentStr, ".*applewebkit/([\\d]+).*") >= 522f)
			return true;

		return false;
	}

	/**
	 * Returns <code>true</code> if a browser is compatible by its request
	 * user-agent header.
	 * 
	 * @see #check(String)
	 * @param request
	 *            current user request instance
	 * @return <code>true</code> if a browser is compatible, else
	 *         <code>false</code>
	 */
	public static boolean isCompatibleBrowser(final HttpServletRequest request) {
		return (request == null) ? false : check(request
				.getHeader("user-agent"));
	}

	/**
	 * Helper method to get the browser version from 'userAgent' with regex. The
	 * first matching group has to be the version number!
	 * 
	 * @param userAgentString
	 *            the user agent string of a browser
	 * @param regex
	 *            the pattern to retrieve the browser version
	 * @return the browser version, or -1f if version can't be determined
	 */
	private static float getBrowserVersion(final String userAgentString,
			final String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(userAgentString);
		if (matcher.matches()) {
			try {
				return Float.parseFloat(matcher.group(1));
			} catch (NumberFormatException e) {
				return -1f;
			}
		}
		return -1f;
	}

}