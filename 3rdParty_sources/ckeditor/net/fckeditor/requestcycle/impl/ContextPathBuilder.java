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
package net.fckeditor.requestcycle.impl;

import javax.servlet.http.HttpServletRequest;

/**
 * UserPathBuilder implementation which always returns static userfiles paths
 * with prepended context path. In particular it returns the {@code
 * connector.userFiles*Path} properties and prepend the current context path to
 * {@link #getUserFilesPath(HttpServletRequest)} only.
 * 
 *
 */
public class ContextPathBuilder extends ServerRootPathBuilder {

	@Override
	public String getUserFilesPath(final HttpServletRequest request) {
		return request.getContextPath().concat(super.getUserFilesPath(request));
	}

}