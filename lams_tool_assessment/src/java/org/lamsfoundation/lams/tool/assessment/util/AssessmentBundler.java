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
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;

public class AssessmentBundler extends Bundler {

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
	String[] directoriesNames = { "jqGrid" + File.separator + "min",
		"images" + File.separator + "jqGrid.basic.theme", "css" };
	List<String> directories = new ArrayList<String>();
	for (String directoryName : directoriesNames) {
	    directories.add(outputDirectory + File.separator + "javascript" + File.separator + directoryName);
	}
	this.createDirectories(directories);

	String[] imageNames = new String[] { "cd_run.gif", "dirty.gif", "down.gif",
		"find.gif", "first.gif", "folder.png", "grid-blue-ft.gif", "grid-blue-hd.gif", "headerbg.gif",
		"headerleft.gif", "headerright.gif", "ico-close.gif", "last.gif", "line3.gif",
		"loading.gif", "minus.gif", "next.gif", "nochild.gif", "off-first.gif", "off-last.gif", "off-next.gif",
		"off-prev.gif", "plus.gif", "prev.gif", "refresh.gif", "resize.gif", "row_add.gif", "row_delete.gif",
		"row_edit.gif", "sort_asc.gif", "sort_desc.gif", "spacer.gif", "tab_close-on.gif", "tree_leaf.gif",
		"tree_minus.gif", "tree_plus.gif", "up.gif" };
	
	for (String imageName : imageNames) {
	    String urlToConnectTo = getIncludesFolder() + "images" + File.separator + "jqGrid.basic.theme" + File.separator + imageName;
	    String directoryToStoreFile = outputDirectory + File.separator + "javascript" + File.separator  + "images" + File.separator + "jqGrid.basic.theme";
	    HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, imageName, cookies);// cookies aren't really needed here.
	}
	
	String urlToConnectTo = getIncludesFolder() + "javascript" + File.separator + "jquery.jqGrid.js";
	String directoryToStoreFile = outputDirectory + File.separator + "javascript";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery.jqGrid.js", cookies); 

	urlToConnectTo = getIncludesFolder() + "javascript" + File.separator + "jquery-1.2.6.pack.js";
	directoryToStoreFile = outputDirectory + File.separator + "javascript";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jquery-1.2.6.pack.js", cookies); 
	
	String[] jsFileNames = new String[] { "grid.locale-en-min.js", "grid.base-min.js", "grid.celledit-min.js", "grid.common-min.js",
		"grid.custom-min.js", "grid.formedit-min.js", "grid.import-min.js", "grid.inlinedit-min.js",
		"grid.postext-min.js", "grid.setcolumns-min.js", "grid.subgrid-min.js", "grid.tbltogrid-min.js",
		"grid.treegrid-min.js", "jquery.fmatter-min.js", "json2-min.js", "JsonXml-min.js" };
	
	for (String jsFileName : jsFileNames) {
	    urlToConnectTo = getIncludesFolder() + "javascript" + File.separator + "jqGrid" + File.separator + "min" + File.separator + jsFileName;
	    directoryToStoreFile = outputDirectory + File.separator + "javascript" + File.separator + "jqGrid" + File.separator + "min";
	    HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, jsFileName, cookies);// cookies aren't really needed here.
	}
	
	urlToConnectTo = getIncludesFolder() + "css" + File.separator + "jqGrid.grid.css";
	directoryToStoreFile = outputDirectory + File.separator + "javascript" + File.separator + "css";
	HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, "jqGrid.grid.css", cookies); 

    }

    private String getIncludesFolder() {
	String spreadsheetUrlPath = Configuration.get(ConfigurationKeys.SERVER_URL);
	if (spreadsheetUrlPath == null) {
	    log.error("Unable to get path to the LAMS Spreadsheet URL from the configuration table. Spreadsheet javascript files export failed");
	    return "";
	} else {
	    spreadsheetUrlPath = spreadsheetUrlPath + File.separator + "tool" + File.separator
		    + AssessmentConstants.TOOL_SIGNATURE + File.separator + "includes" + File.separator;
	    return spreadsheetUrlPath;
	}
    }

}
