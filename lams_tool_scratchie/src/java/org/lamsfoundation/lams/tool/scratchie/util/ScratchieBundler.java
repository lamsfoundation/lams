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

/* $Id$ */
package org.lamsfoundation.lams.tool.scratchie.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learning.export.web.action.Bundler;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;

public class ScratchieBundler extends Bundler {

    private static final char URL_SEPARATOR = '/';

    public ScratchieBundler() {
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
	List<String> directories = new ArrayList<String>();
	directories.add(outputDirectory + File.separator + "includes");
	this.createDirectories(directories);

	// String[] imageNames = new String[] { "scratchie-correct.gif", "scratchie-wrong.gif", "scratchie.gif" };
	//
	// for (String imageName : imageNames) {
	// String urlToConnectTo = getIncludesFolder() + "images" + URL_SEPARATOR + "jquery-ui-redmond-theme" +
	// URL_SEPARATOR + imageName;
	// String directoryToStoreFile = outputDirectory + File. separator + "javascript" + File.separator + "images" +
	// File.separator + "jquery-ui-redmond-theme";
	// HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, imageName, cookies);//
	// cookies aren't really needed here.
	// }
	final String FOLDER_TO_STORE_FILE = outputDirectory + File.separator + "includes";

	final String JAVASCRIPT_FOLDER = getServerUrl() + "includes" + URL_SEPARATOR + "javascript" + URL_SEPARATOR;
	final String CSS_FOLDER = getServerUrl() + "css" + URL_SEPARATOR;
	final String IMAGE_FOLDER = getServerUrl() + "tool" + URL_SEPARATOR + ScratchieConstants.TOOL_SIGNATURE
		+ URL_SEPARATOR + "includes" + URL_SEPARATOR + "images" + URL_SEPARATOR;

	String urlToConnectTo = JAVASCRIPT_FOLDER + "jquery.jqGrid.min.js";
	HttpUrlConnectionUtil
		.writeResponseToFile(urlToConnectTo, FOLDER_TO_STORE_FILE, "jquery.jqGrid.min.js", cookies);
	urlToConnectTo = JAVASCRIPT_FOLDER + "jquery.jqGrid.locale-en.js";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, FOLDER_TO_STORE_FILE, "jquery.jqGrid.locale-en.js",
		cookies);
	urlToConnectTo = JAVASCRIPT_FOLDER + "jquery-1.7.1.min.js";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, FOLDER_TO_STORE_FILE, "jquery-1.7.1.min.js", cookies);
	urlToConnectTo = JAVASCRIPT_FOLDER + "jquery-ui-1.8.11.custom.min.js";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, FOLDER_TO_STORE_FILE,
		"jquery-ui-1.8.11.custom.min.js", cookies);

	urlToConnectTo = CSS_FOLDER + "jquery-ui-1.8.11.redmont-theme.css";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, FOLDER_TO_STORE_FILE,
		"jquery-ui-1.8.11.redmont-theme.css", cookies);
	urlToConnectTo = CSS_FOLDER + "jquery.jqGrid-4.1.2.css";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, FOLDER_TO_STORE_FILE, "jquery.jqGrid-4.1.2.css",
		cookies);

	urlToConnectTo = IMAGE_FOLDER + "scratchie-correct.gif";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, FOLDER_TO_STORE_FILE, "scratchie-correct.gif",
		cookies);
	urlToConnectTo = IMAGE_FOLDER + "scratchie-wrong.gif";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, FOLDER_TO_STORE_FILE, "scratchie-wrong.gif", cookies);
	urlToConnectTo = IMAGE_FOLDER + "scratchie.gif";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, FOLDER_TO_STORE_FILE, "scratchie.gif", cookies);
    }

    private String getServerUrl() {
	String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
	if (serverUrl == null) {
	    log.error("Unable to get path to the LAMS Spreadsheet URL from the configuration table. Spreadsheet javascript files export failed");
	    return "";
	} else {

	    if (!serverUrl.endsWith("/")) {
		serverUrl += "/";
	    }
	    return serverUrl;
	}
    }

}
