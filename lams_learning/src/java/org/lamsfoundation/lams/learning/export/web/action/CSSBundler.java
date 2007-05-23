/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $Id$ */

package org.lamsfoundation.lams.learning.export.web.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.web.filter.LocaleFilter;

public class CSSBundler {

	private static Logger log = Logger.getLogger(CSSBundler.class);
	private static final String RTL_DIR = "rtl";	// right-to-left direction

	Map<String,File> filesToCopy = null;
	List<String> directoriesRequired = null;
	HttpServletRequest request  = null;
	Cookie[] cookies  = null;
	String outputDirectory  = null;
	String centralPath  = null;
	boolean rtl = false;
	Collection<CSSThemeVisualElement> themes = null;

	/**
 	 * @param centralPath the directory path to the lams-central.war. Assumes that it is an expanded war.
	 */
	public CSSBundler(HttpServletRequest request, Cookie[] cookies, String outputDirectory, Collection<CSSThemeVisualElement> themes) {
		filesToCopy = new HashMap<String,File>();
		directoriesRequired = new ArrayList<String>();
		this.request = request;
		this.cookies = cookies;
		this.outputDirectory = outputDirectory;
		this.themes = themes != null ? themes : new ArrayList<CSSThemeVisualElement>();
		
		this.centralPath = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR);
		if ( centralPath == null )  {
			log.error("Unable to get path to the LAMS ear from the configuration file - the exported portfolios will be missing parts of their css (style and images).");
		} else {
			centralPath = centralPath + File.separator + "lams-central.war";
		}
				
		String pageDirection = (String) request.getSession().getAttribute(LocaleFilter.DIRECTION);  // RTL or LTR (default)
		if(pageDirection != null)
			if(pageDirection.toLowerCase().equals(RTL_DIR))
				this.rtl = true;

	}
	
	/** Bundle the stylesheets.
	 * 
	 * @param outputDirectory directory for the export
	 * @param request
	 * @param cookies
	 * @throws IOException
	 */
	public void bundleStylesheet( ) throws IOException
	{
		File central = new File(centralPath);
		if ( centralPath == null || ! central.canRead() || ! central.isDirectory() ) {
			log.error("Unable to get the path for lams-central or unable to read it as a directory. Bundling stylesheets via http but not including images");
			bundleViaHTTP(request, cookies);
		} else {
			log.debug("Copying stylesheets and images from path "+centralPath);
			bundleViaCopy();
		}
		
	}
	
	/** Fallback code if it can't do normal file copies (because it doesn't know the path to lams-central.
	 * Only does the stylesheets, not the images 
	 * 
	 * @param outputDirectory
	 * @param request
	 * @param cookies
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws MalformedURLException 
	 */
	private void bundleViaHTTP(HttpServletRequest request, Cookie[] cookies) throws MalformedURLException, FileNotFoundException, IOException {
		
		String cssDirectory = outputDirectory+File.separator+"css";
		File cssDirectoryFile = new File(cssDirectory);
		cssDirectoryFile.mkdirs();
		
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();

		for ( CSSThemeVisualElement theme : themes) {
			String themeName = theme.getName();
			
			String url = (!rtl)? basePath + "/lams/css/" + themeName + ".css" : basePath + "/lams/css/" + themeName + "_" + RTL_DIR + ".css";
			HttpUrlConnectionUtil.writeResponseToFile(url, cssDirectory, (!rtl)? themeName + ".css" :  themeName + "_" + RTL_DIR + ".css", cookies); //cookies aren't really needed here.

			url = (!rtl)? basePath + "/lams/css/" + themeName + "_learner.css" : basePath + "/lams/css/" + themeName + "_" + RTL_DIR + "_learner.css" ;
			HttpUrlConnectionUtil.writeResponseToFile(url, cssDirectory, (!rtl)? themeName + "_learner.css" : themeName + "_" + RTL_DIR + "_learner.css", cookies); //cookies aren't really needed here.
		}
		
		// include the special IE stylesheet
		String url = basePath + "/lams/css/ie-styles.css";
		HttpUrlConnectionUtil.writeResponseToFile(url, cssDirectory, "ie-styles.css", cookies); //cookies aren't really needed here.
		
	}

	/** Preferred method - copies the files from lams-central.war
	 * 
	 * @param outputDirectory
	 * @throws IOException 
	 */
	private void bundleViaCopy() throws IOException {

		// build up a list of themes to copy
		setupThemeList();

		// build up a list of images to copy
		setupImageList();
		
		// now copy all those files
		createDirectories();
		for ( Map.Entry fileEntry : filesToCopy.entrySet() ) {
			copyFile((String)fileEntry.getKey(), (File)fileEntry.getValue());
		}
	}

	/**
	 * 
	 */
	private void setupThemeList() {
		// build up a list of themes to copy
		String cssDirectory = outputDirectory+File.separatorChar+"css";
		directoriesRequired.add(cssDirectory);
		
		for ( CSSThemeVisualElement theme : themes) {

			String themeName = theme.getName();
			
			String themeFilename = (!rtl) ? themeName + ".css" : themeName + "_" + RTL_DIR + ".css";
			addThemeFile(cssDirectory, themeFilename);

			themeFilename = (!rtl) ? themeName + "_learner.css" : themeName + "_" + RTL_DIR + "_learner.css";
			addThemeFile(cssDirectory, themeFilename);
		}
		
		// include the special IE stylesheet
		addThemeFile(cssDirectory, "ie-styles.css");
	}

	private void addThemeFile(String cssDirectory, String themeFilename) {
		File themeFile = new File(centralPath + File.separatorChar + "css" + File.separatorChar + themeFilename);
		if ( ! themeFile.canRead() ) {
			log.error("Unable to read theme file "+themeFile.getAbsolutePath());
		} else {
			filesToCopy.put(cssDirectory+File.separatorChar+themeFilename,themeFile);

		}
	}

	private void setupImageList() {

		for ( CSSThemeVisualElement theme : themes) {

			String dirName = theme.getImageDirectory();
			if ( dirName != null ) {				
				
				String imageDirectory = centralPath+File.separatorChar+"images"+File.separatorChar+dirName;
				String outputImageDirectory = outputDirectory+File.separatorChar+"images"+File.separatorChar+dirName;
				
				directoriesRequired.add(outputImageDirectory);
		
				File dir = new File(imageDirectory);
				if ( ! dir.canRead() || ! dir.isDirectory() ) {
					log.error("Unable to read css image directory "+dir.getAbsolutePath());
					
				}  else {
		
					File[] files = dir.listFiles();
					for ( File imageFile: files ) {
						filesToCopy.put(outputImageDirectory+File.separatorChar+imageFile.getName(),imageFile);
					}
				}
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
