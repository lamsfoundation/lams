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
package org.lamsfoundation.lams.tool.assessment.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learning.export.web.action.Bundler;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;

public class AssessmentBundler extends Bundler {
    
    private static final char URL_SEPARATOR = '/';

    public AssessmentBundler() {
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
	String[] directoriesNames = {"images" + File.separator + "jquery-ui-redmond-theme", "css" };
	List<String> directories = new ArrayList<String>();
	for (String directoryName : directoriesNames) {
	    directories.add(outputDirectory + File.separator + "javascript" + File.separator + directoryName);
	}
	this.createDirectories(directories);

	String[] imageNames = new String[] { "ui-bg_flat_0_aaaaaa_40x100.png", "ui-bg_flat_55_fbec88_40x100.png",
		"ui-bg_glass_75_d0e5f5_1x400.png", "ui-bg_glass_85_dfeffc_1x400.png",
		"ui-bg_glass_95_fef1ec_1x400.png", "ui-bg_gloss-wave_55_5c9ccc_500x100.png",
		"ui-bg_inset-hard_100_f5f8f9_1x100.png", "ui-bg_inset-hard_100_fcfdfd_1x100.png",
		"ui-icons_217bc0_256x240.png", "ui-icons_2e83ff_256x240.png", "ui-icons_469bdd_256x240.png",
		"ui-icons_6da8d5_256x240.png", "ui-icons_cd0a0a_256x240.png", "ui-icons_d8e7f3_256x240.png",
		"ui-icons_f9bd01_256x240.png" };
	
	for (String imageName : imageNames) {
	    String urlToConnectTo = getServerUrl() + "images" + URL_SEPARATOR +"css" + URL_SEPARATOR + "jquery-ui-redmond-theme" + URL_SEPARATOR + imageName;
	    String directoryToStoreFile = outputDirectory + File. separator + "javascript" + File.separator  + "images" + File.separator + "jquery-ui-redmond-theme";
	    HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, imageName, cookies);// cookies aren't really needed here.
	}
	
	String urlToConnectTo = getServerUrl() + "includes" + URL_SEPARATOR + "javascript" + URL_SEPARATOR + "jquery.jqGrid.locale-en.js";
	String directoryToStoreFile = outputDirectory + File.separator + "javascript";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery.jqGrid.locale-en.js", cookies); 
	
	urlToConnectTo = getServerUrl() + "includes" + URL_SEPARATOR + "javascript" + URL_SEPARATOR + "jquery.jqGrid.js";
	directoryToStoreFile = outputDirectory + File.separator + "javascript";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery.jqGrid.js", cookies); 

	urlToConnectTo = getServerUrl() +"includes" + URL_SEPARATOR + "javascript" + URL_SEPARATOR + "jquery.js";
	directoryToStoreFile = outputDirectory + File.separator + "javascript";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery.js", cookies); 
	
	urlToConnectTo = getServerUrl() + "css" + URL_SEPARATOR + "jquery-ui-redmond-theme.css";
	directoryToStoreFile = outputDirectory + File.separator + "javascript" + File.separator + "css";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery-ui-redmond-theme.css", cookies); 
	
	urlToConnectTo = getServerUrl() + "css" + URL_SEPARATOR + "jquery.jqGrid.css";
	directoryToStoreFile = outputDirectory + File.separator + "javascript" + File.separator + "css";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery.jqGrid.css", cookies); 

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
