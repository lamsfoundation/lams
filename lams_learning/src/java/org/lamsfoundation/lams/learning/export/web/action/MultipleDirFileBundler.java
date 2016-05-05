package org.lamsfoundation.lams.learning.export.web.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;

/**
 * Added this bundler when files need to be accessed from different directories
 *
 * @author pgeorges
 *
 */

public class MultipleDirFileBundler extends Bundler {

    public MultipleDirFileBundler() {
    }

    /**
     * This method bundles the files to the given output dir
     * 
     * @param request
     *            the request for the export
     * @param cookies
     *            cookies for the request
     * @param srcDirs
     *            the locations of the files to copy
     * @param targetDirs
     *            the locations to store the files
     * @param fileNames
     *            an array of file-names (not paths) you wish to include in the bundle
     * @throws Exception
     */
    public void bundle(HttpServletRequest request, Cookie[] cookies, String[] srcDirs, String[] targetDirs,
	    ArrayList<String>[] filenames) throws Exception {
	bundleViaHTTP(request, cookies, srcDirs, targetDirs, filenames);
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
    private void bundleViaHTTP(HttpServletRequest request, Cookie[] cookies, String[] srcDirs, String[] targetDirs,
	    ArrayList<String>[] filenames) throws MalformedURLException, FileNotFoundException, IOException {
	// make the target directories
	for (int i = 0; i < targetDirs.length; i++) {
	    File fileDir = new File(targetDirs[i]);
	    fileDir.mkdirs();

	    ArrayList<String> filenameArray = filenames[i];

	    for (int j = 0; j < filenameArray.size(); j++) {
		String srcUrl = srcDirs[i] + filenameArray.get(j);
		String targetDir = targetDirs[i];
		HttpUrlConnectionUtil.writeResponseToFile(srcUrl, targetDir, filenameArray.get(j), cookies);
	    }
	}
    }
}
