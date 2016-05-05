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
package org.lamsfoundation.lams.tool.spreadsheet.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learning.export.web.action.Bundler;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;

public class SpreadsheetBundler extends Bundler {

    private static final char URL_SEPARATOR = '/';

    public SpreadsheetBundler() {
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

	String[] directoriesNames = { "translations", "tinymce" + File.separator + "langs",
		"tinymce" + File.separator + "utils",
		"tinymce" + File.separator + "plugins" + File.separator + "paste" + File.separator + "css",
		"tinymce" + File.separator + "plugins" + File.separator + "paste" + File.separator + "images",
		"tinymce" + File.separator + "plugins" + File.separator + "paste" + File.separator + "jscripts",
		"tinymce" + File.separator + "plugins" + File.separator + "paste" + File.separator + "langs",
		"tinymce" + File.separator + "themes" + File.separator + "advanced" + File.separator + "css",
		"tinymce" + File.separator + "themes" + File.separator + "advanced" + File.separator + "images",
		"tinymce" + File.separator + "themes" + File.separator + "advanced" + File.separator + "jscripts",
		"tinymce" + File.separator + "themes" + File.separator + "advanced" + File.separator + "langs" };

	List<String> directories = new ArrayList<String>();
	for (String directoryName : directoriesNames) {
	    directories.add(outputDirectory + File.separator + "simple_spreadsheet" + File.separator + directoryName);
	}
	this.createDirectories(directories);

	Map<String, String[]> filesNames = new HashMap<String, String[]>();
	filesNames.put("",
		new String[] { "changelog.txt", "editor.htm", "index_offline.html", "keycodes.html", "LICENSE.txt",
			"manual.html", "print.css", "print_noline.css", "spreadsheet.js", "spreadsheet_exported.html",
			"spreadsheet_offline.html", "styles.css", "styles_noline.css" });
	filesNames.put(URL_SEPARATOR + "translations",
		new String[] { "cz.js", "da.js", "de.js", "en.js", "enUK.js", "es.js", "fr.js", "hr.js", "hu.js",
			"it.js", "nl.js", "pl.js", "ptBR.js", "ru.js", "se.js", "sk.js", "tr.js" });
	filesNames.put(URL_SEPARATOR + "tinymce", new String[] { "blank.htm", "index.html", "license.txt", "test.html",
		"tiny_mce.js", "tiny_mce_popup.js" });
	filesNames.put(URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "langs", new String[] { "en.js" });
	filesNames.put(URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "utils",
		new String[] { "editable_selects.js", "form_utils.js", "mclayer.js", "mctabs.js", "validate.js" });
	filesNames.put(URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "plugins" + URL_SEPARATOR + "paste",
		new String[] { "blank.htm", "editor_plugin.js", "pastetext.htm", "pasteword.htm" });
	filesNames.put(
		URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "plugins" + URL_SEPARATOR + "paste" + URL_SEPARATOR + "css",
		new String[] { "blank.css", "pasteword.css" });
	filesNames.put(URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "plugins" + URL_SEPARATOR + "paste" + URL_SEPARATOR
		+ "images", new String[] { "pastetext.gif", "pasteword.gif", "selectall.gif" });
	filesNames.put(URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "plugins" + URL_SEPARATOR + "paste" + URL_SEPARATOR
		+ "jscripts", new String[] { "pastetext.js", "pasteword.js" });
	filesNames.put(URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "plugins" + URL_SEPARATOR + "paste" + URL_SEPARATOR
		+ "langs", new String[] { "en.js" });
	filesNames.put(URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "themes" + URL_SEPARATOR + "advanced",
		new String[] { "about.htm", "anchor.htm", "charmap.htm", "color_picker.htm", "editor_template.js",
			"image.htm", "link.htm", "source_editor.htm" });
	filesNames.put(URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "themes" + URL_SEPARATOR + "advanced" + URL_SEPARATOR
		+ "css", new String[] { "editor_content.css", "editor_popup.css", "editor_ui.css" });
	filesNames.put(
		URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "themes" + URL_SEPARATOR + "advanced" + URL_SEPARATOR
			+ "images",
		new String[] { "anchor.gif", "anchor_symbol.gif", "backcolor.gif", "bold.gif", "bold_de_se.gif",
			"bold_es.gif", "bold_fr.gif", "bold_ru.gif", "bold_tw.gif", "browse.gif", "bullist.gif",
			"button_menu.gif", "buttons.gif", "cancel_button_bg.gif", "charmap.gif", "cleanup.gif",
			"close.gif", "code.gif", "color.gif", "copy.gif", "custom_1.gif", "cut.gif", "forecolor.gif",
			"help.gif", "hr.gif", "image.gif", "indent.gif", "insert_button_bg.gif", "italic.gif",
			"italic_de_se.gif", "italic_es.gif", "italic_ru.gif", "italic_tw.gif", "justifycenter.gif",
			"justifyfull.gif", "justifyleft.gif", "justifyright.gif", "link.gif", "menu_check.gif",
			"newdocument.gif", "numlist.gif", "opacity.png", "outdent.gif", "paste.gif", "redo.gif",
			"removeformat.gif", "separator.gif", "spacer.gif", "statusbar_resize.gif", "strikethrough.gif",
			"sub.gif", "sup.gif", "underline.gif", "underline_es.gif", "underline_fr.gif",
			"underline_ru.gif", "underline_tw.gif", "undo.gif", "unlink.gif", "visualaid.gif" });
	filesNames.put(
		URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "themes" + URL_SEPARATOR + "advanced" + URL_SEPARATOR
			+ "jscripts",
		new String[] { "about.js", "anchor.js", "charmap.js", "color_picker.js", "image.js", "link.js",
			"source_editor.js" });
	filesNames.put(URL_SEPARATOR + "tinymce" + URL_SEPARATOR + "themes" + URL_SEPARATOR + "advanced" + URL_SEPARATOR
		+ "langs", new String[] { "en.js" });

	for (String filePath : filesNames.keySet()) {
	    for (String fileName : filesNames.get(filePath)) {

		String urlToConnectTo = getImagesUrlDir() + filePath + URL_SEPARATOR + fileName;
		String directoryToStoreFile = outputDirectory + File.separator + "simple_spreadsheet" + filePath;
		HttpUrlConnectionUtil.writeResponseToFile(urlToConnectTo, directoryToStoreFile, fileName, cookies); // cookies aren't really needed here.

		log.debug("Copying image from source: " + urlToConnectTo + "to desitnation: " + directoryToStoreFile);
	    }
	}

    }

    private String getImagesUrlDir() {
	String spreadsheetUrlPath = Configuration.get(ConfigurationKeys.SERVER_URL);
	if (spreadsheetUrlPath == null) {
	    log.error(
		    "Unable to get path to the LAMS Spreadsheet URL from the configuration table. Spreadsheet javascript files export failed");
	    return "";
	} else {
	    if (!spreadsheetUrlPath.endsWith("/")) {
		spreadsheetUrlPath += "/";
	    }

	    spreadsheetUrlPath = spreadsheetUrlPath + "tool" + URL_SEPARATOR + SpreadsheetConstants.TOOL_SIGNATURE
		    + URL_SEPARATOR + "includes" + URL_SEPARATOR + "javascript" + URL_SEPARATOR + "simple_spreadsheet";
	    return spreadsheetUrlPath;
	}
    }

}
