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
package net.fckeditor.connector.impl;

import javax.servlet.ServletContext;

import net.fckeditor.requestcycle.impl.ServerRootPathBuilder;

/**
 * Real local filesystem backend connector. The file access is translated as-is
 * to the local filesystem.
 * <p>
 * This maybe interesting for those who serve userfiles
 * <ul>
 * <li>from a static context of a servlet container/application
 * server,</li>
 * <li>from virtual servers with Apache HTTPd on different
 * domains, e.g. <code>http://userfiles.mydomain.com</code>, or</li>
 * <li>on a per-user basis, e.g. uploading to
 * <code>/home/$USERNAME/public_html/fckeditor</code> and an Apache HTTPd serves
 * as <code>/~$USERNAME/fckeditor</code>.</li>
 * </ul>
 * </p>
 * Use the {@link ServerRootPathBuilder} in conjunction with this class.
 * 
 *
 */
public class LocalConnector extends AbstractLocalFileSystemConnector {

	/**
	 * {@inheritDoc} Assigns only the {@code servletContext}.
	 */
	public void init(final ServletContext servletContext) throws Exception {
		this.servletContext = servletContext;
	}

	/**
	 * Returns the passed parameter as-is.
	 */
	@Override
	protected String getRealUserFilesAbsolutePath(String userFilesAbsolutePath) {
		return userFilesAbsolutePath;
	}

}