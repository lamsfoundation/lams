package org.lamsfoundation.lams.learning.export.web.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.learning.export.web.action.Bundler;

/**
 * Added this bundler when files need to be accessed from different directories
 * 
 * @author pgeorges
 *
 */

public class MultipleDirFileBundler extends Bundler {
	
	public MultipleDirFileBundler() {}
	
	/**
	 * This method bundles the files to the given output dir
	 * 
	 * @param request the request for the export
	 * @param cookies cookies for the request
	 * @param outputDirectory the location where the files should be written
	 * @param toolImageUrlDir the url location of the images directory
	 * @param fileNames an array of file-names (not paths) you wish to include in the bundle
	 * @throws Exception
	 */
	public void bundle(HttpServletRequest request, Cookie[] cookies, String outputDirectory, String[] fileUrlDir, ArrayList<String>[] filenames) throws Exception
	{
		bundleViaHTTP(request, cookies, outputDirectory, fileUrlDir, filenames);
	}
	
	/**
	 * See bundle
	 * 
	 * @param request
	 * @param cookies
	 * @param outputDirectory
	 * @param toolImageUrlDir
	 * @param fileNames
	 * @throws MalformedURLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void bundleViaHTTP(HttpServletRequest request, Cookie[] cookies, String outputDirectory, String[] fileUrlDir, ArrayList<String>[] filenames) 
	throws MalformedURLException, FileNotFoundException, IOException 
	{
		
		String fileDirStr = outputDirectory+File.separator+"files";
		File fileDir = new File(fileDirStr);
		fileDir.mkdirs();
				
		for(int i = 0; i < filenames.length; i++){
			ArrayList<String> filenameArray = filenames[i];
			
			for(int j = 0; j < filenameArray.size(); j++){
				String url = fileUrlDir[i] + filenameArray.get(j);
				HttpUrlConnectionUtil.writeResponseToFile(url, fileDirStr, filenameArray.get(j), cookies);
			}
		}
	}
}
