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


package org.lamsfoundation.lams.learning.export.web.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * Superclass for any files "bundled" as part of the export portfolio. For code reuse.
 *
 */
public class Bundler {

    protected static Logger log = Logger.getLogger(Bundler.class);

    /**
     * 
     */
    public Bundler() {
	super();
    }

    protected void createDirectories(List<String> directoriesRequired) {

	for (String directoryPath : directoriesRequired) {
	    File dir = new File(directoryPath);
	    if (!dir.exists() && !dir.mkdirs()) {
		log.error("Unable to create directory for export portfolio: " + directoryPath);
	    }
	}
    }

    protected void copyFile(String destFilePath, File srcFile) throws IOException {

	log.debug("Copying file " + srcFile + " to " + destFilePath);
	File destFile = new File(destFilePath);

	try {
	    FileUtils.copyFile(srcFile, destFile);
	} catch (IOException e) {
	    String message = "Unable to write out file needed for export portfolio. File was " + srcFile;
	    log.error(message, e);
	}
    }

}