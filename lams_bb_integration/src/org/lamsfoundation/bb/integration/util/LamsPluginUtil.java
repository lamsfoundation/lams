/****************************************************************
 * Copyright (C) 2007 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.bb.integration.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import blackboard.platform.plugin.PlugInException;
import blackboard.platform.plugin.PlugInUtil;

/**
 * 
 * This class basically manages the lams.properties file on the BB server This file contains the LAMS server URL, server
 * ID and secret key These values allow the communication between the two servers
 * 
 * @author <a href="mailto:lfoxton@melcoe.mq.edu.au">Luke Foxton</a>
 */
public class LamsPluginUtil {

    public static final String VENDOR_ID = "lams";
    public static final String PLUGIN_HANDLE = "lamscontent";
    public static final String CONTENT_HANDLE = "resource/x-lams-lamscontent";
    public static final String FILE_PROPERTIES = "lams.properties";

    public static final String PROP_LAMS_SECRET_KEY = "LAMS_SERVER_SKEY";
    public static final String PROP_LAMS_SERVER_ID = "LAMS_SERVER_ID";
    public static final String PROP_LAMS_URL = "LAMS_SERVER_URL";
    public static final String PROP_LAMS_SERVER_TIME_REFRESH_INTERVAL = "LAMS_SERVER_TIME_REFRESH_INTERVAL";

    private static Properties lamsProperties = null;

    /**
     * Returns the properties file that contains the server name, key and connection URL
     * 
     * @return The LAMS Properties file
     * @throws PlugInException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Properties getProperties() {
	if (lamsProperties != null)
	    return lamsProperties;

	// load LAMS Configuration File
	try {
	    File configFile = new File(PlugInUtil.getConfigDirectory(VENDOR_ID, PLUGIN_HANDLE).getPath()
		    + File.separator + FILE_PROPERTIES);
	    Properties p = new Properties();

	    if (configFile.exists())
		p.load(new FileInputStream(configFile));
	    else {
		p.setProperty(PROP_LAMS_URL, "");
		p.setProperty(PROP_LAMS_SECRET_KEY, "");
		p.setProperty(PROP_LAMS_SERVER_ID, "");
		p.setProperty(PROP_LAMS_SERVER_TIME_REFRESH_INTERVAL, "24");
	    }

	    lamsProperties = p;
	    return p;
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * Save a Properties file as the LAMS properties file
     * 
     * @param p
     * @throws PlugInException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void setProperties(Properties p) {
	try {
	    lamsProperties = p;

	    FileOutputStream configFile = new FileOutputStream(PlugInUtil.getConfigDirectory(VENDOR_ID, PLUGIN_HANDLE)
		    .getPath() + File.separator + FILE_PROPERTIES);
	    p.store(configFile, "LAMS configuration");
	    configFile.close();
	} catch (PlugInException e) {
	    throw new RuntimeException(e);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * 
     * @return the secret key from lams.properties
     */
    public static String getServerSecretKey() {
	return getProperties().getProperty(PROP_LAMS_SECRET_KEY);
    }

    /**
     * 
     * @return the secret url from lams.properties
     */
    public static String getServerUrl() {
	return getProperties().getProperty(PROP_LAMS_URL);
    }

    /**
     * 
     * @return the server id from lams.properties
     */
    public static String getServerId() {
	return getProperties().getProperty(PROP_LAMS_SERVER_ID);
    }
    
    /**
     * 
     * @return the LAMS server time refresh interval from lams.properties
     */
    public static String getLamsServerTimeRefreshInterval() {
	return getProperties().getProperty(PROP_LAMS_SERVER_TIME_REFRESH_INTERVAL);
    }

}
