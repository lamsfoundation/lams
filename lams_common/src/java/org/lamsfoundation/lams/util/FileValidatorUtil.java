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

package org.lamsfoundation.lams.util;

import java.text.NumberFormat;

import org.springframework.web.multipart.MultipartFile;

/**
 * This class is used by commons validator. To validate various properties
 * related to any uploaded files
 *
 * @author <a href="mailto:anthony.xiao@lamsinternational.com">Anthony Xiao</a>
 */
public class FileValidatorUtil {

    public static final String LARGE_FILE = "largeFile";
    public static final String MSG_KEY = "errors.maxfilesize";

    /**
     *
     * @param file
     * @param largeFile
     * @param errors
     * @return Be careful, if the file size is under maximum size, return TRUE. Otherwise, return false.
     */
    public static boolean validateFileSize(MultipartFile file, boolean largeFile) {
	Long fileSize = 0L;
	try {
	    fileSize = file.getSize();
	} catch (Exception e) {
	    //skip, do nothing
	    return true;
	}
	float maxFileSize = largeFile ? Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)
		: Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE);

	return fileSize <= maxFileSize;
    }

    public static boolean validateFileSize(Long fileSize, boolean largeFile) {
	float maxFileSize = largeFile ? Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE)
		: Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE);

	return fileSize <= maxFileSize;
    }

    public static String formatSize(double size) {
	String unit = null;
	if (size >= 1024) {
	    size = size / 1024;
	    if (size >= 1024) {
		size = size / 1024;
		unit = " MB";
	    } else {
		unit = " KB";
	    }
	} else {
	    unit = " B";
	}

	NumberFormat format = NumberFormat.getInstance();
	format.setMaximumFractionDigits(1);
	return format.format(size) + unit;
    }
}