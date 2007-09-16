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

/* $Id$ */
package org.lamsfoundation.lams.authoring.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * 
 * @struts.action path = "/authoring/exportToolContent"
 * 				  validate = "false"
 * @struts.action-forward name = "choice" path = "/toolcontent/exportchoice.jsp"
 * @struts.action-forward name = "loading" path = "/toolcontent/exportloading.jsp"
 * @struts.action-forward name = "result" path = "/toolcontent/exportresult.jsp"
 *  
 * Export tool content action. It needs learingDesignID as input parameter.
 * @author Steve.Ni
 * @version $Revision$
 */
public class ExportToolContentAction extends LamsAction {

	private static final long serialVersionUID = 1L;
	public static final String EXPORT_TOOLCONTENT_SERVICE_BEAN_NAME = "exportToolContentService";
	public static final String PARAM_LEARING_DESIGN_ID = "learningDesignID";
	public static final String ATTR_TOOLS_ERROR_MESSAGE = "toolsErrorMessages";
	public static final String ATTR_LD_ERROR_MESSAGE = "ldErrorMessages";
	private static final String PARAM_EXPORT_FORMAT = "format";
	private static final String IMS_XSLT_NAME = "learning-design-ims.xslt";
	private static final String IMS_XSLT_PATH = "/toolcontent";


	private Logger log = Logger.getLogger(ExportToolContentAction.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String param = request.getParameter("method");
		//-----------------------Resource Author function ---------------------------
		if(StringUtils.equals(param,"loading")){
			Long learningDesignId = WebUtil.readLongParam(request,PARAM_LEARING_DESIGN_ID);
			request.setAttribute(PARAM_LEARING_DESIGN_ID,learningDesignId);
			int format = WebUtil.readIntParam(request,PARAM_EXPORT_FORMAT);
			request.setAttribute(PARAM_EXPORT_FORMAT,format);
			//display initial page for automatically loading download pgm
			return mapping.findForward("loading");
		}else if(StringUtils.equals(param,"export")){
			//the export LD pgm
			return exportLD(mapping,request,response);
		}else{ //choice format
			Long learningDesignId = WebUtil.readLongParam(request,PARAM_LEARING_DESIGN_ID);
			request.setAttribute(PARAM_LEARING_DESIGN_ID,learningDesignId);
			//display choose IMS or LAMS format page
			return mapping.findForward("choice");
		}
	}
	private ActionForward exportLD(ActionMapping mapping, HttpServletRequest request,HttpServletResponse response){
		Long learningDesignId = WebUtil.readLongParam(request,PARAM_LEARING_DESIGN_ID);
		int format = WebUtil.readIntParam(request,PARAM_EXPORT_FORMAT);
		IExportToolContentService service = getExportService();
		List<String> ldErrorMsgs = new ArrayList<String>();
		List<String> toolsErrorMsgs = new ArrayList<String>();
		
		try {
			File xslt = new File(this.getServlet().getServletContext().getRealPath(IMS_XSLT_PATH)+File.separator+IMS_XSLT_NAME);
			String zipFilename = service.exportLearningDesign(learningDesignId,toolsErrorMsgs,format,xslt);
			
			// get only filename
			String zipfile = FileUtil.getFileName(zipFilename);
			
			//write zip file as response stream. 

			// Different browsers handle stream downloads differently LDEV-1243
			String agent = request.getHeader("USER-AGENT");
			
			log.debug("Browser is:" + agent);
			
			String filename = null;
			
			if (null != agent && -1 != agent.indexOf("MSIE"))
			{
				// if MSIE then urlencode it
				filename = URLEncoder.encode(zipfile, "UTF-8");

			}
			else if (null != agent && -1 != agent.indexOf("Mozilla"))
			{
				// if Mozilla then base64 url_safe encoding
				filename = MimeUtility.encodeText(zipfile, "UTF-8", "B");
				
			}
			else 
			{
				// any others use same filename. 
				filename = zipfile;
				
			}
			log.debug("Final filename to export: " + filename);
			
			response.setContentType("application/x-download");
//			response.setContentType("application/zip");
			response.setHeader("Content-Disposition","attachment;filename="+filename);
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(zipFilename)); 
				out = response.getOutputStream();
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
			} catch (Exception e) {
			    log.error( "Exception occured writing out file:" + e.getMessage());		
			    throw new ExportToolContentException(e);
			} finally {
			    try	{
					if (in != null) in.close(); // very important
					if (out != null) out.close();
				}
				catch (Exception e) {
				    log.error("Error Closing file. File already written out - no exception being thrown.",e);
				}
			}
			
			return null;
		} catch (Exception e1) {
			log.error("Unable to export tool content: " + e1.toString());
			ldErrorMsgs.add(0,e1.getClass().getName());
			request.setAttribute(ATTR_LD_ERROR_MESSAGE,ldErrorMsgs);
			request.setAttribute(ATTR_TOOLS_ERROR_MESSAGE,toolsErrorMsgs);
		}
		//display initial page for upload
		return mapping.findForward("result");
	}
	
	//***************************************************************************************
	// Private method
	//***************************************************************************************
	private IExportToolContentService getExportService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
		return (IExportToolContentService) webContext.getBean(EXPORT_TOOLCONTENT_SERVICE_BEAN_NAME);		
	}
}
