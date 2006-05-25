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

/* $Id$ */
package org.lamsfoundation.lams.authoring.web;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * Export tool content servlet. It needs learingDesignID as input parameter.
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ExportToolContentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private Logger log = Logger.getLogger(ExportToolContentServlet.class);
	
	/*
	 * @see javax.servlet.http.HttpServlet.service(HttpServletRequest, HttpServletResponse)
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		Long learningDesignId = WebUtil.readLongParam(request,AuthoringConstants.PARAM_LEARING_DESIGN_ID);
		IExportToolContentService service = getExportService();
		try {
			String zipFilename = service.exportToolContent(learningDesignId);
			
			//write zip file as response stream. 
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition","attachment;");
			InputStream in = new BufferedInputStream(new FileInputStream(zipFilename)); 
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
				catch (Exception e) {
				    log.error("Error Closing file. File already written out - no exception being thrown.",e);
				}
			}
		} catch (ExportToolContentException e1) {
			log.error("Unable to export tool content: " + e1.toString());
		} 
	}
	
	//***************************************************************************************
	// Private method
	//***************************************************************************************
	private IExportToolContentService getExportService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		return (IExportToolContentService) webContext.getBean(AuthoringConstants.EXPORT_TOOLCONTENT_SERVICE_BEAN_NAME);		
	}
}
