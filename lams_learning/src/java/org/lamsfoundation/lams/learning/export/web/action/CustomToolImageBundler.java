package org.lamsfoundation.lams.learning.export.web.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;

/**
 * This class originally was made for bundling gmap images for export portfolio
 * But now can be used for any tool that requires custom images
 *
 * @author lfoxton
 *
 */
public class CustomToolImageBundler extends Bundler {

    public CustomToolImageBundler() {
    }

    /**
     * This method bundles the files to the given output dir
     * 
     * @param request
     *            the request for the export
     * @param cookies
     *            cookies for the request
     * @param outputDirectory
     *            the location where the files should be written
     * @param toolImageUrlDir
     *            the url location of the images directory
     * @param fileNames
     *            an array of file-names (not paths) you wish to include in the bundle
     * @throws Exception
     */
    public void bundle(HttpServletRequest request, Cookie[] cookies, String outputDirectory, String toolImageUrlDir,
	    String[] fileNames) throws Exception {
	bundleViaHTTP(request, cookies, outputDirectory, toolImageUrlDir, fileNames);
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
    private void bundleViaHTTP(HttpServletRequest request, Cookie[] cookies, String outputDirectory,
	    String toolImageUrlDir, String[] fileNames)
	    throws MalformedURLException, FileNotFoundException, IOException {

	String imagesDirStr = outputDirectory + File.separator + "tool_images";
	File imagesDir = new File(imagesDirStr);
	imagesDir.mkdirs();

	for (String imageName : fileNames) {
	    String url = toolImageUrlDir + imageName;
	    HttpUrlConnectionUtil.writeResponseToFile(url, imagesDirStr, imageName, cookies); //cookies aren't really needed here.

	    log.debug("Copying image from source: " + url + "to desitnation: " + imagesDirStr);
	}
    }

}
