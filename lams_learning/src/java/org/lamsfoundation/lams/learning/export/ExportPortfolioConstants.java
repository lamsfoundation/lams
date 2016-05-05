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

/* $$Id$$ */
package org.lamsfoundation.lams.learning.export;

/**
 * @author mtruong
 *
 */
public class ExportPortfolioConstants {
    public static final String DIR_SUFFIX_EXPORT = "export"; // will appear as the name of the tmp directory created.
							     // For eg. lamstmp_1129615396387_export
    public static final String DIR_SUFFIX_ZIP = "exportZipFile"; // suffix for the tmp directory. For eg.
								 // lamstmp_1129615396387_exportZipFile
    public static final String MAIN_EXPORT_FILENAME = "export_main.html";
    public static final String MAIN_NOTEBOOK_FILENAME = "notebook_main.html";
    public static final String EXPORT_LEARNER_PREFIX = "Portfolio";
    public static final String EXPORT_TEACHER_PREFIX = "Class Portfolio";
    public static final String EXPORT_TEMP_FILENAME = "export.zip";
    public static final String SUBDIRECTORY_BASENAME = "Activity"; // subdirectory for each activity is ActivityXX where
								   // XX is the activity Id
    public static final String SUBDIRECTORY_NOTEBOOK_BASENAME = "Notebook";
    public static final String PARAM_FILE_LOCATION = "fileLocation";
    public static final String EXPORT_ERROR_FILENAME = "export_error.html";
    public static final String EXPORT_ACTIVITY_ERROR_KEY = "error.export.portfolio.not.supported";
}