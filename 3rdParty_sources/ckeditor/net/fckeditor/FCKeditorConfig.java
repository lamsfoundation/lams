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
package net.fckeditor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.fckeditor.tool.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java representation of the <a href="http://docs.fckeditor.net/FCKeditor_2.x/Developers_Guide/Configuration/Configuration_Options"
 * target="_blank">FCKConfig</a> object from the editor. Every FCKeditor
 * instance will load the <code>config.js</code> by default, if you assign a
 * FCKConfig instance to an editor, it will automatically override these
 * system-wide settings for the current instance only.
 * 
 *
 */
public class FCKeditorConfig extends HashMap<String, String> {

	private static final long serialVersionUID = -4831190504944866644L;
	private static final Logger logger = LoggerFactory
			.getLogger(FCKeditorConfig.class);

	/**
	 * Generates the URL parameter sequence for this configuration.
	 * 
	 * @return HTML-encoded sequence of configuration parameters
	 */
	public String getUrlParams() {
		StringBuffer osParams = new StringBuffer();
		try {
			for (Map.Entry<String, String> entry : this.entrySet()) {
				if (Utils.isNotEmpty(entry.getValue())) {
					osParams.append("&amp;");
					osParams.append(entry.getKey());
					osParams.append("=");
					osParams.append(URLEncoder
							.encode(entry.getValue(), "UTF-8"));
				}
			}

		} catch (UnsupportedEncodingException e) {
			logger.error("Configuration parameters could not be encoded", e);
		}

		if (osParams.length() > 0)
			osParams.delete(0, 5);
		return osParams.toString();
	}
}