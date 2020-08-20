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

import java.io.File;

/**
 * Wraps up interface to the Commons FileUpload as much as possible. Reads configuration information such the maximum
 * size of a file from lams.xml using the ConfigurationService.
 *
 * @author fmalikoff Fei Yang
 *
 */
public class UploadFileUtil {
    private static final int DEFAULT_MAX_SIZE = 5000000; // 5 Meg

    /**
     * @return Maximum size (in megabytes) that a file may be. Standard size, use during running of sequence.
     */
    public static float getMaxFileSize() {
	int max_size = UploadFileUtil.DEFAULT_MAX_SIZE;

	int tempInt = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE);
	if (tempInt != -1) {
	    max_size = tempInt;
	}

	return (max_size != 0 ? (float) max_size / 1024 / 1024 : (float) 0);
    }

    /**
     * @return Maximum size (in megabytes) that a file may be. Large size, use during authoring only.
     */
    public static float getMaxLargeFileSize() {
	int max_size = UploadFileUtil.DEFAULT_MAX_SIZE;

	int tempInt = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_LARGE_MAX_SIZE);
	if (tempInt != -1) {
	    max_size = tempInt;
	}

	return (max_size != 0 ? (float) max_size / 1024 / 1024 : (float) 0);
    }

    /**
     * Returns dir where resources get uploaded for all lessons.
     */
    public static File getUploadBaseDir() {
	String baseDirPath = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator + FileUtil.LAMS_WWW_DIR
		+ File.separator + FileUtil.LAMS_WWW_SECURE_DIR;
	File baseDir = new File(baseDirPath);
	baseDir.mkdir();
	return baseDir;
    }

    /**
     * Returns dir where images and other resources can be uploaded.
     */
    public static File getUploadDir(String contentFolderID, String type) {
	File uploadDir = UploadFileUtil.getUploadBaseDir();
	// get content folder ID split into folders of 2 characters
	String[] splitContentDir = UploadFileUtil.splitContentDir(contentFolderID);
	for (int groupIndex = 0; groupIndex < splitContentDir.length; groupIndex++) {
	    uploadDir = new File(uploadDir, splitContentDir[groupIndex]);
	}
	uploadDir = new File(uploadDir, type);
	uploadDir.mkdirs();
	return uploadDir;
    }

    /**
     * Checks if given name already exists in upload dir. If so, it returns next free name.
     */
    public static String getUploadFileName(File uploadDir, String fileName) {
	int counter = 1;
	String finalName = fileName;
	File uploadFile = new File(uploadDir, fileName);
	while (uploadFile.exists()) {
	    finalName = UploadFileUtil.getFileNameWithoutExtension(fileName) + "_" + counter + "."
		    + UploadFileUtil.getFileExtension(fileName);
	    uploadFile = new File(uploadDir, finalName);
	    counter++;
	}
	return finalName;
    }

    /**
     * Get the path where the uploaded file will be accessible via HTTP.
     */
    public static String getUploadWebPath(String contentFolderID, String type) {
	return "/" + Configuration.get(ConfigurationKeys.SERVER_URL_CONTEXT_PATH) + "/"
		+ CommonConstants.LAMS_WWW_FOLDER + FileUtil.LAMS_WWW_SECURE_DIR + "/"
		+ String.join("/", UploadFileUtil.splitContentDir(contentFolderID)) + "/" + type;
    }

    public static String getFileNameWithoutExtension(String fileName) {
	return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static String getFileExtension(String fileName) {
	return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Take first 12 characters of content folder ID (without "-" and "/")
     * and split them in pairs of 2
     */
    private static String[] splitContentDir(String contentFolderID) {
	String[] result = new String[6];
	String trimmedFolder = contentFolderID.replace("-", "").replace("/", "");
	//if trimmedFolder length is less than 12, supplement it with missing character
	if (trimmedFolder.length() < 12) {
	    trimmedFolder += "123456789012".substring(0, 12 - trimmedFolder.length());
	}

	for (int groupIndex = 0; groupIndex < 6; groupIndex++) {
	    result[groupIndex] = "" + trimmedFolder.charAt(groupIndex * 2) + trimmedFolder.charAt(groupIndex * 2 + 1);
	}
	return result;
    }
}