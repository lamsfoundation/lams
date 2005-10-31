/*
 * Created on Sep 22, 2005
 *
 */
package org.lamsfoundation.lams.tool.noticeboard.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.servlet.ExportPortfolioServletException;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author mtruong
 *
 */
public class NbExportServlet extends AbstractExportPortfolioServlet {
	
	private final String FILENAME = "nb_main.html";
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	{
	
		String mode = WebUtil.readStrParam(request, AttributeNames.PARAM_MODE);
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
