/***************************************************************************
* Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
* =============================================================
* 
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
* USA
* 
* http://www.gnu.org/licenses/gpl.txt
* ***********************************************************************/

/*
 * Created on Oct 11, 2005
 *
 */
package org.lamsfoundation.lams.learning.export;

import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;

/**
 * @author mtruong
 *
 */
public class ExportPortfolioConstants {

	private ExportPortfolioConstants()
	{}
	
	public static final String DIR_SUFFIX_EXPORT = "export"; //will appear as the name of the tmp directory created. For eg. lamstmp_1129615396387_export
	public static final String DIR_SUFFIX_ZIP = "exportZipFile"; //suffix for the tmp directory. For eg. lamstmp_1129615396387_exportZipFile
	public static final String MAIN_EXPORT_FILENAME = "export_main.html";
	public static final String ZIP_FILENAME = "export.zip";
	public static final String HOST = getServerURL();
	public static final String SUBDIRECTORY_BASENAME = "Activity"; //subdirectory for each activity is ActivityXX where XX is the activity Id
	public static final String PARAM_FILE_LOCATION = "fileLocation";
	public static final String TEMP_DIRECTORY = FileUtil.TEMP_DIR;
	public static final String URL_FOR_UNSUPPORTED_EXPORT = "learning/exportNotSupported";
	
	private static String getServerURL()
	{
	    return Configuration.get(ConfigurationKeys.SERVER_URL);
	}
}
