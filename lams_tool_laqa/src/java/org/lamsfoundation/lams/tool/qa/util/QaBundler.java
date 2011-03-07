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
package org.lamsfoundation.lams.tool.qa.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learning.export.web.action.Bundler;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;

public class QaBundler extends Bundler {
    
    private static final char URL_SEPARATOR = '/';

    public QaBundler() {
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
	String[] directoryNames = { "javascript", "images", "css" };
	List<String> directories = new ArrayList<String>();
	for (String directoryName : directoryNames) {
	    directories.add(outputDirectory + File.separator + directoryName);
	}
	this.createDirectories(directories);

	String[] imageNames = new String[] { "bg_jRatingInfos.png", "small.png", "stars.png" };
	for (String imageName : imageNames) {
	    String urlToConnectTo = getToolFolder() + "images" + URL_SEPARATOR + imageName;
	    String directoryToStoreFile = outputDirectory + File. separator + "images";
	    HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, imageName, cookies);// cookies aren't really needed here.
	}
	
	final String includesFolder = getToolFolder() + "includes" + URL_SEPARATOR;
	
	// JS files
	String urlToConnectTo = includesFolder + "javascript" + URL_SEPARATOR + "jquery.js";
	String directoryToStoreFile = outputDirectory + File.separator + "javascript";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery.js", cookies); 
	urlToConnectTo = includesFolder + "javascript" + URL_SEPARATOR + "jRating.jquery.js";
	directoryToStoreFile = outputDirectory + File.separator + "javascript";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jRating.jquery.js", cookies); 

	//CSS files
	urlToConnectTo = includesFolder + "css" + URL_SEPARATOR + "jRating.jquery.css";
	directoryToStoreFile = outputDirectory + File.separator + "css";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jRating.jquery.css", cookies); 
	urlToConnectTo = includesFolder + "css" + URL_SEPARATOR + "ratingStars.css";
	directoryToStoreFile = outputDirectory + File.separator + "css";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "ratingStars.css", cookies); 

    }

    private String getToolFolder() {
	String serverUrlPath = Configuration.get(ConfigurationKeys.SERVER_URL);
	if (serverUrlPath == null) {
	    log.error("Unable to get path to the LAMS Server URL from the configuration table. Q&A javascript files export failed");
	    return "";
	} else {
	    if (!serverUrlPath.endsWith("/")) {
		serverUrlPath += "/";
	    }

	    return serverUrlPath + "tool" + URL_SEPARATOR + QaAppConstants.MY_SIGNATURE + URL_SEPARATOR;
	}
    }

}
