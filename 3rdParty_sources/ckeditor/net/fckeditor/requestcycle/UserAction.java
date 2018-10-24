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

import net.fckeditor.handlers.Command;

/**
 * An interface for user action control. In particular, this interface gives
 * you fine-grained control over the File Browser {@link Command commands}.
 * <p>
 * <strong>Note:</strong> You are free to implement this interface the way you
 * need it, in other words your return values can be global, regardless of the
 * request, or on a per-request basis.
 * </p>
 * 
 *
 */
public interface UserAction {

	/**
	 * Checks if file upload is enabled/allowed. This method maps to
	 * {@link Command#FILE_UPLOAD FileUpload} and {@link Command#QUICK_UPLOAD
	 * QuickUpload}.<br />
	 * <strong>Note:</strong> This method may be renamed to {@code
	 * isFileUploadEnabled} in future versions.
	 * 
	 * @param request
	 *            current user request instance
	 * @return {@code true} if file upload is enabled/allowed, else {@code
	 *         false}
	 */
	public boolean isEnabledForFileUpload(final HttpServletRequest request);

	/**
	 * Checks if resource retrieval/listing is enabled/allowed. This method maps
	 * to {@link Command#GET_FOLDERS GetFolders} and
	 * {@link Command#GET_FOLDERS_AND_FILES GetFoldersAndFiles}.<br />
	 * <strong>Note:</strong> This method may be renamed to {@code
	 * isGetResourcesEnabled} in future versions.
	 * 
	 * @param request
	 *            current user request instance
	 * @return {@code true} if resource retrieval/listing is enabled/allowed,
	 *         else {@code false}
	 */
	public boolean isEnabledForFileBrowsing(final HttpServletRequest request);

	/**
	 * Checks if folder creation is enabled/allowed. This method maps to
	 * {@link Command#CREATE_FOLDER CreateFolder}.
	 * 
	 * @param request
	 *            current user request instance
	 * @return {@code true} if folder creation is enabled/allowed, else {@code
	 *         false}
	 */
	public boolean isCreateFolderEnabled(final HttpServletRequest request);

}