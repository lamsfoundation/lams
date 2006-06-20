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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * @struts.action name = "ExportAction"
 * 				  parameter = "method"
 * 				  validate = "false"
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

	private Logger log = Logger.getLogger(ExportToolContentAction.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String param = mapping.getParameter();
		//-----------------------Resource Author function ---------------------------
		if(param.equals("export")){
			return exportLD(mapping,request,response);
		}else{
			Long learningDesignId = WebUtil.readLongParam(request,PARAM_LEARING_DESIGN_ID);
			request.setAttribute(PARAM_LEARING_DESIGN_ID,learningDesignId);
			//display initial page for upload
			return mapping.findForward("loading");
		}
	}
	private ActionForward exportLD(ActionMapping mapping, HttpServletRequest request,HttpServletResponse response){
		Long learningDesignId = WebUtil.readLongParam(request,PARAM_LEARING_DESIGN_ID);
		IExportToolContentService service = getExportService();
		List<String> ldErrorMsgs = new ArrayList<String>();
		try {
			List<String> toolsErrorMsgs = new ArrayList<String>();
			String zipFilename = service.exportLearningDesign(learningDesignId,toolsErrorMsgs);
			request.setAttribute(ATTR_TOOLS_ERROR_MESSAGE,toolsErrorMsgs);
			//write zip file as response stream. 
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition","attachment;filename="+FileUtil.getFileName(zipFilename));
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
				return null;
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
		} catch (ExportToolContentException e1) {
			log.error("Unable to export tool content: " + e1.toString());
			request.setAttribute(ATTR_LD_ERROR_MESSAGE,ldErrorMsgs);
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
