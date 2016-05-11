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


package org.lamsfoundation.lams.learning.export.web.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.export.ExportPortfolioConstants;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;

/**
 * @author mtruong
 *
 */
public class ExportDownloadServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(ExportDownloadServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	String zipFilename = WebUtil.readStrParam(request, ExportPortfolioConstants.PARAM_FILE_LOCATION);
//	    zipFilename = new String(zipFilename.getBytes(("ISO-8859-1")), "UTF-8");
	zipFilename = new String(zipFilename.getBytes(), "UTF-8");

	/* Extract taken from org.lamsfoundation.lams.contentrepository.client.Download servlet */
	response.setContentType("application/x-download");
	String filename = FileUtil.encodeFilenameForDownload(request, getFilename(zipFilename));
	response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");

	InputStream in = new BufferedInputStream(new FileInputStream(new File(constructAbsolutePath(zipFilename))));
	OutputStream out = response.getOutputStream();
	try {
	    int count = 0;

	    int ch;
	    while ((ch = in.read()) != -1) {
		out.write((char) ch);
		count++;
	    }
	    log.debug("Wrote out " + count + " bytes");
	    response.setContentLength(count);
	    out.flush();
	} catch (IOException e) {
	    // Exception occured writing out file - probably user cancelled on their end.	
	    log.info("Exception occured writing out file - probably just the user cancelling on their end."
		    + e.getMessage(), e);
	    throw e;
	} finally {
	    try {
		if (in != null) {
		    in.close(); // very important
		}
		if (out != null) {
		    out.close();
		}
	    } catch (IOException e) {
		//Error Closing file. File already written out - no exception being thrown.
	    }
	}

    }

    private String constructAbsolutePath(String relativePath) {
	return FileUtil.getTempDir() + File.separator + getDirname(relativePath) + File.separator
		+ ExportPortfolioConstants.EXPORT_TEMP_FILENAME;
    }

    private String getDirname(String path) {
	int index = path.indexOf(File.separator);
	return (index > 0 ? path.substring(0, index) : path);
    }

    private String getFilename(String path) {
	int index = path.lastIndexOf(File.separator);
	return (index > 0 ? path.substring(index + 1) : path);
    }

}
