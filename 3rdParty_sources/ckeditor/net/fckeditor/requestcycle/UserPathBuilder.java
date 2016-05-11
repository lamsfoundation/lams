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
package net.fckeditor.requestcycle;

import javax.servlet.http.HttpServletRequest;

/**
 * An interface for userfiles path construction. In particular, this interface
 * gives you the ability to construct the {@code connector.userFilesPath} and
 * {@code connector.userFilesAbsolutePath} properties dynamically. See <a
 * href="http://java.fckeditor.net/properties.html">configuration</a> for more
 * details.
 * <p>
 * <strong>Note:</strong> You are free to implement this interface the way you
 * need it, in other words your return values can be global, regardless of the
 * request, or on a per-request basis.
 * </p>
 * 
 *
 */
public interface UserPathBuilder {

	/**
	 * Returns the constructed server-side userfiles absolute path. This method
	 * is the dynamic constructor of the {@code connector.userFilesAbsolutePath}
	 * property. A concrete connector implementation will use this value to
	 * resolve the server-side location of resources.
	 * 
	 * @param request
	 *            current user request instance
	 * @return the constructed server-side userfiles absolute path
	 */
	public String getUserFilesAbsolutePath(final HttpServletRequest request);

	/**
	 * Returns the constructed client-side userfiles path. This method is the
	 * dynamic constructor of the {@code connector.userFilesPath} property. A
	 * browser will use this value to resolve the url-side location of resources
	 * on the server.
	 * 
	 * @param request
	 *            current user request instance
	 * @return the constructed client-side userfiles path
	 */
	public String getUserFilesPath(final HttpServletRequest request);
}
