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
package net.fckeditor.handlers;

import javax.servlet.http.HttpServletRequest;

import net.fckeditor.connector.Connector;
import net.fckeditor.requestcycle.UserAction;
import net.fckeditor.requestcycle.UserPathBuilder;
import net.fckeditor.tool.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the {@link UserAction} and {@link UserPathBuilder} interfaces. <br />
 * This class instantiates the the chosen implementations and acts as a proxy
 * for their methods/access.
 * 
 *
 */
public class RequestCycleHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(RequestCycleHandler.class);
	private static UserAction userAction = null;
	private static UserPathBuilder userPathBuilder = null;

	static {
		// If there are more objects to instantiate in future, we could solve
		// the following by reflection!

		// 1. try to instantiate the UserAction object
		String className = PropertiesLoader.getUserActionImpl();
		if (Utils.isEmpty(className))
			logger.error("Empty UserAction implementation class name provided");
		else {
			try {
				Class<?> clazz = Class.forName(className);
				userAction = (UserAction) clazz.newInstance();
				logger.info("UserAction initialized to {}", className);
			} catch (Throwable e) {
				logger.error("UserAction implementation {} could not be instantiated", className);
				throw new RuntimeException("UserAction implementation " + className + " could not be instantiated", e); //$NON-NLS-1$
			}
		}

		// 2. try to instantiate the UserPathBuilder object
		className = PropertiesLoader.getUserPathBuilderImpl();
		if (Utils.isEmpty(className))
			logger.error("Empty UserPathBuilder implementation class name provided");
		else {
			try {
				Class<?> clazz = Class.forName(className);
				userPathBuilder = (UserPathBuilder) clazz.newInstance();
				logger.info("UserPathBuilder initialized to {}", className);
			} catch (Throwable e) {
				logger.error("UserPathBuilder implementation {} could not be instantiated", className);
				throw new RuntimeException("UserPathBuilder implementation " + className + " could not be instantiated", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Returns <code>true</code> if user is allowed to list resources. The
	 * behavior is specified by the current UserAction instance.
	 * 
	 * @param request
	 *            current user request instance
	 * @return true if user is allowed to list resources, false otherwise
	 * @see UserAction#isEnabledForFileBrowsing(HttpServletRequest)
	 */
	public static boolean isGetResourcesEnabled(final HttpServletRequest request) {
		return userAction.isEnabledForFileBrowsing(request);
	}
	
	/**
	 * Returns <code>true</code> if user is allowed to upload files. The
	 * behavior is specified by the current UserAction instance.
	 * 
	 * @param request
	 *            current user request instance
	 * @return true if user is allowed to upload files, false otherwise
	 * @see UserAction#isEnabledForFileUpload(HttpServletRequest)
	 */
	public static boolean isFileUploadEnabled(HttpServletRequest request) {
		return userAction.isEnabledForFileUpload(request); 
	}

	/**
	 * Returns <code>true</code> if user is allowed to create folders. The
	 * behavior is specified by the current UserAction instance.
	 * 
	 * @param request
	 *            current user request instance
	 * @return true if user is allowed to create folders, false otherwise
	 * @see UserAction#isEnabledForFileBrowsing(HttpServletRequest)
	 */
	public static boolean isCreateFolderEnabled(final HttpServletRequest request) {
		return userAction.isCreateFolderEnabled(request);
	}

	/**
	 * Returns the current userfiles path. The path is specified by the current
	 * UserPathBuilder instance.
	 * 
	 * @param request
	 *            current user request instance
	 * @return current userfiles path
	 * @see UserPathBuilder#getUserFilesPath(HttpServletRequest)
	 */
	public static String getUserFilesPath(final HttpServletRequest request) {
		return userPathBuilder.getUserFilesPath(request);
	}

	/**
	 * Returns the current absolute userfiles path. The path is specified by the
	 * current UserPathBuilder instance. <br />
	 * Note that the path is absolute to the underlying system of the current
	 * {@link Connector} instance.
	 * 
	 * @param request
	 *            current user request instance
	 * @return current absolute userfiles path
	 * @see UserPathBuilder#getUserFilesAbsolutePath(HttpServletRequest)
	 */
	public static String getUserFilesAbsolutePath(final HttpServletRequest request) {
		return userPathBuilder.getUserFilesAbsolutePath(request);
	}
}
