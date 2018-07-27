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

import net.fckeditor.requestcycle.UserAction;

/**
 * Standard implementation for {@link UserAction}. It always returns
 * <code>true</code>.<br/>
 * 
 * @see EnabledUserAction
 *
 * @deprecated Class will be removed in FCKeditor.Java 2.6, functionality now
 *             served by {@link EnabledUserAction}.
 */
@Deprecated
public class UserActionImpl implements UserAction {

	/* (non-Javadoc)
	 * @see net.fckeditor.requestcycle.UserAction#isEnabledForFileBrowsing(javax.servlet.http.HttpServletRequest)
	 */
	public boolean isEnabledForFileBrowsing(final HttpServletRequest request) {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.fckeditor.requestcycle.UserAction#isEnabledForFileUpload(javax.servlet.http.HttpServletRequest)
	 */
	public boolean isEnabledForFileUpload(final HttpServletRequest request) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see net.fckeditor.requestcycle.UserAction#isEnabledForFolderCreation(javax.servlet.http.HttpServletRequest)
	 */
	public boolean isCreateFolderEnabled(final HttpServletRequest request) {
		return true;
	}
}
