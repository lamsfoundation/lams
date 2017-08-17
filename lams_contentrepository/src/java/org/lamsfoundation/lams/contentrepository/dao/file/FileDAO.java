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


package org.lamsfoundation.lams.contentrepository.dao.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.dao.IFileDAO;
import org.lamsfoundation.lams.contentrepository.exception.FileException;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * Manages the reading and writing of files to the repository directories.
 * Note: this does not involve the database - so no Hibernate!
 *
 * @author Fiona Malikoff
 */
public class FileDAO implements IFileDAO {

    protected Logger log = Logger.getLogger(FileDAO.class);

    /**
     * @return array of two strings - the first is the directory, the second is the
     *         file path.
     */
    protected String[] generateFilePath(Long uuid, Long versionId) throws FileException {

	String directoryPath = Configuration.get(ConfigurationKeys.CONTENT_REPOSITORY_PATH);

	if (directoryPath == null) {
	    throw new FileException(
		    "Repository location unknown. Cannot access files. This should have been configured in the Spring context.");
	}

	if (uuid == null || uuid.longValue() < 1) {
	    throw new FileException("Unable to generate new filename for uuid=" + uuid);
	}

	String uuidString = uuid.toString();
	if (uuidString.length() % 2 != 0) {
	    uuidString = "0" + uuidString;
	}
	for (int i = 0; i < uuidString.length(); i += 2) {
	    directoryPath = directoryPath + File.separator + uuidString.charAt(i) + uuidString.charAt(i + 1);
	}

	return new String[] { directoryPath, directoryPath + File.separator + versionId };
    }

    // TODO can the writing out of the file be made more efficient
    // with a larger buffer
    /**
     * Write out a file to the repository. Closes the stream. Overwrites any existing files.
     * 
     * @return the path to which the file was written
     */
    @Override
    public String writeFile(Long uuid, Long versionId, InputStream is) throws FileException {

	String paths[] = generateFilePath(uuid, versionId);

	// check that the directory path exists and create if necessary
	File dir = new File(paths[0]);
	dir.mkdirs();

	File file = new File(paths[1]);
	OutputStream os = null;
	try {

	    if (log.isDebugEnabled()) {
		log.debug("Writing out file to " + file.getAbsolutePath());
	    }

	    int bufLen = 1024; // 1 Kbyte
	    byte[] buf = new byte[1024]; // output buffer
	    os = new FileOutputStream(file);

	    BufferedInputStream in = new BufferedInputStream(is);
	    int len = 0;
	    while ((len = in.read(buf, 0, bufLen)) != -1) {
		os.write(buf, 0, len);
	    }

	} catch (IOException e) {
	    String message = "Unable to write file for uuid " + uuid + " versionId " + versionId
		    + " due to an IOException. Generated path was " + paths[1];
	    log.error(message, e);
	    throw new FileException(message, e);

	} finally {

	    try {
		if (is != null) {
		    is.close();
		}
	    } catch (IOException e1) {
		String message = "Unable to close supplied filestream due to IOException";
		log.error(message, e1);
		new FileException(message, e1);
	    }

	    try {
		if (os != null) {
		    os.close();
		}
	    } catch (IOException e2) {
		String message = "Unable to close file in repository due to IOException";
		log.error(message, e2);
		new FileException(message, e2);
	    }
	}

	return paths != null ? paths[0] : null;
    }

    /**
     * Gets a file from the repository.
     * 
     * @param uuid
     *            node id
     * @param versionId
     *            version id
     */
    @Override
    public InputStream getFile(Long uuid, Long versionId) throws FileException {

	String paths[] = generateFilePath(uuid, versionId);
	try {
	    return new FileInputStream(paths[1]);
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new FileException("Unable to read file for uuid " + uuid + " versionId " + versionId
		    + " due to an IOException. Generated path was " + paths[1], e);
	}
    }

    /**
     * Delete a file from the repository. If this makes the directory
     * empty, then the directory should be deleted.
     * 
     * @return 1 if deleted okay, 0 if file not found, -1 if file found but a delete error occured.
     */
    @Override
    public int delete(Long uuid, Long versionId) throws FileException {

	String paths[] = generateFilePath(uuid, versionId);
	int retValue = 0;

	// check the file exists before we try to delete it!
	File file = new File(paths[1]);
	if (file.exists()) {
	    retValue = file.delete() ? 1 : -1;
	}

	switch (retValue) {
	    case 0:
		log.error("Unable to delete file " + file.getPath() + " as the file does not exist.");
		break;
	    case -1:
		log.error("Unable to delete file " + file.getPath()
			+ ". File does exist but can't be deleted for some (unknown) reason.");
		break;
	}

	// is the directory empty? if so, delete
	File dir = new File(paths[0]);
	String[] files = dir.list();
	if (files != null && files.length == 0) {
	    dir.delete();
	}

	return retValue;

    }

    /**
     * Get the actual path of the file ie the path on disk
     * 
     * @throws FileException
     */
    @Override
    public String getFilePath(Long uuid, Long versionId) throws FileException {
	String paths[] = generateFilePath(uuid, versionId);
	return paths[1];
    }

    /**
     * Is there a file on disk?
     * <p>
     * Used to validate file nodes
     */
    @Override
    public boolean fileExists(Long uuid, Long versionId) throws FileException {
	File file = new File(getFilePath(uuid, versionId));
	return (file.exists());
    }
}
