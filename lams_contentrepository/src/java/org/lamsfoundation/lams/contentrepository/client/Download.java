/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.contentrepository.client;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.*;
import org.lamsfoundation.lams.contentrepository.exception.FileException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.exception.ValueFormatException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is a specialised servlet that supports the downloading of single files and the rendering of packages.
 * <p>
 * It has a rather odd format - you can call it initially with the file/package ID (and optional version and
 * preferDownload parameters) using <BR>
 * download?uuid=&lt;uuid&gt;&version=&lt;version&gt;&preferDownload=[true|false].
 * <p>
 * If it is a file, then the file is downloaded. If it is a package, then it redirects to
 * download/&lt;uuid&gt;/&lt;version&gt;/relPath?preferDownload=false where the &lt;uuid&gt; and &lt;version&gt; are the
 * uuid and version of the package node.
 * <p>
 * The download/&lt;uuid&gt;/&lt;version&gt;/relPath should only be used internally - the servlet should be called with
 * the parameter version initially.
 * <p>
 * This / format allows the relative pathed links within an html file to work properly.
 * <p>
 * If you want to try to download the file rather than display the file, add the parameter preferDownload=true to the
 * url. This is only meaningful for a file - it is ignored for packages.
 * <p>
 * This is an abstract class, to allow other modules to customise the repository access. To implement, you must
 * implement getTicket() and getRepositoryService(). If you are using ToolContentHandler, then you can use ToolDownload,
 * which is a concrete implementation of this class using the ToolContentHandler.
 *
 * @author Fiona Malikoff
 * @see org.lamsfoundation.lams.contentrepository.client.ToolDownload
 */

/*
 * A package node could be handled by either getting the stream from the package node - this is the first file in the
 * package - or by using the property in the node that specifies the path to the first file and go back to the
 * repository and get that node. In a roundabout way, this servlet uses the second method - it redirects to the path for
 * the first file.
 *
 * method 1: the package node returns a stream which is the first file. InputStream = node.getFile(); set up any header
 * variables <set up any header variables>
 *
 * method 2: get initial path node and download that IValue value = node.getProperty(PropertyName.INITIALPATH); String
 * initialPath = value != null ? value.getString() : null; IVersionedNode childNode =
 * getRepository().getFileItem(ticket,uuid, initialPath, null); InputStream = node.getFile(); <set up any header
 * variables> <copy input stream to page output stream>
 */

public abstract class Download extends HttpServlet {

    public static final String UUID_NAME = "uuid";
    public static final String VERSION_NAME = "version";
    public static final String PREFER_DOWNLOAD = "preferDownload";
    protected static Logger log = Logger.getLogger(Download.class);

    private static final String expectedFormat =
	    "Expected format /download?" + Download.UUID_NAME + "<num>&" + Download.VERSION_NAME + "=<num>"
		    + Download.PREFER_DOWNLOAD
		    + "=<true|false> (version number optional) or /download/<uuid>/<version>/<relPath>";

    /**
     * Get the ticket that may be used to access the repository.
     */
    public abstract ITicket getTicket() throws RepositoryCheckedException;

    public abstract ITicket getTicket(String source) throws RepositoryCheckedException;

    public abstract IRepositoryService getRepositoryService() throws RepositoryCheckedException;

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request
     * 	the request send by the client to the server
     * @param response
     * 	the response send by the server to the client
     * @throws ServletException
     * 	if an error occurred
     * @throws IOException
     * 	if an error occurred
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	try {
	    handleCall(request, response);
	} catch (RepositoryCheckedException e) {
	    Download.log.error("Exception occured in download. Exception " + e.getMessage() + "Request URL was "
		    + request.getRequestURL(), e);
	    throw new ServletException(e);
	}
    }

    private void handleCall(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException, RepositoryCheckedException {

	ITicket ticket = null;
	String toolContentHandlerName = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_HANDLER_NAME);
	if (StringUtils.isBlank(toolContentHandlerName)) {
	    ticket = getTicket();
	} else {
	    ticket = getTicket(toolContentHandlerName);
	}

	if (ticket == null) {
	    throw new RepositoryCheckedException("Unable to get ticket - getTicket(false) returned null");
	}

	String uuid = request.getParameter(Download.UUID_NAME);
	Long version = null;
	boolean saveFile = Download.getBoolean(request.getParameter(Download.PREFER_DOWNLOAD));

	if (uuid != null) {

	    version = Download.getLong(request.getParameter(Download.VERSION_NAME));
	    // check if it is plain numeric UUID or complex portrait UUID
	    boolean isSimpleUid = !uuid.contains("-");
	    if (isSimpleUid) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN,
			"Downloading files using simple UIDs has been discontinued for security reasons");
		return;
	    }
	    IVersionedNode node = getRepositoryService().getFileItem(ticket, uuid, version);

	    // update versionId in case it was null and we got the latest version...
	    version = node.getVersion();

	    if (!saveFile && node.isNodeType(NodeType.PACKAGENODE)) {

		// now get the path of the initial page in the package
		IValue value = node.getProperty(PropertyName.INITIALPATH);
		String initialPage = value != null ? value.getString() : null;
		if (initialPage == null || initialPage.length() == 0) {
		    throw new RepositoryCheckedException(
			    "No initial page found for this set of content. Node Data is " + node.toString());
		}

		// redirect to the initial path
		// prepend with servlet and id - initial call doesn't include the id
		// and depending on "/"s, the servlet name is sometimes lost by the redirect.
		// make sure it displays the file - rather than trying to download it.
		initialPage =
			WebUtil.getBaseServerURL() + request.getRequestURI() + uuid + "/" + version + "/" + initialPage
				+ "?preferDownload=false";
		Download.log.debug("Attempting to redirect to initial page " + initialPage);
		response.sendRedirect(initialPage);

	    } else if (saveFile || node.isNodeType(NodeType.FILENODE)) {

		handleFileNode(response, request, node, saveFile);

	    } else {
		throw new RepositoryCheckedException(
			"Unsupported node type " + node.getNodeType() + ". Node Data is " + node.toString(), null);
	    }

	} else {

	    // using the /download/<id>/<filename> format - must be a file node!
	    String pathString = request.getPathInfo();
	    String[] strings = deriveIdFile(pathString);
	    uuid = strings[0];
	    version = Download.getLong(strings[1]);
	    String relPathString = strings[2];

	    if (uuid == null) {
		throw new RepositoryCheckedException("UUID value is missing. " + Download.expectedFormat);
	    }

	    if (version == null) {
		throw new RepositoryCheckedException("Version value is missing. " + Download.expectedFormat);
	    }

	    if (relPathString == null) {
		throw new RepositoryCheckedException("Filename is missing. " + Download.expectedFormat);
	    }

	    IVersionedNode node = getRepositoryService().getFileItem(ticket, uuid, version, relPathString);
	    if (!node.isNodeType(NodeType.FILENODE)) {
		throw new RepositoryCheckedException(
			"Unexpected type of node " + node.getNodeType() + " Expected File node. Data is " + node);
	    }
	    handleFileNode(response, request, node, saveFile);
	}
    }

    /**
     * @param response
     * @param aNode
     * @throws IOException
     */
    protected void handleFileNode(HttpServletResponse response, HttpServletRequest request, IVersionedNode fileNode,
	    boolean saveFile) throws IOException, FileException, ValueFormatException {

	// Try to get the mime type and set the response content type.
	// Use the mime type was saved with the file,
	// the type set up in the server (e.g. tomcat's config) or failing
	// that, make it a octet stream.
	IValue prop = fileNode.getProperty(PropertyName.MIMETYPE);
	String mimeType = prop != null ? prop.getString() : null;
	if (mimeType == null) {
	    prop = fileNode.getProperty(PropertyName.FILENAME);
	    if (prop != null) {
		mimeType = getServletContext().getMimeType(prop.getString());
	    }
	}
	if (mimeType == null) {
	    mimeType = "application/octet-stream";
	}
	response.setContentType(mimeType + "; charset=UTF-8");

	// Get the filename stored with the file
	prop = fileNode.getProperty(PropertyName.FILENAME);
	String filename = prop != null ? prop.getString() : null;
	filename = FileUtil.encodeFilenameForDownload(request, filename);

	Download.log.debug("Downloading file " + filename + " mime type " + mimeType);

	if (saveFile) {
	    Download.log.debug("Sending as attachment");
	    response.setHeader("Content-Disposition", "attachment;filename=" + filename);
	} else {
	    Download.log.debug("Sending as inline");
	    response.setHeader("Content-Disposition", "inline;filename=" + filename);
	}
	response.setHeader("Cache-control", "public, max-age=43200");
	if (filename != null) {
	    response.addHeader("Content-Description", filename);
	}

	InputStream in = new BufferedInputStream(fileNode.getFile());
	OutputStream out = response.getOutputStream();
	try {
	    int count = 0;

	    int ch;
	    while ((ch = in.read()) != -1) {
		out.write((char) ch);
		count++;
	    }
	    Download.log.debug("Wrote out " + count + " bytes");
	    response.setContentLength(count);
	    out.flush();
	} catch (IOException e) {
	    Download.log.error("Exception occured writing out file:" + e.getMessage());
	    throw e;
	} finally {
	    try {
		if (in != null) {
		    in.close(); // very important
		}
		if (out != null) {
		    out.close();
		}
	    } catch (IOException e) {
		Download.log.error("Error Closing file. File already written out - no exception being thrown.", e);
	    }
	}
    }

    // Expect format /<id>/<version>/<relPath>. No parts are optional. Filename may be a path.
    // returns an array of three strings.
    private String[] deriveIdFile(String pathInfo) {
	String[] result = new String[3];

	if (pathInfo != null) {

	    String[] strings = pathInfo.split("/", 4);

	    for (int i = 0, j = 0; i < strings.length && j < 3; i++) {
		// splitting sometimes results in empty strings, so skip them!
		if (strings[i].length() > 0) {
		    result[j++] = strings[i];
		}
	    }

	}
	Download.log.debug("Split path into following strings: '" + result[0] + "' '" + result[1] + "' '" + result[2]);

	return result;
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to post.
     *
     * @param request
     * 	the request send by the client to the server
     * @param response
     * 	the response send by the server to the client
     * @throws ServletException
     * 	if an error occurred
     * @throws IOException
     * 	if an error occurred
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

    protected static Long getLong(String longAsString) {
	try {
	    return new Long(longAsString);
	} catch (NumberFormatException e) {
	    return null;
	}
    }

    protected static boolean getBoolean(String booleanAsString) {
	return Boolean.valueOf(booleanAsString).booleanValue();
    }
}