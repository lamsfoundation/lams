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

import net.fckeditor.handlers.ResourceType;

/**
 * Static helper methods for the servlet response.
 * 
 *
 */
public class UtilsResponse {

	/**
	 * Assembles a URL with omitted filename.
	 * 
	 * @see #fileUrl(String, ResourceType, String, String)
	 */
	public static String getUrl(String userFilesPath, ResourceType type,
			String currentFolder) {
		return fileUrl(userFilesPath, type, currentFolder, null);
	}

	/**
	 * 
	 * Assembles a file URL for the File Browser. Simply appends parameters to a
	 * string buffer with reasonable parameter checking.
	 * 
	 * @param userFilesPath
	 *            the current userfiles path (may be null)
	 * @param type
	 *            the current resource type
	 * @param currentFolder
	 *            the selected current folder
	 * @param filename
	 *            the current chosen file (may be null)
	 * @return assembled url for the File Browser
	 */
	public static String fileUrl(String userFilesPath, ResourceType type,
			String currentFolder, String filename) {

		StringBuffer sb = new StringBuffer();
		if (Utils.isNotEmpty(userFilesPath))
			sb.append(userFilesPath);
		sb.append(type.getPath());
		sb.append(currentFolder);
		if (Utils.isNotEmpty(filename))
			sb.append(filename);

		return sb.toString();
	}

}
