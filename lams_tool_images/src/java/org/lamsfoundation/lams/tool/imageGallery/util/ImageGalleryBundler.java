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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.imageGallery.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learning.export.web.action.Bundler;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;

public class ImageGalleryBundler extends Bundler {

    private static final char URL_SEPARATOR = '/';

    public ImageGalleryBundler() {
    }

    /**
     * This method bundles the files to the given output dir
     *
     * @param request
     *            the request for the export
     * @param cookies
     *            cookies for the request
     * @param outputDirectory
     *            the location where the files should be written
     * @throws Exception
     */
    public void bundle(HttpServletRequest request, Cookie[] cookies, String outputDirectory) throws Exception {
	bundleViaHTTP(request, cookies, outputDirectory);
    }

    /**
     * See bundle
     *
     * @param request
     * @param cookies
     * @param outputDirectory
     * @throws MalformedURLException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void bundleViaHTTP(HttpServletRequest request, Cookie[] cookies, String outputDirectory)
	    throws MalformedURLException, FileNotFoundException, IOException {

	String[] directoriesNames = { "css", "javascript", "images" };

	List<String> directories = new ArrayList<String>();
	for (String directoryName : directoriesNames) {
	    directories.add(outputDirectory + File.separator + directoryName);
	}
	this.createDirectories(directories);

	String urlToConnectTo = getImagesUrlDir() + "javascript" + URL_SEPARATOR + "jquery.rating.1.1.js";
	String directoryToStoreFile = outputDirectory + File.separator + "javascript";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery.rating.1.1.js",
		cookies);
	log.debug("Copying image from source: " + urlToConnectTo + "to desitnation: " + directoryToStoreFile);

	urlToConnectTo = getServerUrl() + "includes" + URL_SEPARATOR + "javascript" + URL_SEPARATOR + "jquery.js";
	directoryToStoreFile = outputDirectory + File.separator + "javascript";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery.js", cookies);
	log.debug("Copying image from source: " + urlToConnectTo + "to desitnation: " + directoryToStoreFile);

	urlToConnectTo = getImagesUrlDir() + "css" + URL_SEPARATOR + "jquery.rating.css";
	directoryToStoreFile = outputDirectory + File.separator + "css";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery.rating.css", cookies);
	log.debug("Copying image from source: " + urlToConnectTo + "to desitnation: " + directoryToStoreFile);

	urlToConnectTo = getImagesUrlDir() + "images" + URL_SEPARATOR + "star.gif";
	directoryToStoreFile = outputDirectory + File.separator + "images";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "star.gif", cookies);
	log.debug("Copying image from source: " + urlToConnectTo + "to desitnation: " + directoryToStoreFile);

    }

    private String getImagesUrlDir() {
	String toolFolder = getServerUrl() + "tool" + URL_SEPARATOR + ImageGalleryConstants.TOOL_SIGNATURE
		+ URL_SEPARATOR + "includes" + URL_SEPARATOR;
	return toolFolder;
    }

    private String getServerUrl() {
	String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
	if (serverUrl == null) {
	    log.error(
		    "Unable to get path to the LAMS Server URL from the configuration table. ImageGallery javascript files export failed");
	    return "";
	} else {
	    if (!serverUrl.endsWith("/")) {
		serverUrl += "/";
	    }
	}
	return serverUrl;
    }

}
