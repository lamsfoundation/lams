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
package net.fckeditor.localization;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * An interface for user locale resolution.
 * <p>
 * <strong>Note:</strong> You are free to implement this interface the way you
 * need it, in other words your return values can be global, regardless of the
 * request, or on a per-request basis.
 * </p> 
 *
 */
public interface LocaleResolver {

	/**
	 * Returns the locale of a given request.
	 * 
	 * @param request
	 *            the current request instance
	 * @return the locale of the request, <code>null</code> if the locale
	 *         couldn't be resolved
	 */
	public Locale resolveLocale(final HttpServletRequest request);

}