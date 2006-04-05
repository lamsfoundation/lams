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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
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
import org.lamsfoundation.lams.util.WebUtil;

/**
 * @author mtruong
 *
 */
public class ExportDownloadServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(ExportDownloadServlet.class);
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
	  	    
	    String zipFilename = WebUtil.readStrParam(request, ExportPortfolioConstants.PARAM_FILE_LOCATION);
	    
	    /* Extract taken from org.lamsfoundation.lams.contentrepository.client.Download servlet */
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition","attachment;filename=" + ExportPortfolioConstants.ZIP_FILENAME);
		
		InputStream in = new BufferedInputStream(new FileInputStream(new File(constructAbsolutePath(zipFilename)))); 
		OutputStream out = response.getOutputStream();
		try {
			int count = 0;
				
			int ch;
			while ((ch = in.read()) != -1)
			{
				out.write((char) ch);
				count++;
			}
			log.debug("Wrote out " + count + " bytes");
			response.setContentLength(count);
			out.flush();
		} catch (IOException e) {
		    log.error( "Exception occured writing out file:" + e.getMessage());		
		    throw e;
		} finally {
		    try	{
				if (in != null) in.close(); // very important
				if (out != null) out.close();
			}
			catch (IOException e) {
			    log.error("Error Closing file. File already written out - no exception being thrown.",e);
			}
		}
		
	
	}
	
	private String constructAbsolutePath(String relativePath)
	{
	    return ExportPortfolioConstants.TEMP_DIRECTORY + File.separator + relativePath;
	}
	
}
