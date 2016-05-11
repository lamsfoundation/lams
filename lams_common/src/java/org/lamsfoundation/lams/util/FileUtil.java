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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;
import org.jdom.JDOMException;
import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;

/**
 * General File Utilities
 */
public class FileUtil {

    public static final String ENCODING_UTF_8 = "UTF8";
    public static final String EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
    public static final String EXPORT_TO_SPREADSHEET_CELL_DATE_FORMAT = "dd.MM.yyyy";

    private static Logger log = Logger.getLogger(FileUtil.class);

    public static final String LAMS_WWW_SECURE_DIR = "secure";
    public static final String LAMS_WWW_DIR = "lams-www.war";
    public static final String LAMS_RUNTIME_CONTENT_DIR = "runtime";
    private static final long numMilliSecondsInADay = 24 * 60 * 60 * 1000;
    private static final int FILE_COPY_BUFFER_SIZE = 1024;

    public static final String ALLOWED_EXTENSIONS_FLASH = ".swf,.fla";
    public static final String ALLOWED_EXTENSIONS_IMAGE = ".jpg,.gif,.jpeg,.png,.bmp";
    public static final String ALLOWED_EXTENSIONS_MEDIA = ".3gp,.avi,.flv,.m4v,.mkv,.mov,.mp3,.mp4,.mpe,.mpeg,.mpg,.mpv,.mts,.m2ts,ogg,.wma,.wmv";

    protected static final String prefix = "lamstmp_"; // protected rather than private to suit junit test

    /**
     * Deleting a directory using File.delete() only works if the directory is empty. This method deletes a directory
     * and all of its contained files.
     *
     * This method is not transactional - if it fails to delete some contained files or directories, it will continue
     * deleting all the other files in the directory. If only a partial deletion is done, then the files and directories
     * that could not be deleted are listed in the log file, and the method returns false.
     *
     * This method has not been tested in Linux or Unix systems, so the behaviour across symbolic links is unknown.
     */
    public static boolean deleteDirectory(File directory) {
	boolean retValue = true;

	File[] files = directory.listFiles();
	if (files != null) {
	    for (int i = 0; i < files.length; i++) {
		File file = files[i];
		if (file.isDirectory()) {
		    FileUtil.deleteDirectory(file);
		} else if (!file.delete()) {
		    FileUtil.log.error("Unable to delete file " + file.getName());
		    retValue = false;
		}
	    }
	}
	if (directory.delete()) {
	    return retValue;
	} else {
	    return false;
	}
    }

    public static boolean deleteDirectory(String directoryName) throws FileUtilException {
	boolean isDeleted = false;
	if (directoryName == null || directoryName.length() == 0) {
	    throw new FileUtilException("A directory name must be specified");
	}

	File dir = new File(directoryName);
	isDeleted = FileUtil.deleteDirectory(dir);

	return isDeleted;

    }

    /**
     * Check if this directory is empty. If checkSubdirectories = true, then it also checks its subdirectories to make
     * sure they aren't empty. If checkSubdirectories = true and the directory contains empty subdirectories it will
     * return true. If checkSubdirectories = false and the directory contains empty subdirectories it will return false.
     */
    public static boolean isEmptyDirectory(String directoryName, boolean checkSubdirectories) throws FileUtilException {

	if (directoryName == null || directoryName.length() == 0) {
	    throw new FileUtilException("A directory name must be specified");
	}

	return FileUtil.isEmptyDirectory(new File(directoryName), checkSubdirectories);

    }

    private static boolean isEmptyDirectory(File directory, boolean checkSubdirectories) throws FileUtilException {

	if (directory.exists()) {
	    File files[] = directory.listFiles();

	    if (files.length > 0) {
		if (!checkSubdirectories) {
		    return false;
		} else {
		    boolean isEmpty = true;
		    for (int i = 0; i < files.length && isEmpty; i++) {
			File file = files[i];
			isEmpty = file.isDirectory() ? FileUtil.isEmptyDirectory(file, true) : false;
		    }
		    return isEmpty;
		}

	    } else {
		return true;
	    }
	}

	return true;
    }

    /**
     * Create a temporary directory with the name in the form lamstmp_timestamp_suffix inside the default temporary-file
     * directory for the system. This method is protected (rather than private) so that it may be called by the junit
     * tests for this class.
     *
     * @param zipFileName
     * @return name of the new directory
     * @throws ZipFileUtilException
     *             if the java io temp directory is not defined, or we are unable to calculate a unique name for the
     *             expanded directory, or an IOException occurs.
     */
    public static String createTempDirectory(String suffix) throws FileUtilException {

	String tempSysDirName = FileUtil.getTempDir();
	if (tempSysDirName == null) {
	    throw new FileUtilException(
		    "No temporary directory known to the server. [System.getProperty( \"java.io.tmpdir\" ) returns null. ]\n Cannot upload package.");
	}

	if (!(new File(tempSysDirName)).canWrite()) {
	    String javaTemp = System.getProperty("java.io.tmpdir");
	    if (!(new File(javaTemp).canWrite())) {
		throw new FileUtilException("Do not have write permissions for temporary directory: " + tempSysDirName
			+ " or java temp dir: " + javaTemp);
	    }
	    tempSysDirName = javaTemp;
	}

	String tempDirName = tempSysDirName + File.separator + FileUtil.prefix
		+ FileUtil.generateUniqueContentFolderID() + "_" + suffix;
	File tempDir = new File(tempDirName);

	// try 100 different variations. If I can't find a unique
	// one in 100 tries, then give up.
	int i = 0;
	while (tempDir.exists() && i < 100) {
	    tempDirName = tempSysDirName + File.separator + FileUtil.prefix + FileUtil.generateUniqueContentFolderID()
		    + "_" + suffix;
	    tempDir = new File(tempDirName);
	    i++;
	}
	if (tempDir.exists()) {
	    throw new FileUtilException(
		    "Unable to create temporary directory. The temporary filename/directory that we would use to extract files already exists: "
			    + tempDirName);
	}

	tempDir.mkdirs();
	return tempDirName;
    }

    /**
     * This method creates a directory with the name <code>directoryName</code>. Also creates any necessary parent
     * directories that may not yet exist.
     *
     * If the directoryname is null or an empty string, a FileUtilException is thrown
     *
     * @param directoryName
     *            the name of the directory to create
     * @return boolean. Returns true if the directory is created and false otherwise
     * @throws FileUtilException
     *             if the directory name is null or an empty string
     */
    public static boolean createDirectory(String directoryName) throws FileUtilException {
	boolean isCreated = false;
	// check directoryName to see if its empty or null
	if (directoryName == null || directoryName.length() == 0) {
	    throw new FileUtilException("A directory name must be specified");
	}

	File dir = new File(directoryName);
	isCreated = dir.exists() ? false : dir.mkdirs();

	return isCreated;
    }

    /**
     * Creates a subdirectory under the parent directory <code>parentDirName</code> If the parent or child directory is
     * null, FileUtilException is thrown.
     *
     * If the parent directory has not been created yet, it will be created.
     *
     *
     * @param parentDirName
     *            The name of the parent directory in which the subdirectory should be created in
     * @param subDirName
     *            The name of the subdirectory to create
     * @return boolean. Returns true if the subdirectory was created and false otherwise
     * @throws FileUtilException
     *             if the parent/child directory name is null or empty.
     */
    public static boolean createDirectory(String parentDirName, String subDirName) throws FileUtilException {
	boolean isSubDirCreated = false;
	boolean isParentDirCreated;

	if (parentDirName == null || parentDirName.length() == 0 || subDirName == null || subDirName.length() == 0) {
	    throw new FileUtilException("A parent or subdirectory name must be specified");
	}

	File parentDir = new File(parentDirName);
	if (!parentDir.exists()) {
	    isParentDirCreated = FileUtil.createDirectory(parentDirName);
	} else {
	    isParentDirCreated = true; // parent directory already exists
	}

	if (FileUtil.trailingForwardSlashPresent(parentDirName)) {
	    parentDirName = FileUtil.removeTrailingForwardSlash(parentDirName);
	}

	// concatenate the two together
	String combinedDirName = parentDirName + File.separator + subDirName;

	isSubDirCreated = FileUtil.createDirectory(combinedDirName);

	return isSubDirCreated && isParentDirCreated;
    }

    /**
     * If the directory name specified has a slash at the end of it such as "directoryName/", then the slash will be
     * removed and "directoryName" will be returned. The createDirectory(parentdir, childdir) method requires that there
     * is no slash at the end of the directory name.
     *
     * @param stringToModify
     * @return
     */
    public static String removeTrailingForwardSlash(String stringToModify) {
	String stringWithoutSlashAtEnd = stringToModify.substring(0, stringToModify.length() - 1);
	return stringWithoutSlashAtEnd;
    }

    /**
     * Checks to see if there is a slash at the end of the string.
     *
     * @param stringToCheck
     *            the directoryName to check
     * @return boolean. Returns true if there is a slash at the end and false if not.
     */
    public static boolean trailingForwardSlashPresent(String stringToCheck) {
	int indexOfSlash = stringToCheck.lastIndexOf("/");
	if (indexOfSlash == stringToCheck.length() - 1) {
	    return true;
	} else {
	    return false;
	}
    }

    public static boolean directoryExist(String directoryToCheck) {
	File dir = new File(directoryToCheck);
	return dir.exists();
    }

    private static String generateDumpFilename(String id, String extension) throws FileUtilException {
	// get dump directory name and make sure directory exists
	String dumpDirectory = Configuration.get(ConfigurationKeys.LAMS_DUMP_DIR);
	if (dumpDirectory == null) {
	    dumpDirectory = FileUtil.getTempDir();
	}
	FileUtil.createDirectory(dumpDirectory);

	String dumpFilename = dumpDirectory + File.separator + id + System.currentTimeMillis()
		+ (extension != null ? "." + extension : "");

	File dumpFile = new File(dumpFilename);
	int i = 0;
	while (dumpFile.exists() && i < 100) {
	    dumpFilename = dumpDirectory + File.separator + id + System.currentTimeMillis() + "_" + i
		    + (extension != null ? "." + extension : "");
	    dumpFile = new File(dumpFilename);
	}
	if (dumpFile.exists()) {
	    throw new FileUtilException(
		    "Unable to create dump file. The filename that we would use already exists: " + dumpFile);
	}

	return dumpFilename;
    }

    /**
     * Dump some data to a file in the Dump Directory. The directory is set in the LAMS configuration file. These dumps
     * are primarily for support/debugging/problem reporting uses.
     *
     * If the dump directory is not set, it will revert to the system temp directory.
     *
     * Used by the FlashCrashDump servlet initially, may be used by other dump methods in future.
     *
     * @param data
     *            data to dump
     * @param id
     *            some identification name for the string. Does not need to be unique. e.g. FLASH_jsmith
     * @param extension
     *            optional extension to be added to filename e.g. xml. Note: do not include the "." - that will be added
     *            automatically.
     *
     * @author Fiona Malikoff
     * @throws FileUtilException
     */
    public static String createDumpFile(byte[] data, String id, String extension) throws FileUtilException {
	String dumpFilename = FileUtil.generateDumpFilename(id, extension);
	OutputStream dumpFile = null;
	try {
	    dumpFile = new FileOutputStream(dumpFilename);
	    dumpFile.write(data);
	} catch (IOException e) {
	    FileUtil.log.error("Unable to write dump out byte array to dump file. ID: " + id + " Dump: " + data
		    + " Exception " + e.getMessage(), e);
	    throw new FileUtilException(e);
	} finally {
	    try {
		if (dumpFile != null) {
		    dumpFile.close();
		}
	    } catch (IOException e) {
		FileUtil.log.error(
			"Unable to close dump file. ID: " + id + " Dump: " + data + " Exception " + e.getMessage(), e);
		throw new FileUtilException(e);
	    }
	}
	return dumpFilename;
    }

    /**
     * get file name from a string which may include directory information. For example : "c:\\dir\\ndp\\pp.txt"; will
     * return pp.txt.? If file has no path infomation, then just return input fileName.
     *
     */
    public static String getFileName(String fileName) {
	if (fileName == null) {
	    return "";
	}

	fileName = fileName.trim();

	int dotPos = fileName.lastIndexOf("/");
	int dotPos2 = fileName.lastIndexOf("\\");
	dotPos = Math.max(dotPos, dotPos2);
	if (dotPos == -1) {
	    return fileName;
	}
	return fileName.substring(dotPos + 1, fileName.length());

    }

    /**
     * Get file directory info.
     *
     * @param fileName
     *            with path info.
     * @return return only path info with the given fileName
     */
    public static String getFileDirectory(String fileName) {
	if (fileName == null) {
	    return "";
	}

	fileName = fileName.trim();

	int dotPos = fileName.lastIndexOf("/");
	int dotPos2 = fileName.lastIndexOf("\\");
	dotPos = Math.max(dotPos, dotPos2);
	if (dotPos == -1) {
	    return "";
	}
	// return the last char is '/'
	return fileName.substring(0, dotPos + 1);

    }

    /**
     * Merge two input parameter into full path and adjust File.separator to OS default separator as well.
     *
     * @param path
     * @param file
     *            could be file name,or sub directory path.
     * @return
     */
    public static String getFullPath(String path, String file) {
	String fullpath;
	if (path.endsWith(File.separator)) {
	    fullpath = path + file;
	} else {
	    fullpath = path + File.separator + file;
	}

	return FileUtil.makeCanonicalPath(fullpath);

    }

    public static String makeCanonicalPath(String pathfile) {
	if (File.separator.indexOf("\\") != -1) {
	    pathfile = pathfile.replaceAll("\\/", "\\\\");
	} else {
	    pathfile = pathfile.replaceAll("\\\\", File.separator);
	}

	return pathfile;
    }

    /**
     * get file extension name from a String, such as from "textabc.doc", return "doc" fileName also can contain
     * directory infomation.
     */
    public static String getFileExtension(String fileName) {
	if (fileName == null) {
	    return "";
	}

	fileName = fileName.trim();
	int dotPos = fileName.lastIndexOf(".");
	if (dotPos == -1) {
	    return "";
	}
	return fileName.substring(dotPos + 1, fileName.length());
    }

    /**
     * Check whether file is executable according to its extenstion and executable extension name list from LAMS
     * configuaration.
     *
     * @param filename
     * @return
     */
    public static boolean isExecutableFile(String filename) {
	String extname = FileUtil.getFileExtension(filename);
	FileUtil.log.debug("Check executable file for extension name " + extname);

	if (StringUtils.isBlank(extname)) {
	    return false;
	}
	extname = "." + extname;

	String exeListStr = Configuration.get(ConfigurationKeys.EXE_EXTENSIONS);
	String[] extList = StringUtils.split(exeListStr, ',');
	boolean executable = false;
	for (String ext : extList) {
	    if (StringUtils.equalsIgnoreCase(ext, extname)) {
		executable = true;
		break;
	    }
	}

	return executable;
    }

    /**
     * Verify if a file with such extension is allowed to be uploaded.
     *
     * @param fileType
     *            file type can be of the following values:File, Image, Flash, Media
     * @param fileName
     */
    public static boolean isExtensionAllowed(String fileType, String fileName) {
	String ext = UploadFileUtil.getFileExtension(fileName);
	ext = "." + ext;
	String allowedExtensions;

	if ("File".equals(fileType)) {
	    // executables are not allowed
	    return !FileUtil.isExecutableFile(fileName);

	} else if ("Image".equals(fileType)) {
	    allowedExtensions = ALLOWED_EXTENSIONS_IMAGE;

	} else if ("Flash".equals(fileType)) {
	    allowedExtensions = ALLOWED_EXTENSIONS_FLASH;

	} else if ("Media".equals(fileType)) {
	    allowedExtensions = ALLOWED_EXTENSIONS_MEDIA;

	} else {
	    // unknown fileType
	    return false;
	}

	String[] allowedExtensionsList = StringUtils.split(allowedExtensions, ',');
	for (String allowedExtension : allowedExtensionsList) {
	    if (StringUtils.equalsIgnoreCase(ext, allowedExtension)) {
		return true;
	    }
	}

	return false;
    }

    /**
     * Clean up any old directories in the java tmp directory, where the directory name starts with lamszip_ or lamstmp_
     * and is <numdays> days old or older. This has the potential to be a heavy call - it has to do complete directory
     * listing and then recursively delete the files and directories as needed.
     *
     * Note: this method has not been tested as it is rather hard to write a junit test for!
     *
     * @param directories
     * @return number of directories deleted
     */
    public static int cleanupOldFiles(File[] directories) {
	int numDeleted = 0;
	if (directories != null) {
	    for (int i = 0; i < directories.length; i++) {
		if (FileUtil.deleteDirectory(directories[i])) {
		    FileUtil.log.info("Directory " + directories[i].getPath() + " deleted.");
		} else {
		    FileUtil.log.info("Directory " + directories[i].getPath()
			    + " partially deleted - some directories/files could not be deleted.");
		}
		numDeleted++;
	    }
	}
	return numDeleted;
    }

    /**
     * List files in temp directory older than numDays.
     *
     * @param numDays
     *            Number of days old that the directory should be to be deleted. Must be greater than 0
     * @return array of files older than input date
     * @throws FileUtilException
     *             if numDays <= 0
     */
    public static File[] getOldTempFiles(int numDays) throws FileUtilException {
	// Contract checking
	if (numDays < 0) {
	    throw new FileUtilException("Invalid getOldTempFiles call - the parameter numDays is " + numDays
		    + ". Must not be less than 0.");
	}

	// calculate comparison date
	long newestDateToKeep = System.currentTimeMillis() - numDays * FileUtil.numMilliSecondsInADay;
	Date date = new Date(newestDateToKeep);
	FileUtil.log.info("Getting all temp zipfile expanded directories before " + date.toString() + " (server time) ("
		+ newestDateToKeep + ")");

	File tempSysDir = new File(FileUtil.getTempDir());
	File candidates[] = tempSysDir.listFiles(new TempDirectoryFilter(newestDateToKeep, FileUtil.log));
	return candidates;
    }

    /**
     * Recursively calculates size in bytes of given file or directory.
     *
     * @param file
     * @return Size in bytes.
     */
    public static long calculateFileSize(File file) {
	if (file != null) {
	    if (file.isFile()) {
		return file.length();
	    } else if (file.isDirectory()) {
		File[] fileList = file.listFiles();
		long totalSize = 0;
		if (fileList != null) {
		    for (int i = 0; i < fileList.length; i++) {
			totalSize += FileUtil.calculateFileSize(fileList[i]);
		    }
		    return totalSize;
		} else {
		    return 0;
		}
	    }
	} else {
	    return 0;
	}
	return 0;
    }

    /**
     * Remove chars from a file name that may be invalid on a file system.
     *
     * @param name
     * @return a filename that can be saved to a file system.
     */
    public static String stripInvalidChars(String name) {
	name = name.replaceAll("\\\\", "");
	name = name.replaceAll("\\/", "");
	name = name.replaceAll("\\:", "");
	name = name.replaceAll("\\*", "");
	name = name.replaceAll("\\?", "");
	name = name.replaceAll("\\>", "");
	name = name.replaceAll("\\<", "");
	name = name.replaceAll("\\|", "");
	name = name.replaceAll("\\#", "");
	name = name.replaceAll("\\%", "");
	name = name.replaceAll("\\$", "");
	name = name.replaceAll("\\;", "");
	return name;
    }

    /**
     * Encode a filename in such a way that the UTF-8 characters won't be munged during the download by a browser. Need
     * the request to work out the user's browser type
     *
     * @return encoded filename
     * @throws UnsupportedEncodingException
     */
    public static String encodeFilenameForDownload(HttpServletRequest request, String unEncodedFilename)
	    throws UnsupportedEncodingException {

	// Different browsers handle stream downloads differently LDEV-1243
	String agent = request.getHeader("USER-AGENT");
	String filename = null;

	if (null != agent && -1 != agent.indexOf("MSIE")) {
	    // if MSIE then urlencode it
	    filename = URLEncoder.encode(unEncodedFilename, FileUtil.ENCODING_UTF_8);

	} else if (null != agent && -1 != agent.indexOf("Mozilla")) {
	    // if Mozilla then base64 url_safe encoding
	    filename = MimeUtility.encodeText(unEncodedFilename, FileUtil.ENCODING_UTF_8, "B");

	} else {
	    // any others use same filename.
	    filename = unEncodedFilename;

	}

	return filename;
    }

    public static String generateUniqueContentFolderID() {

	String newUniqueContentFolderID = null;
	Properties props = new Properties();

	IdentifierGenerator uuidGen = new UUIDHexGenerator();
	((Configurable) uuidGen).configure(Hibernate.STRING, props, null);

	// lowercase to resolve OS issues
	newUniqueContentFolderID = ((String) uuidGen.generate(null, null)).toLowerCase();

	return newUniqueContentFolderID;
    }

    /**
     * Return content folder (unique to each learner and lesson) which is used for storing user generated content.
     * It's been used by CKEditor.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    public static String getLearnerContentFolder(Long lessonId, Long userId) {
	return LAMS_RUNTIME_CONTENT_DIR + "/" + lessonId + "/" + userId;
    }

    /**
     * Return lesson's content folder which is used for storing user generated content.
     * It's been used by CKEditor.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    public static String getLearnerContentFolder(Long lessonId) {
	return LAMS_RUNTIME_CONTENT_DIR + "/" + lessonId;
    }

    /**
     * Call xstream to get the POJOs from the XML file. To make it backwardly compatible we catch any exceptions due to
     * added fields, remove the field using the ToolContentVersionFilter functionality and try to reparse. We can't
     * nominate the problem fields in advance as we are making XML created by newer versions of LAMS compatible with an
     * older version.
     *
     * This logic depends on the exception message containing the text. When we upgrade xstream, we must check that this
     * message doesn't change.
     *
     * <pre>
     * 	com.thoughtworks.xstream.converters.ConversionException: unknownField : unknownField
     * 	---- Debugging information ----
     * 	required-type       : org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO
     * 	cause-message       : unknownField : unknownField
     * 	class               : org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO
     * 	message             : unknownField : unknownField
     * 	line number         : 15
     * 	path                : /org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO/unknownField
     * 	cause-exception     : com.thoughtworks.xstream.alias.CannotResolveClassException
     * 	-------------------------------
     * </pre>
     */
    public static Object getObjectFromXML(XStream xStream, String fullFilePath) throws JDOMException, IOException {

	Reader file = null;
	XStream conversionXml = xStream != null ? xStream : new XStream();
	ConversionException finalException = null;
	String lastFieldRemoved = "";
	ToolContentVersionFilter contentFilter = null;
	// cap the maximum number of retries to 30 - if we add more than 30 new
	// fields then we need to rethink our
	// strategy
	int maxRetries = 30;
	int numTries = 0;

	while (true) {
	    try {

		if (numTries > maxRetries) {
		    break;
		}
		numTries++;

		file = new InputStreamReader(new FileInputStream(fullFilePath), FileUtil.ENCODING_UTF_8);
		return conversionXml.fromXML(file);

	    } catch (ConversionException ce) {
		FileUtil.log.debug("Failed import", ce);
		finalException = ce;
		file.close();

		if (ce.getMessage() == null) {
		    // can't retry, so get out of here!
		    break;

		} else {
		    // try removing the field from our XML and retry
		    String message = ce.getMessage();
		    String classname = FileUtil.extractValue(message, "required-type");
		    String fieldname = FileUtil.extractValue(message, "message");
		    if (fieldname == null || fieldname.equals("")
			    || lastFieldRemoved.equals(classname + "." + fieldname)) {
			// can't retry, so get out of here!
			break;
		    } else {
			if (contentFilter == null) {
			    contentFilter = new ToolContentVersionFilter();
			}

			Class problemClass = FileUtil.getClass(classname);
			if (problemClass == null) {
			    // can't retry, so get out of here!
			    break;
			}

			contentFilter.removeField(problemClass, fieldname);
			contentFilter.transformXML(fullFilePath);
			lastFieldRemoved = classname + "." + fieldname;
			FileUtil.log.debug("Retrying import after removing field " + fieldname);
			continue;
		    }
		}
	    } finally {
		if (file != null) {
		    file.close();
		}
	    }
	}
	throw finalException;
    }

    /**
     * Extract the class name or field name from a ConversionException message
     */
    private static String extractValue(String message, String fieldToLookFor) {
	try {
	    int startIndex = message.indexOf(fieldToLookFor);
	    if (startIndex > -1) {
		startIndex = message.indexOf(":", startIndex + 1);
		if (startIndex > -1 && startIndex + 2 < message.length()) {
		    startIndex = startIndex + 2;
		    int endIndex = message.indexOf(" ", startIndex);
		    String value = message.substring(startIndex, endIndex);
		    return value.trim();
		}
	    }
	} catch (ArrayIndexOutOfBoundsException e) {
	}
	return "";
    }

    private static Class getClass(String classname) {
	try {
	    return Class.forName(classname);
	} catch (ClassNotFoundException e) {
	    FileUtil.log.error("Trying to remove unwanted fields from import but we can't find the matching class "
		    + classname + ". Aborting retry.", e);
	    return null;
	}
    }

    /**
     * Gets the temp dir, creates if not exists, returns java system temp dir if inaccessible
     *
     * @return
     */
    public static String getTempDir() {

	String ret = Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR);
	File tempDir = new File(ret);

	// Create if not exists
	if (!tempDir.exists()) {
	    boolean success = tempDir.mkdirs();
	    if (!success) {
		log.error("Could not create temp directory: " + ret);
		return System.getProperty("java.io.tmpdir");
	    }
	}

	// Return java temp dir if not accessible
	if (!tempDir.canWrite()) {
	    return System.getProperty("java.io.tmpdir");
	} else {
	    return ret;
	}

    }
}
