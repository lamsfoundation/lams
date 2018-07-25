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

import net.fckeditor.connector.Dispatcher;
import net.fckeditor.handlers.Command;
import net.fckeditor.handlers.ResourceType;
import net.fckeditor.tool.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maintains base/common request parameters from the File Browser. The gain of
 * this class is to provide abstracted and pre-processed access to common
 * request parameters.
 * <p>
 * In the regular case, you will use an already created instance of this class.
 * </p>
 * 
 *
 */
public class Context {
	// This is just a helper class which has no relevance for the logger
	private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
	
	private String typeStr;
	private String commandStr;
	private String currentFolderStr;
	
	/**
	 * Sole class constructor. Takes in a request instance, processes parameters
	 * and populates private fields which can be accessed through getters.<br />
	 * This class will only be instantiated in {@link ThreadLocalData}.
	 * 
	 * @param request
	 *            current user request instance
	 */
	protected Context(final HttpServletRequest request) {
		commandStr = request.getParameter("Command");
		typeStr = request.getParameter("Type");
		currentFolderStr = request.getParameter("CurrentFolder");
		
		// if this is a QuickUpload request, 'commandStr', 'currentFolderStr'
		// are empty and 'typeStr' maybe empty too
		if (Utils.isEmpty(commandStr) && Utils.isEmpty(currentFolderStr)) {
			commandStr = "QuickUpload";
			currentFolderStr = "/";
			if (Utils.isEmpty(typeStr))
				typeStr = "File";
		}
		
		// checks to meet specs in http://docs.fckeditor.net/FCKeditor_2.x/Developers_Guide/Server_Side_Integration#File_Browser_Requests
		if (currentFolderStr != null && !currentFolderStr.startsWith("/"))
			currentFolderStr = "/".concat(currentFolderStr);
	}
	
	/**
	 * Returns the type parameter of this context.
	 * 
	 * @return the type parameter of this context
	 */
	public String getTypeStr() {
		return typeStr;
	}
	
	/**
	 * Returns a default resource type instance for the type parameter.
	 * 
	 * @see ResourceType#getDefaultResourceType(String)
	 * @return default resource type instance
	 */
	public ResourceType getDefaultResourceType() {
		return ResourceType.getDefaultResourceType(typeStr);
	}

	/**
	 * Returns a resource type instance for the type parameter.
	 * 
	 * @see ResourceType#getResourceType(String)
	 * @return resource type instance
	 */
	public ResourceType getResourceType() {
		return ResourceType.getResourceType(typeStr);
	}

	/**
	 * Returns the command parameter of this context.
	 * 
	 * @return the command parameter of this context
	 */
	public String getCommandStr() {
		return commandStr;
	}

	/**
	 * Returns a command instance for the command parameter.
	 * 
	 * @see Command#getCommand(String)
	 * @return command instance
	 */
	public Command getCommand() {
		return Command.getCommand(commandStr);
	}
	
	/**
	 * Returns the current folder parameter of this context.
	 * 
	 * @return the current folder parameter of this context
	 */
	public String getCurrentFolderStr() {
		return currentFolderStr;
	}
	
	/** Logs base/common request parameters. */
	public void logBaseParameters() {
		logger.debug("Parameter Command: {}", commandStr);
		logger.debug("Parameter Type: {}", typeStr);
		logger.debug("Parameter CurrentFolder: {}", currentFolderStr);
	}

}
