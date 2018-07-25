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
package net.fckeditor.response;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.fckeditor.connector.Connector;
import net.fckeditor.handlers.Command;
import net.fckeditor.handlers.ResourceType;
import net.fckeditor.localization.LocalizedMessages;
import net.fckeditor.requestcycle.ThreadLocalData;
import net.fckeditor.tool.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents the <a href="http://docs.fckeditor.net/FCKeditor_2.x/Developers_Guide/Server_Side_Integration#The_Commands"
 * target="_blank">XML response</a> for the File Browser <code>GET</code>
 * requests.
 * 
 *
 */
public class GetResponse {

	/** Underlying DOM document */
	protected Document document;
	/** Error element, in case of an invalid request */
	protected Element errorElement;
	/** Folders element, in case of a <code>GetResources</code> request */
	protected Element foldersElement;
	/** Files element, in case of a <code>GetResources</code> request */
	protected Element filesElement;

	/** Error number OK */
	public static final int EN_OK = 0;

	/** Error number CUSTOM ERROR */
	public static final int EN_CUSTOM_ERROR = 1;

	/** Error number FOLDER ALREADY EXISTS */
	public static final int EN_FOLDER_ALREADY_EXISTS_ERROR = 101;

	/** Error number INVALID NEW FOLDER NAME */
	public static final int EN_INVALID_NEW_FOLDER_NAME_ERROR = 102;

	/** Error number SECURITY ERROR */
	public static final int EN_CREATE_FOLDER_SECURITY_ERROR = 103;

	/** Error number UNKNOWN ERROR */
	public static final int EN_UKNOWN_CREATE_FOLDER_ERROR = 110;

	/**
	 * Constructs a response with a specific error number and message.
	 * 
	 * @param number
	 *            the error number of the new get response
	 * @param message
	 *            the specific message of the new get response
	 * @throws RuntimeException
	 *             if creation of the underlying DOM document failed
	 */
	public GetResponse(int number, String message) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}

		Element root = document.createElement("Connector");
		document.appendChild(root);
		setError(number, message);
	}

	/**
	 * Constructs a successful response for a specific command and resource
	 * type.
	 * 
	 * @param command
	 *            the current command of the new get response
	 * @param type
	 *            the current resource type of the new get response
	 * @param currentFolder
	 *            the current folder of the new get response
	 * @param constructedUrl
	 *            the final URL of the new get response
	 * @throws RuntimeException
	 *             if creation of the underlying DOM document failed
	 */
	public GetResponse(Command command, ResourceType type,
			String currentFolder, String constructedUrl) {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}

		Element root = document.createElement("Connector");
		document.appendChild(root);
		root.setAttribute("command", command.getName());
		root.setAttribute("resourceType", type.getName());

		Element currentFolderElement = document.createElement("CurrentFolder");
		currentFolderElement.setAttribute("path", currentFolder);

		currentFolderElement.setAttribute("url", constructedUrl);
		root.appendChild(currentFolderElement);

	}

	/**
	 * Constructs a response with a specific error number only.
	 * 
	 * @param number
	 *            the error number of the new get response
	 * @throws RuntimeException
	 *             if creation of the underlying DOM document failed
	 */
	public GetResponse(int number) {
		this(number, null);
	}

	/**
	 * Sets the error number and specific message of this get response.
	 * 
	 * @param number
	 *            the error number of this get response
	 * @param message
	 *            the specific message of this get response
	 */
	public void setError(int number, String message) {

		if (errorElement == null) {
			errorElement = document.createElement("Error");
			document.getDocumentElement().appendChild(errorElement);
		}

		errorElement.setAttribute("number", String.valueOf(number));
		if (Utils.isNotEmpty(message))
			errorElement.setAttribute("text", message);

	}

	/**
	 * Sets the error number of this get response.
	 * 
	 * @param number
	 *            the error number of this get response
	 */
	public void setError(int number) {
		setError(number, null);
	}

	/**
	 * Sets the folders of this get response.
	 * 
	 * @see Connector#getFolders(ResourceType, String)
	 * @param folders
	 *            the folders of this get response
	 */
	public void setFolders(final List<String> folders) {
		if (foldersElement != null) {
			Element parent = (Element) foldersElement.getParentNode();
			parent.removeChild(foldersElement);
		}

		foldersElement = document.createElement("Folders");
		document.getDocumentElement().appendChild(foldersElement);

		for (String folder : folders) {
			Element folderElement = document.createElement("Folder");
			folderElement.setAttribute("name", folder);
			foldersElement.appendChild(folderElement);
		}
	}

	/**
	 * Sets the folders of this get response.
	 * 
	 * @see Connector#getFiles(ResourceType, String)
	 * @param files
	 *            the files of this get response
	 */
	public void setFiles(final List<Map<String, Object>> files) {
		if (filesElement != null) {
			Element parent = (Element) filesElement.getParentNode();
			parent.removeChild(filesElement);
		}

		filesElement = document.createElement("Files");
		document.getDocumentElement().appendChild(filesElement);

		long length = 1L;
		long tempLength;

		for (Map<String, Object> file : files) {
			Element fileElement = document.createElement("File");
			fileElement.setAttribute("name", (String) file
					.get(Connector.KEY_NAME));
			tempLength = ((Number) file.get(Connector.KEY_SIZE)).longValue();
			if (tempLength > 1024L)
				length = tempLength / 1024L;
			fileElement.setAttribute("size", String.valueOf(length));
			filesElement.appendChild(fileElement);
		}
	}

	/**
	 * Creates the XML representation of this get response. The underlying DOM
	 * document will transformed to a string.
	 * 
	 * @throws RuntimeException
	 *             if creation failed
	 * @return XML representation of this get response
	 */
	@Override
	public String toString() {
		document.getDocumentElement().normalize();
		TransformerFactory factory = TransformerFactory.newInstance();

		StringWriter sw = new StringWriter();

		try {
			Transformer transformer = factory.newTransformer();

			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(sw);

			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}

		return sw.toString();
	}

	/** Creates an <code>OK</code> response. */
	public static GetResponse getOK() {
		return new GetResponse(EN_OK);
	}

	/** Creates an <code>INVALID COMMAND</code> error. */
	public static GetResponse getInvalidCommandError() {
		LocalizedMessages lm = LocalizedMessages.getInstance(ThreadLocalData
				.getRequest());
		return new GetResponse(EN_CUSTOM_ERROR, lm.getInvalidCommandSpecified());
	}

	/** Creates an <code>INVALID RESOURCE TYPE</code> error. */
	public static GetResponse getInvalidResourceTypeError() {
		LocalizedMessages lm = LocalizedMessages.getInstance(ThreadLocalData
				.getRequest());
		return new GetResponse(EN_CUSTOM_ERROR, lm
				.getInvalidResouceTypeSpecified());
	}

	/** Creates an <code>INVALID CURRENT FOLDER</code> error. */
	public static GetResponse getInvalidCurrentFolderError() {
		LocalizedMessages lm = LocalizedMessages.getInstance(ThreadLocalData
				.getRequest());
		return new GetResponse(EN_CUSTOM_ERROR, lm
				.getInvalidCurrentFolderSpecified());
	}

	/** Creates a <code>GET RESOURCES DISABLED</code> error. */
	public static GetResponse getGetResourcesDisabledError() {
		LocalizedMessages lm = LocalizedMessages.getInstance(ThreadLocalData
				.getRequest());
		return new GetResponse(EN_CUSTOM_ERROR, lm.getGetResourcesDisabled());
	}

	/** Creates a <code>GET RESOURCES READ</code> error. */
	public static GetResponse getGetResourcesReadError() {
		LocalizedMessages lm = LocalizedMessages.getInstance(ThreadLocalData
				.getRequest());
		return new GetResponse(EN_CUSTOM_ERROR, lm.getGetResourcesReadError());
	}

	/** Creates a <code>CREATE FOLDER DISABLED</code> error. */
	public static GetResponse getCreateFolderDisabledError() {
		LocalizedMessages lm = LocalizedMessages.getInstance(ThreadLocalData
				.getRequest());
		return new GetResponse(EN_CUSTOM_ERROR, lm.getCreateFolderDisabled());
	}

	/** Creates an <code>INVALID NEW FOLDER NAME</code> error. */
	public static GetResponse getInvalidNewFolderNameError() {
		LocalizedMessages lm = LocalizedMessages.getInstance(ThreadLocalData
				.getRequest());
		return new GetResponse(EN_INVALID_NEW_FOLDER_NAME_ERROR, lm
				.getInvalidNewFolderNameSpecified());
	}

	/** Creates a <code>FOLDER ALREADY EXISTS</code> error. */
	public static GetResponse getFolderAlreadyExistsError() {
		LocalizedMessages lm = LocalizedMessages.getInstance(ThreadLocalData
				.getRequest());
		return new GetResponse(EN_FOLDER_ALREADY_EXISTS_ERROR, lm
				.getFolderAlreadyExistsError());
	}

	/** Creates a <code>CREATE FOLDER WRITE</code> error. */
	public static GetResponse getCreateFolderWriteError() {
		LocalizedMessages lm = LocalizedMessages.getInstance(ThreadLocalData
				.getRequest());
		return new GetResponse(EN_UKNOWN_CREATE_FOLDER_ERROR, lm
				.getCreateFolderWriteError());
	}
}