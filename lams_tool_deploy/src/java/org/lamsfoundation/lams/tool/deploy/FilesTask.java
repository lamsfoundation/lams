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


package org.lamsfoundation.lams.tool.deploy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Parent of Tasks that deal with the deploy files.
 *
 * @author chris
 */
public abstract class FilesTask implements Task {

    /**
     * Holds value of property lamsEarPath.
     */
    protected String lamsEarPath;

    /**
     * Holds value of property deployFiles.
     */
    protected List<String> deployFiles;

    /** Creates a new instance of DeployWarTask */
    public FilesTask() {
    }

    /**
     * Sets the path to the lams ear.
     *
     * @param lamsEarPath
     *            New value of property lamsEarPath.
     */
    public void setLamsEarPath(String lamsEarPath) {
	this.lamsEarPath = lamsEarPath;
    }

    /**
     * Sets the list of file paths to operate on.
     *
     * @param deployFiles
     *            New value of property deployFiles.
     */
    public void setDeployFiles(List<String> deployFiles) {
	this.deployFiles = deployFiles;
    }

    /**
     * Copy a file to a given directory
     */
    protected void copyFile(String fileName, File directory) throws DeployException {
	try {
	    File original = new File(fileName);
	    System.out.println("Copying file " + fileName + " to " + directory.getAbsolutePath());
	    FileUtils.copyFileToDirectory(original, directory);
	} catch (IOException ioex) {
	    throw new DeployException("Could not copy file " + fileName + " to " + directory.getAbsolutePath(), ioex);
	}
    }
}