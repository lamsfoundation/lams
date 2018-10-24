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


package org.lamsfoundation.lams.webservice;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.HttpUrlConnectionUtil;
import org.lamsfoundation.lams.util.WebUtil;

/**
 * @author pgeorges
 */
public class GetRecordingServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(GetRecordingServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {

	try {
	    String urlStr = WebUtil.readStrParam(request, "urlStr");
	    String filename = WebUtil.readStrParam(request, "filename");
	    String dir = WebUtil.readStrParam(request, "dir");

	    String absoluteFilePath = dir + filename;

	    File newPath = new File(dir);
	    newPath.mkdirs();

	    File newFile = new File(absoluteFilePath);
	    newFile.createNewFile();

	    // get the stream from the external server
	    int success = HttpUrlConnectionUtil.writeResponseToFile(urlStr, dir, filename, new Cookie[0]);

	    // if success
	    if (success == 1) {
		// add the filename to the list
		logger.debug("file copy complete");
	    } else {
		logger.debug("file copy failed");
	    }

	} catch (Exception e) {
	    logger.error(e.getMessage());
	}
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	doPost(request, response);
    }

}
