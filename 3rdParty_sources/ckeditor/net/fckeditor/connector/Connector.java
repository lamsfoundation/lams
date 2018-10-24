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
package net.fckeditor.connector;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import net.fckeditor.connector.exception.FolderAlreadyExistsException;
import net.fckeditor.connector.exception.InvalidCurrentFolderException;
import net.fckeditor.connector.exception.InvalidNewFolderNameException;
import net.fckeditor.connector.exception.ReadException;
import net.fckeditor.connector.exception.WriteException;
import net.fckeditor.connector.impl.AbstractLocalFileSystemConnector;
import net.fckeditor.handlers.RequestCycleHandler;
import net.fckeditor.handlers.ResourceType;
import net.fckeditor.requestcycle.ThreadLocalData;

/**
 * Backend interface of a File Browser connector. A connector serves and manages
 * files and folders accessed through the File Browser on an arbitrary backend
 * system.<br />
 * The connector will receive a request if, and only if, the request was valid
 * in terms of valid and reasonable parameters up to an abstract point which is
 * independent from a specific connector implementation.
 * <p>
 * Helpful classes and methods:
 * <ol>
 * <li>If you need to access the request instance itself and/or the context
 * parameters sent from the File Browser, take a look at the
 * {@link ThreadLocalData} class.</li>
 * <li>Use
 * {@link RequestCycleHandler#getUserFilesAbsolutePath(javax.servlet.http.HttpServletRequest)
 * RequestCycleHandler.getUserFilesAbsolutePath},
 * {@link AbstractLocalFileSystemConnector#getRealUserFilesAbsolutePath(java.lang.String)
 * AbstractLocalFileSystemConnector.getRealUserFilesAbsolutePath} (if use it) to
 * resolve the real path or simply do it yourself.</li>
 * </ol>
 * </p>
 * 
 *
 */
public interface Connector {

	/** Key 'name' for a file's name */
	public final static String KEY_NAME = "name";
	
	/** Key 'size' for a file's length */
	public final static String KEY_SIZE = "size";

	/**
	 * Initializes this connector. Called at {@link Dispatcher dispatcher}
	 * initialization.
	 * 
	 * @param servletContext
	 *            reference to the {@link ServletContext} in which the caller is
	 *            running
	 * @throws Exception
	 *             if the connector initialization fails due to some reason
	 */
	public void init(final ServletContext servletContext) throws Exception;

	/**
	 * Returns a list of file attributes from the backend. Use the pre-defined
	 * keys ({@value #KEY_NAME}, {@value #KEY_SIZE}) to put file attributes into
	 * the file map. The file length can be any instance of {@link Number}, its
	 * long value will be taken as the final value.
	 * 
	 * @param type
	 *            the current resource type
	 * @param currentFolder
	 *            the current folder
	 * @return a list of file attributes
	 * @throws InvalidCurrentFolderException
	 *             if the current folder name is invalid or does not exist
	 *             within the underlying backend
	 * @throws ReadException
	 *             if the file attributes could not be read due to some reason
	 */
	public List<Map<String, Object>> getFiles(final ResourceType type,
			final String currentFolder) throws InvalidCurrentFolderException,
			ReadException;

	/**
	 * Returns a list of folders from the backend.
	 * 
	 * @param type
	 *            the current resource type
	 * @param currentFolder
	 *            the current folder
	 * @return a list of folder names
	 * @throws InvalidCurrentFolderException
	 *             if the current folder name is invalid or does not exist
	 *             within the underlying backend
	 * @throws ReadException
	 *             if the folder names could not be read due to some reason
	 */
	public List<String> getFolders(final ResourceType type,
			final String currentFolder) throws InvalidCurrentFolderException,
			ReadException;

	/**
	 * Creates a new folder on the backend.
	 * 
	 * @param type
	 *            the current resource type
	 * @param currentFolder
	 *            the current folder
	 * @param newFolder
	 *            name of the new folder
	 * @throws InvalidCurrentFolderException
	 *             if the current folder name is invalid or does not exist
	 *             within the underlying backend
	 * @throws InvalidNewFolderNameException
	 *             if the new folder name is invalid due to some reason
	 * @throws FolderAlreadyExistsException
	 *             if the new folder already exists
	 * @throws WriteException
	 *             if the new folder could not be created due to some reason
	 */
	public void createFolder(final ResourceType type,
			final String currentFolder, final String newFolder)
			throws InvalidCurrentFolderException,
			InvalidNewFolderNameException, FolderAlreadyExistsException,
			WriteException;

	/**
	 * Uploads a new file on to the backend. You are not allowed to overwrite
	 * already existing files, rename the new file and return the new filename.
	 * 
	 * @param type
	 *            the current resource type
	 * @param currentFolder
	 *            the current folder
	 * @param fileName
	 *            the name of the new file
	 * @param inputStream
	 *            input stream of the new file
	 * @return the (eventually renamed) name of the uploaded file
	 * @throws InvalidCurrentFolderException
	 *             if the current folder name is invalid or does not exist
	 *             within the underlying backend
	 * @throws WriteException
	 *             if the new file could not be created due to some reason
	 */
	public String fileUpload(final ResourceType type,
			final String currentFolder, final String fileName,
			final InputStream inputStream)
			throws InvalidCurrentFolderException, WriteException;

}