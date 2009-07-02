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

/* $Id$ */

package org.lamsfoundation.lams.learning.export.web.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * Auxiliary class for supporting portfolio export. Copying all images and files uploaded by user into export temporary
 * folder, plus the FCKEditor smileys, plus a few misc common images.
 * 
 * @author AndreyB
 * 
 */
public class ImageBundler extends Bundler {

    private static Logger log = Logger.getLogger(ImageBundler.class);
    private static String[] miscImages = new String[] { "dash.gif", "cross.gif", "error.jpg", "spacer.gif", "tick.gif",
	    "tree_closed.gif", "tree_open.gif" , "help.jpg"};

    Map<String, File> filesToCopy = null;
    List<String> directoriesRequired = null;
    String outputDirectory = null;
    String contentFolderId = null;
    String lamsWwwPath = null;
    String lamsCentralPath = null;

    /**
     * @param outputDirectory
     *            directory for the export
     * @param contentFolderId
     *            the 32-character content folder name
     */
    public ImageBundler(String outputDirectory, String contentFolderId) {
	filesToCopy = new HashMap<String, File>();
	directoriesRequired = new ArrayList<String>();
	this.outputDirectory = outputDirectory;
	this.contentFolderId = contentFolderId;

	String lamsEarDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR);
	if (lamsEarDir == null) {
	    log
		    .error("Unable to get path to the LAMS ear from the configuration file - the exported portfolios will be missing User generated content and FCKEditor smileys.");
	} else {
	    lamsWwwPath = lamsEarDir + File.separator + "lams-www.war";
	    lamsCentralPath = lamsEarDir + File.separator + "lams-central.war";
	}
    }

    /**
     * Bundle all images and files uploaded by user, also FCKEditor smileys.
     * 
     * @throws IOException
     */
    public void bundleImages() throws IOException {

	// Copy contentFolder
	if (lamsWwwPath != null) {
	    log.debug("Copying user generated content from path " + lamsWwwPath);

	    File contentFolderDir = new File(lamsWwwPath + File.separatorChar + "secure" + File.separatorChar
		    + contentFolderId);

	    if (contentFolderDir.exists() && contentFolderDir.isDirectory()) {
		// copy content folder to output
		File destDir = new File(outputDirectory + File.separatorChar + contentFolderId);
		FileUtils.copyDirectory(contentFolderDir, destDir);
	    } else {
		log.debug("Folder for contentFolderId:" + contentFolderId + "doesn't exist");
	    }
	}

	File central = new File(lamsCentralPath);
	if (lamsCentralPath != null && central.canRead() && central.isDirectory()) {
	    log.debug("Copying FCKeditor smileys from path " + lamsCentralPath);
	    // build up a list of images to copy
	    setupFCKEditorSmileysList();

	    // build up a list of the misc images to copy
	    setupMiscImages();
	    
	    // build up a list of things to add for vr to work
	    setupVideoRecorderExport();
	}

	// now copy all those files
	createDirectories(directoriesRequired);
	for (Map.Entry fileEntry : filesToCopy.entrySet()) {
	    copyFile((String) fileEntry.getKey(), (File) fileEntry.getValue());
	}
    }

    /**
     * Creates list of FCKEditor smiley files that should be exported.
     */
    private void setupFCKEditorSmileysList() {
	String imageDirectory = lamsCentralPath + File.separatorChar + "fckeditor" + File.separatorChar + "editor"
		+ File.separatorChar + "images" + File.separatorChar + "smiley" + File.separatorChar + "msn";
	String outputImageDirectory = outputDirectory + File.separatorChar + "fckeditor" + File.separatorChar
		+ "editor" + File.separatorChar + "images" + File.separatorChar + "smiley" + File.separatorChar + "msn";
	directoriesRequired.add(outputImageDirectory);

	File dir = new File(imageDirectory);
	if (!dir.canRead() || !dir.isDirectory()) {
	    log.debug("Unable to read image directory " + dir.getAbsolutePath());
	} else {
	    File[] files = dir.listFiles();
	    for (File imageFile : files) {
		filesToCopy.put(outputImageDirectory + File.separatorChar + imageFile.getName(), imageFile);
	    }
	}
    }

    /**
     * Creates list of misc image files that should be exported.
     */
    private void setupMiscImages() {
	String imageDirectory = lamsCentralPath + File.separatorChar + "images";
	String outputImageDirectory = outputDirectory + File.separatorChar + "images";

	directoriesRequired.add(outputImageDirectory);

	for (String imageName : miscImages) {
	    String inputFilename = imageDirectory + File.separatorChar + imageName;
	    String outputFilename = outputImageDirectory + File.separatorChar + imageName;

	    File image = new File(inputFilename);
	    if (!image.canRead() || image.isDirectory()) {
		log.error("Unable to copy image " + inputFilename
			+ " as file does not exist or cannot be read as a file.");
	    } else {
		filesToCopy.put(outputFilename, image);
	    }
	}
    }
    
    /**
     * Adds VideoRecorder stuff to be exported
     */
    private void setupVideoRecorderExport() {
    	String vrDirectory = lamsCentralPath + File.separatorChar + "fckeditor" + File.separatorChar + "editor"
		+ File.separatorChar + "plugins" + File.separatorChar + "videorecorder";
    	
    	String outputVrDirectory = outputDirectory + File.separatorChar + "fckeditor" + File.separatorChar
		+ "editor" + File.separatorChar + "plugins" + File.separatorChar + "videorecorder";
	
		directoriesRequired.add(outputVrDirectory);
	
		File dir = new File(vrDirectory);
		if (!dir.canRead() || !dir.isDirectory()) {
		    log.debug("Unable to read vr directory " + dir.getAbsolutePath());
		} else {
		    File[] files = dir.listFiles();
		    for (File file : files) {
		    	if(!file.isDirectory())
		    		filesToCopy.put(outputVrDirectory + File.separatorChar + file.getName(), file);
		    }
		}
    }
}
