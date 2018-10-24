package org.lamsfoundation.lams.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * A servlet that loads at startup to do some maintainence like removing
 * temp directories
 *
 * @author lfoxton
 */
public class LamsStartupServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(LamsStartupServlet.class);
    private static final long serialVersionUID = 8010145709788505351L;

    /**
     * Initialize the servlet.<br>
     * Retrieve from the servlet configuration the "baseDir" which is the root
     * of the file repository:<br>
     * If not specified the value of "/UserFiles/" will be used.
     *
     */
    @Override
    public void init() throws ServletException {

	// Removing all the files in the temp directory
	String tempDirStr = Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR);
	File tempDir = new File(tempDirStr);

	if (tempDir != null) {
	    // create temp directory if it doesn't exist
	    if (!tempDir.exists()) {
		tempDir.mkdirs();
	    }
	    if (tempDir.canWrite()) {
		File[] files = tempDir.listFiles();
		log.info("Deleting temporary files from: " + tempDir);
		for (File file : files) {

		    if (file.isDirectory()) {
			// Recursively delete each directory
			log.debug("Deleting temporary file directory: " + file);
			if (!deleteDir(file)) {
			    log.error("Failed to delete " + file);
			}
		    } else {
			// Delete each file
			log.debug("Deleting temporary file: " + file);
			if (!file.delete()) {
			    log.error("Failed to delete " + file);
			}
		    }

		}
		return;
	    }
	}

	log.error("Cannot delete temporary files, do not have permission for folder: " + tempDirStr);

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
     * Deletes all files and subdirectories under dir. Returns true if all
     * deletions were successful. If a deletion fails, the method stops
     * attempting to delete and returns false.
     */
    private boolean deleteDir(File dir) {
	if (dir.isDirectory()) {
	    String[] children = dir.list();
	    for (int i = 0; i < children.length; i++) {
		boolean success = deleteDir(new File(dir, children[i]));
		if (!success) {
		    return false;
		}
	    }
	}

	// The directory is now empty so delete it
	return dir.delete();
    }

}
