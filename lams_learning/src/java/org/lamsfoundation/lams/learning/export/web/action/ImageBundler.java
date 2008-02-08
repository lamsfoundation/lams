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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * Auxiliary class for supporting portfolio export. Copying all images and files
 * uploaded by user into export temporary folder.
 * 
 * @author AndreyB
 * 
 */
public class ImageBundler {

	private static Logger log = Logger.getLogger(ImageBundler.class);

	Map<String,File> filesToCopy = null;
	List<String> directoriesRequired = null;
	String outputDirectory  = null;
	String contentFolderId = null;
	String lamsWwwPath  = null;
	String lamsCentralPath  = null;

	/**
	 * @param outputDirectory directory for the export
	 * @param contentFolderId the 32-character content folder name
	 */
	public ImageBundler(String outputDirectory, String contentFolderId) {
		filesToCopy = new HashMap<String,File>();
		directoriesRequired = new ArrayList<String>();
		this.outputDirectory = outputDirectory;
		this.contentFolderId  = contentFolderId;
		
		String lamsEarDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR); 
		if ( lamsEarDir == null )  {
			log.error("Unable to get path to the LAMS ear from the configuration file - the exported portfolios will be missing User generated content and FCKEditor smileys.");
		} else {
			lamsWwwPath = lamsEarDir + File.separator + "lams-www.war";
			lamsCentralPath = lamsEarDir + File.separator + "lams-central.war";
		}
	}
	
	/** Bundle all images and files uploaded by user, also FCKEditor smileys.
	 * 
	 * @throws IOException
	 */
	public void bundleImages( ) throws IOException
	{
		File www = new File(lamsWwwPath);
		if ( lamsWwwPath != null && www.canRead() && www.isDirectory() ) {
			log.debug("Copying user generated content from path " + lamsWwwPath);
			
			// build up a list of images to copy
			setupImageList();
			
			// build up a list of themes to copy
			setupFileList();
		}
		
		File central = new File(lamsCentralPath);
		if ( lamsCentralPath != null && central.canRead() && central.isDirectory() ) {
			log.debug("Copying FCKeditor smileys from path " + lamsCentralPath);
			
			// build up a list of images to copy
			setupFCKEditorSmileysList();
		}
		
		// now copy all those files
		createDirectories();
		for ( Map.Entry fileEntry : filesToCopy.entrySet() ) {
			copyFile((String)fileEntry.getKey(), (File)fileEntry.getValue());
		}
	}

	/**
	 * Creates list of files uploaded by user that should be exported.
	 */
	private void setupFileList() {
		String fileDirectory = lamsWwwPath + File.separatorChar + "secure"
				+ File.separatorChar + contentFolderId + File.separatorChar
				+ "File";
		String outputFileDirectory = outputDirectory + File.separatorChar
				+ contentFolderId + File.separatorChar + "File";
		directoriesRequired.add(outputFileDirectory);
	
		File dir = new File(fileDirectory);
		if ( ! dir.canRead() || ! dir.isDirectory() ) {
			log.debug("Unable to read file directory " + dir.getAbsolutePath());
		}  else {
			File[] files = dir.listFiles();
			for ( File imageFile: files ) {
				filesToCopy.put(outputFileDirectory+File.separatorChar+imageFile.getName(),imageFile);
			}
		}
	}

	/**
	 * Creates list of Images uploaded by user that should be exported.
	 */
	private void setupImageList() {
		String imageDirectory = lamsWwwPath + File.separatorChar + "secure"
				+ File.separatorChar + contentFolderId + File.separatorChar
				+ "Image";
		String outputImageDirectory = outputDirectory + File.separatorChar
				+ contentFolderId + File.separatorChar + "Image";
		directoriesRequired.add(outputImageDirectory);
	
		File dir = new File(imageDirectory);
		if ( ! dir.canRead() || ! dir.isDirectory() ) {
			log.debug("Unable to read image directory " + dir.getAbsolutePath());
		}  else {
			File[] files = dir.listFiles();
			for ( File imageFile: files ) {
				filesToCopy.put(outputImageDirectory+File.separatorChar+imageFile.getName(),imageFile);
			}
		}
	}
	
	/**
	 * Creates list of FCKEditor smiley files that should be exported.
	 */
	private void setupFCKEditorSmileysList() {
		String imageDirectory = lamsCentralPath + File.separatorChar
				+ "fckeditor" + File.separatorChar + "editor"
				+ File.separatorChar + "images" + File.separatorChar + "smiley"
				+ File.separatorChar + "msn";
		String outputImageDirectory = outputDirectory + File.separatorChar
				+ "fckeditor" + File.separatorChar + "editor"
				+ File.separatorChar + "images" + File.separatorChar + "smiley"
				+ File.separatorChar + "msn";
		directoriesRequired.add(outputImageDirectory);
	
		File dir = new File(imageDirectory);
		if ( ! dir.canRead() || ! dir.isDirectory() ) {
			log.debug("Unable to read image directory " + dir.getAbsolutePath());
		}  else {
			File[] files = dir.listFiles();
			for ( File imageFile: files ) {
				filesToCopy.put(outputImageDirectory+File.separatorChar+imageFile.getName(),imageFile);
			}
		}
	}
	
	private void createDirectories() {
		
		for ( String directoryPath: directoriesRequired) {
			File dir = new File(directoryPath);
			if ( ! dir.mkdirs() )  {
				log.error("Unable to create directory for export portfolio: "+directoryPath);
			}
		}
	}

	private void copyFile(String filePath, File file) throws IOException {
		
		FileInputStream is = new FileInputStream(file);
		OutputStream os = null;
		try {
			
			int bufLen = 1024; // 1 Kbyte
			byte[] buf = new byte[1024]; // output buffer
			os = new FileOutputStream(filePath);

			BufferedInputStream in = new BufferedInputStream(is);
			int len = 0;
		    while((len = in.read(buf,0,bufLen)) != -1){
		    	os.write(buf,0,len);
		    }	
			
		} catch ( IOException e ) {
			String message = "Unable to write out file needed for export portfolio. File was "+filePath;	
			log.error(message,e);
			throw e;
			
		} finally {

			try {
				if ( is != null )
					is.close();
			} catch (IOException e1) {
				String message = "Unable to close input export portfolio file due to IOException";
				log.warn(message,e1);
			}

			try {
				if ( os != null )
					os.close();
			} catch (IOException e2) {
				String message = "Unable to close output export portfolio file due to IOException";
				log.warn(message,e2);
			}
		}
		

	}
}

 