/*
 * Created on Sep 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.servlet.ExportPortfolioServletException;

/**
 * @author mtruong
 *
 * TODO write a servlet mapping for this servlet. then test to see if the request parameters are also being
 * passed onto the export url
 */
public class NbExportServlet extends AbstractExportPortfolioServlet {
	
	private final String FILENAME = "nb_main.html";
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies) throws NbApplicationException
	{
		/**
		 * Implementation 1: using filters. Problem: cant generate more than one file
		 */
	/*	 try
		{
			String absoluteFilename = directoryName + File.separator + FILENAME;
			request.setAttribute(WebUtil.PARAM_FILENAME, absoluteFilename);
			
			RequestDispatcher requestDip = getServletContext().getRequestDispatcher("/exportPortfolio.do");
			requestDip.forward(request, response);
		}
		catch(ServletException e)
		{
			throw new NbApplicationException("An error has occurred while redirecting to export page. Reason: " + e.getMessage());
		}
		catch(IOException e)
		{
			throw new NbApplicationException("An error has occurred while redirecting to export page. Reason: " + e.getMessage());
		}
			*/
		
		/**
		 * Implementation 2: using HttpURLConnection, can connect to as many pages as the tool wants.
		 */
		
		String mode = WebUtil.readStrParam(request, WebUtil.PARAM_MODE);
		Long toolContentId, toolSessionId, userId;
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();

		String url = basePath + "/exportPortfolio.do";
		String urlWithParameters = null;
		
		if (mode.equals(ToolAccessMode.LEARNER.toString()))
		{
			//read in userId and toolSessionId
			urlWithParameters = appendParametersToLearnerExportURL(request, url);		
			
		}
		else if (mode.equals(ToolAccessMode.TEACHER.toString()))
		{
			//read in toolContentId
			urlWithParameters = appendParametersToTeacherExportURL(request, url);
		}
	
		try
		{
			// Can generate other files here by calling writeResponseToFile and supplying different urls 
			writeResponseToFile(urlWithParameters, directoryName, FILENAME, cookies);
		
		}
		catch(ExportPortfolioServletException e)
		{
			throw new NbApplicationException("An error has occurred while trying to generate files ",e);
		} 
		return FILENAME;
	}
	
	
}
