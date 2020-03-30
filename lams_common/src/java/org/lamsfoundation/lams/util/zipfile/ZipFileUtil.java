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

package org.lamsfoundation.lams.util.zipfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;

/**
 * Handles zip files - expands them to a temporary directory, and deletes them on request.
 *
 * @author Fiona Malikoff
 */
public class ZipFileUtil {

    private static Logger log = Logger.getLogger(ZipFileUtil.class);
    public static final String prefix = "lamszip_"; // public to use in TempDirectoryFilter
    private static int BUFFER_SIZE = 8192;

    /**
     * Given a zip file as an input stream and the name of the zip file, expand the zip file to a temporary directory.
     *
     * The directory will be in the directory specified in the system property "java.io.tmpdir". This will normally be
     * the o/s level temporary directory. e.g. /tmp or c:/temp.
     *
     * A zip file name.zip will be expanded to something like lamszip_1212121212_name. It tries 100 different names,
     * based on the current time in milliseconds and a counter. If it hasn't found a unique name by then, it fails. So
     * potentially, it will fail if the system has to expand more than 100 zip files in a millisecond, where the zip
     * files all have the same name. Want to bet on those chances?
     *
     * @param is
     * @param zipFileName
     * @throws ZipFileUtilException
     */
    public static String expandZip(InputStream is, String zipFileName) throws ZipFileUtilException {
	String tempDirName = ZipFileUtil.prepareTempDirectory(zipFileName);
	ZipFileUtil.extractZipFile(is, tempDirName);
	return tempDirName;
    }

    public static void expandZipToFolder(InputStream is, String destFolder) throws ZipFileUtilException {
	ZipFileUtil.extractZipFile(is, destFolder);
    }

    /**
     * Create a temporary directory in which the zip file contents will go. 
     *
     * @param zipFileName
     * @return name of the new directory
     * @throws ZipFileUtilException
     *             if the java io temp directory is not defined, or we are unable to calculate a unique name for the
     *             expanded directory, or an IOException occurs.
     */
    public static String prepareTempDirectory(String zipFileName) throws ZipFileUtilException {
	int dotIndex = zipFileName.indexOf(".");
	String shortZipName = dotIndex > -1 ? zipFileName.substring(0, dotIndex) : zipFileName;

	String tempSysDirName = System.getProperty("java.io.tmpdir");
	if (tempSysDirName == null) {
	    throw new ZipFileUtilException(
		    "No temporary directory known to the server. [System.getProperty( \"java.io.tmpdir\" ) returns null. ]\n Cannot upload package.");
	}

	String tempDirName = tempSysDirName + File.separator + ZipFileUtil.prefix + System.currentTimeMillis() + "_"
		+ shortZipName;

	// Set up the directory. Check it doesn't exist and if it does
	// try 100 slightly different variations. If I can't find a unique
	// one in ten tries, then give up.
	File tempDir = new File(tempDirName);
	int i = 0;
	while (tempDir.exists() && (i < 100)) {
	    tempDirName = tempSysDirName + File.separator + ZipFileUtil.prefix + System.currentTimeMillis() + "_" + i
		    + shortZipName;
	    tempDir = new File(tempDirName);
	}
	if (tempDir.exists()) {
	    throw new ZipFileUtilException(
		    "Temporary filename/directory that we would use to extract files already exists: " + tempDirName
			    + ". Unable to upload Content Package.");
	}

	tempDir.mkdirs();
	return tempDirName;
    }

    private static void extractZipFile(InputStream is, String tempDirName) throws ZipFileUtilException {
	// got our directory, so write out the input file and expand the zip file
	// this is really a hack - write it out temporarily then read it back in again! urg!!!!
	ZipInputStream zis = new ZipInputStream(is);
	int count;
	byte data[] = new byte[ZipFileUtil.BUFFER_SIZE];
	ZipEntry entry = null;
	BufferedOutputStream dest = null;
	String entryName = null;

	try {

	    // work through each file, creating a node for each file
	    while ((entry = zis.getNextEntry()) != null) {

		if (!entry.isDirectory()) {
		    entryName = entry.getName();

		    String destName = tempDirName + File.separator + entryName;
		    // It must sort out the directory information to current OS.
		    // e.g, if zip file is zipped under windows, but unzip in linux.
		    destName = FileUtil.makeCanonicalPath(destName);

		    ZipFileUtil.prepareDirectory(destName);

		    FileOutputStream fos = new FileOutputStream(destName);
		    dest = new BufferedOutputStream(fos, ZipFileUtil.BUFFER_SIZE);
		    while ((count = zis.read(data, 0, ZipFileUtil.BUFFER_SIZE)) != -1) {
			dest.write(data, 0, count);
		    }
		    dest.flush();
		    dest.close();
		    dest = null;
		}

	    }

	} catch (IOException ioe) {

	    ZipFileUtil.log.error("Exception occured processing entries in zip file. Entry was " + entryName, ioe);
	    throw new ZipFileUtilException("Exception occured processing entries in zip file. Entry was " + entryName,
		    ioe);

	} finally {

	    if (dest != null) {
		try {
		    dest.close();
		} catch (IOException ioe2) {
		    ZipFileUtil.log.error("Exception thrown in finally statement, trying to close destination file.",
			    ioe2);
		}
	    }
	}
    }

    /**
     * @param destName
     */
    private static void prepareDirectory(String destName) {

	File destNameFile = new File(destName);
	String path = destNameFile.getParent();
	if (path != null) {
	    File pathDir = new File(path);
	    pathDir.mkdirs();
	}
    }

    /**
     * Delete a temporary directory. Checks that the directory name supplied looks like a temporary directory (ie is in
     * the java tmp directory and starts with lamszip_)
     *
     * @param directoryName
     * @return false if directoryName == null or one or more of the files/directories could not be deleted. See log for
     *         list of not deleted files.
     * @throws ZipFileUtilException
     *             if the directory name doesn't look like a temporary directory created by the helper.
     */
    public static boolean deleteDirectory(String directoryName) throws ZipFileUtilException {
	if (directoryName != null) {
	    String tempSysDirNamePrefix = System.getProperty("java.io.tmpdir") + File.separator + ZipFileUtil.prefix;
	    if (!directoryName.startsWith(tempSysDirNamePrefix)) {
		throw new ZipFileUtilException("Invalid directory delete request. Received request to delete directory "
			+ directoryName + " but name doesn't start with the temporary directory location ("
			+ tempSysDirNamePrefix + "). Not deleting directory");
	    }

	    return FileUtil.deleteDirectory(new File(directoryName));
	}
	return false;
    }

    private static String appendToString(String origString, String newMessage) {
	if (origString != null) {
	    return origString + "   \n" + newMessage;
	} else {
	    return newMessage;
	}
    }

    /**
     * Creates a ZIP file and places it in the current working directory. The zip file is compressed at the default
     * compression level of the Deflater.
     *
     * @param zipFileName
     *            the filename of the zip to create
     * @param directoryToZip
     *            the directory you want to zip
     * @return the filename of the zip file created
     * @throws ZipFileUtilException
     */
    public static String createZipFile(String zipFileName, String directoryToZip) throws ZipFileUtilException // public
													      // static
													      // String
													      // createZipFile(String
													      // ZipFileName,
													      // String[]
													      // filesToZip)
    {

	int dotIndex = zipFileName.indexOf(".");

	String fileNameOfZipToCreate = dotIndex > -1 ? zipFileName : zipFileName + ".zip"; // append ".zip" extension if
											   // the filename doesnt
											   // contain .zip extension

	try {
	    ZipOutputStream out = new ZipOutputStream(
		    new BufferedOutputStream(new FileOutputStream(fileNameOfZipToCreate)));
	    File directory = new File(directoryToZip);
	    if (!directory.exists()) {
		throw new ZipFileUtilException("The specified directory " + directoryToZip + " does not exist");
	    }
	    File[] files = directory.listFiles();

	    ZipFileUtil.zipFiles(out, files, directory.getCanonicalPath());
	    out.close();
	} catch (IOException e1) {
	    throw new ZipFileUtilException(
		    "An error has occurred while trying to zip the files. Error message is: " + e1.getMessage(), e1);
	}

	return fileNameOfZipToCreate;
    }

    /**
     * Creates a ZIP file and places it in the specified directory. The zip file is compressed at the default
     * compression level of the Deflater.
     *
     * @param zipFileName
     *            the filename of the zip to create
     * @param directoryToZip
     *            the directory you want to zip
     * @param directoryToPlaceZip
     *            the place where you want to place the newly created zip file.
     * @return the (absolute) filename of the zip that was created
     * @throws ZipFileUtilException
     */
    public static String createZipFile(String zipFileName, String directoryToZip, String directoryToPlaceZip)
	    throws ZipFileUtilException // public static String createZipFile(String ZipFileName, String[] filesToZip)
    {

	// check if the directory to place zip file exists, if it doesnt, then create one.
	/* TODO: should we overwrite and delete the folder if it already exists? */
	if (!FileUtil.directoryExist(directoryToPlaceZip)) {
	    try {
		FileUtil.createDirectory(directoryToPlaceZip);
	    } catch (FileUtilException e) {
		throw new ZipFileUtilException(
			"The temporary directory to place the zip file could be not created: " + e.getMessage(), e);
	    }
	}

	int dotIndex = zipFileName.indexOf(".");

	String zipFile = dotIndex > -1 ? zipFileName : zipFileName + ".zip"; // append ".zip" extension if the filename
									     // doesnt contain .zip extension
	String fileNameOfZipToCreate = directoryToPlaceZip + File.separator + zipFile;

	try {
	    ZipOutputStream out = new ZipOutputStream(
		    new BufferedOutputStream(new FileOutputStream(fileNameOfZipToCreate)));

	    File directory = new File(directoryToZip);
	    if (!directory.exists()) {
		throw new ZipFileUtilException("The specified directory " + directoryToZip + " does not exist");
	    }
	    File[] files = directory.listFiles();

	    ZipFileUtil.zipFiles(out, files, directory.getCanonicalPath());
	    out.close();
	} catch (IOException e1) {
	    throw new ZipFileUtilException(
		    "An error has occurred while trying to zip the files. Error message is: " + e1.getMessage(), e1);
	}

	return fileNameOfZipToCreate;
    }

    protected static void zipFiles(ZipOutputStream zop, File[] files, String dirPath) throws ZipFileUtilException {
	File file = null;
	ZipEntry entry = null;
	BufferedInputStream source = null;
	byte[] data = new byte[ZipFileUtil.BUFFER_SIZE]; // byte[] buffer = new byte[ 1024 ]; //what size should we use?
							 // 1024?

	try {
	    for (int i = 0; i < files.length; i++) {
		file = files[i];
		// if file is a directory, recursively call this method
		if (file.isDirectory()) {
		    File[] filesInsideDir = file.listFiles();
		    ZipFileUtil.zipFiles(zop, filesInsideDir, dirPath);
		} else {
		    source = new BufferedInputStream(new FileInputStream(file));
		    // entry = new ZipEntry(file.getPath());
		    entry = new ZipEntry(ZipFileUtil.removeDirPath(file.getCanonicalPath(), dirPath));
		    zop.setMethod(ZipOutputStream.DEFLATED);
		    zop.setLevel(Deflater.DEFAULT_COMPRESSION);

		    zop.putNextEntry(entry);

		    // transfer bytes from file to ZIP file
		    int length;
		    while ((length = source.read(data)) > 0) {
			zop.write(data, 0, length);
		    }

		    zop.closeEntry();
		    source.close();

		    source = null;

		}

	    }
	} catch (IOException e) {
	    throw new ZipFileUtilException(
		    "An error has occurred while trying to zip the files. Error message is: " + e.getMessage(), e);
	}
    }

    /**
     * Helper method used to cut off the root directory path from the absolute path of the file. We want the relative
     * path so that when zipped up, the upper directories arent shown.
     *
     * @param absoluteFilePath
     *            the absolute pathname of the file
     * @param rootDirPath
     *            the root directory path
     * @return
     */
    private static String removeDirPath(String absoluteFilePath, String rootDirPath) {
	int rootDirLength = rootDirPath.length();
	return absoluteFilePath.substring(rootDirLength + 1, absoluteFilePath.length()); // added one to remove the
											 // trailing "\" that is behind
											 // the root dir
    }

}
